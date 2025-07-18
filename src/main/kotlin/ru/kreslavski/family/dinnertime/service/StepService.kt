package ru.kreslavski.family.dinnertime.service

import org.springframework.stereotype.Service
import ru.kreslavski.family.dinnertime.dto.request.CreateStepRequest
import ru.kreslavski.family.dinnertime.dto.response.StepDto
import ru.kreslavski.family.dinnertime.exception.StepInUseException
import ru.kreslavski.family.dinnertime.exception.StepNotFoundException
import ru.kreslavski.family.dinnertime.jooq.dinner.tables.records.StepRecord
import ru.kreslavski.family.dinnertime.repository.StepRepository
import java.util.UUID

@Service
class StepService(private val stepRepository: StepRepository) {
    fun createStep(request: CreateStepRequest): StepDto {
        val record = stepRepository.createStep(request.name, request.description, request.estimatedTime)
        return record.toDto()
    }

    fun findStepById(id: UUID): StepDto {
        val record = stepRepository.findById(id) ?: throw StepNotFoundException(id)
        return record.toDto()
    }

    fun findStepsByName(name: String): List<StepDto> {
        return stepRepository.findByNameLike(name).map { it.toDto() }
    }

    fun deleteStep(id: UUID) {
        if (stepRepository.isStepUsed(id)) {
            throw StepInUseException(id)
        }
        val deleted = stepRepository.deleteStep(id)
        if (deleted == 0) {
            throw StepNotFoundException(id)
        }
    }
}

private fun StepRecord.toDto(): StepDto = StepDto(
    guid = this.guid,
    name = this.name,
    description = this.description,
    estimatedTime = this.estimatedTime,
)
