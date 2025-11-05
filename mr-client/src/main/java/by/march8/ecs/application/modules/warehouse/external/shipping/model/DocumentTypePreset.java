package by.march8.ecs.application.modules.warehouse.external.shipping.model;

/**
 * Класс описывает предустановки для типов документов,
 * которые будут формироваться при обработке оператором
 *
 * @author Andy 25.01.2016.
 */
public class DocumentTypePreset {

    /**
     * Тип накладной
     * 0 - накладная не формируется
     * 1 - тип ТТН-1
     * 2 - тип ТН-2
     * 3 - Корректировочный акт
     */
    private int invoiceType;

    /**
     * Формировать приложение к накладной
     */
    private boolean annexToInvoice;

    private boolean annexToInvoiceVat;
    /**
     * Формировать справки к накладной
     */
    private boolean referenceToInvoice;

    /**
     * Формировать протокол согласования цен
     */
    private boolean priceAgreement;

    private PriceAgreementProtocol protocol;

    private int contractorCode;

    private PriceAgreementProtocol protocolReport;

    private boolean annexSimple;

    private boolean asService;

    private boolean invoiceTTN;

    private int serviceType;


    /**
     * Предустановка для формировангия документов
     *
     * @param invoiceType        тип документа строгой отчетности
     * @param annexToInvoice     флаг формирования приложения к накладной
     * @param referenceToInvoice флаг формирования справок
     * @param priceAgreementType флаг формирования протокола согласования цен
     * @param contractorCode     код контрагента(для предустановки протокола согласования цен)
     */
    public DocumentTypePreset(final int invoiceType, final boolean annexToInvoice, final boolean referenceToInvoice, final boolean priceAgreementType, final int contractorCode) {
        this.invoiceType = invoiceType;
        this.annexToInvoice = annexToInvoice;
        this.referenceToInvoice = referenceToInvoice;
        this.priceAgreement = priceAgreementType;
        this.contractorCode = contractorCode;
    }

    public int getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(final int invoiceType) {
        this.invoiceType = invoiceType;
    }

    public boolean isAnnexToInvoice() {
        return annexToInvoice;
    }

    public void setAnnexToInvoice(final boolean annexToInvoice) {
        this.annexToInvoice = annexToInvoice;
    }


    public int getContractorCode() {
        return contractorCode;
    }

    public void setContractorCode(final int contractorCode) {
        this.contractorCode = contractorCode;
    }


    public boolean isPriceAgreement() {
        return priceAgreement;
    }

    public void setPriceAgreement(final boolean priceAgreement) {
        this.priceAgreement = priceAgreement;
    }


    public PriceAgreementProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(final PriceAgreementProtocol protocol) {
        this.protocol = protocol;
    }

    public boolean isReferenceToInvoice() {
        return referenceToInvoice;
    }

    public void setReferenceToInvoice(final boolean referenceToInvoice) {
        this.referenceToInvoice = referenceToInvoice;
    }

    public PriceAgreementProtocol getProtocolReport() {
        return protocolReport;
    }

    public void setProtocolReport(final PriceAgreementProtocol protocolReport) {
        this.protocolReport = protocolReport;
    }

    public boolean isAnnexSimple() {
        return annexSimple;
    }

    public void setAnnexSimple(final boolean annexSimple) {
        this.annexSimple = annexSimple;
    }

    public boolean isAnnexToInvoiceVat() {
        return annexToInvoiceVat;
    }

    public void setAnnexToInvoiceVat(final boolean annexToInvoiceVat) {
        this.annexToInvoiceVat = annexToInvoiceVat;
    }

    public boolean isAsService() {
        return asService;
    }

    public void setAsService(final boolean asService) {
        this.asService = asService;
    }

    public boolean isInvoiceTTN() {
        return invoiceTTN;
    }

    public void setInvoiceTTN(final boolean invoiceTTN) {
        this.invoiceTTN = invoiceTTN;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }
}
