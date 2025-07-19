package com.mignon.springsecurity.provider

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

// @Component 注解使其成为一个 Spring Bean
@Component
class CustomDaoAuthenticationProvider(
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder
) : DaoAuthenticationProvider() { // 继承 DaoAuthenticationProvider

    // 构造函数注入 UserDetailsService 和 PasswordEncoder
    init {
        // 设置父类所需的 UserDetailsService 和 PasswordEncoder
        this.setUserDetailsService(userDetailsService)
        this.setPasswordEncoder(passwordEncoder)
        println("CustomDaoAuthenticationProvider initialized with PasswordEncoder: ${passwordEncoder.javaClass.simpleName}")
    }

    // 你可以在这里重写父类方法来自定义认证逻辑
    // 例如，如果你想在认证失败时提供更详细的错误信息
    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        try {
            // 调用父类的认证方法
            return super.authenticate(authentication)
        } catch (e: BadCredentialsException) {
            // 捕获特定的认证异常并可以自定义处理
            println("认证失败：${e.message}")
            throw BadCredentialsException("用户名或密码不正确！") // 返回自定义消息
        } catch (e: Exception) {
            println("认证过程中发生未知错误：${e.message}")
            throw e
        }
    }

    // 确保这个 AuthenticationProvider 支持 UsernamePasswordAuthenticationToken
    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }
}