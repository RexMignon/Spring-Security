package com.mignon.springsecurity.service.Impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.mignon.springsecurity.manger.MyBatisUserDetailsManger
import com.mignon.springsecurity.mapper.UserMapper
import com.mignon.springsecurity.model.dto.UserDto
import com.mignon.springsecurity.model.po.UserPo
import com.mignon.springsecurity.service.UserService
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userMapper: UserMapper,
    private val myBatisUserDetailsManger: MyBatisUserDetailsManger
) : UserService, ServiceImpl<UserMapper, UserPo>() {
    override fun saveUserDetails(userDto: UserDto): Boolean {
        var userDetails: UserDetails = User(userDto.userName, userDto.password, ArrayList<GrantedAuthority>())
        myBatisUserDetailsManger.createUser(userDetails)
        return true
    }
}