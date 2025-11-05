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
        @NamedQuery(name = "RouteSheetSelected.findAll",
                query = "SELECT items FROM RouteSheetSelected items " +
                        " order by items.kod.nomer desc ")
})

@Entity
@Table(name = "MARH_LIST2")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class RouteSheetSelected extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM_CODE")
    private RouteSheet kod;

    @TableHeader(name = "Номер", width = 60, sequence = 1)
    public double getNomerMarh() {
        return (kod!=null)?kod.getNomer():0;
    }

    @TableHeader(name = "Дата", width = 60, sequence = 2)
    public Date getDateMarh() {
        return kod.getData();
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_INS")
    private Date date ;

    @Column(name = "TYPE")
    private int type;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public RouteSheet getKod() {
        return kod;
    }

    public void setKod(RouteSheet kod) {
        this.kod = kod;
    }

    @Override
    public String toString() {
        return "маршрутный лист - "+kod.getNomer() ;
    }
}

