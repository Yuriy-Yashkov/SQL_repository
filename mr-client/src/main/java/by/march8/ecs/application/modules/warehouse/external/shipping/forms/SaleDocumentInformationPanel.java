package by.march8.ecs.application.modules.warehouse.external.shipping.forms;

import by.march8.ecs.application.modules.warehouse.external.shipping.model.TotalSummingUp;
import by.march8.entities.warehouse.InvoiceType;
import by.march8.entities.warehouse.SaleDocumentBase;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author Andy 24.02.2016.
 */
public class SaleDocumentInformationPanel extends JPanel {
    public JLabel lblTypeError = new JLabel("<html>Проблема((<br> в документе и детский и взрослый!</html>");
    public JLabel lblNDSError = new JLabel("<html>Проблема((<br> в документе разный НДС!</html>");
    private SummaryInformationPanel sipFirst;
    private SummaryInformationPanel sipSecond;
    private JPanel infoPanel;
    private JLabel lblSummary;
    private JLabel lblDocumentType = new JLabel("Документ");
    private JLabel lblVatLabel = new JLabel("Ставка НДС :");
    private JLabel lblVatValue = new JLabel("-");
    private JLabel lblDiscountType = new JLabel("-");
    private JLabel lblDiscountValue = new JLabel("-");
    private JLabel lblTradeAllowanceLabel = new JLabel("Торговая надбавка");
    private JLabel lblTradeAllowanceValue = new JLabel("-");
    private JLabel lblNoSortError = new JLabel("Разные сорта в документе");
    private JLabel lblNoSort = new JLabel("Несортные изделия");
    private JLabel lblZeroPrice = new JLabel("Нулевые цены в документе");

    public SaleDocumentInformationPanel() {
        super(new MigLayout());
        setPreferredSize(new Dimension(1000, 500));
        init();
    }

    private void init() {
        sipFirst = new SummaryInformationPanel();
        sipSecond = new SummaryInformationPanel();

        lblTypeError.setForeground(Color.BLUE);
        lblNDSError.setForeground(Color.RED);
        lblNoSortError.setForeground(Color.RED);
        lblZeroPrice.setForeground(Color.RED);
        lblNoSort.setForeground(Color.MAGENTA);

        lblSummary = new JLabel("Нет данных");

        infoPanel = new JPanel(new MigLayout());

        infoPanel.add(lblDocumentType, "span 2, wrap");

        infoPanel.add(lblNoSort, "span 2, wrap");

        infoPanel.add(lblVatLabel);
        infoPanel.add(lblVatValue, "wrap");

        infoPanel.add(lblDiscountType);
        infoPanel.add(lblDiscountValue, "wrap");

        infoPanel.add(lblTradeAllowanceLabel);
        infoPanel.add(lblTradeAllowanceValue, "wrap");

        infoPanel.add(lblTypeError, "span 2, wrap");
        infoPanel.add(lblNDSError, "span 2, wrap");
        infoPanel.add(lblNoSortError, "span 2, wrap");


        Color c = new Color(146, 212, 221);

        infoPanel.setBorder(BorderFactory.createLineBorder(c));
        JPanel pContent = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel pStatus = new JPanel(new MigLayout());

        add(pContent, "width 900:10:900");
        pContent.add(infoPanel);
        pContent.add(sipFirst);
        pContent.add(sipSecond);

        pStatus.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pStatus.setBorder(BorderFactory.createTitledBorder("Статус документа"));
        pStatus.add(lblSummary);
        //pStatus.setSize(900,400);
        add(new JPanel(), "height 10:10");
        pStatus.add(lblNoSortError, "wrap");
        add(new JPanel(), "height 10:10");
        pStatus.add(lblZeroPrice, "wrap");
        add(new JPanel(), "height 10:10");
        pStatus.add(lblNDSError, "wrap");


        //add(infoPanel, "width 300:30:300, height 140:140");
        //add(sipFirst, "width 300:30:300, height 120:120");
        //add(sipSecond, "width 300:30:300, height 120:120, wrap");
        add(new JPanel(), "height 50:10,  wrap");
        add(pStatus, "width 900:10:900, height 50:10");

        sipFirst.setCurrencyName("Бел. руб.");
    }

    public void updateInformation(final TotalSummingUp summingUp, SaleDocumentBase documentBase) {
        sipFirst.setSumCostValue(String.format("%.2f", summingUp.getValueSumCost()));
        sipFirst.setSumVatValue(String.format("%.2f", summingUp.getValueSumVat()));
        sipFirst.setSumCostAndVatValue(String.format("%.2f", summingUp.getValueSumCostAndVat()));

        sipSecond.setSumCostValue(String.format("%.2f", summingUp.getValueSumCostCurrency()));
        sipSecond.setSumVatValue(String.format("%.2f", summingUp.getValueSumVatCurrency()));
        sipSecond.setSumCostAndVatValue(String.format("%.2f", summingUp.getValueSumCostAndVatCurrency()));

        String typeDoc = "";
        typeDoc = documentBase.getDocumentType();
        if (documentBase.getDocumentExport() > 0) {
            typeDoc = typeDoc + " (Экспортный)";
        }

        lblDocumentType.setText(typeDoc);

        if (documentBase.getDocumentVATType() != 2) {
            lblVatValue.setVisible(true);
            lblVatLabel.setVisible(true);
            lblVatValue.setText(documentBase.getDocumentVatValue() + " %");
        } else {
            lblVatValue.setVisible(false);
            lblVatLabel.setVisible(false);
            lblVatValue.setText(documentBase.getDocumentVatValue() + " %");
        }


        if (documentBase.getDiscountType() == 0) {
            lblDiscountValue.setVisible(false);
            lblDiscountType.setVisible(false);
            lblDiscountValue.setText(documentBase.getDiscountValue() + " %");
        } else if (documentBase.getDiscountType() == 1) {
            lblDiscountType.setText("Скидка на единицу изделия ");
            lblDiscountValue.setVisible(true);
            lblDiscountType.setVisible(true);
            lblDiscountValue.setText(documentBase.getDiscountValue() + " %");
        } else if (documentBase.getDiscountType() == 2) {
            lblDiscountType.setText("Скидка на сумму документа ");
            lblDiscountValue.setVisible(true);
            lblDiscountType.setVisible(true);
            lblDiscountValue.setText(documentBase.getDiscountValue() + " %");
        } else if (documentBase.getDiscountType() == 3) {
            lblDiscountType.setText("Ручная установка скидки ");
            lblDiscountValue.setVisible(true);
            lblDiscountType.setVisible(true);
            lblDiscountValue.setText(documentBase.getDiscountValue() + " %");
        }

        if (documentBase.getTradeMarkType() == 1) {
            lblTradeAllowanceLabel.setVisible(true);
            lblTradeAllowanceValue.setVisible(true);
            lblTradeAllowanceValue.setText(documentBase.getTradeMarkValue() + " %");
            lblTradeAllowanceLabel.setText("Торговая надбавка: ");
        } else if (documentBase.getTradeMarkType() == 2) {
            lblTradeAllowanceLabel.setText("ТН с учетом уценки: ");
            lblTradeAllowanceLabel.setVisible(true);
            lblTradeAllowanceValue.setVisible(true);
            lblTradeAllowanceValue.setText(documentBase.getTradeMarkValue() + " %");
        } else {
            lblTradeAllowanceLabel.setVisible(false);
            lblTradeAllowanceValue.setVisible(false);
            lblTradeAllowanceValue.setText(documentBase.getTradeMarkValue() + " %");
        }

        if (documentBase.getDocumentType().equals(InvoiceType.DOCUMENT_REFUND_MATERIAL) || documentBase.getDocumentType().equals(InvoiceType.DOCUMENT_SALE_MATERIAL)) {
            lblNoSort.setVisible(false);
        } else {
            lblNoSort.setVisible(documentBase.isNotVarietal());
            lblNoSort.setText("Несортные изделия (" + documentBase.getPriceReduction3Grade() + "%)");
        }
    }

    public SummaryInformationPanel getFirstInfoPanel() {
        return sipFirst;
    }

    public SummaryInformationPanel getSecondInfoPanel() {
        return sipSecond;
    }

    public void setSummaryInformation(String s) {
        lblSummary.setText(s);
    }

    public void setSummaryInformationValue(String s) {
        lblSummary.setText(s);
    }

    public void updateType(final boolean b) {
        if (b) {
            lblTypeError.setVisible(true);
        } else {
            lblTypeError.setVisible(false);
        }
    }

    public void updateNDS(final boolean b) {
        if (b) {
            lblNDSError.setVisible(true);
        } else {
            lblNDSError.setVisible(false);
        }
    }

    public void updateNoSort(final boolean b) {
        if (b) {
            lblNoSortError.setVisible(true);
        } else {
            lblNoSortError.setVisible(false);
        }
    }

    public void setZeroPriceControl(boolean isVisible) {
        lblZeroPrice.setVisible(isVisible);
    }

    public void visibleLeftPanel(boolean visible) {
        infoPanel.setVisible(visible);
    }
}
