package dept.calculationprice.model;

/**
 * Реквизиты для группы цен. Строго для нескольких отчетов
 * @author dbozhkou on 23.09.2020 14:29
 */

public class Requisites {
    private int id;
    private String position;
    private String full_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosition() {
        if (position == null) position = "";
        return position;
    }

    public void setPosition(String position) {
        if (position == null) position = "";
        this.position = position;
    }

    public String getFull_name() {
        if (full_name == null) full_name = "";
        return full_name;
    }

    public void setFull_name(String full_name) {
        if (full_name == null) full_name = "";
        this.full_name = full_name;
    }
}
