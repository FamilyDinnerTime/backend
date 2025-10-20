package ru.kreslavski.family.dinnertime.dto.request

import jakarta.validation.constraints.NotBlank

data class CreateUserGroupRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,
    val description: String? = null,
)
