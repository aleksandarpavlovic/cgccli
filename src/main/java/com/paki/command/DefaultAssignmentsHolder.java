package com.paki.command;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DefaultAssignmentsHolder implements AssignmentsHolder {

    private Set<Assignment> assignments = new HashSet<>();

    @Override
    public Set<Assignment> getAssignments() {
        return assignments;
    }

    @Override
    public void addAssignment(Assignment assignment) {
        this.assignments.add(assignment);
    }

    @Override
    public void addAllAssignments(Set<Assignment> assignments) {
        this.assignments.addAll(assignments);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultAssignmentsHolder that = (DefaultAssignmentsHolder) o;
        return Objects.equals(assignments, that.assignments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assignments);
    }
}
