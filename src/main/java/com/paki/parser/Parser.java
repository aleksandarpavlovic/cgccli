package com.paki.parser;

import com.paki.command.Assignment;
import com.paki.command.Command;
import com.paki.command.Operation;
import com.paki.command.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Parser {
    public Command parseCommand(String[] tokens) {
        Command.CommandBuilder commandBuilder = new Command.CommandBuilder();
        CommandParserState parserState = CommandParserState.START;
        int i = 0;
        while (i < tokens.length) {
            String token = tokens[i];
            switch (parserState) {
                case START:
                    if (isOption(token)) {
                        ParseResult<Option> optionParseResult = parseOption(tokens, i);
                        commandBuilder.withOption(optionParseResult.getResult());
                        i = optionParseResult.getNextTokenPosition();
                    } else if (isResource(token)) {
                        ParseResult<Operation> operationParseResult = parseOperation(tokens, i);
                        commandBuilder.withOperation(operationParseResult.getResult());
                        i = operationParseResult.getNextTokenPosition();
                        parserState = CommandParserState.END;
                    } else {
                        // error
                    }
                    break;
                case END:
                    // error
                    break;
                default:
                    // error
            }
            i++;
        }
        return commandBuilder.build();
    }

    private ParseResult<Operation> parseOperation(String[] tokens, int nextTokenPosition) {
        Operation.OperationBuilder operationBuilder = new Operation.OperationBuilder();

        if (tokens.length - nextTokenPosition < 2) {
            // error
        }
        String resourceToken = tokens[nextTokenPosition++];
        if (!isResource(resourceToken)) {
            // error
        }
        String resource = getResource(resourceToken);
        operationBuilder.withResource(resource);

        String actionToken = tokens[nextTokenPosition++];
        if (!isAction(actionToken)) {
            // error
        }
        String action = getAction(actionToken);
        operationBuilder.withAction(action);

        while(nextTokenPosition < tokens.length) {
            if (isOption(tokens[nextTokenPosition])) {
                ParseResult<Option> optionParseResult = parseOption(tokens, nextTokenPosition);
                operationBuilder.withOption(optionParseResult.getResult());
                nextTokenPosition = optionParseResult.getNextTokenPosition();
            } else if (isAssignment(tokens[nextTokenPosition])) {
                ParseResult<Assignment> assignmentParseResult = parseAssignment(tokens, nextTokenPosition);
                operationBuilder.withAssignment(assignmentParseResult.getResult());
                nextTokenPosition = assignmentParseResult.getNextTokenPosition();
            } else {
                // error
            }
        }

        return new ParseResult<>(operationBuilder.build(), nextTokenPosition);
    }

    private ParseResult<Option> parseOption(String[] tokens, int nextTokenPosition) {
        if (tokens.length - nextTokenPosition < 2) {
            // error
        }
        String optNameToken = tokens[nextTokenPosition++];
        if (!isOption(optNameToken)) {
            // error
        }
        String optionName = getOption(optNameToken);

        String optValToken = tokens[nextTokenPosition++];
        if (!isValue(optValToken)) {
            // error
        }
        String optionValue = getValue(optValToken);

        return new ParseResult<>(new Option(optionName, optionValue), nextTokenPosition);
    }

    private ParseResult<Assignment> parseAssignment(String[] tokens, int nextTokenPosition) {
        if (tokens.length <= nextTokenPosition) {
            // error
        }
        String[] split = tokens[nextTokenPosition++].split("=");
        return new ParseResult<>(new Assignment(split[0], split[1]), nextTokenPosition);
    }

//    public Command parse(String[] tokens) {
//        State parserState = State.START;
//        for (String token: tokens) {
//            switch(parserState) {
//                case START:
//                    if (isOption(token)) {
//                        // command.setOption
//                        parserState = State.COMMAND_OPTION;
//                    } else if (isResource(token)) {
//                        // operatoin.setResource
//                        parserState = State.OPERATION_RESOURCE;
//                    } else {
//                        // error
//                    }
//                    break;
//                case COMMAND_OPTION:
//                    if (isValue(token)) {
//                        // command.setOptionValue
//                        parserState = State.START;
//                    } else {
//                        // error
//                    }
//                    break;
//                case OPERATION_RESOURCE:
//                    if (isAction(token)) {
//                        // operation.setAction
//                        parserState = State.OPERATION_ACTION;
//                    } else {
//                        // error
//                    }
//                    break;
//                case OPERATION_ACTION:
//                    if (isAssignment(token)) {
//                        // operation.setAssignment
//                    } else if (isOption(token)) {
//                        // operation.setOption
//                        parserState = State.OPERATION_OPTION;
//                    } else {
//                        // error
//                    }
//                    break;
//                case OPERATION_OPTION:
//                    if (isValue(token)) {
//                        // operation.setOptionValue
//                        parserState = State.START;
//                    } else {
//                        // error
//                    }
//                    break;
//                default:
//                    throw new IllegalStateException();
//            }
//        }
//
//        if (parserState != State.OPERATION_ACTION) {
//            // nelegalan izraz
//        }
//
//        return null;
//    }

    private boolean isOption(String token) {
        return token.startsWith("--");
    }

    private String getOption(String token) {
        return token.substring(2);
    }

    private boolean isResource(String token) {
        return isWord(token);
    }

    private String getResource(String token) {
        return token;
    }

    private boolean isAction(String token) {
        return isWord(token);
    }

    private String getAction(String token) {
        return token;
    }

    private boolean isWord(String token) {
        return token.matches("[a-zA-Z].*");
    }

    private boolean isValue(String token) {
        return token.matches("[a-zA-Z0-9].*");
    }

    private String getValue(String token) {
        return token;
    }

    private boolean isAssignment(String token) {
        String[] subtokens = token.split("=");
        if (subtokens == null || subtokens.length != 2)
            return false;
        return isWord(subtokens[0]) && isValue(subtokens[1]);
    }

//    private enum State {
//        START, COMMAND_OPTION, OPERATION_RESOURCE, OPERATION_ACTION, OPERATION_OPTION, END
//    }

    private enum CommandParserState {
        START, END
    }

    @Getter @Setter @AllArgsConstructor
    private static class ParseResult<T> {
        T result;
        int nextTokenPosition;
    }
}
