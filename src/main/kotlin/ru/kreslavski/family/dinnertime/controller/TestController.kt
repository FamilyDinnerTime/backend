package ru.kreslavski.family.dinnertime.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Simple test endpoint to check API availability.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "API is available")
        ]
    )
    fun test() = "ok"
}
