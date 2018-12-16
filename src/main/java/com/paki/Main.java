package com.paki;

import com.paki.command.Command;
import com.paki.executor.CGCServerException;
import com.paki.executor.CommandExecutor;
import com.paki.executor.ExecutorException;
import com.paki.parser.CGCParseException;
import com.paki.parser.Parser;

public class Main {
    public static void main(String[] args) {
        try {
            Parser parser = new Parser();
            Command command = parser.parseCommand(args);
            CommandExecutor executor = new CommandExecutor();
            executor.execute(command);
        } catch (CGCParseException e) {
            System.out.println("Parse exception - " + e.getMessage());
            System.out.println("Type --help for usage instructions.");
        } catch (ExecutorException e) {
            System.out.println("Executor exception - " + e.getMessage());
        } catch (CGCServerException e) {
            System.out.println("Command failed. Server error response: ");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }
}
