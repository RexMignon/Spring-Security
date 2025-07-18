package com.mignon.springsecurity.model.dto

import jakarta.validation.constraints.NotBlank

data class UserDto(

    @field:NotBlank(message = "姓名不能为空")
    var userName: String,
    @field:NotBlank(message = "密码不能为空")
    var password: String
)