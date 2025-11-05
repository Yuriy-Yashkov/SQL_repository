package by.march8.ecs.application.modules.sales.model;

import by.march8.entities.sales.PreOrderProductItem;

import java.util.List;

/**
 * @author Andy 14.01.2019 - 8:23.
 */
public class PreOrderSaleDocumentReport {

    private List<PreOrderProductItem> specification;


    public List<PreOrderProductItem> getSpecification() {
        return specification;
    }

    public void setSpecification(List<PreOrderProductItem> specification) {
        this.specification = specification;
    }
}
