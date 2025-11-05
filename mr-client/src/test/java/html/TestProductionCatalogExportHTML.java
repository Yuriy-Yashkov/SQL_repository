package html;

import by.march8.ecs.application.modules.marketing.manager.ProductionCatalogManager;
import by.march8.ecs.application.modules.marketing.model.ProductCatalogArticleReport;
import by.march8.ecs.application.modules.marketing.model.ProductCatalogReport;
import by.march8.ecs.application.modules.marketing.model.ProductionCatalogCategoryReport;
import by.march8.ecs.application.modules.marketing.model.ProductionOrder;
import by.march8.ecs.application.modules.references.classifier.model.ItemNameReplacer;
import by.march8.entities.product.OrderItem;
import by.march8.entities.product.ProductionCatalogProductReport;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestProductionCatalogExportHTML {
    @Test
    public void testHTMLTest() {
        ProductionCatalogManager manager = new ProductionCatalogManager(null);
        prepareHTML(manager, 11);
    }

    @Test
    public void testCreateHTML() {
        ProductionCatalogManager manager = new ProductionCatalogManager(null);
        createHTML(manager, 11);
    }

    @Test
    public void testDeserializeECMAObject() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ProductionOrder order_ = mapper.readValue(new FileInputStream("order.txt"),
                    ProductionOrder.class);
            if (order_ != null) {
                for (OrderItem order : order_.getOrderList()) {
                    System.out.println(order.getItemName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createHTML(ProductionCatalogManager manager, int id) {
    }

    private void prepareHTML(ProductionCatalogManager manager, int id) {
        ProductCatalogReport report = manager.prepareDocumentReportData(id, null, null);
        if (report != null) {
            for (ProductionCatalogCategoryReport category : report.getCategoryList()) {

                System.out.println(category.getCategoryId() + " - " + category.getData().size());
                for (ProductCatalogArticleReport article : category.getData()) {
                    System.out.println("\t\t[" + article.getModelNumber() + "] - "
                            + article.getData().size() + " - "
                            + ItemNameReplacer.transform(article.getItemName()));
                    for (ProductionCatalogProductReport product : article.getData()) {
                        System.out.println("\t\t\t\t[" + product.getModelNumber() + "] - " + product.getSizePrint());
                    }
                }
            }

            ObjectMapper mapper = new ObjectMapper();
            try {
                mapper.writeValue(new FileOutputStream("C:/json.obj"), report);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        // StdDateFormat is ISO8601 since jackson 2.9
        //mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        try {
            ProductCatalogReport report_ = (ProductCatalogReport) mapper.readValue(new FileInputStream("C:/json.obj"),
                    ProductCatalogReport.class);
            if (report != null) {
                for (ProductionCatalogCategoryReport category : report_.getCategoryList()) {

                    System.out.println(category.getCategoryId() + " - " + category.getData().size());
                    for (ProductCatalogArticleReport article : category.getData()) {
                        System.out.println("\t\t[" + article.getModelNumber() + "] - "
                                + article.getData().size() + " - "
                                + ItemNameReplacer.transform(article.getItemName()));
                        for (ProductionCatalogProductReport product : article.getData()) {
                            System.out.println("\t\t\t\t[" + product.getModelNumber() + "] - " + product.getSizePrint());
                        }
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
