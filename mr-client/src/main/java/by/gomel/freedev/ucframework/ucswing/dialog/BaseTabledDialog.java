package by.gomel.freedev.ucframework.ucswing.dialog;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.uccore.utils.TableUtils;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.interfaces.IFrameRegion;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.branch.NiceJTable;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableStringConverter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Базховый класс формы содержащей таблицу. Устаревший вариант,
 * т.к. использует явную ссылку
 * на интерфейс справочника
 *
 * @author andy-linux
 */
@SuppressWarnings("all")
public class BaseTabledDialog extends BaseDialog implements IFrameRegion {
    /**
     * Ссылка на интерфейс справочника
     */
    private final IReference references;
    /**
     * Панель инструментов
     */
    public UCToolBar toolBar = new UCToolBar();
    ;
    /**
     * панель прокрутки
     */
    protected JScrollPane spPick = null;
    /**
     * компонент таблицы
     */
    protected NiceJTable tView = null;
    /**
     * Модель данных
     */
    protected TableModel tModel = null;
    /**
     * Сортировщик модели данных
     */
    private TableRowSorter<TableModel> sorter;
    /**
     * Индекс активной строки таблицы
     */
    private int modelRow;

    /**
     * Флаг справочника
     */
    private boolean isDialogReference;

    /**
     * Конструктор
     *
     * @param controller ссылка на главный контроллер приложения
     * @param references ссылка на интерфейс справочника
     */
    public BaseTabledDialog(final MainController controller,
                            final IReference references) {
        super(controller, new Dimension(800, 400));
        setResizable(true);

        this.references = references;
        setTitle(references.getReferences().getReferenceName());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.initFrame();
        this.initEvents();
        this.initTableEvent();
        loadPesonalSetting(true);
        isDialogReference = false;

        if (references.getControlPane() != null) {
            references.getControlPane().afterEmbedding();
        }
        this.setVisible(true);
    }

    /**
     * Конструктор
     *
     * @param controller ссылка на главный контроллер приложения
     * @param references ссылка на интерфейс справочника
     * @param visible    флаг определяет отображение формы сразу после создания
     */
    public BaseTabledDialog(final MainController controller,
                            final IReference references, final boolean visible) {
        super(controller, new Dimension(800, 400));
        setResizable(true);

        this.references = references;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.initFrame();
        this.initEvents();
        isDialogReference = visible;
        loadPesonalSetting(isDialogReference);

        if (references.getControlPane() != null) {
            references.getControlPane().afterEmbedding();
        }
        this.setVisible(visible);
    }

    /**
     * Конструктор
     *
     * @param controller ссылка на главный контроллер приложения
     * @param references ссылка на интерфейс справочника
     * @param visible    флаг определяет отображение формы сразу после создания
     * @param query      SQL запрос для загрузки из БД
     */
    public BaseTabledDialog(final MainController controller,
                            final IReference references, final boolean visible,
                            final String query) {
        super(controller, new Dimension(800, 400));
        setResizable(true);

        this.references = references;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.initFrame();
        this.initEvents();
        isDialogReference = visible;
        loadPesonalSetting(isDialogReference);

        if (references.getControlPane() != null) {
            references.getControlPane().afterEmbedding();
        }
        this.setVisible(visible);
    }

    /**
     * Конструктор
     *
     * @param controller ссылка на главный контроллер приложения
     * @param reference  тип справочника
     */
    public BaseTabledDialog(final MainController controller,
                            final MarchReferencesType reference) {
        super(controller, new Dimension(800, 300));
        references = null;
    }

    @Override
    public JPanel getBottomContentPanel() {
        return panelButton;
    }

    @Override
    public JPanel getCenterContentPanel() {
        return panelCenter;
    }

    @Override
    public UCToolBar getToolBar() {
        return toolBar;
    }

    @Override
    public JPanel getTopContentPanel() {
        return panelTop;
    }

    /**
     * Метод инициализации событий элементов управления формы
     */
    private void initEvents() {
        getRootPane().registerKeyboardAction(new ActionListener() {
                                                 @Override
                                                 public void actionPerformed(final ActionEvent e) {
                                                     modalResult = false;
                                                     setVisible(false);
                                                 }
                                             }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                modalResult = false;
                setVisible(false);
            }
        });

        toolBar.getBtnNewItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                references.addRecord();
            }
        });

        toolBar.getBtnEditItem().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (modelRow >= 0) {
                    references.editRecord(modelRow);
                }
            }
        });

        toolBar.getBtnDeleteItem().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (modelRow >= 0) {
                    references.deleteRecord(modelRow);
                }
            }
        });

        toolBar.getBtnViewItem().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (modelRow >= 0) {
                    references.viewRecord(modelRow);
                }
            }
        });

        toolBar.getBtnSearchItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String searchString = toolBar.getSearchField().getText();
                searchText(searchString);
            }
        });

        this.addWindowListener(new WindowListener() {
            @Override
            public void windowActivated(final WindowEvent e) {
                if (tView.getModel().getRowCount() > 0) {
                    tView.getSelectionModel().setSelectionInterval(0, 0);
                    tView.scrollRectToVisible(new Rectangle(tView.getCellRect(
                            0, 0, true)));
                }
            }

            @Override
            public void windowClosed(final WindowEvent e) {

            }

            @Override
            public void windowClosing(final WindowEvent e) {

            }

            @Override
            public void windowDeactivated(final WindowEvent e) {
                savePesonalSetting(isDialogReference);
            }

            @Override
            public void windowDeiconified(final WindowEvent e) {

            }

            @Override
            public void windowIconified(final WindowEvent e) {

            }

            @Override
            public void windowOpened(final WindowEvent e) {

            }
        });

        toolBar.getSearchField().addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(final KeyEvent e) {
                final String searchString = toolBar.getSearchField().getText();
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    searchText(searchString);
                }
                if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                    toolBar.getSearchField().setText("");
                    searchText("");
                }
            }

            @Override
            public void keyReleased(final KeyEvent e) {

            }

            @Override
            public void keyTyped(final KeyEvent e) {

            }
        });
    }

    /**
     * Инициализация компонентов формы
     */
    @SuppressWarnings("all")
    private void initFrame() {
        panelButton.setLayout(new FlowLayout(FlowLayout.RIGHT));
        btnSave.setVisible(false);
        btnCancel.setText("Закрыть");

        toolBar.registerHint(controller);
        //setLayout(new BorderLayout());
        panelTop.add(toolBar, BorderLayout.NORTH);

        // references.updateContent();
        toolBar.getBtnReport().setVisible(false);
        tView = new NiceJTable(tModel);
        new TableFilterHeader(tView, AutoChoices.ENABLED);
        tView.setName("tView");
        tView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tView.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(final ListSelectionEvent event) {
                        final int viewRow = tView.getSelectedRow();
                        if (viewRow < 0) {

                        } else {
                            modelRow = tView.convertRowIndexToModel(viewRow);
                        }
                    }
                });

        spPick = new JScrollPane(tView);
        panelCenter.add(spPick);

        if (references.getEditingPane() == null) {
            toolBar.setActive(false);
        } else {
            toolBar.setActive(true);
        }

        references.updateContent();

        TableUtils.setTableColumnWidth(tView, false);

        sorter = new TableRowSorter<TableModel>(tModel);

        sorter.setStringConverter(new TableStringConverter() {
            @Override
            public String toString(final TableModel model, final int row,
                                   final int column) {
                if (model.getValueAt(row, column) == null) {
                    return "";
                } else {
                    return model.getValueAt(row, column).toString()
                            .toLowerCase();
                }
            }
        });
        tView.setRowSorter(sorter);

        if (references.getControlPane() != null) {
            //references.getControlPane().beforeEmbedding(this, references);
        }
    }

    /**
     * Инициализация событий таблицы
     */
    private void initTableEvent() {
        tView.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent me) {
                if (me.getClickCount() == 2) {
                    if (modelRow >= 0) {
                        references.editRecord(modelRow);
                    }
                }
            }
        });
    }

    /**
     * Метод персонализации формы для загрузки настроек для формы
     *
     * @param isDialog флаг принадлежности формы
     *                 <p><code>true</code> - диалог как справочник</p>
     *                 <p><code>false</code> - диалог как выбор из справочника</p>
     */
    private void loadPesonalSetting(final boolean isDialog) {

        if (references != null) {
            if (isDialog) {
                controller.getPersonalization().loadState(
                        references.getReferences().name().toLowerCase()
                                + "_dialog", tView, this);
            } else {
                controller.getPersonalization().loadState(
                        references.getReferences().name().toLowerCase()
                                + "_pickframe", tView, this);
            }
        }
    }

    /**
     * Метод персонализации формы для сохранения настроек для формы
     *
     * @param isDialog флаг принадлежности формы
     *                 <p><code>true</code> - диалог как справочник</p>
     *                 <p><code>false</code> - диалог как выбор из справочника</p>
     */
    private void savePesonalSetting(final boolean isDialog) {
        if (references != null) {
            if (isDialog) {
                controller.getPersonalization().saveState(
                        references.getReferences().name().toLowerCase()
                                + "_dialog", tView, this);
            } else {
                controller.getPersonalization().saveState(
                        references.getReferences().name().toLowerCase()
                                + "_pickframe", tView, this);
            }
        }
    }

    /**
     * Метод поиска данных в таблице используя сортер
     *
     * @param searchString текст для поиска
     */
    private void searchText(final String searchString) {
        if (searchString.trim().equals("")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(searchString
                    .toLowerCase()));
        }
    }
}
