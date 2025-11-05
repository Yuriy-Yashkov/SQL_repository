package by.march8.ecs.application.modules.economists.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces.IActiveFrameRegion;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.economists.editor.WarehousePriceListEditor;
import by.march8.ecs.application.modules.economists.model.PriceListDocumentProperty;
import by.march8.ecs.application.modules.economists.report.OfficeImporter;
import by.march8.ecs.application.modules.marketing.dao.MarketingPriceListMarchJDBC;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.classifier.RemainPriceListDetailItem;
import by.march8.entities.classifier.RemainPriceListItem;
import by.march8.entities.classifier.RemainPriceListSearchItem;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy 13.03.2017.
 */
@SuppressWarnings("all")
public class WarehousePriceListJournalMode extends AbstractFunctionalMode {

    GridViewPort gridPriceListDetail;
    private RightEnum right;
    private ArrayList<Object> dataPriceList;
    private ArrayList<Object> dataPriceListDetail;
    private JButton btnImport = new JButton();
    private JButton btnSearch = new JButton();

    private UCTextField tfDocumentNumber = new UCTextField();
    private MarketingPriceListMarchJDBC db = new MarketingPriceListMarchJDBC();
    private EditingPane editingPane;

    public WarehousePriceListJournalMode(MainController mainController, RightEnum rightEnum) {
        controller = mainController;
        modeName = "Прейскурант остатков на складе";
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

        toolBar.add(btnImport);

        btnImport.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/import_24.png", "Импорт документа"));
        btnImport.setToolTipText("Импортировать прейскурант");

        btnSearch.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/find24.png", "Поиск изделия"));
        btnSearch.setToolTipText("Поиск изделия в прейскурантах");

        // Комбик выбора типа документа
        JPanel pSearchPanel = new JPanel(null);
        JLabel lblDocumentNumber = new JLabel("Наименование :");
        lblDocumentNumber.setBounds(105, 6, 100, 20);
        tfDocumentNumber.setBounds(210, 6, 135, 20);

        pSearchPanel.add(lblDocumentNumber);
        pSearchPanel.add(tfDocumentNumber);

        pSearchPanel.setPreferredSize(new Dimension(350, 28));
        pSearchPanel.setOpaque(false);
        toolBar.add(pSearchPanel);
        toolBar.add(btnSearch);


        toolBar.add(new Box(BoxLayout.X_AXIS));

        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBar.setPreferredSize(new Dimension(0, 47));

        //toolBar.getBtnViewItem().setIcon(new ImageIcon(MainController.getRunPath() + "/Img/edit24.png", "Редактировать размер уценки"));
        toolBar.getBtnViewItem().addActionListener(a -> {
            //updateContent();
        });

        if (right == RightEnum.WRITE) {
            toolBar.getBtnNewItem().setVisible(true);
            toolBar.getBtnEditItem().setVisible(true);
            toolBar.getBtnDeleteItem().setVisible(true);
            btnImport.setVisible(true);
        } else {
            toolBar.getBtnNewItem().setVisible(false);
            toolBar.getBtnEditItem().setVisible(false);
            toolBar.getBtnDeleteItem().setVisible(false);
            btnImport.setVisible(false);
        }

        gridViewPort = new GridViewPort(RemainPriceListItem.class, false);
        gridViewPort.setPreferredSize(new Dimension(10, 100));
        dataPriceList = gridViewPort.getDataModel();

        gridPriceListDetail = new GridViewPort(RemainPriceListDetailItem.class, false);
        dataPriceListDetail = gridPriceListDetail.getDataModel();

        final JSplitPane splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gridViewPort, gridPriceListDetail);
        splitPanel.setResizeWeight(0.7);
        splitPanel.setOneTouchExpandable(true);
        splitPanel.setContinuousLayout(true);

        IActiveFrameRegion frameRegion = frameViewPort.getFrameRegion();
        frameRegion.getCenterContentPanel().add(splitPanel, BorderLayout.CENTER);

        editingPane = new WarehousePriceListEditor(frameViewPort);

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
                if (right == RightEnum.WRITE) {
                    editRecord();
                }
            }

            @Override
            public void onSelectChanged(final int rowIndex, final Object object) {
                RemainPriceListItem selectedItem = (RemainPriceListItem) object;
                if (selectedItem != null) {
                    updateDetail((selectedItem.getId()));
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

        btnImport.addActionListener(a -> {
            RemainPriceListItem selectedItem = (RemainPriceListItem) gridViewPort.getSelectedItem();
            if (selectedItem != null) {
                importFileDialog(selectedItem);
                return;
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
                DaoFactory<RemainPriceListItem> factory = DaoFactory.getInstance();
                IGenericDao<RemainPriceListItem> dao = factory.getGenericDao();
                try {
                    dataPriceList.clear();
                    dataPriceList.addAll(dao.getEntityListByNamedQuery(RemainPriceListItem.class, "RemainPriceListItem.findAll", null));
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

    private void updateDetail(int id) {
        DaoFactory<RemainPriceListDetailItem> factory = DaoFactory.getInstance();
        IGenericDao<RemainPriceListDetailItem> dao = factory.getGenericDao();

        java.util.List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("priceList", id));
        try {
            dataPriceListDetail.clear();
            dataPriceListDetail.addAll(dao.getEntityListByNamedQuery(RemainPriceListDetailItem.class, "RemainPriceListDetailItem.findByPriceList", criteria));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        //frameViewPort.updateContent();
        gridPriceListDetail.updateViewPort();
        if (dataPriceListDetail.size() > 0) {
            btnImport.setEnabled(false);
        } else {
            btnImport.setEnabled(true);
        }
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

    private void importFileDialog(RemainPriceListItem selectedItem) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Файлы электронных таблиц LibreOffice", "ods", "ods");
        fileChooser.setFileFilter(filter);

        //fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        //fileChooser.setCurrentDirectory(new File("c://"));

        int result = fileChooser.showOpenDialog(controller.getMainForm());
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            PriceListDocumentProperty property = new PriceListDocumentProperty();
            property.setDate(selectedItem.getDocumentDate());
            property.setNumber(selectedItem.getDocumentNumber());
            property.setTradeAllowanceValue(selectedItem.getTradeAllowanceValue());
            property.setType("Прейскурант уцененных изделий");

            if (property == null) {
                // Вывод диалого о несоответствии имени файла
                Dialogs.showInformationDialog("<html><div style=\"text-align: left;\">Выбраный прейскурант уцененных изделий содержит неверное имя файла,\nимпортировать данные в базу невозможно!");
            } else {
                property.setFile(selectedFile.getAbsolutePath().replace("\\", "/"));
                // Вывод диалога с информацией о файле прейскуранта
                final int answer = Dialogs.showQuestionDialog("<html><div style=\"text-align: left;\">Выбран прейскурант уцененных изделий:<br>" +
                        "<p>Документ №" + "<font color=\"blue\">" + property.getNumber() + "</font> от " +
                        "<font color=\"blue\">" + DateUtils.getNormalDateFormat(property.getDate()) + "</font> с торговой надбавкой " +
                        "<font color=\"blue\">" + property.getTradeAllowanceValue() + "%</font>" +
                        "<p>Загрузить выбранный прейскурант в базу ?", "Импорт прейскуранта уценки");
                if (answer == 0) {
                    importProcessing(property, selectedItem);
                }
            }
            updateContent();
        }
    }

    private void importProcessing(PriceListDocumentProperty property, RemainPriceListItem priceList) {
        // Импорт спецификации прейскуранта из ODS документа
        OfficeImporter importer = new OfficeImporter(property.getFile());
        List<RemainPriceListDetailItem> detailList = importer.getPriceListDetail();
        if (detailList != null) {
            if (!detailList.isEmpty()) {
                db.priceListDetailProcessing(priceList, detailList);
            }
        }
    }

    @Override
    public void addRecord() {
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.NEW);
        editor.setParentTitle("Прейскурант уценки");
        editingPane.setSourceEntity(null);
        editor.setEditorPane(editingPane);
        if (editor.showModal()) {
            RemainPriceListItem item = (RemainPriceListItem) editingPane.getSourceEntity();
            if (item != null) {
                db.insertPriceList(item);
            }
            updateContent();
        }
    }

    @Override
    public void editRecord() {
        RemainPriceListItem selectedItem = (RemainPriceListItem) gridViewPort.getSelectedItem();
        if (selectedItem == null) {
            return;
        }

        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        editor.setParentTitle("Прейскурант уценки");
        editingPane.setSourceEntity(selectedItem);
        editor.setEditorPane(editingPane);
        if (editor.showModal()) {
            RemainPriceListItem item = (RemainPriceListItem) editingPane.getSourceEntity();
            if (item != null) {
                db.updatePriceList(item);
            }
            updateContent();
        }
    }

    @Override
    public void deleteRecord() {
        RemainPriceListItem selectedItem = (RemainPriceListItem) gridViewPort.getSelectedItem();
        if (selectedItem != null) {
            final int answer = Dialogs.showDeleteDialog(selectedItem.getDocumentInformation());
            if (answer == 0) {
                try {
                    db.deletePriceList(selectedItem);
                } catch (Exception e) {
                    MainController.exception(e, "Ошибка удаления записи");
                }
            }

            updateContent();
        }
    }

}
