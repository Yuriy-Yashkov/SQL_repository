package by.march8.ecs.application.modules.references.classifier.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.uccore.viewport.preview.PreviewViewPort;
import by.march8.ecs.MainController;
import by.march8.entities.classifier.ColorSearchItem;

import java.util.ArrayList;

/**
 * @author Andy 18.10.2018 - 12:51.
 */
public class ColorSearchMode extends PreviewViewPort {

    private final ArrayList<Object> data;

    public ColorSearchMode(MainController mainController, ArrayList<Object> array) {
        controller = mainController;
        frameViewPort = new FrameViewPort(mainController, MarchWindowType.PICKFRAME);
        frameViewPort.getFrameControl().setTitleFrame("Просмотр найденых цветов");

        frameViewPort.getFrameRegion().getToolBar().setVisible(false);
        // Инициализация грида
        gridViewPort = new GridViewPort(ColorSearchItem.class, false);
        gridViewPort.resetFilter();

        frameViewPort.getButtonControl().getOkButton().setVisible(true);
        frameViewPort.getButtonControl().getCancelButton().setText("Отмена");

        frameViewPort.setGridViewPort(gridViewPort);
        // Получаем ссылку на модель данных грида
        data = gridViewPort.getDataModel();

        data.clear();
        data.addAll(array);
        gridViewPort.updateViewPort();

        gridViewPort.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onDoubleClick(int rowIndex, int columnIndex, Object object) {
                if (object != null) {
                    frameViewPort.getButtonControl().getOkButton().doClick();
                }
            }
        });
    }

    public ColorSearchItem showModal() {
        if (frameViewPort.getFrameControl().showModalFrame()) {
            return (ColorSearchItem) gridViewPort.getSelectedItem();
        } else {
            return null;
        }
    }
}
