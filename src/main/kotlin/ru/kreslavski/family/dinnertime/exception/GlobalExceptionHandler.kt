package ru.kreslavski.family.dinnertime.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(DishNotFoundException::class)
    fun handleDishNotFoundException(ex: DishNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(ex.message ?: "Dish not found"), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(IngredientNotFoundException::class)
    fun handleIngredientNotFoundException(ex: IngredientNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(ex.message ?: "Ingredient not found"), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(StepNotFoundException::class)
    fun handleStepNotFoundException(ex: StepNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(ex.message ?: "Step not found"), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(StepInUseException::class)
    fun handleStepInUseException(ex: StepInUseException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(ex.message ?: "Step is in use"), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(ex: UserAlreadyExistsException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(ex.message ?: "User already exists"), HttpStatus.CONFLICT)
    }

    @ExceptionHandler(UnauthorizedAccessException::class)
    fun handleUnauthorizedAccessException(ex: UnauthorizedAccessException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(ex.message ?: "Unauthorized access"), HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(ex: BadCredentialsException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse("Invalid username or password"), HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ValidationErrorResponse> {
        val errors = ex.bindingResult.allErrors.associate { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage ?: "Validation failed"
            fieldName to errorMessage
        }
        return ResponseEntity(ValidationErrorResponse("Validation failed", errors), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(ex.message ?: "Bad request"), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}

data class ErrorResponse(val message: String)

data class ValidationErrorResponse(
    val message: String,
    val errors: Map<String, String>
)
