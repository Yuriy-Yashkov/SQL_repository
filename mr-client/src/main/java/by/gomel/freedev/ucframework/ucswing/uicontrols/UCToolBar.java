package by.gomel.freedev.ucframework.ucswing.uicontrols;

import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IEditableFunctionalMode;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IEditableModule;
import by.march8.ecs.MainController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Панель инструментов с предустановленными кнопками действий.
 *
 * @author andy-linux
 */
@SuppressWarnings("serial")
public class UCToolBar extends JToolBar {

    private JButton btnNewItem = null;

    private JButton btnEditItem = null;

    private JButton btnDeleteItem = null;

    private JButton btnViewItem = null;

    private JButton btnReport = null;

    private JPanel pSearch = null;

    private JButton btnSearch = null;

    private JToggleButton btnFilter = null;

    private UCTextField tfSearch = null;

    private RightEnum right = RightEnum.READ;

    /**
     * Иниицализация панели инструментов
     */
    public UCToolBar() {

        btnViewItem = new JButton();
        btnViewItem.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/view_detail.png", "View"));

        btnNewItem = new JButton();
        btnNewItem.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/add24.png", "New"));

        btnEditItem = new JButton();
        btnEditItem.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/edit24.png", "Edit"));

        btnDeleteItem = new JButton();
        btnDeleteItem.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/delete24.png", "Delete"));

        btnFilter = new JToggleButton();
        btnFilter.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/filter.png", "Filter"));
        btnFilter.setVisible(false);

        btnSearch = new JButton();
        btnSearch.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/find.png", "Find"));

        btnReport = new JButton();
        btnReport.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/report.png", "Find"));

        btnViewItem.setToolTipText("Просмотр спецификации");
        btnNewItem.setToolTipText("Добавить запись");
        btnEditItem.setToolTipText("Редактировать запись");
        btnDeleteItem.setToolTipText("Удалить запись");
        btnFilter.setToolTipText("Показать/Скрыть фильтр");

        btnSearch.setToolTipText("Поиск");

        pSearch = new JPanel(null);
        pSearch.setOpaque(false);


        pSearch.setMaximumSize(new Dimension(200, 32));
        tfSearch = new UCTextField();
        tfSearch.setComponentParams(null, Integer.class, 0);
        tfSearch.setBounds(5, 7, 150, 20);
        btnSearch.setBounds(160, 1, 28, 28);

        pSearch.add(tfSearch);
        pSearch.add(btnSearch);

        btnViewItem.setVisible(false);
        //this.addSeparator();
        this.add(btnNewItem);
        this.add(btnEditItem);
        this.add(btnDeleteItem);
        this.add(btnViewItem);
        // this.addSeparator();
        this.add(btnFilter);
        //this.addSeparator();
        // this.add(btnReport);
        // this.addSeparator();
        this.add(pSearch);

        this.setFloatable(false);
        setVisibleSearchControls(false);

    }

    /**
     * Gets the btn new item.
     *
     * @return the btn new item
     */
    public JButton getBtnNewItem() {
        return btnNewItem;
    }

    /**
     * Gets the btn edit item.
     *
     * @return the btn edit item
     */
    public JButton getBtnEditItem() {
        return btnEditItem;
    }

    /**
     * Gets the btn delete item.
     *
     * @return the btn delete item
     */
    public JButton getBtnDeleteItem() {
        return btnDeleteItem;
    }

    /**
     * Gets the btn view item.
     *
     * @return the btn view item
     */
    public JButton getBtnViewItem() {
        return btnViewItem;
    }

    public JButton getBtnSearchItem() {
        return btnSearch;
    }

    public UCTextField getSearchField() {
        return tfSearch;
    }

    public JButton getBtnReport() {
        return btnReport;
    }

    /**
     * Обновляет активность и видимость компонентов панели согласно прав
     * доступа и количества записей
     *
     * @param count количество записей
     */
    public void updateButton(int count) {
        if (right == null) {
            return;
        }
        if (right == RightEnum.WRITE) {
            if (count > 0) {
                btnDeleteItem.setEnabled(true);
                btnEditItem.setEnabled(true);
                btnViewItem.setEnabled(true);
                btnNewItem.setEnabled(true);
                btnReport.setEnabled(true);
            } else {
                btnDeleteItem.setEnabled(false);
                btnEditItem.setEnabled(false);
                btnViewItem.setEnabled(false);
                btnNewItem.setEnabled(true);
                btnReport.setEnabled(false);
            }

        } else {
            btnDeleteItem.setEnabled(false);
            btnEditItem.setEnabled(false);
            btnViewItem.setEnabled(true);
            btnNewItem.setEnabled(false);
            btnReport.setEnabled(false);
        }
        this.updateUI();
    }

    /**
     * Управляет видимостью элементов группы ПОИСК ЗАПИСИ
     *
     * @param isVisible флаг видимости
     */
    public void setVisibleSearchControls(boolean isVisible) {
        btnSearch.setVisible(isVisible);
        tfSearch.setVisible(isVisible);
        pSearch.setVisible(isVisible);
    }

    /**
     * Метод управляет активностью компонентов панели
     *
     * @param isActive флаг активности
     */
    public void setActive(boolean isActive) {
        btnDeleteItem.setEnabled(isActive);
        btnEditItem.setEnabled(isActive);
        btnViewItem.setEnabled(isActive);
        btnNewItem.setEnabled(isActive);
        btnSearch.setEnabled(isActive);
        tfSearch.setEditable(isActive);
    }

    /**
     * Метод регистрирует события всплывающей подсказки на кнопках
     *
     * @param controller ссылка на главный контроллер приложения
     */
    public void registerHint(MainController controller) {
        MouseListener listener = controller.getHintListener();
        btnNewItem.addMouseListener(listener);
        btnEditItem.addMouseListener(listener);
        btnDeleteItem.addMouseListener(listener);
        btnSearch.addMouseListener(listener);
        tfSearch.addMouseListener(listener);
    }

    /**
     * Возвращает панель системы ПОИСК ЗАПИСИ
     *
     * @return панель
     */
    public JPanel getSearchPanel() {
        return pSearch;
    }

    @SuppressWarnings("unused")
    /**
     * Возвращает ссылку на кнопку Фильтра
     */
    public JToggleButton getBtnFilter() {
        btnFilter.setVisible(true);
        return btnFilter;
    }

    /**
     * Устанавливает уровень доступа для панели
     *
     * @param right уровень доступа
     */
    public void setRight(final RightEnum right) {
        if (right == null) {
            return;
        }
        this.right = right;

        switch (right) {
            case READ:
                btnViewItem.setVisible(true);
                btnNewItem.setEnabled(false);
                btnEditItem.setEnabled(false);
                btnDeleteItem.setEnabled(false);
                break;
            case WRITE:
                btnViewItem.setVisible(false);
                btnNewItem.setEnabled(true);
                btnEditItem.setEnabled(true);
                btnDeleteItem.setEnabled(true);
                break;
            default:
        }
    }

    /**
     * Инициализация всплывающего меню для некоторых кнопок
     *
     * @return ссылка на меню
     */
    public JPopupMenu initPopupMenu() {
        //Create the popup menu.
        final JPopupMenu popup = new JPopupMenu();

        btnNewItem.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                popup.show(e.getComponent(), 0, e.getComponent().getHeight());
            }
        });

        return popup;
    }

    /**
     * Удаление всех компонентов с панели инструментов
     */
    public void clearToolBar() {
        Component[] comp = getComponents();
        for (Component item : comp) {
            remove(item);
        }
    }


    /**
     * Привязка событий кнопоко панели инструментов с внешним интерфейсом
     *
     * @param module внешний интерфейс управления
     */
    public void registerEvents(final IEditableModule module) {
        if (module == null) {
            return;
        }

        btnNewItem.addActionListener(e -> module.addRecord());

        btnEditItem.addActionListener(e -> module.editRecord());


        btnDeleteItem.addActionListener(e -> module.deleteRecord());

        btnViewItem.addActionListener(e -> module.viewRecord());

        btnReport.addActionListener(e -> module.referenceToReport());
    }

    /**
     * Привязка событий кнопоко панели инструментов с внешним интерфейсом
     *
     * @param module внешний интерфейс управления
     */
    public void registerEvents(final IEditableFunctionalMode module) {
        if (module == null) {
            System.err.println("Module is empty");
            return;
        }

        btnNewItem.addActionListener(e -> module.addRecord());

        btnEditItem.addActionListener(e -> module.editRecord());

        btnDeleteItem.addActionListener(e -> module.deleteRecord());

        btnViewItem.addActionListener(e -> module.viewRecord());
    }
}
