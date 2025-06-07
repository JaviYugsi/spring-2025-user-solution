package edu.uoc.epcsd.user.application.rest;

import edu.uoc.epcsd.user.domain.DigitalItem;
import edu.uoc.epcsd.user.domain.DigitalItemStatus;
import edu.uoc.epcsd.user.domain.service.DigitalItemService;
import edu.uoc.epcsd.user.domain.service.DigitalItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class DigitalItemRESTControllerUnitTest {

    private DigitalItemService digitalItemService;
    private DigitalItemRESTController digitalItemRESTController;

    @BeforeEach
    void setUp() {
        digitalItemService = mock(DigitalItemServiceImpl.class);
        digitalItemRESTController = new DigitalItemRESTController(digitalItemService);

    }

    @Test
    void whenCallingFindDigitalItemBySession_shouldReturnListWihObjectsWithProperSessionId() throws Exception {

        Long sessionId = 1L;

        DigitalItem item1 = DigitalItem.builder()
                .id(1L)
                .digitalSessionId(sessionId)
                .description("Test Item 1")
                .lat(13L)
                .lon(46L)
                .link("ejemplo1.com")
                .status(DigitalItemStatus.AVAILABLE)
                .build();

        DigitalItem item2 = DigitalItem.builder()
                .id(2L)
                .digitalSessionId(sessionId)
                .description("Test Item 2")
                .lat(79L)
                .lon(31L)
                .link("ejemplo2.com")
                .status(DigitalItemStatus.AVAILABLE)
                .build();

        List<DigitalItem> resultado = List.of(item1, item2);

        when(digitalItemService.findDigitalItemBySession(anyLong())).thenReturn(resultado);

        MockMvc mockMvc = standaloneSetup(digitalItemRESTController).build();
        mockMvc.perform(get("/digitalItem/digitalItemBySession").param("digitalSessionId", sessionId.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].digitalSessionId").value(sessionId))
                .andExpect(jsonPath("$[1].digitalSessionId").value(sessionId))
                .andReturn();

    }
}