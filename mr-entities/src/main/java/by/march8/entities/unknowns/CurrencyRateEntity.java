package by.march8.entities.unknowns;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;
import java.util.Date;

@NamedQueries({
        @NamedQuery(name = "CurrencyRateEntity.findByActualDate",
                query = "SELECT currency FROM CurrencyRateEntity currency WHERE currency.dateRate = :date " +
                        "order by currency.currencyName"),
        @NamedQuery(name= "CurrencyRateEntity.findRateByActualDate",
                query = "Select currency FROM CurrencyRateEntity currency WHERE currency.dateRate = :date AND currency.currencyId = :valuta ")
})

/**
 * @author Andy 05.08.2016.
 */
@Entity
@Table(name = "V_CURRENCY_RATE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_POSTGRESQL)
public class CurrencyRateEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_valuta2")
    private int currencyId;

    @Column(name = "name")
    @TableHeader(name = "Валюта_Код валюты", sequence = 20)
    private String currencyName ;

    @Column(name = "full_name")
    @TableHeader(name = "Валюта_Наименование", sequence = 30)
    private String currencyFullName ;

    @Column(name = "date_start")
    @Temporal(TemporalType.TIMESTAMP)
    @TableHeader(name = "Курс валюты по нац. банку_По состоянию на", sequence = 50)
    private Date dateRate;

    @Column(name = "kurs")
    private double valueRate ;

    @Column(name = "date_ins")
    @Temporal(TemporalType.TIMESTAMP)
   // @TableHeader(name = "Курс валюты по нац. банку_Запись добавлена", sequence = 60)
    private Date insetRecordTime;

    @Override
    public int getId() {
        return id;
    }

    @TableHeader(name = "Курс валюты по нац. банку_Курс валюты", sequence = 55)
    public String getRateValueAsString(){
        return String.format("%.4f",valueRate);
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(final int currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(final String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyFullName() {
        return currencyFullName;
    }

    public void setCurrencyFullName(final String currencyFullName) {
        this.currencyFullName = currencyFullName;
    }

    public Date getDateRate() {
        return dateRate;
    }

    public void setDateRate(final Date dateRate) {
        this.dateRate = dateRate;
    }

    public double getValueRate() {
        return valueRate;
    }

    public void setValueRate(final double valueRate) {
        this.valueRate = valueRate;
    }

    public Date getInsetRecordTime() {
        return insetRecordTime;
    }

    public void setInsetRecordTime(final Date insetRecordTime) {
        this.insetRecordTime = insetRecordTime;
    }
}
