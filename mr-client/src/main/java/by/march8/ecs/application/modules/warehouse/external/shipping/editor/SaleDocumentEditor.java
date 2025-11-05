package by.march8.ecs.application.modules.warehouse.external.shipping.editor;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.ucdao.jdbc.DocumentJDBC;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextFieldPanel;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.currency.mode.CurrencyRateMonitorMode;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.CourseDateByOrderDB;
import by.march8.ecs.application.modules.warehouse.external.shipping.enums.SaleDocumentStatus;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentManager;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.FinishedGoodsHelper;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentView;
import by.march8.ecs.framework.common.Settings;
import by.march8.entities.readonly.ContractEntity;
import by.march8.entities.readonly.ContractorEntityView;
import by.march8.entities.readonly.CurrencyEntityMarch8;
import by.march8.entities.warehouse.InvoiceType;
import by.march8.entities.warehouse.SaleDocumentBase;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Date;
import java.util.Objects;

/**
 * Редактор основной информации по накладной
 *
 * @author Andy on 12.08.2015.
 */
@SuppressWarnings("all")
public class SaleDocumentEditor extends EditingPane {

    private String sql = "SELECT contractor FROM ContractorEntityView contractor order by contractor.name";

    private JLabel lblDocumentDate;
    private UCDatePicker dpDocumentDate;

    private JLabel lblDocumentNumber;
    private UCTextField tfDocumentNumber;

    private JLabel lblOperationType;
    private ComboBoxPanel<InvoiceType> cbpOperationType;

    private JLabel lblSender;
    private UCTextFieldPanel<ContractorEntityView> tfpSender;
    private UCTextField tfSenderCode;

    private JLabel lblRecipient;
    private UCTextFieldPanel<ContractorEntityView> tfpRecipient;
    private UCTextField tfRecipientCode;
    private JLabel lblRecipientContract;
    private JComboBox<ContractEntity> cbRecipientContract;


    private JLabel lblDiscount;
    private JComboBox<String> cbDiscountType;
    private UCTextField tfDiscountValue;

    private JLabel lblVat;
    private JComboBox<String> cbVatType;
    private UCTextField tfVatValue;

    private JLabel lblTradeMarkup;
    private JComboBox<String> cbTradeMarkupType;
    private UCTextField tfValueTradeMarkup;

    private JLabel lblCurrency;
    private ComboBoxPanel<CurrencyEntityMarch8> cbpCurrency;
    private JPanel pCurrencyValues;
    private JLabel lblFixedCurrency;
    private JLabel lblSaleCurrency;
    private JLabel lblCurrencyOriginDate;
    private UCTextField tfFixedCurrency;
    private UCTextField tfSaleCurrency;
    private JButton btnFixedCurrencyRefresh;
    private JButton btnSaleCurrencyRefresh;
    private UCDatePicker dpCurrencyOriginDate;
    private JLabel lblCertificate;
    private JComboBox<String> cbCertificate;
    private JLabel lblCertificateGGR;
    private JComboBox<String> cbCertificateGGR;

    //private JCheckBox checkBoxVat;
    private JPanel pPrepayment;
    private JCheckBox checkBoxPrepayment;
    private UCDatePicker dpPrepaymentDate;

    private JPanel pCalculationFactor;
    private JCheckBox cbCalculationFactor;
    private UCTextField tfCalculationFactor;

    private JCheckBox checkBoxExport;
    private JCheckBox checkBoxGiveTake;

    private JPanel pNotVarietal;
    private JCheckBox chbNotVarietal;
    private UCTextField tfNotVarietal;

    private SaleDocumentView source;

    private SaleDocumentBase document;

    private SaleDocumentManager documentManager;

    private Date fixedDate = null;
    private Date saleDate = null;

    private boolean isNew = false;

    private JCheckBox checkBoxNewPrice;
    private JButton btnFixedCurrencyQuerter;

    private JComboBox<String> cbServiceType;


    public SaleDocumentEditor(final FrameViewPort frameViewPort) {
        setPreferredSize(new Dimension(680, 670));
        controller = frameViewPort.getController();

        this.setLayout(new MigLayout());
        init();
        initEvents();
    }

    private void init() {
        // Номер документа
        lblDocumentNumber = new JLabel("Номер документа");
        lblDocumentNumber.setHorizontalAlignment(SwingConstants.LEFT);
        tfDocumentNumber = new UCTextField();
        add(lblDocumentNumber);
        add(tfDocumentNumber, "height 20:20, width 100:20:100");

        // дата документа
        lblDocumentDate = new JLabel("Дата документа");

        dpDocumentDate = new UCDatePicker(new Date());
        dpDocumentDate.setEnabled(true);
        dpDocumentDate.getEditor().setEditable(false);

        add(lblDocumentDate, "align right");
        add(dpDocumentDate, "height 20:20,width 105:20:105, wrap");


        add(new JPanel(), "height 5:5,  wrap");
        // Вид операции
        lblOperationType = new JLabel("Вид операции");
        cbpOperationType = new ComboBoxPanel<>(controller, MarchReferencesType.INVOICE_TYPE, false);
        cbpOperationType.setEasy(true);
        add(lblOperationType);
        add(cbpOperationType, "height 20:20, width 260:40:260");

        checkBoxExport = new JCheckBox("Экспортный");
        checkBoxExport.setForeground(Color.BLUE);

        add(checkBoxExport, "align left,  wrap");


        checkBoxGiveTake = new JCheckBox("Услуга...");
        cbServiceType = new JComboBox<>();

        add(checkBoxGiveTake, "align left");
        add(cbServiceType, "height 20:20, width 360:20:360, span 2, wrap");

        add(new JPanel(), "height 10:10,  wrap");

        // Отправитель
        lblSender = new JLabel("Отправитель");
        tfpSender = new UCTextFieldPanel<>(controller, MarchReferencesType.CONTRACTOR_OLD, sql);
        tfSenderCode = new UCTextField();
        tfSenderCode.setEnabled(false);
        add(lblSender);
        add(tfpSender, "height 20:20, width 360:20:360,span 2");
        add(tfSenderCode, "height 20:20,width 100:20:100,wrap");

        add(new JPanel(), "height 10:10,  wrap");
        // Получатель
        lblRecipient = new JLabel("Получатель");
        tfpRecipient = new UCTextFieldPanel<>(controller, MarchReferencesType.CONTRACTOR_OLD, sql);
        tfRecipientCode = new UCTextField();
        tfRecipientCode.setEnabled(false);
        add(lblRecipient);
        add(tfpRecipient, "height 20:20, width 360:20:360,span 2");
        add(tfRecipientCode, "height 20:20,width 100:20:100,wrap");
        lblRecipientContract = new JLabel("Договор ");
        cbRecipientContract = new JComboBox<>();
        add(lblRecipientContract);
        add(cbRecipientContract, "height 20:20, width 360:20:360,span 2,wrap");

        add(new JPanel(), "height 10:10,  wrap");

        // Скидка
        lblDiscount = new JLabel("Скидка");
        cbDiscountType = new JComboBox<>();
        tfDiscountValue = new UCTextField();
        tfDiscountValue.setComponentParams(lblDiscount, Float.class, 2);
        //tfDiscountValue.setEnabled(false);
        add(lblDiscount);
        add(cbDiscountType, "height 20:20, width 360:20:360, span 2");
        add(tfDiscountValue, "height 20:20,width 100:20:100,wrap");

        pCalculationFactor = new JPanel(null);
        cbCalculationFactor = new JCheckBox("Коэффициент расчета");
        cbCalculationFactor.setBounds(0, 0, 180, 20);

        tfCalculationFactor = new UCTextField();
        tfCalculationFactor.setComponentParams(null, Float.class, 6);
        tfCalculationFactor.setBounds(185, 0, 110, 20);
        pCalculationFactor.add(cbCalculationFactor);
        pCalculationFactor.add(tfCalculationFactor);
        //checkBoxVat = new JCheckBox("НДС сверху");

        add(new JLabel(), "");
        add(pCalculationFactor, "align left,height 20:20, width 360:20:360, span 2, wrap");

        pNotVarietal = new JPanel(null);

        chbNotVarietal = new JCheckBox("Уценка несортных, %");
        chbNotVarietal.setForeground(Color.blue);
        chbNotVarietal.setBounds(0, 0, 180, 20);

        tfNotVarietal = new UCTextField();
        tfNotVarietal.setComponentParams(null, Float.class, 6);
        tfNotVarietal.setBounds(185, 0, 110, 20);

        pNotVarietal.add(chbNotVarietal);
        pNotVarietal.add(tfNotVarietal);

        add(new JLabel(), "");
        add(pNotVarietal, "align left,height 20:20, width 360:20:360, span 2, wrap");

        add(new JPanel(), "height 10:10,  wrap");
        // НДС
        lblVat = new JLabel("Ставка НДС");
        cbVatType = new JComboBox<>();
        tfVatValue = new UCTextField();
        tfVatValue.setComponentParams(lblVat, Float.class, 0);
        //tfDiscountValue.setEnabled(false);
        add(lblVat);
        add(cbVatType, "height 20:20, width 360:20:360, span 2");
        add(tfVatValue, "height 20:20,width 100:20:100,wrap");

        // ТОрговая надбавка
        lblTradeMarkup = new JLabel("Торговая надбавка");
        cbTradeMarkupType = new JComboBox<>();
        tfValueTradeMarkup = new UCTextField();
        tfValueTradeMarkup.setComponentParams(lblTradeMarkup, Float.class, 0);
        //tfDiscountValue.setEnabled(false);
        add(lblTradeMarkup);
        add(cbTradeMarkupType, "height 20:20, width 360:20:360, span 2");
        add(tfValueTradeMarkup, "height 20:20,width 100:20:100,wrap");

        pPrepayment = new JPanel(null);

        checkBoxPrepayment = new JCheckBox("Предоплата по документу");
        checkBoxPrepayment.setBounds(0, 0, 180, 20);

        dpPrepaymentDate = new UCDatePicker(new Date());
        dpPrepaymentDate.setBounds(185, 0, 110, 20);

        pPrepayment.add(checkBoxPrepayment);
        pPrepayment.add(dpPrepaymentDate);

        //add(new JLabel(), "");
        //add(checkBoxVat, "wrap");

        //add(new JPanel(), "height 10:10,  wrap");

        add(new JLabel(), "");

        add(pPrepayment, "align left,height 20:20, width 360:20:360, span 2, wrap");
        add(new JPanel(), "height 10:10,  wrap");

        // Валюта
        lblCurrency = new JLabel("Валюта документа");
        cbpCurrency = new ComboBoxPanel<>(controller, MarchReferencesType.CURRENCY_OLD, false);
        cbpCurrency.setEasy(true);
        cbpCurrency.getSelectButton().setVisible(false);
        pCurrencyValues = new JPanel(new MigLayout());
        pCurrencyValues.setPreferredSize(new Dimension(300, 50));

        lblFixedCurrency = new JLabel("Курс начало месяца");
        lblSaleCurrency = new JLabel("Курс на момент отгрузки");
        lblCurrencyOriginDate = new JLabel("дата");
        tfFixedCurrency = new UCTextField();
        tfFixedCurrency.setComponentParams(lblFixedCurrency, Float.class, 4);

        tfSaleCurrency = new UCTextField();
        tfSaleCurrency.setComponentParams(lblSaleCurrency, Float.class, 4);

        dpCurrencyOriginDate = new UCDatePicker(new Date());
        dpCurrencyOriginDate.setEnabled(true);
        dpCurrencyOriginDate.getEditor().setEditable(false);

        btnFixedCurrencyRefresh = new JButton("квартал");
        btnFixedCurrencyQuerter = new JButton("месяц  ");
        btnSaleCurrencyRefresh = new JButton();

        JButton btnCurrency = new JButton("Монитор курсов валюты");
        btnCurrency.addActionListener(a -> new CurrencyRateMonitorMode(controller));

        btnFixedCurrencyRefresh.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/refresh_16.png", "Refresh"));
        btnFixedCurrencyQuerter.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/refresh_16.png", "Refresh"));
        btnSaleCurrencyRefresh.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/refresh_16.png", "Refresh"));
        ((JButton) dpCurrencyOriginDate.getDatePicker().getComponent(1)).setIcon(new ImageIcon(MainController.getRunPath() + "/Img/refresh_16.png", "Refresh"));

        pCurrencyValues.add(lblCurrencyOriginDate, "span 2, align right");
        pCurrencyValues.add(dpCurrencyOriginDate, "height 20:20, width 105:20:105, align right, wrap");
        pCurrencyValues.add(btnFixedCurrencyQuerter, "height 18:18, width 105:20:105, span 3, align right, wrap");
        pCurrencyValues.add(lblFixedCurrency, "align left, height 20:20, width 300:20:300");
        pCurrencyValues.add(tfFixedCurrency, "align left, height 20:20, width 60:20:60");
        pCurrencyValues.add(btnFixedCurrencyRefresh, "align left, height 18:18, width 105:20:105, wrap");

        pCurrencyValues.add(lblSaleCurrency, "align left, height 20:20, width 300:20:300");
        pCurrencyValues.add(tfSaleCurrency, "height 20:20, width 60:20:60");
        pCurrencyValues.add(btnSaleCurrencyRefresh, "align left, height 18:18, width 18:18:18, wrap");
        pCurrencyValues.add(btnCurrency, "height 20:20, width 180:20:180,span 3, align right, wrap");

        add(lblCurrency);
        add(cbpCurrency, "height 20:20, width 150:20:150, span 2, wrap");
        add(pCurrencyValues, "span 3, align left, wrap");

        add(new JPanel(), "height 10:10,  wrap");
        // Сертификат
        lblCertificate = new JLabel("Сертификат");
        cbCertificate = new JComboBox<>();
        add(lblCertificate);
        add(cbCertificate, "height 20:20, width 150:20:150, span 2, wrap");

        // Удостоверение ГГР
        lblCertificateGGR = new JLabel("Удостоверение ГГР");
        cbCertificateGGR = new JComboBox<>();
        add(lblCertificateGGR);
        add(cbCertificateGGR, "height 20:20, width 150:20:150, span 2, wrap");
        add(new JPanel(), "height 10:10,  wrap");

        checkBoxNewPrice = new JCheckBox("Как цены после деноминации");

        tfpSender.setEnabled(false);
    }

    @Override
    public Object getSourceEntity() {
        document.setDocumentNumber(tfDocumentNumber.getText().trim());
        document.setDocumentDate(dpDocumentDate.getDate());
        document.setDocumentType(cbpOperationType.getSelectedItem().getName());

        document.setRecipientCode(tfpRecipient.getValue().getCode());
        document.setRecipientId(tfpRecipient.getValue().getId());
        document.setRecipientContractId(cbRecipientContract.getItemAt(cbRecipientContract.getSelectedIndex()).getId());

        document.setDiscountType(cbDiscountType.getSelectedIndex());

        if (document.getDiscountType() > 0) {
            document.setDiscountValue(Float.valueOf(tfDiscountValue.getText()));
        } else {
            document.setDiscountValue(0.f);
        }

        if (cbTradeMarkupType.getSelectedIndex() > 0) {
            document.setTradeMarkType(cbTradeMarkupType.getSelectedIndex());
            document.setTradeMarkValue(Float.valueOf(tfValueTradeMarkup.getText()));
        } else {
            document.setTradeMarkType(cbTradeMarkupType.getSelectedIndex());
            document.setTradeMarkValue(0);
        }

        document.setCertificateId(cbCertificate.getSelectedIndex());
        document.setCertificateGGRId(cbCertificateGGR.getSelectedIndex());

        document.setNotVarietal(chbNotVarietal.isSelected());

        if (chbNotVarietal.isSelected()) {
            document.setNotVarietal(true);
            document.setPriceReduction3Grade(Float.valueOf(tfNotVarietal.getText()));
        } else {
            document.setPriceReduction3Grade(0);
            document.setNotVarietal(false);
        }

        if (checkBoxExport.isSelected()) {
            document.setDocumentExport(1);
        } else {
            document.setDocumentExport(0);
            document.setCalculationFactor(1);
        }


        if (cbVatType.getSelectedIndex() == 0 || cbVatType.getSelectedIndex() == 1) {
            document.setDocumentVATType(0);
            document.setDocumentVatValue(Float.valueOf(tfVatValue.getText()));
        } else {
            document.setDocumentVATType(cbVatType.getSelectedIndex());
            document.setDocumentVatValue(0);
        }

        document.setPrepayment(checkBoxPrepayment.isSelected());

        if (cbCalculationFactor.isSelected()) {
            document.setCalculationFactor(Float.valueOf(tfCalculationFactor.getText()));
        } else {
            document.setCalculationFactor(1.f);
        }

        if (document.isPrepayment()) {
            document.setPrepaymentDate(dpPrepaymentDate.getDate());
        } else {
            document.setPrepaymentDate(null);
        }

        document.setOriginCurrencyType(lblFixedCurrency.getText());
        document.setOriginCurrencyDate(fixedDate);
        document.setCurrencyId(cbpCurrency.getSelectedItem().getId());
        document.setCurrencyRateFixed(Float.valueOf(tfFixedCurrency.getText()));
        document.setCurrencyRateSale(Float.valueOf(tfSaleCurrency.getText()));
        document.setGiveTake(checkBoxGiveTake.isSelected());
        if (checkBoxGiveTake.isSelected()) {
            document.setServiceType(cbServiceType.getSelectedIndex());
        } else {
            document.setServiceType(0);
        }
        //TODO Добавить служебные поля с датой и юзером корректировки записи
        return document;
    }

    @Override
    public void setSourceEntity(final Object object) {
        documentManager = new SaleDocumentManager();

        if (object == null) {
            isNew = true;
            // Создается новый документ
            source = new SaleDocumentView();
            document = new SaleDocumentBase();
            // Устновки по-умолчанию для нового документа
            dpDocumentDate.setDate(new Date());
            document.setDocumentDate(dpDocumentDate.getDate());
            document.setDocumentNumber("");
            document.setDocumentStatus(SaleDocumentStatus.FORMED);
            document.setDocumentType("");

            document.setDocumentVATType(2);

            document.setCalculationFactor(1.f);

            document.setCreateRecordUser(controller.getWorkSession().getUser().getUserLogin());
            document.setCreateRecordDate(new Date());

            cbRecipientContract.removeAllItems();
            tfpRecipient.clear();
            saleDate = new Date();
            dpPrepaymentDate.setDate(new Date());
            System.out.println("нОВЫЙ ДОКУМЕНТ");
        } else {
            isNew = false;
            // Редактирование уже существующего документа
            source = (SaleDocumentView) object;
            // Получаем  документ из базы
            document = documentManager.getSaleDocumentSimpleByIdGUI(source.getId());
        }
    }

    @Override
    public boolean verificationData() {
        // Валидность номера документа

        if (tfDocumentNumber.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Поле \"Номер документа\" не должно быть пустым", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfDocumentNumber.requestFocusInWindow();
            return false;
        }

        if (isNew) {
            // TODO Заменить проверку на менее затратное
            // ПРоверка на совпадение номеров документа в базе
            System.out.println("проверка корректности ввода номера");
            DocumentJDBC db = new DocumentJDBC();
            if (db.documentIsExist(tfDocumentNumber.getText().trim()) > 0) {
                JOptionPane.showMessageDialog(null,
                        "Документ с таким номером уже существует", "Ошибка!!!",
                        JOptionPane.ERROR_MESSAGE);
                tfDocumentNumber.requestFocusInWindow();
                return false;
            }
        }

        // Тип документа
        if (cbpOperationType.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Укажите тип документа", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            cbpOperationType.setFocus();
            return false;
        }

        // Отгрузка и возврат из розницы для экспортных или в валюте запрещен
        final InvoiceType invoice = cbpOperationType.getSelectedItem();
        if (invoice.getName().equals(InvoiceType.DOCUMENT_REFUND_RETAIL) || invoice.getName().trim().equals(InvoiceType.DOCUMENT_SALE_RETAIL)) {
            if (checkBoxExport.isSelected()) {
                JOptionPane.showMessageDialog(null,
                        "Для ЭКСПОРТНЫХ накладных тип документа [" + invoice.getName().trim() + "] не допустим", "Ошибка!!!",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (cbpCurrency.getSelectedItem() != null) {
                if (cbpCurrency.getSelectedItem().getId() > 1) {
                    JOptionPane.showMessageDialog(null,
                            "Расчет в иностранной валюте для документов типа [" + invoice.getName().trim() + "] не допустим", "Ошибка!!!",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }

        // ПОлучатель не указан
        if (tfpRecipient.getValue() == null) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать получателя документа", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            cbpOperationType.setFocus();
            return false;
        }
        // У получателя пустое имя
        if (tfpRecipient.getValue().getName().trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Поле получателя не должно быть пустым", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            cbpOperationType.setFocus();
            return false;
        }
        // У получателя не верный код
        if (tfpRecipient.getValue().getCode() < 1) {
            JOptionPane.showMessageDialog(null,
                    "Код получателя должен быть больше нуля", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            cbpOperationType.setFocus();
            return false;
        }

        // Не указано основание для отпуска
        if (cbRecipientContract.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "У получателя не указан договор", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            cbRecipientContract.requestFocusInWindow();
            return false;
        }
        // Указана скидка, но не указано ее значение
        if (cbDiscountType.getSelectedIndex() > 0 && cbDiscountType.getSelectedIndex() < 3) {
            if (Float.valueOf(tfDiscountValue.getText()) == 0) {
                JOptionPane.showMessageDialog(null,
                        "У документа указан расчет скидки, но значение равно нулю", "Ошибка!!!",
                        JOptionPane.ERROR_MESSAGE);
                cbDiscountType.requestFocusInWindow();
                return false;
            }
        }

        // Указан расчет НДС, но ставка не указана
        /*if (cbVatType.getSelectedIndex() == 0 || cbVatType.getSelectedIndex() == 1) {
            if (Float.valueOf(tfVatValue.getText()) == 0) {
                JOptionPane.showMessageDialog(null,
                        "У документа указан расчет НДС, но ставка равна нулю", "Ошибка!!!",
                        JOptionPane.ERROR_MESSAGE);
                cbVatType.requestFocusInWindow();
                return false;
            }
        }*/

        // Указана торговая надбавка, но не указано ее значение
        if (cbTradeMarkupType.getSelectedIndex() > 0) {

/*            // Торговая надбавка используется только в накладных розницы
            if (!invoice.getName().equals(InvoiceType.DOCUMENT_SALE_RETAIL) && !invoice.getName().equals(InvoiceType.DOCUMENT_REFUND_RETAIL)) {
                JOptionPane.showMessageDialog(null,
                        "Торговая надбавка используется только в накладных розницы", "Ошибка!!!",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }*/

            if (Float.valueOf(tfValueTradeMarkup.getText()) == 0) {
                JOptionPane.showMessageDialog(null,
                        "У документа указан расчет торговой надбавки, но значение равно нулю", "Ошибка!!!",
                        JOptionPane.ERROR_MESSAGE);
                cbTradeMarkupType.requestFocusInWindow();
                return false;
            }
        }

        // Не указана валюта по документу
        if (cbpCurrency.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Для документа не указан тип валюты", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            cbpCurrency.requestFocusInWindow();
            return false;
        }

        // Установлена валюта отличная от Беларусского рубля,
        // но нет признака что документ экспортный
        if (cbpCurrency.getSelectedItem().getId() > 1) {
            if (!checkBoxExport.isSelected()) {
                JOptionPane.showMessageDialog(null,
                        "В качестве расчетной валюты выбран [" + cbpCurrency.getSelectedItem().getName().trim() + "], " +
                                "но документ не помечен как ЭКСПОРТНЫЙ", "Ошибка!!!",
                        JOptionPane.ERROR_MESSAGE);
                checkBoxExport.requestFocusInWindow();
                return false;
            }
        }

        // Установлен признак экспорта, но валюта = Беларусский рубль
        if (checkBoxExport.isSelected()) {
            if (cbpCurrency.getSelectedItem().getId() == 1) {
                JOptionPane.showMessageDialog(null,
                        "Документ помечен как ЭКСПОРТНЫЙ, но в качестве расчетной валюты указан " +
                                "[" + cbpCurrency.getSelectedItem().getName().trim() + "]", "Ошибка!!!",
                        JOptionPane.ERROR_MESSAGE);
                cbpCurrency.setFocus();
                return false;
            }
        }

        // Если валюта указана а курс валюты не определен - предложить обновить курсы
        if (cbpCurrency.getSelectedItem().getId() > 1) {
            if (Float.valueOf(tfFixedCurrency.getText()) <= 1) {
                int dialogResult = Dialogs.showQuestionCurrencyDialog("Курс валюты для [" + cbpCurrency.getSelectedItem().getName().trim() + "] не определен," +
                        " получить курс валюты из базы ? ");
                if (dialogResult == 0) {
                    btnFixedCurrencyRefresh.doClick();
                }
                btnFixedCurrencyRefresh.requestFocusInWindow();
                return false;
            }
        }


        // Не указан сертификат по документу
        if (cbCertificate.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Не указан тип сертификата для документа", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            cbCertificate.requestFocusInWindow();
            return false;
        }

        // Не указано удостоверение ГГР по документу
        if (cbCertificateGGR.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Не указан тип удостоверения ГГР для документа", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            cbCertificateGGR.requestFocusInWindow();
            return false;
        }

        return true;
    }

    @Override
    public void phaseBeforeShowing() {
        // Загрузка справочников типов документа
        cbpOperationType.updateValues();
        // Загрузка справочников валюты
        cbpCurrency.updateValues();
        // Типы услуг
        FinishedGoodsHelper.getServiceType(cbServiceType);
        // Загрузка типов скидки
        FinishedGoodsHelper.getDiscountType(cbDiscountType);
        // Загрузка типов НДС
        FinishedGoodsHelper.getVatType(cbVatType);
        // Загрузка типов торговой надбавки
        FinishedGoodsHelper.getTradeMarkType(cbTradeMarkupType);
        // Загрузка типов сертификатов страны
        FinishedGoodsHelper.getSertificateCountry(cbCertificate);
        // Загрузка списков сертификатов зарубежья
        FinishedGoodsHelper.getSertificateCountry(cbCertificateGGR);
        //tfpSender.updateValues();
        // Заполнениеконтролов данными
        defaultFillingData();
    }

    // Регистрация событий от контролов
    private void initEvents() {

        checkBoxGiveTake.addActionListener(e -> {
            if (document != null) {
                if (checkBoxGiveTake.isSelected()) {
                    cbServiceType.setEnabled(true);
                } else {
                    cbServiceType.setEnabled(false);
                }
            }
        });

        // Событие выбора из справочника контрагентов
        tfpRecipient.addButtonSelectActionListener(e -> {
            ContractorEntityView contractor = tfpRecipient.selectFromReference(tfpRecipient.getValue());
            if (contractor != null) {
                // Установка наименования контрагента в поле
                tfRecipientCode.setText(String.valueOf(contractor.getCode()));
                // Предустановка скидки по контрагенту
                presetDiscountControls(contractor.getDiscountValue());


                // Устаковка ID контрагента для документа
                document.setRecipientContractId(contractor.getId());
                // Установка скидки по контрагенту для документа
                document.setDiscountValue((int) contractor.getDiscountValue());
                // Если контрагент не резидент страны - делаем документ экспортным
                if (contractor.getResident() == 0) {
                    document.setDocumentExport(1);
                } else {
                    document.setDocumentExport(0);
                }
                // Предустановка контрола признака экспортного документа
                presetExportControls(document.getDocumentExport());
                getRecipientContracts(contractor.getId());
                if (cbRecipientContract.getItemCount() < 1) {
                    JOptionPane.showMessageDialog(null,
                            "У выбранного получателя отсутствуют договора", "Ошибка!!!",
                            JOptionPane.ERROR_MESSAGE);
                    cbRecipientContract.requestFocusInWindow();
                }

                // Предустановка активного договора
                if (contractor.getContractId() != null) {
                    if (contractor.getContractId() > 0) {
                        presetRecipientContract(contractor.getContractId());
                    }
                }
            }
        });


        // Выбор в комбике типов скидки
        cbDiscountType.addActionListener(e -> {
            if (cbDiscountType.getSelectedIndex() > 0) {
                tfDiscountValue.setEnabled(true);
            } else {
                tfDiscountValue.setEnabled(false);
            }
        });


        // Выбор в комбике типов торговой надбавки
        cbTradeMarkupType.addActionListener(e -> {
            if (cbTradeMarkupType.getSelectedIndex() > 0) {
                tfValueTradeMarkup.setEnabled(true);
            } else {
                tfValueTradeMarkup.setEnabled(false);
            }
        });

        // Выбор в комбике типов НДС
        cbVatType.addActionListener(e -> {
            if (cbVatType.getSelectedIndex() == 0 || cbVatType.getSelectedIndex() == 1) {
                tfVatValue.setEnabled(true);
            } else {
                tfVatValue.setEnabled(false);
            }
        });

        // Выбор в комбике валюты
        cbpCurrency.addComboBoxActionListener(e -> {
            if (cbpCurrency.getSelectedItem() != null) {
                presetCurrencyControls(cbpCurrency.getSelectedItem());
                prepareKeyDates(null);
                updateLabelsValue(null);
                updateSaleCurrencyRate();
                updateFixedCurrencyRate(null);
            }
        });

        btnFixedCurrencyRefresh.addActionListener(e -> {
            //   prepareKeyDates(null);
            updateLabelsValue(DateUtils.getDateByStringValue("01.01.2024"));
            updateFixedCurrencyRateByOrder();
            //  updateFixedCurrencyRate(null);
        });

        dpCurrencyOriginDate.addActionListener(e -> {
            float rate = documentManager.getCurrencyRateValue(
                    cbpCurrency.getSelectedItem().getId(), dpCurrencyOriginDate.getDate(), false);
            lblFixedCurrency.setText(String.format("Курс валют на дату(%sг.)", DateUtils.getNormalDateFormat(dpCurrencyOriginDate.getDate())));
            tfFixedCurrency.setText(String.valueOf(rate));
        });

        btnFixedCurrencyQuerter.addActionListener(a -> {
            //Date date = DateUtils.getDateByStringValue("01.01.2020");
            Date date = null;
            try {
                fixedDate = DateUtils.getFirstDay(dpDocumentDate.getDate());
            } catch (Exception e) {
                System.err.println("Ошибка ввода даты");
            }

            prepareKeyDates(date);
            updateLabelsValue(date);
            updateFixedCurrencyRate(date);
        });

        btnSaleCurrencyRefresh.addActionListener(e -> updateSaleCurrencyRate());

        dpDocumentDate.addActionListener(a -> {
                    prepareKeyDates(null);
                    updateLabelsValue(null);
                    updateFixedCurrencyRate(null);
                }
        );

        checkBoxPrepayment.addActionListener(e -> {
            if (document != null) {
                document.setPrepayment(checkBoxPrepayment.isSelected());

                if (checkBoxPrepayment.isSelected()) {
                    dpPrepaymentDate.setEditable(true);

                    tfFixedCurrency.setEnabled(true);
                    btnFixedCurrencyRefresh.setEnabled(true);
                    System.out.println(tfFixedCurrency.getText());

                    if (document.getPrepaymentDate() == null) {
                        dpPrepaymentDate.setDate(new Date());
                    } else {
                        dpPrepaymentDate.setDate(document.getDocumentDate());
                    }


                } else {
                    dpPrepaymentDate.setEditable(false);

                    tfFixedCurrency.setEnabled(false);
                    btnFixedCurrencyRefresh.setEnabled(false);
                }

                prepareKeyDates(null);
                updateLabelsValue(null);
                updateSaleCurrencyRate();
            }
        });

        checkBoxExport.addActionListener(e -> {
            if (document != null) {
                //  chbNotVarietal.setEnabled(checkBoxExport.isSelected());
                cbCalculationFactor.setEnabled(checkBoxExport.isSelected());
                if (checkBoxExport.isSelected()) {
                    cbCertificate.setSelectedIndex(2);
                    cbCertificateGGR.setSelectedIndex(2);
                } else {
                    cbCertificate.setSelectedIndex(1);
                    cbCertificateGGR.setSelectedIndex(1);
                }
            }
        });

        chbNotVarietal.addActionListener(e -> {
            if (chbNotVarietal.isSelected()) {
                tfNotVarietal.setEnabled(true);

            } else {
                tfNotVarietal.setEnabled(false);
            }
        });

        cbCalculationFactor.addActionListener(e -> {
            if (document != null) {
                if (cbCalculationFactor.isSelected()) {
                    tfCalculationFactor.setEditable(true);
                } else {
                    tfCalculationFactor.setEditable(false);
                }
            }
        });

        dpPrepaymentDate.addActionListener(a -> {

            prepareKeyDates(null);
            updateLabelsValue(null);
            updateSaleCurrencyRate();
        });

        dpPrepaymentDate.getEditor().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(final FocusEvent e) {

            }

            @Override
            public void focusLost(final FocusEvent e) {
                try {
                    dpPrepaymentDate.setDate(DateUtils.getDateByStringValueSimple(dpPrepaymentDate.getEditor().getText()));
                    prepareKeyDates(null);
                    updateLabelsValue(null);
                    updateSaleCurrencyRate();
                    dpPrepaymentDate.getEditor().setBackground(new Color(255, 255, 255));
                } catch (Exception ex) {
                    System.err.println("Дата указана неверно");
                    dpPrepaymentDate.getEditor().setBackground(Settings.COLOR_WRONG_VALUE);
                }
            }
        });

        cbpOperationType.addComboBoxActionListener(a -> {
            if (cbpOperationType.getItemCount() > 0) {
                if (cbTradeMarkupType.getItemCount() == 0) {
                    return;
                }

                InvoiceType inv = cbpOperationType.getSelectedItem();

                if (inv != null) {
                    if (inv.getName().equals(InvoiceType.DOCUMENT_SALE_RETAIL) ||
                            inv.getName().equals(InvoiceType.DOCUMENT_REFUND_RETAIL)) {
                        cbTradeMarkupType.setSelectedIndex(2);
                    } else {
                        cbTradeMarkupType.setSelectedIndex(0);
                    }
                }
            }

        });

        tfVatValue.addActionListener(a -> {
                    final InvoiceType invoice = cbpOperationType.getSelectedItem();
                    if (invoice.getName().trim().equals(InvoiceType.DOCUMENT_SALE_RETAIL)) {
                        float value = Float.valueOf((String) tfVatValue.getValue());
                        if (value == 20.0f) {
                            cbTradeMarkupType.setSelectedIndex(2);
                            tfValueTradeMarkup.setText("55.0");
                        } else if (value == 10.0) {
                            cbTradeMarkupType.setSelectedIndex(2);
                            tfValueTradeMarkup.setText("45.0");
                        }
                    }
                }
        );
    }

    private void updateFixedCurrencyRate(Date date) {
        if (date == null) {
            prepareKeyDates(null);
            float rate = 0f;
            CurrencyEntityMarch8 curr = cbpCurrency.getSelectedItem();
            if (curr != null) {
                rate = documentManager.getCurrencyRateValue(curr.getId(), fixedDate, true);
            }
            tfFixedCurrency.setText(String.valueOf(rate));
        } else {
            prepareKeyDates(date);
            float rate = 0f;
            CurrencyEntityMarch8 curr = cbpCurrency.getSelectedItem();
            if (curr != null) {
                rate = documentManager.getCurrencyRateValue(curr.getId(), date, true);
            }
            tfFixedCurrency.setText(String.valueOf(rate));
        }
    }

    private void updateSaleCurrencyRate() {
        prepareKeyDates(null);
        float rate = documentManager.getCurrencyRateValue(cbpCurrency.getSelectedItem().getId(), saleDate, false);

        // Деноминируем курс, на всякий случай
        if (rate > 100) {
            rate = rate / 100;
        }

        tfSaleCurrency.setText(String.valueOf(rate));
    }

    @Override
    public void defaultFillingData() {
        // Если документ null выходим
        if (document == null) {
            return;
        }

        // Номер документа
        tfDocumentNumber.setText(document.getDocumentNumber().trim());
        //дата документа
        dpDocumentDate.setDate(document.getDocumentDate());
        // Тип документа
        cbpOperationType.presetSimple(document.getDocumentType());


        if (document.getRecipientCode() != 0) {
            // Код контрагента
            tfRecipientCode.setText(String.valueOf(document.getRecipientCode()));
            //Наименование контрагента
            tfpRecipient.preset(documentManager.getContractorViewByCode(document.getRecipientCode()));

            // Контракты и договора по контрагенту
            ContractorEntityView contractor = documentManager.getContractorViewByCode(document.getRecipientCode());
            getRecipientContracts(contractor.getId());
            if (document.getRecipientContractId() == null) {
                presetRecipientContract(0);
            } else {
                presetRecipientContract(document.getRecipientContractId());
            }
        } else {
            tfRecipientCode.setText("");
        }
        // Код отправителя документа
        tfpSender.preset(documentManager.getContractorViewByCode(-1));
        tfSenderCode.setText("737");
        // Код отправителя документа
        document.setSenderCode(737);

        // Код валюты
        CurrencyEntityMarch8 currentCurrency = new CurrencyEntityMarch8();
        currentCurrency.setId(document.getCurrencyId());

        // ПРедустановка контролов валюты
        presetCurrencyControls(currentCurrency);

        // Предустановка скидок
        presetDiscountControls(document.getDiscountValue());

        // Предустановка тогровых надбавок
        presetTradeMarkControls(document.getTradeMarkType(), document.getTradeMarkValue());


        // Сертификат страны
        cbCertificate.setSelectedIndex(document.getCertificateId());
        //Сертификат для страны получателя
        cbCertificateGGR.setSelectedIndex(document.getCertificateGGRId());

        presetVatControls(document.getDocumentVATType(), document.getDocumentVatValue());

        checkBoxPrepayment.setSelected(document.isPrepayment());

        chbNotVarietal.setSelected(document.isNotVarietal());
        if (document.isNotVarietal()) {
            tfNotVarietal.setValue(document.getPriceReduction3Grade());
            tfNotVarietal.setEnabled(true);
        } else {
            tfNotVarietal.setValue(0);
            tfNotVarietal.setEnabled(false);
        }

        float factor = document.getCalculationFactor();
        tfCalculationFactor.setText(String.valueOf(factor));
        if (factor > 1.0 || factor < 1.0) {
            cbCalculationFactor.setSelected(true);
            tfCalculationFactor.setEditable(true);
        } else {
            cbCalculationFactor.setSelected(false);
            tfCalculationFactor.setEditable(false);
        }

        if (document.isPrepayment()) {
            dpPrepaymentDate.setEditable(true);
            dpPrepaymentDate.setDate(document.getPrepaymentDate());
        } else {
            dpPrepaymentDate.setEditable(false);

        }

        /*if(SaleDocumentManager.isRetailDocument(document)){
            cbTradeMarkupType.setEnabled(true);
        }else{
            cbTradeMarkupType.setEnabled(false);
        }*/

        // Предустановка значений валюты
        presetCurrencyValues();

        // Экспортный документ
        presetExportControls(document.getDocumentExport());

        if (isNew) {
            // Сертификат страны
            cbCertificate.setSelectedIndex(1);
            //Сертификат для страны получателя
            cbCertificateGGR.setSelectedIndex(1);

            cbpCurrency.getComboBox().setSelectedIndex(0);
        }

        checkBoxGiveTake.setSelected(document.isGiveTake());
        if (checkBoxGiveTake.isSelected()) {
            cbServiceType.setSelectedIndex(document.getServiceType());
            cbServiceType.setEnabled(true);
        } else {
            cbServiceType.setSelectedIndex(0);
            cbServiceType.setEnabled(false);
        }
        if (Objects.nonNull(document.getOriginCurrencyType())) {
            lblFixedCurrency.setText(document.getOriginCurrencyType());
        }
    }

    /**
     * Предустановки для контролов НДС
     *
     * @param vatType  тип НДС
     * @param vatValue ставка НДС
     */
    private void presetVatControls(final int vatType, final float vatValue) {
        switch (vatType) {
            case 0:
                tfVatValue.setText(String.valueOf(vatValue));
                tfVatValue.setEnabled(true);
                break;

            case 1:
                tfVatValue.setText(String.valueOf(vatValue));
                tfVatValue.setEnabled(true);
                break;

            default:
                tfVatValue.setText("0.0");
                tfVatValue.setEnabled(false);
                break;
        }
        cbVatType.setSelectedIndex(vatType);
    }

    private void presetCurrencyValues() {

        if (document != null) {
            // Документ не экспортный - выходим, ничего не меняя
            if (document.getDocumentExport() != 1) {
                return;
            }
            // Установим значение контролов курса
            tfFixedCurrency.setText(String.valueOf(document.getCurrencyRateFixed()));
            tfSaleCurrency.setText(String.valueOf(document.getCurrencyRateSale()));

            // Определим ключевые даты документа
            prepareKeyDates(null);
            // Если валюта отгрузки не Бел.Рубль
            if (document.getCurrencyId() > 1) {
/*                // Если установлен курс
                lblFixedCurrency.setText("Курс на начало месяца(" +
                        (DateUtils.getNormalDateFormat(fixedDate)) + "г.)");*/
                //todo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
                if (isQuarterCurrencyDate(String.valueOf(document.getCurrencyRateFixed()))) {

                    updateLabelsValue(DateUtils
                            .getDateByStringValue(new CourseDateByOrderDB()
                                    .getCourseByOrderStr()));
                } else {
                    updateLabelsValue(null);
                }
            }
        }
    }

    /**
     * Предустановка состояний контролов валюты
     *
     * @param currentCurrency валюта
     */
    private void presetCurrencyControls(final CurrencyEntityMarch8 currentCurrency) {
        // Предустановка комбика валюты
        cbpCurrency.preset(currentCurrency);


        int currencyID = currentCurrency.getId();
        // Тут будем получать курсы валюты на даты
        // Потому как ID валют в разных базах разные - приходится вертеться
        if (tfFixedCurrency.getText().trim().equals("")) {
            tfFixedCurrency.setText("1.0");
        }

        if (tfSaleCurrency.getText().trim().equals("")) {
            tfSaleCurrency.setText("1.0");
        }

        if (currencyID > 1) {
            pCurrencyValues.setVisible(true);
        } else {
            pCurrencyValues.setVisible(false);
        }
    }

    /**
     * Метод для предустановки скидки по документу
     *
     * @param discountValue величина скидки
     */
    private void presetDiscountControls(float discountValue) {
        if (discountValue > 0) {
            cbDiscountType.setSelectedIndex(document.getDiscountType());
            tfDiscountValue.setEnabled(true);
        } else {
            cbDiscountType.setSelectedIndex(0);
            tfDiscountValue.setEnabled(false);
        }
        tfDiscountValue.setText(String.valueOf(discountValue));
    }

    /**
     * Метод для предустановки торговой надбавки
     *
     * @param tradeMarkType  тип торговой надбавки
     * @param tradeMarkValue размер торговой надбавки
     */
    private void presetTradeMarkControls(int tradeMarkType, float tradeMarkValue) {
        switch (tradeMarkType) {
            case 1:
                tfValueTradeMarkup.setText(String.valueOf(tradeMarkValue));
                tfValueTradeMarkup.setEnabled(true);
                break;
            case 2:
                tfValueTradeMarkup.setText(String.valueOf(tradeMarkValue));
                tfValueTradeMarkup.setEnabled(true);
                break;
            case 3:
                tfValueTradeMarkup.setText(String.valueOf(tradeMarkValue));
                tfValueTradeMarkup.setEnabled(true);
                break;
            default:
                tfValueTradeMarkup.setText("0");
                tfValueTradeMarkup.setEnabled(false);
                break;
        }
        cbTradeMarkupType.setSelectedIndex(tradeMarkType);
    }


    /**
     * Предустановка признака экспортного документа
     *
     * @param export 1 - документ экспортный, 0 - документ не экспортный
     */
    private void presetExportControls(int export) {
        if (export == 1) {
            checkBoxExport.setSelected(true);
            //chbNotVarietal.setEnabled(true);
            cbCalculationFactor.setEnabled(true);
        } else {
            checkBoxExport.setSelected(false);
            //chbNotVarietal.setEnabled(false);
            cbCalculationFactor.setEnabled(false);
        }
    }

    /**
     * Метод подготавливает ключевые даты документа для расчетов
     * Дата создания документа для определения фиксированного курса
     * и дата отгрузки/предоплаты для определения соответствующегно курса
     */
    private void prepareKeyDates(Date date) {
        if (document != null) {

            fixedDate = null;

            try {
                if (date != null) {
                    fixedDate = DateUtils.getFirstDay(date);
                } else {
                    fixedDate = DateUtils.getFirstDay(dpDocumentDate.getDate());
                }
            } catch (Exception e) {
                System.err.println("Ошибка ввода даты");
            }

            saleDate = null;

            // Если документ по предоплате
            if (document.getDocumentSaleDate() != null) {
                try {
                    if (checkBoxPrepayment.isSelected()) {
                        saleDate = dpPrepaymentDate.getDate();
                    } else {
                        saleDate = document.getDocumentSaleDate();
                    }
                } catch (Exception e) {
                    System.err.println("Ошибка ввода даты");
                }
            } else {
                if (checkBoxPrepayment.isSelected()) {
                    saleDate = dpPrepaymentDate.getDate();
                } else {
                    saleDate = null;
                }
            }
        }
    }

    /**
     * Метод обновляет текстовые метки согласно параметров документа
     */
    private void updateLabelsValue(Date date) {
        // Если возвратный документ
        String type = document.getDocumentType();
        System.out.println("Тип документа " + type);
        if (type.equals(InvoiceType.DOCUMENT_REFUND_BUYER) || type.equals(InvoiceType.DOCUMENT_REFUND_BUYER)) {
            if (saleDate != null) {
                lblSaleCurrency.setText("Курс на момент возврата(" +
                        (DateUtils.getNormalDateFormat(saleDate)) + "г.)");


            } else {
                lblSaleCurrency.setText("Курс на момент возврата(" +
                        "неизвестно" + ")");
            }

            tfSaleCurrency.setEnabled(true);
            btnSaleCurrencyRefresh.setEnabled(true);

            lblFixedCurrency.setText("Курс на момент отгрузки");
            tfFixedCurrency.setEnabled(true);
            btnFixedCurrencyRefresh.setEnabled(true);

        } else {


            if (saleDate != null) {

                if (document.isPrepayment()) {
                    lblSaleCurrency.setText("Курс на момент предоплаты(" +
                            (DateUtils.getNormalDateFormat(saleDate)) + "г.)");

                } else {
                    lblSaleCurrency.setText("Курс на момент отгрузки(" +
                            (DateUtils.getNormalDateFormat(saleDate)) + "г.)");
                }

                tfSaleCurrency.setEnabled(true);
                btnSaleCurrencyRefresh.setEnabled(true);
            } else {

                if (document.isPrepayment()) {
                    lblSaleCurrency.setText("Дата предоплаты не определена");
                    btnSaleCurrencyRefresh.setEnabled(false);
                } else {
                    lblSaleCurrency.setText("Дата отгрузки не определена");
                    btnSaleCurrencyRefresh.setEnabled(false);
                }
            }
            if (fixedDate != null) {
                if (date != null) {
                    lblFixedCurrency.setText("Курс текущего квартала(" +
                            (DateUtils.getNormalDateFormat(DateUtils.getDateByStringValue(new CourseDateByOrderDB().getCourseByOrderStr()))) + "г.)");
                    fixedDate = DateUtils.getDateByStringValue(new CourseDateByOrderDB().getCourseByOrderStr());
                    tfFixedCurrency.setEnabled(true);
                    btnFixedCurrencyRefresh.setEnabled(true);
                } else {
                    lblFixedCurrency.setText("Курс на начало месяца(" +
                            (DateUtils.getNormalDateFormat(fixedDate)) + "г.)");

                    tfFixedCurrency.setEnabled(true);
                    btnFixedCurrencyRefresh.setEnabled(true);
                }
            } else {
                lblFixedCurrency.setText("Расчетная дата не определена");
                tfFixedCurrency.setEnabled(false);
                btnFixedCurrencyRefresh.setEnabled(false);
            }
        }
    }

    /**
     * Получает договора и контракты по контрагенту и заполняет комбик
     *
     * @param contractorId ID контрагента
     */
    @SuppressWarnings("unchecked")
    private void getRecipientContracts(int contractorId) {
        cbRecipientContract.setModel(new DefaultComboBoxModel(documentManager.getContractsByContractorId(contractorId).toArray()));
        cbRecipientContract.setSelectedIndex(-1);
    }

    private void presetRecipientContract(int contractId) {

        for (int i = 0; i < cbRecipientContract.getItemCount(); i++) {
            final ContractEntity itemAt = cbRecipientContract.getItemAt(i);

            if (itemAt.getId() == contractId) {
                cbRecipientContract.setSelectedIndex(i);
                return;
            }
        }
        cbRecipientContract.setSelectedIndex(-1);
    }

    // TODO Временно захардкожена дата курса текущего квартала, по приказу, нужно перенести ее в файл свойств.
    private void updateFixedCurrencyRateByOrder() {
        // prepareKeyDates(null);
        float rate = 0f;
        CurrencyEntityMarch8 curr = cbpCurrency.getSelectedItem();
        if (curr != null) {
            rate = documentManager
                    .getCurrencyRateValue(curr.getId(), DateUtils.getDateByStringValue(new CourseDateByOrderDB().getCourseByOrderStr()), true);
        }
        tfFixedCurrency.setText(String.valueOf(rate));
    }

    private boolean isQuarterCurrencyDate(String currencyRate) {
        float rate = 0f;
        CurrencyEntityMarch8 curr = cbpCurrency.getSelectedItem();
        if (curr != null) {
            rate = documentManager
                    .getCurrencyRateValue(curr.getId(), DateUtils.getDateByStringValue(new CourseDateByOrderDB().getCourseByOrderStr()), true);
        }
        String quarterRate = String.valueOf(rate);
        return currencyRate.equals(quarterRate);
    }


}