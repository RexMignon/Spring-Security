package com.mignon.springsecurity.manger

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.mignon.springsecurity.mapper.UserMapper
import com.mignon.springsecurity.model.po.UserPo
import com.mignon.springsecurity.service.RoleService
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsPasswordService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.stereotype.Component
import java.util.Objects

@Component
class MyBatisUserDetailsManger(
    private val userMapper: UserMapper,
    private val roleService: RoleService
)
    : UserDetailsManager, UserDetailsPasswordService {
    override fun createUser(user: UserDetails?) {

    }

    override fun userExists(username: String?): Boolean {
        return false
    }

    override fun changePassword(oldPassword: String?, newPassword: String?) {

    }

    override fun updateUser(user: UserDetails?) {

    }

    override fun deleteUser(username: String?) {

    }

    override fun loadUserByUsername(username: String?): UserDetails? {
        var queryWrapper: QueryWrapper<UserPo> = QueryWrapper<UserPo>()

        username?.let {
            queryWrapper.eq("username", it)
            var userPo: UserPo? = userMapper.selectOne(queryWrapper)

            return if (Objects.isNull(userPo)) {
                null
            } else {
                User.withUsername(username)
                    .password(userPo!!.password)
                    .accountExpired(userPo.accountExpired)
                    .accountLocked(userPo.accountLocked)
                    .disabled(userPo.disenabled)
                    .credentialsExpired(userPo.credentialsExpired)
                    .roles(
                        roleService.getById(userPo.roleId).roleName
                    ).build()

            }
        }
        return null
    }

    override fun updatePassword(
        user: UserDetails?,
        newPassword: String?
    ): UserDetails? {
        return null
    }
}