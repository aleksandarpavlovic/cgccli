package com.paki.executor;

import com.paki.command.Command;

public class HelpExecutor implements HttpExecutor {
    @Override
    public void execute(Command command) {
        System.out.println("CGC CLI Tool");
        System.out.println("Usage: --token token_value resource action [option | assignment]*");
    }
}
