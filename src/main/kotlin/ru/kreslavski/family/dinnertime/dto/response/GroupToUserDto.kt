package ru.kreslavski.family.dinnertime.dto.response

import java.util.UUID

data class GroupToUserDto(
    val groupId: UUID,
    val userId: UUID,
    val isEditor: Boolean,
)
