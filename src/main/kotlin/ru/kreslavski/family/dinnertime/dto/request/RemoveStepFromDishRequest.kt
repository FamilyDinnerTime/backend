package ru.kreslavski.family.dinnertime.dto.request

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class RemoveStepFromDishRequest(
    @field:NotNull(message = "Step GUID is required")
    val stepGuid: UUID,
)
