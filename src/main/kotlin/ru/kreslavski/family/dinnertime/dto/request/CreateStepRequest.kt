package ru.kreslavski.family.dinnertime.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero

data class CreateStepRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,
    val description: String? = null,
    @field:PositiveOrZero(message = "Estimated time must be positive or zero")
    val estimatedTime: Int? = null,
)
