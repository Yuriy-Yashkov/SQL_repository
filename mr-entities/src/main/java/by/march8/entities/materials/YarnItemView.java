package by.march8.entities.materials;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 16.11.2018 - 9:03.
 */
@Entity
@Table(name="VIEW_MAN_YARN")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class YarnItemView extends BaseEntity{

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @Column(name = "NAME")
    private String name ;

    @Column(name="CODE")
    private String code ;

    @Column(name = "CATEGORY_NAME")
    private String category ;

    @Column(name="NOTE")
    private String note ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @TableHeader(name = "Наименование", width = 100, sequence = 1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @TableHeader(name = "Шифр", width = 100, sequence = 0)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @TableHeader(name = "Категория", width = 100, sequence = 2)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @TableHeader(name = "Примечание", width = 100, sequence = 3, weight = 0)
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
