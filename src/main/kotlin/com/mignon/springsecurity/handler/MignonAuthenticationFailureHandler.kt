package com.mignon.springsecurity.handler

import com.alibaba.fastjson2.JSON
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler

class MignonAuthenticationFailureHandler: AuthenticationFailureHandler {
    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?
    ) {
        var map = mutableMapOf<String, Any>()
        map.put("msg", "成功响应")
        map.put("code", 401)
        map.put(
            "data", mutableMapOf<String, String>
                (
                "message" to "登录失败",
                "principal" to "${exception?.message}",  //用户身份信息
//                "credential" to "${authentication?.credentials}",// 用户密码
            )
        )
        var json = JSON.toJSONString(map)
        response?.let { res ->
            res.contentType = "application/json"
            res.characterEncoding = "UTF-8"
            res.status = HttpServletResponse.SC_UNAUTHORIZED
            res.writer.println(json)
        }
    }
}