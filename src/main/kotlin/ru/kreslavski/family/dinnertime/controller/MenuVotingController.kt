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
import ru.kreslavski.family.dinnertime.dto.request.AddDishToVotingRequest
import ru.kreslavski.family.dinnertime.dto.request.CreateMenuVotingRequest
import ru.kreslavski.family.dinnertime.dto.request.RemoveDishFromVotingRequest
import ru.kreslavski.family.dinnertime.dto.request.UpdateMenuVotingRequest
import ru.kreslavski.family.dinnertime.dto.response.MenuVotingDto
import ru.kreslavski.family.dinnertime.dto.response.VotingOptionDto
import ru.kreslavski.family.dinnertime.entity.User
import ru.kreslavski.family.dinnertime.service.MenuVotingService
import java.util.UUID

@RestController
@RequestMapping("/menu-votings")
class MenuVotingController @Autowired constructor(
    private val menuVotingService: MenuVotingService,
) {

    @PostMapping
    @Operation(summary = "Create menu voting", description = "Create a new menu voting for the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Menu voting created successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    fun createMenuVoting(
        @RequestBody
        request: CreateMenuVotingRequest,
        authentication: Authentication
    ): MenuVotingDto {
        val user = authentication.principal as User
        return menuVotingService.createMenuVoting(request, user.id)
    }

    @GetMapping("/search")
    @Operation(summary = "Search menu votings", description = "Search for menu votings by name in groups where the user participates.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Menu votings found"),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    fun searchMenuVotingsByName(
        @RequestParam
        name: String,
        authentication: Authentication
    ): List<MenuVotingDto> {
        val user = authentication.principal as User
        return menuVotingService.findMenuVotingsByName(name, user.id)
    }

    @GetMapping("/my")
    @Operation(summary = "Get my menu votings", description = "Get all menu votings created by the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Menu votings returned"),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    fun getMyMenuVotings(
        authentication: Authentication
    ): List<MenuVotingDto> {
        val user = authentication.principal as User
        return menuVotingService.findMenuVotingsByUser(user.id)
    }

    @GetMapping("/groups")
    @Operation(summary = "Get menu votings from user groups", description = "Get all menu votings from groups where the user participates.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Menu votings returned"),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    fun getMenuVotingsFromUserGroups(
        authentication: Authentication
    ): List<MenuVotingDto> {
        val user = authentication.principal as User
        return menuVotingService.findMenuVotingsFromUserGroups(user.id)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get menu voting by ID", description = "Get a menu voting by its ID.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Menu voting found"),
            ApiResponse(responseCode = "404", description = "Menu voting not found")
        ]
    )
    fun getMenuVotingById(
        @PathVariable id: UUID
    ): MenuVotingDto =
        menuVotingService.findMenuVotingById(id)

    @PutMapping("/{id}")
    @Operation(summary = "Update menu voting", description = "Update a menu voting by its ID for the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Menu voting updated successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Menu voting not found")
        ]
    )
    fun updateMenuVoting(
        @PathVariable
        id: UUID,
        @RequestBody
        request: UpdateMenuVotingRequest,
        authentication: Authentication
    ): MenuVotingDto {
        val user = authentication.principal as User
        return menuVotingService.updateMenuVoting(id, request, user.id)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete menu voting", description = "Delete a menu voting by its ID for the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Menu voting deleted successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Menu voting not found")
        ]
    )
    fun deleteMenuVoting(
        @PathVariable id: UUID,
        authentication: Authentication
    ) {
        val user = authentication.principal as User
        menuVotingService.deleteMenuVoting(id, user.id)
    }

    @PostMapping("/{id}/dishes")
    @Operation(summary = "Add dish to voting", description = "Add a dish to a menu voting for the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Dish added successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Menu voting not found")
        ]
    )
    fun addDishToVoting(
        @PathVariable
        id: UUID,
        @RequestBody
        request: AddDishToVotingRequest,
        authentication: Authentication
    ): VotingOptionDto {
        val user = authentication.principal as User
        return menuVotingService.addDishToVoting(id, request, user.id)
    }

    @DeleteMapping("/{id}/dishes")
    @Operation(summary = "Remove dish from voting", description = "Remove a dish from a menu voting for the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Dish removed successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Menu voting not found")
        ]
    )
    fun removeDishFromVoting(
        @PathVariable
        id: UUID,
        @RequestBody
        request: RemoveDishFromVotingRequest,
        authentication: Authentication
    ) {
        val user = authentication.principal as User
        menuVotingService.removeDishFromVoting(id, request, user.id)
    }

    @GetMapping("/{id}/options")
    @Operation(summary = "Get voting options", description = "Get all voting options for a specific menu voting.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Voting options returned"),
            ApiResponse(responseCode = "404", description = "Menu voting not found")
        ]
    )
    fun getVotingOptions(
        @PathVariable id: UUID
    ): List<VotingOptionDto> =
        menuVotingService.getVotingOptions(id)
}
