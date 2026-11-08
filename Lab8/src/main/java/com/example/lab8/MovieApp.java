package com.example.lab8;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MovieApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        stage.setScene(new Scene(loader.load(), 780, 520));
        stage.setTitle("Каталог фильмов — Вариант 9");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
