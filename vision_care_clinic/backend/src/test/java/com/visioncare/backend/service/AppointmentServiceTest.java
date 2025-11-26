package com.visioncare.backend.service;

import com.visioncare.backend.model.Appointment;
import com.visioncare.backend.model.User;
import com.visioncare.backend.repository.AppointmentRepository;
import com.visioncare.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAppointment_Success() {
        // Arrange
        String userEmail = "test@example.com";
        User user = new User();
        user.setEmail(userEmail);
        user.setPhone("1234567890");

        Appointment appointment = new Appointment();
        appointment.setFirstName("John");
        appointment.setLastName("Doe");
        appointment.setDate(LocalDate.now().plusDays(1).toString());
        appointment.setTime("10:00");
        appointment.setService("Eye Exam");

        when(principal.getName()).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        // Act
        Appointment createdAppointment = appointmentService.createAppointment(appointment, principal);

        // Assert
        assertNotNull(createdAppointment);
        assertEquals(userEmail, createdAppointment.getEmail());
        assertEquals("1234567890", createdAppointment.getPhone());
        assertEquals(user, createdAppointment.getUser());
        verify(appointmentRepository, times(1)).save(appointment);
    }

    @Test
    void testCreateAppointment_UserNotFound() {
        // Arrange
        String userEmail = "nonexistent@example.com";
        Appointment appointment = new Appointment();
        appointment.setFirstName("John");
        appointment.setLastName("Doe");
        appointment.setDate(LocalDate.now().plusDays(1).toString());
        appointment.setTime("10:00");
        appointment.setService("Eye Exam");

        when(principal.getName()).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            appointmentService.createAppointment(appointment, principal);
        });
    }

    @Test
    void testCreateAppointment_PastDate() {
        // Arrange
        String userEmail = "test@example.com";
        User user = new User();
        user.setEmail(userEmail);

        Appointment appointment = new Appointment();
        appointment.setFirstName("John");
        appointment.setLastName("Doe");
        appointment.setDate(LocalDate.now().minusDays(1).toString());
        appointment.setTime("10:00");
        appointment.setService("Eye Exam");

        when(principal.getName()).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.createAppointment(appointment, principal);
        });
    }

    @Test
    void testGetAllAppointments() {
        // Arrange
        when(appointmentRepository.findAll()).thenReturn(Collections.singletonList(new Appointment()));

        // Act
        List<Appointment> appointments = appointmentService.getAllAppointments();

        // Assert
        assertFalse(appointments.isEmpty());
        verify(appointmentRepository, times(1)).findAll();
    }

    @Test
    void testGetAppointmentsForUser() {
        // Arrange
        String userEmail = "test@example.com";
        User user = new User();
        user.setId(1L);
        user.setEmail(userEmail);

        when(principal.getName()).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(appointmentRepository.findByUser_Id(1L)).thenReturn(Collections.singletonList(new Appointment()));

        // Act
        List<Appointment> appointments = appointmentService.getAppointmentsForUser(principal);

        // Assert
        assertFalse(appointments.isEmpty());
        verify(appointmentRepository, times(1)).findByUser_Id(1L);
    }
}
