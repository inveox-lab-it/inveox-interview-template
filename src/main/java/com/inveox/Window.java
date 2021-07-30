package com.inveox;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Window {
    private final Duration size;
    private final int limit;
    private final Object lock = new Object();
    
    private TreeMap<Instant, AtomicInteger> instants = new TreeMap<>();
    
    public Window(Duration size, int limit) {
        this.size = size;
        this.limit = limit;
    }
    
    public boolean offer(Instant now) {
        synchronized (lock) {
            if (isFull(now)) {
                return false;
            }
            add(now);
            cleanUpOutdated(now);
        }
        return true;
    }
    
    private void add(Instant now) {
        instants.computeIfAbsent(now, i -> new AtomicInteger()).incrementAndGet();
        
    }
    
    private void cleanUpOutdated(Instant now) {
        instants.headMap(now.minus(size), true).clear();
    }
    
    private boolean isFull(Instant now) {
        long upToDateElements = instants.tailMap(now.minus(size), false).values().stream()
                .mapToLong(AtomicInteger::get)
                .sum();
        return upToDateElements >= limit;
    }
}
