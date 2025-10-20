package ru.kreslavski.family.dinnertime.service

import org.springframework.stereotype.Service
import ru.kreslavski.family.dinnertime.dto.request.AddUserToGroupRequest
import ru.kreslavski.family.dinnertime.dto.request.CreateUserGroupRequest
import ru.kreslavski.family.dinnertime.dto.request.RemoveUserFromGroupRequest
import ru.kreslavski.family.dinnertime.dto.request.UpdateUserGroupRequest
import ru.kreslavski.family.dinnertime.dto.response.GroupToUserDto
import ru.kreslavski.family.dinnertime.dto.response.UserGroupDto
import ru.kreslavski.family.dinnertime.exception.UnauthorizedAccessException
import ru.kreslavski.family.dinnertime.exception.UserAlreadyInGroupException
import ru.kreslavski.family.dinnertime.exception.UserGroupNotFoundException
import ru.kreslavski.family.dinnertime.exception.UserNotInGroupException
import ru.kreslavski.family.dinnertime.jooq.social.tables.records.UserGroupRecord
import ru.kreslavski.family.dinnertime.repository.GroupToUserRepository
import ru.kreslavski.family.dinnertime.repository.UserGroupRepository
import java.util.UUID

@Service
class UserGroupService(
    private val userGroupRepository: UserGroupRepository,
    private val groupToUserRepository: GroupToUserRepository,
) {

    fun createUserGroup(request: CreateUserGroupRequest, createdBy: UUID): UserGroupDto {
        val record = userGroupRepository.createUserGroup(request.name, request.description, createdBy)
        return record.toDto()
    }

    fun findUserGroupsByName(name: String): List<UserGroupDto> {
        return userGroupRepository.findByNameLike(name).map { it.toDto() }
    }

    fun findUserGroupsByUser(userId: UUID): List<UserGroupDto> {
        return userGroupRepository.findGroupsByUser(userId).map { it.toDto() }
    }

    fun findUserGroupsCreatedByUser(userId: UUID): List<UserGroupDto> {
        return userGroupRepository.findByCreatedBy(userId).map { it.toDto() }
    }

    fun findUserGroupById(id: UUID): UserGroupDto {
        val record = userGroupRepository.findById(id) ?: throw UserGroupNotFoundException(id)
        return record.toDto()
    }

    fun updateUserGroup(id: UUID, request: UpdateUserGroupRequest, userId: UUID): UserGroupDto {
        val existingGroup = userGroupRepository.findById(id) ?: throw UserGroupNotFoundException(id)

        // Only creator or editors can update the group
        if (existingGroup.createdBy != userId && !groupToUserRepository.isUserEditor(id, userId)) {
            throw UnauthorizedAccessException("You can only update groups you created or are an editor of")
        }

        val record = userGroupRepository.updateUserGroup(id, request.name, request.description)
            ?: throw UserGroupNotFoundException(id)
        return record.toDto()
    }

    fun deleteUserGroup(id: UUID, userId: UUID) {
        val existingGroup = userGroupRepository.findById(id) ?: throw UserGroupNotFoundException(id)

        // Only creator can delete the group
        if (existingGroup.createdBy != userId) {
            throw UnauthorizedAccessException("You can only delete groups you created")
        }

        val deleted = userGroupRepository.deleteUserGroup(id)
        if (deleted == 0) {
            throw UserGroupNotFoundException(id)
        }
    }

    fun addUserToGroup(groupId: UUID, request: AddUserToGroupRequest, userId: UUID): GroupToUserDto {
        val existingGroup = userGroupRepository.findById(groupId) ?: throw UserGroupNotFoundException(groupId)

        // Only creator or editors can add users to the group
        if (existingGroup.createdBy != userId && !groupToUserRepository.isUserEditor(groupId, userId)) {
            throw UnauthorizedAccessException("You can only add users to groups you created or are an editor of")
        }

        // Check if user is already in the group
        if (groupToUserRepository.isUserInGroup(groupId, request.userId)) {
            throw UserAlreadyInGroupException(groupId, request.userId)
        }

        val record = groupToUserRepository.addUserToGroup(groupId, request.userId, request.isEditor)
        return record.toDto()
    }

    fun removeUserFromGroup(groupId: UUID, request: RemoveUserFromGroupRequest, userId: UUID) {
        val existingGroup = userGroupRepository.findById(groupId) ?: throw UserGroupNotFoundException(groupId)

        // Only creator or editors can remove users from the group
        if (existingGroup.createdBy != userId && !groupToUserRepository.isUserEditor(groupId, userId)) {
            throw UnauthorizedAccessException("You can only remove users from groups you created or are an editor of")
        }

        // Check if user is in the group
        if (!groupToUserRepository.isUserInGroup(groupId, request.userId)) {
            throw UserNotInGroupException(groupId, request.userId)
        }

        val deleted = groupToUserRepository.removeUserFromGroup(groupId, request.userId)
        if (deleted == 0) {
            throw UserNotInGroupException(groupId, request.userId)
        }
    }

    fun getGroupMembers(groupId: UUID, userId: UUID): List<GroupToUserDto> {
        // Check if group exists
        userGroupRepository.findById(groupId) ?: throw UserGroupNotFoundException(groupId)

        // Only group members can see other members
        if (!groupToUserRepository.isUserInGroup(groupId, userId)) {
            throw UnauthorizedAccessException("You can only view members of groups you belong to")
        }

        return groupToUserRepository.findByGroupId(groupId).map { it.toDto() }
    }

    fun updateUserRole(groupId: UUID, targetUserId: UUID, isEditor: Boolean, userId: UUID): GroupToUserDto {
        val existingGroup = userGroupRepository.findById(groupId) ?: throw UserGroupNotFoundException(groupId)

        // Only creator or editors can change user roles
        if (existingGroup.createdBy != userId && !groupToUserRepository.isUserEditor(groupId, userId)) {
            throw UnauthorizedAccessException("You can only change roles in groups you created or are an editor of")
        }

        // Check if target user is in the group
        if (!groupToUserRepository.isUserInGroup(groupId, targetUserId)) {
            throw UserNotInGroupException(groupId, targetUserId)
        }

        val record = groupToUserRepository.updateUserRole(groupId, targetUserId, isEditor)
            ?: throw UserNotInGroupException(groupId, targetUserId)
        return record.toDto()
    }

    private fun UserGroupRecord.toDto(): UserGroupDto = UserGroupDto(
        guid = this.guid,
        name = this.name,
        description = this.description,
        createdBy = this.createdBy,
        createdAt = this.createdAt
    )
}
