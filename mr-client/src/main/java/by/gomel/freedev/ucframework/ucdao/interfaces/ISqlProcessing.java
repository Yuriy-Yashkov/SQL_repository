package by.gomel.freedev.ucframework.ucdao.interfaces;

import java.sql.Connection;

/**
 * @author Andy 14.05.2015.
 */
public interface ISqlProcessing {
    boolean processing(Connection connection);
}
