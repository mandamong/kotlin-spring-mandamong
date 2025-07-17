package com.mandamong.server.infrastructure.gemini

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.genai.Client
import com.google.genai.types.GenerateContentConfig
import com.google.genai.types.Schema
import com.mandamong.server.mandalart.dto.SuggestByObjectiveResponse
import com.mandamong.server.mandalart.dto.SuggestBySubjectResponse
import org.springframework.stereotype.Service

@Service
class GeminiService(
    private val client: Client,
    private val jacksonObjectMapper: ObjectMapper,
) {

    fun generateBySubject(prompt: String): SuggestBySubjectResponse {
        val schema = Schema.fromJson(SUBJECT_SCHEMA)
        val config = GenerateContentConfig.builder()
            .responseSchema(schema)
            .build()
        val response = client.models.generateContent(
            MODEL,
            prompt + SUBJECT_SUGGEST,
            config,
        )
        val json = response.text() ?: "response error"
        return jacksonObjectMapper.readValue(json)
    }

    fun generateByObjective(prompt: String): SuggestByObjectiveResponse {
        val schema = Schema.fromJson(OBJECTIVE_SCHEMA)
        val config = GenerateContentConfig.builder()
            .responseSchema(schema)
            .build()
        val response = client.models.generateContent(
            MODEL,
            prompt + OBJECTIVE_SUGGEST,
            config,
        )
        val json = response.text() ?: "response error"
        return jacksonObjectMapper.readValue(json)
    }

    companion object {
        private const val SUBJECT_SCHEMA = """
            {
                "type": "object",
                "properties": {
                    "objectives": {
                        "type": "array",
                        "items": { "type": "string" },
                        "minItems": 4,
                        "maxItems": 4
                    },
                    "actions": {
                        "type": "array",
                        "items": {
                            "type": "array",
                            "items": { "type": "string" },
                            "minItems": 5,
                            "maxItems": 5
                        },
                    "minItems": 4,
                    "maxItems": 4
                    }
                },
                "required": ["objectives", "actions"]
            }
        """

        private const val OBJECTIVE_SCHEMA = """
            {
                "type": "object",
                "properties": {
                    "actions": {
                        "type": "array",
                        "items": { "type": "string" },
                        "minItems": 5,
                        "maxItems": 5
                    }
                },
              "required": ["actions"]
            }
        """

        private const val SUBJECT_SUGGEST = """
            너는 JSON API 서버야.
            주제(subject)가 입력되면, 주제(subject)를 이루기 위한 목표(objectives)와 행동(actions)에 대한 정보를 JSON 형식으로만 응답해줘.  
            중요: 응답은 반드시 순수 JSON이어야 하며, 코드 블록 표시(예: ```json 또는 ```) 없이 아래 형식 그대로 반환해.
            objectives 는 4개 추천해주고, actions 는 각 objective 를 달성하기 위해 수행하기 적절한 action 으로 5개씩 추천해줘
            
            {
              "objectives": ["사회성 기르기", "매너 갖추기", "배려하기", "개성 살리기"],
              "actions": [
                ["대화 연습하기", "친구들과 모임 참석하기", "낯선 사람에게 인사하기", "공감 능력 키우기", "의견 교류하기"],
                ["정중한 언어 사용하기", "식사 예절 지키기", "감사 인사하기", "전화 예절 익히기", "공공장소 매너 배우기"],
                ["타인의 입장 고려하기", "도움 요청 시 돕기", "작은 호의 베풀기", "칭찬 아끼지 않기", "감정 이해 노력하기"],
                ["자신만의 스타일 찾기", "창의적 취미 시작하기", "자신감 있게 표현하기", "트렌드 공부하기", "새로운 시도 도전하기"]
              ]
            }
        """

        private const val OBJECTIVE_SUGGEST = """
            너는 JSON API 서버야.
            목표(objectives)가 입력되면, 목표(objective)를 이루기 위한 행동(actions)에 대한 정보를 아래 JSON 형식으로만 응답해줘.
            중요: 응답은 반드시 순수 JSON이어야 하며, 코드 블록 표시(예: ```json 또는 ```) 없이 아래 형식 그대로 반환해.
            actions 는 5개 추천해주고, actions 는 입력된 objective 를 달성하기 위해 수행하기 적절한 action 으로 5개 추천해줘
            
            {
              "actions": ["대화 연습하기", "친구들과 모임 참석하기", "낯선 사람에게 인사하기", "공감 능력 키우기", "의견 교류하기"]
            }
        """

        private const val MODEL = "gemini-2.5-flash-lite-preview-06-17"
    }

}
