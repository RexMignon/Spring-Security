package com.mignon.springsecurity.config.entrypoint

import com.alibaba.fastjson2.JSON
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

class MignonAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        // 用户未授权
        var map = mutableMapOf<String, Any>()
        map.put("msg", "用户未登录")
        map.put("code", 401)
        map.put(
            "data", mutableMapOf<String, String>
                (
                "title" to "用户未登录",
                "message" to "${authException?.message}"  //用户身份信息
            )
        )
        var json = JSON.toJSONString(map)
        response?.let { res ->
            res.contentType = "application/json"
            res.characterEncoding = "UTF-8"
            res.status = SC_UNAUTHORIZED
            res.writer.println(json)
        }
    }
}
