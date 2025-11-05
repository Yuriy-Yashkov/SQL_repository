package by.march8.entities.warehouse;

/**
 * @author Andy 08.06.2016.
 */
public class SaleDocumentDrivingAdditional {

    private String loadingAddress;
    private String unloadingAddress;
    private String sealNumber;
    private String loadingDoer;
    private String loadingMethod ;

    private Integer valueVat ;

    public String getLoadingAddress() {
        return loadingAddress;
    }

    public void setLoadingAddress(final String loadingAddres) {
        this.loadingAddress = loadingAddres;
    }

    public String getUnloadingAddress() {
        return unloadingAddress;
    }

    public void setUnloadingAddress(final String unloadingAddress) {
        this.unloadingAddress = unloadingAddress;
    }

    public String getSealNumber() {
        return sealNumber;
    }

    public void setSealNumber(final String sealNumber) {
        this.sealNumber = sealNumber;
    }

    public String getLoadingDoer() {
        return loadingDoer;
    }

    public void setLoadingDoer(final String loadingDoer) {
        this.loadingDoer = loadingDoer;
    }

    public String getLoadingMethod() {
        return loadingMethod;
    }

    public void setLoadingMethod(final String loadingMethod) {
        this.loadingMethod = loadingMethod;
    }

    public Integer getValueVat() {
        return valueVat;
    }

    public void setValueVat(final Integer valueVat) {
        this.valueVat = valueVat;
    }
}
