package com.paki;

import com.paki.command.Command;
import com.paki.executor.CommandExecutor;
import com.paki.parser.Parser;

public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser();
        Command command = parser.parseCommand(args);
        CommandExecutor executor = new CommandExecutor();
        executor.execute(command);
    }
}
