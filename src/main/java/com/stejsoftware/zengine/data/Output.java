package com.stejsoftware.zengine.data;

public class Output {
    String name = "None";

    public static Output ok(String message) {
        Output ok = new Output();
        ok.setName(message);
        return ok;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
