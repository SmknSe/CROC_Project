package ru.croc.javaschool2024.semeykin.project.service.reader;

import java.io.IOException;

public interface ReaderService<T> {
    T read(String filePath) throws IOException;
}
