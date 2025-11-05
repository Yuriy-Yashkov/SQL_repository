package by.gomel.freedev.ucframework.ucswing.uicontrols;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IReferencesDao;
import by.gomel.freedev.ucframework.ucswing.BoundsPopupMenuListener;
import by.march8.api.BaseEntity;
import by.march8.api.ISimpleReference;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.comparators.ComparatorForComboBox;
import by.march8.ecs.framework.sdk.reference.Reference;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Andy on 08.10.2014. Компонент предствляет собой панель с
 * расположенными на нем визуальными компонентами JComboBox и JButton, и
 * реализует их основные методы и события
 */

public class ComboBoxPanel<T> extends JPanel {

    private final JComboBox<T> comboBox = new JComboBox<>();
    private final JButton btnSelect = new JButton();
    private final JButton btnClear = new JButton();
    private final JPanel pButton = new JPanel(new BorderLayout());
    private final MainController controller;
    private final MarchReferencesType reference;

    private boolean isManaged = false;
    private boolean isLoadable = false;

    private ArrayList<Object> dataModel;
    private Class<?> entity;
    private String query = "";
    private int entityParentId;

    /**
     * Если панель создается данным конструктором, то ComboBox-у будет
     * недоступна работа со справочниками:
     * <p>
     * загрузка из справочника
     * <p>
     * выбор из справочника
     */
    public ComboBoxPanel() {
        super(new BorderLayout());
        controller = null;
        reference = null;
        initComponent();
    }

    /**
     * Если панель создается данным конструктором, то ComboBox-у будет работать
     * со справочниками:
     * <p>
     * загрузка из справочника
     * <p>
     * выбор из справочника
     * <p>
     * и всякие другие плюхи, которые пока не реализованы
     */
    public ComboBoxPanel(final MainController mainController,
                         final MarchReferencesType referenceType) {
        super(new BorderLayout());
        controller = mainController;
        reference = referenceType;
        initComponent();
        loadFromDatabase(reference);
    }

    public ComboBoxPanel(final MainController mainController,
                         final MarchReferencesType referenceType, final boolean isPreload) {
        super(new BorderLayout());
        controller = mainController;
        reference = referenceType;
        initComponent();
        if (isPreload) {
            loadFromDatabase(reference);
        }
    }


    /**
     * Конструктор для случаев, если комбик заполняется данными по условию,
     * зависящему от некоторых комбиков
     */
    public ComboBoxPanel(final MarchReferencesType reference) {
        super(new BorderLayout());
        controller = null;
        this.reference = reference;

        initComponent();
    }

    /**
     * Конструктор с возможностью включения\отключения поиска в ComboBox,
     * тут же вызывается метод возвращаюий отсортированыый список элементов в ComboBox.
     */
    public ComboBoxPanel(boolean isSearchable, final MainController mainController,
                         final MarchReferencesType referenceType) {
        super(new BorderLayout());
        controller = mainController;
        reference = referenceType;
        initComponent();
        //if (isSearchable) Configurator.enableAutoCompletion(comboBox);
        if (isSearchable) AutoCompleteDecorator.decorate(comboBox);
        loadFromDatabase(true, reference);
    }

    /**
     * Метод добавляет событие при нажатии на кнопку панели
     */
    public void addButtonActionListener(final ActionListener listener) {
        btnSelect.addActionListener(listener);
    }

    /**
     * Метод добавляет событие при нажатии на кнопку панели
     */
    public void addButtonDefaultActionListener(final ActionListener listener) {
        btnClear.addActionListener(listener);
    }

    /**
     * Метод добавляет событие при выборе из списка в ComboBox панели
     */
    public void addComboBoxActionListener(final ActionListener listener) {
        comboBox.addActionListener(listener);
        comboBox.addActionListener(e -> {
            if (isManaged) {
                if (comboBox.getSelectedIndex() < 0) {
                    btnClear.setVisible(false);
                } else {
                    btnClear.setVisible(true);
                }

                if (comboBox.getSelectedItem() != null) {
                    comboBox.setToolTipText(comboBox.getSelectedItem()
                            .toString());
                } else {
                    comboBox.setToolTipText(null);
                }
            }
        });
    }

    /**
     * Метод добавляет в ComboBox новый элемент
     */
    public void addItem(final T item) {
        comboBox.addItem(item);
    }

    public void addAllItem(final T[] item) {
        for (T list : item)
            comboBox.addItem(list);
    }


    /**
     * Метод добавляет авторский PoppupMenuListener The BoundsPopupMenuListener
     * is a simple class to use. You can set all the properties when the class
     * is created or you can set individual properties after class creation. The
     * properties of the class are:
     * <p>
     * setScrollBarRequired – when true, a horizontal scroll bar will
     * automatically be displayed when necessary setPopupWider – when true, the
     * width of the popup will be based on the items in the combo box. The width
     * will never be smaller than the combo box. setMaximumWidth – can control
     * the width of the popup just in case you have an unreasonably long item to
     * render. setPopupAbove – when true the popup will display above the combo
     * box.
     *
     * @param listener ссылка на слушателя событий
     */
    @SuppressWarnings("all")
    public void addPopupMenuListener(BoundsPopupMenuListener listener) {
        listener = new BoundsPopupMenuListener(true, false);
        comboBox.addPopupMenuListener(listener);
    }

    public void clear() {
        comboBox.removeAllItems();
        btnClear.setVisible(false);
        setLoadable(false);
    }

    public JComboBox<T> getComboBox() {
        return comboBox;
    }

    /**
     * Метод возвращает элемент из ComboBox по индексу index
     */
    public T getItem(final int index) {
        return comboBox.getItemAt(index);
    }

    /**
     * Метод возвращает количество элементов в ComboBox панели
     */
    public int getItemCount() {
        return comboBox.getItemCount();
    }

    public T getSelectedItem() {
        return comboBox.getItemAt(comboBox.getSelectedIndex());
    }

    public boolean isLoadable() {
        return isLoadable;
    }

/*
    @SuppressWarnings("unchecked")
    public void loadFromDatabaseByQuery(
            final MarchReferencesType referenceType, final List<?> indexes,
            final String myQuery) throws SQLException {

        if ((referenceType == null)) {
            return;
        }

        final DaoFactory factory = DaoFactory.getInstance();
        final ICommonDao dao = factory.getCommonDao();
        final ArrayList<T> array = (ArrayList<T>) dao.getEntityByQuery(
                referenceType.getClassifierClass(), indexes, myQuery);
        comboBox.setModel(new DefaultComboBoxModel(array.toArray()));
        *//*
     * if (comboBox.getItemCount() > 0) { comboBox.setSelectedIndex(0); }
     *//*
    }*/

    public void setLoadable(final boolean b) {
        isLoadable = b;
    }

    public void loadingByQuery(final String query) {
        if (!this.query.trim().equalsIgnoreCase(query.trim())) {
            loadFromDatabaseByQuery(query);
            this.query = query.trim();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromProcedure(final MarchReferencesType handbook, final String procedureName, HashMap<String, ?> parameters) {

        final DaoFactory factory = DaoFactory.getInstance();
        final IReferencesDao dao = factory.getReferencesDao();
        try {
            final ArrayList<T> array = (ArrayList<T>) dao.loadComboBoxDataByProcedure(handbook, procedureName, parameters);
            comboBox.setModel(new DefaultComboBoxModel(array.toArray()));
            setSelectedIndex(-1);
        } catch (final SQLException e) {
            System.out.println("Ошибка загрузки данных из справочника ");
            e.printStackTrace();
        }
        btnClear.setVisible(false);
    }

    /**
     * Метод производит предустановку в списке ComboBox по элементу item
     */
    public void preset(final T item) {
        if (comboBox.getItemCount() < 1) {
            return;
        }
        if (item == null) {
            comboBox.setSelectedIndex(-1);
            return;
        }
        if (item instanceof BaseEntity) {
            final BaseEntity object = (BaseEntity) item;
            if (object.getId() == 0) {
                comboBox.setSelectedIndex(-1);
            } else {
                for (int i = 0; i < comboBox.getItemCount(); i++) {
                    final BaseEntity comboItem = (BaseEntity) comboBox.getItemAt(i);
                    if (object.getId() == comboItem.getId()) {
                        comboBox.setSelectedIndex(i);
                        return;
                    }
                }
                comboBox.setSelectedIndex(-1);
            }
        }
    }

    /**
     * Метод производит предустановку в списке ComboBox по элементу ID
     */
    public void preset(final int id) {
        if (comboBox.getItemCount() < 1) {
            return;
        }
        if (id == 0) {
            comboBox.setSelectedIndex(-1);
            return;
        }
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            final BaseEntity comboItem = (BaseEntity) comboBox.getItemAt(i);
            if (id == comboItem.getId()) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
        comboBox.setSelectedIndex(-1);
    }

    /**
     * Метод производит выборку элемента из соответствующего справочника
     */
    @SuppressWarnings("unchecked")
    public T selectFromReference(boolean multiSelect) {
        if ((reference == null) || (controller == null)) {
            return null;
        }
        MarchWindowType type = MarchWindowType.PICKFRAME;
        if (multiSelect) {
            type = MarchWindowType.PICKCHECK;
        }

        final Reference ref = new Reference(controller, reference,
                type);
        comboBox.getSelectedItem();
        final Object obj = ref.showPickFrame();
        if (obj != null) {
            loadFromDatabase(true, reference);
            comboBox.requestFocusInWindow();
            preset((T) obj);
            return (T) obj;
        } else {
            // preset(null);
            return null;
        }
    }

/*    @SuppressWarnings("unchecked")
    public T selectFromReference(final String myQuery, final List<?> indexes) {
        if ((reference == null) || (controller == null)) {
            return null;
        }
        final Reference ref = new Reference(controller, reference,
                MarchWindowType.PICKFRAME);
        final Object obj = ref.showPickFrame();

        try {
            loadFromDatabaseByQuery(reference, indexes, myQuery);
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        preset((T) obj);
        return (T) obj;
    }*/

    @SuppressWarnings("unchecked")
    public T selectFromReference(final Object presetObject) {
        if ((reference == null) || (controller == null)) {
            return null;
        }
        MarchWindowType type = MarchWindowType.PICKFRAME;

        final Reference ref = new Reference(controller, reference,
                type);
        ref.setCustomQuery(query, entityParentId);
        Object o = comboBox.getSelectedItem();
        final Object obj = ref.selectFromReference(presetObject);
        if (obj != null) {
            if (!query.equals("")) {
                loadEntityListByQuery(query);
            } else {
                loadFromDatabase(true, reference);
            }
            comboBox.requestFocusInWindow();
            preset((T) obj);
            return (T) obj;
        } else {
            preset((T) o);
            return (T) o;
        }
    }

    public void setButtonVisible(final boolean b) {
        if (isManaged) {
            btnClear.setVisible(b);
        } else {
            btnClear.setVisible(false);
        }
    }

    public void setManaged(boolean isManaged) {
        this.isManaged = isManaged;
        btnSelect.setVisible(true);
    }

    public void setFocus() {
        this.getComboBox().requestFocusInWindow();
    }

    /**
     * Метод устанавливает ComboBoxModel для ComboBox компонента панели
     */
    public void setModel(final ComboBoxModel<T> aModel) {
        comboBox.setModel(aModel);
    }

    public void setSelectButtonVisible(final boolean b) {
        this.btnSelect.setVisible(b);
    }

    /**
     * Метод устанавливает активным выбранный из ComboBox элемент
     */
    public void setSelectedIndex(final int anIndex) {
        comboBox.setSelectedIndex(anIndex);
    }

    public void toDefaultSelect() {
        comboBox.setSelectedIndex(-1);
        btnClear.setVisible(false);
    }

    private void initComponent() {
        setPreferredSize(new Dimension(120, 20));
        add(comboBox, BorderLayout.CENTER);
        add(pButton, BorderLayout.EAST);
        pButton.setBorder(new EmptyBorder(0, 0, 0, 0));
        // pButton.setSize(new Dimension(20, 20));
        pButton.add(btnSelect, BorderLayout.WEST);
        pButton.add(btnClear, BorderLayout.EAST);

        btnSelect.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/book.png", "Справочник"));
        btnSelect.setPreferredSize(new Dimension(20, 20));
        btnSelect.setFocusable(false);

        btnClear.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/cancel.png", "Справочник"));
        btnClear.setPreferredSize(new Dimension(20, 20));
        btnClear.setVisible(false);
        btnClear.setFocusable(false);

        btnClear.addActionListener(e -> toDefaultSelect());
    }

    @SuppressWarnings("unchecked")
    private void loadFromDatabase(final MarchReferencesType referenceType) {
        if ((referenceType == null) || (controller == null)) {
            return;
        }

        final DaoFactory factory = DaoFactory.getInstance();
        final IReferencesDao dao = factory.getReferencesDao();
        try {
            final ArrayList<T> array = (ArrayList<T>) dao
                    .loadComboBoxData(referenceType);
            comboBox.setModel(new DefaultComboBoxModel(array.toArray()));
            if (comboBox.getItemCount() > 0) {
                comboBox.setSelectedIndex(0);
            }

            setSelectedIndex(-1);
        } catch (final SQLException e) {
            System.out.println("Ошибка загрузки данных из справочника "
                    + referenceType);
            e.printStackTrace();
        }
        btnClear.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    private void loadFromDatabase(boolean sort, final MarchReferencesType referenceType) {
        if ((referenceType == null) || (controller == null)) {
            return;
        }

        final DaoFactory factory = DaoFactory.getInstance();
        final IReferencesDao dao = factory.getReferencesDao();
        try {
            final ArrayList<T> array = (ArrayList<T>) dao
                    .loadComboBoxData(referenceType);
            Collections.sort(array, new ComparatorForComboBox());
            //сортировка элементов
            comboBox.setModel(new DefaultComboBoxModel(array.toArray()));
            if (comboBox.getItemCount() > 0) {
                comboBox.setSelectedIndex(0);
            }
            setSelectedIndex(-1);
        } catch (final SQLException e) {
            System.out.println("Ошибка загрузки данных из справочника "
                    + referenceType);
            e.printStackTrace();
        }
        btnClear.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    public void loadFromDatabaseByQuery(final String query) {
        if ((query == null) || (query.trim().equals(""))) {
            return;
        }

        final DaoFactory factory = DaoFactory.getInstance();
        final IReferencesDao dao = factory.getReferencesDao();
        try {
            final ArrayList<T> array = (ArrayList<T>) dao
                    .loadComboBoxData(query);
            comboBox.setModel(new DefaultComboBoxModel(array.toArray()));
            setSelectedIndex(-1);
        } catch (final SQLException e) {
            System.out.println("Ошибка загрузки данных из справочника ");
            e.printStackTrace();
        }
        btnClear.setVisible(false);
    }

    public JButton getSelectButton() {
        return btnSelect;
    }

    public JButton getClearButton() {
        return btnClear;
    }

    public ArrayList<Object> getDataModel() {
        ArrayList<Object> dataModel = new ArrayList<>();
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            dataModel.add(comboBox.getItemAt(i));
        }
        return dataModel;
    }

    public void updateValues() {
        loadFromDatabase(reference);
    }

    public void setEasy(boolean isEasy) {
        isManaged = !isEasy;
        btnClear.setVisible(!isEasy);
        btnSelect.setVisible(!isEasy);
    }

    @SuppressWarnings("all")
    public void loadEntityListByQuery(final String query) {
        if ((query == null) || (query.trim().equals(""))) {
            return;
        }
        this.query = query;

        final DaoFactory factory = DaoFactory.getInstance();
        final ICommonDao dao = factory.getCommonDao();
        try {
            final ArrayList<T> array = (ArrayList<T>) dao
                    .getAllEntityByStringQuery(reference.getClassifierClass(), query);
            comboBox.setModel(new DefaultComboBoxModel(array.toArray()));
            setSelectedIndex(-1);
        } catch (final SQLException e) {
            System.out.println("Ошибка загрузки данных из справочника ");
            e.printStackTrace();
        }
        btnClear.setVisible(false);
    }

    public int getEntityParentId() {
        return entityParentId;
    }

    public void setEntityParentId(final int entityParentId) {
        this.entityParentId = entityParentId;
    }

    public void presetSimple(final String documentType) {
        //  System.out.println(documentType + " / " + comboBox.getItemCount());
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            final ISimpleReference comboItem = (ISimpleReference) comboBox.getItemAt(i);
            // System.out.println(comboItem.getName().trim() + " - " + documentType);
            if (comboItem.getName().equalsIgnoreCase(documentType.trim())) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
        comboBox.setSelectedIndex(-1);
    }

    public void presetSimple(final int documentType) {
        //  System.out.println(documentType + " / " + comboBox.getItemCount());
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            final ISimpleReference comboItem = (ISimpleReference) comboBox.getItemAt(i);
            // System.out.println(comboItem.getName().trim() + " - " + documentType);
            if (comboItem.getId() == documentType) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
        comboBox.setSelectedIndex(-1);
    }

    /**
     * Кастомный метод установления элемента в комбобокс (первоначальное предназначение для entity
     * классов, у которых для id выступает embeddable класс)
     *
     * @param item объект
     */
    public void presetNew(final T item) {
        if (comboBox.getItemCount() < 1) {
            return;
        }
        if (item == null) {
            comboBox.setSelectedIndex(-1);
        }
    }


}
