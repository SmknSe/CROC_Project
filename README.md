# Проект "Система регистрации избирательных участков"

## Описание проекта

Проект представляет собой информационную систему для регистрации избирательных участков. Система предоставляет возможность регистрации, редактирования и поиска избирательных участков, а также взаимодействия с пользователями через консоль.

### Функциональные требования

1. Регистрация, редактирование и поиск избирательных участков по номеру участка.
2. Выгрузка данных по избирательному участку в отдельный файл.
3. Взаимодействие с пользователем через консоль.
4. Сохранение изменений в реляционной базе данных.
5. Рекомендация наиболее свободного избирательного участка пользователям на основе вместимости и количества зарегистрированных пользователей.
6. Разграничение функционала для администратора участков и обычного пользователя.

## Инструкции по запуску

### Запуск программы

1. Скачайте и распакуйте архив с проектом или клонируйте репозиторий;
2. Перейдите в директорию проекта;
3. Для запуска программы выполните в командной строке следующую команду:

```bash
java -jar out/artifacts/CROC_Project_jar/CROC_Project.jar
```
## Взаимодействие с программой

После запуска программы вам будут предложены опции для регистрации и авторизации. Вы можете зарегистрироваться как обычный пользователь или войти как администратор для управления избирательными участками.

- Для регистрации нового пользователя введите `1`.
- Для авторизации введите `2`.

После успешной авторизации вам будет предоставлен функционал для работы с ситемой.

P.S. В рамках учебного проекта в систему уже будут загружены данные пользователей из переданных CSV файлов папки resources. 
Поэтому для проверки функционала администратора необходимо взять идентификатор и пароль оттуда, либо взять данные значения:
* passportID: 1111111111
* password: admin

## Использованные темы

1. ООП
2. Обработка ошибок
3. IO
4. Обобщенное программирование + коллекции
5. Stream API + лямбда выражения
6. CSV
7. JDBC API