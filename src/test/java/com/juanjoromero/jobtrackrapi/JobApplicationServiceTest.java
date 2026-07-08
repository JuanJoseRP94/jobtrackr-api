package com.juanjoromero.jobtrackrapi;

import com.juanjoromero.jobtrackrapi.dto.JobApplicationRequest;
import com.juanjoromero.jobtrackrapi.dto.JobApplicationResponse;
import com.juanjoromero.jobtrackrapi.exception.ResourceNotFoundException;
import com.juanjoromero.jobtrackrapi.model.ApplicationStatus;
import com.juanjoromero.jobtrackrapi.model.JobApplication;
import com.juanjoromero.jobtrackrapi.repository.JobApplicationRepository;
import com.juanjoromero.jobtrackrapi.service.JobApplicationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobApplicationServiceTest {

    @Mock
    private JobApplicationRepository repository;

    @InjectMocks
    private JobApplicationService service;

    private JobApplication existingApplication;

    @BeforeEach
    void setUp() {
        existingApplication = new JobApplication();
        existingApplication.setId(1L);
        existingApplication.setCompany("Google");
        existingApplication.setPosition("Backend Developer");
        existingApplication.setStatus(ApplicationStatus.APPLIED);
        existingApplication.setAppliedDate(LocalDate.of(2026, 7, 1));
        existingApplication.setSource("LinkedIn");
    }

    @Test
    void findById_devuelveLaCandidatura_cuandoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(existingApplication));

        JobApplicationResponse response = service.findById(1L);

        assertThat(response.getCompany()).isEqualTo("Google");
        assertThat(response.getPosition()).isEqualTo("Backend Developer");
    }

    @Test
    void findById_lanzaExcepcion_cuandoNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_guardaYDevuelveLaCandidaturaCreada() {
        JobApplicationRequest request = new JobApplicationRequest();
        request.setCompany("Microsoft");
        request.setPosition("Java Developer");
        request.setStatus(ApplicationStatus.APPLIED);

        when(repository.save(any(JobApplication.class))).thenAnswer(invocation -> {
            JobApplication saved = invocation.getArgument(0);
            saved.setId(2L);
            return saved;
        });

        JobApplicationResponse response = service.create(request);

        assertThat(response.getId()).isEqualTo(2L);
        assertThat(response.getCompany()).isEqualTo("Microsoft");
        verify(repository, times(1)).save(any(JobApplication.class));
    }

    @Test
    void delete_lanzaExcepcion_cuandoLaCandidaturaNoExiste() {
        when(repository.existsById(50L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(50L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(repository, never()).deleteById(any());
    }

    @Test
    void findAll_devuelveListaVacia_cuandoNoHayCandidaturas() {
        when(repository.findAll()).thenReturn(List.of());

        List<JobApplicationResponse> result = service.findAll();

        assertThat(result).isEmpty();
    }
}