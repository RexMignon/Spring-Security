package com.mignon.springsecurity.service.Impl

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.mignon.springsecurity.manger.MyBatisUserDetailsManger
import com.mignon.springsecurity.mapper.UserMapper
import com.mignon.springsecurity.model.dto.UserDto
import com.mignon.springsecurity.model.po.RolePo
import com.mignon.springsecurity.model.po.UserPo
import com.mignon.springsecurity.service.RoleService
import com.mignon.springsecurity.service.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userMapper: UserMapper,
    private val myBatisUserDetailsManger: MyBatisUserDetailsManger,
    private val roleService: RoleService,
    private val passwordEncoder: PasswordEncoder,

) : UserService, ServiceImpl<UserMapper, UserPo>() {

    /**
     * 构建QueryWrapper, 通过权限名称来查询RulePo
     **/


    override fun saveUserDetails(userDto: UserDto): Boolean {
        /**
         * 获取RoleId,如果获取不了或者想拿ROOT就证明有逆向哥测接口,
         * 直接最低权限
         * */
        var roleId = roleService.selectRoleByRoleName(userDto.permissions)
        if (roleId == 1L) {
            roleId = 7L
        }
        return userMapper.insert(
            UserPo(
                username = userDto.userName,
                password = passwordEncoder.encode(userDto.password),
                roleId = roleId
            )
        ) > 0
    }
}