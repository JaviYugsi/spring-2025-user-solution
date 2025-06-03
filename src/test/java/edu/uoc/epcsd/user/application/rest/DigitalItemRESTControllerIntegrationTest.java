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

        UserEntity userEntity = new UserEntity();
        userEntity.setFullName("Test User");
        userEntity.setEmail("test@test.com");
        userEntity.setPassword("password");
        userEntity.setPhoneNumber("1234567890");
        userRepository.save(userEntity);

        DigitalSessionEntity session = new DigitalSessionEntity();
        session.setDescription("Test Session");
        session.setLocation("Barcelona");
        session.setLink("http://test.session");
        session.setUser(userEntity);
        session = sessionRepository.save(session);
        sessionId = session.getId();

        DigitalItemEntity item = new DigitalItemEntity();
        item.setDescription("Item 1");
        item.setLat(41L);
        item.setLon(2L);
        item.setLink("http://link.item");
        item.setStatus(DigitalItemStatus.AVAILABLE);
        item.setDigitalSession(session);
        itemRepository.save(item);
    }

    @Test
    void shouldReturnDigitalItemsBySessionId() {
        // Arrange
        String url = "http://localhost:" + port + "/digitalItem/digitalItemBySession?digitalSessionId=" + sessionId;

        // Act
        ResponseEntity<List<DigitalItem>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getDescription()).isEqualTo("Item 1");
    }

}