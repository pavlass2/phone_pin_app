package com.example.demo.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ApiError(String message,
                       HttpStatus httpStatus,
                       ZonedDateTime occurredAt) {
}
