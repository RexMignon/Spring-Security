package com.mignon.springsecurity.util
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class hash {
}


fun main() {
    val password = "123" // 替换成你想要哈希的实际密码

    // 创建一个 BCryptPasswordEncoder 实例
    // 默认的强度（strength）是 10，你可以根据安全需求调整，范围通常是 4 到 31。
    // 强度越高，哈希越慢，但安全性越好。
    val passwordEncoder = BCryptPasswordEncoder()

    // 使用 encode() 方法来哈希密码
    val hashedPassword = passwordEncoder.encode(password)

    println("原始密码: $password")
    println("BCrypt 哈希后的密码: $hashedPassword")

    // 验证密码（可选）：
    // 假设用户输入了相同的密码，你想验证它是否与哈希值匹配
    val isMatch = passwordEncoder.matches(password, hashedPassword)
    println("原始密码是否匹配哈希值: $isMatch")

    // 验证一个错误的密码（可选）：
    val wrongPassword = "wrong_password"
    val isWrongMatch = passwordEncoder.matches(wrongPassword, hashedPassword)
    println("错误密码是否匹配哈希值: $isWrongMatch")
}