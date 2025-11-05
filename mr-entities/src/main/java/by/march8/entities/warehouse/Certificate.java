package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 17.05.2016.
 */
@Entity
@Table(name = "nsi_prop")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class Certificate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private int id;

    /**
     * Шифр артикула, частичный или полный
     */
    @Column(name="sar")
    private String articleCode;
    /**
     * Группа сертификатов (0 или 4)
     */
    @Column(name="sgpr")
    private int group;
    /**
     * Наименование документа-сертификата
     */
    @Column(name = "naim")
    private String name;

    /**
     * реквизиты документа-сертификата
     */
    @Column(name = "znach")
    private String value;

    /**
     * Строковое наименование типа документа сертификата
     */
    @Column(name = "tip")
    private String typeName ;

    /**
     * Код типа документа-сертификата
     */
    @Column(name = "vid")
    private int type ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(final String articleCode) {
        this.articleCode = articleCode;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(final int group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getTypeName() {
        if(typeName!=null){
           return typeName.trim();
        }else {
            return "";
        }
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    public int getType() {
        return type;
    }

    public void setType(final int type) {
        this.type = type;
    }

    public String toString(){
        return getTypeName().trim()+": "+getValue();
    }
}
