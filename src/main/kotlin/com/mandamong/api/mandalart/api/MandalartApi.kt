package com.mandamong.api.mandalart.api

import com.mandamong.api.auth.api.ApiPath
import com.mandamong.api.mandalart.api.dto.MandalartCreateRequest
import com.mandamong.api.mandalart.application.MandalartFacade

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MandalartApi(
    private val facade: MandalartFacade,
) {

    @PostMapping(ApiPath.Mandalart.CREATE)
    fun create(@RequestBody request: MandalartCreateRequest) {

    }

    @DeleteMapping(ApiPath.Mandalart.DELETE)
    fun delete(@PathVariable mandalartId: Long) {

    }
}
