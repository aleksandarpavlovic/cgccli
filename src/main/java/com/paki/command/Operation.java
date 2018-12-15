package com.paki.command;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class Operation implements OptionsHolder, AssignmentsHolder {
    @Getter @Setter
    String resource;
    @Getter @Setter
    String action;
    OptionsHolder optionsHolder = new DefaultOptionsHolder();
    AssignmentsHolder assignmentsHolder = new DefaultAssignmentsHolder();

    @Override
    public Set<Assignment> getAssignments() {
        return assignmentsHolder.getAssignments();
    }

    @Override
    public void addAssignment(Assignment assignment) {
        assignmentsHolder.addAssignment(assignment);
    }

    @Override
    public void addAllAssignments(Set<Assignment> assignments) {
        assignmentsHolder.addAllAssignments(assignments);
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return Objects.equals(resource, operation.resource) &&
                Objects.equals(action, operation.action);
    }

    @Override
    public int hashCode() {

        return Objects.hash(resource, action);
    }

    public static class Builder {
        String resource;
        String action;
        Set<Option> options = new HashSet<>();
        Set<Assignment> assignments = new HashSet<>();

        public Builder withResource(String resource) {
            this.resource = resource;
            return this;
        }

        public Builder withAction(String action) {
            this.action = action;
            return this;
        }

        public Builder withOption(Option option) {
            this.options.add(option);
            return this;
        }

        public Builder withAssignment(Assignment assignment) {
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
