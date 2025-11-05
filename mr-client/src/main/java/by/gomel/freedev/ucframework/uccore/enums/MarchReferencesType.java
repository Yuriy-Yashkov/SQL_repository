package by.gomel.freedev.ucframework.uccore.enums;

import by.march8.entities.admin.FunctionMode;
import by.march8.entities.admin.FunctionalRole;
import by.march8.entities.admin.UserInformation;
import by.march8.entities.admin.UserRight;
import by.march8.entities.admin.UserRole;
import by.march8.entities.classifier.EquipmentItem;
import by.march8.entities.classifier.MaterialType;
import by.march8.entities.company.CompanyDepartment;
import by.march8.entities.company.CompanyPosition;
import by.march8.entities.company.Employee;
import by.march8.entities.company.VEmployeeArt;
import by.march8.entities.contractor.ClientEmailItem;
import by.march8.entities.documents.DocumentEntity;
import by.march8.entities.documents.DocumentRelation;
import by.march8.entities.documents.DocumentTypeEntity;
import by.march8.entities.label.LabelOne;
import by.march8.entities.label.VModelTestP;
import by.march8.entities.label.VNSICD;
import by.march8.entities.label.VNSIGOST;
import by.march8.entities.label.VNSIPolotno;
import by.march8.entities.label.VNSITextil;
import by.march8.entities.marketing.PriceListType;
import by.march8.entities.materials.CanvasComponent;
import by.march8.entities.materials.CanvasItem;
import by.march8.entities.materials.CanvasModifier;
import by.march8.entities.materials.CanvasWeave;
import by.march8.entities.materials.YarnCategory;
import by.march8.entities.materials.YarnComponentType;
import by.march8.entities.materials.YarnItem;
import by.march8.entities.plan.TypeItem;
import by.march8.entities.product.ConfectionMap;
import by.march8.entities.product.MeasurementType;
import by.march8.entities.product.ModelMaterial;
import by.march8.entities.product.ModelProduct;
import by.march8.entities.product.ModelSample;
import by.march8.entities.product.ModelSizeChart;
import by.march8.entities.product.ProductBrand;
import by.march8.entities.product.ProductCollection;
import by.march8.entities.product.ProductGroup;
import by.march8.entities.product.ProductKind;
import by.march8.entities.product.ProductRange;
import by.march8.entities.product.ProductType;
import by.march8.entities.product.Protocol;
import by.march8.entities.readonly.ContractorEntityView;
import by.march8.entities.readonly.CurrencyEntityMarch8;
import by.march8.entities.standard.Standard;
import by.march8.entities.standard.StandardType;
import by.march8.entities.standard.Unit;
import by.march8.entities.warehouse.InvoiceType;
import lombok.Getter;

/**
 * Типы справочников, используемых в программе.
 *
 * @see by.gomel.freedev.ucframework.uccore.enums.MarchSection
 */
public enum MarchReferencesType {
    // ***********************************************************************
    // Блок Справочники - Стандарты
    // ***********************************************************************
    /**
     * Справочник единиц измерения
     */
    UNIT("Справочник \"Единицы измерения\"", "Единицы измерения",
            Unit.class, ""),
    /**
     * Справочник типов классификационных стандартов
     */
    CODETYPE("Справочник \"Типы классификационного стандарта\"", "Типы классификационного стандарта",
            StandardType.class, "", "Тип классификационного стандарта"),
    /**
     * Справочник кодов классификационных стандартов
     */
    CODELIST("Справочник \"Коды классификационного стандарта\"", "Коды классификационного стандарта",
            Standard.class, "", "Код классификационного стандарта"),

    // ***********************************************************************
    // Блок Enterprise
    // ***********************************************************************
    /**
     * Справочник должностей предприятия
     */
    COMPANY_POSITION("Справочник \"Должности\"", "Должности",
            CompanyPosition.class, ""),
    /**
     * Справочник подразделений предприятия
     */
    COMPANY_DEPARTMENTS("Справочник \"Подразделения предприятия\"",
            "Подразделения предприятия", CompanyDepartment.class, ""),
    /**
     * Справочник сотрудников предприятия
     */
    COMPANY_EMPLOYEES("Справочник \"Сотрудники\"", "Сотрудники",
            Employee.class, ""),
    /**
     * Печать этикеток - Цвета
     */
    V_COMPANY_EMPLOYEES_ART("Справочник", "Справочник", VEmployeeArt.class, ""),
    /**
     * Справочник производственного оборудования
     */
    EQUIPMENT("Справочник \"Оборудование\"", "Оборудование", EquipmentItem.class,
            ""),

    // ***********************************************************************
    // Блок Materials
    // ***********************************************************************
    /**
     * Справочник полотна (Справочник №92 устар.)
     */
    MATERIAL_CANVAS("Справочник \"Полотно\"", "Полотно", CanvasItem.class, ""),
    /**
     * Справочник модификации полотна, признаков и т.д.
     */
    MATERIAL_CANVAS_MODIFIER("Справочник \"Виды отделки полотна\"",
            "Отделка полотна", CanvasModifier.class, ""),
    /**
     * Справочник набивок полотна
     */
    MATERIAL_CANVAS_WEAVE("Справочник \"Виды набивки\"", "Набивка",
            CanvasWeave.class, ""),
    /**
     * Справочник Пряжи
     */
    MATERIAL_YARN("Справочник \"Пряжа\"", "Пряжа",
            YarnItem.class, "code"),
    /**
     * Справочник компонентов пряжи
     */
    MATERIAL_YARN_TYPE("Справочник \"Типы пряжи\"",
            "Типы пряжи", YarnComponentType.class, "name"),

    /**
     * Справочник категорий пряжи
     */
    MATERIAL_YARN_CATEGORY("Справочник \"Категория пряжи\"",
            "Категория пряжи", YarnCategory.class, ""),

    // ***********************************************************************
    // Блок Справочники - Изделия
    // ***********************************************************************
    /**
     * Справочник моделей изделий
     */
    MODEL_PRODUCT("Справочник моделей изделий", "Модели изделий",
            ModelProduct.class, "model"),

    /**
     * Справочник прикладных материалов моделей изделий
     */
    MODEL_MATERIAL("Справочник прикладных материалов", "Прикладные материалы",
            ModelMaterial.class, "name"),

    /**
     * Справочник размеров моделей изделий
     */
    MODEL_SIZE("Справочник размеров моделей изделий", "Размеры",
            ModelSizeChart.class, "model"),

    /**
     * Справочник типов изделий
     */
    PRODUCT_TYPE("Справочник типов изделий", "Типы изделий",
            ProductType.class, "name"),

    /**
     * Справочник видов изделий
     */
    PRODUCT_KIND("Справочник видов изделий", "Виды изделий",
            ProductKind.class, "name"),

    /**
     * Справочник брендов изделий
     */
    PRODUCT_BRAND("Справочник брендов изделий", "Бренды изделий",
            ProductBrand.class, "name"),

    /**
     * Справочник коллекций изделий
     */
    PRODUCT_COLLECTION("Справочник коллекций изделий", "Коллекции изделий",
            ProductCollection.class, "name"),

    /**
     * Справочник групп изделий
     */
    PRODUCT_GROUP("Справочник групп изделий", "Группы изделий",
            ProductGroup.class, "name"),

    /**
     * Справочник ассортимента изделий
     */
    PRODUCT_RANGE("Справочник ассортимента изделий", "Ассортимент изделий",
            ProductRange.class, "name"),

    /**
     * Справочник типов обмеров
     */
    MEASUREMENT_TYPE("Справочник типов обмеров", "Типы обмеров",
            MeasurementType.class, "name"),

    // ***********************************************************************
    // Блок ХЭО
    // ***********************************************************************

    /**
     * Конфекционная карта модели изделия
     */
    MODEL_CONFECTION_MAP("Конфекционная карта", "Конфекционная карта",
            ConfectionMap.class, ""),

    /**
     * Журнал художника
     */
    MODEL_SAMPLE("Карты описания модели", "Карты описания модели",
            ModelSample.class, "model"),

    /**
     * Справочник протоколов ХТС
     */
    MODEL_PROTOCOL("Справочник протоколов ХТС", "Протоколы ХТС",
            Protocol.class, "code"),

    // ***********************************************************************
    // Блок Администратор
    // ***********************************************************************
    /**
     * Права учетной записи
     */
    ADM_RIGHT("Права пользователя", "Права пользователя", UserRight.class,
            ""),
    /**
     * Роль учетной записи
     */
    ADM_ROLE("Роль пользователя", "Роль пользователя", UserRole.class, ""),
    /**
     * Функциональный режим, на который распространяются права и роль учетной
     * записи
     */
    ADM_FUNCTION_MODE("Функциональный режим", "Функциональный режим",
            FunctionMode.class, ""),
    ADM_FUNCTIONAL_ROLE("Функциональные роли", "Функциональные роли",
            FunctionalRole.class, ""),
    // ***********************************************************************
    // Блок Контракторы
    // ***********************************************************************

    // ***********************************************************************
    // Блок Учёт
    // ***********************************************************************
    /**
     * Склад готовой продукции - контрагенты
     */
    /**
     * Печать этикеток - Печать этикеток на изделия
     */
    ACCOUNTING_LABEL_ONE("Печать этикеток на изделия", "Печать этикеток на изделия", LabelOne.class, ""),
    /**
     * Печать этикеток - Цвета
     */
    ACCOUNTING_VNSICD("Цвета", "Цвета", VNSICD.class, ""),
    /**
     * Печать этикеток - Полотно
     */
    ACCOUNTING_VNSPOLOTNO("Состав полотен", "Состав полотен", VNSIPolotno.class, ""),
    /**
     * Печать этикеток - Текстиль
     */
    ACCOUNTING_VNSITEXTIL("Текстиль", "Текстиль", VNSITextil.class, ""),
    /**
     * Печать этикеток - ГОСТы
     */
    ACCOUNTING_VNSISTANDART("ГОСТы", "ГОСТы", VNSIGOST.class, ""),

    /**
     * Печать этикеток - Изделия
     */
    ACCOUNTING_VMODEL("Изделия", "Изделия", VModelTestP.class, ""),

    //ACCOUNTING_TEMPENTITY("123","321", TempEntity.class,""),
    // ***********************************************************************
    // Блок Настройки->Документы
    // ***********************************************************************
    /**
     * Документы - Типы Документов
     */
    SETTINGS_DOCUMENT_TYPE("Типы документов", "Типы документов",
            DocumentTypeEntity.class, "", "Выберите тип документа"),
    /**
     * Документы - Типы Документов
     */
    SETTINGS_DOCUMENT_RELATION("Взаимодействие документов", "Взаимодействие документов",
            DocumentRelation.class, "", "Выберите тип связи документов"),
    /**
     * Документы - Документы
     */
    SETTINGS_DOCUMENT("Документы", "Документы",
            DocumentEntity.class, "", "Выберите документ"),

    ADM_ACCOUNTS("Учетная запись", "Учетная запись", UserInformation.class, ""),

    MATERIAL_CANVAS_COMPONENT("Компоненты полотна", "Компоненты полотна", CanvasComponent.class, ""),

    INVOICE_TYPE("Типы накладных", "Типы накладных", InvoiceType.class, ""),

    PRICE_LIST_TYPE("Типы прейскурантов", "Типы прейскурантов", PriceListType.class, ""),
    //*********************************************************************
    // СПРАВОЧНИКИ ПРОГРАММЫ MYREPORTS ПРЕИМУЩЕСТВЕННО В РЕЖИМЕ ТОЛЬКО ЧТЕНИЕ
    //*********************************************************************
    CLIENT_EMAIL("E-mail клиентов", "E-mail клиентов", ClientEmailItem.class, ""),


    //*********************************************************************
    // СПРАВОЧНИКИ ПРОГРАММЫ MARCH8 ПРЕИМУЩЕСТВЕННО В РЕЖИМЕ ТОЛЬКО ЧТЕНИЕ
    //*********************************************************************
    CONTRACTOR_OLD("Контрагенты March8", "Контрагенты March8", ContractorEntityView.class, ""),

    CURRENCY_OLD("Валюта March8", "Валюта March8", CurrencyEntityMarch8.class, ""),

    MATERIAL_TYPE("Типы материала", "Тип материала", MaterialType.class, ""),

    PLAN_TYPE("План \"Вид\"", "Вид", TypeItem.class, "");

    // ***********************************************************************
    /**
     * Класс-сущность справочника
     */
    @Getter
    private Class<?> classifierClass;
    /**
     * Название справочника, полное
     */
    @Getter
    private String referenceName;
    /**
     * Название справочника, краткое
     */
    @Getter
    private String shortName;
    /**
     * Поле в таблице справочника, по которому идет сортировка по-умолчанию
     */
    private String orderBy;
    /**
     * Название метки выбора для этого справочника
     */
    @Getter
    private String labelName;

    MarchReferencesType(final Class<?> classifierClass, final String labelName) {
        this.classifierClass = classifierClass;
        this.labelName = labelName;
    }

    MarchReferencesType(final String referenceName, final String shortName,
                        final Class<?> classifierClass, final String orderBy) {
        this.referenceName = referenceName;
        this.shortName = shortName;
        this.classifierClass = classifierClass;
        this.orderBy = orderBy;
    }

    MarchReferencesType(final String referenceName, final String shortName,
                        final Class<?> classifierClass, final String orderBy,
                        final String labelName) {
        this.referenceName = referenceName;
        this.shortName = shortName;
        this.classifierClass = classifierClass;
        this.orderBy = orderBy;
        this.labelName = labelName;
    }

    public static MarchReferencesType getTypeByClassName(Class<?> c) {
        for (MarchReferencesType type : MarchReferencesType.values()) {
            if (type.getClassifierClass() == c) {
                return type;
            }
        }
        return null;
    }

}
