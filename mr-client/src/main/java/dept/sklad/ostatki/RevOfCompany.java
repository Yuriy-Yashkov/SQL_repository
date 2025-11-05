/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.sklad.ostatki;

import common.ProgressBar;
import dept.component.MyButton;
import dept.component.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 *
 * @author user
 */
@SuppressWarnings({"serial"})
public class RevOfCompany extends JDialog {

    FilterRemain rf;
    private MyButton pbut;
    private MyButton pbutCan;
    private MyButton pbutSetting;
    private Table tb;
    private RemainsDataBase rdb;
    private JPanel jpButton;
    private JPanel jpLabel;
    private ArrayList<Object> result;
    private JPanel jpVO;
    private JPanel jpP;
    private JPanel jpR;
    private JPanel jpO;
    private int screenWidth;
    private int screenHeight;
    private double lkVO;
    private double lSFVO;
    private double lSCVO;
    private double lKP;
    private double lSFP;
    private double lSCP;
    private double lKR;
    private double lSFR;
    private double lSCR;
    private double lKO;
    private double lSFO;
    private double lSCO;
    private JLabel lbkVO;
    private JLabel lbSFVO;
    private JLabel lbSCVO;
    private JLabel lbKP;
    private JLabel lbSFP;
    private JLabel lbSCP;
    private JLabel lbKR;
    private JLabel lbSFR;
    private JLabel lbSCR;
    private JLabel lbKO;
    private JLabel lbSFO;
    private JLabel lbSCO;
    private Toolkit tk;
    private Dimension dim;
    private BigDecimal bg;
    private GridBagLayout gbl;
    private GridBagConstraints gbc;
    private JFrame paren;
    private ProgressBar pb;
    private String query = "_get_now_rev_to_company";

    public RevOfCompany(JFrame parent) {
        super(parent);
        paren = parent;


        pb = new ProgressBar(parent, false, "Получение данных...");
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

    public String getQuery() {
        return query;
    }

    private void initialization() {
        try {
            rdb = new RemainsDataBase();

            tb = new Table(query);
            gbl = new GridBagLayout();
            gbc = new GridBagConstraints();
            gbc.insets = new Insets(1, 1, 1, 1);
            setLayout(gbl);
            jpButton = new JPanel(new FlowLayout());
            pbut = new MyButton("Отчет");
            pbut.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    RemainsReports rr = new RemainsReports(tb.getData());
                    rr.createReport("NowRev.ots");
                }
            });
            pbutCan = new MyButton("Закрыть");
            pbutCan.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            pbutSetting = new MyButton("Фильтр");
            pbutSetting.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    rf = new FilterRemain(paren, query);
                    rf.showForm(true);
                    checkAction();
                }
            });
            lbkVO = new JLabel();
            lbSFVO = new JLabel();
            lbSCVO = new JLabel();
            lbKP = new JLabel();
            lbSFP = new JLabel();
            lbSCP = new JLabel();
            lbKR = new JLabel();
            lbSFR = new JLabel();
            lbSCR = new JLabel();
            lbKO = new JLabel();
            lbSFO = new JLabel();
            lbSCO = new JLabel();
            resultSum();
            jpVO = new JPanel(new GridLayout(1, 3));
            jpVO.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            jpVO.setBorder(BorderFactory.createTitledBorder("Входящие остатки"));
            jpVO.add(lbkVO);
            jpVO.add(lbSFVO);
            jpVO.add(lbSCVO);

            jpP = new JPanel(new GridLayout(1, 3));
            jpP.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            jpP.setBorder(BorderFactory.createTitledBorder("Приход"));
            jpP.add(lbKP);
            jpP.add(lbSFP);
            jpP.add(lbSCP);

            jpR = new JPanel(new GridLayout(1, 3));
            jpR.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            jpR.setBorder(BorderFactory.createTitledBorder("Расход"));
            jpR.add(lbKR);
            jpR.add(lbSFR);
            jpR.add(lbSCR);

            jpO = new JPanel(new GridLayout(1, 3));
            jpO.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            jpO.setBorder(BorderFactory.createTitledBorder("Остатки"));
            jpO.add(lbKO);
            jpO.add(lbSFO);
            jpO.add(lbSCO);

            printLabel();

            jpLabel = new JPanel(new GridLayout(4, 1));
            jpLabel.add(jpVO);
            jpLabel.add(jpP);
            jpLabel.add(jpR);
            jpLabel.add(jpO);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbc.fill = GridBagConstraints.BOTH;
            tk = Toolkit.getDefaultToolkit();
            dim = tk.getScreenSize();
            screenWidth = dim.width - 100;
            screenHeight = dim.height - 100;
            tb.setPreferredSize(new Dimension(screenWidth - 10, screenHeight - 300));
            add(tb, gbc);
            jpButton.add(pbut);
            jpButton.add(pbutSetting);
            jpButton.add(pbutCan);
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            add(jpLabel, gbc);
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            add(jpButton, gbc);
            setSize(new Dimension(screenWidth, screenHeight));
            setResizable(false);
            setTitle("Оборотная ведомость");
            setLocationRelativeTo(null);
            setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void resultSum() {
        try {
            lkVO = 0;
            lSFVO = 0;
            lSCVO = 0;
            lKP = 0;
            lSFP = 0;
            lSCP = 0;
            lKR = 0;
            lSFR = 0;
            lSCR = 0;
            lKO = 0;
            lSFO = 0;
            lSCO = 0;
            for (int i = 0; i < tb.getData().size(); i++) {
                ArrayList<Object> buf = tb.getData().get(i);
                lkVO += Double.valueOf(buf.get(7).toString());
                lSFVO += Double.valueOf(buf.get(8).toString());
                lSCVO += Double.valueOf(buf.get(9).toString());
                lKP += Double.valueOf(buf.get(10).toString());
                lSFP += Double.valueOf(buf.get(11).toString());
                lSCP += Double.valueOf(buf.get(12).toString());
                lKR += Double.valueOf(buf.get(13).toString());
                lSFR += Double.valueOf(buf.get(14).toString());
                lSCR += Double.valueOf(buf.get(15).toString());
                lKO += Double.valueOf(buf.get(16).toString());
                lSFO += Double.valueOf(buf.get(17).toString());
                lSCO += Double.valueOf(buf.get(18).toString());
            }
            result = new ArrayList<Object>();
            result.add(lkVO);
            result.add(lSFVO);
            result.add(lSCVO);
            result.add(lKP);
            result.add(lSFP);
            result.add(lSCP);
            result.add(lKR);
            result.add(lSFR);
            result.add(lSCR);
            result.add(lKO);
            result.add(lSFO);
            result.add(lSCO);
        } catch (NumberFormatException ex) {
            System.out.println(ex + this.getClass().getName().toString());
        }
    }

    private void printLabel() {
        bg = new BigDecimal((Double.valueOf(result.get(0).toString())));
        lbkVO.setText("Кол-во: " + String.valueOf(bg));
        bg = new BigDecimal((Double.valueOf(result.get(1).toString())));
        lbSFVO.setText("Сумма факт.: " + String.valueOf(bg));
        bg = new BigDecimal((Double.valueOf(result.get(2).toString())));
        lbSCVO.setText("Сумма цен.: " + String.valueOf(bg));

        bg = new BigDecimal((Double.valueOf(result.get(3).toString())));
        lbKP.setText("Кол-во: " + String.valueOf(bg));
        bg = new BigDecimal((Double.valueOf(result.get(4).toString())));
        lbSFP.setText("Сумма факт.: " + String.valueOf(bg));
        bg = new BigDecimal((Double.valueOf(result.get(5).toString())));
        lbSCP.setText("Сумма цен.: " + String.valueOf(bg));

        bg = new BigDecimal((Double.valueOf(result.get(6).toString())));
        lbKR.setText("Кол-во: " + String.valueOf(bg));
        bg = new BigDecimal((Double.valueOf(result.get(7).toString())));
        lbSFR.setText("Сумма факт.: " + String.valueOf(bg));
        bg = new BigDecimal((Double.valueOf(result.get(8).toString())));
        lbSCR.setText("Сумма цен.: " + String.valueOf(bg));

        bg = new BigDecimal((Double.valueOf(result.get(9).toString())));
        lbKO.setText("Кол-во: " + String.valueOf(bg));
        bg = new BigDecimal((Double.valueOf(result.get(10).toString())));
        lbSFO.setText("Сумма факт.: " + String.valueOf(bg));
        bg = new BigDecimal((Double.valueOf(result.get(11).toString())));
        lbSCO.setText("Сумма цен.: " + String.valueOf(bg));
    }

    @SuppressWarnings("empty-statement")
    private void checkAction() {
        rdb = new RemainsDataBase();
        pb.setTitle("Обновление данных");
        class SWorker extends SwingWorker<String, Object> {

            public SWorker() {
            }

            @Override
            protected String doInBackground() throws Exception {
                try {
                    tb.updateTable(rdb.getDataWithCondition(System.getProperty("query")));
                    System.setProperty(query, "");

                } catch (Exception ex) {
                    System.out.println(ex + this.getClass().getName().toString());
                }
                return "";
            }

            @Override
            protected void done() {
                try {
                    resultSum();
                    printLabel();
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
}