package by.march8.ecs.application.modules.warehouse.external.shipping.services;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.march8.ecs.services.IService;
import by.march8.entities.warehouse.SpecialPrice;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Сервис управляет специальными цена на изделие
 *
 * @author Andy 31.05.2016.
 */
public class SpecialPriceService implements IService {

    private static SpecialPriceService instance = null;
    private static List<SpecialPrice> list = null;

    private SpecialPriceService() {

    }

    /**
     * Возвращает менеджер сертификатов приложения
     *
     * @return менеджер сертификатов
     */
    public static SpecialPriceService getInstance() {
        if (instance == null) {
            instance = new SpecialPriceService();
        }
        return instance;
    }

    public List<SpecialPrice> getPriceList() {
        //System.out.println("Получение сертификатов на продукцию....");
        if (list == null) {
            DaoFactory<SpecialPrice> factory = DaoFactory.getInstance();
            IGenericDao<SpecialPrice> dao = factory.getGenericDao();
            try {
                list = dao.getAllEntity(SpecialPrice.class);
                System.out.println("Загружено [" + list.size() + "] записей о специальных ценах.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * Метод проверяет, есть ли по контрагенту какие либо спец цены.
     *
     * @param contractorCode код контрагента
     * @return флаг наличия специальных цен
     */
    public boolean isHaveSpecialPrice(int contractorCode) {
        // Если ранее не обращались к сервису
        if (list == null) {
            // ПОлучаем список спец. цен
            getPriceList();
        }
        // Проверка, есть ли по конт
        if (list != null) {
            for (SpecialPrice item : list) {
                if (item.getContractorCode() == contractorCode) {
                    return true;
                }
            }
        }
        return false;
    }

    public void updateService() {
        getPriceList();

        Collections.sort(list, new Comparator<SpecialPrice>() {
            public int compare(SpecialPrice a, SpecialPrice b) {
                return a.getId() < b.getId() ? 1 : (a.getId() == b.getId()) ? 0 : -1;
            }
        });
    }

    /**
     * Метод ищет изделие в списке по критериям и возвращает специальную цену для изделия.
     * Если специальная цена не установлена, возвращает значение -1
     *
     * @param contractorCode код контрагента
     * @param modelNumber    номер модели изделия
     * @param articleNumber  номер артикула изделия
     * @param grade          сорт изделия
     * @param size           размер изделия
     * @param growth         рост изделия
     * @return значение специальной цены
     */
    public double getSpecialPriceByCriteria(int contractorCode, String modelNumber, String articleNumber, int grade, int size, int growth) {
        if (list == null) {
            // ПОлдучаем список спец. цен
            getPriceList();
        }
//        System.out.println("["+contractorCode+"]"+"["+modelNumber+"]"+"["+articleNumber+"]"+"["+grade+"]"+"["+size+"]");
        // Проверка, есть ли по конт
        if (list != null) {
            for (SpecialPrice item : list) {
                // Найден контрагент
                if (item.getContractorCode() == contractorCode) {
                    //System.out.println("Найден контрагент");
                    // Найден номер модели
                    if (item.getModelNumber().trim().equals(modelNumber)) {
                        // Найден артикул
                        if (item.getArticleNumber().trim().equals(articleNumber)) {
                            //Найден сорт
                            if (item.getItemGrade() == grade) {
                                // Найден диапазон размеров,
                                // возвращаем значение специальной цены
                                if (size >= item.getSizeMinimum() && size <= item.getSizeMaximum()) {
                                    return item.getValuePrice();
                                }
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }

    public double getGeneralSpecialPriceByCriteria(String modelNumber, String articleNumber, int grade, int size, int growth) {
        if (list == null) {
            // ПОлдучаем список спец. цен
            getPriceList();
        }
        // System.out.println("["+contractorCode+"]"+"["+modelNumber+"]"+"["+articleNumber+"]"+"["+grade+"]"+"["+size+"]");
        // Проверка, есть ли по конт
        if (list != null) {
            System.out.println("Поиск в общих спецценах для критерия: " + modelNumber + " - " + articleNumber + " - " + grade + " - " + size);
            for (SpecialPrice item : list) {
                // Найден контрагент
                if (item.isGeneralPrice()) {
                    // Найден номер модели
                    if (item.getModelNumber().trim().equals(modelNumber)) {
                        // Найден артикул
                        if (item.getArticleNumber().trim().equals(articleNumber)) {
                            //Найден сорт
                            if (item.getItemGrade() == grade) {
                                // Найден диапазон размеров,
                                // возвращаем значение специальной цены
                                if (size >= item.getSizeMinimum() && size <= item.getSizeMaximum()) {
                                    return item.getValuePrice();
                                }
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }
}
