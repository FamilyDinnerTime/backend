package ru.kreslavski.family.dinnertime.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.kreslavski.family.dinnertime.dto.request.CreateStepRequest
import ru.kreslavski.family.dinnertime.dto.response.StepDto
import ru.kreslavski.family.dinnertime.service.StepService
import java.util.UUID

@RestController
@RequestMapping("/steps")
class StepController @Autowired constructor(
    private val stepService: StepService
) {
    @PostMapping
    @Operation(summary = "Create step", description = "Create a new step.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Step created successfully")
        ]
    )
    fun createStep(
        @RequestBody
        request: CreateStepRequest
    ): StepDto =
        stepService.createStep(request)

    @GetMapping("/{id}")
    @Operation(summary = "Get step by ID", description = "Get a step by its ID.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Step found"),
            ApiResponse(responseCode = "404", description = "Step not found")
        ]
    )
    fun getStepById(
        @PathVariable
        id: UUID
    ): StepDto =
        stepService.findStepById(id)

    @GetMapping("/search")
    @Operation(summary = "Search steps", description = "Search for steps by name.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Steps found")
        ]
    )
    fun searchStepsByName(
        @RequestParam
        name: String
    ): List<StepDto> =
        stepService.findStepsByName(name)

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete step", description = "Delete a step by its ID.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Step deleted successfully"),
            ApiResponse(responseCode = "404", description = "Step not found")
        ]
    )
    fun deleteStep(
        @PathVariable
        id: UUID
    ) =
        stepService.deleteStep(id)
}
