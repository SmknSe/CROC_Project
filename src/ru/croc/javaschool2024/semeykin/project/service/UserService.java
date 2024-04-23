package ru.croc.javaschool2024.semeykin.project.service;

import ru.croc.javaschool2024.semeykin.project.DAO.PollingStationDAO;
import ru.croc.javaschool2024.semeykin.project.DAO.UserDAO;
import ru.croc.javaschool2024.semeykin.project.model.User;
import ru.croc.javaschool2024.semeykin.project.util.MD5Hashing;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserDAO userDAO;
    private final PollingStationDAO pollingStationDAO;

    public UserService() {
        userDAO = new UserDAO();
        pollingStationDAO = new PollingStationDAO();
    }

    public User choosePollingStation(User user, long newStationId) throws SQLException {
        userDAO.updateStationId(user.passportId(), newStationId);
        pollingStationDAO.updateRegisteredUsersAmount(newStationId,true);
        if (user.station_id() != null)
            pollingStationDAO.updateRegisteredUsersAmount(user.station_id(),false);
        return userDAO.getUserById(user.passportId());
    }

    public User register(User user) throws SQLException {
        userDAO.createUser(user);
        if (user.station_id() != null){
            PollingStationDAO pollingStationDAO = new PollingStationDAO();
            pollingStationDAO.updateRegisteredUsersAmount(user.station_id(), true);
        }
        return user;
    }

    public User login(Long passportId, String password)
            throws SQLException {
        return userDAO.getUserByIdAndPassword(
                passportId,
                MD5Hashing.hashPassword(password)
        );
    }

    public void registerAll(List<User> users) {
        users.forEach(user-> {
            try {
                register(user);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public boolean isTableEmpty() throws SQLException {
        return userDAO.isTableEmpty();
    }
}
