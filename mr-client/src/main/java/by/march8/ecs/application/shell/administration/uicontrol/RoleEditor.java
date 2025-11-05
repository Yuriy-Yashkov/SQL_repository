package by.march8.ecs.application.shell.administration.uicontrol;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.ISubReferences;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.administration.subpane.SubFunctionalRole;
import by.march8.entities.admin.FunctionalRole;
import by.march8.entities.admin.UserRole;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

/**
 * @author Andy 30.10.2014.
 */
public class RoleEditor extends EditingPane {

    //  private final ArrayList<Object> data = new ArrayList<>();
    /**
     * The standards pick.
     */
    //  private final TableModel tModel = new GeneralTableModel(data, FunctionalRole.class);
    private final EditingPane editPane;

    private final JLabel lNote = new JLabel("Примечание");
    private final JTextField tfRoleName = new JTextField();
    private final JTextField tfNote = new JTextField();
    private final MainController controller;

    // private ToolBarTemplate toolBar = null;
    // private JTable tView = null;

    private UserRole source = null;
    private int selectedRow;

    //  private GridViewPort gridViewPort ;
    private IReference reference;
    private JPanel pTable;
    private ISubReferences subFunctionalRole;


    public RoleEditor(IReference reference) {
        super(reference);
        this.reference = reference;
        this.controller = reference.getMainController();
        setPreferredSize(new Dimension(600, 400));
        setLayout(new BorderLayout());
        initPane();
        //    initEvents();
        editPane = new FunctionalRoleEditor(reference);
        // TableUtils.setTableColumnWidth(tView);
    }

    @Override
    public Object getSourceEntity() {
        source.setName(tfRoleName.getText());
        source.setNote(tfNote.getText());
        source.setFunctionalRole((Set<FunctionalRole>) subFunctionalRole.getData());
        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object == null) {
            source = new UserRole();
            tfRoleName.setText("Новая роль");
            tfNote.setText("Примечание");
        } else {
            source = (UserRole) object;
            tfRoleName.setText(source.getName());
            tfNote.setText(source.getNote());
        }
        //  updateFunctionalRoleList();
        subFunctionalRole.setData(source.getFunctionalRole());
        subFunctionalRole.setSourceEntity(source);
    }

    @Override
    public boolean verificationData() {
        if (tfRoleName.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Наименование роли не должно быть пустым", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            tfRoleName.requestFocusInWindow();
            return false;
        }
        return true;
    }

    /**
     * Инициализация компонентов на панели
     */
    private void initPane() {
        JPanel pHeader = new JPanel(null);
        pHeader.setPreferredSize(new Dimension(0, 100));

        pTable = new JPanel(new BorderLayout());
        subFunctionalRole = new SubFunctionalRole(reference, pTable);
        this.add(pHeader, BorderLayout.NORTH);
        this.add(pTable, BorderLayout.CENTER);

        // toolBar = new ToolBarTemplate();
        //  pTable.add(toolBar, BorderLayout.NORTH);

        //  tView = new JTable(tModel);
        //  tView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //  new TableFilterHeader(tView, AutoChoices.ENABLED);
        //  JScrollPane spPick = new JScrollPane(tView);
        //  pTable.add(spPick, BorderLayout.CENTER);

        //  toolBar.setVisibleSearchControls(false);

        JLabel lRoleName = new JLabel("Наименование роли");


        pHeader.setLayout(new MigLayout());

        pHeader.add(lRoleName, " wrap");
        pHeader.add(tfRoleName, "width 440:20:440,wrap");
        pHeader.add(lNote, "wrap");
        pHeader.add(tfNote, "width 440:20:440, wrap");

        //   JPanel pFoother = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        //   pFoother.setPreferredSize(new Dimension(0, 20));
        //  pTable.add(pFoother, BorderLayout.SOUTH);
    }
/*

    private void initEvents() {

        toolBar.getBtnNewItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                addFunctionalRole();
            }
        });
        toolBar.getBtnEditItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (selectedRow < 0) {
                    return;
                }
                editFunctionalRole(selectedRow);
            }
        });

        toolBar.getBtnDeleteItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (selectedRow < 0) {
                    return;
                }
                deleteFunctionalRole(selectedRow);
            }
        });

        tView.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(final ListSelectionEvent event) {
                        final int viewRow = tView.getSelectedRow();
                        if (viewRow < 0) {

                        } else {
                            selectedRow = tView.convertRowIndexToModel(viewRow);
                        }
                    }
                });

        tView.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent me) {
                final JTable table = (JTable) me.getSource();
                final Point p = me.getPoint();
                final int row = table.rowAtPoint(p);
                if (selectedRow < 0) {
                    return;
                }
                if (me.getClickCount() == 2) {
                    editFunctionalRole(selectedRow);
                }
            }
        });
    }
*/
 /*   private void addFunctionalRole() {
        final BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        editPane.setSourceEntity(null);
        editor.setEditorPane(editPane);

        if (editor.showModal()) {
            FunctionalRole item = (FunctionalRole) editPane.getSourceEntity();
            item.setRole(source);
            source.getFunctionalRole().add(item);

            updateFunctionalRoleList();
        }

    }

    private void editFunctionalRole(final int selectedRow) {
        final BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        FunctionalRole item = (FunctionalRole) data.get(selectedRow);
        editPane.setSourceEntity(item);
        editor.setEditorPane(editPane);

        if (editor.showModal()) {
            data.set(selectedRow, editPane.getSourceEntity());
            updateFunctionalRoleList();
        }
    }

    private void deleteFunctionalRole(final int selectedRow) {
        final int answer = Dialogs.showDeleteDialog(MarchReferencesType.ADM_ROLE, data.get(selectedRow));
        if (answer == 0) {
            FunctionalRole item = (FunctionalRole) data.get(selectedRow);
            source.getFunctionalRole().remove(item);
            updateFunctionalRoleList();
        }
    }

    public void updateFunctionalRoleList() {
        data.clear();
        data.addAll(source.getFunctionalRole());
        ((AbstractTableModel) tModel).fireTableDataChanged();
        toolBar.setRight(right);
        toolBar.updateButton(data.size());
    }

    */

}

