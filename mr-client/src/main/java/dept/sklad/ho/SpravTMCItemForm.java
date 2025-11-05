package dept.sklad.ho;

import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.ProgressBar;
import common.User;
import common.UtilFunctions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
public class SpravTMCItemForm extends javax.swing.JDialog {
    private User user = User.getInstance();

    private SkladHOPDB spdb;
    private JPanel osnova;
    private JPanel upPanel;
    private JPanel buttPanel;
    private JTextField groupTmc;
    private JTextField tmcName;
    private JTextField sarTmc;
    private JTextField narTmc;
    private JTextField edIzm;
    private JTextField priceTmc;
    private JDateChooser sStDate;
    private JComboBox vidTmc;
    private JComboBox madeTmc;
    private JButton buttSave;
    private JButton buttClose;
    private JButton buttGroup;
    private JLabel title;
    private JLabel groupNum;
    private JLabel vvodEmpl;
    private JLabel vvodDate;
    private JLabel insEmpl;
    private JLabel insDate;

    private boolean EDIT;
    private String TYPE;
    private int idTmc;

    private ProgressBar pb;
    private JTextPane noteTmc;

    public SpravTMCItemForm(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        setTitle("Новая запись");

        setPreferredSize(new Dimension(550, 450));

        init();

        EDIT = true;
        TYPE = UtilSkladHO.TYPE_ADD;

        sStDate.setDate((Calendar.getInstance()).getTime());

        upPanel.add(new JLabel("Цена:"), ParagraphLayout.NEW_PARAGRAPH);
        upPanel.add(priceTmc);
        upPanel.add(new JLabel("    от:"));
        upPanel.add(sStDate);

        buttSave.setText("Добавить");

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public SpravTMCItemForm(java.awt.Dialog parent, boolean modal, Vector dataTmc, int idTmc) {
        super(parent, modal);
        setTitle("Новая запись");

        setPreferredSize(new Dimension(550, 450));

        init();

        EDIT = true;
        TYPE = UtilSkladHO.TYPE_ADD;

        sStDate.setDate((Calendar.getInstance()).getTime());

        upPanel.add(new JLabel("Цена:"), ParagraphLayout.NEW_PARAGRAPH);
        upPanel.add(priceTmc);
        upPanel.add(new JLabel("    от:"));
        upPanel.add(sStDate);

        buttSave.setText("Добавить");

        groupNum.setText(dataTmc.get(1).toString());
        groupTmc.setText(dataTmc.get(2).toString());
        tmcName.setText(dataTmc.get(3).toString());
        vidTmc.setSelectedItem(dataTmc.get(4).toString());
        madeTmc.setSelectedItem(dataTmc.get(5).toString());
        sarTmc.setText(dataTmc.get(6).toString());
        narTmc.setText(dataTmc.get(7).toString());
        edIzm.setText(dataTmc.get(8).toString());
        noteTmc.setText(dataTmc.get(9).toString());
        vvodEmpl.setText(dataTmc.get(10).toString());
        vvodDate.setText(dataTmc.get(11).toString());
        insEmpl.setText(dataTmc.get(12).toString());
        insDate.setText(dataTmc.get(13).toString());

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public SpravTMCItemForm(java.awt.Dialog parent, boolean modal, int idTmc, Vector dataTmc) {
        super(parent, modal);
        setTitle("Редактирование");

        this.idTmc = idTmc;
        setPreferredSize(new Dimension(550, 480));

        init();

        EDIT = true;
        TYPE = UtilSkladHO.TYPE_EDIT;

        upPanel.add(new JLabel("Дата ввода:"), ParagraphLayout.NEW_PARAGRAPH);
        upPanel.add(vvodDate);
        upPanel.add(new JLabel("    Автор:"));
        upPanel.add(vvodEmpl);
        upPanel.add(new JLabel("Дата корр.:"), ParagraphLayout.NEW_PARAGRAPH);
        upPanel.add(insDate);
        upPanel.add(new JLabel("    Автор:"));
        upPanel.add(insEmpl);

        buttSave.setText("Сохранить");

        groupNum.setText(dataTmc.get(1).toString());
        groupTmc.setText(dataTmc.get(2).toString());
        tmcName.setText(dataTmc.get(3).toString());
        vidTmc.setSelectedItem(dataTmc.get(4).toString());
        madeTmc.setSelectedItem(dataTmc.get(5).toString());
        sarTmc.setText(dataTmc.get(6).toString());
        narTmc.setText(dataTmc.get(7).toString());
        edIzm.setText(dataTmc.get(8).toString());
        noteTmc.setText(dataTmc.get(9).toString());
        vvodEmpl.setText(dataTmc.get(10).toString());
        vvodDate.setText(dataTmc.get(11).toString());
        insEmpl.setText(dataTmc.get(12).toString());
        insDate.setText(dataTmc.get(13).toString());

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public SpravTMCItemForm(java.awt.Dialog parent, boolean modal, Vector dataTmc) {
        super(parent, modal);
        setTitle("ТМЦ");

        this.idTmc = idTmc;
        setPreferredSize(new Dimension(550, 480));

        init();

        title.setText(title.getText() + " №" + dataTmc.get(0).toString());

        EDIT = false;
        TYPE = UtilSkladHO.TYPE_OPEN;

        upPanel.add(new JLabel("Дата ввода:"), ParagraphLayout.NEW_PARAGRAPH);
        upPanel.add(vvodDate);
        upPanel.add(new JLabel("    Автор:"));
        upPanel.add(vvodEmpl);
        upPanel.add(new JLabel("Дата корр.:"), ParagraphLayout.NEW_PARAGRAPH);
        upPanel.add(insDate);
        upPanel.add(new JLabel("    Автор:"));
        upPanel.add(insEmpl);

        buttSave.setVisible(false);
        
        /*
        tmcName.setEnabled(false);        
        sarTmc.setEnabled(false);      
        narTmc.setEnabled(false);         
        edIzm.setEnabled(false);      
        noteTmc.setEnabled(false);      
        vidTmc.setEnabled(false);      
        madeTmc.setEnabled(false); 
        */

        buttGroup.setEnabled(false);

        groupNum.setText(dataTmc.get(1).toString());
        groupTmc.setText(dataTmc.get(2).toString());
        tmcName.setText(dataTmc.get(3).toString());
        vidTmc.setSelectedItem(dataTmc.get(4).toString());
        madeTmc.setSelectedItem(dataTmc.get(5).toString());
        sarTmc.setText(dataTmc.get(6).toString());
        narTmc.setText(dataTmc.get(7).toString());
        edIzm.setText(dataTmc.get(8).toString());
        noteTmc.setText(dataTmc.get(9).toString());
        vvodEmpl.setText(dataTmc.get(10).toString());
        vvodDate.setText(dataTmc.get(11).toString());
        insEmpl.setText(dataTmc.get(12).toString());
        insDate.setText(dataTmc.get(13).toString());

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private void init() {
        UtilSkladHO.BUTT_ACTION_EDIT = false;
        EDIT = false;

        setMinimumSize(new Dimension(550, 450));

        osnova = new JPanel();
        buttPanel = new JPanel();
        upPanel = new JPanel();
        sStDate = new JDateChooser();
        groupTmc = new JTextField();
        tmcName = new JTextField();
        sarTmc = new JTextField();
        narTmc = new JTextField();
        edIzm = new JTextField();
        priceTmc = new JTextField();
        noteTmc = new JTextPane();
        vidTmc = new JComboBox();
        madeTmc = new JComboBox();
        groupNum = new JLabel();
        vvodEmpl = new JLabel();
        vvodDate = new JLabel();
        insEmpl = new JLabel();
        insDate = new JLabel();
        title = new JLabel("ТМЦ");
        buttSave = new JButton("Сохранить");
        buttClose = new JButton("Закрыть");
        buttGroup = new JButton("Группа");

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        upPanel.setLayout(new ParagraphLayout());
        buttPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        groupTmc.setPreferredSize(new Dimension(300, 20));
        tmcName.setPreferredSize(new Dimension(300, 20));
        vidTmc.setPreferredSize(new Dimension(250, 20));
        madeTmc.setPreferredSize(new Dimension(250, 20));
        sarTmc.setPreferredSize(new Dimension(200, 20));
        narTmc.setPreferredSize(new Dimension(200, 20));
        edIzm.setPreferredSize(new Dimension(80, 20));
        priceTmc.setPreferredSize(new Dimension(200, 20));
        groupNum.setPreferredSize(new Dimension(80, 20));
        noteTmc.setPreferredSize(new Dimension(200, 50));
        vvodEmpl.setPreferredSize(new Dimension(240, 20));
        vvodDate.setPreferredSize(new Dimension(100, 20));
        insEmpl.setPreferredSize(new Dimension(240, 20));
        insDate.setPreferredSize(new Dimension(100, 20));
        sStDate.setPreferredSize(new Dimension(120, 20));

        groupNum.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        noteTmc.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        vvodEmpl.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        vvodDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        insEmpl.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        insDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 18));

        groupTmc.setEnabled(false);

        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseActionPerformed(evt);
            }
        });

        buttSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSaveActionPerformed(evt);
            }
        });

        buttGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttGroupActionPerformed(evt);
            }
        });

        vidTmc.addItem(UtilSkladHO.SKLAD_VID_1);
        vidTmc.addItem(UtilSkladHO.SKLAD_VID_2);

        madeTmc.addItem(UtilSkladHO.SKLAD_MADE_1);
        madeTmc.addItem(UtilSkladHO.SKLAD_MADE_2);

        upPanel.add(buttGroup, ParagraphLayout.NEW_PARAGRAPH);
        upPanel.add(groupTmc, ParagraphLayout.NEW_LINE_STRETCH_H);
        upPanel.add(new JLabel("Название:"), ParagraphLayout.NEW_PARAGRAPH);
        upPanel.add(tmcName, ParagraphLayout.NEW_LINE_STRETCH_H);
        upPanel.add(new JLabel("Артикул:"), ParagraphLayout.NEW_PARAGRAPH);
        upPanel.add(narTmc);
        upPanel.add(new JLabel("Шифр:"), ParagraphLayout.NEW_PARAGRAPH);
        upPanel.add(sarTmc);
        upPanel.add(new JLabel("Вид:"), ParagraphLayout.NEW_PARAGRAPH);
        upPanel.add(vidTmc);
        upPanel.add(new JLabel("Производство:"), ParagraphLayout.NEW_PARAGRAPH);
        upPanel.add(madeTmc);
        upPanel.add(new JLabel("Ед.измерения:"), ParagraphLayout.NEW_PARAGRAPH);
        upPanel.add(edIzm);
        upPanel.add(new JLabel("Примечание:"), ParagraphLayout.NEW_PARAGRAPH);
        upPanel.add(noteTmc, ParagraphLayout.NEW_LINE_STRETCH_H);

        buttPanel.add(buttClose);
        buttPanel.add(buttSave);

        osnova.add(title, BorderLayout.NORTH);
        osnova.add(upPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void buttGroupActionPerformed(ActionEvent evt) {
        try {
            new SpravTMCGroupForm(this, true);

            if (UtilSkladHO.BUTT_ACTION_SELECT_GROUP) {
                groupNum.setText(String.valueOf(UtilSkladHO.SPRAV_GROUPTMC_ID));
                groupTmc.setText(UtilSkladHO.SPRAV_GROUPTMC_NAME);
            }

        } catch (Exception e) {
            groupNum.setText("-1");
            groupTmc.setText("");
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttSaveActionPerformed(ActionEvent evt) {
        try {
            if (EDIT) {
                boolean saveFlag = true;
                String str = "";

                if (tmcName.getText().trim().equals("")) {
                    saveFlag = false;
                    str += "Вы не ввели название ТМЦ!\n";
                }

                if (narTmc.getText().trim().equals("")) {
                    saveFlag = false;
                    str += "Вы не ввели артикул ТМЦ!\n";
                }

                if (edIzm.getText().trim().equals("")) {
                    saveFlag = false;
                    str += "Вы не ввели ед.измерения ТМЦ!\n";
                }

                if (!vidTmc.getSelectedItem().equals(UtilSkladHO.SKLAD_VID_1) &&
                        !vidTmc.getSelectedItem().equals(UtilSkladHO.SKLAD_VID_2)) {
                    saveFlag = false;
                    str += "Вид ТМЦ задан некорректно!\n";
                }

                if (!madeTmc.getSelectedItem().equals(UtilSkladHO.SKLAD_MADE_1) &&
                        !madeTmc.getSelectedItem().equals(UtilSkladHO.SKLAD_MADE_2)) {
                    saveFlag = false;
                    str += "Пр-во ТМЦ задано некорректно!\n";
                }

                try {
                    if (sarTmc.getText().trim().equals("")) {
                        sarTmc.setText("0");
                    } else
                        Integer.valueOf(sarTmc.getText().trim());
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Шифр ТМЦ задан некорректно!\n";
                }

                try {
                    if (priceTmc.getText().trim().equals("")) {
                        priceTmc.setText("0");
                    } else
                        Double.valueOf(priceTmc.getText().trim().replace(",", "."));
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Цена ТМЦ задана некорректно!\n";
                }

                try {
                    if (groupNum.getText().trim().equals("")) {
                        groupNum.setText("-1");
                    } else
                        Integer.valueOf(groupNum.getText().trim());
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Группа ТМЦ задана некорректно!\n";
                }

                if (!saveFlag) {
                    JOptionPane.showMessageDialog(null, str, "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                }

                if (saveFlag) {
                    pb = new ProgressBar(SpravTMCItemForm.this, false, "Сохранение проекта плана ...");
                    SwingWorker sw = new SwingWorker() {
                        protected Object doInBackground() {
                            try {
                                spdb = new SkladHOPDB();
                                boolean save = false;

                                if (TYPE.equals(UtilSkladHO.TYPE_ADD)) {
                                    save = spdb.addTmcSkladHO(
                                            vidTmc.getSelectedItem().toString().equals(UtilSkladHO.SKLAD_VID_1) ? 1 : 2,
                                            madeTmc.getSelectedItem().toString().equals(UtilSkladHO.SKLAD_MADE_1) ? 1 : 2,
                                            Integer.valueOf(sarTmc.getText().trim()),
                                            narTmc.getText().trim().toUpperCase(),
                                            tmcName.getText().trim().toLowerCase(),
                                            edIzm.getText().trim().toLowerCase(),
                                            noteTmc.getText().trim(),
                                            Integer.valueOf(groupNum.getText().trim()),
                                            Double.valueOf(priceTmc.getText().trim().replace(",", ".")),
                                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                            Integer.valueOf(user.getIdEmployee()));
                                } else if (TYPE.equals(UtilSkladHO.TYPE_EDIT)) {
                                    save = spdb.editTmcSkladHO(idTmc,
                                            vidTmc.getSelectedItem().toString().equals(UtilSkladHO.SKLAD_VID_1) ? 1 : 2,
                                            madeTmc.getSelectedItem().toString().equals(UtilSkladHO.SKLAD_MADE_1) ? 1 : 2,
                                            Integer.valueOf(sarTmc.getText().trim()),
                                            narTmc.getText().trim().toUpperCase(),
                                            tmcName.getText().trim().toLowerCase(),
                                            edIzm.getText().trim().toLowerCase(),
                                            noteTmc.getText().trim(),
                                            Integer.valueOf(groupNum.getText().trim()),
                                            Integer.valueOf(user.getIdEmployee()));
                                }

                                if (save) {
                                    if (TYPE.equals(UtilSkladHO.TYPE_ADD))
                                        JOptionPane.showMessageDialog(null, "Запись успешно добавлена! ", "Завершено", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                                    else if (TYPE.equals(UtilSkladHO.TYPE_EDIT))
                                        JOptionPane.showMessageDialog(null, "Запись успешно сохранена! ", "Завершено", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                                    UtilSkladHO.BUTT_ACTION_EDIT = true;

                                    dispose();
                                }

                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                            } finally {
                                spdb.disConn();
                            }
                            return null;
                        }

                        protected void done() {
                            pb.dispose();
                        }
                    };
                    sw.execute();
                    pb.setVisible(true);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
