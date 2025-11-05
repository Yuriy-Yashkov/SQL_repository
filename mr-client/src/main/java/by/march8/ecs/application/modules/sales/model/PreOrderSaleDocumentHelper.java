package by.march8.ecs.application.modules.sales.model;

import javax.swing.*;
import java.util.ArrayList;

@SuppressWarnings("all")
public class PreOrderSaleDocumentHelper {

    public static void getVatType(final JComboBox<String> component) {
        ArrayList<String> array = new ArrayList<>();
        array.add("Без НДС");
        array.add("Фиксированная ставка для всего документа");
        array.add("Из справочника ставок НДС");
        component.setModel(new DefaultComboBoxModel(array.toArray()));
        component.setSelectedIndex(0);
    }

    public static void getDiscountType(JComboBox<String> component) {
        ArrayList<String> array = new ArrayList<>();
        array.add("Нет скидки");
        array.add("На цену изделия");
        array.add("На сумму по документу");
        array.add("Ручная установка скидки");
        component.setModel(new DefaultComboBoxModel(array.toArray()));
        component.setSelectedIndex(0);
    }

    public static void getTradeMarkType(final JComboBox<String> component) {
        ArrayList<String> array = new ArrayList<>();
        array.add("Без торговой надбавки");
        array.add("Фиксированная для всего документа");
        array.add("Фиксированная с учетом уценок ");
        //array.add("Из справочника торговых надбавок") ;
        component.setModel(new DefaultComboBoxModel(array.toArray()));
        component.setSelectedIndex(0);
    }

    public static void getGradeMarkdownType(final JComboBox<String> component) {
        ArrayList<String> array = new ArrayList<>();
        array.add("Без уценки");
        array.add("На основании цены 1-го сорта");
        component.setModel(new DefaultComboBoxModel(array.toArray()));
        component.setSelectedIndex(0);
    }
}
