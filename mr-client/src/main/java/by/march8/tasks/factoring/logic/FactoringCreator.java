package by.march8.tasks.factoring.logic;

import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentDataProvider;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.TotalSummingUp;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import by.march8.entities.warehouse.SaleDocumentEntity;
import by.march8.tasks.factoring.model.blrwbl.BLRWBL;
import by.march8.tasks.factoring.model.blrwbl.Carrier;
import by.march8.tasks.factoring.model.blrwbl.DeliveryNote;
import by.march8.tasks.factoring.model.blrwbl.DespatchAdviceLogisticUnitLineItem;
import by.march8.tasks.factoring.model.blrwbl.FreightPayer;
import by.march8.tasks.factoring.model.blrwbl.LineItem;
import by.march8.tasks.factoring.model.blrwbl.MessageHeader;
import by.march8.tasks.factoring.model.blrwbl.Receiver;
import by.march8.tasks.factoring.model.blrwbl.ShipFrom;
import by.march8.tasks.factoring.model.blrwbl.ShipTo;
import by.march8.tasks.factoring.model.blrwbl.Shipper;
import org.apache.commons.io.FileUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Andy 27.02.2018.
 */
public class FactoringCreator {
    private SaleDocumentDataProvider provider;
    private String exportPath;
    private String exportPathCurrency;
    private List<SaleDocumentDetailItemReport> errorList;

    /**
     * Конструктор
     * <p>
     * Аргументом передается каталог для сохранения документа/ов
     *
     * @param exportPath каталог для сохранения документа/ов
     */
    public FactoringCreator(final String exportPath) {
        this.exportPath = exportPath;
        provider = new SaleDocumentDataProvider();
        errorList = new ArrayList<>();
    }

    public boolean createIssuance(SaleDocumentEntity entity) throws Exception {
        SaleDocumentReport report = provider.prepareDocument(entity.getId(), false);
        boolean result = create(report);

        if (!result) {
            throw new Exception("Ошибка создания XML");
        }

        return true;
    }

    public String getASCIIByString(String s) {
        String tmp = s.toUpperCase();
        char ch = tmp.charAt(0);
        switch (ch) {
            case 'А':
                return "192";
            case 'Б':
                return "193";
            case 'В':
                return "194";
            case 'Г':
                return "195";
            case 'Д':
                return "196";
            case 'Е':
                return "197";
            case 'Ж':
                return "198";
            case 'З':
                return "199";
            case 'И':
                return "200";
            case 'Й':
                return "201";
            case 'К':
                return "202";
            case 'Л':
                return "203";
            case 'М':
                return "204";
            case 'Н':
                return "205";
            case 'О':
                return "206";
            case 'П':
                return "207";
            case 'Р':
                return "208";
            case 'С':
                return "209";
            case 'Т':
                return "210";
            case 'У':
                return "211";
            case 'Ф':
                return "212";
            case 'Х':
                return "213";
            case 'Ц':
                return "214";
            case 'Ч':
                return "215";
            case 'Ш':
                return "216";
            case 'Щ':
                return "217";
            case 'Ъ':
                return "218";
            case 'Ы':
                return "219";
            case 'Ь':
                return "220";
            case 'Э':
                return "221";
            case 'Ю':
                return "222";
            case 'Я':
                return "223";
        }
        return "000";
    }

    private String getDeliveryNoteID(String number) {
        String uid = "4815363900005";
        String s1 = number.substring(0, 1);
        String s2 = number.substring(1, 2);

        String num = number.substring(2, number.length());
        //System.out.println(s1+"-"+s2+"-"+num);
        return "001-" + uid + "-" + getASCIIByString(s1) + getASCIIByString(s2) + "0" + num;
    }


    public boolean create(SaleDocumentReport document) throws Exception {
        HashMap<String, Object> map = document.getDetailMap();
        if (map == null) {
            //System.out.println("Ошибка получения реквизитов документа [" + document.getDocument().getDocumentNumber() + "]");
            return false;
        }

        String documentNumber = "123";//createESCFName((String) map.get("SELF_UNP"));
        if (documentNumber.equals("")) {
            //System.out.println("Ошибка формирования ЕСЧФ номера для документа " + document.getDocument().getDocumentNumber());
            return false;
        }

        Date dateSale = DateUtils.getDateByStringValue((String) map.get("DOCUMENT_DATE"));
        BLRWBL issuance = null;
        try {

            String timeStamp = DateUtils.getMessageDateTime(new Date());
            issuance = new BLRWBL();
            MessageHeader header = new MessageHeader();
            issuance.setMessageHeader(header);
            issuance.setVersion("0.1");
            DeliveryNote delivery = new DeliveryNote();
            issuance.setDeliveryNote(delivery);

            // Заголовок
            header.setTestIndicator("1");
            header.setMessageID("222");
            header.setMessageType("BLRWBL");
            header.setMsgSenderID("4815363900005");
            header.setMsgReceiverID("2222633999997");
            header.setMsgDateTime(timeStamp);

            // Тело документа

            SaleDocumentBase doc = document.getDocument();

            delivery.setDeliveryNoteType("700");
            delivery.setDocumentID(String.valueOf(doc.getId()));
            delivery.setCreationDateTime(timeStamp);
            delivery.setFunctionCode("9");
            delivery.setDeliveryNoteID(getDeliveryNoteID(doc.getDocumentNumber()));
            delivery.setDeliveryNoteDate(DateUtils.getMessageDateTimeShort(dateSale));

            delivery.setContractID((String) map.get("CONTRACT_NUMBER"));
            delivery.setContractDate(DateUtils.getMessageDateTimeShort((Date) map.get("CONTRACT_DATE_BEGIN_AS_DATE")));

            String waybillID = (String) map.get("DOCUMENT_NUMBER");
            if (waybillID == null) {
                waybillID = "0";
            }

            if (waybillID.trim().equals("")) {
                waybillID = "0";
            }
            delivery.setWaybillID(waybillID);

            DeliveryNote.Document doc_ = new DeliveryNote.Document();
            doc_.setDocumentDate(DateUtils.getMessageDateTimeShort(dateSale));
            doc_.setDocumentID(doc.getDocumentNumber());
            doc_.setDocumentName("БСО");

            delivery.getDocument().add(doc_);

            Shipper shipper = new Shipper();
            shipper.setGLN("4815363900005");
            shipper.setName((String) map.get("SENDER_NAME"));
            shipper.setAddress((String) map.get("SENDER_ADDRESS"));
            shipper.setVATRegistrationNumber(((String) map.get("SENDER_UNP")).trim());
            shipper.setContact((String) map.get("SALE_ALLOWED"));
            delivery.setShipper(shipper);

            Receiver receiver = new Receiver();
            receiver.setGLN("2222633999997");
            receiver.setName((String) map.get("RECIPIENT_NAME"));
            receiver.setAddress((String) map.get("RECIPIENT_ADDRESS"));
            receiver.setVATRegistrationNumber(((String) map.get("RECIPIENT_UNP")).trim());
            delivery.setReceiver(receiver);


            FreightPayer payer = new FreightPayer();
            payer.setGLN("4815363900005");
            payer.setName((String) map.get("SENDER_NAME"));
            payer.setAddress((String) map.get("SENDER_ADDRESS"));
            payer.setVATRegistrationNumber(((String) map.get("SENDER_UNP")).trim());
            delivery.setFreightPayer(payer);

            ShipFrom shipFrom = new ShipFrom();
            shipFrom.setGLN("4815363900005");
            shipFrom.setAddress((String) map.get("LOADING_ADDRESS"));// SALE_ALLOWED
            shipFrom.setContact((String) map.get("SALE_ALLOWED"));
            delivery.setShipFrom(shipFrom);

            ShipTo shipTo = new ShipTo();
            shipTo.setGLN("2222633000228");
            shipTo.setAddress((String) map.get("UNLOADING_ADDRESS"));// SALE_ALLOWED
            delivery.setShipTo(shipTo);

            Carrier carrier = new Carrier();
            carrier.setTransportContact((String) map.get("CAR_DRIVER_NAME"));
            carrier.setDeliveryContact((String) map.get("TRANSPORTATION_RECEIVE"));

            String proxyId = (String) map.get("WARRANT_NUMBER");
            if (proxyId != null) {
                if (proxyId.trim().equals("")) {
                    proxyId = "-";
                }
            } else {
                proxyId = "-";
            }

            carrier.setProxyID(proxyId);

            carrier.setProxyDate(DateUtils.getMessageDateTimeShort(dateSale));
            carrier.setPartyIssuingProxyName((String) map.get("SENDER_NAME"));

            delivery.setCarrier(carrier);
            delivery.setTransportOwnerName((String) map.get("CAR_OWNER"));
            delivery.setTransportID((String) map.get("CAR_NUMBER"));

            String trailerId = (String) map.get("CAR_TRAILER_NUMBER");
            if (trailerId != null) {
                if (trailerId.trim().equals("")) {
                    trailerId = "-";
                }
            } else {
                trailerId = "-";
            }

            delivery.setTrailerID(trailerId);
            delivery.setSealID((String) map.get("SEAL_NUMBER"));

            delivery.setQuantityTrip("1");
            delivery.setCurrency("BYN");

            DespatchAdviceLogisticUnitLineItem lineList = new DespatchAdviceLogisticUnitLineItem();
            List<LineItem> itemList = lineList.getLineItem();
            int count = 0;
            for (SaleDocumentDetailItemReport item : document.getDetailList()) {
                count++;
                LineItem newItem = new LineItem();
                newItem.setLineItemNumber(String.valueOf(count));
                newItem.setLineItemID(item.getEanCode());
                newItem.setLineItemName(item.getProductNameSizeStringSingleLine());
                newItem.setQuantityDespatched(new BigDecimal(item.getAmountPrint()).setScale(2, BigDecimal.ROUND_HALF_UP));
                newItem.setGrossWeightValue(new BigDecimal(item.getWeight()).setScale(2, BigDecimal.ROUND_HALF_UP));
                newItem.setLineItemQuantityUOM(item.getMeasureUnitAsUOMCode());
                newItem.setTaxRate(new BigDecimal(item.getValueVAT()).setScale(2, BigDecimal.ROUND_HALF_UP));
                newItem.setLineItemAmountWithoutCharges(new BigDecimal(item.getValueSumCost()).setScale(2, BigDecimal.ROUND_HALF_UP));
                newItem.setLineItemAmountCharges(new BigDecimal(item.getValueSumVat()).setScale(2, BigDecimal.ROUND_HALF_UP));
                newItem.setLineItemAmount(new BigDecimal(item.getValueSumCostAndVat()).setScale(2, BigDecimal.ROUND_HALF_UP));
                newItem.setLineItemPrice(new BigDecimal(item.getValuePrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
                itemList.add(newItem);

                if (item.getWeight() == 0) {
                    item.setItemPriceList(doc.getDocumentNumber());
                    errorList.add(item);
                }
            }

            delivery.setDespatchAdviceLogisticUnitLineItem(lineList);

            DeliveryNote.Total total = new DeliveryNote.Total();
            TotalSummingUp summingUp = document.getSummingUp();
            total.setTotalAmountWithoutCharges(new BigDecimal(summingUp.getValueSumCost()).setScale(2, BigDecimal.ROUND_HALF_UP));
            total.setTotalAmountCharges(new BigDecimal(summingUp.getValueSumVat()).setScale(2, BigDecimal.ROUND_HALF_UP));
            total.setTotalAmount(new BigDecimal(summingUp.getValueSumCostAndVat()).setScale(2, BigDecimal.ROUND_HALF_UP));
            total.setTotalLineItem(new BigInteger(String.valueOf(count)));
            total.setTotalLineItemQuantity(new BigDecimal(summingUp.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
            total.setTotalGrossWeight(new BigDecimal(summingUp.getWeight()).setScale(2, BigDecimal.ROUND_HALF_UP));
            total.setTotalDespatchUnitQuantity(new BigDecimal((Double) map.get("CARGO_SPACE")).setScale(2, BigDecimal.ROUND_HALF_UP));

            delivery.setTotal(total);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BLRWBL.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.marshal(issuance, new File(exportPath +
                    //DateUtils.getMessageDateTimeShortPlain(dateSale)+"_"+
                    document.getDocument().getDocumentNumber() + ".xml"));
            return true;
            //jaxbMarshaller.marshal( issuance, System.out );
        } catch (Exception e) {
            //System.out.println("Ошибка сохранения XML структуры " + document.getDocument().getDocumentNumber());
            e.printStackTrace();
            return false;
        }
    }

    public void createErrorReport() {
        List<String> list = new ArrayList<>();
        for (SaleDocumentDetailItemReport item : errorList) {
            list.add("№ " + item.getItemPriceList() + ": " + item.getArticleNumber() + " - " + item.getItemSizePrint() + " : Не указана масса");
        }
        if (list.size() > 0) {
            try {
                FileUtils.writeLines(new File(exportPath + "\\!!!ErrorsList.log"), list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
