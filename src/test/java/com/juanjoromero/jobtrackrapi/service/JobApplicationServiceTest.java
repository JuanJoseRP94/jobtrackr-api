package com.juanjoromero.jobtrackrapi.service;

import com.juanjoromero.jobtrackrapi.dto.JobApplicationRequest;
import com.juanjoromero.jobtrackrapi.dto.JobApplicationResponse;
import com.juanjoromero.jobtrackrapi.exception.ResourceNotFoundException;
import com.juanjoromero.jobtrackrapi.model.ApplicationStatus;
import com.juanjoromero.jobtrackrapi.model.JobApplication;
import com.juanjoromero.jobtrackrapi.model.User;
import com.juanjoromero.jobtrackrapi.repository.JobApplicationRepository;
import com.juanjoromero.jobtrackrapi.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

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

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JobApplicationService service;

    private User currentUser;
    private JobApplication existingApplication;

    @BeforeEach
    void setUp() {
        currentUser = new User(1L, "juan@example.com", "hashed-password");

        // Simulamos que hay un usuario autenticado, como haría el filtro JWT real.
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(currentUser.getEmail(), null)
        );
        when(userRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(currentUser));

        existingApplication = new JobApplication();
        existingApplication.setId(1L);
        existingApplication.setCompany("Google");
        existingApplication.setPosition("Backend Developer");
        existingApplication.setStatus(ApplicationStatus.APPLIED);
        existingApplication.setAppliedDate(LocalDate.of(2026, 7, 1));
        existingApplication.setSource("LinkedIn");
        existingApplication.setOwner(currentUser);
    }

    @AfterEach
    void tearDown() {
        // Limpiamos el contexto de seguridad para que un test no "contamine" al siguiente.
        SecurityContextHolder.clearContext();
    }

    @Test
    void findById_devuelveLaCandidatura_cuandoExiste() {
        when(repository.findByIdAndOwner(1L, currentUser)).thenReturn(Optional.of(existingApplication));

        JobApplicationResponse response = service.findById(1L);

        assertThat(response.getCompany()).isEqualTo("Google");
        assertThat(response.getPosition()).isEqualTo("Backend Developer");
    }

    @Test
    void findById_lanzaExcepcion_cuandoNoExiste() {
        when(repository.findByIdAndOwner(99L, currentUser)).thenReturn(Optional.empty());

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
        when(repository.findByIdAndOwner(50L, currentUser)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(50L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(repository, never()).delete(any());
    }

    @Test
    void findAll_devuelveListaVacia_cuandoNoHayCandidaturas() {
        when(repository.findByOwner(currentUser)).thenReturn(List.of());

        List<JobApplicationResponse> result = service.findAll();

        assertThat(result).isEmpty();
    }
}