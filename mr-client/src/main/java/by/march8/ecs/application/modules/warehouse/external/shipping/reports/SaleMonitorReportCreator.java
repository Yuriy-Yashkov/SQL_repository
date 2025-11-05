package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.march8.api.utils.DatePeriod;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.SaleDocumentJDBC;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentManager;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.ContractorSequencer;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.PresetContractor;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentSheet;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SetMonitorModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Andy 22.01.2019 - 8:27.
 */
public class SaleMonitorReportCreator {
    private SaleDocumentManager manager;

    public SaleMonitorReportCreator() {
        manager = new SaleDocumentManager();
    }

    public void createDocument(int[] contractors, int[] years) {
        List<SaleDocumentSheet> sheets = new ArrayList<>();
        if (contractors != null) {
            for (int contractor : contractors) {
                sheets.add(manager.getSaleDocumentPerContractorAndYear(contractor, years));
            }
        }

        new SaleMonitorReport(sheets);
    }

    public void createMatrix(List<PresetContractor> contractor, int[] models, DatePeriod period) {
        HashMap<String, HashMap<String, HashMap<String, List<SetMonitorModel>>>> modelItemMap = new HashMap<>();
        HashMap<String, HashMap<String, List<SetMonitorModel>>> sizeItemMap;
        HashMap<String, List<SetMonitorModel>> contractorItemMap;
        List<SetMonitorModel> itemList;

        SaleDocumentJDBC db = new SaleDocumentJDBC();
        // Подготовка данных для запроса
        String whereContractors = getStringForList(contractor);
        String whereModels = getStringForList(models);

        List<SetMonitorModel> list = db.getSaleDocumentMonitorForContractorsAndModels(period, whereContractors, whereModels);
        HashMap<String, HashMap<String, String>> modelSizesMap = new HashMap<>();
        HashMap<String, String> modelSizeMap;
        HashMap<Integer, Integer> contractorMap = new HashMap<>();

        if (list != null) {
            for (SetMonitorModel item : list) {
                // Формируем мапу размеров для моделей
                String keyModel = String.valueOf(item.getModelNumber());
                String keySize = item.getSize() + "_" + item.getGrowth();
                String keyContractor = String.valueOf(item.getUnloadingId());
                String keyModelSize = item.getModelNumber() + "_" + item.getSize() + "_" + item.getGrowth();
                contractorMap.put(item.getUnloadingId(), item.getUnloadingId());

                modelSizeMap = modelSizesMap.get(keyModel);
                if (modelSizeMap == null) {
                    modelSizeMap = new HashMap<>();
                    modelSizeMap.put(keySize, keyModel);
                    modelSizesMap.put(keyModel, modelSizeMap);
                } else {
                    modelSizeMap.put(keySize, keyModel);
                }

                sizeItemMap = modelItemMap.get(keyModel);
                if (sizeItemMap == null) {
                    sizeItemMap = new HashMap<>();
                    contractorItemMap = new HashMap<>();
                    itemList = new ArrayList<>();
                    itemList.add(item);
                    contractorItemMap.put(keyContractor, itemList);
                    sizeItemMap.put(keyModelSize, contractorItemMap);
                    modelItemMap.put(keyModel, sizeItemMap);
                } else {
                    contractorItemMap = sizeItemMap.get(keyModelSize);
                    if (contractorItemMap == null) {
                        contractorItemMap = new HashMap<>();
                        itemList = new ArrayList<>();
                        itemList.add(item);
                        contractorItemMap.put(keyContractor, itemList);
                        sizeItemMap.put(keyModelSize, contractorItemMap);
                    } else {
                        itemList = contractorItemMap.get(keyContractor);
                        if (itemList != null) {
                            itemList.add(item);
                        } else {
                            itemList = new ArrayList<>();
                            itemList.add(item);
                            contractorItemMap.put(keyContractor, itemList);
                        }
                    }
                }
            }

            List<ContractorSequencer> contractorSequencer = db.getContractorsByAddressId(getStringForIterator(contractorMap.entrySet().iterator()));
            new SaleMonitorMatrixReport(models, contractorSequencer, modelItemMap, modelSizesMap);
        }
    }

    private String getStringForIterator(Iterator iterator) {
        StringBuilder result = new StringBuilder();
        while (iterator.hasNext()) {
            Map.Entry item = (Map.Entry) iterator.next();
            result.append(item.getKey()).append(",");

        }
        String result_ = result.toString();
        return result_.substring(0, result_.length() - 1);
    }

    private String getStringForList(List<PresetContractor> list) {
        StringBuilder result = new StringBuilder();
        for (PresetContractor item : list) {
            result.append(item.getCode()).append(",");
        }
        String result_ = result.toString();
        return result_.substring(0, result_.length() - 1);
    }

    private String getStringForList(int[] list) {
        StringBuilder result = new StringBuilder();
        for (int item : list) {
            result.append(item).append(",");
        }
        String result_ = result.toString();
        return result_.substring(0, result_.length() - 1);
    }
}
