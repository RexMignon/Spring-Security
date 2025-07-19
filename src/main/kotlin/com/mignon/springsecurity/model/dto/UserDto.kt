package com.mignon.springsecurity.model.dto

import com.baomidou.mybatisplus.annotation.TableField
import jakarta.validation.constraints.NotBlank

data class UserDto(

    @field:NotBlank(message = "姓名不能为空")
    var userName: String,
    @field:NotBlank(message = "密码不能为空")
    var password: String,

    var permissions: String = "USER",

    var credentialsExpired: Boolean = false,

    var accountExpired: Boolean = false,

    var enabled: Boolean = false,

    var accountLocked: Boolean = false,

)