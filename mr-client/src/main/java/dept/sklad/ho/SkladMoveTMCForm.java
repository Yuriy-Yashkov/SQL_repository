package dept.sklad.ho;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import common.Item;
import common.ProgressBar;
import common.User;
import common.UtilFunctions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class SkladMoveTMCForm extends javax.swing.JDialog {
    private User user = User.getInstance();

    private SkladHOPDB spdb;
    private JPanel osnova;
    private JPanel mainPanel;
    private JPanel buttPanel;
    private JLabel narTmc;
    private JLabel numTmc;
    private JLabel edIzm;
    private JLabel dept;
    private JLabel tip;
    private JLabel doc;
    private JLabel kolvo;
    private JLabel kolvoF;
    private JLabel kolvoK;
    private JLabel part;
    private JLabel title;
    private JLabel vvodEmpl;
    private JLabel vvodDate;
    private JLabel insEmpl;
    private JLabel insDate;
    private JLabel numEmpl;
    private JLabel numProd;
    private JLabel prodName;
    private JLabel stDate;
    private JLabel emplName;
    private JLabel tmcName;
    private JLabel sarTmc;
    private JLabel vidTmc;
    private JLabel madeTmc;
    private JLabel price;
    private JLabel summa;
    private JLabel priceDate;
    private JCheckBox print;
    private JButton buttPrint;
    private JButton buttClose;
    private JButton buttTMC;
    private JButton buttPrice;
    private JButton buttProd;
    private JButton buttEmpl;

    private boolean EDIT;
    private String TYPE;
    private int idTmc;

    private ProgressBar pb;
    private JTextPane note;
    private JPanel centrePanel;
    private JPanel rightPanel;
    private JPanel leftPanel;
    private JPanel upPanel;
    private JPanel downPanel;
    private ButtonGroup buttonGroup;
    private JRadioButton jRadioButton2;
    private JRadioButton jRadioButton3;
    private JRadioButton jRadioButton4;
    private MainController controller;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;

    public SkladMoveTMCForm(MainController mainController, boolean modal, Vector data) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Движение по складу №" + data.get(1).toString());
        setPreferredSize(new Dimension(750, 530));

        initData();

        init();

        EDIT = false;

        try {
            stDate.setText(data.get(2).toString());
            tip.setText(((Item) UtilFunctions.getItemsModel(UtilSkladHO.TIP_MOVE_MODEL, Integer.valueOf(data.get(3).toString()))).getDescription());
            numTmc.setText(data.get(5).toString());
            tmcName.setText(data.get(6).toString());
            narTmc.setText(data.get(7).toString());
            sarTmc.setText(data.get(8).toString());
            vidTmc.setText(data.get(9).toString());
            madeTmc.setText(data.get(10).toString());
            edIzm.setText(data.get(11).toString());
            kolvoF.setText(data.get(12).toString());
            kolvoK.setText(data.get(13).toString());
            part.setText(data.get(14).toString());
            priceDate.setText(data.get(15).toString());
            price.setText(data.get(16).toString());
            summa.setText(data.get(17).toString());
            dept.setText(((Item) UtilFunctions.getItemsModel(UtilSkladHO.DEPT_MODEL, Integer.valueOf(data.get(18).toString()))).getDescription());
            numEmpl.setText(data.get(20).toString());
            emplName.setText(data.get(21).toString());
            doc.setText(data.get(22).toString());
            note.setText(data.get(23).toString());
            vvodDate.setText(data.get(24).toString());
            vvodEmpl.setText(data.get(25).toString());
            insDate.setText(data.get(26).toString());
            insEmpl.setText(data.get(27).toString());
            numProd.setText(data.get(28).toString());

            switch (Integer.valueOf(data.get(30).toString())) {
                case 0:
                    jRadioButton2.setSelected(true);
                    break;
                case 1:
                    jRadioButton3.setSelected(true);
                    break;
                case -1:
                    jRadioButton4.setSelected(true);
                    break;
                default:
                    jRadioButton3.setSelected(true);
                    break;
            }

            kolvo.setText(vidTmc.getText().trim().equals(UtilSkladHO.SKLAD_VID_1) ? UtilSkladHO.KOLVO_MASS : UtilSkladHO.KOLVO_KOL);

            if (tip.getText().trim().equals(UtilSkladHO.TYPE_MOVE_RS))
                rightPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Получатель", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 2, 12)));
            else
                rightPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Источник", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 2, 12)));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu1.setText("Просмотр");

        jMenuItem1.setText("Карты раскроя");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Цены");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 279, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            new CenaTMCSkHOForm(controller, true, numTmc.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void init() {
        UtilSkladHO.BUTT_ACTION_EDIT = false;
        EDIT = false;

        initMenu();

        setMinimumSize(new Dimension(700, 480));

        osnova = new JPanel();
        buttPanel = new JPanel();
        upPanel = new JPanel();
        downPanel = new JPanel();
        mainPanel = new JPanel();
        centrePanel = new JPanel();
        rightPanel = new JPanel();
        leftPanel = new JPanel();
        numTmc = new JLabel();
        narTmc = new JLabel();
        tmcName = new JLabel();
        sarTmc = new JLabel();
        vidTmc = new JLabel();
        madeTmc = new JLabel();
        edIzm = new JLabel();
        kolvo = new JLabel();
        kolvoF = new JLabel();
        kolvoK = new JLabel();
        part = new JLabel();
        priceDate = new JLabel();
        price = new JLabel();
        summa = new JLabel();
        numEmpl = new JLabel();
        emplName = new JLabel();
        numProd = new JLabel();
        prodName = new JLabel();
        title = new JLabel("ТМЦ");
        doc = new JLabel();
        note = new JTextPane();
        dept = new JLabel();
        tip = new JLabel();
        stDate = new JLabel();
        buttPrint = new JButton("Печать");
        buttClose = new JButton("Закрыть");
        buttPrice = new JButton("Цена");
        buttProd = new JButton("Карта");
        buttonGroup = new ButtonGroup();
        jRadioButton2 = new JRadioButton();
        jRadioButton3 = new JRadioButton();
        jRadioButton4 = new JRadioButton();
        vvodEmpl = new JLabel();
        vvodDate = new JLabel();
        insEmpl = new JLabel();
        insDate = new JLabel();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel.setLayout(new BorderLayout(1, 1));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        upPanel.setLayout(new ParagraphLayout());
        downPanel.setLayout(new ParagraphLayout());
        rightPanel.setLayout(new ParagraphLayout());
        leftPanel.setLayout(new ParagraphLayout());
        centrePanel.setLayout(new GridLayout(0, 2, 5, 5));
        buttPanel.setLayout(new GridLayout(0, 4, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        rightPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Детали", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 2, 12)));
        leftPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ТМЦ", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 2, 12)));

        narTmc.setPreferredSize(new Dimension(250, 20));
        numTmc.setPreferredSize(new Dimension(100, 20));
        vidTmc.setPreferredSize(new Dimension(100, 20));
        emplName.setPreferredSize(new Dimension(250, 20));
        numEmpl.setPreferredSize(new Dimension(80, 20));
        numProd.setPreferredSize(new Dimension(80, 20));
        prodName.setPreferredSize(new Dimension(250, 20));
        stDate.setPreferredSize(new Dimension(120, 20));
        tip.setPreferredSize(new Dimension(150, 20));
        note.setPreferredSize(new Dimension(250, 40));
        vvodEmpl.setPreferredSize(new Dimension(240, 20));
        vvodDate.setPreferredSize(new Dimension(120, 20));
        insEmpl.setPreferredSize(new Dimension(240, 20));
        insDate.setPreferredSize(new Dimension(120, 20));

        note.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        vvodEmpl.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        vvodDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        insEmpl.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        insDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton4.setFont(new java.awt.Font("Dialog", 0, 13));

        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jRadioButton2.setText("формируется;");
        jRadioButton3.setText("закрыт;");
        jRadioButton4.setText("удалён;");

        jRadioButton2.setActionCommand("0");
        jRadioButton3.setActionCommand("1");
        jRadioButton4.setActionCommand("-1");

        jRadioButton2.setEnabled(false);
        jRadioButton3.setEnabled(false);
        jRadioButton4.setEnabled(false);

        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 18));

        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseActionPerformed(evt);
            }
        });

        buttPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //     buttSaveActionPerformed(evt);
            }
        });

        buttonGroup.add(jRadioButton2);
        buttonGroup.add(jRadioButton3);
        buttonGroup.add(jRadioButton4);

        upPanel.add(new JLabel("Дата :"), ParagraphLayout.NEW_PARAGRAPH);
        upPanel.add(stDate);
        upPanel.add(new JLabel("    Тип:    "));
        upPanel.add(tip);
        upPanel.add(new JLabel("    Статус:"));
        upPanel.add(jRadioButton2);
        upPanel.add(jRadioButton3);
        upPanel.add(jRadioButton4);

        leftPanel.add(new JLabel("Код ТМЦ:"), ParagraphLayout.NEW_PARAGRAPH);
        leftPanel.add(numTmc);
        leftPanel.add(new JLabel("  Шифр:"));
        leftPanel.add(sarTmc);
        leftPanel.add(new JLabel("Название:"), ParagraphLayout.NEW_PARAGRAPH);
        leftPanel.add(tmcName, ParagraphLayout.NEW_LINE_STRETCH_H);
        leftPanel.add(new JLabel("Артикул:"), ParagraphLayout.NEW_PARAGRAPH);
        leftPanel.add(narTmc);
        leftPanel.add(new JLabel("Вид:"), ParagraphLayout.NEW_PARAGRAPH);
        leftPanel.add(vidTmc);
        leftPanel.add(new JLabel("  Пр-во:"));
        leftPanel.add(madeTmc);
        leftPanel.add(kolvo, ParagraphLayout.NEW_PARAGRAPH);
        leftPanel.add(new JLabel(" факт.:"));
        leftPanel.add(kolvoF, ParagraphLayout.NEW_LINE_STRETCH_H);
        leftPanel.add(new JLabel(edIzm.getText()));
        leftPanel.add(new JLabel(" конд.:"), ParagraphLayout.NEW_LINE);
        leftPanel.add(kolvoK);
        leftPanel.add(new JLabel(edIzm.getText()));
        leftPanel.add(new JLabel("Кусков:"), ParagraphLayout.NEW_PARAGRAPH);
        leftPanel.add(part, ParagraphLayout.NEW_LINE_STRETCH_H);
        leftPanel.add(new JLabel("шт."));

        rightPanel.add(new JLabel("Документ:"), ParagraphLayout.NEW_PARAGRAPH);
        rightPanel.add(doc, ParagraphLayout.NEW_LINE_STRETCH_H);
        rightPanel.add(new JLabel("Цех:"), ParagraphLayout.NEW_PARAGRAPH);
        rightPanel.add(dept, ParagraphLayout.NEW_LINE_STRETCH_H);
        rightPanel.add(new JLabel("Работник:"), ParagraphLayout.NEW_PARAGRAPH);
        rightPanel.add(emplName);
        rightPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        rightPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        rightPanel.add(new JLabel(""), ParagraphLayout.NEW_PARAGRAPH);
        rightPanel.add(new JLabel("Цена:"), ParagraphLayout.NEW_PARAGRAPH);
        rightPanel.add(price);
        rightPanel.add(new JLabel(" от"));
        rightPanel.add(priceDate);
        rightPanel.add(new JLabel("Сумма:"), ParagraphLayout.NEW_PARAGRAPH);
        rightPanel.add(summa);
        rightPanel.add(new JLabel("Карта:"), ParagraphLayout.NEW_PARAGRAPH);
        rightPanel.add(numProd);

        centrePanel.add(leftPanel);
        centrePanel.add(rightPanel);

        downPanel.add(new JLabel("Примечание:"), ParagraphLayout.NEW_PARAGRAPH);
        downPanel.add(note, ParagraphLayout.NEW_LINE_STRETCH_H);
        downPanel.add(new JLabel("Дата ввода:"), ParagraphLayout.NEW_PARAGRAPH);
        downPanel.add(vvodDate);
        downPanel.add(new JLabel("    Автор:"));
        downPanel.add(vvodEmpl);
        downPanel.add(new JLabel("Дата корр.:"), ParagraphLayout.NEW_PARAGRAPH);
        downPanel.add(insDate);
        downPanel.add(new JLabel("    Автор:"));
        downPanel.add(insEmpl);

        mainPanel.add(upPanel, BorderLayout.NORTH);
        mainPanel.add(centrePanel, BorderLayout.CENTER);
        mainPanel.add(downPanel, BorderLayout.SOUTH);

        buttPanel.add(buttClose);
        buttPanel.add(new JLabel());
        buttPanel.add(new JLabel());
        buttPanel.add(buttPrint);

        osnova.add(mainPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void initData() {

        try {
            spdb = new SkladHOPDB();
            Vector dat = spdb.getDept();

            UtilSkladHO.DEPT_MODEL = new Vector();
            UtilSkladHO.DEPT_MODEL.add(new Item(-1, "-", ""));
            UtilFunctions.fullModel(UtilSkladHO.DEPT_MODEL, dat);
            UtilSkladHO.DEPT_MODEL.remove(1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Список подразделений не загружен! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            spdb.disConn();
        }

        try {
            UtilSkladHO.TIP_MOVE_MODEL = new Vector();
            UtilSkladHO.TIP_MOVE_MODEL.add(new Item(1, UtilSkladHO.TYPE_MOVE_PR, "1"));
            UtilSkladHO.TIP_MOVE_MODEL.add(new Item(2, UtilSkladHO.TYPE_MOVE_RS, "2"));
            UtilSkladHO.TIP_MOVE_MODEL.add(new Item(3, UtilSkladHO.TYPE_MOVE_VZ, "3"));
            UtilSkladHO.TIP_MOVE_MODEL.add(new Item(4, UtilSkladHO.TYPE_MOVE_AK, "4"));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Список подразделений не загружен! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initMenu() {
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu1.setText("Просмотр");

        jMenuItem1.setText("Карты раскроя");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Цены");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);
    }
}
