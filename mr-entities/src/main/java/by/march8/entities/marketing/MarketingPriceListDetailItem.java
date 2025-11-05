package by.march8.entities.marketing;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;


@NamedQueries({
        @NamedQuery(name = "MarketingPriceListDetailItem.findByPriceList",
                query = "SELECT item FROM MarketingPriceListDetailItem item WHERE item.priceListId = :priceList " +
                        "order by " +
                        " item.itemId ")
})

/**
 * @author Andy 12.10.2017.
 */
@Entity
@Table(name = "MARKETING_PRICE_LIST_DETAIL")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class MarketingPriceListDetailItem extends BaseEntity {

    @Id
    @Column(name = "ID")
    private int id ;

    @Column(name="REF_DOCUMENT_ID")
    private int priceListId ;

    @Column(name = "ITEM_ID")
    private int itemId ;

    @Column(name="SIZE_RANGE_VALUE")
    private String sizeRange ;

    @Column(name="PRIME_COST_VALUE")
    @TableHeader(name="Себестоимость", width = -90, sequence = 30)
    private double primeCostValue ;

    @Column(name="PROFITABILITY_VALUE")
    @TableHeader(name="Рентабельность", width = -90, sequence = 40)
    private double profitabilityValue ;

    @Column(name="EFFECTIVE_PRICE_VALUE")
    @TableHeader(name="Действующая цена", width = -90, sequence = 50)
    private double effectivePriceValue ;

    @Column(name="SUGGESTED_PRICE_VALUE")
    @TableHeader(name="Предложенная цена", width = -90, sequence = 60)
    private double suggestedPriceValue ;


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public int getPriceListId() {
        return priceListId;
    }

    public void setPriceListId(final int priceListId) {
        this.priceListId = priceListId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(final int itemId) {
        this.itemId = itemId;
    }

    public String getSizeRange() {
        return sizeRange;
    }

    public void setSizeRange(final String sizeRange) {
        this.sizeRange = sizeRange;
    }

    public double getPrimeCostValue() {
        return primeCostValue;
    }

    public void setPrimeCostValue(final double primeCostValue) {
        this.primeCostValue = primeCostValue;
    }

    public double getProfitabilityValue() {
        return profitabilityValue;
    }

    public void setProfitabilityValue(final double profitabilityValue) {
        this.profitabilityValue = profitabilityValue;
    }

    public double getEffectivePriceValue() {
        return effectivePriceValue;
    }

    public void setEffectivePriceValue(final double effectivePriceValue) {
        this.effectivePriceValue = effectivePriceValue;
    }

    public double getSuggestedPriceValue() {
        return suggestedPriceValue;
    }

    public void setSuggestedPriceValue(final double suggestedPriceValue) {
        this.suggestedPriceValue = suggestedPriceValue;
    }
}
