package by.march8.tasks.mnsati.logic;

import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentDataProvider;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.TotalSummingUp;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import by.march8.entities.warehouse.SaleDocumentEntity;
import by.march8.tasks.mnsati.model.Consignee;
import by.march8.tasks.mnsati.model.ConsigneeList;
import by.march8.tasks.mnsati.model.Consignor;
import by.march8.tasks.mnsati.model.ConsignorList;
import by.march8.tasks.mnsati.model.Contract;
import by.march8.tasks.mnsati.model.DeliveryCondition;
import by.march8.tasks.mnsati.model.DocType;
import by.march8.tasks.mnsati.model.Document;
import by.march8.tasks.mnsati.model.DocumentList;
import by.march8.tasks.mnsati.model.ForInvoiceType;
import by.march8.tasks.mnsati.model.General;
import by.march8.tasks.mnsati.model.InvoiceDocType;
import by.march8.tasks.mnsati.model.Issuance;
import by.march8.tasks.mnsati.model.Provider;
import by.march8.tasks.mnsati.model.ProviderStatusType;
import by.march8.tasks.mnsati.model.RateType;
import by.march8.tasks.mnsati.model.Recipient;
import by.march8.tasks.mnsati.model.RecipientStatusType;
import by.march8.tasks.mnsati.model.RosterItem;
import by.march8.tasks.mnsati.model.RosterList;
import by.march8.tasks.mnsati.model.SenderReceiver;
import by.march8.tasks.mnsati.model.TaxesType;
import by.march8.tasks.mnsati.model.Vat;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Класс-конструктор электронных счетов-фактур.
 * <p>
 * Содержит вспомогательные методы управления ходом формирования документа.
 *
 * @author Andy 03.05.2016.
 */
public class IssuanceCreator {

    private SaleDocumentDataProvider provider;
    private String exportPath;
    private String exportPathCurrency;

    /**
     * Конструктор
     * <p>
     * Аргументом передается каталог для сохранения документа/ов
     *
     * @param exportPath каталог для сохранения документа/ов
     */
    public IssuanceCreator(final String exportPath) {
        this.exportPath = exportPath;
        provider = new SaleDocumentDataProvider();
    }

    /**
     * Главный метод выполняющий формирование электронной счет-фактуры
     *
     * @param entity идентификатор документа
     */
    public boolean createIssuance(SaleDocumentEntity entity) throws Exception {
        SaleDocumentReport report = provider.prepareDocument(entity.getId(), false);
        int accuracy = 2;
        if (report.getDocument().getRecipientCode() == 2258) {
            accuracy = 4;
        }

        boolean result = create(report, false, accuracy);

        if (!result) {
            throw new Exception("Ошибка создания XML");
        }

        // Для экспортных выгружаем в валюте
        if (entity.getIsExport() == 1) {
            result = create(report, true, accuracy);
        }

        if (!result) {
            throw new Exception("Ошибка создания XML для валютной накладной");
        }

        return true;
    }

    /**
     * Метод формирует номер документа ЕСЧФ по номеру УНП в параметре
     *
     * @param unpNumber номер УНП
     * @return номер документа ЕСЧФ
     */
    public String createESCFName(String unpNumber) {
        String result = "";
        String unpText;
        if (unpNumber != null) {
            unpText = unpNumber.trim();
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            String uniqueNumber = getUniqueNumber(year);
            result = unpText + "-" + String.valueOf(year) + "-" + uniqueNumber;
        }
        return result;
    }

    /**
     * Метод возвращает 10-ти значное число, уникальное в пределах года
     * Получение уникального номера будет производиться из БД
     *
     * @param yearId Идентификатор года(2015-9999)
     * @return 10-ти значный номер
     */
    @SuppressWarnings("unused")
    public String getUniqueNumber(int yearId) {
        // Номер будет получен из БД, временно статический
        long number = 1;
        // Доведение номера до 10 символов путем добавления 0 вначале номера
        String temp = String.valueOf(number);
        int symbolCount = temp.length();
        int freeCount = 10 - symbolCount;
        String freeNumber = "";

        for (int i = 0; i < freeCount; i++) {
            freeNumber += "0";
        }

        return freeNumber + temp;
    }

    /**
     * Главный метод формирования ЕСЧФ документа
     */
    public boolean create(SaleDocumentReport document, boolean isCurrency, int accuracy) {

        HashMap<String, Object> map = document.getDetailMap();
        if (map == null) {
            //System.out.println("Ошибка получения реквизитов документа [" + document.getDocument().getDocumentNumber() + "]");
            return false;
        }

        String documentNumber = createESCFName((String) map.get("SELF_UNP"));
        if (documentNumber.equals("")) {
            //System.out.println("Ошибка формирования ЕСЧФ номера для документа " + document.getDocument().getDocumentNumber());
            return false;
        }

        Issuance issuance;

        try {
            // Основной объект документа
            issuance = new Issuance();

            //************************************************************************
            // ОБЩИЙ РАЗДЕЛ
            //************************************************************************

            // Первичные данные по документу
            General general = new General();
            // 1
            general.setNumber(documentNumber);

            Date dateSale;

            try {
                dateSale = DateUtils.getDateByStringValue((String) map.get("DOCUMENT_DATE"));
            } catch (Exception ed) {
                dateSale = document.getDocument().getDocumentSaleDate();
            }

            // 2
            general.setDateTransaction(DateUtils.getGregorianDate(dateSale));
            // 3
            //general.set
            // TODO НЕТ ДАННЫХ В XSD СХЕМЕ ДЛЯ ДАННОГО ПОЛЯ

            // 4
            general.setDocumentType(InvoiceDocType.ORIGINAL);

            // 5
            //general.setInvoice(null);

            // 5.1
            //general.getDateCancelled(null);

            //general.setDateCancelled(date2);

            //general.setDateTransaction(date2);
            issuance.setGeneral(general);

            //************************************************************************
            // РЕКВИЗИТЫ ПОСТАЩИКА
            //************************************************************************
            Provider provider = new Provider();
            // 6
            provider.setProviderStatus(ProviderStatusType.SELLER);
            // 6.1
            provider.setDependentPerson(false);
            // 6.2
            provider.setResidentsOfOffshore(false);
            // 6.3
            provider.setSpecialDealGoods(false);
            // 6.4
            provider.setBigCompany(false);
            // 7
            provider.setCountryCode(new BigInteger(String.valueOf(112)));

            // 8
            String providerUnp = (String) map.get("SENDER_UNP");
            if (providerUnp != null) {
                providerUnp = providerUnp.trim();
            }

            issuance.setSender(providerUnp);

            provider.setUnp(providerUnp);
            // 9
            String providerName = (String) map.get("SENDER_NAME");
            if (providerName != null) {
                providerName = providerName.trim();
            }

            provider.setName(providerName != null ? providerName.replace('"', ' ') : null);

            // 10
            String providerAddress = (String) map.get("SENDER_ADDRESS");
            if (providerAddress != null) {
                providerAddress = providerAddress.trim();
            }
            provider.setAddress(providerAddress != null ? providerAddress.replace('"', ' ') : null);

            //11-1
            //11-2
            ForInvoiceType principal = new ForInvoiceType();
            principal.setNumber("");
            principal.setDate(DateUtils.getGregorianDate(new Date()));
            //provider.setPrincipal(principal);

            //12-1
            //12-2
            ForInvoiceType vendor = new ForInvoiceType();
            vendor.setNumber("");
            vendor.setDate(DateUtils.getGregorianDate(new Date()));
            //provider.setVendor(vendor);

            //13-1
            provider.setDeclaration("");
            //13-2
            provider.setDateRelease(DateUtils.getGregorianDate(new Date()));


            //13.1
            // TODO НЕТ ДАННЫХ В XSD СХЕМЕ ДЛЯ ДАННОГО ПОЛЯ

            //13.2
            provider.setDateActualExport(DateUtils.getGregorianDate(new Date()));

            //14-1
            //14-2
            TaxesType taxes = new TaxesType();
            taxes.setNumber("");
            taxes.setDate(DateUtils.getGregorianDate(new Date()));
            //provider.setTaxes(taxes);

            issuance.setProvider(provider);

            //************************************************************************
            // РЕКВИЗИТЫ ПОЛУЧАТЕЛЯ
            //************************************************************************
            Recipient recipient = new Recipient();
            //15
            recipient.setRecipientStatus(RecipientStatusType.CUSTOMER);
            //15.1
            recipient.setDependentPerson(false);
            //15.2
            recipient.setResidentsOfOffshore(false);
            //15.3
            recipient.setSpecialDealGoods(false);
            //15.4
            recipient.setBigCompany(false);
            //16
            recipient.setCountryCode(new BigInteger(String.valueOf(643)));

            //17
            String recipientUnp = (String) map.get("RECIPIENT_UNP");
            if (recipientUnp != null) {
                recipientUnp = recipientUnp.trim();
            }
            recipient.setUnp(recipientUnp);

            //18
            String recipientName = (String) map.get("RECIPIENT_NAME");
            if (recipientName != null) {
                recipientName = recipientName.trim();
            }
            recipient.setName(recipientName != null ? recipientName.replace('"', ' ') : null);

            //19
            String recipientAddress = (String) map.get("RECIPIENT_ADDRESS");
            if (recipientAddress != null) {
                recipientAddress = recipientAddress.trim();
            }

            recipient.setBranchCode(String.valueOf(document.getDocument().getRecipientCode()));

            recipient.setAddress(recipientAddress != null ? recipientAddress.replace('"', ' ') : null);
            //20-1
            recipient.setDeclaration(null);
            //20-2
            recipient.setDateImport(DateUtils.getGregorianDate(new Date()));

            //21-1
            //21-2
            TaxesType taxesRecipient = new TaxesType();
            taxesRecipient.setNumber("");
            taxesRecipient.setDate(DateUtils.getGregorianDate(new Date()));
            //recipient.setTaxes(taxesRecipient);

            //21.1
            // TODO НЕТ ДАННЫХ В XSD СХЕМЕ ДЛЯ ДАННОГО ПОЛЯ

            issuance.setRecipient(recipient);

            //************************************************************************
            // РЕКВИЗИТЫ ГРУЗООТПРАВИТЕЛЯ
            //************************************************************************

            SenderReceiver senderReceiver = new SenderReceiver();

            // Грузоотправитель
            ConsignorList consignorList = new ConsignorList();
            Consignor consignor = new Consignor();
            consignorList.getConsignor().add(consignor);

            //22
            consignor.setCountryCode(new BigInteger(String.valueOf(112)));
            //23
            consignor.setUnp(providerUnp);
            // 24
            consignor.setName(providerName != null ? providerName.replace('"', ' ') : null);
            // 25
            String loadingAddress = (String) map.get("LOADING_ADDRESS");
            if (loadingAddress != null) {
                loadingAddress = loadingAddress.trim();
            }
            consignor.setAddress(loadingAddress != null ? loadingAddress.replace('"', ' ') : null);

            //************************************************************************
            // РЕКВИЗИТЫ ГРУЗОПОЛУЧАТЕЛЯ
            //************************************************************************
            ConsigneeList consigneeList = new ConsigneeList();
            Consignee consignee = new Consignee();
            consigneeList.getConsignee().add(consignee);

            // 26
            consignee.setCountryCode(new BigInteger(String.valueOf(112)));
            // 27
            consignee.setUnp(recipientUnp);
            // 28
            consignee.setName(recipientName != null ? recipientName.replace('"', ' ') : null);
            // 29
            String unloadingAddress = (String) map.get("UNLOADING_ADDRESS");
            if (unloadingAddress != null) {
                unloadingAddress = unloadingAddress.trim();
            }
            consignee.setAddress(unloadingAddress != null ? unloadingAddress.replace('"', ' ') : null);
            // senderReceiver
            senderReceiver.setConsignors(consignorList);
            senderReceiver.setConsignees(consigneeList);
            issuance.setSenderReceiver(senderReceiver);

            //************************************************************************
            // УСЛОВИЯ ПОСТАВКИ
            //************************************************************************
            DeliveryCondition condition = new DeliveryCondition();
            Contract contract = new Contract();
            //30-1
            String txtContract = (String) map.get("CONTRACT_NUMBER");
            contract.setNumber(txtContract);

            // TODO ПАРСИТЬ CONTRACT_NAME ИЛИ ТАЩИТЬ ИЗ БД
            contract.setDate(DateUtils.getGregorianDate((Date) map.get("CONTRACT_DATE_BEGIN_AS_DATE")));

            DocumentList documentList = new DocumentList();
            // Товарная накладная
            Document doc = new Document();

            DocType docType = new DocType();

            String carNumber = (String) map.get("CAR_NUMBER");
            // Если указаны номера автомобиля грузоперевозчика, принимаем тип документа ТТН-1
            if (carNumber != null) {
                if (!carNumber.trim().equals("")) {
                    docType.setCode(new BigInteger(String.valueOf(603)));
                    docType.setValue("ТТН-1");
                } else {
                    docType.setCode(new BigInteger(String.valueOf(602)));
                    docType.setValue("ТН-2");
                }
            } else {
                docType.setCode(new BigInteger(String.valueOf(603)));
                docType.setValue("ТТН-1");
            }


            doc.setDocType(docType);

            // Парсим номер накладной и разносим по полям
            doc.setBlankCode(docType.getValue());

            String docNumber = document.getDocument().getDocumentNumber();
            char c = docNumber.charAt(0);
            boolean isDigit = (c >= '0' && c <= '9');
            String batch;
            String number;
            if (isDigit) {
                number = docNumber;
                batch = "**";
            } else {
                number = docNumber.substring(2);
                batch = docNumber.substring(0, 2);
            }

            doc.setNumber(number);
            doc.setSeria(batch);

            // Дата закрытия накладной
            doc.setDate(DateUtils.getGregorianDate(dateSale));

            documentList.getDocument().add(doc);
            contract.setDocuments(documentList);
            condition.setContract(contract);
            issuance.setDeliveryCondition(condition);


            //************************************************************************
            // ДАННЫЕ ПО ТОВАРАМ (ТАБЛИЧНАЯ ЧАСТЬ)
            //************************************************************************
            RosterList list = new RosterList();
            final List<RosterItem> rosterList = list.getRosterItem();
            int count = 0;
            for (SaleDocumentDetailItemReport item : document.getDetailList()) {
                count++;
                RosterItem newItem = new RosterItem();
                //1
                newItem.setNumber(new BigInteger(String.valueOf(count)));

                //2
                // ЕСли полотно то выводим только наименование
                String name = item.getItemName() + " " + item.getItemColor() + " разм. " + item.getItemSizePrint() + "(" + item.getModelNumber() + ")";
                if (item.getMeasureUnitAsCode() == 166) {
                    name = item.getItemName() + " " + item.getItemColor();
                }
                newItem.setName(name);

                //3.1
                String code = item.getTnvedCode();
                if (code.length() < 4) {
                    code = "";
                }

                newItem.setCode(code);
                //3.2
                //newItem.setCodeOced(new BigInteger(String.valueOf("0")));
                // 4
                newItem.setUnits(new BigInteger(String.valueOf(item.getMeasureUnitAsCode())));
                //5
                newItem.setCount(new BigDecimal(item.getAmountPrint()).setScale(2, BigDecimal.ROUND_HALF_UP));

                if (!isCurrency) {
                    //6
                    newItem.setPrice(new BigDecimal(item.getValuePrice()).setScale(accuracy, BigDecimal.ROUND_HALF_UP));
                    //7
                    newItem.setCost(new BigDecimal(item.getValueSumCost()).setScale(accuracy, BigDecimal.ROUND_HALF_UP));
                    //8
                    newItem.setSummaExcise(new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP));
                    //9
                    Vat vat = new Vat();

                    vat.setRate(new BigDecimal(item.getValueVAT()).setScale(2, BigDecimal.ROUND_HALF_UP));

                    if (item.getValueVAT() < 1) {
                        vat.setRateType(RateType.ZERO);
                    } else {
                        vat.setRateType(RateType.DECIMAL);
                    }

                    vat.setSummaVat(new BigDecimal(item.getValueSumVat()).setScale(accuracy, BigDecimal.ROUND_HALF_UP));
                    newItem.setVat(vat);
                    //10
                    newItem.setCostVat(new BigDecimal(item.getValueSumCostAndVat()).setScale(accuracy, BigDecimal.ROUND_HALF_UP));
                } else {
                    // Иначе, если накладная экспортная, данные по ценам будут в валюте
                    //6
                    newItem.setPrice(new BigDecimal(item.getValuePriceCurrency()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    //7
                    newItem.setCost(new BigDecimal(item.getValueSumCostCurrency()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    //8
                    newItem.setSummaExcise(new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP));
                    //9
                    Vat vat = new Vat();

                    vat.setRate(new BigDecimal(item.getValueVAT()).setScale(2, BigDecimal.ROUND_HALF_UP));

                    if (item.getValueVAT() < 1) {
                        vat.setRateType(RateType.ZERO);
                    } else {
                        vat.setRateType(RateType.DECIMAL);
                    }

                    vat.setSummaVat(new BigDecimal(item.getValueSumVatCurrency()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    newItem.setVat(vat);
                    //10
                    newItem.setCostVat(new BigDecimal(item.getValueSumCostAndVatCurrency()).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                //11
                // TODO НЕТ ДАННЫХ В XSD СХЕМЕ ДЛЯ ДАННОГО ПОЛЯ ИЛИ ПОЛЕ ВЫЧИСЛЯЕМОЕj
                //12
                //newItem.setDescriptions();
                rosterList.add(newItem);
            }

            TotalSummingUp summingUp = document.getSummingUp();

            if (!isCurrency) {
                // Сумма без НДС
                list.setTotalCost(new BigDecimal(summingUp.getValueSumCost()).setScale(accuracy, BigDecimal.ROUND_HALF_UP));
                // Сумма НДС
                list.setTotalVat(new BigDecimal(summingUp.getValueSumVat()).setScale(accuracy, BigDecimal.ROUND_HALF_UP));
                // Сумма с НДС
                list.setTotalCostVat(new BigDecimal(summingUp.getValueSumCostAndVat()).setScale(accuracy, BigDecimal.ROUND_HALF_UP));
            } else {
                // Сумма без НДС
                list.setTotalCost(new BigDecimal(summingUp.getValueSumCostCurrency()).setScale(2, BigDecimal.ROUND_HALF_UP));
                // Сумма НДС
                list.setTotalVat(new BigDecimal(summingUp.getValueSumVatCurrency()).setScale(2, BigDecimal.ROUND_HALF_UP));
                // Сумма с НДС
                list.setTotalCostVat(new BigDecimal(summingUp.getValueSumCostAndVatCurrency()).setScale(2, BigDecimal.ROUND_HALF_UP));
            }

            // Сумма акциз
            list.setTotalExcise(new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP));

            issuance.setRoster(list);


        } catch (Exception e) {
            // System.out.println("Ошибка заполнения XML структуры " + document.getDocument().getDocumentNumber());
            e.printStackTrace();
            return false;
        }
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Issuance.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            if (!isCurrency) {
                jaxbMarshaller.marshal(issuance, new File(exportPath + document.getDocument().getDocumentNumber() + ".xml"));
            } else {
                jaxbMarshaller.marshal(issuance, new File(exportPathCurrency + document.getDocument().getDocumentNumber() + ".xml"));
            }

            return true;
            //jaxbMarshaller.marshal( issuance, System.out );
        } catch (Exception e) {
            //System.out.println("Ошибка сохранения XML структуры " + document.getDocument().getDocumentNumber());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ОТЛАДКА: Загрузка XML счета-фактуры, формирование объекта Issuance на его основе
     * <p>
     * и сохраняет его в виде XML
     */
    public void createObject() {
        try {
            File file = new File("C:\\text.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Issuance.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            Issuance issuance = (Issuance) jaxbUnmarshaller.unmarshal(file);
            saveIssuance(issuance);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ОТЛАДКА: Сохраняет объект счета фактуры в XML файл
     *
     * @param issuance счет-фактура
     */
    public void saveIssuance(Issuance issuance) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Issuance.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.marshal(issuance, new File("C:\\text_parse.xml"));
            //jaxbMarshaller.marshal( issuance, System.out );
        } catch (Exception e) {
            //System.out.println("Ошибка сохранения XML структуры " + document.getDocument().getDocumentNumber());
            e.printStackTrace();
        }
    }

    public void setExportPathCurrency(final String exportPathCurrency) {
        this.exportPathCurrency = exportPathCurrency;
    }
}
