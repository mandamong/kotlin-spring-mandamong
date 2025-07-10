package com.mandamong.api.auth.api

object ApiPath {

    object Auth {
        const val BASIC_REGISTER = "/api/auth/basic/register"
        const val BASIC_LOGIN = "/api/auth/basic/login"
        const val REFRESH = "/api/auth/token/refresh"
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
        const val DELETE = "/api/mandalart/{mandalartId}"
    }

    object Subject {
        const val CREATE = "/api/mandalart/subject"
        const val UPDATE = "/api/mandalart/subject/{subjectId}"
        const val SUGGEST = "/api/gemini/subject"
    }

    object Objective {
        const val CREATE = "/api/mandalart/objective"
        const val UPDATE = "/api/mandalart/objective/objectiveId"
        const val SUGGEST = "/api/gemini/objective"
    }
}
