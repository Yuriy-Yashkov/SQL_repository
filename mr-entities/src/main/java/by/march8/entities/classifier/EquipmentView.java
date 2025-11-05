package by.march8.entities.classifier;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @author Andy 22.11.2018 - 12:08.
 */
@Entity
@Table(name="REF_EQUIPMENT")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class EquipmentView  extends BaseEntity {
    /** The id. */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    /** The name. */
    @Column(name="NAME")
    @TableHeader(name = "Наименование оборудования_Полное", sequence = 0, width = 200)
    private String name;

    /** The name. */
    @Column(name="SHORT_NAME")
    @TableHeader(name = "Наименование оборудования_Краткое", sequence = 10, width = 200)
    private String shortName;

    /** The diametr. */
    @Column(name="DIAMETR")
    //@TableHeader(name = "Диаметр",  sequence = 1)
    private String diametr;

    /** The needle count. */
    @Column(name="NEEDLE_COUNT")
    //@TableHeader(name = "Кол-во игл",  sequence = 2)
    private String needleCount;

    /** The equipment class. */
    @Column(name="EQUIPMENT_CLASS")
    //@TableHeader(name = "Класс машины",  sequence = 3)
    private int equipmentClass;

    /** The system count. */
    @Column(name="SYSTEM_COUNT")
    //@TableHeader(name = "Кол-во систем",  sequence = 4)
    private String systemCount;

    @Column(name="TYPE")
    private int type ;

    @Column(name = "AMOUNT")
    @TableHeader(name = "Кол-во", width = -80, sequence = 40)
    private int amount ;

    public int getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name.trim();
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the diametr.
     *
     * @return the diametr
     */
    public String getDiametr() {
        return diametr.trim();
    }

    /**
     * Sets the diametr.
     *
     * @param diametr the new diametr
     */
    public void setDiametr(String diametr) {
        this.diametr = diametr;
    }

    /**
     * Gets the needle count.
     *
     * @return the needle count
     */
    public String getNeedleCount() {
        return needleCount.trim();
    }

    /**
     * Sets the needle count.
     *
     * @param needleCount the new needle count
     */
    public void setNeedleCount(String needleCount) {
        this.needleCount = needleCount;
    }

    /**
     * Gets the equipe class.
     *
     * @return the equipe class
     */
    public int getEquipmentClass() {
        return equipmentClass;
    }

    /**
     * Sets the equipe class.
     *
     * @param equipeClass the new equipe class
     */
    public void setEquipmentClass(int equipeClass) {
        this.equipmentClass = equipeClass;
    }

    /**
     * Gets the system count.
     *
     * @return the system count
     */
    public String getSystemCount() {
        return systemCount.trim();
    }

    /**
     * Sets the system count.
     *
     * @param systemCount the new system count
     */
    public void setSystemCount(String systemCount) {
        this.systemCount = systemCount;
    }

    @Override
    public String toString() {
        return name.trim()+" ("+id+")" ;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
