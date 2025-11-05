package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.gomel.freedev.ucframework.uccore.report.enums.FontStyle;
import by.gomel.freedev.ucframework.uccore.report.model.ReportProperties;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.ContractorSequencer;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SetMonitorModel;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SizeSequencer;
import com.sun.star.container.XNamed;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.CellRangeAddress;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SaleMonitorMatrixReport extends AbstractInvoiceReport {
    private final HashMap<String, HashMap<String, String>> modelSizesMap;
    private final HashMap<String, HashMap<String, HashMap<String, List<SetMonitorModel>>>> modelItemMap;
    private final List<ContractorSequencer> contractorMap;
    private int[] models;
    private HashMap<String, String> modelSizeMap;
    private HashMap<String, HashMap<String, List<SetMonitorModel>>> sizeItemMap;
    private HashMap<String, List<SetMonitorModel>> contractorItemMap;
    private List<SetMonitorModel> itemList;

    public SaleMonitorMatrixReport(int[] models, List<ContractorSequencer> contractorMap, HashMap<String,
                                           HashMap<String, HashMap<String, List<SetMonitorModel>>>> modelItemMap,
                                   HashMap<String, HashMap<String, String>> modelSizesMap) {
        super();
        this.models = models;
        this.contractorMap = contractorMap;
        this.modelItemMap = modelItemMap;
        this.modelSizesMap = modelSizesMap;
        createCustom();
    }

    @Override
    ReportProperties prepareProperties(ReportProperties properties) {
        // Устанавливаем имя файла шаблона
        properties.setBlankName("sale_monitor_matrix_" + DateUtils.getNormalDateTimeFormatPlus(new Date()));
        properties.getTemplate().setTemplateName("saleMonitorMatrix.ots");
        return properties;
    }

    @Override
    boolean populateData(XComponent component) {
        List<CellRangeAddress> list_ = new ArrayList<>();
        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime
                    .queryInterface(XSpreadsheetDocument.class, component);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Лист1");
            XSpreadsheet xSpreadsheet = UnoRuntime
                    .queryInterface(XSpreadsheet.class, sheet);

            XNamed xNamed = UnoRuntime.queryInterface(XNamed.class,
                    xSpreadsheet);

            xNamed.setName("Монитор отгрузок");

            XCell xCell = xSpreadsheet.getCellByPosition(2, 0);

            int row = 0;
            int total = 0;
            int column = 0;
            int begin = row;
            boolean empty = false;
            for (int i : models) {
                total = 0;
                sizeItemMap = modelItemMap.get(String.valueOf(i));
                modelSizeMap = modelSizesMap.get(String.valueOf(i));
                empty = false;
                //Заголовок для модели
                if (sizeItemMap != null) {
                    List<SizeSequencer> sizes = getSizeSequences(modelSizeMap.entrySet().iterator());
                    if (sizes.size() > 0) {
                        begin = row;
                        mergeCellRange(xSpreadsheet, 3, row, sizes.size() + 2, row);
                        xCell = xSpreadsheet.getCellByPosition(3, row);
                        xCell.setFormula("Модель :" + i);
                        setCharHeightForCell(xCell, 12, FontStyle.Bold);
                        setFullBorderForCellRange(xSpreadsheet, 0, sizes.size() + 3, row, row + contractorMap.size() + 1);
                        row++;
                        //setFullBorderForCell(xSpreadsheet, 0, sizes.size() + 3, row);
                        //Пробросим росторазмеры для модели
                        column = 3;
                        for (SizeSequencer size : sizes) {
                            xCell = xSpreadsheet.getCellByPosition(column, row);
                            xCell.setFormula("'" + size.getValue().replace("_", "-"));
                            setCharHeightForCell(xCell, 8, FontStyle.Bold);
                            column++;
                        }
                        xCell = xSpreadsheet.getCellByPosition(column, row);
                        xCell.setFormula("Всего");
                        row++;

                        // Пробросим контрагентов с адресами
                        int index = 0;
                        for (ContractorSequencer contractor : contractorMap) {
                            xCell = xSpreadsheet.getCellByPosition(0, row + index);
                            xCell.setValue(index + 1);
                            xCell = xSpreadsheet.getCellByPosition(1, row + index);
                            xCell.setFormula(contractor.getName() + "\n" + contractor.getAddress());
                            xCell = xSpreadsheet.getCellByPosition(2, row + index);
                            xCell.setValue(contractor.getCode());
                            index++;
                            //setRowHeight(xSpreadsheet, row-index, 100);
                        }

                        for (ContractorSequencer contractor : contractorMap) {
                            column = 3;
                            int value = 0;
                            //setFullBorderForCell(xSpreadsheet, 0, sizes.size() + 3, row);
                            for (SizeSequencer size : sizes) {

                                contractorItemMap = sizeItemMap.get(String.valueOf(i) + "_" + size.getValue());
                                itemList = contractorItemMap.get(String.valueOf(contractor.getId()));
                                if (itemList != null) {
                                    xCell = xSpreadsheet.getCellByPosition(column, row);
                                    xCell.setValue(getSumAmount(itemList));
                                    value += getSumAmount(itemList);
                                }
                                column++;
                            }
                            xCell = xSpreadsheet.getCellByPosition(column, row);
                            xCell.setValue(value);
                            total += value;
                            row++;
                        }
                    } else {
                        System.out.println("Пустая шкала модели " + i);
                        empty = true;
                    }
                } else {
                    empty = true;
                    System.out.println("Пустая шкала модели " + i);
                }
                try {
                    if (!empty) {
                        mergeCellRange(xSpreadsheet, column - 6, row, column - 1, row);
                        xCell = xSpreadsheet.getCellByPosition(column - 6, row);
                        xCell.setFormula("Модель " + i + " ∑");
                        setCharHeightForCell(xCell, 9, FontStyle.Bold);
                        xCell = xSpreadsheet.getCellByPosition(column, row);

                        xCell.setValue(total);
                        setCharHeightForCell(xCell, 9, FontStyle.Bold);

                        //insertBreakPage(xSpreadsheetDocument,xSpreadsheet,xCell, row);
                        list_.add(getCellRanges(begin, row, 0, column));
                        row += 2;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            CellRangeAddress[] ranges = new CellRangeAddress[list_.size()];
            for (int i = 0; i < list_.size(); i++) {
                ranges[i] = list_.get(i);
            }

            insertBreakPage(xSpreadsheet, ranges);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int getSumAmount(List<SetMonitorModel> list) {
        if (list != null) {
            int amount = 0;
            for (SetMonitorModel item : list) {
                amount += item.getAmount();
            }
            return amount;
        } else {
            return 0;
        }
    }

    public List<SizeSequencer> getSizeSequences(Iterator iterator) {
        List<SizeSequencer> result = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry item = (Map.Entry) iterator.next();
            result.add(new SizeSequencer((String) item.getKey()));

        }
        Comparator<SizeSequencer> comparator = Comparator.comparing(SizeSequencer::getSize);
        comparator = comparator.thenComparing(Comparator.comparing(SizeSequencer::getGrowth));
        Stream<SizeSequencer> stream = result.stream().sorted(comparator);
        return stream.collect(Collectors.toList());
    }

}
