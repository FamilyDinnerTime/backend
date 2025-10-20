package ru.kreslavski.family.dinnertime.dto.response

import java.util.UUID

data class VotingOptionDto(
    val guid: UUID,
    val menuId: UUID,
    val dishId: UUID,
)
