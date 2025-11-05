/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.component;

import common.ProgressBar;
import dept.sklad.ostatki.RemainsDataBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author user
 */
public class ShowFilters extends JDialog {

    private MyButton bAdd;
    private MyButton bCancel;
    private MyButton bEditView;
    private Table tb;
    private Table parentData;
    private String view = " _filters_show ";
    private RemainsDataBase rdb;
    private GridBagLayout gbl;
    private GridBagConstraints gbc;
    private JPanel jpButton;
    private ProgressBar pb;
    private JTable thisTable;
    private Table dat;

    public ShowFilters(Table data, Object parent, JFrame paren) {
        super(paren);
        parentData = data;
        tb = new Table(view + " where Имя_класса_фильтра like '" + parent.getClass().getName() + "'"
                + " GROUP BY Номер_фильтра, [Активен/Нет],Название_фильтра,Имя_класса_фильтра ");
        tb.getMyTable().getColumnModel().getColumn(0).setMaxWidth(0);
        tb.getMyTable().getColumnModel().getColumn(0).setMinWidth(0);
        tb.getMyTable().getColumnModel().getColumn(0).setPreferredWidth(0);
        tb.getMyTable().getColumnModel().getColumn(3).setMaxWidth(0);
        tb.getMyTable().getColumnModel().getColumn(3).setMinWidth(0);
        tb.getMyTable().getColumnModel().getColumn(3).setPreferredWidth(0);

        tb.setViewportView(tb.getMyTable());

        jpButton = new JPanel(new FlowLayout());

        bCancel = new MyButton("Закрыть");
        bCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        bEditView = new MyButton("Смотреть");
        bEditView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = tb.getMyTable().getSelectedRow();
                new FilterFormFromGrid(this, i, parentData);
            }
        });

        bAdd = new MyButton("Добавить");
        bAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();

        setLayout(gbl);
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        tb.setPreferredSize(new Dimension(320, 240));
        add(tb, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        jpButton.add(bEditView);
        jpButton.add(bCancel);
        add(jpButton, gbc);

        setSize(new Dimension(340, 340));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(paren);
        setVisible(true);
    }
}
