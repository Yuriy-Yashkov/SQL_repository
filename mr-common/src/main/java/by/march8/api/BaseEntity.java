package by.march8.api;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Andy on 24.09.2014.
 *
 */

public class BaseEntity implements Serializable {

    @Getter
    @Setter
    protected int id;
}
