package by.march8.ecs.application.modules.warehouse.external.shipping.model;

import by.march8.api.BaseEntity;
import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Andy 05.10.2018 - 7:06.
 */
@Entity
@Table(name = "otgruz2")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class EanCodeSaleDocumentItem extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "scan")
    private String barCode;
    @Column(name = "kod_str")
    private String barCodeId;

    @Column(name = "kod_izd")
    private int itemCode;

    @Column(name = "ncw")
    private String color;

    @Column(name = "eancode")
    private String eanCode;

    @Column(name = "tip")
    private int type;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(int itemCode) {
        this.itemCode = itemCode;
    }

    public String getColor() {
        if (color != null) {
            return color.trim();
        } else {
            return "";
        }
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEanCode() {
        if (eanCode != null) {
            return eanCode.trim();
        } else {
            return "";
        }

    }

    public void setEanCode(String eanCode) {
        this.eanCode = eanCode;
    }


    public String getBarCode() {
        if (barCode != null) {
            return barCode.trim();
        } else {
            return "0";
        }
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getBarCodeId() {
        if (barCodeId != null) {
            return barCodeId.trim();
        } else {
            return "0";
        }
    }

    public void setBarCodeId(String barCodeId) {
        this.barCodeId = barCodeId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
