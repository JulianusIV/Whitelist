package com.julianusiv.whitelist.shared.results;

public class CallResult {
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    private boolean success;
    private String message;
}
