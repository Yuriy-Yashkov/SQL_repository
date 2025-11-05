package dept.tools.imgmanager;

import by.gomel.freedev.ucframework.ucswing.iframe.BaseInternalFrame;
import by.march8.ecs.MainController;

public class ImageManagerForm extends BaseInternalFrame {

    public ImageManagerForm(final MainController controller) {
        super(controller);
        SearchPanel sPanel = new SearchPanel(controller.getMainForm());
        panelContent.add(sPanel);
        toolBar.setVisible(false);
        this.setTitle("Управление фотографиями");
    }

}
