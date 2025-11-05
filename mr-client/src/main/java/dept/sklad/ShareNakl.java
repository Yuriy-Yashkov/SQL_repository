/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.sklad;

import by.march8.ecs.framework.common.LogCrutch;
import common.ProgressBar;
import dept.component.MyButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


/**
 *
 * @author user
 */
public class ShareNakl extends JDialog {

    //   private static final Logger log = new Log().getLoger(ShareNakl.class);
    private static final LogCrutch log = new LogCrutch();
    private JTextField jtfFirstValue;
    private JTextField jtfSecondValue;
    private JLabel jlFirstValue;
    private JLabel jlSecondValue;
    private MyButton mbOk;
    private MyButton mbCancel;
    private GridBagConstraints gbcVariable;
    private GridBagLayout gblVariable;
    private SkladDB sdb;
    private ProgressBar pb;

    public ShareNakl(JFrame parent) {
        super(parent);
        initComp();
    }

    private void initComp() {
        try {
            sdb = new SkladDB();
            jtfFirstValue = new JTextField(12);
            jtfFirstValue.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
                        quickSearch(jtfFirstValue);
                    } else {
                        deleteSymbol(jtfFirstValue);
                    }
                }
            });
            jtfSecondValue = new JTextField(12);
//            jtfSecondValue.addKeyListener(new KeyAdapter() {
//                @Override
//                public void keyReleased(KeyEvent e) {
//                    quickSearch(jtfSecondValue);
//                }
//            });
            jlFirstValue = new JLabel("Номер исходной накладной");
            jlSecondValue = new JLabel("Номер пустой накладной");
            gbcVariable = new GridBagConstraints();
            gblVariable = new GridBagLayout();
            setLayout(gblVariable);
            mbOk = new MyButton("Ok");
            mbOk.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    actionUser();
                }
            });
            mbCancel = new MyButton("Закрыть");
            mbCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            gbcVariable.gridx = 0;
            gbcVariable.gridy = 0;
            gbcVariable.anchor = GridBagConstraints.WEST;
            add(jlFirstValue, gbcVariable);
            gbcVariable.gridx = 1;
            gbcVariable.gridy = 0;
            gbcVariable.anchor = GridBagConstraints.EAST;
            add(jtfFirstValue, gbcVariable);
            gbcVariable.gridx = 0;
            gbcVariable.gridy = 1;
            gbcVariable.anchor = GridBagConstraints.WEST;
            add(jlSecondValue, gbcVariable);
            gbcVariable.gridx = 1;
            gbcVariable.gridy = 1;
            gbcVariable.anchor = GridBagConstraints.EAST;
            add(jtfSecondValue, gbcVariable);
            gbcVariable.gridx = 0;
            gbcVariable.gridy = 2;
            gbcVariable.anchor = GridBagConstraints.WEST;
            add(mbOk, gbcVariable);
            gbcVariable.gridx = 1;
            gbcVariable.gridy = 2;
            gbcVariable.anchor = GridBagConstraints.EAST;
            add(mbCancel, gbcVariable);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setSize(380, 100);
            setTitle("Разделить накладную");
            setLocationRelativeTo(null);
            setVisible(true);
        } catch (Exception ex) {
            exceptionChek(ex);
        }
    }

    private void actionUser() {
        if (!jtfFirstValue.getText().trim().equals("") && !jtfSecondValue.getText().trim().equals("")) {
            int resultCount = sdb.shareNakl(jtfFirstValue.getText().trim(), jtfSecondValue.getText().trim());
            if (chekcResult(resultCount)) {
                JOptionPane.showMessageDialog(this, "Успешно перенесено " + resultCount + " записей.");
            } else {
                JOptionPane.showMessageDialog(this, "Не перенесено ни одной строки.\n\nВозможно:\n* Указано неверное название накладных;"
                        + "\n* Вкл. клавиша CapsLock;\n* Выбран язык Англ./Рус.\n* Одна из накладных не создана.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Пустые строки в названии накладной недопустимы!");
        }
    }

    private boolean chekcResult(int i) {
        boolean result = false;
        try {
            if (i != 0) {
                result = true;
            }
        } catch (Exception ex) {
            exceptionChek(ex);
        } finally {
            return result;
        }
    }

    private void exceptionChek(Exception ex) {
        StackTraceElement[] stak = ex.getStackTrace();
        log.error("\nException in class " + stak[0].getClassName() + ",\nin method " + stak[0].getMethodName() + ", in string number " + stak[0].getLineNumber() + ".\n"
                + "TEXT MESSAGE: \" " + ex.getMessage() + " \"");
        //JOptionPane.showMessageDialog(null, "Exception!.\nPlease contact developer.");
    }

    private void quickSearch(JTextField jtf) {
        String result = sdb.quickSearch(jtf.getText());
        int startPosition = jtf.getText().length();
        int endPosition = result.length() + jtf.getText().length();
        jtf.setText(jtf.getText() + result);
        jtf.select(startPosition, endPosition);
    }

    private void deleteSymbol(JTextField jtf) {
        if (jtf.getText().length() > 1) {
            int lengthSelectText = jtf.getSelectionStart();
            String newString = jtf.getText().substring(0, lengthSelectText - 1);
            //JOptionPane.showMessageDialog(this, jtf.getText().trim());
            jtf.setText(newString);
            quickSearch(jtf);
        } else {

        }
    }
}
