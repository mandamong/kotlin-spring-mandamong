package com.mandamong.api.domain.mandalart.api

import com.mandamong.api.domain.mandalart.api.dto.MandalartCreateRequest
import com.mandamong.api.domain.mandalart.application.MandalartFacade
import com.mandamong.api.global.common.ApiPath
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
        //facade.create(request)
    }

    @DeleteMapping(ApiPath.Mandalart.DELETE)
    fun delete(@PathVariable mandalartId: Long) {
        //facade.delete(mandalartId)
    }
}
