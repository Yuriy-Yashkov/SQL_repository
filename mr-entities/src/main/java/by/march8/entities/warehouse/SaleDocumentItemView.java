package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;
import javax.swing.*;


@NamedQueries({


        @NamedQuery(name = "SaleDocumentItemView.findByDocumentNumber",
                query = "SELECT detail FROM SaleDocumentItemView detail WHERE detail.documentNumber = :document ")
})

@Entity
@Table(name = "V_SaleDocumentItems")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class SaleDocumentItemView extends BaseEntity implements ISummingUp {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Идентификатор документа, которому принадлежит запись
     */
    @Column(name = "doc_id")
    private int documentId;

    /**
     * Номер документа, которому принадлежит запись
     */
    @Column(name = "ndoc")
    private String documentNumber;

    /**
     * Идентификатор позиции товара из таблиц классификатора
     */
    @Column(name = "kod_izd")
    private int itemId;

    /**
     * Количество единиц изделия
     */
    @Column(name = "kol")
    private int amount;

    /**
     * Количество единиц изделия в упаковке
     */
    @Column(name = "kol_in_upack")
    private int amountInPack;

    /**
     * Государственный скан-код (ЕАН 13)
     */
    @Column(name = "eancode")

    private String itemEanCode;

    /**
     * Государственный скан-код (ЕАН 13)
     */
    @Column(name = "scan")

    private String itemScanCode;

    /**
     * Цвет изделия
     */
    @Column(name = "ncw")
    private String itemColor;

    /**
     * Сорт изделия
     */
    @Column(name = "srt")
    private int itemGrade;

    /**
     * Учетная цена
     */
    @Column(name = "cena_uch")

    private double valuePriceForAccounting;
    /**
     * Ставка НДС на изделие
     */
    @Column(name = "nds")
    private float valueVAT;

    /**
     * Цена  в Бел. руб.
     */
    @Column(name = "cena")
    private double valuePrice;

    /**
     * Сумма  в Бел. руб.
     */
    @Column(name = "summa")
    private double valueSumCost;

    /**
     * Сумма  НДС в Бел. руб.
     */
    @Column(name = "summa_nds")
    private double valueSumVat;

    /**
     * Сумма с НДС в Бел. руб.
     */

    @Column(name = "itogo")
    private double valueSumCostAndVat;

    /**
     * Цена  в валюте
     */
    @Column(name = "cenav")

    private double valuePriceCurrency;

    /**
     * Сумма  в валюте
     */
    @Column(name = "summav")
    private double valueSumCostCurrency;

    /**
     * Сумма  НДС в валюте
     */
    @Column(name = "summa_ndsv")
    private double valueSumVatCurrency;

    /**
     * Сумма с НДС в валюте
     */
    @Column(name = "itogov")

    private double valueSumCostAndVatCurrency;

    /**
     * Оптовая цена
     */
    @Column(name = "cno")
/*    @TableHeader(name="Цена_Оптовая",width = -80, sequence = 5)*/
    private float priceWholesale;

    /**
     * Наименование изделия
     */
    @Column(name = "ngpr")
/*    @TableHeader(name="Наименование", sequence = 5)*/
    private String productName;

    /**
     * Номер модели изделия
     */
    @Column(name = "fas")
/*    @TableHeader(name="Модель", sequence = 2)*/
    private int modelNumber;

    /**
     * Наименование артикула
     */
    @Column(name = "nar")
/*    @TableHeader(name="Артикул", sequence = 4)*/
    private String articleName;

    /**
     * Шифр артикула
     */
    @Column(name = "sar")
    @TableHeader(name = "Шифр артикула", width = 0 ,sequence = 0)
    private int articleCode;
    //---------------------------------------------------------------------------

    /*@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private TradeMarkItem tradeMarkItem ;*/

    @Column(name = "torg_nadb")
    private float tradeMarkValue;

    @Column(name = "rozn_cena")
    private float retailPriceValue;

    @Column(name = "rzm_marh")
    private int itemSize;

    @Column(name = "tnvd")
    private String TNVD;

    @Column(name="discount")
    private String discount ;

    @Override
    public double getTradeMarkup() {
        return tradeMarkValue ;
    }

    @Transient
    private boolean changed = false;

    @Transient
    @TableHeader(name = "ТН", width = 0, sequence = 800)
    private boolean wrongAllowance = false;

    @Transient
    private String itemPrintSize ;

    @Transient
    private float nsiVAT ;

    @Transient
    private JLabel image;

    public SaleDocumentItemView() {

    }


    public void setTradeMarkValue(final float tradeMarkValue) {
        this.tradeMarkValue = tradeMarkValue;
    }

    public float getRetailPriceValue() {
        return retailPriceValue;
    }

    public String getTNVD() {
        return TNVD;
    }

    public void setRetailPriceValue(final float retailPriceValue) {
        this.retailPriceValue = retailPriceValue;
    }

    /**
     * Конструктор на копирование изделий
     *
     * @param item объект-источнки
     */
    public SaleDocumentItemView(SaleDocumentItemView item) {
        setAmount(item.getAmount());
        setAmountInPack(item.getAmountInPack());

        setItemEanCode(item.getItemEanCode());

        setItemColor(item.getItemColor());
        setItemGrade(item.getItemGrade());

        setValuePriceForAccounting(item.getValuePriceForAccounting_());
        setValueVAT(item.getValueVAT());

        setValuePrice(item.getValuePrice());
        setValueSumCost(item.getValueSumCost());
        setValueSumVat(item.getValueSumVat());
        setValueSumCostAndVat(item.getValueSumCostAndVat());

        setValuePriceCurrency(item.getValuePriceCurrency());
        setValueSumCostCurrency(item.getValueSumCostCurrency());
        setValueSumVatCurrency(item.getValueSumVatCurrency());
        setValueSumCostAndVatCurrency(item.getValueSumCostAndVatCurrency());
    }

    public float getNsiVAT() {
        return nsiVAT;
    }

    public void setNsiVAT(float nsiVAT) {
        this.nsiVAT = nsiVAT;
    }

    public void setItem(final int itemId) {
        this.itemId = itemId;
    }

    public float getPriceWholesale() {
        return priceWholesale;
    }

    public void setPriceWholesale(final float priceWholesale) {
        this.priceWholesale = priceWholesale;
    }

    public String getName() {
        return productName;
    }

    public void setName(final String name) {
        this.productName = name;
    }

    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(final int modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getArticleName() {
        return articleName.trim();
    }

    public void setArticleName(final String articleName) {
        this.articleName = articleName;
    }

    public int getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(final int articleCode) {
        this.articleCode = articleCode;
    }

    @Override
    public String toString() {
        return "SaleDocumentDetailMarch{" +
                "id=" + id +
                ", amount=" + amount +
                ", amountInPack=" + amountInPack +
                ", itemEanCode=" + itemEanCode +
                ", itemColor='" + itemColor + '\'' +
                ", itemGrade=" + itemGrade +
                ", valuePriceForAccounting=" + valuePriceForAccounting +
                ", valueVAT=" + valueVAT +
                ", valuePrice=" + valuePrice +
                ", valueSum=" + valueSumCost +
                ", valueSumVat=" + valueSumVat +
                ", valueSumAndVat=" + valueSumCostAndVat +
                ", valuePriceCurrency=" + valuePriceCurrency +
                ", valueSumCurrency=" + valueSumCostCurrency +
                ", valueSumVatCurrency=" + valueSumVatCurrency +
                ", valueSumAndVatCurrency=" + valueSumCostAndVatCurrency +
                '}';
    }


    @TableHeader(name = "Модель", width = -50, sequence = 1)
    public String getItemModelNumber() {
        return String.valueOf(modelNumber);
    }

    @TableHeader(name = "Артикул", width = -85, sequence = 2)
    public String getItemArticleName() {
        if (articleName != null) {
            return articleName.trim();
        } else {
            return "";
        }
    }

    @TableHeader(name = "Наименование", sequence = 3)
    public String getItemName() {
        if (productName != null) {
            return productName.trim();
        } else {
            return "";
        }
    }


    @TableHeader(name = "Сорт", width = -40, sequence = 5)
    public int getItemGradeText() {
        return itemGrade;
    }

    @TableHeader(name = "Цвет", sequence = 6)
    public String getItemColorText() {
        if (itemColor != null) {
            return itemColor.trim();
        } else {
            return "";
        }
    }


    public int getItemsAmount() {
        return getAmount() * getAmountInPack();
    }

    @TableHeader(name = "Кол-во", width = -40, sequence = 7)
    public String getItemAmount() {
        if (articleCode >= 47000000 && articleCode <= 48000000) {
            double weight = (double) amountInPack / 100;
            return String.valueOf(weight);
        } else {
            return String.valueOf(getAmount() * getAmountInPack());
        }
    }

    @TableHeader(name = "СК", width = -40, sequence = 10)
    public String getDiscount() {
        if(discount!=null) {
            return discount.trim();
        }else{
            return "0";
        }
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }


    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

    public int getAmountInPack() {
        return amountInPack;
    }

    public void setAmountInPack(final int amountInPack) {
        this.amountInPack = amountInPack;
    }


    /* @TableHeader(name = "EAN", sequence = 20)*/
    public String getItemEanCode() {
        return itemEanCode;
    }

    public void setItemEanCode(final String itemEanCode) {
        this.itemEanCode = itemEanCode;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(final String itemColor) {
        this.itemColor = itemColor;
    }

    public int getItemGrade() {
        return itemGrade;
    }

    public void setItemGrade(final int itemGrade) {
        this.itemGrade = itemGrade;
    }

    public double getValuePriceForAccounting_() {
        return valuePriceForAccounting;
    }

    public double getValuePriceForAccountingAbs() {
        if (valuePriceForAccounting < 0) {
            return valuePriceForAccounting * (-1);
        } else {
            return valuePriceForAccounting;
        }
    }

    public void setValuePriceForAccounting(final double valuePriceForAccounting) {
        this.valuePriceForAccounting = valuePriceForAccounting;
    }

    public float getValueVAT() {
        return valueVAT;
    }

    public void setValueVAT(final float valueVAT) {
        this.valueVAT = valueVAT;
    }

    //@TableHeader(name = "Бел. руб_Цена", width = 85, sequence = 8)
    public double getValuePrice() {
        return valuePrice;
    }

    public double getValueSum() {
        return valueSumCost;
    }

    @TableHeader(name = "Бел. руб_Цена", width = 85, sequence = 80)
    public String getValuePriceText() {
        return String.format("%.2f", valuePrice);
    }

    @TableHeader(name = "Бел. руб_Сумма", width = 85, sequence = 90)
    public String getValueSummText() {
        return String.format("%.2f", valueSumCost);
    }

    @TableHeader(name = "Бел. руб_НДС", width = 85, sequence = 100)
    public String getValueSummVatText() {
        return String.format("%.2f", valueSumVat);
    }

    // @TableHeader(name = "Валюта_Цена", width = 85, sequence = 11)
    public double getValuePriceCurrency() {
        return valuePriceCurrency;
    }

    @TableHeader(name = "Валюта_Цена", width = 85, sequence = 110)
    public String getValuePriceCurrencyString() {
        return String.format("%.2f", valuePriceCurrency);
    }

    @TableHeader(name = "Валюта_Сумма", width = 85, sequence = 120)
    public String getValueSumPriceCurrency() {
        return String.format("%.2f", valueSumCostCurrency);
    }

    //@TableHeader(name = "Валюта_НДС", width = 85, sequence = 13)
    public double getValueSumVatCurrency() {
        return valueSumVatCurrency;
    }

    @TableHeader(name = "Валюта_НДС", width = 85, sequence = 130)
    public String getValueSumVatCurrencyString() {
        return String.format("%.2f", valueSumVatCurrency);
    }

    @TableHeader(name = "Розница_Надбавка", width = 85, sequence = 150)
    public String getTradeMarkValue() {
/*        if (tradeMarkValue != null) {
            return String.format("%.2f", tradeMarkValue);
        } else {
            return "0";
        }*/
        return String.format("%.2f", tradeMarkValue);
    }

    public float getTradeMarkupAsInteger(){
        return tradeMarkValue ;
    }

    @TableHeader(name = "Розница_Цена", width = 85, sequence = 160)
    public String getRetailPrice() {
/*        if (retailPriceValue != null) {
            return String.format("%.2f", retailPriceValue);
        } else {
            return "0";
        }*/
        return String.format("%.2f", retailPriceValue);
    }

    @TableHeader(name = "Уч.цена", width = -50, sequence = 170)
    public String getAccountPrice() {
        return String.format("%.2f", getAccountingPrice());
    }

    public void setValuePrice(final double valuePrice) {
        this.valuePrice = valuePrice;
    }

    public double getValueSumCost() {
        return valueSumCost;
    }

    public void setValueSumCost(final double valueSum) {
        this.valueSumCost = valueSum;
    }

    public double getValueSumVat() {
        return valueSumVat;
    }

    public void setValueSumVat(final double valueSumVat) {
        this.valueSumVat = valueSumVat;
    }

    public double getValueSumCostAndVat() {
        return valueSumCostAndVat;
    }

    public void setValueSumCostAndVat(final double valueSumAndVat) {
        this.valueSumCostAndVat = valueSumAndVat;
    }

    public void setValuePriceCurrency(final double valuePriceCurrency) {
        this.valuePriceCurrency = valuePriceCurrency;
    }

    public double getValueSumCostCurrency() {
        return valueSumCostCurrency;
    }

    public void setValueSumCostCurrency(final double valueSumCurrency) {
        this.valueSumCostCurrency = valueSumCurrency;
    }

    public void setValueSumVatCurrency(final double valueSumVatCurrency) {
        this.valueSumVatCurrency = valueSumVatCurrency;
    }

    public double getValueSumCostAndVatCurrency() {
        return valueSumCostAndVatCurrency;
    }

    @Override
    public double getValueVat() {
        return valueVAT;
    }

    @Override
    public double getValueAllowanceRate() {
        return 0;
    }

    @Override
    public double getValueCost() {
        return getValuePrice();
    }

    @Override
    public RetailValue getRetailValue() {
        return null;
    }

    @Override
    public PriceListValue getPriceListValue() {
        return null;
    }

    @Override
    public double getTotalAmount() {
        return amount * amountInPack;
    }

    @Override
    public double getAccountingPrice() {
        return getValuePriceForAccountingAbs();
    }

    @Override
    public String getModelNumberAsString() {
        return String.valueOf(modelNumber);
    }

    public void setValueSumCostAndVatCurrency(final double valueSumAndVatCurrency) {
        this.valueSumCostAndVatCurrency = valueSumAndVatCurrency;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(final int documentId) {
        this.documentId = documentId;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(final String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(final int itemId) {
        this.itemId = itemId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(final String productName) {
        this.productName = productName;
    }

    public String getItemScanCode() {
        return itemScanCode;
    }

    public void setItemScanCode(final String itemScanCode) {
        this.itemScanCode = itemScanCode;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(final boolean changed) {
        this.changed = changed;
    }

    public int getItemSize() {
        return itemSize;
    }

    public void setItemSize(final int sizeItem) {
        this.itemSize = sizeItem;
    }

    public double getValuePriceForAccounting() {
        return valuePriceForAccounting;
    }

    public boolean isWrongAllowance() {
        return wrongAllowance;
    }

    public void setWrongAllowance(final boolean wrongAllowance) {
        this.wrongAllowance = wrongAllowance;
    }

    @TableHeader(name="Изображение", width = 200, sequence = 900)
    public JLabel getImage() {
        return image;
    }

    public void setImage(final JLabel image) {
        this.image = image;
    }

    @TableHeader(name = "Размер", width = 50, sequence = 4)
    public String getItemPrintSize() {
        if(itemPrintSize!=null) {
            return itemPrintSize.trim();
        }else{
            return "";
        }
    }

    public void setItemPrintSize(final String itemPrintSize) {
        this.itemPrintSize = itemPrintSize;
    }


}

