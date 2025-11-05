package dept.sklad.model;

/** Модель договора контрагента*/
public class Dogovor {
    private int dogovorId;
    private String dogovorDescr;

    public Dogovor(Dogovor c) {
        this.setDogovorId(c.getDogovorId());
        this.setDogovorDescr(c.getDogovorDescr());
    }

    public Dogovor(int id, String descr) {
        this.setDogovorId(id);
        this.setDogovorDescr(descr);
    }

    public int getDogovorId() {
        return dogovorId;
    }

    public void setDogovorId(int dogovorId) {
        this.dogovorId = dogovorId;
    }

    public String getDogovorDescr() {
        return dogovorDescr;
    }

    public void setDogovorDescr(String dogovorDescr) {
        this.dogovorDescr = dogovorDescr;
    }

}
