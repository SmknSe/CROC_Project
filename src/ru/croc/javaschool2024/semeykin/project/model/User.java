package ru.croc.javaschool2024.semeykin.project.model;

import ru.croc.javaschool2024.semeykin.project.util.MD5Hashing;

public record User (
        Long passportId,
        String fullName,
        String password,
        String phone,
        Role role,
        Long station_id
){
    public User(
            Long passportId,
            String fullName,
            String password,
            String phone,
            Role role,
            Long station_id
    ) {
        if (passportId < 1000000000 || passportId > 9999999999L)
            throw new IllegalArgumentException("Incorrect password id provided: "+passportId
                    +"\n10 digits required");
        this.passportId = passportId;
        this.fullName = fullName;
        this.password = MD5Hashing.hashPassword(password);
        phone = phone.replaceAll("[\\s()\\-+A-Za-z]","");
        if (phone.length() != 11)
            throw new IllegalArgumentException("Incorrect phone provided: "+phone
                    +"\n11 digits required");
        this.phone = phone;
        this.role = role;
        this.station_id = station_id;
    }

    public User(Long passportId, String fullName, String password, String phone) {
        this(passportId, fullName, password, phone, Role.USER, null);
    }

    public User(
            Long passportId,
            String fullName,
            String password,
            String phone,
            Long station_id) {
        this(passportId, fullName, password, phone, Role.ELECTOR, station_id);
    }

    @Override
    public String toString() {
        return "User " + passportId +":\n" +
                "\tfullName: " + fullName + '\n' +
                "\tphone: " + phone + '\n' +
                "\trole: " + role + '\n' +
                "\tstation_id: " + station_id;
    }

    public String toStringWithSpacing(String spacing){
        return "User " + passportId +":\n" +
                spacing+"fullName: " + fullName + '\n' +
                spacing+"phone: " + phone + '\n' +
                spacing+"role: " + role + '\n' +
                spacing+"station_id: " + station_id + '\n';
    }
}
