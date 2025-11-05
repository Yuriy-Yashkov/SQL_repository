package by.march8.ecs.application.modules.warehouse.external.shipping.services;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.ProductionItem;
import by.march8.ecs.services.IService;
import by.march8.entities.classifier.RemainsReductionItem;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;

import java.sql.SQLException;
import java.util.List;

/**
 * Сервис контроля и управления остатаками уцененных изделий для фирменных магазинов
 *
 * @author Andy 06.09.2017.
 */
public class RemainsReductionService implements IService {

    private static RemainsReductionService instance = null;
    private static List<RemainsReductionItem> list = null;

    private RemainsReductionService() {
        System.out.println("Инициализация службы уценок остатков...");
    }

    /**
     * Возвращает сервис УценкаИздели
     *
     * @return сервис торговых надбавок
     */
    public static RemainsReductionService getInstance() {
        if (instance == null) {
            instance = new RemainsReductionService();
        }
        return instance;
    }

    /**
     * Метод возвращает список торговых надбавок
     *
     * @return список торговых надбавок
     */
    public List<RemainsReductionItem> getRemainsReductionItemList() {
        if (list == null) {
            DaoFactory<RemainsReductionItem> factory = DaoFactory.getInstance();
            IGenericDao<RemainsReductionItem> dao = factory.getGenericDao();

            try {
                list = dao.getAllEntity(RemainsReductionItem.class);
                System.out.println("Загружено [" + list.size() + "] записей о уценках остатков.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * Метод проверяет, есть ли для изделия с конкретным артикулом, размером, и торговой надбавкой в прейскурантах уценки
     *
     * @param articleNumber артикул изделия
     * @param sizeItem      размер изделия
     * @param tradeMarkup   размер торговой надбавки для сравнения
     * @return true если изделие по условию найдено в прейскурантах уценки
     */
    public int isHaveReductionByArticleNumberAndSize(String articleNumber, int sizeItem, int tradeMarkup) {
        int allowance = -1;
        // Если ранее не обращались к сервису
        if (list == null) {
            // Получаем спецификации уценок остатков
            getRemainsReductionItemList();
        }
        // Проверка, соблюдены ли условия поиска по изделию
        if (list != null) {
            for (RemainsReductionItem item : list) {
                //Находим нужный артикул
                if (item.getArticleNumber().equals(articleNumber)) {
                    //System.out.println("Найден артикул");
                    //Находим нужный размер
                    //System.out.println("Размер "+sizeItem);
                    if (sizeItem >= item.getMinimumSize() & sizeItem <= item.getMaximumSize()) {
                        //System.out.println("Найден размер");
                        //находим нужную торговую надбавку
                        if (tradeMarkup != item.getTradeMarkupValue()) {
                            //System.out.println("Найдена уценка "+ tradeMarkup + " - "+item.getTradeMarkupValue());
                            allowance = item.getTradeMarkupValue();
                        }
                    }
                }
            }
        }
        return allowance;
    }


    public double getTradeMarkupByArticleAndSize(SaleDocumentDetailItemReport articleName, int sizeItem, int tradeMarkup) {
        int allowance = tradeMarkup;
        // Если ранее не обращались к сервису
        if (list == null) {
            // Получаем спецификации уценок остатков
            getRemainsReductionItemList();
        }
        // Проверка, соблюдены ли условия поиска по изделию
        if (list != null) {
            for (RemainsReductionItem item : list) {
                //Находим нужный артикул
                if (item.getArticleNumber().equals(articleName.getArticleName())) {
                    //System.out.println("Найден артикул");
                    //Находим нужный размер
                    //System.out.println("Размер "+sizeItem);
                    if (sizeItem >= item.getMinimumSize() & sizeItem <= item.getMaximumSize()) {
                        //System.out.println("Найден размер");
                        //находим нужную торговую надбавку
                        if (item.getTradeMarkupValue() >= 0) {
                            if (tradeMarkup != item.getTradeMarkupValue()) {
                                //System.out.println("Найдена уценка "+ tradeMarkup + " - "+item.getTradeMarkupValue());
                                return item.getTradeMarkupValue();
                            }
                        }
                    }
                }
            }
        }

        // Дополнительный поиск в черном/меплом списке для правил отличных от документа
        // описывающего уценку изделия планово-экономического отдела

        // TODO Облагородить по случаю для добавления правил формирования торговых надбавок

        // Правило: артикула 5С чулочно-носочный кроме 5С60 и 5С80 т/н составляет 10%
        if (articleName.getArticleCode().startsWith("43")) {
            //Сначала четный список
            if (articleName.getArticleName().startsWith("5С60") || articleName.getArticleName().startsWith("5С80")) {
                return 30;
            } else if (articleName.getArticleName().startsWith("5С")) {
                return 10;
            } else if (articleName.getArticleName().startsWith("8С129")) {
                return 5;
            }
        }
        if (articleName.getArticleCode().startsWith("4589")) {
            //ТН повязки
            return 15;
        }

        return allowance;
    }

    public double getTradeMarkupByArticleAndSize(final ProductionItem productionItem, int sizeItem, int tradeMarkup) {
        int allowance = tradeMarkup;
        // Если ранее не обращались к сервису
        if (list == null) {
            // Получаем спецификации уценок остатков
            getRemainsReductionItemList();
        }
        // Проверка, соблюдены ли условия поиска по изделию
        if (list != null) {
            for (RemainsReductionItem item : list) {
                //Находим нужный артикул
                if (item.getArticleNumber().trim().equals(productionItem.getArticleName())) {
                    //System.out.println("Найден артикул");
                    //Находим нужный размер
                    //System.out.println("Размер "+sizeItem);
                    if (sizeItem >= item.getMinimumSize() & sizeItem <= item.getMaximumSize()) {
                        //System.out.println("Найден размер");
                        //находим нужную торговую надбавку
                        if (item.getTradeMarkupValue() >= 0) {
                            if (tradeMarkup != item.getTradeMarkupValue()) {
                                //System.out.println("Найдена уценка "+ tradeMarkup + " - "+item.getTradeMarkupValue());
                                return item.getTradeMarkupValue();
                            }
                        }
                    }
                }
            }
        }

        // Дополнительный поиск в черном/меплом списке для правил отличных от документа
        // описывающего уценку изделия планово-экономического отдела

        // TODO Облагородить по случаю для добавления правил формирования торговых надбавок

        // Правило: артикула 5С чулочно-носочный кроме 5С60 и 5С80 т/н составляет 10%
        if (productionItem.getArticleCodeAsString().startsWith("43")) {
            //Сначала четный список
            if (productionItem.getArticleName().startsWith("5С60") || productionItem.getArticleName().startsWith("5С80")) {
                return 30;
            } else if (productionItem.getArticleName().startsWith("5С")) {
                return 10;
            } else if (productionItem.getArticleName().startsWith("8С129")) {
                return 5;
            }
        }
        if (productionItem.getArticleCodeAsString().startsWith("4589")) {
            //ТН повязки
            return 15;
        }
        return allowance;
    }
}
