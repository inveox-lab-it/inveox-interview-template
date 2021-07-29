package com.inveox;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class ThrottlingControllerTest {


    @Test
    void shouldFailWhenExceedsRate() {
        //given
        ThrottlingController controller = new ThrottlingController(1);
        MachinePayload payload = mock(MachinePayload.class);
        controller.shouldThrottle(payload);

        //when
        boolean throttle = controller.shouldThrottle(payload);

        //then
        assertTrue(throttle);
    }

}