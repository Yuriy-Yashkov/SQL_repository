package dept.production.planning;

import by.march8.ecs.MainController;
import common.CheckBoxHeader;
import common.ProgressBar;
import common.User;
import common.UtilFunctions;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

/**
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class ProjectUpdateForm extends javax.swing.JDialog {
    PlanPDB ppdb;
    PlanDB pdb;
    User user = User.getInstance();
    private JButton buttClose;
    private JButton buttTab1Print;
    private JButton buttSave;
    private JButton buttTab2Print;
    private JButton buttTab1Cancel;
    private JButton buttTab1Update;
    private JButton buttTab1Article;
    private JButton buttTab2Work;
    private JButton buttTab2Service;
    private JButton buttTab2Select;
    private JPanel osnova;
    private JPanel buttPanel;
    private JPanel centerPanel;
    private JPanel buttEastTab1Panel;
    private JPanel buttEastTab2Panel;
    private JTable tableDetalTab1;
    private JTable tableDetalTab2;
    private DefaultTableModel tModelDetalTab1;
    private DefaultTableModel tModelDetalTab2;
    private TableFilterHeader filterHeaderDetalTab1;
    private TableFilterHeader filterHeaderDetalTab2;
    private JPanel tableDetalTab3Panel;
    private JPanel buttEastTab3Panel;
    private JTable tableDetalTab3;
    private TableFilterHeader filterHeaderDetalTab3;
    private JButton buttTab3Plus;
    private JButton buttTab3Minus;
    private JButton buttTab3Edit;
    private Vector colDetalTab3;
    private DefaultTableModel tModelDetalTab3;
    private DefaultTableCellRenderer rendererWork;
    private Vector colDetalTab1;
    private Vector colDetalTab2;
    private int minSelectedRowDetalTab1 = -1;
    private int maxSelectedRowDetalTab1 = -1;
    private boolean tableDetalTab1ModelListenerIsChanging = false;
    private int minSelectedRowDetalTab3 = -1;
    private int maxSelectedRowDetalTab3 = -1;
    private boolean tableDetalTab3ModelListenerIsChanging = false;
    private int idProj;
    private JPanel tableDetalPanel;
    private JPanel tableDetalTab1Panel;
    private JPanel tableDetalTab2Panel;
    private DefaultTableCellRenderer renderer;
    private ProgressBar pb;
    private JTabbedPane tableTabbedPane;
    private JButton buttTab3Print;
    private JButton buttTab3Search;
    private JButton buttTab3Select;
    private Vector dataVec;
    private DefaultTableCellRenderer rendererArticle;
    private Vector dataProj;
    private Vector dataWork;
    private JButton buttTab1UpdateCena;
    private DefaultTableModel tModelData;
    private MainController controller;

    ProjectUpdateForm(MainController mainController, boolean modal, int idProject) {
        super(mainController.getMainForm(), modal);
        setTitle("Обновление артикулов проекта плана производства");
        controller = mainController;

        try {
            this.idProj = idProject;

            init();
            initPropSetting();

            getData(idProject);

            buttTab1Update.doClick();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка формирования проекта плана производства! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        UtilPlan.YEARS = 9;
        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    private void init() {
        setMinimumSize(new Dimension(850, 550));
        setPreferredSize(new Dimension(1000, 700));

        colDetalTab1 = new Vector();
        colDetalTab2 = new Vector();
        colDetalTab3 = new Vector();

        colDetalTab1.add("");
        colDetalTab1.add("Вид");
        colDetalTab1.add("Экономист");
        colDetalTab1.add("Название");
        colDetalTab1.add("Модель");
        colDetalTab1.add("Арт. ДО");
        colDetalTab1.add("Арт. ПОСЛЕ");
        colDetalTab1.add("Рост");
        colDetalTab1.add("Размер");
        colDetalTab1.add("Состав");
        colDetalTab1.add("Декор");
        colDetalTab1.add("Кол-во(шт.)");
        colDetalTab1.add("x2");
        colDetalTab1.add("Цена BYR");
        colDetalTab1.add("Цена RUB");
        colDetalTab1.add("Шифр");
        colDetalTab1.add("Дополнение");
        colDetalTab1.add("Примечание");
        colDetalTab1.add("Новинка");
        colDetalTab1.add("Арт. В РАБОТЕ");

        colDetalTab2.add("Шифр");
        colDetalTab2.add("Название");
        colDetalTab2.add("Модель");
        colDetalTab2.add("Артикул");
        colDetalTab2.add("Рост");
        colDetalTab2.add("Размер");
        colDetalTab2.add("Полотно");
        colDetalTab2.add("цена план.");
        colDetalTab2.add("цена валют.");

        colDetalTab3.add("");
        colDetalTab3.add("ID");
        colDetalTab3.add("TYPE");
        colDetalTab3.add("Модель");
        colDetalTab3.add("Артикул");
        colDetalTab3.add("Арт. в НСИ");
        colDetalTab3.add("Полотно");
        colDetalTab3.add("Примечание");
        colDetalTab3.add("Дата корр.");
        colDetalTab3.add("Автор корр.");

        tableDetalTab1 = new JTable();
        tableDetalTab1.setAutoCreateColumnsFromModel(true);
        tableDetalTab1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableDetalTab1.getTableHeader().setReorderingAllowed(false);
        tableDetalTab1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() > 1) {
                    buttTab1Article.doClick();
                }
            }
        });
        tableDetalTab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRowDetalTab1 = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRowDetalTab1 = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });

        tableDetalTab1.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    buttTab1Article.doClick();
                }
            }
        });

        filterHeaderDetalTab1 = new TableFilterHeader(tableDetalTab1, AutoChoices.ENABLED);

        buttTab1Cancel = new JButton("отменить");
        buttTab1Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab1CancelActionPerformed(evt);
            }
        });

        buttTab1Update = new JButton("обновить");
        buttTab1Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab1UpdateActionPerformed(evt);
            }
        });

        buttTab1UpdateCena = new JButton("цены");
        buttTab1UpdateCena.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab1UpdateCenaActionPerformed(evt);
            }
        });

        buttTab1Article = new JButton("артикула");
        buttTab1Article.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab1ArticleActionPerformed(evt);
            }
        });

        buttTab1Print = new JButton("печать");
        buttTab1Print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab1PrintActionPerformed(evt);
            }
        });

        buttEastTab1Panel = new JPanel();
        buttEastTab1Panel.setLayout(new GridLayout(7, 0, 5, 5));
        buttEastTab1Panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        buttEastTab1Panel.add(buttTab1Article);
        buttEastTab1Panel.add(buttTab1Cancel);
        buttEastTab1Panel.add(new JLabel());
        buttEastTab1Panel.add(buttTab1Update);
        buttEastTab1Panel.add(buttTab1UpdateCena);
        buttEastTab1Panel.add(new JLabel());
        buttEastTab1Panel.add(buttTab1Print);

        tableDetalTab1Panel = new JPanel();
        tableDetalTab1Panel.setLayout(new BorderLayout(1, 1));
        tableDetalTab1Panel.add(new JScrollPane(tableDetalTab1), BorderLayout.CENTER);
        tableDetalTab1Panel.add(buttEastTab1Panel, BorderLayout.EAST);

        tableDetalTab2 = new JTable();
        tableDetalTab2.setAutoCreateColumnsFromModel(true);
        tableDetalTab2.getTableHeader().setReorderingAllowed(false);
        tableDetalTab2.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() > 1) {
                    buttTab2Select.doClick();
                }
            }
        });

        tableDetalTab2.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    buttTab2Select.doClick();
                }
            }
        });

        filterHeaderDetalTab2 = new TableFilterHeader(tableDetalTab2, AutoChoices.ENABLED);

        renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                try {
                    if (table.getValueAt(row, table.getColumnCount() - 2).toString().equals(UtilPlan.NEW))
                        cell.setForeground(Color.RED);
                    else
                        cell.setForeground(table.getForeground());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return cell;
            }

            ;
        };

        rendererArticle = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                try {
                    if (Boolean.valueOf(table.getValueAt(row, table.getColumnCount() - 1).toString().trim()))
                        cell.setBackground(Color.LIGHT_GRAY);
                    else
                        cell.setBackground(table.getBackground());

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Ошибка! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return cell;
            }

            ;
        };

        buttTab2Work = new JButton("в работу");
        buttTab2Work.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab2WorkActionPerformed(evt);
            }
        });

        buttTab2Service = new JButton("сервис");
        buttTab2Service.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab2ServiceActionPerformed(evt);
            }
        });

        buttTab2Select = new JButton("применить");
        buttTab2Select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab2EditActionPerformed(evt);
            }
        });

        buttTab2Print = new JButton("печать");
        buttTab2Print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab2PrintActionPerformed(evt);
            }
        });

        buttEastTab2Panel = new JPanel();
        buttEastTab2Panel.setLayout(new GridLayout(7, 0, 5, 5));
        buttEastTab2Panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        buttEastTab2Panel.add(buttTab2Select);
        buttEastTab2Panel.add(buttTab2Work);
        buttEastTab2Panel.add(buttTab2Service);
        buttEastTab2Panel.add(new JLabel());
        buttEastTab2Panel.add(new JLabel());
        buttEastTab2Panel.add(new JLabel());
        buttEastTab2Panel.add(buttTab2Print);

        tableDetalTab2Panel = new JPanel();
        tableDetalTab2Panel.setLayout(new BorderLayout(1, 1));
        tableDetalTab2Panel.add(new JScrollPane(tableDetalTab2), BorderLayout.CENTER);
        tableDetalTab2Panel.add(buttEastTab2Panel, BorderLayout.EAST);

        tableDetalTab3 = new JTable();
        tableDetalTab3.setAutoCreateColumnsFromModel(true);
        //tableDetalTab3.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableDetalTab3.getTableHeader().setReorderingAllowed(false);
        tableDetalTab3.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() > 1) {
                    buttTab3Select.doClick();
                }
            }
        });

        tableDetalTab3.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                minSelectedRowDetalTab3 = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
                maxSelectedRowDetalTab3 = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex() == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                        -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
            }
        });

        tableDetalTab3.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    buttTab3Select.doClick();
                }
            }
        });

        filterHeaderDetalTab3 = new TableFilterHeader(tableDetalTab3, AutoChoices.ENABLED);

        rendererWork = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                try {
                    if (table.getValueAt(row, 2).toString().equals("-1"))
                        cell.setBackground(Color.PINK);
                    else
                        cell.setBackground(table.getBackground());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return cell;
            }

            ;
        };

        buttTab3Plus = new JButton("+");
        buttTab3Plus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab3PlusActionPerformed(evt);
            }
        });

        buttTab3Minus = new JButton("-");
        buttTab3Minus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab3MinusActionPerformed(evt);
            }
        });

        buttTab3Edit = new JButton("изменить");
        buttTab3Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab3EditActionPerformed(evt);
            }
        });

        buttTab3Print = new JButton("печать");
        buttTab3Print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab3PrintActionPerformed(evt);
            }
        });

        buttTab3Search = new JButton("поиск");
        buttTab3Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab3SearchActionPerformed(evt);
            }
        });

        buttTab3Select = new JButton("применить");
        buttTab3Select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttTab3SelectActionPerformed(evt);
            }
        });

        buttEastTab3Panel = new JPanel();
        buttEastTab3Panel.setLayout(new GridLayout(7, 0, 5, 5));
        buttEastTab3Panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        buttEastTab3Panel.add(buttTab3Select);
        buttEastTab3Panel.add(buttTab3Search);
        buttEastTab3Panel.add(new JLabel());
        buttEastTab3Panel.add(buttTab3Plus);
        buttEastTab3Panel.add(buttTab3Minus);
        buttEastTab3Panel.add(buttTab3Edit);
        buttEastTab3Panel.add(buttTab3Print);

        tableDetalTab3Panel = new JPanel();
        tableDetalTab3Panel.setLayout(new BorderLayout(1, 1));
        tableDetalTab3Panel.add(new JScrollPane(tableDetalTab3), BorderLayout.CENTER);
        tableDetalTab3Panel.add(buttEastTab3Panel, BorderLayout.EAST);

        tableTabbedPane = new javax.swing.JTabbedPane();
        tableTabbedPane.setPreferredSize(new Dimension(260, 260));
        tableTabbedPane.addTab("Артикула из классификатора", tableDetalTab2Panel);
        tableTabbedPane.addTab("Артикула в работе", tableDetalTab3Panel);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(1, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.add(tableDetalTab1Panel, BorderLayout.CENTER);
        centerPanel.add(tableTabbedPane, BorderLayout.SOUTH);

        buttClose = new JButton("Закрыть");
        buttClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttCloseActionPerformed(evt);
            }
        });

        buttSave = new JButton("Сохранить");
        buttSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttSaveActionPerformed(evt);
            }
        });

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 4, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttPanel.add(new JLabel());
        buttPanel.add(buttClose);
        buttPanel.add(buttSave);

        osnova = new JPanel();
        osnova.setLayout(new BorderLayout(1, 1));
        osnova.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        osnova.add(centerPanel, BorderLayout.CENTER);
        osnova.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnova);
        pack();
    }

    private void buttCloseActionPerformed(ActionEvent evt) {
        try {
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab1PrintActionPerformed(ActionEvent evt) {
        try {
            PlanOO oo = new PlanOO("", tModelDetalTab1, tableDetalTab1.getColumnModel());
            oo.createReport("DefaultTableAlbumFormatCheck.ots");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }

    private void buttSaveActionPerformed(ActionEvent evt) {
        try {
            if (JOptionPane.showOptionDialog(null,
                    "Сохранить изменения?",
                    "Сохранение...",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Да", "Нет"}, "Нет") == JOptionPane.YES_OPTION) {

                pb = new ProgressBar(
                        ProjectUpdateForm.this,
                        false,
                        "Сохранение артикулов в проект плана ...");

                SwingWorker sw = new SwingWorker() {
                    protected Object doInBackground() {
                        Vector rez = new Vector();
                        try {
                            ppdb = new PlanPDB();

                            rez = ppdb.updateProjectTable(idProj,
                                    tModelDetalTab1.getDataVector());

                            if (!rez.isEmpty()) {
                                new SmallTableForm(controller, ProjectUpdateForm.this, true, rez, "");
                            }

                            pb.setMessage("Сохранение артикулов в работе...");

                            ppdb.updateProjectWorkTable(idProj,
                                    tModelDetalTab3.getDataVector(),
                                    Integer.valueOf(user.getIdEmployee()));

                            JOptionPane.showMessageDialog(
                                    null,
                                    "Сохранение завершено! ",
                                    "Завершено",
                                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null,
                                    "Ошибка. " + e.getMessage(),
                                    "Ошибка",
                                    javax.swing.JOptionPane.ERROR_MESSAGE);
                        } finally {
                            ppdb.disConn();
                        }
                        return null;
                    }

                    protected void done() {
                        pb.dispose();
                    }
                };
                sw.execute();
                pb.setVisible(true);

                getData(idProj);

                buttTab1Update.doClick();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab1CancelActionPerformed(ActionEvent evt) {
        try {
            if (tableDetalTab1.getSelectedRow() != -1) {
                tableDetalTab1
                        .setValueAt(false, tableDetalTab1.getSelectedRow(), 0);

                tableDetalTab1
                        .setValueAt("",
                                tableDetalTab1.getSelectedRow(),
                                6);
            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали в таблице моделей!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab1UpdateActionPerformed(ActionEvent evt) {
        try {
            tableTabbedPane.setSelectedIndex(1);

            Vector tmp = new Vector();

            for (int i = 0; i < tModelDetalTab3.getRowCount(); i++) {
                Vector vec = (Vector) tModelDetalTab3.getDataVector().get(i);

                if (!vec.get(2).toString().trim().equals("-1"))
                    if (!tmp.contains(vec.get(3)))
                        tmp.add(vec.get(3).toString().trim());
            }

            for (int i = 0; i < tModelDetalTab1.getRowCount(); i++) {
                Vector vec = (Vector) tModelDetalTab1.getDataVector().get(i);

                if ((Boolean) tmp.contains(vec.elementAt(4).toString().trim()))
                    ((Vector) tModelDetalTab1.getDataVector().get(i)).setElementAt(true, vec.size() - 1);

                else
                    ((Vector) tModelDetalTab1.getDataVector().get(i)).setElementAt(false, vec.size() - 1);
            }

            tModelDetalTab1.fireTableDataChanged();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab1UpdateCenaActionPerformed(ActionEvent evt) {
        try {
            tModelData = tModelDetalTab1;

            pb = new ProgressBar(
                    ProjectUpdateForm.this,
                    false,
                    "Поиск цен...");

            SwingWorker sw = new SwingWorker() {
                protected Object doInBackground() {
                    try {
                        pdb = new PlanDB();

                        for (int i = 0; i < tModelData.getRowCount(); i++) {
                            Vector vec = (Vector) tModelData.getDataVector().get(i);

                            ((Vector) tModelData.getDataVector().get(i))
                                    .setElementAt(
                                            pdb.getCena(
                                                    Integer.valueOf(vec.get(4).toString()),
                                                    vec.get(5).toString(),
                                                    vec.get(8).toString(),
                                                    1).toString().replace("[", "").replace("]", ""),
                                            13);

                            ((Vector) tModelData.getDataVector().get(i))
                                    .setElementAt(
                                            pdb.getCena(
                                                    Integer.valueOf(vec.get(4).toString()),
                                                    vec.get(5).toString(),
                                                    vec.get(8).toString(),
                                                    2).toString().replace("[", "").replace("]", ""),
                                            14);

                        }

                    } catch (Exception e) {
                        tModelData = tModelDetalTab1;
                        JOptionPane.showMessageDialog(null,
                                "Ошибка. " + e.getMessage(),
                                "Ошибка",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        pdb.disConn();
                    }
                    return null;
                }

                protected void done() {
                    pb.dispose();
                }
            };
            sw.execute();
            pb.setVisible(true);

            tModelDetalTab1 = tModelData;

            tModelDetalTab1.fireTableDataChanged();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab1ArticleActionPerformed(ActionEvent evt) {
        try {
            if (tableDetalTab1.getSelectedRow() != -1) {
                Vector row = new Vector();

                try {
                    pdb = new PlanDB();
                    /*System.out.println(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 4).toString());
                    System.out.println("Work article " + String.valueOf(UtilPlan.WORK_ARTICLE));
                    System.out.println("Work YEARS " + String.valueOf(UtilPlan.YEARS));*/
                    row = pdb.searchNar(Integer
                                    .valueOf(tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 4).toString()),
                            UtilPlan.WORK_ARTICLE,
                            UtilPlan.YEARS);

                    tableTabbedPane.setSelectedIndex(0);

                } catch (Exception e) {
                    row = new Vector();
                    JOptionPane.showMessageDialog(null,
                            "Ошибка! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    pdb.disConn();
                }

                createTab2Table(row);
            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали в таблице моделей!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab2WorkActionPerformed(ActionEvent evt) {
        try {
            if (tableDetalTab2.getSelectedRow() != -1) {

                Vector tmp = (Vector) tModelDetalTab2.getDataVector()
                        .get(tableDetalTab2.convertRowIndexToModel(tableDetalTab2.getSelectedRow()));


                new ProjectWorkArticleForm(
                        ProjectUpdateForm.this,
                        true,
                        tmp.get(2).toString(),
                        tmp.get(3).toString(),
                        tmp.get(6).toString()
                );

                if (UtilPlan.EDIT_PROJECT_ARTICL_BUTT_ACTION) {
                    if (!UtilPlan.EDIT_PROJECT_ARTICL_ITEM.isEmpty()) {
                        if (checkItem(UtilPlan.EDIT_PROJECT_ARTICL_ITEM)) {
                            tModelDetalTab3
                                    .insertRow(tModelDetalTab3.getRowCount(),
                                            UtilPlan.EDIT_PROJECT_ARTICL_ITEM);

                            tableTabbedPane.setSelectedIndex(1);
                        }
                    }
                }

            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали в таблице артикулов из классиф.!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab2ServiceActionPerformed(ActionEvent evt) {
        try {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout(1, 1));

            JSpinner rounding = new JSpinner();
            rounding.setValue(UtilPlan.WORK_ARTICLE);

            panel.add(rounding, BorderLayout.CENTER);
            panel.add(new JLabel(" C "), BorderLayout.EAST);

            if (JOptionPane.showOptionDialog(null,
                    panel,
                    "Показывать артикула с: ",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Сохранить", "Отмена"}, rounding) == JOptionPane.YES_OPTION) {

                UtilPlan.WORK_ARTICLE = Integer.valueOf(rounding.getValue().toString());
                UtilFunctions.setSettingPropFile(rounding.getValue().toString(), UtilPlan.SETTING_WORK_ARTICLE);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab2EditActionPerformed(ActionEvent evt) {
        try {
            if (tableDetalTab2.getSelectedRow() != -1) {
                if (tableDetalTab1.getSelectedRow() != -1) {

                    boolean flag = false;

                    if (tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 4).toString()
                            .equals(tableDetalTab2.getValueAt(tableDetalTab2.getSelectedRow(), 2).toString())) {
                        flag = true;

                    } else {
                        if (JOptionPane.showOptionDialog(null,
                                "Разные модели!",
                                "Внимание",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE,
                                null,
                                new Object[]{"Продолжить", "Отмена"}, "Продолжить") == JOptionPane.YES_OPTION)

                            flag = true;
                    }

                    if (flag) {

                        tableDetalTab1
                                .setValueAt(true, tableDetalTab1.getSelectedRow(), 0);

                        tableDetalTab1
                                .setValueAt(tableDetalTab2.getValueAt(tableDetalTab2.getSelectedRow(), 3).toString(),
                                        tableDetalTab1.getSelectedRow(),
                                        6);
                    }

                } else
                    JOptionPane.showMessageDialog(null,
                            "Вы ничего не выбрали в таблице моделей!",
                            "Внимание",
                            javax.swing.JOptionPane.WARNING_MESSAGE);
            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали в таблице артикулов из классиф.!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab2PrintActionPerformed(ActionEvent evt) {
        try {
            PlanOO oo = new PlanOO("Артикула из классификатора ", tModelDetalTab2, tableDetalTab2.getColumnModel());
            oo.createReport("DefaultTableAlbumFormatCheck.ots");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab3PlusActionPerformed(ActionEvent evt) {
        try {
            new ProjectWorkArticleForm(ProjectUpdateForm.this, true);

            if (UtilPlan.EDIT_PROJECT_ARTICL_BUTT_ACTION) {
                if (!UtilPlan.EDIT_PROJECT_ARTICL_ITEM.isEmpty()) {
                    if (checkItem(UtilPlan.EDIT_PROJECT_ARTICL_ITEM)) {
                        tModelDetalTab3
                                .insertRow(tModelDetalTab3.getRowCount(),
                                        UtilPlan.EDIT_PROJECT_ARTICL_ITEM);
                    }
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab3MinusActionPerformed(ActionEvent evt) {
        try {
            if (tableDetalTab3.getSelectedRow() != -1) {

                for (int i = 0; i < tModelDetalTab3.getRowCount(); i++) {
                    Vector vec = (Vector) tModelDetalTab3.getDataVector().get(i);

                    if (Boolean.valueOf(vec.elementAt(0).toString())) {
                        ((Vector) tModelDetalTab3.getDataVector().get(i)).setElementAt(false, 0);

                        if (!vec.get(2).toString().equals("-1")) {
                            ((Vector) tModelDetalTab3.getDataVector().get(i)).setElementAt(-1, 2);

                        } else {
                            if (vec.get(1).toString().equals("-1"))
                                ((Vector) tModelDetalTab3.getDataVector().get(i)).setElementAt(0, 2);
                            else
                                ((Vector) tModelDetalTab3.getDataVector().get(i)).setElementAt(1, 2);
                        }

                        tModelDetalTab3.fireTableDataChanged();
                    }
                }
            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали в таблице артикулов в работе!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab3EditActionPerformed(ActionEvent evt) {
        try {
            if (tableDetalTab3.getSelectedRow() != -1) {

                new ProjectWorkArticleForm(
                        ProjectUpdateForm.this,
                        true,
                        (Vector) tModelDetalTab3.getDataVector()
                                .get(tableDetalTab3.convertRowIndexToModel(tableDetalTab3.getSelectedRow()))
                );

                if (UtilPlan.EDIT_PROJECT_ARTICL_BUTT_ACTION) {
                    if (!UtilPlan.EDIT_PROJECT_ARTICL_ITEM.isEmpty()) {
                        if (checkItem(UtilPlan.EDIT_PROJECT_ARTICL_ITEM)) {
                            tModelDetalTab3.getDataVector()
                                    .set(tableDetalTab3.convertRowIndexToModel(tableDetalTab3.getSelectedRow()),
                                            UtilPlan.EDIT_PROJECT_ARTICL_ITEM);

                            tModelDetalTab3.fireTableDataChanged();
                        }
                    }
                }

            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали в таблице артикулов в работе!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab3PrintActionPerformed(ActionEvent evt) {
        try {
            PlanOO oo = new PlanOO("Артикула в работе ", tModelDetalTab3, tableDetalTab3.getColumnModel());
            oo.createReport("DefaultTableBookFormatCheck.ots");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab3SearchActionPerformed(ActionEvent evt) {
        try {
            if (JOptionPane.showOptionDialog(null,
                    "Искать артикула в классификаторе НСИ?",
                    "Поиск...",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Да", "Нет"}, "Нет") == JOptionPane.YES_OPTION) {

                dataVec = new Vector();

                pb = new ProgressBar(
                        ProjectUpdateForm.this,
                        false,
                        "Поиск артикулов...");

                SwingWorker sw = new SwingWorker() {
                    protected Object doInBackground() {
                        Vector rez_1 = new Vector();

                        try {
                            pdb = new PlanDB();

                            rez_1 = pdb.searchNarWorkArticle(tModelDetalTab3.getDataVector());

                            if (!rez_1.isEmpty())
                                dataVec = rez_1;
                            else
                                dataVec = tModelDetalTab3.getDataVector();

                        } catch (Exception e) {
                            dataVec = tModelDetalTab3.getDataVector();

                            JOptionPane.showMessageDialog(null,
                                    "Ошибка. " + e.getMessage(),
                                    "Ошибка",
                                    javax.swing.JOptionPane.ERROR_MESSAGE);
                        } finally {
                            pdb.disConn();
                        }
                        return null;
                    }

                    protected void done() {
                        pb.dispose();
                    }
                };
                sw.execute();
                pb.setVisible(true);
            }

            createTab3Table(dataVec);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttTab3SelectActionPerformed(ActionEvent evt) {
        try {
            if (tableDetalTab3.getSelectedRow() != -1) {

                if (tableDetalTab3.getValueAt(tableDetalTab3.getSelectedRow(), 5) == null)
                    return;

                if (tableDetalTab3.getValueAt(tableDetalTab3.getSelectedRow(), 5).toString().trim().equals("")) {

                    JOptionPane.showMessageDialog(null,
                            "Отсутствует артикул НСИ в таблице артикула в работе!",
                            "Внимание",
                            javax.swing.JOptionPane.WARNING_MESSAGE);

                } else {
                    if (tableDetalTab1.getSelectedRow() != -1) {
                        boolean flag = false;

                        if (tableDetalTab1.getValueAt(tableDetalTab1.getSelectedRow(), 4).toString()
                                .equals(tableDetalTab3.getValueAt(tableDetalTab3.getSelectedRow(), 3).toString())) {
                            flag = true;

                        } else {
                            if (JOptionPane.showOptionDialog(null,
                                    "Разные модели!",
                                    "Внимание",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.WARNING_MESSAGE,
                                    null,
                                    new Object[]{"Продолжить", "Отмена"}, "Продолжить") == JOptionPane.YES_OPTION)

                                flag = true;
                        }

                        if (flag) {
                            tableDetalTab1
                                    .setValueAt(true, tableDetalTab1.getSelectedRow(), 0);

                            tableDetalTab1
                                    .setValueAt(tableDetalTab3.getValueAt(tableDetalTab3.getSelectedRow(), 5).toString(),
                                            tableDetalTab1.getSelectedRow(),
                                            6);
                        }

                    } else
                        JOptionPane.showMessageDialog(null,
                                "Вы ничего не выбрали в таблице моделей!",
                                "Внимание",
                                javax.swing.JOptionPane.WARNING_MESSAGE);
                }
            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали в таблице артикула в работе!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createTab1Table(final Vector rowAll) {
        tModelDetalTab1 = new DefaultTableModel(rowAll, colDetalTab1) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (rowAll.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 ? true : false;
            }
        };

        tModelDetalTab1.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableDetalTab1ModelListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRowDetalTab1 == -1 || minSelectedRowDetalTab1 == -1) {
                    return;
                }
                tableDetalTab1ModelListenerIsChanging = true;
                boolean value = ((Boolean) tModelDetalTab1.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRowDetalTab1; i <= maxSelectedRowDetalTab1; i++) {
                    tModelDetalTab1.setValueAt(Boolean.valueOf(value), tableDetalTab1.convertRowIndexToModel(i), column);
                }

                minSelectedRowDetalTab1 = -1;
                maxSelectedRowDetalTab1 = -1;

                tableDetalTab1ModelListenerIsChanging = false;
            }
        });

        tableDetalTab1.setModel(tModelDetalTab1);
        tableDetalTab1.setAutoCreateColumnsFromModel(true);

        tableDetalTab1.getColumnModel().getColumn(0).setPreferredWidth(20);
        tableDetalTab1.getColumnModel().getColumn(1).setPreferredWidth(30);
        tableDetalTab1.getColumnModel().getColumn(2).setPreferredWidth(80);
        tableDetalTab1.getColumnModel().getColumn(3).setPreferredWidth(150);
        tableDetalTab1.getColumnModel().getColumn(4).setPreferredWidth(60);
        tableDetalTab1.getColumnModel().getColumn(5).setPreferredWidth(90);
        tableDetalTab1.getColumnModel().getColumn(6).setPreferredWidth(90);
        tableDetalTab1.getColumnModel().getColumn(7).setMinWidth(0);
        tableDetalTab1.getColumnModel().getColumn(7).setMaxWidth(0);
        tableDetalTab1.getColumnModel().getColumn(8).setPreferredWidth(60);
        tableDetalTab1.getColumnModel().getColumn(9).setPreferredWidth(130);
        tableDetalTab1.getColumnModel().getColumn(10).setPreferredWidth(50);
        tableDetalTab1.getColumnModel().getColumn(11).setMinWidth(0);
        tableDetalTab1.getColumnModel().getColumn(11).setMaxWidth(0);
        tableDetalTab1.getColumnModel().getColumn(12).setMinWidth(0);
        tableDetalTab1.getColumnModel().getColumn(12).setMaxWidth(0);
        tableDetalTab1.getColumnModel().getColumn(13).setPreferredWidth(100);
        tableDetalTab1.getColumnModel().getColumn(14).setPreferredWidth(100);
        tableDetalTab1.getColumnModel().getColumn(15).setMinWidth(0);
        tableDetalTab1.getColumnModel().getColumn(15).setMaxWidth(0);
        tableDetalTab1.getColumnModel().getColumn(19).setMinWidth(0);
        tableDetalTab1.getColumnModel().getColumn(19).setMaxWidth(0);

        tableDetalTab1.getColumnModel().getColumn(4).setCellRenderer(renderer);
        tableDetalTab1.getColumnModel().getColumn(6).setCellRenderer(rendererArticle);

        //tableDetalTab1.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableDetalTab1.getTableHeader(), 0, ""));                          
    }

    private void createTab2Table(final Vector rowAll) {
        tModelDetalTab2 = new DefaultTableModel(rowAll, colDetalTab2) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (rowAll.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 ? true : false;
            }
        };

        tableDetalTab2.setModel(tModelDetalTab2);
        tableDetalTab2.setAutoCreateColumnsFromModel(true);

        tableDetalTab2.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableDetalTab2.getColumnModel().getColumn(1).setPreferredWidth(110);
        tableDetalTab2.getColumnModel().getColumn(2).setPreferredWidth(60);
        tableDetalTab2.getColumnModel().getColumn(3).setPreferredWidth(90);
        tableDetalTab2.getColumnModel().getColumn(4).setMinWidth(0);
        tableDetalTab2.getColumnModel().getColumn(4).setMaxWidth(0);
        tableDetalTab2.getColumnModel().getColumn(5).setPreferredWidth(60);
        tableDetalTab2.getColumnModel().getColumn(6).setPreferredWidth(180);
    }

    private void createTab3Table(final Vector rowAll) {
        tModelDetalTab3 = new DefaultTableModel(rowAll, colDetalTab3) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (rowAll.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 ? true : false;
            }
        };

        tModelDetalTab3.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (tableDetalTab3ModelListenerIsChanging) {
                    return;
                }
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                if (column != 0 || maxSelectedRowDetalTab3 == -1 || minSelectedRowDetalTab3 == -1) {
                    return;
                }
                tableDetalTab3ModelListenerIsChanging = true;
                boolean value = ((Boolean) tModelDetalTab3.getValueAt(firstRow, column)).booleanValue();
                for (int i = minSelectedRowDetalTab3; i <= maxSelectedRowDetalTab3; i++) {
                    tModelDetalTab3.setValueAt(Boolean.valueOf(value), tableDetalTab3.convertRowIndexToModel(i), column);
                }

                minSelectedRowDetalTab3 = -1;
                maxSelectedRowDetalTab3 = -1;

                tableDetalTab3ModelListenerIsChanging = false;
            }
        });

        tableDetalTab3.setModel(tModelDetalTab3);
        tableDetalTab3.setAutoCreateColumnsFromModel(true);

        tableDetalTab3.getColumnModel().getColumn(0).setPreferredWidth(20);
        tableDetalTab3.getColumnModel().getColumn(1).setMinWidth(0);
        tableDetalTab3.getColumnModel().getColumn(1).setMaxWidth(0);
        tableDetalTab3.getColumnModel().getColumn(2).setMinWidth(0);
        tableDetalTab3.getColumnModel().getColumn(2).setMaxWidth(0);
        tableDetalTab3.getColumnModel().getColumn(3).setPreferredWidth(120);
        tableDetalTab3.getColumnModel().getColumn(4).setPreferredWidth(180);
        tableDetalTab3.getColumnModel().getColumn(5).setPreferredWidth(220);
        tableDetalTab3.getColumnModel().getColumn(6).setPreferredWidth(220);
        tableDetalTab3.getColumnModel().getColumn(7).setPreferredWidth(300);

        tableDetalTab3.getColumnModel().getColumn(4).setCellRenderer(rendererWork);

        tableDetalTab3.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(tableDetalTab3.getTableHeader(), 0, ""));
    }

    private boolean checkItem(Vector item) {
        try {
            if (tModelDetalTab3.getDataVector().contains(item)) {
                JOptionPane.showMessageDialog(null,
                        "Модель с таким артикулом уже есть в таблице",
                        "Внимание ",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

                return false;

            } else
                return true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void initPropSetting() {
        try {
            UtilPlan.WORK_ARTICLE = (UtilFunctions
                    .readPropFile(UtilPlan.SETTING_WORK_ARTICLE) != -1)
                    ? UtilFunctions.readPropFile(UtilPlan.SETTING_WORK_ARTICLE)
                    : 0;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private Vector getData(final int idProject) {
        dataProj = new Vector();
        dataWork = new Vector();

        try {
            pb = new ProgressBar(ProjectUpdateForm.this, false, "Сбор данных ...");

            SwingWorker sw = new SwingWorker() {
                protected Object doInBackground() {
                    try {
                        ppdb = new PlanPDB();

                        dataProj = ppdb.getDataItemProject(idProject);

                        dataWork = ppdb.getDataItemProjectWork(
                                idProject,
                                Integer.valueOf(user.getIdEmployee()));

                    } catch (Exception e) {
                        dataProj = new Vector();

                        dataWork = new Vector();

                        JOptionPane.showMessageDialog(null,
                                "Данные проекта плана производства не загружены! " + e.getMessage(),
                                "Ошибка",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                    } finally {
                        ppdb.disConn();
                    }
                    return null;
                }

                protected void done() {
                    pb.dispose();
                }
            };
            sw.execute();
            pb.setVisible(true);

        } catch (Exception e) {
            dataProj = new Vector();
            dataWork = new Vector();

            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }


        createTab1Table(dataProj);
        createTab2Table(new Vector());
        createTab3Table(dataWork);

        return dataProj;
    }
}
