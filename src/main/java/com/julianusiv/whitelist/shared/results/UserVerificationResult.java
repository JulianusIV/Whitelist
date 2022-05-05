package com.julianusiv.whitelist.shared.results;

public class UserVerificationResult extends CallResult{
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUUID() {
        return uuid;
    }
    public void setUUID(String uuid) {
        this.uuid = uuid;
    }
    public boolean isValid() {
        return valid;
    }
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    private String username;
    private String uuid;
    private boolean valid;
}
