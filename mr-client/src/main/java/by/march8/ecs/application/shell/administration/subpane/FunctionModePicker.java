package by.march8.ecs.application.shell.administration.subpane;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.ICustomCellEditor;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IReferencesDao;
import by.march8.ecs.MainController;
import by.march8.entities.admin.FunctionMode;
import by.march8.entities.admin.FunctionalRole;
import by.march8.entities.admin.UserRight;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author Andy 20.03.2015.
 */
public class FunctionModePicker implements ICustomCellEditor {
    private final MainController controller;
    private FrameViewPort frameViewPort;

    private GridViewPort gridViewPort;

    private ArrayList<Object> data;
    private ArrayList<Object> checkList;

    public FunctionModePicker(MainController mainController) {
        controller = mainController;
        frameViewPort = new FrameViewPort(controller, MarchWindowType.PICKCHECK);
        frameViewPort.getFrameControl().setTitleFrame("Функциональные режимы");

        gridViewPort = new GridViewPort(FunctionalRole.class, true);
        gridViewPort.setCustomCellEditor(this);

        frameViewPort.setGridViewPort(gridViewPort);
        frameViewPort.getFrameRegion().getToolBar().setVisible(false);
        data = gridViewPort.getDataModel();
    }

    public boolean preset(ArrayList<Object> data_) {
        ArrayList<Object> array = gridViewPort.getDataModel();
        for (Object o : data_) {
            FunctionalRole roleSource = (FunctionalRole) o;
            FunctionMode modeSource = roleSource.getFunctionMode();
            for (Object anArray : array) {
                FunctionalRole roleDest = (FunctionalRole) anArray;
                FunctionMode modeDest = roleDest.getFunctionMode();
                if (modeDest.getNameEng().trim().equals(modeSource.getNameEng().trim())) {
                    roleDest.setId(roleSource.getId());
                    roleDest.setNote(roleSource.getNote());
                    roleDest.setRight(roleSource.getRight());
                    roleDest.setFunctionMode(roleSource.getFunctionMode());
                }
            }
        }
        gridViewPort.preset(data_);
        return frameViewPort.getFrameControl().showModalFrame();
    }

    @SuppressWarnings("all")
    public Set<Object> getCheckList() {

        return gridViewPort.getSelectedItems();
    }

    public void loadDataModel(ArrayList<Object> data_) {
        data.clear();
        data.addAll(data_);
        frameViewPort.updateContent();
    }

    public void showModal() {
        frameViewPort.getFrameControl().showModalFrame();
    }

    @Override
    public void initialCellEditor(final TableColumnModel columnModel) {
        JComboBox<UserRight> cbUserRight = new JComboBox<UserRight>();
        final DaoFactory factory = DaoFactory.getInstance();
        final IReferencesDao dao = factory.getReferencesDao();
        dao.loadComboBoxData(cbUserRight, UserRight.class);
        TableColumn column = columnModel.getColumn(2);
        column.setCellEditor(new MyComboBoxEditor(cbUserRight));
    }

    @Override
    public boolean isCellEditable(final int column) {
        return column == 1;
    }

    @Override
    public void setValueAt(final int columnIndex, final Object sourceValue, final Object changeValue) {
        if (columnIndex == 2) {
            FunctionalRole role = (FunctionalRole) sourceValue;
            UserRight right = (UserRight) changeValue;
            role.setRight(right);
        }
    }

    @Override
    public boolean onEndOfTable(JTable table) {
        return true;
    }

    class MyComboBoxEditor extends DefaultCellEditor {
        public MyComboBoxEditor(JComboBox component) {
            super(component);
        }
    }
}
