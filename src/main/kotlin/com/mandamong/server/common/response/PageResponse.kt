package com.mandamong.server.common.response

import org.springframework.data.domain.Page

data class PageResponse<T>(
    val totalPage: Int,
    val hasNext: Boolean,
    val content: List<T>,
) {

    companion object {
        fun <T> of(result: Page<T>): PageResponse<T> {
            return PageResponse(
                totalPage = result.totalPages,
                hasNext = result.hasNext(),
                content = result.content,
            )
        }
    }

}
