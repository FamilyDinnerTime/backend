package ru.kreslavski.family.dinnertime.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.UUID

data class User(
    val id: UUID,
    val userName: String,
    val email: String,
    val passWord: String,
    val firstName: String?,
    val lastName: String?,
    val enabled: Boolean = true,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val roles: List<Role> = emptyList()
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return roles.map { SimpleGrantedAuthority(it.name) }
    }

    override fun getPassword(): String = passWord

    override fun getUsername(): String = userName

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = enabled
}

data class Role(
    val id: UUID,
    val name: String,
    val description: String?,
    val createdAt: LocalDateTime
)
