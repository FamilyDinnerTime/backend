package ru.kreslavski.family.dinnertime.repository

import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import ru.kreslavski.family.dinnertime.jooq.dinner.Tables
import ru.kreslavski.family.dinnertime.jooq.dinner.tables.records.StepRecord
import java.util.UUID

@Repository
class StepRepository(private val dsl: DSLContext) {
    fun createStep(name: String, description: String?, estimatedTime: Int?): StepRecord {
        return dsl.insertInto(Tables.STEP)
            .set(Tables.STEP.NAME, name)
            .set(Tables.STEP.DESCRIPTION, description)
            .set(Tables.STEP.ESTIMATED_TIME, estimatedTime)
            .returning()
            .fetchOne()!!
    }

    fun findById(id: UUID): StepRecord? {
        return dsl.selectFrom(Tables.STEP)
            .where(Tables.STEP.GUID.eq(id))
            .fetchOne()
    }

    fun findByNameLike(name: String): List<StepRecord> {
        return dsl.selectFrom(Tables.STEP)
            .where(Tables.STEP.NAME.likeIgnoreCase("%$name%"))
            .fetchInto(StepRecord::class.java)
    }

    fun deleteStep(id: UUID): Int {
        return dsl.deleteFrom(Tables.STEP)
            .where(Tables.STEP.GUID.eq(id))
            .execute()
    }

    fun isStepUsed(stepGuid: UUID): Boolean {
        val count = dsl.selectCount()
            .from(Tables.DISH_TO_STEP)
            .where(Tables.DISH_TO_STEP.STEP_GUID.eq(stepGuid))
            .fetchOne(0, Int::class.java) ?: 0
        return count > 0
    }
}
