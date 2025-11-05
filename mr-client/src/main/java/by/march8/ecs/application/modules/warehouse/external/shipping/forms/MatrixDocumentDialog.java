package by.march8.ecs.application.modules.warehouse.external.shipping.forms;

import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDaoGUI;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.api.utils.DatePeriod;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.MatrixJDBC;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.MatrixContractor;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.MatrixModel;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.PresetContractor;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.SaleMonitorReportCreator;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.warehouse.DocumentMatrixEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MatrixDocumentDialog extends BasePickDialog {

    private UCDatePeriodPicker datePeriodPicker;
    private UCTextField tfContractorCode;
    private JCheckBox chbSelectSaveDir;

    private List<DocumentMatrixEntity> documentList;
    private GridViewPort<DocumentMatrixEntity> gvDocuments;

    private List<MatrixContractor> listContractors;
    private GridViewPort<MatrixContractor> gvContractors;

    private List<MatrixModel> listModels;
    private GridViewPort<MatrixModel> gvModels;

    private UCToolBar tbContractors;
    private UCToolBar tbModels;

    private JButton btnCreateDocument;
    private JPanel pLeftPanel;
    private JPanel pRightPanel;
    private JSplitPane spVertical;
    private JSplitPane spHorisontal;

    private JPanel pContractors;
    private JPanel pModels;

    private MatrixJDBC db;
    private JPopupMenu popupMenuTools;
    private JMenuItem miMatrixPeriod;
    private JMenuItem miMatrixCurrent;


    public MatrixDocumentDialog(MainController controller) {
        super(controller);
        init();
        initEvents();
        updateContent();
        showModalFrame();
    }


    private void init() {
        setFrameSize(new Dimension(800, 600));
        setTitle("Матрица отгрузок по контрагентам");

        gvDocuments = new GridViewPort<>(DocumentMatrixEntity.class);
        documentList = gvDocuments.getDataModel();

        gvContractors = new GridViewPort<>(MatrixContractor.class);
        listContractors = gvContractors.getDataModel();

        gvModels = new GridViewPort<>(MatrixModel.class);
        listModels = gvModels.getDataModel();


        btnCreateDocument = new JButton("");
        btnCreateDocument.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/excel_24.png", "Выбрать/Инвертировать"));
        btnCreateDocument.setToolTipText("Сформировать");

        getToolBar().add(btnCreateDocument);
        getToolBar().getBtnDeleteItem().setVisible(false);
        getToolBar().getBtnNewItem().setVisible(false);
        getToolBar().getBtnEditItem().setVisible(false);

        pLeftPanel = new JPanel(new BorderLayout());
        pRightPanel = new JPanel(new BorderLayout());

        pLeftPanel.add(gvDocuments, BorderLayout.CENTER);

        spVertical = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pLeftPanel, pRightPanel);
        spVertical.setResizeWeight(0.5);
        spVertical.setOneTouchExpandable(true);
        spVertical.setContinuousLayout(true);

        pContractors = new JPanel(new BorderLayout());
        pModels = new JPanel(new BorderLayout());

        tbContractors = new UCToolBar();
        tbModels = new UCToolBar();

        //pContractors.add(tbContractors, BorderLayout.NORTH);
        pContractors.add(gvContractors, BorderLayout.CENTER);


        //pModels.add(tbModels, BorderLayout.NORTH);
        pModels.add(gvModels, BorderLayout.CENTER);


        spHorisontal = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pContractors, pModels);
        spHorisontal.setResizeWeight(0.5);
        spHorisontal.setOneTouchExpandable(true);
        spHorisontal.setContinuousLayout(true);
        pRightPanel.add(spHorisontal, BorderLayout.CENTER);

        getCenterContentPanel().add(spVertical);

        db = new MatrixJDBC();

        popupMenuTools = new JPopupMenu();
        miMatrixPeriod = new JMenuItem("За период");
        miMatrixCurrent = new JMenuItem("На текущую дату");

        popupMenuTools.add(miMatrixPeriod);
        popupMenuTools.add(miMatrixCurrent);
    }


    private void createMatrix(DocumentMatrixEntity document, boolean isCurrent) {
        List<PresetContractor> contractors = new ArrayList<>();
        String contractors_ = document.getContractors();
        String[] aContractor = contractors_.split(",");
        for (String s : aContractor) {
            if (s.contains("_")) {
                String[] aCode = s.split("_");
                contractors.add(new PresetContractor(Integer.valueOf(aCode[0]), true));
            } else {
                contractors.add(new PresetContractor(Integer.valueOf(s)));
            }
        }

        String[] aModels = document.getModels().split(",");
        int[] models = new int[aModels.length];
        for (int i = 0; i < aModels.length; i++) {
            models[i] = Integer.valueOf(aModels[i]);
        }

        DatePeriod period = new DatePeriod();
        period.setBegin(document.getPeriodBegin());
        if (isCurrent) {
            period.setEnd(new Date());
        } else {
            period.setEnd(document.getPeriodEnd());
        }

        SaleMonitorReportCreator creator = new SaleMonitorReportCreator();
        creator.createMatrix(contractors, models, period);
    }

    private void initEvents() {
        gvDocuments.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onClick(final int rowIndex, int columnIndex, Object item) {
                updateDocumentDataContext((DocumentMatrixEntity) item);
            }
        });

        btnCreateDocument.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                popupMenuTools.show(e.getComponent(), 0, e.getComponent().getHeight());
            }
        });

        miMatrixCurrent.addActionListener(a -> {
            DocumentMatrixEntity document = gvDocuments.getSelectedItem();
            if (document != null) {
                class Task extends BackgroundTask {
                    public Task(final String messageText) {
                        super(messageText);
                    }

                    @Override
                    protected Boolean doInBackground() throws Exception {
                        createMatrix(document, true);
                        return true;
                    }
                }
                Task task = new Task("Формирование матрицы отгрузки...");
                task.executeTask();
            } else {
                System.out.println("ПУСТО");
            }

        });

        miMatrixPeriod.addActionListener(a -> {
            DocumentMatrixEntity document = gvDocuments.getSelectedItem();
            if (document != null) {

                class Task extends BackgroundTask {
                    public Task(final String messageText) {
                        super(messageText);
                    }

                    @Override
                    protected Boolean doInBackground() throws Exception {
                        createMatrix(document, false);
                        return true;
                    }
                }
                Task task = new Task("Формирование матрицы отгрузки...");
                task.executeTask();
            } else {
                System.out.println("ПУСТО");
            }
        });
    }

    private void updateContent() {
        DaoFactory<DocumentMatrixEntity> factory = DaoFactory.getInstance();
        IGenericDaoGUI<DocumentMatrixEntity> dao = factory.getGenericDao();
        //List<QueryProperty> criteria = new ArrayList<>();
        //criteria.add(new QueryProperty("periodBegin", period.getBegin()));
        //criteria.add(new QueryProperty("periodEnd", period.getEnd()));
        //criteria.add(new QueryProperty("contractor", document.getContractorId()));
        documentList.clear();
        try {
            documentList.addAll(dao.getEntityListByNamedQueryGUI(DocumentMatrixEntity.class, "DocumentMatrixEntity.findAll", null));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        gvDocuments.updateViewPort();
    }

    private void updateDocumentDataContext(DocumentMatrixEntity document) {
        if (document == null) {
            return;
        }
        // парсим контрагента
        String contractors_ = document.getContractors();
        List<String> contractorList = new ArrayList<>();
        String[] aContractor = contractors_.split(",");
        for (String s : aContractor) {
            String code = s;
            if (s.contains("_")) {
                String[] aCode = s.split("_");
                code = aCode[0];
            }
            contractorList.add(code);

        }
        String contractors = String.join(",", contractorList);

        listContractors.clear();
        listContractors.addAll(db.getMatrixContractors(contractors));

        listModels.clear();
        listModels.addAll(db.getMatrixModels(document.getModels()));

        gvContractors.updateViewPort();
        gvModels.updateViewPort();

    }
}
