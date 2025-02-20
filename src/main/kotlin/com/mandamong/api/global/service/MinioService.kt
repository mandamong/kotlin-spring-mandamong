package com.mandamong.api.global.service

import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.http.Method
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class MinioService(
    private val minioClient: MinioClient,

    @Value("\${minio.bucketName}")
    private val buckName: String,
) {

    fun upload(picture: MultipartFile, nickname: String): String {
        val fileName = nickname + "/" + picture.originalFilename

        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(buckName)
                .`object`(fileName)
                .stream(picture.inputStream, picture.size, -1)
                .contentType(picture.contentType)
                .build()
        )

        val preSignedUrl: String = minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .bucket(buckName)
                .`object`(fileName)
                .method(Method.GET)
                .build()
        )

        return preSignedUrl
    }
}
