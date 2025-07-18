package com.mignon.springsecurity.service

import com.baomidou.mybatisplus.extension.service.IService
import com.mignon.springsecurity.model.dto.UserDto
import com.mignon.springsecurity.model.po.UserPo

interface UserService: IService<UserPo> {
    fun saveUserDetails(userDto: UserDto): Boolean

}