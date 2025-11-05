package by.gomel.freedev.ucframework.ucswing.uicontrols;


import by.gomel.freedev.ucframework.ucswing.uicontrols.filters.CustomDocumentFilter;
import by.gomel.freedev.ucframework.ucswing.uicontrols.interfaces.IUCComponent;
import by.march8.ecs.framework.common.listeners.FloatListenerDemo;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import static by.march8.ecs.framework.common.Settings.COLOR_DISABLED;
import static by.march8.ecs.framework.common.Settings.COLOR_ENABLED;

/**
 * Наследник JTextField со своими свистелками и перделками
 * Created by andy-linux on 3/21/15.
 */
@SuppressWarnings("unused")
public class UCTextField extends JTextField implements IUCComponent {

    private String value;
    private Color errorColor;
    private int tabOrder = 0;
    private JLabel label = null;
    private int valueLength = 0;
    private boolean required = false;
    private Class<?> type;
    private UCController ucc = null;
    private String floatMask = "0.0";

    public UCTextField() {
        this(null, null, 0);
    }

    public UCTextField(String text) {
        this(null, text, 0);
    }

    public UCTextField(int columns) {
        this(null, null, columns);
    }

    public UCTextField(String text, int columns) {
        this(null, text, columns);
    }

    public UCTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
        value = super.getText();
        tabOrder = -1;
        // NewFocusListener listener = new NewFocusListener();
        // addFocusListener(listener);
    }

    private double getCutDecimal(double d) {
        String s = String.valueOf(d);
        String outString = "";
        int decimalIndex = s.indexOf(".");
        if (decimalIndex + 3 > s.length()) {
            outString = s.substring(0, s.length());
        } else {
            outString = s.substring(0, decimalIndex + 3);
        }
        return Double.valueOf(outString);
    }

    private void replaceSpace() {
        if (type != Float.class) {
            return;
        }

        String v = this.getText();
        int decimalIndex = v.indexOf(".");
        if (decimalIndex > 0) {
            // Считаем, сколько символов после запятой заполнено
            int symbolCount = v.length() - (decimalIndex + 1);
            // Если количество символов в пределах лимита и если меньше, то заполняем оставшееся место символом 0
            if (symbolCount < valueLength) {
                int replaceCount = valueLength - symbolCount;
                String appendString = "";
                for (int i = 0; i < replaceCount; i++) {
                    appendString += "0";
                }
                setText(getText() + appendString);
            }
        }
    }

    @Override
    public void setText(String t) {
        if (type == Float.class) {
            super.setText(t.replace(',', '.'));
        } else {
            super.setText(t);
        }
        value = super.getText();

        replaceSpace();
    }

    public double getValueDouble() {
        try {
            return Double.valueOf(getText().trim());
        } catch (Exception e) {

            return 0;
        }
    }

    @Override
    public boolean isModified() {
        String value_ = getText();
        if (value_ == null) {
            return value != null;
        } else {
            return value == null || !value_.trim().equals(value.trim());
        }
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public void setComponentParams(JLabel label, Class<?> type, int length) {
        this.label = label;
        this.type = type;
        this.tabOrder = tabOrder;
        valueLength = length;
        required = true;
        if (type == String.class) {
            ((PlainDocument) this.getDocument()).setDocumentFilter(new CustomDocumentFilter(this, Template.REGEX_EMPTY));
        }
        if (type == Integer.class) {
            ((PlainDocument) this.getDocument()).setDocumentFilter(new CustomDocumentFilter(this, Template.REGEX_INTEGER));
        }
        if ((type == Float.class) || (type == Double.class)) {
            ((PlainDocument) this.getDocument()).setDocumentFilter(new CustomDocumentFilter(this, Template.REGEX_FLOAT));
            addKeyListener(new FloatListenerDemo(this));
            addFocusListener(new ReplacerFocusListener());
        }
        //setBackground(Template.requiredFieldColor);
    }

    @Override
    public boolean isVerificationComplete() {
        return UCValidationService.doDataCheck(this);
    }

    @Override
    public int getPresetValueLength() {
        return valueLength;
    }

    public void setPresetValueLength(int length) {
        valueLength = length;
    }

    @Override
    public Class<?> getValueType() {
        return type;
    }

    @Override
    public JLabel getComponentLabel() {
        return label;
    }

    @Override
    public void setFocus() {
        this.requestFocusInWindow();
    }

    @Override
    public Object getValue() {
        return getText();
    }

    /**
     * Тест записи значения типа Double
     *
     * @param t значение
     */
    public void setValue(double t) {
        setText(String.valueOf(t));
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public UCController getUCController() {
        return ucc;
    }

    @Override
    public void setUCController(final UCController ucc) {
        this.ucc = ucc;
    }

    /**
     * Возвращает цвет фона для событий связанных с верификацией данных
     *
     * @return цвет фона компонента
     */
    public Color getErrorColor() {
        return errorColor;
    }

    /**
     * Устанавливает цвет фона для событий связанных с верификацией данных
     *
     * @param errorColor цвет фона компонента
     */
    public void setErrorColor(Color errorColor) {
        this.errorColor = errorColor;
    }

    @Override
    public int getTabOrder() {
        return tabOrder;
    }

    @Override
    public void setTabOrder(int tabOrder) {
        this.tabOrder = tabOrder;
    }

    /**
     * Возвращает сигнатуру маски для дробного числа
     *
     * @return
     */
    public String getFloatMask() {
        return floatMask;
    }

    /**
     * Устанавливает маску для форматирования дробного числа
     * 0.2 - до запятой количество символов не ограничено,
     * после запятой допускается ввести 2 знака
     *
     * @param floatMask маска количества символов дробного числа
     */
    public void setFloatMask(final String floatMask) {
        this.floatMask = floatMask;
    }

    private static class NewFocusListener implements FocusListener {

        Color normalColor;

        @Override
        public void focusGained(final FocusEvent e) {
            Component component = e.getComponent();
            normalColor = component.getBackground();
            if (component instanceof JTextComponent) {
                if (((JTextComponent) component).isEditable()) {
                    component.setBackground(COLOR_ENABLED);
                    ((JTextComponent) component).selectAll();
                } else {
                    component.setBackground(COLOR_DISABLED);
                }
            } else {
                if (component instanceof JComboBox) {
                    if (((JComboBox) component).isEditable()) {
                        component.setBackground(COLOR_ENABLED);
                    } else {
                        component.setBackground(COLOR_DISABLED);
                    }
                } else if (((UCDatePicker) component).isEditable()) {
                    component.setBackground(COLOR_ENABLED);
                } else {
                    component.setBackground(COLOR_DISABLED);
                }
            }
        }

        @Override
        public void focusLost(final FocusEvent e) {
            Component component = e.getComponent();
            if (component.isEnabled()) {
                component.setBackground(Color.white);
            } else {
                component.setBackground(Color.LIGHT_GRAY);
            }
        }
    }

    private class ReplacerFocusListener implements FocusListener {
        @Override
        public void focusGained(final FocusEvent e) {
            Component component = e.getComponent();
            if (component instanceof JTextComponent) {
                if (((JTextComponent) component).isEditable()) {
                    ((JTextComponent) component).selectAll();
                }
            }
            //setCaretPosition(0);
        }

        @Override
        public void focusLost(final FocusEvent e) {
            replaceSpace();
        }
    }


}
