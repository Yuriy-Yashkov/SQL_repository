package by.march8.ecs.application.modules.warehouse.external.shipping.model;

import by.march8.api.BaseEntity;
import by.march8.ecs.application.modules.warehouse.external.shipping.reports.AbstractInvoiceReport;

/**
 * Структура селектора ПРОТОКОЛ СОГЛАСОВАНИЯ ЦЕН
 * @author Andy 23.05.2016.
 */
public class PriceAgreementProtocol extends BaseEntity {
    /**
     * Идентификатор
     */
    private int id;
    /**
     * КОд контрагента, для предварительной подстановки контрагенту
     */
    private int contractorCode[];
    /**
     * Наименование протокола,отображается для комбика
     */
    private String protocolName;
    /**
     * Альтернативное наименование протокола, внутреннее, пока не задействовано
     */
    private String protocolAlternateName;
    /**
     * Имя файла шаблона
     */
    private String templateName;

    private Class<? extends AbstractInvoiceReport> protocolClass = null;


    public PriceAgreementProtocol(final int id, final int[] contractorCode, final String protocolName, final String templateName, final Class<? extends AbstractInvoiceReport> protocolClass) {
        this.id = id;
        this.contractorCode = contractorCode;
        this.protocolName = protocolName;
        this.templateName = templateName;
        this.protocolAlternateName = protocolName;
        this.protocolClass = protocolClass;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public int[] getContractorCode() {
        return contractorCode;
    }

    public void setContractorCode(final int[] contractorCode) {
        this.contractorCode = contractorCode;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(final String protocolName) {
        this.protocolName = protocolName;
    }

    public String getProtocolAlternateName() {
        return protocolAlternateName;
    }

    public void setProtocolAlternateName(final String protocolAlternateName) {
        this.protocolAlternateName = protocolAlternateName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(final String templateName) {
        this.templateName = templateName;
    }

    @Override
    public String toString() {
        return protocolName;
    }

    public Class<? extends AbstractInvoiceReport> getProtocolClass() {
        return protocolClass;
    }

    public void setProtocolClass(final Class<? extends AbstractInvoiceReport> protocolClass) {
        this.protocolClass = protocolClass;
    }
}
