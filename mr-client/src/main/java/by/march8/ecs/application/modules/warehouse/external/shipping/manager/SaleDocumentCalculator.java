package by.march8.ecs.application.modules.warehouse.external.shipping.manager;

import by.march8.ecs.application.modules.warehouse.external.shipping.dao.SaleDocumentJDBC;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.ProductionItem;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.Reduction3Grade;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.TotalSummingUp;
import by.march8.ecs.application.modules.warehouse.external.shipping.services.Reduction3GradeService;
import by.march8.ecs.application.modules.warehouse.external.shipping.services.RemainsReductionService;
import by.march8.ecs.application.modules.warehouse.external.shipping.services.SpecialPriceService;
import by.march8.ecs.framework.common.model.RoundingRule;
import by.march8.ecs.framework.helpers.digits.CurrencyType;
import by.march8.entities.warehouse.ISummingUp;
import by.march8.entities.warehouse.InvoiceType;
import by.march8.entities.warehouse.PriceListValue;
import by.march8.entities.warehouse.RetailValue;
import by.march8.entities.warehouse.SaleDocument;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import by.march8.entities.warehouse.SaleDocumentItem;
import by.march8.entities.warehouse.TradeMarkupItem;
import dept.marketing.cena.CenaPDB;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import static by.march8.entities.warehouse.InvoiceType.DOCUMENT_REFUND_BUYER;
import static by.march8.entities.warehouse.InvoiceType.DOCUMENT_REFUND_MATERIAL;
import static by.march8.entities.warehouse.InvoiceType.DOCUMENT_REFUND_RETAIL;
import static by.march8.entities.warehouse.InvoiceType.DOCUMENT_SALE_BUYER;
import static by.march8.entities.warehouse.InvoiceType.DOCUMENT_SALE_MATERIAL;
import static by.march8.entities.warehouse.InvoiceType.DOCUMENT_SALE_RETAIL;

/**
 * Вспомогательный набор функций для выполнения расчетов документа.
 *
 * @author Andy
 */
@SuppressWarnings("all")
public class SaleDocumentCalculator {

    public static final int ROUND_NO_ROUND = 0;
    public static final int ROUND_XXXX = 1;
    public static final int ROUND_X100 = 2;
    public static final int ROUND_XXX_X = 3;
    public static final int ROUND_XXX_XX = 4;
    public static final int ROUND_XXX_XXXX = 5;
    public static final int ROUND_XXX_XXX = 6;
    public static final int ROUND_DOWN_XXX_XX = 7;
    private static final RoundingRule roundingRule_100 = new RoundingRule(50, 50, 100);
    private static final RoundingRule roundingRule_50 = new RoundingRule(25, 75, 50);
    /**
     * НДС получаем из справочника
     */
    private static final int VAT_CONSTANT = 0;
    /**
     * НДС получаем из справочника
     */
    private static final int VAT_REFERENCE = 1;
    /**
     * Документу не нужен расчет НДС
     */
    private static final int VAT_NOT_NEED = 2;

    /*
    *             // Без округления
            case 0:break;
            // Округлять до целого
            case 1:break;
            // Округление 100 до запятой
            case 2:break;
            // Дробное 1 знак после запятой
            case 3:break;
            // Дробное 2 знака после запятой
            case 4:break;
    * */
    private static SaleDocumentCalculator instance = new SaleDocumentCalculator();
    private static SaleDocument document;
    private HashMap<Integer, ProductionItem> map = null;

    private SaleDocumentCalculator() {
        //
    }

    /**
     * Инициализация калькулятора для выполнения основных расчетов
     * которые затрагивают спецификацию и целостность документа
     *
     * @return экземпляр калькулятора
     */
    public static SaleDocumentCalculator getInstance(SaleDocument saleDocument) {
        document = saleDocument;
        return instance;
    }

    /**
     * Инициализация калькулятора для выполнения некоторых расчетов
     * не затрагивающих спецификацию и целостность документа
     *
     * @return экземпляр калькулятора
     */
    public static SaleDocumentCalculator getInstance() {
        document = null;
        return instance;
    }

    /**
     * Хак в связи с деноминацией корректируем согласно параметра скейла для валют RUB и UAH
     *
     * @param currencyId   код валюты
     * @param currencyRate курс валюты
     * @return курс, с учетом скейла
     */
    public static float getValueCurrencyWithScale(int currencyId, float currencyRate) {
        float result = currencyRate;
        boolean oldRate = (currencyRate > 10.0);
        switch (currencyId) {
            case 2:
                if (!oldRate) {
                    result = new BigDecimal(currencyRate / 100).setScale(6, RoundingMode.HALF_UP).floatValue();
                }
                break;
            // Гривна
            case 5:
                if (!oldRate) {
                    result = new BigDecimal(currencyRate / 100).setScale(6, RoundingMode.HALF_UP).floatValue();
                }
                break;

            default:
                break;
        }


        return result;
    }

    /**
     * Метод возвращает сумму по изделию согласно цены и количества
     *
     * @param priceValue  цена изделия
     * @param amountValue количество изделий
     * @return сумма по изделию
     */
    public static double calculateSumValue(double priceValue, int amountValue) {
        return priceValue * amountValue;
    }

    /**
     * Метод возвращает сумму по изделию согласно цены и количества
     *
     * @param priceValue  цена изделия
     * @param amountValue количество изделий
     * @return сумма по изделию
     */
    public static double calculateSumValue(double priceValue, double amountValue) {
        return priceValue * amountValue;
    }

    /**
     * Метод возвращает сумму НДС по изделию,
     * если НДС на сумму изделия не начислена
     *
     * @param sumCostValue сумма по изделию
     * @param vatValue     ставка НДС для изделия
     * @return сумма НДС для изделия
     */
    public static double calculateVatSumValue(double sumCostValue, double vatValue) {
        return sumCostValue * vatValue / 100;
    }

    /**
     * Метод возвращает итоговую сумму по изделию с НДС
     *
     * @param sumCostValue сумма по изделию
     * @param sumVatValue  сумма НДС по изделию
     * @return итого по изделию
     */
    public static double calculateTotalValue(double sumCostValue, double sumVatValue) {
        return sumCostValue + sumVatValue;
    }

    /**
     * Метод возвращает цену изделия с торговой надбавкой
     *
     * @param priceValue  цена изделия
     * @param tradeMarkup процент надбавки
     * @return цена изделия с надбавкой
     */
    public static RetailValue getPriceAndTradeMarkupValueNew(float priceValue, float tradeMarkup, float vatValue, double amount) {
        RetailValue retail = new RetailValue();
        // Пункт 2 размер т/н для единицы изделия
        retail.setValueAllowance(round(priceValue * tradeMarkup / 100, ROUND_XXX_XXXX));
        // Пункт 3 сумма т/н
        retail.setValueSumAllowance(round(retail.getValueAllowance() * amount, ROUND_XXX_XXXX));
        // Пункт 3.5 цена единицы изделия с т/н
        retail.setValueCostAndAllowance(round(retail.getValueAllowance() + priceValue, ROUND_XXX_XXXX));
        // Пункт 4 НДС от цены единицы изделия с т/н
        retail.setValueVatRetail(round(retail.getValueCostAndAllowance() * vatValue / 100, ROUND_XXX_XXXX));
        // Пункт 5 Розничная цена единицы изделия с НДС
        retail.setValueCostRetail(round(retail.getValueAllowance() + retail.getValueVatRetail() + priceValue, ROUND_DOWN_XXX_XX));
        // Пункт 6 Розничная сумма НДС
        retail.setValueSumVatRetail(retail.getValueVatRetail() * amount);
        // Пункт 7 Розничная сумма с НДС
        retail.setValueSumCostRetail(round(retail.getValueCostRetail() * amount, ROUND_XXX_XX));
        return retail;
    }

    public static double getPriceAndTradeMarkupValue_(double priceValue, float tradeMarkup) {
        return priceValue + (priceValue * tradeMarkup / 100);
    }

    /**
     * Метод возвращает цену изделия со скидкой
     *
     * @param priceValue    цена изделия без скидки
     * @param discountValue процент скидки
     * @return цена изделия со скидкой
     */
    public static double calculatePriceAndDiscountValue(double priceValue, float discountValue) {
        return priceValue - (priceValue * (discountValue) / 100);
    }

    public static double calculatePriceAndDiscountValue(double priceValue, float discountValue, float calculationFactor) {
        //System.out.println("Коэффициент : " + calculationFactor + " Скидка : " + discountValue);
        return ((priceValue - (priceValue * (discountValue) / 100)) * calculationFactor);
    }

    /**
     * Метод возвращает цену изделия с учетом коэффициента
     *
     * @param priceValue           цена изделия учетная
     * @param calculateFactorValue коэффициент расчета
     * @return цена изделия со скидкой
     */
    public static double calculatePriceAndCalculateFactorValue(double priceValue, float calculateFactorValue) {
        return priceValue * calculateFactorValue;
    }

    /**
     * Возвращает цену изделия в беларусских рублях после пересчета с валюты
     *
     * @param priceCurrencyValue цена изделия в валюте
     * @param currencyRate       курс валюты
     * @return цена изделия после пересчета с валюты
     */
    public static double calculatePriceFromCurrencyPriceValue(double priceCurrencyValue, float currencyRate) {
        return priceCurrencyValue * currencyRate;
    }

    /**
     * Возвращает цену изделия в валюте
     *
     * @param priceValue   цена изделия
     * @param currencyRate курс валюты
     * @return цена изделия в валюте
     */
    public static double calculateCurrencyPriceFromPriceValue(double priceValue, float currencyRate) {
        return priceValue / currencyRate;
    }

    /**
     * Округляет значение до определенного типом состояния
     *
     * @param value     значение для округления
     * @param roundType тип округления
     * @return округленное значение
     */
    public static double round(double value, int roundType) {

        switch (roundType) {
            // Без округления
            case 0:
                break;
            // Округлять до целого
            case 1:
                return roundAndGetDouble(value, 0);
            // Округление 100 до запятой
            case 2:
                return roundLongByRoundingRule(roundAndGetDouble(value, -2), roundingRule_100);
            // Дробное 1 знак после запятой
            case 3:
                return roundAndGetDouble(value, 1);
            // Дробное 2 знака после запятой
            case 4:
                return roundAndGetDouble(value, 2);
            case 5:
                return roundAndGetDouble(value, 4);
            case 6:
                return roundAndGetDouble(value, 3);
            case 7:
                return roundAndGetDoubleDOWN(value, 2);

        }
        return 0;
    }

    /**
     * Округление целого числа типа Long по правилу
     * ПРоверено на округлении до 50 и 100
     *
     * @param value значение для округления
     * @param rule  правило округления
     * @return округленное значение
     */
    public static double roundLongByRoundingRule(double value, RoundingRule rule) {
        int rest = (int) value % 100;
        long x = (long) value;
        x /= 100;
        x *= 100;
        if (rule.lowerLimit == rule.upperLimit) {
            if (rest >= rule.lowerLimit) {
                x += rule.roundValue;
            }
        } else {
            if (rest >= rule.upperLimit) {
                x += (rule.roundValue * 2);
            } else if (rest >= rule.lowerLimit && rest < rule.upperLimit) {
                x += rule.roundValue;
            }
        }

        return x;
    }

    public static double roundAndGetDouble(double value, int roundIndex) {
        return roundBigDecimal(value, roundIndex).doubleValue();
    }

    public static double roundAndGetDoubleDOWN(double value, int roundIndex) {
        return roundBigDecimalDOWN(value, roundIndex).doubleValue();
    }

    public static String roundAndGetString(double value, int roundIndex) {
        //return String.format("%s",roundBigDecimal(value, roundIndex).doubleValue());
        return roundBigDecimal(value, roundIndex).toPlainString();
    }

    public static BigDecimal roundBigDecimal(double value, int roundIndex) {
        BigDecimal decimal = new BigDecimal(String.valueOf(value));
        // В цикле округляем с большего (думаю больше 5-ти знаков не имеет смысла вводить) до нужного знака
      /*  if (roundIndex > 5) {
            roundIndex = 5;
        }
        for (int index = 5; index >= roundIndex; index--) {
            decimal = decimal.setScale(index, RoundingMode.HALF_UP);
        }
        */
        return decimal.setScale(10, BigDecimal.ROUND_HALF_UP).setScale(roundIndex, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal roundBigDecimalDOWN(double value, int roundIndex) {
        BigDecimal decimal = new BigDecimal(String.valueOf(value));
        // В цикле округляем с большего (думаю больше 5-ти знаков не имеет смысла вводить) до нужного знака
      /*  if (roundIndex > 5) {
            roundIndex = 5;
        }
        for (int index = 5; index >= roundIndex; index--) {
            decimal = decimal.setScale(index, RoundingMode.HALF_UP);
        }
        */
        return decimal.setScale(10, BigDecimal.ROUND_HALF_DOWN).setScale(roundIndex, BigDecimal.ROUND_HALF_DOWN);
    }

    /**
     * Метод расчета документа, полная обработка(расчет) документа
     *
     * @param productionItemMap мапа продукции по документу для получения артикула и модели (спец. цена)
     * @return документ
     */
    public SaleDocument calculate(final HashMap<Integer, ProductionItem> productionItemMap) {

        // Если калькулятор проинициализирован нулевым документом
        if (document == null) {
            System.out.println("Документ пустой, выходим");
            return null;
        }

        map = productionItemMap;

        // Если документ уже закрыт ничего не расчитываем а сразу возвращаем
/*        if (document.getDocumentStatus() == SaleDocumentStatus.CLOSED) {
            System.out.println("Документ закрыт, выходим");
            return document;
        }*/
        String documentType = document.getDocumentType();

        // Если документ экспортный(за пределы РБ)
        if (document.getDocumentExport() == 1) {
            switch (documentType) {
                case DOCUMENT_SALE_BUYER:       //  Отгрузка покупателю
                    System.out.println("Экспортная отгрузка покупателю...");
                    // Расчет документа по позициям
                    calculateSaleBuyerExport();
                    // Расчет итоговых сумм по документу
                    calculateSaleDocumentTotalCurrency();
                    break;
                case DOCUMENT_REFUND_BUYER:     // Возврат от покупателя
                    System.out.println("Экспортный возврат от покупателя...");
                    // Расчет документа по позициям
                    calculateSaleRefundExport();
                    // Расчет итоговых сумм по документу
                    calculateSaleDocumentTotalCurrency();
                    break;
                case DOCUMENT_SALE_MATERIAL:    //  Отгрузка материала
                    System.out.println("Экспортная отгрузка материала...");
                    // Расчет документа по позициям
                    calculateSaleBuyerExportMaterial();
                    // Расчет итоговых сумм по документу
                    calculateSaleDocumentTotalCurrency();
                    break;

                case DOCUMENT_REFUND_MATERIAL: //   Возврат материала
                    System.out.println("Экспортный возврат материала...");
                    // Расчет документа по позициям
                    calculateSaleRefundExportMaterial();
                    // Расчет итоговых сумм по документу
                    calculateSaleDocumentTotalCurrency();
                    break;
            }
        } else {// Если документ в пределах РБ, то
            switch (documentType) {
                case DOCUMENT_SALE_BUYER: //Если документ о продаже в пределах РБ (основной тип документа)
                    System.out.println("Отгрузка покупателю...");
                    // Расчет документа по изделиям
                    calculateSaleBuyerCountry();
                    // Расчет итоговых сумм по документу
                    calculateSaleDocumentTotal();
                    break;
                case DOCUMENT_REFUND_BUYER: // Если документ возвратный от покупателя
                    System.out.println("Воврат от покупателя...");
                    // Расчет документа по изделиям
                    calculateSaleRefundCountry();
                    // Расчет итоговых сумм по документу
                    calculateSaleDocumentTotal();
                    break;
                case DOCUMENT_SALE_RETAIL: //Если документ о продаже в фирменный магазин
                    System.out.println("Перемещение в розницу...");
                    // Расчет документа по изделиям
                    calculateSaleRetailCountry();
                    // Расчет итоговых сумм по документу
                    calculateSaleDocumentTotal();
                    break;
                case DOCUMENT_REFUND_RETAIL: // Если документ возвратный от фирменного магазина
                    System.out.println("Возврат из розницы...");
                    // Расчет документа по изделиям
                    calculateSaleRefundCountry();
                    // Расчет итоговых сумм по документу
                    calculateSaleDocumentTotal();
                    break;
                case DOCUMENT_SALE_MATERIAL: // Отгрузка материала, по стране
                    System.out.println("Отгрузка материала...");
                    // Расчет документа по позициям
                    calculateSaleBuyerMaterial();
                    // Расчет итоговых сумм по документу
                    calculateSaleDocumentTotal();
                    break;
                case DOCUMENT_REFUND_MATERIAL:// Возврат материала, по стране
                    // Расчет документа по позициям
                    calculateSaleBuyerMaterial();
                    // Расчет итоговых сумм по документу
                    calculateSaleDocumentTotal();
                    break;
            }
        }
        // Расчет количества по документу
        calculateSaleDocumentItemsAmount();

        return document;
    }

    /**
     * Расчет документа при продаже товара в пределах страны контрагенту не филиалу фабрики
     *
     * @return документ после расчета
     */
    private SaleDocument calculateSaleBuyerMaterial() {

        System.out.println("Идет расчет документа "
                + document.getDocumentNumber().trim() + "(ID=" + document.getId() + ")...");
        // Получим список изделий в документе
        List<SaleDocumentItem> itemList = getSaleDocumentItems(document);
        System.out.println("В документе " + itemList.size() + " позиций ");
        // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
        boolean isVatNeeds = documentNeedsVat(document);
        // В цикле пробегаем все изделия накладной
        for (SaleDocumentItem item : itemList) {
            // Получаем цену изделия с учетом скидки по контрагенту
            item.setValuePrice(
                    calculatePriceAndDiscountValue(item.getValuePriceForAccountingAbs(), document.getDiscountValue()));

            item.setValuePrice(round(item.getValuePrice(), ROUND_XXX_XX));

            // Для полотна используется вес, и цена полотна за 1 кг
            double totalAmount = (double) item.getAmountInPack() / 100;

            // Далее считаем сумму по изделию (количество * цена)
            item.setValueSumCost(round(calculateSumValue(item.getValuePrice(), totalAmount), ROUND_XXX_XX));

            // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
            if (isVatNeeds) {
                if (document.getDocumentVATType() == VAT_CONSTANT) {
                    // По документу НДС имеет фиксированное значение - получаем из документа
                    item.setValueSumVat(round(calculateVatSumValue(item.getValueSumCost(), document.getDocumentVatValue()), ROUND_XXX_XX));
                    item.setValueVAT(document.getDocumentVatValue());
                } else if (document.getDocumentVATType() == VAT_REFERENCE) {
                    // По документу необходимо расчитать НДС, для этого берем процент ставки НДС от суммы по изделию
                    item.setValueSumVat(round(calculateVatSumValue(item.getValueSumCost(), item.getValueVAT()), ROUND_XXX_XX));
                }
            } else {
                // По документу НДС расчитывать не нужно, и поэтому в поле суммы НДС принимаем 0
                item.setValueSumVat(0);
                item.setValueVAT(0);
            }

            // Далее считаем итоговую сумму по изделию, сумма по изделию + сумма НДС
            item.setValueSumCostAndVat(round(calculateTotalValue(item.getValueSumCost(), item.getValueSumVat()), ROUND_XXX_XX));

            // Валютные поля принимаем за 0
            item.setValuePriceCurrency(0);
            item.setValueSumCostCurrency(0);
            item.setValueSumVatCurrency(0);
            item.setValueSumCostAndVatCurrency(0);
        }

        return document;
    }

    /**
     * Расчет документа при продаже товара заграничному контрагенту не филиалу фабрики
     *
     * @return документ после расчета
     */
    private SaleDocument calculateSaleBuyerExportMaterial() {
        System.out.println("Идет расчет документа " + document.getDocumentNumber().trim()
                + "(" + document.getId() + ")...");
        // Получим список изделий в документе
        List<SaleDocumentItem> itemList = getSaleDocumentItems(document);
        System.out.println("В документе " + itemList.size() + " позиций ");

        // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
        boolean isVatNeeds = documentNeedsVat(document);
        // Определяем курсы валюты для расчета
        System.out.println("Расчет НДС в документе :" + isVatNeeds);

        /**
         *Курс на начало месяца
         */


        int currencyId = document.getCurrencyId();

        float fixedCurrencyRate = getValueCurrencyWithScale(currencyId, document.getCurrencyRateFixed());
        System.out.println("Курс на начало месяца :" + fixedCurrencyRate + " (" + document.getCurrencyRateFixed() + ")");
        /**
         * Курс на момент окончательного расчета
         */
        float saleCurrencyRate = getValueCurrencyWithScale(currencyId, document.getCurrencyRateSale());
        System.out.println("Курс на момент отгрузки :" + saleCurrencyRate + " (" + document.getCurrencyRateSale() + ")");

        // В цикле пробегаем все изделия накладной
        for (SaleDocumentItem item : itemList) {
            // Получаем цену в валюте с учетом скидки по контрагенту,
            // для этого отпускную цену(с учетом скидки) делим на курс 1-го числа месяца

            /*
             **********************************************************************************************************
             * ВАЛЮТНАЯ ЧАСТЬ
             **********************************************************************************************************
             */

            //При отгрузке полотна скидки и коэффициенты не действительны, т.к. цена уже посчитана
            // и оператор вводит цену полотна с учетом всего, поэтому ValuePriceCurrency уже расчитано


            // Для полотна используется вес, и цена полотна за 1 кг
            double totalAmount = (double) item.getAmountInPack() / 100;
            //System.err.println(totalAmount);
            // Далее считаем сумму по изделию (количество * цена)

            item.setValueSumCostCurrency(round(calculateSumValue(item.getValuePriceCurrency(), totalAmount), ROUND_XXX_XX));

            System.out.println(item.getValuePriceCurrency() + " ------ " + totalAmount + " ------ " + item.getValueSumCostCurrency());
            // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
            if (isVatNeeds) {

                if (document.getDocumentVATType() == VAT_CONSTANT) {
                    // По документу НДС имеет фиксированное значение - получаем из документа
                    item.setValueSumVatCurrency(round(
                            calculateVatSumValue(item.getValueSumCostCurrency(), document.getDocumentVatValue()), ROUND_XXX_XX));
                    item.setValueVAT(document.getDocumentVatValue());
                } else if (document.getDocumentVATType() == VAT_REFERENCE) {
                    // По документу необходимо расчитать НДС, для этого берем процент ставки НДС от суммы по изделию
                    item.setValueSumVatCurrency(round(
                            calculateVatSumValue(item.getValueSumCostCurrency(), item.getValueVAT()), ROUND_XXX_XX));
                }

            } else {
                // По документу НДС расчитывать не нужно, и поэтому в поле суммы НДС принимаем 0
                item.setValueSumVatCurrency(0);
                item.setValueVAT(0);
            }

            // Далее считаем итоговую сумму по изделию, сумма по изделию + сумма НДС
            item.setValueSumCostAndVatCurrency(
                    calculateTotalValue(item.getValueSumCostCurrency(), item.getValueSumVatCurrency()));

            /*
             **********************************************************************************************************
             * РУБЛЕВАЯ ЧАСТЬ
             **********************************************************************************************************
             */
            // Для определения цены изделия в рублях используем курс соответствующий курсу валюты на момент отгрузки
            // И так как изначально скидка по контрагенту посчитана в валюте, то больше ее не считаем
            // Получаем цену изделия в рублях через цену в валюте
            item.setValuePrice((float) round(calculatePriceFromCurrencyPriceValue(item.getValuePriceCurrency(), saleCurrencyRate), ROUND_XXX_XX));
            // Далее считаем сумму по изделию (количество * цена)
            item.setValueSumCost(round(calculateSumValue(item.getValuePrice(), totalAmount), ROUND_XXX_XX));

            // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
            if (isVatNeeds) {
                if (document.getDocumentVATType() == VAT_CONSTANT) {
                    // По документу НДС имеет фиксированное значение - получаем из документа
                    item.setValueSumVat(round(calculateVatSumValue(item.getValueSumCost(), document.getDocumentVatValue()), ROUND_XXX_XX));
                    item.setValueVAT(document.getDocumentVatValue());
                } else if (document.getDocumentVATType() == VAT_REFERENCE) {
                    // По документу необходимо расчитать НДС, для этого берем процент ставки НДС от суммы по изделию
                    item.setValueSumVat(round(calculateVatSumValue(item.getValueSumCost(), item.getValueVAT()), ROUND_XXX_XX));
                }
            } else {
                // По документу НДС расчитывать не нужно, и поэтому в поле суммы НДС принимаем 0
                item.setValueSumVat(0.0);
                item.setValueVAT(0);
            }
            item.setValueSumVat(round(item.getValueSumVat(), ROUND_XXX_XX));

            // Далее считаем итоговую сумму по изделию, сумма по изделию + сумма НДС
            item.setValueSumCostAndVat(calculateTotalValue(item.getValueSumCost(), item.getValueSumVat()));
            // Торговая надбавка
            //item.setTradeMarkValue(0f);
        }
        return document;
    }

    /**
     * Расчет документа при продаже товара заграничному контрагенту не филиалу фабрики
     *
     * @return документ после расчета
     */
    private SaleDocument calculateSaleRefundExport() {
        System.out.println("Идет расчет документа " + document.getDocumentNumber().trim() + "(" + document.getId() + ")...");
        // Получим список изделий в документе
        List<SaleDocumentItem> itemList = getSaleDocumentItems(document);
        System.out.println("В документе " + itemList.size() + " позиций ");
        // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
        boolean isVatNeeds = documentNeedsVat(document);
        // Определяем курсы валюты для расчета
        System.out.println("Расчет НДС в документе :" + isVatNeeds);

        /**
         *Курс на начало месяца
         */

        int currencyId = document.getCurrencyId();

        float fixedCurrencyRate = getValueCurrencyWithScale(currencyId, document.getCurrencyRateFixed());
        System.out.println("Курс на начало месяца :" + fixedCurrencyRate + " (" + document.getCurrencyRateFixed() + ")");
        /**
         * Курс на момент окончательного расчета
         */
        float saleCurrencyRate = getValueCurrencyWithScale(currencyId, document.getCurrencyRateSale());
        System.out.println("Курс на момент отгрузки :" + saleCurrencyRate + " (" + document.getCurrencyRateSale() + ")");

        // Инициализация сервиса спец. цен
        SpecialPriceService specialPriceService = SpecialPriceService.getInstance();
        // Обновить сервис
        specialPriceService.updateService();
        // Есть ли специальные цены по контрагенту
        boolean isSpecialPrice = specialPriceService.isHaveSpecialPrice(document.getRecipientCode());

        System.out.println("Флаг наличия специальной цены по контрагенту [" + document.getRecipientCode() + "]: " + isSpecialPrice);

        // В цикле пробегаем все изделия накладной
        for (SaleDocumentItem item : itemList) {
            item.setValuePriceCurrency(-1);
            /*
             **********************************************************************************************************
             * ВАЛЮТНАЯ ЧАСТЬ ВОЗВРАТОВ
             **********************************************************************************************************
             */

            float discountValue = 0;

            // Такая пляска нужна так как новое поле, которое содержала бы скидку в таблицу otgruz2
            // добавить нельзя. поэтому для хранения размера скидки будем пользовать поле которое
            // пользуется только раз, при добавлении записи, а мы в нем будем хранить скидку
            if (document.getDiscountType() == 3) {
                try {
                    discountValue = Float.valueOf(item.getDiscount());
                } catch (Exception e) {
                    discountValue = document.getDiscountValue();
                }
            } else {
                item.setDiscount(String.valueOf((int) document.getDiscountValue()));
                discountValue = document.getDiscountValue();
            }

            // Если на возврате цена НЕ БЫЛА введена вручную
            if (item.getValuePriceForAccounting() >= 0) {
                // Если есть спеццена у контрагента
                ProductionItem product = getProductionItemByItemCode(item.getItem());

                double specialPriceValue = specialPriceService.getSpecialPriceByCriteria(document.getRecipientCode(),
                        product.getItemModel(), product.getArticleName(), item.getItemGrade(), item.getItemSize(), item.getItemGrowth());
                if (specialPriceValue > 0) {
                    System.out.println("Спеццена 1 на " + product.getArticleName() + " составляет " + specialPriceValue);
                }
                item.setValuePriceCurrency(specialPriceValue);

                double specialPriceValue_ = specialPriceService.getGeneralSpecialPriceByCriteria(product.getItemModel(),
                        product.getArticleName(), item.getItemGrade(), item.getItemSize(), item.getItemGrowth());
                if (specialPriceValue_ > 0) {
                    System.out.println("Спеццена 2 на " + product.getArticleName() + " составляет " + specialPriceValue_);
                    item.setValuePriceCurrency(specialPriceValue_);
                }

                if (product.getArticleName().equals("9С7050Д40")) {
                    item.setValuePriceCurrency(232.27f);
                }


                // Если специальная цена не определена или не установлена
                if (item.getValuePriceCurrency() < 0) {


                    item.setValuePriceCurrency(
                            // Получаем цену в валюте
                            calculateCurrencyPriceFromPriceValue(
                                    // Получаем цену в рублях с учетом скидки
                                    calculatePriceAndDiscountValue(
                                            // Учетная оптовая цена
                                            item.getValuePriceForAccountingAbs(),
                                            // Процент скидки по документу
                                            discountValue, document.getCalculationFactor()),
                                    //Курс на начало месяца
                                    fixedCurrencyRate));
                }
            } else {
                // Если производится сброс учетной цены через установку в 0 цены в валюте,
                // тогда инвертируем цену прейскуранта
                if (item.getValuePriceCurrency() == 0.0) {
                    item.invertAccountingPrice();

                    item.setValuePriceCurrency(
                            // Получаем цену в валюте
                            calculateCurrencyPriceFromPriceValue(
                                    // Получаем цену в рублях с учетом скидки
                                    calculatePriceAndDiscountValue(
                                            // Учетная оптовая цена
                                            item.getValuePriceForAccountingAbs(),
                                            // Процент скидки по документу
                                            discountValue),
                                    //Курс на начало месяца
                                    fixedCurrencyRate));
                }
            }
            item.setValuePriceCurrency(round(item.getValuePriceCurrency(), ROUND_XXX_XX));

            // Получаем количество позиций изделия
            int totalAmount = item.getAmount() * item.getAmountInPack();
            // Далее считаем сумму по изделию (количество * цена)
            item.setValueSumCostCurrency(round(calculateSumValue(item.getValuePriceCurrency(), totalAmount), ROUND_XXX_XX));

            // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
            if (isVatNeeds) {
                if (document.getDocumentVATType() == VAT_CONSTANT) {
                    // По документу НДС имеет фиксированное значение - получаем из документа
                    item.setValueSumVatCurrency(round(
                            calculateVatSumValue(item.getValueSumCostCurrency(), document.getDocumentVatValue()), ROUND_XXX_XX));
                    item.setValueVAT(document.getDocumentVatValue());
                } else if (document.getDocumentVATType() == VAT_REFERENCE) {
                    // По документу необходимо расчитать НДС, для этого берем процент ставки НДС от суммы по изделию
                    item.setValueSumVatCurrency(round(
                            calculateVatSumValue(item.getValueSumCostCurrency(), item.getValueVAT()), ROUND_XXX_XX));
                }
            } else {
                item.setValueVAT(0);
                // По документу НДС расчитывать не нужно, и поэтому в поле суммы НДС принимаем 0
                item.setValueSumVatCurrency(0);
            }

            // Далее считаем итоговую сумму по изделию, сумма по изделию + сумма НДС
            item.setValueSumCostAndVatCurrency(
                    calculateTotalValue(item.getValueSumCostCurrency(), item.getValueSumVatCurrency()));

            /*
             **********************************************************************************************************
             * РУБЛЕВАЯ ЧАСТЬ
             **********************************************************************************************************
             */
            // Для определения цены изделия в рублях используем курс соответствующий курсу валюты на момент отгрузки
            // И так как изначально скидка по контрагенту посчитана в валюте, то больше ее не считаем
            // Получаем цену изделия в рублях через цену в валюте
            item.setValuePrice(round(calculatePriceFromCurrencyPriceValue(item.getValuePriceCurrency(), saleCurrencyRate), ROUND_XXX_XX));
            // Далее считаем сумму по изделию (количество * цена)
            item.setValueSumCost(calculateSumValue(item.getValuePrice(), totalAmount));

            // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
            if (isVatNeeds) {
                if (document.getDocumentVATType() == VAT_CONSTANT) {
                    // По документу НДС имеет фиксированное значение - получаем из документа
                    item.setValueSumVat(calculateVatSumValue(item.getValueSumCost(), document.getDocumentVatValue()));
                } else if (document.getDocumentVATType() == VAT_REFERENCE) {
                    // По документу необходимо расчитать НДС, для этого берем процент ставки НДС от суммы по изделию
                    item.setValueSumVat(calculateVatSumValue(item.getValueSumCost(), item.getValueVAT()));
                }
            } else {
                // По документу НДС расчитывать не нужно, и поэтому в поле суммы НДС принимаем 0
                item.setValueSumVat(0);
            }
            // Округление суммы НДС
            item.setValueSumVat(round(item.getValueSumVat(), ROUND_XXX_XX));

            // Далее считаем итоговую сумму по изделию, сумма по изделию + сумма НДС
            item.setValueSumCostAndVat(round(calculateTotalValue(item.getValueSumCost(), item.getValueSumVat()), ROUND_XXX_XX));
            // Торговая надбавка
            //item.setTradeMarkValue(0f);
        }
        return document;
    }

    /**
     * Расчет документа при продаже товара заграничному контрагенту не филиалу фабрики
     *
     * @return документ после расчета
     */
    private SaleDocument calculateSaleRefundExportMaterial() {
        System.out.println("Идет расчет документа " + document.getDocumentNumber().trim() + "(" + document.getId() + ")...");
        // Получим список изделий в документе
        List<SaleDocumentItem> itemList = getSaleDocumentItems(document);
        System.out.println("В документе " + itemList.size() + " позиций ");
        // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
        boolean isVatNeeds = documentNeedsVat(document);
        // Определяем курсы валюты для расчета
        System.out.println("Расчет НДС в документе :" + isVatNeeds);

        /**
         *Курс на начало месяца
         */

        int currencyId = document.getCurrencyId();

        float fixedCurrencyRate = getValueCurrencyWithScale(currencyId, document.getCurrencyRateFixed());
        System.out.println("Курс на начало месяца :" + fixedCurrencyRate + " (" + document.getCurrencyRateFixed() + ")");
        /**
         * Курс на момент окончательного расчета
         */
        float saleCurrencyRate = getValueCurrencyWithScale(currencyId, document.getCurrencyRateSale());
        System.out.println("Курс на момент отгрузки :" + saleCurrencyRate + " (" + document.getCurrencyRateSale() + ")");

        // Инициализация сервиса спец. цен
        SpecialPriceService specialPriceService = SpecialPriceService.getInstance();
        // Обновить сервис
        specialPriceService.updateService();
        // Есть ли специальные цены по контрагенту
        boolean isSpecialPrice = specialPriceService.isHaveSpecialPrice(document.getRecipientCode());

        System.out.println("Флаг наличия специальной цены по контрагенту [" + document.getRecipientCode() + "]: " + isSpecialPrice);

        // В цикле пробегаем все изделия накладной
        for (SaleDocumentItem item : itemList) {

            /*
             **********************************************************************************************************
             * ВАЛЮТНАЯ ЧАСТЬ
             **********************************************************************************************************
             */

            //При отгрузке полотна скидки и коэффициенты не действительны, т.к. цена уже посчитана
            // и оператор вводит цену полотна с учетом всего, поэтому ValuePriceCurrency уже расчитано


            // Для полотна используется вес, и цена полотна за 1 кг
            double totalAmount = (double) item.getAmountInPack() / 100;
            //System.err.println(totalAmount);
            // Далее считаем сумму по изделию (количество * цена)
            item.setValueSumCostCurrency(round(calculateSumValue(item.getValuePriceCurrency(), totalAmount), ROUND_XXX_XX));

            // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
            if (isVatNeeds) {

                if (document.getDocumentVATType() == VAT_CONSTANT) {
                    // По документу НДС имеет фиксированное значение - получаем из документа
                    item.setValueSumVatCurrency(round(
                            calculateVatSumValue(item.getValueSumCostCurrency(), document.getDocumentVatValue()), ROUND_XXX_XX));
                    item.setValueVAT(document.getDocumentVatValue());
                } else if (document.getDocumentVATType() == VAT_REFERENCE) {
                    // По документу необходимо расчитать НДС, для этого берем процент ставки НДС от суммы по изделию
                    item.setValueSumVatCurrency(round(
                            calculateVatSumValue(item.getValueSumCostCurrency(), item.getValueVAT()), ROUND_XXX_XX));
                }

            } else {
                // По документу НДС расчитывать не нужно, и поэтому в поле суммы НДС принимаем 0
                item.setValueSumVatCurrency(0);
                item.setValueVAT(0);
            }

            // Далее считаем итоговую сумму по изделию, сумма по изделию + сумма НДС
            item.setValueSumCostAndVatCurrency(
                    calculateTotalValue(item.getValueSumCostCurrency(), item.getValueSumVatCurrency()));

            /*
             **********************************************************************************************************
             * РУБЛЕВАЯ ЧАСТЬ
             **********************************************************************************************************
             */
            // Для определения цены изделия в рублях используем курс соответствующий курсу валюты на момент отгрузки
            // И так как изначально скидка по контрагенту посчитана в валюте, то больше ее не считаем
            // Получаем цену изделия в рублях через цену в валюте
            item.setValuePrice(round(calculatePriceFromCurrencyPriceValue(item.getValuePriceCurrency(), saleCurrencyRate), ROUND_XXX_XX));
            // Далее считаем сумму по изделию (количество * цена)
            item.setValueSumCost(round(calculateSumValue(item.getValuePrice(), totalAmount), ROUND_XXX_XX));

            // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
            if (isVatNeeds) {
                if (document.getDocumentVATType() == VAT_CONSTANT) {
                    // По документу НДС имеет фиксированное значение - получаем из документа
                    item.setValueSumVat(calculateVatSumValue(item.getValueSumCost(), document.getDocumentVatValue()));
                } else if (document.getDocumentVATType() == VAT_REFERENCE) {
                    // По документу необходимо расчитать НДС, для этого берем процент ставки НДС от суммы по изделию
                    item.setValueSumVat(calculateVatSumValue(item.getValueSumCost(), item.getValueVAT()));
                }
            } else {
                // По документу НДС расчитывать не нужно, и поэтому в поле суммы НДС принимаем 0
                item.setValueSumVat(0);
            }
            // Округление суммы НДС
            item.setValueSumVat(round(item.getValueSumVat(), ROUND_XXX_XX));

            // Далее считаем итоговую сумму по изделию, сумма по изделию + сумма НДС
            item.setValueSumCostAndVat(round(calculateTotalValue(item.getValueSumCost(), item.getValueSumVat()), ROUND_XXX_XX));
            // Торговая надбавка
            //item.setTradeMarkValue(0f);
        }
        return document;
    }

    /**
     * Расчет документа при продаже товара заграничному контрагенту не филиалу фабрики
     *
     * @return документ после расчета
     */
    private SaleDocument calculateSaleBuyerExport() {
        System.out.println("Идет расчет документа " + document.getDocumentNumber().trim()
                + "(" + document.getId() + ")...");
        // Получим список изделий в документе
        List<SaleDocumentItem> itemList = getSaleDocumentItems(document);
        System.out.println("В документе " + itemList.size() + " позиций ");
        // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
        boolean isVatNeeds = documentNeedsVat(document);
        // Определяем курсы валюты для расчета
        System.out.println("Расчет НДС в документе :" + isVatNeeds);
        CenaPDB cb = new CenaPDB();

        /**
         *Курс на начало месяца
         */
        int currencyId = document.getCurrencyId();

        float fixedCurrencyRate = getValueCurrencyWithScale(currencyId, document.getCurrencyRateFixed());
        System.out.println("Курс на начало месяца :" + fixedCurrencyRate + " (" + document.getCurrencyRateFixed() + ")");
        /**
         * Курс на момент окончательного расчета
         */
        float saleCurrencyRate = getValueCurrencyWithScale(currencyId, document.getCurrencyRateSale());
        System.out.println("Курс на момент отгрузки :" + saleCurrencyRate + " (" + document.getCurrencyRateSale() + ")");

        // Инициализация сервиса спец. цен
        SpecialPriceService specialPriceService = SpecialPriceService.getInstance();
        // Обновить сервис
        specialPriceService.updateService();
        // Есть ли специальные цены по контрагенту
        boolean isSpecialPrice = specialPriceService.isHaveSpecialPrice(document.getRecipientCode());

        System.out.println("Флаг наличия специальной цены по контрагенту [" + document.getRecipientCode() + "]: " + isSpecialPrice);

        // Блок инициализации российских цен
        boolean isNotVarietal = document.isNotVarietal();
        Reduction3GradeService gradeService = null;

        if (isNotVarietal) {
            gradeService = new Reduction3GradeService(CurrencyType.RUB);
        }

        // В цикле пробегаем все изделия накладной
        for (SaleDocumentItem item : itemList) {
            // Получаем цену в валюте с учетом скидки по контрагенту,
            // для этого отпускную цену(с учетом скидки) делим на курс 1-го числа месяца

            item.setValuePriceCurrency(-1);

            /*
             **********************************************************************************************************
             * ВАЛЮТНАЯ ЧАСТЬ
             * Сразу считем цену в валюте, курс при этом тот, который установлен на НАЧАЛО месяца/квартала
             * Потом считаем цену в белках, берем получившуюся на предыдущем шаге валютную цену и применяем курс на ДЕНЬ ОТГРУЗКИ
             **********************************************************************************************************
             */

            float discountValue = 0;

            // Такая пляска нужна так как новое поле, которое содержала бы скидку в таблицу otgruz2
            // добавить нельзя. поэтому для хранения размера скидки будем пользовать поле которое
            // пользуется только раз, при добавлении записи, а мы в нем будем хранить скидку
            if (document.getDiscountType() == 3) {
                try {
                    discountValue = Float.valueOf(item.getDiscount());
                } catch (Exception e) {
                    discountValue = document.getDiscountValue();
                }
            } else {
                item.setDiscount(String.valueOf((int) document.getDiscountValue()));
                discountValue = document.getDiscountValue();
            }

            if (!isNotVarietal) {
                // Если есть спеццена у контрагента
                ProductionItem product = getProductionItemByItemCode(item.getItem());
                if (isSpecialPrice) {
                    double specialPriceValue = specialPriceService.getSpecialPriceByCriteria(document.getRecipientCode(),
                            product.getItemModel(), product.getArticleName(), item.getItemGrade(), item.getItemSize(), item.getItemGrowth());
                    item.setValuePriceCurrency(specialPriceValue);
                    System.out.println("CЦ 1: " + specialPriceValue);
                }

                double specialPriceValue_ = specialPriceService.getGeneralSpecialPriceByCriteria(product.getItemModel(),
                        product.getArticleName(), item.getItemGrade(), item.getItemSize(), item.getItemGrowth());
                if (specialPriceValue_ > 0) {
                    item.setValuePriceCurrency(specialPriceValue_);
                    System.out.println("CЦ 2: " + specialPriceValue_);
                }

//                 Если специальная цена не пределена или не установлена
                if (item.getValuePriceCurrency() < 0) {
                    try {
                        while (((
                                calculatePriceFromCurrencyPriceValue(

                                        calculateCurrencyPriceFromPriceValue(
                                                // Получаем цену в рублях с учетом скидки
                                                calculatePriceAndDiscountValue(
                                                        // Учетная оптовая цена
                                                        item.getValuePriceForAccountingAbs(),
                                                        // Процент скидки по документу
                                                        discountValue, document.getCalculationFactor()
                                                ),
                                                //Курс на начало месяца
                                                fixedCurrencyRate),
                                        saleCurrencyRate) - cb.getSstoimost(Long.valueOf(product.getArticleCode()), Long.valueOf(item.getItemSize()))

                        ) / 100) < 0.00001 && (discountValue > 0) && document.getDiscountType() == 1) {
                            discountValue--;
                        }
                        item.setValuePriceCurrency(
                                // Получаем цену в валюте
                                calculateCurrencyPriceFromPriceValue(
                                        // Получаем цену в рублях с учетом скидки
                                        calculatePriceAndDiscountValue(
                                                // Учетная оптовая цена
                                                item.getValuePriceForAccountingAbs(),
                                                // Процент скидки по документу
                                                discountValue, document.getCalculationFactor()),
                                        //Курс на начало месяца
                                        fixedCurrencyRate));
                        item.setDiscount(String.valueOf(discountValue));
                    } catch (Exception ex) {
                        System.out.println("Ошибаемся");
                    }
                }
            } else {
                // Тут вызывается сервис на получение цены в валюте из классификатора по конкретному изделию
                // Потому что цена посчитана заранее

                // Получение цены 1-го сорта через сервис, и уменшение отпускной цены за счет коэффициента
                if (document != null) {
                    if (document.getPriceReduction3Grade() > 1) {
                        // Получаем цену в валюте для изделий несорта,
                        // заранее посчитанную и указанную в классификаторе

                        Reduction3Grade gradeItem = gradeService.getTopGradeByItemId(document.getId(), item.getId());

                        if (gradeItem != null) {
                            item.setValuePriceCurrency(gradeItem.getPriceCurrency());
                            item.setValuePriceForAccounting(calculatePriceAndDiscountValue(gradeItem.getPrice(), document.getPriceReduction3Grade()));
                            item.setValuePriceForAccounting(round(item.getValuePriceForAccounting(), ROUND_XXX_XX));
                        }
                    }
                }
            }

            item.setValuePriceCurrency(round(item.getValuePriceCurrency(), ROUND_XXX_XX));

            // Получаем количество позиций изделия
            int totalAmount = item.getAmount() * item.getAmountInPack();
            // Далее считаем сумму по изделию (количество * цена)
            item.setValueSumCostCurrency(round(calculateSumValue(item.getValuePriceCurrency(), totalAmount), ROUND_XXX_XX));

            // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
            if (isVatNeeds) {
                if (document.getDocumentVATType() == VAT_CONSTANT) {
                    // По документу НДС имеет фиксированное значение - получаем из документа
                    item.setValueSumVatCurrency(round(
                            calculateVatSumValue(item.getValueSumCostCurrency(), document.getDocumentVatValue()), ROUND_XXX_XX));
                    item.setValueVAT(document.getDocumentVatValue());
                } else if (document.getDocumentVATType() == VAT_REFERENCE) {
                    // По документу необходимо расчитать НДС, для этого берем процент ставки НДС от суммы по изделию
                    item.setValueSumVatCurrency(round(
                            calculateVatSumValue(item.getValueSumCostCurrency(), item.getValueVAT()), ROUND_XXX_XX));
                }

            } else {
                // По документу НДС расчитывать не нужно, и поэтому в поле суммы НДС принимаем 0
                item.setValueSumVatCurrency(0);
                item.setValueVAT(0);
            }

            // Далее считаем итоговую сумму по изделию, сумма по изделию + сумма НДС
            item.setValueSumCostAndVatCurrency(
                    calculateTotalValue(item.getValueSumCostCurrency(), item.getValueSumVatCurrency()));

            /*
             **********************************************************************************************************
             * РУБЛЕВАЯ ЧАСТЬ
             **********************************************************************************************************
             */
            // Для определения цены изделия в рублях используем курс соответствующий курсу валюты на момент отгрузки
            // И так как изначально скидка по контрагенту посчитана в валюте, то больше ее не считаем
            // Получаем цену изделия в рублях через цену в валюте
            item.setValuePrice(round(calculatePriceFromCurrencyPriceValue(item.getValuePriceCurrency(), saleCurrencyRate), ROUND_XXX_XX));
            // Далее считаем сумму по изделию (количество * цена)
            item.setValueSumCost(calculateSumValue(item.getValuePrice(), totalAmount));

            // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
            if (isVatNeeds) {
                if (document.getDocumentVATType() == VAT_CONSTANT) {
                    // По документу НДС имеет фиксированное значение - получаем из документа
                    item.setValueSumVat(calculateVatSumValue(item.getValueSumCost(), document.getDocumentVatValue()));
                } else if (document.getDocumentVATType() == VAT_REFERENCE) {
                    // По документу необходимо расчитать НДС, для этого берем процент ставки НДС от суммы по изделию
                    item.setValueSumVat(calculateVatSumValue(item.getValueSumCost(), item.getValueVAT()));
                }
            } else {
                // По документу НДС расчитывать не нужно, и поэтому в поле суммы НДС принимаем 0
                item.setValueSumVat(0);
                item.setValueVAT(0);
            }
            // Округление суммы НДС
            item.setValueSumVat(round(item.getValueSumVat(), ROUND_XXX_XX));

            // Далее считаем итоговую сумму по изделию, сумма по изделию + сумма НДС
            item.setValueSumCostAndVat(round(calculateTotalValue(item.getValueSumCost(), item.getValueSumVat()), ROUND_XXX_XX));
            // Торговая надбавка
            //item.setTradeMarkValue(0f);
        }
        return document;
    }

    /**
     * Расчет документа при продаже товара в пределах страны в розницу(Фирменный магазин)
     *
     * @return документ после расчета
     */
    private SaleDocument calculateSaleRetailCountry() {

        System.out.println("Идет расчет документа "
                + document.getDocumentNumber().trim() + "(ID=" + document.getId() + ")...");
        // Получим список изделий в документе
        List<SaleDocumentItem> itemList = getSaleDocumentItems(document);
        System.out.println("В документе " + itemList.size() + " позиций ");
        // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
        boolean isVatNeeds = documentNeedsVat(document);


        boolean isNotVarietal = document.isNotVarietal();
        Reduction3GradeService gradeService = null;

        if (isNotVarietal) {
            gradeService = new Reduction3GradeService();
        }

        RemainsReductionService remainsService = RemainsReductionService.getInstance();

        // В цикле пробегаем все изделия накладной
        for (SaleDocumentItem item : itemList) {

            // Получение цены 1-го сорта через сервис, и уменшение отпускной цены за счет коэффициента
            if (isNotVarietal) {
                if (document != null) {
                    if (document.getPriceReduction3Grade() > 1) {

                        Reduction3Grade gradeItem = gradeService.getTopGradeByItemId(document.getId(), item.getId());

                        if (gradeItem != null) {
                            item.setValuePriceForAccounting(calculatePriceAndDiscountValue(gradeItem.getPrice(), document.getPriceReduction3Grade()));
                            item.setValuePriceForAccounting(round(item.getValuePriceForAccounting(), ROUND_XXX_XX));
                        }
                    }
                }
            }

            // Получаем цену изделия с учетом скидки по контрагенту
            item.setValuePrice(
                    calculatePriceAndDiscountValue(item.getValuePriceForAccountingAbs(), document.getDiscountValue()));

            // Округление цены
            item.setValuePrice(round(item.getValuePrice(), ROUND_XXX_XX));
            // Получаем количество позиций изделия
            int totalAmount = item.getAmount() * item.getAmountInPack();

            // Далее считаем сумму по изделию (количество * цена)
            item.setValueSumCost(round(calculateSumValue(item.getValuePrice(), totalAmount), ROUND_XXX_XX));

            // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
            if (isVatNeeds) {
                if (document.getDocumentVATType() == VAT_CONSTANT) {
                    // По документу НДС имеет фиксированное значение - получаем из документа
                    item.setValueSumVat(round(calculateVatSumValue(item.getValueSumCost(), document.getDocumentVatValue()), ROUND_XXX_XX));
                    item.setValueVAT(document.getDocumentVatValue());
                } else if (document.getDocumentVATType() == VAT_REFERENCE) {
                    // По документу необходимо расчитать НДС, для этого берем процент ставки НДС от суммы по изделию
                    item.setValueSumVat(round(calculateVatSumValue(item.getValueSumCost(), item.getValueVAT()), ROUND_XXX_XX));
                }
            } else {
                // По документу НДС расчитывать не нужно, и поэтому в поле суммы НДС принимаем 0
                item.setValueSumVat(0);
                item.setValueVAT(0);
            }

            // Далее считаем итоговую сумму по изделию, сумма по изделию + сумма НДС
            item.setValueSumCostAndVat(round(calculateTotalValue(item.getValueSumCost(), item.getValueSumVat()), ROUND_XXX_XX));

            //Если установлена торговая надбавка
            if (document.getTradeMarkType() == 1) {
                TradeMarkupItem tmItem = item.getTradeMarkupItem();

                double tradeMarkup = document.getValueTradeMarkup();
                //remainsService.getTradeMarkupByArticleAndSize(result.getArticleName(), item.getItemSize(), (int) document.getValueTradeMarkup());

                // Расчет цены с учетом торговой надбавки
                RetailValue tradeMarkupValue = getPriceAndTradeMarkupValueNew((float) item.getValueCost(), (float) tradeMarkup, document.getDocumentVatValue(), totalAmount);
                tradeMarkupValue.setValueTradeMarkup(tradeMarkup);

                // Если для изделия надбавка считалась ранее
                if (tmItem != null) {
                    //Обновляем значение надбавки
                    tmItem.setValueTradeMarkup((float) tradeMarkup);
                    tmItem.setMarkAndPriceValue((float) tradeMarkupValue.getValueCostRetail());
                } else {
                    // Создаем запись надбавки
                    tmItem = new TradeMarkupItem();
                    tmItem.setId(item.getId());
                    tmItem.setValueTradeMarkup((float) tradeMarkup);
                    tmItem.setMarkAndPriceValue((float) tradeMarkupValue.getValueCostRetail());
                    item.setTradeMarkupItem(tmItem);
                }
            } else if (document.getTradeMarkType() == 2) {
                TradeMarkupItem tmItem = item.getTradeMarkupItem();

                ProductionItem result = getProductionItemByItemCode(item.getItem());
                double tradeMarkup = remainsService.getTradeMarkupByArticleAndSize(result, item.getItemSize(), (int) document.getValueTradeMarkup());

                // Расчет цены с учетом торговой надбавки
                RetailValue tradeMarkupValue = getPriceAndTradeMarkupValueNew((float) item.getValueCost(), (float) tradeMarkup, document.getDocumentVatValue(), totalAmount);
                tradeMarkupValue.setValueTradeMarkup(tradeMarkup);

                // Если для изделия надбавка считалась ранее
                if (tmItem != null) {
                    //Обновляем значение надбавки
                    tmItem.setValueTradeMarkup((float) tradeMarkup);
                    tmItem.setMarkAndPriceValue((float) tradeMarkupValue.getValueCostRetail());
                } else {
                    // Создаем запись надбавки
                    tmItem = new TradeMarkupItem();
                    tmItem.setId(item.getId());
                    tmItem.setValueTradeMarkup((float) tradeMarkup);
                    tmItem.setMarkAndPriceValue((float) tradeMarkupValue.getValueCostRetail());
                    item.setTradeMarkupItem(tmItem);
                }
            } else {
                TradeMarkupItem tmItem = item.getTradeMarkupItem();
                // Если для изделия надбавка считалась ранее
                if (tmItem != null) {
                    //Обновляем значение надбавки
                    // tmItem.setMarkValue(0);
                    // tmItem.setMarkAndPriceValue(0);
                    item.setTradeMarkupItem(null);
                }
            }

            // Валютные поля принимаем за 0
            item.setValuePriceCurrency(0);
            item.setValueSumCostCurrency(0);
            item.setValueSumVatCurrency(0);
            item.setValueSumCostAndVatCurrency(0);

            // Торговая надбавка
            //item.setTradeMarkValue(0f);
        }

        return document;
    }

    private SaleDocument calculateSaleRefundCountry() {

        System.out.println("Идет расчет документа "
                + document.getDocumentNumber().trim() + "(ID=" + document.getId() + ")...");
        // Получим список изделий в документе
        List<SaleDocumentItem> itemList = getSaleDocumentItems(document);
        System.out.println("В документе " + itemList.size() + " позиций ");
        // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
        boolean isVatNeeds = documentNeedsVat(document);

        boolean isNotVarietal = document.isNotVarietal();
        Reduction3GradeService gradeService = null;

        if (isNotVarietal) {
            gradeService = new Reduction3GradeService();
        }

        RemainsReductionService remainsService = RemainsReductionService.getInstance();

        // В цикле пробегаем все изделия накладной
        for (SaleDocumentItem item : itemList) {

            // Получение цены 1-го сорта через сервис, и уменшение отпускной цены за счет коэффициента
            if (isNotVarietal) {
                if (document != null) {
                    if (document.getPriceReduction3Grade() > 1) {

                        Reduction3Grade gradeItem = gradeService.getTopGradeByItemId(document.getId(), item.getId());

                        if (gradeItem != null) {
                            item.setValuePriceForAccounting(calculatePriceAndDiscountValue(gradeItem.getPrice(), document.getPriceReduction3Grade()));
                            item.setValuePriceForAccounting(round(item.getValuePriceForAccounting(), ROUND_XXX_XX));
                        }
                    }
                }
            }

            // Если цена прейскуранта отрицательная - значит уже пересчитана, и формировать ее не нужно
            // Для возвратов расчет
            if (item.getValuePriceForAccounting() >= 0) {
                item.setValuePrice(
                        calculatePriceAndDiscountValue(item.getValuePriceForAccountingAbs(), document.getDiscountValue()));
            }

            item.setValuePrice(round(item.getValuePrice(), ROUND_XXX_XX));

            // Если есть торговая надбавка
            if (document.getTradeMarkType() != 0) {
                if (!document.getDocumentType().equals(InvoiceType.DOCUMENT_REFUND_RETAIL)) {
                    //System.out.println("Торговая надбавка");
                    item.setValuePrice(round(getPriceAndTradeMarkupValue_(item.getValuePrice(), document.getValueTradeMarkup()), ROUND_DOWN_XXX_XX));
                    //item.setValuePrice(round(getPriceAndTradeMarkupValue_(item.getValuePrice(), document.getValueTradeMarkup()), ROUND_XXX_XX));
                }
            }

            // Получаем количество позиций изделия
            int totalAmount = item.getAmount() * item.getAmountInPack();

            // Далее считаем сумму по изделию (количество * цена)
            item.setValueSumCost(round(calculateSumValue(item.getValuePrice(), totalAmount), ROUND_XXX_XX));

            // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
            if (isVatNeeds) {
                if (document.getDocumentVATType() == VAT_CONSTANT) {
                    // По документу НДС имеет фиксированное значение - получаем из документа
                    item.setValueSumVat(round(calculateVatSumValue(item.getValueSumCost(), document.getDocumentVatValue()), ROUND_XXX_XX));
                    item.setValueVAT(document.getDocumentVatValue());
                } else if (document.getDocumentVATType() == VAT_REFERENCE) {
                    // По документу необходимо расчитать НДС, для этого берем процент ставки НДС от суммы по изделию
                    item.setValueSumVat(round(calculateVatSumValue(item.getValueSumCost(), item.getValueVAT()), ROUND_XXX_XX));
                }
            } else {
                // По документу НДС расчитывать не нужно, и поэтому в поле суммы НДС принимаем 0
                item.setValueSumVat(0);
                item.setValueVAT(0);
            }

            // Далее считаем итоговую сумму по изделию, сумма по изделию + сумма НДС
            item.setValueSumCostAndVat(round(calculateTotalValue(item.getValueSumCost(), item.getValueSumVat()), ROUND_XXX_XX));

            //Если установлена торговая надбавка
            if (document.getTradeMarkType() == 1) {
                TradeMarkupItem tmItem = item.getTradeMarkupItem();

                // Расчет цены с учетом торговой надбавки
                RetailValue tradeMarkupValue = getPriceAndTradeMarkupValueNew((float) item.getValueCost(), document.getValueTradeMarkup(), document.getDocumentVatValue(), totalAmount);
                tradeMarkupValue.setValueTradeMarkup(document.getValueTradeMarkup());

                // Если для изделия надбавка считалась ранее
                if (tmItem != null) {
                    //Обновляем значение надбавки
                    tmItem.setValueTradeMarkup(document.getValueTradeMarkup());
                    tmItem.setMarkAndPriceValue((float) tradeMarkupValue.getValueCostRetail());
                } else {
                    // Создаем запись надбавки
                    tmItem = new TradeMarkupItem();
                    tmItem.setId(item.getId());
                    tmItem.setValueTradeMarkup(document.getValueTradeMarkup());
                    tmItem.setMarkAndPriceValue((float) tradeMarkupValue.getValueCostRetail());
                    item.setTradeMarkupItem(tmItem);
                }
                // Если торговая надбавка с учетом уценочных документов
            } else if (document.getTradeMarkType() == 2) {
/*                TradeMarkupItem tmItem = item.getTradeMarkupItem();


                ProductionItem result = getProductionItemByItemCode(item.getItem());
                double tradeMarkup = remainsService.getTradeMarkupByArticleAndSize(result.getArticleName(), item.getItemSize(), (int) document.getValueTradeMarkup());


                //double tradeMarkup = document.getValueTradeMarkup() ;
                //remainsService.getTradeMarkupByArticleAndSize(result.getArticleName(), item.getItemSize(), (int) document.getValueTradeMarkup());

                // Расчет цены с учетом торговой надбавки
                RetailValue tradeMarkupValue = getPriceAndTradeMarkupValueNew((float) item.getValueCost(), (float) tradeMarkup, document.getDocumentVatValue(), totalAmount);
                tradeMarkupValue.setValueTradeMarkup(tradeMarkup);*/

                TradeMarkupItem tmItem = item.getTradeMarkupItem();

                ProductionItem result = getProductionItemByItemCode(item.getItem());
                double tradeMarkup;
                if (tmItem != null) {
                    tradeMarkup = tmItem.getValueTradeMarkup();
                } else {
                    tradeMarkup = remainsService.getTradeMarkupByArticleAndSize(result, item.getItemSize(), (int) document.getValueTradeMarkup());
                }
                // Расчет цены с учетом торговой надбавки
                RetailValue tradeMarkupValue = getPriceAndTradeMarkupValueNew((float) item.getValueCost(), (float) tradeMarkup, document.getDocumentVatValue(), totalAmount);
                tradeMarkupValue.setValueTradeMarkup(tradeMarkup);


                // Если для изделия надбавка считалась ранее
                if (tmItem != null) {
                    //Обновляем значение надбавки
                    tmItem.setValueTradeMarkup((float) tradeMarkup);
                    tmItem.setMarkAndPriceValue((float) tradeMarkupValue.getValueCostRetail());
                } else {
                    // Создаем запись надбавки
                    tmItem = new TradeMarkupItem();
                    tmItem.setId(item.getId());
                    tmItem.setValueTradeMarkup((float) tradeMarkup);
                    tmItem.setMarkAndPriceValue((float) tradeMarkupValue.getValueCostRetail());
                    item.setTradeMarkupItem(tmItem);
                }
            } else if (document.getTradeMarkType() == 3) {

                TradeMarkupItem tmItem = item.getTradeMarkupItem();

                ProductionItem result = getProductionItemByItemCode(item.getItem());

                double tradeMarkup = (int) document.getValueTradeMarkup();
                try {
                    String pl = item.getItemPriceList().trim();
                    if (pl.contains("$")) {
                        String v = pl.substring(0, 2);
                        tradeMarkup = Integer.valueOf(v);
                    } else {
                        tradeMarkup = (int) document.getValueTradeMarkup();
                    }
                } catch (Exception te) {
                    System.out.println("Ошибка чтения торговой надбавки для " + item.getItemPriceList().trim());
                    tradeMarkup = (int) document.getValueTradeMarkup();
                }

                // Расчет цены с учетом торговой надбавки
                RetailValue tradeMarkupValue = getPriceAndTradeMarkupValueNew((float) item.getValueCost(), (float) tradeMarkup, document.getDocumentVatValue(), totalAmount);
                tradeMarkupValue.setValueTradeMarkup(tradeMarkup);


                // Если для изделия надбавка считалась ранее
                if (tmItem != null) {
                    //Обновляем значение надбавки
                    tmItem.setValueTradeMarkup((float) tradeMarkup);
                    tmItem.setMarkAndPriceValue((float) tradeMarkupValue.getValueCostRetail());
                } else {
                    // Создаем запись надбавки
                    tmItem = new TradeMarkupItem();
                    tmItem.setId(item.getId());
                    tmItem.setValueTradeMarkup((float) tradeMarkup);
                    tmItem.setMarkAndPriceValue((float) tradeMarkupValue.getValueCostRetail());
                    item.setTradeMarkupItem(tmItem);
                }

            } else {
                TradeMarkupItem tmItem = item.getTradeMarkupItem();
                // Если для изделия надбавка считалась ранее
                if (tmItem != null) {
                    //Обновляем значение надбавки
                    // tmItem.setMarkValue(0);
                    // tmItem.setMarkAndPriceValue(0);
                    item.setTradeMarkupItem(null);
                }
            }

            // Валютные поля принимаем за 0
            item.setValuePriceCurrency(0);
            item.setValueSumCostCurrency(0);
            item.setValueSumVatCurrency(0);
            item.setValueSumCostAndVatCurrency(0);

            // Торговая надбавка
            //item.setTradeMarkValue(0f);
        }

        return document;
    }

    /**
     * Расчет документа при продаже товара в пределах страны контрагенту не филиалу фабрики
     *
     * @return документ после расчета
     */
    private SaleDocument calculateSaleBuyerCountry() {

        System.out.println("Идет расчет документа "
                + document.getDocumentNumber().trim() + "(ID=" + document.getId() + ")...");
        // Получим список изделий в документе
        List<SaleDocumentItem> itemList = getSaleDocumentItems(document);
        System.out.println("В документе " + itemList.size() + " позиций ");
        // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
        boolean isVatNeeds = documentNeedsVat(document);

        boolean isNotVarietal = document.isNotVarietal();
        Reduction3GradeService gradeService = null;

        if (isNotVarietal) {
            gradeService = new Reduction3GradeService();
        }
        /*-----------------------------------------------------------------------------------------------------------------------*/
        // эта дичь нужна для цен МаркФормель...

        // В цикле пробегаем все изделия накладной
        for (SaleDocumentItem item : itemList) {
            // Получение цены 1-го сорта через сервис, и уменшение отпускной цены за счет коэффициента
            if (isNotVarietal) {
                if (document != null) {
                    if (document.getPriceReduction3Grade() > 1) {

                        Reduction3Grade gradeItem = gradeService.getTopGradeByItemId(document.getId(), item.getId());

                        if (gradeItem != null) {
                            item.setValuePriceForAccounting(calculatePriceAndDiscountValue(gradeItem.getPrice(), document.getPriceReduction3Grade()));
                            item.setValuePriceForAccounting(round(item.getValuePriceForAccounting(), ROUND_XXX_XX));
                        }
                    }
                }
            }

            // document.getRecipientCode() != 4037 || document.getRecipientCode() != 2258

            // Получаем цену изделия с учетом скидки по контрагенту
            item.setValuePrice(
                    calculatePriceAndDiscountValue(item.getValuePriceForAccountingAbs(), document.getDiscountValue()));

            item.setValuePrice(round(item.getValuePrice(), ROUND_XXX_XX));

            if (document.getRecipientCode() != 4037 || document.getRecipientCode() != 2258) {

            }

            // Если есть торговая надбавка
            if (document.getTradeMarkType() != 0) {
                System.out.println("Торговая надбавка");
                item.setValuePrice(round(getPriceAndTradeMarkupValue_(item.getValuePrice(), document.getValueTradeMarkup()), ROUND_XXX_XX));
            }

            // Получаем количество позиций изделия
            int totalAmount = item.getAmount() * item.getAmountInPack();

            // Далее считаем сумму по изделию (количество * цена)
            item.setValueSumCost(round(calculateSumValue(item.getValuePrice(), totalAmount), ROUND_XXX_XX));

            // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
            if (isVatNeeds) {
                if (document.getDocumentVATType() == VAT_CONSTANT) {
                    // По документу НДС имеет фиксированное значение - получаем из документа
                    item.setValueSumVat(round(calculateVatSumValue(item.getValueSumCost(), document.getDocumentVatValue()), ROUND_XXX_XX));
                    item.setValueVAT(document.getDocumentVatValue());
                } else if (document.getDocumentVATType() == VAT_REFERENCE) {
                    // По документу необходимо расчитать НДС, для этого берем процент ставки НДС от суммы по изделию
                    item.setValueSumVat(round(calculateVatSumValue(item.getValueSumCost(), item.getValueVAT()), ROUND_XXX_XX));
                }
            } else {
                // По документу НДС расчитывать не нужно, и поэтому в поле суммы НДС принимаем 0
                item.setValueSumVat(0);
                item.setValueVAT(0);
            }

            // Далее считаем итоговую сумму по изделию, сумма по изделию + сумма НДС
            item.setValueSumCostAndVat(round(calculateTotalValue(item.getValueSumCost(), item.getValueSumVat()), ROUND_XXX_XX));

            // Валютные поля принимаем за 0
            item.setValuePriceCurrency(0);
            item.setValueSumCostCurrency(0);
            item.setValueSumVatCurrency(0);
            item.setValueSumCostAndVatCurrency(0);

            // Торговая надбавка
            //item.setTradeMarkValue(0f);
        }

        return document;
    }

    /**
     * Метод расчитывает итоговые значения сумм по экспортному документу
     *
     * @return документ после расчета
     */
    private SaleDocument calculateSaleDocumentTotalCurrency() {
        System.out.println("Расчет итогов в валюте для записи документа в базу...");
        // Получаем список изделий в документе
        List<SaleDocumentItem> itemList = getSaleDocumentItems(document);
        // Обнуляем значения итогов по документу
        document.setValueSumCost(0);
        document.setValueSumVat(0);
        document.setValueSumCostAndVat(0);

        for (SaleDocumentItem item : itemList) {
            // Суммируем стоимость в валюте
            document.setValueSumCost(document.getValueSumCost() + item.getValueSumCostCurrency());
            // Суммируем сумму НДС в валюте
            document.setValueSumVat(document.getValueSumVat() + item.getValueSumVatCurrency());
            // Суммируем итого (стоимость + сумма НДС) в валюте
            document.setValueSumCostAndVat(document.getValueSumCostAndVat() + item.getValueSumCostAndVatCurrency());
        }

        // Округление итоговых значений суммы
        document.setValueSumCost(round(document.getValueSumCost(), ROUND_XXX_XX));
        // Округление итоговых значений суммы НДС
        document.setValueSumVat(round(document.getValueSumVat(), ROUND_XXX_XX));
        // Округление итоговых значений Итого (сумма + НДС)
        document.setValueSumCostAndVat(round(document.getValueSumCostAndVat(), ROUND_XXX_XX));

        return document;
    }

    /**
     * Метод расчитывает итоговые значения сумм по накладной
     *
     * @return документ после расчета
     */
    private SaleDocument calculateSaleDocumentTotal() {
        System.out.println("Расчет итогов в рублях для записи документа в базу...");
        // Получаем список изделий в документе
        List<SaleDocumentItem> itemList = getSaleDocumentItems(document);

        // Обнуляем значения итогов по документу
        document.setValueSumCost(0);
        document.setValueSumVat(0);
        document.setValueSumCostAndVat(0);

        for (SaleDocumentItem item : itemList) {
            // Суммируем стоимость
            document.setValueSumCost(document.getValueSumCost() + item.getValueSumCost());
            // Суммируем сумму НДС
            document.setValueSumVat(document.getValueSumVat() + item.getValueSumVat());
            // Суммируем итого (стоимость + сумма НДС)
            document.setValueSumCostAndVat(document.getValueSumCostAndVat() + item.getValueSumCostAndVat());
        }

        // Округление итоговых значений суммы
        document.setValueSumCost(round(document.getValueSumCost(), ROUND_XXX_XX));
        // Округление итоговых значений суммы НДС
        document.setValueSumVat(round(document.getValueSumVat(), ROUND_XXX_XX));
        // Округление итоговых значений Итого (сумма + НДС)
        document.setValueSumCostAndVat(round(document.getValueSumCostAndVat(), ROUND_XXX_XX));

        return document;
    }
    /*    *//**
     * Округляет значение до определенного типом состояния
     *
     * @param value     значение для округления
     * @param roundType тип округления
     * @return округленное значение
     *//*
    public static double round(double value, int roundType) {

        switch (roundType) {
            // Без округления
            case 0:
                break;
            // Округлять до целого
            case 1:
                return roundAndGetDouble(value, 0);
            // Округление 100 до запятой
            case 2:
                return roundLongByRoundingRule(roundAndGetDouble(value, -2), roundingRule_100);
            // Дробное 1 знак после запятой
            case 3:
                return roundAndGetDouble(value, 1);
            // Дробное 2 знака после запятой
            case 4:
                return roundAndGetDouble(value, 2);
            case 5:
                return roundAndGetDouble(value, 4);

            case 6:
                return roundAndGetDouble(value, 3);
        }
        return 0;
    }*/

    /**
     * Подсчет итогов по документу как для валюты, так и для рублей
     *
     * @param list         список изделий
     * @param documentType тип документа (для весовых иной расчет количества)
     * @return объект итогов по документу
     */
    public TotalSummingUp summingUpCost(List<? extends ISummingUp> list, String documentType, double tradeMark) {
        TotalSummingUp total = new TotalSummingUp();
        boolean wrongCalculation = false;
        //System.out.println("Расчет итогов в валюте и рублях для документа...");
        // Получаем список изделий в документе
        //List<SaleDocumentItem> itemList = getSaleDocumentItems(document);

        // Обнуляем значения итогов по документу
        total.setValueSumCost(0.0);
        total.setValueSumVat(0.0);
        total.setValueSumCostAndVat(0.0);

        total.setValueSumCostCurrency(0.0);
        total.setValueSumVatCurrency(0.0);
        total.setValueSumCostAndVatCurrency(0.0);

        total.setValueSumAllowance(0.0);
        total.setValueSumVatRetail(0.0);
        total.setValueSumCostRetail(0.0);

        boolean isTradeAllowance = documentType.equals(InvoiceType.DOCUMENT_REFUND_RETAIL) || documentType.equals(InvoiceType.DOCUMENT_SALE_RETAIL);

        //RemainsReductionService remainsService = RemainsReductionService.getInstance();
        for (ISummingUp item : list) {

            double tradeMarkup = item.getTradeMarkup();//remainsService.getTradeMarkupByArticleAndSize(item.getArticleName(), item.getItemSize(), (int) tradeMark);
            // Суммируем стоимость
            total.setValueSumCost(round(total.getValueSumCost() + item.getValueSumCost(), ROUND_XXX_XX));
            // Суммируем сумму НДС
            total.setValueSumVat(round(total.getValueSumVat() + item.getValueSumVat(), ROUND_XXX_XX));
            // Суммируем итого (стоимость + сумма НДС)
            total.setValueSumCostAndVat(round(total.getValueSumCostAndVat() + item.getValueSumCostAndVat(), ROUND_XXX_XX));

            // Суммируем стоимость в валюте
            total.setValueSumCostCurrency(total.getValueSumCostCurrency() + item.getValueSumCostCurrency());
            // Суммируем сумму НДС в валюте
            total.setValueSumVatCurrency(total.getValueSumVatCurrency() + item.getValueSumVatCurrency());
            // Суммируем итого (стоимость + сумма НДС) в валюте
            total.setValueSumCostAndVatCurrency(round(total.getValueSumCostAndVatCurrency() + item.getValueSumCostAndVatCurrency(), ROUND_XXX_XX));

            // Расчет торговой надбавки, если необходимо
            if (isTradeAllowance) {
                RetailValue retail = item.getRetailValue();
                // Если есть объект торговой надбавки в изделии
                if (retail != null) {
                    retail.setValueTradeMarkup(tradeMarkup);
                    // ПОлучим ставку торговой надбавки
                    //double allowanceRate = tradeMark;
                    // Если размер торговой надбавки больше 0 расчитываем
                    if (tradeMarkup > 0) {
                       /* if (item.getModelNumberAsString().equals("14547")) {
                            //item.setValueAllowanc();
                            // Пункт 2 размер т/н для единицы изделия
                            retail.setValueAllowance(item.getValueCost() * tradeMarkup / 100);
                            // Пункт 3 сумма т/н
                            retail.setValueSumAllowance(round(retail.getValueAllowance(), ROUND_XXX_XX) * item.getTotalAmount());
                            // Пункт 3.5 цена единицы изделия с т/н
                            retail.setValueCostAndAllowance(retail.getValueAllowance() + item.getValueCost());
                            // Пункт 4 НДС от цены единицы изделия с т/н
                            retail.setValueVatRetail(decimalCliping(retail.getValueCostAndAllowance() * item.getValueVat() / 100));
                            System.out.println(retail.getValueVatRetail());
                            // Пункт 5 Розничная цена единицы изделия с НДС
                            retail.setValueCostRetail(round(retail.getValueAllowance() + retail.getValueVatRetail() + item.getValueCost(), ROUND_XXX_XX));
                            // Пункт 7 Розничная сумма с НДС
                            retail.setValueSumCostRetail(retail.getValueCostRetail() * item.getTotalAmount());
                            // Пункт 6 Розничная сумма НДС
                            retail.setValueSumVatRetail(retail.getValueVatRetail() * item.getTotalAmount());
                            //System.out.println(retail.getValueAllowance() +" - "+ retail.getValueVatRetail()+" - " + item.getValueCost() +" - " + item.getTotalAmount()+" - "+retail.getValueSumCostRetail());
                            wrongCalculation = true;
                        } else {*/
                        //item.setValueAllowanc();
                        // Пункт 2 размер т/н для единицы изделия
                        retail.setValueAllowance(round(item.getValueCost() * tradeMarkup / 100, ROUND_XXX_XX));
                        // Пункт 3 сумма т/н
                        retail.setValueSumAllowance(round(retail.getValueAllowance() * item.getTotalAmount(), ROUND_XXX_XX));
                        // Пункт 3.5 цена единицы изделия с т/н
                        retail.setValueCostAndAllowance(round(retail.getValueAllowance() + item.getValueCost(), ROUND_XXX_XX));
                        // Пункт 4 НДС от цены единицы изделия с т/н
                        retail.setValueVatRetail(round(retail.getValueCostAndAllowance() * item.getValueVat() / 100, ROUND_XXX_XX));
                        // Пункт 5 Розничная цена единицы изделия с НДС
                        retail.setValueCostRetail(round(retail.getValueAllowance() + retail.getValueVatRetail() + item.getValueCost(), ROUND_XXX_XX));
                        // Пункт 6 Розничная сумма НДС
                        retail.setValueSumVatRetail(retail.getValueVatRetail() * item.getTotalAmount());
                        // Пункт 7 Розничная сумма с НДС
                        retail.setValueSumCostRetail(round(retail.getValueCostRetail() * item.getTotalAmount(), ROUND_XXX_XX));
                        // }
                    } else if (tradeMarkup == 0) {// Не, ну а вдруг будет другая методика
                        retail.setValueAllowance(round(item.getValueCost() * tradeMarkup / 100, ROUND_XXX_XX));
                        // Пункт 3 сумма т/н
                        retail.setValueSumAllowance(round(retail.getValueAllowance() * item.getTotalAmount(), ROUND_XXX_XX));
                        // Пункт 3.5 цена единицы изделия с т/н
                        retail.setValueCostAndAllowance(round(retail.getValueAllowance() + item.getValueCost(), ROUND_XXX_XX));
                        // Пункт 4 НДС от цены единицы изделия с т/н
                        retail.setValueVatRetail(round(retail.getValueCostAndAllowance() * item.getValueVat() / 100, ROUND_XXX_XX));
                        // Пункт 5 Розничная цена единицы изделия с НДС
                        retail.setValueCostRetail(round(retail.getValueAllowance() + retail.getValueVatRetail() + item.getValueCost(), ROUND_XXX_XX));
                        // Пункт 6 Розничная сумма НДС
                        retail.setValueSumVatRetail(retail.getValueVatRetail() * item.getTotalAmount());
                        // Пункт 7 Розничная сумма с НДС
                        retail.setValueSumCostRetail(round(retail.getValueCostRetail() * item.getTotalAmount(), ROUND_XXX_XX));
                    } else {
                        retail.setValueAllowance(0);
                        // Пункт 3
                        retail.setValueSumAllowance(0);
                        // Пункт 3.5
                        retail.setValueCostAndAllowance(0);
                        // Пункт 4
                        retail.setValueVatRetail(0);
                        // Пункт 5
                        retail.setValueCostRetail(0);
                        // Пункт 6
                        retail.setValueSumVatRetail(0);
                        // Пункт 7
                        retail.setValueSumCostRetail(0);
                    }

                    if (wrongCalculation) {
                        // Итого по сумме торговых надбавок(3)
                        total.setValueSumAllowance(total.getValueSumAllowance() + retail.getValueSumAllowance());
                        // Пункт 3.1
                        total.setValueCostAndAllowance(total.getValueCostAndAllowance() + retail.getValueCostAndAllowance());
                        // Итого по сумме НДС розничной цены(6)
                        total.setValueSumVatRetail(total.getValueSumVatRetail() + retail.getValueSumVatRetail());
                        // Итого по сумме розничной цены(7)
                        total.setValueSumCostRetail(total.getValueSumCostRetail() + retail.getValueSumCostRetail());
                    } else {
                        // Итого по сумме торговых надбавок(3)
                        total.setValueSumAllowance(round(total.getValueSumAllowance() + retail.getValueSumAllowance(), ROUND_XXX_XX));
                        // Пункт 3.1
                        total.setValueCostAndAllowance(round(total.getValueCostAndAllowance() + retail.getValueCostAndAllowance(), ROUND_XXX_XX));
                        // Итого по сумме НДС розничной цены(6)
                        total.setValueSumVatRetail(round(total.getValueSumVatRetail() + retail.getValueSumVatRetail(), ROUND_XXX_XX));
                        // Итого по сумме розничной цены(7)
                        total.setValueSumCostRetail(round(total.getValueSumCostRetail() + retail.getValueSumCostRetail(), ROUND_XXX_XX));
                    }
                }
            }
            // Расчет стоимости по прейскуранту
            PriceListValue price = item.getPriceListValue();
            // Если расчет структуры отчета - считаем
            if (price != null) {
                //System.out.println(item.getAccountingPrice());
                //Стоимость в прейскуранте
                price.setPriceListSumCost(item.getAccountingPrice() * item.getTotalAmount());
                //Сумма ндс по цене прейскуранта
                price.setPriceListSumVat(round(price.getPriceListSumCost() * item.getValueVat() / 100, ROUND_XXX_XX));
                //Итого с НДС
                price.setPriceListSumCostAndVat(price.getPriceListSumCost() + price.getPriceListSumVat());

                total.setPriceListSumCost(total.getPriceListSumCost() + price.getPriceListSumCost());
                total.setPriceListSumVat(total.getPriceListSumVat() + price.getPriceListSumVat());
                total.setPriceListSumCostAndVat(total.getPriceListSumCostAndVat() + price.getPriceListSumCostAndVat());
            }
        }

        // Округление итоговых значений суммы
        total.setValueSumCost(round(total.getValueSumCost(), ROUND_XXX_XX));
        // Округление итоговых значений суммы НДС
        total.setValueSumVat(round(total.getValueSumVat(), ROUND_XXX_XX));
        // Округление итоговых значений Итого (сумма + НДС)
        total.setValueSumCostAndVat(round(total.getValueSumCostAndVat(), ROUND_XXX_XX));


        // Округление итоговых значений суммы
        total.setValueSumCostCurrency(round(total.getValueSumCostCurrency(), ROUND_XXX_XX));
        // Округление итоговых значений суммы НДС
        total.setValueSumVatCurrency(round(total.getValueSumVatCurrency(), ROUND_XXX_XX));
        // Округление итоговых значений Итого (сумма + НДС)
        total.setValueSumCostAndVatCurrency(round(total.getValueSumCostAndVatCurrency(), ROUND_XXX_XX));

        return total;
    }

    /**
     * Метод считает количества (всего, упаковок, россыпью) изделий по документу
     */
    public void calculateSaleDocumentItemsAmount() {
        if (document == null) {
            return;
        }

        List<SaleDocumentItem> itemList = document.getDetailList();
        int amountAll = 0;
        int amountPack = 0;
        int amountNotPack = 0;

        for (SaleDocumentItem item : itemList) {
            if (item.getItemType() == 2) {
                amountPack++;
                amountAll = amountAll + (item.getAmount() * item.getAmountInPack());
            } else if (item.getItemType() == 1) {
                amountNotPack = amountNotPack + item.getAmount() * item.getAmountInPack();
                amountAll = amountAll + (item.getAmount() * item.getAmountInPack());
            } else if (item.getItemType() == 3) {
                amountPack++;
                amountNotPack = 0;
                amountAll += item.getAmountInPack();
            }
            //amountAll = amountAll + (item.getAmount() * item.getAmountInPack());
        }

        document.setAmountAll(amountAll);
        document.setAmountNotPack(amountNotPack);
        document.setAmountPack(amountPack);
    }

    private double decimalCliping(double value) {
        return Double.valueOf(new DecimalFormat("#0.000").format(value).replace(",", ".").substring(0, 4));
    }

    /**
     * Метод проверяет, нужен ни расчет НДС по накладной, и включать ли его в сумму итого ?
     *
     * @param saleDocument документ
     * @return возвращает true, если НДС расчитывается
     */
    private boolean documentNeedsVat(SaleDocument saleDocument) {
        return saleDocument.getDocumentVATType() != VAT_NOT_NEED;
    }

    /**
     * Метод возвращает список изделий по документу
     *
     * @param saleDocument расчитываемый документ
     * @return список изделий
     */
    private List<SaleDocumentItem> getSaleDocumentItems(SaleDocument saleDocument) {
        return saleDocument.getDetailList();
    }

    /**
     * Метод возвращает цену изделия с торговой надбавкой
     *
     * @param priceValue  цена изделия
     * @param tradeMarkup процент надбавки
     * @return цена изделия с надбавкой
     */
    public double getPriceAndTradeMarkupValue(float priceValue, float tradeMarkup, float vatValue) {
        return round(priceValue + round(priceValue * tradeMarkup / 100, ROUND_XXX_XXXX) + round(priceValue * vatValue / 100, ROUND_XXX_XXXX), ROUND_XXX_XX);
    }

    /**
     * Подведение итогов по количественным и весовым параметрам
     * Так как материал отгружается не в единицах а в килограммах, данный этап необходим
     *
     * @param list       список изделий
     * @param isMaterial документ содержит материал, а он отгружается в килограммах
     * @param summingUp  объект итогов
     * @return объект итогов
     */
    public TotalSummingUp summingUpAmountAndWeight(final List<SaleDocumentDetailItemReport> list, final boolean isMaterial, final TotalSummingUp summingUp) {
        double amount = 0.0;
        double weight = 0.0;
        for (SaleDocumentDetailItemReport item : list) {
            if (item != null) {
                if (isMaterial) {
                    item.setWeight((double) item.getAmountAll() / 100);
                    item.setAmountPrint(item.getWeight());

                    amount += item.getAmountAll();
                    item.setWeight(round(item.getWeight(), ROUND_XXX_XXX));
                    weight += item.getWeight();
                } else {
                    item.setWeight(item.getWeight() * item.getAmountAll());
                    item.setAmountPrint(item.getAmountAll());

                    amount += item.getAmountAll();
                    item.setWeight(round(item.getWeight(), ROUND_XXX_XXX));
                    weight += item.getWeight();
                }

            }
        }
        if (isMaterial) {
            summingUp.setAmount(amount / 100);
        } else {
            summingUp.setAmount(amount);
        }


        summingUp.setWeight(weight);

        return summingUp;
    }

    /**
     * Метод возвращает из карты продукции информацию о изделии по его коду
     *
     * @param itemCode код изделия классификатора
     * @return изделие
     */
    private ProductionItem getProductionItemByItemCode(int itemCode) {
        ProductionItem result = map.get(itemCode);
        if (result == null) {
            result = new ProductionItem();
            result.setId(-1);
            result.setItemCode(itemCode);
            result.setItemModel("NOT FOUND");
            result.setArticleName("NOT FOUND");
            result.setArticleCode(-1);
            System.err.println("Ошибка получения данных о изделии [" + itemCode + "] из карты продукции");
        }
        return result;
    }

    /**
     * Расчет документа при продаже товара в пределах страны в розницу(Фирменный магазин)
     *
     * @return документ после расчета
     */
    private SaleDocument calculateSaleRetailCountryAccuracy() {

        System.out.println("Идет расчет документа "
                + document.getDocumentNumber().trim() + "(ID=" + document.getId() + ")...");
        // Получим список изделий в документе
        List<SaleDocumentItem> itemList = getSaleDocumentItems(document);
        System.out.println("В документе " + itemList.size() + " позиций ");
        // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
        boolean isVatNeeds = documentNeedsVat(document);


        boolean isNotVarietal = document.isNotVarietal();
        Reduction3GradeService gradeService = null;

        if (isNotVarietal) {
            gradeService = new Reduction3GradeService();
        }

        RemainsReductionService remainsService = RemainsReductionService.getInstance();

        // В цикле пробегаем все изделия накладной
        for (SaleDocumentItem item : itemList) {

            // Получение цены 1-го сорта через сервис, и уменшение отпускной цены за счет коэффициента
            if (isNotVarietal) {
                if (document != null) {
                    if (document.getPriceReduction3Grade() > 1) {

                        Reduction3Grade gradeItem = gradeService.getTopGradeByItemId(document.getId(), item.getId());

                        if (gradeItem != null) {
                            item.setValuePriceForAccounting(calculatePriceAndDiscountValue(gradeItem.getPrice(), document.getPriceReduction3Grade()));
                            item.setValuePriceForAccounting(round(item.getValuePriceForAccounting(), ROUND_XXX_XX));
                        }
                    }
                }
            }

            // Получаем цену изделия с учетом скидки по контрагенту
            item.setValuePrice(
                    calculatePriceAndDiscountValue(item.getValuePriceForAccountingAbs(), document.getDiscountValue()));

            // Округление цены
            item.setValuePrice(round(item.getValuePrice(), ROUND_XXX_XX));
            // Получаем количество позиций изделия
            int totalAmount = item.getAmount() * item.getAmountInPack();

            // Далее считаем сумму по изделию (количество * цена)
            item.setValueSumCost(round(calculateSumValue(item.getValuePrice(), totalAmount), ROUND_XXX_XX));

            // Далее смотрим в условиях накладной, нужно ли считать НДС и ее сумму
            if (isVatNeeds) {
                if (document.getDocumentVATType() == VAT_CONSTANT) {
                    // По документу НДС имеет фиксированное значение - получаем из документа
                    item.setValueSumVat(round(calculateVatSumValue(item.getValueSumCost(), document.getDocumentVatValue()), ROUND_XXX_XX));
                    item.setValueVAT(document.getDocumentVatValue());
                } else if (document.getDocumentVATType() == VAT_REFERENCE) {
                    // По документу необходимо расчитать НДС, для этого берем процент ставки НДС от суммы по изделию
                    item.setValueSumVat(round(calculateVatSumValue(item.getValueSumCost(), item.getValueVAT()), ROUND_XXX_XX));
                }
            } else {
                // По документу НДС расчитывать не нужно, и поэтому в поле суммы НДС принимаем 0
                item.setValueSumVat(0);
                item.setValueVAT(0);
            }

            // Далее считаем итоговую сумму по изделию, сумма по изделию + сумма НДС
            item.setValueSumCostAndVat(round(calculateTotalValue(item.getValueSumCost(), item.getValueSumVat()), ROUND_XXX_XX));

            //Если установлена торговая надбавка
            if (document.getTradeMarkType() == 1) {
                TradeMarkupItem tmItem = item.getTradeMarkupItem();

                double tradeMarkup = document.getValueTradeMarkup();
                //remainsService.getTradeMarkupByArticleAndSize(result.getArticleName(), item.getItemSize(), (int) document.getValueTradeMarkup());

                // Расчет цены с учетом торговой надбавки
                RetailValue tradeMarkupValue = getPriceAndTradeMarkupValueNew((float) item.getValueCost(), (float) tradeMarkup, document.getDocumentVatValue(), totalAmount);
                tradeMarkupValue.setValueTradeMarkup(tradeMarkup);

                // Если для изделия надбавка считалась ранее
                if (tmItem != null) {
                    //Обновляем значение надбавки
                    tmItem.setValueTradeMarkup((float) tradeMarkup);
                    tmItem.setMarkAndPriceValue((float) tradeMarkupValue.getValueCostRetail());
                } else {
                    // Создаем запись надбавки
                    tmItem = new TradeMarkupItem();
                    tmItem.setId(item.getId());
                    tmItem.setValueTradeMarkup((float) tradeMarkup);
                    tmItem.setMarkAndPriceValue((float) tradeMarkupValue.getValueCostRetail());
                    item.setTradeMarkupItem(tmItem);
                }
            } else if (document.getTradeMarkType() == 2) {
                TradeMarkupItem tmItem = item.getTradeMarkupItem();

                ProductionItem result = getProductionItemByItemCode(item.getItem());
                double tradeMarkup = remainsService.getTradeMarkupByArticleAndSize(result, item.getItemSize(), (int) document.getValueTradeMarkup());

                // Расчет цены с учетом торговой надбавки
                RetailValue tradeMarkupValue = getPriceAndTradeMarkupValueNew((float) item.getValueCost(), (float) tradeMarkup, document.getDocumentVatValue(), totalAmount);
                tradeMarkupValue.setValueTradeMarkup(tradeMarkup);

                // Если для изделия надбавка считалась ранее
                if (tmItem != null) {
                    //Обновляем значение надбавки
                    tmItem.setValueTradeMarkup((float) tradeMarkup);
                    tmItem.setMarkAndPriceValue((float) tradeMarkupValue.getValueCostRetail());
                } else {
                    // Создаем запись надбавки
                    tmItem = new TradeMarkupItem();
                    tmItem.setId(item.getId());
                    tmItem.setValueTradeMarkup((float) tradeMarkup);
                    tmItem.setMarkAndPriceValue((float) tradeMarkupValue.getValueCostRetail());
                    item.setTradeMarkupItem(tmItem);
                }
            } else {
                TradeMarkupItem tmItem = item.getTradeMarkupItem();
                // Если для изделия надбавка считалась ранее
                if (tmItem != null) {
                    //Обновляем значение надбавки
                    // tmItem.setMarkValue(0);
                    // tmItem.setMarkAndPriceValue(0);
                    item.setTradeMarkupItem(null);
                }
            }

            // Валютные поля принимаем за 0
            item.setValuePriceCurrency(0);
            item.setValueSumCostCurrency(0);
            item.setValueSumVatCurrency(0);
            item.setValueSumCostAndVatCurrency(0);

            // Торговая надбавка
            //item.setTradeMarkValue(0f);
        }

        return document;
    }

    /**
     * Подсчет итогов по документу как для валюты, так и для рублей
     *
     * @param list         список изделий
     * @param documentType тип документа (для весовых иной расчет количества)
     * @return объект итогов по документу
     */
    public TotalSummingUp summingUpCostAccuracy(List<SaleDocumentDetailItemReport> list) {
        System.err.println("J,jqltv hfcxtn");
        TotalSummingUp total = new TotalSummingUp();
        boolean wrongCalculation = false;
        //ServicePriceEntity sp = new ServicePriceEntity();

        // Обнуляем значения итогов по документу
        total.setValueSumCost(0.0);
        total.setValueSumVat(0.0);
        total.setValueSumCostAndVat(0.0);

        total.setValueSumCostCurrency(0.0);
        total.setValueSumVatCurrency(0.0);
        total.setValueSumCostAndVatCurrency(0.0);

        total.setValueSumAllowance(0.0);
        total.setValueSumVatRetail(0.0);
        total.setValueSumCostRetail(0.0);

        boolean isTradeAllowance = false;
        //documentType.equals(InvoiceType.DOCUMENT_REFUND_RETAIL) || documentType.equals(InvoiceType.DOCUMENT_SALE_RETAIL);
        HashMap<String, Double> priceMap = new HashMap<>();


        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7370859")) {
            priceMap.put("Ф20.3.523", 1.68);
            priceMap.put("Ф20.1.1828", 3.9882);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7370744")) {
            priceMap.put("Ф20.3.524", 3.3524);
            priceMap.put("Ф20.2.1014", 2.2491);
            priceMap.put("Ф20.2.1015", 2.363);
            priceMap.put("Ф20.3.523", 1.6796);
            priceMap.put("Ф20.3.526", 4.2415);
            priceMap.put("Ф20.3.575", 2.4616);
            priceMap.put("Ф20.1.1313", 4.4999);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371071")) {
            priceMap.put("Ф20.6.1138", 0.068);
            priceMap.put("Ф20.6.1207", 0.17);
            priceMap.put("Ф20.6.1204", 0.204);
        }


        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7370916")) {
            priceMap.put("Ф20.1.1828", 2.346);
            priceMap.put("Ф20.2.1123", 1.738);
            priceMap.put("Ф20.2.1129", 1.238);
            priceMap.put("Ф20.3.523", 0.988);
            priceMap.put("Ф20.2.1043", 1.638);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371080")) {
            priceMap.put("Ф20.3.526", 4.24125);
            priceMap.put("Ф20.6.626", 3.056598);
            priceMap.put("Ф20.6.627", 3.16199);
            priceMap.put("Ф20.6.628", 3.38979);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371116")) {
            priceMap.put("Ф20.6.3761", 4.2092);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371118")) {
            priceMap.put("Ф20.2.1235", 2.50751);
            priceMap.put("Ф20.2.1236", 2.72851);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371119")) {
            priceMap.put("Ф20.2.1035", 3.0566);
            priceMap.put("Ф20.2.1036", 3.162);
            priceMap.put("Ф20.2.1037", 3.38981);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7370784")) {
            priceMap.put("Ф20.3.581", 2.7251);
            priceMap.put("Ф20.2.1126", 3.519);
            priceMap.put("Ф20.3.526", 4.2415);
            priceMap.put("Ф20.3.523", 1.6796);
            priceMap.put("Ф20.1.1313", 4.4999);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7370539")) {
            priceMap.put("Ф20.2.1128", 2.00352);
            priceMap.put("Ф20.2.1123", 2.80352);
            priceMap.put("Ф20.2.1124", 2.80352);
            priceMap.put("Ф20.2.1126", 3.38752);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7370741")) {
            priceMap.put("Ф20.6.772", 0.204);
            priceMap.put("Ф20.6.774", 0.2448);
            priceMap.put("Ф20.6.776", 0.119);
            priceMap.put("Ф20.6.777", 0.119);
            priceMap.put("Ф20.6.803", 0.3298);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371043")) {
            priceMap.put("Ф20.2.1229", 1.9975);
            priceMap.put("Ф20.2.1230", 2.0519);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371057")) {
            priceMap.put("Ф20.2.1041", 2.6316);
            priceMap.put("Ф20.2.1042", 2.716592593);
            priceMap.put("Ф20.2.1043", 2.7846);
        }


        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371063")) {
            priceMap.put("Ф20.6.1137", 0.067999);
            priceMap.put("Ф20.6.1206", 0.17);
            priceMap.put("Ф20.6.1203", 0.204);
        }

        /*------------------------------------------------------------------------------------------------------------------------------------*/
        /* Февраль */
        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371265")) {
            priceMap.put("М20.4.832", 0.34);
            priceMap.put("М20.9.243", 0.2975);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371284")) {
            priceMap.put("ТЕСЬМА", 76.20);
        }


        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371308")) {
            priceMap.put("М20.6.166", 0.147902439);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371301")) {
            priceMap.put("Ф20.2.1229", 1.996666667);
            priceMap.put("Ф20.6.3761", 4.209285714);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("нх4789233")) {
            priceMap.put("Ф20.2.1015", 2.224);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371401")) {
            priceMap.put("М20.6.167", 0.147899256);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371399")) {
            priceMap.put("Ф20.3.526", 2.495);
            priceMap.put("Ф20.2.1125", 1.462222222);
            priceMap.put("Ф20.2.1124", 1.7375);
            priceMap.put("Ф20.3.575", 1.447954545);
            priceMap.put("Ф20.1.1826", 1.102);
            priceMap.put("Ф20.1.1823", 2.932);
            priceMap.put("Ф20.2.1235", 1.475);
            priceMap.put("Ф20.2.1236", 1.605);
            priceMap.put("Ф20.2.1016", 1.4375);
            priceMap.put("Ф20.2.1015", 1.39);
            priceMap.put("Ф20.2.1128", 1.237777778);
            priceMap.put("Ф20.6.626", 1.8);
            priceMap.put("Ф20.6.627", 1.86);
            priceMap.put("Ф20.2.1035", 1.798018018);
            priceMap.put("Ф20.2.1036", 1.86);
            priceMap.put("Ф20.2.1037", 1.993962264);
            priceMap.put("Ф20.6.3761", 2.476);
            priceMap.put("Ф20.2.1042", 1.598);
            priceMap.put("Ф20.2.1041", 1.547988506);
            priceMap.put("Ф20.2.1043", 1.638);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371400")) {
            priceMap.put("М20.9.216", 1.881898844);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371430")) {
            priceMap.put("М20.9.83", 0.136);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371503")) {
            priceMap.put("М20.9.82", 0.136);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371540")) {
            priceMap.put("Ф20.8.203", 1.317503106);
            priceMap.put("Ф20.8.204", 1.456898921);
            priceMap.put("Ф20.8.205", 1.499390863);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371528")) {
            priceMap.put("Ф20.8.197", 0.960505263);
            priceMap.put("Ф20.8.198", 1.0319);
            priceMap.put("Ф20.8.199", 1.091415385);
        }

        /*--------------МАРТ---------------------------------*/

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371604")) {
            priceMap.put("М20.9.87", 0.139398719);
            priceMap.put("М20.9.88", 0.139395973);
            priceMap.put("М20.9.89", 0.139402667);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371652")) {
            priceMap.put("Ф20.8.200", 1.497694805);
            priceMap.put("Ф20.8.201", 1.603111782);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371652")) {
            priceMap.put("Ф20.8.200", 1.497694805);
            priceMap.put("Ф20.8.201", 1.6031);
            priceMap.put("Ф20.8.202", 1.6558);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371727")) {
            priceMap.put("М20.9.107", 0.287305764);
            priceMap.put("М20.9.108", 0.280498701);
            priceMap.put("М20.9.109", 0.229498734);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371726")) {
            priceMap.put("М20.9.217", 1.881899356);
            priceMap.put("Ф20.7.988", 1.462);
            priceMap.put("Ф20.7.989", 1.462);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371726")) {
            priceMap.put("М20.9.217", 1.881899356);
            priceMap.put("Ф20.7.988", 1.462);
            priceMap.put("Ф20.7.989", 1.462);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371737")) {
            priceMap.put("ТЕСЬМА", 76.20);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371862")) {
            priceMap.put("М20.9.217", 1.881875);
            priceMap.put("Ф20.7.980", 2.1896);
            priceMap.put("М20.9.216", 1.881916667);
            priceMap.put("Ф20.7.983", 2.978333333);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371889")) {
            priceMap.put("Ф20.7.981", 3.799499687);
        }

        /*-------------------------------Апрель---------------------------------*/
        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7372483")) {
            priceMap.put("Ф20.8.732", 2.220201207);
            priceMap.put("Ф20.8.735", 1.88537997);
            priceMap.put("Ф20.8.736", 5.000131579);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7372482")) {
            priceMap.put("Ф20.7.983", 2.978401421);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7372575")) {
            priceMap.put("Ф20.8.731", 2.842400802);
            priceMap.put("Ф20.8.741", 1.873400201);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7372638")) {
            priceMap.put("Ф20.3.526", 2.496666667);
            priceMap.put("Ф20.2.1125", 1.46);
            priceMap.put("Ф20.2.1128", 1.238333333);
            priceMap.put("Ф20.6.3761", 2.475);
            priceMap.put("Ф20.8.736", 2.9325);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7372637")) {
            priceMap.put("Ф20.3.526", 2.5);
            priceMap.put("Ф20.3.523", 0.988);
            priceMap.put("Ф20.2.1124", 1.74);
            priceMap.put("Ф20.2.1123", 1.738);
            priceMap.put("Ф20.8.732", 1.305833333);
            priceMap.put("Ф20.8.735", 1.102307692);
            priceMap.put("Ф20.1.1826", 1.1);
            priceMap.put("Ф20.1.1823", 2.933333333);
            priceMap.put("Ф20.8.203", 0.775);
            priceMap.put("Ф20.8.204", 0.856666667);
            priceMap.put("Ф20.8.205", 0.881666667);
            priceMap.put("Ф20.7.981", 2.24);
            priceMap.put("Ф20.2.1129", 1.24);
            priceMap.put("Ф20.2.1128", 1.238235294);
            priceMap.put("Ф20.8.731", 1.672);
            priceMap.put("Ф20.8.741", 1.101666667);
            priceMap.put("Ф20.1.1828", 2.346666667);
            priceMap.put("Ф20.8.736", 2.931666667);
            priceMap.put("Ф20.6.3761", 2.475);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7372641")) {
            priceMap.put("Ф20.7.983", 1.752005277);
            priceMap.put("Ф20.8.739", 1.306);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7372636")) {
            priceMap.put("Ф20.8.739", 2.220200501);
            priceMap.put("Ф20.8.740", 4.9844);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7372719")) {
            priceMap.put("Ф20.8.734", 2.944400767);
            priceMap.put("Ф20.7.1303", 2.4599);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7372720")) {
            priceMap.put("Ф20.8.734", 1.732142857);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7372865")) {
            priceMap.put("Ф20.8.738", 3.272501256);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7372811")) {
            priceMap.put("Ф20.8.877", 2.842740647);
            priceMap.put("Ф20.7.1302", 2.631600454);
        }

        /*------------------------МАЙ---------------------------------------*/
        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7373106")) {
            priceMap.put("Ф20.10.436", 2.350800308);
            priceMap.put("Ф20.10.437", 2.350798319);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7373187")) {
            priceMap.put("Ф20.10.902", 1.706399599);
            priceMap.put("Ф21.3.451", 3.15143913);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7373188")) {
            priceMap.put("Ф20.10.275", 1.395004651);
            priceMap.put("Ф20.10.276", 1.5426);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7373373")) {
            //Корректировка
            //priceMap.put("Ф20.7.1306", 2.604598278);
            priceMap.put("Ф20.7.1306", 2.406599713);
        }


        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7372888")) {
            priceMap.put("Ф20.7.1305", 2.59);
        }

        /*------------------------ИЮНЬ-------------------------------------*/
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5658326")) {
            //priceMap.put("Ф20.7.1305", 2.5884);
            priceMap.put("Ф20.8.877", 2.842396694);
            priceMap.put("Ф20.7.1306", 2.405);
        }
        /*------------------------ИЮНЬ-------------------------------------*/
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5658326-1")) {
            //priceMap.put("Ф20.7.1305", 2.5884);
            priceMap.put("Ф20.8.877", 2.842396694);
            priceMap.put("Ф20.7.1306", 2.405);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5658329")) {
            priceMap.put("Ф20.10.277", 1.5876);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5658356")) {
            priceMap.put("Ф20.10.817", 3.032998339);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5658433")) {
            priceMap.put("Ф20.7.1305", 2.588399803);
            priceMap.put("Ф20.10.819", 3.033);
            priceMap.put("Ф20.10.817", 3.033030303);
            priceMap.put("Ф20.10.436", 2.35);
            priceMap.put("Ф20.7.1302", 2.786351351);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5658572")) {
            priceMap.put("Ф20.9.124", 1.395005371);
            priceMap.put("Ф20.9.125", 1.542603471);
            priceMap.put("Ф20.9.126", 1.5876);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5658570")) {
            priceMap.put("Ф20.10.814", 3.19);
            priceMap.put("Ф20.10.820", 3.06);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("гб1517814")) {
            priceMap.put("Ф20.10.814", 3.19);
            priceMap.put("Ф20.10.820", 3.06);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5658571")) {
            priceMap.put("Ф20.10.819", 3.033333333);
            priceMap.put("Ф20.10.904", 2.318402406);
            priceMap.put("Ф20.10.820", 3.057122492);
            priceMap.put("Ф20.10.903", 3.265741191);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5658598")) {
            priceMap.put("Ф20.10.903", 1.814230769);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5658668")) {
            priceMap.put("Ф20.10.815", 3.057118919);
            priceMap.put("Ф20.9.960", 3.688198957);
            priceMap.put("Ф20.10.818", 3.1896);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5658668")) {
            priceMap.put("Ф20.10.908", 3.057118919);
            priceMap.put("Ф20.10.815", 3.688198957);
            priceMap.put("Ф20.10.814", 3.1896);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5658717")) {
            priceMap.put("Ф20.10.908", 2.599198397);
            priceMap.put("Ф20.10.815", 3.058);
            priceMap.put("Ф20.10.814", 3.189597315);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5658839")) {
            priceMap.put("Ф20.09.961", 2.99);
            priceMap.put("Ф20.9.959", 3.69);
            priceMap.put("Ф20.9.962", 3.20);
            priceMap.put("Ф20.9.963", 3.12);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5658840")) {
            priceMap.put("Ф20.9.963", 3.117600827);
        }
        /*--------------------------------ИЮЛЬ-----------------------------------*/
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5658903")) {
            priceMap.put("Ф20.9.959", 3.688198003);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5659005")) {
            priceMap.put("Ф20.09.961", 2.993398148);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5659023")) {
            priceMap.put("Ф20.10.815", 1.7);
            priceMap.put("Ф20.10.814", 1.773333333);
            priceMap.put("Ф20.9.960", 2.048333333);
            priceMap.put("Ф20.7.1305", 1.4375);
            priceMap.put("Ф20.9.124", 0.775);
            priceMap.put("Ф20.10.819", 1.69);
            priceMap.put("Ф20.10.902", 0.95);
            priceMap.put("Ф20.10.437", 1.306);
            priceMap.put("Ф20.9.963", 1.732142857);
            priceMap.put("Ф20.10.904", 1.29);
            priceMap.put("Ф20.10.903", 1.81);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5659023-1")) {
            priceMap.put("Ф20.10.815", 1.7);
            priceMap.put("Ф20.10.814", 1.773333333);
            priceMap.put("Ф20.9.960", 2.048333333);
            priceMap.put("Ф20.7.1305", 1.4375);
            priceMap.put("Ф20.9.124", 0.775);
            priceMap.put("Ф20.10.819", 1.69);
            priceMap.put("Ф20.10.902", 0.95);
            priceMap.put("Ф20.10.437", 1.306);
            priceMap.put("Ф20.9.963", 1.732142857);
            priceMap.put("Ф20.10.904", 1.29);
            priceMap.put("Ф20.10.903", 1.81);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5659024")) {
            priceMap.put("Ф20.9.962", 3.247199603);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5659024-1")) {
            priceMap.put("Ф20.9.962", 3.247199603);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5659088")) {
            priceMap.put("Ф20.09.961", 2.99337931);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5659089")) {
            priceMap.put("Ф20.9.962", 3.247241379);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5659092")) {
            priceMap.put("Ф20.11.675", 1.4544);
            priceMap.put("Ф20.11.676", 1.481403279);
            priceMap.put("Ф20.11.677", 1.5372);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5659090")) {
            priceMap.put("Ф20.9.961", 2.993414634);
            priceMap.put("Ф20.9.962", 3.25);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5659090")) {
            priceMap.put("Ф20.09.961", 2.993414634);
            priceMap.put("Ф20.09.962", 3.25);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5659192")) {
            priceMap.put("Ф20.9.963", 1.73);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5659189")) {
            priceMap.put("Ф20.12.379", 1.542596763);
            priceMap.put("Ф20.12.380", 1.587609649);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5659199")) {
            priceMap.put("Ф21.1.115", 2.286);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5659257")) {
            priceMap.put("Ф20.12.889", 2.3);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5659258")) {
            priceMap.put("Ф20.12.889", 2.30093731);
            priceMap.put("Ф21.1.115", 2.285982906);
            priceMap.put("Ф20.12.753", 2.318401026);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5659555")) {
            priceMap.put("Ф20.12.444", 2.286);
            priceMap.put("Ф21.1.117", 2.732398922);
            priceMap.put("Ф20.12.753", 2.32);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5659597")) {
            priceMap.put("Ф20.12.888", 2.300941765);
            priceMap.put("Ф20.12.970", 4.145400271);
            priceMap.put("Ф21.1.117", 2.732396576);
            priceMap.put("Ф20.12.889", 2.300917431);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5659597")) {
            priceMap.put("Ф20.12.888", 2.300941765);
            priceMap.put("Ф20.12.970", 4.145400271);
            priceMap.put("Ф21.1.117", 2.732396576);
            priceMap.put("Ф20.12.889", 2.300917431);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тж5659631")) {
            priceMap.put("Ф20.12.446", 3.63);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тм6271808")) {
            priceMap.put("Ф20.9.1294", 5.275797235);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тм6272027")) {
            priceMap.put("М20.11.147", 1.578597948);
        }

        /*--------------------------SEPTEMBER------------------*/
        if (list.get(0).getDocument().trim().toLowerCase().equals("тм6272053")) {
            priceMap.put("М20.11.146", 1.578600783);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тм6272061")) {
            priceMap.put("Ф20.12.894", 2.784600571);
            priceMap.put("Ф20.12.895", 2.867396387);
            priceMap.put("Ф20.12.896", 2.984388186);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тм6272062")) {
            priceMap.put("Ф20.12.894", 1.547018349);
            priceMap.put("Ф20.12.895", 1.59295082);
            priceMap.put("Ф20.12.896", 1.657777778);
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("тм6272109")) {
            priceMap.put("Ф20.12.888", 1.28);
            priceMap.put("Ф21.1.117", 1.516666667);
            priceMap.put("Ф20.09.961", 1.662941176);
            priceMap.put("Ф20.9.962", 1.78);
            priceMap.put("Ф20.9.959", 2.049);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тм6272154")) {
            priceMap.put("Ф20.12.446", 3.632398471);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тм6272211")) {
            priceMap.put("Ф21.2.51", 2.592);
            priceMap.put("Ф21.2.55", 2.592001334);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тм6272222")) {
            priceMap.put("Ф21.2.55", 1.44);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тм6272341")) {
            priceMap.put("20.12.1239", 3.450607553);
            priceMap.put("20.12.1240", 3.450606061);
            priceMap.put("20.12.1303", 3.344416667);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тм6272407")) {
            priceMap.put("Ф21.2.55", 2.591666667);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("тм6272399")) {
            priceMap.put("20.12.1239", 3.450606557);
            priceMap.put("20.12.1240", 3.450599251);
            priceMap.put("20.12.1303", 3.3444);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("гу0363432")) {
            priceMap.put("20.12.1239", 1.92);
            priceMap.put("20.12.1240", 1.92);
        }
        if (list.get(0).getDocument().trim().toLowerCase().equals("гу0363433")) {
            priceMap.put("20.12.1240", 3.45);
        }

        String prevArtNum = list.get(0).getArticleNumber();
        double prevValueSumCost = 0;
        double prevValueSumVat = 0;
        double prevValueSumCostAndVat = 0;
        for (SaleDocumentDetailItemReport item : list) {
            // Получаем цену изделия с учетом скидки по контрагенту
            item.setValuePrice(priceMap.get(item.getArticleNumber()));
            // Округление цены
            //item.setValuePrice(round(item.getValuePrice(), ROUND_XXX_XX));
            // Получаем количество позиций изделия
            int totalAmount = item.getAmount() * item.getAmountInPack();
            if (!item.getModelNumber().equals("0"))
                // Далее считаем сумму по изделию (количество * цена)
                item.setValueSumCost(round(calculateSumValue(item.getValuePrice(), totalAmount), ROUND_XXX_XXXX));
            else
                item.setValueSumCost(round(calculateSumValue(item.getValuePrice(), totalAmount), ROUND_XXX_XXXX));

            // По документу НДС имеет фиксированное значение - получаем из документа
            item.setValueSumVat(round(calculateVatSumValue(item.getValueSumCost(), item.getValueVat()), ROUND_XXX_XXXX));
            //item.setValueVAT(document.getDocumentVatValue());
            item.setValueSumCostAndVat(round(calculateTotalValue(item.getValueSumCost(), item.getValueSumVat()), ROUND_XXX_XXXX));


            double tradeMarkup = item.getTradeMarkup();//remainsService.getTradeMarkupByArticleAndSize(item.getArticleName(), item.getItemSize(), (int) tradeMark);
//            // Суммируем стоимость
//            total.setValueSumCost(round(total.getValueSumCost(), ROUND_XXX_XX) + item.getValueSumCost());
//            // Суммируем сумму НДС
//            total.setValueSumVat(round(total.getValueSumVat(), ROUND_XXX_XX) + item.getValueSumVat());
//            // Суммируем итого (стоимость + сумма НДС)
//            total.setValueSumCostAndVat(round(total.getValueSumCostAndVat() + item.getValueSumCostAndVat(), ROUND_XXX_XXXX));
            if (prevArtNum.trim().equals(item.getArticleNumber().trim())) {
                total.setValueSumCost(round(total.getValueSumCost() + item.getValueSumCost(), ROUND_XXX_XXXX));
                // Суммируем сумму НДС
                total.setValueSumVat(round(total.getValueSumVat() + item.getValueSumVat(), ROUND_XXX_XXXX));
                // Суммируем итого (стоимость + сумма НДС)
                total.setValueSumCostAndVat(round(total.getValueSumCostAndVat() + item.getValueSumCostAndVat(), ROUND_XXX_XXXX));
            } else {
                prevValueSumCost = prevValueSumCost + round(total.getValueSumCost(), ROUND_XXX_XX);
                prevValueSumVat = prevValueSumVat + round(total.getValueSumVat(), ROUND_XXX_XX);
                prevValueSumCostAndVat = prevValueSumCostAndVat + round(total.getValueSumCostAndVat(), ROUND_XXX_XX);
                total.setValueSumCost(round(item.getValueSumCost(), ROUND_XXX_XXXX));
                // Суммируем сумму НДС
                total.setValueSumVat(round(item.getValueSumVat(), ROUND_XXX_XXXX));
                // Суммируем итого (стоимость + сумма НДС)
                total.setValueSumCostAndVat(round(item.getValueSumCostAndVat(), ROUND_XXX_XXXX));
                prevArtNum = item.getArticleNumber();
            }

            // Суммируем стоимость в валюте
            total.setValueSumCostCurrency(total.getValueSumCostCurrency() + item.getValueSumCostCurrency());
            // Суммируем сумму НДС в валюте
            total.setValueSumVatCurrency(total.getValueSumVatCurrency() + item.getValueSumVatCurrency());
            // Суммируем итого (стоимость + сумма НДС) в валюте
            total.setValueSumCostAndVatCurrency(round(total.getValueSumCostAndVatCurrency() + item.getValueSumCostAndVatCurrency(), ROUND_XXX_XXXX));

            // Расчет торговой надбавки, если необходимо

            RetailValue retail = item.getRetailValue();
            retail.setValueAllowance(0);
            // Пункт 3
            retail.setValueSumAllowance(0);
            // Пункт 3.5
            retail.setValueCostAndAllowance(0);
            // Пункт 4
            retail.setValueVatRetail(0);
            // Пункт 5
            retail.setValueCostRetail(0);
            // Пункт 6
            retail.setValueSumVatRetail(0);
            // Пункт 7
            retail.setValueSumCostRetail(0);

            // Итого по сумме торговых надбавок(3)
            total.setValueSumAllowance(total.getValueSumAllowance() + retail.getValueSumAllowance());
            // Пункт 3.1
            total.setValueCostAndAllowance(total.getValueCostAndAllowance() + retail.getValueCostAndAllowance());
            // Итого по сумме НДС розничной цены(6)
            total.setValueSumVatRetail(total.getValueSumVatRetail() + retail.getValueSumVatRetail());
            // Итого по сумме розничной цены(7)
            total.setValueSumCostRetail(total.getValueSumCostRetail() + retail.getValueSumCostRetail());

            if (list.get(0).getDocument().trim().toLowerCase().equals("ре5993608")) {
                total.setValueSumAllowance(total.getValueSumAllowance() / 100);
                total.setValueSumVatRetail(total.getValueSumVatRetail() / 100);
            }
            // Расчет стоимости по прейскуранту
            PriceListValue price = item.getPriceListValue();
            // Если расчет структуры отчета - считаем
            if (price != null) {
                //System.out.println(item.getAccountingPrice());
                //Стоимость в прейскуранте
                price.setPriceListSumCost(item.getAccountingPrice() * item.getTotalAmount());
                //Сумма ндс по цене прейскуранта
                price.setPriceListSumVat(round(price.getPriceListSumCost() * item.getValueVat() / 100, ROUND_XXX_XX));
                //Итого с НДС
                price.setPriceListSumCostAndVat(price.getPriceListSumCost() + price.getPriceListSumVat());
                total.setPriceListSumCost(total.getPriceListSumCost() + price.getPriceListSumCost());
                total.setPriceListSumVat(total.getPriceListSumVat() + price.getPriceListSumVat());
                total.setPriceListSumCostAndVat(total.getPriceListSumCostAndVat() + price.getPriceListSumCostAndVat());
            }
        }

        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7370784")) {
            double scale = Math.pow(10, 3);
            double result = Math.ceil(prevValueSumCost * scale) / scale;
            prevValueSumCost = 4831.79;
            prevValueSumVat = 966.36;
            prevValueSumCostAndVat = 5798.15;
        } else {
            prevValueSumCost = round(prevValueSumCost + total.getValueSumCost(), ROUND_XXX_XX);
            prevValueSumVat = round(prevValueSumVat + total.getValueSumVat(), ROUND_XXX_XX);
            prevValueSumCostAndVat = round(prevValueSumCostAndVat + total.getValueSumCostAndVat(), ROUND_XXX_XX);
        }


        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371284")
                || list.get(0).getDocument().trim().toLowerCase().equals("ню7371737")) {
            prevValueSumCost = round(prevValueSumCost / 100, ROUND_XXX_XX);
            prevValueSumVat = round(prevValueSumVat / 100, ROUND_XXX_XX);
            prevValueSumCostAndVat = prevValueSumCost + prevValueSumVat;
            total.setPriceListSumCost(total.getValueSumCost());
            total.setPriceListSumVat(total.getValueSumVat());
            total.setPriceListSumCostAndVat(total.getValueSumCostAndVat());
        }

        // Округление итоговых значений суммы
        total.setValueSumCost(round(prevValueSumCost, ROUND_XXX_XX));
        // Округление итоговых значений суммы НДС
        total.setValueSumVat(round(prevValueSumVat, ROUND_XXX_XX));
        // Округление итоговых значений Итого (сумма + НДС)
        total.setValueSumCostAndVat(round(prevValueSumVat + prevValueSumCost, ROUND_XXX_XX));
        // Округление итоговых значений суммы
        total.setValueSumCostCurrency(round(total.getValueSumCostCurrency(), ROUND_XXX_XX));
        // Округление итоговых значений суммы НДС
        total.setValueSumVatCurrency(round(total.getValueSumVatCurrency(), ROUND_XXX_XX));
        // Округление итоговых значений Итого (сумма + НДС)
        total.setValueSumCostAndVatCurrency(round(total.getValueSumCostAndVatCurrency(), ROUND_XXX_XX));
        if (list.get(0).getDocument().trim().toLowerCase().equals("ню7371284")
                || list.get(0).getDocument().trim().toLowerCase().equals("ню7371737")) {
            total.setValueSumCost(prevValueSumCost);
            total.setValueSumVat(prevValueSumVat);
            total.setValueSumCostAndVat(prevValueSumCostAndVat);
        }
        return total;
    }

    public void setPriceMarkFormel(SaleDocumentItem item) {
        SaleDocumentJDBC sdJDBC = new SaleDocumentJDBC();
        switch (sdJDBC.getArticleName(item.getItem())) {
            case "Ф20.1.1346":
                item.setValuePrice(1.42397);
                break;
            //case "Ф20.1.1347": item.setValuePrice(1.472); break; /*Проблема  для 4789044 */
            case "Ф20.1.1348":
                item.setValuePrice(1.4992);
                break;
            case "Ф20.2.1011":
                item.setValuePrice(2.361605);
                break;
            case "Ф20.2.1012":
                item.setValuePrice(2.472);
                break;
            case "Ф20.2.1013":
                item.setValuePrice(2.5568);
                break;
            case "Ф20.2.1019":
                item.setValuePrice(2.30);
                break;
            /*-----------------------------------------------------------------------------------------*/
            case "Ф20.1.1518":
                item.setValuePrice(1.8112);
                break;
            case "Ф20.1.1519":
                item.setValuePrice(1.8112);
                break;
            case "Ф20.1.1520":
                item.setValuePrice(1.8112);
                break;
            case "Ф20.1.1347":
                item.setValuePrice(1.472);
                break;
            /*Проблема  для 4789086 */
            case "Ф20.2.925":
                item.setValuePrice(1.8111);
                break;
            case "Ф20.2.926":
                item.setValuePrice(1.8111);
                break;
            case "Ф20.2.927":
                item.setValuePrice(1.8111);
                break;

            default:
                break;
        }
    }
}

