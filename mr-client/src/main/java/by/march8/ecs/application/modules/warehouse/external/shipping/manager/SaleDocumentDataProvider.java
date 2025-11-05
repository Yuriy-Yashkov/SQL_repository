package by.march8.ecs.application.modules.warehouse.external.shipping.manager;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.application.modules.references.classifier.dao.ClassifierDAO;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.DAOSaleDocumentFactory;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.SaleDocumentJDBC;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.interfaces.ISaleDocumentDao;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.BaseLabelInformation;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.CurrencyName;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.DocumentTypePreset;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentReport;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.TotalSummingUp;
import by.march8.ecs.framework.helpers.digits.CurrencyType;
import by.march8.ecs.framework.helpers.digits.DigitToWords;
import by.march8.ecs.framework.helpers.digits.MeasureUnit;
import by.march8.entities.classifier.ClassifierArticleComposition;
import by.march8.entities.readonly.AddressEntity;
import by.march8.entities.readonly.ContractEntity;
import by.march8.entities.readonly.ContractorEntity;
import by.march8.entities.sales.PreOrderSaleDocument;
import by.march8.entities.warehouse.AdjustmentSaleDocument;
import by.march8.entities.warehouse.CargoSpaceItem;
import by.march8.entities.warehouse.Certificate;
import by.march8.entities.warehouse.ContractorBank;
import by.march8.entities.warehouse.SaleDocument;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import by.march8.entities.warehouse.SaleDocumentDriving;
import by.march8.entities.warehouse.SaleDocumentDrivingAdditional;
import by.march8.entities.warehouse.SaleDocumentEntity;
import by.march8.entities.warehouse.SaleDocumentItem;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
*
*             int page_count = (row - 36) / 29 + 1;
            page_count += (row - 36) % 29 > 0 ? 1 : 0;
*
*
*
*
*
*
*
*
* */

/**
 * AsService
 * Поставщик данных до отгружаемым документам. Производит сбор информации по документу.
 * Является промежуточным звеном между DAO и абстрактным Менеджером экспорта
 *
 * @author Andy 18.05.2016.
 */
@SuppressWarnings("all")
public class SaleDocumentDataProvider {

    private SaleDocumentManager manager;
    private DocumentTypePreset preset;

    private List<CargoSpaceItem> cargoSpaceList = null;

    /**
     * Конструктор поставщика данных для документа
     */
    public SaleDocumentDataProvider() {
        manager = new SaleDocumentManager();
    }

    /**
     * Если формирование документа ведется из формы выбора типов документов
     *
     * @param preset предустановки
     */
    public SaleDocumentDataProvider(DocumentTypePreset preset) {
        this();
        this.preset = preset;
    }

    /**
     * Метод вохзвращает true если изделие детское
     *
     * @param articleCode шифр полотна
     * @return признак детского изделия
     */
    public static boolean isChildItem(final String articleCode) {
        if (articleCode != null) {
            String unit = articleCode.substring(0, 2);
            if (!unit.equals("47") && !unit.equals("48")) {
                char a_char = articleCode.charAt(2);
                return (a_char == '3' || a_char == '6');
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Метод возвращает true если изделие ЧУЛОЧНО-НОСОЧНОЕ
     *
     * @param articleCode шифр полотна
     * @return флаг признака услуги
     */
    public static boolean isSocksItem(final String articleCode) {
        if (articleCode != null) {
            String unit = articleCode.substring(0, 2);
            if (unit.equals("43")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Метод возвращает true если изделие является услугой (окраска полотна например)
     *
     * @param articleCode шифр полотна
     * @return флаг признака услуги
     */
    public static boolean isServiceItem(final String articleCode) {
        if (articleCode != null) {
            String unit = articleCode.substring(0, 2);
            if (unit.equals("47")) {
                char a_char = articleCode.charAt(2);
                return (a_char == '0');
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String[] getProductTypeByCategory(String articleCode) {
        String[] result = {"", "999"};
        String type = articleCode.substring(1, 2);
        String subType = articleCode.substring(2, 3);
        int index = 900;
        int subIndex = 900;
        switch (type) {
            case "1":
                subIndex = 2;
                type = "Бельё";
                break;
            case "2":
                subIndex = 1;
                type = "Верхний трикотаж";
                break;
            case "3":
                subIndex = 3;
                type = "Чулки";
                break;
            case "8":
                subIndex = 9;
                type = "Набор лоскута";
                break;
            case "7":
                subIndex = 8;
                type = "Полотно";
                break;
            default:
                subIndex = 7;
                type = "";
                break;
        }

        switch (subType) {
            case "1":
                index = 10;
                subType = "Мужские";
                break;
            case "2":
                index = 20;
                subType = "Женские";
                break;
            case "3":
                index = 30;
                subType = "Детские";
                break;
            case "5":
                index = 40;
                subType = "Взрослые, спортивные";
                break;
            case "6":
                index = 50;
                subType = "Детские, спортивные";
                break;
            default:
                subType = "";
                index = 900;
                break;
        }

        if (type.equals("")) {
            result[0] = "";
            result[1] = "999";
            return result;
        } else {
            result[0] = subType + ", " + type;
            result[1] = String.valueOf(index + subIndex);
            return result;
        }
    }

    /**
     * Получает исчерпывающую информацию по документу
     *
     * @param saleDocument структура документа
     * @return данные по документу
     */
    public SaleDocumentReport prepareDocument(SaleDocument saleDocument) {
        SaleDocumentReport result = new SaleDocumentReport();
        if (saleDocument != null) {
            int id = saleDocument.getId();

            SaleDocumentBase document = manager.getSaleDocumentSimpleById(id);
            if (document != null) {
                // Получаем базовый объект документа
                result.setDocument(document);

                // Получаем спецификацию документа
                result.setDetailList(getDetailListUniqueRecords(document));


                if (document.getRecipientCode() == 2258 || document.getRecipientCode() == 4037) {
                    // Для отдельных контрагентов применяем расчет без округления
                    result.setSummingUp(getSummingUp(document, result.getDetailList(), true));
                } else {
                    // Подведение итогов по документу
                    result.setSummingUp(getSummingUp(document, result.getDetailList(), false));
                }

                // Подведение итогов по документу
                //result.setSummingUp(getSummingUp(document, result.getDetailList(), false));

                // Формируем карту реквизитов документа
                HashMap<String, Object> detailMap = getDetailMap(document, result.getSummingUp());
                if (detailMap == null) {
                    detailMap = getDetailMapEmpty(document, result.getSummingUp());
                    if (detailMap == null) {
                        return null;
                    } else {
                        result.setDetailMap(detailMap);
                    }
                } else {
                    result.setDetailMap(detailMap);
                }

                // дополнительные сведения
                return getAdditionalInformation(result);
            }
        }
        return null;
    }

    /**
     * Получает исчерпывающую информацию о документе по номеру документа
     *
     * @param documentNumber номер документа
     * @return данные по документу
     */
    public SaleDocumentReport prepareDocument(String documentNumber, boolean isSimple) {

        DaoFactory<SaleDocumentEntity> factory = DaoFactory.getInstance();
        IGenericDao<SaleDocumentEntity> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("number", documentNumber));

        SaleDocumentEntity entity = null;

        try {
            entity = dao.getEntityByNamedQuery(SaleDocumentEntity.class, "SaleDocumentEntity.findByNumber", criteria);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        if (entity == null) {
            return null;
        } else {
            return this.prepareDocument(entity.getId(), isSimple);
        }
    }

    /**
     * Возвращает исчерпывающую информацию по документу
     *
     * @param id             идентификатор документа
     * @param isSimpleReport флаг если требования к наполнению документа минимальны
     * @return данные по документу
     */
    public SaleDocumentReport prepareDocument(int id, boolean isSimpleReport) {
        SaleDocumentReport result = new SaleDocumentReport();
        SaleDocumentBase document = manager.getSaleDocumentSimpleById(id);
        if (document != null) {

            // Получаем базовый объект документа
            result.setDocument(document);
            if (isSimpleReport) {
                // Получаем спецификацию документа
                result.setDetailList(getDetailListAllRecords(document));
            } else {
                result.setDetailList(getDetailListUniqueRecords(document));
            }

            if (document.getRecipientCode() == 2258 || document.getRecipientCode() == 4037) {
                // Для отдельных контрагентов применяем расчет без округления
                result.setSummingUp(getSummingUp(document, result.getDetailList(), true));
            } else {
                // Подведение итогов по документу
                result.setSummingUp(getSummingUp(document, result.getDetailList(), false));
            }

            // Формируем карту реквизитов документа
            if (isSimpleReport) {
                result.setDetailMap(getDetailMapEmpty(document, result.getSummingUp()));
            } else {
                HashMap<String, Object> detailMap = getDetailMap(document, result.getSummingUp());
                if (detailMap == null) {
                    detailMap = getDetailMapEmpty(document, result.getSummingUp());
                    if (detailMap == null) {
                        return null;
                    } else {
                        result.setDetailMap(detailMap);
                    }
                } else {
                    result.setDetailMap(detailMap);
                }
            }
            // дополнительные сведения
            return getAdditionalInformation(result);
        }
        return null;
    }

    /**
     * Метод для получение дополнительных сведений о изделии в спецификации
     *
     * @param result Объект отчета
     * @return обхект отчета
     */
    private SaleDocumentReport getAdditionalInformation(final SaleDocumentReport result) {
        // Если базовый документ не null
        //System.out.println("Дополнительно");
        if (result.getDocument() != null) {

            SaleDocumentBase document = result.getDocument();
            boolean isIgnore = document.getDocumentType().equals("Предварительный заказ");
            HashMap<String, Integer> commonColorMap;

            if (!isIgnore) {
                // ПОлучаем грузоместа объединенных ячеек
                cargoSpaceList = manager.getCargoSpaceListByDocumentId(document.getId());
                commonColorMap = manager.getColorDiffMapMyDocumentId(document.getId());
            } else {
                cargoSpaceList = new ArrayList<>();
                commonColorMap = new HashMap<>();
            }


            // Идентификатор сертификата
            int idCertificate = document.getCertificateId();
            // Идентификатор удостоверения ГГР
            int idLiceense = document.getCertificateGGRId();
            // Инициализация менеджера сертификатов
            CertificateService certificateService = CertificateService.getInstance();

            // Единица изменения
            String measureUnit = null;

            boolean isMaterial = manager.isDocumentMaterialsSale(document);
            // Просмотр спецификации и определение сертификатов для записи
            double cargoSpace = 0;
            double averagePrice = 0;
            int childCount = 0;
            Set<String> productTypes = new HashSet<>();

            for (SaleDocumentDetailItemReport item : result.getDetailList()) {

                if (item != null) {

                    // Проверка, заложен ли в EAN код цвет
                    Integer color_id = commonColorMap.get(item.getEanCode());
                    if (color_id != null) {
                        if (color_id > 2) {
                            // Цвет заложен в EAN код
                            item.setCommonColor(false);
                        } else {
                            // В EAN коде цвет отсутствует
                            item.setCommonColor(true);
                        }
                    }

                    prepareYearsOld(item);

                    item.setGradeAsString(getGradeString(item.getArticleCode(), item.getItemGrade()));
                    if (!isIgnore) {
                        // попытка получить сертификат для изделия
                        final Certificate certificate = certificateService.
                                getCertificateByArticleRecursively(item.getArticleCode(), idCertificate);
                        // Устанавливаем сертификат для изделия и его детали
                        if (certificate != null) {
                            item.setCertificateType(certificate.getTypeName().trim());
                            item.setCertificateName(certificate.getValue().trim());
                        }

                        // попытка получить удостоверение гос регистрации для изделия
                        final Certificate license = certificateService.
                                getLicenseByArticleRecursively(item.getArticleCode(), idCertificate, item.getItemAgeGroup());
                        // Устанавливаем сертификат для изделия и его детали
                        if (license != null) {
                            item.setLicenseType("Свидетельство ГР");
                            item.setLicenseName(license.getValue().trim());
                        }
                    }

                    //Получаем тип изделий, разово за весь цикл
                    productTypes.add(getProductType(item.getArticleCode()));

                    // Единица измерения
                    measureUnit = getMeasureUnit(item.getArticleCode());

                    // Единица измерения
                    item.setMeasureUnit(measureUnit);
                    // Расчет веса если в документе материал
/*                    if(isMaterial){
                        item.setAmountPrint((double)item.getAmountAll()/100.0);
                        item.setWeight((double)item.getWeight()/100.0);
                    }else{
                        item.setAmountPrint(item.getAmountAll());
                        //item.setWeight((double)item.getWeight()*item.getAmountAll());
                    }*/

                    // Узнаем, детское ли это изделие
                    averagePrice += item.getAccountingPrice();
                    String TNVD = item.getTnvedCode();
                    if (isChildItem(item.getArticleCode())) {
                        childCount++;
                        // если это мать его бюстье
                        if (TNVD != null) {
                            if (!TNVD.trim().equals("") && TNVD.trim().length() > 5 &&
                                    !((TNVD.equals("6101209000") && item.getArticleCode().substring(0, 4).equals("4233"))
                                            || (TNVD.equals("6102209000") && item.getArticleCode().substring(0, 4).equals("4233"))
                                            || ((TNVD.substring(0, 4).equals("6103")) && (item.getArticleCode().substring(0, 4).equals("4231")
                                            || item.getArticleCode().substring(0, 4).equals("4233") || item.getArticleCode().substring(0, 4).equals("4238")
                                            || item.getArticleCode().substring(0, 4).equals("4261")))
                                            || ((TNVD.substring(0, 4).equals("6104")) && (item.getArticleCode().substring(0, 4).equals("4231")
                                            || item.getArticleCode().substring(0, 4).equals("4233") || item.getArticleCode().substring(0, 4).equals("4238")
                                            || item.getArticleCode().substring(0, 4).equals("4236") || item.getArticleCode().substring(0, 4).equals("4268")))
                                            || ((TNVD.substring(0, 4).equals("6112")) && (item.getArticleCode().substring(0, 4).equals("4166")
                                            || item.getArticleCode().substring(0, 5).equals("41678") || item.getArticleCode().substring(0, 5).equals("41674")
                                            || item.getArticleCode().substring(0, 5).equals("41679") || item.getArticleCode().substring(0, 4).equals("4265"))))
                            ) {
                                item.setAccountingVat(20);
                            } else {
                                item.setAccountingVat(10);
                            }
                        }

                    } else {
                        item.setAccountingVat(20);
                    }
                    cargoSpace += getCargoSpace(item);
                }

                // ПО ГОСТУ ДЛЯ НЕСОРТНОЙ ЧУЛОЧКИ ПРПИСЫВАЕМ "УД" В АРТИКУЛЕ
                if (isSocksItem(item.getArticleCode())) {
                    if (item.getItemGrade() > 1) {
                        item.setArticleNumber(item.getArticleNumber().replace("Д", "УД"));
                    }
                }
            }
            // Определяем валюту(BYR или BYN), в которой идет расчет
            if ((averagePrice / result.getDetailList().size()) > 1000) {
                document.setOldMoney(true);
            } else {
                document.setOldMoney(false);
            }

            boolean isService = false;

            for (SaleDocumentDetailItemReport item : result.getDetailList()) {
                if (document.isOldMoney()) {
                    item.setValueAccountingPriceAlt(item.getValueAccountingPrice() / 10000);
                } else {
                    item.setValueAccountingPriceAlt(item.getValueAccountingPrice() * 10000);
                }

                // Определяем артикул услуги в документе
                if (!isService) {
                    if (isServiceItem(item.getArticleCode())) {
                        isService = true;
                    }
                }
            }

            //result.getDocument().setAsService(isService);
            if (result.getDocument().getServiceType() > 0) {
                result.getDocument().setAsService(true);
            } else {
                result.getDocument().setAsService(false);
            }

            // Дополняем карту реквизитов документа
            HashMap<String, Object> map = result.getDetailMap();
            if (map != null) {

                // Формируем тип ассортимента для накладной
                String type = "";
                for (String s : productTypes) {
                    type += s + ",";
                }

                if (productTypes.size() > 1) {
                    measureUnit = "пар/шт.";
                }

                if (type.length() > 0) {
                    // Тип изделий
                    map.put("PRODUCT_NAME", type.substring(0, type.length() - 1));
                } else {
                    map.put("PRODUCT_NAME", "");
                }
                // Единица измерений
                map.put("UNIT_OF_MEASURE", measureUnit);
                // Грузоместа
                map.put("CARGO_SPACE", cargoSpace);

                DigitToWords strCargoSpace = new DigitToWords(cargoSpace, MeasureUnit.INTEGER);
                map.put("CARGO_SPACE_STRING", strCargoSpace.num2str().trim());

                // Количество листов
                map.put("PAGE_COUNT", "");
                // Количество детских изделий
                map.put("CHILD_COUNT", childCount);

                CurrencyType nationalCurrency = CurrencyType.BYN;
                if (document.isOldMoney()) {
                    nationalCurrency = CurrencyType.BYR;
                }

                DigitToWords strSumVAT = new DigitToWords(document.getValueSumVat(), CurrencyType.BYN);

                String note = map.get("NOTE").toString().trim() + " ";

                String disc = "";

                if (document.getDiscountType() > 0) {
                    disc = "Скидка " + document.getDiscountValue() + " % ";
                }

                String trade = "";

                if (document.getTradeMarkType() > 0) {
                    trade = "Надбавка " + document.getTradeMarkValue() + " % ";
                }


                if (SaleDocumentManager.isExportDocument(document)) {
                    map.put("VAT_VALUE", document.getDocumentVatValue());
                    if (document.getDocumentVatValue() > 0.0) {
                        map.put("SUM_VAT_STRING", strSumVAT.num2str().trim());
                    } else {
                        map.put("SUM_VAT_STRING", "-");
                    }
                    map.put("NOTE", note);
                } else {
                    if (document.getDocumentVatValue() > 0.0) {
                        map.put("VAT_VALUE", document.getDocumentVatValue());
                        map.put("SUM_VAT_STRING", strSumVAT.num2str().trim());
                    } else {
                        map.put("VAT_VALUE", "-");
                        map.put("SUM_VAT_STRING", "-");
                    }
                    map.put("NOTE", disc + trade + note);
                }

                DigitToWords strSumCostAndVAT = new DigitToWords(document.getValueSumCostAndVat(), CurrencyType.BYN);
                try {
                    map.put("SUM_COST_AND_VAT_STRING", strSumCostAndVAT.num2str().trim());
                } catch (Exception e) {
                    System.err.println("Ошибка функции [число прописью] для валюты");
                    map.put("SUM_COST_AND_VAT_STRING", "");
                }
            }
        }
        return result;
    }

    /**
     * ПОиск грузомест для объединенных записей
     *
     * @param item изделие
     * @return число грузомест по строке
     */
    private double getCargoSpace(SaleDocumentDetailItemReport item) {

        for (CargoSpaceItem cargoItem : cargoSpaceList) {
            if (cargoItem.getId() == item.getId()) {
                item.setAmount(cargoItem.getCargoSpace());
                return cargoItem.getCargoSpace();
            }
        }
        return item.getAmount();
    }

    public void prepareYearsOld(SaleDocumentDetailItemReport item) {
        if (isChildItem(item.getArticleCode())) {
            int size = item.getItemSize();
            int growth = item.getItemGrowz();

            if (size >= 36 && size <= 56) {
                if (growth >= 56 && growth <= 98) {
                    item.setYearsOld("0-3");
                } else {
                    item.setYearsOld("3-6");
                }
            } else if (size >= 52 && size <= 60) {
                if (growth >= 98 && growth <= 128) {
                    item.setYearsOld("3-6");
                } else {
                    item.setYearsOld("6-10");
                }
            } else if (size >= 60 && size <= 76) {
                if (growth >= 122 && growth <= 146) {
                    item.setYearsOld("6-10");
                } else {
                    item.setYearsOld("10-18");
                }
            } else if (size >= 76 && size <= 84) {
                if (growth >= 152 && growth <= 176) {
                    item.setYearsOld("10-18");
                } else {
                    item.setYearsOld("18+");
                }
            }
        } else {
            item.setYearsOld("18+");
        }
    }

    /**
     * Возвращает тип изделия по его шифру артикула
     *
     * @param articleCode шифр артикула
     * @return тип изделия
     */
    private String getProductType(String articleCode) {
        String unit = articleCode.substring(0, 2);
        switch (unit) {
            case "48":
                return "Отходы трикотажного производства";
            case "47": {
                if (articleCode.substring(2, 3).equals("1")) // Проверка на полотно или пряжу
                    return "Пряжа";
                else
                    return "Полотно";
            }
            case "43": {
                if (articleCode.substring(2, 3).equals("7")) // Проверка не услуга ли
                    return "Услуга вязания чулочно-носочных изделий из материала заказчика";
                else
                    return "Чулочно-носочные изделия";
            }
            case "45":
                switch (articleCode) {
                    case "45890000":
                    case "45899830":
                    case "45899831":
                    case "45899832":
                    case "45899833":
                    case "45899838":
                    case "45891103":
                    case "45899800":
                    case "45899834":
                    case "45899835":
                    case "45899836":
                    case "45890028":
                    case "45890027":
                    case "45896001":
                    case "45896002":
                        return "Трикотажные изделия";
                }
            case "41":
                return "Трикотажные изделия";
            case "42":
                return "Трикотажные изделия";
            default:
                return "Трикотажные, чулочно-носочные изделия";
        }
    }

    /**
     * Возвращает единицу измерения для изделия по его шифру артикула
     *
     * @param articleCode шифр артикула
     * @return единица измерения
     */
    private String getMeasureUnit(String articleCode) {
        String unit = articleCode.substring(0, 2);

        switch (unit) {
            case "48":
                return "кг.";
            case "47":
                return "кг.";
            case "43": {
                // Дополнительный парсинг для калготок
                String subCode = articleCode.substring(0, 4);

                // Если шифр артикула принадлежит калготкам
                if (subCode.equals("4331")) {
                    return "шт.";
                } else {
                    // Иначе все в парах
                    return "пар";
                }
            }
            default:
                return "шт.";
        }
    }

    /**
     * Метод возвращает строковое обозначение сорта изделия для чулочки (с/у)
     * иначе строку сорта для прочих
     *
     * @param articleCode шифр артикула
     * @param grade       сорт по классификатору
     * @return строковое обозначение сорта
     */
    private String getGradeString(String articleCode, int grade) {
        // System.err.println("артикул "+articleCode);
        String unit = articleCode.substring(0, 2);

        switch (unit) {
            case "43":
                if (grade > 1) return "у";
                else return "с";
            default:
                return String.valueOf(grade);
        }
    }


    /**
     * Возвращает итоги по документу
     *
     * @param document документ в базовом виде
     * @param list     список интерфейсов для расчета подведения итогов
     * @return объект итогов по документу
     */
    private TotalSummingUp getSummingUp(SaleDocumentBase document, List<SaleDocumentDetailItemReport> list, boolean isAccuracy) {
        SaleDocumentCalculator calculator = SaleDocumentCalculator.getInstance();
        // Подведение итогов по ценовым параметрам
        TotalSummingUp result;
        if (isAccuracy) {
            System.err.println("нгнннгфывщпразщпайугшкпазйшгукпзцшкпзшгцкпзшгцпушкуцйкп");
            result = calculator.summingUpCostAccuracy(list);
        } else {
            result = calculator.summingUpCost(list, document.getDocumentType(), document.getTradeMarkValue());
        }

        // Подведение итогов по количественным и весовым параметрам
        boolean isMaterial = manager.isDocumentMaterialsSale(document);
        result = calculator.summingUpAmountAndWeight(list, isMaterial, result);

        /*        if(isMaterial){

            result.setWeight(result.getAmount());
        }*/

        return result;
    }


    /**
     * Возвращает спецификацию документа, список изделий, с группировкой по однотипным изделиям
     *
     * @param document документ в базовом виде
     * @return спецификация по документу
     */
    private List<SaleDocumentDetailItemReport> getDetailListUniqueRecords(SaleDocumentBase document) {
        DAOSaleDocumentFactory factory = DAOSaleDocumentFactory.getInstance();
        // Получение спецификации из БД
        ISaleDocumentDao dao = factory.getSaleDocumentDao();
        return dao.getSaleDocumentsForReport(0, document.getDocumentNumber());
    }

    /**
     * Возвращает спецификацию документа, список изделий всех изделий
     * без группировки по однотипных изделий
     *
     * @param document документ в базовом виде
     * @return спецификация по документу
     */
    private List<SaleDocumentDetailItemReport> getDetailListAllRecords(SaleDocumentBase document) {
        DAOSaleDocumentFactory factory = DAOSaleDocumentFactory.getInstance();
        // Получение спецификации из БД
        ISaleDocumentDao dao = factory.getSaleDocumentDao();
        return dao.getSaleDocumentsForReport(1, document.getDocumentNumber());
    }

    /**
     * Возвращает набор данных реквизитов документа и путевого листа
     *
     * @param document  документ в базовом виде
     * @param summingUp обхект итогов по документу
     * @return реквизиты документа и путевого листа
     */
    private HashMap<String, Object> getDetailMap(SaleDocumentBase document, TotalSummingUp summingUp) {
        //System.out.println("ID документа " + document.getId());
        // Получаем дополнительную информацию по документу (реквизиты путевого листа)
        // несколькими способами
        // 1- Получаем данные из новой таблицы
        SaleDocumentDriving driving = manager.getDrivingDocumentationByDocumentId(document.getId());
        // Если по способу 1 данные не получены, пробуем получить их из старой таблицы
        if (driving == null) {

            driving = new SaleDocumentDriving();
            driving.setLoadingAddressId(-1);
            driving.setUnloadingAddressId(-1);

            driving.setReaddressing("");
            driving.setCarCustomer("");
            driving.setCarDriverName("");
            driving.setCarNumber("");
            driving.setCarPayer("");
            driving.setCarOwner("");
            driving.setCarTrailerNumber("");

            driving.setLoadingDoer(0);
            driving.setLoadingMethod(0);
            driving.setSealNumberId(0);

            driving.setDocumentDate(new Date());
            driving.setDocumentNumber("");

            driving.setWarrantDate(new Date());
            driving.setWarrantIssued("");
            driving.setWarrantNumber("");
            driving.setWarrantReceive("");
            driving.setWarrantSealNumber("");

            driving.setSaleAllowed("");
            driving.setShipperPassed("");

            driving.setNote("");

            driving.setTransportationReceive("");

        }


        // Если после 3-х способов получить путевой лист провал - выходим
        if (driving == null) {
            return null;
        }

        boolean isMaterial = manager.isDocumentMaterialsSale(document);

        HashMap<String, Object> map = new HashMap<>();

        ContractorEntity contractorSender;
        ContractorEntity contractorReceiver;

        ContractEntity contractEntity;

        if (SaleDocumentManager.isDocumentRefund(document)) {
            // Получаем сведения о контрагенте, грузоотправителе
            contractorSender = manager.getContractorByCode(document.getRecipientCode(), document.getDocumentNumber());
            // Получатель по накладной (код получаем из накладной)
            contractorReceiver = manager.getContractorByCode(-1, document.getDocumentNumber());

            //contract = SaleDocumentDriving.getContractById(contractorSender, document.getRecipientContractId());
            contractEntity = SaleDocumentDriving.getContract(contractorSender, document.getRecipientContractId());
            map.put("SELF_UNP", contractorReceiver.getCodeUNN());

        } else {
            //Получаем сведения о контрагенте, грузополучателе
            contractorSender = manager.getContractorByCode(-1, document.getDocumentNumber());
            // Получатель по накладной (код получаем из накладной)
            contractorReceiver = manager.getContractorByCode(document.getRecipientCode(), document.getDocumentNumber());
            // contract = SaleDocumentDriving.getContractById(contractorReceiver, document.getRecipientContractId());
            contractEntity = SaleDocumentDriving.getContract(contractorReceiver, document.getRecipientContractId());
            map.put("SELF_UNP", contractorSender.getCodeUNN());
        }

        map.put("CONTRACTOR_SENDER", contractorSender);
        map.put("CONTRACTOR_RECIPIENT", contractorReceiver);

        // Получаем реквизиты банков
        DAOSaleDocumentFactory factory = DAOSaleDocumentFactory.getInstance();
        // Получение спецификации из БД
        ISaleDocumentDao dao = factory.getSaleDocumentDao();
        //Отправителя
        int senderCode = contractorSender.getCode();
        if (contractorSender.getCode() == -1) {
            senderCode = 1317;
        }

        ContractorBank senderBank = dao.getContractorBankByContractorCode(senderCode);
        map.put("SENDER_BANK", senderBank);
        // ПОлучатель
        ContractorBank recipientBank = dao.getContractorBankByContractorCode(contractorReceiver.getCode());
        map.put("RECIPIENT_BANK", recipientBank);

        // Обработка контракта
        // Основание отгрузки(контракт/договор/соглашение и т.д.)
        if (contractEntity != null) {
            // Полное наименование договора
            map.put("CONTRACT_NAME", contractEntity.toString());

            // Номер документа
            map.put("CONTRACT_NUMBER", contractEntity.getNumber());
            // Тип документа
            map.put("CONTRACT_TYPE", contractEntity.getName());
            // Дата начала действия документа
            map.put("CONTRACT_DATE_BEGIN", DateUtils.getNormalDateFormat(contractEntity.getDate()));
            // ДАта договора, как Date
            map.put("CONTRACT_DATE_BEGIN_AS_DATE", contractEntity.getDate());
            // Дата окончания действия документа
            map.put("CONTRACT_DATE_END", DateUtils.getNormalDateFormat(contractEntity.getDateOut()));
        }

        map.put("RECIPIENT_UNP", contractorReceiver.getCodeUNN());
        map.put("RECIPIENT_REGION", contractorReceiver.getRegionCode());

        map.put("SENDER_UNP", contractorSender.getCodeUNN());
        map.put("SENDER_REGION", contractorSender.getRegionCode());

        // Адресс отправителя, юридический
        String addressSender = "";
        // Имя отправителя
        String nameSender = "";

        //AddressEntity addressEntitySender = contractorSender.getLegalAddress();
        AddressEntity addressEntitySender = SaleDocumentDriving.getAddressEntityByAddressId(contractorSender, driving.getShipperId());
        if (addressEntitySender != null) {
            nameSender = contractorSender.getFullName().trim();
            addressSender = addressEntitySender.getFullName().trim();
        } else {
            addressEntitySender = contractorSender.getLegalAddress();
            if (addressEntitySender != null) {
                nameSender = contractorSender.getFullName().trim();
                addressSender = addressEntitySender.getFullName().trim();
            }
        }

        map.put("SENDER_NAME", nameSender);
        map.put("SENDER_ADDRESS", addressSender);

        // Адресс получателя, юридический
        String addressReceiver = "";
        // Наименование получателя
        String nameReceiver = "";

        //AddressEntity addressEntityReceiver = contractorReceiver.getLegalAddress();
        AddressEntity addressEntityReceiver = SaleDocumentDriving.getAddressEntityByAddressId(contractorReceiver, driving.getConsigneeId());

        if (addressEntityReceiver != null) {
            nameReceiver = contractorReceiver.getFullName().trim();
            addressReceiver = addressEntityReceiver.getFullName().trim();
        } else {
            addressEntityReceiver = contractorReceiver.getLegalAddress();
            if (addressEntityReceiver != null) {
                nameReceiver = contractorReceiver.getFullName().trim();
                addressReceiver = addressEntityReceiver.getFullName().trim();
            }
        }

        map.put("RECIPIENT_NAME", nameReceiver);
        map.put("RECIPIENT_ADDRESS", addressReceiver);

        // Если отбор деталей выполнен из старой базы
        if (driving.getAdditional() != null) {
            document.setDocumentSaleDate(new Date());
            SaleDocumentDrivingAdditional additional = driving.getAdditional();
            map.put("LOADING_ADDRESS", additional.getLoadingAddress());
            map.put("UNLOADING_ADDRESS", additional.getUnloadingAddress());

            map.put("LOADING_DOER", additional.getLoadingDoer());
            map.put("LOADING_METHOD", additional.getLoadingMethod());
            map.put("SEAL_NUMBER", additional.getSealNumber());

        } else {
            // Если отбор деталей выполнен из новой базы

            // Адресс погрузки
            String addressLoad = SaleDocumentDriving.getContractorAddressByAddressId(contractorSender, driving.getLoadingAddressId());
            map.put("LOADING_ADDRESS", addressLoad);

            // Адресс разгрузки
            String addressUnLoad = SaleDocumentDriving.getContractorAddressByAddressId(contractorReceiver, driving.getUnloadingAddressId());
            map.put("UNLOADING_ADDRESS", addressUnLoad);

            map.put("LOADING_DOER", SaleDocumentDriving.getLoadingDoerById(driving.getLoadingDoer()));
            map.put("LOADING_METHOD", SaleDocumentDriving.getLoadingMethodById(driving.getLoadingMethod()));
            map.put("SEAL_NUMBER", SaleDocumentDriving.getSealNumberById(driving.getSealNumberId()));
        }

        map.put("READDRESSING", driving.getReaddressing().trim());

        map.put("DOCUMENT_DATE", DateUtils.getNormalDateFormat(driving.getDocumentDate()));
        map.put("DOCUMENT_NUMBER", driving.getDocumentNumber().trim());

        map.put("CAR_NUMBER", driving.getCarNumber().trim());
        map.put("CAR_TRAILER_NUMBER", driving.getCarTrailerNumber().trim());
        map.put("CAR_OWNER", driving.getCarOwner().trim());
        map.put("CAR_DRIVER_NAME", driving.getCarDriverName().trim());
        map.put("CAR_CUSTOMER", driving.getCarCustomer().trim());
        map.put("CAR_PAYER", driving.getCarPayer().trim());


        map.put("SUPPORT_DOCUMENT", "");

        // Если документ корректированный, ищем причину корректировки и помещаем в мапу
        if (document.getAdjustmentType() == 1) {

            DaoFactory<AdjustmentSaleDocument> factory_ = DaoFactory.getInstance();
            IGenericDao<AdjustmentSaleDocument> dao_ = factory_.getGenericDao();
            java.util.List<QueryProperty> criteria = new ArrayList<>();
            criteria.add(new QueryProperty("document", document.getId()));
            java.util.List<AdjustmentSaleDocument> list = null;
            try {
                list = dao_.getEntityListByNamedQuery(AdjustmentSaleDocument.class, "AdjustmentSaleDocument.findByDocumentId", criteria);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String adjustmentCause = " - ";
            if (list != null) {
                if (list.size() > 0) {
                    adjustmentCause = list.get(0).getCause();
                }
            }

            map.put("ADJUSTMENT_CAUSE", adjustmentCause);
        }

        if (summingUp != null) {
            CurrencyName currency = manager.getCurrencyNameById(document.getCurrencyId());
            map.put("AMOUNT", summingUp.getAmount());

            map.put("SUM_COST_CURRENCY", summingUp.getValueSumCostCurrency());


            map.put("SUM_VAT_CURRENCY", summingUp.getValueSumVatCurrency());
            map.put("SUM_COST_AND_VAT_CURRENCY", summingUp.getValueSumCostAndVatCurrency());

            map.put("SUM_COST", SaleDocumentCalculator.roundAndGetString(summingUp.getValueSumCost(), 2));
            map.put("SUM_VAT", SaleDocumentCalculator.roundAndGetString(summingUp.getValueSumVat(), 2));
            map.put("SUM_COST_AND_VAT", SaleDocumentCalculator.roundAndGetString(summingUp.getValueSumCostAndVat(), 2));

            int curr = document.getCurrencyId();
            CurrencyType currencyType = CurrencyType.BYN;
            switch (curr) {

                case 2:
                    currencyType = CurrencyType.RUB;
                    break;
                case 3:
                    currencyType = CurrencyType.USD;
                    break;
                case 4:
                    currencyType = CurrencyType.EUR;
                    break;
                case 5:
                    currencyType = CurrencyType.UAH;
                    break;
            }

            DigitToWords strSumVATCurrency = new DigitToWords(document.getValueSumVat(), currencyType);
            DigitToWords strSumCostAndVATCurrency = new DigitToWords(document.getValueSumCostAndVat(), currencyType);

            DigitToWords strCargoWeight = new DigitToWords(summingUp.getWeight(), MeasureUnit.WEIGHT);
            DigitToWords strAmountString = new DigitToWords(summingUp.getAmount(), MeasureUnit.AMOUNT);

            map.put("CARGO_WEIGHT_STRING", strCargoWeight.num2str().trim());
            BigDecimal bd = new BigDecimal(summingUp.getWeight()).setScale(10, BigDecimal.ROUND_HALF_UP).setScale(3, BigDecimal.ROUND_HALF_UP);

            map.put("CARGO_WEIGHT", bd.doubleValue());

            if (isMaterial) {
                //MoneyToStr strAmountString = new MoneyToStr(summingUpCost.getAmount(), "kg");
                map.put("AMOUNT_STRING", strCargoWeight.num2str().trim());
            } else {

                map.put("AMOUNT_STRING", strAmountString.num2str(true).trim());
            }

            map.put("CURRENCY_TYPE", currency.getName());

            try {
                map.put("SUM_VAT_CURRENCY_STRING", strSumVATCurrency.num2str().trim());
            } catch (Exception e) {
                System.err.println("Ошибка функции [число прописью] для валюты");
                map.put("SUM_VAT_CURRENCY_STRING", "");
            }

            try {
                map.put("SUM_COST_AND_VAT_CURRENCY_STRING", strSumCostAndVATCurrency.num2str().trim());
            } catch (Exception e) {
                System.err.println("Ошибка функции [число прописью] для валюты");
                map.put("SUM_COST_AND_VAT_CURRENCY_STRING", "");
            }

            map.put("NOTE", driving.getNote().trim());

            //map.put("CARGO_SPACE", "-");
            map.put("TRANSPORTATION_RECEIVE", driving.getTransportationReceive().trim());

            if (driving.getWarrantNumber().trim().equals("")) {
                map.put("WARRANT_DATE", "");
            } else {
                map.put("WARRANT_DATE", DateUtils.getNormalDateFormat(driving.getWarrantDate()));
            }
            map.put("DOCUMENT_TYPE", driving.getDocumentType());
            map.put("WARRANT_NUMBER", driving.getWarrantNumber().trim());
            map.put("WARRANT_ISSUED", driving.getWarrantIssued().trim());
            map.put("WARRANT_RECEIVE", driving.getWarrantReceive().trim());
            map.put("WARRANT_SEAL_NUMBER", driving.getWarrantSealNumber().trim());

            map.put("SALE_ALLOWED", driving.getSaleAllowed().trim());
            map.put("SHIPPER_PASSED", driving.getShipperPassed().trim());
        }

        return map;
    }

    private HashMap<String, Object> getDetailMapEmpty(SaleDocumentBase document, TotalSummingUp summingUp) {
        //System.out.println("ID документа " + document.getId());
        // Получаем дополнительную информацию по документу (реквизиты путевого листа)
        // несколькими способами
        // 1- ПОлучаем данные из новой таблицы
        SaleDocumentDriving driving = new SaleDocumentDriving();
        // Если по способу 1 данные не получены, пробуем получить их из старой таблицы
/*        if (driving == null) {
            driving = manager.getDrivingDocumentationByDocumentIdOld(document);

            if(driving!=null){
                int vat = driving.getAdditional().getValueVat();
                document.setDocumentVatValue(vat);
            }
        }

        if (driving == null) {
            return null;
        }*/

        boolean isMaterial = manager.isDocumentMaterialsSale(document);

        HashMap<String, Object> map = new HashMap<>();

        ContractorEntity contractorSender;
        ContractorEntity contractorReceiver;

        ContractEntity contractEntity;

        if (SaleDocumentManager.isDocumentRefund(document)) {
            // Получаем сведения о контрагенте, грузоотправителе
            contractorSender = manager.getContractorByCode(document.getRecipientCode(), document.getDocumentNumber());
            // Получатель по накладной (код получаем из накладной)
            contractorReceiver = manager.getContractorByCode(-1, document.getDocumentNumber());

            //contract = SaleDocumentDriving.getContractById(contractorSender, document.getRecipientContractId());
            contractEntity = SaleDocumentDriving.getContract(contractorSender, document.getRecipientContractId());
            map.put("SELF_UNP", contractorReceiver.getCodeUNN());

        } else {
            //Получаем сведения о контрагенте, грузополучателе
            contractorSender = manager.getContractorByCode(-1, document.getDocumentNumber());
            // Получатель по накладной (код получаем из накладной)
            contractorReceiver = manager.getContractorByCode(document.getRecipientCode(), document.getDocumentNumber());
            // contract = SaleDocumentDriving.getContractById(contractorReceiver, document.getRecipientContractId());
            try {
                contractEntity = SaleDocumentDriving.getContract(contractorReceiver, document.getRecipientContractId());
            } catch (Exception ex) {
                System.out.println("");
                contractEntity = null;
            }

            map.put("SELF_UNP", contractorSender.getCodeUNN());
        }

        // Получаем реквизиты банков
        DAOSaleDocumentFactory factory = DAOSaleDocumentFactory.getInstance();
        // Получение спецификации из БД
        ISaleDocumentDao dao = factory.getSaleDocumentDao();
        //Отправителя
        int senderCode = contractorSender.getCode();
        if (contractorSender.getCode() == -1) {
            senderCode = 1317;
        }


        ContractorBank senderBank = dao.getContractorBankByContractorCode(senderCode);
        map.put("SENDER_BANK", senderBank);
        // ПОлучатель
        ContractorBank recipientBank = dao.getContractorBankByContractorCode(contractorReceiver.getCode());
        map.put("RECIPIENT_BANK", recipientBank);

        // Обработка контракта
        // Основание отгрузки(контракт/договор/соглашение и т.д.)
        if (contractEntity != null) {
            // Полное наименование договора
            map.put("CONTRACT_NAME", contractEntity.toString());
            // Номер документа
            map.put("CONTRACT_NUMBER", contractEntity.getNumber());
            // Тип документа
            map.put("CONTRACT_TYPE", contractEntity.getName());
            // Дата начала действия документа
            map.put("CONTRACT_DATE_BEGIN", DateUtils.getNormalDateFormat(contractEntity.getDate()));
            // ДАта договора, как Date
            map.put("CONTRACT_DATE_BEGIN_AS_DATE", contractEntity.getDate());
            // Дата окончания действия документа
            map.put("CONTRACT_DATE_END", DateUtils.getNormalDateFormat(contractEntity.getDateOut()));
        }

        map.put("RECIPIENT_UNP", contractorReceiver.getCodeUNN());
        map.put("SENDER_UNP", contractorSender.getCodeUNN());

        // Адресс отправителя, юридический
        String addressSender = "";
        // Имя отправителя
        String nameSender = "";

        AddressEntity addressEntitySender = contractorSender.getLegalAddress();
        if (addressEntitySender != null) {
            nameSender = contractorSender.getName().trim();
            addressSender = addressEntitySender.getFullName().trim();
        }

        map.put("SENDER_NAME", nameSender);
        map.put("SENDER_ADDRESS", addressSender);

        // Адресс получателя, юридический
        String addressReceiver = "";
        // Наименование получателя
        String nameReceiver = "";
        AddressEntity addressEntityReceiver = contractorReceiver.getLegalAddress();
        if (addressEntityReceiver != null) {
            nameReceiver = contractorReceiver.getName().trim();
            addressReceiver = addressEntityReceiver.getFullName().trim();
        }

        map.put("RECIPIENT_NAME", nameReceiver);
        map.put("RECIPIENT_ADDRESS", addressReceiver);

        // Адресс погрузки
        //String addressLoad = SaleDocumentDriving.getContractorAddressByAddressId(contractorSender, driving.getLoadingAddressId());
        map.put("LOADING_ADDRESS", "-");

        // Адресс разгрузки
        //String addressUnLoad = SaleDocumentDriving.getContractorAddressByAddressId(contractorReceiver, driving.getUnloadingAddressId());
        map.put("UNLOADING_ADDRESS", "-");

        map.put("LOADING_DOER", "-");
        map.put("LOADING_METHOD", "-");
        map.put("SEAL_NUMBER", "-");

        map.put("READDRESSING", "-");

        map.put("DOCUMENT_DATE", "-");
        map.put("DOCUMENT_NUMBER", "-");

        map.put("CAR_NUMBER", "-");
        map.put("CAR_TRAILER_NUMBER", "-");
        map.put("CAR_OWNER", "-");
        map.put("CAR_DRIVER_NAME", "-");
        map.put("CAR_CUSTOMER", "-");
        map.put("CAR_PAYER", "-");


        //Трикотажные,чулочно-носочные изделия
        map.put("SUPPORT_DOCUMENT", "");

        if (summingUp != null) {
            CurrencyName currency = manager.getCurrencyNameById(document.getCurrencyId());
            map.put("AMOUNT", summingUp.getAmount());

            map.put("SUM_COST_CURRENCY", summingUp.getValueSumCostCurrency());


            map.put("SUM_VAT_CURRENCY", summingUp.getValueSumVatCurrency());
            map.put("SUM_COST_AND_VAT_CURRENCY", summingUp.getValueSumCostAndVatCurrency());

            map.put("SUM_COST", SaleDocumentCalculator.roundAndGetString(summingUp.getValueSumCost(), 2));
            map.put("SUM_VAT", SaleDocumentCalculator.roundAndGetString(summingUp.getValueSumVat(), 2));
            map.put("SUM_COST_AND_VAT", SaleDocumentCalculator.roundAndGetString(summingUp.getValueSumCostAndVat(), 2));

            DigitToWords strSumVATCurrency = new DigitToWords(document.getValueSumVat(), CurrencyType.RUB);
            DigitToWords strSumCostAndVATCurrency = new DigitToWords(document.getValueSumCostAndVat(), CurrencyType.RUB);


            DigitToWords strCargoWeight = new DigitToWords(summingUp.getWeight(), MeasureUnit.WEIGHT);
            DigitToWords strAmountString = new DigitToWords(summingUp.getAmount(), MeasureUnit.AMOUNT);

            map.put("CARGO_WEIGHT_STRING", strCargoWeight.num2str().trim());
            map.put("CARGO_WEIGHT", summingUp.getWeight());

            if (isMaterial) {
                //MoneyToStr strAmountString = new MoneyToStr(summingUpCost.getAmount(), "kg");
                map.put("AMOUNT_STRING", strCargoWeight.num2str().trim());
            } else {

                map.put("AMOUNT_STRING", strAmountString.num2str(true).trim());
            }

            map.put("CURRENCY_TYPE", currency.getName());

            try {
                map.put("SUM_VAT_CURRENCY_STRING", strSumVATCurrency.num2str().trim());
            } catch (Exception e) {
                System.err.println("Ошибка функции [число прописью] для валюты");
                map.put("SUM_VAT_CURRENCY_STRING", "");
            }

            try {
                map.put("SUM_COST_AND_VAT_CURRENCY_STRING", strSumCostAndVATCurrency.num2str().trim());
            } catch (Exception e) {
                System.err.println("Ошибка функции [число прописью] для валюты");
                map.put("SUM_COST_AND_VAT_CURRENCY_STRING", "");
            }

            map.put("NOTE", "-");

            //map.put("CARGO_SPACE", "-");
            map.put("TRANSPORTATION_RECEIVE", "-");

            map.put("WARRANT_DATE", "-");
            map.put("WARRANT_NUMBER", "-");
            map.put("WARRANT_ISSUED", "-");
            map.put("WARRANT_RECEIVE", "-");
            map.put("WARRANT_SEAL_NUMBER", "-");

            map.put("SALE_ALLOWED", "-");
            map.put("SHIPPER_PASSED", "-");
        }

        return map;
    }

    public void updateEanCodeAndColor(SaleDocument document) {
        if (document == null) {
            return;
        }
        SaleDocumentJDBC db = new SaleDocumentJDBC();
        Map<Integer, BaseLabelInformation> map = db.getLabelInformationByDocumentIdAsMap(document.getId());
        System.out.println("В спецификации документа " + map.size() + " несоответствий EAN кодов");
        if (map != null) {
            for (SaleDocumentItem item : document.getDetailList()) {
                if (item != null) {
                    BaseLabelInformation mapItem = map.get(item.getId());
                    if (mapItem != null) {
                        //item.setItemColor(mapItem.getColor());
                        item.setItemEanCode(mapItem.getEanCode());
                        System.out.println(item.getItemEanCode() + "(" + item.getItemColor() + ") - " +
                                mapItem.getEanCode() + "(" + mapItem.getColor() + ")");
                    }
                }
            }
        }
    }

    public void updateEanCodeByCodeAndColor(SaleDocument document) {
        if (document == null) {
            return;
        }
        SaleDocumentJDBC db = new SaleDocumentJDBC();
        Map<String, String> map = db.getEanInformationAsMap();
        if (map != null) {
            for (SaleDocumentItem item : document.getDetailList()) {
                if (item != null) {
                    if (item.getItemEanCode().trim().equals("")) {

                        String key = item.getItem() + "_" + item.getItemColor();
                        String eanCode = map.get(key);
                        if (eanCode != null) {
                            item.setItemEanCode(eanCode);
                        }
                    }
                }
            }
        }
    }

    public SaleDocumentReport prepareDocumentForReport(PreOrderSaleDocument document_) {
        SaleDocumentReport result = new SaleDocumentReport();

        SaleDocumentBase document = new SaleDocumentBase(document_);
        if (document != null) {
            // Получаем базовый объект документа
            document.setDocumentType("Предварительный заказ");
            document.setDocumentStatus(0);
            document.setSenderCode(-1);
            document.setDocumentExport(document_.getCurrencyId() > 1 ? 1 : 0);
            SaleDocumentJDBC db = new SaleDocumentJDBC();
            int code = db.getContractorCodeById(document_.getContractorId());
            if (code > 0) {
                document.setRecipientCode(code);
            }

            result.setDocument(document);

            // Получаем спецификацию документа
            result.setDetailList(createDetailList(document_));

            TotalSummingUp summingUp = new TotalSummingUp();
            summingUp.setValueSumCost(document_.getSumPriceValue());
            summingUp.setValueSumVat(document_.getSumVatValue());
            summingUp.setValueSumCostAndVat(document_.getSumPriceVatValue());

            summingUp.setValueSumCostCurrency(document_.getSumPriceCurrencyValue());
            summingUp.setValueSumVatCurrency(document_.getSumVatCurrencyValue());
            summingUp.setValueSumCostAndVatCurrency(document_.getSumPriceVatCurrencyValue());

            summingUp.setValueSumAllowance(0);
            summingUp.setAmount(document_.getAmount());
            //summingUp.setP

            // Подведение итогов по документу
            result.setSummingUp(summingUp);

            // Формируем карту реквизитов документа
            HashMap<String, Object> detailMap = getDetailMapEmpty(result.getDocument(), summingUp);
            if (detailMap != null) {
                result.setDetailMap(detailMap);
            } else {
                result.setDetailMap(new HashMap<>());
            }
            // дополнительные сведения
            getAdditionalInformation(result);
            return result;
        }

        return null;
    }

    private List<SaleDocumentDetailItemReport> createDetailList(PreOrderSaleDocument document_) {
        DAOSaleDocumentFactory factory = DAOSaleDocumentFactory.getInstance();
        // Получение спецификации из БД
        ISaleDocumentDao dao = factory.getSaleDocumentDao();
        return dao.getSaleDocumentsForReport(9, String.valueOf(document_.getId()));
    }

    public void prepareMaterialComposition(SaleDocumentReport reportData) {
        Set<Integer> set = new HashSet<>();
        for (SaleDocumentDetailItemReport item : reportData.getDetailList()) {
            set.add(Integer.valueOf(item.getModelNumber()));
        }
        HashMap<Integer, ClassifierArticleComposition> map = ClassifierDAO.getClassifierArticleCompositionAsMap(set);

        for (SaleDocumentDetailItemReport item : reportData.getDetailList()) {
            ClassifierArticleComposition composition = map.get(Integer.valueOf(item.getModelNumber()));
            if (composition != null) {
                item.setLabelComposition(composition.getAllCompositions());
            }
        }
    }

    public void updateEanCodeAndColor(SaleDocumentReport report) {
        if (report == null) {
            return;
        }

        SaleDocumentJDBC db = new SaleDocumentJDBC();
        Map<Integer, BaseLabelInformation> map = db.getLabelInformationByDocumentIdAsMap(report.getDocument().getId());
        System.out.println("В спецификации документа " + map.size() + " несоответствий EAN кодов");
        if (map != null) {
            for (SaleDocumentDetailItemReport item : report.getDetailList()) {
                if (item != null) {
                    BaseLabelInformation mapItem = map.get(item.getId());
                    if (mapItem != null) {
                        //item.setItemColor(mapItem.getColor());
                        item.setEanCode(mapItem.getEanCode());
                    }
                }
            }
        }
    }
}

