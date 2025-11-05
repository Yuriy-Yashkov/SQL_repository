package dept.production.planning;

import com.jhlabs.awt.ParagraphLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class ProjectWorkArticleForm extends javax.swing.JDialog {
    int ID;
    int TYPE;
    private JPanel osnovaPanel;
    private JPanel centerPanel;
    private JPanel buttPanel;
    private JTextPane noteText;
    private JTextField fasText;
    private JTextField narText;
    private JTextField polotnoText;
    private JButton buttClose;
    private JButton buttSave;

    public ProjectWorkArticleForm(ProjectUpdateForm parent, boolean modal) {
        super(parent, modal);
        setTitle("Добавить артикул в работу");

        init();

        ID = -1;
        TYPE = 0;

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public ProjectWorkArticleForm(ProjectUpdateForm parent, boolean modal, Vector item) {
        super(parent, modal);
        setTitle("Артикул в работе");

        init();

        ID = Integer.valueOf(item.get(1).toString().trim());
        TYPE = Integer.valueOf(item.get(2).toString().trim());
        fasText.setText(item.get(3).toString().trim());
        narText.setText(item.get(4).toString().trim());
        polotnoText.setText(item.get(6).toString().trim());
        noteText.setText(item.get(7).toString().trim());

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public ProjectWorkArticleForm(ProjectUpdateForm parent, boolean modal,
                                  String fas, String nar, String polotno) {

        super(parent, modal);
        setTitle("Добавить артикул в работу");

        init();

        ID = -1;
        TYPE = 0;
        fasText.setText(fas);
        narText.setText(nar);
        polotnoText.setText(polotno);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(550, 250));
        setPreferredSize(new Dimension(550, 250));

        cleanConstants();

        fasText = new JTextField();
        fasText.setPreferredSize(new Dimension(120, 20));

        narText = new JTextField();
        narText.setPreferredSize(new Dimension(220, 20));

        polotnoText = new JTextField();
        polotnoText.setPreferredSize(new Dimension(250, 20));

        noteText = new JTextPane();
        noteText.setPreferredSize(new Dimension(320, 50));
        noteText.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        centerPanel = new JPanel();
        centerPanel.setLayout(new ParagraphLayout());
        centerPanel.add(new JLabel("Модель:"), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(fasText, ParagraphLayout.NEW_LINE_STRETCH_H);
        centerPanel.add(new JLabel("Артикул:"), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(narText, ParagraphLayout.NEW_LINE_STRETCH_H);
        centerPanel.add(new JLabel("Полотно:"), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(polotnoText, ParagraphLayout.NEW_LINE_STRETCH_H);
        centerPanel.add(new JLabel("Примечание"), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(new JScrollPane(noteText), ParagraphLayout.NEW_LINE_STRETCH_H);

        buttClose = new JButton("Закрыть");
        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseActionPerformed(evt);
            }
        });

        buttSave = new JButton("Добавить");
        buttSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSaveActionPerformed(evt);
            }
        });

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttPanel.add(buttClose);
        buttPanel.add(buttSave);

        osnovaPanel = new JPanel();
        osnovaPanel.setLayout(new BorderLayout(1, 1));
        osnovaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        osnovaPanel.add(centerPanel, BorderLayout.CENTER);
        osnovaPanel.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnovaPanel);
        pack();
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void buttSaveActionPerformed(ActionEvent evt) {
        try {
            boolean saveFlag = true;
            String str = "";

            if (fasText.getText().trim().equals("")) {
                saveFlag = false;
                str += "Вы не ввели модель!\n";
            }

            if (narText.getText().trim().equals("")) {
                saveFlag = false;
                str += "Вы не ввели артикул!\n";
            }

            if (!saveFlag) {
                JOptionPane.showMessageDialog(null, str, "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            }

            if (saveFlag) {
                Vector item = getItem();

                item.set(3, fasText.getText().trim());
                item.set(4, narText.getText().trim().toUpperCase());
                item.set(5, "");
                item.set(6, polotnoText.getText().trim().toUpperCase());
                item.set(7, noteText.getText().trim());

                UtilPlan.EDIT_PROJECT_ARTICL_ITEM = item;
                UtilPlan.EDIT_PROJECT_ARTICL_BUTT_ACTION = true;

                dispose();
            }
        } catch (Exception e) {
            UtilPlan.EDIT_PROJECT_ARTICL_BUTT_ACTION = false;
            UtilPlan.EDIT_PROJECT_ARTICL_ITEM = new Vector();

            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cleanConstants() {
        UtilPlan.EDIT_PROJECT_ARTICL_BUTT_ACTION = false;
        UtilPlan.EDIT_PROJECT_ARTICL_ITEM = new Vector();
    }

    private Vector getItem() {
        Vector workItem = new Vector();

        try {
            workItem.add(false);
            workItem.add(ID);
            workItem.add(TYPE);
            workItem.add("");
            workItem.add("");
            workItem.add("");
            workItem.add("");
            workItem.add("");
            workItem.add("");
            workItem.add("");

        } catch (Exception e) {
            workItem = new Vector();
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return workItem;
    }
}
