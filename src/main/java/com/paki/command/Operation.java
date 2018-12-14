package com.paki.command;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Operation implements OptionsHolder, AssignmentsHolder {
    @Getter @Setter
    String resource;
    @Getter @Setter
    String action;
    OptionsHolder optionsHolder = new DefaultOptionsHolder();
    AssignmentsHolder assignmentsHolder = new DefaultAssignmentsHolder();

    @Override
    public List<Assignment> getAssignments() {
        return assignmentsHolder.getAssignments();
    }

    @Override
    public void addAssignment(Assignment assignment) {
        assignmentsHolder.addAssignment(assignment);
    }

    @Override
    public void addAllAssignments(List<Assignment> assignments) {
        assignmentsHolder.addAllAssignments(assignments);
    }

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

    public static class OperationBuilder {
        String resource;
        String action;
        List<Option> options = new ArrayList<>();
        List<Assignment> assignments = new ArrayList<>();

        public OperationBuilder withResource(String resource) {
            this.resource = resource;
            return this;
        }

        public OperationBuilder withAction(String action) {
            this.action = action;
            return this;
        }

        public OperationBuilder withOption(Option option) {
            this.options.add(option);
            return this;
        }

        public OperationBuilder withAssignment(Assignment assignment) {
            this.assignments.add(assignment);
            return this;
        }

        public Operation build() {
            Operation operation = new Operation();
            operation.setResource(this.resource);
            operation.setAction(this.action);
            operation.addAllOptions(this.options);
            operation.addAllAssignments(this.assignments);
            return operation;
        }
    }
}
