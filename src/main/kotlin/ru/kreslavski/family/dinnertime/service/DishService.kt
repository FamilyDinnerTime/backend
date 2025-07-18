package ru.kreslavski.family.dinnertime.service

import org.springframework.stereotype.Service
import ru.kreslavski.family.dinnertime.dto.request.AddIngredientToDishRequest
import ru.kreslavski.family.dinnertime.dto.request.AddStepToDishRequest
import ru.kreslavski.family.dinnertime.dto.request.CreateDishRequest
import ru.kreslavski.family.dinnertime.dto.request.RemoveIngredientFromDishRequest
import ru.kreslavski.family.dinnertime.dto.request.RemoveStepFromDishRequest
import ru.kreslavski.family.dinnertime.dto.request.UpdateDishRequest
import ru.kreslavski.family.dinnertime.dto.response.DishDto
import ru.kreslavski.family.dinnertime.exception.DishNotFoundException
import ru.kreslavski.family.dinnertime.exception.UnauthorizedAccessException
import ru.kreslavski.family.dinnertime.jooq.dinner.tables.records.DishRecord
import ru.kreslavski.family.dinnertime.repository.DishRepository
import java.util.UUID

@Service
class DishService(private val dishRepository: DishRepository) {
    fun createDish(request: CreateDishRequest, createdBy: UUID): DishDto {
        val record = dishRepository.createDish(request.name, request.description, request.estimatedTime, createdBy)
        return record.toDto()
    }

    fun findDishesByName(name: String): List<DishDto> {
        return dishRepository.findByNameLike(name).map { it.toDto() }
    }

    fun findDishesByUser(userId: UUID): List<DishDto> {
        return dishRepository.findByCreatedBy(userId).map { it.toDto() }
    }

    fun findDishById(id: UUID): DishDto {
        val record = dishRepository.findById(id) ?: throw DishNotFoundException(id)
        return record.toDto()
    }

    fun updateDish(id: UUID, request: UpdateDishRequest, userId: UUID): DishDto {
        val existingDish = dishRepository.findById(id) ?: throw DishNotFoundException(id)

        if (existingDish.createdBy != userId) {
            throw UnauthorizedAccessException("You can only update dishes you created")
        }

        val record = dishRepository.updateDish(id, request.name, request.description, request.estimatedTime)
            ?: throw DishNotFoundException(id)
        return record.toDto()
    }

    fun deleteDish(id: UUID, userId: UUID) {
        val existingDish = dishRepository.findById(id) ?: throw DishNotFoundException(id)

        if (existingDish.createdBy != userId) {
            throw UnauthorizedAccessException("You can only delete dishes you created")
        }

        val deleted = dishRepository.deleteDish(id)
        if (deleted == 0) {
            throw DishNotFoundException(id)
        }
    }

    fun addIngredientToDish(dishId: UUID, request: AddIngredientToDishRequest, userId: UUID) {
        val existingDish = dishRepository.findById(dishId) ?: throw DishNotFoundException(dishId)

        if (existingDish.createdBy != userId) {
            throw UnauthorizedAccessException("You can only modify dishes you created")
        }

        dishRepository.addIngredientToDish(dishId, request.ingredientGuid, request.quantity, request.quantityType)
    }

    fun removeIngredientFromDish(dishId: UUID, request: RemoveIngredientFromDishRequest, userId: UUID) {
        val existingDish = dishRepository.findById(dishId) ?: throw DishNotFoundException(dishId)

        if (existingDish.createdBy != userId) {
            throw UnauthorizedAccessException("You can only modify dishes you created")
        }

        dishRepository.removeIngredientFromDish(dishId, request.ingredientGuid)
    }

    fun addStepToDish(dishId: UUID, request: AddStepToDishRequest, userId: UUID) {
        val existingDish = dishRepository.findById(dishId) ?: throw DishNotFoundException(dishId)

        if (existingDish.createdBy != userId) {
            throw UnauthorizedAccessException("You can only modify dishes you created")
        }

        dishRepository.addStepToDish(dishId, request.stepGuid, request.stepNumber)
    }

    fun removeStepFromDish(dishId: UUID, request: RemoveStepFromDishRequest, userId: UUID) {
        val existingDish = dishRepository.findById(dishId) ?: throw DishNotFoundException(dishId)

        if (existingDish.createdBy != userId) {
            throw UnauthorizedAccessException("You can only modify dishes you created")
        }

        dishRepository.removeStepFromDish(dishId, request.stepGuid)
    }
}

private fun DishRecord.toDto(): DishDto = DishDto(
    guid = this.guid,
    name = this.name,
    description = this.description,
    estimatedTime = this.estimatedTime,
    createdBy = this.createdBy,
    createdAt = this.createdAt,
)
