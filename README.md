# Socks Management System

## Описание проекта

Проект представляет собой RESTful API для управления складом носков. Система позволяет добавлять, удалять и фильтровать носки по различным параметрам (цвет, процентное содержание хлопка и количество). Проект использует Spring Boot, PostgreSQL (для production) и H2 (для тестов), а также Flyway для миграций базы данных.

---

## Технологии

- **Java 17**
- **Spring Boot 3.4.3**
- **Spring Data JPA**
- **PostgreSQL** (основная база данных)
- **H2 Database** (для тестов)
- **Flyway** (для миграций базы данных)
- **JUnit 5** (для тестирования)
- **Mockito** (для мокирования зависимостей)
- **Maven** (для сборки проекта)

---

## Структура проекта

### Контроллеры
- `SocksController`: Обрабатывает HTTP-запросы для управления носками.
    - `GET /api/socks/all`: Возвращает общее количество носков на складе.
    - `GET /api/socks/{id}`: Возвращает носки по ID.
    - `POST /api/socks/income`: Добавляет новые носки на склад.
    - `DELETE /api/socks/outcome/{id}`: Удаляет носки по ID.
    - `GET /api/socks`: Фильтрует носки по цвету, содержанию хлопка и операции сравнения.

### DTO
- `SocksDto`: Представляет данные для добавления носков.
- `DeleteSocksResponseDto`: Ответ при удалении носков.
- `GetAllSocksResponseDto`: Ответ при фильтрации носков.
- `CustomErrorResponseDto`: Ответ при возникновении ошибок.

### Сущности
- `Socks`: Основная сущность, представляющая носки в базе данных.
- `Color`: Перечисление доступных цветов носков.

### Сервисы
- `SocksService`: Бизнес-логика для работы с носками.
    - Методы: `getSocks`, `getSocksById`, `addSocks`, `deleteSocksById`, `getSocksWithParams`.

### Репозитории
- `ISocksRepository`: Интерфейс для работы с базой данных через Spring Data JPA.

### Исключения
- `AdvancedRuntimeException`: Кастомное исключение для обработки ошибок.
- `GlobalExceptionHandler`: Глобальный обработчик исключений.

### Тестирование
- **Интеграционные тесты**: Проверяют работу API.
- **Модульные тесты**: Проверяют логику контроллеров и сервисов.

---
## База данных

### Production
Для работы в production используется **PostgreSQL**. Настройки подключения находятся в файле `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/socksdb
    username: user_socks
    password: 123456
    driver-class-name: org.postgresql.Driver
```
---
## Тестирование

### Конфигурация
Для тестов используется H2 Database (встроенная база данных). Настройки находятся в файле application-test.yml:
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driverClassName: org.h2.Driver
    username: sa
    password: password
  h2:
    console:
      enabled: true
```
### Запуск тестов
Для запуска всех тестов выполните следующую команду:
```bash
mvn test
```

### Интеграционные тесты
Интеграционные тесты проверяют работу API с использованием TestRestTemplate. Они эмулируют HTTP-запросы к серверу и проверяют корректность ответов.

Пример интеграционного теста:
```java
@Test
void testAddSocks() {
  SocksDto socksDto = new SocksDto();
  socksDto.setColor(Color.green);
  socksDto.setCottonPart(50);
  socksDto.setQuantity(20);

  ResponseEntity<Long> response = restTemplate.postForEntity(baseUrl + "/socks/income", socksDto, Long.class);
  assertEquals(HttpStatus.OK, response.getStatusCode());
  assertNotNull(response.getBody());

  ResponseEntity<Integer> allSocksResponse = restTemplate.getForEntity(baseUrl + "/socks/all", Integer.class);
  assertTrue(allSocksResponse.getBody() > 0);
}
```
### Модульные тесты
Модульные тесты проверяют логику сервисов и контроллеров с использованием библиотеки Mockito . Они позволяют протестировать методы без запуска всего приложения.

Пример модульного теста:
```java
@Test
void testGetSocks_ReturnsCorrectCount() {
  List<Socks> sockList = Arrays.asList(new Socks(), new Socks());
  when(socksRepository.findAll()).thenReturn(sockList);
  int count = socksService.getSocks();

  assertEquals(2, count, "Количество носков должно быть равно 2");
  verify(socksRepository).findAll();
}
```
---
## Миграции
Для управления миграциями базы данных используется Flyway . SQL-скрипты находятся в директории src/main/resources/db/migration. Чтобы применить миграции, выполните:
```bash
mvn flyway:migrate
```
---

## Запуск проекта
1. Убедитесь, что PostgreSQL запущен и настроен.
2. Выполните миграции Flyway:
```bash
mvn flyway:migrate
```
3. Запустите приложение:
```bash
mvn spring-boot:run
```
После запуска приложение будет доступно по адресу:
**http://localhost:8080/api**
---
## Примеры запросов

### Добавление носков
Чтобы добавить новые носки на склад, выполните следующий запрос:
```http
POST /api/socks/income
Content-Type: application/json

{
  "color": "red",
  "cotton_part": 50,
  "quantity": 100
}
```
### Удаление носков
Чтобы удалить носки по ID, выполните следующий запрос:
```http
DELETE /api/socks/outcome/1
```

### Фильтрация носков
Чтобы найти носки по цвету, содержанию хлопка и операции сравнения, выполните следующий запрос:
```http
GET /api/socks?color=red&operation=equal&cottonPart=50
```