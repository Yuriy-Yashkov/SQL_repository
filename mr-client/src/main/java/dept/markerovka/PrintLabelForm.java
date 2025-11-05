package dept.markerovka;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Vova
 * @date 19.03.2012
 */
public class PrintLabelForm extends JDialog {

    Label l = null;
    LabelPath lp = null;
    Map label = new HashMap();
    Map labelLng = new HashMap();
    Map<String, String> dictionary = new HashMap();
    JDialog self = null;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bFound;
    private javax.swing.JButton bPrint;
    private javax.swing.JComboBox cbLabelPath;
    private javax.swing.JComboBox cbLng;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField tfBarcode;
    private javax.swing.JTextField tfBrend;
    private javax.swing.JTextField tfColor;
    private javax.swing.JTextField tfEan;
    private javax.swing.JTextField tfFas;
    private javax.swing.JTextField tfGost;
    private javax.swing.JTextField tfGost1;
    private javax.swing.JTextField tfItemName;
    private javax.swing.JTextField tfKol;
    private javax.swing.JTextField tfKomplekt;
    private javax.swing.JTextField tfNamePrnt;
    private javax.swing.JTextField tfNar;
    private javax.swing.JTextField tfObhGrudPrnt;
    private javax.swing.JTextField tfRst;
    private javax.swing.JTextField tfRstPrnt;
    private javax.swing.JTextField tfRzm;
    private javax.swing.JTextField tfRzmPrnt;
    private javax.swing.JTextField tfSostav1;
    private javax.swing.JTextField tfSostav2;
    private javax.swing.JTextField tfSostav3;
    private javax.swing.JTextField tfSostav4;
    private javax.swing.JTextField tfSrt;
    private javax.swing.JTextField tfSum;
    private javax.swing.JTextField tfTextil;
    public PrintLabelForm(JFrame parent, boolean f) {
        super(parent, f);
        self = this;
        initComponents();

        MarkerovkaDB db = new MarkerovkaDB();
        List<Language> lngs = db.getLaguages();
        List<LabelPath> lps = db.getLabelsPath();
        cbLng.setEnabled(false);
        cbLng.removeAllItems();
        for (Language lng : lngs) {
            cbLng.addItem(lng);
        }
        cbLng.setSelectedIndex(0);

        cbLabelPath.removeAllItems();
        for (LabelPath lp : lps) {
            cbLabelPath.addItem(lp);
        }
        cbLabelPath.addItem("Добавить этикетку...");
        cbLabelPath.setSelectedIndex(0);
        setLocationRelativeTo(parent);
        setResizable(false);
        setTitle("Печать этикеток");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tfBarcode = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cbLng = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        tfItemName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tfNar = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        tfFas = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        tfRzm = new javax.swing.JTextField();
        tfRst = new javax.swing.JTextField();
        tfSrt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tfNamePrnt = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        tfBrend = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        tfKomplekt = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        tfRzmPrnt = new javax.swing.JTextField();
        tfRstPrnt = new javax.swing.JTextField();
        tfObhGrudPrnt = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        tfColor = new javax.swing.JTextField();
        tfEan = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        tfGost = new javax.swing.JTextField();
        tfGost1 = new javax.swing.JTextField();
        tfSostav1 = new javax.swing.JTextField();
        tfSostav3 = new javax.swing.JTextField();
        tfSostav2 = new javax.swing.JTextField();
        tfSostav4 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        tfTextil = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        tfSum = new javax.swing.JTextField();
        jTextField25 = new javax.swing.JTextField();
        tfKol = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        cbLabelPath = new javax.swing.JComboBox();
        bPrint = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        bFound = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Баркод");

        jLabel2.setText("Язык");

        cbLng.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        cbLng.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbLngActionPerformed(evt);
            }
        });

        jLabel3.setText("Изделие");

        tfItemName.setEditable(false);

        jLabel4.setText("Артикул");

        jLabel5.setText("Модель");

        tfNar.setEditable(false);

        jTextField4.setEditable(false);

        tfFas.setEditable(false);

        jLabel6.setText("Размер");

        jLabel7.setText("Рост");

        jLabel8.setText("Сорт");

        tfRzm.setEditable(false);

        tfRst.setEditable(false);

        tfSrt.setEditable(false);

        jLabel9.setText("Наименование для печати");

        tfNamePrnt.setEditable(false);

        jLabel10.setText("Брэнд, колл");

        tfBrend.setEditable(false);

        jLabel11.setText("Состав компл");

        tfKomplekt.setEditable(false);

        jLabel12.setText("Размер для печати");

        jLabel13.setText("Рост");

        jLabel14.setText("Обхват груди");

        tfRzmPrnt.setEditable(false);

        tfRstPrnt.setEditable(false);

        tfObhGrudPrnt.setEditable(false);

        jLabel15.setText("Цвет");

        jLabel16.setText("Код ЕАН");

        tfColor.setEditable(false);

        tfEan.setEditable(false);

        jLabel17.setText("ГОСТ");

        jLabel18.setText("ГОСТ1");

        jLabel19.setText("Состав1");

        jLabel20.setText("Состав2");

        tfGost.setEditable(false);

        tfGost1.setEditable(false);

        tfSostav1.setEditable(false);

        tfSostav3.setEditable(false);

        tfSostav2.setEditable(false);

        tfSostav4.setEditable(false);

        jLabel21.setText("Набор символов по уходу");

        tfTextil.setEditable(false);

        jLabel22.setText("К печати");

        jLabel23.setText("на изд.");

        jLabel24.setText("упакованных по");

        jLabel25.setText("ед в упак");

        tfSum.setText("0");

        jTextField25.setText("0");

        tfKol.setText("0");

        jLabel26.setText("Шаблон этикетки");

        cbLabelPath.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        cbLabelPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbLabelPathActionPerformed(evt);
            }
        });

        bPrint.setText("Печать");
        bPrint.setEnabled(false);
        bPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPrintActionPerformed(evt);
            }
        });

        jButton2.setText("Отмена");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        bFound.setText("Найти");
        bFound.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bFoundActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel10)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tfBrend, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel11)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tfKomplekt, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel12)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tfRzmPrnt, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel26)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cbLabelPath, 0, 306, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(bPrint)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 306, Short.MAX_VALUE)
                                                .addComponent(jButton2))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(tfBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(bFound)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cbLng, 0, 173, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tfItemName, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel6)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(tfRzm, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel7)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(tfRst, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel4)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(tfNar, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(0, 0, 0)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel5)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(tfFas, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(12, 12, 12)
                                                                .addComponent(jLabel8)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(tfSrt, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                        .addComponent(jLabel18)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(tfGost1))
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                        .addComponent(jLabel17)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(tfGost))
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                        .addComponent(jLabel15)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(tfColor, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                        .addComponent(jLabel13)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(tfRstPrnt, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addGap(12, 12, 12)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addComponent(jLabel14)
                                                                .addComponent(jLabel16))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                .addComponent(tfObhGrudPrnt)
                                                                .addComponent(tfEan, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)))
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                        .addComponent(jLabel21)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(tfTextil))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel22)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(tfSum, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jLabel23)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jLabel24)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(tfKol, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel25))
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                        .addComponent(jLabel19)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(tfSostav1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                        .addComponent(jLabel20)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(tfSostav3)))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addComponent(tfSostav4, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                                                                .addComponent(tfSostav2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLabel9)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tfNamePrnt, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(tfBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2)
                                        .addComponent(cbLng, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(bFound))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(tfItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel5)
                                        .addComponent(tfNar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tfFas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel7)
                                        .addComponent(tfRzm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tfRst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel8)
                                        .addComponent(tfSrt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel9)
                                        .addComponent(tfNamePrnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel10)
                                        .addComponent(tfBrend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel11)
                                        .addComponent(tfKomplekt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel12)
                                        .addComponent(tfRzmPrnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel13)
                                        .addComponent(jLabel14)
                                        .addComponent(tfRstPrnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tfObhGrudPrnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel15)
                                        .addComponent(tfColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tfEan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel16))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel17)
                                        .addComponent(tfGost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel18)
                                        .addComponent(tfGost1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel19)
                                        .addComponent(tfSostav1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tfSostav2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel20)
                                        .addComponent(tfSostav3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tfSostav4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel21)
                                        .addComponent(tfTextil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel22)
                                        .addComponent(jLabel23)
                                        .addComponent(jLabel24)
                                        .addComponent(jLabel25)
                                        .addComponent(tfSum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tfKol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel26)
                                        .addComponent(cbLabelPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(bPrint)
                                        .addComponent(jButton2))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bFoundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bFoundActionPerformed
        MarkerovkaDB db = new MarkerovkaDB();
        try {
            label = db.getAboutLabel(Long.parseLong(tfBarcode.getText()));
            label.put("barcode", Long.parseLong(tfBarcode.getText().trim()));
            l = new Label(Long.parseLong(tfBarcode.getText().trim()),
                    ((Language) cbLng.getSelectedItem()).getId(),
                    db.getItemId(Integer.parseInt(label.get("kod_izd").toString()), Integer.parseInt(label.get("srt").toString()), Integer.parseInt(label.get("rzm_marh").toString()), Integer.parseInt(label.get("rst_marh").toString())));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        tfItemName.setText(label.get("naim").toString());
        tfFas.setText(label.get("fas").toString());
        tfGost.setText(label.get("gost").toString());
        tfGost1.setText(label.get("gost1").toString());
        tfSostav1.setText(label.get("sostav1").toString());
        tfSostav2.setText(label.get("sostav2").toString());
        tfSostav3.setText(label.get("sostav3").toString());
        tfSostav4.setText(label.get("sostav4").toString());
        tfNar.setText(label.get("nar").toString());
        tfRzmPrnt.setText(label.get("rzm").toString());
        tfEan.setText(label.get("ean").toString());
        tfSrt.setText(label.get("srt").toString());
        tfTextil.setText(label.get("textil").toString());
        tfColor.setText(label.get("ncw").toString());
        tfRst.setText(label.get("rst_marh").toString());
        tfRzm.setText(label.get("rzm_marh").toString());
        tfBrend.setText(label.get("Brend").toString());
        tfKomplekt.setText(label.get("komplekt").toString());
        tfRstPrnt.setText(label.get("rst_marh").toString());
        tfObhGrudPrnt.setText(label.get("rzm_marh").toString());
        tfNamePrnt.setText(label.get("naim").toString());

        labelLng = new HashMap(label);
        bPrint.setEnabled(true);
        cbLng.setEnabled(true);
        cbLng.setSelectedIndex(0);
    }//GEN-LAST:event_bFoundActionPerformed

    private void cbLngActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbLngActionPerformed
        if (l != null) {
            MarkerovkaDB db = new MarkerovkaDB();
            try {
                Language lang = (Language) cbLng.getSelectedItem();
                l = new Label(Long.parseLong(tfBarcode.getText().trim()),
                        (lang.getId()),
                        db.getItemId(Integer.parseInt(label.get("kod_izd").toString()), Integer.parseInt(label.get("srt").toString()), Integer.parseInt(label.get("rzm_marh").toString()), Integer.parseInt(label.get("rst_marh").toString())));
                if (!(lang).getCode().equals("ru"))
                    labelLng = db.getAboutLabelLng(l);
                tfNamePrnt.setText(labelLng.get("naim").toString());
                //label.put("naim", labelLng.get("naim").toString());
                Map<String, String> sost = db.getTransSostav(lang);
                String sos = tfSostav1.getText().toLowerCase();

                for (Map.Entry s : sost.entrySet()) {
                    sos = sos.replaceAll(s.getKey().toString(), s.getValue().toString());
                }
                tfSostav1.setText(sos);
                labelLng.put("sostav1", sos);

                sos = tfSostav2.getText().toLowerCase();
                for (Map.Entry s : sost.entrySet()) {
                    sos = sos.replaceAll(s.getKey().toString(), s.getValue().toString());
                }
                tfSostav2.setText(sos);
                labelLng.put("sostav2", sos);

                sos = tfSostav3.getText().toLowerCase();
                for (Map.Entry s : sost.entrySet()) {
                    sos = sos.replaceAll(s.getKey().toString(), s.getValue().toString());
                }
                tfSostav3.setText(sos);
                labelLng.put("sostav3", sos);

                dictionary = db.getTransDictionary(lang);
                labelLng.put("LngRzm1", translate(label.get("LngRzm1").toString()));
                labelLng.put("LngRzm2", translate(label.get("LngRzm2").toString()));
                labelLng.put("LngRzm3", translate(label.get("LngRzm3").toString()));
                labelLng.put("LngBrend", translate(label.get("Brend").toString()).toUpperCase());
                tfBrend.setText(translate(label.get("Brend").toString()));

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_cbLngActionPerformed

    private String translate(String words) {
        for (Map.Entry s : dictionary.entrySet()) {
            words = words.replaceAll(s.getKey().toString(), s.getValue().toString());
        }
        return words;
    }

    private void bPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrintActionPerformed
        if (cbLng.getSelectedItem().toString().equals(""))
            JOptionPane.showMessageDialog(null, "Выберите язык этикетки", " Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        else {
            label.put("label_sum", Integer.parseInt(tfSum.getText().trim()));
            label.put("label_kol", Integer.parseInt(tfKol.getText().trim()));
            LabelPrint lprint = new LabelPrint(label, labelLng, lp);
            lprint.print();
        }
    }//GEN-LAST:event_bPrintActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void cbLabelPathActionPerformed(java.awt.event.ActionEvent evt) {
        if (cbLabelPath.getItemCount() > 0) {
            if (cbLabelPath.getSelectedItem().toString().equals(""))
                JOptionPane.showMessageDialog(null, "Выберите этикетку", " Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            else if (cbLabelPath.getSelectedItem().toString().equals("Добавить этикетку...")) {
                new AddLabelForm(self, true);
                MarkerovkaDB db = new MarkerovkaDB();
                List<LabelPath> lps = db.getLabelsPath();
                cbLabelPath.removeAllItems();
                for (LabelPath lp : lps) {
                    cbLabelPath.addItem(lp);
                }
                cbLabelPath.addItem("Добавить этикетку...");
                cbLabelPath.setSelectedIndex(0);
            } else {
                lp = (LabelPath) cbLabelPath.getSelectedItem();
                if (l != null) l.setIdLabelPath(lp.getId());

                //cbLabelPath.setSelectedIndex(0);
            }
        }

    }
    // End of variables declaration//GEN-END:variables
}
