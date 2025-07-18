package com.mignon.springsecurity.filter

import com.mignon.springsecurity.model.domain.JwtAuthenticationResponse
import com.mignon.springsecurity.provider.JwtTokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
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

@Slf4j
@Component
class JwtAuthenticationFilter(
    private var tokenProvider: JwtTokenProvider,
    private var userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val jwt = getJwtFromRequest(request)?:""
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                val username = tokenProvider.getUsernameFromToken(jwt) // jwt 不会为 null，因为前面已通过 StringUtils.hasText 检查

                val userDetails: UserDetails = userDetailsService.loadUserByUsername(username)
                val authentication = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities
                )
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

                SecurityContextHolder.getContext().authentication = authentication
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
}