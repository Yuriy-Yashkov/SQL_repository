package dept.production.planning.ean;

import java.util.ArrayList;

/**
 *
 * @author lidashka
 */
public class EanList {
    private int idEanlist;
    private int eanNum;
    private String eanName;
    private String eanDate;
    private int idEmplVvod;
    private String vvodFio;
    private String dateVvod;
    private int idEmplIns;
    private String insFio;
    private String insDate;
    private String note;
    private int idStatus;
    private String status;
    private ArrayList<EanItem> eanItems;

    public EanList() {
    }

    public EanList(int idEanlist,
                   int eanNum,
                   String eanName,
                   String eanDate,
                   int idEmplVvod,
                   String vvodFio,
                   String dateVvod,
                   int idEmplIns,
                   String insFio,
                   String insDate,
                   String note,
                   int idStatus,
                   String status,
                   ArrayList<EanItem> eanItems) {
        this.idEanlist = idEanlist;
        this.eanNum = eanNum;
        this.eanName = eanName;
        this.eanDate = eanDate;
        this.idEmplVvod = idEmplVvod;
        this.vvodFio = vvodFio;
        this.dateVvod = dateVvod;
        this.idEmplIns = idEmplIns;
        this.insFio = insFio;
        this.insDate = insDate;
        this.note = note;
        this.idStatus = idStatus;
        this.status = status;
        this.eanItems = eanItems;
    }

    public String getDateVvod() {
        return dateVvod;
    }

    public void setDateVvod(String dateVvod) {
        this.dateVvod = dateVvod;
    }

    public String getEanDate() {
        return eanDate;
    }

    public void setEanDate(String eanDate) {
        this.eanDate = eanDate;
    }

    public String getEanName() {
        return eanName;
    }

    public void setEanName(String eanName) {
        this.eanName = eanName;
    }

    public int getEanNum() {
        return eanNum;
    }

    public void setEanNum(int eanNum) {
        this.eanNum = eanNum;
    }

    public int getIdEanlist() {
        return idEanlist;
    }

    public void setIdEanlist(int idEanlist) {
        this.idEanlist = idEanlist;
    }

    public int getIdEmplIns() {
        return idEmplIns;
    }

    public void setIdEmplIns(int idEmplIns) {
        this.idEmplIns = idEmplIns;
    }

    public int getIdEmplVvod() {
        return idEmplVvod;
    }

    public void setIdEmplVvod(int idEmplVvod) {
        this.idEmplVvod = idEmplVvod;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public String getInsDate() {
        return insDate;
    }

    public void setInsDate(String insDate) {
        this.insDate = insDate;
    }

    public String getInsFio() {
        return insFio;
    }

    public void setInsFio(String insFio) {
        this.insFio = insFio;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVvodFio() {
        return vvodFio;
    }

    public void setVvodFio(String vvodFio) {
        this.vvodFio = vvodFio;
    }

    public ArrayList<EanItem> getEanItems() {
        return eanItems;
    }

    public void setEanItems(ArrayList<EanItem> eanItem) {
        this.eanItems = eanItem;
    }
}
