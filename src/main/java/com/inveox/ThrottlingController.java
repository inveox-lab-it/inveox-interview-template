package com.inveox;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;

public class ThrottlingController {

    private final int ratePerSecond;
    private int counterPerSecond = 0;
    private long currentSecond;
    private CurrentTime currentTime;

    public ThrottlingController(int ratePerSecond, CurrentTime currentTime) {
        this.ratePerSecond = ratePerSecond;
        this.currentTime = currentTime;
    }

    /**
     * Whether next call should be throttled or not.
     * This method accepts machine payload and stores the current state of the processed calls.
     * If the call exceeds the available rate in the current second it is throttled (true is returned).
     * If the call is within the available rate in the current second false is returned. (ver.1)
     *
     * It is a matter of the implementation if limit should be reset every second or in provided time duration. (ver.2)
     *
     * Throttling can be based on the machine id available in the payload - not necessary in the basic implementation.
     * This is a matter of the implementation how requests are spread between machines. (ver.3)
     *
     * @param payload
     * @return whether call should be throttled (true) or not (false)
     */
    boolean shouldThrottle(MachinePayload payload) {
    
    
        long epochSecond = Instant.now().getEpochSecond();
        if (counterPerSecond > ratePerSecond && currentTime.isSameSecond(epochSecond))  {
            return true;
        }
        
        counterPerSecond++;
        
        currentTime.set(epochSecond);

        return false;
    }
    
    public static class CurrentTime {
        private long currentSecond;
        
        public CurrentTime() {
            this.currentSecond = Instant.now().getEpochSecond();
            
        }
        
        public void set(long currentSecond) {
            this.currentSecond = currentSecond;
        }
        
        public boolean isSameSecond(long epochSecond) {
            return currentSecond == epochSecond;
        }
    }
}