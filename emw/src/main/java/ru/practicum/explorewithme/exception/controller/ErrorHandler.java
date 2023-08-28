package ru.practicum.explorewithme.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explorewithme.exception.dto.ApiError;
import ru.practicum.explorewithme.exception.illegal.CategoryNotEmptyException;
import ru.practicum.explorewithme.exception.illegal.EntityExistsException;
import ru.practicum.explorewithme.exception.illegal.EntityIllegalArgumentException;
import ru.practicum.explorewithme.exception.illegal.EntityStateException;
import ru.practicum.explorewithme.exception.illegal.EventStateException;
import ru.practicum.explorewithme.exception.illegal.RequestLimitException;
import ru.practicum.explorewithme.exception.illegal.TimeLimitException;
import ru.practicum.explorewithme.exception.illegal.UserMatchingException;
import ru.practicum.explorewithme.exception.notFound.EntityNotFoundException;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import static ru.practicum.explorewithme.util.Constants.DATE_FORMAT;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(EntityNotFoundException exception) {
        log.info("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
        return ApiError.builder()
                .status(HttpStatus.NOT_FOUND.name())
                .reason("The required object was not found.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now().format(DATE_FORMAT))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleIllegalArgumentException(EntityIllegalArgumentException exception) {
        log.info("{}: {}", exception.getCause().getClass().getSimpleName(), exception.getMessage());
        return ApiError.builder()
                .status(HttpStatus.CONFLICT.name())
                .reason("Illegal argument")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now().format(DATE_FORMAT))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleCategoryNotEmptyException(CategoryNotEmptyException exception) {
        log.info("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
        return ApiError.builder()
                .status(HttpStatus.CONFLICT.name())
                .reason("For the requested operation the conditions are not met.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now().format(DATE_FORMAT))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        Map<String, String> errorReport = new HashMap<>();
        exception.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();
                    errorReport.put(fieldName, message);
                });
        StringBuilder sbMessage = new StringBuilder();
        for (String fieldName : errorReport.keySet()) {
            sbMessage.append("Field: ")
                    .append(fieldName)
                    .append(". Error: ")
                    .append(errorReport.get(fieldName))
                    .append(". ");
        }
        log.info("{}: {}", exception.getClass().getSimpleName(), sbMessage);
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .reason("Incorrectly made request.")
                .message(sbMessage.toString())
                .timestamp(LocalDateTime.now().format(DATE_FORMAT))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEntityExistsException(EntityExistsException exception) {
        log.info("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
        return ApiError.builder()
                .status(HttpStatus.CONFLICT.name())
                .reason("For the requested operation the conditions are not met.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now().format(DATE_FORMAT))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationExceptionException(ValidationException exception) {
        log.info("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .reason("Integrity constraint has been violated.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now().format(DATE_FORMAT))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConstraintViolationException(ConstraintViolationException exception) {
        log.info("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
        return ApiError.builder()
                .status(HttpStatus.CONFLICT.name())
                .reason("Integrity constraint has been violated.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now().format(DATE_FORMAT))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleTimeLimitException(TimeLimitException exception) {
        log.info("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
        return ApiError.builder()
                .status(HttpStatus.CONFLICT.name())
                .reason("For the requested operation the conditions are not met.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now().format(DATE_FORMAT))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleUserMatchingException(UserMatchingException exception) {
        log.info("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
        return ApiError.builder()
                .status(HttpStatus.CONFLICT.name())
                .reason("For the requested operation the conditions are not met.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now().format(DATE_FORMAT))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventStateException(EventStateException exception) {
        log.info("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
        return ApiError.builder()
                .status(HttpStatus.CONFLICT.name())
                .reason("For the requested operation the conditions are not met.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now().format(DATE_FORMAT))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleRequestLimitException(RequestLimitException exception) {
        log.info("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
        return ApiError.builder()
                .status(HttpStatus.CONFLICT.name())
                .reason("For the requested operation the conditions are not met.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now().format(DATE_FORMAT))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEntityStateException(EntityStateException exception) {
        log.info("{}: {}", exception.getClass().getSimpleName(), exception.getMessage());
        return ApiError.builder()
                .status(HttpStatus.CONFLICT.name())
                .reason("For the requested operation the conditions are not met.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now().format(DATE_FORMAT))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(Throwable exception) {
        log.error("Unknown error: {}", exception.getMessage(), exception);
        return ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .reason("UNKNOWN ERROR.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now().format(DATE_FORMAT))
                .build();
    }

}
