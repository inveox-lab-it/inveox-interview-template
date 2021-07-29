package com.inveox;

import java.util.UUID;

public class MachinePayload {
    private final UUID machineId;
    private final Object payload;


    public MachinePayload(UUID machineId, Object payload) {
        this.machineId = machineId;
        this.payload = payload;
    }

    public UUID getMachineId() {
        return machineId;
    }

    public Object getPayload() {
        return payload;
    }
}


