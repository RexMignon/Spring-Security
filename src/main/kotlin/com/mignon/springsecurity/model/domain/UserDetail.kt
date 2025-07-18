package com.mignon.springsecurity.model.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.Collections

data class UserDetail(

    val _username: String,
    val _password: String,
    val role: String,
    val id: Long? = null
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return Collections.singletonList(SimpleGrantedAuthority(this.role))
    }

    override fun getPassword(): String? {
        return _password
    }
    override fun getUsername(): String {
        return _username
    }

    override fun isAccountNonExpired(): Boolean {
        return true //账户未过期
    }

    override fun isAccountNonLocked(): Boolean {
        return true //账户未锁定
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true //凭证未过期
    }

    override fun isEnabled(): Boolean {
        return true //账户可用
    }
}