package ru.kreslavski.family.dinnertime.repository

import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import ru.kreslavski.family.dinnertime.jooq.social.Tables
import ru.kreslavski.family.dinnertime.jooq.social.tables.records.UserGroupRecord
import java.util.UUID

@Repository
class UserGroupRepository(private val dsl: DSLContext) {

    fun findByNameLike(name: String): List<UserGroupRecord> =
        dsl.selectFrom(Tables.USER_GROUP)
            .where(Tables.USER_GROUP.NAME.likeIgnoreCase("%$name%"))
            .fetchInto(UserGroupRecord::class.java)

    fun findByCreatedBy(createdBy: UUID): List<UserGroupRecord> =
        dsl.selectFrom(Tables.USER_GROUP)
            .where(Tables.USER_GROUP.CREATED_BY.eq(createdBy))
            .fetchInto(UserGroupRecord::class.java)

    fun findById(id: UUID): UserGroupRecord? =
        dsl.selectFrom(Tables.USER_GROUP)
            .where(Tables.USER_GROUP.GUID.eq(id))
            .fetchOne()

    fun createUserGroup(name: String, description: String?, createdBy: UUID): UserGroupRecord =
        dsl.insertInto(Tables.USER_GROUP)
            .set(Tables.USER_GROUP.NAME, name)
            .set(Tables.USER_GROUP.DESCRIPTION, description)
            .set(Tables.USER_GROUP.CREATED_BY, createdBy)
            .returning()
            .fetchOne()!!

    fun updateUserGroup(id: UUID, name: String?, description: String?): UserGroupRecord? =
        dsl.update(Tables.USER_GROUP)
            .set(Tables.USER_GROUP.NAME, name)
            .set(Tables.USER_GROUP.DESCRIPTION, description)
            .where(Tables.USER_GROUP.GUID.eq(id))
            .returning()
            .fetchOne()

    fun deleteUserGroup(id: UUID): Int =
        dsl.deleteFrom(Tables.USER_GROUP)
            .where(Tables.USER_GROUP.GUID.eq(id))
            .execute()

    fun findGroupsByUser(userId: UUID): List<UserGroupRecord> =
        dsl.select(Tables.USER_GROUP.asterisk())
            .from(Tables.USER_GROUP)
            .innerJoin(Tables.GROUP_TO_USER).on(Tables.USER_GROUP.GUID.eq(Tables.GROUP_TO_USER.GROUP_ID))
            .where(Tables.GROUP_TO_USER.USER_ID.eq(userId))
            .fetchInto(UserGroupRecord::class.java)
}
