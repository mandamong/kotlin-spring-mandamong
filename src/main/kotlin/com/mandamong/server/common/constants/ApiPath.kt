package com.mandamong.server.common.constants

object ApiPath {

    object User {
        const val CREATE = "/api/auth/basic"
        const val UPDATE = "/api/user"
        const val DELETE = "/api/auth/basic"
        const val VALIDATE_PASSWORD = "/api/user/password"
        const val INITIALIZE_PASSWORD = "/api/user/password/initialize"
    }

    object Duplication {
        const val NICKNAME = "/api/auth/duplication/nickname"
        const val EMAIL = "/api/auth/duplication/email"
    }

    object Email {
        const val SEND = "/api/auth/email/verification"
        const val VERIFY = "/api/auth/email/verification"
    }

    object Auth {
        const val LOGIN = "/api/auth/basic/login"
        const val REFRESH = "/api/auth/token/refresh"
        const val LOGOUT = "/api/auth/logout"
    }

    object Mandalart {
        const val CREATE = "/api/mandalart"
        const val UPDATE = "/api/mandalart/name/{mandalartId}"
        const val DELETE = "/api/mandalart/{mandalartId}"
        const val MANDALARTS = "/api/mandalart"
        const val MANDALART = "/api/mandalart/{mandalartId}"
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
