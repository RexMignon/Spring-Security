package com.mignon.springsecurity.controller

import com.mignon.springsecurity.model.domain.JwtAuthenticationResponse
import com.mignon.springsecurity.model.domain.LoginRequest
import com.mignon.springsecurity.provider.CustomDaoAuthenticationProvider
import com.mignon.springsecurity.provider.JwtTokenProvider
import com.mignon.springsecurity.service.Impl.SessionService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: CustomDaoAuthenticationProvider,
    private val tokenProvider: JwtTokenProvider,
    private val sessionService: SessionService
) {

    @PostMapping("/login")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest, response: HttpServletResponse?): Result<ResponseEntity<*>> {

        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.username,
                loginRequest.password
            )
        )

        SecurityContextHolder.getContext().authentication = authentication
        var userDetails: UserDetails = authentication.principal as UserDetails
        var name = authentication.name
        var sessionId = UUID.randomUUID().toString()
        sessionService.setActiveSession(name, sessionId)
        val jwt: String = tokenProvider.generateToken(authentication, sessionId)
        response?.setHeader(JwtAuthenticationResponse().authorization, "${JwtAuthenticationResponse().tokenType}${jwt}")
        return Result.success(ResponseEntity.ok(JwtAuthenticationResponse(jwt)))
    }


    @PostMapping("/logout")
    public fun logout(authentication: Authentication?): Result<String> {
        authentication?.let {
            if (it.isAuthenticated){
                var name = it.name
                sessionService.invalidateSession(name)
                return Result.success("成功登出")
            }
        }
        return Result.success("登出失败")
    }
}