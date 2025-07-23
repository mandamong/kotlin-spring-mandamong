package com.mandamong.server.common.constants

object ApiPath {

    object User {
        const val CREATE = "/api/auth/basic"
        const val UPDATE_NICKNAME = "/api/user/nickname"
        const val VALIDATE_PASSWORD = "/api/user/password"
        const val UPDATE_PASSWORD = "/api/user/password"
        const val PASSWORD_INITIALIZE = "/api/user/password"
        const val DELETE = "/api/auth/basic"
    }

    object Auth {
        const val LOGIN = "/api/auth/basic/login"
        const val REFRESH = "/api/auth/token/refresh"
        const val LOGOUT = "/api/auth/logout"
    }

    object Duplication {
        const val CHECK_NICKNAME = "/api/auth/duplication/nickname"
        const val CHECK_EMAIL = "/api/auth/duplication/email"
    }

    object Email {
        const val SEND = "/api/auth/email/verification"
        const val VERIFY = "/api/auth/email/verification"
    }

    object Mandalart {
        const val CREATE = "/api/mandalart"
        const val MANDALARTS = "/api/mandalart"
        const val MANDALART = "/api/mandalart/{mandalartId}"
        const val UPDATE_NAME = "/api/mandalart/name/{mandalartId}"
        const val DELETE = "/api/mandalart/{mandalartId}"
    }

    object Subject {
        const val UPDATE = "/api/mandalart/subject/{subjectId}"
        const val SUGGEST = "/api/gemini/subject"
    }

    object Objective {
        const val UPDATE = "/api/mandalart/objective/{objectiveId}"
        const val SUGGEST = "/api/gemini/objective"
    }

    object Action {
        const val UPDATE = "/api/mandalart/action/{actionId}"
    }

}
