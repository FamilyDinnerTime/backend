package ru.kreslavski.family.dinnertime.exception

import java.util.UUID

class StepInUseException(id: UUID) : RuntimeException("Step with id $id is used by dishes and cannot be deleted")
