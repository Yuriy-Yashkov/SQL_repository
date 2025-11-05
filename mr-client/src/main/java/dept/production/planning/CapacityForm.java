package dept.production.planning;

import com.jhlabs.awt.ParagraphLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class CapacityForm extends JDialog {
    JButton butRun;
    JButton butClose;
    JButton butPrint;
    JLabel title;
    JTextField param1;
    JTextField param2;
    JTextField param3;
    JPanel osnova;
    JPanel panel;
    JPanel buttPanel;
    private JTextField param4;
    private JTextField param5;
    private JTextField param6;
    private JTextField param7;
    private JTextField param8;
    private JPanel centerPanel;
    private JPanel panelRez;
    private JTextField param9;
    private JLabel param11;
    private JLabel param12;
    private JTextField param10;
    private JLabel param13;

    public CapacityForm(JDialog parent, boolean modal, int workDay, double[] param) {
        super(parent, modal);
        setTitle("Загрузка цеха");

        init();

        clearParametrs();

        try {
            param9.setText(new DecimalFormat("###,###.###").format(param[0]));
            param10.setText(new DecimalFormat("###,###.###").format(param[2]));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(500, 420));
        setPreferredSize(new Dimension(520, 460));

        osnova = new JPanel();
        panel = new JPanel();
        panelRez = new JPanel();
        centerPanel = new JPanel();
        buttPanel = new JPanel();

        title = new JLabel("Загрузка цеха на месяц");
        butRun = new JButton("Рассчитать");
        butPrint = new JButton("Печать");
        butClose = new JButton("Закрыть");
        param1 = new JTextField();
        param2 = new JTextField();
        param3 = new JTextField();
        param4 = new JTextField();
        param5 = new JTextField();
        param6 = new JTextField();
        param7 = new JTextField();
        param8 = new JTextField();
        param9 = new JTextField();
        param10 = new JTextField();
        param11 = new JLabel();
        param12 = new JLabel();
        param13 = new JLabel();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new ParagraphLayout());
        panelRez.setLayout(new ParagraphLayout());
        centerPanel.setLayout(new BorderLayout(1, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        param1.setPreferredSize(new Dimension(100, 20));
        param2.setPreferredSize(new Dimension(100, 20));
        param3.setPreferredSize(new Dimension(100, 20));
        param4.setPreferredSize(new Dimension(100, 20));
        param5.setPreferredSize(new Dimension(100, 20));
        param6.setPreferredSize(new Dimension(100, 20));
        param7.setPreferredSize(new Dimension(100, 20));
        param8.setPreferredSize(new Dimension(100, 20));
        param9.setPreferredSize(new Dimension(200, 20));
        param10.setPreferredSize(new Dimension(200, 20));
        param11.setPreferredSize(new Dimension(200, 20));
        param12.setPreferredSize(new Dimension(200, 20));
        param13.setPreferredSize(new Dimension(200, 20));

        param1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        param2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        param3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        param4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        param5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        param6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        param7.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        param8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        param9.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        param10.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        param11.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        param12.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        param13.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 24));

        butClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butCloseActionPerformed(evt);
            }
        });

        butPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butPrintActionPerformed(evt);
            }
        });

        butRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butRunActionPerformed(evt);
            }
        });

        panel.add(new JLabel("Кол-во швей:"), ParagraphLayout.NEW_PARAGRAPH);
        panel.add(param1);
        panel.add(new JLabel("чел"));
        panel.add(new JLabel("          Отпуск:"));
        panel.add(param2);
        panel.add(new JLabel("чел"));
        panel.add(new JLabel("Больнич. лист:"), ParagraphLayout.NEW_PARAGRAPH);
        panel.add(param7);
        panel.add(new JLabel("н/ч"));
        panel.add(new JLabel("          Cессии:"));
        panel.add(param8);
        panel.add(new JLabel("н/ч"));
        panel.add(new JLabel("Рабочие дни:"), ParagraphLayout.NEW_PARAGRAPH);
        panel.add(param3);
        panel.add(new JLabel("   Кор.  р/дни:"));
        panel.add(param5);
        panel.add(new JLabel("Рабочие часы:"), ParagraphLayout.NEW_PARAGRAPH);
        panel.add(param4);
        panel.add(new JLabel("  Кор. р/часы:"));
        panel.add(param6);

        panelRez.add(new JLabel("Выпуск кол-во ед.:"), ParagraphLayout.NEW_PARAGRAPH);
        panelRez.add(param9);
        panelRez.add(new JLabel("шт"));
        panelRez.add(new JLabel("Трудоемкость на выпуск:"), ParagraphLayout.NEW_PARAGRAPH);
        panelRez.add(param10);
        panelRez.add(new JLabel("н/ч"));
        panelRez.add(new JLabel("Загрузка цеха на месяц:"), ParagraphLayout.NEW_PARAGRAPH);
        panelRez.add(param11);
        panelRez.add(new JLabel("н/ч"));
        panelRez.add(new JLabel("Пропускная способность цеха:"), ParagraphLayout.NEW_PARAGRAPH);
        panelRez.add(param12);
        panelRez.add(new JLabel("н/ч"));
        panelRez.add(new JLabel("Загрузка цеха по плану:"), ParagraphLayout.NEW_PARAGRAPH);
        panelRez.add(param13);
        panelRez.add(new JLabel("%"));

        centerPanel.add(panel, BorderLayout.CENTER);
        centerPanel.add(panelRez, BorderLayout.SOUTH);

        buttPanel.add(butPrint);
        buttPanel.add(butRun);
        buttPanel.add(butClose);

        osnova.add(title, BorderLayout.NORTH);
        osnova.add(centerPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void butRunActionPerformed(ActionEvent evt) {
        try {

            param11.setText(new DecimalFormat("###,###.###").format(
                    new Double((Double.valueOf(param1.getText().trim().replaceAll(" ", "").replaceAll(",", ".")) -
                            Double.valueOf(param2.getText().trim().replaceAll(" ", "").replaceAll(",", "."))) *
                            ((Double.valueOf(param3.getText().trim().replaceAll(" ", "").replaceAll(",", ".")) *
                                    Double.valueOf(param4.getText().trim().replaceAll(" ", "").replaceAll(",", "."))) +
                                    (Double.valueOf(param5.getText().trim().replaceAll(" ", "").replaceAll(",", ".")) *
                                            Double.valueOf(param6.getText().trim().replaceAll(" ", "").replaceAll(",", ".")))))));

            param12.setText(new DecimalFormat("###,###.###").format(
                    new Double(new DecimalFormat("###,###.###").parse(param11.getText()).doubleValue() -
                            (Double.valueOf(param7.getText().trim().replaceAll(" ", "").replaceAll(",", ".")) +
                                    Double.valueOf(param8.getText().trim().replaceAll(" ", "").replaceAll(",", "."))))));

            param13.setText(new DecimalFormat("###,###.###").format(new Double((new DecimalFormat("###,###.###").parse(
                    param10.getText()).doubleValue() * 100) / new DecimalFormat("###,###.###").parse(param12.getText()).doubleValue())));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void butCloseActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void butPrintActionPerformed(ActionEvent evt) {
        try {
            Vector dataReport = new Vector();
            dataReport.add(param1.getText().trim());
            dataReport.add(param2.getText().trim());
            dataReport.add(param3.getText().trim());
            dataReport.add(param4.getText().trim());
            dataReport.add(param5.getText().trim());
            dataReport.add(param6.getText().trim());
            dataReport.add(param7.getText().trim());
            dataReport.add(param8.getText().trim());
            dataReport.add(param9.getText().trim());
            dataReport.add(param10.getText().trim());
            dataReport.add(param11.getText().trim());
            dataReport.add(param12.getText().trim());
            dataReport.add(param13.getText().trim());

            PlanOO oo = new PlanOO("", dataReport);
            oo.createReport("ProductionPlanCapacityDept.ots");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearParametrs() {
        param1.setText("0");
        param2.setText("0");
        param3.setText("0");
        param4.setText("0");
        param5.setText("0");
        param6.setText("0");
        param7.setText("0");
        param8.setText("0");
        param9.setText("0");
        param10.setText("0");
        param11.setText("0");
        param12.setText("0");
        param13.setText("0");
    }
}
