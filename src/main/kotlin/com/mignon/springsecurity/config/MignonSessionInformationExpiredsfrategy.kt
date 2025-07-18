package com.mignon.springsecurity.config

import com.alibaba.fastjson2.JSON
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import org.springframework.security.web.session.SessionInformationExpiredEvent
import org.springframework.security.web.session.SessionInformationExpiredStrategy

class MignonSessionInformationExpiredsfrategy: SessionInformationExpiredStrategy {
    override fun onExpiredSessionDetected(event: SessionInformationExpiredEvent?) {
        var map = mutableMapOf<String, Any>()
        map.put("msg", "成功响应")
        map.put("code", 401)
        map.put(
            "data", mutableMapOf<String, String>
                (
                "message" to "该设备已在其他设备登录",
                "principal" to "${event?.sessionInformation}",  //用户身份信息
//                "credential" to "${authentication?.credentials}",// 用户密码
            )
        )
        var json = JSON.toJSONString(map)
        event?.let { eve ->
            eve.response.let { res ->
                res.contentType = "application/json"
                res.characterEncoding = "UTF-8"
                res.status = HttpServletResponse.SC_UNAUTHORIZED
                res.writer.println(json)
            }
        }
    }

}