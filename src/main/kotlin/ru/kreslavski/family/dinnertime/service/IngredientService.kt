package ru.kreslavski.family.dinnertime.service

import org.springframework.stereotype.Service
import ru.kreslavski.family.dinnertime.dto.request.CreateIngredientRequest
import ru.kreslavski.family.dinnertime.dto.request.UpdateIngredientRequest
import ru.kreslavski.family.dinnertime.dto.response.IngredientDto
import ru.kreslavski.family.dinnertime.exception.IngredientNotFoundException
import ru.kreslavski.family.dinnertime.jooq.dinner.tables.records.IngredientRecord
import ru.kreslavski.family.dinnertime.repository.IngredientRepository
import java.util.UUID

@Service
class IngredientService(private val ingredientRepository: IngredientRepository) {

    fun findIngredientsByName(name: String): List<IngredientDto> {
        return ingredientRepository.findByNameLike(name).map { it.toDto() }
    }

    fun findIngredientById(id: UUID): IngredientDto {
        val record = ingredientRepository.findById(id) ?: throw IngredientNotFoundException(id)
        return record.toDto()
    }

    fun createIngredient(request: CreateIngredientRequest): IngredientDto {
        val record = ingredientRepository.createIngredient(request.name, request.description)
        return record.toDto()
    }

    fun updateIngredient(id: UUID, request: UpdateIngredientRequest): IngredientDto {
        val record = ingredientRepository.updateIngredient(id, request.name, request.description)
            ?: throw IngredientNotFoundException(id)
        return record.toDto()
    }

    fun deleteIngredient(id: UUID) {
        val deleted = ingredientRepository.deleteIngredient(id)
        if (deleted == 0) {
            throw IngredientNotFoundException(id)
        }
    }
}

private fun IngredientRecord.toDto(): IngredientDto = IngredientDto(
    guid = this.guid,
    name = this.name,
    description = this.description,
)
