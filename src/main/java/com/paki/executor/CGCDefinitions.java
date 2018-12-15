package com.paki.executor;

import com.paki.command.definitions.CommandDefinition;
import com.paki.command.definitions.OperationDefinition;
import com.paki.command.definitions.OptionDefinition;
import lombok.Getter;

public class CGCDefinitions {
    public static CGCDefinitions getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final CGCDefinitions instance = new CGCDefinitions();
    }

    @Getter
    private final CommandDefinition commandDefinition;

    private CGCDefinitions() {
        CommandDefinition.Builder cmdDefBuilder = new CommandDefinition.Builder();

        // token option
        OptionDefinition tokenOption = new OptionDefinition.Builder().
                withName("token").build();
        cmdDefBuilder.withMandatoryOption(tokenOption);

        // projects list operation
        OperationDefinition projectsListOperation = new OperationDefinition.Builder().
                withResource("projects").
                withAction("list").build();
        cmdDefBuilder.withOperation(projectsListOperation);

        // files list operation
        OperationDefinition filesListOperation = new OperationDefinition.Builder().
                withResource("files").
                withAction("list").
                withMandatoryOption(new OptionDefinition.Builder().withName("project").build()).build();
        cmdDefBuilder.withOperation(filesListOperation);

        // file details operation
        OperationDefinition fileDetailsOperation = new OperationDefinition.Builder().
                withResource("files").
                withAction("stat").
                withMandatoryOption(new OptionDefinition.Builder().withName("file").build()).build();
        cmdDefBuilder.withOperation(fileDetailsOperation);

        // file update operation
        OperationDefinition fileUpdateOperation = new OperationDefinition.Builder().
                withResource("files").
                withAction("update").
                withMandatoryOption(new OptionDefinition.Builder().withName("file").build()).
                withAssignmentSupport(true).build();
        cmdDefBuilder.withOperation(fileUpdateOperation);

        // file download operation
        OperationDefinition fileDownloadOperation = new OperationDefinition.Builder().
                withResource("files").
                withAction("download").
                withMandatoryOption(new OptionDefinition.Builder().withName("file").build()).
                withMandatoryOption(new OptionDefinition.Builder().withName("dest").build()).build();
        cmdDefBuilder.withOperation(fileDownloadOperation);

        commandDefinition = cmdDefBuilder.build();
    }
}
