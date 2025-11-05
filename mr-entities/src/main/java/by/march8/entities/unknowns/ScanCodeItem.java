package by.march8.entities.unknowns;

import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Класс описывает структуру данных, принимаемых с терминала
 *
 * @author Andy 30.10.2015.
 */
@Entity
@Table(name = "user_scan")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ScanCodeItem {
    /**
     * Скан-код изделия
     */
    @Id
    @Column(name = "scan")
    protected long scanCode;

    /**
     * Количество единиц изделия по коду (для кодов с упаковки всегда больше 1)
     */
    @Column(name = "kol")
    protected int amount;

    /**
     * Тип изделия: 1 - россыпь, 2 - упаковка
     */
    @Column(name = "tip")
    protected int type;




    public ScanCodeItem() {
    }

    public ScanCodeItem(final long scanCode, final int amount, final int type) {
        this.scanCode = scanCode;
        this.amount = amount;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Скан-код: " + scanCode + " (" + amount + " шт.)" +
                " тип:" + type;
    }

    public long getScanCode() {
        return scanCode;
    }

    public void setScanCode(final long scanCode) {
        this.scanCode = scanCode;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(final int type) {
        this.type = type;
    }


}
