package by.march8.ecs.application.modules.art.model;

import by.march8.api.BaseEntity;


public class FirstProductionReportModel extends BaseEntity {

    // Колонка 1
    private String articleCode;

    // Колонка 2
    private int quantity;

    // Колонка 3
    private double independentValue;

    // Колонка 4
    private double normalCostMaterials;

    // Колонка 5
    private double normalCostAuxiliaryMaterials;

    // Колонка 6
    private double wageStandart;

    // Колонка 7
    private double generalBusines;

    // Колонка 8
    /**
     * Сумма на 1 ед.
     */
    private double sum;

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getIndependentValue() {
        return independentValue;
    }

    public void setIndependentValue(double independentValue) {
        this.independentValue = independentValue;
    }

    public double getNormalCostMaterials() {
        return normalCostMaterials;
    }

    public void setNormalCostMaterials(double normalCostMaterials) {
        this.normalCostMaterials = normalCostMaterials;
    }

    public double getNormalCostAuxiliaryMaterials() {
        return normalCostAuxiliaryMaterials;
    }

    public void setNormalCostAuxiliaryMaterials(double normalCostAuxiliaryMaterials) {
        this.normalCostAuxiliaryMaterials = normalCostAuxiliaryMaterials;
    }

    public double getWageStandart() {
        return wageStandart;
    }

    public void setWageStandart(double wageStandart) {
        this.wageStandart = wageStandart;
    }

    public double getGeneralBusines() {
        return generalBusines;
    }

    public void setGeneralBusines(double generalBusines) {
        this.generalBusines = generalBusines;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    /**
     * Запись основных полей. Рассчет суммы проводится автоматически.
     * @param article Название артикла в формате String.
     * @param quantity Количество изделий по артикулу.
     * @param independent Независимая стоимость 1 еденицы товара(если смеются разные ценники то находим среднее
     *                    арифметическое)
     * @param costMaterial Норма стоимости сырья на 1 ед. товара
     * @param auxiliaryMaterials Норма стоимости вспомогательных материалов
     * @param wage Норматив заработной платы.
     * @param common Общехозяйственный %
     */
    public void recording(String article, int quantity, double independent, double costMaterial,
                          double auxiliaryMaterials, double wage, double common) {
        articleCode = article;
        this.quantity = quantity;
        independentValue = independent;
        normalCostMaterials = costMaterial;
        normalCostAuxiliaryMaterials = auxiliaryMaterials;
        wageStandart = wage;
        generalBusines = common;
        sum = independentValue + normalCostMaterials + normalCostAuxiliaryMaterials + wageStandart + generalBusines;
    }

    @Override
    public String toString() {
        return
                articleCode +
                        "\t" + articleCode +
                        "\t" + quantity +
                        "\t" + independentValue +
                        "\t" + normalCostMaterials +
                        "\t" + normalCostAuxiliaryMaterials +
                        "\t" + wageStandart +
                        "\t" + generalBusines +
                        "\t" + sum;
    }
}
