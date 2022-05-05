package com.julianusiv.whitelist.shared.results;

public class StringCallResult extends CallResult{
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    private String value;
}
