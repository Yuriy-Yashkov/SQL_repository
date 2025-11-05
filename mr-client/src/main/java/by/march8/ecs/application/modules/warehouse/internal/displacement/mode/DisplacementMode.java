package by.march8.ecs.application.modules.warehouse.internal.displacement.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RecordOperationType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.DateSelectorDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.internal.displacement.DisplacementDBF;
import by.march8.ecs.application.modules.warehouse.internal.displacement.DisplacementInvoiceReport;
import by.march8.ecs.application.modules.warehouse.internal.displacement.dao.DAODisplacementFactory;
import by.march8.ecs.application.modules.warehouse.internal.displacement.dao.DisplacementJDBC;
import by.march8.ecs.application.modules.warehouse.internal.displacement.dao.IDisplacementDao;
import by.march8.ecs.application.modules.warehouse.internal.displacement.editor.DisplacementInvoiceEditor;
import by.march8.entities.warehouse.DisplacementCellEntity;
import by.march8.entities.warehouse.DisplacementInvoiceView;
import by.march8.entities.warehouse.VInternalInvoiceItem;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Функциональный режим для работы с накладными внутреннего перемещения
 */
public class DisplacementMode extends AbstractFunctionalMode {

    private RightEnum right;
    // private EntityViewPort entityViewPort;
    private EditingPane editingPane;

    private JButton btnDetail = new JButton();

    private ArrayList<Object> data;

    private JTextField tfDocumentNumber = new JTextField();
    private UCDatePeriodPicker datePeriodPicker;

    private TableRowSorter<TableModel> sorter;
    private JButton btnUpdate = new JButton();

    private JButton btnExport = new JButton();

    private JButton btnDocumentTools = new JButton();

    public DisplacementMode(MainController mainController) {
        controller = mainController;
        modeName = "Журнал накладных внутреннего перемещения";
        frameViewPort = new FrameViewPort(mainController, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);
        right = controller.getRight(modeName);
        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.registerEvents(this);
        toolBar.setRight(right);
        toolBar.getBtnReport().setVisible(false);

        toolBar.getBtnEditItem().setVisible(false);
        toolBar.getBtnNewItem().setVisible(false);
        toolBar.getBtnDeleteItem().setVisible(false);

        toolBar.add(btnDetail);

        btnDetail.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/view_detail.png", "Спецификация"));
        btnDetail.setToolTipText("Спецификация накладной");

        btnUpdate.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/update_data.png", "Обновть данные"));
        btnUpdate.setToolTipText("Обновить данные");

        btnExport.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/email.png", "Экспорт в DBF"));
        btnExport.setToolTipText("Экспорт в DBF");

        btnDocumentTools.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/admin_22.png", "Изменение атрибутов документа"));
        btnDocumentTools.setToolTipText("Изменение атрибутов документа");

        toolBar.add(btnDocumentTools);

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
            editReduce3GradeValue();
            //updateContent();
        });

        btnExport.addActionListener(e -> exportToDBF());


        gridViewPort = new GridViewPort(DisplacementInvoiceView.class, false);
        data = gridViewPort.getDataModel();
        frameViewPort.setGridViewPort(gridViewPort);

        initialGridSorter();
        initEvents();

        // Устанавливаем кастомный рендер гриду
        gridViewPort.setCustomCellRender(new DisplacementCellRender());

        gridViewPort.initialFooter();

        Container footer = gridViewPort.getFooterPanel();
        footer.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel label = gridViewPort.getFooterTextComponent();
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 12));


        JPopupMenu popupMenuTools = new JPopupMenu();

        JMenuItem miChangeDocumentDate = new JMenuItem("Изменить дату документа");
        popupMenuTools.add(miChangeDocumentDate);

        btnDocumentTools.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (btnDocumentTools.isEnabled()) {
                    popupMenuTools.show(e.getComponent(), 0, e.getComponent().getHeight());
                }
            }
        });


        gridViewPort.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onSelectChanged(final int rowIndex, final Object object) {
                DisplacementInvoiceView selectedItem = (DisplacementInvoiceView) object;
                if (selectedItem != null) {

                    toolBar.getBtnViewItem().setEnabled(false);
                    if (selectedItem.getStatusCode() == 3) {
                        toolBar.getBtnViewItem().setEnabled(true);
                    }

                    //System.out.println("Ативный документ ["+selectedItem.getId()+"]");
                    try {


                        gridViewPort.setFooterValue(String.format("СДАЧА: всего единиц: %d в упаковках: %d  упаковок: %d россыпью: %d"
                                , selectedItem.getAmountAll()
                                , selectedItem.getAmountAll() - selectedItem.getAmountUnPack()
                                , selectedItem.getAmountPack()
                                , selectedItem.getAmountUnPack()));
/*"Всего единиц: " + amountAll + " "
                        + "В упаковках: " + (amountAll - amountNotPack) + " "
                        + "Россыпью: " + amountNotPack + " " + "Упаковок: " + amountPack;*/

                        /*if (!selectedItem.getStatusText().trim().equals("Закрыт")) {
                            //toolBar.getBtnNewItem().setEnabled(true);
                            toolBar.getBtnEditItem().setEnabled(true);
                            toolBar.getBtnDeleteItem().setEnabled(true);
                        } else {
                            //toolBar.getBtnNewItem().setEnabled(false);
                            toolBar.getBtnEditItem().setEnabled(false);
                            toolBar.getBtnDeleteItem().setEnabled(false);
                        }*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onDoubleClick(final int rowIndex, int columnIndex, final Object object) {
                viewDetail();
            }
        });

        miChangeDocumentDate.addActionListener(a -> {
            DisplacementInvoiceView item = (DisplacementInvoiceView) gridViewPort.getSelectedItem();
            if (item != null) {
                changeDocumentDate(item);
            }
        });

        editingPane = new DisplacementInvoiceEditor(frameViewPort);
        editingPane.setRight(right);
        editingPane.setSourceEntityClass(DisplacementInvoiceView.class);
        //entityViewPort = new EntityViewPort(frameViewPort, editingPane);
        updateContent();
        gridViewPort.primaryInitialization();
        frameViewPort.getFrameControl().showFrame();
    }

    private void changeDocumentDate(DisplacementInvoiceView item) {
        DateSelectorDialog dialogDateSelector = new DateSelectorDialog(controller);
        DisplacementJDBC db = new DisplacementJDBC();
        Date date = db.getSaleDateForDocumentId(item.getId());
        Date d = dialogDateSelector.selectDate(date);
        if (d != null) {
            db.updateSaleDateForDocumentId(item.getId(), d);
            updateContent();
        }
    }

    private void exportToDBF() {
        DisplacementInvoiceReport report = new DisplacementInvoiceReport();
        report.setDocument((DisplacementInvoiceView) gridViewPort.getSelectedItem());

        DisplacementDBF accountingDbf = new DisplacementDBF("c:/out", "" + report.getDocument().getDocumentNumber());
        accountingDbf.connect();

        DaoFactory<VInternalInvoiceItem> factory = DaoFactory.getInstance();
        IGenericDao<VInternalInvoiceItem> dao = factory.getGenericDao();
        java.util.List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("document", report.getDocument().getId()));
        java.util.List<VInternalInvoiceItem> list = null;
        try {
            list = dao.getEntityListByNamedQuery(VInternalInvoiceItem.class, "VInternalInvoice.findByDocumentId", criteria);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        report.setList(list);
        accountingDbf.processing(report);
        if (accountingDbf != null) {
            accountingDbf.disconnect();
        }
    }

    private void editReduce3GradeValue() {

        final DaoFactory factory = DaoFactory.getInstance();
        // ПОлучаем интерфейс для работы с БД
        final ICommonDao dao = factory.getCommonDao();

        DisplacementInvoiceView item = (DisplacementInvoiceView) gridViewPort.getSelectedItem();
        DisplacementCellEntity entity = null;
        if (item != null) {
            try {
                entity = (DisplacementCellEntity) dao.getEntityById(DisplacementCellEntity.class, item.getId());
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
        } else {
            return;
        }

        if (entity == null) {
            return;
        }
        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.EDIT);
        editor.setParentTitle("Изменение размера уценки");

        editingPane.setSourceEntity(entity);
        // Для созданного пустого диалога устанавливаем панель редактирования
        editor.setEditorPane(editingPane);
        // Модально показываем форму и ожидаем закрытия
        if (editor.showModal()) {
            // Форма закрыта со значением true
            // Получаем DAO слой
            try {
                // Обновляем сущность в БД
                Object o = editingPane.getSourceEntity();
                dao.updateEntity(o);
                //gridViewPort.setUpdatedObject(entity);
                // Изменяем статус расчета по документу как РАСЧИТАН И СОХРАНЕН
                //SaleDocumentManager.checkDocument(((BaseEntity) o).getId(), false);
            } catch (final SQLException e) {
                e.printStackTrace();
            }
            // ПОсле сохранения данных обновляем содержимое грида
            updateContent();
        }
    }

    private void initEvents() {
        btnDetail.addActionListener(e -> viewDetail());
        datePeriodPicker.addOnChangeAction(e -> {
            updateContent();
            //controller.getPersonalization().setPersonalSettings(this, datePeriodPicker);
        });

        tfDocumentNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    sorter.setRowFilter(RowFilter.regexFilter(tfDocumentNumber.getText().trim(), 1));

                } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    //if (rows.size() > 0) {
                    sorter.setRowFilter(null);
                    // }
                    tfDocumentNumber.setText("");
                }
            }
        });

        btnUpdate.addActionListener(a -> updateContent());


    }

    private void initialGridSorter() {
        sorter = new TableRowSorter<>(gridViewPort.getTableModel());
        sorter.setSortsOnUpdates(true);
        gridViewPort.getTable().setRowSorter(sorter);
    }

    @Override
    public void updateContent() {
        DAODisplacementFactory factory = DAODisplacementFactory.getInstance();
        IDisplacementDao dao = factory.getDisplacementDao();
        try {
            data.clear();
            data.addAll(dao.getDisplacementDocumentByPeriodThread(0, datePeriodPicker.getDatePickerBegin(), datePeriodPicker.getDatePickerEnd()));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        frameViewPort.updateContent();
        gridViewPort.updateViewPort();
    }

    public void viewDetail() {
        DisplacementInvoiceView selectedItem = (DisplacementInvoiceView) gridViewPort.getSelectedItem();
        if (selectedItem != null) {
            new DisplacementDetailMode(controller, selectedItem);
        }
    }

    @Override
    public void editRecord() {
        //entityViewPort.editRecord();
    }

    @Override
    public void deleteRecord() {
        //entityViewPort.deleteRecord();
    }

    @Override
    public void addRecord() {

    }
}
