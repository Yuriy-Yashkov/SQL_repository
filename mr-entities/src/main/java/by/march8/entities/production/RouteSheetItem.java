package by.march8.entities.production;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lidashka on 16.10.2018.
 */

@NamedQueries({
        @NamedQuery(name = "RouteSheetItems.findByDate",
                query = "SELECT items FROM RouteSheetItem items  WHERE items.routingDate between :date_start and :date_end " +
                        " order by items.fas")
})

@Entity
@Table(name = "V_MARH_LIST_DETAIL")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class RouteSheetItem extends BaseEntity {

    @Id
    @Column(name = "ID")
    private int id;

    @Column(name = "ID_IZD")
    private int idIzd;

    @Column(name = "SAR")
    private int sar;

    @TableHeader(name = "Артикул", sequence = 2)
    @Column(name = "NAR")
    private String nar;

    @TableHeader(name = "Модель",  sequence = 0)
    @Column(name = "FAS")
    private int fas;

    @TableHeader(name = "Наименование",  sequence = 1)
    @Column(name = "NAIM")
    private String naim;

    @TableHeader(name = "Состав",  sequence = 13)
    @Column(name = "POLOTNO")
    private String polotno;

    @TableHeader(name = "Рост", sequence = 4)
    @Column(name = "RST_")
    private String rst;

    @TableHeader(name = "Размер", sequence = 5)
    @Column(name = "RZM_")
    private String rzm;

    @TableHeader(name = "Цена", sequence = 14)
    @Column(name = "CNO")
    private Double cno ;

    @Column(name = "ID_COLOR")
    private int idColor;

    @TableHeader(name = "Цвет",  sequence = 7)
    @Column(name = "COLOR")
    private String color;

    @TableHeader(name = "Кол м/л ", sequence = 8)
    @Column(name = "KOL_MARH")
    private Double kolMarh ;

    @TableHeader(name = "Кол-во",sequence = 9, footer = false)
    @Column(name = "KOL")
    private Double kol ;

    @Column(name = "ID_MARH")
    private int routingId ;

    @TableHeader(name = "№ МЛ",sequence = 10)
    @Column(name = "NOMER_MARH")
    private double routingNumber ;

    @Temporal(TemporalType.TIMESTAMP)
    @TableHeader(name = "Дата",sequence = 11)
    @Column(name = "DATE_MARH")
    private Date routingDate ;

    @TableHeader(name = "Бригада", sequence = 12)
    @Column(name = "BRIGADA")
    private int brigada;

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

    public int getFas() {
        return fas;
    }

    public void setFas(int fas) {
        this.fas = fas;
    }

    public String getNaim() {
        return naim;
    }

    public void setNaim(String naim) {
        this.naim = naim;
    }

    public String getPolotno() {
        return polotno;
    }

    public void setPolotno(String polotno) {
        this.polotno = polotno;
    }

    public String getRst() {
        return rst;
    }

    public void setRst(String rst) {
        this.rst = rst;
    }

    public String getRzm() {
        return rzm;
    }

    public void setRzm(String rzm) {
        this.rzm = rzm;
    }

    public Double getCno() {
        return cno;
    }

    public void setCno(Double cno) {
        this.cno = cno;
    }

    public int getIdColor() {
        return idColor;
    }

    public void setIdColor(int idColor) {
        this.idColor = idColor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getKolMarh() {
        return kolMarh;
    }

    public void setKolMarh(Double kolMarh) {
        this.kolMarh = kolMarh;
    }

    public Double getKol() {
        return kol;
    }

    public void setKol(Double kol) {
        this.kol = kol;
    }

    public double getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(double routingNumber) {
        this.routingNumber = routingNumber;
    }

    public Date getRoutingDate() {
        return routingDate;
    }

    public void setRoutingDate(Date routingDate) {
        this.routingDate = routingDate;
    }

    public int getBrigada() {
        return brigada;
    }

    public void setBrigada(int brigada) {
        this.brigada = brigada;
    }

    public int getRoutingId() {
        return routingId;
    }

    public void setRoutingId(int routingId) {
        this.routingId = routingId;
    }
}
