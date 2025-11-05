package by.march8.ecs.application.modules.production.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.entities.production.CutCardDocumentDetailView;
import by.march8.entities.production.CutCardDocumentView;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Developer on 04.12.2019 9:48
 */
public class CutCardDetailMode extends AbstractFunctionalMode {

    private EditingPane editingPane;

    private CutCardDocumentView document;
    private List<CutCardDocumentDetailView> data;
    private GridViewPort<CutCardDocumentDetailView> gvDetails;


    public CutCardDetailMode(MainController mainController, CutCardDocumentView document) {

        controller = mainController;
        modeName = "Спецификация карты кроя № " + document.getDocumentNumber() + " от " + DateUtils.getNormalDateFormat(document.getDocumentDate());
        this.document = document;

        frameViewPort = new FrameViewPort(mainController, MarchWindowType.DIALOGSIMPLE);
        frameViewPort.getFrameControl().setTitleFrame(modeName);

        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.registerEvents(this);

        toolBar.getBtnReport().setVisible(false);

        gvDetails = new GridViewPort<>(CutCardDocumentDetailView.class, false);
        data = gvDetails.getDataModel();
        frameViewPort.setGridViewPort(gvDetails);

        frameViewPort.getFrameRegion().getBottomContentPanel().setLayout(new BorderLayout());
        frameViewPort.getFrameRegion().getBottomContentPanel().removeNotify();
        frameViewPort.getFrameRegion().getBottomContentPanel().setPreferredSize(new Dimension(20, 100));

        frameViewPort.getButtonControl().getCancelButton().setVisible(false);
        frameViewPort.getButtonControl().getOkButton().setVisible(false);


        initEvents();
        updateContent();

        gvDetails.primaryInitialization();
        frameViewPort.getFrameControl().setFrameSize(new Dimension(1024, 700));
        frameViewPort.getFrameControl().showFrame();
    }

    private void initEvents() {

    }

    @Override
    public void updateContent() {
        DaoFactory<CutCardDocumentDetailView> factory = DaoFactory.getInstance();
        IGenericDao<CutCardDocumentDetailView> dao = factory.getGenericDao();

        java.util.List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("document", document.getId()));
        java.util.List<CutCardDocumentDetailView> list = null;
        data.clear();
        try {
            list = dao.getEntityListByNamedQuery(CutCardDocumentDetailView.class, "CutCardDocumentDetailView.findByDocumentId", criteria);
            data.addAll(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        frameViewPort.updateContent();
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
