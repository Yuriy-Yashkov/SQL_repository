package by.march8.ecs.application.modules.warehouse.internal.displacement.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.api.utils.DatePeriod;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.internal.displacement.reports.InternalInvoiceReport;
import by.march8.ecs.application.modules.warehouse.internal.displacement.reports.InternalInvoiceToXML;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.ecs.services.rationing.RationingService;
import by.march8.entities.readonly.NsiDepartment;
import by.march8.entities.warehouse.VInternalInvoiceReport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Функциональный режим для работы с накладными внутреннего перемещения
 */
public class DisplacementReportMode extends AbstractFunctionalMode {

    private RightEnum right;


    private ArrayList<Object> data;

    private UCDatePeriodPicker datePeriodPicker;

    private JButton btnUpdate = new JButton();
    private JButton btnExport = new JButton();

    private JComboBox<NsiDepartment> cbDeptSender = new JComboBox<>();
    private JComboBox<NsiDepartment> cbDeptReceiver = new JComboBox<>();
    private JLabel label;
    private RationingService service;


    public DisplacementReportMode(MainController mainController) {
        controller = mainController;
        modeName = "Отчет о сдаче готовой продукции";
        frameViewPort = new FrameViewPort(mainController, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);
        right = controller.getRight(modeName);

        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.registerEvents(this);
        toolBar.setRight(right);
        toolBar.getBtnReport().setVisible(false);

        toolBar.getBtnEditItem().setVisible(false);
        toolBar.getBtnNewItem().setVisible(false);
        toolBar.getBtnDeleteItem().setVisible(false);

        btnUpdate.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/update_data.png", "Обновть данные"));
        btnUpdate.setToolTipText("Обновить данные");

        btnExport.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/excel_24.png", "Экспорт в DBF"));
        btnExport.setToolTipText("Экспорт в Excel");

        presetComboboxes();


        JPanel pSearchPanel = new JPanel(null);
        JLabel lblSender = new JLabel("Отправитель : ");
        JLabel lblReceiver = new JLabel("Получатель : ");

        lblSender.setBounds(5, 4, 90, 20);
        cbDeptSender.setBounds(95, 4, 200, 20);
        lblReceiver.setBounds(305, 4, 90, 20);
        cbDeptReceiver.setBounds(390, 4, 200, 20);

        pSearchPanel.add(lblSender);
        pSearchPanel.add(cbDeptSender);
        pSearchPanel.add(lblReceiver);
        pSearchPanel.add(cbDeptReceiver);

        pSearchPanel.setPreferredSize(new Dimension(605, 28));
        pSearchPanel.setOpaque(false);

        toolBar.add(pSearchPanel);

        datePeriodPicker = new UCDatePeriodPicker();
        datePeriodPicker.setName("datePeriodPicker");
        datePeriodPicker.setDatePickerEnd(new Date());
        datePeriodPicker.setDatePickerBegin(DateUtils.getFirstDay(datePeriodPicker.getDatePickerEnd()));

        datePeriodPicker.setEditable(true);

        toolBar.add(datePeriodPicker);
        toolBar.add(btnUpdate);

        toolBar.add(btnExport);
        toolBar.setSize(10, 50);

        toolBar.add(new Box(BoxLayout.X_AXIS));

        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBar.setPreferredSize(new Dimension(0, 47));

        JPopupMenu popupMenuTools = new JPopupMenu();

        JMenuItem miExportToExcel = new JMenuItem("Выгрузить в EXCEL");
        JMenuItem miExportToXML = new JMenuItem("Выгрузить в XML");

        miExportToExcel.addActionListener(a -> {
            exportToExcel();
        });
        miExportToXML.addActionListener(a -> {
            exportToXML();
        });
        popupMenuTools.add(miExportToExcel);
        popupMenuTools.add(miExportToXML);

        btnExport.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                popupMenuTools.show(e.getComponent(), 0, e.getComponent().getHeight());
            }
        });

        gridViewPort = new GridViewPort(VInternalInvoiceReport.class, false);
        data = gridViewPort.getDataModel();
        frameViewPort.setGridViewPort(gridViewPort);

        initEvents();

        // Устанавливаем кастомный рендер гриду
        //gridViewPort.setCustomCellRender(new DisplacementCellRender());

        gridViewPort.initialFooter();

        Container footer = gridViewPort.getFooterPanel();
        footer.setLayout(new FlowLayout(FlowLayout.LEFT));
        label = gridViewPort.getFooterTextComponent();
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 12));

        service = RationingService.getInstance();

        //updateContent();
        gridViewPort.primaryInitialization();
        frameViewPort.getFrameControl().showFrame();
    }

    private void presetComboboxes() {

        List<NsiDepartment> list = null;

        DaoFactory<NsiDepartment> factory = DaoFactory.getInstance();
        IGenericDao<NsiDepartment> dao = factory.getGenericDao();
        try {
            list = dao.getAllEntity(NsiDepartment.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (list != null) {
            cbDeptSender.setModel(new DefaultComboBoxModel<>());
            cbDeptReceiver.setModel(new DefaultComboBoxModel<>());
            //comboBox.setModel(new DefaultComboBoxModel(contractorReceiver.getAddressList().toArray()));
            for (NsiDepartment item : list) {
                cbDeptSender.addItem(item);
                cbDeptReceiver.addItem(item);
            }

            cbDeptReceiver.setSelectedIndex(-1);
            cbDeptSender.setSelectedIndex(-1);
        }
    }

    private void exportToExcel() {
        if (data.size() > 0) {
            new InternalInvoiceReport(data);
        }
    }

    private void exportToXML() {
        if (data.size() > 0) {
            String dir = Dialogs.getDirectory(controller);
            if (dir != null) {
                String filename = DateUtils.getNormalDateTimeFormatPlus(Calendar.getInstance().getTime()) + ".xml";
                new InternalInvoiceToXML(data, dir, filename);
            }
        }
    }

    private void exportToDBF() {
        if (data.size() > 0) {
            new InternalInvoiceReport(data);
        }
    }


    private void initEvents() {

        gridViewPort.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onSelectChanged(final int rowIndex, final Object object) {

            }

            @Override
            public void onDoubleClick(final int rowIndex, int columnIndex, final Object object) {

            }
        });

        btnUpdate.addActionListener(a -> updateContent());
    }


    @Override
    public void updateContent() {

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {

                int deptSender = -1;
                int deptReceiver = -1;


                if (cbDeptSender.getSelectedIndex() > 0) {
                    deptSender = cbDeptSender.getItemAt(cbDeptSender.getSelectedIndex()).getId();
                }

                if (cbDeptReceiver.getSelectedIndex() > 0) {
                    deptReceiver = cbDeptReceiver.getItemAt(cbDeptReceiver.getSelectedIndex()).getId();
                }

                if (deptSender < 0 || deptReceiver < 0) {
                    return true;
                }

                DatePeriod period = datePeriodPicker.getTimeLimitPeriod();

                DaoFactory<VInternalInvoiceReport> factory = DaoFactory.getInstance();
                IGenericDao<VInternalInvoiceReport> dao = factory.getGenericDao();

                java.util.List<QueryProperty> criteria = new ArrayList<>();
                criteria.add(new QueryProperty("period_begin", period.getBegin()));
                criteria.add(new QueryProperty("period_end", period.getEnd()));
                criteria.add(new QueryProperty("deptSender", deptSender));
                criteria.add(new QueryProperty("deptReceiver", deptReceiver));

                java.util.List<VInternalInvoiceReport> list = null;
                data.clear();
                try {
                    list = dao.getEntityListByNamedQuery(VInternalInvoiceReport.class, "VInternalInvoiceReport.findByPeriodAndDepartments", criteria);
                    for (VInternalInvoiceReport item : list) {
                        item.setNormByItem(
                                service.getModelRationing(
                                        Integer.parseInt(item.getModelNumber())));
                    }
                    data.addAll(list);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }

        Task task = new Task("Получение данных...");
        task.executeTask();

        frameViewPort.updateContent();
        gridViewPort.updateViewPort();
        label.setText("Для отчета отобрано " + data.size() + " позиций");
    }


    @Override
    public void editRecord() {
        //entityViewPort.editRecord();
    }

    @Override
    public void deleteRecord() {
        //entityViewPort.deleteRecord();
    }

    @Override
    public void addRecord() {

    }
}
