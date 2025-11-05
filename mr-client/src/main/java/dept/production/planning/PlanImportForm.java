package dept.production.planning;

import by.march8.ecs.framework.common.LogCrutch;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.Item;
import common.ProgressBar;
import common.User;
import common.UtilFunctions;
import dept.MyReportsModule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author lidashka
 */
public class PlanImportForm extends javax.swing.JDialog {

    /*/ private static final org.apache.log4j.Logger log = new Log().getLoger(PlanImportForm.class);
    private static final LogCrutch log = new LogCrutch();
     */
    private static final LogCrutch log = new LogCrutch();
    PlanPDB ppdb;
    PlanDB pdb;
    private User user = User.getInstance();
    private ArrayList list = new ArrayList();
    private ProgressBar pb;
    private JPanel osnova;
    private JPanel centrPanel;
    private JPanel buttPanel;
    private JButton buttImport;
    private JButton buttClose;
    private JButton buttAdd;
    private JTextField nameText;
    private JTextPane noteText;
    private JLabel dbfLabelText;
    private JLabel title;
    private JComboBox dept;
    private JDateChooser sStDate;
    private ButtonGroup buttonGroup;
    private JRadioButton jRadioButton1;
    private JRadioButton jRadioButton2;
    private JRadioButton jRadioButton3;
    private ButtonGroup buttonTipGroup;
    private JRadioButton jRadioButton4;
    private JRadioButton jRadioButton5;
    private JRadioButton jRadioButton6;
    private int idPlan;
    private JCheckBox specBox;
    private String description;

    public PlanImportForm(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        init();

        try {
            dept.setSelectedItem(UtilFunctions.getItemsModel(UtilPlan.DEPT_MODEL, UtilPlan.DEPT_SELECT_ITEM));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        java.util.Date d = (Calendar.getInstance()).getTime();
        d.setDate(1);
        sStDate.setDate(d);

        setResizable(false);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void init() {
        setTitle("Импорт плана производства");

        setPreferredSize(new Dimension(580, 400));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        osnova = new JPanel();
        centrPanel = new JPanel();
        buttPanel = new JPanel();
        buttImport = new JButton("Загрузить");
        buttAdd = new JButton("Добавить");
        buttClose = new JButton("Закрыть");
        nameText = new JTextField();
        sStDate = new JDateChooser();
        noteText = new JTextPane();
        dbfLabelText = new JLabel("не выбран");
        title = new JLabel("Импорт плана производства из DBF-файлов");
        dept = new JComboBox(UtilPlan.DEPT_MODEL);
        buttonGroup = new ButtonGroup();
        buttonTipGroup = new ButtonGroup();
        jRadioButton1 = new JRadioButton();
        jRadioButton2 = new JRadioButton();
        jRadioButton3 = new JRadioButton();
        jRadioButton4 = new JRadioButton();
        jRadioButton5 = new JRadioButton();
        jRadioButton6 = new JRadioButton();
        specBox = new JCheckBox();

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centrPanel.setLayout(new ParagraphLayout());
        buttPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        nameText.setPreferredSize(new Dimension(400, 20));
        dept.setPreferredSize(new Dimension(230, 20));
        sStDate.setPreferredSize(new Dimension(120, 20));
        noteText.setPreferredSize(new Dimension(400, 60));
        dbfLabelText.setPreferredSize(new Dimension(300, 20));

        buttonGroup.add(jRadioButton1);
        buttonGroup.add(jRadioButton2);
        buttonGroup.add(jRadioButton3);

        buttonTipGroup.add(jRadioButton4);
        buttonTipGroup.add(jRadioButton5);
        buttonTipGroup.add(jRadioButton6);

        jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton4.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton5.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton6.setFont(new java.awt.Font("Dialog", 0, 13));

        jRadioButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jRadioButton1.setText("Главный;");
        jRadioButton2.setText("Проект;");
        jRadioButton3.setText("Копия;");
        jRadioButton4.setText("План;");
        jRadioButton5.setText("Выпуск;");
        jRadioButton6.setText("DBF - Широкой;");

        jRadioButton1.setActionCommand("0");
        jRadioButton2.setActionCommand("1");
        jRadioButton3.setActionCommand("2");
        jRadioButton4.setActionCommand(UtilPlan.PLAN);
        jRadioButton5.setActionCommand(UtilPlan.VYPUSK);
        jRadioButton6.setActionCommand(UtilPlan.VYPUSK_TL);

        jRadioButton1.setSelected(true);
        jRadioButton4.setSelected(true);
        specBox.setSelected(true);

        buttAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttAddtActionPerformed(evt);
            }
        });

        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseActionPerformed(evt);
            }
        });

        buttImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttImportActionPerformed(evt);
            }
        });

        dept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deptActionPerformed(evt);
            }
        });

        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 18));

        centrPanel.add(new JLabel("Название:"));
        centrPanel.add(nameText);
        centrPanel.add(new JLabel("Дата:"), ParagraphLayout.NEW_PARAGRAPH);
        centrPanel.add(sStDate);
        centrPanel.add(new JLabel("     Цех:"));
        centrPanel.add(dept);
        centrPanel.add(new JLabel("Статус:"), ParagraphLayout.NEW_PARAGRAPH);
        centrPanel.add(jRadioButton1);
        centrPanel.add(jRadioButton2);
        centrPanel.add(jRadioButton3);
        centrPanel.add(new JLabel("Тип:"), ParagraphLayout.NEW_PARAGRAPH);
        centrPanel.add(jRadioButton4);
        centrPanel.add(jRadioButton5);
        centrPanel.add(jRadioButton6);
        centrPanel.add(specBox, ParagraphLayout.NEW_PARAGRAPH);
        centrPanel.add(new JLabel("Обновить спецификации;"));
        centrPanel.add(new JLabel("DBF-файл:"), ParagraphLayout.NEW_PARAGRAPH);
        centrPanel.add(dbfLabelText);
        centrPanel.add(buttImport);
        centrPanel.add(new JLabel("Примечание:"), ParagraphLayout.NEW_PARAGRAPH);
        centrPanel.add(new JScrollPane(noteText));

        buttPanel.add(buttClose);
        buttPanel.add(buttAdd);

        osnova.add(title, BorderLayout.NORTH);
        osnova.add(centrPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();

    }

    private void buttAddtActionPerformed(ActionEvent evt) {
        try {
            boolean saveFlag = true;
            String str = "";
            idPlan = -1;

            if (((Item) dept.getSelectedItem()).getId() == -1) {
                saveFlag = false;
                str += "Вы не выбрали цех!\n";
            }

            if (nameText.getText().trim().equals("")) {
                saveFlag = false;
                str += "Вы не ввели название плана!\n";
            }

            if (dbfLabelText.getText().trim().equals("не выбран")) {
                saveFlag = false;
                str += "Вы не выбрали dbf-файл для загрузки плана!\n";
            }

            if (!UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate()))) {
                saveFlag = false;
            }

            if (!saveFlag) {
                JOptionPane.showMessageDialog(null, str, "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            }

            if (saveFlag) {
                try {
                    if (saveFlag && list.size() > 0) {
                        pb = new ProgressBar(PlanImportForm.this, false, "Загрузка плана в базу данных ...");
                        SwingWorker sw = new SwingWorker() {
                            protected Object doInBackground() {
                                try {
                                    ppdb = new PlanPDB();

                                    if (buttonTipGroup.getSelection().getActionCommand().equals(UtilPlan.PLAN)) {
                                        nameText.setText("План " + nameText.getText().trim());

                                    } else if (buttonTipGroup.getSelection().getActionCommand().equals(UtilPlan.VYPUSK)) {
                                        nameText.setText("Выпуск " + nameText.getText().trim());

                                    } else if (buttonTipGroup.getSelection().getActionCommand().equals(UtilPlan.VYPUSK_TL)) {
                                        nameText.setText("Выпуск " + nameText.getText().trim());
                                    }

                                    idPlan = ppdb.addPlan(nameText.getText().trim(),
                                            ((Item) dept.getSelectedItem()).getId(),
                                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                                            Integer.valueOf(buttonGroup.getSelection().getActionCommand()),
                                            noteText.getText().trim(),
                                            list,
                                            Integer.valueOf(user.getIdEmployee()));


                                    if (idPlan > -1 && specBox.isSelected()) {
                                        pb.setMessage("Обновление спецификаций плана №" + idPlan + " ...");
                                        ppdb.updateSpecInPlanProduction(idPlan);
                                    }

                                    JOptionPane.showMessageDialog(null,
                                            "План производства №" + idPlan + " успешно загружен! ",
                                            "Завершено",
                                            javax.swing.JOptionPane.INFORMATION_MESSAGE);

                                    nameText.setText("");

                                } catch (Exception e) {
                                    nameText.setText("");

                                    JOptionPane.showMessageDialog(null,
                                            "Ошибка. " + e.getMessage(),
                                            "Ошибка",
                                            javax.swing.JOptionPane.ERROR_MESSAGE);
                                } finally {
                                    ppdb.disConn();
                                }
                                return null;
                            }

                            protected void done() {
                                pb.dispose();
                            }
                        };
                        sw.execute();
                        pb.setVisible(true);

                    } else {
                        JOptionPane.showMessageDialog(null, "DBF-файл для загрузки плана пуст!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void buttImportActionPerformed(ActionEvent evt) {
        list = new ArrayList();
        dbfLabelText.setText("");

        if (buttonTipGroup.getSelection().getActionCommand().equals(UtilPlan.PLAN)) {
            description = "plan";

        } else if (buttonTipGroup.getSelection().getActionCommand().equals(UtilPlan.VYPUSK)) {
            description = "pr";

        } else if (buttonTipGroup.getSelection().getActionCommand().equals(UtilPlan.VYPUSK_TL)) {
            description = "zatry";
        }

        final JFileChooser fc = new JFileChooser(MyReportsModule.dbfDVIPath.replace("nfs", "misc"));
        fc.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
        fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f != null) {
                    if (f.isDirectory()) {
                        return true;
                    }
                }
                if (f.getName().endsWith(".DBF") && f.getName().startsWith(description.toUpperCase())
                ) {
                    return true;
                }
                return f.getName().endsWith(".dbf") && f.getName().startsWith(description.toLowerCase());
            }

            @Override
            public String getDescription() {
                return description;
            }
        });
        fc.setAcceptAllFileFilterUsed(false);

        if (fc.showDialog(PlanImportForm.this, null) == JFileChooser.APPROVE_OPTION) {
            dbfLabelText.setText(fc.getSelectedFile().getName());

            if (fc.getSelectedFile().exists()) {
                if (fc.getSelectedFile().getName().toLowerCase().endsWith(".dbf") &&
                        fc.getSelectedFile().getName().toLowerCase().startsWith(description)) {
                    pb = new ProgressBar(PlanImportForm.this, false, "Проверка и загрузка DBF-файла ...");
                    SwingWorker sw = new SwingWorker() {
                        protected Object doInBackground() {
                            PlanDBF pdbf = new PlanDBF();
                            try {
                                if (buttonTipGroup.getSelection().getActionCommand().equals(UtilPlan.PLAN)) {
                                    list = pdbf.readPlan(fc.getSelectedFile().getPath());

                                } else if (buttonTipGroup.getSelection().getActionCommand().equals(UtilPlan.VYPUSK)) {
                                    list = pdbf.readVypusk(fc.getSelectedFile().getPath());

                                } else if (buttonTipGroup.getSelection().getActionCommand().equals(UtilPlan.VYPUSK_TL)) {
                                    list = pdbf.readVypuskTL(fc.getSelectedFile().getPath());
                                }
                            } catch (Exception ex) {
                                list = new ArrayList();
                                log.error("Ошибка чтения дбф плана: ", ex);
                                JOptionPane.showMessageDialog(null,
                                        "Ошибка чтения дбф отгрузка: " + ex.getMessage(),
                                        "Ошибка!!!",
                                        javax.swing.JOptionPane.ERROR_MESSAGE);

                            }
                            
                            /*
                            pb.setMessage("Поиск наименований..." );
                            
                            Map tmpMap = new HashMap();
                            try {
                                pdb = new PlanDB();
                                
                                for(int i=0; i<list.size(); i++){
                                    Object[] itemRow = (Object[]) list.get(i);
                                    
                                    if(tmpMap.get(itemRow[1].toString().trim()) == null){                                        
                                        itemRow[7] = pdb.getNameModel(Integer.valueOf(itemRow[1].toString()));                                        
                                    } else {
                                        itemRow[7] = tmpMap.get(itemRow[1].toString().trim());
                                    } 
                                    
                                    list.set(i, itemRow);                                    
                                    
                                }
                                
                            } catch (Exception e) {
                                
                            } finally{
                                pdb.disConn();                            
                            }
                            */
                            return null;
                        }

                        protected void done() {
                            pb.dispose();
                        }
                    };
                    sw.execute();
                    pb.setVisible(true);
                } else {
                    dbfLabelText.setText("не выбран");
                    JOptionPane.showMessageDialog(null, "Некорректный файл!", "Ошибка!", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            } else {
                dbfLabelText.setText("не выбран");
                JOptionPane.showMessageDialog(null, "Файл не существует!", "Ошибка!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deptActionPerformed(ActionEvent evt) {
        try {
            UtilPlan.DEPT_SELECT_ITEM = ((Item) dept.getSelectedItem()).getId();
            UtilFunctions.setSettingPropFile(String.valueOf(((Item) dept.getSelectedItem()).getId()), UtilPlan.SETTING_DEPT_SELECT_ITEM);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}

