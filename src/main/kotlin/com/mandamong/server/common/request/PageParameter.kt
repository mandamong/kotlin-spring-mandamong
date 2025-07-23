package com.mandamong.server.common.request

data class PageParameter(
    val number: Int = START_PAGE,
    val size: Int = DEFAULT_SIZE,
) {

    companion object {
        const val START_PAGE = 1
        const val DEFAULT_SIZE = 5
    }

}
