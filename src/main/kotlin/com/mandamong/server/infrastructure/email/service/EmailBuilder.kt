package com.mandamong.server.infrastructure.email.service

import java.security.SecureRandom
import org.springframework.stereotype.Component

@Component
class EmailBuilder {

    fun createCode(): String {
        val random: SecureRandom = SecureRandom.getInstanceStrong()
        return buildString(CODE_LENGTH) { repeat(CODE_LENGTH) { append(random.nextInt(RANDOM_RANGE)) } }
    }

    fun createEmail(code: String): String {
        return """
            <div style="font-family: 'Apple SD Gothic Neo', Arial, sans-serif; background-color: #f5f7fa; padding: 60px 20px;">
                <div style="max-width: 480px; margin: 0 auto; margin-top: 40px; background: white; border-radius: 10px; box-shadow: 0 6px 16px rgba(0,0,0,0.08); padding: 40px 30px; text-align: center;">
                    <h2 style="color: #2c3e50; margin-bottom: 24px;">이메일 인증 번호</h2>
                    <p style="font-size: 16px; color: #555;">아래 인증번호를 입력해주세요.</p>
                    <div style="font-size: 36px; font-weight: bold; letter-spacing: 8px; color: #ffb100; margin: 28px 0;">
                        $code
                    </div>
                    <p style="font-size: 14px; color: #888;">이 인증번호는 5분간 유효합니다.</p>
                </div>
                <div style="text-align: center; font-size: 12px; color: #aaa; margin-top: 40px;">
                    만다몽 팀 드림
                </div>
            </div>
        """.trimIndent()
    }

    companion object {
        private const val CODE_LENGTH = 6
        private const val RANDOM_RANGE = 10
    }

}
