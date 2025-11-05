/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dept.sklad;

import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.Settings;
import by.march8.entities.warehouse.InvoiceType;
import common.ProgressBar;
import dept.sklad.model.TemplateDocListForm;
import workOO.OO_new;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

/**
 * @author andy
 */
@SuppressWarnings("all")
public class Nakladnie extends TemplateDocListForm {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String modeName = "накладные-склад";
    JButton bPrilojenie;
    JButton bPutList;
    JPopupMenu popMenu;
    JMenuItem openDoc;
    JMenuItem factOst;
    private JMenuItem printP;
    // private JFrame parent;
    private ProgressBar pb;
    private boolean activate;
    private JMenuItem descr;
    private JMenuItem previewMenu;
    private MainController controller;

    public Nakladnie(MainController mainController, boolean b) {
        super(mainController, FRAME_FILTER);
        controller = mainController;
        setTitle("Накладные на отгрузку");
        initComponents_();
        activate = true;

/*		MyReportsModule.desktop.add(this);
        MyReportsModule.desktop.getDesktopManager().activateFrame(this);*/
/*
        parent.setSize(25 + 280 + columns.size() * 70 + 50, 650);
		Dimension size = parent.getSize();
		parent.setLocation(
				(Toolkit.getDefaultToolkit().getScreenSize().width - size.width) / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height - size.height) / 2);
		parent.setTitle(getTitle()+" : "+ MyReportsModule.titleText);
		setVisible(true);*/
        periodPicker.setDatePickerBegin(DateUtils.getFirstDay(periodPicker.getDatePickerEnd()));

        controller.getPersonalization().getPersonalSettings(modeName, periodPicker);
        periodPicker.setDatePickerEnd(new Date());
        getData();
        periodPicker.addOnChangeAction(a -> controller.getPersonalization().setPersonalSettings(modeName, periodPicker));
    }

    private void initComponents_() {
        bPrilojenie = new JButton("Приложение");
        bPrilojenie.setPreferredSize(Settings.BUTTON_NORMAL_SIZE);
        bPrilojenie.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    try {
                        SkladOO oo = new SkladOO();
                        OO_new.connect();
                        SkladDB db = new SkladDB();
                        String savePath = db.getTTNReport(Integer
                                .parseInt(table.getValueAt(
                                        table.getSelectedRow(), 1).toString()));
                        db.disConn();
                        OO_new.openDocument(savePath);
                    } catch (Exception ex) {
                        System.err.println(e);
                        JOptionPane.showMessageDialog(null, ex.getMessage(),
                                "Ошибка getTTNReport ",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        bPutList = new JButton("Путевой лист");
        bPutList.setPreferredSize(Settings.BUTTON_NORMAL_SIZE);
        bPutList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    try {
                        SkladOO oo = new SkladOO();
                        OO_new.connect();
                        SkladDB db = new SkladDB();
                        String savePath = db.getPutListReport(Integer
                                .parseInt(table.getValueAt(
                                        table.getSelectedRow(), 1).toString()));
                        db.disConn();
                        OO_new.openDocument(savePath);
                    } catch (Exception ex) {
                        System.err.println(e);
                        JOptionPane.showMessageDialog(null, ex.getMessage(),
                                "Ошибка getPutListReport",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Создаём высплывающее меню
        popMenu = new JPopupMenu();

        JMenuItem print = new JMenuItem("Формирование документов");
        print.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new PrintTTN(controller.getMainForm(), true, (String) table.getValueAt(
                            table.getSelectedRow(), 1));

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });
        popMenu.add(print);
        JMenuItem detail = new JMenuItem("Спецификация");
        detail.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (table.getSelectedRow() != -1) {
                        pb = new ProgressBar(controller.getMainForm(), false,
                                "Получение спецификации накладной...");
                        class SWorker extends SwingWorker<Object, Object> {
                            public SWorker() {
                            }

                            @Override
                            protected Object doInBackground() throws Exception {
                                try {
                                    controller.openInternalFrame(
                                            new SpecificationDoc(controller, lFoot.getText(),
                                                    (String) table.getValueAt(
                                                            table.getSelectedRow(), 1),
                                                    (String) table.getValueAt(
                                                            table.getSelectedRow(), 4)));

                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(
                                            null,
                                            ex.getMessage(),
                                            "Ошибка создания формы спецификации накладной.",
                                            javax.swing.JOptionPane.ERROR_MESSAGE);
                                } finally {

                                }
                                return 0;
                            }

                            @Override
                            protected void done() {
                                pb.dispose();
                            }
                        }
                        SWorker sw = new SWorker();
                        sw.execute();
                        pb.setVisible(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });

        popMenu.add(detail);

        printP = new JMenuItem("Печать приложения");
        printP.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (table.getSelectedRow() != -1) {
                        SkladOO soo = new SkladOO();
                        OO_new.connect();
                        SkladDB db = new SkladDB();
                        String savePath = db.getTTNReport(Integer
                                .parseInt(table.getValueAt(
                                        table.getSelectedRow(), 1).toString()));
                        db.disConn();
                        // soo.printDocument(savePath);
                        OO_new.openDocument(savePath);
                        // String[] cmds = { "Print" };
                        // soo.executeCommands(cmds);
                        OO_new.closeDocument(savePath, false);
                    }
                } catch (Exception ex) {

                    ex.printStackTrace();
                }
            }

        });
        // popMenu.add(printP);

        descr = new JMenuItem("Возвраты в валюте");
        descr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    if (table.getSelectedRow() != -1) {
                        pb = new ProgressBar(controller.getMainForm(), false,
                                "Получение деталей накладной...");
                        class SWorker extends SwingWorker<Object, Object> {
                            public SWorker() {
                            }

                            @Override
                            protected Object doInBackground() throws Exception {
                                try {

                                    controller.openInternalFrame(new VozvratValuta(controller, lFoot.getText(),
                                            (String) table.getValueAt(
                                                    table.getSelectedRow(), 1),
                                            (String) table.getValueAt(
                                                    table.getSelectedRow(), 4)));

                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(
                                            null,
                                            ex.getMessage(),
                                            "Ошибка создания формы возвратных накладных в валюте.",
                                            javax.swing.JOptionPane.ERROR_MESSAGE);
                                } finally {

                                }
                                return 0;
                            }

                            @Override
                            protected void done() {
                                pb.dispose();
                            }
                        }


                        SWorker sw = new SWorker();
                        sw.execute();
                        pb.setVisible(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });
        popMenu.add(descr);

/*		if ((MainForm.UserName.equals("Admin")
				|| MainForm.UserName.equals("admin")
				|| MainForm.UserName.equals("dtrifon") || MainForm.UserName
					.equals("tamara"))) {
			openDoc = new JMenuItem("Открыть накладную");
			openDoc.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (((table.getValueAt(table.getSelectedRow(), 8))
							.toString()).equals("Закрыт")) {
						SkladDB sdb = new SkladDB();
						if (sdb.OpenCloseTTN(table.getValueAt(
								table.getSelectedRow(), 1).toString())) {
							bShow.doClick();
							JOptionPane.showMessageDialog(null,
									"Накладная открыта");
						}
						sdb.disConn();
					} else {
						JOptionPane.showMessageDialog(null,
								"Только для закрытых накладных");
					}
				}
			});
			popMenu.add(openDoc);
		}*/

        openDoc = new JMenuItem("Открыть накладную");
        openDoc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (((table.getValueAt(table.getSelectedRow(), 8))
                        .toString()).equals("Закрыт")) {
                    SkladDB sdb = new SkladDB();
                    if (sdb.OpenCloseTTN(table.getValueAt(
                            table.getSelectedRow(), 1).toString())) {
                        bShow.doClick();
                        JOptionPane.showMessageDialog(null,
                                "Накладная открыта");
                    }
                    sdb.disConn();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Только для закрытых накладных");
                }
            }
        });

        popMenu.add(openDoc);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    Point point = e.getPoint();
                    int column = table.columnAtPoint(point);
                    int row = table.rowAtPoint(point);

                    // выполняем проверку
                    if (column != -1 && row != -1) {
                        table.setColumnSelectionInterval(column, column);
                        table.setRowSelectionInterval(row, row);
                    }
                    popMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    Point point = e.getPoint();
                    int column = table.columnAtPoint(point);
                    int row = table.rowAtPoint(point);

                    // выполняем проверку
                    if (column != -1 && row != -1) {
                        table.setColumnSelectionInterval(column, column);
                        table.setRowSelectionInterval(row, row);
                    }
                    popMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    Point point = e.getPoint();
                    int column = table.columnAtPoint(point);
                    int row = table.rowAtPoint(point);

                    // выполняем проверку
                    if (column != -1 && row != -1) {
                        table.setColumnSelectionInterval(column, column);
                        table.setRowSelectionInterval(row, row);
                    }
                    popMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        popMenu.addPopupMenuListener(new PopupMenuListener() {

                                         @Override
                                         public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                                             String type = (String) table.getValueAt(table.getSelectedRow(), 2);

                                             if (type.trim().equals(InvoiceType.DOCUMENT_SALE_MATERIAL) || type.trim().equals(InvoiceType.DOCUMENT_REFUND_MATERIAL)) {
                                                 print.setVisible(false);
                                             } else {
                                                 print.setVisible(true);
                                             }

                                             if ((table.getValueAt(table.getSelectedRow(), 2)
                                                     .equals("Возврат от покупателя"))
                                                     && (table.getValueAt(table.getSelectedRow(), 8)
                                                     .equals("Формируется"))) {
                                                 descr.setVisible(true);
                                             } else {
                                                 descr.setVisible(false);
                                             }

                                         }

                                         @Override
                                         public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                                         }

                                         @Override
                                         public void popupMenuCanceled(PopupMenuEvent e) {
                                         }
                                     }

        );

        panelButton.add(bClose);
    }

}
