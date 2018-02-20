package com.github.manolo8.simplemachines.database;

import com.github.manolo8.simplemachines.Config;
import com.github.manolo8.simplemachines.SimpleMachines;
import com.github.manolo8.simplemachines.exception.DataBaseException;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.SQLException;

public class DataBaseBuild {

    private DataBase dataBase;

    public Connection getConnection() throws DataBaseException {
        return dataBase.getConnection();
    }

    public void close() {
        try {
            if(dataBase == null) return;
            this.dataBase.getConnection().close();
            this.dataBase = null;
        } catch (SQLException | DataBaseException e) {
            SimpleMachines.ERROR(e.getMessage(), e.getStackTrace());
        }
    }

    public void buildByConfig(Plugin plugin, Config config) throws DataBaseException {
        String type = config.getString("Database.TYPE", "SQLITE");

        switch (type) {
            case "SQLITE":
                Sqlite sqlite = new Sqlite();
                sqlite.setDataFolder(plugin.getDataFolder());
                this.dataBase = sqlite;
                sqlite.getConnection();
                break;
            case "MYSQL":
                Mysql mysql = new Mysql();
                mysql.setHost(config.getString("Database.MYSQL.HOST"));
                mysql.setUsername(config.getString("Database.MYSQL.USERNAME"));
                mysql.setPassword(config.getString("Database.MYSQL.PASSWORD"));
                mysql.setDataBase(config.getString("Database.MYSQL.DB"));
                mysql.getConnection();
                this.dataBase = mysql;
                break;
            default:
                throw new DataBaseException("Type " + type + " not found");
        }
    }
}
