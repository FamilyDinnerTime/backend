package ru.kreslavski.family.dinnertime.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.kreslavski.family.dinnertime.dto.response.GroupToUserDto
import ru.kreslavski.family.dinnertime.entity.User
import ru.kreslavski.family.dinnertime.service.GroupToUserService
import java.util.UUID

@RestController
@RequestMapping("/group-to-users")
class GroupToUserController @Autowired constructor(
    private val groupToUserService: GroupToUserService,
) {

    @GetMapping("/my-groups")
    @Operation(summary = "Get my groups", description = "Get all groups the authenticated user belongs to.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User groups returned"),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    fun getMyGroups(
        authentication: Authentication,
    ): List<GroupToUserDto> {
        val user = authentication.principal as User
        return groupToUserService.getUserGroups(user.id)
    }

    @GetMapping("/{groupId}/members")
    @Operation(summary = "Get group members", description = "Get all members of a specific group.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Group members returned"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "User group not found")
        ]
    )
    fun getGroupMembers(
        @PathVariable groupId: UUID,
        authentication: Authentication,
    ): List<GroupToUserDto> {
        val user = authentication.principal as User
        return groupToUserService.getGroupMembers(groupId, user.id)
    }

    @GetMapping("/{groupId}/my-role")
    @Operation(summary = "Get my role in group", description = "Get the authenticated user's role in a specific group.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User role returned"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "User group not found")
        ]
    )
    fun getMyRoleInGroup(
        @PathVariable groupId: UUID,
        authentication: Authentication,
    ): Boolean {
        val user = authentication.principal as User
        return groupToUserService.getUserRoleInGroup(groupId, user.id)
    }
}
