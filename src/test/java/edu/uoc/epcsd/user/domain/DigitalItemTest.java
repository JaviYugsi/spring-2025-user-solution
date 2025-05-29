package edu.uoc.epcsd.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DigitalItemTest {

    private DigitalItem digitalItem;

    @BeforeEach
    void setUp() {
        digitalItem = new DigitalItem();
    }

    @Test
    public void shouldStatusBeAvailable() {
        assertEquals(DigitalItemStatus.AVAILABLE, digitalItem.getStatus());
    }
}