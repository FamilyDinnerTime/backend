package ru.kreslavski.family.dinnertime.dto.response

import java.time.OffsetDateTime
import java.util.UUID

data class UserGroupDto(
    val guid: UUID,
    val name: String,
    val description: String?,
    val createdBy: UUID,
    val createdAt: OffsetDateTime,
)
