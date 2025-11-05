package by.march8.tasks.remains.dao;

import by.march8.tasks.remains.logic.RemainsDBFItem;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RemainsDBFReader {

    public List<RemainsDBFItem> readRemainsDBF(String path) {
        Object[][] colName = {{"MODEL", ""}, {"ARTICLE", ""}, {"EANCOD", ""}, {"BALANCE", ""}};

        List<RemainsDBFItem> result = new ArrayList<>();
        DBFField field = null;
        try {
            InputStream inputStream = new FileInputStream(path);
            DBFReader reader = new com.linuxense.javadbf.DBFReader(inputStream);
            reader.setCharactersetName("cp866");
            System.out.println("Количество строк в документе :" + reader.getRecordCount());

            for (int i = 0; i < colName.length; i++) {
                for (int j = 0; j < reader.getFieldCount(); j++) {
                    field = reader.getField(j);
                    if (field.getName().trim().toUpperCase().equals(colName[i][0]))
                        colName[i][1] = j;
                }
                if (colName[i][1].equals(""))
                    JOptionPane.showMessageDialog(null, "Ошибка: в dbf-файле не найден столбец " + colName[i][0], "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            Object obj[];
            for (int i = 0; i < reader.getRecordCount(); i++) {
                obj = reader.nextRecord();

                RemainsDBFItem item = new RemainsDBFItem();

                int modelNumber = 0;
                if (obj[0] != null) {
                    try {
                        modelNumber = Integer.valueOf(obj[0].toString().trim());
                    } catch (Exception ex) {
                        modelNumber = -1;
                    }
                }
                item.setModelNumber(modelNumber);

                float amount = 0;
                if (obj[3] != null) {
                    try {
                        amount = Float.valueOf(obj[3].toString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        amount = -1;
                    }
                }
                item.setAmount((int) amount);

                item.setArticleNumber(obj[1].toString().trim());
                item.setEanCode(obj[2].toString().trim());

                result.add(item);
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
