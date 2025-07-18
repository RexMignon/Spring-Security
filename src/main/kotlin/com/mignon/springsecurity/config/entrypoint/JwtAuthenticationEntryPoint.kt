package com.mignon.springsecurity.config.entrypoint

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException



/**
 * Token验证错误会从这里走
 *
 *
 * */
@Slf4j
@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint::class.java)
    }

    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        logger.error("未认证错误：{}", authException.message)
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "未经授权")
    }
}