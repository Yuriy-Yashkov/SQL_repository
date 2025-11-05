package by.march8.ecs.application.modules.warehouse.external.shipping.services;

import by.gomel.freedev.ucframework.ucdao.jdbc.DocumentJDBC;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.Reduction3Grade;
import by.march8.ecs.framework.helpers.digits.CurrencyType;
import by.march8.ecs.services.IService;

import java.util.List;

/**
 * Служба контролирует цену не сортовых изделий и возвращает для ID otgruz2 оптовую(учетную) цену 1-го сорта
 *
 * @author Andy
 */
public class Reduction3GradeService implements IService {

    private List<Reduction3Grade> list = null;
    private CurrencyType currency = null;

    /**
     * Конструктор сервиса несортных цен
     * @param currencyType тип валюты, на перспективу
     */
    public Reduction3GradeService(final CurrencyType currencyType) {
        currency = currencyType;
    }

    public Reduction3GradeService() {
    }

    /**
     * Получение цены 1-го сорта для изделий несортовой категории (3,4)
     * @param documentId идентификатор документа
     * @return список цен
     */
    public List<Reduction3Grade> getPriceList(int documentId) {
        if (list == null) {
            DocumentJDBC db = new DocumentJDBC();
            list = db.getWholesalePriceByDocumentId(documentId);
            System.out.println("Получение цен первого сорта на продукцию для документа с ID " + documentId);
        }

        return list;
    }

    /**
     * Получение цены изделия(заранее посчитанной) для экспортных документов
     * @param documentId id документа
     * @return список цен
     */
    public List<Reduction3Grade> getPriceListCurrency(int documentId) {
        if (list == null) {
            DocumentJDBC db = new DocumentJDBC();
            list = db.getCurrencyPriceByDocumentId(documentId);
            System.out.println("Получение цены в росс. рублях на продукцию для документа с ID " + documentId);
        }

        return list;
    }

    /**
     * Метод возвращает учетную цену первого сорта для позиции спецификации по ID записи otgruz2
     *
     * @param itemId     идентификатор записи
     * @param documentId идентификатор документа
     * @return значение учетной цены первого сорта
     */
    public Reduction3Grade getTopGradeByItemId(int documentId, int itemId) {

        if (list == null) {
            if (currency == null) {
                getPriceList(documentId);
            } else {
                getPriceListCurrency(documentId);
            }
        }

        if (list != null) {
            for (Reduction3Grade item : list) {
                if (item.getId() == itemId) {
                    if (currency == null) {
                        System.out.println("Цена для изделия с id " + itemId + " найдена: " + item.getPrice());
                    } else {
                        System.out.println("Цена для изделия с id " + itemId + " найдена: " + item.getPriceCurrency());
                    }
                    return item;
                }
            }
        }

        System.out.println("Цена для изделия с id " + itemId + " не найдена: -1");
        Reduction3Grade empty = new Reduction3Grade();
        empty.setId(itemId);
        empty.setPrice(-1);
        empty.setPriceCurrency(-1);
        return empty;
    }
}
