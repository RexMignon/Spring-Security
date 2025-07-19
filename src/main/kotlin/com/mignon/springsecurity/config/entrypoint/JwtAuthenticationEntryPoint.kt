package com.mignon.springsecurity.config.entrypoint

import com.alibaba.fastjson2.JSON
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException



/**
 * Token验证错误登录等问题会从这里走,
 * @author Mignon
 * @see com.mignon.springsecurity.config.SecurityConfig
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

        var map = mutableMapOf<String, Any>()
        map.put("msg", "错误")
        map.put("code", 401)
        map.put(
            "data", mutableMapOf<String, String>
                (
                "message" to "${authException.message}",
                        "url" to "${authException}"
            )
        )
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.status = SC_UNAUTHORIZED
        logger.error("未认证错误：{} and url ${request.requestURI}", authException.message)
        response.writer.println(JSON.toJSONString(map))
    }
}