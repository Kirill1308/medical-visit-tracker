package com.healthcare.visittracker.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ApiError {
    private String path;
    private String message;
    private int statusCode;
    private String statusName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    @Builder.Default
    private List<String> errors = new ArrayList<>();
}
