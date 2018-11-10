package com.stejsoftware.zengine.processor;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Memory {
    private List<Byte> data;
    private List<Integer> bytes;

    public void init(final List<Byte> data) {
        this.data = data;
    }

    public Integer readByte(final Integer address) {
        return (data.get(address) & 0xff);
    }

    public Integer readWord(final Integer address) {
        return (((data.get(address) << 8) | (data.get(address + 1) & 0xff)) & 0xffff);
    }

    public List<Integer> readBytes(final Integer address, final Integer length) {
        List<Integer> bytes = new ArrayList<>();

        data.subList(address, address + length).forEach(b -> {
            bytes.add(b.intValue() & 0xff);
        });

        return bytes;
    }

    public List<Integer> readWords(final Integer address) {
        return new ArrayList<>();
    }

    public void writeByte(final Integer address, final Integer value) {
    }

    public void writeBytes(final Integer address, final List<Integer> values) {
    }

    public void writeWord(final Integer address, final Integer value) {
    }

    public void writeWords(final Integer address, final List<Integer> values) {
    }
}
