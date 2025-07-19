package com.mignon.springsecurity.filter

import com.alibaba.fastjson2.JSON
import com.mignon.springsecurity.config.entrypoint.JwtAuthenticationEntryPoint
import com.mignon.springsecurity.model.domain.JwtAuthenticationResponse
import com.mignon.springsecurity.provider.JwtTokenProvider
import com.mignon.springsecurity.service.Impl.SessionService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED
import lombok.extern.slf4j.Slf4j
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.Objects

@Slf4j
@Component
class JwtAuthenticationFilter(
    private var tokenProvider: JwtTokenProvider,
    private var userDetailsService: UserDetailsService,
    private var sessionService: SessionService
) : OncePerRequestFilter() {
    private val EXCLUDE_URLS = listOf(
        "/api/auth/login", "/oauth2/authorization/**",
        "/login/oauth2/code/**",
        "/oauth2/callback/**"
    )

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            val jwt = getJwtFromRequest(request) ?: ""
            var maps = tokenProvider.validateToken(jwt)
            var result = maps["result"]
            var bool = Objects.equals(result, "")
            if (StringUtils.hasText(jwt) && bool) {
                val username = tokenProvider.getUsernameFromToken(jwt) // jwt 不会为 null，因为前面已通过 StringUtils.hasText 检查
                val sessionId = tokenProvider.getSessionIdFromToken(jwt)
                var activeSessionId = sessionService.getActiveSession(username)
                activeSessionId?.let {
                    if (it == sessionId) {
                        val userDetails: UserDetails = userDetailsService.loadUserByUsername(username)
                        val authentication = UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.authorities
                        )
                        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

                        SecurityContextHolder.getContext().authentication = authentication
                    } else {
                        logger.warn("用户${username}的会话ID不匹配或已失效。Token sessionId: ${sessionId}, Active sessionId: ${activeSessionId}")
                        var map = mutableMapOf<String, Any>()
                        map.put("msg", "用户已在其他浏览器登录")
                        map.put("code", 401)
                        map.put(
                            "data", mutableMapOf<String, String>
                                (
                                "title" to "用户需重新登录"
                            )
                        )
                        response.contentType = "application/json"
                        response.characterEncoding = "UTF-8"
                        response.status = SC_UNAUTHORIZED
                        response.writer.println(JSON.toJSONString(map))

                        return
                    }
                }


            }
            if (!bool) {
                if (Objects.isNull(maps["empty"])) {
                    var map = mutableMapOf<String, Any>()
                    map.put("msg", "$result")
                    map.put("code", 401)
                    map.put(
                        "data", mutableMapOf<String, String>
                            (
                            "title" to "$result"
                        )
                    )
                    response.contentType = "application/json"
                    response.characterEncoding = "UTF-8"
                    response.status = SC_UNAUTHORIZED
                    response.writer.println(JSON.toJSONString(map))
                    return
                }


            }
        } catch (ex: Exception) {
            logger.error("无法设置用户认证", ex)
        }

        filterChain.doFilter(request, response)
    }


    /**
     * 从请求头中获取Token
     * */
    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        // 检查 Authorization 头部是否以 Bearer 开头
        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtAuthenticationResponse().tokenType)) {
            // 提取 JWT 字符串
            bearerToken.removePrefix(JwtAuthenticationResponse().tokenType)
        } else null
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        // 检查请求路径是否在 EXCLUDE_URLS 列表中
        // request.servletPath 获取的是不带 context path 的路径，比如 /api/auth/login
        return EXCLUDE_URLS.contains(request.servletPath) || EXCLUDE_URLS.contains(request.requestURI)
    }
}