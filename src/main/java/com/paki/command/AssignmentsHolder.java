package com.paki.command;

import java.util.Set;

public interface AssignmentsHolder {
    Set<Assignment> getAssignments();
    void addAssignment(Assignment assignment);
    void addAllAssignments(Set<Assignment> assignments);
}
