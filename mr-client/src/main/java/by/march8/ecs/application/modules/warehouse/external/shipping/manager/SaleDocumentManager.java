package by.march8.ecs.application.modules.warehouse.external.shipping.manager;

import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.ICommonDaoThread;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDaoGUI;
import by.gomel.freedev.ucframework.ucdao.interfaces.INativeDao;
import by.gomel.freedev.ucframework.ucdao.jdbc.DocumentJDBC;
import by.gomel.freedev.ucframework.ucdao.model.CriteriaItem;
import by.gomel.freedev.ucframework.ucdao.model.QueryBuilder;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.march8.api.utils.DatePeriod;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.DAOSaleDocumentFactory;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.SaleDocumentQueries;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.interfaces.ISaleDocumentDao;
import by.march8.ecs.application.modules.warehouse.external.shipping.enums.SaleDocumentStatus;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.ContractorChecker;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.CurrencyName;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.DocumentUploadPreset;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.March8Marker;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.ProductionItem;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentSet;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentSheet;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentView;
import by.march8.ecs.application.modules.warehouse.external.shipping.services.RemainsReductionService;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.readonly.ContractEntity;
import by.march8.entities.readonly.ContractorEntity;
import by.march8.entities.readonly.ContractorEntityView;
import by.march8.entities.unknowns.CurrencyEntity;
import by.march8.entities.warehouse.AdjustmentSaleDocument;
import by.march8.entities.warehouse.CargoSpaceItem;
import by.march8.entities.warehouse.InvoiceType;
import by.march8.entities.warehouse.SaleDocument;
import by.march8.entities.warehouse.SaleDocumentBase;
import by.march8.entities.warehouse.SaleDocumentCurrentStatus;
import by.march8.entities.warehouse.SaleDocumentDetailItemReport;
import by.march8.entities.warehouse.SaleDocumentDriving;
import by.march8.entities.warehouse.SaleDocumentDrivingEntity;
import by.march8.entities.warehouse.SaleDocumentEntity;
import by.march8.entities.warehouse.SaleDocumentItem;
import by.march8.entities.warehouse.SaleDocumentItemView;
import lombok.NoArgsConstructor;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
public class SaleDocumentManager {
    /**
     * Определяет возвратный это документ или нет
     *
     * @param document ссылка на документ
     * @return true если документ возвратный
     */
    public static boolean isDocumentRefund(final SaleDocumentBase document) {
        return Objects.equals(document.getDocumentType(), InvoiceType.DOCUMENT_REFUND_BUYER) ||
                Objects.equals(document.getDocumentType(), InvoiceType.DOCUMENT_REFUND_RETAIL) ||
                Objects.equals(document.getDocumentType(), InvoiceType.DOCUMENT_REFUND_MATERIAL);
    }

    public static boolean isDocumentCanBeAdjusted(SaleDocumentView documentType) {
        return Objects.equals(documentType.getOperation(), InvoiceType.DOCUMENT_SALE_BUYER) ||
                Objects.equals(documentType.getOperation(), InvoiceType.DOCUMENT_SALE_MATERIAL) ||
                Objects.equals(documentType.getOperation(), InvoiceType.DOCUMENT_SALE_RETAIL);
    }

    /**
     * Является ли документ отгрузкой в фирменный магазин/розничный магазин
     *
     * @param document ссылка на документ
     * @return true если документ розничный/для фирменных магазинов
     */
    public static boolean isDocumentRetail(final SaleDocumentBase document) {
        return Objects.equals(document.getDocumentType(), InvoiceType.DOCUMENT_SALE_RETAIL);
    }

    /**
     * Метод проверяет статус документа
     *
     * @param document ссылка на документ
     * @return возвращает true если документ закрыт
     */
    public static boolean isDocumentClosed(final SaleDocumentBase document) {
        try {
            return document.getDocumentStatus() == SaleDocumentStatus.CLOSED;
        } catch (Exception e) {
            MainController.exception(e, "Ошибка при получении статуса документа");
        }
        return true;
    }

    /**
     * Метод проверяет статус документа
     *
     * @param document ссылка на документ
     * @return возвращает true если документ закрыт
     */
    public static boolean isDocumentClosed(final SaleDocumentView document) {
        try {
            return document.getStatusText().trim().equals("Закрыт");
        } catch (Exception e) {
            MainController.exception(e, "Ошибка при получении статуса документа");
        }
        return true;
    }

    /**
     * Метод возвращает true если документ помечен как удаленный
     *
     * @param document документ
     * @return true - документ помечен как удаленный
     */
    public static boolean isDocumentDeleted(final SaleDocumentBase document) {
        try {
            return document.getDocumentStatus() == SaleDocumentStatus.DELETED;
        } catch (Exception e) {
            MainController.exception(e, "Ошибка при получении статуса документа");
        }
        return false;
    }

    /**
     * Метод возвращает true если документ помечен как удаленный
     *
     * @param document документ
     * @return true - документ помечен как удаленный
     */
    public static boolean isDocumentDeleted(final SaleDocumentView document) {
        try {
            return document.getStatusText().trim().equals("Удалён");
        } catch (Exception e) {
            MainController.exception(e, "Ошибка при получении статуса документа");
        }
        return false;
    }

    /**
     * Метод изменяет статус документа
     *
     * @param documentBase ссылка на документ
     * @param status       новый статус документа
     */
    public static void changeDocumentStatus(final int documentBase, final int status) {
        SaleDocumentCurrentStatus currentStatus = new SaleDocumentCurrentStatus(documentBase);
        currentStatus.setDocumentStatus(status);
        System.err.println("Изменение статуса на " + status);
        final DaoFactory factory = DaoFactory.getInstance();
        // Получаем интерфейс для работы с БД
        final ICommonDao dao = factory.getCommonDao();
        try {
            // Обновляем сущность в БД
            dao.updateEntity(currentStatus);
        } catch (final SQLException e) {
            MainController.exception(e, "Ошибка изменения статуса документа");
        }
    }

    /**
     * Метод возвращает структуру имени валюты по идентификатору March8
     *
     * @param currencyId идентификатор валюты
     * @return имя валюты
     */
    public static CurrencyName getCurrencyNameById(int currencyId) {
        CurrencyName result = new CurrencyName();
        switch (currencyId) {
            case 1:
                result.setName("руб.коп.");
                result.setSymbol("BYR");
                break;
            case 2:
                result.setName("Рос.руб.");
                result.setSymbol("RUB");
                break;
            case 3:
                result.setName("Долл.США");
                result.setSymbol("USD");
                break;
            case 4:
                result.setName("Евро");
                result.setSymbol("EUR");
                break;
            case 5:
                result.setName("Гривна");
                result.setSymbol("UAH");
                break;
        }
        return result;
    }

    /**
     * Метод проверяет, является ли документ экспортным
     *
     * @param documentBase документ
     * @return true если документ экспортный
     */
    public static boolean isExportDocument(final SaleDocumentBase documentBase) {
        return (documentBase.getDocumentExport() == 1);
    }

    /**
     * Метод проверяет, является ли документ розничным
     *
     * @param documentBase документ
     * @return true если документ розничный
     */
    public static boolean isRetailDocument(final SaleDocumentBase documentBase) {
        return documentBase.getDocumentType().equals(InvoiceType.DOCUMENT_SALE_RETAIL)
                || documentBase.getDocumentType().equals(InvoiceType.DOCUMENT_REFUND_RETAIL);
    }

    /**
     * Изменяет статус расчета документа.
     *
     * @param documentId идентификатор документа
     * @param check      флаг проверки документа null - необходимо обновить цену ; false - расчет не произведен; true - расчет произведен
     */
    @SuppressWarnings("all")
    public static void checkDocument(int documentId, boolean check) {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                DaoFactory factory = DaoFactory.getInstance();
                INativeDao dao = factory.getNativeDao();
                Class<?> dbMarker = March8Marker.class;

                String query = "UPDATE otgruz1 SET saved = ? WHERE item_id = ?";

                dao.processing(connection -> {
                    try {
                        PreparedStatement ps = connection.prepareStatement(query);
                        ps.setBoolean(1, check);
                        ps.setInt(2, documentId);
                        ps.executeUpdate();
                    } catch (Exception e) {
                        System.out.println("Ошибка установки статуса расчета по документу [" + documentId + "]");
                        e.printStackTrace();
                    }
                    return true;
                }, dbMarker);

                return true;
            }
        }
        Task task = new Task("Изменение статуса расчета ...");
        task.executeTask();

    }

    /**
     * Метод возвращает документ по его HOMЕРУ
     *
     * @param documentNumber Номер документа
     * @return документ
     */
    public SaleDocument getSaleDocumentByNumber(String documentNumber) {
        DaoFactory<SaleDocument> factory = DaoFactory.getInstance();
        IGenericDaoGUI<SaleDocument> dao = factory.getGenericDao();
        SaleDocument document = null;
        try {
            List<QueryProperty> criteria = new ArrayList<>();
            criteria.add(new QueryProperty("ndoc", documentNumber.trim() + "-1"));
            document = dao.getEntityByNamedQueryGUI(SaleDocument.class, "SaleDocument.findByName", criteria);
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка получения документа по его номеру [" + documentNumber + "]");
        }

        return document;
    }

    /**
     * Метод возвращает документ по его ID
     *
     * @param documentId ID документа
     * @return документ
     */
    public SaleDocument getSaleDocumentById(int documentId) {
        DaoFactory<SaleDocument> factory = DaoFactory.getInstance();
        IGenericDaoGUI<SaleDocument> dao = factory.getGenericDao();

        SaleDocument document = null;

        try {
            document = dao.getEntityByIdGUI(SaleDocument.class, documentId);
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка получения документа по его номеру [" + documentId + "]");
        }

        return document;
    }

    /**
     * Метод возвращает базовый объект документа без спецификации по его ID
     *
     * @param documentId ID документа
     * @return документ
     */
    public SaleDocumentBase getSaleDocumentBaseById(int documentId) {
        DaoFactory<SaleDocumentBase> factory = DaoFactory.getInstance();
        IGenericDaoGUI<SaleDocumentBase> dao = factory.getGenericDao();

        SaleDocumentBase document = null;

        try {
            document = dao.getEntityById(SaleDocumentBase.class, documentId);
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка получения документа по его ID [" + documentId + "]");
        }

        return document;
    }

    /**
     * Метод возвращает документ по его ID
     *
     * @param documentId ID документа
     * @return документ
     */
    public SaleDocumentBase getSaleDocumentSimpleByIdGUI(int documentId) {
        DaoFactory<SaleDocumentBase> factory = DaoFactory.getInstance();
        IGenericDaoGUI<SaleDocumentBase> dao = factory.getGenericDao();

        SaleDocumentBase document = null;
        try {
            document = dao.getEntityByIdGUI(SaleDocumentBase.class, documentId);
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка получения документа по его ID");
        }
        return document;
    }

    public SaleDocumentBase getSaleDocumentSimpleById(int documentId) {
        DaoFactory<SaleDocumentBase> factory = DaoFactory.getInstance();
        IGenericDao<SaleDocumentBase> dao = factory.getGenericDao();

        SaleDocumentBase document = null;
        try {
            document = dao.getEntityById(SaleDocumentBase.class, documentId);
        } catch (SQLException e) {
            MainController.exception(e, "Ошибка получения документа по его ID");
        }
        return document;
    }

    /**
     * Возвращает Контрагента по его коду(не идентификатор)
     *
     * @param contractorCode код контрагента
     * @return контагент
     */
    public ContractorEntityView getContractorViewByCode(int contractorCode) {
        DaoFactory<ContractorEntityView> factoryDocument = DaoFactory.getInstance();
        IGenericDao<ContractorEntityView> dao = factoryDocument.getGenericDao();

        QueryBuilder queryBuilder = new QueryBuilder(ContractorEntityView.class);
        queryBuilder.addCriteria(new CriteriaItem(contractorCode, "code", "="));
        try {
            return dao.getEntityByQuery(ContractorEntityView.class, queryBuilder.getQuery());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ContractorEntity getContractorByCode(int contractorCode, String documentNumber) {
        DaoFactory<ContractorEntity> factoryDocument = DaoFactory.getInstance();
        IGenericDao<ContractorEntity> dao = factoryDocument.getGenericDao();

        QueryBuilder queryBuilder = new QueryBuilder(ContractorEntity.class);
        queryBuilder.addCriteria(new CriteriaItem(contractorCode, "code", "="));
        try {
            return dao.getEntityByQuery(ContractorEntity.class, queryBuilder.getQuery());
        } catch (SQLException e) {
            System.err.println("Ошибка заполнения контрагента [" + contractorCode + "] в документе [" + documentNumber + "]");
            //e.printStackTrace();
        }
        return null;
    }

    /**
     * Метод возвращает список контрактов по идентификатору контрагента
     *
     * @param contractorId идентификатор контрагента
     * @return список объектов
     */
    public List<ContractEntity> getContractsByContractorId(int contractorId) {
        DaoFactory<ContractEntity> factory = DaoFactory.getInstance();
        IGenericDao<ContractEntity> dao = factory.getGenericDao();

        QueryBuilder queryBuilder = new QueryBuilder(ContractEntity.class);
        queryBuilder.addCriteria(new CriteriaItem(contractorId, "contractor", "="));
        try {
            return dao.getEntityListByQuery(ContractEntity.class, queryBuilder.getQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Такие пляски из-за того что в базах, валюты имеют разный ID.
     * С переездом в единую базу код измениться, хотя кого я обманываю
     *
     * @param currency      march8 код валюты
     * @param dateOperation дата курса
     * @param isFixedRate   признак первого дня месяца. Если true - дата в пределах месяца не важна, будет выбран курс на начало месяца
     * @return курс валюты
     */
    @SuppressWarnings("all")
    public float getCurrencyRateValue(int currency, Date dateOperation, boolean isFixedRate) {

        if (currency <= 1) {
            return 1f;
        }

        if (dateOperation == null) {
            return 0;
        }

        String currencyTemp;
        int currencyId = 0;

        switch (currency) {

            case 2: {
                currencyTemp = "RUB";
                currency = 17;
                break;
            }
            case 3: {
                currencyTemp = "USD";
                currency = 15;
                break;
            }
            case 4: {
                currencyTemp = "EUR";
                currency = 19;
                break;
            }
            case 5: {
                currencyTemp = "UAH";
                currency = 18;
                break;
            }

            default:
                break;
        }
        Date first;

        // Если получаем курс на текущую дату
        if (!isFixedRate) {
            first = dateOperation;
        } else {
            // Если получаем курс на первое чило заданного месяца
            first = DateUtils.getFirstDay(dateOperation);
        }

        DaoFactory<CurrencyEntity> factoryDocument = DaoFactory.getInstance();
        IGenericDao<CurrencyEntity> dao = factoryDocument.getGenericDao();

        CurrencyEntity currencyEntity;

        String dateFormatted = DateUtils.getNormalDateFormat(first);
        QueryBuilder queryBuilder = new QueryBuilder(CurrencyEntity.class);
        queryBuilder.addCriteria(new CriteriaItem(dateFormatted, "rateDate", "date equals"));
        queryBuilder.addCriteria(new CriteriaItem(currency, "currencyId", "="));
        try {

            currencyEntity = dao.getEntityByQuery(CurrencyEntity.class, queryBuilder.getQuery());
            if (currencyEntity != null) {
                return currencyEntity.getRate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Определяет факт отгрузки материала по документу
     *
     * @param document ссылка на документ
     * @return true если по документ отгружается материал
     */
    public boolean isDocumentMaterialsSale(final SaleDocumentBase document) {
        return Objects.equals(document.getDocumentType(), InvoiceType.DOCUMENT_REFUND_MATERIAL) ||
                Objects.equals(document.getDocumentType(), InvoiceType.DOCUMENT_SALE_MATERIAL);
    }

    /**
     * Метод получает максимальный номер партии принятых из терминала изделий.
     * Таким образом возможно отследить во сколько этапов производилось формирование
     * документа на отгрузку/приемку
     *
     * @param documentId идентификатор документа
     * @return номер партии
     */
    @SuppressWarnings("all")
    public int getPartNumberByDocumentId(int documentId) {
        final int[] partNumber = {0};
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao daoNative = factory.getNativeDao();
        daoNative.processing(connection -> {
            try {
                String query = "SELECT MAX(part) AS PARTMAX FROM otgruz2 WHERE doc_id=?";
                PreparedStatement prepStatement = connection.prepareStatement(query);
                prepStatement.setInt(1, documentId);
                ResultSet result = prepStatement.executeQuery();
                while (result.next()) {
                    partNumber[0] = result.getInt(1);
                }
            } catch (Exception ex) {
                System.err.println("Ошибка выполнения метода getPartNumberByDocumentId(int " + documentId + "):"
                        + ex.getMessage());
            }
            return true;
        }, March8Marker.class);
        return partNumber[0];
    }

    /**
     * Метод получает запись путевого листа для накладной
     *
     * @param documentId идентификатор документ
     * @return путевой лист
     */
    public SaleDocumentDriving getDrivingDocumentationByDocumentId(final int documentId) {
        DaoFactory<SaleDocumentDriving> factoryDocument = DaoFactory.getInstance();
        IGenericDao<SaleDocumentDriving> dao = factoryDocument.getGenericDao();
        QueryBuilder queryBuilder = new QueryBuilder(SaleDocumentDriving.class);
        queryBuilder.addCriteria(new CriteriaItem(documentId, "saleDocumentId", "="));
        SaleDocumentDriving result = null;
        try {
            result = dao.getEntityByQuery(SaleDocumentDriving.class, queryBuilder.getQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public SaleDocumentDrivingEntity getDrivingEntityByDocumentId(final int documentId) {
        DaoFactory<SaleDocumentDrivingEntity> factoryDocument = DaoFactory.getInstance();
        IGenericDao<SaleDocumentDrivingEntity> dao = factoryDocument.getGenericDao();
        QueryBuilder queryBuilder = new QueryBuilder(SaleDocumentDrivingEntity.class);
        queryBuilder.addCriteria(new CriteriaItem(documentId, "saleDocumentId", "="));
        SaleDocumentDrivingEntity result = null;
        try {
            result = dao.getEntityByQuery(SaleDocumentDrivingEntity.class, queryBuilder.getQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Метод получает запись путевого листа для накладной(Старый способ)
     *
     * @param document документ
     * @return путевой лист
     */
    @SuppressWarnings("unused")
    public SaleDocumentDriving getDrivingDocumentationByDocumentIdOld(final SaleDocumentBase document) {
        SaleDocumentDriving result;
        DAOSaleDocumentFactory factory = DAOSaleDocumentFactory.getInstance();
        ISaleDocumentDao dao = factory.getSaleDocumentDao();
        try {
            result = dao.getSaleDocumentDrivingByDocumentId(document.getId());
            if (result != null) {
                result.setShipperId(1);
                result.setConsigneeId(document.getRecipientId());

                result.setLoadingAddressId(-1);
                result.setUnloadingAddressId(-1);
                result.setSealNumberId(-1);
                result.setLoadingDoer(-1);
                result.setLoadingMethod(-1);
                result.setReaddressing("");
            }

        } catch (Exception e) {
            return null;
        }
        return result;
    }

    /**
     * Метод возвращает true если документ открыт и находится на этапе формирования
     *
     * @param document документ
     * @return true - документ открыт и формируется
     */
    public boolean isDocumentOpened(final SaleDocumentBase document) {
        try {
            return document.getDocumentStatus() == SaleDocumentStatus.FORMED;
        } catch (Exception e) {
            MainController.exception(e, "Ошибка при получении статуса документа");
        }
        return false;
    }

    /**
     * Метод проверяет, является ли документ экспортным
     *
     * @param document документ
     * @return true если документ экспортный
     */
    public boolean isExportDocument(final SaleDocument document) {
        return (document.getDocumentExport() == 1);
    }

    public boolean isDetailsChanged(final ArrayList<Object> data) {
        for (Object o : data) {
            SaleDocumentItemView item = (SaleDocumentItemView) o;
            if (item != null) {
                if (item.isChanged()) {
                    System.out.println("В одной из позиций спецификации изменена цена изделия");
                    return true;
                }
            }
        }
        System.out.println("Данные без изменений");
        return false;

    }

    /**
     * Метод сохраняет изменения цены через ручное редактирование спецификации
     *
     * @param data список спецификации по изделию
     * @return true в случае успешного сохранения изменений
     */
    @SuppressWarnings("all")
    public boolean saveChangesForDetails(final ArrayList<Object> data) {

        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {

                DaoFactory factory = DaoFactory.getInstance();
                INativeDao dao = factory.getNativeDao();
                Class<?> dbMarker = March8Marker.class;

                String query = "UPDATE otgruz2 SET cena = ?, cenav = ?, cena_uch = ? WHERE item_id = ?";

                for (Object o : data) {
                    SaleDocumentItemView item = (SaleDocumentItemView) o;
                    if (item != null) {
                        if (item.isChanged()) {
                            dao.processing(connection -> {
                                try {

                                    PreparedStatement ps = connection.prepareStatement(query);
                                    ps.setDouble(1, item.getValuePrice());
                                    ps.setDouble(2, item.getValuePriceCurrency());
                                    ps.setDouble(3, item.getValuePriceForAccounting_());
                                    ps.setInt(4, item.getId());
                                    ps.execute();

                                    item.setChanged(false);
                                    return true;
                                } catch (Exception e) {
                                    System.out.println("Ошибка изменения цены для изделия " + item.getItemName() + " [" + item.getId() + "]");
                                    e.printStackTrace();
                                    return false;
                                }

                            }, dbMarker);

                        }
                    }
                }
                return true;
            }
        }
        Task task = new Task("Сохранение цен...");
        task.executeTask();

        return true;
    }

    /**
     * Метод обновляет учетную цену изделий из спецификации документа
     *
     * @param documentId идентификатор документа
     * @param updateType тип обновления. 0 - оптовая цена классификатора, 1 - экспортная цена классификатора, 2 - Оптовая цена из классификатора а затем из истории движений по изделию
     */
    @SuppressWarnings("unused")
    public void updateSaleDocumentPrice(final int documentId, int updateType) {
        class Task extends BackgroundTask {
            public Task(final String messageText) {
                super(messageText);
            }

            @Override
            protected Boolean doInBackground() throws Exception {
                // Группировка изделий в накладной
                //System.err.println("Группировка изделий в документе");

                DaoFactory factory = DaoFactory.getInstance();
                INativeDao daoNative = factory.getNativeDao();
                daoNative.processing(connection -> {
                    try {
                        String query = "{ call PRC_SPLIT_RECORDS_OTGRUZ2_EAN (?) }";
                        CallableStatement cs = connection.prepareCall(query);
                        cs.setInt(1, documentId);
                        cs.execute();
                    } catch (Exception ex) {
                        System.err.println("Ошибка выполнения хранимой процедуры:"
                                + ex.getMessage());
                    }
                    return true;
                }, March8Marker.class);

                System.err.println("Обновление цен на изделия");
                INativeDao daoNative1 = factory.getNativeDao();
                daoNative1.processing(connection -> {
                    try {
                        String query = "{ call PRC_UPDATE_CEN_UCH_OTGRUZ2 (?) }";
                        CallableStatement cs = connection.prepareCall(query);
                        cs.setInt(1, documentId);
                        cs.execute();
                    } catch (Exception ex) {
                        System.err.println("Ошибка выполнения хранимой процедуры:"
                                + ex.getMessage());
                    }
                    return true;
                }, March8Marker.class);
                return true;
            }
        }
        Task task = new Task("Получение и обновление цен...");
        task.executeTask();

    }

    /**
     * Возвращает мапу наименований продукции для документа
     *
     * @param documentId идентификатор документа
     * @return мапа наименований
     */
    public HashMap<Integer, ProductionItem> getProductionItemMapByDocuments(int documentId) {
        HashMap<Integer, ProductionItem> result = new HashMap<>();

        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        Class<?> dbMarker = March8Marker.class;

        // Запрос на отбор данных из классификатора
        String queryProductionItem = SaleDocumentQueries.queryGetProductionItemByDocument;
        // Получаем список цен для изделий из классификатора
        dao.processing(connection -> {
            try {
                PreparedStatement ps = connection.prepareStatement(queryProductionItem);
                ps.setInt(1, documentId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    ProductionItem item = new ProductionItem();
                    item.setId(rs.getInt("id"));
                    item.setItemCode(rs.getInt("item_code"));
                    item.setItemModel(rs.getString("model").trim());
                    item.setArticleName(rs.getString("article_name").trim());
                    item.setArticleCode(rs.getInt("article_code"));

                    result.put(rs.getInt("item_code"), item);
                }

            } catch (Exception e) {
                System.out.println("Ошибка получения цен из классификатора для документа [" + documentId + "]");
                e.printStackTrace();
            }
            return true;
        }, dbMarker);

        return result;
    }

    public List<CargoSpaceItem> getCargoSpaceListByDocumentId(final int documentId) {
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        Class<?> dbMarker = March8Marker.class;

        List<CargoSpaceItem> result = new ArrayList<>();

        // Запрос на отбор данных из классификатора
        String queryProductionItem = SaleDocumentQueries.queryGetCargoSpaceByDocumentNumber;

        // Получаем список цен для изделий из классификатора
        dao.processing(connection -> {
            try {
                PreparedStatement ps = connection.prepareStatement(queryProductionItem);
                ps.setInt(1, documentId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    CargoSpaceItem item = new CargoSpaceItem();
                    item.setId(rs.getInt("id"));
                    item.setDocumentId(rs.getInt("document_id"));
                    item.setCargoSpace(rs.getInt("cargo_space"));

                    result.add(item);
                }

            } catch (Exception e) {
                System.err.println("Ошибка получения грузомест для документа [" + documentId + "]");
                e.printStackTrace();
            }
            return true;
        }, dbMarker);
        return result;
    }


    public boolean checkChildDocument(final ArrayList<Object> data) {
        boolean old = false;
        boolean child = false;
        for (Object o : data) {
            SaleDocumentItemView item = (SaleDocumentItemView) o;
            if (item != null) {
                if (SaleDocumentDataProvider.isChildItem(String.valueOf(item.getArticleCode()))) {
                    child = true;
                } else {
                    old = true;
                }

            }
        }
        return child && old;
    }

    // Проверка разных НДС
    public boolean checkNDSDocument(final ArrayList<Object> data) {
        double prevNDS = ((SaleDocumentItemView) data.get(0)).getValueVat();
        double notValidNDS = -1;
        for (Object o : data) {
            SaleDocumentItemView item = (SaleDocumentItemView) o;
            if (item != null) {
                if (prevNDS != ((SaleDocumentItemView) o).getValueVat()) {
                    notValidNDS = ((SaleDocumentItemView) o).getValueVat();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Проверяет корректность ввода контрагента: Почтовый адрес, Юридический адрес, Активный договор(под сомнением)
     * Расчетный счет(под сомнением)
     *
     * @param contractorId идентификатор контрагента
     * @return результат успеха проверки, true - контрагент заполнен корректно
     */
    public boolean checkContractor(final int contractorId) {
        DocumentJDBC db = new DocumentJDBC();
        ContractorChecker contractor = db.getContractorCheckerByContractorId(contractorId);
        if (contractor != null) {
            List<String> checkResult = new ArrayList<>();
            if (contractor.getPostAddressId() < 1) {
                checkResult.add("Почтовый адрес не установлен");
            }

            if (contractor.getLegalAddressId() < 1) {
                checkResult.add("Юридический адрес не установлен");
            }
            //Обнаружены ошибки заполнения контрагента
            if (checkResult.size() > 0) {
                //"<html><div style=\"text-align: center;\"> Прейскурант с именем " + "<font color=\"green\">" +
                //element + "</font>" + " уже существует в базе"
                //        + "</html>"
                String result = "<html><div style=\"text-align: left;\"> Контрагент " + "<font color=\"green\">"
                        + contractor.getName() + " (код " + contractor.getContractorCode() + ")</font> заполнен некорректно:";
                String prefix = "<p><font color=\"red\">";
                String postfix = "</font></p>";

                //Формируем строку для вывода пользователю
                for (String s : checkResult) {
                    result += prefix + s + postfix;
                }

                result += "<p>Обратитесь в отдел сбыта для корректировки данных.</html>";

                Dialogs.showInformationDialog(result);
                return false;
            }
            return true;
        }

        return false;
    }

    public List<SaleDocumentItemView> getEntityListByNamedQuery(final String criteria) {
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        Class<?> dbMarker = March8Marker.class;

        String query = "SELECT * FROM V_SaleDocumentItems where ndoc= ?";
        List<SaleDocumentItemView> result = new ArrayList<>();

        dao.processing(connection -> {
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, criteria);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    SaleDocumentItemView item = new SaleDocumentItemView();

                    item.setId(rs.getInt("item_id"));
                    item.setDocumentId(rs.getInt("doc_id"));

                    item.setDocumentNumber(rs.getString("ndoc"));
                    item.setItemId(rs.getInt("kod_izd"));
                    item.setAmount(rs.getInt("kol"));
                    item.setAmountInPack(rs.getInt("kol_in_upack"));
                    item.setItemEanCode(rs.getString("eancode"));
                    item.setItemScanCode(rs.getString("scan"));
                    item.setItemColor(rs.getString("ncw"));
                    item.setItemGrade(rs.getInt("srt"));

                    item.setValuePriceForAccounting(rs.getDouble("cena_uch"));
                    item.setValueVAT(rs.getFloat("nds"));

                    item.setValuePrice(rs.getDouble("cena"));
                    item.setValueSumCost(rs.getDouble("summa"));
                    item.setValueSumVat(rs.getDouble("summa_nds"));
                    item.setValueSumCostAndVat(rs.getDouble("itogo"));

                    item.setValuePriceCurrency(rs.getDouble("cenav"));
                    item.setValueSumCostCurrency(rs.getDouble("summav"));
                    item.setValueSumVatCurrency(rs.getDouble("summa_ndsv"));
                    item.setValueSumCostAndVatCurrency(rs.getDouble("itogov"));
                    item.setPriceWholesale(rs.getFloat("cno"));

                    item.setProductName(rs.getString("ngpr"));
                    item.setModelNumber(rs.getInt("fas"));
                    item.setArticleName(rs.getString("nar"));
                    item.setArticleCode(rs.getInt("sar"));

                    item.setTradeMarkValue(rs.getFloat("torg_nadb"));
                    item.setRetailPriceValue(rs.getFloat("rozn_cena"));
                    item.setItemSize(rs.getInt("rzm_marh"));
                    item.setItemPrintSize(rs.getString("rzm_print"));

                    item.setDiscount(rs.getString("discount"));
                    item.setNsiVAT(rs.getFloat("nsi_vat"));

                    result.add(item);
                }
            } catch (Exception e) {
                System.out.println("Ошибка установки статуса расчета по документу [" + criteria + "]");
                e.printStackTrace();
            }
            return true;
        }, dbMarker);

        return result;
    }

    public double getTotalSumByDocumentId(final int documentId) {
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        Class<?> dbMarker = March8Marker.class;

        String query = "select SUM(itogo) from otgruz2 where doc_id = ?";

        final double[] result = {0};

        dao.processing(connection -> {
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, documentId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    result[0] = rs.getDouble(1);
                }
            } catch (Exception e) {
                System.out.println("Ошибка получения итоговой суммы с НДС для документа [" + documentId + "]");
                e.printStackTrace();
            }
            return true;
        }, dbMarker);

        return result[0];
    }

    public Date getSaleDateByDocumentId(final int documentId) {

        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        Class<?> dbMarker = March8Marker.class;

        String query = "select sale_date from otgruz1 where item_id = ?";

        final Date[] result = {null};

        dao.processing(connection -> {
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, documentId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    result[0] = rs.getDate(1);
                }
            } catch (Exception e) {
                System.out.println("Ошибка получения даты расчета для документа [" + documentId + "]");
                e.printStackTrace();
            }
            return true;
        }, dbMarker);

        return result[0];
    }

    /**
     * ПОиск уцененных изделий в документе
     *
     * @param documentBase документ
     * @param data         спецификация документа
     * @return true если документ содержит пересортицу в торговых надбавках
     */
    public boolean isExistRemainsReduction(final SaleDocumentBase documentBase, final ArrayList<Object> data) {
        // Инициализируем службу уценок остатков
        RemainsReductionService remainsReductionService = RemainsReductionService.getInstance();

        ArrayList<String> array = new ArrayList<>();

        for (Object o : data) {
            SaleDocumentItemView item = (SaleDocumentItemView) o;
            item.setWrongAllowance(false);
            if (item != null) {
                int markup = remainsReductionService.isHaveReductionByArticleNumberAndSize(item.getArticleName(), item.getItemSize(), (int) documentBase.getTradeMarkValue());

                if (markup != -1) {
                    //array.add(item.getArticleName() + " - т/н " + markup + " %");
                    item.setWrongAllowance(true);
                }
            }
        }

        if (array.size() > 0) {
            System.out.println("Найдена пересортица для изделий (" + array.size() + ")");
            array.forEach(System.out::println);

            String text = "";
            int step = 0;
            for (String s : array) {
                text += "<p><font color=\"blue\">" + s + "</font></p>";
                step++;
                if (step > 15) {
                    text += "<p><font color=\"blue\">Больше 15 изделий...</font></p>";
                    break;
                }
            }

            return Dialogs.showQuestionDialog("<html><div style=\"text-align: left;\">Документ содержит пересортицу торговых надбавок, расчет не закончен..." +
                    text + "<p>Продолжить расчет ?</div></html>", "Пересортица данных") != 0;
        } else {
            return false;
        }
    }

    public SaleDocumentDetailItemReport getReportItemByEanCode(final String eanCode) {
        if (eanCode == null) {
            return null;
        }

        if (eanCode.trim().length() < 13) {
            return null;
        }

        ArrayList<SaleDocumentDetailItemReport> data = new ArrayList<>();
        Class<?> dbMarker = March8Marker.class;
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        dao.processing(connection -> {
            try {
                String query = SaleDocumentQueries.sqlGetItemByEanCode;

                PreparedStatement ps = connection.prepareStatement(query);

                ps.setString(1, eanCode);

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {

                    SaleDocumentDetailItemReport item = new SaleDocumentDetailItemReport();
                    item.setId(rs.getInt("kod1"));
                    item.setDocument("xx1111111");

                    item.setArticleCode(rs.getString("sar"));
                    item.setArticleNumber(rs.getString("nar"));
                    item.setModelNumber(rs.getString("fas"));
                    item.setItemName(rs.getString("ngpr"));

                    //item.setComposition(rs.getString("composition"));

                    item.setTnvedCode(rs.getString("narp"));
                    item.setEanCode(eanCode);
                    //item.setItemColor(rs.getString("item_color"));
                    item.setItemGrowz(rs.getInt("rst"));
                    item.setItemSize(rs.getInt("rzm"));
                    item.setItemSizePrint(rs.getString("rzm_print"));
                    item.setItemGrade(rs.getInt("srt"));
                    item.setGradeAsString(String.valueOf(item.getItemGrade()));
                    //item.setAmount(rs.getInt("amount"));
                    //item.setAmountAll(rs.getInt("amount_all"));

                    item.setValueAccountingPrice(rs.getDouble("cno"));
                    item.setAccountingVat(rs.getFloat("nds"));

                    //item.setValuePrice(rs.getDouble("price"));
                    // item.setValueSumCost(rs.getDouble("sum_cost"));
                    //item.setValueSumVat(rs.getDouble("sum_vat"));
                    //item.setValueSumCostAndVat(rs.getDouble("sum_cost_vat"));

                    item.setValuePriceCurrency(0);
                    item.setValueSumCostCurrency(0);
                    item.setValueSumVatCurrency(0);
                    item.setValueSumCostAndVatCurrency(0);
                    item.setValueTradeMarkup(0.0);
                    item.setValueRetailPrice(0.0);

                    item.setWeight(rs.getDouble("massa"));
                    item.setItemPriceList(rs.getString("preiscur"));
                    //item.setColorCode(rs.getInt("cd_id"));
                    item.setPtkCode(rs.getInt("ptk"));
                    item.setItemAgeGroup(rs.getInt("vzr"));

                    data.add(item);
                }
            } catch (Exception ex) {
                System.err.println("Ошибка получение списка документов ");
                ex.printStackTrace();
            }
            return true;
        }, dbMarker);
        return data.get(0);
    }

    public HashMap<String, Integer> getColorDiffMapMyDocumentId(int documentId) {
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        Class<?> dbMarker = March8Marker.class;

        String query = "select * from V_OTGRUZ_COLOR_DIFF where DOCUMENT_ID = ?";

        final HashMap<String, Integer> result = new HashMap<>();

        dao.processing(connection -> {
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, documentId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    result.put(rs.getString("EAN"), rs.getInt("COLOR_ID"));
                }
            } catch (Exception e) {
                System.out.println("Ошибка метода getColorDiffMapMyDocumentId [" + documentId + "]");
                e.printStackTrace();
            }
            return true;
        }, dbMarker);

        return result;

    }

    /**
     * Метод проверяет наличие несортных изделий в документе сортных
     *
     * @param documentBase документ
     * @param data         спецификация документа
     * @return true - в документе несортные изделия
     */
    public boolean checkNoSortDocument(SaleDocumentBase documentBase, ArrayList<Object> data) {

        boolean documentGrades = documentBase.getPriceReduction3Grade() > 0;

        boolean noSort = false;

        for (Object o : data) {
            SaleDocumentItemView item = (SaleDocumentItemView) o;
            if (item != null) {
                item.setWrongAllowance(false);
                if (SaleDocumentDataProvider.isSocksItem(String.valueOf(item.getArticleCode()))) {
                    if (documentGrades) {
                        if (item.getItemGrade() == 1) {
                            item.setWrongAllowance(true);
                            noSort = true;
                        }
                    } else {
                        if (item.getItemGrade() > 1) {
                            item.setWrongAllowance(true);
                            noSort = true;
                        }
                    }
                } else {
                    if (documentGrades) {
                        if (item.getItemGrade() < 3) {
                            item.setWrongAllowance(true);
                            noSort = true;
                        }
                    } else {
                        if (item.getItemGrade() > 2) {
                            item.setWrongAllowance(true);
                            noSort = true;
                        }
                    }
                }
            }
        }
        return noSort;
    }

    public SaleDocument createDifferentSaleDocument(SaleDocument documentSale, SaleDocument documentRefund) {


        // Ключ - Код изделия, Цвет, EAN
        String key = "";
        HashMap<String, SaleDocumentItem> map = new HashMap<>();
        // Создаем документ

        SaleDocument document = new SaleDocument(documentSale);

        document.setId(0);
        document.setDocumentDate(documentSale.getDocumentDate());

        // Заполняем мапу на основании исходного документа
        for (SaleDocumentItem item : documentSale.getDetailList()) {
            // Формируем ключ
            key = item.getItem() + "_" + item.getItemColor() + "_" + item.getItemEanCode();
            SaleDocumentItem item_ = map.get(key);
            if (item_ == null) {
                item_ = new SaleDocumentItem(item);
                document.getDetailList().add(item_);
                item_.setDocument(document);
                item_.setId(0);
                item_.setItemScanCode(0l);

                if (item_.getItemType() == 2) {
                    item_.setItemType(1);
                    int packCount = item_.getAmount();
                    int inPackCount = item_.getAmountInPack();

                    item_.setAmount(packCount * inPackCount);
                    item_.setAmountInPack(1);
                } else {

                    int packCount = item_.getAmount();
                    int inPackCount = item_.getAmountInPack();

                    item_.setAmount(packCount * inPackCount);
                    item_.setAmountInPack(1);
                }
                map.put(key, item_);
            } else {
                int packCount = item.getAmount();
                int inPackCount = item.getAmountInPack();

                item_.setAmount(item_.getAmount() + (packCount * inPackCount));
            }
        }

        // Вычитаем из результирующего набора возврат

        for (SaleDocumentItem item : documentRefund.getDetailList()) {
            // Формируем ключ
            key = item.getItem() + "_" + item.getItemColor() + "_" + item.getItemEanCode();
            SaleDocumentItem item_ = map.get(key);
            int notFound = 0;
            if (item_ == null) {
                // Такое изделие не найдено в документе
                notFound++;
                System.out.println("Изделие не найдено в документе: [" + key + "]" + notFound);
            } else {
                int packCount = item.getAmount();
                int inPackCount = item.getAmountInPack();
                item_.setAmount(item_.getAmount() - (packCount * inPackCount));
                item_.setAmountInPack(1);
            }
        }

        System.out.println("До удаления " + document.getDetailList().size());

        List<SaleDocumentItem> removeList = new ArrayList<>();

        for (SaleDocumentItem item : document.getDetailList()) {
            //System.out.println("[" + item.getAmount() + "] - [" + item.getAmountInPack() + "]"+item.getItem());
            if (item.getAmount() < 1) {
                System.out.println("Удаляем " + item.getAmount() + " - " + item.getItemEanCode());
                removeList.add(item);
            }
        }

        for (SaleDocumentItem item : removeList) {
            document.getDetailList().remove(item);
        }

        System.out.println("После удаления " + document.getDetailList().size());

        return document;
    }

    public List<SaleDocumentEntity> getRetailSaleDocumentsByPreset(DocumentUploadPreset preset) {
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        Class<?> dbMarker = March8Marker.class;

        String query = SaleDocumentQueries.sqlRetailInventoryByPeriodAndContractor;
        List<SaleDocumentEntity> result = new ArrayList<>();

        dao.processing(connection -> {
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setDate(1, new java.sql.Date(preset.getPeriodBegin().getTime()));
                ps.setDate(2, new java.sql.Date(preset.getPeriodEnd().getTime()));
                ps.setInt(3, preset.getContractorCode());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    SaleDocumentEntity item = new SaleDocumentEntity();

                    item.setId(rs.getInt("DOCUMENT_ID"));
                    item.setDocumentNumber(rs.getString("DOCUMENT_NUMBER"));
                    item.setDocumentDate(rs.getDate("DOCUMENT_DATE"));

                    result.add(item);
                }
            } catch (Exception e) {
                System.out.println("Ошибка выполнения метода getRetailSaleDocumentsByPreset()");
                e.printStackTrace();
            }
            return true;
        }, dbMarker);

        return result;
    }

    public List<SaleDocumentEntity> getSaleDocumentsByPreset(DocumentUploadPreset preset) {
        DaoFactory factory = DaoFactory.getInstance();
        INativeDao dao = factory.getNativeDao();
        Class<?> dbMarker = March8Marker.class;

        String query = SaleDocumentQueries.sqlInventoryByPeriodAndContractorAndUAddress;
        List<SaleDocumentEntity> result = new ArrayList<>();

        dao.processing(connection -> {
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setDate(1, new java.sql.Date(preset.getPeriodBegin().getTime()));
                ps.setDate(2, new java.sql.Date(preset.getPeriodEnd().getTime()));
                ps.setInt(3, preset.getContractorCode());
                ps.setInt(4, preset.getAddressId());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    SaleDocumentEntity item = new SaleDocumentEntity();

                    item.setId(rs.getInt("DOCUMENT_ID"));
                    item.setDocumentNumber(rs.getString("DOCUMENT_NUMBER"));
                    item.setDocumentDate(rs.getDate("DOCUMENT_DATE"));

                    result.add(item);
                }
            } catch (Exception e) {
                System.out.println("Ошибка выполнения метода getSaleDocumentsByPreset()");
                e.printStackTrace();
            }
            return true;
        }, dbMarker);

        return result;
    }

    public SaleDocument createSaleDocumentCopy(SaleDocument originalDocument) {

        SaleDocument newDocument = new SaleDocument(originalDocument);
        newDocument.setDetailList(new ArrayList<>());
        List<SaleDocumentItem> items = newDocument.getDetailList();

        for (SaleDocumentItem item : originalDocument.getDetailList()) {
            SaleDocumentItem item_ = new SaleDocumentItem(item);
            item_.setDocument(newDocument);
            items.add(item_);
        }

        return newDocument;
    }

    public void saveDocument(SaleDocumentBase document) {
        final DaoFactory factory = DaoFactory.getInstance();
        // ПОлучаем интерфейс для работы с БД
        final ICommonDaoThread dao = factory.getCommonDaoThread();
        try {
            dao.updateEntityThread(document);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveSaleDocumentDriving(SaleDocumentDriving document) {
        final DaoFactory factory = DaoFactory.getInstance();
        // ПОлучаем интерфейс для работы с БД
        final ICommonDaoThread dao = factory.getCommonDaoThread();
        try {
            dao.saveEntityThread(document);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public SaleDocument saveSaleDocument(SaleDocument document) {
        // Сохраним изменения в базу после расчета документа
        DaoFactory factory = DaoFactory.getInstance();
        ICommonDaoThread dao = factory.getCommonDaoThread();
        // Обновляем документ в базе
        try {
            if (document.getId() > 0) {
                dao.updateEntityThread(document);
                return document;
            } else {
                return (SaleDocument) dao.saveEntityThread(document);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void adjustmentRegistration(SaleDocument originalDocument, SaleDocument newDocument, String cause) {
        AdjustmentSaleDocument adjustment = new AdjustmentSaleDocument();
        adjustment.setCurrentDocumentId(newDocument.getId());
        adjustment.setOldDocumentId(originalDocument.getId());
        adjustment.setAdjustmentDate(new Date());
        adjustment.setCause(cause);

        final DaoFactory factory = DaoFactory.getInstance();
        final ICommonDaoThread dao = factory.getCommonDaoThread();
        try {
            dao.saveEntityThread(adjustment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SaleDocumentSheet getSaleDocumentPerContractorAndYear(int contractor, int... year) {
        SaleDocumentSheet result = new SaleDocumentSheet();
        result.setContractor(getContractorViewByCode(contractor));
        for (int y : year) {
            List<DatePeriod> periodList = DateUtils.getPeriodListForYear(y);
            List<SaleDocumentSet> documentSet = new ArrayList<>();
            for (DatePeriod period : periodList) {
                SaleDocumentSet set_ = new SaleDocumentSet();
                set_.setYear(y);
                set_.setPeriod(period);
                set_.setDocuments(getSaleDocumentByContractorCodeAndPeriod(contractor, period));
                documentSet.add(set_);
            }

            result.getDocumentSet().add(documentSet);
        }
        return result;
    }

    public List<SaleDocumentBase> getSaleDocumentByContractorCodeAndPeriod(int contractorId, DatePeriod period) {
        DaoFactory<SaleDocumentBase> factory = DaoFactory.getInstance();
        IGenericDao<SaleDocumentBase> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("periodBegin", period.getBegin()));
        criteria.add(new QueryProperty("periodEnd", period.getEnd()));
        criteria.add(new QueryProperty("contractor", contractorId));
        List<SaleDocumentBase> list = null;
        try {
            list = dao.getEntityListByNamedQuery(SaleDocumentBase.class, "SaleDocumentBase.findByPeriodAndContractorCode", criteria);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
