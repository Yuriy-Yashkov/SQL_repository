package by.march8.ecs.application.modules.warehouse.external.shipping.editor;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.ICustomCellEditor;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDaoGUI;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.api.BaseEntity;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.classifier.enums.ClassifierType;
import by.march8.ecs.application.modules.references.classifier.mode.ClassifierPickMode;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentManager;
import by.march8.entities.classifier.ClassifierItem;
import by.march8.entities.warehouse.SaleDocument;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentItemBase;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Andy 11.03.2016.
 */
public class SaleDocumentItemsTableEditor extends EditingPane implements ICustomCellEditor {

    private GridViewPort gridViewPort;
    private SaleDocumentBase document;
    private SaleDocument source;
    private ArrayList<Object> data;
    private MainController controller;
    private UCToolBar toolBar;

    private UCTextField tfArticleNumber;
    private UCTextField tfCostValue;
    private UCTextField tfWeightValue;
    private UCTextField tfColorValue;
    private UCTextField tfGradeValue;

    public SaleDocumentItemsTableEditor(final FrameViewPort frameViewPort, SaleDocumentBase documentBase) {
        controller = frameViewPort.getController();
        setPreferredSize(new Dimension(1000, 500));

        document = documentBase;

        toolBar = new UCToolBar();
        gridViewPort = new GridViewPort(SaleDocumentItemBase.class);
        data = gridViewPort.getDataModel();
        gridViewPort.getTable().setRowHeight(22);
        setLayout(new BorderLayout());

        add(toolBar, BorderLayout.NORTH);
        add(gridViewPort, BorderLayout.CENTER);

        init();

        gridViewPort.setCustomCellEditor(this);
        gridViewPort.setIgnoreNotEditableCells(true);
        gridViewPort.getTable().setRowSelectionAllowed(true);

        toolBar.getBtnNewItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                addRecord();
            }
        });
        toolBar.getBtnDeleteItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                deleteRecord();
            }
        });
        toolBar.getBtnEditItem().setEnabled(false);
    }

    private void deleteRecord() {
        if (gridViewPort.getSelectedItem() == null) {
            return;
        }

        if (((BaseEntity) gridViewPort.getSelectedItem()).getId() != 0) {
            return;
        }

        final int answer = Dialogs.showDeleteDialog("Полотно");
        if (answer == 0) {
            try {
                //final BaseEntity baseEntity = (BaseEntity) gridViewPort.getSelectedItem();
                data.remove(gridViewPort.getSelectedRowIndex());
            } catch (Exception e) {
                MainController.exception(e, "Ошибка удаления записи");
            }
            gridViewPort.updateViewPort();
        }
    }

    private void addRecord() {
        onEndOfTable(gridViewPort.getTable());
    }


    private void updateContent() {
        DaoFactory<SaleDocumentItemBase> factory = DaoFactory.getInstance();
        IGenericDaoGUI<SaleDocumentItemBase> dao = factory.getGenericDao();
        java.util.List<QueryProperty> criteria = new ArrayList<QueryProperty>();
        criteria.add(new QueryProperty("document", document.getId()));
        List<SaleDocumentItemBase> list = null;
        data.clear();
        try {
            list = dao.getEntityListByNamedQueryGUI(SaleDocumentItemBase.class, "SaleDocumentItemBase.findByDocumentId", criteria);
            data.addAll(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        gridViewPort.updateViewPort();
        updateControls();
    }

    private void updateControls() {
        if (document != null) {
            final TableColumnModel columnModel = gridViewPort.getTable().getColumnModel();
            if (SaleDocumentManager.isExportDocument(document)) {
                columnModel.getColumn(4).setMaxWidth(100);
                columnModel.getColumn(4).setMinWidth(100);
                columnModel.getColumn(4).setPreferredWidth(100);

                columnModel.getColumn(3).setMaxWidth(0);
                columnModel.getColumn(3).setMinWidth(0);
                columnModel.getColumn(3).setPreferredWidth(0);
            } else {
                columnModel.getColumn(4).setMaxWidth(0);
                columnModel.getColumn(4).setMinWidth(0);
                columnModel.getColumn(4).setPreferredWidth(0);

                columnModel.getColumn(3).setMaxWidth(100);
                columnModel.getColumn(3).setMinWidth(100);
                columnModel.getColumn(3).setPreferredWidth(100);
            }

            if (data.size() < 1) {
                //toolBar.getBtnNewItem().setEnabled(true);
                toolBar.getBtnDeleteItem().setEnabled(false);
            } else {
                //toolBar.getBtnNewItem().setEnabled(false);
                toolBar.getBtnDeleteItem().setEnabled(true);
            }
            toolBar.getBtnEditItem().setEnabled(false);
        }
    }


    @Override
    public Object getSourceEntity() {
        return data;
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object != null) {
            document = (SaleDocumentBase) object;
            updateContent();
            updateControls();
        }
    }

    @Override
    public boolean verificationData() {
        for (Object o : data) {
            SaleDocumentItemBase item = (SaleDocumentItemBase) o;
            if (item != null) {
                if (item.getItem() == null) {
                    JOptionPane.showMessageDialog(null,
                            "В одной или нескольких позициях не указано наименование материала", "Ошибка!!!",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if (item.getItemColor() == null) {
                    JOptionPane.showMessageDialog(null,
                            "В одной или нескольких позициях не указан цвет материала", "Ошибка!!!",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Необъяснимая ошибка, обратитесь к программисту", "Ошибка!!!",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    private void init() {
        tfArticleNumber = new UCTextField();
        tfArticleNumber.setComponentParams(null, Integer.class, 9);

        tfGradeValue = new UCTextField();
        tfGradeValue.setComponentParams(null, Integer.class, 9);

        tfCostValue = new UCTextField();
        tfCostValue.setComponentParams(null, Float.class, 2);

        tfWeightValue = new UCTextField();
        tfWeightValue.setComponentParams(null, Float.class, 3);

        tfColorValue = new UCTextField();
        tfColorValue.setComponentParams(null, String.class, 0);


        gridViewPort.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onSelectChanged(final int rowIndex, final Object object) {
                SaleDocumentItemBase selectedItem = (SaleDocumentItemBase) object;
                if (selectedItem != null) {
                    try {
                        if (selectedItem.getId() > 0) {
                            toolBar.getBtnDeleteItem().setEnabled(false);
                        } else {
                            toolBar.getBtnDeleteItem().setEnabled(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    public void initialCellEditor(final TableColumnModel columnModel) {
        TableColumn column = columnModel.getColumn(1);
        column.setCellEditor(new UCTextFieldEditor(tfArticleNumber));

        column = columnModel.getColumn(2);
        column.setCellEditor(new UCTextFieldEditor(tfGradeValue));

        if (SaleDocumentManager.isExportDocument(document)) {
            column = columnModel.getColumn(4);
            column.setCellEditor(new UCTextFieldEditor(tfCostValue));
        } else {
            column = columnModel.getColumn(3);
            column.setCellEditor(new UCTextFieldEditor(tfCostValue));
        }

        column = columnModel.getColumn(5);
        column.setCellEditor(new UCTextFieldEditor(tfWeightValue));

        column = columnModel.getColumn(6);
        column.setCellEditor(new UCTextFieldEditor(tfColorValue));
    }

    @Override
    public boolean isCellEditable(final int column) {
        if (SaleDocumentManager.isExportDocument(document)) {
            return (column != 0 && column != 3);
        } else {
            return (column != 0 && column != 4);
        }
    }

    @Override
    public void setValueAt(final int columnIndex, final Object sourceValue, final Object changeValue) {
        SaleDocumentItemBase editableItem = (SaleDocumentItemBase) sourceValue;

        //Шифр артикула
        //TODO ЭТОТ КОСТЫЛЬ УБРАТЬ
        if (columnIndex == 1) {
            int rowIndex = gridViewPort.getTable().getSelectedRow();
            int article = Integer.valueOf((String) changeValue);
            if (editableItem.getItem() != null) {
                if (editableItem.getItem().getModel() != null) {
                    if (editableItem.getItem().getModel().getArticleCode() != article) {
                        ClassifierItem item = getProductItemByArticleNumber(article);
                        // Тут проверки, если артикул не был изменен
                        if (item != null) {
                            editableItem.setItem(item);
                            gridViewPort.updateViewPort();
                        } else {
                            System.err.println("Изделие не найдено");
                        }
                    }
                } else {
                    ClassifierItem item = getProductItemByArticleNumber(article);
                    // Тут проверки, если артикул не был изменен
                    if (item != null) {
                        editableItem.setItem(item);
                        gridViewPort.updateViewPort();
                    } else {
                        System.err.println("Изделие не найдено");
                    }
                }
            } else {
                ClassifierItem item = getProductItemByArticleNumber(article);
                // Тут проверки, если артикул не был изменен
                if (item != null) {
                    editableItem.setItem(item);
                    gridViewPort.updateViewPort();
                } else {
                    System.err.println("Изделие не найдено");
                }
            }
        }

        //Сорт
        if (columnIndex == 2) {
            editableItem.setItemGrade(Integer.valueOf((String) changeValue));
        }

        // Цена
        if (SaleDocumentManager.isExportDocument(document)) {
            //Экспортная цена
            if (columnIndex == 4) {
                //editableItem.setCost(Double.valueOf((String) changeValue));
                editableItem.setValuePriceCurrency(Double.valueOf((String) changeValue));
            }
        } else {
            // //Беларусская цена
            if (columnIndex == 3) {
                //editableItem.setCost(Double.valueOf((String) changeValue));
                editableItem.setValuePriceForAccounting(Double.valueOf((String) changeValue));
                editableItem.setValuePriceCurrency(0.0);
            }
        }

        //Вес
        if (columnIndex == 5) {
            //editableItem.setWeight(Double.valueOf((String) changeValue));
            double weightFloat = Math.round(Double.valueOf((String) changeValue) * 100);
            int weightInteger = (int) weightFloat;
            editableItem.setAmountInPack(weightInteger);
        }

        //Цвет
        if (columnIndex == 6) {
            editableItem.setItemColor((String) changeValue);
        }
    }

    @Override
    public boolean onEndOfTable(final JTable table) {
        SaleDocumentItemBase newItem = new SaleDocumentItemBase();

        newItem.setSystemUser("SKLAD6 # proiz");
        newItem.setSystemDate(new Date());

        newItem.setItemType(3);
        newItem.setItemScanCode(0L);
        newItem.setPartNumber(1);
        newItem.setItemEanCode("0");
        newItem.setItemPriceList("ПР01");
        newItem.setAmount(1);
        newItem.setItemGrade(1);
        newItem.setItemGrowz(0);
        newItem.setItemSize(0);

        newItem.setDocumentId(document.getId());

        data.add(newItem);
        gridViewPort.updateViewPort();
        return true;
    }

    private ClassifierItem getProductItemByArticleNumber(int articleNumber) {

        ClassifierPickMode pickMode = new ClassifierPickMode(controller, ClassifierType.MATERIAL);
        Object selectedItem = pickMode.showSelectModal(articleNumber);
        ClassifierItem classifierItem;

        if (selectedItem != null) {
            return (ClassifierItem) selectedItem;
        }

        /*        // Поиск артикула в классификаторе
        DaoFactory<ClassifierModelView> factory = DaoFactory.getInstance();
        IGenericDaoGUI<ClassifierModelView> dao = factory.getGenericDaoGUI();

        java.util.List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("article", articleNumber));
        List<ClassifierModelView> list = null;

        try {
            list = dao.getEntityListByNamedQueryGUI(ClassifierModelView.class, "ClassifierModelView.findByArticle", criteria);
            // Если что-то нашли
            if (list != null) {
                if (list.size() > 0) {
                    for(ClassifierModelView t: list){
                        System.out.println("Артикул найден " + t.getCategory());
                    }
                    //Ищем первое попавшееся изделие для полотна
                    DaoFactory<ClassifierItem> factoryModel = DaoFactory.getInstance();
                    IGenericDaoGUI<ClassifierItem> daoModel = factoryModel.getGenericDaoGUI();

                    List<QueryProperty> criteriaItem = new ArrayList<>();
                    criteriaItem.add(new QueryProperty("article", list.get(0).getId()));
                    System.out.println("Поиск изделия для артикула с кодом "+list.get(0).getId());
                    try {
                        List<ClassifierItem> itemList = daoModel.getEntityListByNamedQueryGUI(ClassifierItem.class, "ClassifierItem.findByArticle", criteriaItem);
                        if (itemList != null) {
                            if (itemList.size() > 0) {
                                for(ClassifierItem t: itemList){
                                    System.out.println("Изделие найдено " + t.getId());
                                }

                                return itemList.get(0);
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;*/
        return null;
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
