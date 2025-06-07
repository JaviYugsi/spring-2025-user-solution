package edu.uoc.epcsd.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DigitalItemUnitTest {

    private DigitalItem digitalItem;

    @BeforeEach
    void setUp() {
        digitalItem = new DigitalItem();
    }

    @Test
    public void whenDigitalItemIsCreated_shouldStatusBeAvailable() {
        assertEquals(DigitalItemStatus.AVAILABLE, digitalItem.getStatus());
    }
}