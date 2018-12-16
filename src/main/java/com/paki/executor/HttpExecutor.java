package com.paki.executor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paki.command.Command;
import com.paki.command.OperationCommand;
import com.paki.command.Option;
import com.paki.util.JsonFormatUtil;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public interface HttpExecutor {
    void execute(Command command) throws IOException, ExecutorException, CGCServerException;

    default Map<String, String> getCommonHeaders(OperationCommand command) throws ExecutorException {
        Map<String, String> headers = new HashMap<>();
        Set<String> mandatoryOptions = new HashSet<>(Arrays.asList("token"));

        for (Option option: command.getOptions()) {
            if ("token".equals(option.getName())) {
                headers.put("X-SBG-Auth-Token", option.getValue());
            }
            mandatoryOptions.remove(option.getName());
        }

        if (!mandatoryOptions.isEmpty()) {
            throw new ExecutorException(String.format("Missing mandatory options: [%s].", String.join(", ", mandatoryOptions.stream().map(o -> "--" + o).collect(Collectors.toSet()))));
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
            }
        }
        return null;
    }

    default void validateResponse(Response response) throws IOException, CGCServerException {
        if (!response.isSuccessful()) {
            JsonObject responseJson = new JsonParser().parse(response.body().string()).getAsJsonObject();
            throw new CGCServerException(JsonFormatUtil.jsonObjectToPrintableString(responseJson));
        }
    }
}
