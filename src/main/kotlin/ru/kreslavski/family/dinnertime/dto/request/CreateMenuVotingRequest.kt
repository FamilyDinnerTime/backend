package ru.kreslavski.family.dinnertime.dto.request

import jakarta.validation.constraints.NotBlank

data class CreateMenuVotingRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,
)
