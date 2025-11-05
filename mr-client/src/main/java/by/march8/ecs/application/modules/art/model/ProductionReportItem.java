package by.march8.ecs.application.modules.art.model;

import by.march8.api.BaseEntity;
import by.march8.api.TableHeader;

import java.util.Objects;

/**
 * @author Andy 20.01.2017.
 */
public class ProductionReportItem extends BaseEntity {
    private int id;

    // Колонка 0
    private String articleCode;

    // Колонка 1
    private String itemName;

    // колонка 1.1
    private String articleName;

    // Колонка 2
    private String modelNumber;

    // Колонка 3
    private int remainsBeginPeriod;

    // Колонка 4
    private int inComingCuttingDept;

    // Колонка 5
    private int inComing708Dept;

    // Колонка 6
    private int outComingWarehouse;

    // Колонка 7
    private int outComingCuttingDept;

    // Колонка 8
    private int remainsEndPeriod;


    private int dayNumber;
    private int monthNumber;
    private int yearNumber;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    @TableHeader(name = "Шифр арт", sequence = 10, width = -110)
    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(final String articleCode) {
        this.articleCode = articleCode;
    }

    @TableHeader(name = "Наименование", sequence = 15)
    public String getItemName() {
        return itemName;
    }

    public void setItemName(final String itemName) {
        this.itemName = itemName;
    }

    @TableHeader(name = "Артикул", sequence = 16, width = -110)
    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(final String articleName) {
        this.articleName = articleName;
    }

    @TableHeader(name = "Модель", sequence = 20, width = -80)
    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(final String modelNumber) {

        this.modelNumber = String.valueOf(Integer.parseInt(modelNumber));


        //this.modelNumber = modelNumber;
    }

    @TableHeader(name = "Остатки за период_На начало", sequence = 30, width = -80)
    public int getRemainsBeginPeriod() {
        return remainsBeginPeriod;
    }

    public void setRemainsBeginPeriod(final int remainsBeginPeriod) {
        this.remainsBeginPeriod = remainsBeginPeriod;
    }

    @TableHeader(name = "Приход_из цеха", sequence = 40, width = -80)
    public int getInComingCuttingDept() {
        return inComingCuttingDept;
    }

    public void setInComingCuttingDept(final int inComingCuttingDept) {
        this.inComingCuttingDept = inComingCuttingDept;
    }

    //@TableHeader(name="Приход_708",sequence =45 , width = -80)
    public int getInComing708Dept() {
        return inComing708Dept;
    }

    public void setInComing708Dept(final int inComing708Dept) {
        this.inComing708Dept = inComing708Dept;
    }

    @TableHeader(name = "Сдано изделий_На склад", sequence = 50, width = -80)
    public int getOutComingWarehouse() {
        return outComingWarehouse;
    }

    public void setOutComingWarehouse(final int outComingWarehouse) {
        this.outComingWarehouse = outComingWarehouse;
    }

    @TableHeader(name = "Сдано изделий_В цех", sequence = 55, width = -80)
    public int getOutComingCuttingDept() {
        return outComingCuttingDept;
    }

    public void setOutComingCuttingDept(final int outComingCuttingDept) {
        this.outComingCuttingDept = outComingCuttingDept;
    }

    @TableHeader(name = "Остатки за период_На конец", sequence = 35, width = -80)
    public int getRemainsEndPeriod() {
        return remainsEndPeriod;
    }

    public void setRemainsEndPeriod(final int remainsEndPeriod) {
        this.remainsEndPeriod = remainsEndPeriod;
    }

    @TableHeader(name = "Дата_День", sequence = 1, width = -50)
    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(final int dayNumber) {
        this.dayNumber = dayNumber;
    }

    @TableHeader(name = "Дата_Месяц", sequence = 2, width = -50)
    public int getMonthNumber() {
        return monthNumber;
    }

    public void setMonthNumber(final int monthNumber) {
        this.monthNumber = monthNumber;
    }

    @TableHeader(name = "Дата_Год", sequence = 3, width = 0)
    public int getYearNumber() {
        return yearNumber;
    }

    public void setYearNumber(final int yearNumber) {
        this.yearNumber = yearNumber;
    }


    @Override
    public String toString() {
        return
                articleCode +
                        "\t" + itemName +
                        "\t" + articleName +
                        "\t" + modelNumber +
                        "\t" + remainsBeginPeriod +
                        "\t" + inComingCuttingDept +
                        "\t" + inComing708Dept +
                        "\t" + outComingWarehouse +
                        "\t" + outComingCuttingDept +
                        "\t" + remainsEndPeriod +
                        "\t" + dayNumber +
                        "\t" + monthNumber;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ProductionReportItem that = (ProductionReportItem) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(articleCode, that.articleCode) &&
                Objects.equals(remainsBeginPeriod, that.remainsBeginPeriod) &&
                Objects.equals(inComingCuttingDept, that.inComingCuttingDept) &&
                Objects.equals(inComing708Dept, that.inComing708Dept) &&
                Objects.equals(outComingWarehouse, that.outComingWarehouse) &&
                Objects.equals(outComingCuttingDept, that.outComingCuttingDept) &&
                Objects.equals(remainsEndPeriod, that.remainsEndPeriod) &&
                Objects.equals(dayNumber, that.dayNumber) &&
                Objects.equals(monthNumber, that.monthNumber) &&
                Objects.equals(yearNumber, that.yearNumber) &&
                Objects.equals(itemName, that.itemName) &&
                Objects.equals(articleName, that.articleName) &&
                Objects.equals(modelNumber, that.modelNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, articleCode, itemName, articleName, modelNumber, remainsBeginPeriod, inComingCuttingDept, inComing708Dept, outComingWarehouse, outComingCuttingDept, remainsEndPeriod, dayNumber, monthNumber, yearNumber);
    }
}
