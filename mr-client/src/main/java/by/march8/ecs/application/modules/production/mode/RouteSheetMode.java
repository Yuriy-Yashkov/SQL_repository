package by.march8.ecs.application.modules.production.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractSelectableFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ButtonActionAdapter;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.entities.production.RouteSheet;
import common.DateUtils;

import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by lidashka on 18.10.2018.
 */
public class RouteSheetMode extends AbstractSelectableFunctionalMode {

    private final ArrayList<Object> dataList;
    private RightEnum right;
    private Date dateBegin;
    private Date dateEnd;
    private List<Integer> idList;
    private TableRowSorter<TableModel> sorter;

    public RouteSheetMode(final MainController mainController, Date dateBegin, Date dateEnd, List<Integer> idList) {
        controller = mainController;

        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        this.idList = idList;

        modeName = "м/л за период с "
                + DateUtils.getNormalDateFormat(this.dateBegin)
                + " по "
                + DateUtils.getNormalDateFormat(this.dateEnd);

        frameViewPort = new FrameViewPort(mainController, MarchWindowType.PICKFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);
        frameViewPort.getFrameControl().setFrameSize(new Dimension(500, 600));

        right = RightEnum.READ;

        UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.getBtnNewItem().setVisible(false);
        toolBar.getBtnEditItem().setVisible(false);
        toolBar.getBtnDeleteItem().setVisible(false);
        toolBar.getBtnViewItem().setVisible(false);
        toolBar.setVisibleSearchControls(true);

        // Инициализация гридов
        gridViewPort = new GridViewPort(RouteSheet.class, true);

        frameViewPort.getFrameRegion().getCenterContentPanel().add(gridViewPort);

        // Получаем ссылку на модель данных грида
        dataList = gridViewPort.getDataModel();

        initEvents();

        initialGridSorter();

        gridViewPort.primaryInitialization();
        updateContent();
    }

    private void initEvents() {
        // Получаем ссылку на фрэйм и вешаем слушатель на кнопку ВЫБРАТЬ
        // Так, если из классификатора не будет выбрано ни одной записи - диалог не закроется
        BasePickDialog frame = (BasePickDialog) frameViewPort.getFrame();
        frame.setButtonAction(new ButtonActionAdapter() {
            @Override
            public boolean canSave() {
                Set<Object> objects = gridViewPort.getSelectedItems();
                // Если пользователь ничего не выбрал
                //return (gridViewPort.getSelectedItem() != null);
                return (gridViewPort.getSelectedItems() != null);
            }
        });
    }

    private void initialGridSorter() {
        sorter = new TableRowSorter<>(gridViewPort.getTableModel());
        sorter.setSortsOnUpdates(true);
        gridViewPort.getTable().setRowSorter(sorter);
    }

    @Override
    public void updateContent() {
        DaoFactory<RouteSheet> factory = DaoFactory.getInstance();
        IGenericDao<RouteSheet> dao = factory.getGenericDao();
        java.util.List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("date_start", dateBegin));
        criteria.add(new QueryProperty("date_end", dateEnd));
        criteria.add(new QueryProperty("idLists", idList));

        try {
            dataList.clear();
            dataList.addAll(dao.getEntityListByNamedQuery(RouteSheet.class, "RouteSheet.findByDateAndIdList", criteria));

        } catch (final Exception e) {
            e.printStackTrace();
        }

        frameViewPort.updateContent();
        gridViewPort.updateViewPort();
    }

    @Override
    public Object showSelectModal(final Object presetObject) {
        gridViewPort.resetFilter();

        if (frameViewPort.getFrameControl().showModalFrame())
            return gridViewPort.getSelectedItems();

        return null;
    }
}
