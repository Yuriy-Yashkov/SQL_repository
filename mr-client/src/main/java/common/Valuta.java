package common;

/**
 *
 * @author vova
 *  30.01.2012
 */
@SuppressWarnings("unused")
public class Valuta {
    int id;
    String name;
    String full_name;
    String symbol;
    String about;

    @SuppressWarnings("all")
    public Valuta() {
        id = -1;
        name = "";
        full_name = "";
        symbol = "";
        about = "";
    }

    public Valuta(int id, String name, String full_name, String symbol, String about) {
        this.id = id;
        this.name = name;
        this.full_name = full_name;
        this.symbol = symbol;
        this.about = about;
    }

    @Override
    public String toString() {
        return name;
    }


    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


}
