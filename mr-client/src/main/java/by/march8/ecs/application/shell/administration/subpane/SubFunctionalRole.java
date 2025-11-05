package by.march8.ecs.application.shell.administration.subpane;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.ISubReferences;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucswing.focus.FocusProcessing;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.administration.uicontrol.FunctionalRoleEditor;
import by.march8.ecs.framework.sdk.reference.abstracts.SubReferences;
import by.march8.entities.admin.FunctionMode;
import by.march8.entities.admin.FunctionalRole;
import by.march8.entities.admin.UserRight;
import by.march8.entities.admin.UserRole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author Andy 23.12.14.
 */
public class SubFunctionalRole extends SubReferences {

    private UserRole source;
    private JButton btnCheckList = new JButton();

    public SubFunctionalRole(IReference reference, Container container) {
        super(reference, container, FunctionalRole.class);
        editPane = new FunctionalRoleEditor(reference);
        editPane.setRight(reference.getRight());

        FocusProcessing fp = new FocusProcessing();
        fp.setBorderColor(editPane);
        updateContent();
        btnCheckList.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/checklist.png", "Check List"));
        btnCheckList.setToolTipText("Выбрать из справочника функциональных режимов");
        toolBar.removeAll();
        toolBar.add(btnCheckList);
        initEvents();
    }


    @Override
    public void setSourceEntity(Object object) {
        source = (UserRole) object;
    }

    @Override
    public void updateEntity(Object object) {
        ((FunctionalRole) object).setRole(source);
    }


    private void initEvents() {
        btnCheckList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                customModeSelect();
            }
        });
    }

    private void customModeSelect() {
        ArrayList<Object> functionalModeData = ((FunctionalRoleEditor) editPane).getFunctionalModeData();

        ArrayList<Object> dataArray = new ArrayList<Object>();
        for (Object o : functionalModeData) {
            FunctionMode mode = (FunctionMode) o;
            dataArray.add(new FunctionalRole(-1, source, mode, null, ""));
        }

        FunctionModePicker picker = new FunctionModePicker(controller);
        picker.loadDataModel(dataArray);

        // Формируем коллекцию режимов, существующих у данной роли
        dataArray = new ArrayList<Object>();
        for (Object o : source.getFunctionalRole()) {
            FunctionalRole role = (FunctionalRole) o;
            if (role.getFunctionMode() != null) {
                dataArray.add(new FunctionalRole(role.getId(), source, role.getFunctionMode(), role.getRight(), role.getNote()));
            }
        }

        if (picker.preset(dataArray)) {
            Set<Object> tempData = picker.getCheckList();
            final DaoFactory factory = DaoFactory.getInstance();
            final ICommonDao dao = factory.getCommonDao();

            UserRight userRight;
            try {
                userRight = (UserRight) dao.getEntityById(UserRight.class, 1);
            } catch (SQLException e1) {
                userRight = new UserRight(1, "ТОЛЬКО ЧТЕНИЕ", "Ограниченные права");
            }

            for (Object o : tempData) {
                FunctionalRole role = (FunctionalRole) o;
                if (role.getId() == -1) {
                    role.setId(0);
                }

                if (role.getRight() == null) {
                    role.setRight(userRight);
                }
            }
            ArrayList<Object> preSaveData = gridViewPort.getDataModel();
            preSaveData.clear();
            preSaveData.addAll(tempData);
            source.setFunctionalRole((Set<FunctionalRole>) ((ISubReferences) this).getData());
            /*try {
                dao.updateDocument(source);
                source = (UserRole)dao.getEntityById(UserRole.class, source.getId());
                setData(source.getFunctionalRole());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            */
            gridViewPort.updateViewPort();
        }
    }

}
