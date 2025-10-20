package ru.kreslavski.family.dinnertime.repository

import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import ru.kreslavski.family.dinnertime.jooq.social.Tables
import ru.kreslavski.family.dinnertime.jooq.social.tables.records.MenuVotingRecord
import java.util.UUID

@Repository
class MenuVotingRepository(private val dsl: DSLContext) {

    companion object {
        private val USER_GROUPS = Tables.GROUP_TO_USER.`as`("user_groups")
        private val CREATOR_GROUPS = Tables.GROUP_TO_USER.`as`("creator_groups")
    }

    fun findByNameLikeAndUserGroups(name: String, userId: UUID): List<MenuVotingRecord> {
        return buildUserGroupsQuery()
            .where(
                Tables.MENU_VOTING.NAME.likeIgnoreCase("%$name%"),
                createUserGroupsCondition(userId),
            )
            .fetchInto(MenuVotingRecord::class.java)
    }

    fun findByCreatedBy(createdBy: UUID): List<MenuVotingRecord> {
        return dsl.selectFrom(Tables.MENU_VOTING)
            .where(Tables.MENU_VOTING.CREATED_BY.eq(createdBy))
            .fetchInto(MenuVotingRecord::class.java)
    }

    fun findById(id: UUID): MenuVotingRecord? {
        return dsl.selectFrom(Tables.MENU_VOTING)
            .where(Tables.MENU_VOTING.GUID.eq(id))
            .fetchOne()
    }

    fun createMenuVoting(name: String, createdBy: UUID): MenuVotingRecord {
        return dsl.insertInto(Tables.MENU_VOTING)
            .set(Tables.MENU_VOTING.NAME, name)
            .set(Tables.MENU_VOTING.CREATED_BY, createdBy)
            .returning()
            .fetchOne()!!
    }

    fun updateMenuVoting(id: UUID, name: String?): MenuVotingRecord? {
        return dsl.update(Tables.MENU_VOTING)
            .set(Tables.MENU_VOTING.NAME, name)
            .where(Tables.MENU_VOTING.GUID.eq(id))
            .returning()
            .fetchOne()
    }

    fun deleteMenuVoting(id: UUID): Int {
        return dsl.deleteFrom(Tables.MENU_VOTING)
            .where(Tables.MENU_VOTING.GUID.eq(id))
            .execute()
    }

    fun findByUserGroups(userId: UUID): List<MenuVotingRecord> {
        return buildUserGroupsQuery()
            .where(createUserGroupsCondition(userId))
            .orderBy(Tables.MENU_VOTING.CREATED_AT.desc())
            .fetchInto(MenuVotingRecord::class.java)
    }

    private fun buildUserGroupsQuery() =
        dsl.selectDistinct(Tables.MENU_VOTING.asterisk())
            .from(Tables.MENU_VOTING)

    private fun createUserGroupsCondition(userId: UUID) =
        Tables.MENU_VOTING.CREATED_BY.eq(userId) // User's own votings
            .or(
                DSL.exists(
                    DSL.selectOne()
                        .from(USER_GROUPS)
                        .innerJoin(CREATOR_GROUPS)
                        .on(USER_GROUPS.GROUP_ID.eq(CREATOR_GROUPS.GROUP_ID))
                        .where(USER_GROUPS.USER_ID.eq(userId))
                        .and(CREATOR_GROUPS.USER_ID.eq(Tables.MENU_VOTING.CREATED_BY))
                )
            )
}
