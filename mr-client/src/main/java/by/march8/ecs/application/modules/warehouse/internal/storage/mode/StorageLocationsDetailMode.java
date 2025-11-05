package by.march8.ecs.application.modules.warehouse.internal.storage.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.internal.storage.editor.StorageLocationsDetailItemEditor;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.storage.StorageLocationsDetailItem;
import by.march8.entities.storage.StorageLocationsDetailItemView;
import by.march8.entities.storage.StorageLocationsView;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Andy 03.11.2018 - 12:05.
 */
public class StorageLocationsDetailMode extends AbstractFunctionalMode<StorageLocationsDetailItemView> {

    private RightEnum right;
    private EditingPane editingPane;

    private StorageLocationsView document;
    private ArrayList<StorageLocationsDetailItemView> data;

    private JButton btnTerminal;
    private JPanel pFooter;
    private JPanel pSummary;
    private JLabel lblSummary;
    private UCToolBar toolBar;


    public StorageLocationsDetailMode(MainController mainController, StorageLocationsView document) {

        controller = mainController;
        modeName = "Спецификация документа по месту хранения №" + document.getDocumentNumber() + " (" + document.getNote() + ")";
        this.document = document;

        frameViewPort = new FrameViewPort(mainController, MarchWindowType.DIALOGSIMPLE);
        frameViewPort.getFrameControl().setTitleFrame(modeName);

        right = RightEnum.WRITE;//controller.getRight(modeName);
        toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.registerEvents(this);
        toolBar.setRight(right);
        toolBar.getBtnReport().setVisible(false);

        btnTerminal = new JButton();
        btnTerminal.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/scanner.png", "Принять с терминала"));
        toolBar.add(btnTerminal);

        gridViewPort = new GridViewPort<>(StorageLocationsDetailItemView.class, false);
        data = gridViewPort.getDataModel();
        frameViewPort.setGridViewPort(gridViewPort);

        pFooter = new JPanel(new MigLayout());
        pFooter.setPreferredSize(new Dimension(100, 10));

        pSummary = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pSummary.setBackground(Color.green.brighter());
        lblSummary = new JLabel("");
        pSummary.add(lblSummary);

        pFooter.add(pSummary, "width 1000:20:1000, height 20:20, wrap");

        frameViewPort.getFrameRegion().getBottomContentPanel().setLayout(new BorderLayout());
        frameViewPort.getFrameRegion().getBottomContentPanel().removeNotify();
        frameViewPort.getFrameRegion().getBottomContentPanel().add(pFooter, BorderLayout.SOUTH);
        frameViewPort.getFrameRegion().getBottomContentPanel().setPreferredSize(new Dimension(20, 10));

        frameViewPort.getButtonControl().getCancelButton().setVisible(false);
        frameViewPort.getButtonControl().getOkButton().setVisible(false);


        // Инициализация редактора

        editingPane = new StorageLocationsDetailItemEditor(frameViewPort);
        editingPane.setRight(RightEnum.WRITE);
        editingPane.setSourceEntityClass(StorageLocationsDetailItem.class);

        updateContent();

        gridViewPort.primaryInitialization();
        frameViewPort.getFrameControl().setFrameSize(new Dimension(1024, 700));
        frameViewPort.getFrameControl().showFrame();
    }

    @Override
    public void updateContent() {

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {

                DaoFactory<StorageLocationsDetailItemView> factory = DaoFactory.getInstance();
                IGenericDao<StorageLocationsDetailItemView> dao = factory.getGenericDao();
                java.util.List<QueryProperty> criteria = new ArrayList<>();
                criteria.add(new QueryProperty("document", document.getId()));
                java.util.List<StorageLocationsDetailItemView> list = null;
                data.clear();
                try {
                    list = dao.getEntityListByNamedQuery(StorageLocationsDetailItemView.class, "StorageLocationsDetailItemView.findByDocumentId", criteria);
                    data.addAll(list);
                    toolBar.updateButton(data.size());
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                lblSummary.setText("Здесь может быть Ваша реклама");
                frameViewPort.updateContent();
                return true;
            }
        }

        Task task = new Task("Получение спецификации...");
        task.executeTask();
    }

    @Override
    public void addRecord() {
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.NEW);
        editor.setParentTitle("Изделие в месте хранения");
        editingPane.setSourceEntity(null);
        editor.setEditorPane(editingPane);
        if (editor.showModal()) {


            updateContent();
        }
    }

    @Override
    public void editRecord() {

    }

    @Override
    public void deleteRecord() {

    }
}
