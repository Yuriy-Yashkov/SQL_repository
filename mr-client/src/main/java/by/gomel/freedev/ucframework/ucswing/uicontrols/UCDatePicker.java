package by.gomel.freedev.ucframework.ucswing.uicontrols;

import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.util.Date;

/**
 * Компонент выбора даты из календаря
 * Created by Andy on 12.08.2015.
 */
public class UCDatePicker extends JPanel {


    private MaskFormatter formatter;

    private JXDatePicker datePicker;
    private JFormattedTextField editor;

    public UCDatePicker(final Date date) {
        super(null);
        initComponent(date);
    }


    public JXDatePicker getDatePicker() {
        return datePicker;
    }

    private void initComponent(Date date) {
        try {
            formatter = new MaskFormatter("##.##.####");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
        formatter.setPlaceholderCharacter('0');

        datePicker = new JXDatePicker(date);
        editor = new JFormattedTextField(formatter);

        editor.setBounds(0, 0, 76, 20);
        datePicker.setBounds(78, 0, 22, 20);

        setDate(date);

        JButton dateBtn = (JButton) datePicker.getComponent(1);
        dateBtn.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/calendar.png", ""));
        dateBtn.setFocusPainted(true);
        dateBtn.setMargin(new Insets(0, 0, 0, 0));
        dateBtn.setContentAreaFilled(true);
        dateBtn.setBorderPainted(true);
        dateBtn.setOpaque(false);

        add(editor);
        add(datePicker);
        setOpaque(false);

        editor.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(final FocusEvent e) {

            }

            @Override
            public void focusLost(final FocusEvent e) {
                datePicker.setDate(DateUtils.getDateByStringValue(editor.getText()));
            }
        });

        editor.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {

            }

            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER | e.getKeyCode() == KeyEvent.VK_TAB) {
                    datePicker.setDate(DateUtils.getDateByStringValue(editor.getText()));
                    try {
                        datePicker.commitEdit();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void keyReleased(final KeyEvent e) {

            }

        });

/*        editor.addActionListener(a->{
            datePicker.setDate(DateUtils.getDateByStringValue(editor.getText()));
            try {
                datePicker.commitEdit();
                System.err.println("Ввод даты ["+editor.getText()+"]");
            } catch (ParseException e1) {
                e1.printStackTrace();
                System.err.println("Ошибка ввод даты ["+editor.getText()+"]");
            }
        });*/

        datePicker.addActionListener(a -> editor.setText(DateUtils.getNormalDateFormat(datePicker.getDate())));
    }

    public JFormattedTextField getEditor() {
        return editor;
    }

    public void setEditor(final JFormattedTextField editor) {
        this.editor = editor;
    }

    public Date getDate() {
        return datePicker.getDate();
    }

    public void setDate(Date date) {
        Date date_ = date;
        if (date_ == null) {
            date_ = new Date();
        }
        datePicker.setDate(date_);
        editor.setValue(DateUtils.getNormalDateFormat(date_));
    }


    public void addActionListener(final ActionListener evt) {
        datePicker.addActionListener(evt);
    }

    public boolean isEditable() {
        return editor.isEditable();
    }

    public void setEditable(boolean isEditable) {
        editor.setEditable(isEditable);
        datePicker.setEditable(isEditable);
    }
}
