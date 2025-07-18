package ru.kreslavski.family.dinnertime.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.kreslavski.family.dinnertime.dto.request.CreateIngredientRequest
import ru.kreslavski.family.dinnertime.dto.request.UpdateIngredientRequest
import ru.kreslavski.family.dinnertime.dto.response.IngredientDto
import ru.kreslavski.family.dinnertime.service.IngredientService
import java.util.UUID

@RestController
@RequestMapping("/ingredients")
class IngredientController @Autowired constructor(
    private val ingredientService: IngredientService
) {
    @GetMapping("/search")
    @Operation(summary = "Search ingredients", description = "Search for ingredients by name.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Ingredients found")
        ]
    )
    fun searchIngredientsByName(
        @RequestParam
        name: String
    ): List<IngredientDto> =
        ingredientService.findIngredientsByName(name)

    @GetMapping("/{id}")
    @Operation(summary = "Get ingredient by ID", description = "Get an ingredient by its ID.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Ingredient found"),
            ApiResponse(responseCode = "404", description = "Ingredient not found")
        ]
    )
    fun getIngredientById(
        @PathVariable
        id: UUID
    ): IngredientDto =
        ingredientService.findIngredientById(id)

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create ingredient", description = "Create a new ingredient (admin only).")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Ingredient created successfully"),
            ApiResponse(responseCode = "403", description = "Forbidden")
        ]
    )
    fun createIngredient(
        @Valid
        @RequestBody
        request: CreateIngredientRequest
    ): IngredientDto =
        ingredientService.createIngredient(request)

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update ingredient", description = "Update an ingredient by its ID (admin only).")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Ingredient updated successfully"),
            ApiResponse(responseCode = "403", description = "Forbidden"),
            ApiResponse(responseCode = "404", description = "Ingredient not found")
        ]
    )
    fun updateIngredient(
        @PathVariable
        id: UUID,
        @Valid
        @RequestBody
        request: UpdateIngredientRequest
    ): IngredientDto =
        ingredientService.updateIngredient(id, request)

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete ingredient", description = "Delete an ingredient by its ID (admin only).")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Ingredient deleted successfully"),
            ApiResponse(responseCode = "403", description = "Forbidden"),
            ApiResponse(responseCode = "404", description = "Ingredient not found")
        ]
    )
    fun deleteIngredient(
        @PathVariable
        id: UUID
    ) =
        ingredientService.deleteIngredient(id)
}
