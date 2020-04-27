package com.pgleska.sklepallegro.connector.api.model;

import com.google.gson.Gson;

public abstract class ApiDTO {
    public String toJson() {
        return new Gson().toJson(this);
    }
}