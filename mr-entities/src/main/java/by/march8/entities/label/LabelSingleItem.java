package by.march8.entities.label;

import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = "LabelSingleItem.findByItemCode",
                query = "SELECT labelItem FROM LabelSingleItem labelItem WHERE labelItem.itemCode = :itemCode and labelItem.itemGrade = :itemGrade and " +
                        "labelItem.itemSize = :itemSize and labelItem.itemGrowth = :itemGrowth" +
                        " order by labelItem.id")
})


/**
 * @author Andy 05.11.2018 - 10:17.
 */
@Entity
@Table(name="label_one")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class LabelSingleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "barcode")
    private long id;

    @Column(name="naim")
    private String itemName ;

    @Column(name="nar")
    private String articleNumber ;

    @Column(name="fas")
    private int modelNumber ;

    @Column(name="rzm_marh")
    private int itemSize ;

    @Column(name="rst_marh")
    private int itemGrowth ;

    @Column(name="srt")
    private int itemGrade ;

    @Column(name = "rzm")
    private String itemPrintSize ;

    @Column(name="ncw")
    private String itemColor ;

    @Column(name = "eancode")
    private String eanCode ;

    @Column(name="printed")
    private int labelPrintedCount ;

    @Column(name="kod_izd")
    private int itemCode ;


    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @TableHeader(name = "Баркод", sequence = 10)
    public long getBarCode(){
        return id ;
    }

    @TableHeader(name = "Наименование", sequence = 20)
    public String getItemName() {
        if(itemName!=null) {
            return itemName.trim();
        }else{
            return "";
        }
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @TableHeader(name = "Артикул", sequence = 30)
    public String getArticleNumber() {
        if(articleNumber!=null) {
            return articleNumber.trim();
        }else{
            return "";
        }
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    @TableHeader(name = "Модель", sequence = 40)
    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(int modelNumber) {
        this.modelNumber = modelNumber;
    }

    @TableHeader(name = "Размер", sequence = 50)
    public int getItemSize() {
        return itemSize;
    }

    public void setItemSize(int itemSize) {
        this.itemSize = itemSize;
    }

    @TableHeader(name = "Рост", sequence = 60)
    public int getItemGrowth() {
        return itemGrowth;
    }

    public void setItemGrowth(int itemGrowth) {
        this.itemGrowth = itemGrowth;
    }

    @TableHeader(name = "Сорт", sequence = 45)
    public int getItemGrade() {
        return itemGrade;
    }

    public void setItemGrade(int itemGrade) {
        this.itemGrade = itemGrade;
    }
    @TableHeader(name = "Размер(этикетка)", sequence = 46)
    public String getItemPrintSize() {
        if(itemPrintSize!=null) {
            return itemPrintSize.trim();
        }else{
            return "";
        }
    }

    public void setItemPrintSize(String itemPrintSize) {
        this.itemPrintSize = itemPrintSize;
    }

    @TableHeader(name = "Цвет", sequence = 70)
    public String getItemColor() {
        if(itemColor!=null) {
            return itemColor.trim();
        }else{
            return "";
        }
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    @TableHeader(name = "EAN13 код", sequence = 80)
    public String getEanCode() {
        if(eanCode!=null) {
            return eanCode.trim();
        }else{
            return "";
        }
    }

    public void setEanCode(String eanCode) {
        this.eanCode = eanCode;
    }

    @TableHeader(name = "Отпечатано", sequence = 90)
    public int getLabelPrintedCount() {
        return labelPrintedCount;
    }

    public void setLabelPrintedCount(int labelPrintedCount) {
        this.labelPrintedCount = labelPrintedCount;
    }

    @TableHeader(name = "Код изделия", sequence = 10)
    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(int itemCode) {
        this.itemCode = itemCode;
    }
}
