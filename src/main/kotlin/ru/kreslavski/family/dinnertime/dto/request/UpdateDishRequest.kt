package ru.kreslavski.family.dinnertime.dto.request

import jakarta.validation.constraints.PositiveOrZero

data class UpdateDishRequest(
    val name: String? = null,
    val description: String? = null,
    @field:PositiveOrZero(message = "Estimated time must be positive or zero")
    val estimatedTime: Int? = null
)
