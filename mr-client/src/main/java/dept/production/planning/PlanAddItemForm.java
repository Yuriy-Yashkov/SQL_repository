package dept.production.planning;

import com.jhlabs.awt.ParagraphLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 *
 * @author lidasha
 */
public class PlanAddItemForm extends javax.swing.JDialog {
    private JButton buttClose;
    private JButton buttAdd;
    private JButton buttClear;
    private JPanel osnova;
    private JPanel centerPanel;
    private JPanel buttPanel;
    private JTextField sarText;
    private JTextField fasText;
    private JTextField rstText;
    private JTextField rzmText;
    private JTextField kolText;
    private SmallTableForm smallTableForm;
    private JTextField convText;
    private JTextField dekad1Text;
    private JTextField dekad2Text;
    private JTextField dekad3Text;
    private JTextField noteText;

    public PlanAddItemForm(JDialog parent, boolean modal) {
        super(parent, modal);
        setTitle("Добавить новую запись");

        smallTableForm = (SmallTableForm) parent;

        init();

        setResizable(false);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(350, 290));
        setPreferredSize(new Dimension(350, 390));

        osnova = new JPanel();
        centerPanel = new JPanel();
        buttPanel = new JPanel();

        buttClose = new JButton("Закрыть");
        buttAdd = new JButton("Добавить");
        buttClear = new JButton("Очистить");
        sarText = new JTextField();
        fasText = new JTextField();
        rstText = new JTextField();
        rzmText = new JTextField();
        kolText = new JTextField();
        convText = new JTextField();
        dekad1Text = new JTextField();
        dekad2Text = new JTextField();
        dekad3Text = new JTextField();
        noteText = new JTextField();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.setLayout(new ParagraphLayout());
        buttPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        sarText.setPreferredSize(new Dimension(200, 20));
        fasText.setPreferredSize(new Dimension(200, 20));
        rstText.setPreferredSize(new Dimension(200, 20));
        rzmText.setPreferredSize(new Dimension(200, 20));
        kolText.setPreferredSize(new Dimension(200, 20));
        convText.setPreferredSize(new Dimension(200, 20));
        dekad1Text.setPreferredSize(new Dimension(200, 20));
        dekad2Text.setPreferredSize(new Dimension(200, 20));
        dekad3Text.setPreferredSize(new Dimension(200, 20));
        noteText.setPreferredSize(new Dimension(200, 20));

        sarText.setText(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_SAR));
        fasText.setText(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_FAS));
        rstText.setText(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_RST));
        rzmText.setText(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_RZM));
        kolText.setText(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_KOL));
        convText.setText(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_CONV));
        dekad1Text.setText(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_DEKAD1));
        dekad2Text.setText(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_DEKAD2));
        dekad3Text.setText(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_DEKAD3));
        noteText.setText(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_NOTE));

        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseTableActionPerformed(evt);
            }
        });

        buttAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttAddActionPerformed(evt);
            }
        });

        buttClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttClearActionPerformed(evt);
            }
        });

        centerPanel.add(new JLabel("Шифр.арт."), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(sarText);
        centerPanel.add(new JLabel("Модель"), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(fasText);
        centerPanel.add(new JLabel("Рост"), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(rstText);
        centerPanel.add(new JLabel("Размер"), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(rzmText);
        centerPanel.add(new JLabel("Кол. М."), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(kolText);
        centerPanel.add(new JLabel("Конвейер"), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(convText);
        centerPanel.add(new JLabel("1-я декада"), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(dekad1Text);
        centerPanel.add(new JLabel("2-я декада"), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(dekad2Text);
        centerPanel.add(new JLabel("3-я декада"), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(dekad3Text);
        centerPanel.add(new JLabel("Примечание"), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(noteText);

        buttPanel.add(buttClear);
        buttPanel.add(buttAdd);
        buttPanel.add(buttClose);

        osnova.add(centerPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void buttCloseTableActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void buttClearActionPerformed(ActionEvent evt) {
        try {
            clearConstantAddItemPlan();

            sarText.setText(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_SAR));
            fasText.setText(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_FAS));
            rstText.setText(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_RST));
            rzmText.setText(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_RZM));
            kolText.setText(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_KOL));
            convText.setText(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_CONV));
            dekad1Text.setText(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_DEKAD1));
            dekad2Text.setText(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_DEKAD2));
            dekad3Text.setText(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_DEKAD3));
            noteText.setText(String.valueOf(UtilPlan.EDIT_ADD_ITEM_PLAN_NOTE));
        } catch (Exception e) {
            clearConstantAddItemPlan();
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttAddActionPerformed(ActionEvent evt) {
        try {
            UtilPlan.EDIT_ADD_ITEM_PLAN_SAR = Integer.valueOf(sarText.getText().trim());
            UtilPlan.EDIT_ADD_ITEM_PLAN_FAS = Integer.valueOf(fasText.getText().trim());
            UtilPlan.EDIT_ADD_ITEM_PLAN_RST = Integer.valueOf(rstText.getText().trim());
            UtilPlan.EDIT_ADD_ITEM_PLAN_RZM = Integer.valueOf(rzmText.getText().trim());
            UtilPlan.EDIT_ADD_ITEM_PLAN_KOL = Double.valueOf(kolText.getText().trim());
            UtilPlan.EDIT_ADD_ITEM_PLAN_CONV = Integer.valueOf(convText.getText().trim());
            UtilPlan.EDIT_ADD_ITEM_PLAN_DEKAD1 = Double.valueOf(dekad1Text.getText().trim());
            UtilPlan.EDIT_ADD_ITEM_PLAN_DEKAD2 = Double.valueOf(dekad2Text.getText().trim());
            UtilPlan.EDIT_ADD_ITEM_PLAN_DEKAD3 = Double.valueOf(dekad3Text.getText().trim());
            UtilPlan.EDIT_ADD_ITEM_PLAN_NOTE = noteText.getText().trim();

            smallTableForm.addItemEditSmallTable();

            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearConstantAddItemPlan() {
        UtilPlan.EDIT_ADD_ITEM_PLAN_SPEC_ID = 1;
        UtilPlan.EDIT_ADD_ITEM_PLAN_SPEC_NAME = "Спецификация отсутствует";
        UtilPlan.EDIT_ADD_ITEM_PLAN_SPEC_NORM = 0;
        UtilPlan.EDIT_ADD_ITEM_PLAN_SAR = 0;
        UtilPlan.EDIT_ADD_ITEM_PLAN_FAS = 0;
        UtilPlan.EDIT_ADD_ITEM_PLAN_RST = 0;
        UtilPlan.EDIT_ADD_ITEM_PLAN_RZM = 0;
        UtilPlan.EDIT_ADD_ITEM_PLAN_KOL = 0;
        UtilPlan.EDIT_ADD_ITEM_PLAN_CONV = 0;
        UtilPlan.EDIT_ADD_ITEM_PLAN_DEKAD1 = 0;
        UtilPlan.EDIT_ADD_ITEM_PLAN_DEKAD2 = 0;
        UtilPlan.EDIT_ADD_ITEM_PLAN_DEKAD3 = 0;
        UtilPlan.EDIT_ADD_ITEM_PLAN_NOTE = "";
    }
}
