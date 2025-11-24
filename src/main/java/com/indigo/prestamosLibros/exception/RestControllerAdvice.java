package com.indigo.prestamosLibros.exception;

import com.indigo.prestamosLibros.dto.ResponseError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Optional;

@ControllerAdvice
@ResponseBody
public class RestControllerAdvice {

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError notFoundObjectExceptionHandler(Exception ex) {
        return ResponseError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .date(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleConstraintViolation(ConstraintViolationException ex) {

        // 1. Obtener la colección de violaciones (ConstraintViolation)
        String errorMessage = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .reduce((first, second) -> first + ", " + second)
                .orElse("Validation errors");

        return ResponseError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .date(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleMissingParams(MissingServletRequestParameterException ex) {
        String errorMessage = "Missing parameter: " + ex.getParameterName();
        return ResponseError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .date(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String errorMessage = String.format("Parameter '%s' should be of type %s. Provided value: %s",
                ex.getName(),
                Optional.ofNullable(ex.getRequiredType()).map(Class::getSimpleName).orElse("unknown"),
                ex.getValue()
        );

        return ResponseError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .date(LocalDateTime.now())
                .build();
    }

}
