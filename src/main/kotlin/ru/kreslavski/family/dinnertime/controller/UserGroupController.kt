package ru.kreslavski.family.dinnertime.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.kreslavski.family.dinnertime.dto.request.AddUserToGroupRequest
import ru.kreslavski.family.dinnertime.dto.request.CreateUserGroupRequest
import ru.kreslavski.family.dinnertime.dto.request.RemoveUserFromGroupRequest
import ru.kreslavski.family.dinnertime.dto.request.UpdateUserGroupRequest
import ru.kreslavski.family.dinnertime.dto.response.GroupToUserDto
import ru.kreslavski.family.dinnertime.dto.response.UserGroupDto
import ru.kreslavski.family.dinnertime.entity.User
import ru.kreslavski.family.dinnertime.service.UserGroupService
import java.util.UUID

@RestController
@RequestMapping("/user-groups")
class UserGroupController @Autowired constructor(
    private val userGroupService: UserGroupService,
) {

    @PostMapping
    @Operation(summary = "Create user group", description = "Create a new user group for the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User group created successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    fun createUserGroup(
        @RequestBody
        request: CreateUserGroupRequest,
        authentication: Authentication
    ): UserGroupDto {
        val user = authentication.principal as User
        return userGroupService.createUserGroup(request, user.id)
    }

    @GetMapping("/search")
    @Operation(summary = "Search user groups", description = "Search for user groups by name.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User groups found")
        ]
    )
    fun searchUserGroupsByName(
        @RequestParam
        name: String
    ): List<UserGroupDto> =
        userGroupService.findUserGroupsByName(name)

    @GetMapping("/my")
    @Operation(summary = "Get my user groups", description = "Get all user groups the authenticated user belongs to.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User groups returned"),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    fun getMyUserGroups(
        authentication: Authentication
    ): List<UserGroupDto> {
        val user = authentication.principal as User
        return userGroupService.findUserGroupsByUser(user.id)
    }

    @GetMapping("/created")
    @Operation(summary = "Get created user groups", description = "Get all user groups created by the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User groups returned"),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    fun getCreatedUserGroups(
        authentication: Authentication
    ): List<UserGroupDto> {
        val user = authentication.principal as User
        return userGroupService.findUserGroupsCreatedByUser(user.id)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user group by ID", description = "Get a user group by its ID.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User group found"),
            ApiResponse(responseCode = "404", description = "User group not found")
        ]
    )
    fun getUserGroupById(
        @PathVariable id: UUID
    ): UserGroupDto =
        userGroupService.findUserGroupById(id)

    @PutMapping("/{id}")
    @Operation(summary = "Update user group", description = "Update a user group by its ID for the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User group updated successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "User group not found")
        ]
    )
    fun updateUserGroup(
        @PathVariable
        id: UUID,
        @RequestBody
        request: UpdateUserGroupRequest,
        authentication: Authentication
    ): UserGroupDto {
        val user = authentication.principal as User
        return userGroupService.updateUserGroup(id, request, user.id)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user group", description = "Delete a user group by its ID for the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User group deleted successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "User group not found")
        ]
    )
    fun deleteUserGroup(
        @PathVariable id: UUID,
        authentication: Authentication
    ) {
        val user = authentication.principal as User
        userGroupService.deleteUserGroup(id, user.id)
    }

    @PostMapping("/{id}/members")
    @Operation(summary = "Add user to group", description = "Add a user to a group for the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User added successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "User group not found")
        ]
    )
    fun addUserToGroup(
        @PathVariable
        id: UUID,
        @RequestBody
        request: AddUserToGroupRequest,
        authentication: Authentication
    ): GroupToUserDto {
        val user = authentication.principal as User
        return userGroupService.addUserToGroup(id, request, user.id)
    }

    @DeleteMapping("/{id}/members")
    @Operation(summary = "Remove user from group", description = "Remove a user from a group for the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User removed successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "User group not found")
        ]
    )
    fun removeUserFromGroup(
        @PathVariable
        id: UUID,
        @RequestBody
        request: RemoveUserFromGroupRequest,
        authentication: Authentication
    ) {
        val user = authentication.principal as User
        userGroupService.removeUserFromGroup(id, request, user.id)
    }

    @GetMapping("/{id}/members")
    @Operation(summary = "Get group members", description = "Get all members of a group.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Group members returned"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "User group not found")
        ]
    )
    fun getGroupMembers(
        @PathVariable id: UUID,
        authentication: Authentication
    ): List<GroupToUserDto> {
        val user = authentication.principal as User
        return userGroupService.getGroupMembers(id, user.id)
    }

    @PutMapping("/{id}/members/{userId}/role")
    @Operation(summary = "Update user role in group", description = "Update a user's role (editor/member) in a group.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User role updated successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "User group not found")
        ]
    )
    fun updateUserRole(
        @PathVariable id: UUID,
        @PathVariable userId: UUID,
        @RequestParam isEditor: Boolean,
        authentication: Authentication
    ): GroupToUserDto {
        val user = authentication.principal as User
        return userGroupService.updateUserRole(id, userId, isEditor, user.id)
    }
}
