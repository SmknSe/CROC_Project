package ru.croc.javaschool2024.semeykin.project.exceptions;

public class ObjectNotFoundException extends IllegalArgumentException{
    public ObjectNotFoundException(String s) {
        super(s);
    }
}
