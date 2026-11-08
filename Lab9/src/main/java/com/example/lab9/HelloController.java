package com.example.lab9;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HelloController {

    // Элементы формы
    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtAge;
    @FXML private TextField txtCardId;
    @FXML private ComboBox<String> cmbCategory;
    @FXML private CheckBox chkConsent;

    // Скрытые Label для вывода ошибок (под каждым полем)
    @FXML private Label lblNameError;
    @FXML private Label lblEmailError;
    @FXML private Label lblAgeError;
    @FXML private Label lblCardIdError;
    @FXML private Label lblCategoryError;
    @FXML private Label lblConsentError;

    // Таблица данных
    @FXML private TableView<BookRecord> tableView;
    @FXML private TableColumn<BookRecord, String> colName;
    @FXML private TableColumn<BookRecord, String> colEmail;
    @FXML private TableColumn<BookRecord, Integer> colAge;
    @FXML private TableColumn<BookRecord, String> colCardId;
    @FXML private TableColumn<BookRecord, String> colCategory;

    private final ObservableList<BookRecord> records = FXCollections.observableArrayList();
    private final PseudoClass invalidPseudoClass = PseudoClass.getPseudoClass("invalid");

    @FXML
    public void initialize() {
        // Настройка ComboBox и таблицы
        cmbCategory.setItems(FXCollections.observableArrayList("Студент", "Преподаватель", "Посторонний читатель"));

        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colCardId.setCellValueFactory(new PropertyValueFactory<>("libraryCardId"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));

        tableView.setItems(records);

        // Ограничение ввода (TextFormatter) для возраста - только до 3 цифр
        txtAge.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("\\d{0,3}") ? change : null));
    }

    @FXML
    private void onRegister() {
        // Запускаем валидацию
        ValidationResult result = RegistrationValidator.validate(
                txtName.getText(), txtEmail.getText(), txtAge.getText(),
                txtCardId.getText(), cmbCategory.getValue(), chkConsent.isSelected(),
                records
        );

        showErrors(result);

        if (!result.isValid()) {
            new Alert(Alert.AlertType.WARNING, "Исправьте ошибки в форме!").showAndWait();
            return;
        }

        try {
            // Безопасное добавление объекта
            BookRecord newRecord = new BookRecord(
                    txtName.getText().trim(),
                    txtEmail.getText().trim(),
                    Integer.parseInt(txtAge.getText().trim()),
                    txtCardId.getText().trim(),
                    cmbCategory.getValue()
            );
            records.add(newRecord);
            clearForm();

            // Доп. задание: Сохранение в CSV/Текст с безопасной обработкой IOException
            saveToCSV();

        } catch (RuntimeException e) {
            new Alert(Alert.AlertType.ERROR, "Критическая ошибка при сохранении данных.").showAndWait();
        }
    }

    private void showErrors(ValidationResult r) {
        // Вывод текстов ошибок
        lblNameError.setText(r.error("name"));
        lblEmailError.setText(r.error("email"));
        lblAgeError.setText(r.error("age"));
        lblCardIdError.setText(r.error("cardId"));
        lblCategoryError.setText(r.error("category"));
        lblConsentError.setText(r.error("consent"));

        // Переключение CSS-псевдоклассов для подсветки границ полей
        txtName.pseudoClassStateChanged(invalidPseudoClass, !r.error("name").isEmpty());
        txtEmail.pseudoClassStateChanged(invalidPseudoClass, !r.error("email").isEmpty());
        txtAge.pseudoClassStateChanged(invalidPseudoClass, !r.error("age").isEmpty());
        txtCardId.pseudoClassStateChanged(invalidPseudoClass, !r.error("cardId").isEmpty());
    }

    @FXML
    private void clearForm() {
        txtName.clear();
        txtEmail.clear();
        txtAge.clear();
        txtCardId.clear();
        cmbCategory.setValue(null);
        chkConsent.setSelected(false);
        showErrors(new ValidationResult(java.util.Map.of())); // Сброс визуальных ошибок
    }

    private void saveToCSV() {
        StringBuilder sb = new StringBuilder();
        for (BookRecord r : records) {
            sb.append(String.format("%s;%s;%d;%s;%s\n",
                    r.getFullName(), r.getEmail(), r.getAge(), r.getLibraryCardId(), r.getCategory()));
        }
        try {
            Files.writeString(Path.of("library_records.csv"), sb.toString());
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Не удалось сохранить данные в CSV-файл").showAndWait();
        }
    }
}
