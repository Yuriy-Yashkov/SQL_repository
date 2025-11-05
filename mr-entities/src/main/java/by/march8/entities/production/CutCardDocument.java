package by.march8.entities.production;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Developer on 04.12.2019 8:45
 */
@Entity
@Table(name="kroy")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class CutCardDocument extends BaseEntity {

    @Id
    @Column(name = "kod")
    private int id;

    @Column(name = "nomer")
    private int documentNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data")
    private Date documentDate;

    @Column(name = "kol")
    private int amount;

    @Column(name = "kol_marh")
    private int amountRoute;

    @Column(name = "brigada")
    private int brigadeCode;

    @Transient
    private String brigadeName ;



    @Column(name = "vid_pech")
    private String printType;

    @Column(name = "vid_marh")
    private int routeType;


    @Column(name="n_prikaz")
    private String dispositionNumber ;

    @Column(name = "kod_pol")
    private int customerCode;

    @Transient
    private String customeName;

    @Column(name="d_prix")
    @Transient
    private Date incomingDate ;




    public String getRouteTypeView() {
        if (routeType == 1) {
            return "План производства";
        }

        if (routeType == 2) {
            return "Распоряжение";
        }

        return "";
    }
}
