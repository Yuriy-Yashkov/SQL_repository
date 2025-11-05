package by.march8.entities.admin;

import javax.persistence.*;


@NamedQueries({


        @NamedQuery(name = "UserProperty.findAllByUserId",
                query = "SELECT property FROM UserProperty property WHERE property.userId = :userId ")
})

/**
 * @author Andy 24.03.2016.
 */
@Entity
@Table(name="ADM_USER_PROPERTIES")
public class UserProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "REF_USER_ID")
    private int userId ;
    @Column(name = "PROPERTY_KEY")
    private String key ;
    @Column(name = "PROPERTY_VALUE")
    private String value ;



    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(final int userId) {
        this.userId = userId;
    }
}
