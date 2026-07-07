package com.juanjoromero.jobtrackrapi.service;

import com.juanjoromero.jobtrackrapi.dto.JobApplicationRequest;
import com.juanjoromero.jobtrackrapi.dto.JobApplicationResponse;
import com.juanjoromero.jobtrackrapi.exception.ResourceNotFoundException;
import com.juanjoromero.jobtrackrapi.model.JobApplication;
import com.juanjoromero.jobtrackrapi.repository.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository repository;

    public List<JobApplicationResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public JobApplicationResponse findById(Long id) {
        JobApplication entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidatura no encontrada con id " + id));
        return toResponse(entity);
    }

    public JobApplicationResponse create(JobApplicationRequest request) {
        JobApplication entity = new JobApplication();
        copyRequestToEntity(request, entity);
        return toResponse(repository.save(entity));
    }

    public JobApplicationResponse update(Long id, JobApplicationRequest request) {
        JobApplication entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidatura no encontrada con id " + id));
        copyRequestToEntity(request, entity);
        return toResponse(repository.save(entity));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Candidatura no encontrada con id " + id);
        }
        repository.deleteById(id);
    }

    private void copyRequestToEntity(JobApplicationRequest request, JobApplication entity) {
        entity.setCompany(request.getCompany());
        entity.setPosition(request.getPosition());
        entity.setStatus(request.getStatus());
        entity.setAppliedDate(request.getAppliedDate());
        entity.setSource(request.getSource());
        entity.setNotes(request.getNotes());
    }

    private JobApplicationResponse toResponse(JobApplication entity) {
        return new JobApplicationResponse(
                entity.getId(),
                entity.getCompany(),
                entity.getPosition(),
                entity.getStatus(),
                entity.getAppliedDate(),
                entity.getSource(),
                entity.getNotes()
        );
    }
}