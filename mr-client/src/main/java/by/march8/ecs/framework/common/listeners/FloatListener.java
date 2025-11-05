package by.march8.ecs.framework.common.listeners;

import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.ecs.framework.common.FloatMask;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Обработчик событий компонента типа JTextField для ввода числовых значений
 * с плавающей точкой. В качестве разделителя использется символы точки или запятой.
 * Не реализован запре на ввод символов отличных от цифровых. Все это реализовано
 * в компоненте UCTextField.
 * Для этого экземпляр контрола нужно методом setComponentParams() проинициализировать
 * как  setComponentParams(null, Float.class, 0);
 */
public class FloatListener implements KeyListener {

    private JTextField textField;
    private boolean maskPreset = false;
    private FloatMask mask = new FloatMask();

    public FloatListener(final JTextField textField) {
        this.textField = textField;
        // textField потомок UCTextField ?
        if (textField instanceof UCTextField) {
            String tempMask = ((UCTextField) textField).getFloatMask().trim();
            if (!(tempMask.equals("0.0"))) {
                mask.parse(tempMask);
            } else {
                mask.setPreset(false);
            }
        } else {
            mask.setPreset(false);
        }
    }

    @Override
    public void keyTyped(final KeyEvent e) {
        char c = e.getKeyChar();
        if (c == ',') {
            e.setKeyChar('.');
        }

        if ((c == ',') || (c == '.')) {
            e.consume();
        }
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        int keyKode = e.getKeyCode();
        char c = e.getKeyChar();
        if ((keyKode >= 48 && keyKode <= 57) || (keyKode >= 96 && keyKode <= 105) || (c == '.') || (c == ',')) {
            try {
                UCTextField ucComponent = (UCTextField) textField;
                int caret = textField.getCaret().getMark();
                Document doc = textField.getDocument();
                if ((c == '.') || (c == ',')) {
                    String fillSymbol = "0";

                    for (int i = 0; i < caret; i++) {
                        caret = textField.getCaret().getMark();
                        if (doc.getText(i, 1).equals(".")) {
                            doc.remove(i, 1);
                        }
                    }
                    caret = textField.getCaret().getMark();

/*                    if(caret+1>ucComponent.getPresetValueLength()){
                        doc.remove(caret+1+ucComponent.getPresetValueLength(), 1);
                    }*/

                    if ((caret + 1 <= doc.getLength()) && (!doc.getText(caret, 1).equals("."))) {
                        fillSymbol = doc.getText(caret, 1);
                        doc.remove(caret, doc.getLength() - caret);
                    }
                    if (!doc.getText(caret, 1).equals(".")) {
                        if (caret == 0) {
                            doc.insertString(caret, "0." + fillSymbol, null);
                            textField.getCaret().setDot(caret + 2);
                        } else {
                            doc.insertString(caret, "." + fillSymbol, null);
                            textField.getCaret().setDot(caret + 1);
                        }
                    } else {
                        textField.getCaret().setDot(caret + 1);
                    }


                    e.consume();
                } else {

                    if ((!doc.getText(caret, 1).equals(".")) && (caret + 1 <= doc.getLength())) {
                        doc.remove(caret, 1);
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void keyReleased(final KeyEvent e) {

    }
}
