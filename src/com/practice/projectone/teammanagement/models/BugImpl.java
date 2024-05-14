package com.practice.projectone.teammanagement.models;

import com.practice.projectone.teammanagement.exceptions.InvalidUserInputException;
import com.practice.projectone.teammanagement.models.contracts.Bug;
import com.practice.projectone.teammanagement.models.enums.Priority;
import com.practice.projectone.teammanagement.models.enums.Severity;
import com.practice.projectone.teammanagement.models.enums.Status;

import java.util.ArrayList;
import java.util.List;

public class BugImpl extends Content implements Bug {
    private static final Status INITIAL_STATUS = Status.ACTIVE;
    private static final String SEVERITY_CHANGED = "The severity of item with ID %d switched from %s to %s";
    private static final String SEVERITY_SAME_ERR = "Can't change, severity already at %s";
    private static final String STEPS_EMPTY_ERR = "Please provide steps to reproduce the bug";
    private List<String> steps;
    private Severity severity;

    public BugImpl(String title, String description, Priority priority,
                            Severity severity, String assigneeName, List<String> steps) {

        super(title, description, INITIAL_STATUS, priority, assigneeName);
        this.severity = severity;
        setSteps(steps);
    }

    @Override
    public List<String> getSteps() {
        return new ArrayList<>(steps);
    }

    @Override
    public Severity getSeverity() {
        return severity;
    }

    @Override
    public void changeSeverity(Severity newSeverity) {
        Severity oldSeverity = getSeverity();

        if (newSeverity.equals(oldSeverity)) {
            throw new InvalidUserInputException(String.format(SEVERITY_SAME_ERR, newSeverity));
        }

        this.severity = newSeverity;
        addEventToHistory(new EventLogImpl(String.format(SEVERITY_CHANGED, getId(), oldSeverity, newSeverity)));
    }

    @Override
    protected void validateStatus(Status status) {
        if (!status.getTaskType().equals("Bug") && !status.getTaskType().equals("All")) {
            throw new IllegalArgumentException("Please provide valid story status");
        }
    }

    private void setSteps(List<String> steps) {
        if (steps.isEmpty()) {
            throw new IllegalArgumentException(STEPS_EMPTY_ERR);
        }

        this.steps = steps;
    }
}
