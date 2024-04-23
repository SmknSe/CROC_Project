package ru.croc.javaschool2024.semeykin.project.DAO;

import ru.croc.javaschool2024.semeykin.project.exceptions.ObjectNotFoundException;
import ru.croc.javaschool2024.semeykin.project.model.Role;
import ru.croc.javaschool2024.semeykin.project.model.User;
import ru.croc.javaschool2024.semeykin.project.util.DatabaseConnection;
import ru.croc.javaschool2024.semeykin.project.util.MD5Hashing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private final Connection connection;
    public UserDAO() {
        connection = DatabaseConnection.getConnection();
    }

    public User createUser(User user) throws SQLException {
        String sql;
        if (user.station_id() != null)
            sql = "insert into USERS " +
                "(PASSPORT_ID, FULLNAME, PASSWORD, PHONE, ROLE, STATION_ID) " +
                "values (?, ?, ?, ?, ?, ?);";
        else
            sql = "insert into USERS " +
                    "(PASSPORT_ID, FULLNAME, PASSWORD, PHONE, ROLE) " +
                    "values (?, ?, ?, ?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1,user.passportId());
            statement.setString(2, user.fullName());
            statement.setString(3,MD5Hashing.hashPassword(user.password()));
            statement.setString(4, user.phone());
            statement.setString(5,user.role().toString());
            if (user.station_id()!=null)
                statement.setLong(6,user.station_id());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted == 0)
                throw new SQLException("Error creating user "+user);
        }
        return user;
    }

    public User getUserById(long passportId) throws SQLException {
        String sql = "select * from USERS " +
                "where PASSPORT_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1,passportId);
            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    String fullName = resultSet.getString("fullname");
                    String password = resultSet.getString("password");
                    String  phone = resultSet.getString("phone");
                    Role role = Role.valueOf(resultSet.getString("role"));
                    long station_id = resultSet.getLong("station_id");
                    return new User(
                            passportId,
                            fullName,
                            password,
                            phone,
                            role,
                            station_id
                    );
                } else {
                    throw new SQLException("User with given id "+ passportId +" not found");
                }
            }
        }
    }

    public List<User> getUsersByStation(long stationId) throws SQLException {
        String sql = "select * from USERS where STATION_ID = ?";
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1,stationId);
            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    long passportId = resultSet.getLong("passport_id");
                    String fullName = resultSet.getString("fullname");
                    String password = resultSet.getString("password");
                    String  phone = resultSet.getString("phone");
                    Role role = Role.valueOf(resultSet.getString("role"));
                    users.add(new User(
                            passportId,
                            fullName,
                            password,
                            phone,
                            role,
                            stationId
                    ));
                }
            }
        }
        return users;
    }

    public User getUserByIdAndPassword(Long passportId, String password)
            throws SQLException {
        String sql = "select * from USERS " +
                "where PASSPORT_ID = ? and PASSWORD = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1,passportId);
            statement.setString(2,MD5Hashing.hashPassword(password));
            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    String fullName = resultSet.getString("fullname");
                    String  phone = resultSet.getString("phone");
                    Role role = Role.valueOf(resultSet.getString("role"));
                    long station_id = resultSet.getLong("station_id");
                    return new User(
                            passportId,
                            fullName,
                            password,
                            phone,
                            role,
                            station_id
                    );
                } else {
                    throw new ObjectNotFoundException(
                            "User with given credentials not found"
                    );
                }
            }
        }
    }

    public void changePassword(Long passportId, String password) throws SQLException{
        String sql = "update USERS set " +
                "PASSWORD = ?" +
                "where PASSPORT_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1,password);
            statement.setLong(2,passportId);
            int rows = statement.executeUpdate();
            if (rows == 0)
                throw new RuntimeException("Error while changing password");
        }
    }

    public User updateUserInfo(User user) throws SQLException {
        String sql = "update USERS set " +
                "FULLNAME = ?," +
                "PHONE = ? " +
                "where PASSPORT_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1,user.fullName());
            statement.setString(2, user.phone());
            statement.setLong(3,user.passportId());
            int rows = statement.executeUpdate();
            if (rows == 0)
                throw new RuntimeException("Error while creating user "+user);
            return user;
        }
    }

    public void updateStationId(Long passportId, Long stationId) throws SQLException {
        String sql = "update USERS set " +
                "ROLE = ?," +
                "STATION_ID = ? " +
                "where PASSPORT_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1,Role.ELECTOR.toString());
            statement.setLong(2, stationId);
            statement.setLong(3,passportId);
            int rows = statement.executeUpdate();
            if (rows == 0)
                throw new RuntimeException("Error updating user's stationId");
        }
    }

    public boolean isTableEmpty() throws SQLException{
        try (PreparedStatement statement = connection.prepareStatement("select * from USERS");
             ResultSet resultSet = statement.executeQuery()){
            return !resultSet.next();
        }
    }
}
