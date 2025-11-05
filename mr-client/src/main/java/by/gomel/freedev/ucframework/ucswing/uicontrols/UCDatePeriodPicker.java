package by.gomel.freedev.ucframework.ucswing.uicontrols;


import by.march8.api.utils.DatePeriod;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.Date;

/**
 * Класс визуального компонента выбора календарного периода.
 * Представляет собой панель с расположенными на ней компонентами
 * календаря и текстовыми метками
 * Created by Andy on 13.11.2014.
 */
public class UCDatePeriodPicker extends JPanel {

    /**
     * Компонент даты начала периода
     */
    private UCDatePicker dpBegin;
    /**
     * Компонент даты окончания периода
     */
    private UCDatePicker dpEnd;


    /**
     * Конструктор
     */
    public UCDatePeriodPicker() {
        setLayout(null);

        setOpaque(false);
        setPreferredSize(new Dimension(240, 28));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        dpBegin = new UCDatePicker(calendar.getTime());
        dpEnd = new UCDatePicker(new Date());

        dpBegin.setToolTipText("Дата начала периода");
        dpEnd.setToolTipText("Дата конца периода");

        dpBegin.setBounds(0, 4, 110, 20);
        dpEnd.setBounds(120, 4, 110, 20);

        add(dpBegin);
        add(dpEnd);
    }

    /**
     * Возвращает дату начала периода
     *
     * @return начало периода
     */
    public Date getDatePickerBegin() {
        return dpBegin.getDate();
    }

    /**
     * Устанавливает дату начала периода
     *
     * @param datePickerBegin начало периода
     */
    public void setDatePickerBegin(final Date datePickerBegin) {
        this.dpBegin.setDate(datePickerBegin);
    }

    /**
     * Возвращает дату окончания периода
     *
     * @return окончания периода
     */
    public Date getDatePickerEnd() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dpEnd.getDate());
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        return cal.getTime();
    }

    /**
     * Устанавливает дату окончания периода
     *
     * @param datePickerEnd окончание периода
     */
    public void setDatePickerEnd(final Date datePickerEnd) {
        this.dpEnd.setDate(datePickerEnd);
    }

    /**
     * Метод добавляет событие на изменение периода в компоненте
     *
     * @param evt ссылка на событие
     */
    public void addOnChangeAction(ActionListener evt) {
        dpBegin.addActionListener(evt);
        dpEnd.addActionListener(evt);
    }

    /**
     * Активирует компонент
     *
     * @param active флаг активности компонента
     */
    public void setActive(final boolean active) {
        dpBegin.setEnabled(active);
        dpEnd.setEnabled(active);
    }

    /**
     * Метод активирует событие при появлении подсказки над компонентом
     *
     * @param listener ссылка на событие мыши
     */
    public void addHintListener(MouseListener listener) {
        dpBegin.addMouseListener(listener);
        dpEnd.addMouseListener(listener);

        dpBegin.getEditor().addMouseListener(listener);
        JButton openButton = (JButton) dpBegin.getComponent(1);
        openButton.addMouseListener(listener);

        dpEnd.getEditor().addMouseListener(listener);
        openButton = (JButton) dpEnd.getComponent(1);
        openButton.addMouseListener(listener);
    }

    public void setEditable(final boolean editableEditor) {
        dpBegin.setEditable(editableEditor);
        dpEnd.setEditable(editableEditor);
    }

    public JFormattedTextField getDateBeginEditor() {
        return dpBegin.getEditor();
    }

    public JFormattedTextField getDateEndEditor() {
        return dpEnd.getEditor();
    }

    public DatePeriod getTimeLimitPeriod() {
        DatePeriod result = new DatePeriod();
        result.setBegin(getMinTime(getDatePickerBegin()));
        result.setEnd(getMaxTime(getDatePickerEnd()));
        return result;
    }

    public void setEditorEditable(boolean editable) {
        dpBegin.getEditor().setEditable(editable);
        dpEnd.getEditor().setEditable(editable);
    }

    /**
     *
     */
    public void preparePeriodsLimits() {
        Calendar c = Calendar.getInstance();
        c.setTime(getDatePickerBegin());
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        setDatePickerBegin(c.getTime());

        c.setTime(getDatePickerEnd());
        c.set(Calendar.HOUR, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        setDatePickerEnd(c.getTime());
    }

    private Date getMinTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    private Date getMaxTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        setDatePickerEnd(c.getTime());
        return c.getTime();
    }
}
