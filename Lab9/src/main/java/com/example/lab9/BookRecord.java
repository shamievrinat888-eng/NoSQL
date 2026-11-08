package com.example.lab9;

public class BookRecord {
    private String fullName;
    private String email;
    private int age;
    private String libraryCardId; // Тот самый уникальный идентификатор (Вариант 4)
    private String category;      // Например: Студент, Преподаватель, Читатель

    public BookRecord(String fullName, String email, int age, String libraryCardId, String category) {
        this.fullName = fullName;
        this.email = email;
        this.age = age;
        this.libraryCardId = libraryCardId;
        this.category = category;
    }

    // Геттеры и сеттеры (необходимы для корректного отображения в TableView)
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public int getAge() { return age; }
    public String getLibraryCardId() { return libraryCardId; }
    public String getCategory() { return category; }
}
