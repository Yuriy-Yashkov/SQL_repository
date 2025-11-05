/*
 * @author vova
 * Created on 21.04.2012, 11:47:56
 */
package dept.markerovka;

import common.ProgressBar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NakladnieDescrForm extends javax.swing.JDialog {

    DefaultTableModel tModel;
    Object[] columns = new Object[8];
    Object[][] rows = null;
    int doc_id;
    ProgressBar pb;
    JDialog self;

    Label l = null;
    LabelPath lp = null;
    HashMap label = new HashMap();
    HashMap labelLng = new HashMap();
    Map<String, String> dictionary = new HashMap();
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bPrint;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton cbAther;
    private javax.swing.JRadioButton cbCh;
    private javax.swing.JComboBox cbLabelPath;
    private javax.swing.JComboBox cbLng;
    private javax.swing.JRadioButton cbSh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    public NakladnieDescrForm(JDialog parent, boolean modal, int doc_id) {
        super(parent, modal);
        self = this;
        initComponents();

        this.doc_id = doc_id;
        columns[0] = "Модель";
        columns[1] = "Артикул";
        columns[2] = "Наименование";
        columns[3] = "Размер";
        columns[4] = "Сорт";
        columns[5] = "Цвет";
        columns[6] = "Кол-во";
        columns[7] = "Баркод";

        initComponent();
        //setSize(25 + 280 + 6*70, 510);
        setLocationRelativeTo(parent);
        setResizable(false);
        setTitle("Детали накладной");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initComponent() {

        MarkerovkaDB db = new MarkerovkaDB();
        List<Language> lngs = db.getLaguages();
        List<LabelPath> lps = db.getLabelsPath();
        rows = db.getNaklDescr(doc_id);
        cbAther.setSelected(true);

        cbLng.setEnabled(true);
        cbLng.removeAllItems();

        cbLng.addItem("");
        for (Language lng : lngs) {
            cbLng.addItem(lng);
        }
        cbLng.setSelectedIndex(0);

        cbLabelPath.removeAllItems();
        cbLabelPath.addItem("");
        for (LabelPath lp : lps) {
            cbLabelPath.addItem(lp);
        }
        cbLabelPath.addItem("Добавить этикетку...");
        cbLabelPath.setSelectedIndex(0);

        tModel = new DefaultTableModel(rows, columns);

        //table = new JTable(tModel);
        table.setModel(tModel);
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);

        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(25);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        table.getColumnModel().getColumn(6).setPreferredWidth(40);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        cbAther = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        cbLabelPath = new javax.swing.JComboBox();
        bPrint = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cbLng = new javax.swing.JComboBox();
        cbSh = new javax.swing.JRadioButton();
        cbCh = new javax.swing.JRadioButton();

        buttonGroup1.add(cbAther);
        cbAther.setText("jRadioButton3");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("jLabel1");

        table.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String[]{
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));
        jScrollPane1.setViewportView(table);

        cbLabelPath.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        cbLabelPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbLabelPathActionPerformed(evt);
            }
        });

        bPrint.setText("Печать этикеток");
        bPrint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                bPrintMousePressed(evt);
            }
        });
        bPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPrintActionPerformed(evt);
            }
        });
        bPrint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bPrintKeyPressed(evt);
            }
        });

        jLabel2.setText("Этикетка");

        jLabel3.setText("Язык");

        cbLng.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));

        buttonGroup1.add(cbSh);
        cbSh.setText("Швейные этикетки");

        buttonGroup1.add(cbCh);
        cbCh.setText("Чулочные этикетки");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(44, 44, 44)
                                                .addComponent(jLabel1))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(12, 12, 12)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(cbSh)
                                                                        .addComponent(cbCh))
                                                                .addGap(187, 187, 187)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(jLabel2)
                                                                        .addComponent(jLabel3))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(cbLabelPath, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(cbLng, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(215, 215, 215)
                                                                                .addComponent(bPrint)))
                                                                .addGap(25, 25, 25)))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(cbLabelPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cbSh))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(cbLng, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(bPrint)
                                        .addComponent(cbCh)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrintActionPerformed

    }//GEN-LAST:event_bPrintActionPerformed

    private void bPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bPrintKeyPressed
    }//GEN-LAST:event_bPrintKeyPressed

    private void bPrintMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bPrintMousePressed
        if (cbAther.isSelected()) {
            JOptionPane.showMessageDialog(null, "Выберите тип этикетки", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } else if (cbLabelPath.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Выберите шаблон этикетки", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } else if (cbLng.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Выберите язык этикетки", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } else {

            pb = new ProgressBar(self, true, "Печать этикеток");
            class SWorker extends SwingWorker<Object, Object> {
                Object[][] rows;

                public SWorker(Object[][] rows) {
                    this.rows = rows;
                    pb.setMaxValue(rows.length);
                }

                @Override
                protected Object doInBackground() throws Exception {
                    MarkerovkaDB db = new MarkerovkaDB();
                    Language lang = (Language) cbLng.getSelectedItem();
                    dictionary = db.getTransDictionary(lang);
                    try {
                        for (int i = 0; i < rows.length; i++) {
                            Map label = new HashMap();
                            Map labelLng = new HashMap();

                            Long barcode = Long.parseLong(rows[i][7].toString());
                            String sar = rows[i][1].toString();
                            int sum = Integer.parseInt(rows[i][6].toString());
                            if (cbSh.isSelected() && !(sar.charAt(1) == '1' || sar.charAt(1) == '2')) {
                                System.out.println("не швейное изделие " + barcode);
                                pb.setProgress(i + 1);
                                continue;
                            }
                            if (cbCh.isSelected() && sar.charAt(1) != '3') {
                                System.out.println("не трикотажное изделие " + barcode);
                                pb.setProgress(i + 1);
                                continue;
                            }

                            label = db.getAboutLabel(barcode);
                            label.put("barcode", barcode);
                            l = new Label(barcode,
                                    ((Language) cbLng.getSelectedItem()).getId(),
                                    db.getItemId(Integer.parseInt(label.get("kod_izd").toString()), Integer.parseInt(label.get("srt").toString()), Integer.parseInt(label.get("rzm_marh").toString()), Integer.parseInt(label.get("rst_marh").toString())));

                            labelLng = new HashMap(label);
                            try {
                                labelLng = db.getAboutLabelLng(l);
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                                pb.setProgress(i + 1);
                                continue;
                            }


                            Map<String, String> sost = db.getTransSostav(lang);
                            String sos = label.get("sostav1").toString().toLowerCase();

                            for (Map.Entry s : sost.entrySet()) {
                                sos = sos.replaceAll(s.getKey().toString(), s.getValue().toString());
                            }
                            labelLng.put("sostav1", sos);

                            sos = label.get("sostav2").toString().toLowerCase();
                            for (Map.Entry s : sost.entrySet()) {
                                sos = sos.replaceAll(s.getKey().toString(), s.getValue().toString());
                            }
                            labelLng.put("sostav2", sos);

                            sos = label.get("sostav3").toString().toLowerCase();
                            for (Map.Entry s : sost.entrySet()) {
                                sos = sos.replaceAll(s.getKey().toString(), s.getValue().toString());
                            }
                            labelLng.put("sostav3", sos);

                            labelLng.put("LngRzm1", translate(label.get("LngRzm1").toString()));
                            labelLng.put("LngRzm2", translate(label.get("LngRzm2").toString()));
                            labelLng.put("LngRzm3", translate(label.get("LngRzm3").toString()));
                            labelLng.put("LngBrend", translate(label.get("Brend").toString()).toUpperCase());

                            label.put("label_sum", sum);
                            label.put("label_kol", 0);
                            //  System.out.println(barcode + " " + labelLng.get("naim") + " "+ sum );
                            LabelPrint lprint = new LabelPrint(label, labelLng, lp);
                            lprint.print();

                            pb.setProgress(i + 1);
                        }
                    } catch (Exception ex) {
                        System.err.println("Ошибка при печати этикеток " + ex);
                    }
                    return 0;
                }

                @Override
                protected void done() {
                    try {
                        pb.dispose();
                    } catch (Exception ex) {
                        System.err.println("Ошибка при печати этикеток " + ex);
                    }
                }
            }
            SWorker sw = new SWorker(rows);
            sw.execute();
            pb.setVisible(true);

        }
    }//GEN-LAST:event_bPrintMousePressed

    private void cbLabelPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbLabelPathActionPerformed
        if (cbLabelPath.getItemCount() > 0) {
            if (cbLabelPath.getSelectedItem().toString().equals("")) {
            } else if (cbLabelPath.getSelectedItem().toString().equals("Добавить этикетку...")) {
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
    }//GEN-LAST:event_cbLabelPathActionPerformed

    private String translate(String words) {
        for (Map.Entry s : dictionary.entrySet()) {
            words = words.replaceAll(s.getKey().toString(), s.getValue().toString());
        }
        return words;
    }
    // End of variables declaration//GEN-END:variables
}
