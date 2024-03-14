package edu.java.bot.handler;

import dto.ApiErrorResponse;
import edu.java.bot.exceptions.IncorrectParametersExceptions;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiErrorHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IncorrectParametersExceptions.class)
    @ApiResponse(responseCode = "400",
                 description = "Некорректные параметры запроса",
                 content = @Content(
                     mediaType = "application/json",
                     schema = @Schema(implementation = ApiErrorResponse.class)
                 )
    )
    public ApiErrorResponse incorrectParametersHandler(IncorrectParametersExceptions exception) {
        return new ApiErrorResponse(
            "Некорректные параметры запроса",
            HttpStatus.BAD_REQUEST.toString(),
            exception.getClass().getSimpleName(),
            exception.getMessage(),
            null
        );
    }
}
