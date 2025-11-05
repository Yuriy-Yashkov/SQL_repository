package by.gomel.freedev.ucframework.ucswing.uicontrols;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IReferencesDao;
import by.gomel.freedev.ucframework.ucswing.BoundsPopupMenuListener;
import by.march8.api.BaseEntity;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.sdk.reference.Reference;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Панель с комбобоксом, где находиться только 1 элемент,
 * для таблиц с большим количеством записей или связей, в целях
 * увеличения производительности
 *
 * @param <T>
 */
@SuppressWarnings("all")
public class RelatedFieldPanel<T> extends JPanel {
    private final JComboBox<T> comboBox = new JComboBox<T>();
    private final JButton btnSelect = new JButton();
    private final JButton btnClear = new JButton();
    private final JButton btnChange = new JButton();
    private final JPanel pButton = new JPanel(new BorderLayout());
    private final MainController controller;
    private final MarchReferencesType reference;
    private boolean isControled = false;
    private boolean isLoadable = false;
    private String query = "";

    /**
     * Если панель создается данным конструктором, то ComboBox-у будет работать
     * со справочниками:
     * <p/>
     * загрузка из справочника
     * <p/>
     * выбор из справочника
     */
    public RelatedFieldPanel(final MainController mainController,
                             final MarchReferencesType referenceType) {
        super(new BorderLayout());
        controller = mainController;
        reference = referenceType;
        initComponent();
        //loadFromDatabase(reference);
    }

    /**
     * Конструктор для случаев, если комбик заполняется данными по условию,
     * зависящему от некоторых комбиков
     */
    public RelatedFieldPanel(final MarchReferencesType reference) {
        super(new BorderLayout());
        controller = null;
        this.reference = reference;
        initComponent();
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

    public void addButtonChangeActionListener(final ActionListener listener) {
        btnChange.addActionListener(listener);
        //Создаем пустую форму диалога
          /*  final BaseEditorDialog editor = new BaseEditorDialog(controller,
                    RecordOperationType.EDIT);
            editor.setParentTitle(reference.getShortName());
            // Передаем на панель редактирования объект редактирования
       // ReferenceFactory refFactory = new ReferenceFactory();

        //IReferenceRegion referenceRegion = refFactory.getReferenceRegion();
        // Область редактирования, если есть
        EditingPane editPane = new ContractorAddressEditor(comboBox.getSelectedItem());
        // Область контроля, если есть
        //ControlPane controlPane = referenceRegion.getControlPane();
            editPane.setSourceEntity(this.getSelectedItem());
            // Для созданного пустого диалога устанавливаем панель редактирования
            editor.setEditorPane(editPane);
            // Модально показываем форму и ожидаем закрытия
            if (editor.showModal()) {
                // Форма закрыта со значением true
                // Получаем DAO слой
                final DaoFactory factory = DaoFactory.getInstance();
                // ПОлучаем интерфейс для работы с БД
                final ICommonDao dao = factory.getCommonDao();
                try {
                   // gridViewPort.setUpdatedObject(this.getSelectedItem());
                    // Обновляем запись в БД
                    dao.updateDocument(editPane.getSourceEntity());
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
                // ПОсле сохранения данных обновляем содержимое грида
                //updateContent();
            }
            // диспозим окно диалога
            editor.dispose();*/
    }

    /**
     * Метод добавляет событие при выборе из списка в ComboBox панели
     */
    public void addComboBoxActionListener(final ActionListener listener) {
        comboBox.addActionListener(listener);
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                /*if (comboBox.getSelectedIndex() < 0) {
                    btnClear.setVisible(false);
                } else {
                    btnClear.setVisible(true);
                }*/
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
     * Метод добавляет в ComboBox новый элемент и делает его текущим
     */
    public void addItem(final T item) {
        this.clear();
        comboBox.addItem(item);
        comboBox.setSelectedItem(item);
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
     * <p/>
     * setScrollBarRequired – when true, a horizontal scroll bar will
     * automatically be displayed when necessary setPopupWider – when true, the
     * width of the popup will be based on the items in the combo box. The width
     * will never be smaller than the combo box. setMaximumWidth – can control
     * the width of the popup just in case you have an unreasonably long item to
     * render. setPopupAbove – when true the popup will display above the combo
     * box.
     *
     * @param listener
     */
    public void addPopupMenuListener(BoundsPopupMenuListener listener) {
        listener = new BoundsPopupMenuListener(true, false);
        comboBox.addPopupMenuListener(listener);
    }

    public void clear() {
        comboBox.removeAllItems();
        //btnClear.setVisible(false);
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

    public void setLoadable(final boolean b) {
        isLoadable = b;
    }

    public void loadingByQuery(final String query) {
        if (!this.query.trim().equalsIgnoreCase(query.trim())) {
            loadFromDatabaseByQuery(query);
            this.query = query.trim();
        }
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
        if ((item instanceof BaseEntity) && (comboBox != null)) {
            final BaseEntity object = (BaseEntity) item;
            /*
             * if (object.getId() == ((BaseEntity)
             * comboBox.getSelectedItem()).getId()){ return ; } if
             * (object.getId()==0){ comboBox.setSelectedIndex(-1); return ; }
             */
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

    /*public void setButtonVisible(final boolean b) {
        if (isControled) {
            btnClear.setVisible(b);
        } else {
           // btnClear.setVisible(false);
        }
    }*/

    /**
     * Метод производит выборку элемента из соответствующего справочника
     */
    @SuppressWarnings("unchecked")
    public T selectFromReference() {
        if ((reference == null) || (controller == null)) {
            return null;
        }
        MarchWindowType type = MarchWindowType.PICKFRAME;
        final Reference ref = new Reference(controller, reference,
                type);
        comboBox.getSelectedItem();
        final Object obj = ref.showPickFrame();
        if (obj != null) {
            // loadFromDatabase(true,reference);
            this.clear();
            comboBox.addItem((T) obj);
            //comboBox.requestFocusInWindow();
            //preset((T) obj);
            return (T) obj;
        } else {
            // preset(null);
            return null;
        }
    }

    public void setControled() {
        isControled = true;
        btnSelect.setVisible(true);
        btnClear.setVisible(true);
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
        // btnClear.setVisible(false);
    }

    private void initComponent() {
        setPreferredSize(new Dimension(120, 20));
        add(comboBox, BorderLayout.CENTER);
        add(pButton, BorderLayout.EAST);
        pButton.setBorder(new EmptyBorder(0, 0, 0, 0));
        // pButton.setSize(new Dimension(20, 20));
        pButton.add(btnSelect, BorderLayout.WEST);
        pButton.add(btnClear, BorderLayout.EAST);
        // pButton.add(btnChange,BorderLayout.EAST);
        btnSelect.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/book.png", "Справочник"));
        btnClear.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/cancel.png", "Справочник"));
        //btnChange.setIcon(new ImageIcon(MainController.getRunPath() +"/res/edit24.png", "Справочник"));

        btnSelect.setPreferredSize(new Dimension(20, 20));
        btnClear.setPreferredSize(new Dimension(20, 20));
        // btnChange.setPreferredSize(new Dimension(20,20));
        // btnClear.setVisible(false);
        btnClear.setFocusable(false);
        btnSelect.setFocusable(false);
        btnChange.setFocusable(false);
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                toDefaultSelect();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void loadFromDatabaseByQuery(final String query) {
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
        // btnClear.setVisible(false);
    }

    public JButton getSelectButton() {
        return btnSelect;
    }

    public JButton getClearButton() {
        return btnClear;
    }
}
