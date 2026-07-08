package com.juanjoromero.jobtrackrapi.repository;

import com.juanjoromero.jobtrackrapi.model.JobApplication;
import com.juanjoromero.jobtrackrapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByOwner(User owner);
    Optional<JobApplication> findByIdAndOwner(Long id, User owner);
}