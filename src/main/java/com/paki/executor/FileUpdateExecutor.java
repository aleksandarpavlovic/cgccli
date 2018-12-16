package com.paki.executor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paki.command.Assignment;
import com.paki.command.Command;
import com.paki.command.OperationCommand;
import com.paki.command.Option;
import com.paki.util.JsonFormatUtil;
import com.squareup.okhttp.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FileUpdateExecutor implements HttpExecutor {
    @Override
    public void execute(Command command) throws IOException, ExecutorException, CGCServerException {
        OperationCommand opCommand = (OperationCommand) command;
        OkHttpClient client = new OkHttpClient();
        JsonParser jsonParser = new JsonParser();

        String url = buildUrl("https://cgc-api.sbgenomics.com/v2/files", opCommand);
        Request request = buildRequest(url, opCommand);
        Response response = client.newCall(request).execute();
        validateResponse(response);

        JsonObject responseJson = jsonParser.parse(response.body().string()).getAsJsonObject();
        processResponseBody(responseJson);
    }

    private void processResponseBody(JsonObject responseJson) {
        System.out.println(JsonFormatUtil.jsonObjectToPrintableString(responseJson));
    }

    private Request buildRequest(String url, OperationCommand command) throws ExecutorException {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        Map<String, String> commonHeaders = getCommonHeaders(command);
        for (Map.Entry<String, String> header: commonHeaders.entrySet()) {
            requestBuilder.addHeader(header.getKey(), header.getValue());
        }
        requestBuilder.addHeader("Content-Type", "application/json");
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), buildRequestBody(command));
        return requestBuilder.patch(body).build();
    }

    private String buildUrl(String url, OperationCommand command) throws ExecutorException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        Set<String> mandatoryOptions = new HashSet<>(Arrays.asList("file"));

        for (Option option: command.getOperation().getOptions()) {
            if ("file".equals(option.getName())) {
                urlBuilder.addPathSegment(option.getValue());
            }
            mandatoryOptions.remove(option.getName());
        }

        if (!mandatoryOptions.isEmpty()) {
            throw new ExecutorException(String.format("Missing mandatory options: [%s].", String.join(", ", mandatoryOptions.stream().map(o -> "--" + o).collect(Collectors.toSet()))));
        }

        return urlBuilder.build().toString();
    }

    private String buildRequestBody(OperationCommand command) {
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
