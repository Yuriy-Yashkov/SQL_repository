package by.march8.entities.admin;


import javax.persistence.*;
import java.util.Calendar;

/**
 * Активность учетной записи
 * Created by Andy on 23.10.2014.
 */

@Entity
@Table(name="ADM_ENTITY_LOG")
public class EntityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;
    @Column(name="ENTITY_ID")
    private int entityId ;
    @Column(name = "ENTITY_NAME")
    private String entityName ;
    @Column(name = "LOG_MESSAGE")
    private String logMessage ;
    @Column(name = "LOG_TYPE")
    private int logType ;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOG_DATE")
    private Calendar logDate ;
    @Column(name="USER_IP")
    private String userIp ;
    @Column(name = "NOTE")
    private String note ;


    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(final int entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(final String entityName) {
        this.entityName = entityName;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(final String logMessage) {
        this.logMessage = logMessage;
    }

    public int getLogType() {
        return logType;
    }

    public void setLogType(final int logType) {
        this.logType = logType;
    }

    public Calendar getLogDate() {
        return logDate;
    }

    public void setLogDate(final Calendar logDate) {
        this.logDate = logDate;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(final String userIp) {
        this.userIp = userIp;
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }
}
