package com.mandamong.server.mandalart.controller

import com.mandamong.server.common.constants.ApiPath
import com.mandamong.server.mandalart.dto.request.MandalartCreateRequest
import com.mandamong.server.mandalart.facade.MandalartFacade
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MandalartController(
    private val facade: MandalartFacade,
) {

    @PostMapping(ApiPath.Mandalart.CREATE)
    fun create(@RequestBody request: MandalartCreateRequest) {

    }

    @DeleteMapping(ApiPath.Mandalart.DELETE)
    fun delete(@PathVariable mandalartId: Long) {

    }

}
