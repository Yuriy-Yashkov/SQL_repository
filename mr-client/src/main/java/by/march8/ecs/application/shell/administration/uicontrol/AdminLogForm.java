package by.march8.ecs.application.shell.administration.uicontrol;

import by.gomel.freedev.ucframework.uccore.utils.TableUtils;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.branch.NiceJTable;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.model.GeneralTableModel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.entities.admin.VAdmLog;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;


/**
 * @author Andy 29.10.2014.
 */
public class AdminLogForm extends by.gomel.freedev.ucframework.ucswing.iframe.BaseInternalFrame {

    private final MainController controller;
    private NiceJTable tView;

    private ArrayList<Object> data;

    private String instanceName = "admin_action_monitor";

    @SuppressWarnings("unchecked")
    public AdminLogForm(final MainController controller, ArrayList<?> data) {
        super(controller);
        this.data = (ArrayList<Object>) data;
        this.controller = controller;
        toolBar.setVisibleSearchControls(false);

        initFrame();
        initEvents();
        controller.getPersonalization().loadState(instanceName, tView);
    }

    private void initFrame() {
        setTitle("Монитор событий");
        panelCenter.setLayout(new BorderLayout());

        TableModel tModel = new GeneralTableModel(data, VAdmLog.class);

        tView = new NiceJTable();
        TableFilterHeader filterHeader = new TableFilterHeader(tView, AutoChoices.ENABLED);
        filterHeader.setPosition(TableFilterHeader.Position.INLINE);
        tView.setModel(tModel);
        tView.setName("tView");
        TableUtils.setMultiHeader(tView, VAdmLog.class);
        filterHeader.setSelectionBackground(Color.lightGray);
        filterHeader.setSelectionForeground(Color.BLACK);

        toolBar.getBtnNewItem().setEnabled(false);
        toolBar.getBtnEditItem().setEnabled(false);
        toolBar.getBtnDeleteItem().setEnabled(false);
        toolBar.getBtnViewItem().setEnabled(false);
        JScrollPane spView = new JScrollPane(tView);
        panelCenter.add(spView, BorderLayout.CENTER);
    }

    private void initEvents() {
        this.addInternalFrameListener(new InternalFrameListener() {
            @Override
            public void internalFrameOpened(final InternalFrameEvent e) {

            }

            @Override
            public void internalFrameClosing(final InternalFrameEvent e) {
                controller.getPersonalization().saveState(instanceName, tView);
            }

            @Override
            public void internalFrameClosed(final InternalFrameEvent e) {

            }

            @Override
            public void internalFrameIconified(final InternalFrameEvent e) {

            }

            @Override
            public void internalFrameDeiconified(final InternalFrameEvent e) {

            }

            @Override
            public void internalFrameActivated(final InternalFrameEvent e) {

            }

            @Override
            public void internalFrameDeactivated(final InternalFrameEvent e) {

            }
        });
    }

    public NiceJTable getTView() {
        return tView;
    }

    public UCToolBar getToolBar() {
        return toolBar;
    }
}

