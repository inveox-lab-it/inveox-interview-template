package com.inveox;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class WindowTest {
    @Test
    void shouldBeSameWindowOnTheEdge() {
        //given
        int limit = 3;
        Duration window = Duration.ofSeconds(2);
        Window timer = new Window(window, limit);
        
        //when then
        Instant startTime = Instant.now();
        
        assertTrue(timer.offer(startTime));
        
        Instant inASecond = startTime.plusSeconds(1);
        
        assertTrue(timer.offer(inASecond));
        assertTrue(timer.offer(inASecond));
        assertFalse(timer.offer(inASecond));
        
        Instant inTwoSeconds = startTime.plusSeconds(2);
    
        assertTrue(timer.offer(inTwoSeconds));
        assertFalse(timer.offer(inTwoSeconds));
    
        Instant inThreeSeconds = startTime.plusSeconds(3);
    
        assertTrue(timer.offer(inThreeSeconds));
        assertTrue(timer.offer(inThreeSeconds));
        assertFalse(timer.offer(inThreeSeconds));
    }
}