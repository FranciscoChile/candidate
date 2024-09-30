package com.pancho.demo;


import com.pancho.demo.config.TokenProvider;
import com.pancho.demo.model.*;
import com.pancho.demo.persistence.CandidateRepository;
import com.pancho.demo.persistence.UserRepository;
import com.pancho.demo.service.CandidateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DemoApplicationTests {

    @InjectMocks
    private CandidateService candidateService;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Candidate candidate;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        candidate = new Candidate(1L, "pancho", "email", Gender.MALE, "3000", WorkModel.REMOTE);
        user = new User(1L, "username", "password", null);
    }

    @Test
    void testFindById() {
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        Optional<CandidateDTO> foundCandidate = candidateService.findById(1L);
        assert(foundCandidate.isPresent());
        assertEquals("pancho", foundCandidate.get().getName());
    }

    @Test
    void testSave() {
        CandidateRequest request = new CandidateRequest(null, "pancho", "email", Gender.MALE, "3000", WorkModel.REMOTE);
        when(candidateRepository.save(Mockito.any(Candidate.class))).thenReturn(candidate);

        CandidateDTO savedCandidate = candidateService.save(request);

        assertNotNull(savedCandidate);
        assertEquals("pancho", savedCandidate.getName());
    }

    @Test
    void testDelete() {
        doNothing().when(candidateRepository).deleteById(1L);
        candidateService.delete(1L);
        verify(candidateRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRegisterUser_Success() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User registeredUser = candidateService.registerUser(user);

        assertNotNull(registeredUser);
        assertEquals("username", registeredUser.getUsername());
    }

    @Test
    void testRegisterUser_UsernameExists() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            candidateService.registerUser(user);
        });

        assertEquals("User exists", exception.getMessage());
    }

    @Test
    void testLoginAndGetToken_Success() {
        String token = "token";
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenProvider.createToken(authentication)).thenReturn(token);

        String resultToken = candidateService.loginAndGetToken("username", "password");

        assertEquals(token, resultToken);
    }

    @Test
    void testLoginAndGetToken_InvalidCredentials() {
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenThrow(new RuntimeException("Invalid credentials"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            candidateService.loginAndGetToken("username", "wrongpassword");
        });

        assertEquals("Invalid credentials", exception.getMessage());
    }


}
