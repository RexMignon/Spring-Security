package com.mignon.springsecurity.controller

import com.mignon.springsecurity.util.Result
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/test")
@Tag(name = "权限测试集合", description = "权限测试接口集合")
class TestController (){

    @Operation(summary = "Root权限可访问")
    @PreAuthorize("hasRole('ROOT')")
    @PostMapping("/root")
    fun root(): Result<String>{
        return Result.success("success")
    }

    @Operation(summary = "游客权限可访问")
    @PreAuthorize("hasRole('GUEST')")
    @PostMapping("/guest")
    fun guest(): Result<String>{
        return Result.success("success")
    }

    @Operation(summary = "普通用户权限可访问")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/user")
    fun user(): Result<String>{
        return Result.success("success")
    }

    @Operation(summary = "系统管理员权限可访问")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    fun admin(): Result<String>{
        return Result.success("success")
    }

    @Operation(summary = "业务经理权限可访问")
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/manager")
    fun manager(): Result<String>{
        return Result.success("success")
    }

    @Operation(summary = "vip用户权限可访问")
    @PreAuthorize("hasRole('USER_VIP')")
    @PostMapping("/uservip")
    fun userVip(): Result<String>{
        return Result.success("success")
    }

    @Operation(summary = "svip用户权限可访问")
    @PreAuthorize("hasRole('USER_SVIP')")
    @PostMapping("/usersvip")
    fun userSvip(): Result<String>{
        return Result.success("success")
    }



}