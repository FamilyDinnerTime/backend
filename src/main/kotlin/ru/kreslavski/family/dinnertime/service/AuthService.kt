package ru.kreslavski.family.dinnertime.service

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.kreslavski.family.dinnertime.dto.request.LoginRequest
import ru.kreslavski.family.dinnertime.dto.request.RegisterRequest
import ru.kreslavski.family.dinnertime.dto.response.AuthResponse
import ru.kreslavski.family.dinnertime.entity.User
import ru.kreslavski.family.dinnertime.exception.UserAlreadyExistsException
import ru.kreslavski.family.dinnertime.repository.UserRepository
import ru.kreslavski.family.dinnertime.security.JwtTokenProvider

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    fun login(loginRequest: LoginRequest): AuthResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.username,
                loginRequest.password,
            )
        )

        val token = jwtTokenProvider.generateToken(authentication)
        val user = authentication.principal as User

        return AuthResponse(
            token = token,
            username = user.userName,
            roles = user.roles.map { it.name },
        )
    }

    fun register(registerRequest: RegisterRequest): AuthResponse {
        // Check if user already exists
        if (userRepository.findByUsername(registerRequest.username) != null) {
            throw UserAlreadyExistsException("Username already exists: ${registerRequest.username}")
        }

        if (userRepository.findByEmail(registerRequest.email) != null) {
            throw UserAlreadyExistsException("Email already exists: ${registerRequest.email}")
        }

        // Create new user
        val encodedPassword = passwordEncoder.encode(registerRequest.password)
        val user = userRepository.createUser(
            username = registerRequest.username,
            email = registerRequest.email,
            password = encodedPassword,
            firstName = registerRequest.firstName,
            lastName = registerRequest.lastName,
        )

        // Assign default USER role
        val userRole = userRepository.findRoleByName("ROLE_USER")
        if (userRole != null) {
            userRepository.assignRoleToUser(user.id, userRole.id)
        }

        // Generate token for the new user
        val authentication = UsernamePasswordAuthenticationToken(
            user, null, user.authorities
        )
        val token = jwtTokenProvider.generateToken(authentication)

        return AuthResponse(
            token = token,
            username = user.userName,
            roles = listOf("ROLE_USER"),
        )
    }
}
