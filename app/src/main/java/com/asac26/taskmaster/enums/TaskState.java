package com.asac26.taskmaster.enums;

import androidx.annotation.NonNull;

public enum TaskState {
    NEW("New"),
    ASSIGNED("Assigned"),
    IN_PROGRESS("In Progress"),
    COMPLETE("Complete");

    private final String taskStatus;

    TaskState(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public static TaskState fromString(String possibleStatus){
        for (TaskState task : TaskState.values())
            if(task.taskStatus.equals(possibleStatus)){
                return task;
            }
        return null;
    }


    @NonNull
    @Override
    public String toString(){
        if (taskStatus == null){
            return "";
        }
        return taskStatus;
    }
}
