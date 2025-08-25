package com.mandamong.server.infrastructure.minio.service

import com.mandamong.server.common.error.exception.BadRequestException
import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import io.minio.http.Method
import java.util.UUID
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class MinioService(
    @Value("\${minio.bucketName}") private val bucketName: String,
    private val minioClient: MinioClient,
) {

    fun upload(userId: Long, image: MultipartFile): String {
        val uuid = UUID.randomUUID()
        val extension = image.originalFilename?.substringAfterLast('.', "")
        if (extension.isNullOrEmpty()) {
            throw BadRequestException()
        }
        val objectKey = "$PROFILE_PREFIX/$userId/image-$uuid.$extension"
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

    fun deleteObject(objectKey: String) {
        if (objectKey == "$PROFILE_PREFIX$DEFAULT") {
            return
        }

        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(bucketName)
                .`object`(objectKey)
                .build()
        )
    }

    companion object {
        private const val PROFILE_PREFIX = "user/profile"
        private const val DEFAULT = "/default/default.png"
    }

}
