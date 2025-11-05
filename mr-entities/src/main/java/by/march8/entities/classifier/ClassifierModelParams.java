package by.march8.entities.classifier;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Andy 20.08.2015.
 */

@NamedQueries({
        @NamedQuery(name = "ClassifierModelParams.findByArticleCode",
                query = "SELECT item FROM ClassifierModelParams item WHERE item.articleCode = :article " +
                        "ORDER BY item.assortmentList.size ")
})

@Entity
@Table(name = "nsi_kld")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ClassifierModelParams extends BaseEntity {

    /**
     * Идентификатор изделия
     */
    @Id
    @Column(name = "kod")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //                left join (select ngpr, fas, nar, sar, kod from nsi_kld) as t2 on t3.kod = t2.kod
    /**
     * Наименование изделия
     */
    @Column(name = "ngpr")
    @TableHeader(name = "Наименование", sequence = 5)
    private String name;

    /**
     * Номер модели изделия
     */
    @Column(name = "fas")
    @TableHeader(name = "Модель", sequence = 2)
    private int modelNumber;

    /**
     * Наименование артикула
     */
    @Column(name = "nar")
    @TableHeader(name = "Артикул", sequence = 4)
    private String articleName;

    /**
     * Шифр артикула
     */
    @Column(name = "sar")
    @TableHeader(name = "Шифр артикула", sequence = 1)
    private int articleCode;

    /**
     * Код цеха
     */
    @Column(name = "sc")
    @TableHeader(name = "Цех", width = -50, sequence = 3)
    private int workshopCode;

    /**
     * Комплект
     */
    @Column(name = "komplekt")
    @TableHeader(name = "Комплект", sequence = 6)
    private String packageSet;

    /**
     * Брэнд, коллекция
     */
    @Column(name = "brend")
    @TableHeader(name = "Брэнд, коллекция", sequence = 7)
    private String brandSet;

    /**
     * Примечание
     */
    @Column(name = "prim")
    @TableHeader(name = "Примечание", sequence = 8)
    private String note;

    /**
     * Код ккр
     */
    @Column(name = "kkr")
    @TableHeader(name = "Кр.", width = -30, sequence = 9)
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


    @OneToMany(targetEntity = ClassifierItem.class, mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<ClassifierItem> assortmentList;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

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

    public Set<ClassifierItem> getAssortmentList() {
        return assortmentList;
    }

    public void setAssortmentList(final Set<ClassifierItem> allNomenclature) {
        this.assortmentList = allNomenclature;
    }

    public Map<Integer, ClassifierItem> getAssortmentAsMap() {
        Map<Integer, ClassifierItem> map = new HashMap<>();

        for (ClassifierItem item : getAssortmentList()) {
            map.put(item.getId(), item);
        }

        return map;
    }
}
