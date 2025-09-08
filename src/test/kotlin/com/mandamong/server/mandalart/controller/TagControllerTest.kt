package com.mandamong.server.mandalart.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.mandamong.server.mandalart.dto.AddTagToMandalartRequest
import com.mandamong.server.mandalart.dto.CreateTagRequest
import com.mandamong.server.mandalart.entity.Mandalart
import com.mandamong.server.mandalart.entity.Tag
import com.mandamong.server.mandalart.repository.MandalartRepository
import com.mandamong.server.mandalart.repository.MandalartTagRepository
import com.mandamong.server.mandalart.repository.TagRepository
import com.mandamong.server.user.entity.User
import com.mandamong.server.user.model.Email
import com.mandamong.server.user.repository.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import com.mandamong.server.config.TestConfig
import org.springframework.http.MediaType
import com.mandamong.server.user.dto.LoginUser
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import jakarta.persistence.EntityManager
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig::class)
@ActiveProfiles("test") 
@Transactional
class TagControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var tagRepository: TagRepository

    @Autowired
    private lateinit var mandalartRepository: MandalartRepository

    @Autowired
    private lateinit var mandalartTagRepository: MandalartTagRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    private lateinit var testUser: User
    private lateinit var testMandalart: Mandalart

    @BeforeEach
    fun setUp() {
        // 테스트 사용자 생성
        testUser = User(
            email = Email.from("test@example.com"),
            nickname = "testUser",
            password = "encodedPassword",
            language = "ko"
        )
        userRepository.save(testUser)

        // 테스트 만다라트 생성
        testMandalart = Mandalart(
            name = "테스트 만다라트",
            user = testUser
        )
        mandalartRepository.save(testMandalart)
    }

    @AfterEach
    fun cleanUp() {
        SecurityContextHolder.clearContext()
        // 네이티브 쿼리로 순서대로 삭제
        entityManager.createNativeQuery("DELETE FROM mandalart_tags").executeUpdate()
        entityManager.createNativeQuery("DELETE FROM mandalarts").executeUpdate()
        entityManager.createNativeQuery("DELETE FROM tags").executeUpdate()
        entityManager.createNativeQuery("DELETE FROM users").executeUpdate()
        entityManager.flush()
        entityManager.clear()
    }
    
    private fun setAuthentication(userId: Long) {
        val loginUser = LoginUser(userId)
        val authentication = UsernamePasswordAuthenticationToken(loginUser, null, emptyList())
        SecurityContextHolder.getContext().authentication = authentication
    }

    @Test
        @DisplayName("태그 생성 - 성공")
    fun createTag_Success() {
        // given
        setAuthentication(testUser.id)
        val request = CreateTagRequest(name = "운동")

        // when & then
        mockMvc.perform(
            post("/api/tag")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.payload.name").value("운동"))
            .andExpect(jsonPath("$.payload.id").exists())
    }

    @Test
    @DisplayName("태그 삭제 - 성공")
    fun deleteTag_Success() {
        // given
        setAuthentication(testUser.id)
        val tag = Tag.of(name = "삭제할태그", user = testUser)
        tagRepository.save(tag)

        // when & then
        mockMvc.perform(
            delete("/api/tag/{tagId}", tag.id)
        )
            .andExpect(status().isNoContent)
    }


    @Test
    @DisplayName("사용자 태그 목록 조회 - 성공")
    fun getTags_Success() {
        // given
        setAuthentication(testUser.id)
        val tag1 = Tag.of(name = "운동", user = testUser)
        val tag2 = Tag.of(name = "독서", user = testUser)
        tagRepository.saveAll(listOf(tag1, tag2))

        // when & then
        mockMvc.perform(
            get("/api/tag")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.payload").isArray)
            .andExpect(jsonPath("$.payload.length()").value(2))
            .andExpect(jsonPath("$.payload[0].name").exists())
            .andExpect(jsonPath("$.payload[1].name").exists())
    }

    @Test
    @DisplayName("태그 검색 - 성공")
    fun searchTags_Success() {
        // given
        setAuthentication(testUser.id)
        val tag1 = Tag.of(name = "운동하기", user = testUser)
        val tag2 = Tag.of(name = "독서하기", user = testUser)
        val tag3 = Tag.of(name = "운동계획", user = testUser)
        tagRepository.saveAll(listOf(tag1, tag2, tag3))

        // when & then - "운동" 키워드로 검색
        mockMvc.perform(
            get("/api/tag/search")
                .param("keyword", "운동")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.payload").isArray)
            .andExpect(jsonPath("$.payload.length()").value(2))
    }

    @Test
    @DisplayName("만다라트에 태그 추가 - 성공")
    fun addTagToMandalart_Success() {
        // given
        setAuthentication(testUser.id)
        val tag = Tag.of(name = "목표", user = testUser)
        tagRepository.save(tag)

        val request = AddTagToMandalartRequest(tagId = tag.id)

        // when & then
        mockMvc.perform(
            post("/api/mandalart/{mandalartId}/tag", testMandalart.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
    }


    @Test
    @DisplayName("만다라트에서 태그 제거 - 성공")
    fun removeTagFromMandalart_Success() {
        // given
        setAuthentication(testUser.id)
        val tag = Tag.of(name = "제거할태그", user = testUser)
        tagRepository.save(tag)

        // 먼저 태그를 추가
        val request = AddTagToMandalartRequest(tagId = tag.id)
        mockMvc.perform(
            post("/api/mandalart/{mandalartId}/tag", testMandalart.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // when & then - 태그 제거
        mockMvc.perform(
            delete("/api/mandalart/{mandalartId}/tag/{tagId}", testMandalart.id, tag.id)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
    }

}
