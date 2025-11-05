/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.sklad.ostatki;

import common.ProgressBar;
import dept.component.MyButton;
import dept.component.ShowFilters;
import dept.component.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Форма отображения текущих остатков
 *
 * @author dBozhkou
 */
@SuppressWarnings({"serial"})
public class NowRemains extends JDialog {

    public Table tb;
    private MyButton pbut;
    private MyButton pbutCan;
    private MyButton mbFilter;
    private RemainsDataBase rdb;
    //private ArrayList<String> columnName;
    private JPanel jpButton;
    private double lKO;
    private double lSFO;
    private double lSCO;
    private JLabel lbKO;
    private JLabel lbSFO;
    private JLabel lbSCO;
    private JPanel jpO;
    private ArrayList<Object> result;
    private GridBagLayout gbl;
    private GridBagConstraints gbc;
    private BigDecimal bg;
    private JFrame paren;
    private String query = " _get_now_remains ";
    private FilterRemain rf;
    private RemainsReports rr;
    private ProgressBar pb;

    /**
     * Конструктор
     *
     * @param parent Родительская форма (форма из которой вызвали)
     */
    public NowRemains(JFrame parent) {
        super(parent);
        paren = parent;
        pb = new ProgressBar(this, false, "Получение данных...");
        class SWorker extends SwingWorker<String, Object> {

            public SWorker() {
            }

            @Override
            protected String doInBackground() throws Exception {
                initialization();
                return "";
            }

            @Override
            protected void done() {
                try {

                    pb.dispose();
                } catch (Exception ex) {
                    System.err.println("Ошибка при получении результатов из фонового потока " + ex);
                }
            }
        }
        SWorker ss = new SWorker();
        ss.execute();
        pb.setVisible(true);
    }

    private void initialization() {
        try {
            lbKO = new JLabel();
            lbSFO = new JLabel();
            lbSCO = new JLabel();
            rdb = new RemainsDataBase();
//            columnName = new ArrayList<String>();
//            columnName.add("Шифр");
//            columnName.add("Артикул");
//            columnName.add("Модель");
//            columnName.add("Раз-р");
//            columnName.add("Рост");
//            columnName.add("Сорт");
//            columnName.add("Цена");
//            columnName.add("Кол-во");
//            columnName.add("Сумма факт.");
//            columnName.add("Сумма цен.");
            tb = new Table(query);

            gbl = new GridBagLayout();
            gbc = new GridBagConstraints();
            gbc.insets = new Insets(1, 1, 1, 1);
            setLayout(gbl);
            jpButton = new JPanel(new FlowLayout());
            pbut = new MyButton("Отчет");
            pbut.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    rr = new RemainsReports(tb.getData());
                    rr.createReport("NowRemains.ots");
                }
            });
            pbutCan = new MyButton("Закрыть");
            pbutCan.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            mbFilter = new MyButton("Фильтр");
            mbFilter.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    //new FilterFormFromGrid(this,tb);
                    new ShowFilters(tb, this, paren);
//                    rf = new FilterRemain(paren, query);
//                    rf.showForm(true);
//                    checkAction();
                }
            });
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbc.fill = GridBagConstraints.BOTH;
            tb.setPreferredSize(new Dimension(850, 300));
            tb.getMyTable().getColumnModel().getColumn(0).setPreferredWidth(60);
            tb.getMyTable().getColumnModel().getColumn(1).setPreferredWidth(80);
            tb.getMyTable().getColumnModel().getColumn(2).setPreferredWidth(40);
            tb.getMyTable().getColumnModel().getColumn(3).setPreferredWidth(50);
            tb.getMyTable().getColumnModel().getColumn(4).setPreferredWidth(50);
            tb.getMyTable().getColumnModel().getColumn(5).setPreferredWidth(30);
            tb.getMyTable().getColumnModel().getColumn(6).setPreferredWidth(80);
            tb.getMyTable().getColumnModel().getColumn(7).setPreferredWidth(50);
            tb.getMyTable().getColumnModel().getColumn(8).setPreferredWidth(100);
            tb.getMyTable().getColumnModel().getColumn(9).setPreferredWidth(100);
            resultSum();
            jpO = new JPanel(new GridLayout(1, 3));
            jpO.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            jpO.setBorder(BorderFactory.createTitledBorder("Остатки"));
            jpO.add(lbKO);
            jpO.add(lbSFO);
            jpO.add(lbSCO);
            printLabel();

            add(tb, gbc);
            jpButton.add(pbut);
            jpButton.add(mbFilter);
            jpButton.add(pbutCan);

            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            add(jpButton, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            add(jpO, gbc);


            setSize(new Dimension(860, 450));
            setLocationRelativeTo(null);
            //setResizable(false);
            setVisible(true);
        } catch (Exception ex) {
            System.err.println("Error in class " + this.getClass().getName() + ", method ");
        }
    }

    private void resultSum() {
        int burum = 0;
        try {
            lKO = 0;
            lSFO = 0;
            lSCO = 0;
            for (int i = 0; i < tb.getData().size(); i++) {
                if (burum == 220) {
                    ArrayList<Object> buf = tb.getData().get(i);
                    lKO += Double.valueOf(buf.get(7).toString().replace(",", "."));
                    lSFO += Double.valueOf(buf.get(8).toString().replace(",", "."));
                    lSCO += Double.valueOf(buf.get(9).toString().replace(",", "."));
                    burum = i;
                } else {
                    ArrayList<Object> buf = tb.getData().get(i);
                    lKO += Double.valueOf(buf.get(7).toString().replace(",", "."));
                    lSFO += Double.valueOf(buf.get(8).toString().replace(",", "."));
                    lSCO += Double.valueOf(buf.get(9).toString().replace(",", "."));
                    burum = i;
                }
            }
            result = new ArrayList<Object>();
            result.add(lKO);
            result.add(lSFO);
            result.add(lSCO);
        } catch (Exception ex) {
            System.out.print("ERRrorka " + ex + " " + Integer.toString(burum));
        }
    }

    private void printLabel() {
        bg = new BigDecimal((Double.valueOf(result.get(0).toString())));
        lbKO.setText("Кол-во: " + String.valueOf(bg));
        bg = new BigDecimal((Double.valueOf(result.get(1).toString())));
        lbSFO.setText(("Сумма факт.: " + String.valueOf(bg)));
        bg = new BigDecimal((Double.valueOf(result.get(2).toString())));
        lbSCO.setText(("Сумма цен.: " + String.valueOf(bg)));
    }

    public void checkAction() {
        try {
            rdb = new RemainsDataBase();
            pb.setTitle("Обновление результатов...");
            class SWorker1 extends SwingWorker<String, Object> {

                public SWorker1() {
                }

                @Override
                protected String doInBackground() throws Exception {
                    try {
                        tb.updateTable(rdb.getDataWithCondition(System.getProperty("query")));
                        System.setProperty(query, "");
                    } catch (Exception ex) {
                        System.out.println(ex + this.getClass().getName());
                    }
                    return "";
                }

                @Override
                protected void done() {
                    try {

                        pb.dispose();
                        resultSum();
                        printLabel();
                    } catch (Exception ex) {
                        System.err.println("Ошибка при получении результатов из фонового потока " + ex);
                    }
                }
            }
            SWorker1 ss = new SWorker1();
            ss.execute();
            pb.setVisible(true);
        } catch (Exception ex) {
            System.err.println("Error in class " + this.getClass().getName() + "checkAction");
        }
    }
}
