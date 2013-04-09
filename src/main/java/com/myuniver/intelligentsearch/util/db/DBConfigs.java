package com.myuniver.intelligentsearch.util.db;

import com.myuniver.intelligentsearch.util.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * User: Dmitry Fateev
 * Date: 08.04.13
 * Time: 22:11
 */
public class DBConfigs {


    public static final String DB_DRIVER_CLASS_NAME_PROP = "db.driver.class.name";
    public static final String DB_CONNECTION_URL = "db.connection.url";
    public static final String DB_USER = "db.user";
    public static final String DB_PASS = "db.pass";


    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Config config = Config.getConfig();

        String driver = config.getProperty(DB_DRIVER_CLASS_NAME_PROP);
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("DB driver was not be found");
        }
        String url = config.getProperty(DB_CONNECTION_URL),
                dbUser = config.getProperty(DB_USER),
                pass = config.getProperty(DB_PASS);
        return DriverManager.getConnection(url, dbUser, pass);
    }
}
