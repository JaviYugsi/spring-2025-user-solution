package edu.uoc.epcsd.user.application.rest;

import edu.uoc.epcsd.user.domain.service.DigitalItemService;
import edu.uoc.epcsd.user.domain.service.DigitalItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.BDDAssumptions.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    void findDigitalItemBySession() throws Exception {

        when(digitalItemService.findDigitalItemBySession(anyLong())).thenReturn(anyList());

        MockMvc mockMvc = standaloneSetup(digitalItemRESTController).build();
        mockMvc.perform(get("/digitalItem/digitalItemBySession").param("digitalSessionId", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}