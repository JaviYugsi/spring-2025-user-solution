package edu.uoc.epcsd.user.application.rest;

import edu.uoc.epcsd.user.domain.DigitalItem;
import edu.uoc.epcsd.user.domain.DigitalItemStatus;
import edu.uoc.epcsd.user.infrastructure.repository.jpa.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DigitalItemRESTControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private SpringDataDigitalItemRepository itemRepository;

    @Autowired
    private SpringDataDigitalSessionRepository sessionRepository;

    @Autowired
    private SpringDataUserRepository userRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private Long sessionId;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
        sessionRepository.deleteAll();
        userRepository.deleteAll();

        UserEntity userEntity = UserEntity.builder()
                .fullName("Test User")
                .email("test@test.com")
                .password("password")
                .phoneNumber("1234567890")
                .build();
        userRepository.save(userEntity);

        DigitalSessionEntity session = DigitalSessionEntity.builder()
                .description("Sesión de prueba")
                .link("https://link.com")
                .location("Barcelona")
                .user(userEntity)
                .build();
        session = sessionRepository.save(session);
        sessionId = session.getId();

        DigitalItemEntity item = DigitalItemEntity.builder()
                .description("Item 1")
                .lat(41L)
                .lon(2L)
                .link("http://link.item")
                .status(DigitalItemStatus.AVAILABLE)
                .digitalSession(session)
                .build();
        itemRepository.save(item);
    }

    @Test
    void whenCallingFindDigitalItemBySession_shouldReturnListWihObjectsWithProperSessionId() {
        String url = "http://localhost:" + port + "/digitalItem/digitalItemBySession?digitalSessionId=" + sessionId;

        ResponseEntity<List<DigitalItem>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getDescription()).isEqualTo("Item 1");
        assertThat(response.getBody().get(0).getDigitalSessionId()).isEqualTo(sessionId);
    }

}