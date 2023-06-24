package com.example.goalguru;

public class Task {
    private String taskText;
    private boolean isUrgent;
    private boolean isImportant;
    private String id;
    private String title;
    private String description;

    public Task() {
        // Boş yapıcı metot gereklidir Firestore'da dökümanı çekerken kullanmak için
    }

    public Task(String taskText, boolean isUrgent, boolean isImportant) {
        this.taskText = taskText;
        this.isUrgent = isUrgent;
        this.isImportant = isImportant;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public boolean isUrgent() {
        return isUrgent;
    }

    public void setUrgent(boolean urgent) {
        isUrgent = urgent;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
