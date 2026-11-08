package kz.atu.lab07;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();

        // Получаем ссылку на контроллер, чтобы вызвать метод проверки изменений
        EditorController controller = loader.getController();

        primaryStage.setTitle("Редактор заметок");
        primaryStage.setScene(new Scene(root, 600, 400));

        // Перехват закрытия через системный крестик окна (Вариант 20)
        primaryStage.setOnCloseRequest(event -> {
            if (!controller.checkUnsavedChanges()) {
                event.consume(); // Отменяем закрытие, если пользователь выбрал "Нет"
            }
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
