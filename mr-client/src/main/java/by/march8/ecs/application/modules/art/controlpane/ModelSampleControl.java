package by.march8.ecs.application.modules.art.controlpane;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.abstracts.ControlPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.entities.product.ModelSample;

import javax.swing.*;

//import by.march8.ecs.application.modules.art.reports.ModelSampleReport;

/**
 * Created by lidashka.
 */
public class ModelSampleControl extends ControlPane {
    private IReference references;
    private FrameViewPort form;

    @Override
    public void beforeEmbedding(final FrameViewPort frameViewPort) {
        references = frameViewPort.getReference();
        form = frameViewPort;

        UCToolBar toolBar = form.getFrameRegion().getToolBar();
        final JButton btnPrint = new JButton();
        btnPrint.setIcon(new ImageIcon(MainController.getRunPath() + "/res/printer.png", "Печать"));
        btnPrint.setToolTipText("Печать");
        btnPrint.addMouseListener(frameViewPort.getController().getHintListener());

        JPanel searchPanel = form.getFrameRegion().getToolBar().getSearchPanel();
        toolBar.remove(searchPanel);
        toolBar.getBtnReport().setVisible(false);
        toolBar.add(btnPrint);
        toolBar.addSeparator();
        toolBar.add(searchPanel);

        btnPrint.addActionListener(e -> {
            //new ModelSampleReport(getSelected());
        });
    }

    private ModelSample getSelected() {
        return (ModelSample) form.getGridViewPort().getSelectedItem();
    }

    @Override
    public void afterEmbedding() {

    }
}
