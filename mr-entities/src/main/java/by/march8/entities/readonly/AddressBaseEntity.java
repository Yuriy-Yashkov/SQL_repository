package by.march8.entities.readonly;

import by.march8.api.BaseEntity;

/**
 * @ author Andy 23.10.2015.
 */

public class AddressBaseEntity extends BaseEntity {

    private int id;
    private String fullName;

    public AddressBaseEntity(final int id, final String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
                return fullName.trim();
    }

}
