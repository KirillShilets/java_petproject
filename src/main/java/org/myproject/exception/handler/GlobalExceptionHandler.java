package org.myproject.exception.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.myproject.dto.CustomErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = fieldError.getDefaultMessage();

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                400,
                "Bad Request",
                errorMessage
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<CustomErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String errorMessage;
        String fieldName = null;

        if (ex.getCause() instanceof JsonMappingException) {
            JsonMappingException jsonMappingException = (JsonMappingException) ex.getCause();
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

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                400,
                "Bad Request",
                errorMessage
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}