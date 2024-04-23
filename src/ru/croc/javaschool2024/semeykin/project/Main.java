package ru.croc.javaschool2024.semeykin.project;

import ru.croc.javaschool2024.semeykin.project.service.DBService;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DBService dbService = new DBService();
        try {
            dbService.init();
            ElectoralApplication application = new ElectoralApplication();
            application.start();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
