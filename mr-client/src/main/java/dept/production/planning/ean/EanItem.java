package dept.production.planning.ean;

import java.util.ArrayList;


/**
 * Модель данных одной записи в заявке
 *
 * @author lidashka
 */
public class EanItem {
    private int idFlag;
    private int id;
    private int fasNum;
    private String fasName;
    private int fasVid;
    private int fasSrt;
    private int kolX;
    private Integer fasSar;
    private String fasNar;
    private String noteText;
    private String upacText;
    private int textGpcSeg;
    private int textGpcSem;
    private int textGpcKl;
    private int textGpcBr;
    private String textOKRB;
    private String textTHB;
    private String textGOST;
    private int colorNum;
    private String colorName;
    private ArrayList<EanItemListSize> dataSize;

    public EanItem() {
    }

    public EanItem(int idFlag,
                   int id,
                   int fasNum,
                   String fasName,
                   int fasVid,
                   int fasSrt,
                   int kolX,
                   String fasNar,
                   String noteText,
                   String upacText,
                   int textGpcSeg,
                   int textGpcSem,
                   int textGpcKl,
                   int textGpcBr,
                   String textOKRB,
                   String textTHB,
                   String textGOST,
                   int colorNum,
                   String colorName,
                   ArrayList<EanItemListSize> dataSize) {

        this.idFlag = idFlag;
        this.id = id;
        this.fasNum = fasNum;
        this.fasName = fasName;
        this.fasVid = fasVid;
        this.fasSrt = fasSrt;
        this.kolX = kolX;
        this.fasNar = fasNar;
        this.noteText = noteText;
        this.upacText = upacText;
        this.textGpcSeg = textGpcSeg;
        this.textGpcSem = textGpcSem;
        this.textGpcKl = textGpcKl;
        this.textGpcBr = textGpcBr;
        this.textOKRB = textOKRB;
        this.textTHB = textTHB;
        this.textGOST = textGOST;
        this.colorNum = colorNum;
        this.colorName = colorName;
        this.dataSize = dataSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFlag() {
        return idFlag;
    }

    public ArrayList<EanItemListSize> getDataSize() {
        return dataSize;
    }

    public void setDataSize(ArrayList<EanItemListSize> dataSize) {
        this.dataSize = dataSize;
    }

    public String getFasName() {
        return fasName;
    }

    public void setFasName(String fasName) {
        this.fasName = fasName;
    }

    public Integer getFasSar() {
        return fasSar;
    }

    public void setFasSar(Integer fasSar) {
        this.fasSar = fasSar;
    }

    public int getFasNum() {
        return fasNum;
    }

    public void setFasNum(int fasNum) {
        this.fasNum = fasNum;
    }

    public int getFasSrt() {
        return fasSrt;
    }

    public void setFasSrt(int fasSrt) {
        this.fasSrt = fasSrt;
    }

    public int getFasVid() {
        return fasVid;
    }

    public void setFasVid(int fasVid) {
        this.fasVid = fasVid;
    }

    public int getKolX() {
        return kolX;
    }

    public void setKolX(int kolX) {
        this.kolX = kolX;
    }

    public String getFasNar() {
        return fasNar;
    }

    public void setFasNar(String narText) {
        this.fasNar = narText;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getUpacText() {
        return upacText;
    }

    public void setUpacText(String upacText) {
        this.upacText = upacText;
    }

    public String getTextGOST() {
        return textGOST;
    }

    public void setTextGOST(String textGOST) {
        this.textGOST = textGOST;
    }

    public int getTextGpcBr() {
        return textGpcBr;
    }

    public void setTextGpcBr(int textGpcBr) {
        this.textGpcBr = textGpcBr;
    }

    public int getTextGpcKl() {
        return textGpcKl;
    }

    public void setTextGpcKl(int textGpcKl) {
        this.textGpcKl = textGpcKl;
    }

    public int getTextGpcSeg() {
        return textGpcSeg;
    }

    public void setTextGpcSeg(int textGpcSeg) {
        this.textGpcSeg = textGpcSeg;
    }

    public int getTextGpcSem() {
        return textGpcSem;
    }

    public void setTextGpcSem(int textGpcSem) {
        this.textGpcSem = textGpcSem;
    }

    public String getTextOKRB() {
        return textOKRB;
    }

    public void setTextOKRB(String textOKRB) {
        this.textOKRB = textOKRB;
    }

    public String getTextTHB() {
        return textTHB;
    }

    public void setTextTHB(String textTHB) {
        this.textTHB = textTHB;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public int getColorNum() {
        return colorNum;
    }

    public void setColorNum(int colorNum) {
        this.colorNum = colorNum;
    }

    public void setIdFlag(int idFlag) {
        this.idFlag = idFlag;
    }
}
