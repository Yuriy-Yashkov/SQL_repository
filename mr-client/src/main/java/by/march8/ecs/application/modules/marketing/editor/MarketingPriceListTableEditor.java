package by.march8.ecs.application.modules.marketing.editor;

import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.ICustomCellEditor;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.CallBack;
import by.march8.entities.marketing.MarketingPriceListItem;
import by.march8.entities.marketing.ViewMarketingPriceListDetailItem;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author Andy 03.11.2017.
 */
public class MarketingPriceListTableEditor implements ICustomCellEditor {

    private MarketingPriceListItem priceList;
    private GridViewPort gridViewPort;
    private CallBack callBack;


    public MarketingPriceListTableEditor(GridViewPort gridViewPort, MarketingPriceListItem document) {
        this.priceList = document;
        this.gridViewPort = gridViewPort;
    }

    @Override
    public void initialCellEditor(final TableColumnModel columnModel) {
        final UCTextField tfSuggestedPriceValue = new UCTextField();
        tfSuggestedPriceValue.setComponentParams(null, Float.class, 2);

        final UCTextField tfChangePercent = new UCTextField();
        tfChangePercent.setComponentParams(null, Float.class, 2);

        TableColumn column = columnModel.getColumn(7);
        column.setCellEditor(new UCTextFieldEditor(tfSuggestedPriceValue));

//        column = columnModel.getColumn(8);
//        column.setCellEditor(new UCTextFieldEditor(tfChangePercent));
    }

    @Override
    public boolean isCellEditable(final int column) {
        if (column == 7) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setValueAt(final int columnIndex, final Object sourceValue, final Object changeValue) {
        ViewMarketingPriceListDetailItem editableItem = (ViewMarketingPriceListDetailItem) sourceValue;
        if (columnIndex == 7) {
            double source = editableItem.getSuggestedPriceValue();
            double change = 0;
            if (((String) changeValue).trim().equals("")) {
                change = 0.0;
            } else {
                change = Double.valueOf((String) changeValue);
            }
            if (change != source) {
                editableItem.setChanged(true);
                editableItem.setSuggestedPriceValue(change);
                callBack.onCallBack();
            }
        }
/*
        if (columnIndex == 8) {
            double source = editableItem.getChangePercentValue();
            double change = 0;
            if (((String) changeValue).trim().equals("")) {
                change = 0.0;
            } else {
                change = Double.valueOf((String) changeValue);
            }
            if (change != source) {
                editableItem.setChanged(true);
                editableItem.setChangePercentValue(change);

                // *****************************************************
                // Необходимые расчеты параметров при изменении процента
                // *****************************************************

                editableItem.setSuggestedPriceValue(
                        RoundUtils.round(editableItem.getPrimeCostValue() * change / 100 + editableItem.getPrimeCostValue()
                        , RoundUtils.ROUND_XXX_XX));

                gridViewPort.updateViewPort();
                callBack.onCallBack();
            }
        }
        */
    }

    @Override
    public boolean onEndOfTable(final JTable table) {
        return false;
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
