package org.myproject.exception.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.myproject.dto.CustomErrorResponseDto;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<CustomErrorResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = fieldError.getDefaultMessage();

        CustomErrorResponseDto errorResponse = new CustomErrorResponseDto(
                400,
                "Bad Request",
                errorMessage
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<CustomErrorResponseDto> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
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

        CustomErrorResponseDto errorResponse = new CustomErrorResponseDto(
                400,
                "Bad Request",
                errorMessage
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<CustomErrorResponseDto> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        CustomErrorResponseDto errorResponse = new CustomErrorResponseDto(
                400,
                "Bad Request",
                "Ошибка в теле запроса!"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<CustomErrorResponseDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        CustomErrorResponseDto errorResponse = new CustomErrorResponseDto(
                400,
                "Bad Request",
                "Ошибка в параметрах URL"
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<CustomErrorResponseDto> handleNotFoundException(NoHandlerFoundException ex) {
        CustomErrorResponseDto errorResponse = new CustomErrorResponseDto(
                404,
                "Not Found",
                "Такого пути не существует."
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<CustomErrorResponseDto> exception(Exception ex) {
        CustomErrorResponseDto errorResponse = new CustomErrorResponseDto(
                500,
                "Internal Server Error",
                "Ошибка сервера"
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}