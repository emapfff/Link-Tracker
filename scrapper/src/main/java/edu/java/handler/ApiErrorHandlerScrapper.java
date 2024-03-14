package edu.java.handler;

import dto.ApiErrorResponse;
import edu.java.exceptions.AbsentChatException;
import edu.java.exceptions.IncorrectParametersException;
import edu.java.exceptions.LinkNotFoundException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiErrorHandlerScrapper {
    @ExceptionHandler(IncorrectParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponse(responseCode = "400",
                 description = "Некорректные параметры запроса",
                 content = @Content(
                     mediaType = "application/json",
                     schema = @Schema(implementation = ApiErrorResponse.class)
                 )
    )
    public ApiErrorResponse handleIncorrectParametersHandler(IncorrectParametersException exception) {
        return new ApiErrorResponse(
            "Некорректные параметры запроса",
            HttpStatus.BAD_REQUEST.toString(),
            exception.getName(),
            exception.getMessage(),
            null
        );
    }

    @ExceptionHandler(AbsentChatException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponse(responseCode = "404",
                 description = "Чат не существует",
                 content = @Content(
                     mediaType = "application/json",
                     schema = @Schema(implementation = ApiErrorResponse.class)
                 )
    )
    public ApiErrorResponse handleAbsentChat(AbsentChatException exception) {
        return new ApiErrorResponse(
            "Чат не существует",
            HttpStatus.NOT_FOUND.toString(),
            exception.getName(),
            exception.getMessage(),
            null
        );
    }

    @ExceptionHandler(LinkNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponse(responseCode = "404",
                 description = "Ссылка не найдена",
                 content = @Content(
                     mediaType = "application/json",
                     schema = @Schema(implementation = ApiErrorResponse.class)
                 )
    )
    public ApiErrorResponse handleLinkNotFound(LinkNotFoundException exception) {
        return new ApiErrorResponse(
            "Ссылка не найдена",
            HttpStatus.NOT_FOUND.toString(),
            exception.getName(),
            exception.getMessage(),
            null
        );
    }
}
