package connector.DAO;

import exception.*;
import generator.LotteryTicket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOLotteryTicket extends AbstractDAO {
    /**
     * Запрос для внесения номера билета, чисел билета, статуса(доступен)
     */
    private static final String insertLotteryTicket = "INSERT INTO lottery_ticket (ticket_number, numbers, status) values(?,?,?)";
    /**
     * Запрос для получения билета по его номеру из бд
     */
    private static final String selectLotteryTicketByNumber = "SELECT * FROM lottery_ticket WHERE ticket_number =?";
    /**
     * Запрос для получения всех билетов из бд
     */
    private static final String selectAllLotteryTickets = "SELECT * FROM lottery_ticket";
    /**
     * Запрос для удаления билета по его номеру из бд
     */
    private static final String deleteFromLotteryTicketById = "DELETE FROM lottery_ticket WHERE ticket_number =?";
    /**
     * Запрос для удаления всех билетов из бд
     */
    private static final String deleteAllFromLotteryTicket = "DELETE FROM lottery_ticket";
    /**
     * Запрос для получения билетов по состоянию(продан, доступен) из бд
     */
    private static final String selectLotteryTicketByCondition = "select ticket_number, numbers  FROM lottery_ticket where status =?";
    /**
     * Запрос для изменения состояния билета по его номеру
     */
    private static final String updateConditionOfTicket = "update lottery_ticket set status = ? where ticket_number = ?";

    /**
     * Constructor
     */
    public DAOLotteryTicket() throws DAOException {
        super();
    }

    /**
     * Функция для изменения состояния билета по его номеру
     *
     * @param status        статус, который получит билет после завершения работы функции
     * @param ticket_number номер билета, у которого будет изменено состояние
     */
    public int updateStatusOfTicket(String status, String ticket_number) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int answer;
        try {
            connection = getDBConnector().getConnection();
            preparedStatement = connection.prepareStatement(updateConditionOfTicket);
            preparedStatement.setString(1, status);
            preparedStatement.setString(2, ticket_number);
            answer = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Ошибка в update Status Of Ticket", e);
        } catch (DBConnectionException e) {
            throw new DAOException("Ошибка подключения db Connection LotteryTicket", e);
        } finally {
            toClosePreparedStatement(preparedStatement);
            toCloseConnection(connection);
        }
        return answer;
    }

    /**
     * Функция для получения билетов с заданным статусом из бд
     *
     * @param condition билеты с этим статусом будут получены из бд
     */
    public List<LotteryTicket> getLotteryTicketByCondition(String condition) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<LotteryTicket> lotteryTickets = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            connection = getDBConnector().getConnection();
            preparedStatement = connection.prepareStatement(selectLotteryTicketByCondition);
            preparedStatement.setString(1, condition);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lotteryTickets.add(new LotteryTicket(
                        (resultSet.getString("ticket_number")),
                        (resultSet.getString("numbers"))));
            }
        } catch (SQLException e) {
            throw new DAOException("Ошибка в get LotteryTicket By Condition", e);
        } catch (DBConnectionException e) {
            throw new DAOException("Ошибка подключения db Connection LotteryTicket", e);
        } finally {
            toCloseResultSet(resultSet);
            toClosePreparedStatement(preparedStatement);
            toCloseConnection(connection);
        }
        return lotteryTickets;
    }

    /**
     * Функция для получения всех билетов из бд
     */
    public List<LotteryTicket> getAllTickets() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<LotteryTicket> lotteryTickets = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            connection = getDBConnector().getConnection();
            preparedStatement = connection.prepareStatement(selectAllLotteryTickets);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lotteryTickets.add(new LotteryTicket(
                        (resultSet.getString("ticket_number")),
                        (resultSet.getString("numbers"))));
            }
        } catch (SQLException e) {
            throw new DAOException("Ошибка в Get all Tickets", e);
        } catch (DBConnectionException e) {
            throw new DAOException("Ошибка подключения db Connection LotteryTicket", e);
        } finally {
            toCloseResultSet(resultSet);
            toClosePreparedStatement(preparedStatement);
            toCloseConnection(connection);
        }
        return lotteryTickets;
    }

    /**
     * Функция для внесения данных билета в бд
     *
     * @param lotteryTicket данные этого билета будут внесены в бд
     */
    public int insert(LotteryTicket lotteryTicket) throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int answer;
        try {
            connection = getDBConnector().getConnection();
            preparedStatement = connection.prepareStatement(insertLotteryTicket);
            preparedStatement.setString(1, String.valueOf(lotteryTicket.getNumber()));
            preparedStatement.setString(2, LotteryTicket.convertTicketToString(lotteryTicket.getTicket()));
            preparedStatement.setString(3, "available");
            answer = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Ошибка в insert LotteryTicket", e);
        } catch (DBConnectionException e) {
            throw new DAOException("Ошибка подключения db Connection LotteryTicket", e);
        } finally {
            toClosePreparedStatement(preparedStatement);
            toCloseConnection(connection);
        }
        return answer;
    }

    /**
     * Функция для получения билета с заданным номеро из бд
     *
     * @param ticket_number билет с этим номером будет получен из бд
     */
    public LotteryTicket getByTicketNumber(String ticket_number) throws DAOException {
        Connection connection = null;
        LotteryTicket lotteryTickets = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getDBConnector().getConnection();
            preparedStatement = connection.prepareStatement(selectLotteryTicketByNumber);
            preparedStatement.setString(1, ticket_number);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                lotteryTickets = new LotteryTicket(
                        (resultSet.getString("ticket_number")),
                        (resultSet.getString("numbers")));

            }
        } catch (SQLException e) {
            throw new DAOException("Ошибка в get By Ticket Number", e);
        } catch (DBConnectionException e) {
            throw new DAOException("Ошибка подключения db Connection LotteryTicket", e);
        } finally {
            toCloseResultSet(resultSet);
            toClosePreparedStatement(preparedStatement);
            toCloseConnection(connection);
        }
        return lotteryTickets;
    }

    /**
     * Функция для удаления билета с заданным номером из бд
     *
     * @param ticket_number билет с этим номером будет удален из бд
     */
    public int deleteByTicketNumber(String ticket_number) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int answer;
        try {
            connection = getDBConnector().getConnection();
            preparedStatement = connection.prepareStatement(deleteFromLotteryTicketById);
            preparedStatement.setString(1, ticket_number);
            answer = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Ошибка в delete By Ticket Number", e);
        } catch (DBConnectionException e) {
            throw new DAOException("Ошибка подключения db Connection LotteryTicket", e);
        } finally {
            toClosePreparedStatement(preparedStatement);
            toCloseConnection(connection);
        }
        return answer;
    }

    /**
     * Функция для удаления всех билетов из бд
     */
    public int deleteAllTickets() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int answer;
        try {
            connection = getDBConnector().getConnection();
            preparedStatement = connection.prepareStatement(deleteAllFromLotteryTicket);
            answer = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Ошибка в delete All LotteryTicket", e);
        } catch (DBConnectionException e) {
            throw new DAOException("Ошибка подключения db Connection LotteryTicket", e);
        } finally {
            toClosePreparedStatement(preparedStatement);
            toCloseConnection(connection);
        }
        return answer;
    }

    /**
     * Функция для освобождения ресурсов
     *
     * @param connection объект соединения
     */
    private void toCloseConnection(Connection connection) throws DAOException {
        if (connection != null) {
            try {
                getDBConnector().releaseConnection(connection);
            } catch (DBConnectionException e) {//jdbc
                throw new DAOException("Ошибка при закрытии подключения connection LotteryTicket", e);
            }
        }
    }

    /**
     * Функция для освобождения ресурсов
     *
     * @param preparedStatement объект соединения
     */
    private static void toClosePreparedStatement(PreparedStatement preparedStatement) throws DAOException {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                throw new DAOException("Ошибка при закрытии подключения preparedStatement LotteryTicket", e);
            }
        }
    }

    /**
     * Функция для освобождения ресурсов
     *
     * @param resultSet объект соединения
     */

    private static void toCloseResultSet(ResultSet resultSet) throws DAOException {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new DAOException("Ошибка при закрытии подключения resultSet LotteryTicket", e);
            }
        }
    }
}
