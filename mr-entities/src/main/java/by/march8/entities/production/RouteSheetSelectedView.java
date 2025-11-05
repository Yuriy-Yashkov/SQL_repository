package by.march8.entities.production;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lidashka on 22.10.2018.
 */

@NamedQueries({
        @NamedQuery(name = "RouteSheetSelectedView.findAll",
                query = "SELECT items FROM RouteSheetSelectedView items " +
                        " order by items.routingNumber desc "),

        @NamedQuery(name = "RouteSheetSelectedView.findByDate",
        query = "SELECT items FROM RouteSheetSelectedView items WHERE items.routingDate between :date_start and :date_end " +
                " order by items.routingNumber desc ")
})

@Entity
@Table(name = "V_MARH_LIST")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class RouteSheetSelectedView extends BaseEntity {

    @Id
    @Column(name = "ID")
    private int id;

    @TableHeader(name = "№ МЛ",sequence = 0)
    @Column(name = "NOMER_MARH")
    private double routingNumber ;

    @Temporal(TemporalType.TIMESTAMP)
    @TableHeader(name = "Дата",sequence = 1)
    @Column(name = "DATE_MARH")
    private Date routingDate ;

    @Column(name = "BRIGADA")
    private int brigada;

    @Column(name = "KOL_MARH")
    private double kolMarh ;

    @Column(name = "ID_IZD")
    private int idIzd;

    @Column(name = "SAR")
    private int sar;

    @Column(name = "NAR")
    private String nar;

    @TableHeader(name = "Модель",  sequence = 2)
    @Column(name = "FAS")
    private int fas;

    @Column(name = "NAIM")
    private String naim;

    @Column(name = "POLOTNO")
    private String polotno;

    @Column(name = "ID_COLOR")
    private int idColor;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "ID_MARH_LIST2")
    private int idMarh2;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
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

    public double getKolMarh() {
        return kolMarh;
    }

    public void setKolMarh(double kolMarh) {
        this.kolMarh = kolMarh;
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

    public int getIdMarh2() {
        return idMarh2;
    }

    public void setIdMarh2(int anInt) {
        this.idMarh2 = anInt;
    }

    @Override
    public String toString() {
        return "маршрутный лист - "+ routingNumber ;
    }
}
