package by.march8.entities.product;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.utils.DateUtils;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@NamedQueries({
        @NamedQuery(name = "ProductionCatalog.findByPeriod",
                query = "SELECT item FROM ProductionCatalog item WHERE item.documentDate BETWEEN :periodBegin AND :periodEnd " +
                        "ORDER BY item.documentDate")
})

/**
 * @author Andy 21.01.2019 - 7:28.
 */
@Entity
@Table(name = "PRODUCTION_CATALOG")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ProductionCatalog extends BaseEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableHeader(name = "ID", width = -50, sequence = 999)
    private int id;
    @Column(name = "DOCUMENT_NUMBER")
    private String documentNumber;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DOCUMENT_DATE")
    private Date documentDate;
    @Column(name = "STATUS")
    private int status;
    @Column(name = "TYPE")
    private int type;
    @Column(name = "VALIDITY_BEGIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validityBegin;
    @Column(name = "VALIDITY_END")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validityEnd;
    @Column(name = "VISIBLE")
    private boolean visible;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "ARTICLES")
    private String articles ;
    @Transient
    private String documentStatus;
    @Transient
    private String documentInformation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @TableHeader(name = "Документ_Номер", width = -100, sequence = 10)
    public String getDocumentNumber() {
        if (documentNumber != null) {
            return documentNumber.trim();
        }
        return null;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    @TableHeader(name = "Документ_Дата", width = -80, sequence = 20)
    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @TableHeader(name = "Период действия_Начало", width = -80, sequence = 80)
    public Date getValidityBegin() {
        return validityBegin;
    }

    public void setValidityBegin(Date validityBegin) {
        this.validityBegin = validityBegin;
    }

    @TableHeader(name = "Период действия_Окончание", width = -80, sequence = 85)
    public Date getValidityEnd() {
        return validityEnd;
    }

    public void setValidityEnd(Date validityEnd) {
        this.validityEnd = validityEnd;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @TableHeader(name = "Документ_Примечание", sequence = 30)
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @TableHeader(name = "Состояние документа", width = -100, sequence = 200)
    @Transient
    public String getDocumentStatus() {
        if (status == 0) {
            return "Закрыт";
        } else {
            return "Формируется";
        }
    }

    public String getDocumentInformation() {
        return getDocumentNumber() + " от " + DateUtils.getNormalDateFormat(getDocumentDate());
    }

    public String getArticles() {
        if(articles!=null) {
            return articles.trim();
        }
        return "";
    }

    public void setArticles(String articles) {
        this.articles = articles;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public void setDocumentInformation(String documentInformation) {
        this.documentInformation = documentInformation;
    }
}
