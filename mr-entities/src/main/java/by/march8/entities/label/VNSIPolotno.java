package by.march8.entities.label;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by suvarov on 17.12.14.
 */
@Entity
@Table(name = "VIEW_NSI_POLOTNO")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class VNSIPolotno extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @javax.persistence.Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "NAME_1")
    @TableHeader(name = "СОСТАВ 1", width = 200, sequence = 0)
    private String firstName;
    @Column(name = "NAME_2")
    @TableHeader(name = "СОСТАВ 2", width = 200, sequence = 1)
    private String secondName;
    @Column(name = "NAME_3")
    @TableHeader(name = "СОСТАВ 3", width = 200, sequence = 2)
    private String thirdName;
    @Column(name ="CODE")
    @TableHeader(name="КОД", width = 200,sequence = 3)
    private String code;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        if ((secondName==null)||(secondName.isEmpty()))
            return "";
        else
            return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getThirdName() {
        if ((thirdName==null) || thirdName.isEmpty())
            return "";
        else
            return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String toString(){
        String firstName="";
        String secondName="";
        String thirdName="";
        if (!(this.firstName==null || this.firstName.isEmpty())) firstName=getFirstName();
        if (!(this.secondName==null || this.secondName.isEmpty())) secondName=getSecondName();
        if (!(this.thirdName==null || this.thirdName.isEmpty())) thirdName=getThirdName();
        return getCode()+" "+firstName+" "+secondName+" "+thirdName;
    }
}
