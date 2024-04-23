package ru.croc.javaschool2024.semeykin.project.DAO;

import ru.croc.javaschool2024.semeykin.project.exceptions.ObjectNotFoundException;
import ru.croc.javaschool2024.semeykin.project.model.PollingStation;
import ru.croc.javaschool2024.semeykin.project.model.Role;
import ru.croc.javaschool2024.semeykin.project.model.User;
import ru.croc.javaschool2024.semeykin.project.util.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class PollingStationDAO {
    private final Connection connection;

    public PollingStationDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public PollingStation createPollingStation(PollingStation pollingStation) throws SQLException {
        String sql = "insert into POLLING_STATION " +
                "(ID, ADDRESS, CAPACITY, BOXES_AMOUNT, REGISTERED_USERS_AMOUNT) " +
                "values (?, ?, ?, ?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1,pollingStation.id());
            statement.setString(2, pollingStation.address());
            statement.setInt(3, pollingStation.capacity());
            statement.setInt(4, pollingStation.boxesAmount());
            statement.setInt(5,pollingStation.registeredUsersAmount());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted == 0)
                throw new SQLException("Error creating polling station "+pollingStation);

        }
        return pollingStation;
    }

    public PollingStation getPollingStationById(Long id, boolean withUsers) throws SQLException{
        String sql="select * from POLLING_STATION " +
                "where ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1,id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()){
                    String address = resultSet.getString("address");
                    int capacity = resultSet.getInt("capacity");
                    int boxes_amount = resultSet.getInt("boxes_amount");
                    int users_registered_amount = resultSet.getInt("registered_users_amount");
                    Map<Role,List<User>> registeredUsers = new TreeMap<>();
                    if (withUsers){
                        UserDAO userDAO = new UserDAO();
                        userDAO.getUsersByStation(id)
                                .forEach(user -> {
                                    registeredUsers.computeIfAbsent(user.role(),k->new ArrayList<>());
                                    registeredUsers.get(user.role()).add(user);
                                });
                    }
                    return new PollingStation(
                            id,
                            address,
                            capacity,
                            boxes_amount,
                            users_registered_amount,
                            registeredUsers
                    );
                }
                else
                    throw new ObjectNotFoundException("Polling station with id " + id + " not found");
            }
        }
    }

    public List<PollingStation> getAllPollingStations() throws SQLException {
        String sql = "select *  from POLLING_STATION";
        List<PollingStation> pollingStations = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()){
                long id = resultSet.getLong("id");
                String address = resultSet.getString("address");
                int capacity = resultSet.getInt("capacity");
                int boxes_amount = resultSet.getInt("boxes_amount");
                int users_registered_amount = resultSet.getInt("registered_users_amount");
                pollingStations.add(new PollingStation(
                        id,
                        address,
                        capacity,
                        boxes_amount,
                        users_registered_amount
                ));
            }
        }
        return pollingStations;
    }

    public List<PollingStation> getAllAvailablePollingStations() throws SQLException {
        String sql = "select *  from POLLING_STATION where CAPACITY > REGISTERED_USERS_AMOUNT";
        List<PollingStation> pollingStations = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()){
                long id = resultSet.getLong("id");
                String address = resultSet.getString("address");
                int capacity = resultSet.getInt("capacity");
                int boxes_amount = resultSet.getInt("boxes_amount");
                int users_registered_amount = resultSet.getInt("registered_users_amount");
                pollingStations.add(new PollingStation(
                        id,
                        address,
                        capacity,
                        boxes_amount,
                        users_registered_amount
                ));
            }
        }
        return pollingStations;
    }

    public void updateRegisteredUsersAmount(Long id, boolean wasRegistered) throws SQLException {
        String incrementSql = "update POLLING_STATION set " +
                "REGISTERED_USERS_AMOUNT = REGISTERED_USERS_AMOUNT + 1 " +
                "where ID = ?";
        String decrementSql = "update POLLING_STATION set " +
                "REGISTERED_USERS_AMOUNT = REGISTERED_USERS_AMOUNT - 1 " +
                "where ID = ?";
        String sql = wasRegistered ? incrementSql : decrementSql;

        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1,id);
            int rows = statement.executeUpdate();
            if (rows == 0)
                throw new SQLException("Error updating registered users amount");
        }
    }

    public boolean isTableEmpty() throws SQLException{
        try (PreparedStatement statement = connection.prepareStatement("select * from POLLING_STATION");
        ResultSet resultSet = statement.executeQuery()){
            return !resultSet.next();
        }
    }

    public void updatePollingStation(PollingStation pollingStation) throws SQLException {
        String sql="update POLLING_STATION set " +
                "ADDRESS = ?," +
                "CAPACITY = ?," +
                "BOXES_AMOUNT = ?," +
                "REGISTERED_USERS_AMOUNT = ? " +
                "where ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1,pollingStation.address());
            statement.setInt(2,pollingStation.capacity());
            statement.setInt(3,pollingStation.boxesAmount());
            statement.setInt(4,pollingStation.registeredUsersAmount());
            statement.setLong(5,pollingStation.id());
            int rows = statement.executeUpdate();
            if (rows != 1)
                throw new SQLException("Error updating polling station with id "+pollingStation.id());
        }
    }
}
