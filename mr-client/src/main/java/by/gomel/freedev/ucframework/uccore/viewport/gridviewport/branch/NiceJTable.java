package by.gomel.freedev.ucframework.uccore.viewport.gridviewport.branch;

import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.branch.gtheader.GroupableTableHeader;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.ICustomCellEditor;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.IGridToolTipEvent;
import by.march8.api.utils.DateUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.EventObject;

/**
 * Улучшенный компонент JTable
 */
public class NiceJTable extends JTable {
    private boolean doingLayout;
    private int currentRow;
    private boolean isShowToolTip = true;
    private IGridToolTipEvent gridToolTip = null;
    private IGridToolTipEvent toolTipHandler;
    private ICustomCellEditor customCellEditor;
    private boolean isFooter;

    private boolean isColumnWidthChanged;


    public NiceJTable(TableModel tModel) {
        super(tModel);
        applyListener();
//        if (tModel instanceof GeneralTableModel){
//            final GeneralTableModel model = (GeneralTableModel) tModel;
//            final ArrayList<Object> data = model.getDataModel();
//
//        }
    }


    public NiceJTable(TableModel tModel, boolean isFooter) {
        super(tModel);
        this.isFooter = isFooter;
        applyListener();
//        if (tModel instanceof GeneralTableModel){
//            final GeneralTableModel model = (GeneralTableModel) tModel;
//            final ArrayList<Object> data = model.getDataModel();
//
//        }
    }

    public NiceJTable() {
        super();
    }

    public void applyListener() {
        this.getSelectionModel().addListSelectionListener(
                event -> {
                    final int viewRow = getSelectedRow();
                    if (viewRow >= 0) {
                        currentRow = convertRowIndexToModel(viewRow);
                    }
                });
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return getPreferredSize().width < getParent().getWidth();
    }

    @Override
    protected JTableHeader createDefaultTableHeader() {
        return new GroupableTableHeader(columnModel);
    }

    @Override
    public void doLayout() {
        if (getScrollableTracksViewportWidth()) {
            autoResizeMode = AUTO_RESIZE_SUBSEQUENT_COLUMNS;
        }
        doingLayout = true;
        super.doLayout();
        doingLayout = false;
        autoResizeMode = AUTO_RESIZE_OFF;
    }

    @Override
    public void columnMarginChanged(ChangeEvent e) {
        if (isEditing()) {
            removeEditor();
        }

        TableColumn resizingColumn = getTableHeader().getResizingColumn();
        if (resizingColumn != null && autoResizeMode == AUTO_RESIZE_OFF
                && !doingLayout) {
            resizingColumn.setPreferredWidth(resizingColumn.getWidth());
        }
        resizeAndRepaint();
    }

    /**
     * Всплывающая подсказка позывающая содержимое ячейки
     */
    @Override
    public String getToolTipText(MouseEvent event) {
        String result = "";
        if (!isShowToolTip) {
            return result;
        }

        int column = columnAtPoint(event.getPoint());
        int row = rowAtPoint(event.getPoint());

        if (toolTipHandler != null) {
            String toolTip = toolTipHandler.onToolTipActivated(getValueAt(row, column), row, column, convertRowIndexToModel(row));
            if (toolTip != null) {
                if (!toolTip.trim().equals("")) {
                    return toolTip;
                }
            }
        }

        if (column != -1 && row != -1 && getValueAt(row, column) != null) {
            switch (getValueAt(row, column).getClass().toString()) {
                case "class java.lang.String":
                    result = (String) getValueAt(row, column);
                    break;
                case "class java.sql.Date": {
                    result = getValueAt(row, column).toString();
                    break;
                }
                case "class java.util.Date": {
                    result = DateUtils.getNormalDateFormat((Date) getValueAt(row, column));
                    break;
                }
                case "class java.lang.Boolean":
                    result = "" + ((boolean) getValueAt(row, column));
                    break;
                case "class java.lang.Integer":
                    result = "" + (int) getValueAt(row, column);
                default:
                    break;
            }
        }
        return result;
    }

    public int getSelectedRowIndex() {
        return currentRow;
    }

    @SuppressWarnings("unused")
    public boolean isShowToolTip() {
        return isShowToolTip;
    }

    public void setShowToolTip(final boolean isShowToolTip) {
        this.isShowToolTip = isShowToolTip;
    }

    public void setToolTipHandler(final IGridToolTipEvent toolTipHandler) {
        this.toolTipHandler = toolTipHandler;
    }

    @Override
    public boolean editCellAt(int row, int column, EventObject e) {
        if (customCellEditor != null) {
            boolean result = super.editCellAt(row, column, e);
            final Component editor = getEditorComponent();
            if (editor == null || !(editor instanceof JTextField)) {
                return result;
            }
            if (e instanceof MouseEvent) {
                EventQueue.invokeLater(() -> {
                    ((JTextComponent) editor).requestFocus();
                    ((JTextComponent) editor).selectAll();
                });
            } else {
                ((JTextComponent) editor).requestFocus();
                ((JTextComponent) editor).selectAll();
            }
            return result;
        } else {
            return super.editCellAt(row, column, e);
        }
    }

    public ICustomCellEditor getCustomCellEditor() {
        return customCellEditor;
    }

    public void setCustomCellEditor(final ICustomCellEditor customCellEditor) {
        this.customCellEditor = customCellEditor;
    }

    public boolean getColumnWidthChanged() {
        return isColumnWidthChanged;
    }

    public void setColumnWidthChanged(boolean widthChanged) {
        isColumnWidthChanged = widthChanged;
    }


}
