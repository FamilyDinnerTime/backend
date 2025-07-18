package ru.kreslavski.family.dinnertime.dto.response

import java.time.LocalDateTime
import java.util.UUID

data class DishDto(
    val guid: UUID,
    val name: String,
    val description: String?,
    val estimatedTime: Int?,
    val createdBy: UUID?,
    val createdAt: LocalDateTime?
)
