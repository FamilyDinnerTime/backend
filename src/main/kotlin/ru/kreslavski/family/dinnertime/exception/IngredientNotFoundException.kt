package ru.kreslavski.family.dinnertime.exception

import java.util.UUID

class IngredientNotFoundException(id: UUID) : RuntimeException("Ingredient with id $id not found")
