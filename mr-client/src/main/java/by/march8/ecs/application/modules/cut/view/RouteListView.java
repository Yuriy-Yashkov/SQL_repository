package by.march8.ecs.application.modules.cut.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Представление (View) для отображения списка маршрутов (RouteList).
 * <p>
 * Класс инкапсулирует загрузку JavaFX-контента из FXML-файла
 * {@code RouteListView.fxml}, который находится в ресурсах по пути:
 * <pre>
 * /by/march8/ecs/application/modules/cut/reports/views/RouteListView.fxml
 * </pre>
 * </p>
 *
 * <p>
 * Основное предназначение класса — предоставить корневой узел, который может
 * быть встроен в сцену или другое JavaFX-окно.
 * 👉 Загружает FXML и возвращает узел (Node/Parent) — то есть кусок интерфейса, а не окно и не сцену.
 * </p>
 */
public class RouteListView {

    /**
     * Путь к FXML-файлу с описанием интерфейса.
     */
    private static final String FXML_PATH =
            "/by/march8/ecs/application/modules/cut/reports/views/RouteListView.fxml";

    /**
     * Загружает интерфейс из FXML и возвращает корневой {@link Parent}.
     *
     * @return Корневой узел JavaFX, загруженный из FXML.
     * @throws RuntimeException если файл не найден или произошла ошибка загрузки.
     */
    public Parent createContent() {
        URL fxmlUrl = Objects.requireNonNull(
                getClass().getResource(FXML_PATH),
                "FXML фай не найден: " + FXML_PATH
        );

        FXMLLoader loader = new FXMLLoader(fxmlUrl);

        try {
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить FXML: " + FXML_PATH, e);
        }
    }
}
