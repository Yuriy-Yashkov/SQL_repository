package dept.sklad.model;

public class ResultTTN {

    private int countItems;

    private Long summaRub;
    private Long summaRubNDS;
    private Long summaRubItogo;

    private Double summaValuta;
    private Double summaValutaNDS;
    private Double summaValutaItogo;

    private int valutaID;
    private String valutaName;

    public ResultTTN() {
        //setCountItems(countItems);

    }


    public ResultTTN(ResultTTN rttn) {
        this.setCountItems(rttn.getCountItems());

        this.setSummaRub(rttn.getSummaRub());
        this.setSummaRubNDS(rttn.getSummaRubNDS());
        this.setSummaRubItogo(rttn.getSummaRubItogo());

        this.setSummaValuta(rttn.getSummaValuta());
        this.setSummaValutaNDS(rttn.getSummaValutaNDS());
        this.setSummaValutaItogo(rttn.getSummaValutaItogo());

        this.setValutaID(rttn.getValutaID());
        this.setValutaName(rttn.getValutaName());

    }

    public int getCountItems() {
        return countItems;
    }

    public void setCountItems(int countItems) {
        this.countItems = countItems;
    }

    public Long getSummaRub() {
        return summaRub;
    }

    public void setSummaRub(Long summaRub) {
        this.summaRub = summaRub;
    }

    public Long getSummaRubNDS() {
        return summaRubNDS;
    }

    public void setSummaRubNDS(Long summaRubNDS) {
        this.summaRubNDS = summaRubNDS;
    }

    public Long getSummaRubItogo() {
        return summaRubItogo;
    }

    public void setSummaRubItogo(Long summaRubItogo) {
        this.summaRubItogo = summaRubItogo;
    }

    public Double getSummaValuta() {
        return summaValuta;
    }

    public void setSummaValuta(Double summaValuta) {
        this.summaValuta = summaValuta;
    }

    public Double getSummaValutaNDS() {
        return summaValutaNDS;
    }

    public void setSummaValutaNDS(Double summaValutaNDS) {
        this.summaValutaNDS = summaValutaNDS;
    }

    public Double getSummaValutaItogo() {
        return summaValutaItogo;
    }

    public void setSummaValutaItogo(Double summaValutaItogo) {
        this.summaValutaItogo = summaValutaItogo;
    }

    public int getValutaID() {
        return valutaID;
    }

    public void setValutaID(int valutaID) {
        this.valutaID = valutaID;
    }

    public String getValutaName() {
        return valutaName;
    }

    public void setValutaName(String valutaName) {
        this.valutaName = valutaName;
    }

}
