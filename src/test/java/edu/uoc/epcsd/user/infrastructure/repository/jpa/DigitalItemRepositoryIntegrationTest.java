package edu.uoc.epcsd.user.infrastructure.repository.jpa;

import edu.uoc.epcsd.user.domain.DigitalItem;
import edu.uoc.epcsd.user.domain.DigitalItemStatus;
import edu.uoc.epcsd.user.domain.repository.DigitalItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DigitalItemRepositoryIntegrationTest {

    @Autowired
    private DigitalItemRepository digitalItemRepository;

    @Autowired
    private SpringDataDigitalSessionRepository digitalSessionRepository;

    @Autowired
    private SpringDataUserRepository userRepository;

    private Long digitalSessionId;

    @BeforeEach
    void setUp() {

        UserEntity userEntity = UserEntity.builder()
                .fullName("Test User")
                .email("test@test.com")
                .password("password")
                .phoneNumber("1234567890")
                .build();
        userRepository.save(userEntity);

        DigitalSessionEntity digitalSession = DigitalSessionEntity.builder()
                .description("Sesión de prueba")
                .link("https://link.com")
                .location("Barcelona")
                .user(userEntity)
                .build();
        digitalSessionId = digitalSessionRepository.save(digitalSession).getId();
    }

    @Test
    void whenCallingFindDigitalItemBySession_shouldReturnListWihObjectsWithProperSessionId() {

        DigitalItem digitalItem = DigitalItem.builder()
                .digitalSessionId(digitalSessionId)
                .description("test description")
                .lon(13L)
                .lat(31L)
                .link("link.test.com")
                .status(DigitalItemStatus.AVAILABLE)
                .build();
        digitalItemRepository.createDigitalItem(digitalItem);

        DigitalItem digitalItem2 = DigitalItem.builder()
                .digitalSessionId(digitalSessionId)
                .description("test description2")
                .lon(13L)
                .lat(31L)
                .link("link.test2.com")
                .status(DigitalItemStatus.AVAILABLE)
                .build();
        digitalItemRepository.createDigitalItem(digitalItem2);

        List<DigitalItem> digitalItemsBySession = digitalItemRepository.findDigitalItemBySession(digitalSessionId);

        assertThat(digitalItemsBySession.isEmpty()).isFalse();
        assertThat(digitalItemsBySession.size()).isEqualTo(2);
        assertThat(digitalItemsBySession.get(0).getDigitalSessionId()).isEqualTo(digitalSessionId);
        assertThat(digitalItemsBySession.get(1).getDigitalSessionId()).isEqualTo(digitalSessionId);
    }


}