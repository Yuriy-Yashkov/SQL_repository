package by.march8.entities.classifier;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = "ClassifierModelView.findAll",
                query = "SELECT model FROM ClassifierModelView model  " +
                        "order by " +
                        "model.modelNumber, model.name , model.articleName  "),
        @NamedQuery(name = "ClassifierModelView.findCanvas",
                query = "SELECT model FROM ClassifierModelView model WHERE model.articleCode >=47000000 AND model.articleCode < 48000000 " +
                        "order by " +
                        " model.articleCode "),
        @NamedQuery(name = "ClassifierModelView.findItems",
                query = "SELECT model FROM ClassifierModelView model WHERE model.articleCode >=41000000 AND model.articleCode < 44000000 " +
                        "order by " +
                        "model.modelNumber, model.name , model.articleName  "),
        @NamedQuery(name = "ClassifierModelView.findByArticle",
                query = "SELECT model FROM ClassifierModelView model WHERE model.articleCode = :article " +
                        "order by " +
                        "model.modelNumber, model.name , model.articleName  "),
        @NamedQuery(name = "ClassifierModelView.findByGroup",
                query = "SELECT model FROM ClassifierModelView model WHERE model.article like :article  " +
                        "order by " +
                        "model.modelNumber, model.name , model.articleName  "),
        @NamedQuery(name = "ClassifierModelView.findByGroupAndAssortment",
                query = "SELECT model FROM ClassifierModelView model WHERE model.article like :article and " +
                        "model.name = :name "+
                        "order by " +
                        "model.modelNumber, model.name , model.articleName  "),
        @NamedQuery(name = "ClassifierModelView.findByGroupAndModel",
                query = "SELECT model FROM ClassifierModelView model WHERE model.modelNumber = :model " +
                        "order by " +
                        "model.modelNumber, model.name , model.articleName  "),
        @NamedQuery(name = "ClassifierModelView.findByArticleName",
                query = "SELECT model FROM ClassifierModelView model WHERE model.articleName = :article " +
                        "order by " +
                        "model.modelNumber, model.name , model.articleName ")
})
@Entity
@Table(name = "nsi_kld")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ClassifierModelView extends BaseEntity {

    /**
     * Идентификатор изделия
     */
    @Id
    @Column(name = "kod")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableHeader(name = "ID",width = -80, sequence = 1000)
    private int id;

    //                left join (select ngpr, fas, nar, sar, kod from nsi_kld) as t2 on t3.kod = t2.kod
    /**
     * Наименование изделия
     */
    @Column(name = "ngpr")
    private String name;

    /**
     * Номер модели изделия
     */
    @Column(name = "fas")
    @TableHeader(name = "Модель", width = -100, sequence = 4)
    private int modelNumber;

    /**
     * Наименование артикула
     */
    @Column(name = "nar")
    private String articleName;

    /**
     * Шифр артикула
     */
    @Column(name = "sar")
    @TableHeader(name = "Шифр артикула", width = -100, sequence = 1)
    private int articleCode;

    @Column(name = "sar", insertable = false, updatable = false)
    private String article;

    /**
     * Код цеха
     */
    @Column(name = "sc")
    //@TableHeader(name = "Цех", width = -50, sequence = 3)
    private int workshopCode;

    /**
     * Комплект
     */
    @Column(name = "komplekt")
    //@TableHeader(name = "Комплект", sequence = 6)
    private String packageSet;

    /**
     * Брэнд, коллекция
     */
    @Column(name = "brend")
    // @TableHeader(name = "Брэнд, коллекция", sequence = 7)
    private String brandSet;

    /**
     * Примечание
     */
    @Column(name = "prim")
    //@TableHeader(name = "Примечание", sequence = 8)
    private String note;

    /**
     * Код ккр
     */
    @Column(name = "ptk")
    @TableHeader(name = "КПК", width = -30, sequence = 9)
    private int codeKKR;

    /**
     * ПРизнак размера 1
     */
    @Column(name = "rzm_naim1")
    private String sizeName1 = "";

    /**
     * ПРизнак размера 2
     */
    @Column(name = "rzm_naim2")
    private String sizeName2 = "";

    /**
     * ПРизнак размера 3
     */
    @Column(name = "rzm_naim3")
    private String sizeName3 = "";


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    @TableHeader(name = "Наименование", sequence = 3)
    public String getName() {
        if (name != null) {
            return name.trim();
        } else {
            return "";
        }
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(final int modelNumber) {
        this.modelNumber = modelNumber;
    }

    @TableHeader(name = "Артикул", width = -120, sequence = 2)
    public String getArticleName() {
        if (articleName != null) {
            return articleName.trim();
        } else {
            return "";
        }
    }

    public void setArticleName(final String articleName) {
        this.articleName = articleName;
    }

    public int getArticleCode() {


        return articleCode;
    }

    public void setArticleCode(final int articleCode) {
        this.articleCode = articleCode;
    }

    @Override
    public String toString() {
        return "ClassifierModelParams{" +
                "sizeName3='" + sizeName3 + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", modelNumber=" + modelNumber +
                ", articleName='" + articleName + '\'' +
                ", articleCode=" + articleCode +
                ", workshopCode=" + workshopCode +
                ", packageSet='" + packageSet + '\'' +
                ", brandSet='" + brandSet + '\'' +
                ", note='" + note + '\'' +
                ", codeKKR=" + codeKKR +
                ", sizeName1='" + sizeName1 + '\'' +
                ", sizeName2='" + sizeName2 + '\'' +
                '}';
    }

    public int getWorkshopCode() {
        return workshopCode;
    }

    public void setWorkshopCode(final int workshopCode) {
        this.workshopCode = workshopCode;
    }

    public String getPackageSet() {
        return packageSet;
    }

    public void setPackageSet(final String packageSet) {
        this.packageSet = packageSet;
    }

    public String getBrandSet() {
        return brandSet;
    }

    public void setBrandSet(final String brandSet) {
        this.brandSet = brandSet;
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    public int getCodeKKR() {
        return codeKKR;
    }

    public void setCodeKKR(final int codeKKR) {
        this.codeKKR = codeKKR;
    }

    public String getSizeName1() {
        return sizeName1;
    }

    public void setSizeName1(final String sizeName1) {
        this.sizeName1 = sizeName1;
    }

    public String getSizeName2() {
        return sizeName2;
    }

    public void setSizeName2(final String sizeName2) {
        this.sizeName2 = sizeName2;
    }

    public String getSizeName3() {
        return sizeName3;
    }

    public void setSizeName3(final String sizeName3) {
        this.sizeName3 = sizeName3;
    }

}
