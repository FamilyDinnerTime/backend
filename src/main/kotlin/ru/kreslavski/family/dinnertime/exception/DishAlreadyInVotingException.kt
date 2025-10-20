package ru.kreslavski.family.dinnertime.exception

import java.util.UUID

class DishAlreadyInVotingException(votingId: UUID, dishId: UUID) : RuntimeException("Dish $dishId is already in voting $votingId")
