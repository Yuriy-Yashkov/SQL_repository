package by.march8.ecs.application.modules.marketing.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces.IActiveFrameRegion;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.economists.mode.WarehousePriceListPreviewMode;
import by.march8.ecs.application.modules.marketing.editor.MarketingPriceListEditor;
import by.march8.ecs.application.modules.marketing.model.MarketingPriceListCellRenderer;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.classifier.RemainPriceListSearchItem;
import by.march8.entities.marketing.MarketingPriceListItem;
import by.march8.entities.marketing.ViewMarketingPriceListDetailItem;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Andy 11.10.2017.
 */
public class MarketingPriceListJournalMode extends AbstractFunctionalMode {

    private RightEnum right;


    private ArrayList<Object> dataPriceList;
    private ArrayList<Object> dataPriceListDetail;

    private GridViewPort gridPriceListDetail;

    private JButton btnImport = new JButton();
    private JButton btnSearch = new JButton();
    private JButton btnDetail = new JButton();

    private UCTextField tfDocumentNumber = new UCTextField();
    private EditingPane editingPane;

    private JLabel lblOrderInformation = new JLabel("Документ приказа не определен");

    public MarketingPriceListJournalMode(MainController mainController, RightEnum rightEnum) {
        controller = mainController;
        modeName = "Журнал документов уценки: Отдел маркетинга";
        frameViewPort = new FrameViewPort(mainController, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);
        right = rightEnum;
        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.registerEvents(this);
        toolBar.setRight(right);
        toolBar.getBtnReport().setVisible(false);

        toolBar.getBtnEditItem().setVisible(true);
        toolBar.getBtnNewItem().setVisible(true);
        toolBar.getBtnDeleteItem().setVisible(false);

        //toolBar.add(btnImport);

        btnImport.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/import_24.png", "Импорт документа"));
        btnImport.setToolTipText("Импортировать прейскурант");

        btnSearch.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/find24.png", "Поиск изделия"));
        btnSearch.setToolTipText("Поиск изделия в прейскурантах");

        // Спецификация накладной
        btnDetail = new JButton();
        btnDetail.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/view_detail.png", "Спецификация документа"));
        btnDetail.setToolTipText("Спецификация документа");
        toolBar.add(btnDetail);

        // Комбик выбора типа документа
        JPanel pSearchPanel = new JPanel(null);
        JLabel lblDocumentNumber = new JLabel("Наименование :");
        lblDocumentNumber.setBounds(105, 6, 100, 20);
        tfDocumentNumber.setBounds(210, 6, 135, 20);

        pSearchPanel.add(lblDocumentNumber);
        pSearchPanel.add(tfDocumentNumber);

        pSearchPanel.setPreferredSize(new Dimension(250, 28));
        pSearchPanel.setOpaque(false);
        //toolBar.add(pSearchPanel);
        //toolBar.add(btnSearch);
        JPanel pLeftPanel = new JPanel(new BorderLayout());
        JPanel pLeftFooter = new JPanel(new BorderLayout());
        pLeftFooter.setPreferredSize(new Dimension(250, 30));
        pLeftFooter.add(lblOrderInformation, BorderLayout.CENTER);
        // lblOrderInformation.setForeground(Color.BLUE);


        toolBar.add(new Box(BoxLayout.X_AXIS));

        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBar.setPreferredSize(new Dimension(0, 47));

        //toolBar.getBtnViewItem().setIcon(new ImageIcon(MainController.getRunPath() + "/Img/edit24.png", "Редактировать размер уценки"));
        toolBar.getBtnViewItem().addActionListener(a -> {
            //updateContent();
        });

        if (right == RightEnum.WRITE) {
            toolBar.getBtnEditItem().setEnabled(true);
            btnImport.setEnabled(true);
        } else {
            toolBar.getBtnEditItem().setEnabled(false);
            btnImport.setEnabled(false);
        }

        gridViewPort = new GridViewPort(MarketingPriceListItem.class, false);
        gridViewPort.setPreferredSize(new Dimension(10, 100));
        gridViewPort.setCustomCellRender(new MarketingPriceListCellRenderer());
        gridViewPort.getTable().setRowSelectionAllowed(true);

        dataPriceList = gridViewPort.getDataModel();

        pLeftPanel.add(gridViewPort, BorderLayout.CENTER);
        pLeftPanel.add(pLeftFooter, BorderLayout.SOUTH);

        gridPriceListDetail = new GridViewPort(ViewMarketingPriceListDetailItem.class, false);
        dataPriceListDetail = gridPriceListDetail.getDataModel();
        gridPriceListDetail.getTable().setRowSelectionAllowed(true);

        final JSplitPane splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pLeftPanel, gridPriceListDetail);
        splitPanel.setResizeWeight(0.5);
        splitPanel.setOneTouchExpandable(true);
        splitPanel.setContinuousLayout(true);

        IActiveFrameRegion frameRegion = frameViewPort.getFrameRegion();
        frameRegion.getCenterContentPanel().add(splitPanel, BorderLayout.CENTER);

        editingPane = new MarketingPriceListEditor(frameViewPort);
        editingPane.setRight(RightEnum.WRITE);
        //editingPane.setSourceEntityClass(SaleDocumentView.class);
        initEvents();

//        gridViewPort.initialFooter();

/*        Container footer = gridViewPort.getFooterPanel();
        footer.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel label = gridViewPort.getFooterTextComponent();
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 12));*/

        //entityViewPort = new EntityViewPort(frameViewPort, editingPane);
        updateContent();
        gridViewPort.primaryInitialization();
        gridPriceListDetail.primaryInitialization();

        frameViewPort.getFrameControl().showFrame();
    }

    private void initEvents() {
        // двойной клик на строке
        gridViewPort.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onDoubleClick(int rowIndex, int columnIndex, Object object) {
                MarketingPriceListItem selectedItem = (MarketingPriceListItem) object;
                if (selectedItem != null) {
                    MarketingPriceListJournalDetailMode detailMode = new MarketingPriceListJournalDetailMode(controller, selectedItem);
                    if (detailMode.showMode()) {
                        updateContent();
                    }
                }
            }

            @Override
            public void onSelectChanged(final int rowIndex, final Object object) {
                MarketingPriceListItem selectedItem = (MarketingPriceListItem) object;
                if (selectedItem != null) {
                    updateDetail(selectedItem);
                    updateOrderInformation(selectedItem);
                }
            }
        });

        btnSearch.addActionListener(a -> {
            doSearch();
        });

        tfDocumentNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doSearch();
                }
            }
        });

        btnDetail.addActionListener(a -> {
            MarketingPriceListItem selectedItem = (MarketingPriceListItem) gridViewPort.getSelectedItem();
            if (selectedItem != null) {
                MarketingPriceListJournalDetailMode detailMode = new MarketingPriceListJournalDetailMode(controller, selectedItem);
                if (detailMode.showMode()) {
                    updateContent();
                }
            }
        });
    }

    @Override
    public void updateContent() {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                DaoFactory<MarketingPriceListItem> factory = DaoFactory.getInstance();
                IGenericDao<MarketingPriceListItem> dao = factory.getGenericDao();
                try {
                    dataPriceList.clear();
                    dataPriceList.addAll(dao.getEntityListByNamedQuery(MarketingPriceListItem.class, "MarketingPriceListItem.findAll", null));
                    frameViewPort.updateContent();
                    gridViewPort.updateViewPort();
                    return true;
                } catch (final Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        Task task = new Task("Получение данных...");
        task.executeTask();
    }

    private void updateDetail(MarketingPriceListItem item) {
        DaoFactory<ViewMarketingPriceListDetailItem> factory = DaoFactory.getInstance();
        IGenericDao<ViewMarketingPriceListDetailItem> dao = factory.getGenericDao();

        java.util.List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("priceList", item.getId()));
        try {
            dataPriceListDetail.clear();
            dataPriceListDetail.addAll(dao.getEntityListByNamedQuery(ViewMarketingPriceListDetailItem.class, "ViewMarketingPriceListDetailItem.findByPriceList", criteria));
        } catch (final Exception e) {
            e.printStackTrace();
        }

        final TableColumnModel columnModel = gridPriceListDetail.getTable().getColumnModel();


        if (item.getDocumentType() == 1) {
            for (int c = 4; c < 8; c++) {
                columnModel.getColumn(c).setMaxWidth(0);
                columnModel.getColumn(c).setMinWidth(0);
                columnModel.getColumn(c).setPreferredWidth(0);
            }
        } else {
            for (int c = 4; c < 8; c++) {
                columnModel.getColumn(c).setMaxWidth(80);
                columnModel.getColumn(c).setMinWidth(80);
                columnModel.getColumn(c).setPreferredWidth(80);
            }
        }

        //frameViewPort.updateContent();
        gridPriceListDetail.updateViewPort();
    }

    private void doSearch() {
        if (tfDocumentNumber.getText().trim().equals("")) {
            return;
        }

        if (tfDocumentNumber.getText().length() < 3) {
            return;
        }


        // Если введено число, то ищем в номере модели
        int searchValue = 0;
        boolean isModel = true;
        try {
            searchValue = Integer.valueOf(tfDocumentNumber.getText().trim());
        } catch (Exception e) {
            isModel = false;
            System.out.println();
        }

        ArrayList<Object> resultList = new ArrayList<Object>();
        resultList.clear();

        final boolean finalIsModel = isModel;
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                DaoFactory<RemainPriceListSearchItem> factory = DaoFactory.getInstance();
                IGenericDao<RemainPriceListSearchItem> dao = factory.getGenericDao();

                java.util.List<QueryProperty> criteria = new ArrayList<>();
                if (finalIsModel) {
                    criteria.add(new QueryProperty("model", "%" + tfDocumentNumber.getText().trim() + "%"));
                } else {
                    criteria.add(new QueryProperty("article", "%" + tfDocumentNumber.getText().trim().toLowerCase() + "%"));
                }

                try {
                    resultList.clear();
                    if (finalIsModel) {
                        resultList.addAll(dao.getEntityListByNamedQuery(RemainPriceListSearchItem.class, "RemainPriceListSearchItem.findByModelNumber", criteria));
                    } else {
                        resultList.addAll(dao.getEntityListByNamedQuery(RemainPriceListSearchItem.class, "RemainPriceListSearchItem.findByArticleNumber", criteria));
                    }

                    return true;
                } catch (final Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        Task task = new Task("Поиск изделия по критерию...");
        task.executeTask();

        if (resultList.size() > 0) {
            new WarehousePriceListPreviewMode(controller, resultList);
        }
    }

    private void updateOrderInformation(final MarketingPriceListItem selectedItem) {
        String value = "Документ приказа не определен";

        if (!selectedItem.getOrderNumber().equals("")) {
            value = "<html><div style=\"text-align: left;\"> Приказ № " +
                    "<font color=\"blue\">" + selectedItem.getOrderNumber() + "</font> от " +
                    "<font color=\"blue\">" + DateUtils.getNormalDateFormat(selectedItem.getOrderDate()) + " г.</font><p> О торговой надбавке " +
                    "<font color=\"blue\">" + selectedItem.getAllowanceValue() + "%</font>";
        }

        lblOrderInformation.setText(value);
    }


    @Override
    public void addRecord() {
        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.NEW);
        editor.setParentTitle("Документ уценки");
        editingPane.setSourceEntity(null);
        // Для созданного пустого диалога устанавливаем панель редактирования
        editor.setEditorPane(editingPane);
        // Модально показываем форму и ожидаем закрытия
        if (editor.showModal()) {
            // Форма закрыта со значением true
            // Получаем DAO слой
            final DaoFactory factory = DaoFactory.getInstance();
            // ПОлучаем интерфейс для работы с БД
            final ICommonDao dao = factory.getCommonDao();
            try {
                // Сохраняем сущность в БД
                Object o = dao.saveEntity(editingPane.getSourceEntity());
                gridViewPort.setUpdatedObject(o);
            } catch (final SQLException e) {
                e.printStackTrace();
            }
            // ПОсле сохранения данных обновляем содержимое грида
            updateContent();
        }
    }

    @Override
    public void editRecord() {
        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        editor.setParentTitle("Документ уценки");

        MarketingPriceListItem selectedItem = (MarketingPriceListItem) gridViewPort.getSelectedItem();

        if (selectedItem.getStatus() == 0) {
            editor.getBtnSave().setEnabled(false);
        }

        editingPane.setSourceEntity(gridViewPort.getSelectedItem());
        // Для созданного пустого диалога устанавливаем панель редактирования
        editor.setEditorPane(editingPane);
        // Модально показываем форму и ожидаем закрытия
        if (editor.showModal()) {
            // Форма закрыта со значением true
            // Получаем DAO слой
            final DaoFactory factory = DaoFactory.getInstance();
            // ПОлучаем интерфейс для работы с БД
            final ICommonDao dao = factory.getCommonDao();
            try {
                // Обновляем сущность в БД
                Object o = editingPane.getSourceEntity();
                dao.updateEntity(o);
                gridViewPort.setUpdatedObject(o);
            } catch (final SQLException e) {
                e.printStackTrace();
            }
            // ПОсле сохранения данных обновляем содержимое грида
            updateContent();
        }
    }

    @Override
    public void deleteRecord() {

    }
}
