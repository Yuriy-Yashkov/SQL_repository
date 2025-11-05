package by.march8.ecs.application.modules.references.classifier.ui;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.ICustomCellEditor;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextFieldPanel;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.planning.model.ProductionEquipment;
import by.march8.ecs.application.modules.references.classifier.dao.ClassifierJDBC;
import by.march8.ecs.application.modules.references.classifier.model.CompositionCellRender;
import by.march8.ecs.application.modules.references.classifier.model.ConsumptionRateItem;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.ecs.framework.sdk.reference.Reference;
import by.march8.entities.classifier.ClassifierItem;
import by.march8.entities.classifier.ClassifierModelParams;
import by.march8.entities.classifier.CompositionMaterialView;
import by.march8.entities.classifier.EquipmentView;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andy 20.11.2018 - 7:44.
 */
@SuppressWarnings("all")
public class ProductionParameterMonitor extends JPanel implements ICustomCellEditor {

    private final int COLUMN_SELECTED = 0;
    private final int COLUMN_RATES = 2;
    private MainController controller;
    private ClassifierModelParams controlItem;
    private ArrayList<ConsumptionRateItem> data;
    private GridViewPort<ConsumptionRateItem> gridViewPort;
    private UCTextField tfRateValue;
    private JList sizeList;
    private ClassifierItem activeProduct;
    private ProductionEquipment activeEquipment;
    private List<CompositionMaterialView> materialList;
    private JTabbedPane tabPanel = new JTabbedPane();
    private UCTextFieldPanel<String> tfpEquipment;
    private UCTextField tfPerformance;
    private ClassifierJDBC db = new ClassifierJDBC();
    private JButton btnSetPerformance;


    public ProductionParameterMonitor(MainController mainController, ClassifierModelParams item) {
        super(new BorderLayout());
        controller = mainController;
        setPreferredSize(new Dimension(100, 200));
        controlItem = item;
        initPanel();
        initEvents();

        gridViewPort.updateViewPort();
    }

    private void initEvents() {

        sizeList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (sizeList.getModel().getSize() > 0) {
                    activeProduct = getSelectedClassifierItem(sizeList.getSelectedValue().toString());
                    if (activeProduct != null) {

                        matchingMaterialList(activeProduct.getId());
                        matchingEquipment(activeProduct.getId());

                    }
                }
            }
        });

        tfpEquipment.addButtonSelectActionListener(a -> {
            Reference ref = new Reference(controller, MarchReferencesType.EQUIPMENT,
                    MarchWindowType.PICKFRAME);

            final Object obj = ref.showPickFrame();
            if (obj != null) {
                EquipmentView item = (EquipmentView) obj;
                if (activeEquipment == null) {
                    ProductionEquipment newItem = new ProductionEquipment();
                    newItem.setEquipmentId(item.getId());
                    newItem.setProductId(activeProduct.getId());
                    db.insertEquipment(newItem);
                } else {
                    activeEquipment.setEquipmentId(item.getId());
                    db.updateEquipment(activeEquipment);
                }
                matchingEquipment(activeProduct.getId());
            }
        });

        btnSetPerformance.addActionListener(a -> {
            if (activeEquipment != null) {
                activeEquipment.setPerformance(Double.valueOf(tfPerformance.getText()));
                db.updateEquipment(activeEquipment);
            }
        });

    }


    private void initPanel() {

        tfRateValue = new UCTextField();
        tfRateValue.setComponentParams(null, Float.class, 2);

        gridViewPort = new GridViewPort<>(ConsumptionRateItem.class, false);
        data = gridViewPort.getDataModel();

        gridViewPort.setCustomCellEditor(this);
        gridViewPort.setIgnoreNotEditableCells(true);
        gridViewPort.getTable().setRowSelectionAllowed(true);

        gridViewPort.setCustomCellRender(new CompositionCellRender(), 1);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String val : prepareSizeList())
            listModel.addElement(val);

        sizeList = new JList(listModel);
        sizeList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        sizeList.setLayoutOrientation(JList.VERTICAL);
        sizeList.setVisibleRowCount(-1);

        JPanel pEquipment = new JPanel(new MigLayout());
        tfPerformance = new UCTextField();
        tfPerformance.setComponentParams(null, Double.class, 4);

        tfpEquipment = new UCTextFieldPanel<>();
        tfpEquipment.getEditor().setEditable(false);

        btnSetPerformance = new JButton("");
        btnSetPerformance.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/check_.png", "Установить производительность"));
        btnSetPerformance.setToolTipText("Установить производительность");

        if (listModel.size() > 0) {
            sizeList.setSelectedIndex(0);
            activeProduct = getSelectedClassifierItem(sizeList.getSelectedValue().toString());
            matchingEquipment(activeProduct.getId());
            tfpEquipment.getButtonSelect().setEnabled(true);
        } else {
            tfpEquipment.getButtonSelect().setEnabled(false);
        }

        JScrollPane spSizeScale = new JScrollPane(sizeList);
        spSizeScale.setPreferredSize(new Dimension(200, 80));

        pEquipment.add(new JLabel("Оборудование: "), "width 150:20:150");
        pEquipment.add(tfpEquipment, "width 250:20:250, height 20:20, span 2, wrap");

        JLabel lblPerformance = new JLabel("Производительность: ");

        pEquipment.add(lblPerformance, "width 150:20:150");
        pEquipment.add(tfPerformance, "width 90:20:90");
        pEquipment.add(btnSetPerformance, "width 20:20:20, height 20:20 , wrap");

        tabPanel.addTab("Нормы расхода", gridViewPort);
        tabPanel.addTab("Производство", pEquipment);

        tabPanel.setSelectedIndex(0);

        JPanel pSizeScale = new JPanel(new BorderLayout());
        pSizeScale.add(new JLabel("Размер"), BorderLayout.NORTH);
        pSizeScale.add(spSizeScale, BorderLayout.CENTER);
        add(pSizeScale, BorderLayout.WEST);

        this.add(tabPanel, BorderLayout.CENTER);
    }

    private List<String> prepareSizeList() {
        List<String> result = new ArrayList<>();
        if (controlItem != null) {
            for (ClassifierItem item : controlItem.getAssortmentList()) {
                if (item.getItemGrade() == 1) {
                    String s = item.getSize() + "-" + item.getGrowth();
                    result.add(s);
                }
            }
        }
        return result;
    }

    @Override
    public void initialCellEditor(TableColumnModel columnModel) {
        TableColumn column = columnModel.getColumn(COLUMN_RATES);
        column.setCellEditor(new UCTextFieldEditor(tfRateValue));
    }

    @Override
    public boolean isCellEditable(int column) {
        return (column == COLUMN_SELECTED || column == COLUMN_RATES);
    }

    @Override
    public void setValueAt(int columnIndex, Object sourceValue, Object changeValue) {
        ConsumptionRateItem editableItem = (ConsumptionRateItem) sourceValue;
        if (columnIndex == COLUMN_SELECTED) {
            editableItem.setSelected((Boolean) changeValue);
            if (editableItem.isSelected()) {
                if (editableItem.getRateId() < 1) {
                    db.insertRate(editableItem);
                }
            } else {
                db.deleteRate(editableItem);
            }
            matchingMaterialList(activeProduct.getId());
        }

        if (columnIndex == COLUMN_RATES) {
            editableItem.setRate(Double.valueOf((String) changeValue));
            if (editableItem.isSelected()) {
                db.updateRate(editableItem);
            }
        }
    }

    @Override
    public boolean onEndOfTable(JTable table) {
        return false;
    }

    public void updateMaterialList(List<CompositionMaterialView> list) {
        materialList = list;
        data.clear();
        if (materialList != null) {
            for (CompositionMaterialView item : materialList) {
                ConsumptionRateItem rateItem = new ConsumptionRateItem();
                rateItem.setId(item.getId());
                rateItem.setMaterialId(item.getId());
                rateItem.setMaterial(item.getMaterialName());
                data.add(rateItem);
            }

            int productId = -1;

            if (sizeList.getModel().getSize() > 0) {
                productId = activeProduct.getId();
            }
            matchingMaterialList(productId);
        }
    }

    private void matchingMaterialList(int productId) {
        Map<Integer, ConsumptionRateItem> map = db.getConsumptionRatesBySDCode(productId);
        if (map == null) {
            gridViewPort.updateViewPort();
            return;
        }

        for (ConsumptionRateItem item : data) {
            if (item != null) {
                item.setProductId(productId);
                ConsumptionRateItem item_ = map.get(item.getMaterialId());

                if (item_ != null) {
                    item.setRateId(item_.getRateId());
                    //item.setMaterialId(item_.getMaterialId());
                    item.setSelected(true);
                    item.setRate(item_.getRate());
                } else {
                    item.setSelected(false);
                    item.setRate(0.0000);
                    //item.setMaterialId(0);
                    item.setRateId(0);
                }
            }
        }
        gridViewPort.updateViewPort();
    }

    private void matchingEquipment(int id) {
        activeEquipment = db.getEquipmentBySDCode(id);
        if (activeEquipment != null) {
            tfpEquipment.getEditor().setText(activeEquipment.getEquipmentName());
            tfPerformance.setText(String.valueOf(activeEquipment.getPerformance()));
            btnSetPerformance.setEnabled(true);
        } else {
            tfpEquipment.getEditor().setText("");
            tfPerformance.setText(String.valueOf(0.0));
            btnSetPerformance.setEnabled(false);
        }
    }

    public void deleteComponents(CompositionMaterialView selectedItem) {
        db.deleteRatesForModel(selectedItem, controlItem);
    }

    public void applyComponentsForAll() {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                db.applyComponentsForAll(controlItem, materialList);
                matchingMaterialList(activeProduct.getId());
                return true;
            }
        }

        Task task = new Task("Сохранение компонентов ...");
        task.executeTask();
    }

    private ClassifierItem getSelectedClassifierItem(String sizeGrowthValue) {
        if (sizeGrowthValue != null) {
            String[] array = sizeGrowthValue.split("-");
            int size_;
            int growth_ = 0;

            size_ = Integer.valueOf(array[0]);

            if (array.length > 1) {
                growth_ = Integer.valueOf(array[1]);
            }

            for (ClassifierItem item : controlItem.getAssortmentList()) {
                if (item.getItemGrade() == 1) {
                    int size = item.getSize();
                    int growth = item.getGrowth();


                    if (size == size_ && growth == growth_) {
                        return item;
                    }
                }
            }
        }
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
