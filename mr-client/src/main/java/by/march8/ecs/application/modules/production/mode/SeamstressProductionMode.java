package by.march8.ecs.application.modules.production.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.seamstress.ViewSeamstressItem;
import dept.production.zsh.spec.SpecPDB;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Andy on 04.12.2021 7:30
 */
public class SeamstressProductionMode extends AbstractFunctionalMode {

    private RightEnum right;
    private EditingPane editingPane;

    private JButton btnDetail = new JButton();

    private List<ViewSeamstressItem> data;
    private GridViewPort<ViewSeamstressItem> gvDocuments;

    private UCDatePeriodPicker datePeriodPicker;

    private TableRowSorter<TableModel> sorter;
    private JButton btnUpdate = new JButton();

    private JButton btnExport = new JButton();

    private SpecPDB db = new SpecPDB();
    private JLabel label;

    public SeamstressProductionMode(MainController mainController) {
        controller = mainController;
        modeName = "Отчет о выработке швеи";
        frameViewPort = new FrameViewPort(mainController, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);
        right = controller.getRight(modeName);

        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.registerEvents(this);
        toolBar.setRight(right);
        toolBar.getBtnViewItem().setVisible(false);
        toolBar.getBtnReport().setVisible(false);
        toolBar.getBtnNewItem().setVisible(false);
        toolBar.getBtnEditItem().setVisible(false);
        toolBar.getBtnDeleteItem().setVisible(false);

        //toolBar.add(btnDetail);

        //btnDetail.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/view_detail.png", "Спецификация"));
        //btnDetail.setToolTipText("Спецификация карты кроя");

        btnUpdate.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/update_data.png", "Обновть данные"));
        btnUpdate.setToolTipText("Обновить данные");

        btnExport.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/excel_24.png", "Экспорт в XML"));
        btnExport.setToolTipText("Экспорт в XML");

        datePeriodPicker = new UCDatePeriodPicker();
        datePeriodPicker.setName("datePeriodPicker");
        datePeriodPicker.setDatePickerEnd(new Date());
        datePeriodPicker.setDatePickerBegin(DateUtils.getFirstDay(datePeriodPicker.getDatePickerEnd()));

        datePeriodPicker.setEditable(true);

        toolBar.add(datePeriodPicker);

        toolBar.add(btnUpdate);
        toolBar.addSeparator();
        toolBar.add(btnExport);

        toolBar.add(new Box(BoxLayout.X_AXIS));

        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBar.setPreferredSize(new Dimension(0, 47));

        toolBar.getBtnViewItem().setIcon(new ImageIcon(MainController.getRunPath() + "/Img/edit24.png", "Редактировать размер уценки"));
        toolBar.getBtnViewItem().addActionListener(a -> {
            //editReduce3GradeValue();
            //updateContent();
        });

        //btnExport.addActionListener(e->exportToDBF());


        gvDocuments = new GridViewPort<>(ViewSeamstressItem.class, false);
        data = gvDocuments.getDataModel();
        frameViewPort.setGridViewPort(gvDocuments);

        initialGridSorter();
        initEvents();

        gvDocuments.initialFooter();

        Container footer = gvDocuments.getFooterPanel();
        footer.setLayout(new FlowLayout(FlowLayout.LEFT));
        label = gvDocuments.getFooterTextComponent();
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 12));


        btnExport.addActionListener(a -> {
            exportToXML();
        });

        gvDocuments.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onSelectChanged(final int rowIndex, final Object object) {
                /*SeamstressMapDocument selectedItem = (SeamstressMapDocument) object;
                if (selectedItem != null) {
                    // TODO SOME THINGS
                }
                */
            }

            @Override
            public void onDoubleClick(final int rowIndex, int columnIndex, final Object object) {
                viewDetail();
            }
        });

        btnUpdate.addActionListener(a -> updateContent());

        updateContent();

        gvDocuments.primaryInitialization();
        frameViewPort.getFrameControl().showFrame();
    }

    private void initEvents() {

    }

    private void initialGridSorter() {

    }

    public void viewDetail() {
        /*SeamstressMapDocument selectedItem = gvDocuments.getSelectedItem();
        if (selectedItem != null) {
            new SeamstressProductionDetailMode(controller, selectedItem);
        }
        */
    }

    private void exportToXML() {
        if (!data.isEmpty()) {
            String dir = Dialogs.getDirectory(controller);
            if (dir != null) {
                String filename = DateUtils.getNormalDateTimeFormatPlus(Calendar.getInstance().getTime()) + ".xml";
                new SeamstressToXML(data, dir, filename);
            }
        }
    }

    @Override
    public void updateContent() {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    data.clear();
                    data.addAll(db.getSeamstressMap(datePeriodPicker.getTimeLimitPeriod()));
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }

        Task task = new Task("Получение данных...");
        task.executeTask();
        frameViewPort.updateContent();
        label.setText("Для отчета отобрано " + data.size() + " позиций");
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
