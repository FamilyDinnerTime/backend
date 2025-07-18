package ru.kreslavski.family.dinnertime.repository

import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import ru.kreslavski.family.dinnertime.jooq.dinner.Tables
import ru.kreslavski.family.dinnertime.jooq.dinner.tables.records.IngredientRecord
import java.util.UUID

@Repository
class IngredientRepository(private val dsl: DSLContext) {
    fun findByNameLike(name: String): List<IngredientRecord> {
        return dsl.selectFrom(Tables.INGREDIENT)
            .where(Tables.INGREDIENT.NAME.likeIgnoreCase("%$name%"))
            .fetchInto(IngredientRecord::class.java)
    }

    fun findById(id: UUID): IngredientRecord? {
        return dsl.selectFrom(Tables.INGREDIENT)
            .where(Tables.INGREDIENT.GUID.eq(id))
            .fetchOne()
    }

    fun createIngredient(name: String, description: String?): IngredientRecord {
        return dsl.insertInto(Tables.INGREDIENT)
            .set(Tables.INGREDIENT.NAME, name)
            .set(Tables.INGREDIENT.DESCRIPTION, description)
            .returning()
            .fetchOne()!!
    }

    fun updateIngredient(id: UUID, name: String?, description: String?): IngredientRecord? {
        val update = dsl.update(Tables.INGREDIENT)
            .set(Tables.INGREDIENT.NAME, name)
            .set(Tables.INGREDIENT.DESCRIPTION, description)
            .where(Tables.INGREDIENT.GUID.eq(id))
            .returning()
            .fetchOne()
        return update
    }

    fun deleteIngredient(id: UUID): Int {
        return dsl.deleteFrom(Tables.INGREDIENT)
            .where(Tables.INGREDIENT.GUID.eq(id))
            .execute()
    }
}
