package ru.kreslavski.family.dinnertime.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.kreslavski.family.dinnertime.dto.request.LoginRequest
import ru.kreslavski.family.dinnertime.dto.request.RegisterRequest
import ru.kreslavski.family.dinnertime.dto.response.AuthResponse
import ru.kreslavski.family.dinnertime.service.AuthService

@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Login successful, JWT returned"),
            ApiResponse(responseCode = "401", description = "Invalid credentials")
        ]
    )
    fun login(
        @Valid
        @RequestBody
        loginRequest: LoginRequest
    ): ResponseEntity<AuthResponse> {
        val response = authService.login(loginRequest)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user and return JWT token.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Registration successful, JWT returned"),
            ApiResponse(responseCode = "409", description = "User already exists")
        ]
    )
    fun register(
        @Valid
        @RequestBody
        registerRequest: RegisterRequest
    ): ResponseEntity<AuthResponse> {
        val response = authService.register(registerRequest)
        return ResponseEntity.ok(response)
    }
}
