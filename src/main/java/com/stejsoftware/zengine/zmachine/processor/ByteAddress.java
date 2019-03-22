package com.stejsoftware.zengine.zmachine.processor;

public class ByteAddress implements Address {

    private Short value;

    public ByteAddress(Short value) {
        this.value = value;
    }

    @Override
    public Short get() {
        return value;
    }
}
