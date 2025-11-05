package by.march8.ecs.application.modules.warehouse.external.shipping.services;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.march8.ecs.services.IService;
import by.march8.entities.warehouse.ResponsiblePersons;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Andy 12.12.2016.
 */
public class ResponsiblePersonsService implements IService {

    private static ResponsiblePersonsService instance = null;
    private List<ResponsiblePersons> list = null;
    private ArrayList<ResponsiblePersons> dataList[];

    private ResponsiblePersonsService() {

        dataList = new ArrayList[7];
        dataList[0] = new ArrayList<ResponsiblePersons>();
        dataList[1] = new ArrayList<ResponsiblePersons>();
        dataList[2] = new ArrayList<ResponsiblePersons>();
        dataList[3] = new ArrayList<ResponsiblePersons>();
        dataList[4] = new ArrayList<ResponsiblePersons>();
        dataList[5] = new ArrayList<ResponsiblePersons>();// Доверенность выдана
        dataList[6] = new ArrayList<ResponsiblePersons>();// Примечание

        updateAll();
    }

    public static ResponsiblePersonsService getInstance() {
        if (instance == null) {
            instance = new ResponsiblePersonsService();
        }
        return instance;
    }

    /**
     * Функция обновляет набор данных из БД
     */
    private void updateAll() {
        // Инициализация и очистка коллекции
        if (list == null) {
            list = new ArrayList<>();
        } else {
            list.clear();
        }
        // Запрос к БД на загрузку данных
        DaoFactory<ResponsiblePersons> factory = DaoFactory.getInstance();
        IGenericDao<ResponsiblePersons> dao = factory.getGenericDao();
        try {
            list = dao.getAllEntity(ResponsiblePersons.class);
            System.out.println("Загружено [" + list.size() + "] записей о ответственных лицах");

            // Сортировка записей по имени
            Collections.sort(list, (item2, item1) -> item2.getName().compareTo(item1.getName()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dataForming() {
        // Очистка массива коллекций
        dataList[0].clear();
        dataList[1].clear();
        dataList[2].clear();
        dataList[3].clear();
        dataList[4].clear();
        dataList[5].clear();
        dataList[6].clear();

        for (ResponsiblePersons person : list) {
            if (person != null) {
                dataList[person.getType()].add(person);
            }
        }
    }

    /**
     * Первичная инициализация службы
     */
    public void primaryInitialization() {
        dataForming();
    }

    public void updateService() {
        updateAll();
        dataForming();
    }

    public void loadComboBoxDate(final JComboBox comboBox, final int i, final boolean isExport) {
        String temp = comboBox.getEditor().getItem().toString().trim();

        ArrayList<String> array = new ArrayList<String>();
        for (ResponsiblePersons person : dataList[i]) {
            if (person != null) {

                switch (i) {
                    case 0:
                        if (isExport) {
                            if (person.isExport()) {
                                array.add(person.getOfficialPosition() + " " + person.getName());
                            }
                        } else {
                            if (!person.isExport()) {
                                array.add(person.getOfficialPosition() + " " + person.getName());
                            }
                        }
                        break;

                    case 3:
                        array.add(person.getName());
                        break;

                    case 4:
                        array.add(person.getName());
                        break;

                    case 5:
                        array.add(person.getName());
                        break;

                    case 6:
                        array.add(person.getName());
                        break;

                    default:
                        array.add(person.getOfficialPosition() + " " + person.getName());
                        break;
                }
            }

            comboBox.setModel(new DefaultComboBoxModel<>());
            comboBox.setModel(new DefaultComboBoxModel(array.toArray()));
            comboBox.getEditor().setItem(temp);
        }
    }

    public List<ResponsiblePersons> getList() {
        return list;
    }

    public int getTabelNumber(String findString) {
        for (ResponsiblePersons person : list) {
            if (findString.equals(person.getOfficialPosition() + " " + person.getName())) {
                return person.getTabelNumber();
            }
        }
        return 0;
    }
}


