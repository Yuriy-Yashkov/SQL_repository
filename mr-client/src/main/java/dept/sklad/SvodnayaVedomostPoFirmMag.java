/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.sklad;

import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.SummaryStatementByContractorReport;
import common.ProgressBar;
import dept.component.MyButton;
import workDB.DBF;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author user
 */
@SuppressWarnings("all")
public class SvodnayaVedomostPoFirmMag extends JDialog {

    public ProgressBar pb;
    public JDialog parampam;
    private JComboBox jcbFirmShop;
    private JLabel jlFirstDate;
    private JLabel jlLastDate;
    private JComboBox jcbTypeReport;
    private JFormattedTextField jtfFirstDate;
    private JFormattedTextField jtfLastDate;
    private MyButton mbCreateReport;
    private MyButton mbExitForm;
    private JButton btnExport;
    private MaskFormatter mask;
    private List<Map<String, Object>> listShop;
    private int kpl;
    private String typeRep = "";
    private String typeOper = "";
    private String dfstr = "";
    private SkladDB sdb;
    private List ara;
    private String[] listType = {"Отгрузка(Готовая)", "Возврат(Готовая)", "Отгрузка(Несортное)", "Возврат(Несортное)", "Отгрузка(Ширпотреб)", "Возврат(Ширпотреб)"};
    private String[] tRep = {" (((kld.sar like '41%' or kld.sar like '42%') and (sd.srt=1 or sd.srt=2)) or (kld.sar like '43%' and sd.srt=1)) ",
            " (((kld.sar like '41%' or kld.sar like '42%') and (sd.srt=3)) or (kld.sar like '43%' and (sd.srt=2 or sd.srt=3 or sd.srt=4))) ",
            " (kld.sar like '48%' and sd.srt=4) "};
    private String[] tOp = {" (t1.operac like '%Отгруз%' or t1.operac like '%Перем%') ",
            " (t1.operac like '%Возв%') "};
    private Map<String, Object> hhmm;
    private HashMap<String, Object> hhm;
    private ArrayList<HashMap<String, Object>> arHHM;

    public SvodnayaVedomostPoFirmMag(JFrame parent) {
        super(parent);
        parampam = this;
        initComponent();
    }

    private void initComponent() {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(gbl);
        sdb = new SkladDB();
        jcbFirmShop = new JComboBox();
        listShop = sdb.getFirmShop();
        for (int i = 0; i < listShop.size(); i++) {
            Map hm = listShop.get(i);
            jcbFirmShop.addItem(hm.get("name"));
        }
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        add(jcbFirmShop, gbc);

        jcbTypeReport = new JComboBox(listType);
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(jcbTypeReport, gbc);

        jlFirstDate = new JLabel("Дата с:");
        jlFirstDate.setLabelFor(jtfFirstDate);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(jlFirstDate, gbc);


        try {
            mask = new MaskFormatter("##.##.####");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
        mask.setPlaceholderCharacter('0');
        Calendar cc = Calendar.getInstance();
        dfstr = "01.";
        int i = cc.get(Calendar.MONTH) + 1;
        if (i < 10) {
            dfstr += "0" + Integer.toString(i) + ".";
        } else {
            dfstr += "" + Integer.toString(i) + ".";
        }
        dfstr += "" + cc.get(Calendar.YEAR);
        jtfFirstDate = new JFormattedTextField(mask);
        Object strqw = null;
        try {
            strqw = mask.stringToValue(dfstr);
        } catch (Exception e) {
        }
        jtfFirstDate.setValue(strqw);

        dfstr = "";
        i = cc.get(Calendar.DAY_OF_MONTH);
        if (i < 10) {
            dfstr += "0" + Integer.toString(i) + ".";
        } else {
            dfstr += "" + Integer.toString(i) + ".";
        }
        i = cc.get(Calendar.MONTH) + 1;
        if (i < 10) {
            dfstr += "0" + Integer.toString(i) + ".";
        } else {
            dfstr += "" + Integer.toString(i) + ".";
        }
        dfstr += "" + cc.get(Calendar.YEAR);
        jtfLastDate = new JFormattedTextField(mask);
        strqw = null;
        try {
            strqw = mask.stringToValue(dfstr);
        } catch (Exception e) {
        }
        jtfLastDate.setValue(strqw);
        jtfFirstDate.setColumns(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(jtfFirstDate, gbc);


        jlLastDate = new JLabel("Дата по:");
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        add(jlLastDate, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.EAST;
        jtfLastDate.setColumns(15);
        add(jtfLastDate, gbc);

        mbCreateReport = new MyButton("Отчет");
        mbCreateReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //prepareReport(0);
                String[] buff;
                buff = jcbFirmShop.getItemAt(jcbFirmShop.getSelectedIndex()).toString().split("^\\(|\\)");
                kpl = Integer.valueOf(buff[1].toString());

                new SummaryStatementByContractorReport(kpl,
                        DateUtils.getDateByStringValue(jtfFirstDate.getText()),
                        DateUtils.getDateByStringValue(jtfLastDate.getText()),
                        jcbFirmShop.getItemAt(jcbFirmShop.getSelectedIndex()).toString());

            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        add(mbCreateReport, gbc);


        gbc.gridx = 1;
        btnExport = new JButton("Отчет для 1C");
        btnExport.addActionListener(a -> {
            prepareReport(1);
        });
        //add(btnExport, gbc);


        mbExitForm = new MyButton("Закрыть");
        mbExitForm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();
            }
        });


        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(mbExitForm, gbc);
        setSize(new Dimension(640, 130));
        setLocationRelativeTo(null);
        setTitle("Сводная ведомость(7-я сводка)");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }


    private void prepareReport(int type) {
        String[] buff;
        buff = jcbFirmShop.getItemAt(jcbFirmShop.getSelectedIndex()).toString().split("^\\(|\\)");
        kpl = Integer.valueOf(buff[1].toString());

        int chek = jcbTypeReport.getSelectedIndex();
        switch (chek) {
            case 0: {
                typeRep = tRep[0];
                typeOper = tOp[0];
                break;
            }
            case 1: {
                typeRep = tRep[0];
                typeOper = tOp[1];
                break;
            }
            case 2: {
                typeRep = tRep[1];
                typeOper = tOp[0];
                break;
            }
            case 3: {
                typeRep = tRep[1];
                typeOper = tOp[1];
                break;
            }
            case 4: {
                typeRep = tRep[2];
                typeOper = tOp[0];
                break;
            }
            case 5: {
                typeRep = tRep[2];
                typeOper = tOp[1];
                break;
            }
        }

        hhmm = new HashMap<String, Object>();
        hhmm.put("operac", typeOper);
        hhmm.put("kpl", String.valueOf(kpl));
        hhmm.put("type", typeRep);
        hhmm.put("date", "'" + jtfFirstDate.getText() + "' and '" + jtfLastDate.getText() + "'");
        hhm = new HashMap<String, Object>();
        arHHM = new ArrayList<HashMap<String, Object>>();
        hhm.put("key", "Сводная ведомость по: " + jcbFirmShop.getItemAt(jcbFirmShop.getSelectedIndex()).toString() + " (" + jcbTypeReport.getItemAt(jcbTypeReport.getSelectedIndex()).toString() + ").\nПериод: с " + jtfFirstDate.getText() + " по " + jtfLastDate.getText());
        arHHM.add(hhm);
        pb = new ProgressBar(parampam, false, "Получение деталей...");
        class SWorker extends SwingWorker<List, Object> {

            public SWorker() {
            }

            @Override
            protected List doInBackground() throws Exception {
                ara = sdb.svodnayaVedomostPoFirmMag(hhmm);
                return ara;
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
        SWorker sw = new SWorker();
        sw.execute();
        pb.setVisible(true);
        if (ara != null) {
            if (ara.size() > 0) {
                if (type == 0) {
                    SkladOO so = new SkladOO(ara, arHHM);
                    so.createReport("firmMag.ots");
                } else {

                    JFileChooser fc = new JFileChooser();
                    int f = 1; // тип накладной
                    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                    if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                        String pathSave = new String(fc.getSelectedFile().getPath());
                        exportDbf(ara, pathSave, (String) hhmm.get("kpl"), jtfFirstDate.getText());
                    }
                }
                JOptionPane.showMessageDialog(null, "Отчет сформирован.");
            } else {
                JOptionPane.showMessageDialog(null, "Для " + jcbFirmShop.getItemAt(jcbFirmShop.getSelectedIndex()).toString() + " \nне было "
                        + jcbTypeReport.getItemAt(jcbTypeReport.getSelectedIndex()).toString() + ".");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Для " + jcbFirmShop.getItemAt(jcbFirmShop.getSelectedIndex()).toString() + " \nне было "
                    + jcbTypeReport.getItemAt(jcbTypeReport.getSelectedIndex()).toString() + ".");

        }
    }


    public String exportDbf(List<Map> dataArray, String pathSave, String contractorCode, String dateBegin) {

        StringBuilder fName = new StringBuilder();

        String param;
        DBF dbf = null;
        if (pathSave.equals("".toString()) || pathSave == null) {
            dbf = new DBF(99, "7_" + dateBegin + "_" + contractorCode, pathSave);
        } else dbf = new DBF(99, "7_" + dateBegin + "_" + contractorCode, pathSave);

        try {
            dbf.conn();
            for (Map<String, Object> map : dataArray) {
                Object[] v = new Object[13];

                v[0] = map.get("ndoc");
                v[1] = map.get("date_doc");
                v[2] = map.get("nds_value");
                v[3] = jcbTypeReport.getSelectedItem();
                v[4] = map.get("put_list");
                v[5] = map.get("kol");
                v[6] = map.get("summa");
                v[7] = map.get("summa_nds");
                v[8] = map.get("sum_torg_nad");
                v[9] = map.get("nds_tn");
                v[10] = map.get("all_nds");
                v[11] = map.get("rozn_sum_kid");
                v[12] = map.get("rozn_sum_adult");

                dbf.write(v);
            }
        } catch (Exception e) {
            System.out.println(e);

        } finally {
            if (dbf != null) dbf.disconn();
        }
        return fName.toString();
    }
}

/* 

Готовая  - (((kld.sar like '41%' or kld.sar like '42%') and (sd.srt=1 or sd.srt=2)) or (kld.sar like '43%' and sd.srt=1))
Несортная - (((kld.sar like '41%' or kld.sar like '42%') and (sd.srt=3)) or (kld.sar like '43%' and (sd.srt=2 or sd.srt=3 or sd.srt=4))
ШирПотреб - (kld.sar like '48%' and sd.srt=4)

Отгрузка - t1.operac like '%Отгруз%' or t1.operac like '%Перем%'
Возвраты - t1.operac like '%Возв%'

Кому:
3850 - Валентина
2869 - Светлогорск
6045 - Янина
6250 - Элегия
3855 - 31 Павильон

date 

*/