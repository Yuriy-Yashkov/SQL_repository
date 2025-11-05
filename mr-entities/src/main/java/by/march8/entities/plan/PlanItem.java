package by.march8.entities.plan;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;

/**
 * Created by dpliushchai on 19.11.2014.
 * Главная сущность программы. Отображает данные по SAR .
        */
@Entity
@Table(name = "PLAN_ANALYZ_NEW")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_POSTGRESQL)
public class PlanItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ASSORTMENT_NAME")
    @TableHeader(name = "Ассортимент", width = 200, sequence = 20)
    private String assortmentName;

    @Column(name = "MODEL")
    @TableHeader(name = "Модель", width = 60, sequence = 30)
    private int model;

    @Column(name = "NOVETLY")
    @TableHeader(name = "Новинка", width = 40, sequence = 40)
    private String novetly;





    @Column(name = "COMPOSITION_CANVAS")
    @TableHeader(name = "Состав полотна", width = 0, sequence = 80)
    private String compositionCanvas;

    @Column(name = "ARTICLE_CANVAS")
    @TableHeader(name = "Артикул_полотна", width = 70, sequence = 62)
    private String article_canvas;




    @Column(name = "COLOR_CANVAS")
    @TableHeader(name = "Цвет полотна", width = 0, sequence = 90)
    private String colorCanvas;

    @Column(name = "CARRYOVERS")
    @TableHeader(name = "Переход. ост", width = 83, sequence = 100)
    private int carryovers;

    @Column(name = "ISSUED_TINCTORIAL")
    @TableHeader(name = "Выд." + "\n" +
            " в " +
            "кр", width = 73, sequence = 100)
    private int issuedTinctorial;


    /**
     *
     * @return  % выполнения Выдано В Красильный Цех
     */
    @TableHeader(name = "% вып", width = 0, sequence = 100)
    public int getIssuedTinctorialPercent() {
        if(getPlan()!=0){

            return (int)(100* ((getIssuedTinctorial()*1.0/getPlan())));
        }
        return 0;
    }

    @Column(name = "ISSUED_CLOSE")
    @TableHeader(name = "Выд. на закр.", width = 80, sequence = 110)
    private int issuedClose;


    /**
     *
     * @return  % выполнения Выдано На Закрой
     */
    @TableHeader(name = "% вып", width = 0, sequence = 110)
    public int getIssuedClosePercent() {
        if(getPlan()!=0){

            return (int)(100* ((getIssuedClose()*1.0/getPlan())));
        }
        return 0;
    }

    //@TableHeader(name = "ФООООТТТООООО", width = 200, sequence = 11)
   // protected Icon photo;


    @Column(name = "SHIPPED_CONTRACTORS")
    @TableHeader(name = "Отгр. подр", width = 70, sequence = 120)
    private int shippedContractors;

    @Column(name = "ISSUED_TAILORING")

    private int issuedTailoring;


    //@Column(name = "upack_1_sort")
    //  @TableHeader(name = "На упаковке 1 сорт", width = 200, sequence = 15)
    private int upack_1_sort;

    // @Column(name = "upack_2_sort")
    // @TableHeader(name = "На упаковке 2 сорт", width = 200, sequence = 15)
    private int upack_2_sort;

    // @Column(name = "upack_3_sort")
    // @TableHeader(name = "На упаковке 3 сорт", width = 200, sequence = 15)
    private int upack_3_sort;

    @Column(name = "upack_sum")
   // @TableHeader(name = "Выд. на уп", width = 80, sequence = 16)
    private int upack_sum;


    @Column(name = "PLAN")

    private int plan;

    @Column(name = "DEVIATION")
    private int deviation;

    @Column(name = "DEVIATION_PERCENT")
    @TableHeader(name = "% план", width = 0, sequence = 210)
    private double deviation_percent;

    @Column(name = "IMAGE_BYTE")
    @TableHeader(name = "Картинка", width = 100, sequence = 50)
    private byte[]  image_byte;

    @Column(name = "ARTICLE")
    @TableHeader(name = "Артикул", width = 90, sequence = 58)
    private String article;


    @TableHeader(name = "План ", width = 60, sequence = 59)
    public int getPlanCopy() {
        return getPlan();
    }

    @Column(name = "FACTOR")
    @TableHeader(name = "Кф", width = 0, sequence = 68)
    private int factor;


    @Column(name = "NOTE")
    @TableHeader(name = "Примечание", width = 40, sequence = 62)
    private String note;

    @Column(name = "CLOSE")

    private int close;


    @Column(name = "deviation_close_percent")

    private double deviation_close_percent;

    @Column(name = "RENTED_WAREHOUSE")


    private int rented_warehouse;

    @Column(name = "RENTED_WAREHOUSE_CONTRACTORS")

    private int rented_warehouse_contractors;


    @TableHeader(name = "image", width = 0, sequence = 1000)
    private String image;


    private int number;
    //@Column(name = "KOD")
    //@TableHeader(name = "Шифр артикула", width = 200, sequence = 1 )
    // private int kod;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ID_TYPE")
    private TypeItem type;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ID_PLAN")
    private PlanIdItem planId;


    @TableHeader(name = "Вид", width = 40, sequence = 1)
    public String getTypeName() {
        return type.getName();
    }


    @Column(name = "SIZE")
    @TableHeader(name = "Размерная шкала", width = 0, sequence = 990)
    private String size;


    @Column(name = "COST")
    @TableHeader(name = "Цена", width = 0, sequence = 990)
    private String cost;


    //@TableHeader(name = "Вид111111111111", width = 100, sequence = 0 )
    // private String getTypeItem() {return  type.getName(); }


    // @Column(name = "VISIBLE")
    //private boolean visible;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }


    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    public boolean isVisible() {
        //  return visible;
        return true;
    }

    public void setVisible(final boolean visible) {
        // this.visible = visible;
    }

    @Override
    public String toString() {
        return "";
    }


    public String getAssortmentName() {
        return assortmentName;
    }

    public void setAssortmentName(String assortmentName) {
        this.assortmentName = assortmentName;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public String getNovetly() {
        return novetly;
    }

    public void setNovetly(String novetly) {
        this.novetly = novetly;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCompositionCanvas() {
        return compositionCanvas;
    }

    public void setCompositionCanvas(String compositionCanvas) {
        this.compositionCanvas = compositionCanvas;
    }

    public String getArticle_canvas() {
        return article_canvas;
    }

    public void setArticle_canvas(String article_canvas) {
        this.article_canvas = article_canvas;
    }



    public String getColorCanvas() {
        return colorCanvas;
    }

    public void setColorCanvas(String colorCanvas) {
        this.colorCanvas = colorCanvas;
    }


    public int getIssuedTinctorial() {
        return issuedTinctorial;
    }

    public void setIssuedTinctorial(int issuedTinctorial) {
        this.issuedTinctorial = issuedTinctorial;
    }

    public int getIssuedClose() {
        return issuedClose;
    }

    public void setIssuedClose(int issuedClose) {
        this.issuedClose = issuedClose;
    }


    @TableHeader(name = "Закроено", width = 60, sequence = 130)
    public int getClose() {
        return close*factor;
    }

    public void setClose(int close) {
        this.close = close;
    }

    public int getShippedContractors() {
        return shippedContractors;
    }

    public void setShippedContractors(int shippedContractors) {
        this.shippedContractors = shippedContractors;
    }

    @TableHeader(name = "Выд. на пошив", width = 87, sequence = 131)
    public int getIssuedTailoring() {
        return issuedTailoring;
    }

    public void setIssuedTailoring(int issuedTailoring) {
        this.issuedTailoring = issuedTailoring;
    }


    @TableHeader(name = "План ", width = 60, sequence = 190)
    public int getPlan() {
        return plan*factor;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    @TableHeader(name = "Откл. от плана", width = 90, sequence = 200)
    public int getDeviation() {
        return plan-rented_warehouse-rented_warehouse_contractors;
    }

    public void setDeviation(int deviation) {
        this.deviation = deviation;
    }



    public TypeItem getType() {
        return type;
    }

    public void setType(TypeItem type) {
        this.type = type;
    }


    public PlanIdItem getPlanId() {
        return planId;
    }

    public void setPlanId(PlanIdItem planId) {
        this.planId = planId;
    }


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public int getUpack_1_sort() {
        return upack_1_sort;
    }

    public void setUpack_1_sort(int upack_1_sort) {
        this.upack_1_sort = upack_1_sort;
    }

    public int getUpack_2_sort() {
        return upack_2_sort;
    }

    public void setUpack_2_sort(int upack_2_sort) {
        this.upack_2_sort = upack_2_sort;
    }

    public int getUpack_3_sort() {
        return upack_3_sort;
    }

    public void setUpack_3_sort(int upack_3_sort) {
        this.upack_3_sort = upack_3_sort;
    }

    @TableHeader(name = "Выд. на уп", width = 0, sequence = 160)
    public int getUpack_sum() {
       // System.out.println("fffffffffffffffffff");
        return upack_sum*factor;
    }

    public void setUpack_sum(int upack_sum) {
        this.upack_sum = upack_sum;
    }

    @TableHeader(name = "Сдано склад ", width = 70, sequence = 170)
    public int getRented_warehouse() {
        return rented_warehouse*factor;
    }

    public void setRented_warehouse(int rented_warehouse) {
        this.rented_warehouse = rented_warehouse;
    }

    @TableHeader(name = "Сдано подр", width = 70, sequence = 180)
    public int getRented_warehouse_contractors() {
        return rented_warehouse_contractors*factor;
    }


    public void setRented_warehouse_contractors(int rented_warehouse_contractors) {
        this.rented_warehouse_contractors = rented_warehouse_contractors;
    }

    public double getDeviation_percent() {
        return deviation_percent;
    }

    public void setDeviation_percent(double deviation_percent) {
        this.deviation_percent = deviation_percent;
    }

    @TableHeader(name = "% закр", width = 0, sequence = 140)
    public double getDeviation_close_percent() {

        if(getPlan()!=0){

            return (int)(100* (((getClose() + getCarryovers())*1.0/getPlan())));
        }
        return 0;
    }

    public void setDeviation_close_percent(double deviation_close_percent) {
        this.deviation_close_percent = deviation_close_percent;
    }





    public byte[] getImage_byte() {
        return image_byte;
    }

    public void setImage_byte(byte[] image_byte) {
        this.image_byte = image_byte;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }


    public int getPlan1() {
        return plan;
    }


    public int getCarryovers() {
        return carryovers;
    }

    public void setCarryovers(int carryovers) {
        this.carryovers = carryovers;
    }

    public int getFactor() {
        return factor;
    }

    public void setFactor(int factor) {
        this.factor = factor;
    }
}


