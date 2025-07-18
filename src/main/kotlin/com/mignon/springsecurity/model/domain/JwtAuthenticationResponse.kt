package com.mignon.springsecurity.model.domain

data class JwtAuthenticationResponse(
    val accessToken: String = "",
    val authorization :String = "Authorization",
    public val tokenType: String = "FutureByMignonToken_"
)