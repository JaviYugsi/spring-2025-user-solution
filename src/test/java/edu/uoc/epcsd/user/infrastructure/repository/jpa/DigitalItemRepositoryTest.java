package edu.uoc.epcsd.user.infrastructure.repository.jpa;

import edu.uoc.epcsd.user.domain.DigitalItem;
import edu.uoc.epcsd.user.domain.DigitalItemStatus;
import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.domain.repository.DigitalItemRepository;
import edu.uoc.epcsd.user.domain.repository.DigitalSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DigitalItemRepositoryTest {

    @Autowired
    private DigitalItemRepository digitalItemRepository;

    @Autowired
    private SpringDataDigitalSessionRepository digitalSessionRepository;

    @Autowired
    private SpringDataUserRepository userRepository;

    private Long digitalSessionId;

    @BeforeEach
    void setUp() {

        UserEntity userEntity = new UserEntity();
        userEntity.setFullName("Test User");
        userEntity.setEmail("test@test.com");
        userEntity.setPassword("password");
        userEntity.setPhoneNumber("1234567890");
        userRepository.save(userEntity);

        DigitalSessionEntity digitalSession = new DigitalSessionEntity();
        digitalSession.setDescription("Sesión de prueba");
        digitalSession.setLink("https://link.com");
        digitalSession.setLocation("Barcelona");
        digitalSession.setUser(userEntity);

        digitalSessionId = digitalSessionRepository.save(digitalSession).getId();
    }

    @Test
    void findDigitalItemBySession() {

        DigitalItem digitalItem = new DigitalItem();
        digitalItem.setDigitalSessionId(digitalSessionId);
        digitalItem.setDescription("test description");
        digitalItem.setLon(13L);
        digitalItem.setLat(31L);
        digitalItem.setLink("link.test.com");
        digitalItem.setStatus(DigitalItemStatus.AVAILABLE);

        Long itemId = digitalItemRepository.createDigitalItem(digitalItem);

        assertNotNull(itemId);

        Optional<DigitalItem> digitalItemById = digitalItemRepository.getDigitalItemById(itemId);
        assertThat(digitalItemById.isPresent()).isTrue();
        assertThat(digitalItemById.get().getDescription()).isEqualTo("test description");

    }

    @Test
    void findDigitalItemsBySession() {

        DigitalItem digitalItem = new DigitalItem();
        digitalItem.setDigitalSessionId(digitalSessionId);
        digitalItem.setDescription("test description");
        digitalItem.setLon(13L);
        digitalItem.setLat(31L);
        digitalItem.setLink("link.test.com");
        digitalItem.setStatus(DigitalItemStatus.AVAILABLE);
        digitalItemRepository.createDigitalItem(digitalItem);

        DigitalItem digitalItem2 = new DigitalItem();
        digitalItem2.setDigitalSessionId(digitalSessionId);
        digitalItem2.setDescription("test description 2");
        digitalItem2.setLon(13L);
        digitalItem2.setLat(31L);
        digitalItem2.setLink("link.test2.com");
        digitalItem2.setStatus(DigitalItemStatus.AVAILABLE);
        digitalItemRepository.createDigitalItem(digitalItem);


        List<DigitalItem> digitalItemsBySession = digitalItemRepository.findDigitalItemBySession(digitalSessionId);

        assertThat(digitalItemsBySession.isEmpty()).isFalse();
        assertThat(digitalItemsBySession.size()).isEqualTo(2);
    }


}