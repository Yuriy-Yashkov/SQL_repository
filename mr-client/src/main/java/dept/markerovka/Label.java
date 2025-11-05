package dept.markerovka;

/**
 *
 * @author vova
 * @date 20.03.2012
 */
public class Label {

    long barcode;
    int lng_id;
    int item_id;
    int idLabelPath;

    public Label(long barcode, int lng_id, int item_id) {
        this.barcode = barcode;
        this.lng_id = lng_id;
        this.item_id = item_id;
    }

    public long getBarcode() {
        return barcode;
    }

    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getLng_id() {
        return lng_id;
    }

    public void setLng_id(int lng_id) {
        this.lng_id = lng_id;
    }

    public int getIdLabelPath() {
        return idLabelPath;
    }

    public void setIdLabelPath(int idLabelPath) {
        this.idLabelPath = idLabelPath;
    }
}
