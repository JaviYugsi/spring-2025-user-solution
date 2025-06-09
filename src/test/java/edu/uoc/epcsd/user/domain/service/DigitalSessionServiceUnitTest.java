package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.domain.User;
import edu.uoc.epcsd.user.domain.exception.UserNotFoundException;
import edu.uoc.epcsd.user.domain.repository.DigitalSessionRepository;
import edu.uoc.epcsd.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DigitalSessionServiceUnitTest {

    private DigitalSessionRepository digitalSessionRepository;

    private UserRepository userRepository;

    private DigitalSessionService sut;

    @BeforeEach
    void setUp() {
        digitalSessionRepository = mock(DigitalSessionRepository.class);
        userRepository = mock(UserRepository.class);
        sut = new DigitalSessionServiceImpl(digitalSessionRepository, userRepository);
    }

    @Test
    void findDigitalSessionByUser_shouldReturnListOfDigitalSession() {

        User user = User.builder()
                .id(1L)
                .fullName("Test User")
                .email("test@test.com")
                .password("password")
                .phoneNumber("1234567890").build();

        DigitalSession digitalSession = DigitalSession.builder() .description("Sesión de prueba")
                .link("https://link.com")
                .location("Barcelona")
                .userId(user.getId())
                .build();

        when(userRepository.findUserById(user.getId())).thenReturn(Optional.of(user));
        when(digitalSessionRepository.findDigitalSessionByUser(anyLong())).thenReturn(List.of(digitalSession));
        List<DigitalSession> digitalSessionByUserResult = sut.findDigitalSessionByUser(user.getId());

        assertNotNull(digitalSessionByUserResult);
        assertFalse(digitalSessionByUserResult.isEmpty());
        assertEquals(digitalSession.getId(), digitalSessionByUserResult.get(0).getId());
    }

    @Test
    void findDigitalSessionByUser_shouldThrowExceptionWhenUserDoesNotExist() {
        when(userRepository.findUserById(anyLong())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, ()  -> sut.findDigitalSessionByUser(anyLong()));
    }
}