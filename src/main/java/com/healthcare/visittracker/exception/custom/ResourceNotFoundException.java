package com.healthcare.visittracker.exception.custom;

import com.healthcare.visittracker.exception.BaseException;
import com.healthcare.visittracker.exception.ErrorCode;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(String message) {
        super(ErrorCode.RESOURCE_NOT_FOUND, message);
    }
}
