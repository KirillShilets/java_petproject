package org.myproject.exception.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.ConstraintViolationException;
import org.myproject.dto.CustomErrorResponseDto;
import org.myproject.exception.AdvancedRuntimeException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CustomErrorResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = fieldError.getDefaultMessage();

        CustomErrorResponseDto errorResponse = new CustomErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                errorMessage
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CustomErrorResponseDto> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String errorMessage;
        String fieldName = null;

        if (ex.getCause() instanceof JsonMappingException jsonMappingException) {
            if (jsonMappingException.getPath() != null && !jsonMappingException.getPath().isEmpty()) {
                fieldName = jsonMappingException.getPath().get(0).getFieldName();
            }
        }

        if (fieldName != null && fieldName.equals("color")) {
            errorMessage = "Неверное значение цвета";
        } else if (fieldName != null) {
            errorMessage = "Неверный тип данных или неправильное имя поля.";
        }
        else {
            errorMessage = "Ошибка в формате запроса.";
        }

        CustomErrorResponseDto errorResponse = new CustomErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                errorMessage
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CustomErrorResponseDto> handleMissingServletRequestParameterException() {
        CustomErrorResponseDto errorResponse = new CustomErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Ошибка в теле запроса."
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CustomErrorResponseDto> handleMethodArgumentTypeMismatchException() {
        CustomErrorResponseDto errorResponse = new CustomErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Ошибка в параметрах URL."
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<CustomErrorResponseDto> handleNotFoundException() {
        CustomErrorResponseDto errorResponse = new CustomErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                "Такого пути не существует."
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AdvancedRuntimeException.class)
    public ResponseEntity<CustomErrorResponseDto> handleAdvancedRuntimeException(AdvancedRuntimeException ex) {
        CustomErrorResponseDto errorResponse = new CustomErrorResponseDto(
                ex.getCode(),
                ex.getMessage(),
                ex.getError()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getCode()));
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<CustomErrorResponseDto> handleEmptyResultDataAccessException() {
        CustomErrorResponseDto errorResponse = new CustomErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                "Ничего не найдено."
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<CustomErrorResponseDto> handleMethodNotAllowed() {
        CustomErrorResponseDto error = new CustomErrorResponseDto(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Method Not Allowed",
                "Запрашиваемый URL не существует или метод HTTP не поддерживается."
        );
        return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CustomErrorResponseDto> handleConstraintViolationException() {
        CustomErrorResponseDto errorResponse = new CustomErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Ошибка в параметрах URL."
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}