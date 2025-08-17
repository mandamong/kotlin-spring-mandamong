package com.mandamong.server.infrastructure.minio

import io.minio.GetPresignedObjectUrlArgs
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

    fun upload(id: Long, image: MultipartFile): String {
        val objectKey = "$PROFILE_PREFIX/$id/${image.originalFilename}"
        putObject(objectKey, image)
        return objectKey
    }

    fun putObject(objectKey: String, image: MultipartFile) {
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .`object`(objectKey)
                .stream(image.inputStream, image.size, -1)
                .contentType(image.contentType)
                .build()
        )
    }

    fun getPresignedUrlByObjectKey(objectKey: String): String {
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                .`object`(objectKey)
                .method(Method.GET)
                .build()
        )
    }

    companion object {
        private const val PROFILE_PREFIX = "/user/profile"
    }

}
