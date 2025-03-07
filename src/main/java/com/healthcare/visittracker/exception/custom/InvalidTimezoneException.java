package com.healthcare.visittracker.exception.custom;

import com.healthcare.visittracker.exception.BaseException;
import com.healthcare.visittracker.exception.ErrorCode;

public class InvalidTimezoneException extends BaseException {
    public InvalidTimezoneException(String message) {
        super(ErrorCode.BAD_REQUEST, message);
    }
}
