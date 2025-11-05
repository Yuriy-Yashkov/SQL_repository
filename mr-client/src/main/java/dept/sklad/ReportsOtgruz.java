/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.sklad;

import by.march8.ecs.framework.common.LogCrutch;
import common.ProgressBar;
import dept.component.MyButton;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author user
 */
public class ReportsOtgruz extends JDialog {

    //private static final Logger log = new Log().getLoger(SkladDB.class);
    private static final LogCrutch log = new LogCrutch();
    private JFormattedTextField jftfDateStart;
    private JFormattedTextField jftfDateEnd;
    private JLabel jlDateStart;
    private JLabel jlDateEnd;
    private MyButton mbOk;
    private MyButton mbClose;
    private MaskFormatter mfFormatDate;
    private Object oBuffer;
    private Calendar cDate;
    private String sDate;
    private GridBagLayout gblVariable;
    private GridBagConstraints gbcVariable;
    private SkladDB sdb;
    private SkladOO soo;
    private List alDataForRep;
    private ProgressBar pbUserAction;

    public ReportsOtgruz(JFrame parent) {
        super(parent);
        initComponent();
    }

    private void initComponent() {
        try {
            sdb = new SkladDB();
            gblVariable = new GridBagLayout();
            gbcVariable = new GridBagConstraints();
            setLayout(gblVariable);
            mfFormatDate = new MaskFormatter("##.##.####");
            mfFormatDate.setPlaceholderCharacter('0');
            oBuffer = null;

            jlDateStart = new JLabel("Дата от:");
            gbcVariable.gridx = 0;
            gbcVariable.gridy = 0;
            gbcVariable.anchor = GridBagConstraints.WEST;
            add(jlDateStart, gbcVariable);

            sDate = getFirstDateRO();
            oBuffer = mfFormatDate.stringToValue(sDate);
            jftfDateStart = new JFormattedTextField(mfFormatDate);
            jftfDateStart.setColumns(13);
            jftfDateStart.setValue(oBuffer);
            gbcVariable.gridx = 1;
            gbcVariable.gridy = 0;
            gbcVariable.anchor = GridBagConstraints.EAST;
            add(jftfDateStart, gbcVariable);

            jlDateEnd = new JLabel("Дата до:");
            gbcVariable.gridx = 0;
            gbcVariable.gridy = 1;
            gbcVariable.anchor = GridBagConstraints.WEST;
            add(jlDateEnd, gbcVariable);

            sDate = getNowDateRO();
            oBuffer = mfFormatDate.stringToValue(sDate);
            jftfDateEnd = new JFormattedTextField(mfFormatDate);
            jftfDateEnd.setColumns(13);
            jftfDateEnd.setValue(oBuffer);
            gbcVariable.gridx = 1;
            gbcVariable.gridy = 1;
            gbcVariable.anchor = GridBagConstraints.EAST;
            add(jftfDateEnd, gbcVariable);

            mbOk = new MyButton("Ok");
            mbOk.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    actionUser();
                }
            });
            gbcVariable.gridx = 0;
            gbcVariable.gridy = 2;
            gbcVariable.anchor = GridBagConstraints.WEST;
            add(mbOk, gbcVariable);

            mbClose = new MyButton("Закрыть");
            mbClose.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            gbcVariable.gridx = 1;
            gbcVariable.gridy = 2;
            gbcVariable.anchor = GridBagConstraints.EAST;
            add(mbClose, gbcVariable);

            setSize(350, 100);
            setLocationRelativeTo(null);
            setTitle("Отчет по отгрузке за период");
            setVisible(true);

        } catch (Exception ex) {
            exceptionChek(ex);
        }
    }

    private void exceptionChek(Exception ex) {
        StackTraceElement[] stak = ex.getStackTrace();
        log.error("\nException in class " + stak[0].getClassName() + ",\nin method " + stak[0].getMethodName() + ", in string number " + stak[0].getLineNumber() + ".\n"
                + "TEXT MESSAGE: \" " + ex.getMessage() + " \"");
    }

    private String getNowDateRO() {
        cDate = Calendar.getInstance();
        String sda = "";
        int i = cDate.get(Calendar.DAY_OF_MONTH);
        if (i < 10) {
            sda += "0" + Integer.toString(i) + ".";
        } else {
            sda += "" + Integer.toString(i) + ".";
        }
        i = cDate.get(Calendar.MONTH) + 1;
        if (i < 10) {
            sda += "0" + Integer.toString(i) + ".";
        } else {
            sda += "" + Integer.toString(i) + ".";
        }
        sda += "" + cDate.get(Calendar.YEAR);
        return sda;
    }

    private String getFirstDateRO() {
        cDate = Calendar.getInstance();
        String sda = "";
        int i;
        sda += "01.";
        i = cDate.get(Calendar.MONTH) + 1;
        if (i < 10) {
            sda += "0" + Integer.toString(i) + ".";
        } else {
            sda += "" + Integer.toString(i) + ".";
        }
        sda += "" + cDate.get(Calendar.YEAR);
        return sda;
    }

    private void actionUser() {
        if (ceheckInputValue()) {
            pbUserAction = new ProgressBar(this, false, "Получение данных...");
            class SWorker extends SwingWorker<Void, Void> {

                public SWorker() {
                }

                @Override
                protected Void doInBackground() throws Exception {
                    Void qw;
                    qw = null;
                    alDataForRep = sdb.otgruzkaSkladNakl(jftfDateStart.getText().trim(), jftfDateEnd.getText().trim());
                    return qw;
                }

                @Override
                protected void done() {
                    try {
                        pbUserAction.dispose();
                    } catch (Exception ex) {
                        exceptionChek(ex);
                    }
                }
            }
            SWorker sw = new SWorker();
            sw.execute();
            pbUserAction.setVisible(true);

            if (alDataForRep.size() != 0) {
                pbUserAction = new ProgressBar(this, false, "Формирование отчета...");
                class SWorker1 extends SwingWorker<Void, Void> {

                    public SWorker1() {
                    }

                    @Override
                    protected Void doInBackground() throws Exception {
                        Void qw;
                        qw = null;
                        soo = new SkladOO(alDataForRep);
                        soo.reportForOtgruz("OtzruzkaReports.ots");
                        return qw;
                    }

                    @Override
                    protected void done() {
                        try {
                            JOptionPane.showMessageDialog(null, "Отчет сформирован");
                            pbUserAction.dispose();
                        } catch (Exception ex) {
                            exceptionChek(ex);
                        }
                    }
                }
                SWorker1 sw1 = new SWorker1();
                sw1.execute();
                pbUserAction.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "За заданный период отгрузки не было.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Введено некоректное значение.\nПроверьте правильность ввода.");
        }
    }

    public boolean ceheckInputValue() {
        boolean result;
        int dayE = Integer.valueOf(jftfDateEnd.getText().substring(0, 2));
        int monthE = Integer.valueOf(jftfDateEnd.getText().substring(3, 5));
        int yearE = Integer.valueOf(jftfDateEnd.getText().substring(6, 10));
        int dayS = Integer.valueOf(jftfDateStart.getText().substring(0, 2));
        int monthS = Integer.valueOf(jftfDateStart.getText().substring(3, 5));
        int yearS = Integer.valueOf(jftfDateStart.getText().substring(6, 10));
        if ((dayE > 0 && dayE < 32) && (dayS > 0 && dayS < 32)) {
            if (monthE > 0 && monthE < 13 && monthS > 0 && monthS < 13) {
                result = yearE > 1990 && yearE < 2050 && yearS > 1990 && yearS < 2050;
            } else {
                result = false;
            }
        } else {
            result = false;
        }
        return result;
    }
}
