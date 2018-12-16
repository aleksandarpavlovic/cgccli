package com.paki.executor;

import com.paki.command.Command;
import java.io.IOException;

public class CommandExecutor {

    public void execute(Command command) throws IOException, ExecutorException, CGCServerException {
        HttpExecutor executor = findExecutor(command);
        executor.execute(command);
    }

    private HttpExecutor findExecutor(Command command) throws ExecutorException {
        switch (command.identifier()) {
            case "help":
                return new HelpExecutor();
            case "projects list":
                return new ProjectListExecutor();
            case "files list":
                return new FileListExecutor();
            case "files stat":
                return new FileDetailsExecutor();
            case "files update":
                return new FileUpdateExecutor();
            case "files download":
                return new FileDownloadExecutor();
            default:
                throw new ExecutorException(String.format("Command with identifier: %s not supported for execution.", command.identifier()));
        }
    }
}
