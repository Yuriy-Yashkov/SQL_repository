package by.march8.ecs.application.modules.planning.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.planning.manager.ProductPlanningManager;
import by.march8.ecs.application.modules.planning.manager.ProductionPlanningDataProvider;
import by.march8.ecs.application.modules.planning.model.ProductionPlanningReport;
import by.march8.ecs.application.modules.planning.report.PlanningDocumentReport;
import by.march8.ecs.application.modules.references.classifier.enums.ClassifierType;
import by.march8.ecs.application.modules.references.classifier.mode.ClassifierPickMode;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.ecs.framework.common.Settings;
import by.march8.entities.classifier.ClassifierModelView;
import by.march8.entities.planning.ProductionPlanningDetailView;
import by.march8.entities.planning.ProductionPlanningView;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy 27.11.2018 - 8:52.
 */
public class ProductionPlanningDetailMode extends AbstractFunctionalMode<ProductionPlanningDetailView> {

    private ProductionPlanningView activeDocument;
    private List<ProductionPlanningDetailView> data;
    private UCToolBar toolBar;
    private ClassifierPickMode classifier;
    private ProductPlanningManager manager;

    private ProductionPlanningDetailView gridFooter;

    private JButton btnAdditional;
    private JButton btnCalculate;
    private JButton btnPrint;

    public ProductionPlanningDetailMode(MainController controller, ProductPlanningManager manager, ProductionPlanningView item) {
        this.controller = controller;
        modeName = "Спецификация плана произодства " + item.getDocumentInformation();
        activeDocument = item;
        this.manager = manager;

        frameViewPort = new FrameViewPort(this.controller, MarchWindowType.DIALOGSIMPLE);
        frameViewPort.getFrameControl().setTitleFrame(modeName);

        gridViewPort = new GridViewPort<>(ProductionPlanningDetailView.class);
        data = gridViewPort.getDataModel();
        toolBar = frameViewPort.getFrameRegion().getToolBar();

        initializeComponent();
        initEvents();

        updateContent();

        gridViewPort.primaryInitialization();
        frameViewPort.getFrameControl().setFrameSize(new Dimension(1024, 700));
        frameViewPort.getFrameControl().showFrame();
    }

    private void initializeComponent() {

        if (frameViewPort.getFrame() instanceof BaseDialog) {
            BaseDialog dialog = (BaseDialog) frameViewPort.getFrame();
            dialog.setResizable(true);
        }

        frameViewPort.getButtonControl().getButtonPanel().setVisible(false);

        JPanel pFooter = new JPanel(new BorderLayout());
        ///////////////////////////////////////////////////////
        pFooter.setPreferredSize(new Dimension(100, 47));
        ///////////////////////////////////////////////////////

        JPanel pControl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pControl.setPreferredSize(new Dimension(0, 37));

        btnAdditional = new JButton();
        btnAdditional.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/money_24.png", "Дополнительно"));
        btnAdditional.setText("Дополнительно");

        btnCalculate = new JButton();
        btnCalculate.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/sum_24.png", "Расчет документа"));
        btnCalculate.setText("Расчет документа");

        btnPrint = new JButton();
        btnPrint.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/printer.png", "Печать документа"));
        btnPrint.setText("Сформировать");

        btnAdditional.setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE);
        btnCalculate.setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE);
        btnPrint.setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE);

        pControl.add(btnAdditional);
        pControl.add(btnCalculate);
        pControl.add(btnPrint);
        pFooter.add(pControl, BorderLayout.SOUTH);

        frameViewPort.getFrameRegion().getBottomContentPanel().setLayout(new BorderLayout());
        frameViewPort.getFrameRegion().getBottomContentPanel().add(pFooter, BorderLayout.SOUTH);

        int fontSize = 14;

        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBar.setPreferredSize(new Dimension(0, 47));

        toolBar.setRight(RightEnum.WRITE);
        toolBar.getBtnReport().setVisible(false);

        toolBar.getBtnNewItem().setEnabled(true);
        toolBar.getBtnEditItem().setEnabled(true);
        toolBar.getBtnDeleteItem().setVisible(false);

        JPanel pHeader = new JPanel(new MigLayout());
        pHeader.setPreferredSize(new Dimension(0, 60));

        JLabel lblDocumentNumber = new JLabel("Документ № : ");
        JLabel lblDocumentDate = new JLabel("Дата : ");

        Font font = new Font(toolBar.getFont().getName(), Font.BOLD, fontSize);

        JLabel lblDocumentNumberValue = new JLabel(activeDocument.getDocumentNumber());
        JLabel lblDocumentDateValue = new JLabel(DateUtils.getNormalDateFormat(activeDocument.getDocumentDate()));

        lblDocumentDate.setFont(font);
        lblDocumentDateValue.setFont(font);
        lblDocumentNumber.setFont(font);
        lblDocumentNumberValue.setFont(font);

        pHeader.add(lblDocumentNumber);
        pHeader.add(lblDocumentNumberValue, "wrap");

        pHeader.add(lblDocumentDate);
        pHeader.add(lblDocumentDateValue, "wrap");

        frameViewPort.getFrameRegion().getTopContentPanel().add(pHeader);
        frameViewPort.getFrameRegion().getCenterContentPanel().add(gridViewPort);

        classifier = new ClassifierPickMode(controller, ClassifierType.CUSTOM);

        gridFooter = new ProductionPlanningDetailView();
        gridViewPort.initializeFooterBar(gridFooter);
    }

    private void initEvents() {
        toolBar.registerEvents(this);

        gridViewPort.setTableEventHandler(new TableEventAdapter<ProductionPlanningDetailView>() {
            @Override
            public void onDoubleClick(int rowIndex, int columnIndex, ProductionPlanningDetailView item) {
                viewDetail(item);
            }
        });

        btnAdditional.addActionListener(a -> {

        });

        btnCalculate.addActionListener(a -> {
            calculate();
        });

        btnPrint.addActionListener(a -> {
            createDocument();
        });
    }

    private void createDocument() {
        ProductionPlanningDataProvider provider = ProductionPlanningDataProvider.getInstance();
        ProductionPlanningReport reportData = provider.getReportData(manager, activeDocument, data);

        if (reportData != null) {
            System.out.println("Создание отчета начато");
            new PlanningDocumentReport(reportData);
        } else {
            System.out.println("Создание отчета отменено");
        }
    }

    private void calculate() {

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                manager.calculateDocument(activeDocument);
                return true;
            }
        }

        Task task = new Task("Расчет документа");
        task.executeTask();
    }

    private void viewDetail(ProductionPlanningDetailView item) {
        if (item != null) {
            new ProductionPlanningArticleListMode(controller, manager, item);
            updateContent();
        }
    }

    private void updateFooterContent() {
        gridFooter.setAmount(0);
        for (ProductionPlanningDetailView item : data) {
            gridFooter.setAmount(gridFooter.getAmount() + item.getAmount());
        }
    }

    @Override
    public void updateContent() {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                DaoFactory<ProductionPlanningDetailView> factory = DaoFactory.getInstance();
                IGenericDao<ProductionPlanningDetailView> dao = factory.getGenericDao();
                List<QueryProperty> criteria = new ArrayList<>();
                criteria.add(new QueryProperty("documentId", activeDocument.getId()));
                List<ProductionPlanningDetailView> list;
                data.clear();
                try {
                    list = dao.getEntityListByNamedQuery(ProductionPlanningDetailView.class, "ProductionPlanningDetailView.findByDocumentId", criteria);
                    data.addAll(list);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                updateFooterContent();
                gridViewPort.updateViewPort();
                return true;
            }
        }

        Task task = new Task("Получение спецификации...");
        task.executeTask();
    }

    @Override
    public void addRecord() {
        ClassifierModelView item = (ClassifierModelView) classifier.showSelectModal(null);
        if (item != null) {

            class Task extends BackgroundTask {
                public Task(final String messageText) {
                    super(messageText);
                }

                @Override
                protected Boolean doInBackground() throws Exception {
                    // Добавляем артикул из справочника
                    manager.addNewArticle(activeDocument, item);
                    updateContent();
                    gridViewPort.updateViewPort();
                    return true;
                }
            }

            Task task = new Task("Обработка артикула " + item.getArticleName() + "...");
            task.executeTask();
        }
    }


    @Override
    public void editRecord() {
        viewDetail(gridViewPort.getSelectedItem());
    }

    @Override
    public void deleteRecord() {

    }

}
