package com.mignon.springsecurity.config


import com.mignon.springsecurity.config.entrypoint.JwtAuthenticationEntryPoint
import com.mignon.springsecurity.filter.JwtAuthenticationFilter
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.security.Security

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Spring Security 6+ 推荐使用 @EnableMethodSecurity
class SecurityConfig(
    private val userDetailsService: UserDetailsService,
    private val unauthorizedHandler: JwtAuthenticationEntryPoint,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val passwordEncoder: PasswordEncoder
) {


    fun init(){


        // 确保 Bouncy Castle FIPS Provider 被添加到安全提供者列表中
        // 注意：BC-FIPS 的提供者类名可能因版本而异，请查阅其文档
        // 通常是 org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider
        // 或者 org.bouncycastle.jce.provider.BouncyCastleProvider
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(BouncyCastleProvider())
        }


    }



    // 推荐使用 AuthenticationManager Bean 的定义方式
    @Bean
    fun authenticationManager(passwordEncoder: PasswordEncoder): AuthenticationManager {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(userDetailsService)
        println("Authentication provider: ${passwordEncoder.toString()}")

        authenticationProvider.setPasswordEncoder(passwordEncoder)
        return ProviderManager(authenticationProvider)
    }

    // 使用 SecurityFilterChain 配置 HttpSecurity
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors {

            } // 启用 CORS (使用 Kotlin DSL)
            .csrf { it.disable() } // 禁用 CSRF (在无状态的 REST API 中不需要)
            .exceptionHandling { it.authenticationEntryPoint(unauthorizedHandler) } // 处理未认证请求
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } // 设置会话管理策略为无状态
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/api/auth/**").permitAll() // 允许所有用户访问认证接口
                    .requestMatchers("/api/public/**").permitAll() // 允许所有用户访问公共接口
                    .requestMatchers("/doc.html").permitAll()
                    .requestMatchers("/webjars/**").permitAll()
                    .requestMatchers("/v3/**").permitAll()
                    .anyRequest().authenticated() // 其他所有请求都需要认证
            }

        // 添加 JWT 认证过滤器
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}