package ru.kreslavski.family.dinnertime.dto.response

data class AuthResponse(
    val token: String,
    val tokenType: String = "Bearer",
    val username: String,
    val roles: List<String>
)
