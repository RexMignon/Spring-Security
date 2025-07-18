package com.mignon.springsecurity.controller

import com.mignon.springsecurity.model.domain.JwtAuthenticationResponse
import com.mignon.springsecurity.model.domain.LoginRequest
import com.mignon.springsecurity.provider.JwtTokenProvider
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val tokenProvider: JwtTokenProvider
) {

    @PostMapping("/login")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest, response: HttpServletResponse?): ResponseEntity<*> {

        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.username,
                loginRequest.password
            )
        )

        SecurityContextHolder.getContext().authentication = authentication

        val jwt: String = tokenProvider.generateToken(authentication)
        response?.setHeader(JwtAuthenticationResponse().authorization, "${JwtAuthenticationResponse().tokenType}${jwt}")
        return ResponseEntity.ok(JwtAuthenticationResponse(jwt))
    }
}