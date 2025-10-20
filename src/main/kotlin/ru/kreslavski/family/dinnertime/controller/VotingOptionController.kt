package ru.kreslavski.family.dinnertime.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.kreslavski.family.dinnertime.dto.response.VotingOptionDto
import ru.kreslavski.family.dinnertime.service.VotingOptionService
import java.util.UUID

@RestController
@RequestMapping("/voting-options")
class VotingOptionController @Autowired constructor(
    private val votingOptionService: VotingOptionService
) {

    @GetMapping("/menu/{menuId}")
    @Operation(summary = "Get voting options by menu", description = "Get all voting options for a specific menu voting.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Voting options returned"),
            ApiResponse(responseCode = "404", description = "Menu voting not found")
        ]
    )
    fun getVotingOptionsByMenu(
        @PathVariable menuId: UUID
    ): List<VotingOptionDto> =
        votingOptionService.getVotingOptionsByMenu(menuId)

    @GetMapping("/{id}")
    @Operation(summary = "Get voting option by ID", description = "Get a voting option by its ID.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Voting option found"),
            ApiResponse(responseCode = "404", description = "Voting option not found")
        ]
    )
    fun getVotingOptionById(
        @PathVariable id: UUID
    ): VotingOptionDto =
        votingOptionService.getVotingOptionById(id)

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete voting option", description = "Delete a voting option by its ID.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Voting option deleted successfully"),
            ApiResponse(responseCode = "404", description = "Voting option not found")
        ]
    )
    fun deleteVotingOption(
        @PathVariable id: UUID
    ): Int =
        votingOptionService.removeVotingOption(id)
}
