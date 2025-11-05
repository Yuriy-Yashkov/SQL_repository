package dept.production.planning.ean;

import by.march8.ecs.MainController;
import com.jhlabs.awt.ParagraphLayout;
import com.toedter.calendar.JDateChooser;
import common.CheckBoxHeader;
import common.ProgressBar;
import common.SendMail;
import common.User;
import common.UtilFunctions;
import dept.MyReportsModule;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Vector;
import java.util.stream.Collectors;

import static dept.production.planning.ean.EanOO.TNVED;
import static dept.production.planning.ean.UtilEan.sendGtinsToDatamark;

/**
 * Стартовая форма для пункта меню EAN-коды.
 *
 * @author lidashka
 */
@SuppressWarnings("all")
public class EanForm extends javax.swing.JDialog {
    private static TreeMap menu;
    private User user;

    private JPanel osnovaPanel;
    private JPanel importSearchPanel;
    private JPanel searchPanel;
    private JPanel searchButtPanel;
    private JPanel buttPanel;
    private JPanel headPanel;
    private JPanel factoryPanel;
    private JPanel mailPanel;
    private JPanel statusPanel;
    private JDateChooser sStDate;
    private JDateChooser eStDate;
    private JDateChooser sInsDate;
    private JDateChooser eInsDate;
    private JDateChooser importDate;
    private JButton buttSearch;
    private JButton buttAdd;
    private JButton buttCopy;
    private JButton buttOpen;
    private JButton buttEdit;
    private JLabel title;
    private JCheckBox checkboxSt;
    private JCheckBox checkboxIns;
    private ButtonGroup buttonGroup;
    private ButtonGroup buttonGroupImport;
    private JRadioButton jRadioButton1;
    private JRadioButton jRadioButton2;
    private JRadioButton jRadioButton3;
    private JRadioButton jRadioButton4;
    private JRadioButton jRadioButton5;
    private JRadioButton jRadioButton6;
    private JRadioButton jRadioButton7;
    private JRadioButton jRadioButton8;
    private JRadioButton jRadioButton9;
    private JRadioButton jRadioButton10;
    private JTextField factoryName;
    private JTextField factoryGS1;
    private JTextField factoryEmpl;
    private JTextField factoryTel;
    private JTextField mailName;
    private JTextField mailAddress;
    private JTextField mailText;
    private JTextField fasText;
    private JTextField narText;
    private JComboBox statusCombo;

    private Vector rezaltImport;
    private Vector itemCombo;
    private Vector col;
    private Vector row;
    private JTable table;
    private DefaultTableModel tModel;
    private TableRowSorter sorter;
    private TableFilterHeader filterHeader;
    private DefaultTableCellRenderer renderer;

    private int minSelectedRow;
    private int maxSelectedRow;
    private boolean tableModelListenerIsChanging;

    private EanPDB epdb;
    private EanDB edb;

    private ProgressBar pb;
    private EanList dataEanlist;
    private boolean result;
    private ArrayList<EanList> eanListsImport;

    private MainController controller;

    private JTextField importFas;
    private JTextField importNar;
    private JTextField importIdEanList;

    private String titleImport;
    // Variables declaration - do not modify
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    /**
     * Конструктор инициализирует только меню формы
     */
    public EanForm() {
        initMenu();
    }
    /**
     * Конструктор инициализирует все компоненты формы
     *
     * @param modal
     */
    public EanForm(MainController mainController, boolean modal) {
        super(mainController.getMainForm(), modal);
        controller = mainController;

        this.setTitle("EAN-коды");

        user = User.getInstance();

        if (user.getFio() != null) {
            initMenu();
            initPropSetting();
            init();
            initData();

            buttSearch.doClick();

            this.setLocationRelativeTo(controller.getMainForm());
            this.setVisible(true);
        } else
            JOptionPane.showMessageDialog(null,
                    "Такой логин и пароль не присвоен ни одному пользователю. \n Обратитесь к администратору! ",
                    "Вход",
                    javax.swing.JOptionPane.WARNING_MESSAGE);

    }

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {
        openEanlistReport("ZayavkaEanZO.ots", false);
    }

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {
        //openEanlistReport("ZayavkaEan.ots", false);
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (JOptionPane.showOptionDialog(
                    null,
                    factoryPanel,
                    "Заказчик заявки ean-кодов",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Сохранить", "Отмена"},
                    "Сохранить") == JOptionPane.YES_OPTION) {

                try {
                    UtilFunctions.setSettingPropFile(factoryName.getText().trim(), UtilEan.SETTING_FACTORY_NAME);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Ошибка! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                try {
                    UtilFunctions.setSettingPropFile(factoryGS1.getText().trim(), UtilEan.SETTING_FACTORY_GS1);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Ошибка! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                try {
                    UtilFunctions.setSettingPropFile(factoryEmpl.getText().trim(), UtilEan.SETTING_FACTORY_EMPL);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Ошибка! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                try {
                    UtilFunctions.setSettingPropFile(factoryTel.getText().trim(), UtilEan.SETTING_FACTORY_TEL);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Ошибка! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                initPropSetting();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                openEanlistReport("ZayavkaEanZO.ots", true);

                if (JOptionPane.showOptionDialog(
                        null,
                        "Отправить заявку №" +
                                table.getValueAt(table.getSelectedRow(), 2).toString().trim() + "?",
                        "Отправка письма",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Да", "Отмена"},
                        "Отмена") == JOptionPane.YES_OPTION) {

                    if (sendMailEanlist()) {

                        JOptionPane.showMessageDialog(null,
                                "Отправка письма завершена успешно! ",
                                "Отправка письма",
                                javax.swing.JOptionPane.INFORMATION_MESSAGE);

                        try {
                            epdb = new EanPDB();

                            if (epdb.setStatus(
                                    Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()),
                                    0))

                                buttSearch.doClick();

                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null,
                                    "Ошибка. Статус отправлен не установлен! " + e.getMessage(),
                                    "Ошибка",
                                    javax.swing.JOptionPane.ERROR_MESSAGE);
                        } finally {
                            epdb.disConn();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Что-то пошло не так!\n "
                                        + "Проверьте параметры отправки и повторите попытку.",
                                "Отправка письма",
                                javax.swing.JOptionPane.WARNING_MESSAGE);
                    }
                }
            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (JOptionPane.showOptionDialog(
                    null,
                    mailPanel,
                    "Текст письма заявки ean-кодов",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Сохранить", "Отмена"},
                    "Сохранить") == JOptionPane.YES_OPTION) {

                try {
                    UtilFunctions.setSettingPropFile(mailName.getText().trim(), UtilEan.SETTING_MAIL_NAME);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Ошибка! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                try {
                    UtilFunctions.setSettingPropFile(mailAddress.getText().trim(), UtilEan.SETTING_MAIL_ADDRESS);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Ошибка! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                try {
                    UtilFunctions.setSettingPropFile(mailText.getText().trim(), UtilEan.SETTING_MAIL_TEXT);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Ошибка! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                initPropSetting();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {
        //("ZayavkaEanSpisok.ots", false);
    }

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {
        new CheckEanList(controller, true);
    }

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (table.getSelectedRow() != -1) {
                final JFileChooser fc = new JFileChooser(UtilEan.FOLDER_SELECT);
                fc.setMultiSelectionEnabled(true);
                fc.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
                fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        if (f != null) {
                            if (f.isDirectory()) {
                                return true;
                            }
                        }
                        if (f.getName().toLowerCase().endsWith(".xlsx")) {
                            return true;
                        }
                        if (f.getName().toLowerCase().endsWith(".xls")) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public String getDescription() {
                        return "";
                    }
                });
                fc.setAcceptAllFileFilterUsed(false);

                if (fc.showDialog(this, null) == JFileChooser.APPROVE_OPTION) {

                    if (fc.getSelectedFile().exists()) {
                        pb = new ProgressBar(this, false, "Проверка и загрузка файла ...");
                        SwingWorker sw = new SwingWorker() {
                            @Override
                            protected Object doInBackground() {
                                pb.toFront();

                                ArrayList<EanItem> dataReport = new ArrayList<EanItem>();
                                try {
//                                    EanOO oo = new EanOO(fc.getSelectedFile().getPath());
                                    EanOO oo = new EanOO(Arrays.stream(fc.getSelectedFiles()).map(File::getPath).collect(Collectors.toList()));
                                    dataReport = oo.openReports(EanForm.this);

                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(
                                            null,
                                            "Ошибка чтения ответа " + ex.getMessage(),
                                            "Ошибка!",
                                            javax.swing.JOptionPane.ERROR_MESSAGE);
                                }

                                pb.toFront();

                                pb.setMessage("Сохранение данных...");

                                try {
                                    epdb = new EanPDB();

                                    switch (epdb.addEan13ReportZayavka(
                                            Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()),
                                            dataReport)) {

                                        case -1:
                                            JOptionPane.showMessageDialog(null,
                                                    "Ответ на заявку не добавлен!",
                                                    "Завершено",
                                                    javax.swing.JOptionPane.WARNING_MESSAGE);
                                            break;

                                        case 0:
                                            JOptionPane.showMessageDialog(null,
                                                    "Есть не соответствующие заявке записи!",
                                                    "Завершено",
                                                    javax.swing.JOptionPane.WARNING_MESSAGE);
                                            break;

                                        case 1:
                                            JOptionPane.showMessageDialog(null,
                                                    "Ответ на заявку успешно добавлен! ",
                                                    "Завершено",
                                                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
                                            break;

                                        default:
                                            JOptionPane.showMessageDialog(null,
                                                    " ",
                                                    "Завершено",
                                                    javax.swing.JOptionPane.WARNING_MESSAGE);
                                            break;
                                    }
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null,
                                            "Ошибка. " + e.getMessage(),
                                            "Ошибка",
                                            javax.swing.JOptionPane.ERROR_MESSAGE);

                                } finally {
                                    epdb.disConn();
                                }

                                return null;
                            }

                            protected void done() {
                                pb.dispose();
                            }

                        };
                        sw.execute();
                        pb.setVisible(true);

                    } else {

                        JOptionPane.showMessageDialog(
                                null,
                                "Файл не существует!",
                                "Ошибка!",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                    }

                    EanForm.this.toFront(); // на самый верх

                    try {
                        UtilEan.FOLDER_SELECT = fc.getSelectedFile().getPath();
                        UtilFunctions.setSettingPropFile(fc.getSelectedFile().getPath(), UtilEan.SETTING_FOLDER_SELECT);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }

                    buttSearch.doClick();
                }
            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (JOptionPane.showOptionDialog(
                    null,
                    importSearchPanel,
                    "Импорт EAN-кодов ",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Показать", "Отмена"},
                    "Показать") == JOptionPane.YES_OPTION) {

                eanListsImport = new ArrayList<>();

                pb = new ProgressBar(this, false, "Сбор данных ...");
                SwingWorker sw = new SwingWorker() {
                    protected Object doInBackground() {
                        try {
                            epdb = new EanPDB();

                            if (buttonGroupImport.getSelection().getActionCommand().equals("1")) {

                                titleImport = "EAN-коды за период с "
                                        + new SimpleDateFormat("dd.MM.yyyy").format(importDate.getDate())
                                        + " по " + new SimpleDateFormat("dd.MM.yyyy").format((Calendar.getInstance()).getTime());

                                eanListsImport =
                                        epdb.getDataEanlistNotImport(UtilFunctions
                                                .convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(importDate.getDate())));

                            } else if (buttonGroupImport.getSelection().getActionCommand().equals("2")) {
                                String fas = importFas.getText().trim();
                                String nar = importNar.getText().trim().toUpperCase();

                                if (fas.length() > 0 && nar.length() > 0) {
                                    titleImport = "EAN-коды: модель - " + fas + ", артикул - " + nar;

                                    eanListsImport =
                                            epdb.getDataEanlistImport(fas, nar);

                                } else {
                                    JOptionPane.showMessageDialog(null,
                                            "Данные не загружены!\n " +
                                                    "Критерии поиска не заданы!\n" +
                                                    "Нужно заполнить поля модель и артикул.",
                                            "Внимание",
                                            JOptionPane.ERROR_MESSAGE);

                                }
                            } else if (buttonGroupImport.getSelection().getActionCommand().equals("3")) {
                                try {
                                    int idEanList = Integer.valueOf(importIdEanList.getText().trim());

                                    titleImport = "Заявка ГС1 № " + idEanList;

                                    eanListsImport =
                                            epdb.getDataEanlistImport(idEanList);

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null,
                                            "Данные не загружены!\n" +
                                                    "Критерии поиска не заданы!\n" +
                                                    "Нужно заполнить поле: код.",
                                            "Внимание",
                                            JOptionPane.ERROR_MESSAGE);

                                }
                            }

                        } catch (Exception e) {
                            eanListsImport = new ArrayList<>();
                            JOptionPane.showMessageDialog(null,
                                    "Данные не загружены!\n " + e.getMessage(),
                                    "Ошибка",
                                    javax.swing.JOptionPane.ERROR_MESSAGE);
                        } finally {
                            epdb.disConn();
                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                        pb.dispose();
                    }
                };
                sw.execute();
                pb.setVisible(true);

                new EanImportForm(
                        controller,
                        true,
                        eanListsImport,
                        titleImport);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (JOptionPane.showOptionDialog(
                    null,
                    statusPanel,
                    "Изменить статус?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Продолжить", "Отмена"},
                    "Отмена") == JOptionPane.YES_OPTION) {
                try {

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Ошибка. " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка. " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            new SearchEANByMarshListForm(controller, true, "'01.09.2018'");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {
        SwingWorker sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {

                if (table.getSelectedRow() != -1) {
                    String requestNumberId = table.getValueAt(table.getSelectedRow(), 1).toString();
                    EanPDB eanPDB = new EanPDB();
                    Map<String, String> eans = eanPDB.getEansByRequestNumberId(requestNumberId);
                    List<String> gtins = eans.entrySet().stream()
                            .filter(entry -> Arrays.asList(TNVED).stream().anyMatch(el -> entry.getValue().startsWith(el)))
                            .map(Map.Entry::getKey).collect(Collectors.toList());
                    Map<String, Boolean> gtinsValidated = sendGtinsToDatamark(gtins);
                    gtinsValidated.entrySet().stream().filter(entry -> entry.getValue()).forEach(gtin -> {
                        eanPDB.eanVerified(gtin.getKey());
                    });
                    String errorMessage = gtinsValidated.entrySet().stream().filter(gtin -> !gtin.getValue())
                            .map(gtin -> gtin.getKey()).collect(Collectors.joining("\n"));
                    if (!errorMessage.equals("")) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Не экспортированные коды:\n" + errorMessage,
                                "Внимание",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                    Long validated = gtinsValidated.entrySet().stream().filter(entry -> entry.getValue()).count();
                    Long count = gtinsValidated.entrySet().stream().count();
                    JOptionPane.showMessageDialog(
                            null,
                            "Экспортировано " + validated + " из " + count,
                            "Информация",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    if (validated.equals(0L)) {
                        eanPDB.changeEanRequestDatamarkExportStatusById(requestNumberId, "Не экспортировано");
                    } else if (validated < count) {
                        eanPDB.changeEanRequestDatamarkExportStatusById(requestNumberId, "Экспортировано частично");
                    } else if (validated.equals(count)) {
                        eanPDB.changeEanRequestDatamarkExportStatusById(requestNumberId, "Экспортировано");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Выберите заявку", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                return 0;
            }

            @Override
            protected void done() {
                pb.dispose();
            }
        };
        sw.execute();
    }
    // End of variables declaration                   

    private void initMenu() {
        menu = new TreeMap();

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jMenu4.setText("Выполнить");

        jMenuItem7.setText("Проверить заявки");
        jMenuItem7.addActionListener(evt -> jMenuItem7ActionPerformed(evt));
        //jMenu4.add(jMenuItem7);

        jMenuItem9.setText("Импорт EAN-кодов");
        jMenuItem9.addActionListener(evt -> jMenuItem9ActionPerformed(evt));
        jMenu4.add(jMenuItem9);

        jMenuItem10.setText("Изменить статус заявки");
        jMenuItem10.addActionListener(evt -> jMenuItem10ActionPerformed(evt));
        //  jMenu4.add(jMenuItem10);

        jMenuItem11.setText("Поиск EAN по моделям из м/л");
        jMenuItem11.addActionListener(evt -> jMenuItem11ActionPerformed(evt));
        jMenu4.add(jMenuItem11);

        jMenuItem12.setText("Отправить ответ в Datamark");
        jMenuItem12.addActionListener(evt -> jMenuItem12ActionPerformed(evt));
        jMenu4.add(jMenuItem12);

        jMenuBar1.add(jMenu4);

        jMenu2.setText("Почта");

        jMenuItem4.setText("Отправить заявку");
        jMenuItem4.addActionListener(evt -> jMenuItem4ActionPerformed(evt));
        jMenu2.add(jMenuItem4);

        jMenuItem8.setText("Прикрепить ответ");
        jMenuItem8.addActionListener(evt -> jMenuItem8ActionPerformed(evt));
        jMenu2.add(jMenuItem8);

        jMenuBar1.add(jMenu2);

        jMenu1.setText("Сервис");

        jMenuItem1.setText("Данные о организации");
        jMenuItem1.addActionListener(evt -> jMenuItem1ActionPerformed(evt));
        jMenu1.add(jMenuItem1);

        jMenuItem5.setText("Тект письма заявки");
        jMenuItem5.addActionListener(evt -> jMenuItem5ActionPerformed(evt));
        jMenu1.add(jMenuItem5);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Документы");

        jMenuItem2.setText("Заявка (шаблон ZO-F)");
        jMenuItem2.addActionListener(evt -> jMenuItem2ActionPerformed(evt));
        jMenu3.add(jMenuItem2);

        jMenuItem3.setText("Заявка (документ calc)");
        jMenuItem3.addActionListener(evt -> jMenuItem3ActionPerformed(evt));
        jMenu3.add(jMenuItem3);

        jMenuItem6.setText("Заявка (список)");
        jMenuItem6.addActionListener(evt -> jMenuItem6ActionPerformed(evt));
        jMenu3.add(jMenuItem6);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

    }

    private void init() {
        this.setMinimumSize(new Dimension(750, 600));
        this.setPreferredSize(new Dimension(900, 700));

        title = new JLabel("Заявки на получение EAN-кодов");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("serif", Font.PLAIN, 24));

        osnovaPanel = new JPanel();
        osnovaPanel.setLayout(new BorderLayout(1, 1));
        osnovaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        headPanel = new JPanel();
        headPanel.setLayout(new BorderLayout(1, 1));
        headPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        searchPanel = new JPanel();
        searchPanel.setLayout(new ParagraphLayout());

        searchButtPanel = new JPanel();
        searchButtPanel.setLayout(new GridLayout(0, 3, 5, 5));
        searchButtPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        buttPanel = new JPanel();
        buttPanel.setLayout(new GridLayout(0, 5, 5, 5));
        buttPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        factoryPanel = new JPanel();
        factoryPanel.setLayout(new GridLayout(0, 1, 5, 5));
        factoryPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(0, 1, 5, 5));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        mailPanel = new JPanel();
        mailPanel.setLayout(new GridLayout(0, 1, 5, 5));
        mailPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        sStDate = new JDateChooser();
        sStDate.setPreferredSize(new Dimension(120, 20));
        sStDate.setEnabled(false);

        eStDate = new JDateChooser();
        eStDate.setPreferredSize(new Dimension(120, 20));
        eStDate.setEnabled(false);

        sInsDate = new JDateChooser();
        sInsDate.setPreferredSize(new Dimension(120, 20));

        eInsDate = new JDateChooser();
        eInsDate.setPreferredSize(new Dimension(120, 20));

        importDate = new JDateChooser();
        importDate.setPreferredSize(new Dimension(120, 20));

        buttSearch = new JButton("Поиск");
        buttSearch.setPreferredSize(new Dimension(50, 20));
        buttSearch.addActionListener(evt -> buttSearchActionPerformed(evt));

        buttAdd = new JButton("Добавить");
        buttAdd.addActionListener(evt -> buttAddActionPerformed(evt));

        buttCopy = new JButton("Копировать");
        buttCopy.addActionListener(evt -> buttCopyActionPerformed(evt));

        buttOpen = new JButton("Открыть");
        buttOpen.addActionListener(EanForm.this::buttOpenActionPerformed);

        buttEdit = new JButton("Редактировать");
        buttEdit.addActionListener(evt -> buttEditActionPerformed(evt));

        checkboxSt = new JCheckBox("Дата");
        checkboxSt.addActionListener(evt -> checkboxStActionPerformed(evt));

        checkboxIns = new JCheckBox("Корр-ка");
        checkboxIns.addActionListener(evt -> checkboxInsActionPerformed(evt));
        checkboxIns.setSelected(true);

        fasText = new JTextField();
        fasText.setPreferredSize(new Dimension(150, 20));
        fasText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textTableSearchKeyPressed(evt);
            }
        });

        narText = new JTextField();
        narText.setPreferredSize(new Dimension(150, 20));
        narText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textTableSearchKeyPressed(evt);
            }
        });

        factoryName = new JTextField();
        factoryName.setPreferredSize(new Dimension(120, 20));

        factoryGS1 = new JTextField();
        factoryGS1.setPreferredSize(new Dimension(120, 20));

        factoryEmpl = new JTextField();
        factoryEmpl.setPreferredSize(new Dimension(120, 20));

        factoryTel = new JTextField();
        factoryTel.setPreferredSize(new Dimension(120, 20));

        mailName = new JTextField();
        mailName.setPreferredSize(new Dimension(300, 20));

        mailAddress = new JTextField();
        mailAddress.setPreferredSize(new Dimension(300, 20));

        mailText = new JTextField();
        mailText.setPreferredSize(new Dimension(300, 20));

        jRadioButton1 = new JRadioButton();
        jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton1.setText("Все;");
        jRadioButton1.setActionCommand("");
        jRadioButton1.setSelected(true);

        jRadioButton2 = new JRadioButton();
        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton2.setText("Отправлен;");
        jRadioButton2.setActionCommand("0");

        jRadioButton3 = new JRadioButton();
        jRadioButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton3.setText("Формируется;");
        jRadioButton3.setActionCommand("1");

        jRadioButton5 = new JRadioButton();
        jRadioButton5.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton5.setText("Ответ;");
        jRadioButton5.setActionCommand("2");

        jRadioButton6 = new JRadioButton();
        jRadioButton6.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton6.setText("Импорт;");
        jRadioButton6.setActionCommand("3");

        jRadioButton7 = new JRadioButton();
        jRadioButton7.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton7.setText("Закрыт;");
        jRadioButton7.setActionCommand("4");

        jRadioButton4 = new JRadioButton();
        jRadioButton4.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton4.setText("Удалён;");
        jRadioButton4.setActionCommand("-1");

        buttonGroup = new ButtonGroup();
        buttonGroup.add(jRadioButton1);
        buttonGroup.add(jRadioButton2);
        buttonGroup.add(jRadioButton3);
        buttonGroup.add(jRadioButton5);
        buttonGroup.add(jRadioButton6);
        buttonGroup.add(jRadioButton7);
        buttonGroup.add(jRadioButton4);

        itemCombo = new Vector();
        itemCombo.add(jRadioButton3.getText());
        itemCombo.add(jRadioButton2.getText());
        itemCombo.add(jRadioButton5.getText());
        itemCombo.add(jRadioButton6.getText());
        itemCombo.add(jRadioButton7.getText());
        itemCombo.add(jRadioButton4.getText());

        statusCombo = new JComboBox(itemCombo);

        jRadioButton8 = new JRadioButton();
        jRadioButton8.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton8.setText("За период ");
        jRadioButton8.setActionCommand("1");
        jRadioButton8.setSelected(true);

        jRadioButton9 = new JRadioButton();
        jRadioButton9.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton9.setText("Искать по всей БД ");
        jRadioButton9.setActionCommand("2");

        jRadioButton10 = new JRadioButton();
        jRadioButton10.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jRadioButton10.setText("Заявка ГС1 ");
        jRadioButton10.setActionCommand("3");

        buttonGroupImport = new ButtonGroup();
        buttonGroupImport.add(jRadioButton8);
        buttonGroupImport.add(jRadioButton9);
        buttonGroupImport.add(jRadioButton10);

        importFas = new JTextField();
        importFas.setPreferredSize(new Dimension(120, 20));

        importNar = new JTextField();
        importNar.setPreferredSize(new Dimension(120, 20));

        importIdEanList = new JTextField();
        importIdEanList.setPreferredSize(new Dimension(120, 20));

        importSearchPanel = new JPanel();
        importSearchPanel.setLayout(new ParagraphLayout());
        importSearchPanel.add(jRadioButton8, ParagraphLayout.NEW_PARAGRAPH);
        importSearchPanel.add(new JLabel("начиная с: "), ParagraphLayout.NEW_PARAGRAPH);
        importSearchPanel.add(importDate);
        importSearchPanel.add(jRadioButton9, ParagraphLayout.NEW_PARAGRAPH);
        importSearchPanel.add(new JLabel("модель: "), ParagraphLayout.NEW_PARAGRAPH);
        importSearchPanel.add(importFas);
        importSearchPanel.add(new JLabel("артикул: "), ParagraphLayout.NEW_PARAGRAPH);
        importSearchPanel.add(importNar);
        importSearchPanel.add(jRadioButton10, ParagraphLayout.NEW_PARAGRAPH);
        importSearchPanel.add(new JLabel("код: "), ParagraphLayout.NEW_PARAGRAPH);
        importSearchPanel.add(importIdEanList);

        minSelectedRow = -1;
        maxSelectedRow = -1;
        tableModelListenerIsChanging = false;

        row = new Vector();

        col = new Vector();
        col.add("");
        col.add("Код ");
        col.add("Заявка №");
        col.add("Название");
        col.add("Дата");
        col.add("Автор");
        col.add("Дата коррект.");
        col.add("Статус");
        col.add("idСтатус");
        col.add("Datamark");

        table = new JTable();
        table.setAutoCreateColumnsFromModel(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            minSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex()
                    == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                    -1 : ((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex();
            maxSelectedRow = (((DefaultListSelectionModel) e.getSource()).getMinSelectionIndex()
                    == ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex()) ?
                    -1 : ((DefaultListSelectionModel) e.getSource()).getMaxSelectionIndex();
        });

        final JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem sendItem = new JMenuItem("Отправить заявку");
        sendItem.addActionListener(evt -> jMenuItem4.doClick());
        popupMenu.add(sendItem);

        JMenuItem responseItem = new JMenuItem("Прикрепить ответ");
        responseItem.addActionListener(evt -> jMenuItem8.doClick());
        popupMenu.add(responseItem);


        table.setComponentPopupMenu(popupMenu);

        filterHeader = new TableFilterHeader(table, AutoChoices.ENABLED);

        tModel = new DefaultTableModel();

        renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                try {
                    switch (Integer.valueOf(table.getValueAt(row, table.getColumnCount() - 2).toString())) {
                        case -1:
                            cell.setBackground(Color.PINK);
                            break;
                        case 1:
                            cell.setBackground(Color.YELLOW);
                            break;
                        case 0:
                            cell.setBackground(Color.GREEN);
                            break;
                        case 2:
                        case 3:
                            cell.setBackground(Color.LIGHT_GRAY);
                            break;
                        default:
                            cell.setBackground(table.getBackground());
                            break;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Ошибка! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return cell;
            }

            ;
        };

        factoryPanel.add(new JLabel("Заказчик:"));
        factoryPanel.add(factoryName);
        factoryPanel.add(new JLabel("Номер ГС1:"));
        factoryPanel.add(factoryGS1);
        factoryPanel.add(new JLabel("Подготовил(а):"));
        factoryPanel.add(factoryEmpl);
        factoryPanel.add(new JLabel("Тел:"));
        factoryPanel.add(factoryTel);

        statusPanel.add(new JLabel("Для всех отмеченных заявок будет изменен статус."));
        statusPanel.add(statusCombo);

        mailPanel.add(new JLabel("Заголовок:"));
        mailPanel.add(mailName);
        mailPanel.add(new JLabel("Кому:"));
        mailPanel.add(mailAddress);
        mailPanel.add(new JLabel("Текст:"));
        mailPanel.add(mailText);

        searchPanel.add(new JLabel("Статус:"));
        searchPanel.add(jRadioButton1);
        searchPanel.add(jRadioButton2);
        searchPanel.add(jRadioButton3);
        searchPanel.add(jRadioButton5);
        searchPanel.add(jRadioButton6);
        searchPanel.add(jRadioButton7);
        searchPanel.add(jRadioButton4);
        searchPanel.add(checkboxSt, ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(new JLabel(" с "));
        searchPanel.add(sStDate);
        searchPanel.add(new JLabel(" по "));
        searchPanel.add(eStDate);
        searchPanel.add(new JLabel("        Модель:    "));
        searchPanel.add(fasText);
        searchPanel.add(checkboxIns, ParagraphLayout.NEW_PARAGRAPH);
        searchPanel.add(new JLabel(" с "));
        searchPanel.add(sInsDate);
        searchPanel.add(new JLabel(" по "));
        searchPanel.add(eInsDate);
        searchPanel.add(new JLabel("        Артикул:    "));
        searchPanel.add(narText);

        searchButtPanel.add(buttSearch);

        headPanel.add(title, BorderLayout.NORTH);
        headPanel.add(searchPanel, BorderLayout.CENTER);
        headPanel.add(searchButtPanel, BorderLayout.SOUTH);

        buttPanel.add(buttAdd);
        buttPanel.add(buttCopy);
        buttPanel.add(buttEdit);
        buttPanel.add(new JLabel());
        buttPanel.add(buttOpen);

        osnovaPanel.add(headPanel, BorderLayout.NORTH);
        osnovaPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        osnovaPanel.add(buttPanel, BorderLayout.SOUTH);

        getContentPane().add(osnovaPanel);
        pack();

    }

    private void checkboxStActionPerformed(ActionEvent evt) {
        if (checkboxSt.isSelected()) {
            sStDate.setEnabled(true);
            eStDate.setEnabled(true);
        } else {
            sStDate.setEnabled(false);
            eStDate.setEnabled(false);
        }
    }

    private void checkboxInsActionPerformed(ActionEvent evt) {
        if (checkboxIns.isSelected()) {
            sInsDate.setEnabled(true);
            eInsDate.setEnabled(true);
        } else {
            sInsDate.setEnabled(false);
            eInsDate.setEnabled(false);
        }
    }

    private void buttSearchActionPerformed(ActionEvent evt) {
        createTable(getData());
    }

    private void buttAddActionPerformed(ActionEvent evt) {
        new EanDetalForm(controller, true);
    }

    private void buttEditActionPerformed(ActionEvent evt) {
        if (table.getSelectedRow() != -1) {
            new EanDetalForm(controller, true, getDataEanlist(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString())), UtilEan.EDIT);

        } else
            JOptionPane.showMessageDialog(null,
                    "Вы ничего не выбрали!",
                    "Внимание",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
    }

    private void buttCopyActionPerformed(ActionEvent evt) {
        if (table.getSelectedRow() != -1) {
            new EanDetalForm(controller, true, getDataEanlist(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString())), UtilEan.COPY);

        } else
            JOptionPane.showMessageDialog(null,
                    "Вы ничего не выбрали!",
                    "Внимание",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
    }

    private void buttOpenActionPerformed(ActionEvent evt) {
        if (table.getSelectedRow() != -1) {
            new EanDetalForm(controller, true, getDataEanlist(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString())), UtilEan.OPEN);

        } else
            JOptionPane.showMessageDialog(null,
                    "Вы ничего не выбрали!",
                    "Внимание",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
    }

    private void textTableSearchKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == evt.VK_ENTER) buttSearch.doClick();
    }

    private void initPropSetting() {
        try {
            UtilEan.FACTORY_NAME = UtilFunctions.readPropFileString(UtilEan.SETTING_FACTORY_NAME);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            UtilEan.FACTORY_GS1 = UtilFunctions.readPropFileString(UtilEan.SETTING_FACTORY_GS1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            UtilEan.FACTORY_EMPL = UtilFunctions.readPropFileString(UtilEan.SETTING_FACTORY_EMPL);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            UtilEan.FACTORY_TEL = UtilFunctions.readPropFileString(UtilEan.SETTING_FACTORY_TEL);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            UtilEan.MAIL_NAME = UtilFunctions.readPropFileString(UtilEan.SETTING_MAIL_NAME);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            UtilEan.MAIL_ADDRESS = UtilFunctions.readPropFileString(UtilEan.SETTING_MAIL_ADDRESS);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            UtilEan.MAIL_TEXT = UtilFunctions.readPropFileString(UtilEan.SETTING_MAIL_TEXT);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            UtilEan.FOLDER_SELECT = UtilFunctions.readPropFileString(UtilEan.SETTING_FOLDER_SELECT);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initData() {
        try {

            java.util.Date d = (Calendar.getInstance()).getTime();
            d.setDate(1);

            sStDate.setDate(d);
            eStDate.setDate((Calendar.getInstance()).getTime());

            sInsDate.setDate(d);
            eInsDate.setDate((Calendar.getInstance()).getTime());

            importDate.setDate(d);

            factoryName.setText(UtilEan.FACTORY_NAME);
            factoryGS1.setText(UtilEan.FACTORY_GS1);
            factoryEmpl.setText(UtilEan.FACTORY_EMPL);
            factoryTel.setText(UtilEan.FACTORY_TEL);

            mailName.setText(UtilEan.MAIL_NAME);
            mailAddress.setText(UtilEan.MAIL_ADDRESS);
            mailText.setText(UtilEan.MAIL_TEXT);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);

        }
    }

    private void createTable(final Vector row) {
        tModel = new DefaultTableModel(row, col) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (row.isEmpty())
                    return super.getClass();
                else
                    return getValueAt(0, col).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 ? true : false;
            }
        };

        tModel.addTableModelListener(e -> {
            if (tableModelListenerIsChanging) {
                return;
            }
            int firstRow = e.getFirstRow();
            int column = e.getColumn();

            if (column != 0 || maxSelectedRow == -1 || minSelectedRow == -1) {
                return;
            }
            tableModelListenerIsChanging = true;
            boolean value = ((Boolean) tModel.getValueAt(firstRow, column));
            for (int i = minSelectedRow; i <= maxSelectedRow; i++) {
                tModel.setValueAt(value, table.convertRowIndexToModel(i), column);
            }

            minSelectedRow = -1;
            maxSelectedRow = -1;

            tableModelListenerIsChanging = false;
        });

        table.setModel(tModel);
        table.setAutoCreateColumnsFromModel(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(1);
        table.getColumnModel().getColumn(1).setPreferredWidth(10);
        table.getColumnModel().getColumn(2).setPreferredWidth(10);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(30);
        table.getColumnModel().getColumn(5).setPreferredWidth(30);
        table.getColumnModel().getColumn(6).setPreferredWidth(30);
        table.getColumnModel().getColumn(7).setPreferredWidth(30);
        table.getColumnModel().getColumn(8).setMinWidth(0);
        table.getColumnModel().getColumn(8).setMaxWidth(0);
        table.getColumnModel().getColumn(9).setPreferredWidth(30);

        sorter = new TableRowSorter<TableModel>(tModel);
        table.setRowSorter(sorter);
        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxHeader(table.getTableHeader(), 0, ""));

        table.getColumnModel().getColumn(7).setCellRenderer(renderer);
    }

    private Vector getData() {
        row = new Vector();
        try {
            epdb = new EanPDB();
            pb = new ProgressBar(this, false, "Поиск заявок...");
            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    row = epdb.getAllEanList(
                            fasText.getText().trim().toUpperCase(),
                            narText.getText().trim().toUpperCase(),
                            checkboxSt.isSelected(),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sStDate.getDate())),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eStDate.getDate())),
                            checkboxIns.isSelected(),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(sInsDate.getDate())),
                            UtilFunctions.convertDateStrToLong(new SimpleDateFormat("dd.MM.yyyy").format(eInsDate.getDate())),
                            buttonGroup.getSelection().getActionCommand());
                    return null;
                }

                @Override
                protected void done() {
                    pb.dispose();
                }
            };
            sw.execute();
            pb.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            pb.setVisible(true);
        } catch (Exception e) {
            row = new Vector();
            JOptionPane.showMessageDialog(null, "Ошибка поиска заявок! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            epdb.disConn();
        }
        return row;
    }

    public EanList getDataEanlist(final int idEanlist) {
        dataEanlist = new EanList();

        pb = new ProgressBar(this, false, "Сбор данных ...");
        SwingWorker sw = new SwingWorker() {
            protected Object doInBackground() {
                try {
                    epdb = new EanPDB();

                    dataEanlist = epdb.getDataEanlist(idEanlist);

                } catch (Exception e) {
                    dataEanlist = new EanList();
                    JOptionPane.showMessageDialog(null,
                            "Данные заявки не загружены!\n " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    epdb.disConn();
                }
                return null;
            }

            protected void done() {
                pb.dispose();
            }
        };
        sw.execute();
        pb.setVisible(true);

        return dataEanlist;
    }

    private boolean sendMailEanlist() {
        result = false;
        try {
            pb = new ProgressBar(this, false, "Отправка письма...");

            final SwingWorker sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    File configfile = new File(MyReportsModule.confPath + "Conf.properties");
                    Properties prop = new Properties();
                    prop.load(new FileInputStream(configfile));

                    String subject = UtilEan.MAIL_NAME;
                    String content = UtilEan.MAIL_TEXT;
                    String smtpHost = prop.getProperty("email.host");
                    String address = UtilEan.MAIL_ADDRESS;
                    String login = prop.getProperty("email.address");
                    String password = prop.getProperty("email.password");
                    String smtpPort = "25";

                    String path = (new File(MyReportsModule.confPath + "/Eanlist")).getCanonicalPath().replace('\\', '/');
                    String docName = table.getValueAt(table.getSelectedRow(), 2).toString();

                    String attachment = path + "/" + docName + ".xls";

                    SendMail.sendMultiMessage(
                            login,
                            password,
                            login,
                            address,
                            content,
                            subject,
                            attachment,
                            smtpPort,
                            smtpHost);

                    File fileEanlist = new File(attachment);
                    if (fileEanlist.exists())
                        fileEanlist.delete();

                    result = true;

                    return null;
                }

                @Override
                protected void done() {
                    pb.dispose();
                }
            };
            sw.execute();
            pb.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            pb.setVisible(true);
        } catch (Exception e) {
            result = false;
            JOptionPane.showMessageDialog(null,
                    "Ошибка отправки письма с заявкой! " + e.getMessage(),
                    "Ошибка",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        return result;
    }

    private void openEanlistReport(String nameDoc, boolean flagSave) {
        try {
            if (table.getSelectedRow() != -1) {
                EanList list = new EanList();

                try {
                    epdb = new EanPDB();
                    list = epdb.getDataEanlist(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 1).toString()));

                } catch (Exception e) {
                    list = new EanList();
                    JOptionPane.showMessageDialog(null,
                            "Ошибка! " + e.getMessage(),
                            "Ошибка",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                } finally {
                    epdb.disConn();
                }

                EanOO oo = new EanOO(
                        list.getEanName(),
                        list.getDateVvod().replace("-", "."),
                        list.getEanItems());

                oo.createReport(nameDoc, flagSave);

            } else
                JOptionPane.showMessageDialog(null,
                        "Вы ничего не выбрали!",
                        "Внимание",
                        javax.swing.JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка ",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
