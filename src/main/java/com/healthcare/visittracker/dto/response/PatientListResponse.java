package com.healthcare.visittracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientListResponse {
    @Builder.Default
    private List<PatientResponse> data = new ArrayList<>();
    private Integer count;
}
