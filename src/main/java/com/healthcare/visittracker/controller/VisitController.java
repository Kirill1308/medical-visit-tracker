package com.healthcare.visittracker.controller;

import com.healthcare.visittracker.dto.request.VisitRequest;
import com.healthcare.visittracker.service.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    public void createVisit(@Valid @RequestBody VisitRequest request) {
        visitService.createVisit(request);
    }
}
