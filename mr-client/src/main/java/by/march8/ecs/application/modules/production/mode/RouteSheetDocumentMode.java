package by.march8.ecs.application.modules.production.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.entities.production.CutCardDocumentView;
import by.march8.entities.production.RouteSheetDocumentView;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Developer on 05.12.2019 7:31
 */
public class RouteSheetDocumentMode extends AbstractFunctionalMode {

    private RightEnum right;
    private EditingPane editingPane;

    private JButton btnDetail = new JButton();

    private List<RouteSheetDocumentView> data;
    private GridViewPort<RouteSheetDocumentView> gvDocuments;

    private JTextField tfDocumentNumber = new JTextField();
    private UCDatePeriodPicker datePeriodPicker;

    private TableRowSorter<TableModel> sorter;
    private JButton btnUpdate = new JButton();

    private JButton btnExport = new JButton();

    public RouteSheetDocumentMode(MainController mainController) {
        controller = mainController;
        modeName = "Журнал маршрутных листов";
        frameViewPort = new FrameViewPort(mainController, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);
        right = controller.getRight(modeName);

        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.registerEvents(this);
        toolBar.setRight(right);
        toolBar.getBtnReport().setVisible(false);

        toolBar.add(btnDetail);

        btnDetail.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/view_detail.png", "Спецификация"));
        btnDetail.setToolTipText("Спецификация маршрутного листа");

        btnUpdate.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/update_data.png", "Обновть данные"));
        btnUpdate.setToolTipText("Обновить данные");

        btnExport.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/email.png", "Экспорт в DBF"));
        btnExport.setToolTipText("Экспорт в DBF");

        JPanel pSearchPanel = new JPanel(null);
        JLabel lblDocumentNumber = new JLabel("№  : ");
        lblDocumentNumber.setBounds(5, 7, 40, 20);
        tfDocumentNumber.setBounds(50, 8, 100, 20);

        pSearchPanel.add(lblDocumentNumber);
        pSearchPanel.add(tfDocumentNumber);

        pSearchPanel.setPreferredSize(new Dimension(150, 28));
        pSearchPanel.setOpaque(false);
        toolBar.add(pSearchPanel);

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


        gvDocuments = new GridViewPort<>(RouteSheetDocumentView.class, false);
        data = gvDocuments.getDataModel();
        frameViewPort.setGridViewPort(gvDocuments);

        initialGridSorter();
        initEvents();

        // Устанавливаем кастомный рендер гриду
        //gridViewPort.setCustomCellRender(new DisplacementCellRender());

        gvDocuments.initialFooter();

        Container footer = gvDocuments.getFooterPanel();
        footer.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel label = gvDocuments.getFooterTextComponent();
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 12));


        gvDocuments.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onSelectChanged(final int rowIndex, final Object object) {
                CutCardDocumentView selectedItem = (CutCardDocumentView) object;
                if (selectedItem != null) {
                    // TODO SOME THINGS
                }
            }

            @Override
            public void onDoubleClick(final int rowIndex, int columnIndex, final Object object) {
                viewDetail();
            }
        });

        //editingPane = new DisplacementInvoiceEditor(frameViewPort);
        //editingPane.setRight(right);
        //editingPane.setSourceEntityClass(DisplacementInvoiceView.class);
        //entityViewPort = new EntityViewPort(frameViewPort, editingPane);
        updateContent();

        gvDocuments.primaryInitialization();
        frameViewPort.getFrameControl().showFrame();
    }

    private void initEvents() {

    }

    private void initialGridSorter() {

    }

    public void viewDetail() {
        RouteSheetDocumentView selectedItem = gvDocuments.getSelectedItem();
        if (selectedItem != null) {
            // new CutCardDetailMode(controller, selectedItem);
        }
    }


    @Override
    public void updateContent() {
        DaoFactory<RouteSheetDocumentView> factory = DaoFactory.getInstance();
        IGenericDao<RouteSheetDocumentView> dao = factory.getGenericDao();
        java.util.List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("date_start", datePeriodPicker.getDatePickerBegin()));
        criteria.add(new QueryProperty("date_end", datePeriodPicker.getDatePickerEnd()));
        try {
            data.clear();
            data.addAll(dao.getEntityListByNamedQuery(RouteSheetDocumentView.class, "RouteSheetDocumentView.findByPeriod", criteria));

        } catch (final Exception e) {
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
}
