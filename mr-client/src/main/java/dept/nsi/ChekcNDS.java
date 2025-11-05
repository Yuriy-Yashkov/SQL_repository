/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.nsi;

import com.toedter.calendar.JDateChooser;
import common.ProgressBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.HashMap;

/**
 *
 * @author DBozhkou
 * Класс проверки НДС в таблице nsi_sd с таблицей nsi_nds
 */
public class ChekcNDS extends JDialog {

    JPanel jpContent;
    JPanel jpFooter;

    JCheckBox cbBelTrikVz;          //Бельевой трикотаж взрослый
    JCheckBox cbBelTrikDet;          //Бельевой трикотаж детский
    JCheckBox cbVerhTrikVz;          //Верхний трикотаж взрослый
    JCheckBox cbVerhTrikDet;          //Верхний трикотаж детский
    JCheckBox cbChulkiVz;          //Чулочно-носочные взрослые
    JCheckBox cbChulkiDet;          //Чулочно-носочные детские
    JCheckBox cbPolotno;          //Полотно
    JCheckBox cbEksport;          //Экспорт
    JCheckBox cbDavlSir;          //Давальческое сырье

    JButton btnCheck;
    JButton btnClose;

    JDateChooser startDate;
    JDateChooser endDate;
    Calendar cal;

    SwingWorker sw;
    ProgressBar pb;

    JFrame thisFrom;

    public ChekcNDS(JFrame parent) {
        thisFrom = parent;
        init();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    public void init() {

        this.jpContent = new JPanel(new GridLayout(12, 1, 3, 3));
        this.jpContent.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.jpContent.setBorder(BorderFactory.createTitledBorder("Какой тип изделий для проверки."));
        this.jpFooter = new JPanel(new FlowLayout());

        cal = Calendar.getInstance();
        this.startDate = new JDateChooser();
        this.endDate = new JDateChooser();
        startDate.setDate(cal.getTime());
        endDate.setDate(cal.getTime());

        this.cbBelTrikVz = new JCheckBox("Бельевой трикотаж вз.");
        this.cbBelTrikVz.setSelected(true);
        this.cbBelTrikDet = new JCheckBox("Бельевой трикотаж дет.");
        this.cbBelTrikDet.setSelected(true);
        this.cbVerhTrikVz = new JCheckBox("Верхний трикотаж вз.");
        this.cbVerhTrikVz.setSelected(true);
        this.cbVerhTrikDet = new JCheckBox("Верхний трикотаж дет.");
        this.cbVerhTrikDet.setSelected(true);
        this.cbChulkiVz = new JCheckBox("Чулочно-носочные вз.");
        this.cbChulkiVz.setSelected(true);
        this.cbChulkiDet = new JCheckBox("Чулочно-носочные дет.");
        this.cbChulkiDet.setSelected(true);
        this.cbPolotno = new JCheckBox("Полотно");
        this.cbPolotno.setSelected(true);
        this.cbEksport = new JCheckBox("Экспорт");
        this.cbEksport.setSelected(true);
        this.cbDavlSir = new JCheckBox("Давальческое сырье");
        this.cbDavlSir.setSelected(true);

        this.btnCheck = new JButton("Проверить");
        this.btnCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pb = new ProgressBar(thisFrom, false, "Проверка НДС");
                sw = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        check();
                        return 0;
                    }

                    @Override
                    protected void done() {
                        pb.dispose();
                    }
                };
                sw.execute();
                pb.setVisible(true);
            }
        });

        this.btnClose = new JButton("Закрыть");
        this.btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        this.jpFooter.add(btnCheck);
        this.jpFooter.add(btnClose);

        this.jpContent.add(startDate);
        this.jpContent.add(endDate);
        this.jpContent.add(cbBelTrikVz);
        this.jpContent.add(cbBelTrikDet);
        this.jpContent.add(cbVerhTrikVz);
        this.jpContent.add(cbVerhTrikDet);
        this.jpContent.add(cbChulkiVz);
        this.jpContent.add(cbChulkiDet);
        this.jpContent.add(cbPolotno);
        this.jpContent.add(cbEksport);
        this.jpContent.add(cbDavlSir);
        this.jpContent.add(jpFooter);

        this.add(jpContent);
        this.pack();
        this.setSize(270, 450);
        this.setTitle("Проверка НДС");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }

    public void check() {

        HashMap<String, Object> hm = new HashMap();
        long sDate = startDate.getDate().getTime();
        long eDate = endDate.getDate().getTime();
        int i = 0;

        if (this.cbBelTrikVz.isSelected()) {
            hm.put("cbBelTrikVz", 1);
        } else {
            hm.put("cbBelTrikVz", 0);
        }
        if (this.cbVerhTrikVz.isSelected()) {
            hm.put("cbVerhTrikVz", 1);
        } else {
            hm.put("cbVerhTrikVz", 0);
        }
        if (this.cbChulkiVz.isSelected()) {
            hm.put("cbChulkiVz", 1);
        } else {
            hm.put("cbChulkiVz", 0);
        }
        if (this.cbBelTrikDet.isSelected()) {
            hm.put("cbBelTrikDet", 1);
        } else {
            hm.put("cbBelTrikDet", 0);
        }
        if (this.cbVerhTrikDet.isSelected()) {
            hm.put("cbVerhTrikDet", 1);
        } else {
            hm.put("cbVerhTrikDet", 0);
        }
        if (this.cbChulkiDet.isSelected()) {
            hm.put("cbChulkiDet", 1);
        } else {
            hm.put("cbChulkiDet", 0);
        }
        if (this.cbPolotno.isSelected()) {
            hm.put("cbPolotno", 1);
        } else {
            hm.put("cbPolotno", 0);
        }
        if (this.cbEksport.isSelected()) {
            hm.put("cbEksport", 1);
        } else {
            hm.put("cbEksport", 0);
        }
        if (this.cbDavlSir.isSelected()) {
            hm.put("cbDavlSir", 1);
        } else {
            hm.put("cbDavlSir", 0);
        }
        hm.put("sDate", sDate);
        hm.put("eDate", eDate);
        NsiDB ndb = new NsiDB();
        if (ndb.checkNDS(hm)) {
            JOptionPane.showMessageDialog(this, "Все НДС проставленны верно.");
        }
    }


}
