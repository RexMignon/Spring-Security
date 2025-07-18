package com.mignon.springsecurity.util

import org.bouncycastle.crypto.digests.SM3Digest
import org.bouncycastle.util.encoders.Hex
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.util.Arrays

/**
 * 基于SM3算法实现的PasswordEncoder，支持加盐。
 * 格式为：{sm3}盐值$SM3哈希值
 * 盐值和哈希值都使用Hex编码表示。
 */
@Component
class Sm3PasswordEncoder : PasswordEncoder {

    companion object {
        private const val ID_PREFIX = "{mignon}"
    }

    private val random = SecureRandom()
    private val saltLength = 16 // 盐值长度，单位字节

    override fun encode(rawPassword: CharSequence?): String {
        require(rawPassword != null) { "rawPassword cannot be null" }

        val salt = generateSalt()
        val hashedBytes = hashWithSm3(rawPassword.toString().toByteArray(), salt)

        // 拼接成 {sm3}盐值$哈希值 格式
        return ID_PREFIX + Hex.toHexString(salt) + "$" + Hex.toHexString(hashedBytes)
    }

    override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
        if (rawPassword == null || encodedPassword == null) {
            return false
        }
        if (!encodedPassword.startsWith(ID_PREFIX)) {
            // 如果不是 SM3 格式，可以考虑抛出异常或返回 false
            // 这里我们返回 false，表示不匹配
            return false
        }

        // 截取前缀后的字符串
        val actualEncodedPassword = encodedPassword.removePrefix(ID_PREFIX)
        val parts = actualEncodedPassword.split("$") // 使用 split() 方法

        if (parts.size != 2) {
            // 格式不正确
            return false
        }

        val salt = Hex.decode(parts[0])
        val storedHashedBytes = Hex.decode(parts[1])

        val newHashedBytes = hashWithSm3(rawPassword.toString().toByteArray(), salt)

        return newHashedBytes.contentEquals(storedHashedBytes)
    }

    private fun generateSalt(): ByteArray {
        val salt = ByteArray(saltLength)
        random.nextBytes(salt)
        return salt
    }

    private fun hashWithSm3(passwordBytes: ByteArray, salt: ByteArray): ByteArray {
        val sm3 = SM3Digest()
        sm3.update(passwordBytes, 0, passwordBytes.size)
        sm3.update(salt, 0, salt.size) // 将盐值也加入哈希
        val result = ByteArray(sm3.digestSize)
        sm3.doFinal(result, 0)
        return result
    }
}

fun main(){
    println(Sm3PasswordEncoder().encode("pwd"))
}