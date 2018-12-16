package com.paki.executor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paki.command.Command;
import com.paki.command.OperationCommand;
import com.paki.command.Option;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FileDownloadExecutor implements HttpExecutor {
    private static final int BUFFER_SIZE = 102400; // 100 KB
    @Override
    public void execute(Command command) throws IOException, ExecutorException, CGCServerException {
        OperationCommand opCommand = (OperationCommand) command;
        OkHttpClient client = new OkHttpClient();
        JsonParser jsonParser = new JsonParser();

        validateOptions(opCommand);

        String url = buildUrl("https://cgc-api.sbgenomics.com/v2/files", opCommand);
        Request request = buildRequest(url, opCommand);
        Response response = client.newCall(request).execute();
        validateResponse(response);

        JsonObject responseJson = jsonParser.parse(response.body().string()).getAsJsonObject();
        String downloadUrl = responseJson.get("url") != null ? responseJson.get("url").getAsString() : null;
        if (downloadUrl == null) {
            throw new ExecutorException("File download link could not be obtained.");
        }
        String destPath = extractFileDestinationPath(opCommand);
        downloadFile(downloadUrl, destPath);
    }

    private void validateOptions(OperationCommand command) throws ExecutorException {
        Set<String> mandatoryOptions = new HashSet<>(Arrays.asList("file", "dest"));

        for (Option option: command.getOperation().getOptions()) {
            mandatoryOptions.remove(option.getName());
        }

        if (!mandatoryOptions.isEmpty()) {
            throw new ExecutorException(String.format("Missing mandatory options: [%s].", String.join(", ", mandatoryOptions.stream().map(o -> "--" + o).collect(Collectors.toSet()))));
        }
    }

    private void downloadFile(String downloadUrl, String path) throws IOException {
        URL url = new URL(downloadUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream()); BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path), BUFFER_SIZE)){
            long totalSize = connection.getContentLengthLong();
            long downloadedSize = 0;
            byte[] data = new byte[BUFFER_SIZE];
            ProgressBar progressBar = new ProgressBar();

            int readChunkSize;
            while((readChunkSize = in.read(data, 0, BUFFER_SIZE)) >= 0) {
                downloadedSize += readChunkSize;
                out.write(data, 0, readChunkSize);
                progressBar.update(downloadedSize, totalSize);
            }
            System.out.println("Download complete.");
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    private String extractFileDestinationPath(OperationCommand command) {
        for (Option option: command.getOperation().getOptions()) {
            if ("dest".equals(option.getName())) {
                return option.getValue();
            }
        }
        return null;
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
        for (Option option: command.getOperation().getOptions()) {
            if ("file".equals(option.getName())) {
                urlBuilder.addPathSegment(option.getValue());
                urlBuilder.addPathSegment("download_info");
            }
        }
        return urlBuilder.build().toString();
    }

    private static class ProgressBar {
        private static final int BAR_LENGTH = 60;

        /**
         * called whenever the progress bar needs to be updated.
         * that is whenever progress was made.
         *
         * @param done an int representing the work done so far
         * @param total an int representing the total work
         */
        public void update(long done, long total) {
            String format = "\r%3d%% %s";

            int percent = (int) ((done * 100) / total);
            int fillCount = percent * BAR_LENGTH / 100;
            int emptyCount = BAR_LENGTH - fillCount;

            StringBuilder progressBuilder = new StringBuilder();
            while (fillCount-- > 0) {
                progressBuilder.append('#');
            }
            while (emptyCount-- > 0) {
                progressBuilder.append('.');
            }

            System.out.printf(format, percent, progressBuilder.toString());

            if (done == total) {
                System.out.flush();
                System.out.println();
            }
        }
    }
}
