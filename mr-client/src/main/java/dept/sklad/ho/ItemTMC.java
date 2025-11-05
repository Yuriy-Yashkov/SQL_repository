package dept.sklad.ho;

/**
 *
 * @author lidashka
 */
public class ItemTMC {
    private int id;
    private String name;
    private String nar;
    private String vid;
    private String izm;
    private Double kolvo;
    private int part;

    public ItemTMC() {
    }

    public ItemTMC(int id, String name, String nar, String vid, String izm, Double kolvo, int part) {
        this.id = id;
        this.name = name;
        this.nar = nar;
        this.vid = vid;
        this.izm = izm;
        this.kolvo = kolvo;
        this.part = part;
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

    public String getNar() {
        return nar;
    }

    public void setNar(String nar) {
        this.nar = nar;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getIzm() {
        return izm;
    }

    public void setIzm(String izm) {
        this.izm = izm;
    }

    public Double getKolvo() {
        return kolvo;
    }

    public void setKolvo(Double kolvo) {
        this.kolvo = kolvo;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

}
