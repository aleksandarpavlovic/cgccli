package com.paki.command.definitions;

import lombok.Getter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Getter
public class OperationDefinition {
    private String resource;
    private String action;
    private Optional<Set<OptionDefinition>> mandatoryOptions;
    private Optional<Set<OptionDefinition>> optionalOptions;
    private boolean assignmentSupport;

    private OperationDefinition(String resource, String action, Set<OptionDefinition> mandatoryOptions, Set<OptionDefinition> optionalOptions, boolean assignmentSupport) {
        this.resource = resource;
        this.action = action;
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
        this.assignmentSupport = assignmentSupport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationDefinition that = (OperationDefinition) o;
        return Objects.equals(resource, that.resource) &&
                Objects.equals(action, that.action);
    }

    @Override
    public int hashCode() {

        return Objects.hash(resource, action);
    }

    public static class Builder {
        private String resource;
        private String action;
        private Set<OptionDefinition> mandatoryOptions;
        private Set<OptionDefinition> optionalOptions;
        private boolean assignmentSupport = false;

        public Builder withResource(String resource) {
            this.resource = resource;
            return this;
        }

        public Builder withAction(String action) {
            this.action = action;
            return this;
        }

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

        public Builder withAssignmentSupport(boolean assignmentSupport) {
            this.assignmentSupport = assignmentSupport;
            return this;
        }

        public OperationDefinition build() {
            return new OperationDefinition(resource, action, mandatoryOptions, optionalOptions, assignmentSupport);
        }
    }
}
