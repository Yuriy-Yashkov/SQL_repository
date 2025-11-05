package by.march8.ecs.application.modules.references.materals.editor;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.uccore.utils.TableUtils;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.branch.NiceJTable;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.model.GeneralTableModel;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.helpers.FormatUtils;
import by.march8.entities.materials.YarnCategory;
import by.march8.entities.materials.YarnComponent;
import by.march8.entities.materials.YarnItem;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

//import by.march8.ecs.framework.common.comparators.SortByPercent;

/**
 * Панель редактирования пряжи. Реализация старая, и новых фишек в ней нет.
 * С течением времени все перепишется 100%
 *
 * @author andy-linux
 * @see by.march8.entities.materials.YarnItem
 * @see by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane
 */

@SuppressWarnings("serial")
public class YarnItemEditor extends EditingPane {

    private final ArrayList<Object> data = new ArrayList<>();

    /**
     * The standards pick.
     */

    private final TableModel tModel = new GeneralTableModel(data, YarnComponent.class);
    private final JLabel lPercent = new JLabel();
    private final JLabel lName = new JLabel("Наименование ");
    private final JTextField tfName = new JTextField();
    private final JLabel lNote = new JLabel("Примечание ");
    private final JTextField tfNote = new JTextField();
    private final MainController controller;
    private final JCheckBox cbComposite = new JCheckBox("Компонентная пряжа");
    private EditingPane editPane;
    private UCToolBar toolBar = null;
    private NiceJTable tView = null;
    private YarnItem source = null;
    private YarnCategory category;

    private JFormattedTextField tfCode = null;
    private ComboBoxPanel<YarnCategory> cbpCategory;

    public YarnItemEditor(final IReference reference) {
        super(reference);
        this.controller = reference.getMainController();
        editPane = new YarnComponentEditor(reference);

        initPane();
        initEvents();

        toolBar.setRight(reference.getRight());
        toolBar.getBtnReport().setVisible(false);
    }

    public YarnItemEditor(final FrameViewPort frameViewPort) {
        super();
        this.controller = frameViewPort.getController();
        editPane = new YarnComponentEditor(frameViewPort);
        initPane();
        initEvents();
        frameViewPort.getFrameRegion().getToolBar().getBtnReport().setVisible(false);
    }

    @Override
    public Object getSourceEntity() {
        source.setCode(Integer.valueOf(tfCode.getText()));
        source.setCategory(category);
        source.setName(tfName.getText().trim());
        source.setNote(tfNote.getText());
        source.setComposite(cbComposite.isSelected());
        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object == null) {
            source = new YarnItem();
            tfName.setText("");
            tfNote.setText("");
            tfCode.setValue(900000000);
            category = new YarnCategory();
            data.clear();

        } else {
            source = (YarnItem) object;
            tfName.setText(source.getName());
            tfCode.setValue(source.getCode());
            tfNote.setText(source.getNote());
            category = source.getCategory();

            cbComposite.setSelected(source.isComposite());
        }
        cbpCategory.preset(category);
        updateYarnComponentList();
        tfName.setEditable(cbComposite.isSelected());
    }

    @Override
    public boolean verificationData() {
        if (cbComposite.isSelected()) {
            if (tfName.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,
                        "Укажите наименование пряжи", "Ошибка!",
                        JOptionPane.ERROR_MESSAGE);
                tfName.requestFocusInWindow();
                return false;
            }
        } else {

            if (cbpCategory.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null,
                        "Укажите категорию пряжи", "Ошибка!",
                        JOptionPane.ERROR_MESSAGE);
                cbpCategory.setFocus();
                return false;
            }

            try {
                if (source.getComponents().size() < 1) {
                    JOptionPane.showMessageDialog(null,
                            "Пряжа должна содержать как минимум один вид сырья", "Ошибка!",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                float totalPercent = 0;
                for (YarnComponent item : source.getComponents()) {
                    totalPercent += item.getComponentPercent();
                }

                if ((totalPercent < 100) || (totalPercent > 100)) {
                    JOptionPane.showMessageDialog(null,
                            "Полное процентное содержание всех компонентов пряжи всегда 100%", "Ошибка!",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                tfName.setText(prepareName());
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    private String prepareName() {
        ArrayList<YarnComponent> components = new ArrayList<>(source.getComponents());
        //   Collections.sort(components, new SortByPercent());
        StringBuilder sb = new StringBuilder();
        for (YarnComponent item : components) {
            sb.append(item.getComponentAbbrev()).append("/");

        }

        String tmp = sb.toString();
        tmp = tmp.substring(0, tmp.length() - 1);
        tmp = tmp + " - " + category.getName();

        return tmp.toUpperCase();
    }

    public void addYarnComponent() {
        final BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        editPane.setSourceEntity(null);
        editor.setEditorPane(editPane);

        if (editor.showModal()) {
            YarnComponent item = (YarnComponent) editPane.getSourceEntity();
            item.setYarn(source);
            source.getComponents().add(item);
            updateYarnComponentList();
        }

    }

    public void editYarnComponent(final int selectedRow) {
        final BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        YarnComponent item = (YarnComponent) data.get(selectedRow);
        editPane.setSourceEntity(item);
        editor.setEditorPane(editPane);

        if (editor.showModal()) {
            data.set(selectedRow, editPane.getSourceEntity());
            updateYarnComponentList();
        }
    }

    public void deleteYarnComponent(final int selectedRow) {
        final int answer = JOptionPane.showConfirmDialog(null,
                "Удалить компонент пряжи ?", "Удаление записи",
                JOptionPane.YES_NO_OPTION);
        if (answer == 0) {
            YarnComponent item = (YarnComponent) data.get(selectedRow);
            source.getComponents().remove(item);
            updateYarnComponentList();
        }
    }

    public void updateYarnComponentList() {
        data.clear();
        data.addAll(source.getComponents());

        ((AbstractTableModel) tModel).fireTableDataChanged();
        toolBar.updateButton(data.size());
    }


    /**
     * Инициализация событий на панели
     */
    private void initEvents() {
        cbpCategory.addComboBoxActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                category = cbpCategory.getSelectedItem();
            }
        });

        cbpCategory.addButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                category = cbpCategory.selectFromReference(false);
            }
        });

        toolBar.getBtnNewItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                addYarnComponent();
            }
        });
        toolBar.getBtnEditItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final int row = tView.getSelectedRow();
                if (row < 0) {
                    return;
                }
                editYarnComponent(tView.getSelectedRow());
            }
        });

        toolBar.getBtnDeleteItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final int row = tView.getSelectedRow();
                if (row < 0) {
                    return;
                }
                deleteYarnComponent(row);
            }
        });

        tView.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent me) {
                final JTable table = (JTable) me.getSource();
                final Point p = me.getPoint();
                final int row = table.rowAtPoint(p);
                if (row < 0) {
                    return;
                }
                if (me.getClickCount() == 2) {
                    editYarnComponent(tView.getSelectedRow());
                }
            }
        });

        cbComposite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                tfName.setEditable(cbComposite.isSelected());
            }
        });
    }

    /**
     * Инициализация компонентов на панели
     */
    private void initPane() {
        setPreferredSize(new Dimension(460, 400));

        cbpCategory = new ComboBoxPanel<>(controller, MarchReferencesType.MATERIAL_YARN_CATEGORY);
        cbpCategory.setButtonVisible(false);
        setLayout(new BorderLayout());


        JPanel pHeader = new JPanel(null);
        pHeader.setPreferredSize(new Dimension(0, 150));

        JPanel pTable = new JPanel(new BorderLayout());
        this.add(pHeader, BorderLayout.NORTH);
        this.add(pTable, BorderLayout.CENTER);

        toolBar = new UCToolBar();
        toolBar.getBtnReport().setVisible(false);
        pTable.add(toolBar, BorderLayout.NORTH);

        tView = new NiceJTable(tModel);
        tView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane spPick = new JScrollPane(tView);
        pTable.add(spPick, BorderLayout.CENTER);

        toolBar.setVisibleSearchControls(false);

        tfName.setEditable(false);

        JLabel lCode = new JLabel("Шифр артикула");
        tfCode = new JFormattedTextField(FormatUtils.getSarYarnFormat());
        JLabel lCategory = new JLabel("Категория пряжи *");

        pHeader.setLayout(new MigLayout());

        pHeader.add(lCode, "width 120:20:120");
        pHeader.add(lCategory, "wrap");
        pHeader.add(tfCode, "width 80:20:80");
        pHeader.add(cbpCategory, "width 315:20:315, height 20:20, wrap");

        pHeader.add(lName);
        pHeader.add(cbComposite, "wrap");
        pHeader.add(tfName, "span 2, width 440:20:440, wrap");

        pHeader.add(lNote, "wrap");
        pHeader.add(tfNote, "span 2, width 440:20:440, wrap");

        JPanel pFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pFooter.setPreferredSize(new Dimension(0, 20));
        pFooter.add(lPercent);
        pTable.add(pFooter, BorderLayout.SOUTH);

        TableUtils.setTableColumnWidth(tView, false);
    }

    @Override
    public void updateRights(RightEnum right) {
        toolBar.setRight(right);
    }
}
