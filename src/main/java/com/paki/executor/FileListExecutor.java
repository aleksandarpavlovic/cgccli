package com.paki.executor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paki.command.Command;
import com.paki.command.OperationCommand;
import com.paki.command.Option;
import com.paki.util.JsonFormatUtil;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FileListExecutor implements HttpExecutor {
    @Override
    public void execute(Command command) throws IOException, ExecutorException, CGCServerException {
        OperationCommand opCommand = (OperationCommand) command;

        OkHttpClient client = new OkHttpClient();
        JsonParser jsonParser = new JsonParser();

        String nextPageUrl = buildUrl("https://cgc-api.sbgenomics.com/v2/files", opCommand);
        while (nextPageUrl != null) {
            Request request = buildRequest(nextPageUrl, opCommand);
            Response response = client.newCall(request).execute();
            validateResponse(response);

            JsonObject responseJson = jsonParser.parse(response.body().string()).getAsJsonObject();
            processResponseBody(responseJson);
            nextPageUrl = extractNextPageUrl(responseJson);
        }
    }

    private Request buildRequest(String url, OperationCommand command) throws ExecutorException {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        Map<String, String> commonHeaders = getCommonHeaders(command);
        for (Map.Entry<String, String> header: commonHeaders.entrySet()) {
            requestBuilder.addHeader(header.getKey(), header.getValue());
        }
        return requestBuilder.build();
    }

    private String buildUrl(String url, OperationCommand command) throws ExecutorException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        Set<String> mandatoryOptions = new HashSet<>(Arrays.asList("project"));

        for (Option option: command.getOperation().getOptions()) {
            if ("project".equals(option.getName())) {
                urlBuilder.addQueryParameter("project", option.getValue());
            }
            mandatoryOptions.remove(option.getName());
        }

        if (!mandatoryOptions.isEmpty()) {
            throw new ExecutorException(String.format("Missing mandatory options: [%s].", String.join(", ", mandatoryOptions.stream().map(o -> "--" + o).collect(Collectors.toSet()))));
        }

        return urlBuilder.build().toString();
    }

    private void processResponseBody(JsonObject responseJson) {
        JsonArray items = responseJson.getAsJsonArray("items");
        System.out.println(JsonFormatUtil.jsonArrayToPrintableString(items));
    }
}
