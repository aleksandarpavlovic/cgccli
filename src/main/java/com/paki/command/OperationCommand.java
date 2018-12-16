package com.paki.command;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationCommand that = (OperationCommand) o;
        return Objects.equals(operation, that.operation) &&
                Objects.equals(optionsHolder, that.optionsHolder);
    }

    @Override
    public int hashCode() {

        return Objects.hash(operation, optionsHolder);
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
