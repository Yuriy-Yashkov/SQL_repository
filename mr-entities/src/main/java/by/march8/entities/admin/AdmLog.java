package by.march8.entities.admin;

import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * Активность аккаунта
 * Created by Andy on 23.10.2014.
 */
@Entity
@Table(name="ADM_LOG")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class AdmLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "ADM_USER_INFORMATION_ID")
    private int user;
    @Column(name = "USER_IP")
    private String userIp;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SESSION_START")
    private Date sessionStart;
    @Column(name = "SESSION_END")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sessionEnd;
    @Column(name = "NOTE")
    private String note;


    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
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
        this.note = "";
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

    public int getUser() {
        return user;
    }

    public void setUser(final int user) {
        this.user = user;
    }
}