package ru.kreslavski.family.dinnertime.exception

import java.util.UUID

class UserAlreadyInGroupException(groupId: UUID, userId: UUID) : RuntimeException("User $userId is already a member of group $groupId")
