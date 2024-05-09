package edu.java.bot.handler;

import dto.ApiErrorResponse;
import edu.java.bot.exceptions.IncorrectParametersException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiErrorHandlerBot {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IncorrectParametersException.class)
    public ApiErrorResponse incorrectParametersHandler(@NotNull IncorrectParametersException exception) {
        return new ApiErrorResponse(
            "Некорректные параметры запроса",
            HttpStatus.BAD_REQUEST.toString(),
            exception.getName(),
            exception.getMessage(),
            null
        );
    }
}
