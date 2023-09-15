package connector.DAO;


import connector.ConnectionPool;
import exception.DAOException;
import exception.DBConnectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractDAO {

    private static final Logger logger = LogManager.getLogger(ConnectionPool.class.getName());
    private ConnectionPool dbc;

    /**
     * Constructor
     */
    public AbstractDAO() throws DAOException {
        try {
            dbc = ConnectionPool.getInstance();
            logger.info("Connection, success");
        } catch (DBConnectionException e) {
            throw new DAOException("DBConnection, failed ", e);
        }
    }

    protected ConnectionPool getDBConnector() {
        logger.info("getting connector");
        return dbc;
    }
}
