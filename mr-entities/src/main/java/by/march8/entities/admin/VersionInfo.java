package by.march8.entities.admin;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * @ author Andy 21.10.2015.
 */
@Entity
@Table(name = "ADM_VERSION")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLSERVER)
public class VersionInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "VERSION")
    private String versionName ;
    @Column(name = "DETAILS")
    private String versionDetail ;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(final String versionName) {
        this.versionName = versionName;
    }

    public String getVersionDetail() {
        return versionDetail;
    }

    public void setVersionDetail(final String versionDetail) {
        this.versionDetail = versionDetail;
    }
}
