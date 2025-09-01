package com.mandamong.server.common.dto

import org.springframework.data.domain.Page

open class PaginationResponse<T>(
    val totalPage: Int,
    val hasNext: Boolean,
    val content: List<T>,
) {
    companion object {
        fun <T> of(result: Page<T>): PaginationResponse<T> =
            PaginationResponse(
                totalPage = result.totalPages,
                hasNext = result.hasNext(),
                content = result.content,
            )
    }
}
