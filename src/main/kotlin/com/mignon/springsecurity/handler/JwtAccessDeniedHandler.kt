package com.mignon.springsecurity.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.util.Date


@Component
class JwtAccessDeniedHandler(
    private var objectMapper: ObjectMapper
) : AccessDeniedHandler {


    private val logger: Logger = LoggerFactory.getLogger(JwtAccessDeniedHandler::class.java)

    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {
        if (request != null) {
            logger.warn(
                "访问被拒绝: 用户 {} 尝试访问受限资源 {}",
                request.userPrincipal?.name ?: "匿名用户",
                request.requestURI,
                accessDeniedException
            )
        }
        response?.let{ it ->
            it.status = HttpStatus.FORBIDDEN.value()
            it.contentType = "application/json;charset=UTF-8"
            val errorDetails = mapOf(
                "status" to HttpStatus.FORBIDDEN.value(),
                "error" to "Forbidden",
                "message" to "您没有足够的访问权限"

            )
          objectMapper.writeValue(it.writer, errorDetails)
        }
    }
}