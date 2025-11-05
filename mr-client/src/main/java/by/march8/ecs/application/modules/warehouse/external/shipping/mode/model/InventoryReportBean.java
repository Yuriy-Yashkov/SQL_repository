package by.march8.ecs.application.modules.warehouse.external.shipping.mode.model;

/**
 * @author Developer on 26.08.2020 9:52
 */
public class InventoryReportBean {
    private int id;
    private String eanCode;
    private String nar;
    private String ngpr;
    private int srt;
    private String rzmPrint;
    private double cena;
    private int kol;
    private int nds;
    private double summa;

    public InventoryReportBean(int id, String eanCode, int nds, String nar, String ngpr, int srt, String rzmPrint, double cena, int kol) {
        this.id = id;
        this.eanCode = eanCode;
        this.nds = nds;
        this.nar = nar;
        this.ngpr = ngpr;
        this.srt = srt;
        this.rzmPrint = rzmPrint;
        this.cena = cena;
        this.kol = kol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEanCode() {
        return eanCode;
    }

    public void setEanCode(String eanCode) {
        this.eanCode = eanCode;
    }

    public String getNar() {
        return nar;
    }

    public void setNar(String nar) {
        this.nar = nar;
    }

    public String getNgpr() {
        return ngpr;
    }

    public void setNgpr(String ngpr) {
        this.ngpr = ngpr;
    }

    public int getSrt() {
        return srt;
    }

    public void setSrt(int srt) {
        this.srt = srt;
    }

    public String getRzmPrint() {
        return rzmPrint;
    }

    public void setRzmPrint(String rzmPrint) {
        this.rzmPrint = rzmPrint;
    }

    public void setCena(double nadb) {
        this.cena = cena;
    }

    public double getCena(double nadb) {
        System.out.println(cena + " " + getNds() + " " + nadb);
        double temp = (double) getNds() / 100 + 1;
        double temp2 = nadb / 100 + 1;
        System.out.println(temp + " " + temp2);
        return round(cena * temp * temp2);

    }

    private double round(double d) {
        return Math.round(d * 100) / 100.0;
    }

    public int getKol() {
        return kol;
    }

    public void setKol(int kol) {
        this.kol = kol;
    }

    public double getSum(double nadb) {
        return kol * getCena(nadb);
    }

    @Override
    public String toString() {
        return "InventoryReportBean{" +
                "id=" + id +
                ", eanCode='" + eanCode + '\'' +
                ", nar='" + nar + '\'' +
                ", ngpr='" + ngpr + '\'' +
                ", srt=" + srt +
                ", rzmPrint='" + rzmPrint + '\'' +
                ", cena=" + cena +
                ", kol=" + kol +
                '}';
    }


    public int getNds() {
        return nds;
    }

    public void setNds(int nds) {
        this.nds = nds;
    }
}
