package com.mignon.springsecurity.controller

import com.mignon.springsecurity.model.dto.UserDto
import com.mignon.springsecurity.model.vo.UserVo
import com.mignon.springsecurity.service.UserService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mignon.springsecurity.util.Result
import jakarta.validation.Valid
import org.springframework.beans.BeanUtils
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.stream.Collectors

@RestController
@RequestMapping("/user")
class UserController
    (private val userService: UserService,
     private val userDetailsService: UserDetailsService,
            ) {

    @GetMapping("/list")
    public fun getUserList(): Result<List<UserVo>> {
        var result = userService.list()
        var list = result.stream().map { user ->
            val userVo = UserVo()
            BeanUtils.copyProperties(user, userVo)
            userVo
        }
            .collect(Collectors.toList())
        return Result.success(list)
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public fun register(@Valid @RequestBody userDto: UserDto): Result<Boolean> {
        userService.saveUserDetails(userDto)
        var auth  = SecurityContextHolder.getContext().authentication
        return Result.success(true)
    }

}