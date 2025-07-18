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
import ru.kreslavski.family.dinnertime.dto.request.AddIngredientToDishRequest
import ru.kreslavski.family.dinnertime.dto.request.AddStepToDishRequest
import ru.kreslavski.family.dinnertime.dto.request.CreateDishRequest
import ru.kreslavski.family.dinnertime.dto.request.RemoveIngredientFromDishRequest
import ru.kreslavski.family.dinnertime.dto.request.RemoveStepFromDishRequest
import ru.kreslavski.family.dinnertime.dto.request.UpdateDishRequest
import ru.kreslavski.family.dinnertime.dto.response.DishDto
import ru.kreslavski.family.dinnertime.entity.User
import ru.kreslavski.family.dinnertime.service.DishService
import java.util.UUID

@RestController
@RequestMapping("/dishes")
class DishController @Autowired constructor(
    private val dishService: DishService
) {
    @PostMapping
    @Operation(summary = "Create dish", description = "Create a new dish for the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Dish created successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    fun createDish(
        @RequestBody
        request: CreateDishRequest,
        authentication: Authentication
    ): DishDto {
        val user = authentication.principal as User
        return dishService.createDish(request, user.id)
    }

    @GetMapping("/search")
    @Operation(summary = "Search dishes", description = "Search for dishes by name.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Dishes found")
        ]
    )
    fun searchDishesByName(
        @RequestParam
        name: String
    ): List<DishDto> =
        dishService.findDishesByName(name)

    @GetMapping("/my")
    @Operation(summary = "Get my dishes", description = "Get all dishes created by the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Dishes returned"),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    fun getMyDishes(
        authentication: Authentication
    ): List<DishDto> {
        val user = authentication.principal as User
        return dishService.findDishesByUser(user.id)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get dish by ID", description = "Get a dish by its ID.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Dish found"),
            ApiResponse(responseCode = "404", description = "Dish not found")
        ]
    )
    fun getDishById(
        @PathVariable id: UUID
    ): DishDto =
        dishService.findDishById(id)

    @PutMapping("/{id}")
    @Operation(summary = "Update dish", description = "Update a dish by its ID for the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Dish updated successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Dish not found")
        ]
    )
    fun updateDish(
        @PathVariable
        id: UUID,
        @RequestBody
        request: UpdateDishRequest,
        authentication: Authentication
    ): DishDto {
        val user = authentication.principal as User
        return dishService.updateDish(id, request, user.id)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete dish", description = "Delete a dish by its ID for the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Dish deleted successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Dish not found")
        ]
    )
    fun deleteDish(
        @PathVariable id: UUID,
        authentication: Authentication
    ) {
        val user = authentication.principal as User
        dishService.deleteDish(id, user.id)
    }

    @PostMapping("/{id}/ingredients")
    @Operation(summary = "Add ingredient to dish", description = "Add an ingredient to a dish for the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Ingredient added successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Dish or ingredient not found")
        ]
    )
    fun addIngredientToDish(
        @PathVariable
        id: UUID,
        @RequestBody
        request: AddIngredientToDishRequest,
        authentication: Authentication
    ) {
        val user = authentication.principal as User
        dishService.addIngredientToDish(id, request, user.id)
    }

    @DeleteMapping("/{id}/ingredients")
    @Operation(summary = "Remove ingredient from dish", description = "Remove an ingredient from a dish for the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Ingredient removed successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Dish or ingredient not found")
        ]
    )
    fun removeIngredientFromDish(
        @PathVariable
        id: UUID,
        @RequestBody
        request: RemoveIngredientFromDishRequest,
        authentication: Authentication
    ) {
        val user = authentication.principal as User
        dishService.removeIngredientFromDish(id, request, user.id)
    }

    @PostMapping("/{id}/steps")
    @Operation(summary = "Add step to dish", description = "Add a step to a dish for the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Step added successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Dish or step not found")
        ]
    )
    fun addStepToDish(
        @PathVariable
        id: UUID,
        @RequestBody
        request: AddStepToDishRequest,
        authentication: Authentication
    ) {
        val user = authentication.principal as User
        dishService.addStepToDish(id, request, user.id)
    }

    @DeleteMapping("/{id}/steps")
    @Operation(summary = "Remove step from dish", description = "Remove a step from a dish for the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Step removed successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Dish or step not found")
        ]
    )
    fun removeStepFromDish(
        @PathVariable
        id: UUID,
        @RequestBody
        request: RemoveStepFromDishRequest,
        authentication: Authentication
    ) {
        val user = authentication.principal as User
        dishService.removeStepFromDish(id, request, user.id)
    }
}
