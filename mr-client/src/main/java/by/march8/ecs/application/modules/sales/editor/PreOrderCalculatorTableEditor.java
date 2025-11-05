package by.march8.ecs.application.modules.sales.editor;

import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.ICustomCellEditor;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.ecs.application.modules.sales.mode.PreOrderCalculatorMode;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.CallBack;
import by.march8.entities.sales.PreOrderCalculatorItem;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PreOrderCalculatorTableEditor implements ICustomCellEditor {

    private GridViewPort gridViewPort;
    private CallBack callBack;
    private PreOrderCalculatorMode preOrderCalculatorMode;


    public PreOrderCalculatorTableEditor(GridViewPort gridViewPort, PreOrderCalculatorMode mode) {
        this.gridViewPort = gridViewPort;
        this.preOrderCalculatorMode = mode;
        callBack = mode;
    }

    @Override
    public void initialCellEditor(final TableColumnModel columnModel) {
        final UCTextField tfDiscount = new UCTextField();
        tfDiscount.setComponentParams(null, Float.class, 1);

        TableColumn column = columnModel.getColumn(12);
        column.setCellEditor(new UCTextFieldEditor(tfDiscount));
    }

    @Override
    public boolean isCellEditable(final int column) {

        return column == 12;
    }

    @Override
    public void setValueAt(final int columnIndex, final Object sourceValue, final Object changeValue) {
        PreOrderCalculatorItem editableItem = (PreOrderCalculatorItem) sourceValue;
        if (editableItem != null) {
            if (columnIndex == 12) {
                double source = editableItem.getDiscount();
                double change = 0;
                if (((String) changeValue).trim().equals("")) {
                    change = 1.0;
                } else {
                    change = Double.valueOf((String) changeValue);
                }
                if (change != source) {
                    editableItem.setDiscount(change);
                    preOrderCalculatorMode.calculateItem(editableItem);
                    gridViewPort.updateViewPort();
                    callBack.onCallBack();
                }
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

}
