package by.march8.entities.materials;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 16.04.2015.
 */
@Entity
@Table(name="VIEW_MOD_CANVAS")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class VCanvasItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableHeader(name = "ID",sequence = 0)
    private int id;
    @TableHeader(name = "Шифр полотна", sequence = 1)
    private int cipher ;
    @TableHeader(name = "Артикул", sequence = 2)
    private String article ;
    @TableHeader(name = "Ширина", sequence = 3)
    private int width ;
    @TableHeader(name = "Вес", sequence = 4)
    private int weight ;
    @TableHeader(name = "Модификатор", sequence = 5)
    private String abbreviation ;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getCipher() {
        return cipher;
    }

    public void setCipher(final int cipher) {
        this.cipher = cipher;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(final String article) {
        this.article = article;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(final int weight) {
        this.weight = weight;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(final String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public String toString() {
        return article;
    }
}
