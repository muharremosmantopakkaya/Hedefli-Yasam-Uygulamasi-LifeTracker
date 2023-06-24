package com.example.goalguru;

public interface TaskListener {
    void onTaskAdded();
    void onTaskDeleted();
    void synchronizeData();
    void switchToMatrisTab();
    void clearAllTasks();
}
