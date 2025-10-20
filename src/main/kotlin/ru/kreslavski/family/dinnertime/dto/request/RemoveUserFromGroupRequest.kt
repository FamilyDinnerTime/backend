package ru.kreslavski.family.dinnertime.dto.request

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class RemoveUserFromGroupRequest(
    @field:NotNull(message = "User ID is required")
    val userId: UUID,
)
