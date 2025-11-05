package by.march8.ecs.application.modules.marketing.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.marketing.dao.MarketingPriceListMarchJDBC;
import by.march8.ecs.application.modules.marketing.editor.MarketingPriceListDetailItemEditor;
import by.march8.ecs.application.modules.marketing.editor.MarketingPriceListTableEditor;
import by.march8.ecs.application.modules.marketing.model.MarketingPriceListDetailCellRenderer;
import by.march8.ecs.application.modules.marketing.model.MarketingReportData;
import by.march8.ecs.application.modules.marketing.model.ScaleItem;
import by.march8.ecs.application.modules.marketing.reports.MarketingPriceListAllowanceReport;
import by.march8.ecs.application.modules.marketing.reports.MarketingPriceListMarkdownReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.CallBack;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.ecs.framework.common.Settings;
import by.march8.ecs.framework.common.money.RoundUtils;
import by.march8.entities.classifier.ProductionItemBase;
import by.march8.entities.marketing.MarketingPriceListItem;
import by.march8.entities.marketing.ViewMarketingPriceListDetailItem;
import dept.marketing.cena.CenaPDB;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy 24.10.2017.
 */
public class MarketingPriceListJournalDetailMode extends AbstractFunctionalMode implements CallBack {

    private ArrayList<Object> data;
    private MarketingPriceListItem item;
    private boolean needToUpdate = false;

    private JPanel pFooter;
    private JPanel pControl;

    private JButton btnCalculate;
    private JButton btnUpdate;
    private JButton btnImport;
    private JButton btnPrint;
    private JButton btnInsert;


    // private JButton btnAddItem;
    //private JButton btnEditItem;
    //private JButton btnDeleteItem;
    private JButton btnTableEditor;
    private RightEnum rights = RightEnum.WRITE;

    private EditingPane editingPane;
    private MarketingPriceListTableEditor cellEditor;

    public MarketingPriceListJournalDetailMode(MainController mainController, MarketingPriceListItem parentItem) {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                controller = mainController;
                item = parentItem;
                // this.document = document ;
                String title = "Спецификация документа ";
                if (item != null) {
                    if (item.getDocumentNumber() != null) {
                        title += "№" + item.getDocumentNumber();
                        if (item.getDocumentDate() != null) {
                            title += " от " + DateUtils.getNormalDateFormat(item.getDocumentDate()) + "г.";
                        }

                        if (parentItem.getDocumentType() == 2) {
                            title += " уценки ";
                            if (item.isToPrimeCost()) {
                                title += "до себестоимости";
                            } else {
                                title += "с торговой надбавкой  " + item.getDocumentValue() + "%";
                            }
                        } else {
                            title += "с торговой надбавкой  " + item.getDocumentValue() + "%";
                        }
                    }
                }

                frameViewPort = new FrameViewPort(mainController, MarchWindowType.DIALOG);
                frameViewPort.getFrameControl().setTitleFrame(title);

                initComponents();


                // Инициализация грида
                gridViewPort = new GridViewPort(ViewMarketingPriceListDetailItem.class, false);
                //gridViewPort.resetFilter();

                frameViewPort.getButtonControl().getOkButton().setVisible(false);
                frameViewPort.getButtonControl().getCancelButton().setText("Закрыть");

                frameViewPort.setGridViewPort(gridViewPort);
                // Получаем ссылку на модель данных грида
                data = gridViewPort.getDataModel();
                //data.addAll(document.getDetailList());

                gridViewPort.setCustomCellRender(new MarketingPriceListDetailCellRenderer());

                editingPane = new MarketingPriceListDetailItemEditor(frameViewPort);
                editingPane.setRight(RightEnum.WRITE);
                editingPane.setSourceEntityClass(ScaleItem.class);

                cellEditor = new MarketingPriceListTableEditor(gridViewPort, item);
                gridViewPort.setCustomCellEditor(cellEditor);
                gridViewPort.setIgnoreNotEditableCells(true);
                gridViewPort.getTable().setRowSelectionAllowed(true);


                initEvents();

                gridViewPort.updateViewPort();
                //prepareView();
                if (item != null) {
                    gridViewPort.primaryInitialization();
                    updateContent();
                    frameViewPort.getFrameControl().setFrameSize(new Dimension(1124, 680));
                }
                return true;
            }
        }

        Task task = new Task("Получение спецификации документа...");
        task.executeTask();
    }

    @Override
    public void updateContent() {
        DaoFactory<ViewMarketingPriceListDetailItem> factory = DaoFactory.getInstance();
        IGenericDao<ViewMarketingPriceListDetailItem> dao = factory.getGenericDao();

        java.util.List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("priceList", item.getId()));
        try {
            data.clear();
            data.addAll(dao.getEntityListByNamedQuery(ViewMarketingPriceListDetailItem.class, "ViewMarketingPriceListDetailItem.findByPriceList", criteria));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        //frameViewPort.updateContent();
        gridViewPort.updateViewPort();
        frameViewPort.getFrameRegion().getToolBar().updateButton(data.size());

        updateControls();

        cellEditor.setCallBack(this);
    }

    private void initComponents() {
        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();

        toolBar.setRight(rights);

        toolBar.getBtnNewItem().setVisible(true);
        toolBar.getBtnEditItem().setVisible(false);
        toolBar.getBtnDeleteItem().setVisible(true);


        toolBar.getBtnReport().setVisible(false);
        toolBar.setVisibleSearchControls(false);

        btnInsert = new JButton();
        btnInsert.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/download_24.png", "Заполнить из запроса"));
        btnInsert.setToolTipText("Заполнить из запроса");
        toolBar.add(btnInsert);

        toolBar.registerEvents(this);

        pFooter = new JPanel(new BorderLayout());
        pFooter.setPreferredSize(new Dimension(100, 40));
        //pFooter.setBackground(Color.black);

        frameViewPort.getFrameRegion().getBottomContentPanel().setLayout(new BorderLayout());
        frameViewPort.getFrameRegion().getBottomContentPanel().add(pFooter, BorderLayout.SOUTH);

        if (frameViewPort.getFrame() instanceof BaseDialog) {
            BaseDialog dialog = (BaseDialog) frameViewPort.getFrame();
            frameViewPort.getButtonControl().getButtonPanel().setVisible(false);
            dialog.setResizable(true);
        }

        pControl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pControl.setPreferredSize(new Dimension(0, 37));

        pFooter.add(pControl, BorderLayout.SOUTH);


        btnCalculate = new JButton();
        btnCalculate.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/sum_24.png", "Расчет "));
        btnCalculate.setText("Расчет документа");

        btnUpdate = new JButton();
        btnUpdate.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/sum_24.png", "Обновить Данные "));
        btnUpdate.setText("Обновить данные");

        btnImport = new JButton();
        btnImport.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/import_24.png", "Импортировать в справочник"));
        btnImport.setText("Импортировать в справочник");

        btnPrint = new JButton();
        btnPrint.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/printer.png", "Печать документа"));
        btnPrint.setText("Печать документа");

        btnTableEditor = new JButton();
        btnTableEditor.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/table_edit_24.png", "Редактирование в таблице"));

        toolBar.addSeparator();

        btnCalculate.setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE_PLUS);
        btnUpdate.setPreferredSize(Settings.BUTTON_NORMAL_SIZE);
        btnImport.setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE_PLUS);
        btnPrint.setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE_PLUS);

        pControl.add(btnImport);
        pControl.add(btnCalculate);
        //pControl.add(btnUpdate);
        pControl.add(btnPrint);
    }

    /**
     * Метод отображает форму и в случае, если документ был изменен и расчитан,
     * после закрытия формы произойдет обновление данных в главной таблице
     *
     * @return true - документ был расчитан, нужно произвести обновление содержимого грида
     */
    public boolean showMode() {
        if (item != null) {
            frameViewPort.getFrameControl().showFrame();
        } else {
            frameViewPort.getFrameControl().closeFrame();
        }

        return needToUpdate;
    }

    private void initEvents() {
        btnCalculate.addActionListener(a -> {
            class Task extends BackgroundTask {
                public Task(final String messageText) {
                    super(messageText);
                }

                @Override
                protected Boolean doInBackground() throws Exception {
                    refreshData();
                    recalculate();
                    return true;
                }
            }

            Task task = new Task("Сохранение документа...");
            task.executeTask();

        });

        // двойной клик на строке
        gridViewPort.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onDoubleClick(int rowIndex, int columnIndex, Object object) {
            }

            @Override
            public void onSelectChanged(final int rowIndex, final Object object) {
              /*  ViewMarketingPriceListDetailItem selectedItem = (ViewMarketingPriceListDetailItem) object;
                if (selectedItem != null) {
                    //updateDetail((selectedItem.getId()));
                }
                */
            }
        });

        btnPrint.addActionListener(a -> {
            MarketingReportData reportData = new MarketingReportData();
            reportData.setDocument(item);
            reportData.setData(data);
            if (item.getDocumentType() == 2) {

                Object[] options = {"Да",
                        "Нет"};
                if (Dialogs.showQuestionDialog("<html><div style=\"text-align: left;\"> " + "<font>" +
                        "Сформировать документ вместе с картинками?" +
                        "</font>"
                        + "</html>", "Формирование документа...", options) == 0) {
                    new MarketingPriceListMarkdownReport(reportData, true);
                } else {
                    new MarketingPriceListMarkdownReport(reportData, false);
                }
            } else {
                new MarketingPriceListAllowanceReport(reportData);
            }
        });

        btnImport.addActionListener(a -> {

        });

        btnUpdate.addActionListener(a -> {
            class Task extends BackgroundTask {
                public Task(final String messageText) {
                    super(messageText);
                }

                @Override
                protected Boolean doInBackground() throws Exception {
                    refreshData();
                    recalculate();
                    return true;
                }
            }

            Task task = new Task("Обновление данных");
            task.executeTask();
        });

        btnInsert.addActionListener(a -> {

        });
    }

    private void refreshData() {
        MarketingPriceListMarchJDBC db = new MarketingPriceListMarchJDBC();
        CenaPDB cdb = new CenaPDB();
        for (Object o : data) {
            ViewMarketingPriceListDetailItem activeItem = (ViewMarketingPriceListDetailItem) o;
            if (activeItem != null) {
                // Берем свежие данные по изделию и пишем в базу
                ProductionItemBase item = db.getPriceItemByArticleNumberAndSizeAndGrade(activeItem.getArticleNumber(), activeItem.getMinimumSize(), 1);
                if (item != null) {
                    // Получаем оптовую цену
                    activeItem.setEffectivePriceValue(item.getWholesalePrice());
                    // Получаем себестоимость и рентабельность
                    try {
                        float[] values = cdb.getSstoimostRentabel(item.getArticleCode(), activeItem.getMinimumSize());
                        double primeCost = values[0];
                        double profitability = values[1];

                        if (profitability == 0) {
                            profitability = (((activeItem.getEffectivePriceValue() - primeCost) / primeCost) * 100);
                        }

                        double primeCostCalculated = (item.getWholesalePrice() * 100) / (profitability + 100);
                        activeItem.setPrimeCostValue(RoundUtils.round(primeCostCalculated, 5));
                        activeItem.setProfitabilityValue(RoundUtils.round(profitability, 4));
                        activeItem.setChanged(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        gridViewPort.updateViewPort();
    }

    private void recalculate() {
        // Необходимые проверки перед пакетным расчетом и сохранением спецификации


        //Сохраниение документа
        for (Object o : data) {
            ViewMarketingPriceListDetailItem activeItem = (ViewMarketingPriceListDetailItem) o;
            if (activeItem != null) {
                // Расчет документа
                if (item.isToPrimeCost()) {
                    // System.out.println("По себестоимости");
                    activeItem.setSuggestedPriceValue(activeItem.getPrimeCostValue() + RoundUtils.round((activeItem.getPrimeCostValue() * 5 / 100), RoundUtils.ROUND_XXX_XX));
                    activeItem.setChangePercentValue(-1);
                } else {
                    activeItem.setChangePercentValue(item.getDocumentValue());
                    activeItem.setSuggestedPriceValue(
                            RoundUtils.round(activeItem.getEffectivePriceValue() - activeItem.getEffectivePriceValue() * activeItem.getChangePercentValue() / 100
                                    , RoundUtils.ROUND_XXX_XX));
                }
                activeItem.setChanged(true);
            }
        }

        //gridViewPort.updateViewPort();
        //updateContent();

        MarketingPriceListMarchJDBC db = new MarketingPriceListMarchJDBC();
        if (db.updatePriceList(data)) {
            //db.changePriceListStatus(item.getId(), 1);
            //item.setStatus(1);
            updateContent();
        }
    }

    @Override
    public void addRecord() {
        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.NEW);
        editor.setParentTitle("Изделие в прейскуранте");
        editingPane.setSourceEntity(null);
        // Для созданного пустого диалога устанавливаем панель редактирования
        editor.setEditorPane(editingPane);
        // Модально показываем форму и ожидаем закрытия
        if (editor.showModal()) {

            //TODO АПАСНА!!!!!!!!
            List<ScaleItem> scaleList = (List<ScaleItem>) editingPane.getSourceEntity();
            if (scaleList != null) {
                saveNewItem(scaleList);
            }
            // ПОсле сохранения данных обновляем содержимое грида
            updateContent();
        }
    }

    private void saveNewItem(List<ScaleItem> list) {
        for (Object o : data) {
            ViewMarketingPriceListDetailItem currentItem = (ViewMarketingPriceListDetailItem) o;
            if (currentItem != null) {
                for (ScaleItem item : list) {

                    if ((currentItem.getItemId() == item.getId()) && (currentItem.getSizeRange().equals(item.getSizeScale()))) {
                        item.setSaveable(false);
                    }
                }
            }
        }

        MarketingPriceListMarchJDBC db = new MarketingPriceListMarchJDBC();
        db.saveScaleItem(item, list);
    }

    @Override
    public void editRecord() {

    }

    @Override
    public void deleteRecord() {

        ViewMarketingPriceListDetailItem selectedItem = (ViewMarketingPriceListDetailItem) gridViewPort.getSelectedItem();
        if (selectedItem != null) {
            //updateDetail((selectedItem.getId()));

            final int answer = Dialogs.showDeleteDialog(selectedItem.getItemName());

            if (answer == 0) {
                try {
                    // final BaseEntity baseEntity = (BaseEntity) gridViewPort.getSelectedItem();
                    MarketingPriceListMarchJDBC db = new MarketingPriceListMarchJDBC();
                    if (db.deletePriceListDetailItemById(selectedItem.getId())) {
                        gridViewPort.setDeletedObject(gridViewPort.getSelectedItem());
                        updateContent();
                    }
                } catch (Exception e) {
                    MainController.exception(e, "Ошибка удаления записи");
                }
            }
        }
    }

    private void updateControls() {
        btnImport.setEnabled(false);

        switch (item.getStatus()) {
            case 0:
                btnCalculate.setEnabled(false);
                btnImport.setEnabled(true);
                btnPrint.setEnabled(true);

                final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();

                toolBar.getBtnNewItem().setVisible(false);
                toolBar.getBtnEditItem().setVisible(false);
                toolBar.getBtnDeleteItem().setVisible(false);
                break;
            case 1:
                btnCalculate.setEnabled(false);
                btnPrint.setEnabled(true);
                break;
            case 2:
                btnPrint.setEnabled(false);
                btnCalculate.setEnabled(true);
                break;
            case 3:
                btnPrint.setEnabled(false);
                btnCalculate.setEnabled(true);
                break;
        }

        final TableColumnModel columnModel = gridViewPort.getTable().getColumnModel();

        if (item.getDocumentType() == 1) {
            for (int c = 4; c < 8; c++) {
                columnModel.getColumn(c).setMaxWidth(0);
                columnModel.getColumn(c).setMinWidth(0);
                columnModel.getColumn(c).setPreferredWidth(0);
            }
            btnCalculate.setEnabled(false);

        } else {
            for (int c = 4; c < 8; c++) {
                columnModel.getColumn(c).setMaxWidth(80);
                columnModel.getColumn(c).setMinWidth(80);
                columnModel.getColumn(c).setPreferredWidth(80);
            }
        }

        if (data.size() < 1) {
            btnCalculate.setEnabled(false);
        } else {
            btnPrint.setEnabled(true);
        }
    }

    @Override
    public boolean onCallBack() {
        // Тут будем сохранять в БД текущие изменения
        MarketingPriceListMarchJDBC db = new MarketingPriceListMarchJDBC();
        db.updatePriceList(data);

        for (Object o : data) {
            ViewMarketingPriceListDetailItem item = (ViewMarketingPriceListDetailItem) o;
            if (item != null) {
                // System.out.println(item.getId() +" - "+item.getPriceListId()+" - "+ item.getProductId()+" - "+ item.isDataChanged());
                item.setChanged(false);
            }
        }

        return true;
    }

}
