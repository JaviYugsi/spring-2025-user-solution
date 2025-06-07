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

        when(userRepository.findUserById(anyLong())).thenReturn(Optional.of(new User()));
        when(digitalSessionRepository.findDigitalSessionByUser(anyLong())).thenReturn(List.of(new DigitalSession()));
        List<DigitalSession> digitalSessionByUserResult = sut.findDigitalSessionByUser(anyLong());

        assertNotNull(digitalSessionByUserResult);
        assertFalse(digitalSessionByUserResult.isEmpty());
    }

    @Test
    void findDigitalSessionByUser_shouldThrowExceptionWhenUserDoesNotExist() {
        when(userRepository.findUserById(anyLong())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, ()  -> sut.findDigitalSessionByUser(anyLong()));
    }
}