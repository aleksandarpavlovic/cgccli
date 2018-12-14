package com.paki.command;

import java.util.ArrayList;
import java.util.List;

public class DefaultAssignmentsHolder implements AssignmentsHolder {

    private List<Assignment> assignments = new ArrayList<>();

    @Override
    public List<Assignment> getAssignments() {
        return assignments;
    }

    @Override
    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    @Override
    public void addAllAssignments(List<Assignment> assignments) {
        this.assignments.addAll(assignments);
    }
}
