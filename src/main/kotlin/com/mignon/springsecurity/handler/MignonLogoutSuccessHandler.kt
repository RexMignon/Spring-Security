package com.mignon.springsecurity.handler

import com.alibaba.fastjson2.JSON
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler

class MignonLogoutSuccessHandler: LogoutSuccessHandler {
    override fun onLogoutSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        var map = mutableMapOf<String, Any>()
        map.put("msg", "成功响应")
        map.put("code", 0)
        map.put(
            "data", mutableMapOf<String, String>
                (
                "message" to "登出成功",
                "principal" to "${authentication?.principal}",  //用户身份信息
//                "credential" to "${authentication?.credentials}",// 用户密码
                "name" to "${authentication?.name}"
            )
        )
        var json = JSON.toJSONString(map)
        response?.let { res ->
            res.contentType = "application/json"
            res.characterEncoding = "UTF-8"
            res.writer.println(json)
        }
    }
}