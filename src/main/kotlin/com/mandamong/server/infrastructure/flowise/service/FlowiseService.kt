package com.mandamong.server.infrastructure.flowise.service

import com.mandamong.server.common.error.exception.base.BusinessBaseException
import com.mandamong.server.mandalart.dto.FlowiseResponse
import com.mandamong.server.mandalart.dto.SuggestByObjectiveRequest
import com.mandamong.server.mandalart.dto.SuggestByObjectiveResponse
import com.mandamong.server.mandalart.dto.SuggestBySubjectRequest
import com.mandamong.server.mandalart.dto.SuggestBySubjectResponse
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class FlowiseService(
    private val subjectClient: WebClient,
    private val objectiveClient: WebClient,
) {
    /**
         * 주제 기반 제안 요청을 Flowise 주제 서비스로 전송하고 파싱된 응답을 반환한다.
         *
         * @param request 제안 생성을 위한 입력 데이터
         * @return Flowise에서 파싱된 제안 응답
         */
        fun suggestBySubject(request: SuggestBySubjectRequest): SuggestBySubjectResponse =
        sendRequest(subjectClient, request)

    /**
         * 주어진 목표(Objective)에 대해 Flowise에서 제안(Suggestion)을 요청하고 반환합니다.
         *
         * 요청 페이로드를 Flowise 목표 전용 엔드포인트로 전송하여 파싱된 SuggestByObjectiveResponse를 반환합니다.
         *
         * @param request 요청할 목표 정보와 옵션을 포함한 DTO
         * @return Flowise에서 반환된 SuggestByObjectiveResponse
         * @throws BusinessBaseException Flowise 응답의 `json` 페이로드가 비어있을 경우 발생합니다.
         */
        fun suggestByObjective(request: SuggestByObjectiveRequest): SuggestByObjectiveResponse =
        sendRequest(objectiveClient, request)

    /**
             * 지정한 WebClient로 POST 요청을 보내고 응답의 `json` 페이로드를 T 타입으로 반환합니다.
             *
             * 상세:
             * - 요청 본문으로 `request`를 전송하고 응답은 `FlowiseResponse<T>`로 역직렬화됩니다.
             * - 호출은 동기적으로 차단(block)되어 결과를 기다립니다.
             * - 응답의 `json` 필드를 그대로 반환합니다.
             *
             * @param request 전송할 요청 본문(요청 타입은 호출자에 의해 결정됩니다).
             * @return 응답의 `json` 필드에 해당하는 T 타입의 값.
             * @throws BusinessBaseException 응답의 `json` 필드가 null인 경우 발생합니다.
             */
            private inline fun <reified T> sendRequest(
        webClient: WebClient,
        request: Any,
    ): T =
        webClient
            .post()
            .bodyValue(request)
            .retrieve()
            .bodyToMono(typeReference<T>())
            .block()
            ?.json
            ?: throw BusinessBaseException()

    private inline fun <reified T> typeReference() = object : ParameterizedTypeReference<FlowiseResponse<T>>() {}
}
