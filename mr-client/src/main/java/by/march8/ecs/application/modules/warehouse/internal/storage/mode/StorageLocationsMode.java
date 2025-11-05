package by.march8.ecs.application.modules.warehouse.internal.storage.mode;

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
import by.march8.ecs.application.modules.warehouse.internal.storage.BalanceJDBC;
import by.march8.ecs.application.modules.warehouse.internal.storage.editor.StorageLocationsEditor;
import by.march8.ecs.application.modules.warehouse.internal.storage.model.BalanceArticle;
import by.march8.ecs.application.modules.warehouse.internal.storage.model.BalanceGrade;
import by.march8.ecs.application.modules.warehouse.internal.storage.model.BalanceSize;
import by.march8.entities.storage.BalanceItem;
import by.march8.entities.storage.StorageLocationsItem;
import by.march8.entities.storage.StorageLocationsView;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andy 03.11.2018 - 7:35.
 */
public class StorageLocationsMode extends AbstractFunctionalMode {

    private JButton btnBalance;
    private RightEnum right;
    private EditingPane editingPane;
    private ArrayList<Object> data;

    private JTextField tfDocumentNumber = new JTextField();
    private UCDatePeriodPicker datePeriodPicker;

    private TableRowSorter<TableModel> sorter;
    private JButton btnUpdate = new JButton();


    public StorageLocationsMode(MainController mainController) {
        controller = mainController;
        modeName = "Данные по местам хранения";
        frameViewPort = new FrameViewPort(controller, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);
        right = controller.getRight(modeName);
        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();
        toolBar.registerEvents(this);
        toolBar.setRight(right);
        toolBar.getBtnReport().setVisible(false);

        toolBar.getBtnEditItem().setVisible(true);
        toolBar.getBtnNewItem().setVisible(true);
        toolBar.getBtnDeleteItem().setVisible(true);

        btnUpdate.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/update_data.png", "Обновть данные"));
        btnUpdate.setToolTipText("Обновить данные");

        btnBalance = new JButton();
        btnBalance.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/import_24.png", "Остатки на складе"));
        btnBalance.setToolTipText("Остатки на складе");

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
        datePeriodPicker.setDatePickerBegin(DateUtils.getDateByStringValue("01.10.2018"));

        datePeriodPicker.setEditable(true);


        toolBar.add(datePeriodPicker);

        toolBar.add(btnUpdate);
        toolBar.addSeparator();
        toolBar.add(btnBalance);

        toolBar.add(new Box(BoxLayout.X_AXIS));

        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBar.setPreferredSize(new Dimension(0, 47));

        gridViewPort = new GridViewPort<>(StorageLocationsView.class, false);
        data = gridViewPort.getDataModel();
        frameViewPort.setGridViewPort(gridViewPort);

        initialGridSorter();
        initEvents();

        // Устанавливаем кастомный рендер гриду
//        gridViewPort.setCustomCellRender(new DisplacementCellRender());

        gridViewPort.initialFooter();

        Container footer = gridViewPort.getFooterPanel();
        footer.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel label = gridViewPort.getFooterTextComponent();
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 12));


        gridViewPort.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onDoubleClick(final int rowIndex, int columnIndex, final Object object) {
                viewDetail((StorageLocationsView) object);
            }
        });

        editingPane = new StorageLocationsEditor(frameViewPort);
        editingPane.setRight(right);
        editingPane.setSourceEntityClass(StorageLocationsItem.class);

        updateContent();
        gridViewPort.primaryInitialization();
        frameViewPort.getFrameControl().showFrame();
    }

    @Override
    public void updateContent() {
        DaoFactory<StorageLocationsView> factory = DaoFactory.getInstance();
        IGenericDaoGUI<StorageLocationsView> dao = factory.getGenericDao();
        java.util.List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("dateBegin", datePeriodPicker.getDatePickerBegin()));
        criteria.add(new QueryProperty("dateEnd", datePeriodPicker.getDatePickerEnd()));

        try {
            data.clear();
            data.addAll(dao.getEntityListByNamedQueryGUI(StorageLocationsView.class, "StorageLocationsView.findByPeriod", criteria));

        } catch (final Exception e) {
            e.printStackTrace();
        }

        frameViewPort.updateContent();
        gridViewPort.updateViewPort();
    }

    @Override
    public void addRecord() {
        // Создаем пустую форму диалога
        BaseEditorDialog editor = new BaseEditorDialog(controller,
                RecordOperationType.NEW);
        editor.setParentTitle("Документ на место хранения");
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
        editor.setParentTitle("Документ на место хранения");

        StorageLocationsView selectedItem = (StorageLocationsView) gridViewPort.getSelectedItem();
        if (selectedItem == null) {
            return;
        }

        final DaoFactory factory = DaoFactory.getInstance();
        final ICommonDao dao = factory.getCommonDao();
        StorageLocationsItem editableItem = null;

        try {
            editableItem = (StorageLocationsItem) dao.getEntityById(StorageLocationsItem.class, selectedItem.getId());
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

    private void viewDetail(StorageLocationsView item) {
        if (item != null) {
            new StorageLocationsDetailMode(controller, item);
        }
    }

    private void initEvents() {
        datePeriodPicker.addOnChangeAction(e -> {
            updateContent();
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

        btnBalance.addActionListener(a -> {
            prepareBalanceReport((StorageLocationsView) gridViewPort.getSelectedItem());
        });
    }

    private void prepareBalanceReport(StorageLocationsView selectedItem) {
        if (selectedItem == null) {
            return;
        }

        BalanceJDBC db = new BalanceJDBC();

        List<BalanceItem> list = db.getBalanceListByStorageDocumentId(selectedItem.getId());
        List<Integer> orderList = new ArrayList<>();
        if (list != null) {
            // Группируем по росто-размеру в пределах цены
            Map<Integer, BalanceArticle> map = new HashMap<>();
            for (BalanceItem item : list) {
                int key = item.getArticleCode();
                BalanceArticle article = map.get(item.getArticleCode());
                if (article == null) {
                    article = new BalanceArticle(item);
                    orderList.add(key);
                    map.put(key, article);
                }

                int gradeId = item.getGrade();
                BalanceGrade grade = article.getGradeById(gradeId);

                grade.attachToBalance(item);
            }
            // Получить данные оборотки
            int month = 8;
            List<BalanceItem> accessList = db.getBalanceAccessByMonth(month);
            List<Integer> orderListAccess = new ArrayList<>();
            if (accessList != null) {
                // Группируем по росто-размеру в пределах цены
                Map<Integer, BalanceArticle> mapAccess = new HashMap<>();
                for (BalanceItem item : accessList) {
                    int key = item.getArticleCode();
                    BalanceArticle article = mapAccess.get(item.getArticleCode());
                    if (article == null) {
                        article = new BalanceArticle(item);
                        orderListAccess.add(key);
                        mapAccess.put(key, article);
                    }

                    int gradeId = item.getGrade();
                    BalanceGrade grade = article.getGradeById(gradeId);
                    grade.attachToBalance(item);
                }

                // Заполняем остатки данными из Access

                for (Integer key : orderList) {
                    BalanceArticle item = map.get(key);
                    BalanceArticle itemAccess = mapAccess.get(key);

                    if (item != null) {
                        for (BalanceGrade grade : item.getGrades()) {
                            if (grade != null) {
                                for (BalanceSize size : grade.getSizes()) {
                                    if (itemAccess != null) {
                                        itemAccess.fillCost(item, grade, size);
                                    } else {
                                        System.err.println("В Access не найден артикул: " + key);
                                    }
                                }
                            }
                        }
                    }
                }

            }


            createHTML(orderList, map, "SOURCE");
            //createHTML(orderList, map, "REMAINS");


        }
    }

    private void createHTML(List<Integer> orderList, Map<Integer, BalanceArticle> map, String postfix) {
        String html = "<tr $ROW_STYLE>\n" +
                "<td>$ITEM_NAME</td>\n" +
                "<td>$MODEL_NUMBER</td>\n" +
                "<td>$ARTICLE_NUMBER</td>\n" +
                "<td>$ARTICLE_CODE</td>\n" +
                "<td>$GRADE</td>\n" +
                "<td>$SIZES</td>\n" +
                "<td>$GROWTH</td>\n" +
                "<td>$PRICE</td>\n" +
                "<td $AMOUNT_STYLE>$AMOUNT</td>\n" +
                "<td>$COST</td>\n" +
                "<td>$APRICE</td>\n" +
                "<td>$AAMOUNT</td>\n" +
                "<td>$ACOST</td>\n" +
                "</tr>";

        String templatePath = MainController.getRunPath() + "/Templates/htmlTemplates/warehouseBalance/";
        System.out.println("Путь к файлу шаблона : " + templatePath);
        String templateName = "templateWarehouseBalance.html";

        File htmlTemplateFile = new File(templatePath + templateName);
        String htmlTemplate;
        try {
            System.out.println("Чтение шаблона :" + htmlTemplateFile.getAbsolutePath() + " : " + htmlTemplateFile.getName());
            htmlTemplate = FileUtils.readFileToString(htmlTemplateFile, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String content = "";
        // Пробежим по мапе
        for (Integer key : orderList) {
            BalanceArticle item = map.get(key);
            if (item != null) {
                for (BalanceGrade grade : item.getGrades()) {
                    if (grade != null) {
                        for (BalanceSize size : grade.getSizes()) {
                            String amountStyle = "";
                            if (size.getAmount() != size.getAmountAccess()) {
                                amountStyle = "style=\"color: #FF0000; font-weight:bold\"";
                            }
                            content += html.replace("\n", "")
                                    .replace("$ROW_STYLE", "")
                                    .replace("$AMOUNT_STYLE", amountStyle)
                                    .replace("$ITEM_NAME", item.getItemName())
                                    .replace("$MODEL_NUMBER", String.valueOf(item.getModelNumber()))
                                    .replace("$ARTICLE_NUMBER", item.getArticleNumber())
                                    .replace("$ARTICLE_CODE", String.valueOf(item.getArticleCode()))
                                    .replace("$GRADE", String.valueOf(grade.getGrade()))
                                    .replace("$SIZES", size.getSizeRange())
                                    .replace("$GROWTH", size.getGrowthRange())
                                    .replace("$PRICE", String.valueOf(size.getPrice()))
                                    .replace("$AMOUNT", String.valueOf(size.getAmount()))
                                    .replace("$COST", String.valueOf(
                                            new BigDecimal(size.getCost())
                                                    .setScale(2, BigDecimal.ROUND_HALF_UP)))
                                    .replace("$APRICE", String.valueOf(size.getPriceAccess()))
                                    .replace("$AAMOUNT", String.valueOf(size.getAmountAccess()))
                                    .replace("$ACOST", String.valueOf(
                                            new BigDecimal(size.getCostAccess())
                                                    .setScale(2, BigDecimal.ROUND_HALF_UP))) + "\n";
                        }
                    }
                }
            }


            content += html.replace("\n", "")
                    .replace("$ROW_STYLE", "style=\"font-weight:bold\"")
                    .replace("$ITEM_NAME", "")
                    .replace("$MODEL_NUMBER", "")
                    .replace("$ARTICLE_NUMBER", "")
                    .replace("$ARTICLE_CODE", "")
                    .replace("$GRADE", "")
                    .replace("$SIZES", "-")
                    .replace("$GROWTH", "Итого:")
                    .replace("$PRICE", "")
                    .replace("$AMOUNT", String.valueOf(item.getTotalAmount()))
                    .replace("$COST", String.valueOf(new BigDecimal(item.getTotalCost())
                            .setScale(2, BigDecimal.ROUND_HALF_UP)))
                    .replace("$APRICE", "")
                    .replace("$AAMOUNT", String.valueOf(item.getTotalAmountAccess()))
                    .replace("$ACOST", String.valueOf(new BigDecimal(item.getTotalCostAccess())
                            .setScale(2, BigDecimal.ROUND_HALF_UP)))
                    + "\n";
        }

        htmlTemplate = htmlTemplate.replace("$TABLE_CONTENT", content);

        File outputFile = new File("C:\\index_" + postfix + ".html");
        System.out.println("Сохраняем созданный документ: " + outputFile.getAbsolutePath() + ":" + outputFile.getName());
        try {
            // Сохраняем HTML
            FileUtils.writeStringToFile(outputFile, htmlTemplate, Charset.forName("utf-8"));
            //Создаем архив каталога
            //ZipUtil.pack(new File(pathToSave + "preview/"), new File(pathToSave + "mail/catalog_" + data.getDocument().getDocumentNumber() + ".zip"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println("DOCUMENT ID : "+selectedItem.getId());
    }


    private void initialGridSorter() {
        sorter = new TableRowSorter<>(gridViewPort.getTableModel());
        sorter.setSortsOnUpdates(true);
        gridViewPort.getTable().setRowSorter(sorter);
    }
}
