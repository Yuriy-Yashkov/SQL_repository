package by.march8.entities.admin;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Entity
@Table(name = "ADM_FORMS_RIGHTS")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class UserFormRights extends BaseEntity {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserInformation user;

    @Getter
    @OneToOne
    @JoinColumn(name = "FORM_ID")
    private ApplicationForm form;

    @Column(name = "RIGHTS")
    private String rightsValue;

    public String getRightsValue() {
        if (rightsValue == null) {
            return "";
        } else {
            return rightsValue;
        }
    }

    @Override
    public String toString() {
        return "UserFormRights{" +
                "id=" + id +
                ", user=" + user +
                ", form=" + form +
                ", rightsValue='" + rightsValue + '\'' +
                '}';
    }
}
