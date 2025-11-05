package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.ISimpleReference;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Типы накладных(Отгрузка покупателю/ Возврат от покупателя и т.д. )
 * Created by Andy on 13.08.2015.
 */


@Entity
@Table(name = "REF_INVOICE_TYPE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class InvoiceType extends BaseEntity implements ISimpleReference {

    /**
     * Отгрузка покупателю
     */
    public static final String DOCUMENT_SALE_BUYER = "Отгрузка покупателю";

    /**
     * Перемещение в розницу
     */
    public static final String DOCUMENT_SALE_RETAIL = "Перемещение в розницу";

    /**
     * Возврат от покупателя
     */
    public static final String DOCUMENT_REFUND_BUYER = "Возврат от покупателя";

    /**
     * Возврат из розницы
     */
    public static final String DOCUMENT_REFUND_RETAIL = "Возврат из розницы";

    /**
     * Отгрузка материала
     */
    public static final String DOCUMENT_SALE_MATERIAL = "Отгрузка материала";

    /**
     * Возврат материала
     */
    public static final String DOCUMENT_REFUND_MATERIAL = "Возврат материала";

    @Id
    private int id;
    @TableHeader(name = "Наименование", width = 150, sequence = 0)
    private String name;
    @TableHeader(name = "Примечание", width = 150, sequence = 1)
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
