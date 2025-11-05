package by.march8.entities.admin;

import by.march8.api.TableHeader;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Andy 29.10.2014.
 */
@Entity
@Table(name="V_ADM_LOG")
public class VAdmLog {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
   // @TableHeader(name = "ID", sequence = 0)
    private int id ;
    @Column(name="NAME")
    @TableHeader(name = "Сотрудник_Инициалы", sequence = 1)
    private String name ;
    @Column(name ="POSITION")
   // @TableHeader(name = "Сотрудник_Должность", sequence = 2)
    private String position ;
    @Column(name ="COMPANY_DEPARTMENT")
    //@TableHeader(name = "Сотрудник_Предприятие", sequence = 3)
    private String companyDepartment ;
    @Column(name ="USER_INFORMATION_ID")
    private Integer uid ;
    @Column(name ="LOGIN")
    @TableHeader(name = "Сессия_Пользователь", sequence = 5)
    private String login ;
    @Column(name ="SESSION_START")
    @Temporal(TemporalType.TIMESTAMP)
    @TableHeader(name = "Сессия_Сессия открыта", sequence = 5)
    private Date sessionStart ;
    @Column(name ="SESSION_END")
    @Temporal(TemporalType.TIMESTAMP)
    @TableHeader(name = "Сессия_Сессия закрыта", sequence = 7)
    private Date sessionEnd ;
    @Column(name ="IP")
   // @TableHeader(name = "Сессия_IP адрес", sequence = 8)
    private String ip ;
    @Column(name ="OPERATION")
    @TableHeader(name = "Внесение изменений_Операция", sequence = 9)
    private String operation ;
    @Column(name ="ENTITY_NAME")
    @TableHeader(name = "Внесение изменений_Имя таблицы", sequence = 10)
    private String tableName ;
    @Column(name ="ENTITY_ID")
   // @TableHeader(name = "Внесение изменений_ID записи", sequence = 11)
    private int entityId ;
    @Column(name="LOG_MESSAGE")
    @TableHeader(name = "Внесение изменений_Детали изменения", sequence = 12)
    private String logMessage ;
    @Column(name ="LOG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @TableHeader(name = "Внесение изменений_Дата изменения", sequence = 13)
    private Date logDate ;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(final String position) {
        this.position = position;
    }

    public String getCompanyDepartment() {
        return companyDepartment;
    }

    public void setCompanyDepartment(final String companyDepartment) {
        this.companyDepartment = companyDepartment;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(final Integer uid) {
            this.uid = uid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public Date getSessionStart() {
        return sessionStart;
    }

    public void setSessionStart(final Date sessionStart) {
        this.sessionStart = sessionStart;
    }

    public Date getSessionEnd() {
        return sessionEnd;
    }

    public void setSessionEnd(final Date sessionEnd) {
        this.sessionEnd = sessionEnd;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(final String ip) {
        this.ip = ip;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(final String operation) {
        this.operation = operation;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(final int entityId) {
        this.entityId = entityId;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(final String logMessage) {
        this.logMessage = logMessage;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(final Date logDate) {
        this.logDate = logDate;
    }
}
