package by.march8.ecs.framework.common.listeners;

import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.ecs.framework.common.FloatMask;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author Andy 20.06.2016.
 */
public class FloatListenerDemo implements KeyListener {
    private JTextField textField;
    private boolean maskPreset = false;
    private FloatMask mask = new FloatMask();

    public FloatListenerDemo(final JTextField textField) {
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
        UCTextField ucComponent = (UCTextField) textField;

        int i = textField.getText().indexOf('.');
        int caret = textField.getCaret().getMark();

        if (i > 0) {
            if (ucComponent.getPresetValueLength() > 0) {
                int floatPart = textField.getText().length() - i;
                if (caret > i) {
                    if (floatPart > ucComponent.getPresetValueLength()) {
                        if (c != ',') {
                            e.consume();
                        }
                    }
                }
            } else {
                int floatPart = textField.getText().length() - i;
                if (caret >= i + 2) {
                    if (floatPart > ucComponent.getPresetValueLength()) {
                        if (c != ',') {
                            e.consume();

                        }
                    }
                } else {
                    if (caret > i) {
                        e.setKeyChar('0');
                    }
                }
            }
        }

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

                    // Чистка предыдущих запятых в числе
                    for (int i = 0; i < caret; i++) {
                        caret = textField.getCaret().getMark();
                        if (doc.getText(i, 1).equals(".")) {
                            doc.remove(i, 1);

                            if (doc.getText(0, 1).equals("0")) {
                                doc.remove(0, 1);
                            }
                        }
                    }
                    caret = textField.getCaret().getMark();
                    int length = 0;
                    if ((caret + 1 <= doc.getLength()) && (!doc.getText(caret, 1).equals("."))) {

                        int amount = doc.getLength() - caret;

                        if (amount < ucComponent.getPresetValueLength()) {
                            length = amount;
                        } else {
                            length = ucComponent.getPresetValueLength();
                        }

                        if (length < 1) {
                            fillSymbol = "0";
                        } else {
                            fillSymbol = doc.getText(caret, length);
                        }
                        doc.remove(caret, doc.getLength() - caret);
                    }

                    fillSymbol = clearDot(fillSymbol, ucComponent.getPresetValueLength());

                    if (!doc.getText(caret, 1).equals(".")) {
                        if (caret == 0) {
                            doc.insertString(caret, "0." + fillSymbol, null);
                            textField.getCaret().setDot(caret + 2);
                        } else {
                            doc.insertString(caret, "." + fillSymbol, null);
                            if (ucComponent.getPresetValueLength() > 0) {
                                textField.getCaret().setDot(caret + 1);
                            } else {
                                textField.getCaret().setDot(caret);
                            }
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

    private String clearDot(String s, int length) {
        String result = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != '.') {
                result += s.charAt(i);
            }
        }

        if (result.equals("")) {
            return "0";
        }

        if (result.length() > length) {
            return result.substring(0, length);
        }

        return result;
    }

    @Override
    public void keyReleased(final KeyEvent e) {

    }
}
