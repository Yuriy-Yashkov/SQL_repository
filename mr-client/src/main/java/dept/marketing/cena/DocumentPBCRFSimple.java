package dept.marketing.cena;

import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.UtilFunctions;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author lidashka
 */
@SuppressWarnings("all")
public class DocumentPBCRFSimple extends JDialog {
    JPanel osnova;
    JPanel upPanel;
    JPanel downPanel;
    JPanel panel;
    JPanel pan1;
/*    JPanel pan2;
    JPanel pan3;
    JPanel pan4;
    JPanel pan5;    */

    JButton buttonCancel;
    JButton buttonPrint;
    JButton but1p;
    JButton but1m;
  /*JButton but2p;
    JButton but2m;
    JButton but3p;
    JButton but3m;
    JButton but4p;
    JButton but4m;
    JButton but5p;
    JButton but5m;
    */

    JDateChooser date;

    JLabel title1;
    JLabel title2;

    JTextPane list1;
    /*  JTextPane list2;
      JTextPane list3;
      JTextPane list4;
      JTextPane list5;
      */
    JTextField num;
    JTextField kof1;
   /* JTextField kof2;
    JTextField kof3;
    JTextField kof4;
    JTextField kof5;*/

    Vector data;

    public DocumentPBCRFSimple(CenaForm parent, boolean modal, Vector dataTable) {
        super(parent, modal);
        init();

        data = dataTable;

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void init() {
        this.setTitle("Перечень базовых цен для РФ");

        setPreferredSize(new Dimension(470, 230));

        osnova = new JPanel();
        upPanel = new JPanel();
        downPanel = new JPanel();
        panel = new JPanel();
        pan1 = new JPanel();
/*        pan2 = new JPanel();
        pan3 = new JPanel();
        pan4 = new JPanel();
        pan5 = new JPanel();*/
        title1 = new JLabel("Перечень базовых цен для оптовых покупателей РФ");
        title2 = new JLabel("без учета повышающего коэффициента");
        buttonCancel = new JButton("Закрыть");
        buttonPrint = new JButton("Печать");
        but1p = new JButton("+");
        but1m = new JButton("-");
       /* but2p = new JButton("+");
        but2m = new JButton("-");
        but3p = new JButton("+");
        but3m = new JButton("-");
        but4p = new JButton("+");
        but4m = new JButton("-");
        but5p = new JButton("+");
        but5m = new JButton("-");*/
        date = new JDateChooser();
        num = new JTextField();
        kof1 = new JTextField("1");
       /* kof2 = new JTextField("1.025");
        kof3 = new JTextField("1.05");
        kof4 = new JTextField("1.125");
        kof5 = new JTextField("1.25");  */
        list1 = new JTextPane();
/*      list2 = new JTextPane();
        list3 = new JTextPane();
        list4 = new JTextPane();
        list5 = new JTextPane(); */

        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        upPanel.setLayout(new GridLayout(2, 0, 5, 5));
        downPanel.setLayout(new GridLayout(0, 3, 5, 5));
        downPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        pan1.setLayout(new GridLayout(2, 0, 5, 5));
/*        pan2.setLayout(new GridLayout(2, 0, 5, 5));
        pan3.setLayout(new GridLayout(2, 0, 5, 5));
        pan4.setLayout(new GridLayout(2, 0, 5, 5));
        pan5.setLayout(new GridLayout(2, 0, 5, 5)); */
        panel.setLayout(new ParagraphLayout());

        title1.setHorizontalAlignment(JLabel.CENTER);
        title1.setFont(new Font("serif", Font.PLAIN, 16));
        title2.setHorizontalAlignment(JLabel.CENTER);
        title2.setFont(new Font("serif", Font.PLAIN, 16));

        num.setPreferredSize(new Dimension(50, 20));
        date.setPreferredSize(new Dimension(120, 20));
        kof1.setPreferredSize(new Dimension(100, 20));
       /* kof2.setPreferredSize(new Dimension(100, 20));
        kof3.setPreferredSize(new Dimension(100, 20));
        kof4.setPreferredSize(new Dimension(100, 20));
        kof5.setPreferredSize(new Dimension(100, 20)); */
        list1.setPreferredSize(new Dimension(250, 50));
/*        list2.setPreferredSize(new Dimension(250, 50));
        list3.setPreferredSize(new Dimension(250, 50));
        list4.setPreferredSize(new Dimension(250, 50));
        list5.setPreferredSize(new Dimension(250, 50)); */
        but1p.setPreferredSize(new Dimension(45, 20));
        but1m.setPreferredSize(new Dimension(45, 20));
    /*    but2p.setPreferredSize(new Dimension(45, 20));
        but2m.setPreferredSize(new Dimension(45, 20));
        but3p.setPreferredSize(new Dimension(45, 20));
        but3m.setPreferredSize(new Dimension(45, 20));
        but4p.setPreferredSize(new Dimension(45, 20));
        but4m.setPreferredSize(new Dimension(45, 20));
        but5p.setPreferredSize(new Dimension(45, 20));
        but5m.setPreferredSize(new Dimension(45, 20));*/

        date.setDate((Calendar.getInstance()).getTime());
        /*        list5.setText(" Все остальные покупатели;");*/

        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });

        buttonPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    if (UtilFunctions.checkDate(new SimpleDateFormat("dd.MM.yyyy").format(date.getDate()))) {
                        Vector dataOO = new Vector();
                        Vector kofOO = new Vector();
                        Vector clientsOO = new Vector();

                        kofOO.add(Double.valueOf(kof1.getText().trim().replaceAll(",", ".")));
                    /*    kofOO.add(Double.valueOf(kof2.getText().trim().replaceAll(",", ".")));
                        kofOO.add(Double.valueOf(kof3.getText().trim().replaceAll(",", ".")));
                        kofOO.add(Double.valueOf(kof4.getText().trim().replaceAll(",", ".")));
                        kofOO.add(Double.valueOf(kof5.getText().trim().replaceAll(",", ".")));
*/
                        clientsOO.add(list1.getText().trim());
/*                      clientsOO.add(list2.getText().trim());
                        clientsOO.add(list3.getText().trim());
                        clientsOO.add(list4.getText().trim());
                        clientsOO.add(list5.getText().trim());*/

                        for (Iterator it = data.iterator(); it.hasNext(); ) {
                            Vector row = (Vector) it.next();
                            if ((Boolean) row.elementAt(0)) {
                                Vector tmp = new Vector();
                                tmp.add(row.elementAt(1));
                                tmp.add(row.elementAt(2));
                                tmp.add(row.elementAt(3));
                                tmp.add(row.elementAt(4));
                                tmp.add(row.elementAt(11));
                                tmp.add(row.elementAt(12));
                                //1.0
                                tmp.add(Double.valueOf(row.elementAt(20).toString()) * Double.valueOf(kof1.getText().trim().replaceAll(",", ".")));
                                tmp.add(Double.valueOf(row.elementAt(21).toString()) * Double.valueOf(kof1.getText().trim().replaceAll(",", ".")));
                                tmp.add(Double.valueOf(row.elementAt(22).toString()) * Double.valueOf(kof1.getText().trim().replaceAll(",", ".")));
                          /*      //1.025
                                tmp.add(Double.valueOf(row.elementAt(20).toString())*Double.valueOf(kof2.getText().trim().replaceAll(",", ".")));
                                tmp.add(Double.valueOf(row.elementAt(21).toString())*Double.valueOf(kof2.getText().trim().replaceAll(",", ".")));
                                tmp.add(Double.valueOf(row.elementAt(22).toString())*Double.valueOf(kof2.getText().trim().replaceAll(",", ".")));
                                 //1.05
                                tmp.add(Double.valueOf(row.elementAt(20).toString())*Double.valueOf(kof3.getText().trim().replaceAll(",", ".")));
                                tmp.add(Double.valueOf(row.elementAt(21).toString())*Double.valueOf(kof3.getText().trim().replaceAll(",", ".")));
                                tmp.add(Double.valueOf(row.elementAt(22).toString())*Double.valueOf(kof3.getText().trim().replaceAll(",", ".")));
                                //1.125
                                tmp.add(Double.valueOf(row.elementAt(20).toString())*Double.valueOf(kof4.getText().trim().replaceAll(",", ".")));
                                tmp.add(Double.valueOf(row.elementAt(21).toString())*Double.valueOf(kof4.getText().trim().replaceAll(",", ".")));
                                tmp.add(Double.valueOf(row.elementAt(22).toString())*Double.valueOf(kof4.getText().trim().replaceAll(",", ".")));
                                //1.5
                                tmp.add(Double.valueOf(row.elementAt(20).toString())*Double.valueOf(kof5.getText().trim().replaceAll(",", ".")));
                                tmp.add(Double.valueOf(row.elementAt(21).toString())*Double.valueOf(kof5.getText().trim().replaceAll(",", ".")));
                                tmp.add(Double.valueOf(row.elementAt(22).toString())*Double.valueOf(kof5.getText().trim().replaceAll(",", ".")));
                        */
                                dataOO.add(tmp);
                            }
                        }

                        try {
                            CenaOO oo = new CenaOO(num.getText(), dataOO, kofOO, clientsOO, new SimpleDateFormat("dd.MM.yyyy").format(date.getDate()),
                                    "Курсы: RUB/BYR -" + UtilCena.KURS_RUB + ", USD/RUB - " + UtilCena.KURS_USD + ", EUR/RUB - " + UtilCena.KURS_EUR);
                            oo.createReport("PriceTableDocumentPBCRFSimple.ots");
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    JOptionPane.showMessageDialog(null, "Введено некорректное значение: " + e.getMessage(), "Завершено!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        but1p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new ClientForm(DocumentPBCRFSimple.this, true, UtilCena.DOCUMENT_LIST1);
            }
        });

        but1m.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                list1.setText("");
            }
        });
        
/*        but2p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {  
                new ClientForm(DocumentPBCRFSimple.this, true, UtilCena.DOCUMENT_LIST2);
            }
        });
        
        but2m.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {  
                list2.setText("");
            }
        });
        
        but3p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {  
                new ClientForm(DocumentPBCRFSimple.this, true, UtilCena.DOCUMENT_LIST3);
            }
        });
        
        but3m.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {  
                list3.setText("");
            }
        });
        
        but4p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {  
                new ClientForm(DocumentPBCRFSimple.this, true, UtilCena.DOCUMENT_LIST4);
            }
        });
        
        but4m.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {  
                list4.setText("");
            }
        });
        
        but5p.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {  
                new ClientForm(DocumentPBCRFSimple.this, true, UtilCena.DOCUMENT_LIST5);
            }
        });
        
        but5m.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {  
                list5.setText("");
            }
        });*/


        upPanel.add(title1);
        upPanel.add(title2);

        downPanel.add(buttonCancel);
        downPanel.add(buttonPrint);

        pan1.add(but1p);
        pan1.add(but1m);
   /*     pan2.add(but2p);
        pan2.add(but2m);
        pan3.add(but3p);
        pan3.add(but3m);
        pan4.add(but4p);
        pan4.add(but4m);
        pan5.add(but5p);
        pan5.add(but5m);
*/

        panel.add(new JLabel("Документ №"), ParagraphLayout.NEW_PARAGRAPH_TOP);
        panel.add(num);
        panel.add(new JLabel("  от  "));
        panel.add(date);
        // panel.add(new JLabel("Коэффициент:"), ParagraphLayout.NEW_PARAGRAPH_TOP);
        // panel.add(kof1);
        panel.add(new JLabel("Для покупателей:"), ParagraphLayout.NEW_PARAGRAPH_TOP);
        panel.add(new JScrollPane(list1));
        panel.add(pan1);
/*        panel.add(new JLabel("Коэффициент:"), ParagraphLayout.NEW_PARAGRAPH_TOP);
        panel.add(kof2);
        panel.add(new JLabel("Для покупателей:"), ParagraphLayout.NEW_PARAGRAPH_TOP); 
        panel.add(new JScrollPane(list2));
        panel.add(pan2);
        panel.add(new JLabel("Коэффициент:"), ParagraphLayout.NEW_PARAGRAPH_TOP); 
        panel.add(kof3);
        panel.add(new JLabel("Для покупателей:"), ParagraphLayout.NEW_PARAGRAPH_TOP); 
        panel.add(new JScrollPane(list3));
        panel.add(pan3);
        panel.add(new JLabel("Коэффициент:"), ParagraphLayout.NEW_PARAGRAPH_TOP); 
        panel.add(kof4);
        panel.add(new JLabel("Для покупателей:"), ParagraphLayout.NEW_PARAGRAPH_TOP); 
        panel.add(new JScrollPane(list4));
        panel.add(pan4);
        panel.add(new JLabel("Коэффициент:"), ParagraphLayout.NEW_PARAGRAPH_TOP); 
        panel.add(kof5);
        panel.add(new JLabel("Для покупателей:"), ParagraphLayout.NEW_PARAGRAPH_TOP); 
        panel.add(new JScrollPane(list5));
        panel.add(pan5);*/


        osnova.add(upPanel, BorderLayout.NORTH);
        osnova.add(panel, BorderLayout.CENTER);
        osnova.add(downPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();

    }

    public void setNameClient(String name, String type) {
        if (type != null && name.length() > 0) {
            if (type.equals(UtilCena.DOCUMENT_LIST1)) {
                list1.setText(list1.getText().trim() + " " + name + ";\n");
            }/*else if(type.equals(UtilCena.DOCUMENT_LIST2)){
                list2.setText(list2.getText().trim() + " " + name + ";\n");
            }else if(type.equals(UtilCena.DOCUMENT_LIST3)){
                list3.setText(list3.getText().trim() + " " + name + ";\n");
            }else if(type.equals(UtilCena.DOCUMENT_LIST4)){
                list4.setText(list4.getText().trim() + " " + name + ";\n");
            }else if(type.equals(UtilCena.DOCUMENT_LIST5)){
                list5.setText(list5.getText().trim() + " " + name + ";\n");
            }*/
        }
    }
}
