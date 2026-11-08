package com.example.lab8;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Movie {
    private final SimpleStringProperty title;
    private final SimpleStringProperty genre;
    private final SimpleIntegerProperty year;

    public Movie(String title, String genre, int year) {
        this.title = new SimpleStringProperty(title);
        this.genre = new SimpleStringProperty(genre);
        this.year = new SimpleIntegerProperty(year);
    }

    public String getTitle() { return title.get(); }
    public void setTitle(String value) { title.set(value); }
    public SimpleStringProperty titleProperty() { return title; }

    public String getGenre() { return genre.get(); }
    public void setGenre(String value) { genre.set(value); }
    public SimpleStringProperty genreProperty() { return genre; }

    public int getYear() { return year.get(); }
    public void setYear(int value) { year.set(value); }
    public SimpleIntegerProperty yearProperty() { return year; }
}
