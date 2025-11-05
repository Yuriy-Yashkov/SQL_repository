package by.march8.entities.production;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lidashka on 17.10.2018.
 */
@NamedQueries({
        @NamedQuery(name = "RouteSheet.findByDate",
                query = "SELECT items FROM RouteSheet items WHERE items.data between :date_start and :date_end " +
                        "order by items.nomer"),

        @NamedQuery(name = "RouteSheet.findByDateAndIdList",
        query = "SELECT items FROM RouteSheet items " +
                " WHERE items.id not in (:idLists) and items.data between :date_start and :date_end " +
                " order by items.nomer")
})
@Entity
@Table(name="marh_list")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class RouteSheet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kod")
    private int id;

    @TableHeader(name = "№ М/Л", sequence = 0)
    @Column(name = "nomer")
    private double nomer;

    @Temporal(TemporalType.TIMESTAMP)
    @TableHeader(name = "Дата", sequence = 1)
    @Column(name = "data")
    private Date data;

    @Column(name="brigadir")
    private String brigadir;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "d_prix")
    private Date dPrix;

    @Column(name = "kroy")
    private int kroy;

    @TableHeader(name = "Кол-во", sequence = 4)
    @Column(name = "kol")
    private int kol;

    @Column(name = "kod_owner")
    private int kodOwner;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datevrkv")
    private Date datevrkv;

    @Column(name="uservrkv")
    private String uservrkv;

    @Column(name="kpodvrkv")
    @TableHeader(name = "Бригада", sequence = 3)
    private String kpodvrkv;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datekrkv")
    private Date datekrkv;

    @Column(name="userkrkv")
    private String userkrkv;

    @Column(name="kpodkrkv")
    private String kpodkrkv;

    public RouteSheet() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public double getNomer() {
        return nomer;
    }

    public void setNomer(double nomer) {
        this.nomer = nomer;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getBrigadir() {
        return brigadir;
    }

    public void setBrigadir(String brigadir) {
        this.brigadir = brigadir;
    }

    public Date getdPrix() {
        return dPrix;
    }

    public void setdPrix(Date dPrix) {
        this.dPrix = dPrix;
    }

    public int getKroy() {
        return kroy;
    }

    public void setKroy(int kroy) {
        this.kroy = kroy;
    }

    public int getKol() {
        return kol;
    }

    public void setKol(int kol) {
        this.kol = kol;
    }

    public int getKodOwner() {
        return kodOwner;
    }

    public void setKodOwner(int kodOwner) {
        this.kodOwner = kodOwner;
    }

    public Date getDatevrkv() {
        return datevrkv;
    }

    public void setDatevrkv(Date datevrkv) {
        this.datevrkv = datevrkv;
    }

    public String getUservrkv() {
        return uservrkv;
    }

    public void setUservrkv(String uservrkv) {
        this.uservrkv = uservrkv;
    }

    public String getKpodvrkv() {
        return kpodvrkv;
    }

    public void setKpodvrkv(String kpodvrkv) {
        this.kpodvrkv = kpodvrkv;
    }

    public Date getDatekrkv() {
        return datekrkv;
    }

    public void setDatekrkv(Date datekrkv) {
        this.datekrkv = datekrkv;
    }

    public String getUserkrkv() {
        return userkrkv;
    }

    public void setUserkrkv(String userkrkv) {
        this.userkrkv = userkrkv;
    }

    public String getKpodkrkv() {
        return kpodkrkv;
    }

    public void setKpodkrkv(String kpodkrkv) {
        this.kpodkrkv = kpodkrkv;
    }
}
