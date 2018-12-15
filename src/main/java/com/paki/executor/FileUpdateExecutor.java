package com.paki.executor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paki.command.Assignment;
import com.paki.command.Command;
import com.paki.command.Option;
import com.squareup.okhttp.*;

import java.io.IOException;
import java.util.Map;

public class FileUpdateExecutor implements HttpExecutor {
    @Override
    public void execute(Command command) throws IOException {
        OkHttpClient client = new OkHttpClient();
        JsonParser jsonParser = new JsonParser();

        String url = buildUrl("https://cgc-api.sbgenomics.com/v2/files", command);
        Request request = buildRequest(url, command);
        Response response = client.newCall(request).execute();

        JsonObject responseJson = jsonParser.parse(response.body().string()).getAsJsonObject();
        processResponseBody(responseJson);
    }

    private void processResponseBody(JsonObject responseJson) {
        System.out.println(responseJson.toString());
    }

    private Request buildRequest(String url, Command command) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        Map<String, String> commonHeaders = getCommonHeaders(command);
        for (Map.Entry<String, String> header: commonHeaders.entrySet()) {
            requestBuilder.addHeader(header.getKey(), header.getValue());
        }
        requestBuilder.addHeader("Content-Type", "application/json");
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), buildRequestBody(command));
        return requestBuilder.patch(body).build();
    }

    private String buildUrl(String url, Command command) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        for (Option option: command.getOperation().getOptions()) {
            if ("file".equals(option.getName())) {
                urlBuilder.addPathSegment(option.getValue());
                break;
            }
        }
        return urlBuilder.build().toString();
    }

    private String buildRequestBody(Command command) {
        JsonObject json = new JsonObject();
        for (Assignment assignment: command.getOperation().getAssignments()) {
            if (isMetadata(assignment.getKey())) {
                if (json.get("metadata") == null) {
                    json.add("metadata", new JsonObject());
                }
                json.get("metadata").getAsJsonObject().addProperty(extractMetadataName(assignment.getKey()), assignment.getValue());
            } else {
                json.addProperty(assignment.getKey(), assignment.getValue());
            }
        }
        return json.toString();
    }

    private boolean isMetadata(String s) {
        return s.matches("metadata\\..+");
    }

    private String extractMetadataName(String s) {
        return s.split("\\.", 2)[1];
    }
}
