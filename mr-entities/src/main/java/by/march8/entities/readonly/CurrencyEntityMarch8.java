package by.march8.entities.readonly;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = "CurrencyEntityMarch8.findAll",
                query = "SELECT currency FROM CurrencyEntityMarch8 currency  " +
                        "order by id")
})


@Entity
@Table(name = "valuta")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class CurrencyEntityMarch8 extends BaseEntity  {
    @Id
    @Column(name = "kod")
    @TableHeader(name = "Код валюты",width = -50,sequence = 0)
    private int id ;

    @Column(name = "naim")
    @TableHeader(name = "Наименование валюты",sequence = 1)
    private String name ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        if(name!=null) {
            return name.trim();
        }
        return null;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
