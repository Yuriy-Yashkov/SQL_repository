package by.march8.entities.planning;

import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;


@NamedQueries({
        @NamedQuery(name = "ProductionPlanningDetailComponentView.findByDetailItemId",
                query = "SELECT item FROM ProductionPlanningDetailComponentView item WHERE item.itemId = :itemId ORDER BY item.name")
})


/**
 * @author Andy 28.11.2018 - 13:40.
 */
@Entity
@Table(name = "VIEW_PLANNING_DETAIL_COMPONENT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ProductionPlanningDetailComponentView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @TableHeader(name = "ID", width = -50, sequence = 100)
    private int id;

    @Column(name = "ITEM_ID")
    @TableHeader(name = "IID", width = -80, sequence = 99)
    private int itemId;

    @Column(name = "COMPONENT_NAME")
    private String name;

    @Column(name = "RATE")
    @TableHeader(name = "Расход компонента_Единица", width = -100, sequence = 20)
    private Double rate;

    @Column(name = "USE_VALUE")
    @TableHeader(name = "Расход компонента_Всего", width = -100, sequence = 30)
    private Double useValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int productId) {
        this.itemId = productId;
    }

    @TableHeader(name = "Наименование компонента", sequence = 10)
    public String getName() {
        if (name != null) {
            return name.trim();
        } else {
            return "";
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getUseValue() {
        return useValue;
    }

    public void setUseValue(double useValue) {
        this.useValue = useValue;
    }
}
