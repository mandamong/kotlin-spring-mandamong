package com.mandamong.server.infrastructure.minio

import com.mandamong.server.common.error.exception.NotFoundException
import io.minio.GetPresignedObjectUrlArgs
import io.minio.ListObjectsArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.http.Method
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class MinioService(
    @Value("\${minio.bucketName}") private val bucketName: String,
    private val minioClient: MinioClient,
) {

    fun upload(image: MultipartFile, nickname: String): String {
        val fileName = nickname + "/" + image.originalFilename

        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .`object`(fileName)
                .stream(image.inputStream, image.size, -1)
                .contentType(image.contentType)
                .build()
        )

        val preSignedUrl: String = minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                .`object`(fileName)
                .method(Method.GET)
                .build()
        )

        return preSignedUrl
    }

    fun getPresignedUrlByNickname(nickname: String): String {
        val objects = minioClient.listObjects(
            ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix("$nickname/")
                .build()
        )
        val file = objects.iterator().asSequence().firstOrNull()?.get()?.objectName() ?: throw NotFoundException()
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                .`object`(file)
                .method(Method.GET)
                .build()
        )
    }

}
