package by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces;

import javax.swing.*;
import javax.swing.table.TableColumnModel;

/**
 * Интерфейс позволяет обрабатывать ручное редактирование таблицы с помощью
 * внешних визуальных компонентов
 *
 * @author Andy 26.12.14.
 */
public interface ICustomCellEditor {

    /**
     * Метод вызывается при инициализации модели таблицы.
     * В реализацию метода помещается код, определяющий компонент
     * редактирования для конкретного столбца грида
     */
    void initialCellEditor(TableColumnModel columnModel);

    /**
     * Метод устанавливает признак редактируемости ячейки в позиции column
     * Аналогичен методу JTable. Но т.к. мы ушли от стандартного грида с его векторами
     * к гриду с POJO в качестве записей, решение будет такое если не придумаю замену
     *
     */
    boolean isCellEditable(int column);

    /**
     * Метод срабатывает при редактировании ячейки грида, аналогичен JTable
     */
    void setValueAt(final int columnIndex, final Object sourceValue, final Object changeValue);

    /**
     * Метод срабатывает когда пользователь находясь в последней строке последней колонки таблицы
     * нажимает [Enter]
     */
    boolean onEndOfTable(JTable table);
}
