package com.healthcare.visittracker.exception.custom;

import com.healthcare.visittracker.exception.BaseException;
import com.healthcare.visittracker.exception.ErrorCode;

public class VisitConflictException extends BaseException {
    public VisitConflictException(String message) {
        super(ErrorCode.RESOURCE_ALREADY_EXISTS, message);
    }
}
