package com.mignon.springsecurity.handler

import com.mignon.springsecurity.model.domain.JwtAuthenticationResponse
import com.mignon.springsecurity.model.dto.UserDto
import com.mignon.springsecurity.provider.CustomDaoAuthenticationProvider
import com.mignon.springsecurity.provider.JwtTokenProvider
import com.mignon.springsecurity.service.Impl.SessionService
import com.mignon.springsecurity.service.UserService
import com.mignon.springsecurity.util.Sm3PasswordEncoder
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.util.Objects
import java.util.UUID

@Component
class GitHubAuthenticationSuccessHandler(
    private val userDetailsService: UserDetailsService,
    private val userService: UserService,
    private val authenticationManager: CustomDaoAuthenticationProvider,
    private val sessionService: SessionService,
    private val tokenProvider: JwtTokenProvider
) : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?

    ) {
        // 1. 获取 Spring Security 提供的 OAuth2User 对象
        val oauth2User = authentication?.principal as OAuth2User

        // 2. 从 oauth2User 中提取 GitHub 用户信息
        // 属性名取决于 GitHub API 返回的 JSON 结构，常见的有 "id", "login", "email"
        val githubId = oauth2User.attributes["id"]?.toString()
        val githubLogin = oauth2User.attributes["login"]?.toString()
        val githubEmail = oauth2User.attributes["email"]?.toString()

        println("--- GitHub OAuth2 认证成功 ---")
        println("GitHub ID: $githubId")
        println("GitHub Login: $githubLogin")
        println("GitHub Email: $githubEmail")
        println("--- 提取用户信息完成 ---")

        var userDetails: UserDetails?
            = userDetailsService.loadUserByUsername(githubLogin) // 假设你的UserDetailsService能处理这个
        if (Objects.isNull(userDetails)){
            /**
             * 重定向到注册页或者创建新用户
             * */
            var userDto : UserDto = UserDto(
                githubLogin?:"default",
                "pwd"
            )
            userService.saveUserDetails(userDto)
        }
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                githubLogin?:"default",
                "pwd"
            )
        )

        SecurityContextHolder.getContext().authentication = authentication
        userDetails = authentication.principal as UserDetails
        var name = authentication.name
        var sessionId = UUID.randomUUID().toString()
        sessionService.setActiveSession(name, sessionId)
        val jwt: String = tokenProvider.generateToken(authentication, sessionId)
        response?.setHeader(JwtAuthenticationResponse().authorization, "${JwtAuthenticationResponse().tokenType}${jwt}")
        val redirectUrl = "http://127.0.0.1/?token=${JwtAuthenticationResponse().tokenType}${jwt}&username=${name}"
            response?.sendRedirect(redirectUrl)
    }
}