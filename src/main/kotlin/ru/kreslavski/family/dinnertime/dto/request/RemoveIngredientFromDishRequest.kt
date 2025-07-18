package ru.kreslavski.family.dinnertime.dto.request

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class RemoveIngredientFromDishRequest(
    @field:NotNull(message = "Ingredient GUID is required")
    val ingredientGuid: UUID
)
