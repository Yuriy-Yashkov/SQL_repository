package by.march8.ecs.application.modules.warehouse.external.shipping.forms;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDaoThread;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.DAOSaleDocumentFactory;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.interfaces.ISaleDocumentDao;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentManager;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.planning.ProductionPlanningEntity;
import by.march8.entities.warehouse.SaleDocument;
import by.march8.entities.warehouse.SaleDocumentEntity;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @author Andy 06.09.2018 - 7:23.
 */
public class ExitTradeDialog extends BasePickDialog {

    private JComboBox<SaleDocumentEntity> cbSaleDocument;
    private JComboBox<SaleDocumentEntity> cbRefundDocument;
    private JTextField tfResultDocumentNumber = new JTextField();

    private ProductionPlanningEntity source = null;

    private MainController controller;
    private UCDatePeriodPicker datePeriodPicker;
    private UCTextField tfContractor;
    private int contractorCode;


    public ExitTradeDialog(MainController controller, UCDatePeriodPicker periodPicker, int contractorCode) {
        super(controller, true);
        datePeriodPicker = periodPicker;

        datePeriodPicker.preparePeriodsLimits();
        this.contractorCode = contractorCode;

        initComponents();


        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                loadDocuments();
                return true;
            }
        }

        Task task = new Task("Получение документов ...");
        task.executeTask();
    }

    public void initComponents() {
        setFrameSize(new Dimension(450, 250));
        getToolBar().setVisible(false);
        setTitle("Формирование документа по итогам выездной торговли");

        Container panel = getCenterContentPanel();
        panel.setLayout(new MigLayout());

        tfContractor = new UCTextField();
        tfContractor.setComponentParams(null, Integer.class, 4);
        tfContractor.setEditable(false);
        tfContractor.setText(String.valueOf(contractorCode));

        cbSaleDocument = new JComboBox<>();
        cbRefundDocument = new JComboBox<>();
        tfResultDocumentNumber = new JTextField();

        panel.add(new JLabel("Код контрагента"), "width 200:20:200");
        panel.add(tfContractor, "width 200:20:200, height 20:20, wrap");
        panel.add(new JPanel(), "height 10:10,  wrap");
        panel.add(new JLabel("Документ отгрузки ВТ"), "width 200:20:200");
        panel.add(cbSaleDocument, "width 200:20:200, height 20:20, wrap");
        panel.add(new JLabel("Документ возврата ВТ"), "width 200:20:200");
        panel.add(cbRefundDocument, "width 200:20:200, height 20:20, wrap");
        panel.add(new JPanel(), "height 10:10,  wrap");
        panel.add(new JLabel("Документ реализации"), "width 200:20:200");
        panel.add(tfResultDocumentNumber, "width 200:20:200, height 20:20, wrap");

        panel.add(new JLabel("<html><font color=\"green\">После обработки, сформированный документ ВВХХХХХХХ необходимо рассчитать</font></html>"), "span 2");

        getBtnSave().setText("Создать");
        getBtnCancel().setText("Закрыть");

        getBtnSave().addActionListener(a -> {
            createExitTradeSaleDocument();
        });


        cbSaleDocument.addActionListener(a -> {
            SaleDocumentEntity eSaleDocument = (SaleDocumentEntity) cbSaleDocument.getSelectedItem();
            if (eSaleDocument != null) {
                String docNumber = eSaleDocument.getDocumentNumber();
                if (docNumber.length() > 6) {
                    String s = "ВВ" + docNumber.substring(2, docNumber.length());
                    tfResultDocumentNumber.setText(s);
                }
            }
        });
    }

    private void createExitTradeSaleDocument() {
        if (!tfResultDocumentNumber.getText().trim().equals("")) {
            if (deleteOldSaleDocument(tfResultDocumentNumber.getText().trim())) {
                SaleDocumentEntity eSaleDocument = (SaleDocumentEntity) cbSaleDocument.getSelectedItem();
                SaleDocumentEntity eRefundDocument = (SaleDocumentEntity) cbRefundDocument.getSelectedItem();
                if (eSaleDocument != null && eRefundDocument != null) {
                    // Документ  удален успешно, начинаем формировать доумент реализации
                    SaleDocumentManager manager = new SaleDocumentManager();
                    // Получает документ отгрузки ВТ
                    SaleDocument documentSale = manager.getSaleDocumentById(eSaleDocument.getId());
                    if (documentSale != null) {
                        // Получаем документ возврата ВТ
                        SaleDocument documentRefund = manager.getSaleDocumentById(eRefundDocument.getId());
                        if (documentRefund != null) {
                            // Формируем документ разности изделий
                            SaleDocument documentDiff = manager.createDifferentSaleDocument(documentSale, documentRefund);
                            documentDiff.setDocumentNumber(tfResultDocumentNumber.getText().trim());
                            // Сохраняем документ разности
                            saveDocument(documentDiff);

                            setVisible(false);

                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Документ возврата не найден в базе данных", "Ошибка!!!",
                                    JOptionPane.ERROR_MESSAGE);
                            cbRefundDocument.requestFocusInWindow();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Документ отгрузки не найден в базе данных", "Ошибка!!!",
                                JOptionPane.ERROR_MESSAGE);
                        cbSaleDocument.requestFocusInWindow();
                    }
                } else {
                    if (eSaleDocument == null) {
                        JOptionPane.showMessageDialog(null,
                                "Документ отгрузки указан не верно", "Ошибка!!!",
                                JOptionPane.ERROR_MESSAGE);
                        cbSaleDocument.requestFocusInWindow();
                    }

                    if (eRefundDocument == null) {
                        JOptionPane.showMessageDialog(null,
                                "Документ возврата указан не верно", "Ошибка!!!",
                                JOptionPane.ERROR_MESSAGE);
                        cbRefundDocument.requestFocusInWindow();
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "Документ реализации указан не верно", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            cbSaleDocument.requestFocusInWindow();
        }
    }


    private void saveDocument(SaleDocument document) {
        // Сохраним изменения в базу после расчета документа
        DaoFactory factory = DaoFactory.getInstance();
        ICommonDaoThread dao = factory.getCommonDaoThread();
        // Обновляем документ в базе
        try {
            dao.updateEntityThread(document);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Object showDialog() {
        if (showModalFrame()) {
            return null;
        } else {
            return null;
        }
    }

    private void loadDocuments() {
        List<SaleDocumentEntity> docList = getSaleDocumentListByPeriodAndContractor(contractorCode, datePeriodPicker.getDatePickerBegin(), datePeriodPicker.getDatePickerEnd());
        cbSaleDocument.setModel(new DefaultComboBoxModel(docList.toArray()));
        cbSaleDocument.setSelectedIndex(-1);

        cbRefundDocument.setModel(new DefaultComboBoxModel(docList.toArray()));
        cbRefundDocument.setSelectedIndex(-1);
    }

    private List<SaleDocumentEntity> getSaleDocumentListByPeriodAndContractor(int contractorCode, Date dateBegin, Date dateEnd) {
        java.util.List<SaleDocumentEntity> result = null;
        DAOSaleDocumentFactory factory = DAOSaleDocumentFactory.getInstance();
        ISaleDocumentDao dao = factory.getSaleDocumentDao();
        try {
            result = dao.getAllSaleDocumentByPeriodAndContractor(0, dateBegin, dateEnd, contractorCode);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean deleteOldSaleDocument(String documentNumber) {
        // Ищем документ по номеру
        DAOSaleDocumentFactory factory = DAOSaleDocumentFactory.getInstance();
        ISaleDocumentDao dao = factory.getSaleDocumentDao();
        int documentId = dao.getOpenedSaleDocumentEntityByNumber(documentNumber);
        if (documentId != 0) {
            System.out.println("Документ найден и удален");
            //Документ существует, удаляем
            DaoFactory factory_ = DaoFactory.getInstance();
            ICommonDao dao_ = factory_.getCommonDao();
            try {
                dao_.deleteEntity(SaleDocumentEntity.class, documentId);
            } catch (SQLException e) {
                MainController.exception(e, "Ошибка удаления документа с ID [" + documentId + "]");
                return false;
            }
            return true;
        } else {
            System.out.println("Документ не найден");
        }

        return true;
    }
}

