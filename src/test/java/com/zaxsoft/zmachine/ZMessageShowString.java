package com.zaxsoft.zmachine;

public class ZMessageShowString implements ZMessage {
    private String string;

    public ZMessageShowString(String string) {
        this.string = string;
    }

    @Override
    public void process() {
        System.out.print(string);
    }
}
