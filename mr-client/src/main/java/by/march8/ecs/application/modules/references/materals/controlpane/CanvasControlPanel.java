package by.march8.ecs.application.modules.references.materals.controlpane;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IEditableModule;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ControlPane;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.entities.materials.CanvasItem;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**Панель управления справочника ПОЛОТНО
 * Created by Andy on 14.04.2015.
 */
public class CanvasControlPanel extends ControlPane {

    private FrameViewPort viewPort;
    private GridViewPort gridViewPort;
    private ArrayList<Object> data;

    @Override
    public void beforeEmbedding(final FrameViewPort formViewPort) {
        viewPort = formViewPort;

        UCToolBar toolBar = viewPort.getFrameRegion().getToolBar();
        final JButton btnCopyRole = new JButton();
        btnCopyRole.setIcon(new ImageIcon(MainController.getRunPath() + "/res/copy24.png", "Копировать"));
        btnCopyRole.setToolTipText("Новое полотно на основании выбранного");
        btnCopyRole.addMouseListener(viewPort.getController().getHintListener());
        toolBar.addSeparator();
        toolBar.add(btnCopyRole);

        btnCopyRole.addActionListener(e -> createCanvasBySelectCanvas());

        toolBar.getBtnReport().setVisible(false);
    }

    @Override
    public void afterEmbedding() {
        gridViewPort = viewPort.getGridViewPort();
        data = gridViewPort.getDataModel();
    }

    private void createCanvasBySelectCanvas() {
        CanvasItem selectedItem = (CanvasItem) gridViewPort.getSelectedItem();
        if (selectedItem == null) {
            return;
        }

        String s = (String) JOptionPane.showInputDialog(
                null,
                "Создается новое полотно на основании полотна [" + selectedItem.getArticle() + "]\nВведите имя нового полотна.",
                "Новое полотно",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "Копия " + selectedItem.getArticle());

        if ((s != null) && (s.trim().length() > 0)) {

            CanvasItem newCanvasItem = new CanvasItem(selectedItem);
            newCanvasItem.setArticle(s);

            DaoFactory factory = DaoFactory.getInstance();
            ICommonDao dao = factory.getCommonDao();
            try {
                Object o = dao.saveEntity(newCanvasItem);
                viewPort.getGridViewPort().setUpdatedObject(o);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            IEditableModule module = viewPort.getReference().getEditableModule();
            module.updateContent();
        }
    }
}
