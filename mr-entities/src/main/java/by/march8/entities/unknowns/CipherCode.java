package by.march8.entities.unknowns;

import by.march8.api.BaseEntity;
import by.march8.api.TableHeader;

/**
 *
 * Created by Andy on 19.03.2015.
 */
public class CipherCode extends BaseEntity {
    private int id ;
    private String name ;

    public CipherCode(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    @TableHeader(name = "ID",width = -50, sequence = 0)
    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    @TableHeader(name = "ШТРИХ-КОД",sequence = 1)
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
