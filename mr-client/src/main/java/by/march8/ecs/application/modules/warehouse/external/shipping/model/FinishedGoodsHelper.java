package by.march8.ecs.application.modules.warehouse.external.shipping.model;

import by.march8.ecs.application.modules.warehouse.external.shipping.reports.ProtocolBelPostReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.ProtocolBelVillesdenReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.ProtocolDororsReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.ProtocolEvrotorgReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.ProtocolGranitexReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.ProtocolGreenRetailReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.ProtocolHyberMallReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.ProtocolProstorReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.ProtocolStandardReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.ProtocolTDVlagrans;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.ProtocolTHMoscow;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.ProtocolTabakInvestReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.ProtocolUnifudReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.ProtocolStandardCurrencyReport;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy on 13.08.2015.
 */
@SuppressWarnings("all")
public class FinishedGoodsHelper {
    public static final String[] INVOICES_TYPES = {"Показать все", "Отгрузка покупателю",
            "Возврат от покупателя", "Перемещение в розницу",
            "Возврат из розницы", "Отгрузка материала", "Возврат материала"};
    public static final String[] INVOICES_TYPES_EMPTY = {"", "Отгрузка покупателю",
            "Возврат от покупателя", "Перемещение в розницу",
            "Возврат из розницы", "Отгрузка материала", "Возврат материала"};
    public static final String[] CURRENCY_TYPES = {"Все", "Рубли", "Валюта"};
    public static final String[] CURRENCY_TYPES_EMPTY = {"", "BYN", "RUB", "USD", "EUR", "UAH"};

    public static void getSertificateCountry(JComboBox<String> combobox) {
        ArrayList<String> array = new ArrayList<String>();
        array.add("");
        array.add("Беларусь");
        array.add("Россия");
        combobox.setModel(new DefaultComboBoxModel(array.toArray()));
        combobox.setSelectedIndex(0);
    }

    public static void getDiscountType(JComboBox<String> combobox) {
        ArrayList<String> array = new ArrayList<String>();
        array.add("Нет скидки");
        array.add("На цену изделия");
        array.add("На сумму по документу");
        array.add("Ручная установка скидки");
        combobox.setModel(new DefaultComboBoxModel(array.toArray()));
        combobox.setSelectedIndex(0);
    }

    public static void getTradeMarkType(final JComboBox<String> combobox) {
        ArrayList<String> array = new ArrayList<String>();
        array.add("Без торговой надбавки");
        array.add("Фиксированная для всего документа");
        array.add("Фиксированная с учетом уценок ");
        array.add("По данным возврата");
        //array.add("Из справочника торговых надбавок") ;
        combobox.setModel(new DefaultComboBoxModel(array.toArray()));
        combobox.setSelectedIndex(0);
    }

    public static void getVatType(final JComboBox<String> combobox) {
        ArrayList<String> array = new ArrayList<String>();
        array.add("Фиксированная ставка для всего документа");
        array.add("Из справочника ставок НДС");
        array.add("Без НДС");
        combobox.setModel(new DefaultComboBoxModel(array.toArray()));
        combobox.setSelectedIndex(2);
    }

    public static void getSealeNumber(final JComboBox comboBox) {
        ArrayList<String> array = new ArrayList<String>();
        array.add("\"8 Марта\" Склад № 6");
        array.add("8M/3");
        comboBox.setModel(new DefaultComboBoxModel(array.toArray()));
        comboBox.setSelectedIndex(1);
    }

    public static void getLoadingDoer(final JComboBox comboBox) {
        ArrayList<String> array = new ArrayList<String>();
        array.add("ОАО \"8 Марта\"");
        comboBox.setModel(new DefaultComboBoxModel(array.toArray()));
        comboBox.setSelectedIndex(0);
    }

    public static void getLoadingMethod(final JComboBox comboBox) {
        ArrayList<String> array = new ArrayList<String>();
        array.add("ручн.");
        comboBox.setModel(new DefaultComboBoxModel(array.toArray()));
        comboBox.setSelectedIndex(0);
    }

    public static void getNotes(final JComboBox comboBox) {
        ArrayList<String> array = new ArrayList<String>();
        array.add("Франко-станция назначения");
        array.add("Условия поставки ");
        comboBox.setModel(new DefaultComboBoxModel(array.toArray()));
        comboBox.setSelectedIndex(0);
        comboBox.setEditable(true);
    }

    public static void getCarOwners(final JComboBox comboBox) {
        ArrayList<String> array = new ArrayList<String>();
        array.add("грузоотправитель");
        array.add("грузополучатель");
        comboBox.setModel(new DefaultComboBoxModel(array.toArray()));
        comboBox.setSelectedIndex(0);
        comboBox.setEditable(true);
    }

    public static void fillingPriceAgreementProtocol(JComboBox<PriceAgreementProtocol> combobox) {
        ArrayList<PriceAgreementProtocol> array = new ArrayList<PriceAgreementProtocol>();
        array.add(new PriceAgreementProtocol(1, new int[]{-1}, "Общий вид", "протоколСтандартный.ots", ProtocolStandardReport.class));
        array.add(new PriceAgreementProtocol(16, new int[]{-1}, "Общий валютный", "протоколСтандартный.ots", ProtocolStandardCurrencyReport.class));
        array.add(new PriceAgreementProtocol(2, new int[]{3688, 4816, 4814, 4817, 4815, 2565}, "Евроторг", "протоколЕвроторг.ots", ProtocolEvrotorgReport.class));
        array.add(new PriceAgreementProtocol(3, new int[]{9449}, "БелВиллесден", "протоколБелВиллесденНовый.ots", ProtocolBelVillesdenReport.class));
        array.add(new PriceAgreementProtocol(4, new int[]{3502, 3223, 3500}, "Юнифуд", "протоколЮнифуд.ots", ProtocolUnifudReport.class));
        array.add(new PriceAgreementProtocol(5, new int[]{9242}, "Белпочта", "протоколБелпочта.ots", ProtocolBelPostReport.class));
        array.add(new PriceAgreementProtocol(6, new int[]{3093}, "ГринРозница", "протоколГринРозница.ots", ProtocolGreenRetailReport.class));
        array.add(new PriceAgreementProtocol(7, new int[]{9448}, "Табак инвест", "протоколТабакИнвест.ots", ProtocolTabakInvestReport.class));
        array.add(new PriceAgreementProtocol(8, new int[]{9949, 9438}, "Простор", "протоколПростор.ots", ProtocolProstorReport.class));
        array.add(new PriceAgreementProtocol(9, new int[]{5417, 5437}, "ОАО Дорорс", "протоколДорорс.ots", ProtocolDororsReport.class));
        array.add(new PriceAgreementProtocol(10, new int[]{8224}, "Гронитекс", "протоколБелпочта.ots", ProtocolGranitexReport.class));
        array.add(new PriceAgreementProtocol(11, new int[]{5015}, "ТД Гомель - ВЛАГРАНС", "протоколТорговыйДомВлагранс.ots", ProtocolTDVlagrans.class));
        array.add(new PriceAgreementProtocol(12, new int[]{4643}, "ГипперМолл", "протоколГиппермолл.ots", ProtocolHyberMallReport.class));
        array.add(new PriceAgreementProtocol(13, new int[]{5015}, "ТД Москва-ИНТЕРНЕТ", "protocol_trade_house_moscow.ots", ProtocolTHMoscow.class));
        array.add(new PriceAgreementProtocol(14, new int[]{3860}, "Универмаг Беларусь", "протоколБелпочта.ots", ProtocolBelPostReport.class));
        array.add(new PriceAgreementProtocol(15, new int[]{0000}, "РЦ ФиксМаркет", "протоколФиксМаркет.ots", ProtocolBelPostReport.class));
        // array.add(new PriceAgreementProtocol(11, new int[]{3093}, "ГринРозница-Акция", "протоколГринРозницаАкция.ots", ProtocolGreenRetailDiscountReport.class));
        combobox.setModel(new DefaultComboBoxModel(array.toArray()));
        combobox.setSelectedIndex(0);
    }

    public static void getAdjustmentCauses(final JComboBox comboBox) {
        List<String> array = new ArrayList<String>();

        array.add("Неправильное указание цены");
        array.add("Неправильное указание ставки НДС");
        array.add("Неправильное указание количества товара");
        array.add("Неправильный курс валюты в расчете");

        comboBox.setModel(new DefaultComboBoxModel(array.toArray()));
        comboBox.setSelectedIndex(0);
        comboBox.setEditable(true);
    }

    public static void getServiceType(JComboBox<String> comboBox) {
        List<String> array = new ArrayList<String>();

        array.add("Реализация товаров");
        array.add("Услуга крашения");
        array.add("Пошив из давальческого сырья");

        comboBox.setModel(new DefaultComboBoxModel(array.toArray()));
        comboBox.setSelectedIndex(0);
        comboBox.setEditable(true);
    }
}
