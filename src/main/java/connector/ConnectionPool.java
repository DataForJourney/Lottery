package connector;


import exception.DBConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionPool {

    private static ConnectionPool instance;
    // private static log4j.appender.
    private static final Logger logger = LogManager.getLogger(ConnectionPool.class.getName());
    private static final int initConnectionsCount = 5;
    private BlockingQueue<Connection> connections;

    private static String DRIVER;
    private static String URL;
    private static String USER;
    private static String PASS;

    /**
     * init form properties file database pool of connections
     *
     * @throws DBConnectionException if properties file loading error
     */
    private ConnectionPool() throws DBConnectionException {
        if (instance != null)
            return;
        ResourceBundle resource = ResourceBundle.getBundle("database");

        DRIVER = resource.getString("driver");
        URL = resource.getString("url");
        USER = resource.getString("user");
        PASS = resource.getString("password");

        try {
            Class.forName(DRIVER);
            logger.info("Driver loaded");
        } catch (ClassNotFoundException e) {
            throw new DBConnectionException("Error loading driver!", e);
        }

        connections = new ArrayBlockingQueue<>(initConnectionsCount);
        try {
            for (int i = 0; i < initConnectionsCount; i++) {
                Connection connection = DriverManager.getConnection(URL, USER, PASS);
                if (connection == null) {
                    throw new DBConnectionException("Driver type is not correct in URL " + URL + ".");
                }
                connections.add(connection);
                logger.info("Connection " + i + " established");
            }
        } catch (SQLTimeoutException e) {
            throw new DBConnectionException("Failed to authorize", e);
        } catch (SQLException e) {
            throw new DBConnectionException("Failed to establish connection", e);
        }
        logger.info("DB pool of connections inited");
    }

    /**
     * return instance ConnectorPool or create it
     *
     * @return instance of Singleton
     */
    public static synchronized ConnectionPool getInstance() throws DBConnectionException {
        if (instance == null)
            instance = new ConnectionPool();

        return instance;
    }

    /**
     * deinit database pool of connections
     *
     * @throws DBConnectionException if properties file loading error
     */
    public synchronized void deinitDBConnector() throws DBConnectionException {
        try {
            while (connections.size() > 0) {
                connections.take().close();
            }
        } catch (SQLException e) {
            throw new DBConnectionException("Could not close database connection ", e);
        } catch (InterruptedException e) {
            throw new DBConnectionException("Problem with concurrent queue", e);
        }
        logger.info("DB pool of connections deinited");
    }


    /**
     * take connection from pool
     *
     * @return connection
     * @throws DBConnectionException if something goes wrong
     */
    public synchronized Connection getConnection() throws DBConnectionException {
        try {
            logger.info("got connection from the pool");
            return connections.take();
        } catch (InterruptedException e) {
            throw new DBConnectionException("Failed to get connection from pool", e);
        }
    }

    /**
     * return the connection to pool
     *
     * @param connection to add back to pool
     */
    public synchronized void releaseConnection(Connection connection) throws DBConnectionException {
        try {
            if (connection.isClosed()) {
                logger.info("connection was closed");
                logger.info("created new connection");
                Connection newConnection = DriverManager.getConnection(URL, USER, PASS);
                connections.add(newConnection);
            } else {
                connections.add(connection);
                logger.info("returned connection to the pool");
            }
        } catch (SQLException e) {
            throw new DBConnectionException("Failed to establish connection", e);
        }
    }
}
