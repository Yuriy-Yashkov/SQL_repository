package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.entities.sales.PreOrderSaleDocumentItem;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;


/*@NamedQueries({
        @NamedQuery(name = "SaleDocumentDetailItemReport.findByDocumentNumber",
                query = "SELECT detail FROM SaleDocumentDetailItemReport detail WHERE detail.document = :document ")
})*/

/**
 * Класс спецификации единицы изделия в накладной для отчетов
 */
//@Entity
//@Table(name = "VSaleDocumentDetailReport")
@SuppressWarnings("unused")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class SaleDocumentDetailItemReport extends BaseEntity implements ISummingUp {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "ndoc")
    private String document;

    /**
     * Шифр артикула
     */
    @Column(name = "article_code")
    private String articleCode;
    /**
     * Номер артикула
     */
    @Column(name = "article_number")
    private String articleNumber;
    /**
     * Номер модели
     */
    @Column(name = "model_number")
    private String modelNumber;

    @Column(name = "ptk")
    private int ptkCode;

    /**
     * ТНВЭД код
     */
    @Column(name = "tnvd_code")
    private String tnvedCode;

    /**
     * EAN13 код
     */
    @Column(name = "ean_code")
    private String eanCode;

    /**
     * Наименование модели
     */
    @Column(name = "item_name")
    private String itemName;

    /**
     * Цвет
     */
    @Column(name = "item_color")
    private String itemColor;

    /**
     * Сорт изделия
     */
    @Column(name = "item_grade")
    private int itemGrade;

    /**
     * Размер изделия
     */
    @Column(name = "item_size")
    private int itemSize;

    /**
     * Рост изделия
     */
    @Column(name = "item_growth")
    private int itemGrowz;

    /**
     * Размер изделия для печати
     */
    @Column(name = "item_size_print")
    private String itemSizePrint;


    /**
     * Количество единиц изделия
     */
    @Column(name = "amount")
    private int amount;

    /**
     * Количество единиц изделия с учетом упаковок
     */
    @Column(name = "amount_all")
    private int amountAll;

    private double amountPrint;

    /**
     * Учетная цена
     */
    @Column(name = "accounting_price")
    private double valueAccountingPrice;

    @Transient
    private double valueAccountingPriceAlt;
    /**
     * Ставка НДС на изделие
     */
    @Column(name = "vat")
    private float valueVAT;
    /**
     * Цена  в Бел. руб.
     */
    @Column(name = "price")
    private double valuePrice;

    /**
     * Сумма  в Бел. руб.
     */
    @Column(name = "sum_cost")
    private double valueSumCost;

    /**
     * Сумма  НДС в Бел. руб.
     */
    @Column(name = "sum_vat")
    private double valueSumVat;

    /**
     * Сумма с НДС в Бел. руб.
     */
    @Column(name = "sum_cost_vat")
    private double valueSumCostAndVat;

    /**
     * Цена  в валюте
     */
    @Column(name = "price_currency")
    private double valuePriceCurrency;

    /**
     * Сумма  в валюте
     */
    @Column(name = "sum_cost_currency")
    private double valueSumCostCurrency;

    /**
     * Сумма  НДС в валюте
     */
    @Column(name = "sum_vat_currency")
    private double valueSumVatCurrency;

    /**
     * Сумма с НДС в валюте
     */
    @Column(name = "sum_cost_vat_currency")
    private double valueSumCostAndVatCurrency;

    /**
     * Размер торговой надбавки
     */
    @Column(name = "trade_markup")
    private double valueTradeMarkup;

    /**
     * Розничная цена
     */
    @Column(name = "retail_price")
    private double valueRetailPrice;

    /**
     * Вес изделия
     */
    @Column(name = "weight")
    private double weight;
    /**
     * Прейскурант цены на  изделие
     */
    @Column(name = "price_list")
    private String itemPriceList;



    @Column(name = "discount")
    private double discount;


    private String composition;
    private String canvasComposition;

    @Transient
    private String certificateType;

    @Transient
    private String certificateName;

    @Transient
    private String licenseType;

    @Transient
    private String licenseName;

    @Transient
    private int itemAgeGroup;

    @Transient
    private String gradeAsString;

    @Transient
    private RetailValue retailValue = new RetailValue();

    @Transient
    private PriceListValue priceListValue = new PriceListValue();

    private String measureUnit;
    private String valueVATCode;

    @Transient
    private double accountingVat;

    @Transient
    private int colorCode;

    @Transient
    private String imagePath;

    @Transient
    private double tradeMarkup;

    @Transient
    private boolean commonColor;
    @Transient
    private String labelComposition;

    @Transient
    private double planPrice;

    @Transient
    private double planProfitability;

    @Transient
    private double planPrimeCost;

    @Transient
    private String yearsOld;

    //private double discountValue;

    public SaleDocumentDetailItemReport(PreOrderSaleDocumentItem item) {

    }


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }


    @Override
    public double getValueVat() {
        return valueVAT;
    }

    @Override
    public double getValueAllowanceRate() {
        return valueTradeMarkup;
    }

    @Override
    public double getValueCost() {
        return valuePrice;
    }


    public SaleDocumentDetailItemReport(final String itemName, final String modelNumber, final String tnvedCode, final String itemColor, final int itemGrade, final String itemSizePrint, final double amountPrint, final String imagePath) {
        this.modelNumber = modelNumber;
        this.tnvedCode = tnvedCode;
        this.itemName = itemName;
        this.itemColor = itemColor;
        this.itemGrade = itemGrade;
        this.itemSizePrint = itemSizePrint;
        this.amountPrint = amountPrint;
        this.imagePath = imagePath;
    }

    public SaleDocumentDetailItemReport() {
    }

    public String getArticleCode() {
        if (articleCode != null) {
            return articleCode.trim();
        } else {
            return "";
        }
    }

    public void setArticleCode(final String articleCode) {
        this.articleCode = articleCode;
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

    public String getTnvedCode() {
        if (tnvedCode != null) {
            return tnvedCode.trim();
        } else {
            return "";
        }
    }

    public void setTnvedCode(final String tnvedCode) {
        this.tnvedCode = tnvedCode;
    }

    public String getEanCode() {
        return eanCode;
    }

    public void setEanCode(final String eanCode) {
        this.eanCode = eanCode;
    }

    public String getItemName() {
        if (itemName != null) {
            return itemName.trim().replace("  ", " ");
        } else {
            return "";
        }
    }

    public void setItemName(final String itemName) {
        this.itemName = itemName;
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
        return getArticleNumber();
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


    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

    public int getAmountAll() {
        return amountAll;
    }

    public void setAmountAll(final int amountAll) {
        this.amountAll = amountAll;
    }

    public double getValueAccountingPrice() {
        return valueAccountingPrice;
    }

    public double getValueAccountingPriceAbs() {
        if (valueAccountingPrice < 0) {
            return valueAccountingPrice * (-1);
        } else {
            return valueAccountingPrice;
        }
    }

    public void setValueAccountingPrice(final double valueAccountingPrice) {
        this.valueAccountingPrice = valueAccountingPrice;
    }

    public float getValueVAT() {
        return valueVAT;
    }

    public void setValueVAT(final float valueVAT) {
        this.valueVAT = valueVAT;
    }

    public double getValuePrice() {
        return valuePrice;
    }

    public void setValuePrice(final double valuePrice) {
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

    public double getValueSumCostAndVat() {
        return valueSumCostAndVat;
    }

    public void setValueSumCostAndVat(final double valueSumCostAndVat) {
        this.valueSumCostAndVat = valueSumCostAndVat;
    }

    public double getValuePriceCurrency() {
        return valuePriceCurrency;
    }

    public void setValuePriceCurrency(final double valuePriceCurrency) {
        this.valuePriceCurrency = valuePriceCurrency;
    }

    public double getValueSumCostCurrency() {
        return valueSumCostCurrency;
    }

    public void setValueSumCostCurrency(final double valueSumCostCurrency) {
        this.valueSumCostCurrency = valueSumCostCurrency;
    }

    public double getValueSumVatCurrency() {
        return valueSumVatCurrency;
    }

    public void setValueSumVatCurrency(final double valueSumVatCurrency) {
        this.valueSumVatCurrency = valueSumVatCurrency;
    }

    public double getValueSumCostAndVatCurrency() {
        return valueSumCostAndVatCurrency;
    }

    public int getAmountInPack() {
        return getAmountAll();
    }

    public void setValueSumCostAndVatCurrency(final double valueSumCostAndVatCurrency) {
        this.valueSumCostAndVatCurrency = valueSumCostAndVatCurrency;
    }

    public double getValueTradeMarkup() {
        return valueTradeMarkup;
    }

    public void setValueTradeMarkup(final double valueTradeMarkup) {
        this.valueTradeMarkup = valueTradeMarkup;
    }

    public double getValueRetailPrice() {
        return valueRetailPrice;
    }

    public void setValueRetailPrice(final double valueRetailPrice) {
        this.valueRetailPrice = valueRetailPrice;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(final double weight) {
        this.weight = weight;
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


    public String getDocument() {
        return document;
    }

    public void setDocument(final String document) {
        this.document = document;
    }

    public String getItemSizePrint() {
        if (itemSizePrint != null) {
            return itemSizePrint.trim().replace("-", "-");
        } else {
            return "_";
        }
    }

    public void setItemSizePrint(final String itemSizePrint) {
        this.itemSizePrint = itemSizePrint;
    }

    public String getCertificateType() {
        if (certificateType != null) {
            return certificateType.trim();
        } else {
            return "";
        }
    }

    public void setCertificateType(final String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateName() {
        if (certificateName != null) {
            return certificateName.trim();
        } else {
            return "";
        }
    }

    public String getLicenseType() {
        if (licenseType != null) {
            return licenseType.trim();
        } else {
            return "";
        }
    }

    public void setLicenseType(final String licenseType) {
        this.licenseType = licenseType;
    }

    public String getLicenseName() {
        if (licenseName != null) {
            return licenseName.trim();
        } else {
            return "";
        }
    }

    public void setLicenseName(final String licenseName) {
        this.licenseName = licenseName;
    }

    public void setCertificateName(final String certificateName) {
        this.certificateName = certificateName;
    }

    /**
     * Возвращает полное наименование изделия строкой в формате приложения к документу
     *
     * @return наименование изделия
     */
    public String getProductNameString() {
        if (getMeasureUnitAsCode() == 166) {
            return getItemName() + " " + getArticleNumber();
        } else {
            return getItemName() + " " + getArticleNumber() + " модель " + getModelNumber();
        }
    }

    public String getProductNameSizeString() {
        return getItemName() + "\n" + getArticleNumber() + "\nмодель " + getModelNumber() + "\nРазмер:" + getItemSizePrint();
    }

    public String getProductNameSizeStringSingleLine() {
        return getItemName() + " модель: " + getModelNumber() + " Размер:" + getItemSizePrint();
    }

    public String getProductNameSizeStringNew() {
        if (isCommonColor()) {
            return getItemName() + " " + getArticleNumber() + "\nМодель: " + getModelNumber() + "\nРазмер: " + getItemSizePrint();
        } else {
            return getItemName() + " " + getArticleNumber() + "\nМодель: " + getModelNumber() + "\nРазмер: " + getItemSizePrint() + "\nЦвет: " + getItemColor();
        }
    }

    public String getCertificateDescription() {
        if (getLicenseType().equals("")) {
            return getCertificateType() + ":" + getCertificateName();
        } else {
            return getCertificateType() + ":" + getCertificateName() + " : " + getLicenseType() + ":" + getLicenseName();
        }
    }

    public void setGradeAsString(final String gradeAsString) {
        this.gradeAsString = gradeAsString;
    }

    public String getGradeAsString() {
        return gradeAsString;
    }

    /**
     * Маска вывода [X сорт]
     *
     * @return строка сорта
     */
    public String getGradeAsStringPlus() {
        if (gradeAsString != null) {
            return gradeAsString + " сорт";
        } else {
            return "сорт";
        }
    }

    public double getAmountPrint() {
        if (amountPrint == 0) {
            return getAmountAll();
        }
        return amountPrint;
    }

    public void setAmountPrint(final double amountPrint) {
        this.amountPrint = amountPrint;
    }

    public RetailValue getRetailValue() {
        return retailValue;
    }

    @Override
    public double getTotalAmount() {
        return amountAll;
    }

    public void setRetailValue(final RetailValue retailValue) {
        this.retailValue = retailValue;
    }

    @Override
    public double getAccountingPrice() {
        if (valueAccountingPrice < 0) {
            return valueAccountingPrice * (-1);
        } else {
            return valueAccountingPrice;
        }
    }

    @Override
    public String getModelNumberAsString() {
        return getModelNumber();
    }

    @Override
    public PriceListValue getPriceListValue() {
        return priceListValue;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(final String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public double getValueAccountingPriceAlt() {
        return valueAccountingPriceAlt;
    }

    public void setValueAccountingPriceAlt(final double valueAccountingPriceAlt) {
        this.valueAccountingPriceAlt = valueAccountingPriceAlt;
    }

    /**
     * Возвращает единицу измерения изделия как международный код
     *
     * @return код единицы измерения
     */
    public int getMeasureUnitAsCode() {
        switch (measureUnit) {
            case "шт.":
                return 796;
            case "пар":
                return 715;
            case "кг.":
                return 166;
        }
        return 796;
    }

    public String getMeasureUnitAsUOMCode() {
        switch (measureUnit) {
            case "шт.":
                return "PCE";
            case "пар":
                return "NPR";
            case "кг.":
                return "KGM";
        }
        return "PCE";
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(final int colorCode) {
        this.colorCode = colorCode;
    }

    public String getValueVATCode() {
        if (getValueVAT() > 1.0) {
            return String.valueOf(valueVAT);
        } else {
            return "-";
        }
    }

    public double getAccountingVat() {
        return accountingVat;
    }

    public void setAccountingVat(final double accountingVat) {
        this.accountingVat = accountingVat;
    }

    /**
     * Возвращает сорт для всех изделий по-умолчанию, и если изделие чулочно-носочное
     * и сортом больше 1-го то принимаем сорт как 3 (уценка)
     *
     * @return сорт изделия
     */
    public int getGradeIfItemIsSock() {
        switch (measureUnit) {
            case "шт.":
                String unit = articleCode.substring(0, 2);
                boolean stock = false;

                switch (unit) {
                    case "43":
                        stock = true;
                        break;
                    default:
                        stock = false;
                }

                if (stock) {
                    if (itemGrade > 1) {
                        return 3;
                    } else {
                        return itemGrade;
                    }
                } else {
                    return itemGrade;
                }

            case "пар":
                if (itemGrade > 1) {
                    return 3;
                } else {
                    return itemGrade;
                }
            case "кг.":
                return itemGrade;
        }
        return 796;
    }

    public int getPtkCode() {
        return ptkCode;
    }

    public void setPtkCode(final int ptkCode) {
        this.ptkCode = ptkCode;
    }

    public int getItemAgeGroup() {
        return itemAgeGroup;
    }

    public void setItemAgeGroup(final int itemAgeGroup) {
        this.itemAgeGroup = itemAgeGroup;
    }

    public String getComposition() {
        if (composition != null) {
            return composition.trim();
        } else {
            return "";
        }
    }

    public void setComposition(final String composition) {
        this.composition = composition;
    }


    public String getItemGenderType() {
        try {
            String s = articleCode.substring(2, 3);

            //int type = Integer.valueOf(s);
            switch (s) {
                case "1":
                    return "МУЖСКАЯ";
                case "2":
                    return "ЖЕНСКАЯ";
                case "3":
                    return "ДЕТСКАЯ";
                case "5":
                    return "СПОРТИВНЫЙ ВЗРОСЛЫЙ";
                case "6":
                    return "СПОРТИВНЫЙ ДЕТСКИЙ";
                default:
                    return "ПРОЧЕЕ";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Нет данных";
    }

    public String getItemType() {
        try {
            String s = articleCode.substring(1, 2);

            //int type = Integer.valueOf(s);
            switch (s) {
                case "1":
                    return "Белье";
                case "2":
                    return "Верхний трикотаж";
                case "3":
                    return "Чулки";
                default:
                    return "Прочее";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Нет данных";
    }

    public String getItemColorNew() {
        String color = itemColor.toUpperCase().trim().replace(".", "");
        String constColor = "МУЛЬТИКОЛОР";
        switch (color) {
            case "ПЕСТРОВЯЗ.":
                return constColor;
            case "НАБИВКА":
                return constColor;
            case ".РАЗНОЦВЕТ":
                return constColor;
            case "ПЕСТРОВЯЗ.БЕЛ":
                return constColor;
            case "ПЕСТРОВЯЗ.КРАШ":
                return constColor;
            case "ПЕСТРОВЯЗ.СЕРЫЙ":
                return constColor;
            case "РАЗНОЦВЕТ":
                return constColor;
            case "ПОЛОСКА":
                return constColor;
            case "ПЕСТРОВЯЗАНЫЙ":
                return constColor;

            default:
                return getItemColor();
        }
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(final String imagePath) {
        this.imagePath = imagePath;
    }


    @Override
    public double getTradeMarkup() {
        return tradeMarkup;
    }

    public void setTradeMarkup(final double tradeMarkup) {
        this.tradeMarkup = tradeMarkup;
    }

    public boolean isCommonColor() {
        return commonColor;
    }

    public void setCommonColor(boolean commonColor) {
        this.commonColor = commonColor;
    }

    public String getCanvasComposition() {
        if (canvasComposition != null) {
            return canvasComposition.trim();
        } else {
            return "";
        }
    }

    public void setCanvasComposition(String canvasComposition) {
        this.canvasComposition = canvasComposition;
    }

    public String getBelongAgeCategory() {
        String name = getItemName();
        if (name.toLowerCase().contains("муж")) {
            return "мужской";
        }

        if (name.toLowerCase().contains("жен")) {
            return "женский";
        }

        if (name.toLowerCase().contains("мал")) {
            return "мальчуковый";
        }

        if (name.toLowerCase().contains("дев")) {
            return "девичий";
        }

        if (name.toLowerCase().contains("дет")) {
            return "детский";
        }

        return "";
    }

    public String getLabelComposition() {
        return labelComposition;
    }

    public void setLabelComposition(String labelComposition) {
        this.labelComposition = labelComposition;
    }

    public String getItemCategory() {
        if (!getArticleCode().equals("")) {
            return articleCode.substring(1, 3);
        } else {
            return "000";
        }
    }

    public double getPlanPrice() {
        return planPrice;
    }

    public void setPlanPrice(double planPrice) {
        this.planPrice = planPrice;
    }

    public double getPlanProfitability() {
        return planProfitability;
    }

    public void setPlanProfitability(double planProfitability) {
        this.planProfitability = planProfitability;
    }

    public void setPlanPrimeCost(double planPrimeCost) {
        this.planPrimeCost = planPrimeCost;
    }

    public double getPlanPrimeCost() {
        return planPrimeCost;
    }

    public String getYearsOld() {
        return yearsOld;
    }

    public void setYearsOld(String yearsOld) {
        this.yearsOld = yearsOld;
    }

    public double getDiscountValue() {
        return discount;
    }

    public void setDiscountValue(double discountValue) {
        this.discount = discountValue;
    }
}

