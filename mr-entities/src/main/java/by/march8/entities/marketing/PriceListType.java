package by.march8.entities.marketing;

import by.march8.api.BaseEntity;
import by.march8.api.TableHeader;
import by.march8.api.ISimpleReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Andy 25.10.2017.
 */
@Entity
@Table(name="REF_PRICELIST_TYPE")
public class PriceListType extends BaseEntity implements ISimpleReference {
    /**
     * Отгрузка покупателю
     */
    public static final String PRICE_LIST_CHANGE_TRADE_ALLOWANCE = "Изменение торговой надбавки";

    /**
     * Перемещение в розницу
     */
    public static final String PRICE_LIST_CHANGE_REDUCTION = "Уценка изделий";

    @Id
    @Column(name="ID")
    private int id;

    @TableHeader(name = "Наименование", width = 150, sequence = 0)
    @Column(name="NAME")
    private String name;

    @TableHeader(name = "Примечание", width = 150, sequence = 1)
    @Column(name="NOTE")
    private String note;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public void setNote(final String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return name;
    }
}
