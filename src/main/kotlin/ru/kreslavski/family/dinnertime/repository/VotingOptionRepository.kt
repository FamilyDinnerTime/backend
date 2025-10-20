package ru.kreslavski.family.dinnertime.repository

import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import ru.kreslavski.family.dinnertime.jooq.social.Tables
import ru.kreslavski.family.dinnertime.jooq.social.tables.records.VotingOptionRecord
import java.util.UUID

@Repository
class VotingOptionRepository(private val dsl: DSLContext) {

    fun findByMenuId(menuId: UUID): List<VotingOptionRecord> =
        dsl.selectFrom(Tables.VOTING_OPTION)
            .where(Tables.VOTING_OPTION.MENU_ID.eq(menuId))
            .fetchInto(VotingOptionRecord::class.java)

    fun findById(id: UUID): VotingOptionRecord? =
        dsl.selectFrom(Tables.VOTING_OPTION)
            .where(Tables.VOTING_OPTION.GUID.eq(id))
            .fetchOne()

    fun findByMenuIdAndDishId(menuId: UUID, dishId: UUID): VotingOptionRecord? =
        dsl.selectFrom(Tables.VOTING_OPTION)
            .where(Tables.VOTING_OPTION.MENU_ID.eq(menuId))
            .and(Tables.VOTING_OPTION.DISH_ID.eq(dishId))
            .fetchOne()

    fun addDishToVoting(menuId: UUID, dishId: UUID): VotingOptionRecord =
        dsl.insertInto(Tables.VOTING_OPTION)
            .set(Tables.VOTING_OPTION.MENU_ID, menuId)
            .set(Tables.VOTING_OPTION.DISH_ID, dishId)
            .onDuplicateKeyIgnore()
            .returning()
            .fetchOne()!!

    fun removeDishFromVoting(menuId: UUID, dishId: UUID): Int =
        dsl.deleteFrom(Tables.VOTING_OPTION)
            .where(Tables.VOTING_OPTION.MENU_ID.eq(menuId))
            .and(Tables.VOTING_OPTION.DISH_ID.eq(dishId))
            .execute()

    fun removeVotingOption(id: UUID): Int {
        return dsl.deleteFrom(Tables.VOTING_OPTION)
            .where(Tables.VOTING_OPTION.GUID.eq(id))
            .execute()
    }

    fun isDishInVoting(menuId: UUID, dishId: UUID): Boolean {
        return dsl.selectCount()
            .from(Tables.VOTING_OPTION)
            .where(Tables.VOTING_OPTION.MENU_ID.eq(menuId))
            .and(Tables.VOTING_OPTION.DISH_ID.eq(dishId))
            .fetchOne(0, Int::class.java)!! > 0
    }
}
