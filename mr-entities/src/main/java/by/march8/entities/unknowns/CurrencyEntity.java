package by.march8.entities.unknowns;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * @ author Andy 26.10.2015.
 */
@NamedQueries({
        @NamedQuery(name = "CurrencyEntity.findByCurrencyType",
                query = "SELECT currency FROM CurrencyEntity currency  " +
                        "WHERE currency.currencyId = :currency and rateDate = :date")
})

@Entity
@Table(name = "valuta_kurs")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_POSTGRESQL)
public class CurrencyEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "id_valuta2")
    private int currencyId;
    @Column(name = "date_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rateDate;

    @Column(name = "kurs")
    private float rate ;

    public float getRate() {
        return rate;
    }

    public void setRate(final float rate) {
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(final int currencyId) {
        this.currencyId = currencyId;
    }

    public Date getRateDate() {
        return rateDate;
    }

    public void setRateDate(final Date rateDate) {
        this.rateDate = rateDate;
    }
}
