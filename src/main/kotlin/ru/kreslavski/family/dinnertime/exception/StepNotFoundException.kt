package ru.kreslavski.family.dinnertime.exception

import java.util.UUID

class StepNotFoundException(id: UUID) : RuntimeException("Step with id $id not found")
