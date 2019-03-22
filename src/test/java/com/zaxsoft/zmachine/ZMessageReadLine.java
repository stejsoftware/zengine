package com.zaxsoft.zmachine;

import java.util.Scanner;

public class ZMessageReadLine implements ZMessage {
    private static final Scanner scanner = new Scanner(System.in);

    private StringBuffer stringBuffer;

    public ZMessageReadLine(StringBuffer sb, int time) {
        this.stringBuffer = sb;
    }

    @Override
    public void process() {
        stringBuffer.append(scanner.nextLine());
    }
}
