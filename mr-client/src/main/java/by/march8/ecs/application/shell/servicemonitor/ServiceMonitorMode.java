package by.march8.ecs.application.shell.servicemonitor;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.servicemonitor.model.ServicePanel;

import javax.swing.*;
import java.awt.*;

public class ServiceMonitorMode extends AbstractFunctionalMode {

    private JPanel pContent;
    private JScrollPane sp;

    public ServiceMonitorMode(MainController controller) {
        this.controller = controller;
        modeName = "Монитор служб";
        frameViewPort = new FrameViewPort(controller, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);
        init();
        frameViewPort.getFrameControl().showFrame();
    }

    private void init() {

        pContent = new JPanel();
        pContent.setLayout(new BoxLayout(pContent, BoxLayout.Y_AXIS));

        pContent.add(new ServicePanel());
        pContent.add(new ServicePanel());
        pContent.add(new ServicePanel());
        pContent.add(new ServicePanel());
        pContent.add(new ServicePanel());
        pContent.add(new ServicePanel());
        pContent.add(new ServicePanel());
        pContent.add(new ServicePanel());
        pContent.add(new ServicePanel());
        pContent.add(new ServicePanel());

        sp = new JScrollPane(pContent);

        sp.getVerticalScrollBar().setUnitIncrement(16);
        frameViewPort.getFrameRegion().getCenterContentPanel().add(sp, BorderLayout.CENTER);
    }

    @Override
    public void updateContent() {

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
