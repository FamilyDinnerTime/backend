package ru.kreslavski.family.dinnertime.exception

import java.util.UUID

class UserGroupNotFoundException(id: UUID) : RuntimeException("User group with ID $id not found")
