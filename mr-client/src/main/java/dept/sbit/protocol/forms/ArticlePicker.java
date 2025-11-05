package dept.sbit.protocol.forms;

import by.gomel.freedev.ucframework.ucswing.uicontrols.UCCheckBoxList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Andy 15.06.2017.
 */
public class ArticlePicker extends javax.swing.JDialog {

    private UCCheckBoxList cblArticles;
    private String articleList;

    public ArticlePicker(JDialog parent, String articleList) {
        super(parent, true);
        this.articleList = articleList;
        setTitle("Артикула для отбора");

        init();
        initData();

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initData() {

        for (int i = 0; i < cblArticles.getModel().getSize(); i++) {
            cblArticles.getModel().getElementAt(i).setSelected(false);
        }

        // Предустановка комбиков исходя из строки артикулов
        if (articleList != null) {
            String[] array = articleList.split(";");
            if (array.length > 0) {
                for (int i = 0; i < cblArticles.getModel().getSize(); i++) {
                    for (int z = 0; z < array.length; z++) {
                        if (array[z].equals(cblArticles.getModel().getElementAt(i).getText())) {
                            cblArticles.getModel().getElementAt(i).setSelected(true);
                        }
                    }
                }
            }
        }
    }

    private void init() {
        setMinimumSize(new Dimension(250, 250));

        DefaultListModel<JCheckBox> model = new DefaultListModel<JCheckBox>();

        model.addElement(new JCheckBox("0С"));
        model.addElement(new JCheckBox("1С"));
        model.addElement(new JCheckBox("2С"));
        model.addElement(new JCheckBox("3С"));
        model.addElement(new JCheckBox("4С"));
        model.addElement(new JCheckBox("5С"));
        model.addElement(new JCheckBox("6С"));
        model.addElement(new JCheckBox("7С"));
        model.addElement(new JCheckBox("8С"));
        model.addElement(new JCheckBox("9С"));

        cblArticles = new UCCheckBoxList(model);

        JButton buttClose = new JButton("Закрыть");
        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseActionPerformed(evt);
            }
        });


        JButton buttSelect = new JButton("Выбрать");
        buttSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSelectActionPerformed(evt);
            }
        });

        JPanel buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 2, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.add(buttSelect);
        buttPanel.add(buttClose);

        JPanel osnova = new JPanel();
        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        osnova.add(cblArticles, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void buttSelectActionPerformed(ActionEvent evt) {
        try {
            String result = "";
            for (int i = 0; i < cblArticles.getModel().getSize(); i++) {
                if (cblArticles.getModel().getElementAt(i).isSelected()) {
                    result += cblArticles.getModel().getElementAt(i).getText() + ";";
                }
            }
            if (result.length() < 1) {
            } else {
                result = result.substring(0, result.length() - 1);
                articleList = result;
            }
            dispose();
        } catch (Exception e) {

        }
    }

    private String prepareArticles() {
        return articleList;
    }

    public String selectArticles() {
        return prepareArticles();
    }
}
