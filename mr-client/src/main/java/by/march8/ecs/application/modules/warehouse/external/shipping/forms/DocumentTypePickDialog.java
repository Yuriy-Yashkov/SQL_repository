package by.march8.ecs.application.modules.warehouse.external.shipping.forms;

import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.DocumentTypePreset;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.FinishedGoodsHelper;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.PriceAgreementProtocol;
import by.march8.ecs.framework.common.Settings;
import by.march8.entities.warehouse.SaleDocumentBase;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author Andy 25.01.2016.
 */
public class DocumentTypePickDialog extends BasePickDialog {

    private JCheckBox chbInvoiceSelector;
    private ButtonGroup bgInvoiceType;
    private JRadioButton rbSelectorTTN1;
    private JRadioButton rbSelectorTN2;
    private JRadioButton rbSelectorAdjustmentDocument;
    private JCheckBox cbAsService;
    private JPanel pInvoiceType;

    private JCheckBox chbAnnexToInvoice;
    private JCheckBox chbAnnexToInvoiceVat;
    private JCheckBox chbAnnexSimple;
    private JCheckBox chbReferenceTTN;
    private JCheckBox chbInvoiceTTN;

    private JCheckBox chbPriceAgreement;
    private JPanel pPriceAgreement;
    private JComboBox<PriceAgreementProtocol> cbPriceAgreement;
    private DocumentTypePreset documentPreset;
    private SaleDocumentBase documentBase;


    public DocumentTypePickDialog(final MainController controller, SaleDocumentBase documentBase) {

        super(controller);
        this.documentBase = documentBase;
        initComponents();
    }

    private void initComponents() {

        setFrameSize(new Dimension(370, 374));

        Container panel = getCenterContentPanel();
        panel.setLayout(new MigLayout());

        chbInvoiceSelector = new JCheckBox("Товарная накладная");
        cbAsService = new JCheckBox("Услуги крашения");
        cbAsService.setForeground(Color.blue);

        panel.add(chbInvoiceSelector);
        panel.add(cbAsService, " wrap");

        pInvoiceType = new JPanel(null);
        pInvoiceType.setPreferredSize(new Dimension(300, 72));

        rbSelectorTTN1 = new JRadioButton("ТТН - 1");
        rbSelectorTN2 = new JRadioButton("ТН - 2");
        rbSelectorAdjustmentDocument = new JRadioButton("Корректировочный акт");
        rbSelectorAdjustmentDocument.setForeground(Color.red);

        bgInvoiceType = new ButtonGroup();
        bgInvoiceType.add(rbSelectorTTN1);
        bgInvoiceType.add(rbSelectorTN2);
        bgInvoiceType.add(rbSelectorAdjustmentDocument);
        if (documentBase.getAdjustmentType() == 1) {
            rbSelectorAdjustmentDocument.setVisible(true);
        } else {
            rbSelectorAdjustmentDocument.setVisible(false);
        }

        rbSelectorTTN1.setSelected(true);

        rbSelectorTTN1.setBounds(30, 0, 150, 23);
        rbSelectorTN2.setBounds(30, 24, 150, 23);
        rbSelectorAdjustmentDocument.setBounds(30, 48, 220, 23);

        pInvoiceType.add(rbSelectorTTN1);
        pInvoiceType.add(rbSelectorTN2);
        pInvoiceType.add(rbSelectorAdjustmentDocument);


        panel.add(pInvoiceType, "wrap");

        chbAnnexToInvoice = new JCheckBox("Приложение к накладной");
        chbAnnexToInvoiceVat = new JCheckBox("Доп. приложение с НДС");
        chbAnnexSimple = new JCheckBox("Прейскурант ");

        panel.add(chbAnnexToInvoice, "");
        panel.add(chbAnnexSimple, "wrap");

        // panel.add(chbAnnexToInvoiceVat, "wrap");

        chbReferenceTTN = new JCheckBox("Справки к накладной");
        panel.add(chbReferenceTTN, "wrap");

        chbPriceAgreement = new JCheckBox("Протокол согласования цен");
        panel.add(chbPriceAgreement, "wrap");

        chbInvoiceTTN = new JCheckBox("Счет-фактура");


        pPriceAgreement = new JPanel(null);
        pPriceAgreement.setPreferredSize(new Dimension(370, 25));

        cbPriceAgreement = new JComboBox<PriceAgreementProtocol>();
        cbPriceAgreement.setBounds(30, 0, 180, 20);
        FinishedGoodsHelper.fillingPriceAgreementProtocol(cbPriceAgreement);

        cbPriceAgreement.setSelectedIndex(1);

        pPriceAgreement.add(cbPriceAgreement);

        panel.add(pPriceAgreement, "wrap");

        panel.add(chbInvoiceTTN, "wrap");

        getToolBar().setVisible(false);

        //getCenterContentPanel().add(calendar);
        chbInvoiceSelector.setSelected(true);
        chbAnnexToInvoice.setSelected(true);
        chbAnnexToInvoiceVat.setSelected(false);
        chbAnnexSimple.setSelected(false);
        chbReferenceTTN.setSelected(false);
        chbPriceAgreement.setSelected(true);

        chbInvoiceTTN.setSelected(false);

        getBtnSave().setText("Сформировать");
        getBtnSave().setPreferredSize(Settings.BUTTON_HALF_BIG_SIZE);
        initEvents();
    }

    private void initEvents() {
        chbInvoiceSelector.addActionListener(e -> {
            rbSelectorTTN1.setEnabled(chbInvoiceSelector.isSelected());
            rbSelectorTN2.setEnabled(chbInvoiceSelector.isSelected());
            rbSelectorAdjustmentDocument.setEnabled(chbInvoiceSelector.isSelected());
        });

        chbPriceAgreement.addActionListener(e -> {
            cbPriceAgreement.setEnabled(chbPriceAgreement.isSelected());
        });

        chbAnnexToInvoice.addActionListener(e -> {
            chbAnnexSimple.setEnabled(chbAnnexToInvoice.isSelected());
        });

        cbPriceAgreement.addActionListener(e -> documentPreset.setProtocol(
                cbPriceAgreement.getItemAt(cbPriceAgreement.getSelectedIndex())));
    }


    /**
     * Метод отображает форму и согласно документа инициализирует  элементы управления
     *
     * @param documentPreset обрабатываемый документ
     * @return предустановка для обработки документа
     */
    public DocumentTypePreset showDialog(DocumentTypePreset documentPreset, boolean tradeMaterial) {
        this.documentPreset = documentPreset;
        setTitle("Формирование сопроводительных документов");
        preset(documentPreset);

        cbAsService.setVisible(tradeMaterial);

        if (showModalFrame()) {
            if (chbInvoiceSelector.isSelected()) {
                if (rbSelectorTTN1.isSelected()) {
                    documentPreset.setInvoiceType(1);
                }
                if (rbSelectorTN2.isSelected()) {
                    documentPreset.setInvoiceType(2);
                }
                if (rbSelectorAdjustmentDocument.isSelected()) {
                    documentPreset.setInvoiceType(3);
                }
            } else {
                documentPreset.setInvoiceType(0);
            }

            documentPreset.setAnnexToInvoice(chbAnnexToInvoice.isSelected());

            documentPreset.setAnnexToInvoiceVat(chbAnnexToInvoiceVat.isSelected());

            documentPreset.setAnnexSimple(chbAnnexSimple.isSelected());

            documentPreset.setReferenceToInvoice(chbReferenceTTN.isSelected());

            documentPreset.setPriceAgreement(chbPriceAgreement.isSelected());

            documentPreset.setAsService(cbAsService.isSelected());

            documentPreset.setInvoiceTTN(chbInvoiceTTN.isSelected());

            if (chbPriceAgreement.isSelected()) {
                documentPreset.setProtocolReport(cbPriceAgreement.getItemAt(cbPriceAgreement.getSelectedIndex()));
            } else {
                documentPreset.setProtocolReport(null);
            }

            return documentPreset;
        } else {
            return null;
        }
    }


    /**
     * Предустановки элементов управления
     *
     * @param presetDocument класс предустановок
     */
    private void preset(final DocumentTypePreset presetDocument) {
        int invoiceType = presetDocument.getInvoiceType();

        chbAnnexToInvoice.setEnabled(true);
        chbAnnexSimple.setEnabled(true);


        chbPriceAgreement.setEnabled(true);

        switch (invoiceType) {
            case 0: {
                chbInvoiceSelector.setSelected(false);
                rbSelectorTTN1.setEnabled(false);
                rbSelectorTN2.setEnabled(false);
                rbSelectorAdjustmentDocument.setEnabled(false);
                break;
            }

            case 1: {
                chbInvoiceSelector.setSelected(true);
                rbSelectorTTN1.setSelected(true);
                rbSelectorTTN1.setEnabled(true);
                rbSelectorTN2.setEnabled(true);
                rbSelectorAdjustmentDocument.setEnabled(true);
                break;
            }

            case 2: {
                chbInvoiceSelector.setSelected(true);
                rbSelectorTTN1.setEnabled(true);
                rbSelectorTN2.setEnabled(true);
                rbSelectorAdjustmentDocument.setEnabled(true);
                rbSelectorTN2.setSelected(true);
                break;
            }

            case 3: {
                chbInvoiceSelector.setSelected(true);
                rbSelectorAdjustmentDocument.setSelected(true);
                rbSelectorAdjustmentDocument.setEnabled(true);
                rbSelectorTTN1.setEnabled(true);
                rbSelectorTN2.setEnabled(true);
                break;
            }
        }

        chbAnnexToInvoice.setSelected(presetDocument.isAnnexToInvoice());
        if (chbAnnexToInvoice.isSelected()) {
            chbAnnexToInvoiceVat.setEnabled(true);
            chbAnnexToInvoiceVat.setSelected(presetDocument.isAnnexToInvoiceVat());
            chbAnnexSimple.setSelected(presetDocument.isAnnexSimple());
            chbAnnexSimple.setEnabled(true);
        } else {
            chbAnnexSimple.setEnabled(false);
            chbAnnexSimple.setSelected(false);
            chbAnnexToInvoiceVat.setEnabled(false);
            chbAnnexToInvoiceVat.setSelected(false);
        }


        chbReferenceTTN.setSelected(presetDocument.isReferenceToInvoice());

        if (presetDocument.isPriceAgreement()) {
            chbPriceAgreement.setSelected(true);
            cbPriceAgreement.setEnabled(true);
        } else {
            chbPriceAgreement.setSelected(false);
            cbPriceAgreement.setEnabled(false);
        }

        chbAnnexSimple.setSelected(true);
        chbInvoiceTTN.setSelected(false);

        presetAgreementProtocol(documentPreset.getContractorCode());
    }

    /**
     * Метод предустановки протокола согласования цен согласно выбранного контрагента
     *
     * @param contractorCode код контрагента
     */
    private void presetAgreementProtocol(int contractorCode) {
        for (int i = 0; i < cbPriceAgreement.getItemCount(); i++) {
            PriceAgreementProtocol item = cbPriceAgreement.getItemAt(i);
            if (item != null) {
                if (item.getContractorCode() != null) {
                    for (int id : item.getContractorCode()) {
                        if (id == contractorCode) {
                            cbPriceAgreement.setSelectedIndex(i);
                            documentPreset.setProtocol(item);
                            chbAnnexSimple.setSelected(true);
                            return;
                        }
                    }
                }
            }
        }
        cbPriceAgreement.setSelectedIndex(0);
    }
}
