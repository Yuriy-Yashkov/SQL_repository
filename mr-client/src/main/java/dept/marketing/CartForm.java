package dept.marketing;

import common.User;
import workDB.DB;
import workDB.PDB;
import workOO.OpenOffice;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("all")
public class CartForm extends javax.swing.JDialog {
    public static Vector valuta = new Vector();
    public static String printSetting = "Для склада";
    public static String ndsSetting = "По умолчанию";
    public static int ndsValueSetting = -1;
    public static int kodValutaSetting = 1;
    public static String propisValutaSetting = "бел.руб.";
    public static int exchangeSetting = 0;
    public static int kursSetting;
    public static String nameSetting = "Произвольный курс";
    public static String moneySetting = " ";
    public String summa;
    public PDB pdb;
    public DB db;

    public int selectRow = 0;
    private int idOrder;
    private int idClient;
    private int status;
    private String nameClient;
    private MarketingForm marketing = null;
    private OrdersForm ordersform = null;
    private DefaultTableModel tm;
    private JTable table = new JTable();
    private User user = User.getInstance();
    private String[] print = {"Общая", "Для склада"};
    private String[] nds = {"По умолчанию", "Произвольный НДС"};
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    public CartForm(javax.swing.JDialog parent, boolean modal, int idclient, String nameclient) {
        super(parent, modal);
        initComponents();
        this.setTitle("Корзина");
        setResizable(false);

        jPanel1.setLayout(new BorderLayout());

        idOrder = 0;
        idClient = idclient;
        nameClient = nameclient;
        status = 1;

        marketing = (MarketingForm) parent;
        pdb = marketing.pdb;

        showCatrTable();
        createNoteOrder();

        jLabel3.setText(nameclient);
        jLabel8.setVisible(false);
        jLabel9.setVisible(false);
        jLabel10.setVisible(false);

        setLocationRelativeTo(parent);
        setVisible(true);
    }
    public CartForm(javax.swing.JDialog parent, boolean modal, int idorder, int idclient, String nameclient, int state) {
        super(parent, modal);
        initComponents();
        this.setTitle("Редактирование заявки");
        setResizable(false);

        jPanel1.setLayout(new BorderLayout());

        idOrder = idorder;
        idClient = idclient;
        nameClient = nameclient;
        status = state;

        marketing = (MarketingForm) parent;
        pdb = marketing.pdb;

        showCatrTable();
        createNoteOrder();

        jLabel3.setText(nameclient);
        jLabel2.setText("Заявка №" + idOrder + " для:");
        jLabel8.setVisible(false);
        jLabel9.setVisible(false);
        jLabel10.setVisible(false);
        jButton5.setText("Сохранить");

        setLocationRelativeTo(parent);
        setVisible(true);
    }
    public CartForm(javax.swing.JDialog parent, boolean modal, int idorder, int idclient, String nameclient, String date,
                    int state, String madeorder, String gatherorder, String editorder, String datemade, String dateedit) {
        super(parent, modal);
        initComponents();
        this.setTitle("Выполнение заявки");

        jPanel1.setLayout(new BorderLayout());

        idOrder = idorder;
        idClient = idclient;
        nameClient = nameclient;
        status = state;

        ordersform = (OrdersForm) parent;
        pdb = ordersform.pdb;

        displayCartTable();

        jLabel6.setText(date);
        jLabel3.setText(nameclient);
        jLabel2.setText("Заявка №" + idOrder + " для:");
        jLabel8.setText(jLabel8.getText() + "  " + madeorder + " " + datemade);
        jLabel9.setText(jLabel9.getText() + "  " + editorder + " " + dateedit);
        jLabel10.setText(jLabel10.getText() + "  " + gatherorder);
        jLabel4.setVisible(false);
        jLabel5.setVisible(false);
        jLabel7.setVisible(false);
        jScrollPane1.setVisible(false);
        jButton1.setVisible(false);
        jButton5.setVisible(false);
        jButton2.setVisible(false);
        jMenu1.setVisible(false);
        jMenu5.setVisible(false);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void printForm(JDialog aThis, int idOrder, String namecl, String formatDate, int param) {
        String printForm = null;
        if (printSetting.equals("Для склада"))
            printForm = "ЗаявкаСклад.ots";
        else if (printSetting.equals("Общая"))
            printForm = "ЗаказыОбщаяФорма.ots";
        OpenOffice oo = new OpenOffice(aThis, idOrder, namecl, formatDate, param, kodValutaSetting, kursSetting, ndsValueSetting, MarketingForm.noteorder);
        oo.createReport(printForm);
    }

    public static boolean checkDate(String chDate) {
        Calendar cal = new GregorianCalendar();
        cal.setLenient(false);
        cal.clear();
        try {
            int d = Integer.parseInt(chDate.substring(0, 2));
            int m = Integer.parseInt(chDate.substring(3, 5));
            int y = Integer.parseInt(chDate.substring(6, 10));
            cal.set(y, m - 1, d);
            java.util.Date dt = cal.getTime();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка преобразования даты: " + chDate + " " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static MaskFormatter maskFormatterDate() {
        MaskFormatter mask = null;
        try {
            mask = new MaskFormatter("##.##.####");
            mask.setPlaceholderCharacter('0');
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
        }
        return mask;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 16));
        jLabel4.setText("Итого в корзине продукции на сумму:");

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 16));

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 16));
        jLabel2.setText("Заявка для:");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 16));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 16));
        jLabel1.setText("на");

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 16));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1071, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 398, Short.MAX_VALUE)
        );

        jButton1.setText("Продолжить");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton5.setText("Оформить заказ");
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jButton2.setText("Изменить");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(jTextPane1);

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 16));
        jLabel7.setText("Примечание:");

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel8.setText("Cоставил:");

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel9.setText("Изменил:");

        jLabel10.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel10.setText("Выполнил:");

        jMenu5.setText("Файл");

        jMenuItem3.setText("Открыть заявку");
        jMenuItem3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem3);

        jMenuItem2.setText("Сохранить заявку");
        jMenuItem2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem2);

        jMenuItem1.setText("Открыть в OpenOffice...");
        jMenuItem1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem1);

        jMenuBar1.add(jMenu5);

        jMenu1.setText("Вид");

        jMenu3.setText("Валюта");
        jMenu3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu3.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }

            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }

            public void menuSelected(javax.swing.event.MenuEvent evt) {
                jMenu3MenuSelected(evt);
            }
        });
        jMenu1.add(jMenu3);

        jMenu6.setText("НДС");
        jMenu6.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }

            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }

            public void menuSelected(javax.swing.event.MenuEvent evt) {
                jMenu6MenuSelected(evt);
            }
        });
        jMenu1.add(jMenu6);

        jMenu4.setText("Форма для печати");
        jMenu4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu4.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }

            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }

            public void menuSelected(javax.swing.event.MenuEvent evt) {
                jMenu4MenuSelected(evt);
            }
        });
        jMenu1.add(jMenu4);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(20, 20, 20)
                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                                                .addComponent(jLabel1)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(12, 12, 12)
                                                .addComponent(jButton2)
                                                .addGap(31, 31, 31))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addGap(29, 29, 29)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel7)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 895, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 3, Short.MAX_VALUE)))
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(279, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(280, 280, 280))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton2)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel7)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton5)
                                        .addComponent(jButton1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE))
                                                .addGap(16, 16, 16))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            Object[][] tmpcart = pdb.returnDataCart(kodValutaSetting, kursSetting, ndsValueSetting, false);
            boolean flag = true;
            if (tmpcart.length > 0) {
                for (int i = 0; i < tmpcart.length; i++) {
                    if (Integer.parseInt(tmpcart[i][9].toString()) < 1) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    if (status != 3) {
                        if (JOptionPane.showOptionDialog(null, "Отправить заявку на склад?", "Оформление заказа", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да") == JOptionPane.YES_OPTION) {
                            if (pdb.saveCart(idOrder, idClient, status, Integer.parseInt(user.getIdEmployee()), MarketingForm.date, kodValutaSetting, kursSetting, ndsValueSetting, MarketingForm.noteorder)) {
                                JOptionPane.showMessageDialog(null, "Заявка для " + nameClient + "\n на " + MarketingForm.date + " отправлена на склад!", "Оформление заказа", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                                MarketingForm.noteorder = "";
                                pdb.updateCart();
                                cartFormUpdate();
                                if (jButton5.getText().equals("Сохранить")) {
                                    marketing.dispose();
                                    marketing.getCreateOrderSkladForm();
                                }
                            }
                        }
                    } else
                        JOptionPane.showMessageDialog(null, "Заявка уже выполнена!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                } else
                    JOptionPane.showMessageDialog(null, "В столбце 'Кол-во' есть значение: 0", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } else
                JOptionPane.showMessageDialog(null, "Корзина пуста!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jMenu3MenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_jMenu3MenuSelected
        ValutaItem valuta_;
        jMenu3.removeAll();

        if (valuta.isEmpty()) {
            db = new DB();
            valuta = db.getValuta();
            Vector tmpValuta = new Vector();
            tmpValuta.add(exchangeSetting);
            tmpValuta.add(nameSetting);
            tmpValuta.add(moneySetting);
            tmpValuta.add(kursSetting);
            valuta.add(tmpValuta);
        }

        int size = valuta.size(), i = 0;
        while (size-- > 0) {
            Vector row = (Vector) valuta.elementAt(i++);
            int kod = Integer.parseInt(row.get(0).toString());
            valuta_ = new ValutaItem();
            if (kod == kodValutaSetting)
                valuta_.setSelected(true);
            valuta_.setKod(kod);
            valuta_.setPropis(row.get(2).toString());
            valuta_.setText(row.get(1).toString() + " (" + row.get(3).toString() + ")");
            valuta_.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent event) {
                    if (((ValutaItem) event.getComponent()).getKod() == 0) {
                        try {
                            Object kurs = JOptionPane.showInputDialog("Введите произвольный курс.");
                            if (kurs != null) {
                                if (Integer.parseInt(kurs.toString()) > 0) {
                                    valuta = new Vector();
                                    kursSetting = Integer.parseInt(kurs.toString());
                                    kodValutaSetting = ((ValutaItem) event.getComponent()).getKod();
                                    propisValutaSetting = ((ValutaItem) event.getComponent()).getPropis();
                                    jMenu3MenuSelected(null);
                                } else throw new Exception();
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Введено некорректное значение!\n " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        kodValutaSetting = ((ValutaItem) event.getComponent()).getKod();
                        propisValutaSetting = ((ValutaItem) event.getComponent()).getPropis();
                    }
                    cartFormUpdate();
                }
            });
            jMenu3.add(valuta_);
        }
    }//GEN-LAST:event_jMenu3MenuSelected

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            printForm(this, idOrder, nameClient, new SimpleDateFormat("yyyy.MM.dd").format(new SimpleDateFormat("dd.MM.yyyy").parse(MarketingForm.date)), 1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        if (pdb.returnDataCart(kodValutaSetting, kursSetting, ndsValueSetting, false).length > 0) {
            pdb.saveCart(0, idClient, 0, Integer.parseInt(user.getIdEmployee()), MarketingForm.date, kodValutaSetting, kursSetting, ndsValueSetting, MarketingForm.noteorder);
        } else {
            JOptionPane.showMessageDialog(null, "Корзина пуста!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        new OrdersForm(this, true, idClient);
    }//GEN-LAST:event_jMenuItem3ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void jMenu4MenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_jMenu4MenuSelected
        JCheckBoxMenuItem print_;
        jMenu4.removeAll();

        for (int i = 0; i < print.length; i++) {
            print_ = new JCheckBoxMenuItem();
            if (print[i].equals(printSetting)) print_.setSelected(true);
            print_.setText(print[i]);
            print_.setName(print[i]);
            print_.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent event) {
                    printSetting = event.getComponent().getName();
                }
            });
            jMenu4.add(print_);
        }
    }//GEN-LAST:event_jMenu4MenuSelected

    private void jMenu6MenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_jMenu6MenuSelected
        JCheckBoxMenuItem nds_;
        jMenu6.removeAll();

        for (int i = 0; i < nds.length; i++) {
            nds_ = new JCheckBoxMenuItem();
            if (nds[i].equals(ndsSetting)) nds_.setSelected(true);
            if (nds[i].equals("По умолчанию")) nds_.setText(nds[i]);
            else nds_.setText(nds[i] + " (" + ndsValueSetting + "%)");
            nds_.setName(nds[i]);
            nds_.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent event) {
                    if (!event.getComponent().getName().equals("По умолчанию")) {
                        try {
                            Object nds = JOptionPane.showInputDialog("Введите НДС.");
                            if (nds != null) {
                                if (Integer.parseInt(nds.toString()) >= 0) {
                                    ndsValueSetting = Integer.parseInt(nds.toString());
                                    ndsSetting = event.getComponent().getName();
                                    jMenu6MenuSelected(null);
                                } else throw new Exception();
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Введено некорректное значение!\n " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        ndsValueSetting = -1;
                        ndsSetting = event.getComponent().getName();
                    }
                    cartFormUpdate();
                }
            });
            jMenu6.add(nds_);
        }
    }//GEN-LAST:event_jMenu6MenuSelected

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            JFormattedTextField format = new javax.swing.JFormattedTextField(maskFormatterDate());
            format.setText(MarketingForm.date);
            format.setSelectionStart(0);
            format.addKeyListener(null);
           /*format.addKeyListener (new KeyAdapter () {
                 public void keyPressed(KeyEvent ke){
                     if ( (ke.getKeyChar() == KeyEvent.VK_ENTER)) {
                         SwingUtilities.windowForComponent(this).dispose();
                         System.out.println("Enter has been pressed");
                     }
                 }
             });*/
            if (JOptionPane.showOptionDialog(null, format, "Заявка на дату:", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, format) == JOptionPane.YES_OPTION) {
                String tmpDate = format.getText().trim();
                if (checkDate(tmpDate)) {
                    if (new SimpleDateFormat("dd.MM.yyyy").parse(tmpDate).compareTo(new SimpleDateFormat("dd.MM.yyyy").parse(MarketingForm.tekDate)) < 0)
                        JOptionPane.showMessageDialog(null, "Ошибка актуальности ввода " + tmpDate + "\n Сегодня " + MarketingForm.tekDate, "Ошибка ввода", javax.swing.JOptionPane.ERROR_MESSAGE);
                    else
                        jLabel6.setText(MarketingForm.date = tmpDate);
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(CartForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    /**         Корзина с сортировкой         */
    public void showCatrTable() {
        TableSorterPanel newContentPane = new TableSorterPanel(this, true);
        newContentPane.setBounds(0, 0, jPanel1.getWidth(), jPanel1.getHeight());
        jPanel1.add(newContentPane);
        jLabel5.setText(summa + " " + propisValutaSetting);
        jLabel5.setHorizontalAlignment(JLabel.RIGHT);
        jLabel6.setText(MarketingForm.date);
        jTextPane1.setText(MarketingForm.noteorder);
    }

    private void displayCartTable() {
        Vector col = new Vector();
        col.add("наличие в заявке");
        col.add("№");
        col.add("Шифр");
        col.add("Артикул");
        col.add("Модель");
        col.add("Сорт");
        col.add("Изделие");
        col.add("Рост");
        col.add("Размер");
        col.add("Цвет");
        col.add("Кол-во");
        col.add("<html><B>Шифр</B></html>");
        col.add("<html><B>Артикул</B></html>");
        col.add("<html><B>Модель</B></html>");
        col.add("<html><B>Сорт</B></html>");
        col.add("<html><B>Изделие</B></html>");
        col.add("<html><B>Рост</B></html>");
        col.add("<html><B>Размер</B></html>");
        col.add("<html><B>Цвет</B></html>");
        col.add("<html><B>Кол-во</B></html>");

        try {
            tm = new DefaultTableModel(pdb.returnDataCartFromSklad(idClient, idOrder), col) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };

            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (table.getValueAt(row, 0).equals(0))
                        cell.setBackground(Color.LIGHT_GRAY);
                    else
                        cell.setBackground(table.getBackground());
                    cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (column > 10)
                        cell.setFont(new Font("Arial", Font.BOLD, 12));
                    return cell;
                }
            };

            table.setModel(tm);
            table.setAutoCreateColumnsFromModel(true);
            table.getColumnModel().getColumn(0).setMinWidth(0);
            table.getColumnModel().getColumn(0).setMaxWidth(0);
            table.getColumnModel().getColumn(1).setPreferredWidth(25);
            table.getColumnModel().getColumn(2).setPreferredWidth(80);
            table.getColumnModel().getColumn(3).setPreferredWidth(80);
            table.getColumnModel().getColumn(4).setPreferredWidth(40);
            table.getColumnModel().getColumn(5).setPreferredWidth(30);
            table.getColumnModel().getColumn(6).setPreferredWidth(110);
            table.getColumnModel().getColumn(7).setPreferredWidth(30);
            table.getColumnModel().getColumn(8).setPreferredWidth(40);
            table.getColumnModel().getColumn(9).setPreferredWidth(65);
            table.getColumnModel().getColumn(10).setPreferredWidth(40);
            table.getColumnModel().getColumn(11).setPreferredWidth(80);
            table.getColumnModel().getColumn(12).setPreferredWidth(80);
            table.getColumnModel().getColumn(13).setPreferredWidth(40);
            table.getColumnModel().getColumn(14).setPreferredWidth(30);
            table.getColumnModel().getColumn(15).setPreferredWidth(110);
            table.getColumnModel().getColumn(16).setPreferredWidth(30);
            table.getColumnModel().getColumn(17).setPreferredWidth(40);
            table.getColumnModel().getColumn(18).setPreferredWidth(65);
            table.getColumnModel().getColumn(19).setPreferredWidth(40);

            for (int i = 0; i < col.size(); i++)
                table.getColumnModel().getColumn(i).setCellRenderer(renderer);

            final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tm) {
                @Override
                public Comparator<?> getComparator(int column) {
                    if (column > 0 && column < 20 && column != 3 && column != 6 && column != 9 && column != 12 && column != 15 && column != 18) {
                        return new Comparator<String>() {
                            public int compare(String s1, String s2) {
                                try {
                                    return Integer.parseInt(s1) - Integer.parseInt(s2);
                                } catch (Exception e) {
                                    return s1.compareTo(s2);
                                }
                            }
                        };
                    }
                    return super.getComparator(column);
                }
            };
            table.setRowSorter(sorter);
            JScrollPane sp = new JScrollPane(table);
            sp.setSize(jPanel1.getWidth(), jPanel1.getHeight());
            jPanel1.add(sp);
        } catch (Exception e) {
            System.out.println("Ошибка! OrdersSklad() " + e);
        }
    }

    private void createNoteOrder() {
        final AbstractDocument document = (AbstractDocument) jTextPane1.getDocument();
        document.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((document.getLength() + string.length()) <= 245) {
                    fb.insertString(offset, string, attr);
                    MarketingForm.noteorder = jTextPane1.getText().trim();
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((document.getLength() - length + text.length()) <= 245) {
                    fb.replace(offset, length, text, attrs);
                    MarketingForm.noteorder = jTextPane1.getText().trim();
                }
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
                MarketingForm.noteorder = jTextPane1.getText().trim();
            }
        });
    }

    public void cartFormUpdate() {
        jPanel1.removeAll();
        showCatrTable();
        jPanel1.repaint();
        setVisible(true);
    }

    public class ValutaItem extends JCheckBoxMenuItem {
        private int kod;
        private String fullnaim;
        private String propis;

        public ValutaItem() {
        }

        public ValutaItem(int kod, String fullnaim, String propis) {
            this.kod = kod;
            this.fullnaim = fullnaim;
            this.propis = propis;
        }

        public int getKod() {
            return kod;
        }

        public void setKod(int kod) {
            this.kod = kod;
        }

        public String getFullnaim() {
            return fullnaim;
        }

        public void setFullnaim(String fullnaim) {
            this.fullnaim = fullnaim;
        }

        public String getPropis() {
            return propis;
        }

        public void setPropis(String propis) {
            this.propis = propis;
        }
    }
}
