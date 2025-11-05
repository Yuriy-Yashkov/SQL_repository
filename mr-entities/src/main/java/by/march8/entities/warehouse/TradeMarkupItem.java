package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Andy 05.05.2016.
 */
@Entity
@Table(name="_otgruz2_addition")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class TradeMarkupItem extends BaseEntity{

    @Id
    @Column(name="id_item")
    private int id;

    @Column(name="torg_nadb")
    private float valueTradeMarkup;

    @Column(name="rozn_cena")
    private float markAndPriceValue ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public float getValueTradeMarkup() {
        return valueTradeMarkup;
    }

    public void setValueTradeMarkup(final float markValue) {
        this.valueTradeMarkup = markValue;
    }

    public float getMarkAndPriceValue() {
        return markAndPriceValue;
    }

    public void setMarkAndPriceValue(final float markAndPriceValue) {
        this.markAndPriceValue = markAndPriceValue;
    }
}
