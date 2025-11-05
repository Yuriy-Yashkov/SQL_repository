package by.march8.ecs.application.modules.marketing.manager;

import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.ProductionItem;
import by.march8.ecs.application.modules.warehouse.external.shipping.services.Reduction3GradeService;
import by.march8.ecs.application.modules.warehouse.external.shipping.services.RemainsReductionService;
import by.march8.ecs.application.modules.warehouse.external.shipping.services.SpecialPriceService;
import by.march8.ecs.framework.helpers.digits.CurrencyType;
import by.march8.entities.sales.PreOrderSaleDocument;
import by.march8.entities.sales.PreOrderSaleDocumentItem;
import by.march8.entities.sales.PreOrderSaleDocumentItemView;
import by.march8.entities.sales.PreOrderSaleDocumentView;
import by.march8.entities.warehouse.RetailValue;

import java.util.HashMap;
import java.util.List;

import static by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator.ROUND_XXX_XX;
import static by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator.calculateCurrencyPriceFromPriceValue;
import static by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator.calculatePriceAndDiscountValue;
import static by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator.calculatePriceFromCurrencyPriceValue;
import static by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator.calculateSumValue;
import static by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator.calculateTotalValue;
import static by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator.calculateVatSumValue;
import static by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator.getPriceAndTradeMarkupValueNew;
import static by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentCalculator.round;

public class PreOrderSaleDocumentCalculator {

    public static PreOrderSaleDocumentCalculator instance = null;
    private PreOrderSaleDocument document;
    private HashMap<Integer, PreOrderSaleDocumentItemView> viewMap;


    private PreOrderSaleDocumentCalculator() {
    }

    public static PreOrderSaleDocumentCalculator getInstance() {
        if (instance == null) {
            instance = new PreOrderSaleDocumentCalculator();
        }
        return instance;
    }


    public boolean calculate(PreOrderSaleDocument document, PreOrderSaleDocumentView documentView, List<PreOrderSaleDocumentItemView> viewList) {
        if (document == null) {
            return false;
        }
        // Подготовим мапу, что бы дергать данные по ID записи
        prepareProductionMap(viewList);

        document.setSumPriceCurrencyValue(0);
        document.setSumVatCurrencyValue(0);
        document.setSumPriceVatCurrencyValue(0);
        document.setSumPriceValue(0);
        document.setSumVatValue(0);
        document.setSumPriceVatValue(0);
        document.setAmount(0);

        this.document = document;

        int currencyId = document.getCurrencyId();
        // Флаг валюты
        boolean isCurrency = currencyId > 1;
        // Флаг дополнительного понижения курса
        boolean isRateCoefficient = (currencyId == 2 || currencyId == 5);

        double rateCommon = document.getCurrencyRateCommon();
        double rateAddition = document.getCurrencyRateAddition();

        if (isRateCoefficient) {
            rateCommon /= 100;
            rateAddition /= 100;
        }

        // Документ с несортовым товаром
        boolean isNotVarietal = document.getGradeMarkdownType() > 0;
        // Документ с торговой надбавкой
        boolean isTradeAllowance = document.getTradeAllowanceType() > 0;
        // Флаг наличия спец цены для контрагента
        boolean isSpecialPrice = false;

        SpecialPriceService specialPriceService = null;
        Reduction3GradeService gradeService = null;
        RemainsReductionService allowanceService = null;
        // Для валютнного долкумента подключим сервис спеццен
        if (isCurrency) {
            specialPriceService = SpecialPriceService.getInstance();
            // Перед расчетом проверим есть ли по контрагенту спец цены
            isSpecialPrice = specialPriceService.isHaveSpecialPrice(documentView.getContractorCode());
        }

        // Для документа с несортом подключим сервис учета уценок
        if (isNotVarietal) {
            gradeService = new Reduction3GradeService(CurrencyType.RUB);
        }

        // Для расчета торговой надбавки подключим службу уценок
        if (isTradeAllowance) {
            allowanceService = RemainsReductionService.getInstance();
        }

        for (PreOrderSaleDocumentItem item : document.getSpecification()) {
            PreOrderSaleDocumentItemView itemView = viewMap.get(item.getId());
            // Предустановим учутную цену если 0
            item.setAccountingPrice(itemView.getClassifierPrice());
            item.setVat(itemView.getClassifierVat());
            int amount = (int) item.getAmount();
            // Узнаем валюту расчета, в приоритете иностранная, т.к цена в бел. рублях расчитывается
            // на основании цены в валюте за единицу
            double price = -1;
            if (isCurrency) {

                int specialPriceMarker = 0;
                // Просмотр спец цен для контрагента
                if (isSpecialPrice) {
                    double price_ = specialPriceService.getSpecialPriceByCriteria(documentView.getContractorCode(),
                            itemView.getModelNumberAsString(), itemView.getArticleNumber(),
                            itemView.getItemGrade(), itemView.getItemSize(), itemView.getItemGrowth());
                    if (price_ > 0) {
                        price = price_;
                        specialPriceMarker = 1;
                    }
                }

                // Просмотр общих спец цен
                double price_ = specialPriceService.getGeneralSpecialPriceByCriteria(itemView.getModelNumberAsString(),
                        itemView.getArticleNumber(),
                        itemView.getItemGrade(), itemView.getItemSize(), itemView.getItemGrowth());
                if (price_ > 0) {
                    price = price_;
                    specialPriceMarker = 2;
                }

                // Просмотр уценки ??????

                // Выводим итоговую цену единицы изделия для последующего расчета
                if (price < 0) {

                    float discount = 0;
                    if (document.getDiscountType() == 3) {
                        discount = (float) item.getDiscount();
                        if (discount < 1) {
                            discount = (float) document.getDiscountValue();
                            item.setDiscount(discount);
                        }
                    } else {
                        item.setDiscount(document.getDiscountValue());
                        discount = (float) document.getDiscountValue();
                    }

                    price = SaleDocumentCalculator.round(calculateCurrencyPriceFromPriceValue(
                            // Получаем цену в рублях с учетом скидки
                            calculatePriceAndDiscountValue(
                                    // Учетная оптовая цена
                                    item.getAccountingPrice(),
                                    // Процент скидки по документу
                                    discount, 1),
                            //Курс на начало месяца
                            (float) rateCommon), SaleDocumentCalculator.ROUND_XXX_XX);
                    specialPriceMarker = 0;
                }

                item.setSpecialPrice(specialPriceMarker);
                item.setCostCurrency(price);
                item.setSumCostCurrency(round(calculateSumValue(item.getCostCurrency(), amount), ROUND_XXX_XX));
                item.setSumVatCurrency(SaleDocumentCalculator.round(
                        calculateVatSumValue(item.getSumCostCurrency(), document.getVatValue()),
                        SaleDocumentCalculator.ROUND_XXX_XX));
                item.setSumCostVatCurrency(calculateTotalValue(item.getSumCostCurrency(), item.getSumVatCurrency()));

                document.setSumPriceCurrencyValue(document.getSumPriceCurrencyValue() + item.getSumCostCurrency());
                document.setSumVatCurrencyValue(document.getSumVatCurrencyValue() + item.getSumVatCurrency());
                document.setSumPriceVatCurrencyValue(document.getSumPriceVatCurrencyValue() + item.getSumCostVatCurrency());

            } else {
                item.setCostCurrency(0);
                item.setSumCostCurrency(0);
                item.setSumVatCurrency(0);
                item.setSumCostVatCurrency(0);

                document.setSumPriceCurrencyValue(document.getSumPriceCurrencyValue() + item.getSumCostCurrency());
                document.setSumVatCurrencyValue(document.getSumVatCurrencyValue() + item.getSumVatCurrency());
                document.setSumPriceVatCurrencyValue(document.getSumPriceVatCurrencyValue() + item.getSumCostVatCurrency());
            }

            // Счталась валюта, цену в BYN считаем через валютную курсом ADDITION
            if (price >= 0) {
                price = round(calculatePriceFromCurrencyPriceValue(item.getCostCurrency(), (float) rateAddition), ROUND_XXX_XX);
            } else {

                float discount = 0;
                if (document.getDiscountType() == 3) {
                    discount = (float) item.getDiscount();
                } else {
                    item.setDiscount(document.getDiscountValue());

                    discount = (float) document.getDiscountValue();
                }

                price = SaleDocumentCalculator.round(
                        // Получаем цену в рублях с учетом скидки
                        calculatePriceAndDiscountValue(
                                // Учетная оптовая цена
                                item.getAccountingPrice(),
                                // Процент скидки по документу
                                discount, 1)
                        , SaleDocumentCalculator.ROUND_XXX_XX);
            }
            item.setCost(price);
            item.setSumCost(round(calculateSumValue(item.getCost(), amount), ROUND_XXX_XX));
            item.setSumVat(SaleDocumentCalculator.round(
                    calculateVatSumValue(item.getSumCost(), document.getVatValue()),
                    SaleDocumentCalculator.ROUND_XXX_XX));
            item.setVat(document.getVatValue());
            item.setSumCostVat(calculateTotalValue(item.getSumCost(), item.getSumVat()));
            // Если есть Торговая надбавка
            if (isTradeAllowance) {
                // Торговая надбавка фиксирована и не зависит от уценки ТН
                if (document.getTradeAllowanceType() == 1) {
                    RetailValue retail = getPriceAndTradeMarkupValueNew(
                            (float) item.getCost(),
                            (float) document.getTradeAllowanceValue(),
                            (float) document.getVatValue(),
                            amount);
                    item.setTradeAllowance(retail.getValueCostRetail());
                } else {
                    ProductionItem product = new ProductionItem();
                    product.setArticleName(itemView.getArticleNumber());
                    product.setArticleCode(Integer.valueOf(itemView.getCategory()));
                    double tradeMarkup = allowanceService.getTradeMarkupByArticleAndSize(product, itemView.getItemSize(), (int) document.getTradeAllowanceValue());
                    RetailValue retail = getPriceAndTradeMarkupValueNew(
                            (float) item.getCost(),
                            (float) tradeMarkup,
                            (float) document.getVatValue(),
                            amount);
                    item.setTradeAllowance(retail.getValueCostRetail());
                }
            } else {
                item.setRetailPrice(0);
            }

            document.setSumPriceValue(document.getSumPriceValue() + item.getSumCost());
            document.setSumVatValue(document.getSumVatValue() + item.getSumVat());
            document.setSumPriceVatValue(document.getSumPriceVatValue() + item.getSumCostVat());
            document.setAmount(document.getAmount() + item.getAmount());

        }
        return true;
    }


    private void prepareProductionMap(List<PreOrderSaleDocumentItemView> list) {
        viewMap = new HashMap<>();
        for (PreOrderSaleDocumentItemView item : list) {
            viewMap.put(item.getId(), item);
        }
    }

}
