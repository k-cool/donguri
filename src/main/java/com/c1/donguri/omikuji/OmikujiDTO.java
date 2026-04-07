package com.c1.donguri.omikuji;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OmikujiDTO {
    public String omikujiId;
    public String luck;
    public String message;
    public String createdAt;
    public String updatedAt;

    public JsonObject toJsonObject() {
        return JsonParser.parseString(new Gson().toJson(this)).getAsJsonObject();
    }
}
