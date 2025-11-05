package by.march8.ecs.application.modules.warehouse.external.shipping.model;

import by.march8.entities.readonly.ContractorEntityView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy 22.01.2019 - 8:42.
 */
public class SaleDocumentSheet {
    private ContractorEntityView contractor;
    private List<List<SaleDocumentSet>> documentSet;

    public ContractorEntityView getContractor() {
        return contractor;
    }

    public void setContractor(ContractorEntityView contractor) {
        this.contractor = contractor;
    }

    public List<List<SaleDocumentSet>> getDocumentSet() {
        if (documentSet == null) {
            documentSet = new ArrayList<>();
        }
        return documentSet;
    }


    public void setDocumentSet(List<List<SaleDocumentSet>> documentSet) {
        this.documentSet = documentSet;
    }
}
