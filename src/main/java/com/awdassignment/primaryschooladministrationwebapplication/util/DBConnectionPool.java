package com.awdassignment.primaryschooladministrationwebapplication.util;

import jakarta.naming.InitialContext;
import jakarta.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public final class DBConnectionPool {

    private static DataSource dataSource;

    private DBConnectionPool() {
    }

    private static DataSource lookupDataSource() throws NamingException {
        if (dataSource == null) {
            synchronized (DBConnectionPool.class) {
                if (dataSource == null) {
                    InitialContext ctx = new InitialContext();
                    dataSource = (DataSource) ctx.lookup(Constants.JNDI_DATASOURCE);
                }
            }
        }
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        try {
            return lookupDataSource().getConnection();
        } catch (NamingException e) {
            throw new SQLException("Unable to lookup DataSource", e);
        }
    }
}

