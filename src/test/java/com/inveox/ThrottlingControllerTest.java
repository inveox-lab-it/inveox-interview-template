package com.inveox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;

public class ThrottlingControllerTest {
    
    
    @Test
    void shouldFailWhenExceedsRate() {
        //given
        ThrottlingController.CurrentTime currentTime = mock(ThrottlingController.CurrentTime.class);
        given(currentTime.isSameSecond(anyLong())).willReturn(true);
        ThrottlingController controller = new ThrottlingController(1, currentTime);
        MachinePayload payload = mock(MachinePayload.class);
        controller.shouldThrottle(payload);
        
        
        //when
        boolean throttle = controller.shouldThrottle(payload);
        
        //then
        assertTrue(throttle);
    }
    
    
    @Test
    void shouldPassFirstCall() {
        //given
        ThrottlingController controller = new ThrottlingController(1, new ThrottlingController.CurrentTime());
        MachinePayload payload = mock(MachinePayload.class);
        
        //when
        boolean throttle = controller.shouldThrottle(payload);
        
        //then
        assertFalse(throttle);
    }
    
}