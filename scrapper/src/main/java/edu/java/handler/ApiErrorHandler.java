package edu.java.handler;

import dto.ApiErrorResponse;
import exceptions.AbsentChatException;
import exceptions.IncorrectParametersExceptions;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiErrorHandler {
    @ExceptionHandler(IncorrectParametersExceptions.class)
    @ApiResponse(responseCode = "400",
                 description = "Некорректные параметры запроса",
                 content = @Content(
                     mediaType = "application/json",
                     schema = @Schema(implementation = ApiErrorResponse.class)
                 )
    )
    public ResponseEntity<ApiErrorResponse> incorrectParametersHandler(IncorrectParametersExceptions exception) {
        ApiErrorResponse apiResponse = new ApiErrorResponse(
            "Некорректные параметры запроса",
            "400",
            "IncorrectParameters",
            exception.getMessage(),
            null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AbsentChatException.class)
    @ApiResponse(responseCode = "404",
                 description = "Чат не существует",
                 content = @Content(
                     mediaType = "application/json",
                     schema = @Schema(implementation = ApiErrorResponse.class)
                 )
    )
    public ResponseEntity<ApiErrorResponse> absentChat(AbsentChatException exception) {
        ApiErrorResponse apiResponse = new ApiErrorResponse(
            "Чат не существует",
            "404",
            "AbsentChat",
            exception.getMessage(),
            null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
