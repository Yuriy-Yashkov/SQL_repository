package by.march8.entities.label;

import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;

@NamedQueries({
        @NamedQuery(name = "LabelPackItem.findBySingleLabelBarcode",
                query = "SELECT labelItem FROM LabelPackItem labelItem WHERE labelItem.barCodeSingle = :barCodeSingle" +
                        " order by labelItem.id")
})

/**
 * @author Andy 05.11.2018 - 11:40.
 */
@Entity
@Table(name="V_LABEL_PACK")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class LabelPackItem {

    @Id
    @Column(name="BARCODE_PACK")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;

    @Column(name="BARCODE_SINGLE")
    private long barCodeSingle ;

    @Column(name="LABEL_PACK_ID")
    private int labelPackId ;

    @Column(name="LABEL_SIZE_PRINT")
    private String labelSizePrint ;

    @Column(name="LABEL_PRINTED")
    private int labelPrintedCount ;

    @Column(name = "EANCODE")
    private String eanCode ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dataprint")
    private Date datePrint;

    @Column(name = "userprint")
    private String userPrint;

    @Column(name = "mashine")
    private String stationPrint;

    @TableHeader(name="Баркод_Упаковочный", sequence = 10)
    public long getBarCodePack(){
        return id ;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @TableHeader(name="Баркод_Штучный", sequence = 20)
    public long getBarCodeSingle() {
        return barCodeSingle;
    }

    public void setBarCodeSingle(long barCodeSingle) {
        this.barCodeSingle = barCodeSingle;
    }

    @TableHeader(name="Этикетка_Код", sequence = 30)
    public int getLabelPackId() {
        return labelPackId;
    }

    public void setLabelPackId(int labelPackId) {
        this.labelPackId = labelPackId;
    }

    @TableHeader(name="Этикетка_Размер", sequence = 40)
    public String getLabelSizePrint() {
        return labelSizePrint;
    }

    public void setLabelSizePrint(String labelSizePrint) {
        this.labelSizePrint = labelSizePrint;
    }

    @TableHeader(name="Этикетка_ШТ.в уп.", sequence = 35)
    public int getLabelPrintedCount() {
        return labelPrintedCount;
    }

    public void setLabelPrintedCount(int labelPrintedCount) {
        this.labelPrintedCount = labelPrintedCount;
    }

    @TableHeader(name="Этикетка_EAN13 код", sequence = 60)
    public String getEanCode() {
        return eanCode;
    }

    public void setEanCode(String eanCode) {
        this.eanCode = eanCode;
    }

    @TableHeader(name="Печать_Дата", sequence = 100)
    public Date getDatePrint() {
        return datePrint;
    }
    @TableHeader(name="Печать_Оператор", sequence = 110)
    public void setDatePrint(Date datePrint) {
        this.datePrint = datePrint;
    }
    @TableHeader(name="Печать_Станция", sequence = 120)
    public String getUserPrint() {
        return userPrint;
    }

    public void setUserPrint(String userPrint) {
        this.userPrint = userPrint;
    }

    public String getStationPrint() {
        return stationPrint;
    }

    public void setStationPrint(String stationPrint) {
        this.stationPrint = stationPrint;
    }
}
