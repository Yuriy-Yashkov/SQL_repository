package by.march8.ecs.application.modules.economists.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.preview.PreviewViewPort;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.economists.model.WarecousePriceListPreviewCellRenderer;
import by.march8.entities.classifier.RemainPriceListSearchItem;

import java.util.ArrayList;

/**
 * @author Andy 13.03.2017.
 */
public class WarehousePriceListPreviewMode extends PreviewViewPort {

    private final ArrayList<Object> data;


    public WarehousePriceListPreviewMode(MainController mainController, ArrayList<Object> array) {
        controller = mainController;
        // this.document = document ;

        frameViewPort = new FrameViewPort(mainController, MarchWindowType.DIALOG);
        frameViewPort.getFrameControl().setTitleFrame("Просмотр найденых изделий");

        frameViewPort.getFrameRegion().getToolBar().setVisible(false);
        // Инициализация грида
        gridViewPort = new GridViewPort(RemainPriceListSearchItem.class, false);
        gridViewPort.resetFilter();

        frameViewPort.getButtonControl().getOkButton().setVisible(false);
        frameViewPort.getButtonControl().getCancelButton().setText("Закрыть");

        frameViewPort.setGridViewPort(gridViewPort);
        // Получаем ссылку на модель данных грида
        data = gridViewPort.getDataModel();
        //data.addAll(document.getDetailList());

        gridViewPort.setCustomCellRender(new WarecousePriceListPreviewCellRenderer());

        data.clear();
        data.addAll(array);
        gridViewPort.updateViewPort();
        //prepareView();

        // createPreviewList(document);
        //frameViewPort.
        //frameViewPort.getFrameControl().showFrame();
        frameViewPort.getFrameControl().showFrame();
    }
}
