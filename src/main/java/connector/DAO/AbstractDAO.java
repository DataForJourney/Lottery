package connector.DAO;


import connector.ConnectionPool;
import exception.DAOException;
import exception.DBConnectionException;

import java.util.logging.Logger;

public abstract class AbstractDAO {

    protected Logger logger = Logger.getLogger(AbstractDAO.class.getName());
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
