package by.march8.ecs.application.modules.art.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.api.utils.CalendarUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.art.dbf.ProductionReportDBF;
import by.march8.ecs.application.modules.art.model.ProductionReportData;
import by.march8.ecs.application.modules.art.model.ProductionReportItem;
import by.march8.ecs.application.modules.art.model.ProductionReportTotal;
import by.march8.ecs.application.modules.art.report.ProductionReport;
import by.march8.ecs.application.modules.art.report.SecondProductionReport;
import by.march8.ecs.framework.common.BackgroundTask;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * @author Andy 20.01.2017.
 */
public class ProductionReportMode extends AbstractFunctionalMode {

    private RightEnum right;
    // private EntityViewPort entityViewPort;
    private EditingPane editingPane;

    private JButton btnDetail = new JButton();

    private ArrayList<Object> data;

    private UCDatePeriodPicker datePeriodPicker;

    private JComboBox<String> cbMonths = new JComboBox<String>(CalendarUtils.MONTHS);

    private TableRowSorter<TableModel> sorter;
    private JButton btnUpdate = new JButton();
    private JButton btnExport = new JButton();
    private JButton btnCeatingReport = new JButton();

    private ProductionReportDBF reportDBF;
    private ProductionReportData reportData = null;

    public ProductionReportMode(final MainController mainController) {
        controller = mainController;
        modeName = "Производственный отчет отдела УМиРА";

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
        toolBar.getBtnViewItem().setVisible(false);

        // Комбик выбора типа документа
        JPanel pSearchPanel = new JPanel(null);
        JLabel lblPeriod = new JLabel("Период ");
        lblPeriod.setBounds(5, 4, 80, 20);


        pSearchPanel.add(lblPeriod);
        cbMonths.setBounds(80, 4, 180, 20);
        pSearchPanel.add(cbMonths);
        pSearchPanel.setPreferredSize(new Dimension(260, 28));
        pSearchPanel.setOpaque(false);
        toolBar.add(pSearchPanel);

        btnUpdate.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/update_data.png", "Обновить данные"));
        btnUpdate.setToolTipText("Обновить данные");

        btnExport.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/excel_24.png", "Сформировать отчет"));
        btnExport.setToolTipText("Сформировать отчет");
        btnExport.setEnabled(false);

        btnCeatingReport.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/excel_24.png", "Сформировать новый отчет"));
        btnCeatingReport.setToolTipText("Сформировать отчет на основе 2х файлов");

        toolBar.add(btnUpdate);
        toolBar.add(btnExport);
        // todo заготовка для отчета по НЗП, не готова, надо узнать откуда вытащить данные.
        //toolBar.add(btnCeatingReport);
        toolBar.add(new Box(BoxLayout.X_AXIS));
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        //toolBar.setPreferredSize(new Dimension(0, 47));


        gridViewPort = new GridViewPort(ProductionReportItem.class, false);
        gridViewPort.setCustomCellRender(new ProductionReportCellRenderer());
        data = gridViewPort.getDataModel();
        frameViewPort.setGridViewPort(gridViewPort);


        //initEvents();

        gridViewPort.initialFooter();
        gridViewPort.getFooterPanel().setPreferredSize(new Dimension(0, 40));


        //Инициализация панели редактирования

        reportDBF = new ProductionReportDBF();

        initEvents();

        //updateContent();
        gridViewPort.primaryInitialization();
        //gridViewPort.getTable().setColumnSelectionAllowed(false);
        //gridViewPort.getTable().setRowSelectionAllowed(true);
        frameViewPort.getFrameControl().showFrame();
    }

    private void initEvents() {
        btnUpdate.addActionListener(a -> {
            if (!reportDBF.check()) {
                Dialogs.showInformationDialog("Нет доступа к каталогу с DBF файлами!\n" + reportDBF.getEnvironmentFiles());
                return;
            }


            if (cbMonths.getSelectedIndex() > 0) {
                class Task extends BackgroundTask {
                    public Task(final String messageText) {
                        super(messageText);
                    }

                    @Override
                    protected Boolean doInBackground() throws Exception {
                        updateContent();
                        return true;
                    }
                }
                Task task = new Task("Обработка DBF файлов...");
                task.executeTask();

            } else {
                Dialogs.showInformationDialog("Укажите необходимый период и повторите запрос");
            }
        });

        btnExport.addActionListener(e -> {
            createReport();
        });

        btnCeatingReport.addActionListener(e -> {
            createSecondReport();
        });

        gridViewPort.getTable().
                addMouseListener(new MouseAdapter() {
                                     @Override
                                     public void mouseClicked(MouseEvent e) {
                                         if (e.isPopupTrigger()) {
                                             Point point = e.getPoint();
                                             int column = gridViewPort.getTable().columnAtPoint(point);
                                             int row = gridViewPort.getTable().rowAtPoint(point);

                                             // выполняем проверку
                                             if (column != -1 && row != -1) {
                                                 gridViewPort.getTable().setColumnSelectionInterval(column, column);
                                                 gridViewPort.getTable().setRowSelectionInterval(row, row);
                                             }
                                         }
                                     }

                                     @Override
                                     public void mousePressed(MouseEvent e) {
                                         if (e.isPopupTrigger()) {
                                             Point point = e.getPoint();
                                             int column = gridViewPort.getTable().columnAtPoint(point);
                                             int row = gridViewPort.getTable().rowAtPoint(point);

                                             // выполняем проверку
                                             if (column != -1 && row != -1) {
                                                 gridViewPort.getTable().setColumnSelectionInterval(column, column);
                                                 gridViewPort.getTable().setRowSelectionInterval(row, row);
                                             }
                                         }
                                     }

                                     @Override
                                     public void mouseReleased(MouseEvent e) {
                                         if (e.isPopupTrigger()) {
                                             Point point = e.getPoint();
                                             int column = gridViewPort.getTable().columnAtPoint(point);
                                             int row = gridViewPort.getTable().rowAtPoint(point);

                                             // выполняем проверку
                                             if (column != -1 && row != -1) {
                                                 gridViewPort.getTable().setColumnSelectionInterval(column, column);
                                                 gridViewPort.getTable().setRowSelectionInterval(row, row);
                                             }
                                         }
                                     }
                                 }

                );
    }

    private void createReport() {
        // System.out.println(((JTextComponent)cbMonths.getEditor().getEditorComponent()).getText());
        new ProductionReport(reportData, cbMonths.getSelectedIndex());
    }

    private void createSecondReport() {
        // System.out.println(((JTextComponent)cbMonths.getEditor().getEditorComponent()).getText());
        ProductionReportData repData = reportDBF.getNZPItemList(cbMonths.getSelectedIndex());
        new SecondProductionReport(repData, cbMonths.getSelectedIndex());
    }

    private void getNZP() {

    }

    @Override
    public void updateContent() {
        reportData = reportDBF.getProductReportItemList(cbMonths.getSelectedIndex());
        data.clear();
        data.addAll(reportData.getData());
        ProductionReportTotal total = reportData.getTotal();

        gridViewPort.getFooterTextComponent().setText("<html><div style=\"text-align: right;\"> Остаток на начало периода : " + "<font color=\"blue\">" + total.getRemainsBeginPeriod() + "</font><br>" +
                "Остаток на конец периода : " + "<font color=\"blue\">" + total.getRemainsEndPeriod() + "</font>"
                + "</html>");

        frameViewPort.updateContent();
        btnExport.setEnabled(true);
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
}
