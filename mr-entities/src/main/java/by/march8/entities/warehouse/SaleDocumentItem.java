package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;
import java.util.Date;


/*@NamedQueries({
        @NamedQuery(name = "SaleDocumentDetailMarch.findByDocumentId",
                query = "SELECT detail FROM SaleDocumentItem detail WHERE detail.document.id = :document " +
                        "order by " +
                        "detail.item.model.articleCode," +
                        //"detail.item.model.articleName," +
                        "detail.item.model.modelNumber, " +
                        "detail.item.growth, " +
                        "detail.item.sizeItem, " +
 *//*                       //deta
                        "detail.itemColor, " +
                        "detail.item.sizePrint, " +
 *//*                      "detail.item.grade ASC "),

        @NamedQuery(name = "SaleDocumentDetailMarch.findByDocumentNumber_",
                query = "SELECT detail FROM SaleDocumentItem detail WHERE detail.document.documentNumber = :document " +
                        "order by " +
                        "detail.item.model.articleCode," +
                        //"detail.item.model.articleName," +
                        "detail.item.model.modelNumber, " +
                        "detail.item.growth, " +
                        "detail.item.sizeItem, " +
 *//*                       //deta
                        "detail.itemColor, " +
                        "detail.item.sizePrint, " +
 *//*                      "detail.item.grade ASC ")
})*/

/**
 * Класс спецификации единицы изделия в накладной
 */
@Entity
@Table(name = "otgruz2")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class SaleDocumentItem extends BaseEntity implements ISummingUp {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Идентификатор документа, которому принадлежит запись
     */
    @ManyToOne()
    @JoinColumn(name = "doc_id")
    private SaleDocument document;

    /**
     * Идентификатор позиции товара из таблиц внутреннего перемещения и упаковки
     */
    @Column(name = "kod_str")
    private int itemLabelId;

    /**
     * Идентификатор позиции товара из таблиц классификатора
     */
    //@OneToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "kod_izd")
    @Column(name = "kod_izd")
    private int item;

    /**
     * Идентификатор этапа добавления записей в документ
     * В случаях если заполнение накладной происходит не за один этап загрузки со сканера
     */
    @Column(name = "part")
    private int partNumber;

    /**
     * Тип изделия (назначение пока не определено) 1 или 2
     */
    @Column(name = "tip")
    private int itemType;

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
     * Производственный скан-код
     */
    @Column(name = "scan")
    private Long itemScanCode;

    /**
     * Государственный скан-код (ЕАН 13)
     */
    @Column(name = "eancode")

    private String itemEanCode;

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
     * Размер изделия
     */
    @Column(name = "rzm_marh")
    private int itemSize;

    /**
     * Рост изделия
     */
    @Column(name = "rst_marh")
    private int itemGrowth;

    /**
     * Прейскурант цены на  изделие
     */
    @Column(name = "preiscur")
    private String itemPriceList;

    /**
     * Учетная цена
     */
    @Column(name = "cena_uch")
    private double valuePriceForAccounting;
    /**
     * Ставка НДС на изделие
     */
    @Column(name = "nds")
    private double valueVAT;

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

/*  @Column(name = "trade_mark_value")
    private float tradeMarkValue;*/

    //---------------------------------------------------------------------------
    // Штамп создания позиции
    /**
     * Имя рабочей станции, всегда SKLAD6 # proiz
     * 12.08.19 теперь тут храним размер скидки
     */

    // Такая пляска нужна так как новое поле, которое содержало бы скидку, в таблицу otgruz2
    // добавить нельзя. поэтому для хранения размера скидки будем пользовать поле которое
    // используется только раз - при добавлении новой записи, а мы в нем будем хранить размер скидки
    @Column(name = "pc_ins")
    private String discount;

    /**
     * Дата добавления записи, штамп
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_ins")
    private Date systemDate;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private TradeMarkupItem tradeMarkupItem;
    //---------------------------------------------------------------------------


    @Override
    public double getTradeMarkup() {
        if (tradeMarkupItem != null) {
            return tradeMarkupItem.getValueTradeMarkup();
        } else {
            return 0;
        }
    }

    public SaleDocumentItem() {

    }

    /**
     * Конструктор на копирование изделий
     *
     * @param item объект-источнки
     */
    public SaleDocumentItem(SaleDocumentItem item) {
        setItemLabelId(item.getItemLabelId());
        setItem(item.getItem());

        setPartNumber(item.getPartNumber());
        setItemType(item.getItemType());

        setAmount(item.getAmount());
        setAmountInPack(item.getAmountInPack());

        setItemScanCode(item.getItemScanCode());
        setItemEanCode(item.getItemEanCode());

        setItemColor(item.getItemColor());
        setItemGrade(item.getItemGrade());
        setItemSize(item.getItemSize());
        setItemGrowth(item.getItemGrowth());

        setItemPriceList(item.getItemPriceList());

        setValuePriceForAccounting(item.getValuePriceForAccounting());
        setValueVAT(item.getValueVAT());

        setValuePrice(item.getValuePrice());
        setValueSumCost(item.getValueSumCost());
        setValueSumVat(item.getValueSumVat());
        setValueSumCostAndVat(item.getValueSumCostAndVat());

        setValuePriceCurrency(item.getValuePriceCurrency());
        setValueSumCostCurrency(item.getValueSumCostCurrency());
        setValueSumVatCurrency(item.getValueSumVatCurrency());
        setValueSumCostAndVatCurrency(item.getValueSumCostAndVatCurrency());

        // setTradeMarkValue(item.getTradeMarkValue());

        setSystemDate(item.getSystemDate());
        setDiscount(item.getDiscount());
    }

    @Override
    public String toString() {
        return "SaleDocumentDetailMarch{" +
                "id=" + id +
                ", document=" + document +
                ", itemMovingId=" + itemLabelId +
                ", partNumber=" + partNumber +
                ", itemType=" + itemType +
                ", amount=" + amount +
                ", amountInPack=" + amountInPack +
                ", itemScanCode=" + itemScanCode +
                ", itemEanCode=" + itemEanCode +
                ", itemColor='" + itemColor + '\'' +
                ", itemGrade=" + itemGrade +
                ", itemSize=" + itemSize +
                ", itemGrowz=" + itemGrowth +
                ", itemPriceList='" + itemPriceList + '\'' +
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
                ", systemUser='" + discount + '\'' +
                ", systemDate=" + systemDate +
                '}';
    }

    @Override
    public double getValueVat() {
        return valueVAT;
    }

    @Override
    public double getValueAllowanceRate() {
        if (tradeMarkupItem != null) {
            return tradeMarkupItem.getValueTradeMarkup();
        }
        return -1;
    }

    @Override
    public double getValueCost() {
        return valuePrice;
    }


    @TableHeader(name = "Цвет", sequence = 6)
    public String getItemColorText() {
        return getItemColor();
    }

    @TableHeader(name = "Кол-во", width = -40, sequence = 7)
    public int getItemsAmount() {
        return getAmount() * getAmountInPack();
    }


    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public SaleDocument getDocument() {
        return document;
    }

    public void setDocument(final SaleDocument document) {
        this.document = document;
    }

    public int getItemLabelId() {
        return itemLabelId;
    }

    public void setItemLabelId(final int itemMovingId) {
        this.itemLabelId = itemMovingId;
    }

    public int getItem() {
        return item;
    }

    public void setItem(final int item) {
        this.item = item;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(final int partNumber) {
        this.partNumber = partNumber;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(final int itemType) {
        this.itemType = itemType;
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


    public double getWeight() {
        return 0;
    }

    public void setAmountInPack(final int amountInPack) {
        this.amountInPack = amountInPack;
    }

    public Long getItemScanCode() {
        return itemScanCode;
    }

    public void setItemScanCode(final Long itemScanCode) {
        this.itemScanCode = itemScanCode;
    }

    @TableHeader(name = "EAN", sequence = 20)
    public String getItemEanCode() {
        if (itemEanCode != null) {
            return itemEanCode.trim();
        }
        System.out.println("Пустой EAN код");
        return "";
    }

    public void setItemEanCode(final String itemEanCode) {
        this.itemEanCode = itemEanCode;
    }

    public String getItemColor() {
        if (itemColor != null) {
            return itemColor.trim();
        } else {
            return "";
        }
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

    public int getItemSize() {
        return itemSize;
    }

    @Override
    public String getArticleName() {
        return "";
    }

    public String getArtInNSI(){

        return  "";
    }

    public void setItemSize(final int itemSize) {
        this.itemSize = itemSize;
    }

    public int getItemGrowth() {
        return itemGrowth;
    }

    public void setItemGrowth(final int itemGrowth) {
        this.itemGrowth = itemGrowth;
    }

    public String getItemPriceList() {
        if (itemPriceList != null) {
            return itemPriceList.trim();
        } else {
            return "";
        }
    }

    public void setItemPriceList(final String itemPriceList) {
        this.itemPriceList = itemPriceList;
    }

    public double getValuePriceForAccounting() {
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

    public double getValueVAT() {
        return valueVAT;
    }

    public void setValueVAT(final double valueVAT) {
        this.valueVAT = valueVAT;
    }

    @TableHeader(name = "Бел. руб_Цена", width = 80, sequence = 8)
    public double getValuePrice() {
        return valuePrice;
    }

    @TableHeader(name = "Бел. руб_Сумма", width = 80, sequence = 9)
    public double getValueSum() {
        return valueSumCost;
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

    @TableHeader(name = "Бел. руб_НДС", width = 80, sequence = 10)
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

    @TableHeader(name = "Валюта_Цена", width = 80, sequence = 11)
    public double getValuePriceCurrency() {
        return valuePriceCurrency;
    }

    @TableHeader(name = "Валюта_Сумма", width = 80, sequence = 12)
    public double getValueSumPriceCurrency() {
        return valueSumCostCurrency;
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

    @TableHeader(name = "Валюта_НДС", width = 80, sequence = 13)
    public double getValueSumVatCurrency() {
        return valueSumVatCurrency;
    }

    public void setValueSumVatCurrency(final double valueSumVatCurrency) {
        this.valueSumVatCurrency = valueSumVatCurrency;
    }

    public double getValueSumCostAndVatCurrency() {
        return valueSumCostAndVatCurrency;
    }


    @Override
    public RetailValue getRetailValue() {
        return null;
    }

    @Override
    public double getTotalAmount() {
        return amount * amountInPack;
    }

    public void setValueSumCostAndVatCurrency(final double valueSumAndVatCurrency) {
        this.valueSumCostAndVatCurrency = valueSumAndVatCurrency;
    }

    public String getDiscount() {
        if (discount == null) {
            discount = "0";
            return discount;
        } else {
            return discount.trim();
        }
    }

    public void setDiscount(final String discount) {
        this.discount = discount;
    }

    public Date getSystemDate() {
        return systemDate;
    }

    public void setSystemDate(final Date systemDate) {
        this.systemDate = systemDate;
    }

/*    public float getTradeMarkValue() {
        return tradeMarkValue;
    }

    public void setTradeMarkValue(final float tradeMark) {
        this.tradeMarkValue = tradeMark;
    }*/

    public TradeMarkupItem getTradeMarkupItem() {
        return tradeMarkupItem;
    }

    public void setTradeMarkupItem(final TradeMarkupItem tradeMarkupItem) {
        this.tradeMarkupItem = tradeMarkupItem;
    }

    @Override
    public double getAccountingPrice() {
        if (valuePriceForAccounting < 0) {
            return valuePriceForAccounting * (-1);
        } else {
            return valuePriceForAccounting;
        }
    }

    @Override
    public String getModelNumberAsString() {
        return "";
    }

    @Override
    public PriceListValue getPriceListValue() {
        return null;
    }

    /**
     * Инвертирование цены прейскуратна
     */
    public void invertAccountingPrice() {
        if (valuePriceForAccounting < 0) {
            valuePriceForAccounting = valuePriceForAccounting * (-1);
        }
    }
}

