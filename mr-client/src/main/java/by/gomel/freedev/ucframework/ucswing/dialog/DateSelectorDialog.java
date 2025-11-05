package by.gomel.freedev.ucframework.ucswing.dialog;

/**
 * @author Andy 20.03.2016.
 */

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ButtonActionAdapter;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import org.jdesktop.swingx.JXMonthView;

import java.awt.*;
import java.util.Date;

/**
 * Форма выбора даты, т.к. пока нигде вне данного режима не используется,
 * будет приватным классом
 * //TODO переместить в отдельный класс как только возникнет необходимость в этом
 */
public class DateSelectorDialog extends BasePickDialog {

    private JXMonthView calendar;
    private boolean customView = false;

    public DateSelectorDialog(final MainController controller) {
        super(controller);
        setTitle("Укажите дату отгрузки");

        init();
    }

    public DateSelectorDialog(final MainController controller, String titleText) {
        super(controller);
        setTitle(titleText);
        customView = true;
        init();
    }

    private void init() {
        setFrameSize(new Dimension(370, 280));
        calendar = new JXMonthView(new Date());
        calendar.setPreferredColumnCount(2);
        calendar.setPreferredRowCount(1);
        calendar.setTraversable(true);
        getToolBar().setVisible(false);

        getCenterContentPanel().add(calendar);
        setButtonAction(new ButtonActionAdapter() {
            @Override
            public boolean canSave() {
                return calendar.getSelectionDate() != null;
            }

            @Override
            public boolean canCancel() {
                calendar.setSelectionDate(null);
                return true;
            }
        });
    }

    /**
     * Метод показывает диалог выбора даты
     *
     * @param date предустановка для выбора даты
     * @return выбранная дата
     */
    public Date selectDate(Date date) {

        if (!customView) {
            if (date == null) {
                calendar.setSelectionDate(new Date());
                setTitle("Дата отгрузки ( не установлена )");
            } else {
                calendar.setSelectionDate(date);
                calendar.ensureDateVisible(date);
                setTitle("Дата отгрузки (" + DateUtils.getNormalDateFormat(date) + ")");
            }
        }

        showModalFrame();
        return calendar.getSelectionDate();
    }

    public Date selectCustomDate(Date date) {

        if (!customView) {
            if (date == null) {
                calendar.setSelectionDate(new Date());
                //setTitle("Дата отгрузки ( не установлена )");
            } else {
                calendar.setSelectionDate(date);
                //setTitle("Дата отгрузки (" + DateUtils.getNormalDateFormat(date) + ")");
            }
        }

        showModalFrame();
        return calendar.getSelectionDate();
    }

}