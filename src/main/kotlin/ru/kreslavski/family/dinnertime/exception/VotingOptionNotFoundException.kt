package ru.kreslavski.family.dinnertime.exception

import java.util.UUID

class VotingOptionNotFoundException(id: UUID) : RuntimeException("Voting option with ID $id not found")
