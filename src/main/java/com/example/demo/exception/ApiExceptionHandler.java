package com.example.demo.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = ApiRequestException.class)
    public ResponseEntity<Object> handleApiRequestException(
            ApiRequestException e
    ) {

        ApiError apiError = new ApiError(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(
                apiError,
                HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(value = NotFoundException.class)
//    public ResponseEntity<Object> handleNotFoundException(
//            NotFoundException e
//    ) {
//
//        ApiException apiException = new ApiException(
//                e.getMessage(),
//                e,
//                HttpStatus.NOT_FOUND,
//                ZonedDateTime.now()
//        );
//
//        return new ResponseEntity<>(
//                apiException,
//                HttpStatus.NOT_FOUND);
//    }

        @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        String message = e.getFieldError() == null ? e.getMessage() : e.getFieldError().getDefaultMessage();
        ApiError apiError = new ApiError(
                message,
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(
                apiError,
                HttpStatus.NOT_FOUND);
    }
}
