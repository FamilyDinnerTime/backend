package ru.kreslavski.family.dinnertime.dto.response

import java.time.OffsetDateTime
import java.util.UUID

data class MenuVotingDto(
    val guid: UUID,
    val name: String,
    val createdBy: UUID,
    val createdAt: OffsetDateTime,
)
