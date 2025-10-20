package ru.kreslavski.family.dinnertime.exception

import java.util.UUID

class MenuVotingNotFoundException(id: UUID) : RuntimeException("Menu voting with ID $id not found")
