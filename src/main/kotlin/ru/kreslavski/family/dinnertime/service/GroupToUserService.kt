package ru.kreslavski.family.dinnertime.service

import org.springframework.stereotype.Service
import ru.kreslavski.family.dinnertime.dto.response.GroupToUserDto
import ru.kreslavski.family.dinnertime.exception.UnauthorizedAccessException
import ru.kreslavski.family.dinnertime.exception.UserGroupNotFoundException
import ru.kreslavski.family.dinnertime.jooq.social.tables.records.GroupToUserRecord
import ru.kreslavski.family.dinnertime.repository.GroupToUserRepository
import ru.kreslavski.family.dinnertime.repository.UserGroupRepository
import java.util.UUID

@Service
class GroupToUserService(
    private val groupToUserRepository: GroupToUserRepository,
    private val userGroupRepository: UserGroupRepository
) {

    fun getUserGroups(userId: UUID): List<GroupToUserDto> =
        groupToUserRepository.findByUserId(userId).map { it.toDto() }

    fun getGroupMembers(groupId: UUID, userId: UUID): List<GroupToUserDto> {
        // Check if group exists
        userGroupRepository.findById(groupId) ?: throw UserGroupNotFoundException(groupId)

        // Only group members can see other members
        if (!groupToUserRepository.isUserInGroup(groupId, userId)) {
            throw UnauthorizedAccessException("You can only view members of groups you belong to")
        }

        return groupToUserRepository.findByGroupId(groupId).map { it.toDto() }
    }

    fun getUserRoleInGroup(groupId: UUID, userId: UUID): Boolean {
        // Check if group exists
        userGroupRepository.findById(groupId) ?: throw UserGroupNotFoundException(groupId)

        // Only group members can check their role
        if (!groupToUserRepository.isUserInGroup(groupId, userId)) {
            throw UnauthorizedAccessException("You are not a member of this group")
        }

        return groupToUserRepository.isUserEditor(groupId, userId)
    }
}

fun GroupToUserRecord.toDto(): GroupToUserDto = GroupToUserDto(
    groupId = this.groupId,
    userId = this.userId,
    isEditor = this.isEditor
)
