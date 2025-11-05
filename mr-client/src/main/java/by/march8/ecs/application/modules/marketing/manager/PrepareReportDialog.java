package by.march8.ecs.application.modules.marketing.manager;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.marketing.model.ProductionCatalogHelper;
import by.march8.ecs.application.modules.marketing.model.ProductionCatalogReportPreset;
import by.march8.ecs.application.modules.references.general.CurrencyDAO;
import by.march8.entities.readonly.CurrencyEntityMarch8;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class PrepareReportDialog extends BasePickDialog {
    private UCDatePeriodPicker datePeriodPicker;
    private UCTextField tfContractorCode;
    private JCheckBox chbSelectSaveDir;
    private JPanel pCurrency;


    private JLabel lblCurrency;
    private ComboBoxPanel<CurrencyEntityMarch8> cbpCurrency;
    private JPanel pCurrencyValues;

    private JLabel lblCommonCurrency;
    private UCTextField tfCommonCurrency;
    private UCDatePicker dpCommonCurrency;
    private JButton btnCommonCurrency;
    private JLabel lblReportType;
    private JComboBox<ProductionCatalogReportTypeItem> cbReportType;

    private ProductionCatalogReportPreset preset;

    public PrepareReportDialog(MainController controller) {
        super(controller);
        init();
        initEvents();
    }

    private void init() {
        setFrameSize(new Dimension(400, 250));
        getToolBar().setVisible(false);
        setTitle("Параметры выгрузки документа");
        Container c = getCenterContentPanel();
        c.setLayout(new MigLayout());

        cbpCurrency = new ComboBoxPanel<>(controller, MarchReferencesType.CURRENCY_OLD, false);
        cbpCurrency.setEasy(true);
        cbpCurrency.getSelectButton().setVisible(false);

        pCurrencyValues = new JPanel(new MigLayout());
        pCurrencyValues.setPreferredSize(new Dimension(400, 20));


        cbReportType = new JComboBox<>();
        ProductionCatalogHelper.prepareReportTypes(cbReportType);

        lblReportType = new JLabel("Тип документа документа");
        lblCurrency = new JLabel("Валюта документа");
        lblCommonCurrency = new JLabel("Курс валюты");
        tfCommonCurrency = new UCTextField();
        tfCommonCurrency.setComponentParams(lblCommonCurrency, Float.class, 4);
        tfCommonCurrency.getComponent().setEnabled(false);
        dpCommonCurrency = new UCDatePicker(new Date());
        btnCommonCurrency = new JButton();
        btnCommonCurrency.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/refresh_16.png", "Refresh"));

        pCurrencyValues.add(lblCommonCurrency, "align left, height 20:20, width 100:20:100");
        pCurrencyValues.add(tfCommonCurrency, "height 20:20, width 80:20:80");
        pCurrencyValues.add(dpCommonCurrency, "height 20:20, width 110:20:110");
        pCurrencyValues.add(btnCommonCurrency, "align left, height 18:18, width 18:18:18, wrap");

        c.add(lblReportType, "height 20:20, width 150:20:150, span 2, wrap");
        c.add(cbReportType, "height 20:20, width 350:20:350, span 2, wrap");
        c.add(new JPanel(), "height 10:10,  wrap");
        c.add(lblCurrency, "wrap");
        c.add(cbpCurrency, "height 20:20, width 150:20:150, span 2, wrap");
        c.add(pCurrencyValues, "span 4, align left, wrap");

        pCurrencyValues.setVisible(false);
        CurrencyDAO.loadCurrencyTypesToComboBox(cbpCurrency.getComboBox());
    }

    private void initEvents() {
        cbpCurrency.addComboBoxActionListener(e -> {
            if (cbpCurrency.getSelectedItem() != null) {
                CurrencyEntityMarch8 currency = cbpCurrency.getSelectedItem();
                if (currency != null) {
                    if (currency.getId() > 1) {
                        pCurrencyValues.setVisible(true);
                        dpCommonCurrency.setDate(new Date());
                    } else {
                        pCurrencyValues.setVisible(false);
                    }
                    updateCurrencyControlsContent();
                }
            }
        });

        dpCommonCurrency.addActionListener(a -> {
            updateCurrencyControlsContent();
        });

        btnCommonCurrency.addActionListener(a -> {
            updateCurrencyControlsContent();
        });
    }

    public ProductionCatalogReportPreset showDialog(ProductionCatalogReportPreset preset_) {
        preset = preset_;
        if (preset == null) {
            preset = new ProductionCatalogReportPreset();
        }

        if (showModalFrame()) {
            preset.setCurrencyType(cbpCurrency.getSelectedItem());
            preset.setCurrencyDate(dpCommonCurrency.getDate());
            preset.setCurrencyRate(tfCommonCurrency.getValueDouble());
            preset.setReportType((ProductionCatalogReportTypeItem) cbReportType.getSelectedItem());
            if (preset.getCurrencyType().getId() == 1) {
                preset.setCurrencyRate(1);
            }
            if (preset.getCurrencyType().getId() == 2) {
                preset.setCurrencyRate(preset.getCurrencyRate() / 100);
            }
            return preset;
        } else {
            return null;
        }
    }

    private void updateCurrencyControlsContent() {
        if (cbpCurrency.getSelectedItem() != null) {
            CurrencyEntityMarch8 currency = cbpCurrency.getSelectedItem();
            if (currency.getId() > 1) {
                tfCommonCurrency.setText(String.valueOf(CurrencyDAO.getCurrencyRateForCurrency(currency, dpCommonCurrency.getDate())));
            } else {
                tfCommonCurrency.setText("1.0");
            }
        }
    }
}
