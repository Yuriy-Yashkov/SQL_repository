package by.march8.ecs.application.modules.cut.view;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.swing.*;

/**
 * Абстрактный класс диалога Swing, содержащий JavaFX-интерфейс.
 * <p>
 * Этот класс создаёт окно {@link JDialog}, внутри которого размещён {@link JFXPanel},
 * служащий "мостом" между Swing и JavaFX. JavaFX-контент загружается в несколько шагов:
 *
 * <ol>
 *   <li>Выполнение {@link #createContent()} в JavaFX-потоке (через {@link Platform#runLater(Runnable)}).</li>
 *   <li>Установка сцены в {@link JFXPanel}.</li>
 *   <li>Ожидание следующего JavaFX-прохода layout (второй {@code runLater}).</li>
 *   <li>Вызов {@link #onFxReady(Parent)} после того, как JavaFX полностью построил UI
 *       и доступен корректный размер root-ноды.</li>
 *   <li>Вызов {@code pack()} и позиционирование окна уже в Swing-потоке.</li>
 * </ol>
 *
 * Такой жизненный цикл гарантирует:
 * <ul>
 *     <li>корректное получение размеров JavaFX UI после layout;</li>
 *     <li>вызов Swing-методов только в EDT (Event Dispatch Thread);</li>
 *     <li>отсутствие артефактов, когда окно появляется "недоразмеренным".</li>
 * </ul>
 *
 * Чтобы использовать класс, необходимо реализовать два метода:
 * <ul>
 *     <li>{@link #createContent()} — создание JavaFX UI;</li>
 *     <li>{@link #onFxReady(Parent)} — реакция на готовность UI (например,
 *         вычисление размеров и установка предпочтительного размера).</li>
 * </ul>
 *
 * @author
 */
public abstract class BaseFxDialog extends JDialog {

    public BaseFxDialog(JFrame owner, String title) {
        super(owner, true); // modal
        setTitle(title);

        JFXPanel fxPanel = new JFXPanel(); // переходник между Swing и JavaFX
        add(fxPanel);

        // 1️⃣ Загружаем JavaFX UI
        Platform.runLater(() -> {
            Parent root = createContent();
            Scene scene = new Scene(root);
            fxPanel.setScene(scene);

            // 2️⃣ Ждём следующего JavaFX цикла после установки сцены
            Platform.runLater(() -> {

                onFxReady(root); // <-- сюда придёт реальный размер JavaFX формы

                // 3️⃣ И только потом вызываем Swing pack().
                // переключаемся обратно в Swing-поток
                SwingUtilities.invokeLater(() -> {
                    pack();                    // Подгоняем размер под JavaFX, pack() должен быть вызван в Swing-потоке.
                    setLocationRelativeTo(owner); // позиционирование окна тоже делается в Swing-потоке.
                });
            });
        });
    }

    /** Каждый наследник должен вернуть JavaFX UI */
    protected abstract Parent createContent();

    protected abstract void onFxReady(Parent root);      // можно переопределить в наследнике
}
