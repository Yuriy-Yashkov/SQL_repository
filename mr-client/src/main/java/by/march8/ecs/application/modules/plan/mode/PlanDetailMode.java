package by.march8.ecs.application.modules.plan.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDaoThread;
import by.gomel.freedev.ucframework.ucdao.model.CriteriaItem;
import by.gomel.freedev.ucframework.ucdao.model.QueryBuilder;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.entities.plan.PlanDetalization;
import by.march8.entities.plan.PlanItem;

import java.util.ArrayList;

/**
 * @author Andy 24.03.2016.
 */
public class PlanDetailMode extends AbstractFunctionalMode {
    private final ArrayList<Object> data;
    private MainController controller;
    private PlanItem plan;

    public PlanDetailMode(MainController mainController, PlanItem plan) {
        controller = mainController;
        this.plan = plan;
        modeName = "Детализация плана для " + plan.getAssortmentName() + " (арт. " + plan.getArticle() + ", модель " + plan.getModel() + ")";

        frameViewPort = new FrameViewPort(mainController, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);

        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.setVisible(false);

        // Инициализация грида
        gridViewPort = new GridViewPort(PlanDetalization.class, false);
        // Получаем ссылку на модель данных грида
        data = gridViewPort.getDataModel();
        frameViewPort.setGridViewPort(gridViewPort);


        updateContent();

        gridViewPort.primaryInitialization();
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
    public void updateContent() {
        QueryBuilder query = new QueryBuilder(PlanDetalization.class);

        query.addCriteria(new CriteriaItem(plan.getId(), "planItem", "="));
        System.out.println(query.getQuery());

        DaoFactory factory = DaoFactory.getInstance();
        ICommonDaoThread dao = factory.getCommonDaoThread();

        data.clear();

        try {
            data.addAll(dao.getAllEntityByStringQueryThread(PlanDetalization.class, query.getQuery()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        gridViewPort.updateViewPort();
    }
}
