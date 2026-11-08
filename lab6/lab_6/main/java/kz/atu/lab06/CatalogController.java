package kz.atu.lab06;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class CatalogController {

    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> cmbFilterBrand;
    @FXML private ListView<String> listBrands;

    @FXML private TableView<Smartphone> tableSmartphones;
    @FXML private TableColumn<Smartphone, String> colBrand;
    @FXML private TableColumn<Smartphone, String> colModel;
    @FXML private TableColumn<Smartphone, Integer> colMemory;
    @FXML private TableColumn<Smartphone, Double> colPrice;

    @FXML private ComboBox<String> cmbBrand;
    @FXML private TextField txtModel;
    @FXML private TextField txtMemory;
    @FXML private TextField txtPrice;
    @FXML private Label lblCount;

    private final ObservableList<Smartphone> smartphones = FXCollections.observableArrayList();
    private FilteredList<Smartphone> filteredSmartphones;

    @FXML
    public void initialize() {
        // Заполнение начальными тестовыми данными
        smartphones.addAll(
                new Smartphone("iPhone 15", "Apple", 128, 450000),
                new Smartphone("Galaxy S24", "Samsung", 256, 420000),
                new Smartphone("Redmi Note 13", "Xiaomi", 256, 150000),
                new Smartphone("iPhone 13", "Apple", 128, 320000),
                new Smartphone("Galaxy A55", "Samsung", 128, 200000)
        );

        // Настройка столбцов таблицы
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colMemory.setCellValueFactory(new PropertyValueFactory<>("memory"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Инициализация FilteredList
        filteredSmartphones = new FilteredList<>(smartphones, p -> true);
        tableSmartphones.setItems(filteredSmartphones);

        // Заполнение выпадающих списков брендов
        ObservableList<String> brandList = FXCollections.observableArrayList("Apple", "Samsung", "Xiaomi", "Huawei");
        cmbBrand.setItems(brandList);

        cmbFilterBrand.getItems().add("Все");
        cmbFilterBrand.getItems().addAll(brandList);
        cmbFilterBrand.setValue("Все");

        // Заполнение бокового списка быстрого фильтра
        listBrands.getItems().add("Все бренды");
        listBrands.getItems().addAll(brandList);

        // Слушатели для динамического поиска и фильтрации
        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());
        cmbFilterBrand.setOnAction(e -> applyFilter());

        // Связывание бокового ListView с ComboBox-фильтром
        listBrands.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                if (newVal.equals("Все бренды")) {
                    cmbFilterBrand.setValue("Все");
                } else {
                    cmbFilterBrand.setValue(newVal);
                }
                applyFilter();
            }
        });

        // Слушатель выбора строки таблицы (перенос данных в форму заполнения)
        tableSmartphones.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cmbBrand.setValue(newVal.getBrand());
                txtModel.setText(newVal.getModel());
                txtMemory.setText(String.valueOf(newVal.getMemory()));
                txtPrice.setText(String.valueOf(newVal.getPrice()));
            }
        });

        updateCount();
    }

    private void applyFilter() {
        String searchText = txtSearch.getText().trim().toLowerCase();
        String selectedBrand = cmbFilterBrand.getValue();

        filteredSmartphones.setPredicate(smartphone -> {
            boolean matchesSearch = smartphone.getModel().toLowerCase().contains(searchText);
            boolean matchesBrand = selectedBrand == null || selectedBrand.equals("Все") ||
                    smartphone.getBrand().equals(selectedBrand);

            return matchesSearch && matchesBrand;
        });

        updateCount();
    }

    @FXML
    private void onAddClick() {
        String brand = cmbBrand.getValue();
        String model = txtModel.getText().trim();
        String memoryText = txtMemory.getText().trim();
        String priceText = txtPrice.getText().trim();

        if (brand == null || model.isBlank() || memoryText.isBlank() || priceText.isBlank()) {
            showError("Пожалуйста, заполните все поля формы.");
            return;
        }

        try {
            int memory = Integer.parseInt(memoryText);
            double price = Double.parseDouble(priceText);

            if (memory <= 0 || price <= 0) {
                showError("Память и цена должны содержать положительные значения.");
                return;
            }

            smartphones.add(new Smartphone(model, brand, memory, price));
            clearInput();
            applyFilter();

        } catch (NumberFormatException e) {
            showError("Некорректный формат числовых данных. Проверьте поля памяти и цены.");
        }
    }

    @FXML
    private void onDeleteClick() {
        Smartphone selected = tableSmartphones.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Выберите смартфон из таблицы для его удаления.");
            return;
        }

        // Подтверждение удаления через Alert Confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setHeaderText(null);
        alert.setContentText("Вы действительно хотите удалить выбранный смартфон: " + selected.getBrand() + " " + selected.getModel() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            smartphones.remove(selected);
            clearInput();
            applyFilter();
        }
    }

    @FXML
    private void onClearFilterClick() {
        txtSearch.clear();
        cmbFilterBrand.setValue("Все");
        listBrands.getSelectionModel().clearSelection();
        applyFilter();
    }

    private void updateCount() {
        lblCount.setText("Найдено записей: " + filteredSmartphones.size());
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка ввода данных");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearInput() {
        cmbBrand.getSelectionModel().clearSelection();
        txtModel.clear();
        txtMemory.clear();
        txtPrice.clear();
        tableSmartphones.getSelectionModel().clearSelection();
    }
}