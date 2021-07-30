package com.inveox;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ThrottlingController {
    
    private final Clock clock;
    private final Map<UUID, Window> machineWindowMap = new ConcurrentHashMap<>();
    private final Supplier<Window> windowSupplier;
    
    
    ThrottlingController(Clock clock, Supplier<Window> windowSupplier) {
        this.clock = clock;
        this.windowSupplier = windowSupplier;
    }
    
    ThrottlingController(Clock clock, int ratePerWindow, Duration windowSize) {
        this(clock, () -> new Window(windowSize, ratePerWindow));
    }
    
    public ThrottlingController(int ratePerWindow, Duration windowSize) {
        this(new Clock(), ratePerWindow, windowSize);
    }
    
    /**
     * Whether next call should be throttled or not.
     * This method accepts machine payload and stores the current state of the processed calls.
     * If the call exceeds the available rate in the current second it is throttled (true is returned).
     * If the call is within the available rate in the current second false is returned. (ver.1)
     * <p>
     * It is a matter of the implementation if limit should be reset every second or in provided time duration. (ver.2)
     * <p>
     * Throttling can be based on the machine id available in the payload - not necessary in the basic implementation.
     * This is a matter of the implementation how requests are spread between machines. (ver.3)
     *
     * @param payload
     * @return whether call should be throttled (true) or not (false)
     */
    boolean shouldThrottle(MachinePayload payload) {
        Window window = machineWindowMap.computeIfAbsent(payload.getMachineId(), id -> windowSupplier.get());
        
        return !window.offer(clock.now());
    }
}