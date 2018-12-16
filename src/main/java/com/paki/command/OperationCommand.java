package com.paki.command;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
public class OperationCommand implements Command, OptionsHolder {
    @Getter @Setter
    Operation operation;
    OptionsHolder optionsHolder = new DefaultOptionsHolder();

    @Override
    public Set<Option> getOptions() {
        return optionsHolder.getOptions();
    }

    @Override
    public void addOption(Option option) {
        optionsHolder.addOption(option);
    }

    @Override
    public void addAllOptions(Set<Option> options) {
        optionsHolder.addAllOptions(options);
    }

    @Override
    public String identifier() {
        return getOperation().getResource() + " " + getOperation().getAction();
    }

    @NoArgsConstructor
    public static class Builder {
        private Operation operation;
        private Set<Option> options = new HashSet<>();

        public Builder withOption(Option option) {
            this.options.add(option);
            return this;
        }

        public Builder withOperation(Operation operation) {
            this.operation = operation;
            return this;
        }

        public OperationCommand build() {
            OperationCommand command = new OperationCommand();
            command.setOperation(this.operation);
            command.addAllOptions(this.options);
            return command;
        }
    }
}
