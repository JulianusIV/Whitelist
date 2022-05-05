package com.julianusiv.whitelist.shared.results;

public class BoolCallResult extends CallResult {
    public boolean isValue() {
        return value;
    }
    public void setValue(boolean value) {
        this.value = value;
    }
    private boolean value;
}
