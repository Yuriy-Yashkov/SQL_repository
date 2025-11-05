package by.march8.ecs.services.eancode;

import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.SaleDocumentJDBC;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.ecs.services.IService;

import java.util.HashMap;
import java.util.List;

/**
 * @author Andy 04.06.2018 - 8:37.
 */
public class EanCodeControlService implements IService {
    private static EanCodeControlService instance = null;
    private static HashMap<String, Integer> map = null;

    private MainController controller;

    private EanCodeControlService() {
        if (map == null) {

            System.out.println("Инициализация службы контроля EAN13 кодов...");
            class Task extends BackgroundTask {
                public Task(final String messageText) {
                    super(messageText);
                }

                @Override
                protected Object doInBackground() throws Exception {
                    resultObject = mapInitializer();
                    return resultObject;
                }
            }

            Task task = new Task("Запуск службы EAN13...");
            task.executeTask();
            try {
                map = (HashMap<String, Integer>) task.getResultObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static EanCodeControlService getInstance() {
        if (instance == null) {
            instance = new EanCodeControlService();
        }
        return instance;
    }

    private HashMap<String, Integer> mapInitializer() {
        HashMap<String, Integer> result = new HashMap<>();

        SaleDocumentJDBC db = new SaleDocumentJDBC();

        List<String> list = db.getAllEanCode();
        if (list != null) {
            if (list.size() > 0) {
                System.out.println("В списке " + list.size());
                for (String s : list) {
                    Integer get_ = result.get(s);
                    if (get_ != null) {
                        get_ += 1;
                        result.put(s, get_);
                    } else {
                        result.put(s, 1);
                    }
                }
                System.out.println("В мапе " + result.size());
            }
        }
        return result;
    }

    public boolean isEmptyMap() {
        if (map.size() < 1) {
            return true;
        } else {
            return false;
        }
    }

    public int getAmountEanCode(String ean) {
        if (map == null) {
            return 0;
        } else {
            Integer get_ = map.get(ean);
            if (get_ == null) {
                return 0;
            } else {
                return map.get(ean);
            }
        }
    }

    public HashMap<String, Integer> getUniqueEanMap() {
        return map;
    }


}
