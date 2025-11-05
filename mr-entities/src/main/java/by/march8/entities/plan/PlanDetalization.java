package by.march8.entities.plan;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * Created by dpliushchai on 22.12.2014.
 */
@Entity
@Table(name = "PLAN_ANALYZ_DETAL")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_POSTGRESQL)
public class PlanDetalization extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "HEIGHT")
    @TableHeader(name = "Рост", width = 40, sequence = 6)
    private int height;

    @Column(name = "SIZE")
    @TableHeader(name = "Размер", width = 60, sequence = 5)
    private int size;

    @Column(name = "CLOSE")

    private int close;

    @Column(name = "PLAN")

    private int plan;

    @Column(name = "cost_1_sort")
    @TableHeader(name = "Цена 1 сорт", width = 80, sequence = 7)
    private int cost1sort;

  //  @Column(name = "cost_2_sort")
  //  @TableHeader(name = "Цена 2 сорт", width = 100, sequence = 5)
   // private int cost2sort;

 //   @Column(name = "cost_3_sort")
 //   @TableHeader(name = "Цена 3 сорт", width = 100, sequence = 5)
  //  private int cost3sort;

   // @Column(name = "rented_warehouse_1_sort")
  //  @TableHeader(name = "Сдано на склад 1 сорт", width = 200, sequence = 6)
  //  private int rented_warehouse_1_sort;

  //  @Column(name = "rented_warehouse_2_sort")
  //  @TableHeader(name = "Сдано на склад 2 сорт", width = 200, sequence = 6)
  //  private int rented_warehouse_2_sort;

 //   @Column(name = "rented_warehouse_3_sort")
   // @TableHeader(name = "Сдано на склад 3 сорт", width = 200, sequence = 6)
  //  private int rented_warehouse_3_sort;

    @Column(name = "rented_warehouse_sum")

    private int rented_warehouse_sum;


    @Column(name = "ISSUED_TAILORING")

    private int issued_tailoring;



    @Column(name = "rented_warehouse_contractors_sum")
    private int rented_warehouse_contractors_sum;

    @OneToOne
    @JoinColumn(name = "ID_PLAN_NEW")
    private PlanItem planItem;




    @TableHeader(name = "Модель", width = 70, sequence = 2)
    public int getPlanItemModel() {
        return getPlanItem().getModel();
    }

    @TableHeader(name = "Артикул", width = 90, sequence = 3)
    public String getPlanItemArticle() {
        return getPlanItem().getArticle();
    }

    @TableHeader(name = "Артикул полотна", width = 320, sequence = 4)
    public String getPlanItemArticleCanvas() {
        return getPlanItem().getArticle_canvas();
    }

    @TableHeader(name = "Ассортимент", width = 220, sequence = 1)
    public String getPlanItemAssortment() {
        return getPlanItem().getAssortmentName();
    }


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @TableHeader(name = "План", width = 50, sequence = 7)
    public int getPlan() {
        return plan*planItem.getFactor();
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    public PlanItem getPlanItem() {
        return planItem;
    }

    public void setPlanItem(PlanItem planItem) {
        this.planItem = planItem;
    }

    @TableHeader(name = "Закроено", width = 60, sequence = 8)
    public int getClose() {
        return close *planItem.getFactor();
    }

    public void setClose(int close) {
        this.close = close;
    }

    public int getCost1sort() {
        return cost1sort;
    }

    public void setCost1sort(int cost1sort) {
        this.cost1sort = cost1sort;
    }



    @TableHeader(name = "Сдано на склад ", width = 150, sequence = 9)
    public int getRented_warehouse_sum() {
        return rented_warehouse_sum*planItem.getFactor();
    }

    public void setRented_warehouse_sum(int rented_warehouse_sum) {
        this.rented_warehouse_sum = rented_warehouse_sum;
    }

    @TableHeader(name = "Сдано на склад от подрядчиков ", width = 200, sequence = 10)
    public int getRented_warehouse_contractors_sum() {
        return rented_warehouse_contractors_sum*planItem.getFactor();
    }

    public void setRented_warehouse_contractors_sum(int rented_warehouse_contractors_sum) {
        this.rented_warehouse_contractors_sum = rented_warehouse_contractors_sum;
    }

    @TableHeader(name = "Выдано на пошив ", width = 170, sequence = 8)
    public int getIssued_tailoring() {
        return issued_tailoring*planItem.getFactor();
    }

    public void setIssued_tailoring(int issued_tailoring) {
        this.issued_tailoring = issued_tailoring;
    }
}