package ru.kreslavski.family.dinnertime.exception

import java.util.UUID

class DishNotFoundException(id: UUID) : RuntimeException("Dish with id $id not found")
