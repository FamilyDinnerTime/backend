package ru.kreslavski.family.dinnertime.repository

import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import ru.kreslavski.family.dinnertime.jooq.social.Tables
import ru.kreslavski.family.dinnertime.jooq.social.tables.records.GroupToUserRecord
import java.util.UUID

@Repository
class GroupToUserRepository(private val dsl: DSLContext) {

    fun findByGroupId(groupId: UUID): List<GroupToUserRecord> =
        dsl.selectFrom(Tables.GROUP_TO_USER)
            .where(Tables.GROUP_TO_USER.GROUP_ID.eq(groupId))
            .fetchInto(GroupToUserRecord::class.java)

    fun findByUserId(userId: UUID): List<GroupToUserRecord> =
        dsl.selectFrom(Tables.GROUP_TO_USER)
            .where(Tables.GROUP_TO_USER.USER_ID.eq(userId))
            .fetchInto(GroupToUserRecord::class.java)

    fun findByGroupIdAndUserId(groupId: UUID, userId: UUID): GroupToUserRecord? =
        dsl.selectFrom(Tables.GROUP_TO_USER)
            .where(Tables.GROUP_TO_USER.GROUP_ID.eq(groupId))
            .and(Tables.GROUP_TO_USER.USER_ID.eq(userId))
            .fetchOne()

    fun addUserToGroup(groupId: UUID, userId: UUID, isEditor: Boolean): GroupToUserRecord =
        dsl.insertInto(Tables.GROUP_TO_USER)
            .set(Tables.GROUP_TO_USER.GROUP_ID, groupId)
            .set(Tables.GROUP_TO_USER.USER_ID, userId)
            .set(Tables.GROUP_TO_USER.IS_EDITOR, isEditor)
            .onDuplicateKeyUpdate()
            .set(Tables.GROUP_TO_USER.IS_EDITOR, isEditor)
            .returning()
            .fetchOne()!!

    fun removeUserFromGroup(groupId: UUID, userId: UUID): Int =
        dsl.deleteFrom(Tables.GROUP_TO_USER)
            .where(Tables.GROUP_TO_USER.GROUP_ID.eq(groupId))
            .and(Tables.GROUP_TO_USER.USER_ID.eq(userId))
            .execute()

    fun updateUserRole(groupId: UUID, userId: UUID, isEditor: Boolean): GroupToUserRecord? =
        dsl.update(Tables.GROUP_TO_USER)
            .set(Tables.GROUP_TO_USER.IS_EDITOR, isEditor)
            .where(Tables.GROUP_TO_USER.GROUP_ID.eq(groupId))
            .and(Tables.GROUP_TO_USER.USER_ID.eq(userId))
            .returning()
            .fetchOne()

    fun isUserInGroup(groupId: UUID, userId: UUID): Boolean =
        dsl.selectCount()
            .from(Tables.GROUP_TO_USER)
            .where(Tables.GROUP_TO_USER.GROUP_ID.eq(groupId))
            .and(Tables.GROUP_TO_USER.USER_ID.eq(userId))
            .fetchOne(0, Int::class.java)!! > 0

    fun isUserEditor(groupId: UUID, userId: UUID): Boolean =
        dsl.selectFrom(Tables.GROUP_TO_USER)
            .where(Tables.GROUP_TO_USER.GROUP_ID.eq(groupId))
            .and(Tables.GROUP_TO_USER.USER_ID.eq(userId))
            .and(Tables.GROUP_TO_USER.IS_EDITOR.eq(true))
            .fetchOne() != null
}
