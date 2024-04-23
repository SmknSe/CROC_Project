package ru.croc.javaschool2024.semeykin.project.service.reader;

import ru.croc.javaschool2024.semeykin.project.model.PollingStation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class PollingStationCsvReaderService implements ReaderService<List<PollingStation>> {
    @Override
    public List<PollingStation> read(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            return reader.lines()
                    .map(line->{
                        String[] spliited = line.split(",");
                        return new PollingStation(
                                Long.parseLong(spliited[0]),
                                spliited[1],
                                Integer.parseInt(spliited[2]),
                                Integer.parseInt(spliited[3])
                        );
                    }).toList();
        }
    }
}
