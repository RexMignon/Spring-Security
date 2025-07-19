package com.mignon.springsecurity.service.Impl


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class SessionService {

    @Autowired
    private lateinit var redisTemplate: StringRedisTemplate

    companion object {
        private const val USER_SESSION_KEY_PREFIX = "user:activeSession:"
        private const val SESSION_EXPIRATION_DAYS: Long = 1
    }

    /**
     * 设置用户当前活跃的会话ID。
     * 当新登录时调用，会覆盖旧的会话ID，从而使旧令牌失效。
     * @param username 用户名
     * @param sessionId 唯一会话ID
     */
    fun setActiveSession(username: String, sessionId: String) {
        val key = USER_SESSION_KEY_PREFIX + username
        redisTemplate.opsForValue().set(key, sessionId, SESSION_EXPIRATION_DAYS, TimeUnit.DAYS)
    }

    /**
     * 获取用户当前活跃的会话ID。
     * @param username 用户名
     * @return 当前活跃的sessionId，如果没有则返回null
     */
    fun getActiveSession(username: String): String? {
        val key = USER_SESSION_KEY_PREFIX + username
        return redisTemplate.opsForValue().get(key)
    }

    /**
     * 使指定用户的会话失效 (例如，用户登出)。
     * @param username 用户名
     */
    fun invalidateSession(username: String) {
        val key = USER_SESSION_KEY_PREFIX + username
        redisTemplate.delete(key)
    }
}