package by.march8.entities.storage;

import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 03.11.2018 - 10:10.
 */

@NamedQueries({
        @NamedQuery(name = "UniqueIdentifiers.findByKey",
                query = "SELECT items FROM UniqueIdentifiers items WHERE items.name =:key ")
})

@Entity
@Table(name = "unictabl")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class UniqueIdentifiers {

    @Transient
    public static final String STORAGE_LOCATIONS = "places1" ;

    @Id
    @Column(name = "NAME")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String name ;

    @Column(name="DOPNAME")
    private String additionName ;

    @Column(name="NOMER")
    private long currentNumber;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdditionName() {
        return additionName;
    }

    public void setAdditionName(String additionName) {
        this.additionName = additionName;
    }

    public long getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(long currentNumber) {
        this.currentNumber = currentNumber;
    }

}
