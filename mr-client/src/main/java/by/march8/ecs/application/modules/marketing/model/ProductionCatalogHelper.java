package by.march8.ecs.application.modules.marketing.model;

import by.march8.ecs.application.modules.marketing.manager.ProductionCatalogReportTypeItem;
import by.march8.ecs.application.modules.marketing.reports.ProductionCatalogReportType;

import javax.swing.*;
import java.util.ArrayList;

public class ProductionCatalogHelper {
    public static void prepareReportTypes(final JComboBox<ProductionCatalogReportTypeItem> component) {
        ArrayList<ProductionCatalogReportTypeItem> array = new ArrayList<>();
        array.add(new ProductionCatalogReportTypeItem(1, "Каталог на печать (EXCEL)", ProductionCatalogReportType.PLAIN_EXCEL));
        array.add(new ProductionCatalogReportTypeItem(2, "Каталог покупателю (HTML)", ProductionCatalogReportType.HTML_CATALOG));
        component.setModel(new DefaultComboBoxModel(array.toArray()));
        component.setSelectedIndex(0);
    }
}
