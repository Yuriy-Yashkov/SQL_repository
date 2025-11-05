package by.march8.ecs.services.rationing;

import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.ecs.services.IService;
import dept.production.zsh.spec.SpecPDB;
import dept.production.zsh.spec.SpecificationItem;

import java.util.HashMap;
import java.util.List;

/**
 * @author Andy 01.12.2021 - 10:01.
 */
public class RationingService implements IService {

    private static RationingService instance = null;
    private static HashMap<Integer, Double> map = null;

    private RationingService() {
        if (map == null) {
            System.out.println("Инициализация службы нормирования...");
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

            Task task = new Task("Запуск службы нормирования...");
            task.executeTask();
            try {
                map = (HashMap<Integer, Double>) task.getResultObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static RationingService getInstance() {
        if (instance == null) {
            instance = new RationingService();
        }
        return instance;
    }

    private HashMap<Integer, Double> mapInitializer() {
        HashMap<Integer, Double> result = new HashMap<>();

        SpecPDB db = new SpecPDB();
        try {
            List<SpecificationItem> list = db.getAllSpecifications();
            if (list != null) {
                if (list.size() > 0) {
                    System.out.println("В списке " + list.size());
                    for (SpecificationItem s : list) {
                        result.put(s.modelNumber, s.rating);
                    }
                    System.out.println("В мапе " + result.size());
                }
            }
        } catch (Exception e) {

        }
        return result;
    }

    public double getModelRationing(int modelNumber) {
        if (map == null) {
            return 0;
        } else {
            Double get_ = map.get(modelNumber);
            if (get_ == null) {
                return 0;
            } else {
                return map.get(modelNumber);
            }
        }
    }

}
