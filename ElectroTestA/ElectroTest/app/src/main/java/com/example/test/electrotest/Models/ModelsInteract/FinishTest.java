package com.example.test.electrotest.Models.ModelsInteract;

public class FinishTest {
    private int Value;
    private boolean Timeout;


    public int getValue() {
        return Value;
    }

    public void setValue(int value) {
        Value = value;
    }

    public boolean isTimeout() {
        return Timeout;
    }

    public void setTimeout(boolean timeout) {
        Timeout = timeout;
    }

    public FinishTest(int value, boolean timeout) {
        Value = value;
        Timeout = timeout;
    }
}
