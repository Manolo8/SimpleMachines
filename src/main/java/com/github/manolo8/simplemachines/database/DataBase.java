package com.github.manolo8.simplemachines.database;

import com.github.manolo8.simplemachines.exception.DataBaseException;

import java.sql.Connection;

public interface DataBase {

    Connection getConnection() throws DataBaseException;
}
