package dbf;

import by.march8.ecs.application.modules.art.dbf.ProductionReportDBF;
import by.march8.ecs.application.modules.art.model.ProductionReportData;
import by.march8.ecs.application.modules.art.model.ProductionReportItem;
import by.march8.ecs.application.modules.art.model.ProductionReportTotal;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * @author Andy 21.01.2017.
 */
public class TestProductionReport {

    @Test
    @Ignore
    public void testGetProductionList() {
        ProductionReportDBF dbf = new ProductionReportDBF();
        ProductionReportData dataReport = dbf.getProductReportItemList(12);
        List<ProductionReportItem> list = dataReport.getData();
        ProductionReportTotal total = dataReport.getTotal();
        System.out.println("Всего записей попало в карту: " + list.size());

        for (ProductionReportItem item : list) {
            System.out.println(item.toString());
        }

        System.out.println(total.toString());
    }

    private String getStringWithoutZero(String value) {
        int index = 0;
        while (value.substring(index, index + 1).equals("0")) {

            index++;
        }
        return value.substring(index, value.length());
    }

    /**
     * Метод принимает строку определенного формата ("302" или "1311")
     * и возвращает целочисленный массив из двух элементов,
     * в первой ячейке  номер дня (3 или 13)
     * во второй ячейке номер месяца (2 или 11)
     *
     * @param oldDate исходная строка даты
     * @return итоговый массив даты
     */
    private int[] getDateOldFormat(String oldDate) {
        int[] result = {0, 0};

        if (oldDate.length() > 3) {
            result[0] = Integer.valueOf(getStringWithoutZero(oldDate.substring(0, 2)));
            result[1] = Integer.valueOf(getStringWithoutZero(oldDate.substring(2, 4)));
        } else {
            result[0] = Integer.valueOf(getStringWithoutZero(oldDate.substring(0, 1)));
            result[1] = Integer.valueOf(getStringWithoutZero(oldDate.substring(1, 3)));
        }
        return result;
    }


    /**
     * Метод возвращает строку без "хвоста" полсе точки
     *
     * @param value исходная строка
     * @return итоговая строка
     */
    private String getStringWithoutPoint(String value) {
        int pointIndex = value.indexOf(".");
        if (pointIndex >= 0) {
            return value.substring(0, pointIndex);
        } else {
            return value;
        }
    }

    @Test
    @Ignore
    public void testZeroRemover() {
        //System.out.println(getStringWithoutZero("090"));
        //System.out.println(getStringWithoutZero("00000101"));
        //int[] datamatrix = getDateOldFormat("311");
        //System.out.println(datamatrix[0] + " - " + datamatrix[1]);

        System.out.println(getStringWithoutPoint("12344563456.4.56"));
    }
}
