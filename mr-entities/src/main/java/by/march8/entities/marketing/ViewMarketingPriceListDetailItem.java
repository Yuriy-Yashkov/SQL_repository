package by.march8.entities.marketing;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;


@NamedQueries({
        @NamedQuery(name = "ViewMarketingPriceListDetailItem.findByPriceList",
                query = "SELECT item FROM ViewMarketingPriceListDetailItem item WHERE item.priceListId = :priceList " +
                        "order by " +
                        " item.itemName, item.modelNumber, item.articleNumber ")
})

/**
 * @author Andy 12.10.2017.
 */
@Entity
@Table(name = "V_MARKETING_PRICE_LIST_DETAIL")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ViewMarketingPriceListDetailItem extends BaseEntity {

    @Id
    @Column(name = "ID")
    private int id;

    @Column(name = "REF_DOCUMENT_ID")
    private int priceListId;

    @Column(name = "ITEM_NAME")
    private String itemName;

    @Column(name = "MODEL_NUMBER")
    @TableHeader(name = "Модель", width = -90, sequence = 10)
    private String modelNumber;

    @Column(name = "ARTICLE_NUMBER")
    @TableHeader(name = "Артикул", width = -100, sequence = 15)
    private String articleNumber;

    @Column(name = "ITEM_ID")
    private int itemId;

    @Column(name = "SIZE_RANGE_VALUE")
    private String sizeRange;

    @Column(name = "PRIME_COST_VALUE")
    @TableHeader(name = "Себестоимость", width = -90, sequence = 30)
    private double primeCostValue;

    @Column(name = "PROFITABILITY_VALUE")
    @TableHeader(name = "Рентабельность", width = -90, sequence = 40)
    private double profitabilityValue;

    @Column(name = "EFFECTIVE_PRICE_VALUE")
    @TableHeader(name = "Действующая цена", width = -90, sequence = 50)
    private double effectivePriceValue;

    @Column(name = "SUGGESTED_PRICE_VALUE")
    @TableHeader(name = "Предложенная цена", width = 80, sequence = 60)
    private double suggestedPriceValue;

    @Column(name = "CHANGE_PERCENT_VALUE")
    //@TableHeader(name = "% изменения", width = 80, sequence = 70)
    private double changePercentValue;

    @Transient
    private boolean changed ;


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


    @TableHeader(name = "Размеры", width = -90, sequence = 20)
    public String getSizeRange() {
        if(sizeRange!=null) {
            return sizeRange.trim();
        }else{
            return "";
        }
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

    @TableHeader(name = "Наименование", sequence = 5)
    public String getItemName() {
        if (itemName != null) {
            return itemName.trim();
        } else {
            return "";
        }
    }

    public void setItemName(final String itemName) {
        this.itemName = itemName;
    }

    public String getModelNumber() {
        if (modelNumber != null) {
            return modelNumber.trim();
        } else {
            return "";
        }

    }

    public void setModelNumber(final String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getArticleNumber() {
        if (articleNumber != null) {
            return articleNumber.trim();
        } else {
            return "";
        }
    }

    public void setArticleNumber(final String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(final boolean changed) {
        this.changed = changed;
    }

    public double getChangePercentValue() {
        return changePercentValue;
    }

    public void setChangePercentValue(final double changePercentValue) {
        this.changePercentValue = changePercentValue;
    }

/*    public int getMaximumSize(){
        if(!getSizeRange().equals("")){

            String[] sizeArray = getSizeRange().split("-");
            try{
                return Integer.valueOf(sizeArray[1]);
            }catch (Exception e){
                e.printStackTrace();
                return 0;
            }
        }else{
            return 0 ;
        }
    }*/


    public int getMinimumSize(){
        if(!getSizeRange().equals("")){

            String[] sizeArray = getSizeRange().split("-");
            try{
                return Integer.valueOf(sizeArray[0]);
            }catch (Exception e){
                e.printStackTrace();
                return 0;
            }
        }else{
            return 0 ;
        }
    }
}
