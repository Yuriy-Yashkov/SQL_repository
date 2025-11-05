package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.utils.DateUtils;
import by.march8.api.MarchDataSourceEnum;
import by.march8.entities.sales.PreOrderSaleDocument;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import javax.persistence.*;
import java.util.Date;

@NamedQueries({
        @NamedQuery(name = "SaleDocumentBase.findByPeriodAndContractorCode",
                query = "SELECT item FROM SaleDocumentBase item WHERE item.documentDate BETWEEN :periodBegin AND :periodEnd " +
                        "AND item.recipientCode = :contractor AND item.documentStatus = 0 AND item.documentType='Отгрузка покупателю' " +
                        "ORDER BY item.documentDate")
})

/**
 * Описание таблицы otgruz1 программы March8 упрощенный, без спецификации
 * Created by Andy on 01.02.2016.
 */
@Entity
@Table(name = "otgruz1")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@NoArgsConstructor
public class SaleDocumentBase extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    /**
     * Дата создания документа
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    protected Date documentDate;
    /**
     * Номер документа
     */
    @Column(name = "ndoc")
    @Getter(AccessLevel.NONE)
    protected String documentNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sale_date")
    protected Date documentSaleDate;

    /**
     * Статус документа
     * 0 - закрыт
     * 1 - удален
     * 2 - неизвесно
     * 3 - формируется(только чтение)
     * 8 - формируется(запись)
     */
    @Column(name = "status")
    protected int documentStatus;

    /**
     * Тип документа:
     * Отгрузка покупателю
     * Перемещение в розницу
     * Возврат от покупателя
     * Возврат из розницы
     */
    @Column(name = "operac")
    @Getter(AccessLevel.NONE)
    protected String documentType;

    /**
     * Признак экспортной накладной
     * 0 - нет
     * 1 - да
     */
    @Column(name = "export")
    protected int documentExport;

    /**
     * Признак документа по предоплате
     */
    @Column(name = "prepayment")
    protected boolean prepayment;

    /**
     * Дата предоплаты по документу
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "prepayment_date")
    protected Date prepaymentDate;

    /**
     * Старая дебильная система, баба ЯгА против. Долбаная совместимость...
     * Признак начисления НДС по накладной
     * 1 - НДС сверху
     * 2 - без НДС
     */
    @Column(name = "nalogi")
    protected int documentVATType;

    @Column(name = "vat_value")
    protected float documentVatValue;

    /**
     * Код отправителя, для склада 6 всегда значение 737
     */
    @Column(name = "kpodot")
    protected int senderCode;
    /**
     * Код получателя
     */
    @Column(name = "kpl")
    protected int recipientCode;
    /**
     * Идентификатор получателя в базе
     */
    @Column(name = "klient_id")
    protected int recipientId;

    /**
     * Идентификатор договора получателя в базе
     */
    @Column(name = "dogovor_id")
    protected Integer recipientContractId = 0;

    /**
     * Сумма по документу без НДС
     */
    @Column(name = "summa")
    protected double valueSumCost;
    /**
     * Сумма НДС
     */
    @Column(name = "summa_nds")
    protected double valueSumVat;
    /**
     * Сумма по документу с НДС
     */
    @Column(name = "summa_all")
    protected double valueSumCostAndVat;

    /**
     * Тип скидки
     */
    @Column(name = "skidka_tip")
    protected int discountType;
    /**
     * Размер скидки
     */
    @Column(name = "skidka")
    protected float discountValue;

    /**
     * Количество полное
     */
    @Column(name = "kola")
    protected int amountAll;
    /**
     * Количество упаковок
     */
    @Column(name = "kolk")
    protected int amountPack;
    /**
     * Количество россыпью
     */
    @Column(name = "kolr")
    protected int amountNotPack;


    /**
     * Уценка 3 сорта, всегда 0
     */
    @Column(name = "ucenka3s")
    protected float priceReduction3Grade;

    /**
     * Идентификатор принадлежности сертификата, 1 для РБ, 2 для России
     */
    @Column(name = "vid_sert")
    protected int certificateId;

    /**
     * Идентификатор принадлежности сертификата государственной регистрации, 1 для РБ, 2 для России
     */
    @Column(name = "vid_ggr")
    protected int certificateGGRId;

    /**
     * Идентификатор валюты формата March8
     * 1 - Бел. руб.
     * 2 - Росс.руб.
     * 3 - Долл. США
     * 4 - Евро
     * 5 - Гривна
     */
    @Column(name = "valuta_id")
    protected int currencyId;

    /**
     * Фиксированнный курс Нац. банка к валюте currencyId на начало отчетного месяца
     */
    @Column(name = "kurs")
    protected float currencyRateFixed;

    /**
     * Курс Нац. банка к валюте currencyId на момент отгрузки
     */
    @Column(name = "kurs_bank")
    protected float currencyRateSale;


    /**
     * Тип торговой надбавки
     * 0 - нет торговой надбавки
     * 1 - фиксирована ставка для всего документа
     * 2 - из справочника торговых надбавок
     */
    @Column(name = "trade_mark_type")
    protected int tradeMarkType;

    /**
     * Размер торговой надбавки
     * Имеет значение если тип торговой надбавки равен 1, иначе значение надбавки 0
     */
    @Column(name = "trade_mark_value")
    protected float tradeMarkValue;

    @Column(name = "calculation_factor")
    protected float calculationFactor;


    //--------------------------------------------------------------
    // Штампы создания и правки документа
    /**
     * Дата создания записи
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datevrkv")
    protected Date createRecordDate;
    /**
     * Имя пользователя создавшего запись
     */
    @Column(name = "uservrkv")
    protected String createRecordUser;
    /**
     * Код отдела создавшего запись
     */
    @Column(name = "kpodvrkv")
    protected String createRecordCode;

    /**
     * Дата коректировки записи
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datekrkv")
    protected Date editRecordDate;
    /**
     * Имя пользователя корректирующего запись
     */
    @Column(name = "userkrkv")
    protected String editRecordUser;
    /**
     * Код отдела корректирующего запись
     */
    @Column(name = "kpodkrkv")
    protected String editRecordCode;

    @Getter(AccessLevel.NONE)
    @Column(name="saved")
    protected Boolean check = false;

    @Column(name = "nosort")
    private boolean notVarietal ;

    @Column(name = "give_item")
    private boolean giveTake ;

    @Column(name = "service")
    private int serviceType ;

    @Column(name = "source_doc_number")
    private String sourceDocNumber ;
    @Column(name = "source_doc_date")
    private Date sourceDocDate ;

    @Column(name = "refund_doc_number")
    private String refundDocNumber ;
    @Column(name = "refund_doc_date")
    private Date refundDocDate ;

    @Column(name="adjustment_type")
    private int adjustmentType ;

    @Column(name="adjustment_ndoc")
    private int adjustmentDocumentId;

    @Column(name = "origin_currency_date")
    private Date originCurrencyDate;

    @Column(name = "origin_currency_type")
    private String originCurrencyType;

    @Transient
    private boolean oldMoney ;

    @Transient
    private boolean asService ;

    public SaleDocumentBase(final SaleDocumentBase document) {
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



        setCreateRecordCode(document.getCreateRecordCode());
        setCreateRecordDate(document.getCreateRecordDate());
        setCreateRecordUser(document.getCreateRecordUser());

        setEditRecordCode(document.getEditRecordCode());
        setEditRecordDate(document.getEditRecordDate());
        setEditRecordUser(document.getEditRecordUser());

        setNotVarietal(document.isNotVarietal());
        setOriginCurrencyDate(document.getOriginCurrencyDate());
        setOriginCurrencyType(document.getOriginCurrencyType());
    }

    public SaleDocumentBase(PreOrderSaleDocument other) {
        this.id = other.getId();
        this.documentDate = other.getDocumentDate();
        this.documentNumber = other.getDocumentNumber();
        //this.documentType = other.documentType;
        this.documentStatus = other.getStatus();
        this.currencyId = other.getCurrencyId();
        this.currencyRateFixed = (float)other.getCurrencyRateCommon();
        this.currencyRateSale = (float) other.getCurrencyRateAddition();
        //this.cu = other.currencyRateCommonDate;
        this.documentSaleDate = other.getCurrencyRateAdditionDate();
        this.documentVATType = other.getVatType();
        this.documentVatValue = (float)other.getVatValue();
        this.discountType = other.getDiscountType();
        this.discountValue = (float)other.getDiscountValue();
        this.tradeMarkType = other.getTradeAllowanceType();
        this.tradeMarkValue = (float)other.getTradeAllowanceValue();
        this.notVarietal  = other.getGradeMarkdownType()>0;
        this.priceReduction3Grade = (float)other.getGradeMarkdownValue();

        this.recipientId = other.getContractorId();
        this.recipientContractId = other.getContractId();

        this.amountAll = (int)other.getAmount();

        if(currencyId>1) {
            this.valueSumCost = other.getSumPriceCurrencyValue();
            this.valueSumVat = other.getSumVatCurrencyValue();
            this.valueSumCostAndVat = other.getSumPriceVatCurrencyValue();
        }else {
            this.valueSumCost = other.getSumPriceValue();
            this.valueSumVat = other.getSumVatValue();
            this.valueSumCostAndVat = other.getSumPriceVatValue();
        }
    }

    public String getDocumentNumber() {
        if (documentNumber != null) {
            return documentNumber.trim();
        } else {
            return "";
        }
    }

    public String getDocumentType() {
        if (documentDate != null) {
            return documentType.trim();
        } else {
            return "";
        }
    }

    @Override
    public String toString() {
        return "SaleDocumentBase{" +
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
                ", originCurrencyDate='" + originCurrencyDate + '\'' +
                ", originCurrencyType='" + originCurrencyType + '\'' +
                '}';
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

    public String getItemAmount(){
        if (documentType!=null){
            if(documentType.trim().equals(InvoiceType.DOCUMENT_SALE_MATERIAL)||documentType.trim().equals(InvoiceType.DOCUMENT_REFUND_MATERIAL)){
                double weight = (double)amountAll / 100 ;
                return "Вес материала, всего: "+weight+" кг." ;
            }else{
                return "Всего единиц: " + amountAll + " "
                        + "В упаковках: " + (amountAll - amountNotPack) + " "
                        + "Россыпью: " + amountNotPack + " " + "Упаковок: " + amountPack;
            }
        }
        return "Нет данных";
    }

    public Boolean getCheck() {
        if(check==null){
            return false ;
        }
        return check;
    }
}

