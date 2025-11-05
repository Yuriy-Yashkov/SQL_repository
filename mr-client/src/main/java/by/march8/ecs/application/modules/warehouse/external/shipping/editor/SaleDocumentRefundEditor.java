package by.march8.ecs.application.modules.warehouse.external.shipping.editor;

import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.ICustomCellEditor;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentManager;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.CallBack;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentItemView;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Панель редактирования ценника для возвратных документов
 *
 * @author Andy 15.06.2016.
 */
public class SaleDocumentRefundEditor implements ICustomCellEditor {
    private SaleDocumentBase documentBase;
    private GridViewPort gridViewPort;

    private CallBack callBack;


    public SaleDocumentRefundEditor(GridViewPort gridViewPort, SaleDocumentBase documentBase) {
        this.documentBase = documentBase;
        this.gridViewPort = gridViewPort;
    }

    @Override
    public void initialCellEditor(final TableColumnModel columnModel) {

        final UCTextField tfValuePrice = new UCTextField();
        tfValuePrice.setComponentParams(null, Float.class, 2);

        final UCTextField tfValuePriceCurrency = new UCTextField();
        tfValuePriceCurrency.setComponentParams(null, Float.class, 2);

        TableColumn column = columnModel.getColumn(9);
        column.setCellEditor(new UCTextFieldEditor(tfValuePrice));

        column = columnModel.getColumn(12);
        column.setCellEditor(new UCTextFieldEditor(tfValuePriceCurrency));
    }

    @Override
    public boolean isCellEditable(final int column) {
        if (SaleDocumentManager.isDocumentClosed(documentBase)) {
            return false;
        }

        if (SaleDocumentManager.isDocumentRefund(documentBase)) {
            if (SaleDocumentManager.isExportDocument(documentBase)) {
                return (column == 9 || column == 12);
            } else {
                return (column == 9);
            }
        } else {
            return false;
        }
    }

    @Override
    public void setValueAt(final int columnIndex, final Object sourceValue, final Object changeValue) {
        SaleDocumentItemView editableItem = (SaleDocumentItemView) sourceValue;
        if (columnIndex == 9) {
            double source = editableItem.getValuePrice();
            double change = 0;
            if (((String) changeValue).trim().equals("")) {
                change = 0.0;
            } else {
                change = Double.valueOf((String) changeValue);
            }
            if (change != source) {
                editableItem.setChanged(true);

                double price = editableItem.getValuePriceForAccounting_();
                if (price >= 0) {
                    editableItem.setValuePriceForAccounting(price * (-1));
                }
                callBack.onCallBack();
            }
            editableItem.setValuePrice(change);
        }

        if (columnIndex == 12) {
            double source = editableItem.getValuePriceCurrency();
            double change = 0;
            if (((String) changeValue).trim().equals("")) {
                change = 0.0;
            } else {
                change = Double.valueOf((String) changeValue);
            }

            if (change != source) {
                editableItem.setChanged(true);
                callBack.onCallBack();

                double price = editableItem.getValuePriceForAccounting_();
                if (price >= 0) {
                    editableItem.setValuePriceForAccounting(price * (-1));
                }
            }
            editableItem.setValuePriceCurrency(change);
        }
    }

    @Override
    public boolean onEndOfTable(final JTable table) {
        return false;
    }

    public void updateDocument(SaleDocumentBase document) {
        documentBase = document;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
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
