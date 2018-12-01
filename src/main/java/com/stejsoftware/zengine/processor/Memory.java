package com.stejsoftware.zengine.processor;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Memory {
    private List<Byte> data;

    public void init(final List<Byte> data) {
        this.data = data;
    }

    public Byte readByte(final Integer address) {
        return data.get(address);
    }

    public Integer readWord(final Integer address) {
        return (((data.get(address) << 8) | (data.get(address + 1) & 0xff)) & 0xffff);
    }

    public List<Byte> readBytes(final int address, final int length) {
        return data.subList(address, address + length);
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
