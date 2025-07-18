package ru.kreslavski.family.dinnertime.repository

import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import ru.kreslavski.family.dinnertime.jooq.dinner.Tables
import ru.kreslavski.family.dinnertime.jooq.dinner.tables.records.DishRecord
import java.util.UUID

@Repository
class DishRepository(private val dsl: DSLContext) {
    fun findByNameLike(name: String): List<DishRecord> {
        return dsl.selectFrom(Tables.DISH)
            .where(Tables.DISH.NAME.likeIgnoreCase("%$name%"))
            .fetchInto(DishRecord::class.java)
    }

    fun findByCreatedBy(createdBy: UUID): List<DishRecord> {
        return dsl.selectFrom(Tables.DISH)
            .where(Tables.DISH.CREATED_BY.eq(createdBy))
            .fetchInto(DishRecord::class.java)
    }

    fun findById(id: UUID): DishRecord? {
        return dsl.selectFrom(Tables.DISH)
            .where(Tables.DISH.GUID.eq(id))
            .fetchOne()
    }

    fun createDish(name: String, description: String?, estimatedTime: Int?, createdBy: UUID): DishRecord {
        return dsl.insertInto(Tables.DISH)
            .set(Tables.DISH.NAME, name)
            .set(Tables.DISH.DESCRIPTION, description)
            .set(Tables.DISH.ESTIMATED_TIME, estimatedTime)
            .set(Tables.DISH.CREATED_BY, createdBy)
            .returning()
            .fetchOne()!!
    }

    fun updateDish(id: UUID, name: String?, description: String?, estimatedTime: Int?): DishRecord? {
        val update = dsl.update(Tables.DISH)
            .set(Tables.DISH.NAME, name)
            .set(Tables.DISH.DESCRIPTION, description)
            .set(Tables.DISH.ESTIMATED_TIME, estimatedTime)
            .where(Tables.DISH.GUID.eq(id))
            .returning()
            .fetchOne()
        return update
    }

    fun deleteDish(id: UUID): Int {
        return dsl.deleteFrom(Tables.DISH)
            .where(Tables.DISH.GUID.eq(id))
            .execute()
    }

    fun addStepToDish(dishId: UUID, stepGuid: UUID, stepNumber: Int) {
        dsl.insertInto(Tables.DISH_TO_STEP)
            .set(Tables.DISH_TO_STEP.DISH_GUID, dishId)
            .set(Tables.DISH_TO_STEP.STEP_GUID, stepGuid)
            .set(Tables.DISH_TO_STEP.STEP_NUMBER, stepNumber)
            .onDuplicateKeyUpdate()
            .set(Tables.DISH_TO_STEP.STEP_NUMBER, stepNumber)
            .execute()
    }

    fun removeStepFromDish(dishId: UUID, stepGuid: UUID) {
        dsl.deleteFrom(Tables.DISH_TO_STEP)
            .where(Tables.DISH_TO_STEP.DISH_GUID.eq(dishId))
            .and(Tables.DISH_TO_STEP.STEP_GUID.eq(stepGuid))
            .execute()
    }

    fun addIngredientToDish(dishId: UUID, ingredientGuid: UUID, quantity: Int, quantityType: String) {
        dsl.insertInto(Tables.DISH_TO_INGREDIENT)
            .set(Tables.DISH_TO_INGREDIENT.DISH_GUID, dishId)
            .set(Tables.DISH_TO_INGREDIENT.INGREDIENT_GUID, ingredientGuid)
            .set(Tables.DISH_TO_INGREDIENT.QUANTITY, quantity)
            .set(Tables.DISH_TO_INGREDIENT.QUANTITY_TYPE, quantityType)
            .onDuplicateKeyUpdate()
            .set(Tables.DISH_TO_INGREDIENT.QUANTITY, quantity)
            .set(Tables.DISH_TO_INGREDIENT.QUANTITY_TYPE, quantityType)
            .execute()
    }

    fun removeIngredientFromDish(dishId: UUID, ingredientGuid: UUID) {
        dsl.deleteFrom(Tables.DISH_TO_INGREDIENT)
            .where(Tables.DISH_TO_INGREDIENT.DISH_GUID.eq(dishId))
            .and(Tables.DISH_TO_INGREDIENT.INGREDIENT_GUID.eq(ingredientGuid))
            .execute()
    }
}
