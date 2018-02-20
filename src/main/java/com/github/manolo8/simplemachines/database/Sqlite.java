package com.github.manolo8.simplemachines.database;

import com.github.manolo8.simplemachines.exception.DataBaseException;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class Sqlite implements DataBase {

    private Connection connection;
    private File dataFolder;

    public String getHost() {
        return "jdbc:sqlite:" + dataFolder + "/simplemachines.db";
    }


    @Override
    public Connection getConnection() throws DataBaseException {
        try {
            Class.forName("org.sqlite.JDBC");
            if (connection != null && !connection.isClosed()) return connection;

            connection = DriverManager.getConnection(getHost());

        } catch (Exception e) {
            e.printStackTrace();
            throw new DataBaseException("Could not connect to the database");
        }

        return connection;
    }

    public void setDataFolder(File dataFolder) {
        dataFolder.mkdir();
        this.dataFolder = dataFolder;
    }
}
