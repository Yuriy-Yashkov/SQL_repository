package by.march8.ecs.application.modules.references.product.editor;

import by.gomel.freedev.ucframework.ucswing.dialog.BaseDialog;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.product.utils.UtilProduct;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lidashka.
 */
public class PrintSizeCreatePane extends BaseDialog {
    MainController controller;
    private JRadioButton rule1;
    private JRadioButton rule2;
    private JRadioButton rule3;
    private JRadioButton rule4;
    private JCheckBox rule5;
    private JRadioButton box1;
    private JRadioButton box2;
    private JLabel jlPrintsize;
    private ButtonGroup buttonGroupRule;
    private ButtonGroup buttonGroupBox;
    private String height;
    private String size;

    public PrintSizeCreatePane(final MainController controller, String height, String size) {
        super(controller, new Dimension(300, 270));

        setTitle("Размер для печати");

        this.controller = controller;
        this.height = height;
        this.size = size;

        initConstants();
        init();
        initEvents();

        ruleAction();

        setVisible(true);
    }

    private void initConstants() {
        UtilProduct.ACTION_BUTT_CREATE_PRINTSIZE = false;
        UtilProduct.ITEM_PRINTSIZE = "";
    }

    private void init() {
        jlPrintsize = new JLabel();
        jlPrintsize.setHorizontalAlignment(JLabel.CENTER);
        jlPrintsize.setPreferredSize(new Dimension(100, 24));
        jlPrintsize.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        rule1 = new JRadioButton();
        rule1.setFont(new java.awt.Font("Dialog", 0, 13));
        rule1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rule1.setText("рост - размер;");
        rule1.setActionCommand("0");
        rule1.setSelected(true);

        rule2 = new JRadioButton();
        rule2.setFont(new java.awt.Font("Dialog", 0, 13));
        rule2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rule2.setText("диапазон роста - размер;");
        rule2.setActionCommand("1");

        rule3 = new JRadioButton();
        rule3.setFont(new java.awt.Font("Dialog", 0, 13));
        rule3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rule3.setText("без роста - диапазон размера;");
        rule3.setActionCommand("2");

        rule4 = new JRadioButton();
        rule4.setFont(new java.awt.Font("Dialog", 0, 13));
        rule4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rule4.setText("без роста - размер;");
        rule4.setActionCommand("3");

        rule5 = new JCheckBox("+ обхват бедер");

        box1 = new JRadioButton();
        box1.setFont(new java.awt.Font("Dialog", 0, 13));
        box1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        box1.setText("+6;");
        box1.setActionCommand("+6");
        box1.setSelected(true);
        box1.setEnabled(false);

        box2 = new JRadioButton();
        box2.setFont(new java.awt.Font("Dialog", 0, 13));
        box2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        box2.setText("-10;");
        box2.setActionCommand("-10");
        box2.setEnabled(false);

        buttonGroupRule = new ButtonGroup();
        buttonGroupRule.add(rule1);
        buttonGroupRule.add(rule2);
        buttonGroupRule.add(rule3);
        buttonGroupRule.add(rule4);

        buttonGroupBox = new ButtonGroup();
        buttonGroupBox.add(box1);
        buttonGroupBox.add(box2);

        JPanel tmp = new JPanel();
        tmp.add(box1);
        tmp.add(box2);

        panelTop.add(jlPrintsize);

        panelCenter.setLayout(new GridLayout(0, 1, 5, 5));
        panelCenter.add(rule1);
        panelCenter.add(rule2);
        panelCenter.add(rule3);
        panelCenter.add(rule4);
        panelCenter.add(rule5);
        panelCenter.add(tmp);
    }

    private void initEvents() {
        btnCancel.addActionListener(e -> {
            setVisible(false);
            dispose();
        });

        btnSave.addActionListener(e -> {
            UtilProduct.ACTION_BUTT_CREATE_PRINTSIZE = true;
            UtilProduct.ITEM_PRINTSIZE = jlPrintsize.getText().trim();
            setVisible(false);
            dispose();
        });

        rule1.addActionListener(evt -> ruleAction());

        rule2.addActionListener(evt -> ruleAction());

        rule3.addActionListener(evt -> ruleAction());

        rule4.addActionListener(evt -> ruleAction());

        rule5.addActionListener(evt -> {
            if (rule5.isSelected()) {
                box1.setEnabled(true);
                box2.setEnabled(true);
            } else {
                box1.setEnabled(false);
                box2.setEnabled(false);
            }
            ruleAction();
        });

        box1.addActionListener(evt -> ruleAction());

        box2.addActionListener(evt -> ruleAction());
    }

    private void ruleAction() {
        jlPrintsize.setText(getValuePrintSize(
                height,
                size,
                buttonGroupRule.getSelection().getActionCommand(),
                rule5.isSelected(),
                Integer.valueOf(buttonGroupBox.getSelection().getActionCommand())
        ));
    }

    /**
     * Метод по исходным данным роста, размера и правила, возвращает значение этикетки.
     * @param height - значение роста
     * @param size - значение размера
     * @param rule - правило
     * @param flag - использовать или не использовать обхват бедер
     * @param value - значение обхвата бедер
     * @return
     * @throws Exception
     */
    private String getValuePrintSize(String height, String size, String rule, boolean flag, int value) {
        String str = "";

        if (!height.equals("") && !size.equals("")) {

            height = height.replace(".0", "");
            size = size.replace(".0", "");

            try {
                switch (Integer.valueOf(rule)) {
                    case 0:
                        str = height + "-" + size;
                        break;
                    case 1:
                        str = height + "," + (Integer.valueOf(height) + 6) + "-" + size;
                        break;
                    case 2:
                        str = size + "-" + (Integer.valueOf(size) + 1);
                        break;
                    case 3:
                        str = size;
                        break;
                    default:
                        str = height + "-" + size;
                        break;
                }

                if (flag) {
                    str = str + "-" + (Integer.valueOf(size) + value);

                }

            } catch (Exception e) {
                str = "";
                JOptionPane.showMessageDialog(null,
                        "Введено некорректное значение: " + e.getMessage(),
                        "Ошибка",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
        return str;
    }
}
