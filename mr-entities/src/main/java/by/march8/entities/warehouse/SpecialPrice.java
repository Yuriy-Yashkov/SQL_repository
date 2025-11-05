package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Объект специальной цены
 *
 * @author Andy 31.05.2016.
 */
@Entity
@Table(name="_speccena")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class SpecialPrice extends BaseEntity {
    /**
     * Идентификатор записи
     */
    @Id
    @Column(name = "id")
    private int id;
    /**
     * Код контрагента
     */
    @Column(name="skod_kontragenta")
    private int contractorCode;
    /**
     * Номер модели изделия
     */
    @Column(name="smodel")
    private String modelNumber;
    /**
     * Номер артьикула изделия
     */
    @Column(name="sart")
    private String articleNumber;
    /**
     * Сорт изделия
     */
    @Column(name = "ssort")
    private int itemGrade;
    /**
     * Минимальный размер в диапазоне
     */
    @Column(name = "srazmer")
    private int sizeMinimum;
    /**
     * Максимальный размер в диапазоне
     */
    @Column(name = "srazmer_end")
    private int sizeMaximum;
    /**
     * Минимальный рост в диапазоне
     */
    @Column(name = "srost")
    private int growthMinimum;
    /**
     * Максимальный рост в диапазоне
     */
    @Column(name = "srost_end")
    private int growthMaximum;

    /**
     * Значение специальной цены для изделия в диапазонах
     */
    @Column(name = "scena")
    private double valuePrice;

    @Column(name="general_price")
    private boolean generalPrice ;


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public int getContractorCode() {
        return contractorCode;
    }

    public void setContractorCode(final int contractorCode) {
        this.contractorCode = contractorCode;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(final String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(final String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public int getItemGrade() {
        return itemGrade;
    }

    public void setItemGrade(final int itemGrade) {
        this.itemGrade = itemGrade;
    }

    public int getSizeMinimum() {
        return sizeMinimum;
    }

    public void setSizeMinimum(final int sizeMinimum) {
        this.sizeMinimum = sizeMinimum;
    }

    public int getSizeMaximum() {
        return sizeMaximum;
    }

    public void setSizeMaximum(final int sizeMaximum) {
        this.sizeMaximum = sizeMaximum;
    }

    public int getGrowthMinimum() {
        return growthMinimum;
    }

    public void setGrowthMinimum(final int growthMinimum) {
        this.growthMinimum = growthMinimum;
    }

    public int getGrowthMaximum() {
        return growthMaximum;
    }

    public void setGrowthMaximum(final int growthMaximum) {
        this.growthMaximum = growthMaximum;
    }

    public double getValuePrice() {
        return valuePrice;
    }

    public void setValuePrice(final double valuePrice) {
        this.valuePrice = valuePrice;
    }

    public boolean isGeneralPrice() {
        return generalPrice;
    }

    public void setGeneralPrice(boolean generalPrice) {
        this.generalPrice = generalPrice;
    }
}
