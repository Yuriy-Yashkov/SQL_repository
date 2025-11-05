package date;

import by.march8.api.utils.DateUtils;
import org.junit.Test;

import java.util.Date;

public class TestDateFormat {

    @Test
    public void testParsePriceList() {
        System.out.println(DateUtils.getNormalDateFormat(parsePriceList("03.12.18")));
    }

    private Date parsePriceList(String priceList) {
        String date = "";
        if (priceList.length() > 9) {
            if (priceList.endsWith(".")) {
                date = priceList.substring(priceList.length() - 9, priceList.length() - 1);
            } else {
                date = priceList.substring(priceList.length() - 8, priceList.length());
            }

            return DateUtils.getDateByStringValueSimple(date);
        }

        return DateUtils.getDateByStringValue("01.01.0001");
    }
}
