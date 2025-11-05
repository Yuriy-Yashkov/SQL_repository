package by.gomel.freedev.ucframework.ucdao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public interface BaseDB {

    Connection getConnection() throws SQLException, ClassNotFoundException;

    long DAY = 1000 * 60 * 60 * 24L;
}
