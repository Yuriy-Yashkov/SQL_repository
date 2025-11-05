package by.march8.ecs.application.modules.marketing.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.abstracts.TableEventAdapter;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCImageLabel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.marketing.dao.MarketingPriceListMarchJDBC;
import by.march8.ecs.application.modules.marketing.fx.SwingController;
import by.march8.ecs.application.modules.marketing.fx.model.MovementProductionItem;
import by.march8.ecs.application.modules.marketing.model.ChartItem;
import by.march8.ecs.application.modules.marketing.model.ItemSimple;
import by.march8.ecs.application.modules.marketing.model.MarketingRemainsReportData;
import by.march8.ecs.application.modules.marketing.model.ModelImageSize;
import by.march8.ecs.application.modules.marketing.model.MultiLineTableCellRenderer;
import by.march8.ecs.application.modules.marketing.model.WarehouseRemainsRenderer;
import by.march8.ecs.application.modules.marketing.reports.MarketingRemainsReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentDataProvider;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.ProductionItem;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.ecs.framework.common.Settings;
import by.march8.ecs.services.images.ImageService;
import by.march8.ecs.services.images.ModelImageServiceDB;
import by.march8.entities.warehouse.WarehouseRemainsItem;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import dept.production.planning.PlanPDB;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Andy 26.04.2018 - 12:41.
 */
public class WarehouseRemainsMode extends AbstractFunctionalMode {

    MarketingPriceListMarchJDBC db;
    //private JButton btnImport = new JButton();
    private JButton btnSearch = new JButton();
    private JButton btnDetail = new JButton();
    private RightEnum right;
    private ArrayList<Object> data;
    private String fileName;

    public WarehouseRemainsMode(MainController mainController, RightEnum rightEnum) {
        controller = mainController;
        modeName = "Монитор остатков в складе ГП [Файл остатков ostday.dbf не найден]";
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

        //toolBar.add(btnDetail);

        btnDetail.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/view_detail.png", "Спецификация"));
        btnDetail.setToolTipText("Спецификация накладной");

        btnSearch.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/excel_24.png", "Отбор на печать"));
        btnSearch.setToolTipText("Отбор на печать");

        toolBar.add(btnSearch);

        toolBar.add(new Box(BoxLayout.X_AXIS));

        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBar.setPreferredSize(new Dimension(0, 47));

        toolBar.getBtnViewItem().setIcon(new ImageIcon(MainController.getRunPath() + "/Img/edit24.png", "Редактировать размер уценки"));
        gridViewPort = new GridViewPort(WarehouseRemainsItem.class, false);

        gridViewPort.setIgnoreNotEditableCells(true);
        gridViewPort.getTable().setRowSelectionAllowed(true);
        gridViewPort.setCustomCellRender(new WarehouseRemainsRenderer());
        gridViewPort.getTable().setRowHeight(180);

        MultiLineTableCellRenderer renderer = new MultiLineTableCellRenderer();

        gridViewPort.getTable().getColumnModel().getColumn(5).setCellRenderer(renderer);
        gridViewPort.getTable().getColumnModel().getColumn(6).setCellRenderer(renderer);
        gridViewPort.getTable().getColumnModel().getColumn(9).setCellRenderer(renderer);
        gridViewPort.getTable().getColumnModel().getColumn(10).setCellRenderer(renderer);

        data = gridViewPort.getDataModel();
        frameViewPort.setGridViewPort(gridViewPort);

        // Устанавливаем кастомный рендер гриду
        //gridViewPort.setCustomCellRender(new DisplacementCellRender());
        //gridViewPort.initialFooter();


        db = new MarketingPriceListMarchJDBC();

        gridViewPort.setTableEventHandler(new TableEventAdapter() {
            @Override
            public void onSelectChanged(final int rowIndex, final Object object) {
                WarehouseRemainsItem selectedItem = (WarehouseRemainsItem) object;
                if (selectedItem != null) {

                }
            }

            @Override
            public void onDoubleClick(final int rowIndex, int columnIndex, final Object object) {
                WarehouseRemainsItem selectedItem = (WarehouseRemainsItem) object;
                if (selectedItem != null) {

/*                    if (columnIndex == 11) {
                        ImageSelectorDialog selector = new ImageSelectorDialog(controller, Integer.valueOf(selectedItem.getModelNumber()));
                        selector.selectImage();
                    }*/

                    if (columnIndex < 11 && columnIndex >= 0) {
                        getMovementStatistic(selectedItem);
                    }
                }
            }
        });

        // Подготовка файла к работе
        File workFile = prepareRemainsFile("ostday.dbf");
        if (workFile != null) {
            if (workFile.exists()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                modeName = "Монитор остатков в складе ГП по состоянию на " + sdf.format(workFile.lastModified());
                frameViewPort.getFrameControl().setTitleFrame(modeName);
                //entityViewPort = new EntityViewPort(frameViewPort, editingPane);
                fileName = workFile.getAbsolutePath();
                updateContent();
            }
        }

        btnSearch.addActionListener(a -> {
            if (data.size() > 0) {
                final int answer = Dialogs.showQuestionDialog("Сформировать документ отбора остатков?", "Формирование документа");
                if (answer == 0) {
                    exportToExcel();
                }
            }
        });

        gridViewPort.primaryInitialization();
        frameViewPort.getFrameControl().showFrame();
    }

    private void exportToExcel() {
        List<WarehouseRemainsItem> reportList = new ArrayList<>();
        MarketingRemainsReportData reportData = new MarketingRemainsReportData();

        for (int index = 0; index < gridViewPort.getTable().getRowCount(); index++) {
            int modelIndex = gridViewPort.getTable().convertRowIndexToModel(index);
            WarehouseRemainsItem item = (WarehouseRemainsItem) data.get(modelIndex);
            reportList.add(item);
        }
        reportData.setData(reportList);
        new MarketingRemainsReport(reportData);
    }

    private void getMovementStatistic(WarehouseRemainsItem productItem) {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
/*                ItemSimple entity = db.getItemIdByArticleCodeAndSizeGrowthGrade(Integer.valueOf(selectedItem.getCategory()),
                        selectedItem.getSize(),
                        selectedItem.getGrowth(),
                        selectedItem.getGrade());*/

                ItemSimple entity = new ItemSimple();
                entity.setId(Integer.valueOf(productItem.getArticleCode()));

                if (entity != null) {
                    List<ChartItem> inList = db.getIncomeListByItemId(entity.getId());
                    List<ChartItem> outList = db.getSaleListByItemId(entity.getId());
                    List<ChartItem> planList = new ArrayList<>();
                    try {
                        PlanPDB planDb = new PlanPDB();
                        Vector v = planDb.getPowerSearch(productItem.getArticleCode(), "", "");

                        for (Object o : v) {
                            ChartItem item = new ChartItem();
                            String value = ((Vector) o).get(4).toString();
                            item.setAmount(Integer.valueOf(value.substring(0, value.length() - 2)));
                            item.setDate(((Vector) o).get(9).toString());
                            planList.add(item);
                        }

                        inList.sort((a, b) -> a.getDate().compareTo(b.getDate()));
                        outList.sort((a, b) -> a.getDate().compareTo(b.getDate()));
                        planList.sort((a, b) -> a.getDate().compareTo(b.getDate()));

                        MovementProductionItem item = new MovementProductionItem();
                        ProductionItem productionItem = new ProductionItem();
                        productionItem.setStockBalance(productItem.getAmountByArticle());
                        productionItem.setName(productItem.getItemName());
                        productionItem.setItemModel(String.valueOf(productItem.getModelNumber()));
                        productionItem.setArticleName(productItem.getArticleNumber());

                        item.setItem(productionItem);
                        item.setInList(inList);
                        item.setOutList(outList);
                        item.setPlanList(planList);

                        new SwingController(controller, item);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }

        }

        Task task = new Task("Получение данных по изделию ...");
        task.executeTask();
    }

    private File prepareRemainsFile(String fileName) {
        // Есть ли файл во временном каталоге программы
        String tempFileName = Settings.TEMPORARY_DIR + fileName;
        String exchangeFileName = Settings.getExchangeCatalog() + fileName;
        File tempFile = new File(tempFileName);
        File exchangeFile = new File(exchangeFileName);
        if (!tempFile.exists()) {
            // Есть ли в обменной папке данный файл
            // И если есть то копируем
            if (exchangeFile.exists()) {
                try {
                    FileUtils.copyFile(exchangeFile, tempFile);
                    return tempFile;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
            // Если файл во временном каталоге обнаружен, сверяем версии файлов
        } else {
            // Если исходный файл доступен, иначе возвращаем рабочий файл
            if (exchangeFile.exists()) {
                // Сверяем время последнего изменения файлов
                if (tempFile.lastModified() < exchangeFile.lastModified()) {
                    // Файлы отличаются, копируем во временной каталог
                    try {
                        FileUtils.copyFile(exchangeFile, tempFile);
                        return tempFile;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return tempFile;
                    }
                } else {
                    return tempFile;
                }
            } else {
                return tempFile;
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
                data.clear();
                data.addAll(preSorting(readRemainsOldLine(fileName)));
                gridViewPort.updateViewPort();
                ImageService modelImageService = ModelImageServiceDB.getInstance();

                for (Object o : data) {
                    try {
                        WarehouseRemainsItem selectedItem = (WarehouseRemainsItem) o;
                        if (selectedItem != null) {
                            UCImageLabel label = (UCImageLabel) selectedItem.getImage();
                            if (label == null) {
                                label = new UCImageLabel();
                                selectedItem.setImage(label);
                            }
                            try {
                                label.setImageFile(modelImageService.getDefaultImageFileByModelNumber(String.valueOf(selectedItem.getModelNumber()), ModelImageSize.SMALL));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                frameViewPort.updateContent();
                gridViewPort.updateViewPort();
                return true;
            }
        }

        Task task = new Task("Получение остатков ...");
        task.executeTask();
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

    private List<WarehouseRemainsItem> readRemainsOldLine(String path) {

        //DATATTN,C,20	NOMERTTN,C,12	REGISTR,C,70

        //DATA,C,4	SK,N,4,0	SAR,N,8,0	NAR,C,10	NM_IZD,C,30	FAS,N,6,0
        // SOT,N,3,0	KCV,N,3,0	NMCV,C,20
        // SRT,N,1,0	RZM,N,3,0	RST,N,3,0
        // KOLO,N,10,1	KOLP,N,10,1	KOLR,N,10,1	KOL,N,10,1
        // CENO,N,10,2	CENR,N,10,0
        // PTK,N,3,0	NPTK,C,25	PR,C,1


        Object[][] colname = {{"DATA", ""}, {"SK", ""}, {"SAR", ""}, {"NAR", ""}, {"NM_IZD", ""}, {"FAS", ""},
                {"SOT", ""}, {"KCV", ""}, {"NMCV", ""},
                {"SRT", ""}, {"RZM", ""}, {"RST", ""},
                {"KOLO", ""}, {"KOLP", ""}, {"KOLR", ""}, {"KOL", ""},
                {"CENO", ""}, {"CENR", ""},
                {"PTK", ""}, {"NPTK", ""}, {"PR", ""}};

        List<WarehouseRemainsItem> result = new ArrayList<>();
        //DB db = new DB();
        DBFField field = null;
        try {
            InputStream inputStream = new FileInputStream(path);
            DBFReader reader = new com.linuxense.javadbf.DBFReader(inputStream);
            reader.setCharactersetName("cp866");
            System.out.println("Загрузка файла остатков:" + path);
            System.out.println("Количество строк в документе :" + reader.getRecordCount());

            for (int i = 0; i < colname.length; i++) {
                for (int j = 0; j < reader.getFieldCount(); j++) {
                    field = reader.getField(j);
                    if (field.getName().trim().toUpperCase().equals(colname[i][0]))
                        colname[i][1] = j;
                }
                if (colname[i][1].equals(""))
                    JOptionPane.showMessageDialog(null, "Ошибка: в dbf-файле не найден столбец " + colname[i][0], "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            Object obj[] = new Object[reader.getFieldCount()];
            for (int i = 0; i < reader.getRecordCount(); i++) {
                obj = reader.nextRecord();
                WarehouseRemainsItem item = new WarehouseRemainsItem();

                item.setId(i + 1);

                item.setItemName(obj[4].toString().trim());

                item.setModelNumber(Double.valueOf(obj[5].toString()).intValue());
                item.setArticleNumber(obj[3].toString().trim());
                item.setArticleCode(String.format("%.0f", Float.valueOf(obj[2].toString().trim())));

                item.setGrade(Double.valueOf(obj[9].toString()).intValue());
                item.setSize(Double.valueOf(obj[10].toString()).intValue());
                item.setGrowth(Double.valueOf(obj[11].toString()).intValue());

                item.setAmount(Double.valueOf(obj[15].toString()).intValue());
                item.setCost(Double.valueOf(obj[16].toString().trim()));
                item.setItemNamePrint(obj[19].toString().trim());


                if (item.getArticleCode().startsWith("41") || item.getArticleCode().startsWith("42") || item.getArticleCode().startsWith("43")) {
                    result.add(item);
                }

            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<WarehouseRemainsItem> groupBy(List<WarehouseRemainsItem> inputList) {
        List<WarehouseRemainsItem> outputList = new ArrayList<>();
        String key = "";
        try {
            for (WarehouseRemainsItem item : inputList) {

                if (key.equals(item.getArticleNumber() + item.getGrade())) {

                    WarehouseRemainsItem lastItem = outputList.get(outputList.size() - 1);
                    lastItem.setSizePrint(item.getSize());
                    lastItem.setAmountPrint(item.getAmount());
                    lastItem.setGrowthPrint(item.getGrowth());
                    lastItem.setCostPrint(item.getCost());

                } else {
                    item.setSizePrint(item.getSize());
                    item.setAmountPrint(item.getAmount());
                    item.setGrowthPrint(item.getGrowth());
                    item.setCostPrint(item.getCost());
                    outputList.add(item);
                }

                key = item.getArticleNumber() + item.getGrade();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputList;
    }

    private List<WarehouseRemainsItem> amountCalculator(List<WarehouseRemainsItem> list) {
        HashMap<String, Integer> map = new HashMap<>();
        for (WarehouseRemainsItem item : list) {
            // Суммируем по модели
            String keyModel = String.valueOf(item.getModelNumber());
            Integer byModel = map.get(keyModel);
            if (byModel == null) {
                byModel = item.getAmount();
            } else {
                byModel += item.getAmount();
            }
            map.put(keyModel, byModel);

            // Суммируем по артикулу
            String keyArticle = item.getArticleNumber();
            Integer byArticle = map.get(keyArticle);
            if (byArticle == null) {
                byArticle = item.getAmount();
            } else {
                byArticle += item.getAmount();
            }
            map.put(keyArticle, byArticle);
        }

        for (WarehouseRemainsItem item : list) {
            String keyModel = String.valueOf(item.getModelNumber());
            String keyArticle = item.getArticleNumber();
            item.setAmountByModel(map.get(keyModel));
            item.setAmountByArticle(map.get(keyArticle));

            String[] result = SaleDocumentDataProvider.getProductTypeByCategory(item.getArticleCode());
            item.setType(result[0]);
            item.setTypeIndex(Integer.valueOf(result[1]));
        }
        return list;
    }


    private List<WarehouseRemainsItem> grouBy(List<WarehouseRemainsItem> list) {
        return null;
    }

    private List<WarehouseRemainsItem> preSorting(List<WarehouseRemainsItem> list) {
        List<WarehouseRemainsItem> listSource = amountCalculator(list);
        Comparator<WarehouseRemainsItem> comparator = Comparator.comparing(WarehouseRemainsItem::getTypeIndex);
        comparator = comparator.thenComparing(Comparator.comparing(WarehouseRemainsItem::getModelNumber));
        comparator = comparator.thenComparing(Comparator.comparing(WarehouseRemainsItem::getArticleNumber));
        comparator = comparator.thenComparing(Comparator.comparing(WarehouseRemainsItem::getGrade));
        comparator = comparator.thenComparing(Comparator.comparing(WarehouseRemainsItem::getSize));
        comparator = comparator.thenComparing(Comparator.comparing(WarehouseRemainsItem::getGrowth));
        Stream<WarehouseRemainsItem> personStream = listSource.stream().sorted(comparator);
        return groupBy(personStream.collect(Collectors.toList()));
    }
}
