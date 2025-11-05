package by.march8.ecs.application.modules.warehouse.internal.displacement.mode;


import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.jdbc.DocumentJDBC;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator;
import by.march8.ecs.application.modules.warehouse.internal.displacement.ReductionPriceNSI;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.warehouse.DisplacementInvoiceView;
import by.march8.entities.warehouse.VInternalInvoiceItem;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Спецификация накладной
 */
public class DisplacementDetailMode extends AbstractFunctionalMode {
    private RightEnum right;
    private EditingPane editingPane;

    private DisplacementInvoiceView document;
    private ArrayList<Object> data;

    private JButton btnTerminal;
    private JButton btnInventoryCalculate;

    private JPanel pFooter;

    private JPanel pSender;
    private JPanel pRecipient;

    private JLabel lblSender;
    private JLabel lblRecipient;

    public DisplacementDetailMode(MainController mainController, DisplacementInvoiceView document) {

        controller = mainController;
        modeName = "Спецификация накладной внутреннего перемещения №" + document.getDocumentNumber().trim();
        this.document = document;

        frameViewPort = new FrameViewPort(mainController, MarchWindowType.DIALOGSIMPLE);
        frameViewPort.getFrameControl().setTitleFrame(modeName);

        right = controller.getRight(modeName);
        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.registerEvents(this);
        toolBar.setRight(right);
        toolBar.getBtnReport().setVisible(false);


        btnTerminal = new JButton();
        btnTerminal.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/scanner.png", "Расчет документа"));

        btnInventoryCalculate = new JButton();
        btnInventoryCalculate.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/currency_24.png", "Расчет инвентаризационной ячейки"));
        btnInventoryCalculate.setToolTipText("Расчет инвентаризации");

        toolBar.add(btnTerminal);
        toolBar.add(btnInventoryCalculate);

        gridViewPort = new GridViewPort(VInternalInvoiceItem.class, false);
        data = gridViewPort.getDataModel();
        frameViewPort.setGridViewPort(gridViewPort);

        pFooter = new JPanel(new MigLayout());
        pFooter.setPreferredSize(new Dimension(100, 70));

        pSender = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pSender.setBackground(Color.green.brighter());
        lblSender = new JLabel("");
        pSender.add(lblSender);

        pRecipient = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pRecipient.setBackground(Color.MAGENTA.brighter());
        lblRecipient = new JLabel("");
        pRecipient.add(lblRecipient);

        pFooter.add(pSender, "width 1000:20:1000, height 20:20, wrap");
        pFooter.add(pRecipient, "width 1000:20:1000,height 20:20, wrap");

        frameViewPort.getFrameRegion().getBottomContentPanel().setLayout(new BorderLayout());
        frameViewPort.getFrameRegion().getBottomContentPanel().removeNotify();
        frameViewPort.getFrameRegion().getBottomContentPanel().add(pFooter, BorderLayout.SOUTH);
        frameViewPort.getFrameRegion().getBottomContentPanel().setPreferredSize(new Dimension(20, 100));

        frameViewPort.getButtonControl().getCancelButton().setVisible(false);
        frameViewPort.getButtonControl().getOkButton().setVisible(false);


        initEvents();
        updateContent();

        gridViewPort.primaryInitialization();
        frameViewPort.getFrameControl().setFrameSize(new Dimension(1024, 700));
        frameViewPort.getFrameControl().showFrame();
    }

    private void initEvents() {
        btnTerminal.addActionListener(a -> {
            if (document.getStatusCode() == 3) {
                if (document.getReduction3Grade() > 0) {
                    final int answer = Dialogs.showQuestionDialog("Вы действительно хотите пересчитать цены?"
                            , "Расчет цен");
                    if (answer == 0) {
                        calculateDocument();
                    }
                }
            }
        });

        btnInventoryCalculate.addActionListener(a -> {
            if (document.getOperationName().trim().equals("Инвентаризация")) {
                final int answer = Dialogs.showQuestionDialog("Вы действительно хотите пересчитать цены для инвентаризационной ячейки?"
                        , "Расчет цен для инвентаризации");
                if (answer == 0) {
                    doInventoryCalculate();
                }
            }
        });
    }

    private double getCutDecimal(double d) {
        String s = String.valueOf(d);
        String outString = "";
        int decimalIndex = s.indexOf(".");
        if (decimalIndex + 3 > s.length()) {
            outString = s;
        } else {
            outString = s.substring(0, decimalIndex + 3);
        }
        return Double.valueOf(outString);
    }

    private double getHalfSearch(double d) {
        String s = String.valueOf(d);
        int decimalIndex = s.indexOf(".");
        if (decimalIndex + 3 < s.length()) {

            if (s.charAt(decimalIndex + 3) == '5') {
                System.out.println("Режем хвост");
                return getCutDecimal(d);
            } else {
                BigDecimal bd = new BigDecimal(d).setScale(10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                return bd.doubleValue();
            }
        } else {
            return d;
        }
    }

    private double round(double d) {
        BigDecimal bd = new BigDecimal(d).setScale(10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    private double getCutDecimalHelp(double d) {
        String s = String.valueOf(d);
        String outString = "";
        int decimalIndex = s.indexOf(".");
        if (decimalIndex + 3 > s.length()) {
            outString = s;
        } else {
            outString = s.substring(0, decimalIndex + 3);
        }
        return Double.valueOf(outString);
    }

    private void calculateDocument() {
        // Получаем цены первого сорта
        DocumentJDBC db = new DocumentJDBC();
        List<ReductionPriceNSI> priceList = db.getNSIPriceByDocumentId(document.getId());
        // Пересчет цен по документу, с сохранением
        int reductionValue = (int) document.getReduction3Grade();
        if (reductionValue > 1) {
            for (ReductionPriceNSI item : priceList) {

                if (item.getGrade() < 2) {
                    JOptionPane.showMessageDialog(null,
                            "Несортный документ содержит сортные изделия, расчет документа не произведен!!!");
                    return;
                }

                BigDecimal percent = new BigDecimal(item.getPrice1Grade() * reductionValue / 100);

                BigDecimal bd = new BigDecimal(String.valueOf(round(item.getPrice1Grade() - percent.doubleValue())));

                item.setPrice3Grade(bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

                item.setSumm1Grade(SaleDocumentCalculator.roundBigDecimal(item.getPrice1Grade() * item.getAmount(), 2).doubleValue());

            }

            db.saveItemPrice3GradeByDocument(priceList);
            updateContent();
        }
    }

    private void doInventoryCalculate() {


        // Получаем цены первого сорта
        DocumentJDBC db = new DocumentJDBC();
        List<ReductionPriceNSI> priceList = db.getNSIPriceByDocumentId(document.getId());
        // Пересчет цен по документу, с сохранением
        //  (int) document.getReduction3Grade();
        int reductionValue = 0;
        for (ReductionPriceNSI item : priceList) {

            int grade = item.getGrade();

            if (grade > 2) {
                reductionValue = 70;
            } else {
                reductionValue = 40;
            }

            //BigDecimal percent = new BigDecimal(pr.doubleValue()*(int)reductionValue/100) .setScale(10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);

            BigDecimal percent = new BigDecimal(item.getPrice1Grade() * reductionValue / 100);

            BigDecimal bd = new BigDecimal(String.valueOf(round(item.getPrice1Grade() - percent.doubleValue())));

            item.setPrice3Grade(bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

            item.setSumm1Grade(SaleDocumentCalculator.roundBigDecimal(item.getPrice1Grade() * item.getAmount(), 2).doubleValue());

            System.out.println(
                    "Кол-во " + item.getAmount()
                            + " Цена(1) " + item.getPrice1Grade()
                            + " Сумма(1) " + item.getSumm1Grade()
                            + " Процент " + percent.doubleValue()
                            + " Цена(У) " + bd.doubleValue()
                            + " Цена(У+) " + item.getPrice3Grade()
                            + " Сумма(У) " + SaleDocumentCalculator.roundBigDecimal(item.getPrice3Grade() * item.getAmount(), 2).doubleValue());
        }

        db.saveItemPrice3GradeByDocument(priceList);
        updateContent();
    }

    @Override
    public void updateContent() {

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {

                DaoFactory<VInternalInvoiceItem> factory = DaoFactory.getInstance();
                IGenericDao<VInternalInvoiceItem> dao = factory.getGenericDao();

                java.util.List<QueryProperty> criteria = new ArrayList<>();
                criteria.add(new QueryProperty("document", document.getId()));
                java.util.List<VInternalInvoiceItem> list = null;
                data.clear();
                try {
                    list = dao.getEntityListByNamedQuery(VInternalInvoiceItem.class, "VInternalInvoice.findByDocumentId", criteria);
                    data.addAll(list);
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                String senderText = "";
                String recipientText = "";

                //String outputPattern = "СДАЧА: всего единиц: %d сумма %.2f";
                String outputPattern = "СДАЧА: всего единиц: %d в упаковках: %d  упаковок: %d россыпью: %d сумма %.2f";

                int amountTotalSend = 0;
                int amountTotalInPackSend = 0;
                int amountTotalPackSend = 0;
                int amountTotalUnPackSend = 0;
                double costSend = 0;

                int amountTotalRec = 0;
                int amountTotalInPackRec = 0;
                int amountTotalPackRec = 0;
                int amountTotalUnPackRec = 0;
                double costRec = 0;

                for (VInternalInvoiceItem item : list) {

                    int amountAllSend = 0;
                    int amountPackSend = 0;
                    int amountNotPackSend = 0;

                    int amountAllRec = 0;
                    int amountPackRec = 0;
                    int amountNotPackRec = 0;


                    if (item.getType() == 2) {
                        amountPackSend++;
                        amountAllSend = (item.getQuantity() * item.getQuantityPack());

                        amountPackRec++;
                        amountAllRec = (item.getQuantityPattern() * item.getQuantityPack());

                    } else if (item.getType() == 1) {
                        amountNotPackSend = item.getQuantity() * item.getQuantityPack();
                        amountAllSend = (item.getQuantity() * item.getQuantityPack());

                        amountNotPackRec = item.getQuantityPattern() * item.getQuantityPack();
                        amountAllRec = (item.getQuantityPattern() * item.getQuantityPack());

                    } else if (item.getType() == 3) {
                        amountPackSend++;
                        amountNotPackSend = 0;
                        amountAllSend += item.getQuantityPack();

                        amountPackRec++;
                        amountNotPackRec = 0;
                        amountAllRec += item.getQuantityPack();
                    }


                    amountTotalSend += amountAllSend;
                    // В упаковках
                    amountTotalInPackSend += amountAllSend - amountNotPackSend;
                    // Упаковок
                    amountTotalPackSend += amountPackSend;
                    // Россыпь
                    amountTotalUnPackSend += amountNotPackSend;
                    costSend += item.getCostAll();//(item.getQuantity()*item.getQuantityPack();
                    senderText = String.format(outputPattern,
                            amountTotalSend
                            , amountTotalInPackSend
                            , amountTotalPackSend
                            , amountTotalUnPackSend
                            , costSend);

                    // Отправлено
                    amountTotalRec += amountAllRec;
                    // В упаковках
                    amountTotalInPackRec += amountAllRec - amountNotPackRec;
                    // Упаковок
                    amountTotalPackRec += amountPackRec;
                    // Россыпь
                    amountTotalUnPackRec += amountNotPackRec;
                    costRec += item.getCostPattern();//(item.getQuantity()*item.getQuantityPack();
                    recipientText = String.format(outputPattern,
                            amountTotalRec
                            , amountTotalInPackRec
                            , amountTotalPackRec
                            , amountTotalUnPackRec
                            , costRec);

                }

                lblSender.setText(senderText);
                lblRecipient.setText(recipientText);

                frameViewPort.updateContent();

                btnTerminal.setEnabled(false);
                btnInventoryCalculate.setEnabled(false);

                if ((document.getStatusCode() == 3)) {
                    if (document.getReduction3Grade() > 0) {
                        btnTerminal.setEnabled(true);
                    }
                }

                if (document.getOperationName().trim().equals("Инвентаризация")) {
                    btnInventoryCalculate.setEnabled(true);
                }

                return true;
            }
        }

        Task task = new Task("Получение спецификации...");
        task.executeTask();
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
