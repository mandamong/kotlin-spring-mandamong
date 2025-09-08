package com.mandamong.server.mandalart.dto

import com.mandamong.server.mandalart.entity.Action
import com.mandamong.server.mandalart.enums.Status

data class UpdateActionRequest(
    val action: String?,
    val status: Status?,
) {
    companion object {
        /**
             * Action 엔티티에서 필드를 복사하여 UpdateActionRequest 인스턴스를 생성합니다.
             *
             * 주로 클라이언트로 전송할 업데이트용 DTO를 빠르게 만들 때 사용합니다.
             *
             * @param action 복사할 원본 Action 객체 (action과 status 필드를 복사함)
             * @return 원본 Action의 `action`과 `status` 값을 가진 UpdateActionRequest
             */
            fun of(action: Action): UpdateActionRequest =
            UpdateActionRequest(action = action.action, status = action.status)
    }
}
