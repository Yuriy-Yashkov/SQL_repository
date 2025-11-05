package by.march8.ecs.application.modules.planning.model;

import by.march8.entities.planning.PlanningItemComponent;
import by.march8.entities.planning.ProductionPlanningComposition;
import by.march8.entities.planning.ProductionPlanningItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy 07.12.2018 - 14:38.
 */
public class ProductionPlanningBucket {

    private ProductionPlanningItem item;
    private List<ProductionPlanningComposition> slots;
    private List<ProductionPlanningItem> items;

    public ProductionPlanningBucket(ProductionPlanningItem item_) {
        slots = new ArrayList<>();
        for (PlanningItemComponent component : item_.getComponentList()) {
            System.out.println("Добавлен слот " + component.getComponent().getMaterialName());
            slots.add(component.getComponent());
        }

        item = item_;
        items = new ArrayList<>();
        items.add(item_);
        item_.setUsing(true);
    }

    private boolean isComponentExist(ProductionPlanningComposition item) {
        for (ProductionPlanningComposition value : slots) {
            // если Компонента изделия нету в бакете - добавляем в темповый
            if (value.getId() == item.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean addToBucket(ProductionPlanningItem item) {
        List<ProductionPlanningComposition> tempList = new ArrayList<>();
        // просмотр компонентов изделия
        for (PlanningItemComponent component : item.getComponentList()) {
            // Просмотр компонентов в бакете
            if (!isComponentExist(component.getComponent())) {
                tempList.add(component.getComponent());
            }
        }

        if ((getSlotCount() + tempList.size()) <= 8) {
            for (ProductionPlanningComposition component : tempList) {
                slots.add(component);
            }
            item.setUsing(true);
            items.add(item);
            return true;
        } else {
            return false;
        }
    }

    public int getSlotNumberByComponentId(ProductionPlanningComposition component) {
        int n = 0;
        for (ProductionPlanningComposition composition : slots) {
            if (component.getMaterialId() == composition.getMaterialId()) {
                return n;
            }
            n++;
        }
        return -1;
    }

    public int getSlotCount() {
        return slots.size();
    }

    public List<ProductionPlanningComposition> getSlots() {
        return slots;
    }

    public void setSlots(List<ProductionPlanningComposition> slots) {
        this.slots = slots;
    }

    public List<ProductionPlanningItem> getItems() {
        return items;
    }

    public void setItems(List<ProductionPlanningItem> items) {
        this.items = items;
    }

    public ProductionPlanningItem getItem() {
        return item;
    }

    public void setItem(ProductionPlanningItem item) {
        this.item = item;
    }

    public String getBucketInformation() {
        if (item != null) {
            return item.getItemName() + " арт. " + item.getArticleNumber() + " мод. " + item.getModelNumber();
        } else {
            return "Нет информации о изделии";
        }
    }
}
