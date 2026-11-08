package com.example.lab9;

import java.util.LinkedHashMap;
import java.util.List;

public final class RegistrationValidator {

    public static ValidationResult validate(
            String name, String email, String ageText,
            String cardId, String category, boolean consent,
            List<BookRecord> existingRecords) {

        var errors = new LinkedHashMap<String, String>();

        // Валидация ФИО
        if (name == null || name.trim().length() < 2 || name.trim().length() > 80) {
            errors.put("name", "ФИО: от 2 до 80 символов");
        }

        // Валидация Email
        if (email == null || !email.trim().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            errors.put("email", "Введите корректный email");
        }

        // Валидация возраста
        try {
            int age = Integer.parseInt(ageText.trim());
            if (age < 16 || age > 100) {
                errors.put("age", "Возраст: от 16 до 100 лет");
            }
        } catch (NumberFormatException | NullPointerException e) {
            errors.put("age", "Возраст должен быть целым числом");
        }

        // Валидация уникального идентификатора (Вариант 4)
        if (cardId == null || !cardId.trim().matches("^LIB-\\d{4}$")) {
            errors.put("cardId", "Формат ID: LIB-XXXX (где X - цифры)");
        } else {
            // Проверка на уникальность среди уже добавленных записей
            boolean isDuplicate = existingRecords.stream()
                    .anyMatch(r -> r.getLibraryCardId().equalsIgnoreCase(cardId.trim()));
            if (isDuplicate) {
                errors.put("cardId", "Этот ID читателя уже зарегистрирован!");
            }
        }

        // Валидация категории/формата
        if (category == null || category.isBlank()) {
            errors.put("category", "Выберите категорию читателя");
        }

        // Валидация согласия
        if (!consent) {
            errors.put("consent", "Необходимо согласие с правилами");
        }

        return new ValidationResult(errors);
    }
}
