package by.march8.ecs.application.modules.warehouse.external.shipping.manager;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDaoThread;
import by.march8.entities.readonly.ContractorEntityView;
import by.march8.entities.sales.SalesDocumentSimple;
import by.march8.entities.warehouse.SaleDocument;
import by.march8.entities.warehouse.SaleDocumentItem;
import by.march8.entities.warehouse.SaleDocumentItemView;
import common.DateUtils;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author tmp on 16.11.2021 12:08
 */
public class MergingDocumentsProcessor {


    public void doMergingDocuments(Set<SalesDocumentSimple> list, ContractorEntityView refundId, ContractorEntityView saleId) {

        List<SaleDocument> documents = new ArrayList<>();
        SaleDocumentManager manager = new SaleDocumentManager();
        HashMap<Integer, SaleDocumentItemView> map = new HashMap<Integer, SaleDocumentItemView>();

        // Получаем документы в цикле
        for (SalesDocumentSimple item : list) {
            SaleDocument document = manager.getSaleDocumentById(item.getId());
            if (document != null) {
                documents.add(document);
            }

            // для каждого наименование подтащим доп информацию по всем наименованиям в накладной
            List<SaleDocumentItemView> _list;
            try {
                _list = manager.getEntityListByNamedQuery(item.getDocumentNumber());
                if (_list != null) {
                    for (SaleDocumentItemView view : _list) {
                        Integer key = view.getItemId();
                        if (!map.containsKey(key)) {
                            map.put(key, view);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Предустановки
        String operation = "Отгрузка покупателю";

        Date documentDate = new Date();

        String stamp = DateUtils.getTimestampSale(documentDate);

        String numberAdult = stamp + "В";
        String numberChild10 = stamp + "Д10";
        String numberChild20 = stamp + "Д20";
        String numberBandage = stamp + "П";

        // Объединяем документы в мапу/список

        // Создаем 4 документа:
        // ДАТА_ЧАСЫ_В - взрослое      // 1011202112В
        // ДАТА_ЧАСЫ_Д10 - ДЕТСКОЕ 10  // 1011202112Д10
        // ДАТА_ЧАСЫ_Д20 - ДЕТСКОЕ 20  // 1011202112Д20
        // ДАТА_ЧАСЫ_П - ПОВЯЗКИ       // 1011202112П

        SaleDocument source = documents.get(0);

        SaleDocument documentAdult = new SaleDocument(source);
        documentAdult.setId(0);
        documentAdult.setDocumentDate(documentDate);
        documentAdult.setDocumentNumber(numberAdult);
        documentAdult.setDocumentType(operation);
        documentAdult.setDocumentVATType(0);
        documentAdult.setDocumentVatValue(20);
        documentAdult.setDocumentStatus(3);
        documentAdult.setRecipientCode(saleId.getCode());
        documentAdult.setRecipientId(saleId.getId());
        documentAdult.setRecipientContractId(saleId.getContractId());

        SaleDocument documentChild10 = new SaleDocument(source);
        documentChild10.setId(0);
        documentChild10.setDocumentNumber(numberChild10);
        documentChild10.setDocumentDate(documentDate);
        documentChild10.setDocumentType(operation);
        documentChild10.setDocumentVATType(0);
        documentChild10.setDocumentVatValue(10);
        documentChild10.setDocumentStatus(3);
        documentChild10.setRecipientCode(saleId.getCode());
        documentChild10.setRecipientId(saleId.getId());
        documentChild10.setRecipientContractId(saleId.getContractId());

        SaleDocument documentChild20 = new SaleDocument(source);
        documentChild20.setId(0);
        documentChild20.setDocumentDate(documentDate);
        documentChild20.setDocumentNumber(numberChild20);
        documentChild20.setDocumentType(operation);
        documentChild20.setDocumentVATType(0);
        documentChild20.setDocumentVatValue(20);
        documentChild20.setDocumentStatus(3);
        documentChild20.setRecipientCode(saleId.getCode());
        documentChild20.setRecipientId(saleId.getId());
        documentChild20.setRecipientContractId(saleId.getContractId());

        SaleDocument documentBandage = new SaleDocument(source);
        documentBandage.setDocumentDate(documentDate);
        documentBandage.setId(0);
        documentBandage.setDocumentNumber(numberBandage);
        documentBandage.setDocumentType(operation);
        documentBandage.setDocumentVATType(0);
        documentBandage.setDocumentVatValue(20);
        documentBandage.setDocumentStatus(3);
        documentBandage.setRecipientCode(saleId.getCode());
        documentBandage.setRecipientId(saleId.getId());
        documentBandage.setRecipientContractId(saleId.getContractId());

        // В цикле разносим из мапы/списка по документам
        for (SaleDocument document : documents) {
            if (document != null) {
                for (SaleDocumentItem item : document.getDetailList()) {
                    Integer key = item.getItem();
                    // Ищем в мапе изделие
                    if (map.containsKey(key)) {
                        SaleDocumentItemView line = map.get(key);
                        if (line != null) {
                            // Если изделие взрослое
                            if (!SaleDocumentDataProvider.isChildItem(String.valueOf(line.getArticleCode()))) {
                                SaleDocumentItem _item = new SaleDocumentItem(item);
                                _item.setDocument(documentAdult);
                                documentAdult.getDetailList().add(_item);
                            } else {

                                if (line.getNsiVAT() < 20) {
                                    // Если изделие детское с НДС 10
                                    SaleDocumentItem _item = new SaleDocumentItem(item);
                                    _item.setDocument(documentChild10);
                                    documentChild10.getDetailList().add(_item);
                                } else {
                                    // Если изделие детское с НДС 20
                                    SaleDocumentItem _item = new SaleDocumentItem(item);
                                    _item.setDocument(documentChild20);
                                    documentChild20.getDetailList().add(_item);
                                }
                            }
                            // Если изделие Повязка, а надо ли ее формировать отдельно?
                            // Все равно повязки приходЯт и так отдельной накладной

                            /*
                            // Сжижжено с 2178 коммита в RemainsReductionService.java
                            if (line.getArticleCode().startsWith("4589")) {
                                //ТН повязки
                                return 15;
                            }
                            */
                        }
                    }
                }
            }
        }

        String text = "";

        if (documentAdult.getDetailList().size() > 0) {
            text += "<html>Накладная [Взрослый]: <font color=\"green\">" + numberAdult + "</font></html>\n";
            saveDocument(documentAdult);
        }
        if (documentChild10.getDetailList().size() > 0) {
            text += "<html>Накладная [Детский 10% НДС]: <font color=\"green\">" + numberChild10 + "</font></html>\n";
            saveDocument(documentChild10);
        }
        if (documentChild20.getDetailList().size() > 0) {
            text += "<html>Накладная [Детский 20% НДС]: <font color=\"green\">" + numberChild10 + "</font></html>\n";
            saveDocument(documentChild20);
        }

        /// TODO Дописать вывод сообщения пользователю с номерами сформированных накладных 17.11.2021
        JOptionPane.showMessageDialog(null,
                "Документы отгрузки сформированы :\n" + text, "Формирование завершено",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveDocument(SaleDocument document) {
        // Сохраним изменения в базу после расчета документа
        DaoFactory factory = DaoFactory.getInstance();
        ICommonDaoThread dao = factory.getCommonDaoThread();
        // Обновляем документ в базе
        try {
            dao.updateEntityThread(document);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
