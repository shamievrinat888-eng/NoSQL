package kz.atu.lab07;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class EditorController {

    @FXML private MenuItem miNew;
    @FXML private MenuItem miOpen;
    @FXML private MenuItem miSave;
    @FXML private TextArea txtEditor;
    @FXML private Label lblStatus;

    private Path currentFile = null;
    private boolean isChanged = false; // Флаг для 20 варианта

    @FXML
    public void initialize() {
        // Установка горячих клавиш (Accelerators)
        miNew.setAccelerator(KeyCombination.keyCombination("Shortcut+N"));
        miOpen.setAccelerator(KeyCombination.keyCombination("Shortcut+O"));
        miSave.setAccelerator(KeyCombination.keyCombination("Shortcut+S"));

        // Слушатель изменения текста в TextArea
        txtEditor.textProperty().addListener((obs, oldText, newText) -> {
            lblStatus.setText("Символов: " + newText.length());
            isChanged = true; // Фиксируем изменения
        });
    }

    // Проверка на наличие несохраненных изменений (20 вариант)
    public boolean checkUnsavedChanges() {
        if (!isChanged) {
            return true;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Несохраненные изменения");
        alert.setHeaderText("В документе есть несохраненные изменения.");
        alert.setContentText("Вы уверены, что хотите продолжить без сохранения?");

        ButtonType buttonYes = new ButtonType("Да");
        ButtonType buttonNo = new ButtonType("Нет", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == buttonYes;
    }

    @FXML
    private void onNew() {
        if (checkUnsavedChanges()) {
            txtEditor.clear();
            currentFile = null;
            isChanged = false;
            lblStatus.setText("Новый документ");
        }
    }

    @FXML
    private void onOpen() {
        if (!checkUnsavedChanges()) return;

        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt"));
        File file = chooser.showOpenDialog(txtEditor.getScene().getWindow());

        if (file == null) return;

        try {
            txtEditor.setText(Files.readString(file.toPath(), StandardCharsets.UTF_8));
            currentFile = file.toPath();
            isChanged = false; // Текст только что загружен, изменений нет
            lblStatus.setText("Открыт: " + file.getName());
        } catch (IOException ex) {
            showError("Ошибка открытия: " + ex.getMessage());
        }
    }

    @FXML
    private void onSave() {
        if (currentFile == null) {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt"));
            File file = chooser.showSaveDialog(txtEditor.getScene().getWindow());
            if (file == null) return;
            currentFile = file.toPath();
        }

        try {
            Files.writeString(currentFile, txtEditor.getText(), StandardCharsets.UTF_8);
            isChanged = false; // Изменения успешно сохранены
            lblStatus.setText("Сохранено: " + currentFile.getFileName());
        } catch (IOException ex) {
            showError("Ошибка сохранения: " + ex.getMessage());
        }
    }

    @FXML private void onCopy() { txtEditor.copy(); }
    @FXML private void onCut() { txtEditor.cut(); }
    @FXML private void onPaste() { txtEditor.paste(); }

    @FXML
    private void onExit() {
        if (checkUnsavedChanges()) {
            Platform.exit();
        }
    }

    @FXML
    private void onAbout() {
        Alert a = new Alert(Alert.AlertType.INFORMATION,
                "Редактор заметок. Лабораторная работа №7.", ButtonType.OK);
        a.setHeaderText("О программе");
        a.showAndWait();
    }

    private void showError(String message) {
        Alert a = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        a.setHeaderText("Ошибка");
        a.showAndWait();
    }
}
