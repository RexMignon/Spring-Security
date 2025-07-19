package com.mignon.springsecurity.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.security.Principal


@Controller
@RequestMapping("/")
class IndexController {

    @GetMapping(value = ["/","/index"])

    public fun index():String = "index"


    @PreAuthorize("hasRole('USER')")
    @ResponseBody
    @GetMapping("/success")
    fun successInfo(principal: Principal): Result<String> {
        val username = principal.name
        return Result.success("恭喜您，$username！您已成功访问")
    }
}