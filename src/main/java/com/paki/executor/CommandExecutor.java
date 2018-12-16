package com.paki.executor;

import com.paki.command.Command;

import java.io.IOException;

public class CommandExecutor {

    public void execute(Command command) {
        HttpExecutor executor;
        if ("projects".equals(command.getOperation().getResource()) && "list".equals(command.getOperation().getAction()))
            executor = new ProjectListExecutor();
        else if ("files".equals(command.getOperation().getResource()) && "list".equals(command.getOperation().getAction()))
            executor = new FileListExecutor();
        else if ("files".equals(command.getOperation().getResource()) && "stat".equals(command.getOperation().getAction()))
            executor = new FileDetailsExecutor();
        else if ("files".equals(command.getOperation().getResource()) && "update".equals(command.getOperation().getAction()))
            executor = new FileUpdateExecutor();
        else if ("files".equals(command.getOperation().getResource()) && "download".equals(command.getOperation().getAction()))
            executor = new FileDownloadExecutor();
        else {
            // unsupported operation/command
            return;
        }

        try {
            executor.execute(command);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        validateMandatoryCommandOptions(command);
//        validateOptionalCommandOptions(command);
//        validateOperation(command.getOperation());
    }
//
//    void validateMandatoryCommandOptions(Command command) {
//        Set<String> commandOptionsNames = command.getOptions().stream().map(option -> option.getName()).collect(Collectors.toSet());
//        Set<String> mandatoryOptionsNames = definitions.getCommandDefinition().getMandatoryOptions().orElse(Collections.emptySet()).stream().map(optionDefinition -> optionDefinition.getName()).collect(Collectors.toSet());
//        if (!commandOptionsNames.containsAll(mandatoryOptionsNames)) {
//            // error
//        }
//    }
//
//    void validateOptionalCommandOptions(Command command) {
//        Set<String> commandOptionsNames = command.getOptions().stream().map(option -> option.getName()).collect(Collectors.toSet());
//        Set<String> definedOptionsNames = definitions.getCommandDefinition().getOptionalOptions().orElse(Collections.emptySet()).stream().map(optionDefinition -> optionDefinition.getName()).collect(Collectors.toSet());
//        definedOptionsNames.addAll(definitions.getCommandDefinition().getMandatoryOptions().orElse(Collections.emptySet()).stream().map(optionDefinition -> optionDefinition.getName()).collect(Collectors.toSet()))
//        if (!definedOptionsNames.containsAll(commandOptionsNames)) {
//            // error
//        }
//    }
//
//    void validateOperation(Operation operation) {
//        definitions.getCommandDefinition().getOperations().orElse(Collections.emptySet()).
//    }
}
