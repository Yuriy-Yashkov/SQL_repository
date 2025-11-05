/* FormsForm.java
 *
 * Created on 13.08.2012, 12:19:08
 */
package dept.tools;

import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDaoThread;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseDialog;
import by.march8.ecs.MainController;
import by.march8.entities.admin.ApplicationForm;
import by.march8.entities.admin.UserInformation;

import java.awt.*;
import java.util.ArrayList;

/**
 *
 * @author vova
 */
public class FormsForm extends BaseDialog {

    private MainController controller;
    private GridViewPort gridViewPort;
    private UserInformation userInformation;

    public FormsForm(final MainController mainController, UserInformation user) {
        super(mainController, new Dimension(550, 300));
        setTitle("Список форм доступных пользователю " + user.getUserLogin());
        controller = mainController;
        userInformation = user;

        gridViewPort = new GridViewPort(ApplicationForm.class, false);
        pContent.add(gridViewPort);
        final ArrayList<Object> data = gridViewPort.getDataModel();

        final DaoFactory factory = DaoFactory.getInstance();
        ICommonDaoThread dao = factory.getCommonDaoThread();

        try {
            data.clear();
            data.addAll(dao.getAllEntityThread(ApplicationForm.class));
        } catch (final Exception e) {
            e.printStackTrace();
        }

        gridViewPort.updateViewPort();

        btnSave.setText("Редактировать");
        btnCancel.setText("Закрыть");

        btnCancel.addActionListener(e -> {
            modalResult = false;
            setVisible(false);
        });

        btnSave.addActionListener(e -> {
            RightsUserForm2 dialog = new RightsUserForm2(controller, userInformation, (ApplicationForm) gridViewPort.getSelectedItem());
            if (dialog.showModal()) {
                // ПОлучаем интерфейс для работы с БД
                final ICommonDao daoCommon = factory.getCommonDao();
                try {
                    // Обновляем запись в БД
                    userInformation = (UserInformation) daoCommon.getEntityById(UserInformation.class,
                            userInformation.getId());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


}
