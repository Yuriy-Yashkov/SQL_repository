package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;
import by.march8.entities.classifier.ClassifierItem;
import by.march8.entities.classifier.ClassifierModelParams;

import javax.persistence.*;
import java.util.Date;


@NamedQueries({
        @NamedQuery(name = "SaleDocumentItemBase.findByDocumentId",
                query = "SELECT detail FROM SaleDocumentItemBase detail WHERE detail.documentId = :document " +
                        "order by " +
                        "detail.item.model.articleCode," +
                        "detail.item.model.modelNumber, " +
                        "detail.item.growth, " +
                        "detail.item.size, " +
                        "detail.item.itemGrade ASC ")
})

@Entity
@Table(name = "otgruz2")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class SaleDocumentItemBase extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    /**
     * Идентификатор документа, которому принадлежит запись
     */
    @Column(name = "doc_id")
    protected int documentId;

    /**
     * Идентификатор позиции товара из таблиц внутреннего перемещения и упаковки
     */
    @Column(name = "kod_str")
    protected int itemLabelId;

    /**
     * Идентификатор позиции товара из таблиц классификатора
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kod_izd")
    protected ClassifierItem item;

    /**
     * Идентификатор этапа добавления записей в документ
     * В случаях если заполнение накладной происходит не за один этап загрузки со сканера
     */
    @Column(name = "part")
    protected int partNumber;

    /**
     * Тип изделия (назначение пока не определено) 1 или 2
     */
    @Column(name = "tip")
    protected int itemType;

    /**
     * Количество единиц изделия
     */
    @Column(name = "kol")
    protected int amount;

    /**
     * Количество единиц изделия в упаковке
     */
    @Column(name = "kol_in_upack")
    protected int amountInPack;

    /**
     * Производственный скан-код
     */
    @Column(name = "scan")
    protected Long itemScanCode;

    /**
     * Государственный скан-код (ЕАН 13)
     */
    @Column(name = "eancode")

    protected String itemEanCode;

    /**
     * Цвет изделия
     */
    @Column(name = "ncw")
    protected String itemColor;

    /**
     * Сорт изделия
     */
    @Column(name = "srt")
    protected int itemGrade;

    /**
     * Размер изделия
     */
    @Column(name = "rzm_marh")
    protected int itemSize;

    /**
     * Рост изделия
     */
    @Column(name = "rst_marh")
    protected int itemGrowz;

    /**
     * Прейскурант цены на  изделие
     */
    @Column(name = "preiscur")
    protected String itemPriceList;

    /**
     * Учетная цена
     */
    @Column(name = "cena_uch")
    protected double valuePriceForAccounting;
    /**
     * Ставка НДС на изделие
     */
    @Column(name = "nds")
    protected float valueVAT;

    /**
     * Цена  в Бел. руб.
     */
    @Column(name = "cena")
    protected double valuePrice;

    /**
     * Сумма  в Бел. руб.
     */
    @Column(name = "summa")
    protected double valueSumCost;

    /**
     * Сумма  НДС в Бел. руб.
     */
    @Column(name = "summa_nds")
    protected double valueSumVat;

    /**
     * Сумма с НДС в Бел. руб.
     */

    @Column(name = "itogo")
    protected double valueSumCostAndVat;

    /**
     * Цена  в валюте
     */
    @Column(name = "cenav")

    protected double valuePriceCurrency;

    /**
     * Сумма  в валюте
     */
    @Column(name = "summav")
    protected double valueSumCostCurrency;

    /**
     * Сумма  НДС в валюте
     */
    @Column(name = "summa_ndsv")
    protected double valueSumVatCurrency;

    /**
     * Сумма с НДС в валюте
     */
    @Column(name = "itogov")

    protected double valueSumCostAndVatCurrency;

    //---------------------------------------------------------------------------
    // Штамп создания позиции
    /**
     * Имя рабочей станции, всегда SKLAD6 # proiz
     */
    @Column(name = "pc_ins")
    protected String systemUser;

    /**
     * Дата добавления записи, штамп
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_ins")
    protected Date systemDate;
    //---------------------------------------------------------------------------

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private TradeMarkupItem tradeMarkupItem;


    public SaleDocumentItemBase() {

    }

    /**
     * Конструктор на копирование изделий
     *
     * @param item объект-источнки
     */
    public SaleDocumentItemBase(SaleDocumentItemBase item) {
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
        setItemGrowz(item.getItemGrowz());

        setItemPriceList(item.getItemPriceList());

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

       // setTradeMarkValue(item.getTradeMarkValue());

        setSystemDate(item.getSystemDate());
        setSystemUser(item.getSystemUser());
    }


    @Override
    public String toString() {
        return "SaleDocumentDetailMarch{" +
                "id=" + id +
                ", document=" + documentId +
                ", itemMovingId=" + itemLabelId +
                ", item=" + item.toString() +
                ", partNumber=" + partNumber +
                ", itemType=" + itemType +
                ", amount=" + amount +
                ", amountInPack=" + amountInPack +
                ", itemScanCode=" + itemScanCode +
                ", itemEanCode=" + itemEanCode +
                ", itemColor='" + itemColor + '\'' +
                ", itemGrade=" + itemGrade +
                ", itemSize=" + itemSize +
                ", itemGrowz=" + itemGrowz +
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
                ", systemUser='" + systemUser + '\'' +
                ", systemDate=" + systemDate +
                '}';
    }


    public String printInfo() {
        ClassifierModelParams model = item.getModel();
        return model.getModelNumber() + " | " +
                model.getId() + " | " +
                item.getId() + " | " +
                model.getArticleName() + " | " +
                model.getName() + " | " +
                item.getSizePrint() + " | " +
                item.getItemGrade() + " | " +
                itemColor + " | " +
                amount + " | " +
                valuePrice + " | " +
                valuePriceCurrency + " | " +
                itemEanCode + " | " +
                itemScanCode + " | ";
    }

   // @TableHeader(name = "Модель", width = -50, sequence = 1)
    public String getItemModelNumber() {
        if (item != null) {
            ClassifierModelParams model = item.getModel();
            if (model != null) {
                return String.valueOf(model.getModelNumber());
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    @TableHeader(name = "Артикул", width = -85, sequence = 20)
    public int getItemArticleName() {
        if (item != null) {
            ClassifierModelParams model = item.getModel();
            if (model != null) {
                return model.getArticleCode();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @TableHeader(name = "Наименование", sequence = 10)
    public String getItemName() {
        if (item != null) {
            ClassifierModelParams model = item.getModel();
            if (model != null) {
                return model.getName();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

 //   @TableHeader(name = "Размер", sequence = 4)
    public String getItemSizePrint() {
        if (item != null) {
            return item.getSizePrint();
        } else {
            return "";
        }
    }

    @TableHeader(name = "Кол-во", width = -80, sequence = 40)
    public String getItemAmount() {
        if (item != null) {
            ClassifierModelParams model = item.getModel();
            if (model != null) {
                if (model.getArticleCode() >= 47000000 && model.getArticleCode() <= 48000000) {
                    double weight = (double) amountInPack / 100;
                    return String.valueOf(weight);
                } else {
                    return String.valueOf(getAmount() * getAmountInPack());
                }
            } else {
                return "0";
            }
        } else {
            return "0";
        }

    }


    @TableHeader(name = "Сорт", width = -40, sequence = 25)
    public int getItemGradeText() {
        return itemGrade ;
    }

    @TableHeader(name = "Цвет", sequence = 50)
    public String getItemColorText() {
        return getItemColor();
    }

    //@TableHeader(name = "Кол-во", width = -40, sequence = 7)
    public int getItemsAmount() {
        return getAmount() * getAmountInPack();
    }


    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getItemLabelId() {
        return itemLabelId;
    }

    public void setItemLabelId(final int itemMovingId) {
        this.itemLabelId = itemMovingId;
    }

    public ClassifierItem getItem() {
        return item;
    }

    public void setItem(final ClassifierItem item) {
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

    public void setAmountInPack(final int amountInPack) {
        this.amountInPack = amountInPack;
    }

    public Long getItemScanCode() {
        return itemScanCode;
    }

    public void setItemScanCode(final Long itemScanCode) {
        this.itemScanCode = itemScanCode;
    }

    //@TableHeader(name = "EAN", sequence = 20)
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

    public int getItemSize() {
        return itemSize;
    }

    public void setItemSize(final int itemSize) {
        this.itemSize = itemSize;
    }

    public int getItemGrowz() {
        return itemGrowz;
    }

    public void setItemGrowz(final int itemGrowz) {
        this.itemGrowz = itemGrowz;
    }

    public String getItemPriceList() {
        return itemPriceList;
    }

    public void setItemPriceList(final String itemPriceList) {
        this.itemPriceList = itemPriceList;
    }

    @TableHeader(name = "Цена, рубли", width = 80, sequence = 30)
    public double getValuePriceForAccountingAbs() {
        if(valuePriceForAccounting<0){
            return valuePriceForAccounting *(-1);
        }else {
            return valuePriceForAccounting;
        }
    }

    public double getValuePriceForAccounting_() {
        return valuePriceForAccounting;
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

    //@TableHeader(name = "Бел. руб_Цена", width = 80, sequence = 8)
    public double getValuePrice() {
        return valuePrice;
    }

   // @TableHeader(name = "Бел. руб_Сумма", width = 80, sequence = 9)
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

   // @TableHeader(name = "Бел. руб_НДС", width = 80, sequence = 10)
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

    @TableHeader(name = "Цена, валюта", width = 80, sequence = 31)
    public double getValuePriceCurrency() {
        return valuePriceCurrency;
    }

   // @TableHeader(name = "Валюта_Сумма", width = 80, sequence = 12)
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
   // @TableHeader(name = "Валюта_НДС", width = 80, sequence = 13)
    public double getValueSumVatCurrency() {
        return valueSumVatCurrency;
    }

    public void setValueSumVatCurrency(final double valueSumVatCurrency) {
        this.valueSumVatCurrency = valueSumVatCurrency;
    }

    public double getValueSumCostAndVatCurrency() {
        return valueSumCostAndVatCurrency;
    }

    public void setValueSumCostAndVatCurrency(final double valueSumAndVatCurrency) {
        this.valueSumCostAndVatCurrency = valueSumAndVatCurrency;
    }

    public String getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(final String systemUser) {
        this.systemUser = systemUser;
    }

    public Date getSystemDate() {
        return systemDate;
    }

    public void setSystemDate(final Date systemDate) {
        this.systemDate = systemDate;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(final int document) {
        this.documentId = document;
    }

    public TradeMarkupItem getTradeMarkupItem() {
        return tradeMarkupItem;
    }

    public void setTradeMarkupItem(final TradeMarkupItem tradeMarkupItem) {
        this.tradeMarkupItem = tradeMarkupItem;
    }
}

