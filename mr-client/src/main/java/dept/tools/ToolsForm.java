package dept.tools;

import by.gomel.freedev.ucframework.uccore.utils.SystemUtils;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextFieldPanel;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.CourseDateByOrderDB;
import by.march8.ecs.framework.common.LogCrutch;
import by.march8.ecs.framework.common.Settings;
import common.PanelWihtFone;
import dept.MyReportsModule;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * @author vova
 */
public class ToolsForm extends javax.swing.JDialog {

    //  private static final Logger log = new Log().getLoger(ToolsForm.class);
    private static final LogCrutch log = new LogCrutch();

    JTree tree;
    DefaultMutableTreeNode root1;
    DefaultTreeModel tm;
    CardLayout card;
    PanelWihtFone mainPanel;
    JPanel optionPanel;
    JPanel dbPanel;
    JPanel dbMSSQLPanel;
    JPanel dbPostgresPanel;
    JPanel programmPanel;
    JPanel emailPanel;
    JPanel dbfPanel;
    JButton saveButtom;
    JButton canselButtom;
    JLabel lPathProgrammPanel;
    JTextField tfPathProgrammPanel;
    JLabel lCourseByOrderPanel;
    JTextField tfCourseByOrderPanel;
    JLabel laddresDBMSSQLPanel;
    JLabel laddresDBPostgresPanel;
    JTextField tfAddresDBMSSQLPanel;
    JTextField tfAddresDBPostgresPanel;
    JLabel luserDBMSSQLPanel;
    JLabel luserDBPostgresPanel;
    JTextField tfUserDBMSSQLPanel;
    JTextField tfUserDBPostgresPanel;
    JLabel lpasswordDBMSSQLPanel;
    JLabel lpasswordDBPostgresPanel;
    JPasswordField pfPasswordDBMSSQLPanel;
    JPasswordField pfPasswordDBPostgresPanel;
    JLabel laddresEmailPanel;
    JTextField tfAddresEmailPanel;
    JLabel lPasswordEmailPanel;
    JPasswordField pfPasswordEmailPanel;
    JLabel lTrudoZatdbfPanel;
    JTextField tfTrudoZatdbfPanel;
    Properties prop = new Properties();
    JLabel lHostEmailPanel;
    JTextField tfHostEmailPanel;
    private JPanel pOpenOffice;
    private UCTextFieldPanel<String> tfpOfficePath;
    private UCTextFieldPanel<String> tfpCatalogPath;
    private JLabel lPlandbfPanel;
    private JTextField tfPlandbfPanel;
    private MainController controller;


    public ToolsForm(MainController mainController, boolean modal) {
        super(mainController.getMainForm(), modal);
        controller = mainController;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Настройки");
        initComponents();
        initPanel();
        initComponent();
        setSize(420, 400);
        setResizable(false);

        setContentPane(mainPanel);

        setLocationRelativeTo(controller.getMainForm());
        setVisible(true);
    }

    public void initPanel() {
        mainPanel = new PanelWihtFone();
        //mainPanel = new JPanel();
        optionPanel = new JPanel();
        dbPanel = new JPanel();
        dbPostgresPanel = new JPanel();
        emailPanel = new JPanel();
        programmPanel = new JPanel();
        dbfPanel = new JPanel();
        pOpenOffice = new JPanel(new MigLayout());
        saveButtom = new JButton();
        canselButtom = new JButton();
        int opx = 10;
        int opy = 20;
        int mpx = 5;
        int mpy = 10;

//--------- заполняем форму меню Программа------------
        programmPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Общие"));
        programmPanel.setLayout(null);
        lPathProgrammPanel = new JLabel("Путь к программе:");
        lPathProgrammPanel.setBounds(opx, opy, 150, 15);
        programmPanel.add(lPathProgrammPanel);
        tfPathProgrammPanel = new JTextField();
        tfPathProgrammPanel.setBounds(opx, opy + 20, 280, 20);
        programmPanel.add(tfPathProgrammPanel);
        lCourseByOrderPanel = new JLabel("Дата курсов валют на квартал по приказу:");
        lCourseByOrderPanel.setBounds(opx, opy + 40, 280, 15);
        programmPanel.add(lCourseByOrderPanel);
        tfCourseByOrderPanel = new JTextField();
        tfCourseByOrderPanel.setBounds(opx, opy + 60, 280, 20);
        programmPanel.add(tfCourseByOrderPanel);


        //dbPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        dbPanel.setLayout(null);
        //--------- заполняем форму меню База данных MS SQL------------
        dbMSSQLPanel = new JPanel();
        dbMSSQLPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("База данных MS SQL"));
        dbMSSQLPanel.setBounds(0, 0, 280, 150);
        dbMSSQLPanel.setLayout(null);
        dbPanel.add(dbMSSQLPanel);
        laddresDBMSSQLPanel = new JLabel("адрес:");
        laddresDBMSSQLPanel.setBounds(opx, opy, 50, 15);
        dbMSSQLPanel.add(laddresDBMSSQLPanel);
        tfAddresDBMSSQLPanel = new JTextField();
        tfAddresDBMSSQLPanel.setBounds(opx, opy + 20, 260, 20);
        dbMSSQLPanel.add(tfAddresDBMSSQLPanel);
        luserDBMSSQLPanel = new JLabel("пользователь:");
        luserDBMSSQLPanel.setBounds(opx, opy + 40, 120, 15);
        dbMSSQLPanel.add(luserDBMSSQLPanel);
        tfUserDBMSSQLPanel = new JTextField();
        tfUserDBMSSQLPanel.setBounds(opx, opy + 60, 260, 20);
        dbMSSQLPanel.add(tfUserDBMSSQLPanel);
        lpasswordDBMSSQLPanel = new JLabel("пароль:");
        lpasswordDBMSSQLPanel.setBounds(opx, opy + 80, 100, 15);
        dbMSSQLPanel.add(lpasswordDBMSSQLPanel);
        pfPasswordDBMSSQLPanel = new JPasswordField();
        pfPasswordDBMSSQLPanel.setBounds(opx, opy + 100, 260, 20);
        dbMSSQLPanel.add(pfPasswordDBMSSQLPanel);

//--------- заполняем форму меню База данных PostgreSQL------------
        dbPostgresPanel = new JPanel();
        dbPostgresPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("База данных PostgreSQL"));
        dbPostgresPanel.setBounds(0, 160, 280, 150);
        dbPostgresPanel.setLayout(null);
        dbPanel.add(dbPostgresPanel);
        laddresDBPostgresPanel = new JLabel("адрес:");
        laddresDBPostgresPanel.setBounds(opx, opy, 50, 15);
        dbPostgresPanel.add(laddresDBPostgresPanel);
        tfAddresDBPostgresPanel = new JTextField();
        tfAddresDBPostgresPanel.setBounds(opx, opy + 20, 260, 20);
        dbPostgresPanel.add(tfAddresDBPostgresPanel);
        luserDBPostgresPanel = new JLabel("пользователь:");
        luserDBPostgresPanel.setBounds(opx, opy + 40, 120, 15);
        dbPostgresPanel.add(luserDBPostgresPanel);
        tfUserDBPostgresPanel = new JTextField();
        tfUserDBPostgresPanel.setBounds(opx, opy + 60, 260, 20);
        dbPostgresPanel.add(tfUserDBPostgresPanel);
        lpasswordDBPostgresPanel = new JLabel("пароль:");
        lpasswordDBPostgresPanel.setBounds(opx, opy + 80, 100, 15);
        dbPostgresPanel.add(lpasswordDBPostgresPanel);
        pfPasswordDBPostgresPanel = new JPasswordField();
        pfPasswordDBPostgresPanel.setBounds(opx, opy + 100, 260, 20);
        dbPostgresPanel.add(pfPasswordDBPostgresPanel);

//--------- заполняем форму меню Эпочта------------
        emailPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Эл. почта"));
        emailPanel.setLayout(null);
        laddresEmailPanel = new JLabel("адрес:");
        laddresEmailPanel.setBounds(opx, opy, 50, 15);
        emailPanel.add(laddresEmailPanel);
        tfAddresEmailPanel = new JTextField();
        tfAddresEmailPanel.setBounds(opx, opy + 20, 280, 20);
        emailPanel.add(tfAddresEmailPanel);
        lPasswordEmailPanel = new JLabel("пароль:");
        lPasswordEmailPanel.setBounds(opx, opy + 40, 120, 15);
        emailPanel.add(lPasswordEmailPanel);
        pfPasswordEmailPanel = new JPasswordField();
        pfPasswordEmailPanel.setBounds(opx, opy + 60, 280, 20);
        emailPanel.add(pfPasswordEmailPanel);
        lHostEmailPanel = new JLabel("хост:");
        lHostEmailPanel.setBounds(opx, opy + 80, 120, 15);
        emailPanel.add(lHostEmailPanel);
        tfHostEmailPanel = new JTextField();
        tfHostEmailPanel.setBounds(opx, opy + 100, 280, 20);
        emailPanel.add(tfHostEmailPanel);

//--------- заполняем форму меню Эпочта------------
        dbfPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("DBF файлы"));
        dbfPanel.setLayout(null);
        lTrudoZatdbfPanel = new JLabel("справочник трудозатрат:");
        lTrudoZatdbfPanel.setBounds(opx, opy, 250, 15);
        dbfPanel.add(lTrudoZatdbfPanel);
        tfTrudoZatdbfPanel = new JTextField();
        tfTrudoZatdbfPanel.setBounds(opx, opy + 20, 280, 20);
        dbfPanel.add(tfTrudoZatdbfPanel);

        lPlandbfPanel = new JLabel("план:");
        lPlandbfPanel.setBounds(opx, opy + 40, 250, 15);
        dbfPanel.add(lPlandbfPanel);
        tfPlandbfPanel = new JTextField();
        tfPlandbfPanel.setBounds(opx, opy + 60, 280, 20);
        dbfPanel.add(tfPlandbfPanel);

        //--------- OPEN OFFICE и отчеты ------------


        pOpenOffice.add(new JLabel("Путь к программе LibreOffice"), "wrap");
        tfpOfficePath = new UCTextFieldPanel<>();
        pOpenOffice.add(tfpOfficePath, "width 290:20:290, height 20:20, wrap");
        tfpOfficePath.preset("");

        pOpenOffice.add(new JLabel("Путь к каталогу изображений"), "wrap");
        tfpCatalogPath = new UCTextFieldPanel<>();
        pOpenOffice.add(tfpCatalogPath, "width 290:20:290, height 20:20, wrap");
        tfpCatalogPath.preset("");

//----------формируем панель настроек-------------
        card = new CardLayout();
        optionPanel.setLayout(card);
        optionPanel.add(programmPanel, "Общие");
        optionPanel.add(dbPanel, "Базы данных");
        optionPanel.add(emailPanel, "Эл. почта");
        optionPanel.add(dbfPanel, "DBF");
        optionPanel.add(pOpenOffice, "Отчеты");
        optionPanel.setBounds(mpx + 105, mpy - 8, 300, 330);

//----------компануем главную форму------------------
        //mainPanel.setLayout(null);

        //-----------создаём дерево меню настроек--------
        tree = new JTree();
        tree.setRootVisible(false);
        root1 = new DefaultMutableTreeNode("root");
        tm = new DefaultTreeModel(root1);

        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode("Общие");
        DefaultMutableTreeNode childNode2 = new DefaultMutableTreeNode("Базы данных");
        root1.add(childNode);
        root1.add(childNode2);

        //root2 = new DefaultMutableTreeNode("b");
        //tm.insertNodeInto(root2, childNode2, childNode2.getChildCount());
        DefaultTreeCellRenderer renderer2 = new DefaultTreeCellRenderer();
        //renderer2.setOpenIcon(null);
        //renderer2.setClosedIcon(null);
        renderer2.setLeafIcon(null);
        tree.setCellRenderer(renderer2);


        /*
        DefaultMutableTreeNode childNodeb1 = new DefaultMutableTreeNode("b1");
        DefaultMutableTreeNode childNodeb2 = new DefaultMutableTreeNode("b2");
        tm = new DefaultTreeModel(root2);
        root2.add(childNode);
        root2.add(childNode2);
        root3 = new DefaultMutableTreeNode("c");*/


        tm.insertNodeInto(new DefaultMutableTreeNode("Эл. почта"), root1, root1.getChildCount());
        tm.insertNodeInto(new DefaultMutableTreeNode("DBF"), root1, root1.getChildCount());
        tm.insertNodeInto(new DefaultMutableTreeNode("Отчеты"), root1, root1.getChildCount());
        //tm.insertNodeInto(root2, root3, root1.getChildCount());
        tree.setModel(tm);

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent event) {
                TreePath path = event.getPath();
                String str = path.getLastPathComponent().toString();
                if (str.equals("Общие")) {
                    card.show(optionPanel, "Общие");
                } else if (str.equals("Базы данных")) {
                    card.show(optionPanel, "Базы данных");
                } else if (str.equals("Эл. почта")) {
                    card.show(optionPanel, "Эл. почта");
                } else if (str.equals("DBF")) {
                    card.show(optionPanel, "DBF");
                } else if (str.equals("Пути")) {
                    card.show(optionPanel, "Отчеты");
                }
            }

        });
        tree.setBounds(mpx, mpy, 100, 320);

        mainPanel.add(tree);
        mainPanel.add(optionPanel);
        saveButtom.setText("Применить");
        saveButtom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveConfiguration();
            }
        });
        saveButtom.setBounds(mpx + 170, mpy + 330, 115, 25);
        mainPanel.add(saveButtom);
        canselButtom.setText("Отмена");
        canselButtom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });
        canselButtom.setBounds(mpx + 290, mpy + 330, 115, 25);
        mainPanel.add(canselButtom);

        tfpOfficePath.addButtonSelectActionListener(e -> {
            openOfficePathSelector();
        });

        tfpCatalogPath.addButtonSelectActionListener(e -> {
            catalogPathSelector();
        });
    }

    private void catalogPathSelector() {
        String catalogPath = tfpCatalogPath.getValue();
        File file = new File(catalogPath);

        FileNameExtensionFilter filter;
        JFileChooser chooser = new JFileChooser();

        if (SystemUtils.isWindows()) {
            filter = new FileNameExtensionFilter("Файл коллекции изображений tree.list.windows", "windows", "tree.list.windows");
            chooser.setFileFilter(filter);
        } else {
            filter = new FileNameExtensionFilter("Файл коллекции изображений tree.list.linux", "linux", "tree.list.linux");
            chooser.setFileFilter(filter);
        }

        if (file.exists()) {
            chooser.setCurrentDirectory(file);
        } else {
            chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        }

        int ret = chooser.showDialog(null, "Открыть");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File openFile = chooser.getSelectedFile();
            if (openFile.exists()) {
                catalogPath = chooser.getSelectedFile().getAbsolutePath();
                tfpCatalogPath.preset(catalogPath);
                MainController.getConfiguration().setProperty(Settings.PROPERTY_CATALOG_PATH, catalogPath);
            }
        }
    }

    private void openOfficePathSelector() {
        String sofficePath = tfpOfficePath.getValue();
        File file = new File(sofficePath);

        FileNameExtensionFilter filter;
        JFileChooser chooser = new JFileChooser();
        if (SystemUtils.isWindows()) {
            filter = new FileNameExtensionFilter("Исполнимый файл soffice.exe", "exe", "soffice.exe");
            chooser.setFileFilter(filter);
        } /*else {
            filter = new FileNameExtensionFilter("Исполнимый файл soffice", "soffice");
        }*/


        if (file.exists()) {
            chooser.setCurrentDirectory(file);
        } else {
            if (SystemUtils.isWindows()) {
                chooser.setCurrentDirectory(new File(System.getenv("ProgramFiles")));
            } else {
                chooser.setCurrentDirectory(new File("/usr/bin"));
            }
        }


        int ret = chooser.showDialog(null, "Открыть");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File openFile = chooser.getSelectedFile();
            if (openFile.exists()) {
                sofficePath = chooser.getSelectedFile().getAbsolutePath();
                tfpOfficePath.preset(sofficePath);
                //controller.getConfiguration().setProperty(Settings.PROPERTY_OPEN_OFFICE,sofficePath);
            }

        }

    }

    public void initComponent() {
        try {
            File configfile = new File(MyReportsModule.confPath + "Conf.properties");
            prop.load(new FileInputStream(configfile));
            tfPathProgrammPanel.setText(prop.getProperty("programm.path"));
            tfAddresDBMSSQLPanel.setText(prop.getProperty("db.conn.url"));
            tfUserDBMSSQLPanel.setText(prop.getProperty("db.conn.user"));
            pfPasswordDBMSSQLPanel.setText(prop.getProperty("db.conn.password"));
            tfAddresDBPostgresPanel.setText(prop.getProperty("dbpostgres.conn.url"));
            tfUserDBPostgresPanel.setText(prop.getProperty("dbpostgres.conn.user"));
            pfPasswordDBPostgresPanel.setText(prop.getProperty("dbpostgres.conn.password"));
            tfAddresEmailPanel.setText(prop.getProperty("email.address"));
            pfPasswordEmailPanel.setText(prop.getProperty("email.password"));
            tfHostEmailPanel.setText(prop.getProperty("email.host"));
            tfTrudoZatdbfPanel.setText(prop.getProperty("dbf.trudozat"));
            tfPlandbfPanel.setText(prop.getProperty("dbf.plan"));
            tfpOfficePath.preset(prop.getProperty(Settings.PROPERTY_OPEN_OFFICE));
            tfpCatalogPath.preset(prop.getProperty(Settings.PROPERTY_CATALOG_PATH));
            tfCourseByOrderPanel.setText(new CourseDateByOrderDB().getCourseByOrderStr());

        } catch (Exception e) {
            System.err.println("Ошибка при чтении настроек из файла с конфигурациями Conf.properties: " + e);
            log.error("Ошибка при чтении настроек из файла с конфигурациями Conf.properties", e);
            JOptionPane.showMessageDialog(null, "Ошибка при чтении настроек из файла с конфигурациями Conf.properties", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    void saveConfiguration() {
        try {
            File configfile = new File(MyReportsModule.confPath + "Conf.properties");
            prop.setProperty("programm.path", tfPathProgrammPanel.getText());
            prop.setProperty("db.conn.url", tfAddresDBMSSQLPanel.getText());
            prop.setProperty("db.conn.user", tfUserDBMSSQLPanel.getText());
            prop.setProperty("db.conn.password", new String(pfPasswordDBMSSQLPanel.getPassword()));
            prop.setProperty("dbpostgres.conn.url", tfAddresDBPostgresPanel.getText());
            prop.setProperty("dbpostgres.conn.user", tfUserDBPostgresPanel.getText());
            prop.setProperty("dbpostgres.conn.password", new String(pfPasswordDBPostgresPanel.getPassword()));
            prop.setProperty("email.address", tfAddresEmailPanel.getText());
            prop.setProperty("email.password", new String(pfPasswordEmailPanel.getPassword()));
            prop.setProperty("email.host", tfHostEmailPanel.getText());
            prop.setProperty("dbf.trudozat", tfTrudoZatdbfPanel.getText());
            prop.setProperty("dbf.plan", tfPlandbfPanel.getText());
            prop.setProperty(Settings.PROPERTY_OPEN_OFFICE, tfpOfficePath.getValue());
            prop.setProperty(Settings.PROPERTY_CATALOG_PATH, tfpCatalogPath.getValue());
            prop.store(new FileOutputStream(configfile), null);
        } catch (Exception e) {
            System.err.println("Ошибка при попытке сохранить файл с конфигурациями Conf.properties: " + e);
            log.error("Ошибка при попытке сохранить файл с конфигурациями Conf.properties", e);
            JOptionPane.showMessageDialog(null, "Ошибка при попытке сохранить файл с конфигурациями Conf.properties", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        } finally {
            dispose();
        }
        saveCourseDateByOrder();
    }

    void saveCourseDateByOrder() {
        new CourseDateByOrderDB().updateCourseByOrder(tfCourseByOrderPanel.getText());
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 460, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 355, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
