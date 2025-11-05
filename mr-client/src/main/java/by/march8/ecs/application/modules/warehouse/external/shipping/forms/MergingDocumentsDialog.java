package by.march8.ecs.application.modules.warehouse.external.shipping.forms;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextFieldPanel;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.general.ContractorDAO;
import by.march8.ecs.application.modules.sales.manager.ContractorSelectorDialog;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.SaleDocumentJDBC;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.MergingDocumentsProcessor;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.readonly.ContractorEntityView;
import by.march8.entities.sales.SalesDocumentSimple;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Andy 06.09.2018 - 7:23.
 */
public class MergingDocumentsDialog extends BasePickDialog {

    private MainController controller;
    private UCDatePeriodPicker datePeriodPicker;
    private UCTextFieldPanel<String> tfpContractorRefund;
    private UCTextFieldPanel<String> tfpContractorSale;
    private UCTextField tfContractorCodeRefund;
    private UCTextField tfContractorCodeSale;

    private GridViewPort<SalesDocumentSimple> gvItems;
    private List<SalesDocumentSimple> dataList;
    private SaleDocumentJDBC db;

    private ContractorEntityView contractorRefund;
    private ContractorEntityView contractorSale;


    public MergingDocumentsDialog(MainController controller) {
        super(controller, true);
        this.controller = controller;
        initComponents();
        datePeriodPicker.preparePeriodsLimits();
    }

    public void initComponents() {
        setFrameSize(new Dimension(450, 500));
        getToolBar().setVisible(false);
        setTitle("Объединение документов с разбивкой по категориям");
        getBtnSave().setVisible(false);
        getBtnCancel().setVisible(false);

        JButton btnTest = new JButton("Сформировать документы");
        getButtonPanel().add(btnTest);

        Container pHeader = getTopContentPanel();

        tfContractorCodeRefund = new UCTextField();
        tfContractorCodeRefund.setComponentParams(null, Integer.class, 10);
        tfContractorCodeRefund.setEnabled(false);

        tfContractorCodeSale = new UCTextField();
        tfContractorCodeSale.setComponentParams(null, Integer.class, 10);
        tfContractorCodeSale.setEnabled(false);

        datePeriodPicker = new UCDatePeriodPicker();
        datePeriodPicker.setName("datePeriodPicker");
        datePeriodPicker.setDatePickerEnd(new Date());
        datePeriodPicker.setDatePickerBegin(DateUtils.getFirstDay(datePeriodPicker.getDatePickerEnd()));
        datePeriodPicker.setEditable(true);

        //pHeader = new JPanel(new MigLayout());
        pHeader.setLayout(new MigLayout());
        pHeader.add(new JLabel("Критерии отбора возвратов"));
        pHeader.add(new JLabel(""), "wrap");
        pHeader.add(new JLabel("Период :"));
        pHeader.add(datePeriodPicker, "width 300:20:300, height 50:50, wrap");

        pHeader.add(new JLabel("Возврат : "));
        tfpContractorRefund = new UCTextFieldPanel<>(controller, MarchReferencesType.CONTRACTOR_OLD);
        pHeader.add(tfpContractorRefund, "width 300:20:300, height 20:20, wrap");

        pHeader.add(new JLabel("Код : "));
        pHeader.add(tfContractorCodeRefund, "width 100:20:100, height 20:20, wrap");

        pHeader.add(new JLabel("Продажа : "));
        tfpContractorSale = new UCTextFieldPanel<>(controller, MarchReferencesType.CONTRACTOR_OLD);
        pHeader.add(tfpContractorSale, "width 300:20:300, height 20:20, wrap");

        pHeader.add(new JLabel("Код : "));
        pHeader.add(tfContractorCodeSale, "width 100:20:100, height 20:20, wrap");

        datePeriodPicker.addOnChangeAction(a -> {
            if (contractorRefund != null) {
                getSaleDocumentsForContractor(contractorRefund.getCode());
            }
        });

        tfpContractorRefund.addButtonSelectActionListener(e -> {
            ContractorSelectorDialog dialog = new ContractorSelectorDialog(controller);
            ContractorEntityView select_ = dialog.selectContractor();
            if (select_ != null) {
                ContractorEntityView contractor = ContractorDAO.getContractorViewById(select_.getId());
                if (contractor != null) {
                    contractorRefund = contractor;

                    tfpContractorRefund.setText(contractor.getName());
                    tfContractorCodeRefund.setText(String.valueOf(contractor.getCode()));

                    getSaleDocumentsForContractor(contractor.getCode());
                }
            }
        });

        tfpContractorSale.addButtonSelectActionListener(e -> {
            ContractorSelectorDialog dialog = new ContractorSelectorDialog(controller);
            ContractorEntityView select_ = dialog.selectContractor();
            if (select_ != null) {
                ContractorEntityView contractor = ContractorDAO.getContractorViewById(select_.getId());
                if (contractor != null) {
                    contractorSale = contractor;

                    tfpContractorSale.setText(contractor.getName());
                    tfContractorCodeSale.setText(String.valueOf(contractor.getCode()));
                }
            }
        });

        gvItems = new GridViewPort<>(SalesDocumentSimple.class, true);
        dataList = gvItems.getDataModel();
        getCenterContentPanel().add(gvItems);

        btnTest.addActionListener(a -> {
            if (contractorRefund == null) {
                JOptionPane.showMessageDialog(null,
                        "Необходимо указать контрагента для накладных возврата ", "Ошибка ввода",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (contractorSale == null) {
                JOptionPane.showMessageDialog(null,
                        "Необходимо указать контрагента для накладных продажи", "Ошибка ввода",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            Set<SalesDocumentSimple> list = gvItems.getSelectedItems();
            if (list != null) {
                if (list.size() > 0) {
                    MergingDocumentsProcessor processor = new MergingDocumentsProcessor();
                    processor.doMergingDocuments(list, contractorRefund, contractorSale);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Не выбрана ни одна возвратная накладая!", "Ошибка ввода",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Не выбрана ни одна возвратная накладая!", "Ошибка ввода",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        db = new SaleDocumentJDBC();
    }

    private void getSaleDocumentsForContractor(int code) {
        dataList.clear();
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                dataList.addAll(db.getSalesDocumentSimpleForContractor(datePeriodPicker.getTimeLimitPeriod(), code));
                return true;
            }
        }
        Task task = new Task("Получение документов...");
        task.executeTask();
        gvItems.updateViewPort();
    }

    public boolean showDialog() {
        return showModalFrame();
    }

}

