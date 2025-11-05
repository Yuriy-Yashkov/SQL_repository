package by.march8.ecs.application.modules.references.materals.controlpane;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ControlPane;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.IGridToolTipEvent;

import java.util.ArrayList;

/**
 *
 * Created by Andy on 03.03.2015.
 */
public class YarnControlPane extends ControlPane implements IGridToolTipEvent {
    private FrameViewPort viewPort;
    private GridViewPort gridViewPort;
    private ArrayList<Object> data;

    @Override
    public void beforeEmbedding(final FrameViewPort formViewPort) {
        viewPort = formViewPort;
    }

    @Override
    public void afterEmbedding() {
        gridViewPort = viewPort.getGridViewPort();
        gridViewPort.addToolTipHandler(this);
        data = gridViewPort.getDataModel();
    }

    @Override
    public String onToolTipActivated(final Object object, final int row, final int column, final int rowModel) {
       /* YarnItem yarnItem = (YarnItem)data.get(rowModel);
        if (column==1){
            return yarnItem.getComponentsAsHTML() ;
        }
        if (column==2){
            return yarnItem.getCategoryAsHTML() ;
        }
        */
        return null;
    }
}
