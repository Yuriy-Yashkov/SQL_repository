package by.march8.entities.plan;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;

/**
 * Created by dpliushchai on 26.11.2014.
 * Сущность месяцев ( 1 - январь и т.д)
 */
@Entity
@Table(name = "PLAN_ANALYZ_ARRAY")
@NamedQuery(name = "PLAN_ANALYZ_ARRAY.findAll", query = "SELECT c FROM PlanIdItem c")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_POSTGRESQL)
public class PlanIdItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "NAME")
    @TableHeader(name = "Название", width = 100, sequence = 0)
    private String Name;


    // @Column(name = "VISIBLE")
    //private boolean visible;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString(){
        return getName();
    }
}
