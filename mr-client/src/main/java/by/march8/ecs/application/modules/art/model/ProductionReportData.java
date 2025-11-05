package by.march8.ecs.application.modules.art.model;

import java.util.List;

/**
 * @author Andy 24.01.2017.
 */
public class ProductionReportData {
    private List<ProductionReportItem> data = null;
    private ProductionReportTotal total;

    public List<ProductionReportItem> getData() {
        return data;
    }

    public void setData(final List<ProductionReportItem> data) {
        this.data = data;
    }

    public ProductionReportTotal getTotal() {
        return total;
    }

    public void setTotal(final ProductionReportTotal total) {
        this.total = total;
    }
}
