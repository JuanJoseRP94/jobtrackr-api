package com.juanjoromero.jobtrackrapi.dto;

import com.juanjoromero.jobtrackrapi.model.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class JobApplicationRequest {

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    private String company;

    @NotBlank(message = "El puesto es obligatorio")
    private String position;

    private ApplicationStatus status;
    private LocalDate appliedDate;
    private String source;
    private String notes;
}