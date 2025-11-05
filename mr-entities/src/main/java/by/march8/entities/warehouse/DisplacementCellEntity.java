package by.march8.entities.warehouse;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Andy 10.08.2016.
 */
@Entity
@Table(name="vnperem1")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class DisplacementCellEntity extends BaseEntity {

    @Id
    @Column(name = "item_id")
    private int id ;

    @Column(name = "ucenka3s")
    private double reduce3Grade ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public double getReduce3Grade() {
        return reduce3Grade;
    }

    public void setReduce3Grade(final double reduce3Grade) {
        this.reduce3Grade = reduce3Grade;
    }
}
