package com.juanjoromero.jobtrackrapi.dto;

import com.juanjoromero.jobtrackrapi.model.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class JobApplicationResponse {
    private Long id;
    private String company;
    private String position;
    private ApplicationStatus status;
    private LocalDate appliedDate;
    private String source;
    private String notes;
}