package com.paki.command;

import java.util.List;

public interface AssignmentsHolder {
    List<Assignment> getAssignments();
    void addAssignment(Assignment assignment);
    void addAllAssignments(List<Assignment> assignments);
}
