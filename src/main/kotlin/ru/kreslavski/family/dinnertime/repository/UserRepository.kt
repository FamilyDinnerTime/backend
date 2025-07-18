package ru.kreslavski.family.dinnertime.repository

import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import ru.kreslavski.family.dinnertime.entity.Role
import ru.kreslavski.family.dinnertime.entity.User
import ru.kreslavski.family.dinnertime.jooq.auth.Tables
import ru.kreslavski.family.dinnertime.jooq.auth.tables.records.RolesRecord
import ru.kreslavski.family.dinnertime.jooq.auth.tables.records.UsersRecord
import java.time.LocalDateTime
import java.util.UUID

@Repository
class UserRepository(private val dsl: DSLContext) {

    fun findByUsername(username: String): User? {
        val userRecord = dsl.selectFrom(Tables.USERS)
            .where(Tables.USERS.USERNAME.eq(username))
            .fetchOne() ?: return null

        val roles = findUserRoles(userRecord.id)
        return userRecord.toUser(roles)
    }

    fun findByEmail(email: String): User? {
        val userRecord = dsl.selectFrom(Tables.USERS)
            .where(Tables.USERS.EMAIL.eq(email))
            .fetchOne() ?: return null

        val roles = findUserRoles(userRecord.id)
        return userRecord.toUser(roles)
    }

    fun createUser(
        username: String,
        email: String,
        password: String,
        firstName: String?,
        lastName: String?
    ): User {
        val now = LocalDateTime.now()
        val userRecord = dsl.insertInto(Tables.USERS)
            .set(Tables.USERS.USERNAME, username)
            .set(Tables.USERS.EMAIL, email)
            .set(Tables.USERS.PASSWORD, password)
            .set(Tables.USERS.FIRST_NAME, firstName)
            .set(Tables.USERS.LAST_NAME, lastName)
            .set(Tables.USERS.CREATED_AT, now)
            .set(Tables.USERS.UPDATED_AT, now)
            .returning()
            .fetchOne()!!

        return userRecord.toUser(emptyList())
    }

    fun assignRoleToUser(userId: UUID, roleId: UUID) {
        dsl.insertInto(Tables.USER_ROLES)
            .set(Tables.USER_ROLES.USER_ID, userId)
            .set(Tables.USER_ROLES.ROLE_ID, roleId)
            .onDuplicateKeyIgnore()
            .execute()
    }

    fun findRoleByName(roleName: String): Role? {
        val roleRecord = dsl.selectFrom(Tables.ROLES)
            .where(Tables.ROLES.NAME.eq(roleName))
            .fetchOne() ?: return null

        return roleRecord.toRole()
    }

    private fun findUserRoles(userId: UUID): List<Role> {
        return dsl.select(Tables.ROLES.asterisk())
            .from(Tables.ROLES)
            .join(Tables.USER_ROLES)
            .on(Tables.ROLES.ID.eq(Tables.USER_ROLES.ROLE_ID))
            .where(Tables.USER_ROLES.USER_ID.eq(userId))
            .fetchInto(RolesRecord::class.java)
            .map { it.toRole() }
    }

    private fun UsersRecord.toUser(roles: List<Role>): User = User(
        id = this.id,
        userName = this.username,
        email = this.email,
        passWord = this.password,
        firstName = this.firstName,
        lastName = this.lastName,
        enabled = this.enabled,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        roles = roles,
    )

    private fun RolesRecord.toRole(): Role = Role(
        id = this.id,
        name = this.name,
        description = this.description,
        createdAt = this.createdAt,
    )
}
