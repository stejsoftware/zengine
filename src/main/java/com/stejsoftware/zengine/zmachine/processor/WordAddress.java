package com.stejsoftware.zengine.zmachine.processor;

public class WordAddress implements Address {
    private Short value;

    public WordAddress(Short value) {
        this.value = value;
    }

    @Override
    public Short get() {
        return null;
    }
}
