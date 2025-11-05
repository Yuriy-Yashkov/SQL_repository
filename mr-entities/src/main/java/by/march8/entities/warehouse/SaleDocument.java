package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.utils.DateUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = "SaleDocument.findAllPerPeriod",
                query = "SELECT doc FROM SaleDocument doc WHERE doc.documentDate BETWEEN :beginDate AND :endDate " +
                        "order by " +
                        "doc.documentDate ASC "),
        @NamedQuery(name = "SaleDocument.findByName",
                query = "SELECT doc FROM SaleDocument doc WHERE doc.documentNumber = :ndoc " +
                        "order by " +
                        "doc.documentDate ASC")

})


/**
 * Описание таблицы otgruz1 программы March8
 * Created by Andy on 18.08.2015.
 */
@Entity
@Table(name = "otgruz1")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
@SuppressWarnings("all")
public class SaleDocument extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Дата создания документа
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    private Date documentDate;
    /**
     * Номер документа
     */
    @Column(name = "ndoc")
    private String documentNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sale_date")
    private Date documentSaleDate;

    /**
     * Статус документа
     * 0 - закрыт
     * 1 - удален
     * 2 - неизвесно
     * 3 - формируется(только чтение)
     * 8 - формируется(запись)
     */
    @Column(name = "status")
    private int documentStatus;

    /**
     * Тип документа:
     * Отгрузка покупателю
     * Перемещение в розницу
     * Возврат от покупателя
     * Возврат из розницы
     */
    @Column(name = "operac")
    private String documentType;

    /**
     * Признак экспортной накладной
     * 0 - нет
     * 1 - да
     */
    @Column(name = "export")
    private int documentExport;

    /**
     * Признак документа по предоплате
     */
    @Column(name = "prepayment")
    private boolean prepayment;

    /**
     * Дата предоплаты по документу
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "prepayment_date")
    private Date prepaymentDate ;

    /**
     * Старая дебильная система, баба ЯгА против. Долбаная совместимость...
     * Признак начисления НДС по накладной
     * 1 - НДС сверху
     * 2 - без НДС
     *
     */
    @Column(name = "nalogi")
    private int documentVATType;

    @Column(name="vat_value")
    private float documentVatValue ;

    /**
     * Код отправителя, для склада 6 всегда значение 737
     */
    @Column(name = "kpodot")
    private int senderCode;
    /**
     * Код получателя
     */
    @Column(name = "kpl")
    private int recipientCode;
    /**
     * Идентификатор получателя в базе
     */
    @Column(name = "klient_id")
    private int recipientId;

    /**
     * Идентификатор договора получателя в базе
     */
    @Column(name = "dogovor_id")
    private Integer recipientContractId = 0;

    /**
     * Сумма по документу без НДС
     */
    @Column(name = "summa")
    private double valueSumCost;
    /**
     * Сумма НДС
     */
    @Column(name = "summa_nds")
    private double valueSumVat;
    /**
     * Сумма по документу с НДС
     */
    @Column(name = "summa_all")
    private double valueSumCostAndVat;

    /**
     * Тип скидки
     */
    @Column(name = "skidka_tip")
    private int discountType;
    /**
     * Размер скидки
     */
    @Column(name = "skidka")
    private float discountValue;

    @Column(name="nosort")
    private boolean notVarietal ;

    /**
     * Количество полное
     */
    @Column(name = "kola")
    private int amountAll;
    /**
     * Количество упаковок
     */
    @Column(name = "kolk")
    private int amountPack;
    /**
     * Количество россыпью
     */
    @Column(name = "kolr")
    private int amountNotPack;


    public String getItemAmount() {
        return "Всего единиц: " + amountAll + " "
                + "В упаковках: " + (amountAll - amountNotPack) + " "
                + "Россыпью: " + amountNotPack + " " + "Упаковок: " + amountPack;
    }

    /**
     * Уценка 3 сорта, всегда 0
     */
    @Column(name = "ucenka3s")
    private float priceReduction3Grade;

    /**
     * Идентификатор принадлежности сертификата, 1 для РБ, 2 для России
     */
    @Column(name = "vid_sert")
    private int certificateId;

    /**
     * Идентификатор принадлежности сертификата государственной регистрации, 1 для РБ, 2 для России
     */
    @Column(name = "vid_ggr")
    private int certificateGGRId;

    /**
     * Идентификатор валюты формата March8
     * 1 - Бел. руб.
     * 2 - Росс.руб.
     * 3 - Долл. США
     * 4 - Евро
     * 5 - Гривна
     */
    @Column(name = "valuta_id")
    private int currencyId;

    /**
     * Фиксированнный курс Нац. банка к валюте currencyId на начало отчетного месяца
     */
    @Column(name = "kurs")
    private float currencyRateFixed;

    /**
     * Курс Нац. банка к валюте currencyId на момент отгрузки
     */
    @Column(name = "kurs_bank")
    private float currencyRateSale;


    /**
     * Тип торговой надбавки
     * 0 - нет торговой надбавки
     * 1 - фиксирована ставка для всего документа
     * 2 - из справочника торговых надбавок
     */
    @Column(name = "trade_mark_type")
    private int tradeMarkType;

    /**
     * Размер торговой надбавки
     * Имеет значение если тип торговой надбавки равен 1, иначе значение надбавки 0
     */
    @Column(name = "trade_mark_value")
    private float valueTradeMarkup;

    @Column(name = "calculation_factor")
    private float calculationFactor ;

    @Column(name="adjustment_type")
    private int adjustmentType ;

    @Column(name="adjustment_ndoc")
    private int adjustmentDocumentId;

    //--------------------------------------------------------------
    // Штампы создания и правки документа
    /**
     * Дата создания записи
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datevrkv")
    private Date createRecordDate;
    /**
     * Имя пользователя создавшего запись
     */
    @Column(name = "uservrkv")
    private String createRecordUser;
    /**
     * Код отдела создавшего запись
     */
    @Column(name = "kpodvrkv")
    private String createRecordCode;

    /**
     * Дата коректировки записи
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datekrkv")
    private Date editRecordDate;
    /**
     * Имя пользователя корректирующего запись
     */
    @Column(name = "userkrkv")
    private String editRecordUser;
    /**
     * Код отдела корректирующего запись
     */
    @Column(name = "kpodkrkv")
    private String editRecordCode;

    @Column(name = "give_item")
    private boolean giveTake ;

    @Column(name = "service")
    private int serviceType ;

    //--------------------------------------------------------------
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    //@OrderBy("item")
    private List<SaleDocumentItem> detailList = new ArrayList<SaleDocumentItem>();

    public SaleDocument() {
    }

    public SaleDocument(final SaleDocument document) {
        setDocumentDate(document.getDocumentDate());
        setDocumentNumber(document.getDocumentNumber());
        setDocumentStatus(document.getDocumentStatus());
        setDocumentType(document.getDocumentType());
        setDocumentExport(document.getDocumentExport());

        setPrepayment(document.isPrepayment());
        setDocumentVATType(document.getDocumentVATType());

        setSenderCode(document.getSenderCode());
        setRecipientCode(document.getRecipientCode());
        setRecipientId(document.getRecipientId());
        setRecipientContractId(document.getRecipientContractId());

        setValueSumCost(document.getValueSumCost());
        setValueSumVat(document.getValueSumVat());
        setValueSumCostAndVat(document.getValueSumCostAndVat());

        setDiscountType(document.getDiscountType());
        setDiscountValue(document.getDiscountValue());

        setAmountAll(document.getAmountAll());
        setAmountPack(document.getAmountPack());
        setAmountNotPack(document.getAmountNotPack());

        setPriceReduction3Grade(document.getPriceReduction3Grade());
        setCertificateId(document.getCertificateId());
        setCertificateGGRId(document.getCertificateGGRId());


        setCurrencyId(document.getCurrencyId());
        setCurrencyRateFixed(document.getCurrencyRateFixed());
        setCurrencyRateSale(document.getCurrencyRateSale());

        setCalculationFactor(document.getCalculationFactor());

        setCreateRecordCode(document.getCreateRecordCode());
        setCreateRecordDate(document.getCreateRecordDate());
        setCreateRecordUser(document.getCreateRecordUser());

        setEditRecordCode(document.getEditRecordCode());
        setEditRecordDate(document.getEditRecordDate());
        setEditRecordUser(document.getEditRecordUser());


        setDocumentSaleDate(document.getDocumentSaleDate());
        setTradeMarkType(document.getTradeMarkType());
        setValueTradeMarkup(document.getValueTradeMarkup());
        setDocumentVatValue(document.getDocumentVatValue());
        setPrepaymentDate(document.getPrepaymentDate());

        setNotVarietal(document.isNotVarietal());
    }

    public SaleDocument(final SaleDocumentBase document) {
        setDocumentDate(document.getDocumentDate());
        setDocumentNumber(document.getDocumentNumber());
        setDocumentStatus(document.getDocumentStatus());
        setDocumentType(document.getDocumentType());
        setDocumentExport(document.getDocumentExport());

        setPrepayment(document.isPrepayment());
        setDocumentVATType(document.getDocumentVATType());

        setSenderCode(document.getSenderCode());
        setRecipientCode(document.getRecipientCode());
        setRecipientId(document.getRecipientId());
        setRecipientContractId(document.getRecipientContractId());

        setValueSumCost(document.getValueSumCost());
        setValueSumVat(document.getValueSumVat());
        setValueSumCostAndVat(document.getValueSumCostAndVat());

        setDiscountType(document.getDiscountType());
        setDiscountValue(document.getDiscountValue());

        setAmountAll(document.getAmountAll());
        setAmountPack(document.getAmountPack());
        setAmountNotPack(document.getAmountNotPack());

        setPriceReduction3Grade(document.getPriceReduction3Grade());
        setCertificateId(document.getCertificateId());
        setCertificateGGRId(document.getCertificateGGRId());


        setCurrencyId(document.getCurrencyId());
        setCurrencyRateFixed(document.getCurrencyRateFixed());
        setCurrencyRateSale(document.getCurrencyRateSale());

        setCalculationFactor(document.getCalculationFactor());

        setCreateRecordCode(document.getCreateRecordCode());
        setCreateRecordDate(document.getCreateRecordDate());
        setCreateRecordUser(document.getCreateRecordUser());

        setEditRecordCode(document.getEditRecordCode());
        setEditRecordDate(document.getEditRecordDate());
        setEditRecordUser(document.getEditRecordUser());


        setId(document.getId());
        setDocumentSaleDate(document.getDocumentSaleDate());
        setTradeMarkType(document.getTradeMarkType());
        setValueTradeMarkup(document.getTradeMarkValue());
        setDocumentVatValue(document.getDocumentVatValue());
        setPrepaymentDate(document.getPrepaymentDate());


        setNotVarietal(document.isNotVarietal());

    }


    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(final Date documentDate) {
        this.documentDate = documentDate;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(final String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public int getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(final int documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getDocumentType() {
        if (documentDate != null) {
            return documentType.trim();
        } else {
            return "";
        }
    }

    public void setDocumentType(final String documentType) {
        this.documentType = documentType;
    }

    public int getDocumentExport() {
        return documentExport;
    }

    public void setDocumentExport(final int documentExport) {
        this.documentExport = documentExport;
    }

    public boolean isPrepayment() {
        return prepayment;
    }

    public void setPrepayment(final boolean prepayment) {
        this.prepayment = prepayment;
    }

    public int getDocumentVATType() {
        return documentVATType;
    }

    public void setDocumentVATType(final int docomentVATType) {
        this.documentVATType = docomentVATType;
    }

    public int getSenderCode() {
        return senderCode;
    }

    public void setSenderCode(final int senderCode) {
        this.senderCode = senderCode;
    }

    public int getRecipientCode() {
        return recipientCode;
    }

    public void setRecipientCode(final int recipientCode) {
        this.recipientCode = recipientCode;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(final int recipientId) {
        this.recipientId = recipientId;
    }

    public Integer getRecipientContractId() {
        return recipientContractId;
    }

    public void setRecipientContractId(final int recipientContractId) {
        this.recipientContractId = recipientContractId;
    }

    public double getValueSumCost() {
        return valueSumCost;
    }

    public void setValueSumCost(final double valueSum) {
        this.valueSumCost = valueSum;
    }

    public double getValueSumVat() {
        return valueSumVat;
    }

    public void setValueSumVat(final double valueVat) {
        this.valueSumVat = valueVat;
    }

    public double getValueSumCostAndVat() {
        return valueSumCostAndVat;
    }

    public void setValueSumCostAndVat(final double valueSumAndVat) {
        this.valueSumCostAndVat = valueSumAndVat;
    }

    public int getDiscountType() {
        return discountType;
    }

    public void setDiscountType(final int discountType) {
        this.discountType = discountType;
    }

    public float getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(final float discountValue) {
        this.discountValue = discountValue;
    }

    public int getAmountAll() {
        return amountAll;
    }

    public void setAmountAll(final int amountAll) {
        this.amountAll = amountAll;
    }

    public int getAmountPack() {
        return amountPack;
    }

    public void setAmountPack(final int amountPack) {
        this.amountPack = amountPack;
    }

    public int getAmountNotPack() {
        return amountNotPack;
    }

    public void setAmountNotPack(final int amountNotPack) {
        this.amountNotPack = amountNotPack;
    }

    public float getPriceReduction3Grade() {
        return priceReduction3Grade;
    }

    public void setPriceReduction3Grade(final float mark3Grade) {
        this.priceReduction3Grade = mark3Grade;
    }

    public int getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(final int certificateId) {
        this.certificateId = certificateId;
    }

    public int getCertificateGGRId() {
        return certificateGGRId;
    }

    public void setCertificateGGRId(final int certificateGGRId) {
        this.certificateGGRId = certificateGGRId;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(final int currencyId) {
        this.currencyId = currencyId;
    }

    public float getCurrencyRateFixed() {
        return currencyRateFixed;
    }

    public void setCurrencyRateFixed(final float currencyRateFixed) {
        this.currencyRateFixed = currencyRateFixed;
    }

    public float getCurrencyRateSale() {
        return currencyRateSale;
    }

    public void setCurrencyRateSale(final float currencyRateSale) {
        this.currencyRateSale = currencyRateSale;
    }

    public Date getCreateRecordDate() {
        return createRecordDate;
    }

    public void setCreateRecordDate(final Date createRecordDate) {
        this.createRecordDate = createRecordDate;
    }

    public String getCreateRecordUser() {
        return createRecordUser;
    }

    public void setCreateRecordUser(final String createRecordUser) {
        this.createRecordUser = createRecordUser;
    }

    public String getCreateRecordCode() {
        return createRecordCode;
    }

    public void setCreateRecordCode(final String createRecordCode) {
        this.createRecordCode = createRecordCode;
    }

    public Date getEditRecordDate() {
        return editRecordDate;
    }

    public void setEditRecordDate(final Date editRecordDate) {
        this.editRecordDate = editRecordDate;
    }

    public String getEditRecordUser() {
        return editRecordUser;
    }

    public void setEditRecordUser(final String editRecordUser) {
        this.editRecordUser = editRecordUser;
    }

    public String getEditRecordCode() {
        return editRecordCode;
    }

    public void setEditRecordCode(final String editRecordCode) {
        this.editRecordCode = editRecordCode;
    }

    public Date getDocumentSaleDate() {
        return documentSaleDate;
    }

    public void setDocumentSaleDate(final Date documentSaleDate) {
        this.documentSaleDate = documentSaleDate;
    }

    public int getTradeMarkType() {
        return tradeMarkType;
    }

    public void setTradeMarkType(final int tradeMarkType) {
        this.tradeMarkType = tradeMarkType;
    }

    public float getValueTradeMarkup() {
        return valueTradeMarkup;
    }

    public void setValueTradeMarkup(final float valueTradeMarkup) {
        this.valueTradeMarkup = valueTradeMarkup;
    }

    public float getDocumentVatValue() {
        return documentVatValue;
    }

    public void setDocumentVatValue(final float documentVatValue) {
        this.documentVatValue = documentVatValue;
    }

    public float getCalculationFactor() {
        return calculationFactor;
    }

    public void setCalculationFactor(final float calculationFactor) {
        this.calculationFactor = calculationFactor;
    }

    @Override
    public String toString() {
        return "SaleDocumentMarch{" +
                "id=" + id +
                ", documentDate=" + documentDate +
                ", documentNumber='" + documentNumber + '\'' +
                ", documentStatus=" + documentStatus +
                ", documentType='" + documentType + '\'' +
                ", documentExport=" + documentExport +
                ", prepayment=" + prepayment +
                ", documentVATType=" + documentVATType +
                ", senderCode=" + senderCode +
                ", recipientCode=" + recipientCode +
                ", recipientId=" + recipientId +
                ", recipientContractId=" + recipientContractId +
                ", valueSum=" + valueSumCost +
                ", valueVat=" + valueSumVat +
                ", valueSumAndVat=" + valueSumCostAndVat +
                ", discountType=" + discountType +
                ", discountValue=" + discountValue +
                ", amountAll=" + amountAll +
                ", amountPack=" + amountPack +
                ", amountNotPack=" + amountNotPack +
                ", markOwn3Grade=" + priceReduction3Grade +
                ", certificateId=" + certificateId +
                ", certificateGGRId=" + certificateGGRId +
                ", currencyId=" + currencyId +
                ", currencyRateFixed=" + currencyRateFixed +
                ", currencyRateSale=" + currencyRateSale +
                ", createRecordDate=" + createRecordDate +
                ", createRecordUser='" + createRecordUser + '\'' +
                ", createRecordCode='" + createRecordCode + '\'' +
                ", editRecordDate=" + editRecordDate +
                ", editRecordUser='" + editRecordUser + '\'' +
                ", editRecordCode='" + editRecordCode + '\'' +
                '}';
    }

    public boolean isNotVarietal() {
        return notVarietal;
    }

    public void setNotVarietal(final boolean notVarietal) {
        this.notVarietal = notVarietal;
    }

    public Date getPrepaymentDate() {
        return prepaymentDate;
    }

    public void setPrepaymentDate(final Date prepaymentDate) {
        this.prepaymentDate = prepaymentDate;
    }

    public List<SaleDocumentItem> getDetailList() {
        return detailList;
    }

    public void setDetailList(final List<SaleDocumentItem> detailList) {
        this.detailList = detailList;
    }

    /**
     * Выводит в консоль основные параметры документа
     *
     * @return строка
     */
    public String printDocumentInConsole() {
        return "|Дата накладной\t|Номер документа\t|Операция документа\t\t\t|Скидка\t|Сумма\t\t|Сумма НДС\t|Всего\t|\n|"
                + DateUtils.getNormalDateFormat(getDocumentDate()) + "\t\t|" + getDocumentNumber().trim() + "\t\t\t|" + getDocumentType().trim() +
                "\t\t|" + getDiscountValue() + "\t\t|" +
                String.format("%.2f", getValueSumCost()) + "\t|" +
                String.format("%.2f", getValueSumVat()) + "\t|" +
                String.format("%.2f", getValueSumCostAndVat()) + "|";
    }

    public int getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(int adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public int getAdjustmentDocumentId() {
        return adjustmentDocumentId;
    }

    public void setAdjustmentDocumentId(int adjustmentDocumentNumber) {
        this.adjustmentDocumentId = adjustmentDocumentNumber;
    }

    public boolean isGiveTake() {
        return giveTake;
    }

    public void setGiveTake(boolean giveTake) {
        this.giveTake = giveTake;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }
}

