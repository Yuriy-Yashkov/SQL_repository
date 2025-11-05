package dept.marketing.cena;

import by.march8.ecs.framework.common.LogCrutch;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.CheckBoxHeader;
import common.ProgressBar;
import common.UtilFunctions;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;


/**
 *
 * @author lidashka
 */
public class AdvanSearchForm extends JDialog {
    // private static final Logger log = new Log().getLoger(AdvanSearchForm.class);
    private static final LogCrutch log = new LogCrutch();
    private static AdvanSearchForm instance = null;
    JPanel osnova;
    JPanel panel;
    JPanel pan;
    JPanel butt;
    JButton button1;
    JButton button2;
    JButton button3;
    JDateChooser startDatePreiscur;
    JDateChooser endDatePreiscur;
    JDateChooser startDateCenaVP;
    JDateChooser endDateCenaVP;
    JLabel title;
    JCheckBox checkboxCV;
    JCheckBox checkboxCVP;
    JCheckBox checkboxXO;
    JCheckBox checkDialog;
    JTable table;
    ProgressBar pb;
    CenaDB db;
    Vector row;
    Vector col;
    ArrayList<JTextField> arraySar;
    ArrayList<JTextField> arrayNar;
    ArrayList<JTextField> arrayFas;
    CenaForm cenaform;
    int i;

    public AdvanSearchForm(JDialog parent, boolean modal) {
        super(parent, modal);
        init();

        cenaform = (CenaForm) parent;

        java.util.Date d = (Calendar.getInstance()).getTime();
        d.setDate(1);
        startDatePreiscur.setDate(d);
        endDatePreiscur.setDate((Calendar.getInstance()).getTime());

        startDateCenaVP.setDate(d);
        endDateCenaVP.setDate((Calendar.getInstance()).getTime());

        createTablePreiscur();

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public static AdvanSearchForm getInstance(JDialog parent, boolean modal) {
        if (instance == null) {
            synchronized (AdvanSearchForm.class) {
                if (instance == null)
                    instance = new AdvanSearchForm(parent, modal);
            }
        }
        return instance;
    }

    private void init() {
        try {
            this.setTitle("Поиск по всей базе, включая неиспользуемые модели");

            setPreferredSize(new Dimension(550, 500));
            setMinimumSize(new Dimension(550, 500));

            addWindowListener(new WindowListener() {
                public void windowClosed(WindowEvent e) {
                    instance = null;
                }

                public void windowClosing(WindowEvent e) {
                    instance = null;
                }

                public void windowOpened(WindowEvent e) {
                }

                public void windowIconified(WindowEvent e) {
                }

                public void windowDeiconified(WindowEvent e) {
                }

                public void windowActivated(WindowEvent e) {
                }

                public void windowDeactivated(WindowEvent e) {
                }
            });

            osnova = new JPanel();
            panel = new JPanel();
            pan = new JPanel();
            butt = new JPanel();
            button1 = new JButton("Найти");
            button2 = new JButton("Поиск");
            button3 = new JButton("Закрыть");
            startDatePreiscur = new JDateChooser();
            endDatePreiscur = new JDateChooser();
            startDateCenaVP = new JDateChooser();
            endDateCenaVP = new JDateChooser();
            title = new JLabel("Расширенный поиск");
            checkboxCV = new JCheckBox();
            checkboxCVP = new JCheckBox();
            checkboxXO = new JCheckBox();
            checkDialog = new JCheckBox("Закрепить окно");
            table = new JTable();
            row = new Vector();
            col = new Vector();
            arraySar = new ArrayList();
            arrayNar = new ArrayList();
            arrayFas = new ArrayList();

            final JPanel panAddSar = new JPanel();
            final JPanel panAddNar = new JPanel();
            final JPanel panAddFas = new JPanel();
            JButton buttPlusSar = new JButton("+");
            JButton buttPlusNar = new JButton("+");
            JButton buttPlusFas = new JButton("+");

            osnova.setLayout(new BorderLayout(1, 1));
            osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            panel.setLayout(new ParagraphLayout());
            pan.setLayout(new ParagraphLayout());
            butt.setLayout(new GridLayout(0, 3, 10, 5));
            butt.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            panAddSar.setLayout(new GridLayout(0, 3, 10, 5));
            panAddNar.setLayout(new GridLayout(0, 3, 10, 5));
            panAddFas.setLayout(new GridLayout(0, 3, 10, 5));

            title.setHorizontalAlignment(JLabel.CENTER);
            title.setFont(new Font("serif", Font.PLAIN, 24));

            checkboxCVP.setEnabled(false);
            startDateCenaVP.setEnabled(false);
            endDateCenaVP.setEnabled(false);

            startDateCenaVP.setPreferredSize(new Dimension(120, 20));
            endDateCenaVP.setPreferredSize(new Dimension(120, 20));
            startDatePreiscur.setPreferredSize(new Dimension(120, 20));
            endDatePreiscur.setPreferredSize(new Dimension(120, 20));
            buttPlusSar.setPreferredSize(new Dimension(45, 18));
            buttPlusNar.setPreferredSize(new Dimension(45, 18));
            buttPlusFas.setPreferredSize(new Dimension(45, 18));

            col.addElement("");
            col.addElement("Прейскурант");

            table.setAutoCreateColumnsFromModel(true);

            checkboxCV.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    checkboxCVActionPerformed(evt);
                }
            });

            checkboxCVP.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    checkboxCVPActionPerformed(evt);
                }
            });

            button1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    button1ActionPerformed(evt);
                }
            });

            button2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    button2ActionPerformed(evt);
                }
            });

            button3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    button3ActionPerformed(evt);
                }
            });

            buttPlusSar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonPlusActionPerformed(evt, panAddSar, arraySar);
                }
            });
            buttPlusSar.doClick();

            buttPlusNar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonPlusActionPerformed(evt, panAddNar, arrayNar);
                }
            });
            buttPlusNar.doClick();

            buttPlusFas.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonPlusActionPerformed(evt, panAddFas, arrayFas);
                }
            });
            buttPlusFas.doClick();

            pan.add(new JLabel("Период изменения"), ParagraphLayout.NEW_PARAGRAPH);
            pan.add(new JLabel("прейскуранта:"), ParagraphLayout.NEW_PARAGRAPH);
            pan.add(startDatePreiscur, ParagraphLayout.NEW_PARAGRAPH);
            pan.add(endDatePreiscur, ParagraphLayout.NEW_PARAGRAPH);
            pan.add(button1, ParagraphLayout.NEW_PARAGRAPH);

            panel.add(new JLabel("Шифр. артикул:"), ParagraphLayout.NEW_PARAGRAPH_TOP);
            panel.add(buttPlusSar);
            panel.add(panAddSar);
            panel.add(new JLabel("Артикул:"), ParagraphLayout.NEW_PARAGRAPH_TOP);
            panel.add(buttPlusNar);
            panel.add(panAddNar);
            panel.add(new JLabel("Модель:"), ParagraphLayout.NEW_PARAGRAPH_TOP);
            panel.add(buttPlusFas);
            panel.add(panAddFas);
            panel.add(checkboxCV, ParagraphLayout.NEW_PARAGRAPH);
            panel.add(new JLabel("Показывать только изделия с ценой в валюте;"));
            panel.add(checkboxCVP, ParagraphLayout.NEW_LINE);
            panel.add(new JLabel("за период ввода"));
            panel.add(new JLabel(" с "), ParagraphLayout.NEW_LINE);
            panel.add(startDateCenaVP);
            panel.add(new JLabel(" по "));
            panel.add(endDateCenaVP);
            panel.add(checkboxXO, ParagraphLayout.NEW_PARAGRAPH);
            panel.add(new JLabel("Показывать артикула с %ОБ%, %ХД%, %ВД%;"));
            panel.add(pan, ParagraphLayout.NEW_PARAGRAPH_TOP);
            panel.add(new JScrollPane(table), ParagraphLayout.NEW_LINE_STRETCH_HV);

            butt.add(button3);
            butt.add(button2);
            butt.add(checkDialog);

            osnova.add(title, BorderLayout.NORTH);
            osnova.add(panel, BorderLayout.CENTER);
            osnova.add(butt, BorderLayout.SOUTH);

            getContentPane().add(osnova);
            pack();
        } catch (Exception e) {
            log.error("Ошибка AdvanSearchForm(): ", e);
            JOptionPane.showMessageDialog(null, "Ошибка AdvanSearchForm(): " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkboxCVActionPerformed(ActionEvent evt) {
        if (checkboxCV.isSelected()) {
            checkboxCVP.setEnabled(true);
        } else {
            checkboxCVP.setEnabled(false);
            checkboxCVP.setSelected(false);
            checkboxCVPActionPerformed(null);
        }
    }

    private void checkboxCVPActionPerformed(ActionEvent evt) {
        if (checkboxCVP.isSelected()) {
            startDateCenaVP.setEnabled(true);
            endDateCenaVP.setEnabled(true);
        } else {
            startDateCenaVP.setEnabled(false);
            endDateCenaVP.setEnabled(false);
        }
    }

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (testDate(startDatePreiscur.getDate(), endDatePreiscur.getDate())) {
            try {
                db = new CenaDB();
                pb = new ProgressBar(this, false, "Поиск прейскурантов...");
                final SwingWorker sw = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        row = db.getPreiscur(new SimpleDateFormat("dd.MM.yyyy").format(startDatePreiscur.getDate()),
                                new SimpleDateFormat("dd.MM.yyyy").format(endDatePreiscur.getDate()));
                        return null;
                    }

                    @Override
                    protected void done() {
                        pb.dispose();
                    }
                };
                sw.execute();
                pb.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                pb.setVisible(true);
                createTablePreiscur();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка поиска прейскурантов. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {
        Vector rezPreiscur = new Vector();
        for (int j = 0; j < table.getRowCount(); j++) {
            if (Boolean.valueOf(table.getValueAt(j, 0).toString()))
                rezPreiscur.add(table.getValueAt(j, 1));
        }

        String strPreiscur = "";
        int j = 0;
        if (!rezPreiscur.isEmpty()) {
            for (Object preiscur : rezPreiscur) {
                strPreiscur += j++ < 1 ? " and( " : " or ";
                strPreiscur += "sd1.preiscur LIKE '" + preiscur + "%'";
            }
            if (j >= 1) strPreiscur += " )";
        }

        String strSar = getElementsArrayList(arraySar, "nsi_kld.sar");
        String strNar = getElementsArrayList(arrayNar, "nsi_kld.nar");
        String strFas = getElementsArrayList(arrayFas, "nsi_kld.fas");

        if (strSar.equals("") && strNar.equals("") && strFas.equals("") && strPreiscur.equals("") && !checkboxCV.isSelected() && !checkboxXO.isSelected() && !checkboxCVP.isSelected())
            JOptionPane.showMessageDialog(null, "Вы не задали ни одного критерия для поиска!", "Внимание!", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        else {
            if (testDate(startDateCenaVP.getDate(), endDateCenaVP.getDate())) {
                if (!checkDialog.isSelected()) dispose();

                cenaform.advanSearch = true;
                cenaform.createDataTable(strFas,
                        strSar,
                        strNar,
                        "",
                        strPreiscur,
                        checkboxCV.isSelected(),
                        checkboxXO.isSelected(),
                        checkboxCVP.isSelected(),
                        new SimpleDateFormat("dd.MM.yyyy").format(startDateCenaVP.getDate()),
                        new SimpleDateFormat("dd.MM.yyyy").format(endDateCenaVP.getDate()));
            }
        }
    }

    private void button3ActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void buttonPlusActionPerformed(java.awt.event.ActionEvent evt, JPanel p, ArrayList array) {
        if (array.size() < 6) {
            array.add(array.size(), p.add(new JTextField(8)));
            p.revalidate();
            p.repaint();
        }
    }

    private void createTablePreiscur() {
        DefaultTableModel tModel = new DefaultTableModel(row, col) {
            @Override
            public Class<?> getColumnClass(int col) {
                return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0;
            }
        };
        table.setModel(tModel);
        if (!row.isEmpty())
            table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table.getTableHeader(), 0, ""));
    }

    private String getElementsArrayList(ArrayList<JTextField> array, String string) {
        String str = "";
        int num = 0;
        try {
            for (JTextField field : array) {
                if (!field.getText().trim().equals("") && !field.getText().trim().toLowerCase().equals("null")) {
                    str += num++ < 1 ? " and( " : " or ";
                    str += string + " LIKE '" + field.getText().trim().toUpperCase() + "%'";
                }
            }
            if (num >= 1) str += " )";
        } catch (Exception e) {
            str = "";
        }
        return str;
    }

    private boolean testDate(Date start, Date end) {
        try {
            UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(start));
            UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(end));
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка преобразования даты. " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
