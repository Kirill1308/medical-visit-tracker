package com.healthcare.visittracker.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Resource errors
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found"),
    RESOURCE_ALREADY_EXISTS(HttpStatus.CONFLICT, "Resource already exists"),
    RESOURCE_INVALID(HttpStatus.BAD_REQUEST, "Resource is invalid"),

    // Validation errors
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad request"),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Validation failed"),

    // Business logic errors
    BUSINESS_RULE_VIOLATION(HttpStatus.CONFLICT, "Business rule violation"),

    // System errors
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");

    private final HttpStatus status;
    private final String message;
}
