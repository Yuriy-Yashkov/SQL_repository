package by.march8.ecs.application.modules.terminal;

import by.march8.api.BaseEntity;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.terminal.dao.TerminalJDBC;
import by.march8.ecs.application.modules.terminal.model.BaseTerminalItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy 22.10.2018 - 14:20.
 */

/*
        9000178647  ,1,0000164
        9000178647  ,1,0000164
        9000178647  ,1,0000164
        0001798380  ,1,0000158
        0001798380  ,1,0000158
        0001798380  ,1,0000158
        0001798380  ,1,0000158
        0001798380  ,1,0000158
        005013035472,2,0000589
        005013035458,2,0000593
        005013035451,2,0000586
        9000178647  ,1,0000164
        005012573515,2,0000593
        005013035569,2,0000596
        9000178647  ,1,0000164
        005013035545,2,0000590
*/


public class TerminalHandler {

    private List<BaseTerminalItem> itemList;
    private List<String> wrongList = new ArrayList<>();
    private MainController controller;

    /**
     * Инициализация обработчика списком из терминала сбора данных
     *
     * @param res
     */
    public TerminalHandler(MainController controller, String[] res) {
        this.controller = controller;
        itemList = prepareItemList(res);



        /*for(BaseTerminalItem item:prepareItemList(res)){
            System.out.println(item.toString());
        }*/
    }

    public static boolean checkControlSum(String[] res) {
        for (String s : res) {
            if (!s.substring(s.length() - 2, s.length())
                    .equals(getControlSum(s.substring(0, s.length() - 2)))) {
                return false;
            }
        }
        return true;
    }

    private static String getControlSum(String input) {

        String out = null;
        int sum = 0;

        for (int i = 0; i < input.length(); i++) {
            sum += (int) input.charAt(i);
        }

        String _out = String.valueOf(sum);

        if (_out.length() > 2) {
            out = _out.substring(_out.length() - 2);
        } else {
            out = _out.substring(0, 1);
        }

        return out;
    }

    // Формирование списка изделий для последующей обработки
    private List<BaseTerminalItem> prepareItemList(String[] array) {
        List<BaseTerminalItem> result = new ArrayList<>();
        if (array != null) {
            for (String s : array) {
                BaseTerminalItem item = prepareItem(s);
                if (item != null) {
                    // Добавляем код к последующей обработке
                    result.add(item);
                } else {
                    // Пишем сюда, что не могли распарсить код с терминала
                    wrongList.add(s);
                }
            }
        }
        return result;
    }

    /**
     * Парсер кодов КОД, КОЛИЧЕСТВО, ТИП(1-штучная этикетка 2-Упаковочная этикетка)
     */

    private BaseTerminalItem prepareItem(String item) {
        if (item != null) {
            String[] tempItem = item.split(",");
            if (tempItem.length == 3) {
                try {
                    int type = Integer.valueOf(tempItem[1]);
                    BaseTerminalItem result = new BaseTerminalItem();

                    if (type == 1) {
                        result.setBarCode(Long.valueOf(tempItem[0].trim()));
                        result.setAmount(Integer.valueOf(tempItem[2].substring(0, tempItem[2].length() - 2)));
                    }

                    if (type == 2) {
                        String bar = tempItem[0].trim();
                        result.setBarCode(Long.valueOf(bar.substring(3, bar.length())));
                        result.setAmount(1);
                    }


                    result.setPack(type);
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }

    // Подсчет контрольной суммы для подтверждения корректности передачи данных
    private boolean checkSumDone() {
        return true;
    }

    // Сохранение данных в ЮЗЕРСКАН ????
    public boolean saveToDocument(BaseEntity document) {
        // Проверки перед выполнением
        if (itemList == null) {
            return false;
        }

        if (itemList.size() < 1) {
            return false;
        }

        if (document == null) {
            return false;
        }

        // Удаляем из юзерскана все данные для пользователя
        String userName = controller.getWorkSession().getUser().getUserLogin();
        TerminalJDBC jdbc = new TerminalJDBC();
        // Удаление данных терминала для пользователя
        jdbc.deleteUserScanForUserName(userName);
        // Узнаем, сколько раз добавляли данные в накладную, и инкрементируем
        int partCount = jdbc.getInsertCountByDocumentId(document.getId());
        partCount++;
        System.out.println(partCount);
        //Сохраняем данные сканирования в таблицу USER_SCAN_EAN
        jdbc.saveTerminalData(document.getId(), itemList, userName);
        // Вызов процедуры UPDATE_USER_SCAN_EAN
        jdbc.updateUserScan(userName);
        // Сохраняем обработанные записи в OTGRUZ2
        jdbc.saveUploadedData(userName, partCount);
        // Заполнение документа недостающими данными
        jdbc.updateDocumentData(document.getId());
        return true;
    }

    // Удалиение данных из отгрузки через сканер

}
