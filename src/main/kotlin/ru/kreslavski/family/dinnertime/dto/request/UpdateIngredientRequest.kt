package ru.kreslavski.family.dinnertime.dto.request

import jakarta.validation.constraints.Size

data class UpdateIngredientRequest(
    @field:Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    val name: String? = null,

    val description: String? = null,
)
