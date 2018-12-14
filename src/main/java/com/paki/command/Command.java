package com.paki.command;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class Command implements OptionsHolder {
    @Getter @Setter
    Operation operation;
    OptionsHolder optionsHolder;

    @Override
    public List<Option> getOptions() {
        return optionsHolder.getOptions();
    }

    @Override
    public void addOption(Option option) {
        optionsHolder.addOption(option);
    }

    @Override
    public void addAllOptions(List<Option> options) {
        optionsHolder.addAllOptions(options);
    }

    @NoArgsConstructor
    public static class CommandBuilder {
        private Operation operation;
        private List<Option> options = new ArrayList<>();

        public CommandBuilder withOption(Option option) {
            this.options.add(option);
            return this;
        }

        public CommandBuilder withOperation(Operation operation) {
            this.operation = operation;
            return this;
        }

        public Command build() {
            Command command = new Command();
            command.setOperation(this.operation);
            command.addAllOptions(this.options);
            return command;
        }
    }
}
