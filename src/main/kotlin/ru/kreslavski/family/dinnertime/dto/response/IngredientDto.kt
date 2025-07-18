package ru.kreslavski.family.dinnertime.dto.response

import java.util.UUID

data class IngredientDto(
    val guid: UUID,
    val name: String,
    val description: String?,
)
