package by.march8.ecs.application.modules.production.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.seamstress.SeamstressMapDetailItem;
import by.march8.entities.seamstress.SeamstressMapDocument;
import common.UtilFunctions;
import dept.production.zsh.zplata.Item;
import dept.production.zsh.zplata.UtilZPlata;
import dept.production.zsh.zplata.ZPlataPDB;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * @author Developer on 04.12.2019 9:48
 */
public class SeamstressProductionDetailMode extends AbstractFunctionalMode {

    private EditingPane editingPane;

    private SeamstressMapDocument document;
    private List<SeamstressMapDetailItem> data;
    private GridViewPort<SeamstressMapDetailItem> gvDetails;


    private ZPlataPDB zpdb;

    private JPanel pContent;

    private JComboBox dept;
    private JComboBox brig;
    private JComboBox brigadir;
    private Vector dataBrigadir;


    public SeamstressProductionDetailMode(MainController mainController, SeamstressMapDocument document) {

        controller = mainController;
        modeName = "Спецификация документа выработки швеи № " + document.getDocumentNumber() + " от " + DateUtils.getNormalDateFormat(document.getDocumentDate());
        this.document = document;

        frameViewPort = new FrameViewPort(mainController, MarchWindowType.DIALOGSIMPLE);
        frameViewPort.getFrameControl().setTitleFrame(modeName);

        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.registerEvents(this);

        toolBar.getBtnReport().setVisible(false);

        gvDetails = new GridViewPort<>(SeamstressMapDetailItem.class, false);
        data = gvDetails.getDataModel();
        frameViewPort.setGridViewPort(gvDetails);


        frameViewPort.getFrameRegion().getBottomContentPanel().setLayout(new BorderLayout());
        frameViewPort.getFrameRegion().getBottomContentPanel().removeNotify();
        frameViewPort.getFrameRegion().getBottomContentPanel().setPreferredSize(new Dimension(20, 100));

        frameViewPort.getButtonControl().getCancelButton().setVisible(false);
        frameViewPort.getButtonControl().getOkButton().setVisible(false);

        prepareEmployees();
        initComponents();
        presetUIData();
        initEvents();
        //updateContent();

        gvDetails.primaryInitialization();
        frameViewPort.getFrameControl().setFrameSize(new Dimension(1024, 700));
        frameViewPort.getFrameControl().showFrame();
    }

    private void initEvents() {

    }

    private void initComponents() {

        dept = new JComboBox(UtilZPlata.DEPT_MODEL);
        brig = new JComboBox(UtilZPlata.BRIG_MODEL);
        brigadir = new JComboBox(UtilZPlata.BRIGADIR_MODEL);

        frameViewPort.getFrameRegion().getTopContentPanel().setLayout(new BorderLayout());

        pContent = new JPanel(new MigLayout());
        frameViewPort.getFrameRegion().getTopContentPanel().add(pContent, BorderLayout.CENTER);
        pContent.add(new JLabel("Цех"), "align left");
        pContent.add(dept, "height 20:20, width 200:20:200");

        pContent.add(new JLabel("Бригада"), "align left");
        pContent.add(brig, "height 20:20, width 200:20:200");

        pContent.add(new JLabel("Бригадир"), "align left");
        pContent.add(brigadir, "height 20:20, width 200:20:200, wrap");

        dept.addActionListener(a -> deptActionPerformed(a));
        brig.addActionListener(a -> brigActionPerformed(a));
        brigadir.addActionListener(a -> brigadirActionPerformed(a));
    }

    @Override
    public void updateContent() {
        DaoFactory<SeamstressMapDetailItem> factory = DaoFactory.getInstance();
        IGenericDao<SeamstressMapDetailItem> dao = factory.getGenericDao();

        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("document", document.getId()));
        List<SeamstressMapDetailItem> list = null;
        data.clear();
        try {
            list = dao.getEntityListByNamedQuery(SeamstressMapDetailItem.class, "CutCardDocumentDetailView.findByDocumentId", criteria);
            data.addAll(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        frameViewPort.updateContent();
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


    private void brigActionPerformed(ActionEvent evt) {
        try {
            UtilZPlata.BRIG_SELECT_ITEM = ((Item) brig.getSelectedItem()).getId();
            UtilFunctions.setSettingPropFile(String.valueOf(((Item) brig.getSelectedItem()).getId()), UtilZPlata.SETTING_BRIG_SELECT_ITEM);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void brigadirActionPerformed(ActionEvent evt) {
      /*  try {
            UtilZPlata.BRIGADIR_SELECT_ITEM = ((Item) UtilZPlata.BRIGADIR_MODEL.get(brigadir.getSelectedIndex())).getId();
            UtilFunctions.setSettingPropFile(String.valueOf(((Item) UtilZPlata.BRIGADIR_MODEL.get(brigadir.getSelectedIndex())).getId()), UtilZPlata.SETTING_BRIGADIR_SELECT_ITEM);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        */
    }


    private void deptActionPerformed(ActionEvent evt) {
        dataBrigadir = new Vector();
        try {
            UtilZPlata.DEPT_SELECT_ITEM = ((Item) dept.getSelectedItem()).getId();
            UtilFunctions.setSettingPropFile(String.valueOf(((Item) dept.getSelectedItem()).getId()), UtilZPlata.SETTING_DEPT_SELECT_ITEM);


            try {
                zpdb = new ZPlataPDB();
                class Task extends BackgroundTask {
                    public Task(final String messageText) {
                        super(messageText);
                    }

                    @Override
                    protected Boolean doInBackground() throws Exception {
                        dataBrigadir = zpdb.getBrigadir(((Item) dept.getSelectedItem()).getId());
                        return true;
                    }
                }
                Task task = new Task("Формирование инвентаризационной описи...");
                task.executeTask();
            } catch (Exception e) {
                dataBrigadir = new Vector();
                JOptionPane.showMessageDialog(null, "Ошибка поиска cписка бригадиров! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                zpdb.disConn();
            }

            brigadir.removeAllItems();

            UtilZPlata.BRIGADIR_MODEL = new Vector();
            UtilZPlata.fullModel(UtilZPlata.BRIGADIR_MODEL, dataBrigadir);

            for (Iterator it = UtilZPlata.BRIGADIR_MODEL.iterator(); it.hasNext(); )
                brigadir.addItem(it.next());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }


    private void prepareEmployees() {

        try {
            UtilZPlata.DEPT_SELECT_ITEM = UtilFunctions.readPropFile(UtilZPlata.SETTING_DEPT_SELECT_ITEM);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            UtilZPlata.BRIG_SELECT_ITEM = UtilFunctions.readPropFile(UtilZPlata.SETTING_BRIG_SELECT_ITEM);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            UtilZPlata.BRIGADIR_SELECT_ITEM = UtilFunctions.readPropFile(UtilZPlata.SETTING_BRIGADIR_SELECT_ITEM);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }


        try {
            zpdb = new ZPlataPDB();
            Vector data = zpdb.getDept();

            UtilZPlata.DEPT_MODEL = new Vector();
            UtilZPlata.fullModel(UtilZPlata.DEPT_MODEL, data);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Список подразделений не загружен! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            zpdb.disConn();
        }

        try {
            zpdb = new ZPlataPDB();
            Vector data = zpdb.getBrig();

            UtilZPlata.BRIG_MODEL = new Vector();
            UtilZPlata.fullModel(UtilZPlata.BRIG_MODEL, data);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Список бригад не загружен! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            zpdb.disConn();
        }

        try {
            zpdb = new ZPlataPDB();
            Vector data = zpdb.getBrigadir(UtilZPlata.DEPT_SELECT_ITEM);

            UtilZPlata.BRIGADIR_MODEL = new Vector();
            UtilZPlata.fullModel(UtilZPlata.BRIGADIR_MODEL, data);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Список бригадиров не загружен! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            zpdb.disConn();
        }


    }

    private void presetUIData() {
        try {
            dept.setSelectedItem(UtilZPlata.getIndexModel(UtilZPlata.DEPT_MODEL, UtilZPlata.DEPT_SELECT_ITEM));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            brig.setSelectedItem(UtilZPlata.getIndexModel(UtilZPlata.BRIG_MODEL, UtilZPlata.BRIG_SELECT_ITEM));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        try {
            brigadir.setSelectedItem(UtilZPlata.getIndexModel(UtilZPlata.BRIGADIR_MODEL, UtilZPlata.BRIGADIR_SELECT_ITEM));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка! " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }
}
