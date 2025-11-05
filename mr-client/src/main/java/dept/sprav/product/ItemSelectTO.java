package dept.sprav.product;

/**
 * Класс модели данных выбора/поиска  
 *
 * @author lidashka
 */
class ItemSelectTO {
    private int id;
    private String num;
    /** Тип операции ТО
     *  0 - основной; 1 - не зависящий от роста; 2 - зависящий от роста  */
    private int tip;
    private String name;
    private double val1;
    private double val2;
    private String note;

    public ItemSelectTO() {
    }

    public ItemSelectTO(int id, String num, int tip, String name, double val1, double val2, String note) {
        this.id = id;
        this.num = num;
        this.tip = tip;
        this.name = name;
        this.val1 = val1;
        this.val2 = val2;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getTip() {
        return tip;
    }

    public void setTip(int tip) {
        this.tip = tip;
    }

    public double getVal1() {
        return val1;
    }

    public void setVal1(double val1) {
        this.val1 = val1;
    }

    public double getVal2() {
        return val2;
    }

    public void setVal2(double val2) {
        this.val2 = val2;
    }

}
