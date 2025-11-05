package by.march8.entities.planning;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 28.11.2018 - 9:49.
 */
@Entity
@Table(name="MOD_PLANNING_DOCUMENT_DETAIL_COMPONENT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class PlanningItemComponent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id ;

    @ManyToOne()
    @JoinColumn(name = "REF_PLANNING_DOCUMENT_DETAIL_ID")
    private ProductionPlanningItem product ;

    @ManyToOne()
    @JoinColumn(name = "REF_PLANNING_COMPOSITION_ID")
    private ProductionPlanningComposition component ;

    @Column(name="RATE")
    private double rate ;

    @Column(name = "USE_VALUE" )
    private double useValue ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public ProductionPlanningItem getProduct() {
        return product;
    }

    public void setProduct(ProductionPlanningItem product) {
        this.product = product;
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

    public ProductionPlanningComposition getComponent() {
        return component;
    }

    public void setComponent(ProductionPlanningComposition component) {
        this.component = component;
    }
}
