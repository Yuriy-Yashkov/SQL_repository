package dept.production.zsh.spec;


import com.jhlabs.awt.ParagraphLayout;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class FormulaForm extends JDialog {
    JButton butRun;
    JButton butAdd;
    JLabel title;
    JLabel formula;
    JLabel formulaRezalt;
    JLabel param3Text;
    JTextField param1;
    JTextField param2;
    JTextField param3;
    JPanel osnova;
    JPanel panel;
    JPanel buttPanel;
    JComboBox formulaText;

    Vector model;

    public FormulaForm(JDialog parent, boolean modal) {
        super(parent, modal);
        setTitle("Формулы");

        init();

        clearParametrs();

        formulaText.setSelectedIndex(formulaText.getSelectedIndex());

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(500, 280));
        setPreferredSize(new Dimension(550, 280));

        osnova = new JPanel();
        panel = new JPanel();
        buttPanel = new JPanel();
        title = new JLabel("Формулы расчёта норм");
        butRun = new JButton("Рассчитать");
        butAdd = new JButton("Добавить");
        formulaText = new JComboBox();
        formula = new JLabel();
        formulaRezalt = new JLabel();
        param3Text = new JLabel("Параметр z");
        param1 = new JTextField();
        param2 = new JTextField();
        param3 = new JTextField();
        model = new Vector();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new ParagraphLayout());
        buttPanel.setLayout(new GridLayout(0, 2, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        formulaText.setPreferredSize(new Dimension(300, 20));
        formula.setPreferredSize(new Dimension(300, 20));
        formulaRezalt.setPreferredSize(new Dimension(100, 20));
        param1.setPreferredSize(new Dimension(100, 20));
        param2.setPreferredSize(new Dimension(100, 20));
        param3.setPreferredSize(new Dimension(100, 20));

        formula.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        formulaRezalt.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        param1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        param2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        param3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 24));

        model.add(new Item(1, "Настилание РК", "x/60/y*2*1.085"));
        model.add(new Item(2, "Резак РК", "8/(28800/((x+12)*60/y/z*1.0855))"));

        for (Iterator itr = model.iterator(); itr.hasNext(); ) {
            Item it = (Item) itr.next();
            formulaText.addItem(it.getDescription());
        }

        formulaText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                formulaTextActionPerformed(evt);
            }
        });

        butAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butAddActionPerformed(evt);
            }
        });

        butRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butRunActionPerformed(evt);
            }
        });

        panel.add(formulaText, ParagraphLayout.NEW_PARAGRAPH);
        panel.add(formula, ParagraphLayout.NEW_PARAGRAPH);
        panel.add(new JLabel(" = "));
        panel.add(formulaRezalt);
        panel.add(new JLabel("Параметр х"), ParagraphLayout.NEW_PARAGRAPH);
        panel.add(param1);
        panel.add(new JLabel("Параметр у"), ParagraphLayout.NEW_PARAGRAPH);
        panel.add(param2);
        panel.add(param3Text, ParagraphLayout.NEW_PARAGRAPH);
        panel.add(param3);

        buttPanel.add(butRun);
        buttPanel.add(butAdd);

        osnova.add(title, BorderLayout.NORTH);
        osnova.add(panel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void formulaTextActionPerformed(ActionEvent evt) {
        double norm = new BigDecimal(new Double(0)).setScale(UtilSpec.ROUNDING_NORM, RoundingMode.HALF_UP).doubleValue();
        try {
            if (((Item) model.get(formulaText.getSelectedIndex())).getId() == 1) {
                param3.setText("1");
                param3.setVisible(false);
                param3Text.setVisible(false);
            } else {
                param3.setVisible(true);
                param3Text.setVisible(true);
            }

            if (Double.valueOf(param1.getText().trim().replace(",", ".")) > 0 &&
                    Double.valueOf(param2.getText().trim().replace(",", ".")) > 0 &&
                    Double.valueOf(param3.getText().trim().replace(",", ".")) > 0) {

                ScriptEngine jsEngine = new ScriptEngineManager().getEngineByName("JavaScript");
                String str = ((Item) model.get(formulaText.getSelectedIndex())).getFormula();
                str = str.replace("x", param1.getText().trim().replace(",", "."));
                str = str.replace("y", param2.getText().trim().replace(",", "."));
                str = str.replace("z", param3.getText().trim().replace(",", "."));

                norm = new BigDecimal((Double) jsEngine.eval(str)).setScale(UtilSpec.ROUNDING_NORM, RoundingMode.HALF_UP).doubleValue();
            } else
                JOptionPane.showMessageDialog(null, "Деление на 0!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            formula.setText(((Item) model.get(formulaText.getSelectedIndex())).getFormula());
            formulaRezalt.setText(UtilSpec.formatNorm(norm));
        }
    }

    private void butAddActionPerformed(ActionEvent evt) {
        try {
            UtilSpec.FORMULA_NORM = UtilSpec.formatNorm(Double.valueOf(formulaRezalt.getText().trim()));
        } catch (Exception e) {
            UtilSpec.FORMULA_NORM = UtilSpec.formatNorm(new Double(0));
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            dispose();
        }
    }

    private void butRunActionPerformed(ActionEvent evt) {
        try {
            formulaText.setSelectedIndex(formulaText.getSelectedIndex());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearParametrs() {
        param1.setText("1");
        param2.setText("1");
        param3.setText("1");
    }
}
