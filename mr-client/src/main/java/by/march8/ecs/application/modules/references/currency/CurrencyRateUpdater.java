package by.march8.ecs.application.modules.references.currency;

import by.gomel.freedev.ucframework.ucswing.dialog.DateSelectorDialog;
import by.march8.ecs.MainController;
import curen.Getter;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Andy 20.03.2016.
 */
public class CurrencyRateUpdater {

    public CurrencyRateUpdater(final MainController mainController) {

        final DateSelectorDialog dateSelectorDialog = new DateSelectorDialog(mainController, "Обновление курса валюты");
        Date d = dateSelectorDialog.selectDate(new Date());

        if (d != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH) + 1;
            int year = c.get(Calendar.YEAR);

            String dayS = String.valueOf(day);

            String monthS = String.valueOf(month);

            if (day < 10) {
                dayS = "0" + dayS;
            }
            if (month < 10) {
                monthS = "0" + monthS;
            }

            String[] args = {dayS, monthS, String.valueOf(year)};
            //System.out.println(Arrays.toString(args));
            //new RateGetter(args);
            try {
                new Getter(args);
            } catch (IOException ex) {
                System.out.println("Error in method updateCurrencyRate in class CurrencyRateMonitor");
            }
        }
    }
}
