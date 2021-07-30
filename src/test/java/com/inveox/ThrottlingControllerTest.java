package com.inveox;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ThrottlingControllerTest {
    
    private static final Object ANY_PAYLOAD = null;
    
    @Test
    void shouldFailWhenExceedsRateOnSameMachine() {
        //given
        Clock clock = mock(Clock.class);
        Instant now = Instant.now();
        Instant inHalfOfASecond = now.plusMillis(500);
        Instant inASecond = now.plusSeconds(1);
        
        given(clock.now()).willReturn(now, inHalfOfASecond, inASecond);
        
        int ratePerWindow = 1;
        Duration windowSize = Duration.ofSeconds(1);
        ThrottlingController controller = new ThrottlingController(clock, ratePerWindow, windowSize);
        
        MachinePayload payload = aPayloadForMachine(UUID.randomUUID());
        
        //when then
        assertFalse(controller.shouldThrottle(payload));
        assertTrue(controller.shouldThrottle(payload));
        assertFalse(controller.shouldThrottle(payload));
    }
    
    @Test
    void shouldNotInterfereWithDifferentMachines() {
        //given
        Clock clock = mock(Clock.class);
    
        given(clock.now()).willReturn(Instant.now());
        
        int ratePerWindow = 1;
        Duration windowSize = Duration.ofSeconds(1);
        ThrottlingController controller = new ThrottlingController(clock, ratePerWindow, windowSize);
        
        MachinePayload payloadForMachine1 = aPayloadForMachine(UUID.randomUUID());
        MachinePayload payloadForMachine2 = aPayloadForMachine(UUID.randomUUID());
        
        //when then
        assertFalse(controller.shouldThrottle(payloadForMachine1));
        assertFalse(controller.shouldThrottle(payloadForMachine2));
    }
    
    @Test
    void shouldFailWhenExceedsRateOnSameMachineMultithreaded() throws Exception {
        //given
        ThrottlingController controller = new ThrottlingController(1000, Duration.ofMinutes(10));
        MachinePayload payloadForMachine1 = aPayloadForMachine(UUID.randomUUID());
        MachinePayload payloadForMachine2 = aPayloadForMachine(UUID.randomUUID());
        
        int numberOfThreads = 20;
        CyclicBarrier barrier = new CyclicBarrier(numberOfThreads);
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        
        //when then
        IntStream.range(0, numberOfThreads/2).forEach(i -> executor.submit(() -> {
            barrier.await();
            IntStream.range(0, 100).forEach(i2 -> assertFalse(controller.shouldThrottle(payloadForMachine1)));
            return null;
        }));
        IntStream.range(numberOfThreads/2, numberOfThreads).forEach(i -> executor.submit(() -> {
            barrier.await();
            IntStream.range(0, 100).forEach(i2 -> assertFalse(controller.shouldThrottle(payloadForMachine2)));
            return null;
        }));
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        assertTrue(controller.shouldThrottle(payloadForMachine1));
        assertTrue(controller.shouldThrottle(payloadForMachine2));
    }
    
    private MachinePayload aPayloadForMachine(UUID machineId) {
        return new MachinePayload(machineId, ANY_PAYLOAD);
    }
}