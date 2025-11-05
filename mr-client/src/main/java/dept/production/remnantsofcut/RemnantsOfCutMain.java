package dept.production.remnantsofcut;

import com.jhlabs.awt.ParagraphLayout;
import com.sun.star.awt.FontWeight;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.CellHoriJustify;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import dept.MyReportsModule;
import workOO.OO_new;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author norg
 */
@SuppressWarnings("all")
public class RemnantsOfCutMain extends JDialog {

    private JPanel mainPanel;
    private JPanel typePanel;
    private JPanel bodyPanel;
    private JPanel butPanel;

    private JLabel lbBrigade;
    private JTextField txNBrigade;
    private JTextField txBrigade;
    private JButton btBrX;

    private JLabel lbModel;
    private JTextField txModel;
    private JButton btMdX;

    private JLabel lbArticle;
    private JTextField txArticle;
    private JButton btArX;

    private JLabel lbSize;
    private JTextField txSize;
    private JButton btSzX;
    private JCheckBox chSz;
    private JLabel lbChSz;

    private JLabel lbGrowth;
    private JTextField txGrowth;
    private JButton btGrX;
    private JCheckBox chGr;
    private JLabel lbChGr;

    private JLabel lbColor;
    private JComboBox cbColor;
    private JButton btClX;
    private JCheckBox chCl;
    private JLabel lbChCl;

    private JButton bPrint;
    private JButton bExit;

    private Color backGround;

    private XComponent document;
    private XSpreadsheetDocument xSpreadsheetDocument;
    private XSpreadsheets xSpreadsheets;
    private Object sheet;
    private XSpreadsheet xSpreadsheet;
    private com.sun.star.beans.XPropertySet xPropSet;
    private XCell xCell;

    public RemnantsOfCutMain(Frame owner, boolean modal) {
        super(owner, modal);
        initComponents();
        setLayouts();
        setSizes();
        setAttributes();
        setMappingOnPanels();
        setMappingOnForm();
        setFocusKey();
        fillColors();
        setEventModel();
        setTitle("Отчет по текущим остаткам");
        setSize(370, 280);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(modal);
    }

    private void initComponents() {
        mainPanel = new JPanel();
        bodyPanel = new JPanel();
        butPanel = new JPanel();


        lbBrigade = new JLabel("Бригада: ");
        txNBrigade = new JTextField();
        txBrigade = new JTextField();
        btBrX = new JButton();

        lbModel = new JLabel("Moдель: ");
        txModel = new JTextField();
        btMdX = new JButton();

        lbArticle = new JLabel("Артикул: ");
        txArticle = new JTextField();
        btArX = new JButton();

        lbSize = new JLabel("Размер: ");
        txSize = new JTextField();
        btSzX = new JButton();
        chSz = new JCheckBox();
        lbChSz = new JLabel("откл.");

        lbGrowth = new JLabel("Рост: ");
        txGrowth = new JTextField();
        btGrX = new JButton();
        chGr = new JCheckBox();
        lbChGr = new JLabel("откл.");

        lbColor = new JLabel("Цвет: ");
        cbColor = new JComboBox();
        btClX = new JButton();
        chCl = new JCheckBox();
        lbChCl = new JLabel("откл.");

        bPrint = new JButton("Печать");
        bExit = new JButton("Выход");
    }

    private void setLayouts() {
        mainPanel.setLayout(new BorderLayout(1, 1));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        bodyPanel.setLayout(new ParagraphLayout());
        bodyPanel.setBorder(BorderFactory.createTitledBorder(""));

        butPanel.setLayout(new GridLayout(1, 2));
    }

    private void setSizes() {
        txNBrigade.setPreferredSize(new Dimension(50, 20));
        txBrigade.setPreferredSize(new Dimension(120, 20));
        btBrX.setPreferredSize(new Dimension(20, 20));

        txModel.setPreferredSize(new Dimension(50, 20));
        btMdX.setPreferredSize(new Dimension(20, 20));

        txArticle.setPreferredSize(new Dimension(120, 20));
        btArX.setPreferredSize(new Dimension(20, 20));

        txSize.setPreferredSize(new Dimension(50, 20));
        btSzX.setPreferredSize(new Dimension(20, 20));
        chSz.setPreferredSize(new Dimension(17, 17));

        txGrowth.setPreferredSize(new Dimension(50, 20));
        btGrX.setPreferredSize(new Dimension(20, 20));
        chGr.setPreferredSize(new Dimension(17, 17));

        cbColor.setPreferredSize(new Dimension(170, 20));
        btClX.setPreferredSize(new Dimension(20, 20));
        chCl.setPreferredSize(new Dimension(17, 17));
    }

    private void setAttributes() {
        txBrigade.setEnabled(false);

        setIco(btBrX);
        setIco(btMdX);
        setIco(btArX);
        setIco(btSzX);
        setIco(btGrX);
        setIco(btClX);
    }

    private void setMappingOnPanels() {
        bodyPanel.add(lbBrigade);
        bodyPanel.add(txNBrigade);
        bodyPanel.add(txBrigade);
        bodyPanel.add(btBrX);

        bodyPanel.add(lbModel, ParagraphLayout.NEW_PARAGRAPH);
        bodyPanel.add(txModel);
        bodyPanel.add(btMdX);

        bodyPanel.add(lbArticle, ParagraphLayout.NEW_PARAGRAPH);
        bodyPanel.add(txArticle);
        bodyPanel.add(btArX);

        bodyPanel.add(lbSize, ParagraphLayout.NEW_PARAGRAPH);
        bodyPanel.add(txSize);
        bodyPanel.add(btSzX);
        bodyPanel.add(chSz);
        bodyPanel.add(lbChSz);

        bodyPanel.add(lbGrowth, ParagraphLayout.NEW_PARAGRAPH);
        bodyPanel.add(txGrowth);
        bodyPanel.add(btGrX);
        bodyPanel.add(chGr);
        bodyPanel.add(lbChGr);

        bodyPanel.add(lbColor, ParagraphLayout.NEW_PARAGRAPH);
        bodyPanel.add(cbColor);
        bodyPanel.add(btClX);
        bodyPanel.add(chCl);
        bodyPanel.add(lbChCl);

        butPanel.add(bPrint);
        butPanel.add(bExit);
    }

    private void setMappingOnForm() {
        mainPanel.add(bodyPanel, BorderLayout.NORTH);
        mainPanel.add(butPanel, BorderLayout.SOUTH);
        getContentPane().add(mainPanel);
        pack();
    }

    private void setIco(JButton btn) {
        btn.setIcon(new ImageIcon(MyReportsModule.progPath + "/Img/deletered.png"));
    }

    private void fillColors() {
        try {
            ReportDB db = new ReportDB();
            cbColor.addItem("");
            for (int i = 0; i < db.getColors().size(); i++) {
                cbColor.addItem(db.getColors().get(i));
            }
            cbColor.setSelectedItem("");
            db.disConn();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ошибка получения списка цветов: " + ex);
            new Exception("Ошибка в fillColors():" + ex);
        }
    }

    private void setFocusKey() {
        setFocusTraversalKeysEnabled(false);
        Set<AWTKeyStroke> forwardKeySet = new HashSet<AWTKeyStroke>();
        forwardKeySet.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
        setFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeySet);
    }

    private void setEventModel() {
        setFocusListener(txNBrigade);
        setFocusListener(txModel);
        setFocusListener(txArticle);
        setFocusListener(txSize);
        setFocusListener(txGrowth);

        setActionListener(btBrX, txNBrigade);
        setActionListener(btBrX, txBrigade);
        setActionListener(btMdX, txModel);
        setActionListener(btArX, txArticle);
        setActionListener(btSzX, txSize);
        setActionListener(btGrX, txGrowth);
        setActionListener(btClX, cbColor);

        setActionListener(chSz, txSize);
        setActionListener(chGr, txGrowth);
        setActionListener(chCl, cbColor);

        txNBrigade.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!txNBrigade.getText().trim().isEmpty()) {
                    try {
                        ReportDB db = new ReportDB();
                        int nBr = Integer.parseInt(txNBrigade.getText().toString().trim());
                        txBrigade.setText(db.getBrigadeName(nBr));
                        db.disConn();
                    } catch (Exception ex) {
                        new Exception("Ошибка setEventModel():" + ex);
                    }
                } else {
                    txBrigade.setText("");
                }
            }
        });

        bPrint.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    ReportDB db = new ReportDB();
                    int numBrigade = Integer.parseInt(txNBrigade.getText().isEmpty() ? "0" : txNBrigade.getText().toString().trim());
                    int fas = Integer.parseInt(txModel.getText().isEmpty() ? "0" : txModel.getText().toString().trim());
                    String art = txArticle.getText().isEmpty() ? "" : txArticle.getText().toString().toUpperCase().trim();
                    int size = Integer.parseInt(txSize.getText().isEmpty() ? "0" : txSize.getText().toString().trim());
                    int growth = Integer.parseInt(txGrowth.getText().isEmpty() ? "0" : txGrowth.getText().toString().trim());
                    int colorIndex = cbColor.getSelectedItem().toString().trim().equals("") ? 0 : db.getColorIndex(cbColor.getSelectedItem().toString().trim().toUpperCase());/*txC.getText().isEmpty()?"":txArticle.getText().toString().toUpperCase().trim()*/
                    boolean checkSize = chSz.isSelected();
                    boolean checkGrowth = chGr.isSelected();
                    boolean checkColor = chCl.isSelected();
                    db.setQueryParams(numBrigade, fas, art, size, growth, colorIndex, checkSize, checkGrowth, checkColor);
                    getReport(db.getModelsList(numBrigade, fas, art, size, growth, colorIndex));
                    db.disConn();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Ошибка получения данных: " + ex);
                    new Exception("Ошибка в setEventModel(): " + ex);
                }
            }
        });

        bExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }

    private void setActionListener(final JButton btn, final Object field) {
        btn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                relVal(field);
            }
        });
    }

    public void relVal(Object field) {
        if (!field.getClass().getName().equals("javax.swing.JComboBox")) {
            ((JTextField) field).setText("");
        } else {
            ((JComboBox) field).setSelectedItem("");
        }
    }

    private void setFocusListener(final JTextField field) {
        field.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                setColorFocusG(field);
            }

            public void focusLost(FocusEvent e) {
                setColorFocusL(field);
            }
        });
    }

    private void setColorFocusG(JTextField field) {
        backGround = field.getBackground();
        field.setBackground(new Color(185, 211, 238));
        field.selectAll();
    }

    private void setColorFocusL(JTextField field) {
        field.setBackground(backGround);
    }

    private void setActionListener(final JCheckBox ch, final Object field) {
        ch.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (ch.isSelected()) {
                    setFalseState(field);
                } else {
                    setTrueState(field);
                }
            }
        });
    }

    private void setFalseState(Object field) {
        if (!field.getClass().getName().equals("javax.swing.JComboBox")) {
            ((JTextField) field).setText("");
            ((JTextField) field).setEditable(false);
        } else {
            ((JComboBox) field).setSelectedItem("");
            ((JComboBox) field).setEnabled(false);
        }
    }

    private void setTrueState(Object field) {
        if (!field.getClass().getName().equals("javax.swing.JComboBox")) {
            ((JTextField) field).setEditable(true);
        } else {
            ((JComboBox) field).setEnabled(true);
        }
    }

    private void getReport(Vector data) {
        try {
            if (data.size() > 0) {
                Date d = new Date();
                String date = (String.valueOf(d.getDate()).trim().length() == 2 ? String.valueOf(d.getDate()).trim() : "0" + String.valueOf(d.getDate()).trim()) + "." +
                        (String.valueOf(d.getMonth() + 1).trim().length() == 2 ? String.valueOf(d.getMonth() + 1).trim() : "0" + String.valueOf(d.getMonth() + 1).trim()) + "." +
                        (String.valueOf(d.getYear() + 1900).trim()) + " " + String.valueOf(d.getHours()).trim() +
                        ":" + String.valueOf(d.getMinutes()).trim() + ":" + String.valueOf(d.getSeconds()).trim();

                OO_new.connect();
                document = OO_new.openDocumentOld("Templates/RemnantsOfCut.ots");
                xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, document);
                xSpreadsheets = xSpreadsheetDocument.getSheets();
                sheet = xSpreadsheets.getByName("Лист1");
                xSpreadsheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
                xPropSet = null;

                com.sun.star.table.BorderLine aLine = new com.sun.star.table.BorderLine();
                aLine.Color = 0x000000;
                aLine.InnerLineWidth = aLine.LineDistance = 0;
                aLine.OuterLineWidth = 1;
                com.sun.star.table.TableBorder aBorder = new com.sun.star.table.TableBorder();
                aBorder.TopLine = aBorder.BottomLine = aBorder.LeftLine = aBorder.RightLine = aLine;
                aBorder.IsTopLineValid = aBorder.IsBottomLineValid = true;
                aBorder.IsLeftLineValid = aBorder.IsRightLineValid = true;

                setReportTitle(date, data);

                int total_m = 0;
                int total_b = 0;
                int total_bb = 0;
                int row_start_position = 3;
                for (int i = 0; i < data.size(); i++) {
                    if (((data.size() - 1) - i) != 0) {
                        if (i > 0) {
                            if (Integer.parseInt(((Vector) data.elementAt(i)).get(0).toString().trim()) !=
                                    Integer.parseInt(((Vector) data.elementAt(i - 1)).get(0).toString().trim())) {
                                setBrigadeTotalValue(row_start_position, aBorder, String.valueOf(total_bb).trim(), false);
                                total_bb = 0;
                                row_start_position += 1;
                            }
                        }
                        if (Integer.parseInt(((Vector) data.elementAt(i)).get(2).toString()) ==
                                Integer.parseInt(((Vector) data.elementAt(i + 1)).get(2).toString())) {
                            for (int j = 0; j < ((Vector) data.elementAt(i)).size(); j++) {
                                if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(1))) {
                                    if (i > 0) {
                                        if (!((Vector) data.elementAt(i)).get(1).toString().trim().equals(((Vector) data.elementAt(i - 1)).get(1).toString().trim())) {
                                            setValueAtCell(0, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                        }
                                    } else {
                                        setValueAtCell(0, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                    }
                                }
                                if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(2))) {
                                    if (i > 0) {
                                        if (!((Vector) data.elementAt(i)).get(2).toString().trim().equals(((Vector) data.elementAt(i - 1)).get(2).toString().trim())) {
                                            setValueAtCell(1, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                        }
                                    } else {
                                        setValueAtCell(1, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                    }
                                }
                                if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(3))) {
                                    if (i > 0) {
                                        if (!((Vector) data.elementAt(i)).get(3).toString().trim().equals(((Vector) data.elementAt(i - 1)).get(3).toString().trim())) {
                                            setValueAtCell(2, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                        }
                                    } else {
                                        setValueAtCell(2, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                    }
                                }
                                if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(5))) {
                                    setValueAtCell(3, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                }
                                if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(6))) {
                                    setValueAtCell(4, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                }
                                if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(7))) {
                                    setValueAtCell(5, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                }
                                if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(8))) {
                                    setValueAtCell(6, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                }
                                if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(4))) {
                                    if (i > 0) {
                                        if (!((Vector) data.elementAt(i)).get(4).toString().trim().equals(((Vector) data.elementAt(i - 1)).get(4).toString().trim())) {
                                            setValueAtCell(7, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                        }
                                    } else {
                                        setValueAtCell(7, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                    }
                                }
                            }
                            total_m = total_m + Integer.parseInt(((Vector) data.elementAt(i)).get(8).toString().trim());
                            total_bb = total_bb + Integer.parseInt(((Vector) data.elementAt(i)).get(8).toString().trim());
                        } else {
                            for (int j = 0; j < ((Vector) data.elementAt(i)).size(); j++) {
                                if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(1))) {
                                    if (i > 0) {
                                        if (!((Vector) data.elementAt(i)).get(1).toString().trim().equals(((Vector) data.elementAt(i - 1)).get(1).toString().trim())) {
                                            setValueAtCell(0, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                        }
                                    } else {
                                        setValueAtCell(0, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                    }
                                }
                                if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(2))) {
                                    if (i > 0) {
                                        if (!((Vector) data.elementAt(i)).get(2).toString().trim().equals(((Vector) data.elementAt(i - 1)).get(2).toString().trim())) {
                                            setValueAtCell(1, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                        }
                                    } else {
                                        setValueAtCell(1, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                    }
                                }
                                if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(3))) {
                                    if (i > 0) {
                                        if (!((Vector) data.elementAt(i)).get(3).toString().trim().equals(((Vector) data.elementAt(i - 1)).get(3).toString().trim())) {
                                            setValueAtCell(2, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                        }
                                    } else {
                                        setValueAtCell(2, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                    }
                                }
                                if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(5))) {
                                    setValueAtCell(3, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                }
                                if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(6))) {
                                    setValueAtCell(4, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                }
                                if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(7))) {
                                    setValueAtCell(5, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                }
                                if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(8))) {
                                    setValueAtCell(6, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                }
                                if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(4))) {
                                    if (i > 0) {
                                        if (!((Vector) data.elementAt(i)).get(4).toString().trim().equals(((Vector) data.elementAt(i - 1)).get(4).toString().trim())) {
                                            setValueAtCell(7, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                        }
                                    } else {
                                        setValueAtCell(7, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                    }
                                }
                            }
                            total_bb = total_bb + Integer.parseInt(((Vector) data.elementAt(i)).get(8).toString().trim());
                            row_start_position += 1;
                            total_m = total_m + Integer.parseInt(((Vector) data.elementAt(i)).get(8).toString().trim());
                            setModelTotalValue(row_start_position, String.valueOf(total_m).trim());
                            total_b = total_b + total_m;
                            total_m = 0;
                        }
                        row_start_position += 1;
                    } else {
                        for (int j = 0; j < ((Vector) data.elementAt(i)).size(); j++) {
                            if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(1))) {
                                if (i > 0) {
                                    if (!((Vector) data.elementAt(i)).get(1).toString().trim().equals(((Vector) data.elementAt(i - 1)).get(1).toString().trim())) {
                                        setValueAtCell(0, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                    }
                                } else {
                                    setValueAtCell(0, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                }
                            }
                            if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(2))) {
                                if (i > 0) {
                                    if (!((Vector) data.elementAt(i)).get(2).toString().trim().equals(((Vector) data.elementAt(i - 1)).get(2).toString().trim())) {
                                        setValueAtCell(1, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                    }
                                } else {
                                    setValueAtCell(1, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                }
                            }
                            if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(3))) {
                                if (i > 0) {
                                    if (!((Vector) data.elementAt(i)).get(3).toString().trim().equals(((Vector) data.elementAt(i - 1)).get(3).toString().trim())) {
                                        setValueAtCell(2, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                    }
                                } else {
                                    setValueAtCell(2, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                }
                            }
                            if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(5))) {
                                setValueAtCell(3, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                            }
                            if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(6))) {
                                setValueAtCell(4, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                            }
                            if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(7))) {
                                setValueAtCell(5, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                            }
                            if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(8))) {
                                setValueAtCell(6, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                            }
                            if ((((Vector) data.elementAt(i)).get(j)).equals(((Vector) data.elementAt(i)).get(4))) {
                                if (i > 0) {
                                    if (!((Vector) data.elementAt(i)).get(4).toString().trim().equals(((Vector) data.elementAt(i - 1)).get(4).toString().trim())) {
                                        setValueAtCell(7, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                    }
                                } else {
                                    setValueAtCell(7, row_start_position, aBorder, ((Vector) data.elementAt(i)).get(j).toString().trim());
                                }
                            }
                        }
                        row_start_position += 1;
                        total_bb = total_bb + Integer.parseInt(((Vector) data.elementAt(i)).get(8).toString().trim());
                        total_m = total_m + Integer.parseInt(((Vector) data.elementAt(i)).get(8).toString().trim());
                        total_b = total_b + total_m;
                        if (!(((Vector) data.elementAt(i)).get(2)).equals((((Vector) data.elementAt(0)).get(2)))) {
                            setModelTotalValue(row_start_position, String.valueOf(total_m).trim());
                            if (!(((Vector) data.elementAt(i)).get(0)).equals((((Vector) data.elementAt(0)).get(0)))) {
                                row_start_position += 1;
                                setBrigadeTotalValue(row_start_position, aBorder, String.valueOf(total_bb).trim(), false);
                                row_start_position += 1;
                                setBrigadeTotalValue(row_start_position, aBorder, String.valueOf(total_b).trim(), true);
                            } else {
                                row_start_position += 1;
                                setBrigadeTotalValue(row_start_position, aBorder, String.valueOf(total_bb).trim(), false);
                            }
                        } else {
                            setBrigadeTotalValue(row_start_position, aBorder, String.valueOf(total_bb).trim(), false);
                            row_start_position += 1;
                            setModelTotalValue(row_start_position, String.valueOf(total_m).trim());
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Нет данных");
            }
        } catch (Exception e) {
            new Exception("Ошибка в getReport(Vector data):" + e);
        }
    }

    private void setReportTitle(String date, Vector data) throws IndexOutOfBoundsException, UnknownPropertyException, IllegalArgumentException, PropertyVetoException, WrappedTargetException {
        xCell = xSpreadsheet.getCellByPosition(0, 1);
        xCell.setFormula("по состоянию на " + date);
        xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
        xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
    }

    private void setValueAtCell(int col_rep, int row_rep, com.sun.star.table.TableBorder Border, String value) throws IndexOutOfBoundsException, UnknownPropertyException, IllegalArgumentException, PropertyVetoException, WrappedTargetException {
        xCell = xSpreadsheet.getCellByPosition(col_rep, row_rep);
        xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
        xPropSet.setPropertyValue("TableBorder", Border);
        xCell.setFormula(value);
    }

    private void setModelTotalValue(int row_rep, String value) throws IndexOutOfBoundsException, UnknownPropertyException, IllegalArgumentException, PropertyVetoException, WrappedTargetException {
        xCell = xSpreadsheet.getCellByPosition(0, row_rep);
        xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
        xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
        xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
        xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.RIGHT);
        xCell.setFormula("Всего по модели:");
        xCell = xSpreadsheet.getCellByPosition(6, row_rep);
        xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
        xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
        xCell.setFormula(value);
    }

    private void setBrigadeTotalValue(int row_rep, com.sun.star.table.TableBorder Border, String value, boolean bAll) throws IndexOutOfBoundsException, UnknownPropertyException, IllegalArgumentException, PropertyVetoException, WrappedTargetException {
        if (bAll) {
            xCell = xSpreadsheet.getCellByPosition(0, row_rep);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.RIGHT);
            xCell.setFormula("Всего по бригадам:");
            xCell = xSpreadsheet.getCellByPosition(6, row_rep);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell.setFormula(value);
        } else {
            xCell = xSpreadsheet.getCellByPosition(0, row_rep);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("HoriJustify", CellHoriJustify.RIGHT);
            xCell.setFormula("Всего по бригадe:");
            xCell = xSpreadsheet.getCellByPosition(6, row_rep);
            xPropSet = (com.sun.star.beans.XPropertySet) UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, xCell);
            xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            xCell.setFormula(value);
        }
    }
}
