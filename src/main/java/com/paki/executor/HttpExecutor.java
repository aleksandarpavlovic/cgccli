package com.paki.executor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.paki.command.Command;
import com.paki.command.Option;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public interface HttpExecutor {
    void execute(Command command) throws IOException;

    default Map<String, String> getCommonHeaders(Command command) {
        Map<String, String> headers = new HashMap<>();
        for (Option option: command.getOptions()) {
            if ("token".equals(option.getName()))
                headers.put("X-SBG-Auth-Token", option.getValue());
            else {
                // unrecognized header error
            }
        }
        return headers;
    }

    default String extractNextPageUrl(JsonObject responseJson) {
        JsonArray links = responseJson.getAsJsonArray("links");
        if (links == null || links.size() == 0)
            return null;
        for (JsonElement link: links) {
            if ("next".equals(link.getAsJsonObject().get("rel").getAsString())) {
                return link.getAsJsonObject().get("href").getAsString();
            } else {
                // error unsupported option
            }
        }
        return null;
    }
}
