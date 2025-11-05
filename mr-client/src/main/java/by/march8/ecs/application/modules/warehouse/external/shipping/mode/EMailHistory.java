package by.march8.ecs.application.modules.warehouse.external.shipping.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.entities.warehouse.VEMailHistory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy 15.11.2017.
 */
public class EMailHistory extends AbstractFunctionalMode {

    private ArrayList<Object> dataList;


    public EMailHistory(final MainController mainController) {
        controller = mainController;
        modeName = "История отправки электронных накладных ";

        frameViewPort = new FrameViewPort(mainController, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);
        //frameViewPort.getFrameControl().setFrameSize(new Dimension(800,400));
//        frameViewPort.getButtonControl().getOkButton().setVisible(false);
//        frameViewPort.getButtonControl().getCancelButton().setText("Закрыть");


        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.setVisible(false);

        toolBar.getBtnDeleteItem().setVisible(false);
        toolBar.getBtnNewItem().setVisible(false);
        toolBar.getBtnEditItem().setVisible(false);


        toolBar.add(new Box(BoxLayout.X_AXIS));
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBar.setPreferredSize(new Dimension(0, 47));

        // Инициализация гридов
        gridViewPort = new GridViewPort(VEMailHistory.class, false);

        // Получаем ссылку на модель данных грида
        dataList = gridViewPort.getDataModel();

        frameViewPort.setGridViewPort(gridViewPort);
        gridViewPort.primaryInitialization();

        updateContent();

        frameViewPort.getFrameControl().showFrame();
    }

    @Override
    public void updateContent() {
        DaoFactory<VEMailHistory> factory = DaoFactory.getInstance();
        IGenericDao<VEMailHistory> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("type", 10));

        try {
            dataList.clear();
            dataList.addAll(dao.getEntityListByNamedQuery(VEMailHistory.class, "VEMailHistory.findByDocument", criteria));

        } catch (final Exception e) {
            e.printStackTrace();
        }
        frameViewPort.updateContent();
        gridViewPort.updateViewPort();
    }

    @Override
    public void addRecord() {

    }

    @Override
    public void editRecord() {

    }

    @Override
    public void deleteRecord() {

    }
}
