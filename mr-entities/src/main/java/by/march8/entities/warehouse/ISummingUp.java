package by.march8.entities.warehouse;

/**
 * @author Andy 18.05.2016.
 */
public interface ISummingUp {

    /**
     * Возвращает сумму изделий в рублях
     *
     * @return сумма в рублях
     */
    double getValueSumCost();

    /**
     * Возвращает сумму НДС
     *
     * @return сумма НДС
     */
    double getValueSumVat();

    /**
     * ВОзвращает сумму с НДС в рублях
     *
     * @return сумма с НДС в рублях
     */
    double getValueSumCostAndVat();

    /**
     * Возвращает сумму изделий в валюте
     *
     * @return сумма в валюте
     */
    double getValueSumCostCurrency();

    /**
     * Возвращает сумму НДС в валюте
     *
     * @return сумма НДС в валюте
     */
    double getValueSumVatCurrency();

    /**
     * Возвращает сумму с НДС в валюте
     *
     * @return сумма с НДС в валюте
     */
    double getValueSumCostAndVatCurrency();

    /**
     * Возвращает ставку НДС для изделия
     *
     * @return ставка НДС
     */
    double getValueVat();

    /**
     * Возвращает ставку торговой надбавки
     *
     * @return ставка торговой надбавки
     */
    double getValueAllowanceRate();

    /**
     * Возвращает стоимость изделия без НДС
     *
     * @return стоимость изделия без НДС
     */
    double getValueCost();

    double getTradeMarkup();

    /**
     * Возвращает структуру расчета торговой надбавки
     *
     * @return структура торговой надбавки
     */
    RetailValue getRetailValue();

    /**
     * Возвращает структуру расчета скидок
     *
     * @return структура расчета скидок
     */
    PriceListValue getPriceListValue();

    /**
     * Количество изделий, полное
     *
     * @return количество
     */
    double getTotalAmount();

    /**
     * Возвращает учетную цену для изделия в прейскуранте
     *
     * @return цена изделия в прейскуранте
     */
    double getAccountingPrice();

    /**
     * Возвращает номер модели
     * @return номер модели, строка
     */
    String getModelNumberAsString();

    int getItemSize();

    String getArticleName();

}
