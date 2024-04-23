package ru.croc.javaschool2024.semeykin.project.service.export;

import java.io.IOException;
import java.sql.SQLException;

public interface ExportService {
    void export(long stationId) throws IOException;
}
