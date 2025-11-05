package by.march8.ecs.application.modules.warehouse.external.shipping.model;

import by.march8.api.TableHeader;
import by.march8.entities.warehouse.SaleDocumentItem;

/**
 * @author Andy 03.11.2015.
 */
public class SaleDocumentPreview extends PreviewEntity {

    @TableHeader(name = "Наименование", sequence = 0)
    private String itemName;
    @TableHeader(name = "Артикул", sequence = 1)
    private String itemArticleNumber;
    @TableHeader(name = "Номер", sequence = 1)
    private int itemArticleCode;
    @TableHeader(name = "Кол-во", sequence = 2)
    private int itemAmount;
    @TableHeader(name = "Учетная цена", sequence = 3)
    private double valuePriceForAccounting;
    @TableHeader(name = "Ставка НДС", sequence = 4)
    private double valueVat;
    @TableHeader(name = "Стоимость", sequence = 5)
    private double valuePrice;
    @TableHeader(name = "Сумма", sequence = 6)
    private double valueSumCost;
    @TableHeader(name = "Сумма НДС", sequence = 7)
    private double valueSumVat;
    @TableHeader(name = "Стоимость с НДС", sequence = 8)
    private double valueSumConstAndVat;

    public SaleDocumentPreview(final SaleDocumentItem documentItem) {
        //itemName = documentItem.getItem().getModel().getName();
        //itemArticleNumber = documentItem.getItem().getModel().getArticleName();
        //itemArticleCode = documentItem.getItem().getModel().getCategory();
        itemAmount = documentItem.getItemsAmount();

        valuePriceForAccounting = documentItem.getValuePriceForAccounting();
        valueVat = documentItem.getValueVAT();
        valuePrice = documentItem.getValuePrice();
        valueSumCost = documentItem.getValueSumCost();
        valueSumVat = documentItem.getValueSumVat();
        valueSumConstAndVat = documentItem.getValueSumCostAndVat();
    }

    public SaleDocumentPreview() {

    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(final String itemName) {
        this.itemName = itemName;
    }

    public String getItemArticleNumber() {
        return itemArticleNumber;
    }

    public void setItemArticleNumber(final String itemArticleNumber) {
        this.itemArticleNumber = itemArticleNumber;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(final int itemAmount) {
        this.itemAmount = itemAmount;
    }

    public double getValuePriceForAccounting() {
        return valuePriceForAccounting;
    }

    public void setValuePriceForAccounting(final float valuePriceForAccounting) {
        this.valuePriceForAccounting = valuePriceForAccounting;
    }

    public double getValueVat() {
        return valueVat;
    }

    public void setValueVat(final double valueVat) {
        this.valueVat = valueVat;
    }

    public double getValuePrice() {
        return valuePrice;
    }

    public void setValuePrice(final float valuePrice) {
        this.valuePrice = valuePrice;
    }

    public double getValueSumCost() {
        return valueSumCost;
    }

    public void setValueSumCost(final double valueSumCost) {
        this.valueSumCost = valueSumCost;
    }

    public double getValueSumVat() {
        return valueSumVat;
    }

    public void setValueSumVat(final double valueSumVat) {
        this.valueSumVat = valueSumVat;
    }

    public double getValueSumConstAndVat() {
        return valueSumConstAndVat;
    }

    public void setValueSumConstAndVat(final double valueSumConstAndVat) {
        this.valueSumConstAndVat = valueSumConstAndVat;
    }

    @Override
    public String getMarkerFieldValue() {
        return String.valueOf(itemArticleCode);
    }

    @Override
    public PreviewEntity generateFooter(final int footerType) {
        String footerTypeString = "Итого по артикулу";
        if (footerType == 1) {
            footerTypeString = "Итого по документу";
        }

        SaleDocumentPreview item = new SaleDocumentPreview();
        item.setItemName("");
        item.setItemArticleNumber(footerTypeString);
        item.setItemArticleCode(-1);

        item.setValuePriceForAccounting(-1);
        item.setValuePrice(-1);
        item.setValueVat(-1);

        item.setItemAmount(0);
        item.setValueSumCost(0);

        item.setValueSumVat(0);
        item.setValueSumConstAndVat(0);

        return item;
    }

    @Override
    public PreviewEntity calculateFooter(final PreviewEntity item, final PreviewEntity current) {
        SaleDocumentPreview counterItem = (SaleDocumentPreview) item;
        SaleDocumentPreview currentItem = (SaleDocumentPreview) current;

        counterItem.setItemAmount(counterItem.getItemAmount() + currentItem.getItemAmount());
        counterItem.setValueSumCost(counterItem.getValueSumCost() + currentItem.getValueSumCost());
        counterItem.setValueSumVat(counterItem.getValueSumVat() + currentItem.getValueSumVat());
        counterItem.setValueSumConstAndVat(counterItem.getValueSumConstAndVat() + currentItem.getValueSumConstAndVat());
        return item;
    }

    @Override
    public boolean isFooter() {
        return valuePrice < 0;
    }

    public int getItemArticleCode() {
        return itemArticleCode;
    }

    public void setItemArticleCode(final int itemArticleCode) {
        this.itemArticleCode = itemArticleCode;
    }
}
