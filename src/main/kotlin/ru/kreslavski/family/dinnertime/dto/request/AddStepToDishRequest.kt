package ru.kreslavski.family.dinnertime.dto.request

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.util.UUID

data class AddStepToDishRequest(
    @field:NotNull(message = "Step GUID is required")
    val stepGuid: UUID,
    @field:Positive(message = "Step number must be positive")
    val stepNumber: Int,
)
