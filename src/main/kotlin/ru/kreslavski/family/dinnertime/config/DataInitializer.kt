package ru.kreslavski.family.dinnertime.config

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import ru.kreslavski.family.dinnertime.repository.UserRepository

@Component
@ConditionalOnProperty(
    name = ["app.init-admin"],
    havingValue = "true",
    matchIfMissing = false
)
class DataInitializer(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        // Create admin user if it doesn't exist
        val adminUsername = "admin"
        if (userRepository.findByUsername(adminUsername) == null) {
            val adminPassword = passwordEncoder.encode("admin123")
            val adminUser = userRepository.createUser(
                username = adminUsername,
                email = "admin@familydinnertime.com",
                password = adminPassword,
                firstName = "Admin",
                lastName = "User"
            )

            // Assign admin role
            val adminRole = userRepository.findRoleByName("ROLE_ADMIN")
            if (adminRole != null) {
                userRepository.assignRoleToUser(adminUser.id, adminRole.id)
            }

            println("Admin user created with username: $adminUsername and password: admin123")
        }
    }
}
