package by.march8.ecs.application.modules.marketing.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.classifier.EANCodeItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EanCodeJournalMode extends AbstractFunctionalMode {
    private GridViewPort<EANCodeItem> gvEanCode;
    private List<EANCodeItem> dataList;
    private UCToolBar toolBar;
    private JButton btnSearch;
    private JTextField tfEanCode = new JTextField();


    public EanCodeJournalMode(MainController controller) {
        this.controller = controller;
        modeName = "Журнал EAN кодов ОАО \"8 Марта\"";
        frameViewPort = new FrameViewPort(controller, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);

        init();
        initEvents();
        updateContent();

        frameViewPort.getFrameControl().showFrame();
    }

    private void init() {
        gvEanCode = new GridViewPort<>(EANCodeItem.class);
        dataList = gvEanCode.getDataModel();
        toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.setRight(RightEnum.WRITE);
        toolBar.registerEvents(this);
        toolBar.getBtnViewItem().setVisible(false);
        toolBar.getBtnNewItem().setVisible(false);
        toolBar.getBtnEditItem().setVisible(false);
        toolBar.getBtnDeleteItem().setVisible(false);
        frameViewPort.setGridViewPort(gvEanCode);


        JPanel pSearchPanel = new JPanel(null);
        JLabel lblDocumentNumber = new JLabel("EAN: ");
        lblDocumentNumber.setBounds(5, 7, 40, 20);
        tfEanCode.setBounds(50, 8, 130, 20);

        pSearchPanel.add(lblDocumentNumber);
        pSearchPanel.add(tfEanCode);

        pSearchPanel.setPreferredSize(new Dimension(220, 32));
        pSearchPanel.setOpaque(false);

        toolBar.addSeparator();
        toolBar.add(pSearchPanel);

        btnSearch = new JButton();
        btnSearch.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/find24.png", "Обновить данные"));
        btnSearch.setToolTipText("Поиск");
        toolBar.addSeparator();
        btnSearch.setBounds(190, 3, 26, 26);
        pSearchPanel.add(btnSearch);
    }

    private void initEvents() {

        btnSearch.addActionListener(a -> {
            searchByEanCode();
        });

        tfEanCode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchByEanCode();
                } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    tfEanCode.setText("");
                }
            }
        });
    }

    private void searchByEanCode() {
        if (tfEanCode.getText().trim().isEmpty()) {
            return;
        }

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {

                DaoFactory<EANCodeItem> factory = DaoFactory.getInstance();
                IGenericDao<EANCodeItem> dao = factory.getGenericDao();
                List<QueryProperty> criteria = new ArrayList<>();
                criteria.add(new QueryProperty("eanCode", "%" + tfEanCode.getText().trim() + "%"));
                List<EANCodeItem> list = null;
                dataList.clear();
                try {
                    list = dao.getEntityListByNamedQuery(EANCodeItem.class, "EANCodeItem.likeByEanCode", criteria);
                    dataList.addAll(list);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                frameViewPort.updateContent();
                return true;
            }
        }
        Task task = new Task("Поиск продукции ...");
        task.executeTask();
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
