package by.march8.entities.production;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * Created by lidashka on 19.12.2018.
 */

@NamedQueries({
        @NamedQuery(name = "EanCodeByColorsView.findAll",
                query = "SELECT items FROM EanCodeByColorsView items "),

        @NamedQuery(name = "EanCodeByColorsView.findByModel",
                query = "SELECT items FROM EanCodeByColorsView items WHERE items.fas = :model "),

        @NamedQuery(name = "EanCodeByColorsView.findByFasAndNarAndEan",
                query = "SELECT items FROM EanCodeByColorsView items " +
                        "   WHERE   items.fas like :model and " +
                        "           items.nar like :article and " +
                        "           items.eanWithColor like :eancode  ")
})

@Entity
@Table(name = "V_ITEM_BY_EAN_CODE_AND_COLOR")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class EanCodeByColorsView extends BaseEntity {

    @Id
    @Column(name = "ID")
    private int id;

    @Column(name = "ID_IZD")
    private int idIzd;

    @TableHeader(name = "Шифр.артикул", sequence = 0, width = -90)
    @Column(name = "ARTICLE_CODE")
    private int sar;

    @TableHeader(name = "Артикул", sequence = 1, width = -90)
    @Column(name = "ARTICLE_NUMBER")
    private String nar;

    @TableHeader(name = "Модель", sequence = 3, width = -90)
    @Column(name = "MODEL_NUMBER")
    private String fas;

    @TableHeader(name = "Наименование", sequence = 2)
    @Column(name = "ITEM_NAME")
    private String naim;

    @Column(name = "GROWTH")
    private int growth;

    @Column(name = "[SIZE]")
    private int size;

    @TableHeader(name = "Размер", sequence = 4)
    @Column(name = "SIZE_PRINT")
    private String  sizePrint;

    @TableHeader(name = "Сорт", sequence = 5, width = -40)
    @Column(name = "GRADE")
    private int srt;

    @TableHeader(name = "Цвет", sequence = 6)
    @Column(name = "COLOR")
    private String color;

    @TableHeader(name = "EAN", sequence = 7)
    @Column(name = "EANCODE_WITH_COLOR")
    private String eanWithColor;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getIdIzd() {
        return idIzd;
    }

    public void setIdIzd(int idIzd) {
        this.idIzd = idIzd;
    }

    public int getSar() {
        return sar;
    }

    public void setSar(int sar) {
        this.sar = sar;
    }

    public String getNar() {
        return nar;
    }

    public void setNar(String nar) {
        this.nar = nar;
    }

    public String getFas() {
        return fas;
    }

    public void setFas(String fas) {
        this.fas = fas;
    }

    public String getNaim() {
        return naim;
    }

    public void setNaim(String naim) {
        this.naim = naim;
    }

    public int getGrowth() {
        return growth;
    }

    public void setGrowth(int growth) {
        this.growth = growth;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSizePrint() {
        return sizePrint;
    }

    public void setSizePrint(String sizePrint) {
        this.sizePrint = sizePrint;
    }

    public int getSrt() {
        return srt;
    }

    public void setSrt(int srt) {
        this.srt = srt;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEanWithColor() {
        return eanWithColor;
    }

    public void setEanWithColor( String eanWithColor) {
        this.eanWithColor = eanWithColor;
    }
}
