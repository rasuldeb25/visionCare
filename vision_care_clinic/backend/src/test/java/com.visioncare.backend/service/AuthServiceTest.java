package com.visioncare.backend.service;

import com.visioncare.backend.dto.LoginRequest;
import com.visioncare.backend.dto.RegisterRequest;
import com.visioncare.backend.model.User;
import com.visioncare.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPhone("1234567890");
        request.setDateOfBirth("2000-01-01");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        User registeredUser = authService.register(request);

        // Assert
        assertNotNull(registeredUser);
        assertEquals(request.getEmail(), registeredUser.getEmail());
        assertEquals("encodedPassword", registeredUser.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_EmailInUse() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            authService.register(request);
        });
    }

    @Test
    void testLogin_Success() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        User user = new User();
        user.setEmail(request.getEmail());

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("test_token");

        // Act
        String token = authService.login(request);

        // Assert
        assertEquals("test_token", token);
    }

    @Test
    void testUpdateUserProfile() {
        // Arrange
        String email = "test@example.com";
        User existingUser = new User();
        existingUser.setEmail(email);

        User updatedUser = new User();
        updatedUser.setFirstName("Jane");
        updatedUser.setLastName("Smith");
        updatedUser.setPhone("0987654321");
        updatedUser.setDateOfBirth("1990-01-01");
        updatedUser.setHasPet(true);
        updatedUser.setPetType("Dog");
        updatedUser.setPetName("Buddy");
        updatedUser.setPetFavoriteTreat("Bacon");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        User result = authService.updateUserProfile(email, updatedUser);

        // Assert
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("0987654321", result.getPhone());
        assertEquals("1990-01-01", result.getDateOfBirth());
        assertTrue(result.getHasPet());
        assertEquals("Dog", result.getPetType());
        assertEquals("Buddy", result.getPetName());
        assertEquals("Bacon", result.getPetFavoriteTreat());
    }
}
