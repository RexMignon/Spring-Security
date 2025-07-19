package com.mignon.springsecurity.provider


import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date
import java.util.Objects

@Component
class JwtTokenProvider() {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(JwtTokenProvider::class.java)
    }

    @Value("\${app.jwtSecret}") // 从 application.properties 获取密钥
    private lateinit var jwtSecret: String

    @Value("\${app.jwtExpirationMs}") // 从 application.properties 获取过期时间
    private var jwtExpirationMs: Int = 0

    private lateinit var key: Key

    @PostConstruct
    fun init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    }
        // 签发Token
    fun generateToken(authentication: Authentication, sessionId : String): String {
        // 通常，UserDetails 实现了 Authentication
        // TODO 这里可以设置id
        // 如果你的 UserDetails 类是自定义的，确保它有获取用户名的方法
        val username = authentication.name

        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationMs)

        return Jwts.builder()
            .setSubject(username) // 主题：通常是用户名
            .setIssuedAt(Date()) // 签发时间
            .setExpiration(expiryDate) // 过期时间
            .claim("sessionId", sessionId)
            .signWith(key, SignatureAlgorithm.HS512) // 签名算法和密钥
            .compact()
    }
        // 从Token获取用户名
    fun getUsernameFromToken(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }


    public fun getSessionIdFromToken(token: String): String {
        var claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
        return claims["sessionId"] as String
    }

    /**
     * TODO 应当从这里返回信息
     * */
    // 验证Token
    fun validateToken(authToken: String): Map<String, String> {
        // 第一个参数是result 结果, 如果为空,就证明 是验证成功
        var map = mutableMapOf<String, String>()
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken)
            map.put("result", "")

            return map
        } catch (e: io.jsonwebtoken.security.SecurityException) {
            logger.error("SecurityException 无效的 JWT 签名 -> {}", e.message)
            map.put("result", "无效的 JWT 签名")
            return map
        } catch (e: MalformedJwtException) {
            logger.error("MalformedJwtException 无效的 JWT 签名 -> {}", e.message)
            map.put("result", "无效的 JWT 签名")
            return map
        } catch (e: ExpiredJwtException) {
            logger.error("过期的 JWT 令牌 -> {}", e.message)
            map.put("result", "过期的 JWT 令牌")
            return map
        } catch (e: UnsupportedJwtException) {
            logger.error("不支持的 JWT 令牌 -> {}", e.message)
            map.put("result", "不支持的 JWT 令牌")
            return map
        } catch (e: IllegalArgumentException) {
            map.put("result", "JWT 字符串为空")
            map.put("empty", "empty")
            return map
        }
    }
}