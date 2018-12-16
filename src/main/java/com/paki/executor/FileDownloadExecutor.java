package com.paki.executor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paki.command.Command;
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
import java.util.Map;

public class FileDownloadExecutor implements HttpExecutor {
    private static final int BUFFER_SIZE = 102400; // 100 KB
    @Override
    public void execute(Command command) throws IOException {
        OkHttpClient client = new OkHttpClient();
        JsonParser jsonParser = new JsonParser();

        String url = buildUrl("https://cgc-api.sbgenomics.com/v2/files", command);
        Request request = buildRequest(url, command);
        Response response = client.newCall(request).execute();

        JsonObject responseJson = jsonParser.parse(response.body().string()).getAsJsonObject();
        String downloadUrl = responseJson.get("url") != null ? responseJson.get("url").getAsString() : null;
        if (downloadUrl == null) {
            // error file download url not found
        }
        String destPath = extractFileDestinationPath(command);
        downloadFile(downloadUrl, destPath);
    }

    private void downloadFile(String downloadUrl, String path) throws IOException{
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
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    private String extractFileDestinationPath(Command command) {
        for (Option option: command.getOperation().getOptions()) {
            if ("dest".equals(option.getName())) {
                return option.getValue();
            }
        }
        return null;
    }

    private Request buildRequest(String url, Command command) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        Map<String, String> commonHeaders = getCommonHeaders(command);
        for (Map.Entry<String, String> header: commonHeaders.entrySet()) {
            requestBuilder.addHeader(header.getKey(), header.getValue());
        }
        return requestBuilder.build();
    }

    private String buildUrl(String url, Command command) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        for (Option option: command.getOperation().getOptions()) {
            if ("file".equals(option.getName())) {
                urlBuilder.addPathSegment(option.getValue());
                urlBuilder.addPathSegment("download_info");
                break;
            }
        }
        return urlBuilder.build().toString();
    }

    private static class ProgressBar {
        private StringBuilder progress;

        /**
         * initialize progress bar properties.
         */
        public ProgressBar() {
            init();
        }

        /**
         * called whenever the progress bar needs to be updated.
         * that is whenever progress was made.
         *
         * @param done an int representing the work done so far
         * @param total an int representing the total work
         */
        public void update(long done, long total) {
            char[] workchars = {'|', '/', '-', '\\'};
            String format = "\r%3d%% %s %c";

            int percent = (int) ((++done * 100) / total);
            int extrachars = (percent / 2) - this.progress.length();

            while (extrachars-- > 0) {
                progress.append('#');
            }

            System.out.printf(format, percent, progress,
                    workchars[(int)(done % workchars.length)]);

            if (done == total) {
                System.out.flush();
                System.out.println();
                init();
            }
        }

        private void init() {
            this.progress = new StringBuilder(60);
        }
    }
}
