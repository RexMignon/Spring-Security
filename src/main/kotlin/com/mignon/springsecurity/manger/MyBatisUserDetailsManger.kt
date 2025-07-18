package com.mignon.springsecurity.manger

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.mignon.springsecurity.mapper.UserMapper
import com.mignon.springsecurity.model.po.UserPo
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsPasswordService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.stereotype.Component
import java.util.Objects

@Component
class MyBatisUserDetailsManger(private val userMapper: UserMapper) : UserDetailsManager, UserDetailsPasswordService {
    override fun createUser(user: UserDetails?) {
        user?.let {
            var userPo = UserPo(null, user.username, user.password, true)
            userMapper.insert(userPo)
        }
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
            var userPo: UserPo = userMapper.selectOne(queryWrapper)

            if (Objects.isNull(userPo)) {
                throw UsernameNotFoundException("Username not found")
            } else {
                var authorities = ArrayList<GrantedAuthority>()
                    return User(
                        userPo.username,
                        userPo.password,
                        userPo.enabled,
                        true,//用户账号是否未过期?
                        true,//用户凭证是否未过期?
                        true,//用户是否未锁定?
                        authorities //权限
                    )
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