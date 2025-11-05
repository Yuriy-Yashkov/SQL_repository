package dept.production.planning.ean;

/**
 *
 * @author lidashka
 */
public class EanItemListSize {
    private int id;
    private int idEanItem;
    private int rst;
    private int rzm;
    private String weight;

    private String sizePrint;
    private String ean13;
    private boolean eanImport;

    public EanItemListSize() {
    }

    public EanItemListSize(int id, int idEanItem, int rst, int rzm, String sizePrint, String ean13, boolean eanImport) {
        this.id = id;
        this.idEanItem = idEanItem;
        this.rst = rst;
        this.rzm = rzm;
        this.sizePrint = sizePrint;
        this.ean13 = ean13;
        this.eanImport = eanImport;
    }

    public String getEan13() {
        return ean13;
    }

    public void setEan13(String ean13) {
        this.ean13 = ean13;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEanItem() {
        return idEanItem;
    }

    public void setIdEanItem(int idEanItem) {
        this.idEanItem = idEanItem;
    }

    public int getRst() {
        return rst;
    }

    public void setRst(int rst) {
        this.rst = rst;
    }

    public int getRzm() {
        return rzm;
    }

    public void setRzm(int rzm) {
        this.rzm = rzm;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSizePrint() {
        return sizePrint;
    }

    public void setSizePrint(String sizePrint) {
        this.sizePrint = sizePrint;
    }

    public boolean isEanImport() {
        return eanImport;
    }

    public void setEanImport(boolean eanImport) {
        this.eanImport = eanImport;
    }
}
