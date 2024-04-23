package ru.croc.javaschool2024.semeykin.project.service.export;


import ru.croc.javaschool2024.semeykin.project.DAO.PollingStationDAO;
import ru.croc.javaschool2024.semeykin.project.model.PollingStation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

public class TxtExportService implements ExportService{

    public TxtExportService() {}

    @Override
    public void export(long stationId) throws IOException {
        String filePath = "src/ru/croc/javaschool2024/semeykin/project/resources/exportData/"
                +stationId+".txt";
        PollingStationDAO pollingStationDAO = new PollingStationDAO();
        PollingStation pollingStation;
        try {
            pollingStation = pollingStationDAO.getPollingStationById(stationId, true);
        } catch (SQLException e){
            throw new IllegalArgumentException(e);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(pollingStation.toString());
        }

    }
}
