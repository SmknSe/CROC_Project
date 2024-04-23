package ru.croc.javaschool2024.semeykin.project.service;

import ru.croc.javaschool2024.semeykin.project.DAO.PollingStationDAO;
import ru.croc.javaschool2024.semeykin.project.model.PollingStation;

import java.sql.SQLException;
import java.util.List;

public class PollingStationService {
    private final PollingStationDAO pollingStationDAO;

    public PollingStationService() {
        this.pollingStationDAO = new PollingStationDAO();
    }

    public List<PollingStation> getRecommendedPollingStations(int amount) throws SQLException {
        return pollingStationDAO.getAllAvailablePollingStations().stream()
                .sorted((a,b)->{
                    double firstFullness = (double) a.registeredUsersAmount() /a.boxesAmount();
                    double secondFullness = (double) b.registeredUsersAmount() /b.boxesAmount();
                    double diff = firstFullness - secondFullness;
                    return diff > 0 ? 1 :
                            diff < 0 ? -1: 0;
                })
                .limit(amount)
                .toList();
    }

    public PollingStation create(PollingStation pollingStation) throws SQLException {
        return pollingStationDAO.createPollingStation(pollingStation);
    }

    public void createAll(List<PollingStation> pollingStations) {
        pollingStations.forEach(pollingStation -> {
            try {
                create(pollingStation);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public boolean isTableEmpty() throws SQLException {
        return pollingStationDAO.isTableEmpty();
    }

    public List<PollingStation> getAll() throws SQLException {
        return pollingStationDAO.getAllPollingStations();
    }

    public PollingStation get(Long id) throws SQLException {
        return pollingStationDAO.getPollingStationById(id,false);
    }

    public void updatePollingService(PollingStation pollingStation) throws SQLException {
        pollingStationDAO.updatePollingStation(pollingStation);
    }
}
