package by.march8.ecs.application.modules.planning.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDaoGUI;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.planning.editor.ProductionPlanningEditor;
import by.march8.ecs.application.modules.planning.manager.ProductPlanningManager;
import by.march8.entities.planning.ProductionPlanningEntity;
import by.march8.entities.planning.ProductionPlanningView;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Andy 27.11.2018 - 7:45.
 */
public class ProductionPlanningMode extends AbstractFunctionalMode<ProductionPlanningView> {

    private UCToolBar toolBar;
    private List<ProductionPlanningView> data;
    private UCDatePeriodPicker datePeriodPicker;

    private JButton btnUpdate;
    private JButton btnRest;
    private ProductPlanningManager manager;

    private EditingPane editingPane;

    public ProductionPlanningMode(MainController controller) {
        this.controller = controller;
        modeName = "Журнал планирования производства";
        frameViewPort = new FrameViewPort(this.controller, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);

        toolBar = frameViewPort.getFrameRegion().getToolBar();

        gridViewPort = new GridViewPort<>(ProductionPlanningView.class);
        data = gridViewPort.getDataModel();

        initializeComponents();
        initEvents();
        updateContent();

        gridViewPort.primaryInitialization();
        frameViewPort.getFrameControl().showFrame();
    }

    private void initializeComponents() {
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBar.setPreferredSize(new Dimension(0, 47));

        toolBar.setRight(RightEnum.WRITE);
        toolBar.getBtnReport().setVisible(false);
        toolBar.getBtnNewItem().setEnabled(true);
        toolBar.getBtnEditItem().setEnabled(true);

        btnUpdate = new JButton("Обновить");
        btnUpdate.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/update_data.png", "Обновить данные"));
        btnUpdate.setToolTipText("Обновить данные");

        btnRest = new JButton("");
        btnRest.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/update_data.png", "Обновить данные"));
        btnRest.setToolTipText("Обновить данные");

        datePeriodPicker = new UCDatePeriodPicker();
        datePeriodPicker.setName("datePeriodPicker");
        datePeriodPicker.setDatePickerEnd(new Date());
        datePeriodPicker.setDatePickerBegin(DateUtils.getDateByStringValue("01.01.2018"));

        datePeriodPicker.setEditable(true);

        toolBar.addSeparator();
        toolBar.add(datePeriodPicker);
        toolBar.add(btnRest);
        //toolBar.add(btnUpdate);

        toolBar.add(new Box(BoxLayout.X_AXIS));
        manager = new ProductPlanningManager();

        controller.getPersonalization().getPersonalSettings(this, datePeriodPicker);
        datePeriodPicker.setDatePickerEnd(new Date());
        frameViewPort.setGridViewPort(gridViewPort);

        editingPane = new ProductionPlanningEditor(frameViewPort);
        editingPane.setRight(RightEnum.WRITE);
        editingPane.setSourceEntityClass(ProductionPlanningEntity.class);
    }

    private void initEvents() {
        toolBar.registerEvents(this);

        btnUpdate.addActionListener(a -> updateContent());

        gridViewPort.setTableEventHandler(new TableEventAdapter<ProductionPlanningView>() {
            @Override
            public void onDoubleClick(int rowIndex, int columnIndex, ProductionPlanningView item) {
                viewDetail(item);
            }
        });

        datePeriodPicker.addOnChangeAction(a -> {
            updateContent();
            controller.getPersonalization().setPersonalSettings(this, datePeriodPicker);
        });

        btnRest.addActionListener(a -> {
            getFromRest();
        });
    }

    private void getFromRest() {
        String urlString = "http://localhost:8080/greeting/table/";
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(conn.getInputStream());
            NodeList nodeList = document.getDocumentElement().getChildNodes();

/*            for(int i = 0; i < nodeList.getLength(); ++i) {
                Node curr = nodeList.item(i);
                if(curr.getNodeType() == 1) {
                    Element elem = (Element)curr;
                    int id = Integer.parseInt(curr.getAttributes().getNamedItem("Id").getNodeValue());
                    String name = elem.getElementsByTagName("CharCode").item(0).getChildNodes().item(0).getNodeValue();
                    Float rate = Float.valueOf(Float.parseFloat(elem.getElementsByTagName("Rate").item(0).getChildNodes().item(0).getNodeValue()));
                    if(name.equals("RUB") || name.equals("UAH") || name.equals("USD") || name.equals("EUR")) {
                        currList.add(new Currency(id, name, rate.floatValue()));
                    }
                }
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }


/*

        System.out.print(RateGetter.getTimeStampNow() + "Get currency rates...");

        for(int i = 0; i < nodeList.getLength(); ++i) {
            Node curr = nodeList.item(i);
            if(curr.getNodeType() == 1) {
                Element elem = (Element)curr;
                int id = Integer.parseInt(curr.getAttributes().getNamedItem("Id").getNodeValue());
                String name = elem.getElementsByTagName("CharCode").item(0).getChildNodes().item(0).getNodeValue();
                Float rate = Float.valueOf(Float.parseFloat(elem.getElementsByTagName("Rate").item(0).getChildNodes().item(0).getNodeValue()));
                if(name.equals("RUB") || name.equals("UAH") || name.equals("USD") || name.equals("EUR")) {
                    this.currList.add(new Currency(id, name, rate.floatValue()));
                }
            }
        }

        System.out.println("OK");
        Iterator var12 = this.currList.iterator();

        while(var12.hasNext()) {
            Currency var13 = (Currency)var12.next();
            System.out.println(RateGetter.getTimeStampNow() + var13.toString());
        }*/


    }

    @Override
    public void updateContent() {
        DaoFactory<ProductionPlanningView> factory = DaoFactory.getInstance();
        IGenericDaoGUI<ProductionPlanningView> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("dateBegin", datePeriodPicker.getDatePickerBegin()));
        criteria.add(new QueryProperty("dateEnd", datePeriodPicker.getDatePickerEnd()));

        try {
            data.clear();
            data.addAll(dao.getEntityListByNamedQueryGUI(ProductionPlanningView.class, "ProductionPlanningView.findByPeriod", criteria));
        } catch (final Exception e) {
            e.printStackTrace();
        }

        frameViewPort.updateContent();
        gridViewPort.updateViewPort();
    }

    private void viewDetail(ProductionPlanningView item) {
        new ProductionPlanningDetailMode(controller, manager, item);
    }

    @Override
    public void addRecord() {
        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.NEW);
        editor.setParentTitle("Документ планирования производства");
        editingPane.setSourceEntity(null);
        // Для созданного пустого диалога устанавливаем панель редактирования
        editor.setEditorPane(editingPane);
        // Модально показываем форму и ожидаем закрытия
        if (editor.showModal()) {
            // Форма закрыта со значением true
            // Получаем DAO слой
            final DaoFactory factory = DaoFactory.getInstance();
            // ПОлучаем интерфейс для работы с БД
            final ICommonDao dao = factory.getCommonDao();
            try {
                // Сохраняем сущность в БД
                Object o = dao.saveEntity(editingPane.getSourceEntity());
                gridViewPort.setUpdatedObject(o);
            } catch (final SQLException e) {
                e.printStackTrace();
            }
            // ПОсле сохранения данных обновляем содержимое грида
            updateContent();
        }
    }

    @Override
    public void editRecord() {
        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        editor.setParentTitle("Документ планирования производства");

        final DaoFactory factory = DaoFactory.getInstance();
        final ICommonDao dao = factory.getCommonDao();
        ProductionPlanningEntity editableItem = null;
        try {
            editableItem = (ProductionPlanningEntity) dao.getEntityById(ProductionPlanningEntity.class, gridViewPort.getSelectedItem().getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (editableItem == null) {
            return;
        }

        editingPane.setSourceEntity(editableItem);
        editor.setEditorPane(editingPane);
        if (editor.showModal()) {
            try {
                Object o = editingPane.getSourceEntity();
                dao.updateEntity(o);
                gridViewPort.setUpdatedObject(o);
            } catch (final SQLException e) {
                e.printStackTrace();
            }
            updateContent();
        }
    }

    @Override
    public void deleteRecord() {

    }
}
