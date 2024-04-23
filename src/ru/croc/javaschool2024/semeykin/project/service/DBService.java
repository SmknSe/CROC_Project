package ru.croc.javaschool2024.semeykin.project.service;

import ru.croc.javaschool2024.semeykin.project.service.reader.PollingStationCsvReaderService;
import ru.croc.javaschool2024.semeykin.project.service.reader.UserCsvReaderService;
import ru.croc.javaschool2024.semeykin.project.util.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBService {
    private final Connection connection;
    private String USERS_PATH = "src/ru/croc/javaschool2024/semeykin/project/resources/Users.csv";
    private String POLLING_STATION_PATH = "src/ru/croc/javaschool2024/semeykin/project/resources/PollingStations.csv";

    public DBService() {
        this.connection = DatabaseConnection.getConnection();
    }

    public DBService(String USERS_PATH, String POLLING_STATION_PATH) {
        this.connection = DatabaseConnection.getConnection();
        this.USERS_PATH = USERS_PATH;
        this.POLLING_STATION_PATH = POLLING_STATION_PATH;
    }

    public void init() throws SQLException, IOException {
        createDBSchema();
        fillInitialData();
    }

    private void createDBSchema() throws SQLException {
        try (final Statement statement = connection.createStatement()){
            statement.execute("create table if not exists Polling_Station(" +
                    "id long primary key," +
                    "address varchar not null," +
                    "capacity int not null," +
                    "boxes_amount int not null," +
                    "registered_users_amount int not null, " +
                    "check (registered_users_amount <= capacity)" +
                    ")");
            statement.execute("create table if not exists Users(" +
                    "passport_id long primary key," +
                    "fullName varchar not null," +
                    "password varchar not null," +
                    "phone varchar unique not null," +
                    "role varchar not null," +
                    "station_id long," +
                    "foreign key (station_id) references polling_station(id)" +
                    ")");
        }
    }

    private void fillInitialData() throws IOException, SQLException {
        UserCsvReaderService userReader = new UserCsvReaderService();
        PollingStationCsvReaderService pollingStationReader = new PollingStationCsvReaderService();

        UserService userService = new UserService();
        PollingStationService pollingStationService = new PollingStationService();

        if (userService.isTableEmpty() && pollingStationService.isTableEmpty()){
            pollingStationService.createAll(pollingStationReader.read(POLLING_STATION_PATH));
            userService.registerAll(userReader.read(USERS_PATH));
        }
    }
}
