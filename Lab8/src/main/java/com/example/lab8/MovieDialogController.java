package com.example.lab8;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MovieDialogController {
    @FXML private TextField txtTitle;
    @FXML private TextField txtGenre;
    @FXML private TextField txtYear;
    @FXML private ColorPicker colorPicker;

    private boolean applied = false;

    public void setInitialData(Color color, Movie movie) {
        colorPicker.setValue(color);
        if (movie != null) {
            txtTitle.setText(movie.getTitle());
            txtGenre.setText(movie.getGenre());
            txtYear.setText(String.valueOf(movie.getYear()));
        }
    }

    public boolean isApplied() { return applied; }
    public Color getSelectedColor() { return colorPicker.getValue(); }
    public String getTxtTitle() { return txtTitle.getText().trim(); }
    public String getTxtGenre() { return txtGenre.getText().trim(); }

    public int getTxtYear() {
        try {
            return Integer.parseInt(txtYear.getText().trim());
        } catch (NumberFormatException e) {
            return 2026; // Год по умолчанию при ошибке ввода
        }
    }

    @FXML
    private void onApply() {
        if (!getTxtTitle().isEmpty()) {
            applied = true;
            close();
        }
    }

    @FXML
    private void onCancel() {
        applied = false;
        close();
    }

    private void close() {
        ((Stage) colorPicker.getScene().getWindow()).close();
    }
}
