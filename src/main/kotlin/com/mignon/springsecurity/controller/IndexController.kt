package com.mignon.springsecurity.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("/")
class IndexController {

    @GetMapping(value = ["/","/index"])
    public fun index() = "index"
}