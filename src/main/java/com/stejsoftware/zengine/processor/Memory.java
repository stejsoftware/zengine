package com.stejsoftware.zengine.processor;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Memory {
    private List<Byte> data;

    public void init(final List<Byte> data) {
        this.data = data;
    }

    public Byte read(final Integer address) {
        return this.data.get(address);
    }

    public void write(final Integer address, final Byte value) {
        if (address < this.data.size()) {
            this.data.set(address, value);
        }
    }
}
