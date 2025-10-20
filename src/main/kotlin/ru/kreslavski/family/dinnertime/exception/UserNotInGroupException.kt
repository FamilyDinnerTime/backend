package ru.kreslavski.family.dinnertime.exception

import java.util.UUID

class UserNotInGroupException(groupId: UUID, userId: UUID) : RuntimeException("User $userId is not a member of group $groupId")
