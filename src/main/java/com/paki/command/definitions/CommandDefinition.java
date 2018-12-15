package com.paki.command.definitions;

import lombok.Getter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Getter
public class CommandDefinition {
    private Optional<Set<OptionDefinition>> mandatoryOptions;
    private Optional<Set<OptionDefinition>> optionalOptions;
    private Optional<Set<OperationDefinition>> operations;

    private CommandDefinition(Set<OptionDefinition> mandatoryOptions, Set<OptionDefinition> optionalOptions, Set<OperationDefinition> operations) {
        if (mandatoryOptions == null || mandatoryOptions.isEmpty()) {
            this.mandatoryOptions = Optional.empty();
        } else {
            this.mandatoryOptions = Optional.of(mandatoryOptions);
        }
        if (optionalOptions == null || optionalOptions.isEmpty()) {
            this.optionalOptions = Optional.empty();
        } else {
            this.optionalOptions = Optional.of(optionalOptions);
        }
        if (operations == null || operations.isEmpty()) {
            this.operations = Optional.empty();
        } else {
            this.operations = Optional.of(operations);
        }
    }

    public static class Builder {
        private Set<OptionDefinition> mandatoryOptions;
        private Set<OptionDefinition> optionalOptions;
        private Set<OperationDefinition> operations;

        public Builder withMandatoryOption(OptionDefinition option) {
            if (mandatoryOptions == null)
                mandatoryOptions = new HashSet<>();
            mandatoryOptions.add(option);
            return this;
        }

        public Builder withOptionalOption(OptionDefinition option) {
            if (optionalOptions == null)
                optionalOptions = new HashSet<>();
            optionalOptions.add(option);
            return this;
        }

        public Builder withOperation(OperationDefinition operation) {
            if (operations == null)
                operations = new HashSet<>();
            operations.add(operation);
            return this;
        }

        public CommandDefinition build() {
            return new CommandDefinition(mandatoryOptions, optionalOptions, operations);
        }
    }
}
