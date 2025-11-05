package by.march8.ecs.application.modules.marketing.fx.model;

import by.march8.ecs.application.modules.marketing.model.ChartItem;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.ProductionItem;

import java.util.List;

/**
 * @author Andy 07.05.2018 - 14:20.
 */
public class MovementProductionItem {

    private ProductionItem item;

    private List<ChartItem> inList;
    private List<ChartItem> outList;
    private List<ChartItem> planList;

    public ProductionItem getItem() {
        return item;
    }

    public void setItem(final ProductionItem item) {
        this.item = item;
    }

    public List<ChartItem> getInList() {
        return inList;
    }

    public void setInList(final List<ChartItem> inList) {
        this.inList = inList;
    }

    public List<ChartItem> getOutList() {
        return outList;
    }

    public void setOutList(final List<ChartItem> outList) {
        this.outList = outList;
    }

    public List<ChartItem> getPlanList() {
        return planList;
    }

    public void setPlanList(final List<ChartItem> planList) {
        this.planList = planList;
    }
}
