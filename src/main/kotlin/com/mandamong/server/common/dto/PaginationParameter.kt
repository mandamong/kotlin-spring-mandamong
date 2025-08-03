package com.mandamong.server.common.dto

data class PaginationParameter(
    val number: Int = START_PAGE,
    val size: Int = DEFAULT_SIZE,
) {

    companion object {
        private const val START_PAGE = 1
        private const val DEFAULT_SIZE = 5
    }

}
