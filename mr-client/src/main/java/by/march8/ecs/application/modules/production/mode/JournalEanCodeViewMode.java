package by.march8.ecs.application.modules.production.mode;

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
import by.march8.ecs.application.modules.production.model.JournalEanCodeViewReportData;
import by.march8.ecs.application.modules.production.report.JournalEanCodeViewOO;
import by.march8.entities.production.EanCodeByColorsView;
import common.ProgressBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Журнал Ean-кодов
 * Created by lidashka on 19.12.2018.
 */
public class JournalEanCodeViewMode extends AbstractFunctionalMode {
    private RightEnum right;
    private JButton btnSearch = new JButton();
    private JButton btnPrint = new JButton();
    private ArrayList<Object> list;
    private JTextField tfModelNumber = new JTextField();
    private JTextField tfArticleNumber = new JTextField();
    private JTextField tfEanCode = new JTextField();
    private ProgressBar pb = null;

    public JournalEanCodeViewMode(MainController mainController) {
        controller = mainController;
        modeName = "Журнал EAN-кодов";
        frameViewPort = new FrameViewPort(mainController, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);

        right = RightEnum.READ;

        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.registerEvents(this);
        toolBar.setRight(right);
        toolBar.getBtnViewItem().setVisible(false);
        toolBar.getBtnNewItem().setVisible(false);
        toolBar.getBtnEditItem().setVisible(false);
        toolBar.getBtnDeleteItem().setVisible(false);

        btnPrint.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/printer.png", "Печать документа"));
        btnPrint.setToolTipText("Печать отчета");

        toolBar.add(btnPrint);

        JPanel pSearchPanel = new JPanel(null);
        JLabel lblModelNumber = new JLabel("Модель:");
        lblModelNumber.setBounds(5, 7, 60, 20);
        tfModelNumber.setBounds(61, 7, 120, 20);

        pSearchPanel.add(lblModelNumber);
        pSearchPanel.add(tfModelNumber);

        JLabel lblArticleNumber = new JLabel("Артикул:");
        lblArticleNumber.setBounds(200, 7, 60, 20);
        tfArticleNumber.setBounds(261, 7, 120, 20);

        pSearchPanel.add(lblArticleNumber);
        pSearchPanel.add(tfArticleNumber);

        JLabel lblEanCode = new JLabel("EAN-код:");
        lblEanCode.setBounds(400, 7, 60, 20);
        tfEanCode.setBounds(461, 7, 120, 20);

        pSearchPanel.add(lblEanCode);
        pSearchPanel.add(tfEanCode);

        pSearchPanel.setPreferredSize(new Dimension(590, 28));
        pSearchPanel.setOpaque(false);
        toolBar.add(pSearchPanel);

        btnSearch.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/update_data.png", "Обновить данные"));
        btnSearch.setToolTipText("Обновить данные");

        toolBar.add(new JLabel("     "));
        toolBar.add(btnSearch);
        toolBar.add(new Box(BoxLayout.X_AXIS));
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        gridViewPort = new GridViewPort(EanCodeByColorsView.class, false);
        list = gridViewPort.getDataModel();

        initEvents();

        frameViewPort.setGridViewPort(gridViewPort);
        gridViewPort.primaryInitialization();

        frameViewPort.getFrameControl().showFrame();
    }

    private void initEvents() {
        btnPrint.addActionListener(a -> {
            if (list.size() > 0) {
                final int answer = JOptionPane
                        .showOptionDialog(null,
                                "Сформировать отчет?",
                                "Печать...",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                new Object[]{"Да", "Отмена"},
                                "Да");
                if (answer == 0) {
                    printContentItems();
                }
            }
        });

        btnSearch.addActionListener(a -> {
            updateContent();
        });

        tfModelNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    updateContent();

                } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    updateContent();
                }
            }
        });

        tfArticleNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    updateContent();

                } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    updateContent();
                }
            }
        });

        tfEanCode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    updateContent();

                } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    updateContent();
                }
            }
        });
    }

    private void printContentItems() {
        List<EanCodeByColorsView> reportList = new ArrayList<>();

        for (int index = 0; index < gridViewPort.getTable().getRowCount(); index++) {
            int modelIndex = gridViewPort.getTable().convertRowIndexToModel(index);
            EanCodeByColorsView item = (EanCodeByColorsView) list.get(modelIndex);
            reportList.add(item);
        }

        JournalEanCodeViewReportData reportData = new JournalEanCodeViewReportData();
        reportData.setData((ArrayList<EanCodeByColorsView>) reportList);

        try {
            JournalEanCodeViewOO oo = new JournalEanCodeViewOO(modeName, reportData);
            oo.createReport("JournalEanCodeRoport.ots");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка ", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void updateContent() {
        if (isCorrectSearch()) {

            DaoFactory<EanCodeByColorsView> factory = DaoFactory.getInstance();
            IGenericDao<EanCodeByColorsView> dao = factory.getGenericDao();
            List<QueryProperty> criteria = new ArrayList<>();
            criteria.add(new QueryProperty("model", tfModelNumber.getText().trim() + "%"));
            criteria.add(new QueryProperty("article", tfArticleNumber.getText().trim().toUpperCase() + "%"));
            criteria.add(new QueryProperty("eancode", tfEanCode.getText().trim() + "%"));

            try {
                pb = new ProgressBar(controller.getMainForm(), false, "Сбор данных...");
                final SwingWorker sw = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        try {
                            list.clear();
                            list.addAll(dao.getEntityListByNamedQuery(EanCodeByColorsView.class,
                                    "EanCodeByColorsView.findByFasAndNarAndEan", criteria));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                        pb.dispose();
                    }
                };
                sw.execute();
                pb.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                pb.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Ошибка! " + e.getMessage(),
                        "Ошибка",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            frameViewPort.updateContent();
            gridViewPort.updateViewPort();
        } else
            JOptionPane.showMessageDialog(
                    null,
                    "Поля для поиска не должны быть пустыми!",
                    "Важно!",
                    JOptionPane.WARNING_MESSAGE);
    }

    private boolean isCorrectSearch() {
        if ((tfModelNumber.getText().trim() + tfArticleNumber.getText().trim() + tfEanCode.getText().trim()).equals(""))
            return false;
        else
            return true;
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
