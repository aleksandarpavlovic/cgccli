package com.paki.executor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paki.command.Command;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;

public class ProjectListExecutor implements HttpExecutor {
    @Override
    public void execute(Command command) throws IOException {
        OkHttpClient client = new OkHttpClient();
        JsonParser jsonParser = new JsonParser();

        String nextPageUrl = "https://cgc-api.sbgenomics.com/v2/projects?offset=0&limit=1";
        while (nextPageUrl != null) {
            Request request = buildRequest(nextPageUrl, command);
            Response response = client.newCall(request).execute();

            JsonObject responseJson = jsonParser.parse(response.body().string()).getAsJsonObject();
            processResponseBody(responseJson);
            nextPageUrl = extractNextPageUrl(responseJson);
        }
    }

    private Request buildRequest(String url, Command command) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        Map<String, String> commonHeaders = getCommonHeaders(command);
        for (Map.Entry<String, String> header: commonHeaders.entrySet()) {
            requestBuilder.addHeader(header.getKey(), header.getValue());
        }
        return requestBuilder.build();
    }

    private void processResponseBody(JsonObject responseJson) {
        System.out.println(responseJson.toString());
    }
}
