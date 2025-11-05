package dept.production.planning.ean;

import com.jhlabs.awt.ParagraphLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 *
 * @author lidashka
 */
public class SmallEditDetalForm extends JDialog {
    private JPanel osnovaPanel;
    private JPanel centerPanel;
    private JPanel buttPanel;
    private JButton buttClose;
    private JButton buttSave;
    private JButton buttSearch;
    private JTextField numColorMarshList;
    private JTextField nameColorMarshList;
    private JTextField numColorEan;
    private JTextField nameColorEan;
    private EanDB edb;

    public SmallEditDetalForm(java.awt.Dialog parent,
                              boolean modal,
                              int idColorMarshList,
                              String nameColorMarshList,
                              int idColor,
                              String nameColor) {

        super(parent, modal);

        init();
        initData(idColorMarshList, nameColorMarshList, idColor, nameColor);

        this.setTitle("Корректировка цвета");
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(580, 220));
        setPreferredSize(new Dimension(580, 220));

        osnovaPanel = new JPanel();
        osnovaPanel.setLayout(new BorderLayout(1, 1));
        osnovaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        centerPanel = new JPanel();
        centerPanel.setLayout(new ParagraphLayout());

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        buttClose = new JButton("Закрыть");
        buttClose.addActionListener(evt -> buttCloseActionPerformed(evt));

        buttSave = new JButton("Сохранить");
        buttSave.addActionListener(evt -> buttSaveActionPerformed(evt));

        buttSearch = new JButton("Изменить");
        buttSearch.addActionListener(evt -> buttSearchActionPerformed(evt));

        numColorMarshList = new JTextField();
        numColorMarshList.setPreferredSize(new Dimension(80, 20));
        numColorMarshList.setEditable(false);

        nameColorMarshList = new JTextField();
        nameColorMarshList.setPreferredSize(new Dimension(250, 20));
        nameColorMarshList.setEditable(false);

        numColorEan = new JTextField();
        numColorEan.setPreferredSize(new Dimension(80, 20));
        numColorEan.setEditable(false);

        nameColorEan = new JTextField();
        nameColorEan.setPreferredSize(new Dimension(250, 20));
        nameColorEan.setEditable(false);

        centerPanel.add(new JLabel("Цвет маршрута:"));
        centerPanel.add(numColorMarshList, ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(new JLabel(""));
        centerPanel.add(nameColorMarshList);
        centerPanel.add(new JLabel("Цвет EAN-заявки:"), ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(numColorEan, ParagraphLayout.NEW_PARAGRAPH);
        centerPanel.add(new JLabel(""));
        centerPanel.add(nameColorEan);
        centerPanel.add(new JLabel(""));
        centerPanel.add(buttSearch);

        buttPanel.add(buttClose);
        buttPanel.add(buttSave);

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

            UtilEan.ACTION_BUTT_EDIT_COLOR = false;

            try {
                if (!numColorEan.getText().trim().equals(""))
                    Integer.valueOf(numColorEan.getText().trim());

            } catch (NumberFormatException e) {
                saveFlag = false;
                JOptionPane.showMessageDialog(
                        null,
                        "Цвет задан некорректно!",
                        "Внимание",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            if (saveFlag) {
                try {
                    edb = new EanDB();

                    if (edb.updateColor(Integer.valueOf(numColorEan.getText().trim()), Integer.valueOf(numColorMarshList.getText().trim()))) {

                        UtilEan.ACTION_BUTT_EDIT_COLOR = true;

                        JOptionPane.showMessageDialog(null,
                                "Цвет успешно изменен!",
                                "Завершено",
                                javax.swing.JOptionPane.INFORMATION_MESSAGE);

                        dispose();
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Ошибка. " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    edb.disConn();
                }
            }

        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSearchActionPerformed(ActionEvent evt) {
        try {
            new SearchForm(this, true, UtilEan.SELECT_COLOR, "");

            if (UtilEan.ACTION_BUTT_SELECT) {
                numColorEan.setText(UtilEan.ITEM_ADD_SELECT_ITEM_ID);
                nameColorEan.setText(UtilEan.ITEM_ADD_SELECT_ITEM);
            }

        } catch (Exception e) {
            numColorEan.setText("");
            nameColorEan.setText("");

            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initData(int idColorM, String nameColorM, int idColor, String nameColor) {
        numColorMarshList.setText(String.valueOf(idColorM));
        nameColorMarshList.setText(nameColorM);
        numColorEan.setText(String.valueOf(idColor));
        nameColorEan.setText(nameColor);
    }

}
