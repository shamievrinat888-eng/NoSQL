package com.example.lab8;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML private TableView<Movie> tblMovies;
    @FXML private TableColumn<Movie, String> colTitle;
    @FXML private TableColumn<Movie, String> colGenre;
    @FXML private TableColumn<Movie, Integer> colYear;
    @FXML private Label lblStatus;

    private final ObservableList<Movie> movieList = FXCollections.observableArrayList();
    private Color currentTextColor = Color.BLACK;

    @FXML
    public void initialize() {
        // Связываем колонки таблицы со свойствами класса Movie
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));

        // Заполняем таблицу демонстрационными динамическими данными
        movieList.add(new Movie("Интерстеллар", "Фантастика", 2014));
        movieList.add(new Movie("Начало", "Триллер", 2010));
        movieList.add(new Movie("Матрица", "Боевик", 1999));

        tblMovies.setItems(movieList);
    }

    @FXML
    private void onAddMovie() throws IOException {
        openDialog(null); // Передаем null, так как это создание новой записи
    }

    @FXML
    private void onEditMovie() throws IOException {
        Movie selected = tblMovies.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblStatus.setText("⚠️ Сначала выберите фильм из таблицы!");
            return;
        }
        openDialog(selected);
    }

    private void openDialog(Movie targetMovie) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("movie-dialog.fxml"));
        Parent root = loader.load();
        MovieDialogController controller = loader.getController();

        // Передаем текущий цвет интерфейса и объект фильма (если редактируем)
        controller.setInitialData(currentTextColor, targetMovie);

        Stage dialog = new Stage();
        dialog.initOwner(tblMovies.getScene().getWindow());
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle(targetMovie == null ? "Добавить фильм" : "Редактировать фильм");
        dialog.setScene(new Scene(root));
        dialog.showAndWait();

        // Если пользователь нажал кнопку «Применить»
        if (controller.isApplied()) {
            // Обновляем цвет текста всей таблицы
            currentTextColor = controller.getSelectedColor();
            tblMovies.setStyle("-fx-text-fill: " + toCss(currentTextColor) + ";");

            // Получаем данные фильма из диалога
            String title = controller.getTxtTitle();
            String genre = controller.getTxtGenre();
            int year = controller.getTxtYear();

            if (targetMovie == null) {
                // Добавление новой динамической строки
                movieList.add(new Movie(title, genre, year));
                lblStatus.setText("Фильм '" + title + "' успешно добавлен.");
            } else {
                // Динамическое обновление существующей строки
                targetMovie.setTitle(title);
                targetMovie.setGenre(genre);
                targetMovie.setYear(year);
                tblMovies.refresh();
                lblStatus.setText("Данные фильма обновлены.");
            }
        }
    }

    @FXML
    private void onDeleteMovie() {
        int selectedIndex = tblMovies.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Movie removed = movieList.remove(selectedIndex);
            lblStatus.setText("Удален фильм: " + removed.getTitle());
        } else {
            lblStatus.setText("⚠️ Выберите строку для удаления!");
        }
    }

    @FXML
    private void onChooseColor() throws IOException {
        openDialog(tblMovies.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void onOpen() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Открыть каталог");
        File file = chooser.showOpenDialog(tblMovies.getScene().getWindow());
        if (file == null) return;

        try {
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            movieList.clear();
            for (String line : lines) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    movieList.add(new Movie(parts[0].trim(), parts[1].trim(), Integer.parseInt(parts[2].trim())));
                }
            }
            lblStatus.setText("Загружено из файла: " + file.getName());
        } catch (Exception ex) {
            showError("Неверный формат файла. Используйте разделитель ';'");
        }
    }

    @FXML
    private void onSaveAs() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Сохранить каталог");
        chooser.setInitialFileName("movies.txt");
        File file = chooser.showSaveDialog(tblMovies.getScene().getWindow());
        if (file == null) return;

        try {
            List<String> lines = new ArrayList<>();
            for (Movie m : movieList) {
                lines.add(m.getTitle() + ";" + m.getGenre() + ";" + m.getYear());
            }
            Files.writeString(file.toPath(), String.join("\n", lines), StandardCharsets.UTF_8);
            lblStatus.setText("Сохранено в: " + file.getName());
        } catch (IOException ex) {
            showError(ex.getMessage());
        }
    }

    @FXML private void onAbout() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("О программе");
        a.setHeaderText("Динамический Каталог Фильмов");
        a.setContentText("Лабораторная работа №8\nВариант №9\nПолная поддержка динамического CRUD.");
        a.showAndWait();
    }

    @FXML private void onExit() { System.exit(0); }

    private String toCss(Color c) {
        return String.format("#%02X%02X%02X", Math.round(c.getRed()*255), Math.round(c.getGreen()*255), Math.round(c.getBlue()*255));
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(msg);
        a.showAndWait();
    }
}
