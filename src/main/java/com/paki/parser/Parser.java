package com.paki.parser;

import com.paki.command.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Parser {
    public Command parseCommand(String[] tokens) throws CGCParseException {
        OperationCommand.Builder commandBuilder = new OperationCommand.Builder();
        CommandParserState parserState = CommandParserState.START;
        int i = 0;
        while (i < tokens.length) {
            String token = tokens[i];
            switch (parserState) {
                case START:
                    if (isHelp(token)) {
                        return HelpCommand.instance();
                    } else if (isOption(token)) {
                        ParseResult<Option> optionParseResult = parseOption(tokens, i);
                        commandBuilder.withOption(optionParseResult.getResult());
                        i = optionParseResult.getNextTokenPosition();
                    } else if (isResource(token)) {
                        ParseResult<Operation> operationParseResult = parseOperation(tokens, i);
                        commandBuilder.withOperation(operationParseResult.getResult());
                        i = operationParseResult.getNextTokenPosition();
                        parserState = CommandParserState.END;
                    } else {
                        throw new CGCParseException(String.format("Token: %s does not semantically fit into given command expression.", token));
                    }
                    break;
                case END:
                    throw new CGCParseException(String.format("End of command expression expected instead of token: %s.", token));
                default:
                    throw new CGCParseException("Command parser encountered an invalid parsing state.");
            }
        }
        if (parserState != CommandParserState.END) {
            throw new CGCParseException("Incomplete command.");
        }
        return commandBuilder.build();
    }

    private ParseResult<Operation> parseOperation(String[] tokens, int nextTokenPosition) throws CGCParseException {
        Operation.Builder operationBuilder = new Operation.Builder();

        if (tokens.length - nextTokenPosition < 2) {
            throw new CGCParseException(String.format("Operation parser expects at least 2 arguments, but found only %d.", tokens.length - nextTokenPosition));
        }
        String resourceToken = tokens[nextTokenPosition++];
        if (!isResource(resourceToken)) {
            throw new CGCParseException(String.format("Token: %s is not a valid operation resource.", resourceToken));
        }
        String resource = getResource(resourceToken);
        operationBuilder.withResource(resource);

        String actionToken = tokens[nextTokenPosition++];
        if (!isAction(actionToken)) {
            throw new CGCParseException(String.format("Token: %s is not a valid operation action.", actionToken));
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
                throw new CGCParseException(String.format("Token: %s does not semantically fit into given operation expression. Operation parser expects either an option or an assignment.", tokens[nextTokenPosition]));
            }
        }

        return new ParseResult<>(operationBuilder.build(), nextTokenPosition);
    }

    private ParseResult<Option> parseOption(String[] tokens, int nextTokenPosition) throws CGCParseException {
        if (tokens.length - nextTokenPosition < 2) {
            throw new CGCParseException(String.format("Option parser expects at least 2 arguments, but found only %d.", tokens.length - nextTokenPosition));
        }
        String optNameToken = tokens[nextTokenPosition++];
        if (!isOption(optNameToken)) {
            throw new CGCParseException(String.format("Token: %s is not a valid option name.", optNameToken));
        }
        String optionName = getOption(optNameToken);

        String optValToken = tokens[nextTokenPosition++];
        if (!isValue(optValToken)) {
            throw new CGCParseException(String.format("Token: %s is not a valid option value.", optValToken));
        }
        String optionValue = getValue(optValToken);

        return new ParseResult<>(new Option(optionName, optionValue), nextTokenPosition);
    }

    private ParseResult<Assignment> parseAssignment(String[] tokens, int nextTokenPosition) throws CGCParseException {
        if (tokens.length <= nextTokenPosition) {
            throw new CGCParseException("Assignment parsing error. No assignment token found.");
        }
        String[] split = tokens[nextTokenPosition++].split("=");
        return new ParseResult<>(new Assignment(split[0], split[1]), nextTokenPosition);
    }

    private boolean isHelp(String token) {
        return token.equals("--help");
    }

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
        return token.matches("[^-].*");
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

    private enum CommandParserState {
        START, END
    }

    @Getter @Setter @AllArgsConstructor
    private static class ParseResult<T> {
        T result;
        int nextTokenPosition;
    }
}
