package by.march8.ecs.application.modules.general.forms;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ButtonActionAdapter;
import by.gomel.freedev.ucframework.ucdao.model.CriteriaItem;
import by.gomel.freedev.ucframework.ucdao.model.QueryBuilder;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextFieldPanel;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.general.ContractorDAO;
import by.march8.ecs.application.modules.sales.dao.CommonFormatDBF;
import by.march8.ecs.application.modules.sales.manager.ContractorSelectorDialog;
import by.march8.ecs.application.modules.sales.manager.PreOrderSaleDocumentManager;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.ecs.framework.common.Settings;
import by.march8.ecs.framework.mail.SendEMail;
import by.march8.entities.contractor.ClientEmailItem;
import by.march8.entities.readonly.ContractorEntityView;
import by.march8.entities.sales.PreOrderSaleDocumentView;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SendEMailDialog extends BasePickDialog {

    private JPanel pContent;
    private JButton btnSaveMail;
    private JButton btnSendMail;
    private UCTextFieldPanel<String> tfpContractor;
    private ComboBoxPanel<ClientEmailItem> cbpContractorEmails;
    private QueryBuilder qbContractorEmails = new QueryBuilder(ClientEmailItem.class);
    private JPanel pContractor;

    private ContractorEntityView contractor;
    private PreOrderSaleDocumentManager manager;
    private PreOrderSaleDocumentView document;


    public SendEMailDialog(MainController controller) {
        super(controller);
        setTitle("Отправка документа по электронной почте");

        init();
        initEvents();
    }

    private void init() {
        setFrameSize(new Dimension(420, 250));
        getToolBar().setVisible(false);
        Dimension dimension = new Dimension(26, 26);

        btnSaveMail = new JButton("Сохранить");
        btnSaveMail.setToolTipText("Сохранить электронную накладную");

        btnSendMail = new JButton("Отправить");
        btnSendMail.setToolTipText("Отправить электронную накладную");

        pContent = new JPanel(new MigLayout());
        getButtonPanel().add(btnSaveMail, 0);
        getButtonPanel().add(btnSendMail, 1);

        getBtnSave().setVisible(false);

        tfpContractor = new UCTextFieldPanel<>(controller, MarchReferencesType.CONTRACTOR_OLD);

        cbpContractorEmails = new ComboBoxPanel<>(controller, MarchReferencesType.CLIENT_EMAIL, false);
        qbContractorEmails.addCriteria(new CriteriaItem(0, "clientCode", "="));
        qbContractorEmails.setOrderBy("email", "id");

        pContractor = new JPanel(new MigLayout());
        pContractor.add(new JLabel("Наименование"), "wrap");
        pContractor.add(tfpContractor, "height 20:20, width 360:20:360, wrap");
        pContractor.add(new JLabel("Электронный адрес"), "wrap");
        pContractor.add(cbpContractorEmails, "height 20:20, width 360:20:360, wrap");

        pContractor.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pContractor.setBorder(BorderFactory.createTitledBorder("Контрагент"));

        pContent.add(pContractor);
        getCenterContentPanel().add(pContent);

        manager = PreOrderSaleDocumentManager.getInstance(controller);
    }

    private void initEvents() {
        btnSaveMail.addActionListener(a -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fc.showSaveDialog(controller.getMainForm()) == JFileChooser.APPROVE_OPTION) {
                class Task extends BackgroundTask {
                    public Task(final String messageText) {
                        super(messageText);
                    }

                    @Override
                    protected Boolean doInBackground() throws Exception {
                        SaleDocumentReport report = manager.prepareDataForReport(document.getId());
                        if (report != null) {
                            prepareDBF(new File(fc.getSelectedFile().getPath() + File.separator), report);
                        }
                        return true;
                    }
                }

                Task task = new Task("Сохранение электронной накладной...");
                task.executeTask();
            }
        });

        btnSendMail.addActionListener(a -> {
            ClientEmailItem selectedItem = cbpContractorEmails.getSelectedItem();
            if (selectedItem != null) {
                //gvItem.selectAll(true);
                if (document != null) {
                    class Task extends BackgroundTask {
                        public Task(final String messageText) {
                            super(messageText);
                        }

                        @Override
                        protected Boolean doInBackground() throws Exception {
                            SaleDocumentReport report = manager.prepareDataForReport(document.getId());
                            if (report != null) {
                                String file = prepareDBF(new File(Settings.TEMPORARY_DIR), report);
                                if (file != null) {
                                    SendEMail sender = new SendEMail("Электронная накладная " + report.getDocument().getDocumentNumber() + " - ОАО \"8 Марта\"");
                                    sender.getAddressList().add(selectedItem.getEmail());
                                    sender.getAttachList().add(new File(file));
                                    sender.send();
                                    Dialogs.showInformationDialog("Документ электронной накладной отправлен");
                                }
                            }
                            return true;
                        }
                    }

                    Task task = new Task("Отправка электронной накладной...");
                    task.executeTask();
                }
            } else {
                Dialogs.showInformationDialog("Необходимо выбрать адрес электронной почты.");
            }
        });


        setButtonAction(new ButtonActionAdapter() {
            @Override
            public boolean canSave() {
                return false;
            }

            @Override
            public boolean canCancel() {
                return true;
            }
        });

        tfpContractor.addButtonSelectActionListener(e -> {
            ContractorSelectorDialog dialog = new ContractorSelectorDialog(controller);
            ContractorEntityView select_ = dialog.selectContractor();
            if (select_ != null) {
                ContractorEntityView contractor = ContractorDAO.getContractorViewById(select_.getId());
                if (contractor != null) {
                    tfpContractor.setText(contractor.getName());
                    loadEmailsForContractor(contractor.getCode());
                }
            }
        });
    }

    private String prepareDBF(File path, SaleDocumentReport report) {
        if (report != null) {
            //new ProtocolBelPostReport(report);
            CommonFormatDBF dbf = new CommonFormatDBF(path.getAbsolutePath(), report.getDocument().getDocumentNumber());
            dbf.connect();
            dbf.addItem(report);
            dbf.disconnect();
            return dbf.getFilePath();
        }
        return null;
    }

    public boolean showDialog(PreOrderSaleDocumentView document) {
        this.document = document;

        tfpContractor.setText(document.getContractorName());
        loadEmailsForContractor(document.getContractorCode());

        setTitle("Отправка документа [" + document.getDocumentNumber() + "] по электронной почте");

        return showModalFrame();
    }

    private void loadEmailsForContractor(int contractorCode) {
        qbContractorEmails.updateCriteria("clientCode", contractorCode);
        cbpContractorEmails.setEntityParentId(contractorCode);
        cbpContractorEmails.loadEntityListByQuery(qbContractorEmails.getQuery());
    }
}