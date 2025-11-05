package dept.ves.mode;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ButtonActionAdapter;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.VolumeOfPurchasesReport;
import by.march8.ecs.framework.common.BackgroundTask;
import dept.ves.dao.ExternalEconomicRelationsJDBC;
import dept.ves.model.VolumeOfPurchases;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Andy on 02.03.2020 7:48
 */
public class VolumeOfPurchasesDialog extends BasePickDialog {

    private JRadioButton rbFlexiblePeriod;
    private JRadioButton rbFixedPeriod;

    private JButton btnCreate;

    private UCDatePeriodPicker datePeriodPicker;

    private UCTextField tfPeriodDecrement;

    private GridViewPort<VolumeOfPurchases> gvList;
    private List<VolumeOfPurchases> list;

    private ExternalEconomicRelationsJDBC db;
    private JButton btnExportDocument;


    public VolumeOfPurchasesDialog(MainController controller) {
        super(controller);
        getBtnSave().setVisible(false);
        getBtnCancel().setText("Закрыть");
        setTitle("Объем заказов на внешнем рынке сбыта предприятия");
        initComponents();
        initEvents();
    }


    private void initEvents() {
        rbFlexiblePeriod.addActionListener(a -> {
            if (rbFlexiblePeriod.isSelected()) {
                tfPeriodDecrement.setVisible(true);
                tfPeriodDecrement.setText("30");
                datePeriodPicker.setVisible(false);
            } else {
                tfPeriodDecrement.setVisible(false);
                tfPeriodDecrement.setText("30");
                datePeriodPicker.setVisible(true);
            }
            list.clear();
            btnExportDocument.setEnabled(false);
        });

        rbFixedPeriod.addActionListener(a -> {
            if (rbFixedPeriod.isSelected()) {
                tfPeriodDecrement.setVisible(false);
                datePeriodPicker.setVisible(true);
            } else {
                tfPeriodDecrement.setVisible(true);
                datePeriodPicker.setVisible(false);
            }

            list.clear();
            btnExportDocument.setEnabled(false);
        });

        btnCreate.addActionListener(a -> {
            updateContext();
        });

        btnExportDocument.addActionListener(a -> {
            exportToExcel();
        });

        datePeriodPicker.addOnChangeAction(a -> {
            list.clear();
            btnExportDocument.setEnabled(false);
        });
    }

    private void initComponents() {

        setFrameSize(new Dimension(600, 580));

        gvList = new GridViewPort<>(VolumeOfPurchases.class, false);
        list = gvList.getDataModel();
        gvList.setCustomCellRender(new VolumeOfPurchasesCellRender());

        getToolBar().getBtnNewItem().setVisible(false);
        getToolBar().getBtnEditItem().setVisible(false);
        getToolBar().getBtnDeleteItem().setVisible(false);


        btnExportDocument = new JButton();
        btnExportDocument.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/save_24.png", "Обновить данные"));
        btnExportDocument.setToolTipText("Выгрузить каталог");

        getToolBar().add(btnExportDocument);


        rbFlexiblePeriod = new JRadioButton("Плавающий период");
        rbFixedPeriod = new JRadioButton("Фиксированный период");

        tfPeriodDecrement = new UCTextField("30");
        tfPeriodDecrement.setComponentParams(null, Integer.class, 0);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(rbFixedPeriod);
        buttonGroup.add(rbFlexiblePeriod);

        rbFlexiblePeriod.setSelected(true);

        datePeriodPicker = new UCDatePeriodPicker();
        datePeriodPicker.setName("datePeriodPicker");
        datePeriodPicker.setDatePickerBegin(DateUtils.getFirstDay(datePeriodPicker.getDatePickerEnd()));
        datePeriodPicker.setEditable(true);

        JPanel pContent = new JPanel(new MigLayout());
        pContent.add(rbFlexiblePeriod);
        pContent.add(tfPeriodDecrement, "height 20:20, width 50:20:50, wrap");
        pContent.add(rbFixedPeriod);
        pContent.add(datePeriodPicker, "wrap");

        pContent.add(gvList, "span 3,height 350:350, width 560:20:560, wrap");

        getCenterContentPanel().add(pContent);
        setButtonAction(new ButtonActionAdapter() {
            @Override
            public boolean canSave() {
                return true;
            }

            @Override
            public boolean canCancel() {
                return true;
            }
        });

        btnCreate = new JButton("Получить данные");

        getButtonPanel().add(btnCreate, 0);

        datePeriodPicker.setVisible(false);
        btnExportDocument.setEnabled(false);

        db = new ExternalEconomicRelationsJDBC();
    }

    public boolean showDialog() {
        showModalFrame();
        return true;
    }

    private void updateContext() {

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                list.clear();
                try {
                    if (rbFixedPeriod.isSelected()) {
                        list.addAll(db.getVolumeOfPurchasesByPeriod(datePeriodPicker.getTimeLimitPeriod()));
                    } else {
                        list.addAll(db.getVolumeOfPurchasesByFlexiblePeriod(Integer.valueOf(tfPeriodDecrement.getText())));
                    }

                    if (list.isEmpty()) {
                        btnExportDocument.setEnabled(false);
                    } else {
                        btnExportDocument.setEnabled(true);
                    }
                } catch (ArithmeticException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Номер документа не является числом ", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                gvList.updateViewPort();
                return true;
            }
        }
        Task task = new Task("Получение данных...");
        task.executeTask();
    }

    private void exportToExcel() {
        if (!list.isEmpty()) {
            String text = "Итоговая сумма заказов на внешнем рынке РБ по контрагентам за плавающий период " + tfPeriodDecrement.getText() + " дней.";
            if (rbFixedPeriod.isSelected()) {
                text = "Итоговая сумма заказов на внешнем рынке РБ по контрагентам за период \nс "
                        + DateUtils.getNormalDateFormat(datePeriodPicker.getDatePickerBegin()) + " по "
                        + DateUtils.getNormalDateFormat(datePeriodPicker.getDatePickerEnd()) + "гг.";
            }
            new VolumeOfPurchasesReport(list, text);
        }
    }
}
