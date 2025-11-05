package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * Структура торговой надбавки
 *
 * @author Andy 01.06.2016.
 */
@Entity
@Table(name = "_torg_nadbavka")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ReductionItem extends BaseEntity {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "klient_id")
    private int contractorId;

    @Column(name = "art")
    private String articleMask;

    @Column(name = "sort")
    private int grade;

    @Column(name = "tn")
    private int valueAllowance;

    @Transient
    private String mask ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public int getContractorId() {
        return contractorId;
    }

    public void setContractorId(final int contarctorId) {
        this.contractorId = contarctorId;
    }

    public String getArticleMask() {
        return articleMask;
    }

    public void setArticleMask(final String articleMask) {
        this.articleMask = articleMask;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(final int grade) {
        this.grade = grade;
    }

    public int getValueAllowance() {
        return valueAllowance;
    }

    public void setValueAllowance(final int valueAllowance) {
        this.valueAllowance = valueAllowance;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(final String mask) {
        this.mask = mask;
    }
}
