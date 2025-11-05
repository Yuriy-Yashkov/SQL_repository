package dept.sklad.ho;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.Item;
import common.User;
import common.UtilFunctions;
import dept.sprav.employe.EmployeForm;
import dept.sprav.employe.UtilEmploye;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class SkladMoveTMCItemForm extends javax.swing.JDialog {
    private User user = User.getInstance();

    private SkladHOPDB spdb;
    private JPanel osnova;
    private JPanel mainPanel;
    private JPanel buttPanel;
    private JPanel centrePanel;
    private JPanel rightPanel;
    private JPanel leftPanel;
    private JPanel upPanel;
    private JPanel downPanel;
    private JLabel tmcNar;
    private JLabel numTmc;
    private JLabel nameTmc;
    private JLabel edIzmTmc;
    private JLabel vidTmc;
    private JLabel vvodEmpl;
    private JLabel vvodDate;
    private JLabel insEmpl;
    private JLabel insDate;
    private JLabel numEmpl;
    private JLabel emplName;
    private JLabel numProd;
    private JLabel prodName;
    private JComboBox dept;
    private JComboBox tip;
    private JTextField doc;
    private JTextField part;
    private JTextField kolvoF;
    private JTextField kolvoK;
    private JLabel kolvo;
    private JLabel title;
    private JCheckBox print;
    private JButton buttSave;
    private JButton buttCreate;
    private JButton buttClose;
    private JButton buttTMC;
    private JButton buttEmpl;
    private JButton buttProd;
    private JButton buttKolvoK;
    private JButton buttKolvoF;
    private JDateChooser stDate;
    private ButtonGroup buttonGroup;
    private JRadioButton jRadioButton2;
    private JRadioButton jRadioButton3;
    private JRadioButton jRadioButton4;
    private JTextPane note;
    private int idMove;

    private boolean EDIT;
    private String TYPE;

    private Vector dataItemsTMC;
    private Vector colItemsTMC;
    private JTable tableItemsTMC;
    private DefaultTableModel tModelItemsTMC;
    private JPanel leftTableTMC;
    private MainController controller;

    public SkladMoveTMCItemForm(MainController mainController, boolean modal) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Новое движение по складу");
        setPreferredSize(new Dimension(900, 410));

        TYPE = UtilSkladHO.TYPE_ADD;

        init();

        EDIT = true;

        stDate.setDate((Calendar.getInstance()).getTime());
        try {
            if (!UtilSkladHO.DATE_VVOD.equals(""))
                // if((new SimpleDateFormat("MM.yyyy").format(stDate.getDate())).equals(UtilSkladHO.DATE_VVOD.substring(3)))
                stDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(UtilSkladHO.DATE_VVOD));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            Item item = UtilFunctions.getItemsModel(UtilSkladHO.DEPT_MODEL, UtilSkladHO.DEPT_SELECT_ITEM);

            if (item != null)
                dept.setSelectedItem(item);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            Item item = UtilFunctions.getItemsModel(UtilSkladHO.TIP_MOVE_MODEL, UtilSkladHO.TIP_MOVE_SELECT_ITEM);

            if (item != null)
                tip.setSelectedItem(item);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public SkladMoveTMCItemForm(SkladProductItemForm parent, boolean modal, String date,
                                int idDept, String idEmpl, String nameEmpl, String status, Vector items) {

        super(parent, modal);
        controller = parent.getController();
        setTitle("Список новых движений по складу.");
        setPreferredSize(new Dimension(900, 410));

        TYPE = UtilSkladHO.TYPE_ADD_BIG_RETURN;

        spdb = ((SkladProductItemForm) parent).spdb;
        dataItemsTMC = items;

        init();
        initListTMC();

        EDIT = true;

        dept.setEnabled(false);
        buttEmpl.setEnabled(false);
        buttProd.setEnabled(false);

        try {
            if (jRadioButton2.getActionCommand().equals(status))
                jRadioButton2.setSelected(true);
            else if (jRadioButton3.getActionCommand().equals(status))
                jRadioButton3.setSelected(true);
            else if (jRadioButton4.getActionCommand().equals(status))
                jRadioButton4.setSelected(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        stDate.setDate((Calendar.getInstance()).getTime());
        try {
            if (!date.equals(""))
                stDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(date));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            Item item = UtilFunctions.getItemsModel(UtilSkladHO.DEPT_MODEL, idDept);

            if (item != null)
                dept.setSelectedItem(item);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            Item item = UtilFunctions.getItemsModel(UtilSkladHO.TIP_MOVE_MODEL, 3);

            if (item != null)
                tip.setSelectedItem(item);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            numEmpl.setText(idEmpl);
            emplName.setText(nameEmpl);
        } catch (Exception e) {
            numEmpl.setText("");
            emplName.setText("");
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            numProd.setText("");
            prodName.setText("");
        } catch (Exception e) {
            numProd.setText("");
            prodName.setText("");
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        centrePanel.remove(leftPanel);
        centrePanel.add(leftTableTMC, 0);

        buttSave.setVisible(false);
        buttPanel.remove(buttSave);
        buttPanel.add(buttCreate);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public SkladMoveTMCItemForm(SkladProductItemForm parent, boolean modal, String date,
                                int tmcId_, String tmcName_, String tmcNar_, String tmcVid_, String tmcIzm,
                                int idDept, String idEmpl, String nameEmpl, String status) {
        super(parent, modal);
        setTitle("Новое движение по складу");
        setPreferredSize(new Dimension(900, 410));

        TYPE = UtilSkladHO.TYPE_ADD_RETURN;
        controller = parent.getController();

        spdb = ((SkladProductItemForm) parent).spdb;

        init();

        EDIT = true;

        //tip.setEnabled(false);
        dept.setEnabled(false);
        buttEmpl.setEnabled(false);
        buttProd.setEnabled(false);

        try {
            if (jRadioButton2.getActionCommand().equals(status))
                jRadioButton2.setSelected(true);
            else if (jRadioButton3.getActionCommand().equals(status))
                jRadioButton3.setSelected(true);
            else if (jRadioButton4.getActionCommand().equals(status))
                jRadioButton4.setSelected(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        stDate.setDate((Calendar.getInstance()).getTime());
        try {
            if (!date.equals(""))
                stDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(date));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            Item item = UtilFunctions.getItemsModel(UtilSkladHO.DEPT_MODEL, idDept);

            if (item != null)
                dept.setSelectedItem(item);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            Item item = UtilFunctions.getItemsModel(UtilSkladHO.TIP_MOVE_MODEL, 3);

            if (item != null)
                tip.setSelectedItem(item);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            if (tmcId_ > -1) {
                numTmc.setText(String.valueOf(tmcId_));
                nameTmc.setText(tmcName_);
                tmcNar.setText(tmcNar_);
                vidTmc.setText(tmcVid_);
                edIzmTmc.setText(tmcIzm);
            }
        } catch (Exception e) {
            numTmc.setText("");
            nameTmc.setText("");
            tmcNar.setText("");
            vidTmc.setText("");
            edIzmTmc.setText("");
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            numEmpl.setText(idEmpl);
            emplName.setText(nameEmpl);
        } catch (Exception e) {
            numEmpl.setText("");
            emplName.setText("");
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            numProd.setText("");
            prodName.setText("");
        } catch (Exception e) {
            numProd.setText("");
            prodName.setText("");
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public SkladMoveTMCItemForm(MainController mainController, boolean modal, Vector dataEdit) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Новое движение по складу");
        setPreferredSize(new Dimension(900, 410));

        TYPE = UtilSkladHO.TYPE_ADD;

        init();

        EDIT = true;

        stDate.setDate((Calendar.getInstance()).getTime());
        try {
            if (!UtilSkladHO.DATE_VVOD.equals(""))
                // if((new SimpleDateFormat("MM.yyyy").format(stDate.getDate())).equals(UtilSkladHO.DATE_VVOD.substring(3)))
                stDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(UtilSkladHO.DATE_VVOD));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            Item item = UtilFunctions.getItemsModel(UtilSkladHO.DEPT_MODEL, UtilSkladHO.DEPT_SELECT_ITEM);

            if (item != null)
                dept.setSelectedItem(item);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            Item item = UtilFunctions.getItemsModel(UtilSkladHO.TIP_MOVE_MODEL, UtilSkladHO.TIP_MOVE_SELECT_ITEM);

            if (item != null)
                tip.setSelectedItem(item);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {

            stDate.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(dataEdit.get(2).toString()));
            tip.setSelectedItem(UtilFunctions.getItemsModel(UtilSkladHO.TIP_MOVE_MODEL, Integer.valueOf(dataEdit.get(3).toString())));
            numTmc.setText(dataEdit.get(5).toString());
            nameTmc.setText(dataEdit.get(6).toString());
            tmcNar.setText(dataEdit.get(7).toString());
            vidTmc.setText(dataEdit.get(9).toString());
            edIzmTmc.setText(dataEdit.get(11).toString());
            kolvoF.setText(dataEdit.get(12).toString());
            kolvoK.setText(dataEdit.get(13).toString());
            part.setText(dataEdit.get(14).toString());
            dept.setSelectedItem(UtilFunctions.getItemsModel(UtilSkladHO.DEPT_MODEL, Integer.valueOf(dataEdit.get(18).toString())));
            numEmpl.setText(dataEdit.get(20).toString());
            emplName.setText(dataEdit.get(21).toString());
            doc.setText(dataEdit.get(22).toString());
            note.setText(dataEdit.get(23).toString());

            switch (Integer.valueOf(dataEdit.get(30).toString())) {
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

            buttKolvoK.doClick();
            kolvo.setText(UtilSkladHO.SPRAV_TMC_VID.equals(UtilSkladHO.SKLAD_VID_1) ? UtilSkladHO.KOLVO_MASS : UtilSkladHO.KOLVO_KOL);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            dispose();
        }

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public SkladMoveTMCItemForm(MainController mainController, boolean modal, Vector dataEdit, String type) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setTitle("Движение по складу №" + dataEdit.get(1).toString());
        setPreferredSize(new Dimension(900, 470));

        TYPE = type;

        init();

        EDIT = true;

        downPanel.add(new JLabel("Дата ввода:"), ParagraphLayout.NEW_PARAGRAPH);
        downPanel.add(vvodDate);
        downPanel.add(new JLabel("    Автор:"));
        downPanel.add(vvodEmpl);
        downPanel.add(new JLabel("Дата корр.:"), ParagraphLayout.NEW_PARAGRAPH);
        downPanel.add(insDate);
        downPanel.add(new JLabel("    Автор:"));
        downPanel.add(insEmpl);

        stDate.setDate((Calendar.getInstance()).getTime());
        try {
            if (!UtilSkladHO.DATE_VVOD.equals(""))
                //  if((new SimpleDateFormat("MM.yyyy").format(stDate.getDate())).equals(UtilSkladHO.DATE_VVOD.substring(3)))
                stDate.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(UtilSkladHO.DATE_VVOD));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            Item item = UtilFunctions.getItemsModel(UtilSkladHO.DEPT_MODEL, UtilSkladHO.DEPT_SELECT_ITEM);

            if (item != null)
                dept.setSelectedItem(item);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            Item item = UtilFunctions.getItemsModel(UtilSkladHO.TIP_MOVE_MODEL, UtilSkladHO.TIP_MOVE_SELECT_ITEM);

            if (item != null)
                tip.setSelectedItem(item);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {

            if (TYPE.equals(UtilSkladHO.TYPE_EDIT_RETURN)) {
                spdb = new SkladHOPDB();

                //tip.setEnabled(false);
                dept.setEnabled(false);
                buttEmpl.setEnabled(false);
                buttProd.setEnabled(false);
            }

            idMove = Integer.valueOf(dataEdit.get(1).toString());
            stDate.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(dataEdit.get(2).toString()));
            tip.setSelectedItem(UtilFunctions.getItemsModel(UtilSkladHO.TIP_MOVE_MODEL, Integer.valueOf(dataEdit.get(3).toString())));
            numTmc.setText(dataEdit.get(5).toString());
            nameTmc.setText(dataEdit.get(6).toString());
            tmcNar.setText(dataEdit.get(7).toString());
            vidTmc.setText(dataEdit.get(9).toString());
            edIzmTmc.setText(dataEdit.get(11).toString());
            kolvoF.setText(dataEdit.get(12).toString());
            kolvoK.setText(dataEdit.get(13).toString());
            part.setText(dataEdit.get(14).toString());
            dept.setSelectedItem(UtilFunctions.getItemsModel(UtilSkladHO.DEPT_MODEL, Integer.valueOf(dataEdit.get(18).toString())));
            numEmpl.setText(dataEdit.get(20).toString());
            emplName.setText(dataEdit.get(21).toString());
            doc.setText(dataEdit.get(22).toString());
            note.setText(dataEdit.get(23).toString());
            vvodDate.setText(dataEdit.get(24).toString());
            vvodEmpl.setText(dataEdit.get(25).toString());
            insDate.setText(dataEdit.get(26).toString());
            insEmpl.setText(dataEdit.get(27).toString());
            numProd.setText(dataEdit.get(28).toString());

            switch (Integer.valueOf(dataEdit.get(30).toString())) {
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

            buttKolvoK.doClick();
            kolvo.setText(UtilSkladHO.SPRAV_TMC_VID.equals(UtilSkladHO.SKLAD_VID_1) ? UtilSkladHO.KOLVO_MASS : UtilSkladHO.KOLVO_KOL);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            dispose();
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

        setMinimumSize(new Dimension(600, 410));

        idMove = 0;

        osnova = new JPanel();
        buttPanel = new JPanel();
        upPanel = new JPanel();
        downPanel = new JPanel();
        mainPanel = new JPanel();
        centrePanel = new JPanel();
        rightPanel = new JPanel();
        leftPanel = new JPanel();
        numTmc = new JLabel();
        nameTmc = new JLabel();
        tmcNar = new JLabel();
        vidTmc = new JLabel();
        numEmpl = new JLabel();
        emplName = new JLabel();
        numProd = new JLabel();
        prodName = new JLabel();
        edIzmTmc = new JLabel();
        title = new JLabel("ТМЦ");
        kolvo = new JLabel(UtilSkladHO.KOLVO_MASS);
        kolvoF = new JTextField("0");
        kolvoK = new JTextField("0");
        part = new JTextField("0");
        doc = new JTextField();
        note = new JTextPane();
        dept = new JComboBox(UtilSkladHO.DEPT_MODEL);
        tip = new JComboBox(UtilSkladHO.TIP_MOVE_MODEL);
        stDate = new JDateChooser();
        print = new JCheckBox("Печать");
        buttTMC = new JButton("ТМЦ");
        buttProd = new JButton("Карта");
        buttKolvoK = new JButton("конд.");
        buttKolvoF = new JButton("факт.");
        buttEmpl = new JButton("Работник");
        buttSave = new JButton("Сохранить");
        buttClose = new JButton("Закрыть");
        buttonGroup = new ButtonGroup();
        jRadioButton2 = new JRadioButton();
        jRadioButton3 = new JRadioButton();
        jRadioButton4 = new JRadioButton();
        vvodEmpl = new JLabel();
        vvodDate = new JLabel();
        insEmpl = new JLabel();
        insDate = new JLabel();

        buttProd.setEnabled(false);

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
        print.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        print.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        rightPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Источник", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 2, 12)));
        leftPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Детали", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 2, 12)));

        tmcNar.setPreferredSize(new Dimension(250, 20));
        vidTmc.setPreferredSize(new Dimension(250, 20));
        nameTmc.setPreferredSize(new Dimension(250, 20));
        numTmc.setPreferredSize(new Dimension(80, 20));
        kolvoK.setPreferredSize(new Dimension(150, 20));
        numEmpl.setPreferredSize(new Dimension(80, 20));
        emplName.setPreferredSize(new Dimension(250, 20));
        numProd.setPreferredSize(new Dimension(80, 20));
        prodName.setPreferredSize(new Dimension(250, 20));
        stDate.setPreferredSize(new Dimension(120, 20));
        tip.setPreferredSize(new Dimension(150, 20));
        note.setPreferredSize(new Dimension(250, 40));
        vvodEmpl.setPreferredSize(new Dimension(240, 20));
        vvodDate.setPreferredSize(new Dimension(120, 20));
        insEmpl.setPreferredSize(new Dimension(240, 20));
        insDate.setPreferredSize(new Dimension(120, 20));

        numTmc.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        nameTmc.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        tmcNar.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        vidTmc.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        numEmpl.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        emplName.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        numProd.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        prodName.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
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

        jRadioButton2.setSelected(true);

        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 18));

        kolvoF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kolvoKKeyPressed(evt);
            }
        });

        kolvoK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kolvoFKeyPressed(evt);
            }
        });

        dept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deptActionPerformed(evt);
            }
        });

        tip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipActionPerformed(evt);
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

        buttKolvoK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttKolvoKActionPerformed(evt);
            }
        });

        buttKolvoF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttKolvoFActionPerformed(evt);
            }
        });

        buttProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttProdActionPerformed(evt);
            }
        });

        buttEmpl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttEmplActionPerformed(evt);
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

        leftPanel.add(buttTMC);
        leftPanel.add(tmcNar);
        leftPanel.add(nameTmc, ParagraphLayout.NEW_LINE);
        leftPanel.add(kolvo, ParagraphLayout.NEW_PARAGRAPH);
        leftPanel.add(buttKolvoF);
        leftPanel.add(kolvoF, ParagraphLayout.NEW_LINE_STRETCH_H);
        leftPanel.add(edIzmTmc);
        leftPanel.add(buttKolvoK, ParagraphLayout.NEW_LINE);
        leftPanel.add(kolvoK, ParagraphLayout.NEW_LINE_STRETCH_H);
        leftPanel.add(new JLabel("Кусков:"), ParagraphLayout.NEW_PARAGRAPH);
        leftPanel.add(part, ParagraphLayout.NEW_LINE_STRETCH_H);
        leftPanel.add(new JLabel("шт."));

        rightPanel.add(new JLabel("Документ:"), ParagraphLayout.NEW_PARAGRAPH);
        rightPanel.add(doc, ParagraphLayout.NEW_LINE_STRETCH_H);
        rightPanel.add(new JLabel("Цех:"), ParagraphLayout.NEW_PARAGRAPH);
        rightPanel.add(dept, ParagraphLayout.NEW_LINE_STRETCH_H);
        rightPanel.add(buttEmpl, ParagraphLayout.NEW_PARAGRAPH);
        rightPanel.add(numEmpl);
        rightPanel.add(emplName, ParagraphLayout.NEW_LINE);
        rightPanel.add(buttProd, ParagraphLayout.NEW_PARAGRAPH);
        rightPanel.add(numProd);

        centrePanel.add(leftPanel);
        centrePanel.add(rightPanel);

        downPanel.add(new JLabel("Примечание :"), ParagraphLayout.NEW_PARAGRAPH);
        downPanel.add(note, ParagraphLayout.NEW_LINE_STRETCH_H);

        mainPanel.add(upPanel, BorderLayout.NORTH);
        mainPanel.add(centrePanel, BorderLayout.CENTER);
        mainPanel.add(downPanel, BorderLayout.SOUTH);

        buttPanel.add(buttClose);
        buttPanel.add(buttSave);

        osnova.add(mainPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void initListTMC() {
        colItemsTMC = new Vector();

        colItemsTMC.add("Код TMС");
        colItemsTMC.add("Артикул");
        colItemsTMC.add("Название");
        colItemsTMC.add("Кол-во Ф");

        buttCreate = new JButton("Создать");
        buttCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCreateActionPerformed(evt);
            }
        });

        tableItemsTMC = new JTable();
        tableItemsTMC.setAutoCreateColumnsFromModel(true);
        tableItemsTMC.getTableHeader().setReorderingAllowed(false);

        tModelItemsTMC = new DefaultTableModel();

        createTableItemsTMC();
    }

    private void kolvoKKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == evt.VK_ENTER) buttKolvoK.doClick();
    }

    private void kolvoFKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == evt.VK_ENTER) buttKolvoF.doClick();
    }

    private void deptActionPerformed(ActionEvent evt) {
        try {
            UtilSkladHO.DEPT_SELECT_ITEM = ((Item) dept.getSelectedItem()).getId();
            UtilFunctions.setSettingPropFile(String.valueOf(((Item) dept.getSelectedItem()).getId()), UtilSkladHO.SETTING_DEPT_SELECT_ITEM);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tipActionPerformed(ActionEvent evt) {
        try {
            UtilSkladHO.TIP_MOVE_SELECT_ITEM = ((Item) tip.getSelectedItem()).getId();
            UtilFunctions.setSettingPropFile(String.valueOf(((Item) tip.getSelectedItem()).getId()), UtilSkladHO.SETTING_TIP_MOVE_SELECT_ITEM);

            if (((Item) tip.getSelectedItem()).getDescription().equals(UtilSkladHO.TYPE_MOVE_RS))
                rightPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Получатель", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 2, 12)));
            else
                rightPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Источник", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 2, 12)));


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        try {
            if (TYPE.equals(UtilSkladHO.TYPE_OPEN) ||
                    TYPE.equals(UtilSkladHO.TYPE_ADD_RETURN) ||
                    TYPE.equals(UtilSkladHO.TYPE_EDIT_RETURN)) {
                dispose();
            } else {
                if (JOptionPane.showOptionDialog(null, "Сохранить изменения?", "Сохранение...", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Нет") == JOptionPane.YES_OPTION)
                    buttSave.doClick();
                else {
                    dispose();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
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
                    if (!UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate()))) {
                        saveFlag = false;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                try {
                    Integer.valueOf(((Item) tip.getSelectedItem()).getId());
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Тип движения ТМЦ задан некорректно!\n";
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
                    if (!kolvoF.getText().trim().equals("")) {
                        Double.valueOf(kolvoF.getText().trim().replace(",", "."));
                        if (Double.valueOf(kolvoF.getText().trim().replace(",", ".")) == 0)
                            str += "Кол-во факт. = 0!\n";
                    } else {
                        saveFlag = false;
                        str += "Кол-во задано некорректно!\n";
                    }
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Кол-во задано некорректно!\n";
                }

                try {
                    if (!part.getText().trim().equals(""))
                        Integer.valueOf(part.getText().trim());
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Кол-во кусков задано некорректно!\n";
                }

                try {
                    Integer.valueOf(((Item) dept.getSelectedItem()).getId());
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Цех задан некорректно!\n";
                }

                try {
                    if (!numEmpl.getText().trim().equals(""))
                        Integer.valueOf(numEmpl.getText().trim());
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Код работника задан некорректно!\n";
                }

                try {
                    if (!numProd.getText().trim().equals(""))
                        Integer.valueOf(numProd.getText().trim());
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Карта раскроя задана некорректно!\n";
                }

                if (!saveFlag) {
                    JOptionPane.showMessageDialog(null, str, "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                }

                if (saveFlag) {
                    if (TYPE.equals(UtilSkladHO.TYPE_ADD_RETURN)) {
                        if (spdb.addMoveReturnTemp(
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate())),
                                Integer.valueOf(((Item) tip.getSelectedItem()).getId()),
                                Integer.valueOf(numTmc.getText().trim()),
                                Double.valueOf(kolvoF.getText().trim().replace(",", ".")),
                                part.getText().trim().equals("") ? 0 : Integer.valueOf(part.getText().trim()),
                                doc.getText().trim(),
                                note.getText().trim(),
                                Integer.valueOf(((Item) dept.getSelectedItem()).getId()) == -1 ? null : Integer.valueOf(((Item) dept.getSelectedItem()).getId()),
                                numEmpl.getText().trim().equals("") ? null : Integer.valueOf(numEmpl.getText().trim()),
                                Integer.valueOf(user.getIdEmployee()),
                                Integer.valueOf(buttonGroup.getSelection().getActionCommand()))) {
                            JOptionPane.showMessageDialog(null, "Запись успешно добавлена! ", "Завершено", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                            UtilSkladHO.BUTT_ACTION_EDIT = true;

                            dispose();
                        }
                    } else if (TYPE.equals(UtilSkladHO.TYPE_EDIT_RETURN)) {
                        if (spdb.editMoveReturnTemp(
                                idMove,
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate())),
                                Integer.valueOf(((Item) tip.getSelectedItem()).getId()),
                                Integer.valueOf(numTmc.getText().trim()),
                                Double.valueOf(kolvoF.getText().trim().replace(",", ".")),
                                part.getText().trim().equals("") ? 0 : Integer.valueOf(part.getText().trim()),
                                doc.getText().trim(),
                                note.getText().trim(),
                                Integer.valueOf(((Item) dept.getSelectedItem()).getId()) == -1 ? null : Integer.valueOf(((Item) dept.getSelectedItem()).getId()),
                                numEmpl.getText().trim().equals("") ? null : Integer.valueOf(numEmpl.getText().trim()),
                                Integer.valueOf(user.getIdEmployee()),
                                Integer.valueOf(buttonGroup.getSelection().getActionCommand()))) {
                            JOptionPane.showMessageDialog(null, "Запись успешно добавлена! ", "Завершено", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                            UtilSkladHO.BUTT_ACTION_EDIT = true;

                            dispose();
                        }
                    } else {
                        try {
                            spdb = new SkladHOPDB();

                            if (TYPE.equals(UtilSkladHO.TYPE_ADD)) {
                                if (spdb.addMove(
                                        UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate())),
                                        Integer.valueOf(((Item) tip.getSelectedItem()).getId()),
                                        Integer.valueOf(numTmc.getText().trim()),
                                        Double.valueOf(kolvoF.getText().trim().replace(",", ".")),
                                        part.getText().trim().equals("") ? 0 : Integer.valueOf(part.getText().trim()),
                                        doc.getText().trim(),
                                        note.getText().trim(),
                                        Integer.valueOf(((Item) dept.getSelectedItem()).getId()) == -1 ? null : Integer.valueOf(((Item) dept.getSelectedItem()).getId()),
                                        numEmpl.getText().trim().equals("") ? null : Integer.valueOf(numEmpl.getText().trim()),
                                        Integer.valueOf(user.getIdEmployee()),
                                        Integer.valueOf(buttonGroup.getSelection().getActionCommand()))) {
                                    JOptionPane.showMessageDialog(null, "Запись успешно добавлена! ", "Завершено", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                                    UtilSkladHO.BUTT_ACTION_EDIT = true;

                                    dispose();
                                }
                            } else if (TYPE.equals(UtilSkladHO.TYPE_EDIT)) {
                                if (spdb.editMove(
                                        idMove,
                                        UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate())),
                                        Integer.valueOf(((Item) tip.getSelectedItem()).getId()),
                                        Integer.valueOf(numTmc.getText().trim()),
                                        Double.valueOf(kolvoF.getText().trim().replace(",", ".")),
                                        part.getText().trim().equals("") ? 0 : Integer.valueOf(part.getText().trim()),
                                        doc.getText().trim(),
                                        note.getText().trim(),
                                        Integer.valueOf(((Item) dept.getSelectedItem()).getId()) == -1 ? null : Integer.valueOf(((Item) dept.getSelectedItem()).getId()),
                                        numEmpl.getText().trim().equals("") ? null : Integer.valueOf(numEmpl.getText().trim()),
                                        Integer.valueOf(user.getIdEmployee()),
                                        Integer.valueOf(buttonGroup.getSelection().getActionCommand()))) {
                                    JOptionPane.showMessageDialog(null, "Запись успешно изменена! ", "Завершено", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                                    UtilSkladHO.BUTT_ACTION_EDIT = true;

                                    dispose();
                                }
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        } finally {
                            spdb.disConn();
                        }
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCreateActionPerformed(ActionEvent evt) {
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
                    if (!UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate()))) {
                        saveFlag = false;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                try {
                    Integer.valueOf(((Item) tip.getSelectedItem()).getId());
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Тип движения ТМЦ задан некорректно!\n";
                }

                try {
                    Integer.valueOf(((Item) dept.getSelectedItem()).getId());
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Цех задан некорректно!\n";
                }

                try {
                    if (!numEmpl.getText().trim().equals(""))
                        Integer.valueOf(numEmpl.getText().trim());
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Код работника задан некорректно!\n";
                }

                try {
                    if (!numProd.getText().trim().equals(""))
                        Integer.valueOf(numProd.getText().trim());
                } catch (Exception e) {
                    saveFlag = false;
                    str += "Карта раскроя задана некорректно!\n";
                }

                if (!saveFlag) {
                    JOptionPane.showMessageDialog(null,
                            str,
                            "Внимание",
                            javax.swing.JOptionPane.WARNING_MESSAGE);
                }

                if (saveFlag) {
                    if (TYPE.equals(UtilSkladHO.TYPE_ADD_BIG_RETURN)) {
                        if (spdb.addMoveTMCTemp(
                                UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(stDate.getDate())),
                                Integer.valueOf(((Item) tip.getSelectedItem()).getId()),
                                doc.getText().trim(),
                                note.getText().trim(),
                                Integer.valueOf(((Item) dept.getSelectedItem()).getId()) == -1 ? null : Integer.valueOf(((Item) dept.getSelectedItem()).getId()),
                                numEmpl.getText().trim().equals("") ? null : Integer.valueOf(numEmpl.getText().trim()),
                                Integer.valueOf(user.getIdEmployee()),
                                Integer.valueOf(buttonGroup.getSelection().getActionCommand()),
                                dataItemsTMC)) {

                            JOptionPane.showMessageDialog(null,
                                    "Записи успешно добавлены! ",
                                    "Завершено",
                                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

                            UtilSkladHO.BUTT_ACTION_EDIT = true;

                            dispose();
                        }
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttKolvoKActionPerformed(ActionEvent evt) {
        try {
            kolvoK.setText(vidTmc.getText().trim().toLowerCase().equals(UtilSkladHO.SKLAD_VID_2) ?
                    kolvoF.getText().trim() :
                    String.valueOf(UtilSkladHO.countKolvoK(Double.valueOf(kolvoF.getText().replace(",", ".").trim()))));

        } catch (Exception e) {
            kolvoK.setText("0");
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttKolvoFActionPerformed(ActionEvent evt) {
        try {
            kolvoF.setText(vidTmc.getText().trim().toLowerCase().equals(UtilSkladHO.SKLAD_VID_2) ?
                    kolvoK.getText().trim() :
                    String.valueOf(UtilSkladHO.countKolvoF(Double.valueOf(kolvoK.getText().replace(",", ".").trim()))));

        } catch (Exception e) {
            kolvoF.setText("0");
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttProdActionPerformed(ActionEvent evt) {
        try {

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTMCActionPerformed(ActionEvent evt) {
        try {
            new SpravTMCSkHOForm(controller, true, new SimpleDateFormat("dd-MM-yyyy").format(stDate.getDate()));

            if (UtilSkladHO.BUTT_ACTION_SELECT_SPRAV) {

                tmcNar.setText(UtilSkladHO.SPRAV_TMC_NAR);
                nameTmc.setText(UtilSkladHO.SPRAV_TMC_NAME);
                numTmc.setText(String.valueOf(UtilSkladHO.SPRAV_TMC_ID));
                edIzmTmc.setText(String.valueOf(UtilSkladHO.SPRAV_TMC_EDIZM));
                vidTmc.setText(String.valueOf(UtilSkladHO.SPRAV_TMC_VID));
                kolvo.setText(UtilSkladHO.SPRAV_TMC_VID.equals(UtilSkladHO.SKLAD_VID_1) ? UtilSkladHO.KOLVO_MASS : UtilSkladHO.KOLVO_KOL);

                buttKolvoK.doClick();
            }

        } catch (Exception e) {
            tmcNar.setText("");
            nameTmc.setText("");
            numTmc.setText("");
            edIzmTmc.setText("");
            vidTmc.setText("");
            kolvo.setText(UtilSkladHO.KOLVO_MASS);

            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttEmplActionPerformed(ActionEvent evt) {
        try {
            new EmployeForm(controller, true, ((Item) dept.getSelectedItem()).getId(), "");

            numEmpl.setText(UtilEmploye.NUM);
            emplName.setText(UtilEmploye.NAIM);

        } catch (Exception e) {
            numEmpl.setText("");
            emplName.setText("");

            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createTableItemsTMC() {
        final Vector row = getDataTable(dataItemsTMC);

        tModelItemsTMC = new DefaultTableModel(row, colItemsTMC);

        tableItemsTMC.setModel(tModelItemsTMC);
        tableItemsTMC.setAutoCreateColumnsFromModel(true);

        tableItemsTMC.getColumnModel().getColumn(0).setPreferredWidth(1);

        leftTableTMC = new JPanel();
        leftTableTMC.setLayout(new BorderLayout(1, 1));
        leftTableTMC.add(new JScrollPane(tableItemsTMC), BorderLayout.CENTER);
    }

    private Vector getDataTable(Vector dataItemsTMC) {
        Vector rez = new Vector();

        try {
            for (Iterator it = dataItemsTMC.iterator(); it.hasNext(); ) {
                ItemTMC item = (ItemTMC) it.next();

                Vector tmp = new Vector();
                tmp.add(item.getId());
                tmp.add(item.getName());
                tmp.add(item.getNar());
                tmp.add(item.getKolvo());
                rez.add(tmp);
            }
        } catch (Exception e) {
            rez = new Vector();
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        return rez;
    }
}
