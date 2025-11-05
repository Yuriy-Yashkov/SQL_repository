package dept.sbit.unloading;

/**
 * @author Andy 05.07.2017.
 */
public class NSIItem {
    private String tnvdCode;
    private double price;
    private double vat;
    private double weight;

    public String getTnvdCode() {
        return tnvdCode;
    }

    public void setTnvdCode(final String tnvdCode) {
        this.tnvdCode = tnvdCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(final double price) {
        this.price = price;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(final double vat) {
        this.vat = vat;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(final double weight) {
        this.weight = weight;
    }
}
