package by.march8.entities.classifier;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 25.04.2018 - 10:21.
 */

@NamedQueries({
        @NamedQuery(name = "ProductionItemBase.likeByModel",
                query = "SELECT item FROM ProductionItemBase item WHERE lower(item.modelNumber)  like :model " +
                        "order by " +
                        " item.modelNumber "),

        @NamedQuery(name = "ProductionItemBase.likeByArticle",
                query = "SELECT item FROM ProductionItemBase item WHERE lower(item.articleNumber)  like :article " +
                        "order by " +
                        " item.articleCode ")
})

@Entity
@Table(name = "nsi_kld")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ProductionItemBase extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="kod")
    private int id ;
    @Column(name = "sar")
    private String articleCode;
    @Column(name = "nar")
    private String articleNumber;
    @Column(name="fas")
    private String modelNumber;
    @Column(name ="ngpr")
    private String name ;
    @Transient
    private double wholesalePrice;
    @Transient
    private double vat ;


    public String getArticleCode() {
        if (articleCode != null) {
            return articleCode.trim();
        } else {
            return "";
        }
    }

    public void setArticleCode(final String articleCode) {
        this.articleCode = articleCode;
    }

    public String getArticleNumber() {
        if (articleNumber != null) {
            return articleNumber.trim();
        } else {
            return "";
        }
    }

    public void setArticleNumber(final String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public String getModelNumber() {
        if (modelNumber != null) {
            return modelNumber.trim();
        } else {
            return "";
        }
    }

    public void setModelNumber(final String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public double getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(final double wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(final double vat) {
        this.vat = vat;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProductionItemBase{" +
                "name='" + name + '\'' +
                ", articleCode='" + articleCode + '\'' +
                ", articleNumber='" + articleNumber + '\'' +
                '}';
    }
}
