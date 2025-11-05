package dept.sklad.ho;

import by.march8.ecs.MainController;
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
@SuppressWarnings("all")
public class CenaTMCItemForm extends javax.swing.JDialog {
    private User user = User.getInstance();

    private SkladHOPDB spdb;
    private JPanel osnova;
    private JPanel upPanel;
    private JPanel buttPanel;
    private JLabel tmcName;
    private JLabel numTmc;
    private JLabel sarTmc;
    private JLabel narTmc;
    private JLabel vidTmc;
    private JLabel madeTmc;
    private JLabel title;
    private JLabel vvodEmpl;
    private JLabel vvodDate;
    private JLabel insEmpl;
    private JLabel insDate;
    private JButton buttSave;
    private JButton buttClose;
    private JButton buttTMC;
    private JTextField priceTmc;
    private JDateChooser stDate;

    private boolean EDIT;
    private String TYPE;
    private int idTmc;

    private ProgressBar pb;
    private JTextPane noteTmc;
    private MainController controller;

    public CenaTMCItemForm(MainController mainController, boolean modal) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Новая цена");
        TYPE = UtilSkladHO.TYPE_ADD;
        setPreferredSize(new Dimension(600, 210));

        init();

        EDIT = true;

        stDate.setDate((Calendar.getInstance()).getTime());

        try {
            if (!UtilSkladHO.DATE_VVOD.equals(""))
                if ((new SimpleDateFormat("MM.yyyy").format(stDate.getDate())).equals(UtilSkladHO.DATE_VVOD.substring(3)))
                    stDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(UtilSkladHO.DATE_VVOD));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public CenaTMCItemForm(MainController mainController, boolean modal, Vector dataTmc) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Новая цена");
        TYPE = UtilSkladHO.TYPE_ADD;
        setPreferredSize(new Dimension(600, 210));

        init();

        EDIT = true;

        stDate.setDate((Calendar.getInstance()).getTime());

        try {
            if (!UtilSkladHO.DATE_VVOD.equals(""))
                if ((new SimpleDateFormat("MM.yyyy").format(stDate.getDate())).equals(UtilSkladHO.DATE_VVOD.substring(3)))
                    stDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(UtilSkladHO.DATE_VVOD));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            numTmc.setText(dataTmc.get(0).toString());
            tmcName.setText(dataTmc.get(1).toString());
            vidTmc.setText(dataTmc.get(2).toString());
            madeTmc.setText(dataTmc.get(3).toString());
            sarTmc.setText(dataTmc.get(4).toString());
            narTmc.setText(dataTmc.get(5).toString());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public CenaTMCItemForm(MainController mainController, boolean modal, Vector dataTmc, String str) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Цена");
        TYPE = UtilSkladHO.TYPE_OPEN;
        setPreferredSize(new Dimension(600, 240));

        this.idTmc = idTmc;

        init();

        EDIT = false;

        upPanel.add(new JLabel("Дата корр.:"), ParagraphLayout.NEW_PARAGRAPH);
        upPanel.add(insDate);
        upPanel.add(new JLabel("    Автор:"));
        upPanel.add(insEmpl);

        buttSave.setVisible(false);

        priceTmc.setEnabled(false);
        stDate.setEnabled(false);

        try {
            numTmc.setText(dataTmc.get(0).toString());
            tmcName.setText(dataTmc.get(1).toString());
            vidTmc.setText(dataTmc.get(2).toString());
            madeTmc.setText(dataTmc.get(3).toString());
            sarTmc.setText(dataTmc.get(4).toString());
            narTmc.setText(dataTmc.get(5).toString());
            priceTmc.setText(dataTmc.get(6).toString());
            stDate.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(dataTmc.get(7).toString()));
            insEmpl.setText(dataTmc.get(8).toString());
            insDate.setText(dataTmc.get(9).toString());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        setLocationRelativeTo(controller.getMainForm());
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

        setMinimumSize(new Dimension(600, 230));

        osnova = new JPanel();
        buttPanel = new JPanel();
        upPanel = new JPanel();
        tmcName = new JLabel();
        numTmc = new JLabel();
        sarTmc = new JLabel();
        narTmc = new JLabel();
        vidTmc = new JLabel();
        madeTmc = new JLabel();
        noteTmc = new JTextPane();
        vvodEmpl = new JLabel();
        vvodDate = new JLabel();
        insEmpl = new JLabel();
        insDate = new JLabel();
        priceTmc = new JTextField();
        stDate = new JDateChooser();
        title = new JLabel("ТМЦ");
        buttTMC = new JButton("ТМЦ");
        buttSave = new JButton("Сохранить");
        buttClose = new JButton("Закрыть");

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        upPanel.setLayout(new ParagraphLayout());
        buttPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        tmcName.setPreferredSize(new Dimension(400, 20));
        numTmc.setPreferredSize(new Dimension(80, 20));
        vidTmc.setPreferredSize(new Dimension(180, 20));
        madeTmc.setPreferredSize(new Dimension(180, 20));
        sarTmc.setPreferredSize(new Dimension(180, 20));
        narTmc.setPreferredSize(new Dimension(180, 20));
        noteTmc.setPreferredSize(new Dimension(180, 30));
        vvodEmpl.setPreferredSize(new Dimension(240, 20));
        vvodDate.setPreferredSize(new Dimension(80, 20));
        insEmpl.setPreferredSize(new Dimension(240, 20));
        insDate.setPreferredSize(new Dimension(120, 20));
        priceTmc.setPreferredSize(new Dimension(180, 20));
        stDate.setPreferredSize(new Dimension(180, 20));

        tmcName.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        numTmc.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        sarTmc.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        narTmc.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        priceTmc.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        vidTmc.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        madeTmc.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        vvodEmpl.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        vvodDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        insEmpl.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        insDate.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        noteTmc.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 18));

        priceTmc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textTableSearchKeyPressed(evt);
            }
        });

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

        buttTMC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTMCActionPerformed(evt);
            }
        });

        if (TYPE.equals(UtilSkladHO.TYPE_ADD))
            upPanel.add(buttTMC);
        else
            upPanel.add(new JLabel("ТМЦ:"));
        upPanel.add(numTmc);
        upPanel.add(tmcName);
        //upPanel.add(new JLabel("Вид:"), ParagraphLayout.NEW_PARAGRAPH);
        // upPanel.add(vidTmc);
        // upPanel.add(new JLabel("    Пр-во:"));
        //   upPanel.add(madeTmc);
        upPanel.add(new JLabel("Артикул:"), ParagraphLayout.NEW_PARAGRAPH);
        upPanel.add(narTmc);
        upPanel.add(new JLabel("    Шифр:"));
        upPanel.add(sarTmc);
        upPanel.add(new JLabel("Дата :"), ParagraphLayout.NEW_PARAGRAPH);
        upPanel.add(stDate);
        upPanel.add(new JLabel("    Цена:"));
        upPanel.add(priceTmc);

        buttPanel.add(buttClose);
        buttPanel.add(buttSave);

        osnova.add(upPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void buttSaveActionPerformed(ActionEvent evt) {
        try {
            if (EDIT) {
                boolean saveFlag = true;
                String str = "";

                try {
                    if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate()))) {
                        UtilSkladHO.DATE_VVOD = new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate());
                        UtilFunctions.setSettingPropFile(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate()), UtilSkladHO.SETTING_DATE_VVOD);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка сохранения в файл настроек даты ввода! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                try {
                    if (numTmc.getText().trim().equals("") || numTmc.getText().trim().equals("-1")) {
                        saveFlag = false;
                        str += "Код ТМЦ задан некорректно!\n";
                    } else
                        Integer.valueOf(numTmc.getText().trim());
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Код ТМЦ задан некорректно!\n";
                }

                try {
                    if (priceTmc.getText().trim().equals("")) {
                        saveFlag = false;
                        str += "Цена ТМЦ задана некорректно!\n";
                    } else
                        Double.valueOf(priceTmc.getText().trim().replace(",", "."));
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Цена ТМЦ задана некорректно!\n";
                }

                if (!UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate()))) {
                    saveFlag = false;
                }

                if (!saveFlag) {
                    JOptionPane.showMessageDialog(null, str, "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                }

                if (saveFlag) {
                    try {
                        spdb = new SkladHOPDB();

                        if (spdb.addPrice(
                                Integer.valueOf(numTmc.getText().trim()),
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate())),
                                Double.valueOf(priceTmc.getText().trim().replace(",", ".")),
                                Integer.valueOf(user.getIdEmployee()))) {
                            JOptionPane.showMessageDialog(null, "Запись успешно добавлена! ", "Завершено", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                            UtilSkladHO.BUTT_ACTION_EDIT = true;

                            dispose();
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        spdb.disConn();
                    }

                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTMCActionPerformed(ActionEvent evt) {
        try {
            new SpravTMCSkHOForm(controller, true, new SimpleDateFormat("dd-MM-yyyy").format(stDate.getDate()));

            if (UtilSkladHO.BUTT_ACTION_SELECT_SPRAV) {

                tmcName.setText(UtilSkladHO.SPRAV_TMC_NAME);
                numTmc.setText(String.valueOf(UtilSkladHO.SPRAV_TMC_ID));
                sarTmc.setText(String.valueOf(UtilSkladHO.SPRAV_TMC_SAR));
                narTmc.setText(UtilSkladHO.SPRAV_TMC_NAR);
                vidTmc.setText(UtilSkladHO.SPRAV_TMC_VID);
                madeTmc.setText(UtilSkladHO.SPRAV_TMC_MADE);
            }

        } catch (Exception e) {
            tmcName.setText("");
            numTmc.setText("");
            sarTmc.setText("");
            narTmc.setText("");
            vidTmc.setText("");
            madeTmc.setText("");

            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void textTableSearchKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == evt.VK_ENTER) buttSave.doClick();
    }
}
