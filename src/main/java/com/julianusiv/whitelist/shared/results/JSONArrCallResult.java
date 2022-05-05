package com.julianusiv.whitelist.shared.results;

import org.json.simple.JSONArray;

public class JSONArrCallResult extends CallResult{
    public JSONArray getValue() {
        return value;
    }
    public void setValue(JSONArray value) {
        this.value = value;
    }
    private JSONArray value;
}
