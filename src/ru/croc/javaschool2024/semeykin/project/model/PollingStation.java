package ru.croc.javaschool2024.semeykin.project.model;

import java.util.List;
import java.util.Map;

public record PollingStation(
        Long id,
        String address,
        Integer capacity,
        Integer boxesAmount,
        Integer registeredUsersAmount,
        Map<Role,List<User>> registeredUsers
) {
    public PollingStation(
            Long id,
            String address,
            Integer capacity,
            Integer boxesAmount) {
        this(id, address, capacity, boxesAmount, 0, null);
    }

    public PollingStation(
            Long id,
            String address,
            Integer capacity,
            Integer boxesAmount,
            Integer registeredUsersAmount) {
        this(id, address, capacity, boxesAmount, registeredUsersAmount, null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder ("PollingStation " + id + ":\n" +
                "\taddress: " + address + '\n' +
                "\tcapacity: " + capacity + '\n' +
                "\tboxesAmount: " + boxesAmount + '\n' +
                "\tregisteredUsersAmount: " + registeredUsersAmount + '\n');
        if (registeredUsers != null && !registeredUsers.isEmpty()){
            sb.append( "Registered Users:\n");
            for (var entry : registeredUsers.entrySet()){
                sb.append(entry.getKey()).append(":\n");
                entry.getValue().forEach(user -> sb.append('\t').append(user.toStringWithSpacing("\t\t")));
            }
        }
        return sb.toString();
    }
}
