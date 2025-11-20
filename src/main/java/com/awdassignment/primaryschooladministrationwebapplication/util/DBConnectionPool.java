package com.awdassignment.primaryschooladministrationwebapplication.util;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Lightweight connection pool using the MySQL {@link MysqlConnectionPoolDataSource}.
 * Falls back to sensible defaults but allows overrides via system properties or environment variables:
 * DB_URL, DB_USERNAME, DB_PASSWORD.
 */
public final class DBConnectionPool {

    private static final DataSource DATA_SOURCE = initialize();

    private DBConnectionPool() {
    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }

    private static DataSource initialize() {
        MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
        ds.setURL(resolve("DB_URL", "jdbc:mysql://localhost:3306/primary_school?useSSL=false&serverTimezone=UTC"));
        ds.setUser(resolve("DB_USERNAME", "school_admin"));
        ds.setPassword(resolve("DB_PASSWORD", "changeMe123!"));
        try {
            ds.setAllowMultiQueries(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ds;
    }

    private static String resolve(String key, String defaultValue) {
        String sys = System.getProperty(key);
        if (sys != null && !sys.isBlank()) {
            return sys;
        }
        String env = System.getenv(key);
        if (env != null && !env.isBlank()) {
            return env;
        }
        return Objects.requireNonNullElse(defaultValue, "");
    }
}

