package ru.croc.javaschool2024.semeykin.project;

import ru.croc.javaschool2024.semeykin.project.exceptions.ObjectNotFoundException;
import ru.croc.javaschool2024.semeykin.project.model.PollingStation;
import ru.croc.javaschool2024.semeykin.project.model.Role;
import ru.croc.javaschool2024.semeykin.project.model.User;
import ru.croc.javaschool2024.semeykin.project.service.PollingStationService;
import ru.croc.javaschool2024.semeykin.project.service.UserService;
import ru.croc.javaschool2024.semeykin.project.service.export.ExportService;
import ru.croc.javaschool2024.semeykin.project.service.export.TxtExportService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ElectoralApplication {

    private final UserService userService = new UserService();
    private final PollingStationService pollingStationService = new PollingStationService();
    private User user;

    public ElectoralApplication() {
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Выберите действие:");
            System.out.println("1. Регистрация");
            System.out.println("2. Авторизация");
            System.out.println("3. Выход");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    registerUser(scanner);
                    break;
                case "2":
                    loginUser(scanner);
                    break;
                case "3":
                    System.out.println("Выход из программы.");
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private void loginUser(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите серию и номер паспорта без пробелов и других знаков:");
                Long passportId = scanner.nextLong();
                scanner.nextLine();

                System.out.println("Введите пароль:");
                String password = scanner.nextLine();

                user = userService.login(passportId, password);
                if (user.role() == Role.ADMIN)
                    adminActions(scanner);
                else
                    userActions(scanner);
                return;
            } catch (SQLException | NumberFormatException | ObjectNotFoundException e) {
                System.err.println(e.getMessage());
                return;
            } catch (InputMismatchException e) {
                System.err.println("Ошибка ввода!");
                scanner.nextLine();
            }
        }
    }

    private void registerUser(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите серию и номер паспорта без пробелов и других знаков:");
                Long passportId = scanner.nextLong();
                scanner.nextLine();

                System.out.println("Введите имя пользователя:");
                String username = scanner.nextLine();

                System.out.println("Введите пароль:");
                String password = scanner.nextLine();

                System.out.println("Введите номер телефона:");
                String phone = scanner.nextLine();

                user = userService.register(new User(
                        passportId,
                        username,
                        password,
                        phone
                ));
                if (user.role() == Role.ADMIN)
                    adminActions(scanner);
                else
                    userActions(scanner);
                return;
            } catch (SQLException | IllegalArgumentException e) {
                System.err.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.err.println("Ошибка ввода!");
                scanner.nextLine();
            }
        }
    }

    private void userActions(Scanner scanner) {
        System.out.println("Добро пожаловать," + user.fullName());
        while (true) {
            System.out.println("Выберите действие для пользователя:");
            System.out.println("1. Зарегистрироваться на участок");
            System.out.println("2. Посмотреть все доступные участки");
            System.out.println("3. Посмотреть наиболее свободные участки");
            System.out.println("4. Посмотреть информацию о пользователе");
            System.out.println("5. Посмотреть информацию об участке");
            System.out.println("6. Выход");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    choosePollingStation(scanner);
                    break;
                case "2":
                    showAllPollingStations();
                    break;
                case "3":
                    showRecommendedPollingStations();
                    break;
                case "4":
                    showUserInfo();
                    break;
                case "5":
                    getPollingStation(scanner);
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private void getPollingStation(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите номер участка:");
                Long id = scanner.nextLong();
                scanner.nextLine();
                System.out.println(pollingStationService.get(id));
                return;
            } catch (SQLException | IllegalArgumentException e) {
                System.err.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.err.println("Ошибка ввода!");
                scanner.nextLine();
            }
        }
    }

    private void showUserInfo() {
        System.out.println(user);
    }

    private void showRecommendedPollingStations() {
        try {
            pollingStationService.getRecommendedPollingStations(3)
                    .forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void showAllPollingStations() {
        try {
            pollingStationService.getAll()
                    .forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void choosePollingStation(Scanner scanner) {
        while (true) {
            System.out.println("Введите номер участка для регистрации:");
            try {
                long stationId = scanner.nextLong();
                userService.choosePollingStation(user, stationId);
                scanner.nextLine();
                break;
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.err.println("Ошибка ввода!");
                scanner.nextLine();
            }
        }
    }

    private void adminActions(Scanner scanner) {
        System.out.println("Добро пожаловать," + user.fullName());
        while (true) {
            System.out.println("Выберите действие для администратора:");
            System.out.println("1. Добавить участок");
            System.out.println("2. Редактировать участок");
            System.out.println("3. Найти участок");
            System.out.println("4. Экспортировать информацию об участке в файл");
            System.out.println("5. Добавить членов комиссии на участок");
            System.out.println("6. Выход");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createPollingStation(scanner);
                    break;
                case "2":
                    updatePollingStation(scanner);
                    break;
                case "3":
                    getPollingStation(scanner);
                    break;
                case "4":
                    exportPollingStation(scanner);
                    break;
                case "5":
                    addComisionMembers(scanner);
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private void addComisionMembers(Scanner scanner) {
        while (true) {
            System.out.println("Выберите действие для администратора:");
            System.out.println("1. Добавить члена комиссии");
            System.out.println("2. Выход");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addComisionMember(scanner, null);
                    break;
                case "2":
                    return;
            }
        }
    }

    private void addComisionMembers(Scanner scanner, Long id) {
        while (true) {
            System.out.println("Выберите действие для администратора:");
            System.out.println("1. Добавить члена комиссии");
            System.out.println("2. Выход");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addComisionMember(scanner, id);
                    break;
                case "2":
                    return;
            }
        }
    }

    private void addComisionMember(Scanner scanner, Long stationId) {
        while (true) {
            try {
                if (stationId == null){
                    System.out.println("Введите номер участка:");
                    stationId = scanner.nextLong();
                    scanner.nextLine();
                }

                System.out.println("Введите серию и номер паспорта без пробелов и других знаков:");
                Long passportId = scanner.nextLong();
                scanner.nextLine();

                System.out.println("Введите имя пользователя:");
                String username = scanner.nextLine();

                System.out.println("Введите пароль:");
                String password = scanner.nextLine();

                System.out.println("Введите номер телефона:");
                String phone = scanner.nextLine();

                user = userService.register(new User(
                        passportId,
                        username,
                        password,
                        phone,
                        Role.COMMISSION_MEMBER,
                        stationId
                ));
                return;
            } catch (SQLException | IllegalArgumentException e) {
                System.err.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.err.println("Ошибка ввода!");
                scanner.nextLine();
            }
        }
    }

    private void exportPollingStation(Scanner scanner) {
        while (true) {
            System.out.println("Введите номер участка:");
            try {
                long id = scanner.nextLong();
                scanner.nextLine();

                ExportService exportService = new TxtExportService();
                exportService.export(id);
                return;
            } catch (InputMismatchException e) {
                System.err.println("Ошибка ввода!");
                scanner.nextLine();
            } catch (IOException | IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void updatePollingStation(Scanner scanner) {
        while (true) {
            try {
                PollingStation input = inputPollingStation(scanner);
                PollingStation prev = pollingStationService.get(input.id());
                input = new PollingStation(
                        input.id(),
                        input.address(),
                        input.capacity(),
                        input.boxesAmount(),
                        prev.registeredUsersAmount()
                );
                pollingStationService.updatePollingService(input);
                System.out.println(input);
                return;
            } catch (SQLException | IllegalArgumentException e) {
                System.err.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.err.println("Ошибка ввода!");
                scanner.nextLine();
            }
        }
    }

    private void createPollingStation(Scanner scanner) {
        while (true) {
            try {
                PollingStation input = inputPollingStation(scanner);

                PollingStation pollingStation = pollingStationService.create(input);
                addComisionMembers(scanner, input.id());
                System.out.println(pollingStation);
                return;
            } catch (SQLException | IllegalArgumentException e) {
                System.err.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.err.println("Ошибка ввода!");
                scanner.nextLine();
            }
        }
    }

    private PollingStation inputPollingStation(Scanner scanner) {
        System.out.println("Введите номер участка:");
        Long id = scanner.nextLong();
        scanner.nextLine();

        System.out.println("Введите адрес:");
        String address = scanner.nextLine();

        System.out.println("Введите вместимость:");
        int capacity = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Введите количество урн:");
        int boxes_amount = scanner.nextInt();
        scanner.nextLine();

        return new PollingStation(
                id,
                address,
                capacity,
                boxes_amount
        );
    }
}
