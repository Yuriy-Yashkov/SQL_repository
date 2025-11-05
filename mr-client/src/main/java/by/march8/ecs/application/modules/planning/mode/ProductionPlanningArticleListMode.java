package by.march8.ecs.application.modules.planning.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.ICustomCellEditor;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.gomel.freedev.ucframework.ucswing.uicontrols.interfaces.UCTextFieldAdvanced;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.planning.manager.ProductPlanningManager;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.classifier.EquipmentItem;
import by.march8.entities.planning.ProductionPlanningArticleView;
import by.march8.entities.planning.ProductionPlanningDetailComponentView;
import by.march8.entities.planning.ProductionPlanningDetailView;
import by.march8.entities.planning.ProductionPlanningItem;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy 27.11.2018 - 11:08.
 */
public class ProductionPlanningArticleListMode extends AbstractFunctionalMode<ProductionPlanningArticleView> implements ICustomCellEditor {


    private final int COLUMN_PERCENT = 2;
    private final int COLUMN_AMOUNT = 3;
    private final int COLUMN_PERFORMANCE = 4;
    private final int COLUMN_EQUIPCOUNT = 5;
    private final int COLUMN_EQUIPMENT = 6;
    private ProductPlanningManager manager;
    private ProductionPlanningDetailView activeArticle;
    private List<ProductionPlanningArticleView> productList;
    private List<ProductionPlanningDetailComponentView> componentList;
    private UCToolBar toolBar;
    private GridViewPort<ProductionPlanningDetailComponentView> componentGrid;
    private JPanel pHeader;
    private JPanel pHeaderLeft;
    private JPanel pContentPane;
    private JPanel pCalculate;
    private UCTextFieldAdvanced tfTotalCount;
    private UCTextFieldAdvanced tfHourPerDay;
    private UCTextFieldAdvanced tfEquipmentCount;
    private UCTextFieldAdvanced tfPerformance;
    private UCTextField tfPercent;
    private UCTextField tfAmount;
    private UCTextField tfEquipCount;
    private UCTextField tfEquipPerformance;
    private JComboBox<EquipmentItem> cbEquipment;

    private ProductionPlanningArticleView gridFooter;


    public ProductionPlanningArticleListMode(MainController controller, ProductPlanningManager manager, ProductionPlanningDetailView item) {
        this.controller = controller;
        this.manager = manager;
        modeName = "Спецификация артикула " + item.getArticleNumber() + " в плане производства";
        activeArticle = item;

        frameViewPort = new FrameViewPort(this.controller, MarchWindowType.DIALOGSIMPLE);
        frameViewPort.getFrameControl().setTitleFrame(modeName);
        toolBar = frameViewPort.getFrameRegion().getToolBar();

        initializeComponent();
        initEvents();

        updateContent();

        if (productList.size() > 0) {
            gridViewPort.getTable().setRowSelectionInterval(0, 0);
            updateDetail(gridViewPort.getSelectedItem());
            calculateArticle(CalculateType.INITIAL);
        }

        gridViewPort.primaryInitialization();
        frameViewPort.getFrameControl().setFrameSize(new Dimension(1024, 700));
        frameViewPort.getFrameControl().showFrame();
    }

    public static double roundToDouble(double value, int scale) {
        System.out.println(value);
        BigDecimal decimal = new BigDecimal(String.valueOf(value));
        return decimal.setScale(10, BigDecimal.ROUND_HALF_UP).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static int roundToInt(double value, int scale) {
        BigDecimal decimal = new BigDecimal(String.valueOf(value));
        return decimal.setScale(10, BigDecimal.ROUND_HALF_UP).setScale(scale, BigDecimal.ROUND_HALF_UP).intValue();
    }

    private void initializeComponent() {

        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBar.setPreferredSize(new Dimension(0, 47));

        toolBar.setRight(RightEnum.WRITE);
        toolBar.getBtnReport().setVisible(false);

        toolBar.getBtnNewItem().setEnabled(true);
        toolBar.getBtnEditItem().setEnabled(true);
        toolBar.getBtnDeleteItem().setEnabled(true);

        gridViewPort = new GridViewPort<>(ProductionPlanningArticleView.class);
        componentGrid = new GridViewPort<>(ProductionPlanningDetailComponentView.class);

        productList = gridViewPort.getDataModel();
        componentList = componentGrid.getDataModel();

        frameViewPort.setGridViewPort(gridViewPort);

        pContentPane = new JPanel(new BorderLayout());
        pHeader = new JPanel(new BorderLayout());
        // pHeader.setPreferredSize(new Dimension(0, 250));

        pHeaderLeft = new JPanel(new MigLayout());
        pHeaderLeft.setPreferredSize(new Dimension(350, 120));

        JLabel lblItem = new JLabel("Наименование: ");
        JLabel lblArticle = new JLabel("Артикул: ");
        JLabel lblModel = new JLabel("Модель: ");
        JLabel lblAmount = new JLabel("Количество: ");

        JLabel lblItemValue = new JLabel(activeArticle.getItemName());
        JLabel lblArticleValue = new JLabel(activeArticle.getArticleNumber());
        JLabel lblModelValue = new JLabel(String.valueOf(activeArticle.getModelNumber()));
        JLabel lblAmountValue = new JLabel(String.valueOf(activeArticle.getAmount()));

        pHeaderLeft.add(lblItem);
        pHeaderLeft.add(lblItemValue, "wrap");

        pHeaderLeft.add(lblArticle);
        pHeaderLeft.add(lblArticleValue, "wrap");

        pHeaderLeft.add(lblModel);
        pHeaderLeft.add(lblModelValue, "wrap");

        pHeaderLeft.add(lblAmount);
        pHeaderLeft.add(lblAmountValue, "wrap");

        int fontSize = 14;
        Font font = new Font(toolBar.getFont().getName(), Font.BOLD, fontSize);

        lblItem.setFont(font);
        lblItemValue.setFont(font);
        lblArticle.setFont(font);
        lblArticleValue.setFont(font);
        lblModel.setFont(font);
        lblModelValue.setFont(font);
        lblAmountValue.setFont(font);
        lblAmount.setFont(font);

        pCalculate = new JPanel(new MigLayout());
        pCalculate.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pCalculate.setBorder(BorderFactory.createTitledBorder("Расчетные параметры"));

        tfTotalCount = new UCTextFieldAdvanced();
        tfHourPerDay = new UCTextFieldAdvanced();
        tfEquipmentCount = new UCTextFieldAdvanced();
        tfPerformance = new UCTextFieldAdvanced();

        ImageIcon icon = new ImageIcon(MainController.getRunPath() + "/Img/equal_16.png", "Расчет");

        pCalculate.add(new JLabel("Общее количество : "));
        pCalculate.add(tfTotalCount, "height 20:20, width 150:20:150,wrap");
        tfTotalCount.getTextField().setComponentParams(null, Integer.class, 5);
        tfTotalCount.getButton().setIcon(icon);

        pCalculate.add(new JLabel("Рабочие часы в день :"));
        pCalculate.add(tfHourPerDay, "height 20:20, width 150:20:150,wrap");
        tfHourPerDay.getTextField().setComponentParams(null, Integer.class, 2);
        tfHourPerDay.getButton().setIcon(icon);

        pCalculate.add(new JLabel("Количество машин :"));
        pCalculate.add(tfEquipmentCount, "height 20:20, width 150:20:150,wrap");
        tfEquipmentCount.getTextField().setComponentParams(null, Double.class, 2);
        tfEquipmentCount.getButton().setIcon(icon);

        pCalculate.add(new JLabel("Производительность оборудования :"));
        pCalculate.add(tfPerformance, "height 20:20, width 150:20:150,wrap");
        tfPerformance.getTextField().setComponentParams(null, Double.class, 4);
        tfPerformance.getButton().setIcon(icon);

        pHeader.add(pHeaderLeft, BorderLayout.WEST);
        pHeader.add(pCalculate, BorderLayout.CENTER);

        pContentPane.add(pHeader, BorderLayout.NORTH);

        final JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, gridViewPort, componentGrid);
        splitPanel.setResizeWeight(0.5);
        splitPanel.setOneTouchExpandable(true);
        splitPanel.setContinuousLayout(true);
        pContentPane.add(splitPanel, BorderLayout.CENTER);

        frameViewPort.getFrameRegion().getCenterContentPanel().add(pContentPane, BorderLayout.CENTER);


        gridFooter = new ProductionPlanningArticleView();
        gridFooter.setAmount(0);
        gridFooter.setPercent(0);

        gridViewPort.initializeFooterBar(gridFooter);

        tfPercent = new UCTextField();
        tfAmount = new UCTextField();
        tfEquipCount = new UCTextField();
        tfEquipPerformance = new UCTextField();
        cbEquipment = new JComboBox<>();


        tfPercent.setComponentParams(null, Double.class, 2);
        tfAmount.setComponentParams(null, Integer.class, 5);
        tfEquipCount.setComponentParams(null, Double.class, 2);
        tfEquipPerformance.setComponentParams(null, Double.class, 4);


        tfTotalCount.setValue(0);
        tfHourPerDay.setValue(8);
        tfEquipmentCount.setValue(0d);
        tfPerformance.setValue(0d);

        gridViewPort.getTable().putClientProperty("terminateEditOnFocusLost", true);
        gridViewPort.setCustomCellEditor(this);
        gridViewPort.setIgnoreNotEditableCells(true);
        gridViewPort.getTable().setRowSelectionAllowed(true);

        // Зарузка справочника оборудования
        manager.loadEquipments(cbEquipment);

    }

    private void initEvents() {
        toolBar.registerEvents(this);

        gridViewPort.setTableEventHandler(new TableEventAdapter<ProductionPlanningArticleView>() {
            @Override
            public void onClick(int rowIndex, int columnIndex, ProductionPlanningArticleView item) {
                updateDetail(item);
            }

            @Override
            public void onSelectChanged(int rowIndex, ProductionPlanningArticleView item) {

            }
        });

        frameViewPort.getButtonControl().getOkButton().addActionListener(a -> {
            if (calculateArticle(CalculateType.ALL_COMPONENTS)) {

                class Task extends BackgroundTask {
                    public Task(final String messageText) {
                        super(messageText);
                    }

                    @Override
                    protected Boolean doInBackground() throws Exception {
                        if (saveArticle()) {
                            frameViewPort.getFrameControl().closeFrame();
                        }
                        return true;
                    }
                }

                Task task = new Task("Сохранение изменений...");
                task.executeTask();
            }
        });

        tfTotalCount.getButton().addActionListener(a -> {
            calculateArticle(CalculateType.TOTAL_AMOUNT);
        });

        tfTotalCount.getTextField().addActionListener(a -> {
            calculateArticle(CalculateType.TOTAL_AMOUNT);
        });
    }

    private boolean saveArticle() {
        // Обходим артикул
        for (ProductionPlanningArticleView item : productList) {
            // ПОлучаем артикул из базы
            ProductionPlanningItem entity = manager.getProductionPlanningItemById(item.getId());
            if (entity != null) {
                ProductionPlanningItem product = manager.prepareProductionPlanningItem(entity, item);
                if (product != null) {
                    if (!manager.saveProductionPlanningItem(product)) {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean calculateArticle(CalculateType type) {

        gridFooter.setAmount(0);
        gridFooter.setPercent(0);

        if (productList.isEmpty()) {
            return false;
        }

        // Проходим всю коллекцию росто-размеров и считаем
        switch (type) {
            case TOTAL_AMOUNT: {

                double percent = roundToDouble((double) 100 / productList.size(), 2);
                int amount = roundToInt(Integer.valueOf(tfTotalCount.getTextField().getText()) / productList.size(), 0);

                for (ProductionPlanningArticleView item : productList) {
                    item.setPercent(percent);
                    item.setAmount(amount);
                    item.setChanged(true);
                }
                break;
            }

            case PERCENT: {
                int amount = Integer.valueOf(tfTotalCount.getTextField().getText());
                ProductionPlanningArticleView item = gridViewPort.getSelectedItem();
                item.setAmount(roundToInt(amount * item.getPercent() / 100, 0));
                item.setChanged(true);
                break;
            }

            case AMOUNT: {
                int amount = Integer.valueOf(tfTotalCount.getTextField().getText());
                ProductionPlanningArticleView item = gridViewPort.getSelectedItem();
                double percent = roundToDouble((double) item.getAmount() * 100 / amount, 2);
                System.out.println(percent);
                item.setPercent(percent);
                item.setChanged(true);
                break;
            }

            case ALL_COMPONENTS: {
                for (ProductionPlanningArticleView item : productList) {
                    if (item.getComponentList() == null) {
                        item.setComponentList(loadComponentsForItem(item));
                    }

                    for (ProductionPlanningDetailComponentView component : item.getComponentList()) {
                        component.setUseValue(item.getAmount() * component.getRate());
                    }

                    gridFooter.setAmount(gridFooter.getAmount() + item.getAmount());
                    gridFooter.setPercent(gridFooter.getPercent() + item.getPercent());
                }

                componentGrid.updateViewPort();
                gridViewPort.updateViewPort();
                return true;
            }

            case INITIAL: {
                int amount = 0;

                for (ProductionPlanningArticleView item : productList) {
                    amount += item.getAmount();
                }
                tfTotalCount.setValue(amount);
                break;
            }
        }

        for (ProductionPlanningArticleView item : productList) {
            gridFooter.setAmount(gridFooter.getAmount() + item.getAmount());
            gridFooter.setPercent(gridFooter.getPercent() + item.getPercent());
        }


        ProductionPlanningArticleView item_ = gridViewPort.getSelectedItem();
        if (item_ != null) {
            for (ProductionPlanningDetailComponentView item : item_.getComponentList()) {
                item.setUseValue(item_.getAmount() * item.getRate());
            }
        }

        componentGrid.updateViewPort();
        gridViewPort.updateViewPort();
        return false;
    }

    private void updateDetail(ProductionPlanningArticleView item) {
        if (item.getComponentList() == null) {
            item.setComponentList(loadComponentsForItem(item));
        }

        componentList.clear();
        componentList.addAll(item.getComponentList());
        componentGrid.updateViewPort();
    }

    private List<ProductionPlanningDetailComponentView> loadComponentsForItem(ProductionPlanningArticleView item) {
        List<ProductionPlanningDetailComponentView> result = null;
        DaoFactory<ProductionPlanningDetailComponentView> factory = DaoFactory.getInstance();
        IGenericDao<ProductionPlanningDetailComponentView> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("itemId", item.getId()));
        try {
            result = dao.getEntityListByNamedQuery(ProductionPlanningDetailComponentView.class,
                    "ProductionPlanningDetailComponentView.findByDetailItemId", criteria);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void updateContent() {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                DaoFactory<ProductionPlanningArticleView> factory = DaoFactory.getInstance();
                IGenericDao<ProductionPlanningArticleView> dao = factory.getGenericDao();
                List<QueryProperty> criteria = new ArrayList<>();
                criteria.add(new QueryProperty("documentId", activeArticle.getDocumentId()));
                criteria.add(new QueryProperty("articleId", activeArticle.getId()));
                List<ProductionPlanningArticleView> list;
                productList.clear();
                try {
                    list = dao.getEntityListByNamedQuery(ProductionPlanningArticleView.class, "ProductionPlanningArticleView.findByDocumentIdAndArticleId", criteria);
                    productList.addAll(list);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                gridViewPort.updateViewPort();
                return true;
            }
        }

        Task task = new Task("Получение спецификации артикула...");
        task.executeTask();
    }


    @Override
    public void addRecord() {

    }

    @Override
    public void editRecord() {

    }

    @Override
    public void deleteRecord() {

    }

    @Override
    public void setValueAt(int columnIndex, Object sourceValue, Object changeValue) {
        ProductionPlanningArticleView editableItem = (ProductionPlanningArticleView) sourceValue;

        if (columnIndex == COLUMN_PERCENT) {
            Double value = Double.valueOf((String) changeValue);
            if (!value.equals(editableItem.getPercent())) {
                editableItem.setPercent(value);
                editableItem.setChanged(true);

                calculateArticle(CalculateType.PERCENT);
            }
        }

        if (columnIndex == COLUMN_AMOUNT) {
            Integer value = Integer.valueOf((String) changeValue);
            if (!value.equals(editableItem.getAmount())) {
                editableItem.setAmount(value);
                editableItem.setChanged(true);
                calculateArticle(CalculateType.AMOUNT);
            }
        }

        if (columnIndex == COLUMN_EQUIPCOUNT) {
            Double value = Double.valueOf((String) changeValue);
            if (!value.equals(editableItem.getEquipmentCount())) {
                editableItem.setEquipmentCount(value);
                editableItem.setChanged(true);
            }
        }

        if (columnIndex == COLUMN_PERFORMANCE) {
            Double value = Double.valueOf((String) changeValue);
            if (!value.equals(editableItem.getPerformance())) {
                editableItem.setPerformance(value);
                editableItem.setChanged(true);
            }
        }

        if (columnIndex == COLUMN_EQUIPMENT) {
            EquipmentItem item = (EquipmentItem) cbEquipment.getSelectedItem();
            if (item != null) {
                if (editableItem.getEquipmentId() != item.getId()) {
                    editableItem.setEquipmentId(item.getId());
                    editableItem.setEquipmentName(item.getName());
                    editableItem.setEquipmentCount(item.getAmount());
                }
            }
        }
    }


    @Override
    public void initialCellEditor(TableColumnModel columnModel) {
        TableColumn column = columnModel.getColumn(COLUMN_PERCENT);
        column.setCellEditor(new UCTextFieldEditor(tfPercent));

        column = columnModel.getColumn(COLUMN_AMOUNT);
        column.setCellEditor(new UCTextFieldEditor(tfAmount));

        column = columnModel.getColumn(COLUMN_EQUIPCOUNT);
        column.setCellEditor(new UCTextFieldEditor(tfEquipCount));

        column = columnModel.getColumn(COLUMN_PERFORMANCE);
        column.setCellEditor(new UCTextFieldEditor(tfEquipPerformance));

        column = columnModel.getColumn(COLUMN_EQUIPMENT);
        column.setCellEditor(new UCTextFieldEditor(cbEquipment));
    }

    @Override
    public boolean isCellEditable(int column) {
        return (column == COLUMN_AMOUNT
                || column == COLUMN_EQUIPCOUNT
                || column == COLUMN_PERCENT
                || column == COLUMN_PERFORMANCE
                || column == COLUMN_EQUIPMENT);
    }

    @Override
    public boolean onEndOfTable(JTable table) {
        return false;
    }

    private enum CalculateType {
        TOTAL_AMOUNT, AMOUNT, PERCENT, ALL_COMPONENTS, INITIAL;
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

        public UCTextFieldEditor(JComboBox component) {
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
