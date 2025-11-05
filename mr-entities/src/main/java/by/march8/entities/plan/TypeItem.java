package by.march8.entities.plan;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * Created by dpliushchai on 25.11.2014.
 * Сущность Тип Продукции
 * Тип 1 - муж 2 - жен 3 - дет 0 - нет
 */
@Entity
@Table(name = "PLAN_ANALYZ_TYPE")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_POSTGRESQL)
public class TypeItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "NAME")
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
