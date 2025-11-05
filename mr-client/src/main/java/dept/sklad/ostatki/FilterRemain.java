/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.sklad.ostatki;

import dept.component.MyButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 *
 * @author user
 */
@SuppressWarnings({"serial"})
public class FilterRemain extends JDialog {

    private String sKidSortSocks = " ((Шифр_арт like '433%' or Шифр_арт like '436%') and Сорт=1) ";
    private String sKidSortKnitwear = " ((Шифр_арт like '413%' or Шифр_арт like '416%' or Шифр_арт like '423%' or Шифр_арт like '426%') and (Сорт=1 or Сорт=2)) ";
    private String sAdultSortKnitwear = " ((Шифр_арт like '411%' or Шифр_арт like '412%' or Шифр_арт like '421%' or Шифр_арт like '422%' or Шифр_арт like '425%') and (Сорт=1 or Сорт=2)) ";
    private String sAdultSortSocks = " ((Шифр_арт like '431%' or Шифр_арт like '432%' or Шифр_арт like '435%') and Сорт=1) ";
    private String sKidNoSortSocks = " ((Шифр_арт like '433%' or Шифр_арт like '436%') and Сорт!=1) ";
    private String sKidNoSortKnitwear = " ((Шифр_арт like '413%' or Шифр_арт like '416%' or Шифр_арт like '423%' or Шифр_арт like '426%') and (Сорт!=1 or Сорт!=2)) ";
    private String sAdultNoSortSocks = " ((Шифр_арт like '431%' or Шифр_арт like '432%' or Шифр_арт like '435%') and Сорт!=1) ";
    private String sAdultNoSortKnitwear = " ((Шифр_арт like '411%' or Шифр_арт like '412%' or Шифр_арт like '421%' or Шифр_арт like '422%' or Шифр_арт like '425%') and (Сорт!=1 or Сорт!=2)) ";
    private String sManufacturingAndTechnical = " (Шифр_арт like '45%' or Шифр_арт like '48%') ";
    private String sCanvas = " (Шифр_арт like '47%' and Шифр_арт not like '470%') ";
    private String sCanvasServices = " (Шифр_арт like '470%') ";
    private ArrayList<String> condition = new ArrayList<String>();
    private JPanel jpNoSort;
    private JPanel jpSort;
    private JPanel jpManufacturingAndTechnical;
    private JPanel jpCanvasServices;
    private JPanel jpCanvas;
    private JPanel jpButton;
    private JCheckBox cbKidSortSocks;
    private JCheckBox cbKidSortKnitwear;
    private JCheckBox cbKidNoSortSocks;
    private JCheckBox cbKidNoSortKnitwear;
    private JCheckBox cbAdultSortSocks;
    private JCheckBox cbAdultSortKnitwear;
    private JCheckBox cbAdultNoSortSocks;
    private JCheckBox cbAdultNoSortKnitwear;
    private JCheckBox cbManufacturingAndTechnical;      //sar 45***,48****    
    private JCheckBox cbCanvas;
    private JCheckBox cbCanvasServices;
    private MyButton mbSet;
    private MyButton mbCancel;
    private GridBagLayout gbl;
    private GridBagConstraints gbc;
    private String query;
    private JFrame paren;
    private RevOfCompany df;

    /**
     *
     * @param parent
     * @param nameView
     */
    public FilterRemain(JFrame parent, String nameView) {
        super(parent, true);
        paren = parent;
        query = nameView;
        initialization();
    }

    public String getQuery() {
        return query;
    }

    private void initialization() {
        try {

            gbl = new GridBagLayout();
            gbc = new GridBagConstraints();
            setLayout(gbl);
            jpSort = new JPanel(new FlowLayout(FlowLayout.LEFT));
            jpSort.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            jpSort.setBorder(BorderFactory.createTitledBorder("Сортное"));
            cbKidSortSocks = new JCheckBox("Детское ч.-н.");
            cbKidSortKnitwear = new JCheckBox("Детское трик-ж");
            cbAdultSortSocks = new JCheckBox("Взрослое ч.-н.");
            cbAdultSortKnitwear = new JCheckBox("Взрослое трик-ж");
            jpSort.add(cbKidSortSocks);
            jpSort.add(cbKidSortKnitwear);
            jpSort.add(cbAdultSortSocks);
            jpSort.add(cbAdultSortKnitwear);

            jpNoSort = new JPanel(new FlowLayout(FlowLayout.LEFT));
            jpNoSort.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            jpNoSort.setBorder(BorderFactory.createTitledBorder("Не сортные"));
            cbKidNoSortSocks = new JCheckBox("Детское ч.-н.");
            cbKidNoSortKnitwear = new JCheckBox("Детское трик-ж");
            cbAdultNoSortSocks = new JCheckBox("Взрослое ч.-н.");
            cbAdultNoSortKnitwear = new JCheckBox("Взрослое трик-ж");
            jpNoSort.add(cbKidNoSortSocks);
            jpNoSort.add(cbKidNoSortKnitwear);
            jpNoSort.add(cbAdultNoSortSocks);
            jpNoSort.add(cbAdultNoSortKnitwear);

            jpManufacturingAndTechnical = new JPanel(new FlowLayout(FlowLayout.LEFT));
            jpManufacturingAndTechnical.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            jpManufacturingAndTechnical.setBorder(BorderFactory.createTitledBorder("Производственно-технические"));
            cbManufacturingAndTechnical = new JCheckBox("Производственно-технические");
            jpManufacturingAndTechnical.add(cbManufacturingAndTechnical);

            jpCanvas = new JPanel(new FlowLayout(FlowLayout.LEFT));
            jpCanvas.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            jpCanvas.setBorder(BorderFactory.createTitledBorder("Полотно"));
            cbCanvas = new JCheckBox("Полотно");
            jpCanvas.add(cbCanvas);

            jpCanvasServices = new JPanel(new FlowLayout(FlowLayout.LEFT));
            jpCanvasServices.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            jpCanvasServices.setBorder(BorderFactory.createTitledBorder("Полотно(услуги)"));
            cbCanvasServices = new JCheckBox("Полотно(услуги)");
            jpCanvasServices.add(cbCanvasServices);

            jpButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
            mbSet = new MyButton("Применить");
            mbSet.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createCondition();
                    CreateQuery cq = new CreateQuery(query, condition);
                    query = cq.creteQ();
                    System.setProperty("query", query);
                    dispose();
                }
            });
            mbCancel = new MyButton("Закрыть");
            jpButton.add(mbSet);
            jpButton.add(mbCancel);

            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridx = 0;
            gbc.gridy = 0;
            this.add(jpSort, gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            this.add(jpNoSort, gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            this.add(jpManufacturingAndTechnical, gbc);
            gbc.gridx = 0;
            gbc.gridy = 3;
            this.add(jpCanvas, gbc);
            gbc.gridx = 0;
            gbc.gridy = 4;
            this.add(jpCanvasServices, gbc);
            gbc.gridx = 0;
            gbc.gridy = 5;
            this.add(jpButton, gbc);

            this.setLayout(gbl);
            this.setResizable(false);
            this.setLocationRelativeTo(null);
            this.setTitle("Настройка фильтра");
            this.setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width / 2 + 50, Toolkit.getDefaultToolkit().getScreenSize().height / 3));
        } catch (Exception ex) {
            System.out.println(ex + this.getClass().getName().toString());
        }
    }

    public void showForm(boolean status) {
        this.setVisible(status);
    }

    private void createCondition() {
        try {
            condition = new ArrayList<String>();
            if (cbKidSortSocks.isSelected()) {
                condition.add(sKidSortSocks);
            }
            if (cbKidSortKnitwear.isSelected()) {
                condition.add(sKidSortKnitwear);
            }
            if (cbKidNoSortSocks.isSelected()) {
                condition.add(sKidNoSortSocks);
            }
            if (cbKidNoSortKnitwear.isSelected()) {
                condition.add(sKidNoSortKnitwear);
            }

            if (cbAdultSortSocks.isSelected()) {
                condition.add(sAdultSortSocks);
            }
            if (cbAdultSortKnitwear.isSelected()) {
                condition.add(sAdultSortKnitwear);
            }
            if (cbAdultNoSortSocks.isSelected()) {
                condition.add(sAdultNoSortSocks);
            }
            if (cbAdultNoSortKnitwear.isSelected()) {
                condition.add(sAdultNoSortKnitwear);
            }

            if (cbManufacturingAndTechnical.isSelected()) {
                condition.add(sManufacturingAndTechnical);
            }
            if (cbCanvas.isSelected()) {
                condition.add(sCanvas);
            }
            if (cbCanvasServices.isSelected()) {
                condition.add(sCanvasServices);
            }
        } catch (Exception ex) {
            System.out.println(ex + this.getClass().getName().toString());
        }
    }
}
