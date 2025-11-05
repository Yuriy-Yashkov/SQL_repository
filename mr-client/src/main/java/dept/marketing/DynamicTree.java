package dept.marketing;

import common.ProgressBar;
import dept.marketing.cena.CenaForm;
import workDB.DB;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

@SuppressWarnings("all")
public class DynamicTree extends JPanel implements TreeWillExpandListener {
    public static boolean flagSearch = false;
    public static Object textSearch;
    static String criteriaSearch = "";
    private static boolean flag = false;
    protected DB db;
    protected DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;
    protected JTree tree;
    protected int num;
    boolean checkbox = false;
    boolean treeFasony = false;
    MarketingForm marketingform;
    CenaForm cenaform;
    Vector treeNode = new Vector();
    ProgressBar pb;
    boolean valueSet = false;
    String tmpCriteriaSearch = "";
    JDialog parentDialog;

    public DynamicTree(final javax.swing.JDialog parent, boolean modal, boolean checkbox, boolean treefasony) {
        super(new GridLayout(1, 0));

        parentDialog = parent;

        marketingform = (MarketingForm) parent;
        db = marketingform.mydb;

        this.checkbox = checkbox;
        this.treeFasony = treefasony;
        rootNode = new DefaultMutableTreeNode(new DBTreeNode(4, "Продукция предприятия", 0), true);
        treeModel = new DefaultTreeModel(rootNode, true); //опр.тип узла
        tree = new JTree(treeModel);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);

        //Регистрируем слушателей для визуального дерева
        // 1 выделение элемента
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectednode = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
                if (selectednode != null) {
                    DBTreeNode obj = (DBTreeNode) (selectednode.getUserObject());
                    if (obj.getpid() > 40000000) {
                        try {
                            marketingform.jpanelRemove();
                            marketingform.showSmallImage(obj.getpid());
                            marketingform.jpanelRepaint();
                            marketingform.cartDetailsBox(obj.getpid(), obj.getname());
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Ошибка! " + ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    if (obj.getpid() == 0) {
                        marketingform.jpanelRemove();
                        marketingform.jpanelRepaint();
                        marketingform.updateMarketingForm();
                        marketingform.cartDetailsBox2_0(obj.getname(), obj.getppid(), criteriaSearch, e.getPath().getParentPath().getLastPathComponent().toString());
                    }
                }
            }
        });
        // 2 открытие / закрытие узла
        tree.addTreeWillExpandListener(this);

        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane);
    }

    public DynamicTree(final javax.swing.JDialog parent, boolean modal, boolean treefasony) {
        super(new GridLayout(1, 0));

        parentDialog = parent;

        cenaform = (CenaForm) parent;
        db = cenaform.mydb;

        this.treeFasony = treefasony;
        rootNode = new DefaultMutableTreeNode(new DBTreeNode(4, "Продукция предприятия", 0), true);
        treeModel = new DefaultTreeModel(rootNode, true);
        tree = new JTree(treeModel);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectednode = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
                if (selectednode != null && flagSearch == false) {
                    DBTreeNode obj = (DBTreeNode) (selectednode.getUserObject());
                    if (obj.getpid() == 0) {
                        Object partNar = "";
                        String nptk = e.getPath().getParentPath().getLastPathComponent().toString();
                        if (criteriaSearch.equals("nar")) partNar = textSearch;
                        if (nptk.equals("Без категории") || nptk.equals("Продукция предприятия")) nptk = "";
                        cenaform.createDataTable(obj.getname(), obj.getppid(), partNar, nptk, new Vector(), false, false, false, "", "");
                    }
                }
            }
        });
        tree.addTreeWillExpandListener(this);

        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane);
    }

    //Добавленме ПАПКИ к родительскому узлу
    public DefaultMutableTreeNode addFolder(DefaultMutableTreeNode parent, Object childobj) {
        return addObject(parent, childobj, false, true);
    }

    //Добавленме ЛИСТА к родительскому узлу
    public DefaultMutableTreeNode addLeaf(DefaultMutableTreeNode parent, Object childobj) {
        return addObject(parent, childobj, false, false);
    }

    //Добавить элемент - 4 параметра (родитель,пользОбъект,показать,этоГруппа)
    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object childobj, boolean shouldBeVisible, boolean allowsChildren) {
        DefaultMutableTreeNode childNode = null;
        DBTreeNode tmp = null;
        String criteria = "";
        //проверка на допустимость добавления
        if ((parent != null) && (parent.getAllowsChildren())) {
            childNode = new DefaultMutableTreeNode(childobj, allowsChildren);
            tmp = (DBTreeNode) childNode.getUserObject();
            int id = tmp.getpid();

            if (!tmpCriteriaSearch.isEmpty() && tmp.getppid() > 4000) {
                id = tmp.getppid();
                criteria = tmpCriteriaSearch;
            }

            //Проверка: поиск в БД классификатора (Пример: like %41)
            if (db.searchChildren(id, tmp.getname(), parent.toString().trim(), criteria, textSearch, checkbox)) {
                //добавление в модель getChildCount() нужен при показе дерева...чтоб добавлять в конец
                //правильную сортировку при этом обеспечивает SQL
                treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
                //Сделать так, чтоб новый узел был виден на экране.
                if (shouldBeVisible) {
                    tree.scrollPathToVisible(new TreePath(childNode.getPath()));
                }
            } else {
                childNode = null;
            }
        }
        return childNode;
    }

    /**
     * Заполнение обьекта данных из БД при первом показе дерева
     */
    public void madeTree() {
        addFolder(rootNode, new DBTreeNode(0, "None", 0));
        tree.collapsePath(new TreePath(rootNode.getPath()));
        tree.expandPath(new TreePath(rootNode.getPath()));
    }

    public void updateTree() {
        rootNode.removeAllChildren();
        treeModel.reload();
        madeTree();
    }

    private void open(final Vector sarArray) {
        DefaultMutableTreeNode node = null;
        Enumeration enum_;
        Enumeration enum_tmp;
        int sarHistory = 0;

        if (!sarArray.isEmpty()) {
            enum_ = rootNode.breadthFirstEnumeration();
            while (enum_.hasMoreElements()) {
                node = (DefaultMutableTreeNode) enum_.nextElement();
                DBTreeNode obj = (DBTreeNode) (node.getUserObject());
                if (obj.getpid() == 40) {
                    tree.expandPath(new TreePath(node.getPath()));
                    enum_tmp = node.breadthFirstEnumeration();
                    enum_tmp.nextElement();
                    if (enum_tmp.hasMoreElements()) {
                        for (num = 0; num < treeModel.getChildCount(node); num++) {   //цикл по детям
                            DefaultMutableTreeNode p1 = (DefaultMutableTreeNode) treeModel.getChild(node, num);
                            int sar = ((DBTreeNode) p1.getUserObject()).getpid();
                            if (!sarArray.contains(sar)) {
                                removeNode(p1);
                            } else
                                sarArray.removeElement(sar);
                        }
                    }
                }
            }
        }

        while (!sarArray.isEmpty()) {
            int sarSearch = Integer.parseInt(sarArray.elementAt(0).toString());
            if (sarSearch == sarHistory) {
                JOptionPane.showMessageDialog(null, "Поиск завершен c ошибкой! ", "Поиск", javax.swing.JOptionPane.ERROR_MESSAGE);
                break;
            } else {
                sarHistory = sarSearch;
                node = null;
                enum_ = rootNode.breadthFirstEnumeration();
                while (enum_.hasMoreElements()) {
                    node = (DefaultMutableTreeNode) enum_.nextElement();
                    DBTreeNode obj = (DBTreeNode) (node.getUserObject());
                    if (obj.getpid() == sarSearch / 1000000) {
                        tree.expandPath(new TreePath(node.getPath()));
                        break;
                    }
                }

                enum_ = node.breadthFirstEnumeration();
                while (enum_.hasMoreElements()) {
                    node = (DefaultMutableTreeNode) enum_.nextElement();
                    DBTreeNode obj = (DBTreeNode) (node.getUserObject());
                    if (obj.getpid() == sarSearch / 100000) {
                        tree.expandPath(new TreePath(node.getPath()));
                        break;
                    }
                }

                enum_ = node.breadthFirstEnumeration();
                enum_.nextElement();
                while (enum_.hasMoreElements()) {
                    node = (DefaultMutableTreeNode) enum_.nextElement();
                    DBTreeNode obj = (DBTreeNode) (node.getUserObject());
                    if (obj.getpid() == sarSearch / 10000) {
                        tree.expandPath(new TreePath(node.getPath()));
                        enum_tmp = node.breadthFirstEnumeration();
                        enum_tmp.nextElement();
                        if (enum_tmp.hasMoreElements()) {
                            for (num = 0; num < treeModel.getChildCount(node); num++) {   //цикл по детям
                                DefaultMutableTreeNode p1 = (DefaultMutableTreeNode) treeModel.getChild(node, num);
                                int sar = ((DBTreeNode) p1.getUserObject()).getpid();
                                if (!sarArray.contains(sar)) {
                                    removeNode(p1);
                                } else
                                    sarArray.removeElement(sar);
                            }
                        }
                    }
                }
            }
        }
    }

    private void open(final Map<Object, String> array) {
        Set<Object> keys = array.keySet();
        Object[] sortedKeys = keys.toArray();
        Arrays.sort(sortedKeys);

        Map<Object, String> sortedMap = new LinkedHashMap<Object, String>();
        for (Object key : sortedKeys) {
            sortedMap.put(key, array.get(key));
        }

        DefaultMutableTreeNode node = null;
        Enumeration enum_;
        Enumeration enum_tmp;
        int sarHistory = 0;
        Vector tmp = new Vector();

        if (!sortedMap.isEmpty()) {
            enum_ = rootNode.breadthFirstEnumeration();
            while (enum_.hasMoreElements()) {
                node = (DefaultMutableTreeNode) enum_.nextElement();
                DBTreeNode obj = (DBTreeNode) (node.getUserObject());
                if (obj.getpid() == 40) {
                    tree.expandPath(new TreePath(node.getPath()));
                    enum_tmp = node.breadthFirstEnumeration();
                    enum_tmp.nextElement();
                    if (enum_tmp.hasMoreElements()) {
                        for (num = 0; num < treeModel.getChildCount(node); num++) {   //цикл по детям
                            DefaultMutableTreeNode p1 = (DefaultMutableTreeNode) treeModel.getChild(node, num);
                            if (!sortedMap.containsValue(p1.toString())) {
                                removeNode(p1);
                            }
                        }
                    }
                }
            }
        }

        int sarSearch = 0;
        while (!sortedMap.isEmpty()) {
            for (Object k : sortedMap.keySet()) {
                sarSearch = Integer.parseInt(k.toString());
                break;
            }
            if (sarSearch == sarHistory) {
                JOptionPane.showMessageDialog(null, "Поиск завершен c ошибкой! ", "Поиск", javax.swing.JOptionPane.ERROR_MESSAGE);
                break;
            } else {
                sarHistory = sarSearch;
                node = null;
                enum_ = rootNode.breadthFirstEnumeration();
                while (enum_.hasMoreElements()) {
                    node = (DefaultMutableTreeNode) enum_.nextElement();
                    DBTreeNode obj = (DBTreeNode) (node.getUserObject());
                    if (obj.getpid() == sarSearch / 1000000) {
                        tree.expandPath(new TreePath(node.getPath()));
                        break;
                    }
                }

                enum_ = node.breadthFirstEnumeration();
                while (enum_.hasMoreElements()) {
                    node = (DefaultMutableTreeNode) enum_.nextElement();
                    DBTreeNode obj = (DBTreeNode) (node.getUserObject());
                    if (obj.getpid() == sarSearch / 100000) {
                        tree.expandPath(new TreePath(node.getPath()));
                        break;
                    }
                }

                enum_ = node.breadthFirstEnumeration();
                enum_.nextElement();
                while (enum_.hasMoreElements() && !sortedMap.isEmpty()) {
                    node = (DefaultMutableTreeNode) enum_.nextElement();
                    DBTreeNode obj = (DBTreeNode) (node.getUserObject());
                    if (obj.getpid() == sarSearch / 10000) {
                        tree.expandPath(new TreePath(node.getPath()));
                        enum_tmp = node.breadthFirstEnumeration();
                        enum_tmp.nextElement();
                        if (enum_tmp.hasMoreElements()) {
                            for (num = 0; num < treeModel.getChildCount(node); num++) {   //цикл по детям
                                DefaultMutableTreeNode p1 = (DefaultMutableTreeNode) treeModel.getChild(node, num);
                                if (!sortedMap.containsValue(p1.toString())) {
                                    removeNode(p1);
                                } else {
                                    for (Iterator i = sortedMap.keySet().iterator(); i.hasNext(); ) {
                                        Object key = i.next();
                                        if (sortedMap.get(key).equals(p1.toString())) {
                                            while (i.hasNext()) {
                                                i.next();
                                            }
                                            sortedMap.remove(key);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                tmp = new Vector();
                for (Object key : sortedMap.keySet()) {
                    if (String.valueOf(sarSearch).substring(0, 4).equals(key.toString().substring(0, 4))) {
                        tmp.add(key);
                    }
                }
                for (Object object : tmp)
                    sortedMap.remove(object);
            }
        }
    }

    private void removeNode(final DefaultMutableTreeNode p1) {
        if (SwingUtilities.isEventDispatchThread()) {
            treeModel.removeNodeFromParent(p1);
            num--;
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    try {
                        treeModel.removeNodeFromParent(p1);
                        num--;
                    } catch (Exception e) {
                        System.out.println("Ошибка " + e + " " + p1);
                    }
                }
            });
        }
    }

    public void search(final String criteria, final String text) {
        criteriaSearch = criteria;
        textSearch = text;

        if (treeFasony) tmpCriteriaSearch = criteria;
        else tmpCriteriaSearch = "";

        pb = new ProgressBar(parentDialog, false, "Идет поиск...");
        final SwingWorker sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                updateTree();
                if (!flagSearch) {
                    if (treeFasony) {
                        open(db.searchFas(criteria, text, checkbox));
                    } else {
                        open(db.searchSar(criteria, text, checkbox));
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                if (treeFasony) {
                    criteriaSearch = criteria;
                    tmpCriteriaSearch = "";
                }
                pb.dispose();
            }
        };
        sw.execute();
        pb.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        pb.setVisible(true);
    }

    public void selectedPath() {
        TreePath patch = tree.getSelectionPath();
        cleanSelectedPath();
        tree.setSelectionPath(patch);
    }

    public void cleanSelectedPath() {
        tree.setSelectionPath(null);
    }

    //Реализация слушателя для события при открытии ветки
    public void treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException {
        if (!flag) {
            TreePath path;
            DefaultMutableTreeNode node, p1;
            DBTreeNode obj;
            String isfolder, nested, descr;
            int i;

            path = e.getPath();//получить путь на узел
            node = (DefaultMutableTreeNode) (path.getLastPathComponent()); //получаем узел
            if (node == null) {
                throw new ExpandVetoException(e);
            }
            //получаем польз.обьект
            obj = (DBTreeNode) (node.getUserObject());
            //получаем ID родителя в БД
            int pid = obj.getpid();
            String name = obj.getname();
            int ppid = obj.getppid();

            //------------------------------------очистить ветку-----
            int childs = treeModel.getChildCount(node);
            for (i = 0; i < childs; i++) {   //цикл по детям
                p1 = (DefaultMutableTreeNode) (treeModel.getChild(node, 0));
                treeModel.removeNodeFromParent(p1);
            }
            //------------------------------------добавить ветку-----
            if (flagSearch) {
                if (treeFasony) treeNode = db.openTreeFasony(pid, criteriaSearch, textSearch, checkbox);
                else treeNode = db.openTree(pid, criteriaSearch, textSearch, checkbox);
            } else {
                treeNode = db.openNode(pid, name, checkbox, treeFasony);
                if (treeFasony) criteriaSearch = "";
            }
            int rows = getRowCount();

            //цикл по строкам
            for (i = 0; i < rows; i++) {
                pid = Integer.parseInt(getValueAt(i, 0).toString().trim());
                ppid = Integer.parseInt(getValueAt(i, 1).toString().trim());
                isfolder = getValueAt(i, 2).toString();
                nested = getValueAt(i, 3).toString();
                if (nested == null) {
                    nested = "-";
                }
                descr = getValueAt(i, 4).toString();
                //Создание узла
                if (nested.equals("+")) {       //полная папка
                    p1 = addFolder(node, new DBTreeNode(pid, descr, ppid));
                    addFolder(p1, new DBTreeNode(0, "None", 0));
                } else {        //нет вложений
                    if (isfolder.equals("1")) {  //пустая папка
                        p1 = addFolder(node, new DBTreeNode(pid, descr, ppid));
                        flag = true;
                        tree.expandPath(new TreePath(p1.getPath()));
                        flag = false;
                    } else {                   //просто лист
                        addLeaf(node, new DBTreeNode(pid, descr, ppid));
                    }
                }
            }
            //--------------------------------------------------------
        }
    }

    //Возникает, когда закрываем узел...
    public void treeWillCollapse(TreeExpansionEvent e) {
    }

    //Получить общее количество строк
    public int getRowCount() {
        return treeNode.size();
    }

    //Получить значение ячейки результирующей таблицы (отсчет с "0")
    public Object getValueAt(int aRow, int aColumn) {
        Vector r = (Vector) treeNode.elementAt(aRow);
        return r.elementAt(aColumn);
    }

    public class DBTreeNode {
        protected int pid;
        protected String name;
        protected int ppid;

        public DBTreeNode() {
            pid = 0;
            name = "";
            ppid = 0;
        }

        public DBTreeNode(String str) {
            pid = 0;
            name = str;
            ppid = 0;
        }

        public DBTreeNode(int id, String str, int par) {
            pid = id;
            name = str;
            ppid = par;
        }

        //Получить ID строки в таблице БД
        public int getpid() {
            return pid;
        }

        //Получить  наименеование обьекта
        public String getname() {
            return name;
        }

        //Получить ID родителя строки
        public int getppid() {
            return ppid;
        }

        //Метод для отображения в дереве
        @Override
        public String toString() {
            if (treeFasony) return name;
            else return name + " (" + Integer.toString(pid) + ")";
        }
    }
}