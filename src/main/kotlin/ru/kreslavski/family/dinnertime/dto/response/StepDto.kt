package ru.kreslavski.family.dinnertime.dto.response

import java.util.UUID

data class StepDto(
    val guid: UUID,
    val name: String,
    val description: String?,
    val estimatedTime: Int?,
)
