package com.mandamong.server.mandalart.controller

import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.common.dto.ApiResponse
import com.mandamong.server.mandalart.dto.AddTagToMandalartRequest
import com.mandamong.server.mandalart.dto.CreateTagRequest
import com.mandamong.server.mandalart.dto.TagResponse
import com.mandamong.server.mandalart.service.MandalartTagService
import com.mandamong.server.mandalart.service.TagService
import com.mandamong.server.user.dto.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TagController(
    private val tagService: TagService,
    private val mandalartTagService: MandalartTagService,
) {
    @PostMapping(ApiPath.Tag.CREATE)
    fun createTag(
        @RequestBody request: CreateTagRequest,
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<TagResponse>> {
        val tag = tagService.create(request.name, loginUser.userId)
        return ApiResponse.created(TagResponse.from(tag))
    }

    @DeleteMapping(ApiPath.Tag.DELETE)
    fun deleteTag(
        @PathVariable tagId: Long,
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<Nothing>> {
        tagService.delete(tagId, loginUser.userId)
        return ApiResponse.deleted()
    }

    @GetMapping(ApiPath.Tag.TAGS)
    fun getTags(
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<List<TagResponse>>> {
        val tags = tagService.findByUser(loginUser.userId)
        val tagResponses = tags.map { TagResponse.from(it) }
        return ApiResponse.ok(tagResponses)
    }

    @GetMapping(ApiPath.Tag.SEARCH)
    fun searchTags(
        @RequestParam keyword: String,
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<List<TagResponse>>> {
        val tags = tagService.searchByName(loginUser.userId, keyword)
        val tagResponses = tags.map { TagResponse.from(it) }
        return ApiResponse.ok(tagResponses)
    }

    @PostMapping(ApiPath.Tag.ADD_TO_MANDALART)
    fun addTagToMandalart(
        @PathVariable mandalartId: Long,
        @RequestBody request: AddTagToMandalartRequest,
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<Nothing>> {
        mandalartTagService.addTagToMandalart(mandalartId, request.tagId, loginUser.userId)
        return ApiResponse.ok()
    }

    @DeleteMapping(ApiPath.Tag.REMOVE_FROM_MANDALART)
    fun removeTagFromMandalart(
        @PathVariable mandalartId: Long,
        @PathVariable tagId: Long,
        @AuthenticationPrincipal loginUser: LoginUser,
    ): ResponseEntity<ApiResponse<Nothing>> {
        mandalartTagService.removeTagFromMandalart(mandalartId, tagId, loginUser.userId)
        return ApiResponse.ok()
    }
}
