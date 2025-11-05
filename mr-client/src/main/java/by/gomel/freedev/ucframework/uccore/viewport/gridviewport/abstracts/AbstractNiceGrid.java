package by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts;

import by.gomel.freedev.ucframework.uccore.utils.TableUtils;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.branch.NiceJTable;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.ICustomCellEditor;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.IMultiSelector;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.ITableEvent;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.IViewPortActions;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.model.GeneralTableModel;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.model.TableCell;
import by.march8.api.BaseEntity;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;

/**
 * Базовый абстрактный класс инкапсулирует системные методы
 * Created by Andy on 19.12.2014.
 */
public abstract class AbstractNiceGrid<T> extends Container implements IViewPortActions<T> {

    protected final Class<?> c;
    protected JScrollPane scrollPane;
    protected NiceJTable tView;
    protected GeneralTableModel<T> tModel;
    protected ArrayList<T> data;
    protected TableFilterHeader filterHeader;

    protected ITableEvent tableEventHandler;
    protected Object updatedObject = null;
    protected Object deletedObject = null;
    protected NiceJTable tFooter;
    protected GeneralTableModel<T> tFooterModel;
    protected ArrayList<T> footerData;
    private boolean multiSelect = false;
    private boolean columnLimit = false;
    private int preDeleteRow = 0;
    private int minSelectedRow = -1;
    private int maxSelectedRow = -1;
    private boolean tableModelListenerIsChanging = false;
    private JPanel footerPanel = null;
    private JLabel lblFooter = null;
    private int currentIndex;
    private boolean ignoreNotEditableCells = false;


    protected AbstractNiceGrid(final Class<?> c, boolean multiSelect) {
        this(c, multiSelect, false);
    }

    protected AbstractNiceGrid(final Class<?> c) {
        this(c, false, false);
    }

    protected AbstractNiceGrid(final Class<?> c, boolean multiSelect, boolean columnLimit) {
        super();
        this.c = c;
        this.multiSelect = multiSelect;
        this.columnLimit = columnLimit;
        initComponent();
        filterHeader = new TableFilterHeader(tView, AutoChoices.ENABLED);
    }

    /**
     * Метод возвращает компонент NiceJTable
     */
    public NiceJTable getTable() {
        return tView;
    }

    /**
     * Метод возвращает модель данных
     */
    public ArrayList<T> getDataModel() {
        return data;
    }

    /**
     * Метод определяет обработчик событий типа ITableEven для таблицы t
     */
    public void setTableEventHandler(final ITableEvent tableEventHandler) {
        if (tableEventHandler != null) {
            this.tableEventHandler = tableEventHandler;
            initialisationEvents();
            tView.applyListener();
        }
    }

    protected void initializeFooterTable(T item) {
        if (tFooter == null) {
            footerData = new ArrayList<>();
            tFooterModel = new GeneralTableModel<>(footerData, c, item);
            tFooter = new NiceJTable(tFooterModel);
            //tFooter

            TableColumnModel columnModel = tView.getColumnModel();
            columnModel.addColumnModelListener(new TableColumnWidthListener());

            footerData.add(item);
            //tFooter.setTableHeader(null);

            final TableColumnModel sourceModel = tView.getColumnModel();
            final TableColumnModel destModel = tFooter.getColumnModel();

            tFooter.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            for (int i = 0; i < sourceModel.getColumnCount(); i++) {
                destModel.getColumn(i).setPreferredWidth(sourceModel.getColumn(i).getPreferredWidth());
                destModel.getColumn(i).setMinWidth(sourceModel.getColumn(i).getMinWidth());
                destModel.getColumn(i).setMaxWidth(sourceModel.getColumn(i).getMaxWidth());
            }

            JScrollPane spFooter = new JScrollPane(tFooter, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            spFooter.setPreferredSize(new Dimension(0, 20 + 20 * 1));

            tFooter.getTableHeader().setPreferredSize(new Dimension(0, 0));
            add(spFooter, BorderLayout.SOUTH);
        }
    }

    public void setCustomCellEditor(ICustomCellEditor customCellEditor) {
        if (customCellEditor == null) {
            return;
        }

        tModel.setCustomCellEditor(customCellEditor);
        customCellEditor.initialCellEditor(this.getTable().getColumnModel());
        this.getTable().setCustomCellEditor(customCellEditor);

        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        tView.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter,
                "action");
        tView.getActionMap().put("action", new EnterAction());
    }


    private void initComponent() {
        data = new ArrayList<>();
        tModel = new GeneralTableModel<>(data, c, multiSelect, columnLimit);
        tView = new NiceJTable(tModel);

        TableUtils.setMultiHeader(tView, c);
        TableUtils.setTableColumnWidth(tView, multiSelect);
        if (multiSelect) {
            TableColumn column = tView.getColumnModel().getColumn(0);
            column.setMaxWidth(20);
            column.setMinWidth(20);
            column.setPreferredWidth(20);
        }

        scrollPane = new JScrollPane(tView);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

    @SuppressWarnings("all")
    private void initialisationEvents() {
        tView.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(final ListSelectionEvent event) {
                        if (event.getValueIsAdjusting()) {
                            return;
                        }

                        minSelectedRow = ((
                                ((DefaultListSelectionModel) event.getSource()).getMinSelectionIndex() ==
                                        ((DefaultListSelectionModel) event.getSource()).getMaxSelectionIndex()) ?
                                -1 : ((DefaultListSelectionModel) event.getSource()).getMinSelectionIndex());

                        maxSelectedRow = ((((DefaultListSelectionModel) event.getSource()).getMinSelectionIndex() ==
                                ((DefaultListSelectionModel) event.getSource()).getMaxSelectionIndex()) ?
                                -1 : ((DefaultListSelectionModel) event.getSource()).getMaxSelectionIndex());


                        if ((tView.getSelectedRow() >= 0) && (tView.getSelectedRowIndex() < data.size())) {
                            if (currentIndex != tView.getSelectedRowIndex()) {
                                tableEventHandler.onSelectChanged(tView.getSelectedRowIndex(), data.get(tView.getSelectedRowIndex()));
                                currentIndex = tView.getSelectedRowIndex();
                            }
                        }
                        // event.get
                    }
                });

        tView.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent me) {
                if (tView.getSelectedRowIndex() >= 0) {

                    int columnIndex = tView.columnAtPoint(me.getPoint());
                    ;

                    if (me.getClickCount() == 2) {
                        tableEventHandler.onDoubleClick(tView.getSelectedRowIndex(), columnIndex, data.get(tView.getSelectedRowIndex()));
                    }
                    if (me.getClickCount() == 1) {
                        tableEventHandler.onClick(tView.getSelectedRowIndex(), columnIndex, data.get(tView.getSelectedRowIndex()));
                    }

/*                    if ((tView.getSelectedRow()>=0)&&(tView.getSelectedRowIndex()<data.size())) {
                        tableEventHandler.onSelectChanged(tView.getSelectedRowIndex(), data.get(tView.getSelectedRowIndex()));
                    }*/
                }
            }
        });


        tView.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (tableModelListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRow == -1 || minSelectedRow == -1) {
                    return;
                }

                tableModelListenerIsChanging = true;
                boolean value = ((Boolean) tModel.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRow; i <= maxSelectedRow; i++) {
                    tModel.setValueAt(Boolean.valueOf(value), tView.convertRowIndexToModel(i), column);
                }

                minSelectedRow = -1;
                maxSelectedRow = -1;

                tableModelListenerIsChanging = false;
            }
        });
    }

    private void scrollToVisible(int rowIndex) {
        tView.scrollRectToVisible(new Rectangle(tView.getCellRect(rowIndex, 0, true)));
    }

    /**
     * Метод делает активной строку по индексу записи в коллекции данных
     */
    protected void setActiveRowByDataIndex(int index) {
        ListSelectionModel selectionModel =
                tView.getSelectionModel();
        int index_ = tView.convertRowIndexToView(index);
        selectionModel.setSelectionInterval(index_, index_);
        scrollToVisible(index_);
    }

    /**
     * Метод делает активной строку по индексу записи в коллекции данных
     */
    protected void setActiveRowByTableIndex(int index) {
        ListSelectionModel selectionModel =
                tView.getSelectionModel();
        int ind = tView.convertRowIndexToView(index);
        int index_ = tView.convertRowIndexToModel(ind);
        scrollToVisible(index_);
        selectionModel.setSelectionInterval(index_, index_);
    }

    /**
     * Метод делает активной строку по объекту из набора данных
     */
    protected void setActiveRowByObject(Object object) {
        int index = getObjectIndexInDataModel(object);
        if (index >= 0) {
            this.setActiveRowByDataIndex(index);
        } else {
            System.out.println("Объект обновления не найден в таблице");
        }
    }

    public int getObjectIndexInDataModel(Object object) {
        try {
            BaseEntity source = (BaseEntity) object;
            for (int i = 0; i < data.size(); i++) {
                BaseEntity item = (BaseEntity) data.get(i);
                if (source.getId() == item.getId()) {
                    return i;
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка приведения " + object + " к типу BaseEntity");
            e.printStackTrace();
        }
        return -1;
    }

    /***/
    protected void beforeDeleteUpdate(Object object) {
        if (tView.getRowCount() == 0) {
            return;
        }
        // Получаем номер выделенной ячейки
        int idInDataSource = getObjectIndexInDataModel(object);
        // Сопоставляем индекс с номером строки в гриде
        preDeleteRow = tView.convertRowIndexToView(idInDataSource);
    }

    protected void afterDeleteUpdate() {
        int rowCount = tView.getRowCount();
        if (rowCount == 0) {
            return;
        }

        if (preDeleteRow >= rowCount - 1) {
            if (preDeleteRow - 1 < 0) {
                if (preDeleteRow == rowCount - 1) {
                    setActiveRowByTableIndex(preDeleteRow);
                }
            } else {
                if (preDeleteRow == rowCount - 1) {
                    setActiveRowByTableIndex(preDeleteRow);
                } else {
                    setActiveRowByTableIndex(preDeleteRow - 1);
                }
            }
        } else {
            setActiveRowByTableIndex(preDeleteRow);
        }
    }

    public GeneralTableModel getTableModel() {
        return tModel;
    }

    public IMultiSelector getMultiSelector() {
        return tModel;
    }

    @Override
    public int getSelectedRowIndex() {
        return tView.getSelectedRowIndex();
    }

    public Class<?> getViewEntityClass() {
        return c;
    }

    public void initialFooter() {
        footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblFooter = new JLabel();
        footerPanel.add(lblFooter);
        add(footerPanel, BorderLayout.SOUTH);
    }

    public JPanel getFooterPanel() {
        return footerPanel;
    }

    public JLabel getFooterTextComponent() {
        return lblFooter;
    }

    /**
     * Метод устанавливает кастомный текст в нижнюю текстовую панель грида
     *
     * @param value строковое значение в фузуре грида
     */
    public void setFooterValue(String value) {
        if (footerPanel != null) {
            if (value != null) {
                lblFooter.setText(value.trim());
            }
        }
    }

    /**
     * Метод устанавливает кастомный рендер для столбцов грида
     *
     * @param cellRenderer рендер
     */
    public void setCustomCellRender(TableCellRenderer cellRenderer) {
        TableColumn tableColumn;
        if (multiSelect) {
            for (int i = 1; i < tView.getColumnCount(); i++) {
                tableColumn = tView.getColumnModel().getColumn(i);
                tableColumn.setCellRenderer(cellRenderer);
            }
        } else {
            for (int i = 0; i < tView.getColumnCount(); i++) {
                tableColumn = tView.getColumnModel().getColumn(i);
                tableColumn.setCellRenderer(cellRenderer);
            }
        }
    }

    public void setCustomCellRender(TableCellRenderer cellRenderer, int startRow) {
        TableColumn tableColumn;
        for (int i = startRow; i < tView.getColumnCount(); i++) {
            tableColumn = tView.getColumnModel().getColumn(i);
            tableColumn.setCellRenderer(cellRenderer);
        }
    }

    public TableFilterHeader getFilterHeader() {
        return filterHeader;
    }

    public void setFilterHeader(final TableFilterHeader filterHeader) {
        this.filterHeader = filterHeader;
    }

    /**
     * Метод устанавливает ризнак игнорирования не-редактируемой ячейки при переходе по Enter-у
     *
     * @param ignoreNotEditableCells флаг игнорирования нередактируемых ячеек
     */
    public void setIgnoreNotEditableCells(final boolean ignoreNotEditableCells) {
        this.ignoreNotEditableCells = ignoreNotEditableCells;
    }

    /**
     * Метод перемещает курсор на следующую ячейку таблицы сверху вниз, слева направо.
     *
     * @param e источник события
     */
    public void nextCell(EventObject e) {
        // Индексы текущей ячейки
        int column = tView.getSelectedColumn();
        int row = tView.getSelectedRow();
        if (column < 0 || row < 0) {
            return;
        }

        // Источник событий
        boolean isEditor = isEditorEvent(e);

        // Текущая ячейка
        TableCell currentCell = new TableCell(row, column, false);

        // Если не игнорируем нередактируемые ячейки
        if (!ignoreNotEditableCells) {
            // Если текущая ячейка последняя
            if (endOfTable(row, column)) {
                if (!isEditor) {
                    eventEndOfTable(currentCell);
                }
            } else {
                // Получаем следующую ячейку
                TableCell cell = getNextCell(row, column);
                //Переводим курсор на новую позицию
                moveToCell(cell);
                // Если ячейка редактируемая
                if (isCellEditable(cell)) {
                    tView.editCellAt(cell.row, cell.column, e);
                }
            }
        } else {
            // Если игнорируем нередактируемые ячейки
            // Если текущая ячейка последняя
            if (endOfTable(row, column)) {
                if (!isEditor) {
                    eventEndOfTable(currentCell);
                }
            } else {
                // Сканируем ячейки в поисках первой попавшейся редактируемой
                TableCell cell;
                TableCell nextCell = getNextCell(row, column);
                if (!isEditor) {
                    // ENTER в гриде
                    cell = enumerationCell(nextCell.row, nextCell.column);
                } else {
                    // ENTER в редакторе
                    cell = enumerationCell(row, column);
                }

                moveToCell(cell);
                if (isCellEditable(cell)) {
                    tView.editCellAt(cell.row, cell.column, e);
                }

            }
        }
        scrollToVisible(currentIndex);
    }

    private void eventEndOfTable(TableCell cell) {
        if (tView.getCustomCellEditor() != null) {
            if (tView.getCustomCellEditor().onEndOfTable(tView)) {
                tModel.fireTableDataChanged();
                if (ignoreNotEditableCells) {
                    TableCell cell_ = getNextCell(cell.row, cell.column);
                    moveToCell(enumerationCell(cell_.row, cell_.column));
                } else {
                    moveToCell(getNextCell(cell.row, cell.column));
                }

                scrollToVisible(tView.getRowCount() - 1);
            }
        }
    }

    /**
     * Метод проходит ячеки грида в поиске первой редактируемой ячейки
     *
     * @param rowIndex    текущая ячейка
     * @param columnIndex текущая ячейка
     * @return ссылка ближайшую редактируемую или на последнюю в гриде ячейку
     */
    private TableCell enumerationCell(int rowIndex, int columnIndex) {
        // если текущая ячейка последняя в гриде
        if (!endOfTable(rowIndex, columnIndex)) {
            TableCell cell = new TableCell(rowIndex, columnIndex, false);
            //cell = getNextCell(cell.row, cell.column);

            while (!tView.isCellEditable(cell.row, cell.column)) {
                cell = getNextCell(cell.row, cell.column);
                // Если в процессе поиска достигли последней ячейки
                if (cell.isLastCell) {
                    return cell;
                }
            }

            if (endOfTable(cell.row, cell.column)) {
                cell.isLastCell = true;
            }
            return cell;
        } else {
            return new TableCell(rowIndex, columnIndex, true);
        }
    }

    /**
     * Метод проверяет, является ячейка редактируемой
     *
     * @param cell ссылка на ячейку
     * @return true если ячейка редактируемая
     */
    private boolean isCellEditable(TableCell cell) {
        return tView.isCellEditable(cell.row, cell.column);
    }

    /**
     * Метод проверяет является ли ячейка последняя в гриде
     *
     * @param rowIndex    индекс строки
     * @param columnIndex индекс столбца
     * @return true если ячека грида последняя
     */
    private boolean endOfTable(int rowIndex, int columnIndex) {
        return rowIndex >= tView.getRowCount() - 1 && columnIndex >= tView.getColumnCount() - 1;
    }

    /**
     * Метод проверяет источник события в гриде
     *
     * @param e событие
     * @return true если источник события компонент редактирования
     */
    private boolean isEditorEvent(EventObject e) {
        return e != null && !(e.getSource() instanceof NiceJTable);
    }

    /**
     * Метод переводим курсор грида на следующую ячейку от предустановленной
     *
     * @param rowIndex    индекс строки
     * @param columnIndex индекс столбца
     * @return ссылка на ячейку после перевода курсора грида
     */
    private TableCell getNextCell(int rowIndex, int columnIndex) {

        int newRow;
        int newCol;
        boolean isLastCell = false;

        // Если курсор в пределах строки
        if (rowIndex < tView.getRowCount() - 1) {
            if (columnIndex < tView.getColumnCount() - 1) {
                //Если курсор в пределах строки
                newRow = rowIndex;
                newCol = columnIndex + 1;
            } else {
                // Если курсор на последней ячейке строки
                // переход на новую строку
                newRow = rowIndex + 1;
                newCol = 0;
            }

        } else {
            // Если курсор на последней строчке таблицы
            if (columnIndex < tView.getColumnCount() - 1) {
                //Если курсор в пределах строки
                newRow = rowIndex;
                newCol = columnIndex + 1;
            } else {
                newRow = rowIndex;
                newCol = columnIndex;
                // Если курсор на последней ячейке строки
                // переход на новую строку
                isLastCell = true;
            }
        }

        return new TableCell(newRow, newCol, isLastCell);
    }

    /**
     * Метод перемещает курсор ячейки на новую позицию
     *
     * @param cell ссылка на ячейку
     */
    private TableCell moveToCell(TableCell cell) {

        tView.setRowSelectionInterval(cell.row, cell.row);
        tView.setColumnSelectionInterval(cell.column, cell.column);
        return cell;
    }

    private class EnterAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            nextCell(e);
        }
    }

    private class TableColumnWidthListener implements TableColumnModelListener {

        @Override
        public void columnMarginChanged(ChangeEvent e) {
            final TableColumnModel sourceModel = tView.getColumnModel();
            final TableColumnModel destModel = tFooter.getColumnModel();

            for (int i = 0; i < sourceModel.getColumnCount(); i++) {
                destModel.getColumn(i).setPreferredWidth(sourceModel.getColumn(i).getPreferredWidth());
                destModel.getColumn(i).setMinWidth(sourceModel.getColumn(i).getMinWidth());
                destModel.getColumn(i).setMaxWidth(sourceModel.getColumn(i).getMaxWidth());
            }
        }

        @Override
        public void columnMoved(TableColumnModelEvent e) {
        }

        @Override
        public void columnAdded(TableColumnModelEvent e) {
        }

        @Override
        public void columnRemoved(TableColumnModelEvent e) {
        }

        @Override
        public void columnSelectionChanged(ListSelectionEvent e) {
        }
    }


}
