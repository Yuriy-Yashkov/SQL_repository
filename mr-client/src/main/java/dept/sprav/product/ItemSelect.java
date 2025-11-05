package dept.sprav.product;

/**
 * Класс модели данных выбора/поиска  
 *
 * @author lidashka
 */
class ItemSelect {
    private int id;
    private String name;
    private String note;

    public ItemSelect() {
    }

    public ItemSelect(int id, String item, String note) {
        this.id = id;
        this.name = item;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String item) {
        this.name = item;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
