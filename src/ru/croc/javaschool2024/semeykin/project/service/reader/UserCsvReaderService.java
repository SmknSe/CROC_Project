package ru.croc.javaschool2024.semeykin.project.service.reader;

import ru.croc.javaschool2024.semeykin.project.model.Role;
import ru.croc.javaschool2024.semeykin.project.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class UserCsvReaderService implements ReaderService<List<User>>{
    @Override
    public List<User> read(String filePath) throws IOException, NumberFormatException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            return reader.lines()
                    .map(line->{
                        String[] spliited = line.split(",");
                        Long stationId;
                        try {
                            stationId = Long.parseLong(spliited[5]);
                        } catch (NumberFormatException e){
                            stationId = null;
                        }
                        return new User(
                                Long.parseLong(spliited[0]),
                                spliited[1],
                                spliited[2],
                                spliited[3],
                                Role.valueOf(spliited[4]),
                                stationId
                        );
                    }).toList();
        }
    }
}
