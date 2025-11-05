package by.march8.ecs.application.modules.references.client.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.CriteriaItem;
import by.gomel.freedev.ucframework.ucdao.model.QueryBuilder;
import by.march8.ecs.MainController;
import by.march8.entities.readonly.ContractEntityView;
import by.march8.entities.readonly.ContractorEntityView;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Andy 11.12.2015.
 */
public class ClientMode extends AbstractFunctionalMode {

    private final ArrayList<Object> contractorList;
    private RightEnum right;
    private ArrayList<Object> contractList;
    private DaoFactory factory = DaoFactory.getInstance();

    private GridViewPort contractGridViewPort;

    public ClientMode(final MainController mainController) {
        controller = mainController;
        modeName = "Справочник контрагентов March8";
        frameViewPort = new FrameViewPort(mainController, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);
        //right = controller.getRight(modeName);
        right = RightEnum.WRITE;
        frameViewPort.getFrameRegion().getToolBar().setVisible(false);

        // Инициализация гридов
        gridViewPort = new GridViewPort(ContractorEntityView.class, false);
        contractGridViewPort = new GridViewPort(ContractEntityView.class, false);

        final JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, gridViewPort, contractGridViewPort);
        splitPanel.setResizeWeight(0.6);
        splitPanel.setOneTouchExpandable(true);
        splitPanel.setContinuousLayout(true);

        frameViewPort.getFrameRegion().getCenterContentPanel().add(splitPanel);

        // Получаем ссылку на модель данных грида
        contractorList = gridViewPort.getDataModel();
        contractList = contractGridViewPort.getDataModel();

        // двойной клик на строке
        gridViewPort.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onDoubleClick(int rowIndex, int columnIndex, Object object) {
                if (right == RightEnum.WRITE) {
                    editRecord();
                }
            }

            @Override
            public void onSelectChanged(final int rowIndex, final Object object) {
                ContractorEntityView selectedItem = (ContractorEntityView) object;
                if (selectedItem != null) {
                    updateContractList((selectedItem.getId()));
                }
            }
        });

        gridViewPort.primaryInitialization();
        contractGridViewPort.primaryInitialization();

        updateContent();

        frameViewPort.getFrameControl().showFrame();
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

    @Override
    @SuppressWarnings("unchecked")
    public void updateContent() {
        DaoFactory<ContractorEntityView> factory = DaoFactory.getInstance();
        IGenericDao<ContractorEntityView> dao = factory.getGenericDao();
        try {
            contractorList.clear();
            contractorList.addAll(dao.getEntityListByNamedQuery(ContractorEntityView.class, "ContractorEntityView.findAll", null));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        frameViewPort.updateContent();
        gridViewPort.updateViewPort();
    }

    @SuppressWarnings("unchecked")
    private void updateContractList(int id) {
        DaoFactory<ContractEntityView> factory = DaoFactory.getInstance();
        IGenericDao<ContractEntityView> dao = factory.getGenericDao();

        QueryBuilder queryBuilder = new QueryBuilder(ContractEntityView.class);
        queryBuilder.setOrderBy("id");
        queryBuilder.addCriteria(new CriteriaItem(id, "contractorId", "="));

        try {
            contractList.clear();
            contractList.addAll(dao.getEntityListByQuery(ContractEntityView.class, queryBuilder.getQuery()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        contractGridViewPort.updateViewPort();
    }
}
