package com.healthcare.visittracker.service;

import com.healthcare.visittracker.dto.request.VisitRequest;

public interface VisitService {
    void createVisit(VisitRequest request);
}
