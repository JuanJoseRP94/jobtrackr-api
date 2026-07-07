package com.juanjoromero.jobtrackrapi.repository;

import com.juanjoromero.jobtrackrapi.model.ApplicationStatus;
import com.juanjoromero.jobtrackrapi.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByStatus(ApplicationStatus status);
}