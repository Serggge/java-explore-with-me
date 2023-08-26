package ru.practicum.explorewithme.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleDateTimeException(DateTimeException exception) {
        log.info("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .reason("Integrity constraint has been violated.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .build();
    }
}
