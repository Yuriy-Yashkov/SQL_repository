package by.gomel.freedev.ucframework.uccore.enums;


import by.march8.entities.plan.PlanDetalization;
import by.march8.entities.plan.PlanIdItem;
import by.march8.entities.plan.PlanItem;
import by.march8.entities.plan.TypeItem;

/**
 * Created by dpliushchai on 19.11.2014.
 */
public enum MarchPlanType {
    // ***********************************************************************
    // Блок Standards
    // ***********************************************************************
    /**
     *План Производства
     */
    PLAN_ANALYSIS(" \"Выполнение плана\"", "Выполнение плана",
            PlanItem.class, ""),

    PLAN_DETALIZATION(" \"Детали плана\"", "Детали плана",
            PlanDetalization.class, ""),


    PLAN_TYPE("План \"Вид\"", "Вид", TypeItem.class, ""),

    PLAN_NAME("План \"Имя\"", "Имя", PlanIdItem.class, ""),

    PLAN_REPORT("План \"Отчет\"", "Отчеты",
            PlanItem.class, ""),

    PLAN_REPORT_MARKETING("План \"Отчет\"", "Отчеты",
            PlanItem.class, "");

    // ***********************************************************************
    /**
     * Класс-сущность справочника
     */
    private Class<?> classifierClass;
    /**
     * Название справочника, полное
     */
    private String planName;
    /**
     * Название справочника, краткое
     */
    private String shortName;
    /**
     * Поле в таблице справочника, по которому идет сортировка по-умолчанию
     */
    private String orderBy;
    /**
     * Название метки выбора для этого справочника
     */
    private String labelName;

    MarchPlanType(final Class<?> classifierClass, final String labelName) {
        this.classifierClass = classifierClass;
        this.labelName = labelName;
    }

    MarchPlanType(final String planName, final String shortName,
                  final Class<?> classifierClass, final String orderBy) {
        this.planName = planName;
        this.shortName = shortName;
        this.classifierClass = classifierClass;
        this.orderBy = orderBy;
    }

    MarchPlanType(final String planName, final String shortName,
                  final Class<?> classifierClass, final String orderBy,
                  final String labelName) {
        this.planName = planName;
        this.shortName = shortName;
        this.classifierClass = classifierClass;
        this.orderBy = orderBy;
        this.labelName = labelName;
    }

    public Class<?> getClassifierClass() {
        return classifierClass;
    }

    public String getLabelName() {
        return labelName;
    }

    public String getPlanName() {
        return planName;
    }

    public String getShortName() {
        return shortName;
    }

}
