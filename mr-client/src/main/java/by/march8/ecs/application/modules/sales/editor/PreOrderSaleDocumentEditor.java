package by.march8.ecs.application.modules.sales.editor;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxHelper;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextFieldPanel;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.currency.mode.CurrencyRateMonitorMode;
import by.march8.ecs.application.modules.references.general.ContractorDAO;
import by.march8.ecs.application.modules.references.general.CurrencyDAO;
import by.march8.ecs.application.modules.sales.manager.ContractorSelectorDialog;
import by.march8.ecs.application.modules.sales.model.PreOrderSaleDocumentHelper;
import by.march8.entities.readonly.ContractEntity;
import by.march8.entities.readonly.ContractorEntityView;
import by.march8.entities.readonly.CurrencyEntityMarch8;
import by.march8.entities.sales.PreOrderSaleDocumentBase;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

/**
 * Редактор основной информации по документу
 *
 * @author Andy on 04.02.2019.
 */
@SuppressWarnings("all")
public class PreOrderSaleDocumentEditor extends EditingPane {

    private PreOrderSaleDocumentBase source;
    private ContractorEntityView contractor;

    private JLabel lblDocumentDate;
    private UCDatePicker dpDocumentDate;

    private JLabel lblDocumentNumber;
    private UCTextField tfDocumentNumber;

    private JLabel lblOperationType;
    private ComboBoxPanel<String> cbpOperationType;

    private JLabel lblRecipient;
    private UCTextFieldPanel<String> tfpContractor;
    private UCTextField tfContractorCode;
    private JLabel lblContract;
    private JComboBox<ContractEntity> cbContract;


    private JLabel lblDiscount;
    private JComboBox<String> cbDiscountType;
    private UCTextField tfDiscountValue;

    private JLabel lblVat;
    private JComboBox<String> cbVatType;
    private UCTextField tfVatValue;

    private JLabel lblTradeMarkup;
    private JComboBox<String> cbTradeMarkupType;
    private UCTextField tfTradeMarkupValue;

    private JLabel lblCurrency;
    private ComboBoxPanel<CurrencyEntityMarch8> cbpCurrency;
    private JPanel pCurrencyValues;

    private JLabel lblCommonCurrency;
    private UCTextField tfCommonCurrency;
    private UCDatePicker dpCommonCurrency;
    private JButton btnCommonCurrency;

    private JLabel lblAdditionCurrency;
    private UCTextField tfAdditionCurrency;
    private UCDatePicker dpAdditionCurrency;
    private JButton btnAdditionCurrency;

    private JComboBox<String> cbGradeMarkdown;
    private UCTextField tfGradeMarkdownValue;
    private JButton btnCurrency;

    private JCheckBox chbInvoiceDocument;

    public PreOrderSaleDocumentEditor(final FrameViewPort frameViewPort) {
        setPreferredSize(new Dimension(640, 480));
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

        chbInvoiceDocument = new JCheckBox("Счет-фактура");
        add(new JLabel());
        add(chbInvoiceDocument, "height 20:20,width 200:20:200, wrap");

        // Вид операции
        lblOperationType = new JLabel("Вид операции");
        cbpOperationType = new ComboBoxPanel<>(controller, MarchReferencesType.INVOICE_TYPE, false);
        cbpOperationType.setEasy(true);
        //add(lblOperationType);
        //add(cbpOperationType, "height 20:20, width 260:40:260, wrap");

        add(new JPanel(), "height 10:10,  wrap");
        // Получатель
        lblRecipient = new JLabel("Получатель");
        tfpContractor = new UCTextFieldPanel<>(controller, MarchReferencesType.CONTRACTOR_OLD);
        tfContractorCode = new UCTextField();
        tfContractorCode.setEnabled(false);
        add(lblRecipient);
        add(tfpContractor, "height 20:20, width 360:20:360,span 2");
        add(tfContractorCode, "height 20:20,width 100:20:100,wrap");
        lblContract = new JLabel("Договор ");
        cbContract = new JComboBox<>();
        add(lblContract);
        add(cbContract, "height 20:20, width 360:20:360,span 2,wrap");
        add(new JPanel(), "height 10:10,  wrap");

        // НДС
        lblVat = new JLabel("Ставка НДС");
        cbVatType = new JComboBox<>();
        tfVatValue = new UCTextField();
        tfVatValue.setComponentParams(lblVat, Float.class, 0);
        add(lblVat);
        add(cbVatType, "height 20:20, width 360:20:360, span 2");
        add(tfVatValue, "height 20:20,width 100:20:100,wrap");
        add(new JPanel(), "height 10:10,  wrap");

        // Скидка
        lblDiscount = new JLabel("Скидка");
        cbDiscountType = new JComboBox<>();
        tfDiscountValue = new UCTextField();
        tfDiscountValue.setComponentParams(lblDiscount, Float.class, 2);
        add(lblDiscount);
        add(cbDiscountType, "height 20:20, width 360:20:360, span 2");
        add(tfDiscountValue, "height 20:20,width 100:20:100,wrap");
        add(new JPanel(), "height 10:10,  wrap");

        // Торговая надбавка
        lblTradeMarkup = new JLabel("Торговая надбавка");
        cbTradeMarkupType = new JComboBox<>();
        tfTradeMarkupValue = new UCTextField();
        tfTradeMarkupValue.setComponentParams(lblTradeMarkup, Float.class, 0);
        add(lblTradeMarkup);
        add(cbTradeMarkupType, "height 20:20, width 360:20:360, span 2");
        add(tfTradeMarkupValue, "height 20:20,width 100:20:100,wrap");
        add(new JPanel(), "height 10:10,  wrap");

        // Уценка
        JLabel lblGradeMarkdown = new JLabel("Уценка");
        cbGradeMarkdown = new JComboBox<>();
        tfGradeMarkdownValue = new UCTextField();
        tfGradeMarkdownValue.setComponentParams(lblDiscount, Float.class, 2);
        add(lblGradeMarkdown);
        add(cbGradeMarkdown, "height 20:20, width 360:20:360, span 2");
        add(tfGradeMarkdownValue, "height 20:20,width 100:20:100,wrap");
        add(new JPanel(), "height 10:10,  wrap");


        // Валюта
        lblCurrency = new JLabel("Валюта документа");
        cbpCurrency = new ComboBoxPanel<>(controller, MarchReferencesType.CURRENCY_OLD, false);
        cbpCurrency.setEasy(true);
        cbpCurrency.getSelectButton().setVisible(false);

        pCurrencyValues = new JPanel(new MigLayout());
        pCurrencyValues.setPreferredSize(new Dimension(400, 50));

        lblCommonCurrency = new JLabel("Курс основной");
        lblAdditionCurrency = new JLabel("Курс дополнительный");

        tfCommonCurrency = new UCTextField();
        tfCommonCurrency.setEditable(false);
        tfCommonCurrency.setComponentParams(lblCommonCurrency, Float.class, 4);

        tfAdditionCurrency = new UCTextField();
        tfAdditionCurrency.setComponentParams(lblAdditionCurrency, Float.class, 4);
        tfAdditionCurrency.setEditable(false);

        btnCommonCurrency = new JButton();
        btnAdditionCurrency = new JButton();

        btnCurrency = new JButton("Монитор курсов валюты");

        btnCommonCurrency.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/refresh_16.png", "Refresh"));
        btnAdditionCurrency.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/refresh_16.png", "Refresh"));

        dpCommonCurrency = new UCDatePicker(new Date());
        dpCommonCurrency.getEditor().setEditable(false);

        dpAdditionCurrency = new UCDatePicker(new Date());
        dpAdditionCurrency.getEditor().setEditable(false);

        pCurrencyValues.add(lblCommonCurrency, "align left, height 20:20, width 150:20:150");
        pCurrencyValues.add(tfCommonCurrency, "height 20:20, width 80:20:80");
        pCurrencyValues.add(dpCommonCurrency, "height 20:20, width 110:20:110");
        pCurrencyValues.add(btnCommonCurrency, "align left, height 18:18, width 18:18:18, wrap");

        pCurrencyValues.add(lblAdditionCurrency, "align left, height 20:20, width 150:20:150");
        pCurrencyValues.add(tfAdditionCurrency, "height 20:20, width 80:20:80 ");
        pCurrencyValues.add(dpAdditionCurrency, "height 20:20, width 110:20:110");
        pCurrencyValues.add(btnAdditionCurrency, "align left, height 18:18, width 18:18:18, wrap");
        //pCurrencyValues.add(btnCurrency, "height 20:20, width 180:20:180,span 3, align right, wrap");

        add(lblCurrency);
        add(cbpCurrency, "height 20:20, width 150:20:150, span 2, wrap");
        add(pCurrencyValues, "span 4, align left, wrap");

        add(new JPanel(), "height 10:10,  wrap");

        CurrencyDAO.loadCurrencyTypesToComboBox(cbpCurrency.getComboBox());

        // Загрузка типов НДС
        PreOrderSaleDocumentHelper.getVatType(cbVatType);
        // Загрузка типов скидки
        PreOrderSaleDocumentHelper.getDiscountType(cbDiscountType);
        // Загрузка типов торговой надбавки
        PreOrderSaleDocumentHelper.getTradeMarkType(cbTradeMarkupType);
        // Загрузка типов Уценки товара
        PreOrderSaleDocumentHelper.getGradeMarkdownType(cbGradeMarkdown);
    }


    @Override
    public Object getSourceEntity() {
        source.setDocumentNumber(tfDocumentNumber.getText().trim());
        source.setDocumentDate(dpDocumentDate.getDate());
        if (chbInvoiceDocument.isSelected()) {
            source.setDocumentType(2);
        } else {
            source.setDocumentType(1);
        }

        source.setContractId(((ContractEntity) cbContract.getSelectedItem()).getId());
        source.setContractorId(contractor.getId());

        if (cbVatType.getSelectedIndex() > 0) {
            source.setVatType(cbVatType.getSelectedIndex());
            source.setVatValue(tfVatValue.getValueDouble());
        } else {
            source.setVatType(0);
            source.setVatValue(0);
        }

        if (cbDiscountType.getSelectedIndex() > 0) {
            source.setDiscountType(cbDiscountType.getSelectedIndex());
            source.setDiscountValue(tfDiscountValue.getValueDouble());
        } else {
            source.setDiscountType(0);
            source.setDiscountValue(0);
        }

        if (cbTradeMarkupType.getSelectedIndex() > 0) {
            source.setTradeAllowanceType(cbTradeMarkupType.getSelectedIndex());
            source.setTradeAllowanceValue(tfTradeMarkupValue.getValueDouble());
        } else {
            source.setTradeAllowanceType(0);
            source.setTradeAllowanceValue(0);
        }

        if (cbGradeMarkdown.getSelectedIndex() > 0) {
            source.setGradeMarkdownType(cbGradeMarkdown.getSelectedIndex());
            source.setGradeMarkdownValue(tfGradeMarkdownValue.getValueDouble());
        } else {
            source.setGradeMarkdownType(0);
            source.setGradeMarkdownValue(0);
        }

        source.setCurrencyId(cbpCurrency.getSelectedItem().getId());
        if (cbpCurrency.getSelectedItem().getId() == 1) {
            source.setCurrencyRateCommon(1);
            source.setCurrencyRateAddition(1);

            source.setCurrencyRateCommonDate(null);
            source.setCurrencyRateAdditionDate(null);
        } else {
            source.setCurrencyRateCommon(Float.valueOf(tfCommonCurrency.getText()));
            source.setCurrencyRateAddition(Float.valueOf(tfAdditionCurrency.getText()));

            source.setCurrencyRateCommonDate(dpCommonCurrency.getDate());
            source.setCurrencyRateAdditionDate(dpAdditionCurrency.getDate());
        }

        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {

        if (object == null) {
            // Создается новый документ
            source = new PreOrderSaleDocumentBase();
            // Устновки по-умолчанию для нового документа
            dpDocumentDate.setDate(new Date());
            source.setDocumentDate(dpDocumentDate.getDate());
            source.setDocumentNumber("");
            source.setDocumentType(1);

            source.setVatType(0);
            source.setDiscountValue(0);
            source.setTradeAllowanceType(0);
            source.setGradeMarkdownType(0);

            source.setCurrencyId(1);
            cbContract.removeAllItems();
            tfpContractor.clear();
            contractor = null;
        } else {
            source = (PreOrderSaleDocumentBase) object;
            contractor = ContractorDAO.getContractorViewById(source.getContractorId());
        }
    }

    @Override
    public void phaseBeforeShowing() {
        // Загрузка контрагента
        if (contractor != null) {
            tfpContractor.setText(contractor.getName());
            tfContractorCode.setText(String.valueOf(contractor.getCode()));
        } else {
            tfpContractor.setText("");
            tfContractorCode.setText("");
        }

        presetContractList(contractor, cbContract, source.getContractId());

        // Заполнениеконтролов данными
        defaultFillingData();
    }

    // Регистрация событий от контролов

    private void initEvents() {
        tfpContractor.addButtonSelectActionListener(e -> {
            ContractorSelectorDialog dialog = new ContractorSelectorDialog(controller);
            ContractorEntityView select_ = dialog.selectContractor();
            if (select_ != null) {
                contractor = ContractorDAO.getContractorViewById(select_.getId());
                if (contractor != null) {

                    tfpContractor.setText(contractor.getName());
                    tfContractorCode.setText(String.valueOf(contractor.getCode()));

                    presetContractList(contractor, cbContract, contractor.getContractId());
                }
            }
        });

        cbpCurrency.addComboBoxActionListener(e -> {
            if (cbpCurrency.getSelectedItem() != null) {
                CurrencyEntityMarch8 currency = cbpCurrency.getSelectedItem();
                if (currency != null) {
                    if (currency.getId() > 1) {
                        pCurrencyValues.setVisible(true);
                        if (source.getCurrencyRateCommonDate() != null) {
                            dpCommonCurrency.setDate(source.getCurrencyRateCommonDate());
                        } else {
                            dpCommonCurrency.setDate(new Date());
                        }

                        if (source.getCurrencyRateAdditionDate() != null) {
                            dpAdditionCurrency.setDate(source.getCurrencyRateAdditionDate());
                        } else {
                            dpAdditionCurrency.setDate(new Date());
                        }
                        updateCurrencyControlsContent();
                    } else {
                        pCurrencyValues.setVisible(false);
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

        // Выбор в комбике типов скидки
        cbVatType.addActionListener(e -> {
            if (cbVatType.getSelectedIndex() > 0) {
                tfVatValue.setEnabled(true);
            } else {
                tfVatValue.setEnabled(false);
            }
        });

        // Выбор в комбике типов скидки
        cbGradeMarkdown.addActionListener(e -> {
            if (cbGradeMarkdown.getSelectedIndex() > 0) {
                tfGradeMarkdownValue.setEnabled(true);
            } else {
                tfGradeMarkdownValue.setEnabled(false);
            }
        });


        // Выбор в комбике типов торговой надбавки
        cbTradeMarkupType.addActionListener(e -> {
            if (cbTradeMarkupType.getSelectedIndex() > 0) {
                tfTradeMarkupValue.setEnabled(true);
            } else {
                tfTradeMarkupValue.setEnabled(false);
            }
        });

        dpCommonCurrency.addActionListener(a -> {
            updateCurrencyControlsContent();
        });

        dpAdditionCurrency.addActionListener(a -> {
            updateCurrencyControlsContent();
        });

        btnCommonCurrency.addActionListener(a -> {
            updateCurrencyControlsContent();
        });

        btnAdditionCurrency.addActionListener(a -> {
            updateCurrencyControlsContent();
        });

        cbpOperationType.addComboBoxActionListener(a -> {

        });

        btnCurrency.addActionListener(a -> new CurrencyRateMonitorMode(controller));
    }

    @Override
    public void defaultFillingData() {
        // Номер документа
        tfDocumentNumber.setText(source.getDocumentNumber().trim());
        // Дата документа
        dpDocumentDate.setDate(source.getDocumentDate());

        // Предустановка комбиков
        cbVatType.setSelectedIndex(source.getVatType());
        cbDiscountType.setSelectedIndex(source.getDiscountType());
        cbTradeMarkupType.setSelectedIndex(source.getTradeAllowanceType());
        cbGradeMarkdown.setSelectedIndex(source.getGradeMarkdownType());

        tfVatValue.setValue(source.getVatValue());
        tfDiscountValue.setValue(source.getDiscountValue());
        tfTradeMarkupValue.setValue(source.getTradeAllowanceValue());
        tfGradeMarkdownValue.setValue(source.getGradeMarkdownValue());

        ComboBoxHelper.presetById(cbpCurrency.getComboBox(), source.getCurrencyId());
        tfCommonCurrency.setText(String.valueOf(source.getCurrencyRateCommon()));
        tfAdditionCurrency.setText(String.valueOf(source.getCurrencyRateAddition()));

        dpCommonCurrency.setDate(source.getCurrencyRateCommonDate());
        dpAdditionCurrency.setDate(source.getCurrencyRateAdditionDate());

        if (source.getDocumentType() == 1) {
            chbInvoiceDocument.setSelected(false);
        } else {
            chbInvoiceDocument.setSelected(true);
        }
    }

    private void updateCurrencyControlsContent() {
        if (cbpCurrency.getSelectedItem() != null) {
            CurrencyEntityMarch8 currency = cbpCurrency.getSelectedItem();
            if (currency.getId() > 1) {
                tfCommonCurrency.setText(String.valueOf(CurrencyDAO.getCurrencyRateForCurrency(currency, dpCommonCurrency.getDate())));
                tfAdditionCurrency.setText(String.valueOf(CurrencyDAO.getCurrencyRateForCurrency(currency, dpAdditionCurrency.getDate())));
            }
        }
    }

    private void presetContractList(ContractorEntityView entity, JComboBox<ContractEntity> cbContract, int contractId) {
        if (entity != null) {
            List<ContractEntity> list = ContractorDAO.getContractsForContractorByContractorId(entity.getId());
            if (list != null) {
                cbContract.setModel(new DefaultComboBoxModel(list.toArray()));
            } else {
                cbContract.setModel(new DefaultComboBoxModel<ContractEntity>());
            }

            if (contractId > 0) {
                ComboBoxHelper.presetById(cbContract, contractId);
            } else {
                cbContract.setSelectedIndex(-1);
            }
        } else {
            cbContract.setModel(new DefaultComboBoxModel<ContractEntity>());
            cbContract.setSelectedIndex(-1);
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

        // ПОлучатель не указан
        if (tfpContractor.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать получателя документа", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            cbpOperationType.setFocus();
            return false;
        }

        // Не указано основание для отпуска

        if (cbContract.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(null,
                    "У получателя не указан договор", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            cbContract.requestFocusInWindow();
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

        // Указана торговая надбавка, но не указано ее значение
        if (cbTradeMarkupType.getSelectedIndex() > 0) {
            if (Float.valueOf(tfTradeMarkupValue.getText()) == 0) {
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

        // Если валюта указана а курс валюты не определен - предложить обновить курсы
        if (cbpCurrency.getSelectedItem().getId() > 1) {
            if (Float.valueOf(tfCommonCurrency.getText()) <= 1) {
                int dialogResult = Dialogs.showQuestionCurrencyDialog("Курс валюты для [" + cbpCurrency.getSelectedItem().getName().trim() + "] не определен," +
                        " получить курс валюты из базы ? ");
                if (dialogResult == 0) {
                    btnCommonCurrency.doClick();
                }
                btnCommonCurrency.requestFocusInWindow();
                return false;
            }
        }

        return true;
    }
}

