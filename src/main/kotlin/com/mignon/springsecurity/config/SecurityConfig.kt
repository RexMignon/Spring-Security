package com.mignon.springsecurity.config


import com.mignon.springsecurity.config.entrypoint.JwtAuthenticationEntryPoint
import com.mignon.springsecurity.filter.JwtAuthenticationFilter
import com.mignon.springsecurity.handler.JwtAccessDeniedHandler
import com.mignon.springsecurity.provider.JwtTokenProvider
import com.mignon.springsecurity.util.Sm3PasswordEncoder
import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.security.Security

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val userDetailsService: UserDetailsService,
    private val unauthorizedHandler: JwtAuthenticationEntryPoint,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val passwordEncoder: PasswordEncoder,
    private val accessDeniedHandler: JwtAccessDeniedHandler,
    private val successHandler: AuthenticationSuccessHandler
) {


    fun init(){
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(BouncyCastleProvider())
        }
    }



//    // 推荐使用 AuthenticationManager Bean 的定义方式
//    @Bean
//    fun authenticationManager(passwordEncoder: PasswordEncoder): AuthenticationManager {
//        val authenticationProvider = DaoAuthenticationProvider()
//        authenticationProvider.setUserDetailsService(userDetailsService)
//        println("Authentication provider: ${passwordEncoder.toString()}")
//
//        authenticationProvider.setPasswordEncoder(passwordEncoder)
//        return ProviderManager(authenticationProvider)
//    }

    // 使用 SecurityFilterChain 配置 HttpSecurity
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors {

            } // 启用 CORS (使用 Kotlin DSL)
            .csrf { it.disable() } // 禁用 CSRF (在无状态的 REST API 中不需要)
            .exceptionHandling {
                it.authenticationEntryPoint(unauthorizedHandler)// 处理未认证请求
                it.accessDeniedHandler(accessDeniedHandler)
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            } // 设置会话管理策略为无状态
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/api/auth/**").permitAll() // 允许所有用户访问认证接口
                    .requestMatchers("/api/public/**").permitAll() // 允许所有用户访问公共接口
                    .requestMatchers("/doc.html").permitAll()
                    .requestMatchers("/webjars/**").permitAll()
                    .requestMatchers("/v3/**").permitAll()
                    .requestMatchers("/").permitAll()
                    .requestMatchers("/oauth2/callback/**").permitAll()
                    .requestMatchers("/oauth2/authorization/**").permitAll() // 启动 OAuth2 登录的入口
                    .requestMatchers("/login/oauth2/code/**").permitAll() // OAuth2 回调 URI，接收授权码
                    .anyRequest().authenticated() // 其他所有请求都需要认证

            }
            .oauth2Login { oauth2Login ->
                oauth2Login
                    // 当用户需要认证时，直接重定向到 GitHub 的 OAuth2 授权页面
                    .loginPage("/oauth2/authorization/github") // <-- 关键：将默认登录页指向 GitHub 的认证URL
                 .successHandler(successHandler)
            }

        // 添加 JWT 认证过滤器
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }


    /**
     * Spring Security 会自动识别,从而获取角色层级
     * */
    @Bean
    fun roleHierarchy(): RoleHierarchy {
        val hierarchy = """
            ROLE_ROOT > ROLE_ADMIN
            ROLE_ADMIN > ROLE_MANAGER
            ROLE_MANAGER > ROLE_USER_SVIP
            ROLE_USER_SVIP > ROLE_USER_VIP
            ROLE_USER_VIP > ROLE_USER
            ROLE_USER > ROLE_GUEST
        """.trimIndent()
        val roleHierarchy = RoleHierarchyImpl.fromHierarchy(hierarchy)
        return roleHierarchy
    }
}