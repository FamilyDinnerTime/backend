package ru.kreslavski.family.dinnertime.dto.request

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class RemoveDishFromVotingRequest(
    @field:NotNull(message = "Dish ID is required")
    val dishId: UUID,
)
