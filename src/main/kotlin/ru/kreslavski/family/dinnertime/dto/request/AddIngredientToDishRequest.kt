package ru.kreslavski.family.dinnertime.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.util.UUID

data class AddIngredientToDishRequest(
    @field:NotNull(message = "Ingredient GUID is required")
    val ingredientGuid: UUID,
    @field:Positive(message = "Quantity must be positive")
    val quantity: Int,
    @field:NotBlank(message = "Quantity type is required")
    val quantityType: String
)
