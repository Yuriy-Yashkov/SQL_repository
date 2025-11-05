package by.march8.ecs.application.modules.sales.editor;

import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.ICustomCellEditor;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.ecs.application.modules.filemanager.model.ColorPresetHelper;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.CallBack;
import by.march8.entities.sales.PreOrderSaleDocumentItem;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ProductParamsTableEditor implements ICustomCellEditor {

    private GridViewPort gridViewPort;
    private CallBack callBack;


    public ProductParamsTableEditor(GridViewPort gridViewPort) {
        this.gridViewPort = gridViewPort;
    }

    @Override
    public void initialCellEditor(final TableColumnModel columnModel) {
        final UCTextField tfAmount = new UCTextField();
        tfAmount.setComponentParams(null, Float.class, 1);

        TableColumn column = columnModel.getColumn(2);
        column.setCellEditor(new UCTextFieldEditor(tfAmount));

        JComboBox<String> cbItemColor = new JComboBox<>();
        ColorPresetHelper.fillingColorListAsStringList(cbItemColor);

        final MyComboBoxEditor cbeItemColor = new MyComboBoxEditor(cbItemColor);

        column = columnModel.getColumn(1);
        column.setCellEditor(cbeItemColor);
    }

    @Override
    public boolean isCellEditable(final int column) {
        return column == 1 || column == 2;
    }

    @Override
    public void setValueAt(final int columnIndex, final Object sourceValue, final Object changeValue) {
        PreOrderSaleDocumentItem editableItem = (PreOrderSaleDocumentItem) sourceValue;
        if (editableItem != null) {
            if (columnIndex == 2) {
                double source = editableItem.getAmount();
                double change = 0;
                if (((String) changeValue).trim().equals("")) {
                    change = 1.0;
                } else {
                    change = Double.valueOf((String) changeValue);
                }
                if (change != source) {
                    editableItem.setAmount(change);
                    //gridViewPort.updateViewPort();
                    //callBack.onCallBack();
                }
            }

            if (columnIndex == 1) {
                editableItem.setItemColor((String) changeValue);
                //gridViewPort.updateViewPort();
            }
        }
    }

    @Override
    public boolean onEndOfTable(final JTable table) {
        return false;
    }

    class UCTextFieldEditor extends DefaultCellEditor {
        public UCTextFieldEditor(UCTextField component) {
            super(component);
            component.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(final KeyEvent e) {
                    int key = e.getKeyCode();
                    if (key == KeyEvent.VK_ENTER) {
                        gridViewPort.nextCell(e);
                    }
                }
            });
        }
    }

    class MyComboBoxEditor extends DefaultCellEditor {

        public MyComboBoxEditor(JComboBox component) {
            super(component);
        }
    }
}
