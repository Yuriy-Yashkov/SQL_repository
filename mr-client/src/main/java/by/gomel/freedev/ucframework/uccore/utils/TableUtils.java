package by.gomel.freedev.ucframework.uccore.utils;

import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.branch.gtheader.ColumnGroup;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.branch.gtheader.ColumnGroupItem;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.branch.gtheader.GroupableTableHeader;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.IColumnSizeSupport;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.model.ColumnProperty;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.model.CustomTableCellRenderer;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.model.GeneralTableModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;

/**
 * Вспомогательный класс для работы с JTable
 */
public class TableUtils {
    /**
     * Метод устанавливает необходимую ширину колонок у таблицы
     */
    public static void setTableColumnWidth(final JTable table, boolean isMultiselect) {
        int statusColumnId = 0;
        for (int i = 0; i < table.getColumnCount(); i++) {
            final String column = table.getColumnName(i);
            if (column.equals("S")) {
                statusColumnId = i;
                break;
            }
        }
        if (statusColumnId > 0) {
            for (int i = 0; i < table.getColumnCount(); i++) {
                final TableColumn column = table.getColumnModel().getColumn(i);
                column.setCellRenderer(new CustomTableCellRenderer(
                        statusColumnId));
            }
        }

        TableModel tModel = table.getModel();

        if (tModel instanceof IColumnSizeSupport) {
            final IColumnSizeSupport model = (IColumnSizeSupport) table
                    .getModel();
            final int[] size = model.getColumnWidth();
            for (int i = 0; i < size.length; i++) {
                if (size[i] > 0) {
                    table.getColumnModel().getColumn(i).setMinWidth(size[i]);
                } else if (size[i] < 0) {
                    table.getColumnModel().getColumn(i)
                            .setMaxWidth(size[i] * (-1));
                } else {
                    table.getColumnModel().getColumn(i).setMinWidth(0);
                    table.getColumnModel().getColumn(i).setMaxWidth(0);
                    table.getColumnModel().getColumn(i).setWidth(0);
                }
            }

        } else if (tModel instanceof GeneralTableModel) {
            GeneralTableModel model = (GeneralTableModel) tModel;
            ArrayList<ColumnProperty> properties = model.getColumnProperties();
            if (isMultiselect) {
                for (int i = 0; i < properties.size(); i++) {
                    int size = properties.get(i).getWidth();

                    if (size > 0) {
                        table.getColumnModel().getColumn(i + 1).setMinWidth(size);
                    } else if (size < 0) {
                        table.getColumnModel().getColumn(i + 1)
                                .setMaxWidth(size * (-1));
                    } else {
                        table.getColumnModel().getColumn(i + 1).setMinWidth(0);
                        table.getColumnModel().getColumn(i + 1).setMaxWidth(0);
                        table.getColumnModel().getColumn(i + 1).setWidth(0);
                    }
                }
            } else {
                for (int i = 0; i < properties.size(); i++) {
                    int size = properties.get(i).getWidth();

                    if (size > 0) {
                        table.getColumnModel().getColumn(i).setMinWidth(size);
                    } else if (size < 0) {
                        table.getColumnModel().getColumn(i)
                                .setMaxWidth(size * (-1));
                    } else {
                        table.getColumnModel().getColumn(i).setMinWidth(0);
                        table.getColumnModel().getColumn(i).setMaxWidth(0);
                        table.getColumnModel().getColumn(i).setWidth(0);
                    }
                }
            }
        } else {
            System.out.println("Модель \""
                    + table.getModel().getClass().getName()
                    + "\" не реализует IColumnSizeSupport");
        }
    }

    public static void setMultiHeader(final JTable table, final Class<?> clazz) {
        boolean isFind;
        try {
            // if (clazz.getClass().isAnnotationPresent(MultiHeader.class)) {
            TableColumnModel columnModel = table.getColumnModel();
            ArrayList<ColumnGroupItem> group = new ArrayList<>();

            for (int i = 0; i < columnModel.getColumnCount(); i++) {
                isFind = false;
                TableColumn column = columnModel.getColumn(i);
                String[] columnName = table.getModel().getColumnName(i).split("_");
                if (columnName.length > 1) {
                    for (ColumnGroupItem item : group) {
                        ColumnGroup columnGroup = item.getItem();
                        if (item.getName().trim().equals(columnName[0])) {
                            column.setHeaderValue(columnName[1]);
                            columnGroup.add(column);
                            isFind = true;
                        }
                    }

                    if (!isFind) {
                        ColumnGroup g = new ColumnGroup(columnName[0]);
                        column.setHeaderValue(columnName[1]);
                        g.add(column);
                        group.add(new ColumnGroupItem(g, columnName[0]));
                    }
                }
            }

            GroupableTableHeader header = (GroupableTableHeader) table.getTableHeader();
            for (ColumnGroupItem item : group) {
                ColumnGroup columnGroup = item.getItem();
                header.addColumnGroup(columnGroup);
            }
          /*  } else {
                System.out.println("Класс не аннотирован");
                return;
            }
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
