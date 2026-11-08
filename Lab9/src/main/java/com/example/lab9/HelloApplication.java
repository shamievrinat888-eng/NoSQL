package com.example.lab9;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Загрузка fxml из правильного пути (автоматически ищет в resources/com/example/lab9/)
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        // Если сцена не инициализирована в FXML, укажите размеры по умолчанию
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        stage.setTitle("Библиотека - Лабораторная №9");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
