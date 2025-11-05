package by.march8.entities.seamstress;

import by.march8.api.MarchDataSourceEnum;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;

import javax.persistence.*;
import java.util.Date;

@NamedQueries({
        @NamedQuery(name = "SeamstressMapDocument.findByPeriod",
                query = "SELECT items FROM SeamstressMapDocument items WHERE items.documentDate between :date_start and :date_end " +
                        "order by items.documentNumber")
})


/**
 * @Andy tmp on 13.12.2021 11:45
 */
@Entity
@Table(name = "seamstress_production_map")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_POSTGRESQL)
public class SeamstressMapDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @TableHeader(name = "ID", sequence = 10)
    private int id;
    @Column(name = "DATE")
    @TableHeader(name = "ДАТА", sequence = 20)
    @Temporal(TemporalType.TIMESTAMP)
    private Date documentDate;
    @Column(name = "NUMBER")
    @TableHeader(name = "№ документа", sequence = 30)
    private int documentNumber;
    @Column(name = "TAB_NUMBER")
    @TableHeader(name = "Таб. №", sequence = 40)
    private int tabNumber;
    @Column(name = "EMPLOYEE_NAME")
    @TableHeader(name = "Ф.И.О.", sequence = 50)
    private String employeeName;
    @Column(name = "BRIGADE_ID")
    @TableHeader(name = "Бригада", sequence = 60)
    private int brigadeId;
    @Column(name = "DEPARTMENT_ID")
    @TableHeader(name = "Цех", sequence = 70)
    private int departmentId;
    @Column(name = "WORK_TIME_BEGIN")
    @Temporal(TemporalType.TIMESTAMP)
    @TableHeader(name = "ДАТА НАЧАЛА", sequence = 80)
    private Date workTimeBegin;
    @Column(name = "WORK_TIME_END")
    @TableHeader(name = "ДАТА ОКОНЧАНИЯ", sequence = 90)
    @Temporal(TemporalType.TIMESTAMP)
    private Date workTimeEnd;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    public int getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(int documentNumber) {
        this.documentNumber = documentNumber;
    }

    public int getTabNumber() {
        return tabNumber;
    }

    public void setTabNumber(int tabNumber) {
        this.tabNumber = tabNumber;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getBrigadeId() {
        return brigadeId;
    }

    public void setBrigadeId(int brigadeId) {
        this.brigadeId = brigadeId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public Date getWorkTimeBegin() {
        return workTimeBegin;
    }

    public void setWorkTimeBegin(Date workTimeBegin) {
        this.workTimeBegin = workTimeBegin;
    }

    public Date getWorkTimeEnd() {
        return workTimeEnd;
    }

    public void setWorkTimeEnd(Date workTimeEnd) {
        this.workTimeEnd = workTimeEnd;
    }
}
