package by.gomel.freedev.ucframework.ucswing.iframe;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.uccore.utils.TableUtils;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.branch.NiceJTable;
import by.march8.ecs.MainController;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
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

/**
 * Базовый фрейм дочернего окна, содержащий таблицу. Устаревший
 *
 * @author andy-linux
 */
@SuppressWarnings("all")
public class BaseTabledInternalFrame extends BaseInternalFrame {

    private static final long serialVersionUID = 1L;
    private final String prefix = "_iframe";
    /**
     * The sp pick.
     */
    protected JScrollPane spView = null;
    /**
     * The table pick.
     */
    protected NiceJTable tView = null;
    /**
     * The standards pick.
     */
    protected TableModel tModel = null;
    private IReference references = null;
    private TableRowSorter<TableModel> sorter;
    private int modelRow;

    public BaseTabledInternalFrame(final MainController controller,
                                   final IReference references) {
        super(controller);
        this.references = references;
        setTitle(references.getReferences().getReferenceName());

        initFrame();
        initEvents();

        TableUtils.setTableColumnWidth(tView, false);
        controller.getPersonalization().loadState(references.getReferences().name().toLowerCase() + prefix, tView);

        if (references.getControlPane() != null) {
            references.getControlPane().afterEmbedding();
        }
    }

    public JTable getTable() {
        return tView;
    }


    private void initEvents() {

        addInternalFrameListener(new InternalFrameListener() {
            @Override
            public void internalFrameOpened(final InternalFrameEvent e) {

            }

            @Override
            public void internalFrameClosing(final InternalFrameEvent e) {

            }

            @Override
            public void internalFrameClosed(final InternalFrameEvent e) {
                savePesonalSetting();
            }

            @Override
            public void internalFrameIconified(final InternalFrameEvent e) {

            }

            @Override
            public void internalFrameDeiconified(final InternalFrameEvent e) {

            }

            @Override
            public void internalFrameActivated(final InternalFrameEvent e) {
                if (tView.getModel().getRowCount() > 0) {
                    tView.getSelectionModel().setSelectionInterval(0, 0);
                    tView.scrollRectToVisible(new Rectangle(tView.getCellRect(0, 0, true)));
                }
            }

            @Override
            public void internalFrameDeactivated(final InternalFrameEvent e) {

            }

        });

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

        toolBar.getBtnReport().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                references.referenceToReport();
            }
        });

        toolBar.getBtnSearchItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                System.out.println("Search");
                final String searchString = toolBar.getSearchField().getText();
                searchText(searchString);
            }
        });

        toolBar.getSearchField().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                String searchString = toolBar.getSearchField().getText();
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    searchText(searchString);
                }
                if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                    toolBar.getSearchField().setText("");
                    searchText("");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    private void searchText(String searchString) {
        if (searchString.trim().equals("")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(searchString.toLowerCase()));
          /*  sorter.setRowFilter(RowFilter.regexFilter(toolBar
                    .getSearchField().getText(), tView
                    .getSelectedColumn()));*/
        }
    }

    private void initFrame() {
        tView = new NiceJTable(tModel);

        TableFilterHeader filter = new TableFilterHeader(tView, AutoChoices.ENABLED);

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

        spView = new JScrollPane(tView);
        tView.setSize(Toolkit.getDefaultToolkit().getScreenSize());

        panelCenter.setLayout(new BorderLayout());
        panelCenter.add(spView, BorderLayout.CENTER);

        panelButton.add(btnClose);


        if (references.getEditingPane() == null) {
            toolBar.setActive(false);
        } else {
            toolBar.setActive(true);
        }

        references.updateContent();


        sorter = new TableRowSorter<TableModel>(tModel);

        sorter.setStringConverter(new TableStringConverter() {
            @Override
            public String toString(TableModel model, int row, int column) {
                if (model.getValueAt(row, column) == null) {
                    String result = "";
                    return result;
                } else {
                    return model.getValueAt(row, column).toString().toLowerCase();
                }
            }
        });


        tView.setRowSorter(sorter);

        if (references.getControlPane() != null) {
            //references.getControlPane().beforeEmbedding(this, references);
        }

        toolBar.getBtnReport().setVisible(true);
    }

    private void savePesonalSetting() {
        if (references != null) {
            controller.getPersonalization().saveState(references.getReferences().name().toLowerCase() + prefix, tView);
        }
    }

}