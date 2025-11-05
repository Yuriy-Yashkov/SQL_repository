package common;

public class Item {
    private int id;
    private String description;
    private String note;

    public Item(int id, String description, String note) {
        this.id = id;
        this.description = description;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public String getNote() {
        return note;
    }

    public String toString() {
        return getDescription();
    }
}
