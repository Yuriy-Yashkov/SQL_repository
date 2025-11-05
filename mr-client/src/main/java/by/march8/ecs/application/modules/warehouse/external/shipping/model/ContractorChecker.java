package by.march8.ecs.application.modules.warehouse.external.shipping.model;

/**
 * @author Andy 03.08.2016.
 */
public class ContractorChecker {
    private int contractorCode;
    private String name;
    private int legalAddressId;
    private int postAddressId;
    private int contractId;


    public int getContractorCode() {
        return contractorCode;
    }

    public void setContractorCode(final int contractorCode) {
        this.contractorCode = contractorCode;
    }

    public String getName() {
        if (name != null) {
            return name.trim();
        } else {
            return "";
        }
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getLegalAddressId() {
        return legalAddressId;
    }

    public void setLegalAddressId(final int legalAddressId) {
        this.legalAddressId = legalAddressId;
    }

    public int getPostAddressId() {
        return postAddressId;
    }

    public void setPostAddressId(final int postAddressId) {
        this.postAddressId = postAddressId;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(final int contractId) {
        this.contractId = contractId;
    }
}
