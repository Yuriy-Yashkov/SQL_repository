package dept.sklad.specCena;

import common.ProgressBar;
import dept.sklad.SkladDB;
import dept.sklad.SkladOO;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 *
 * @author user
 */
public class SpecCenaReport extends JDialog {

    JFormattedTextField tfDateS = new JFormattedTextField();
    JFormattedTextField tfDateE = new JFormattedTextField();
    JLabel jlStartDate;
    JLabel jlEndDate;
    JFrame parent;
    String dfstr;
    MaskFormatter formatter;
    JButton btnOk;
    JButton btnCancel;
    SwingWorker sw;
    ProgressBar pb;

    public SpecCenaReport(JFrame paren) {
        super(paren);
        parent = paren;
        try {
            formatter = new MaskFormatter("##.##.####");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
        formatter.setPlaceholderCharacter('0');
        initcomp();
    }


    public void initcomp() {

        this.setLayout(new GridLayout(3, 2));
        jlStartDate = new JLabel("Дата от: ");
        jlEndDate = new JLabel("Дата до: ");

        Calendar cc = Calendar.getInstance();
        dfstr = "";
        int i = cc.get(Calendar.DAY_OF_MONTH);
        if (i < 10)
            dfstr += "0" + Integer.toString(i) + ".";
        else
            dfstr += "" + Integer.toString(i) + ".";
        i = cc.get(Calendar.MONTH) + 1;
        if (i < 10)
            dfstr += "0" + Integer.toString(i) + ".";
        else
            dfstr += "" + Integer.toString(i) + ".";
        dfstr += "" + cc.get(Calendar.YEAR);

        Object strqw = null;
        try {
            strqw = formatter.stringToValue(dfstr);
        } catch (Exception e) {
        }
        tfDateE = new JFormattedTextField(formatter);
        tfDateE.setValue(strqw);
        tfDateE.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnOk.doClick();
                }
            }
        });

        dfstr = "01" + dfstr.substring(2, 10);

        strqw = null;
        try {
            strqw = formatter.stringToValue(dfstr);
        } catch (Exception e) {
        }
        tfDateS = new JFormattedTextField(formatter);
        tfDateS.setValue(strqw);

        btnOk = new JButton("Сформировать");
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pb = new ProgressBar(parent, false, "Создание файла. Ожидайте...");
                class SWorker extends SwingWorker<HashMap, Object> {
                    @Override
                    protected HashMap doInBackground() throws Exception {

                        ArrayList arl = null;
                        HashMap hm = new HashMap();
                        SkladDB sdb = new SkladDB();
                        arl = sdb.getActualSpecCena(tfDateS.getText().trim(), tfDateE.getText().trim());
                        sdb.disConn();
                        SkladOO oo = new SkladOO(arl);
                        oo.createReport("ReportActualSpecCen.ots");

//                          
                        return new HashMap();
                    }

                    @Override
                    protected void done() {
                        try {
                            pb.setVisible(false);
                            pb.dispose();
                            setVisible(false);
                            dispose();
                        } catch (Exception ex) {
                            System.err.println("Ошибка при создании файла отгрузки. " + ex);
                        }
                    }
                }
                sw = new SWorker();
                sw.execute();
                pb.setVisible(true);
            }
        });

        btnCancel = new JButton("Отмена/Закрыть");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        this.add(jlStartDate);
        this.add(tfDateS);
        this.add(jlEndDate);
        this.add(tfDateE);
        this.add(btnOk);
        this.add(btnCancel);
        this.pack();
        setSize(350, 100);
        setLocationRelativeTo(parent);
        setResizable(false);
        setTitle("Экспорт актуальных спеццен.");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

    }
}