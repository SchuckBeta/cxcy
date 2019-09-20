package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

public class RtPxAssignment<T> {
    private T assignee;
    private List<RtPxCusers> candidateUsers;
    private List<RtPxCroles> candidateGroups;

    public RtPxAssignment() {
        super();
    }

    public RtPxAssignment(T assignee) {
        this.assignee = assignee;
    }

    public RtPxAssignment(T assignee, List<RtPxCusers> candidateUsers, List<RtPxCroles> candidateGroups) {
        super();
        this.assignee = assignee;
        this.candidateUsers = candidateUsers;
        this.candidateGroups = candidateGroups;
    }

    public T getAssignee() {
        return assignee;
    }

    public void setAssignee(T assignee) {
        this.assignee = assignee;
    }

    public List<RtPxCusers> getCandidateUsers() {
        return candidateUsers;
    }

    public void setCandidateUsers(List<RtPxCusers> candidateUsers) {
        this.candidateUsers = candidateUsers;
    }

    public List<RtPxCroles> getCandidateGroups() {
        return candidateGroups;
    }

    public void setCandidateGroups(List<RtPxCroles> candidateGroups) {
        this.candidateGroups = candidateGroups;
    }
}
