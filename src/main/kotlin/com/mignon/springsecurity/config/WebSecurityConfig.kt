package com.mignon.springsecurity.config

import com.mignon.springsecurity.handler.MignonAuthenticationFailureHandler
import com.mignon.springsecurity.handler.MignonAuthenticationSuccessHandler
import com.mignon.springsecurity.mapper.UserMapper
import org.springframework.aot.generate.ValueCodeGenerator.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy


//@Configuration
open class WebSecurityConfig(private val userMapper: UserMapper) {


    /**
     * 把用户的信息管理到内存当中
     * */
    /*    @Bean
        public fun userDetailsService(): UserDetailsService {
            // 创建基于内存的用户管理器
            var manager = InMemoryUserDetailsManager()
            // 窗口按userDetails对象用于管理用户名用户密码,用户角色用户 权限等内容
            manager.createUser(
                User.withDefaultPasswordEncoder().username("user")
                    .password("password").roles("USER").build()
            )
            return manager
        }*/

//    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                // 对login关闭授权保护
//                authorize(AntPathRequestMatcher("/login"), permitAll)
                // 对所有的请求开启授权保护         //已认证的请求不会被自动授权
                authorize(anyRequest, authenticated)
            }
            formLogin {
                withDefaults() //使用默认表单授权方式
//                loginPage = "/login" //自定义登录adress
                usernameParameter = "username" //表单接收的字段
                passwordParameter = "password" //表单接收的字段名
//                failureUrl = "/login" //出错的导向地址
                permitAll()
                authenticationSuccessHandler = MignonAuthenticationSuccessHandler()
                authenticationFailureHandler = MignonAuthenticationFailureHandler()
            }


            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.IF_REQUIRED

                sessionConcurrency {
                    maximumSessions = 1
                    // 当达到最大会话数时，是否阻止新登录 (true: 阻止新登录, false: 使旧会话失效)
                    maxSessionsPreventsLogin = false // 旧会话将被踢出，允许新会话登录
                    // 必须引用 SessionRegistry Bean
                    expiredSessionStrategy = MignonSessionInformationExpiredsfrategy()
                    // 如果 maxSessionsPreventsLogin = false，会话被踢出后通常会重定向到 invalidSessionUrl
                    // 或者你可以设置一个自定义的 maximumSessionsExceededHandler 来处理

                }

            }


            cors {
                withDefaults()
            }

            exceptionHandling {
                //验证未登录
//                authenticationEntryPoint= MignonAuthenticationEntryPoint()
            }

            logout {

            }

            httpBasic {
//                withDefaults() //使用基本授权方式

            }
            csrf {
                disable()
            }
        }

        return http.build()
    }

}

/**
 *
--Create database

CREATE DATABASE security-demo;

USE security-demo;

-- Create user table

CREATE TABLE `user` (

`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,

`username` VARCHAR(50) DEFAULT NULL

`password` VARCHAR(500) DEFAULT NULL,

'enabled` BOOLEAN NOT NULL

);

-- unique index

CREATE UNIQUE INDEX `user_username_uindex` ON `user` (`username`);

-- Insert user data (password is "abc")

INSERT INTO `user` (`username`, `password', 'enabled`) VALUES
('admin', '{bcrypt}$2a$10$GRLdNijSQMUv1/au9ofL.eDwmoohzzS7.rmNSJZ.0Fx0/BTk76k1W', TRUE),
('Helen', '{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0Fx0/BTk76k1W', TRUE),
('Tom', '{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0Fx0/BTk76k1W', TRUE);

 * */