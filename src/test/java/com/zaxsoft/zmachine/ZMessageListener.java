package com.zaxsoft.zmachine;

import java.util.List;

public interface ZMessageListener<T> {
    void onData(List<T> data);

    void onDone();
}
