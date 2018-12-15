package com.paki.executor;

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
}
