package by.march8.entities.classifier;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;


@NamedQueries({
        @NamedQuery(name = "ClassifierItem.findByArticle",
                query = "SELECT product FROM ClassifierItem product WHERE product.model.id = :article " +
                        "order by " +
                        " product.itemGrade ")
})

/**
 * Таблица классификатора для единицы изделия по модели
 * Created by Andy on 20.08.2015.
 */
@Entity
@Table(name = "nsi_sd")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ClassifierItem extends BaseEntity {

    /**
     * Внешний идентификатор изделия
     */
    @Id
    @Column(name = "kod1")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableHeader(name="ID",width = -90,sequence = 999 )
    private int id;

    /**
     * Размер изделия
     */
    @Column(name = "rzm")
    @TableHeader(name="Разм.",width = -40, sequence = 20)
    private int size;
    /**
     * Рост изделия
     */
    @Column(name = "rst")
    @TableHeader(name="Рост",width = -40, sequence = 30)
    private int growth;
    /**
     * Размер изделия для печати
     */
    @Column(name = "rzm_print")
    @TableHeader(name="Размер для печати", sequence = 10)
    private String sizePrint;

    /**
     * Сорт изделия
     */
    @Column(name = "srt")
    @TableHeader(name="Сорт",width = -40, sequence = 40)
    private int itemGrade;


    /**
     * Цена за пределы страны
     */
    @Column(name = "cnp")
    //@TableHeader(name="Цена_Экспортная",width = -80, sequence = 4)
    private float priceExport;

    /**
     * Оптовая цена
     */
    @Column(name = "cno")
    @TableHeader(name="Цена_Оптовая",width = -80, sequence = 100)
    private float priceWholesale;

    /**
     * Розничная цена
     */
    @Column(name = "cnr")
    //@TableHeader(name="Цена_Розничная",width = -80, sequence = 6)
    private float priceRetail;

    /**
     * Величина НДС
     */
    @Column(name = "nds")
    @TableHeader(name="НДС_%",width = -50, sequence = 110)
    private float valueVAT;

    /**
     * Сумма НДС
     */
    @Column(name = "cnv")
    //@TableHeader(name="НДС_Сумма",width = -80, sequence = 8)
    private float valueSumVat ;


    /**
     * EAN код
     */
    @Column(name = "ean")
    @TableHeader(name="EAN код",sequence = 200)
    private String eanCode ;

    /**
     * Признак возврата
     */
    @Column(name = "vzr")
    //@TableHeader(name="Возвр.",width = -30, sequence = 9)
    private int refundSign;

    /**
     * Размер принт 1
     */
    @Column(name = "rzm_pr1")
    //@TableHeader(name="Разм1.",width = -50, sequence = 13)
    private String sizePrint1 ;

    /**
     * Размер принт 2
     */
    @Column(name = "rzm_pr2")
   // @TableHeader(name="Разм2.",width = -50, sequence = 14)
    private String sizePrint2 ;

    /**
     * Размер принт 3
     */
    @Column(name = "rzm_pr3")
    //@TableHeader(name="Разм3.",width = -50, sequence = 15)
    private String sizePrint3 ;

    /**
     * Масса
     */
    @Column(name = "massa")
    //@TableHeader(name="Масса, кг", sequence = 12)
    private float mass ;

    /**
     * Прейскурант
     */
    @Column(name = "preiscur")
    private String priceList ;

    /**
     * Внутренний ключ классификатора для получения модели и артикула
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kod")
    private ClassifierModelParams model;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    public int getGrowth() {
        return growth;
    }

    public void setGrowth(final int growth) {
        this.growth = growth;
    }

    public String getSizePrint() {
        return sizePrint;
    }

    public void setSizePrint(final String sizePrint) {
        this.sizePrint = sizePrint;
    }

    public int getItemGrade() {
        return itemGrade;
    }

    public void setItemGrade(final int grade) {
        this.itemGrade = grade;
    }
/*
    public ClassifierModelParams getModel() {
        return model;
    }

    public void setModel(final ClassifierModelParams model) {
        this.model = model;
    }*/



    public float getPriceExport() {
        return priceExport;
    }

    public void setPriceExport(final float priceExport) {
        this.priceExport = priceExport;
    }

    public float getPriceWholesale() {
        return priceWholesale;
    }

    public void setPriceWholesale(final float priceWholesale) {
        this.priceWholesale = priceWholesale;
    }

    public float getPriceRetail() {
        return priceRetail;
    }

    public void setPriceRetail(final float priceRetail) {
        this.priceRetail = priceRetail;
    }

    public float getValueVat() {
        return valueVAT;
    }

    public void setValueVAT(final float valueVAT) {
        this.valueVAT = valueVAT;
    }

    public float getValueSumVat() {
        return valueSumVat;
    }

    public void setValueSumVat(final float valueSumVat) {
        this.valueSumVat = valueSumVat;
    }

    public String getEanCode() {
        return eanCode;
    }

    public void setEanCode(final String eanCode) {
        this.eanCode = eanCode;
    }

    public int getRefundSign() {
        return refundSign;
    }

    public void setRefundSign(final int refundSign) {
        this.refundSign = refundSign;
    }

    public String getSizePrint1() {
        return sizePrint1;
    }

    public void setSizePrint1(final String sizePrint1) {
        this.sizePrint1 = sizePrint1;
    }

    public String getSizePrint2() {
        return sizePrint2;
    }

    public void setSizePrint2(final String sizePrint2) {
        this.sizePrint2 = sizePrint2;
    }

    public String getSizePrint3() {
        return sizePrint3;
    }

    public void setSizePrint3(final String sizePrint3) {
        this.sizePrint3 = sizePrint3;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(final float mass) {
        this.mass = mass;
    }

    public ClassifierModelParams getModel() {
        return model;
    }

    public void setModel(final ClassifierModelParams model) {
        this.model = model;
    }


    @Override
    public String toString() {
        return "ClassifierGrowthParams{" +
                "id=" + id +
                ", size=" + size +
                ", growth=" + growth +
                ", sizePrint='" + sizePrint + '\'' +
                ", grade=" + itemGrade +
                ", priceExport=" + priceExport +
                ", priceWholesale=" + priceWholesale +
                ", priceRetail=" + priceRetail +
                ", valueVAT=" + valueVAT +
                ", valueSumVat=" + valueSumVat +
                ", eanCode='" + eanCode + '\'' +
                ", refundSign=" + refundSign +
                ", sizePrint1='" + sizePrint1 + '\'' +
                ", sizePrint2='" + sizePrint2 + '\'' +
                ", sizePrint3='" + sizePrint3 + '\'' +
                ", mass=" + mass +
                '}';
    }

    public String getPriceList() {
        return priceList;
    }

    public void setPriceList(final String priceList) {
        this.priceList = priceList;
    }
}
