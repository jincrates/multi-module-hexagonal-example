package me.jincrates.ecommerce.application.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            "Unexpected error!"
        );
    }

    @ResponseBody
    @ExceptionHandler(value = {ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(ValidationException validationException) {
        if (validationException instanceof ConstraintViolationException) {
            String violations = extractViolationsFromException(
                (ConstraintViolationException) validationException);
            log.error(violations, validationException);
            return new ErrorResponse(
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                violations
            );
        } else {
            String exceptionMessage = validationException.getMessage();
            log.error(exceptionMessage, validationException);
            return new ErrorResponse(
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exceptionMessage
            );
        }

    }

    private String extractViolationsFromException (ConstraintViolationException validationException){
        return validationException.getConstraintViolations()
            .stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.joining("--"));
    }
}

