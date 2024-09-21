# HTTP Server

## Описание
Этот проект реализует HTTP сервер с поддержкой основных методов HTTP 1.1, используя `java.nio` и `ServerSocketChannel`. Сервер может обрабатывать запросы GET, POST, PUT, PATCH и DELETE. Заголовки запросов представлены в виде `Map`, что упрощает доступ к ним. Сервер также поддерживает обработку тела запроса и может отправлять корректные HTTP-ответы.

## Задание
Цель проекта — реализовать HTTP сервер с использованием Java NIO:
- Обработка методов GET, POST, PUT, PATCH, DELETE.
- Возможность добавления обработчиков (handlers) для определенных маршрутов.
- Чтение заголовков и тела запроса.
- Формирование корректных HTTP ответов.

## Структура проекта
```bash
├── com.Nezdanchik.spbpu
│   ├── HttpHeaders.java        # Класс для обработки заголовков HTTP
│   ├── HttpMethod.java         # Перечисление поддерживаемых HTTP методов
│   ├── HttpRequest.java        # Класс для парсинга и хранения запроса
│   ├── HttpResponse.java       # Класс для формирования и отправки ответа
│   ├── HttpServer.java         # Основной класс сервера
│   ├── PathListener.java       # Обработчик маршрутов и методов
│   ├── RequestHandler.java     # Интерфейс для реализации обработчиков запросов
├── test
│   ├── HttpServerTest.java     # Тесты для проверки работы сервера
```
### `HttpServer`
Основной класс, отвечающий за создание и управление сервером.

#### Конструкторы:
- `HttpServer(String host, int port)` — инициализирует сервер на указанном хосте и порте.

#### Методы:
- `start()` — запускает сервер, который принимает соединения и обрабатывает запросы.
- `stop()` — останавливает сервер.
- `addHandler(String method, String path, RequestHandler handler)` — регистрирует обработчик для конкретного метода и маршрута.

### `HttpRequest`
Класс для представления и обработки входящего HTTP запроса.

#### Поля:
- `String method` — HTTP метод (GET, POST и т.д.).
- `String path` — путь запроса.
- `Map<String, String> headers` — заголовки запроса.
- `String body` — тело запроса.

#### Методы:
- `HttpRequest(String method, String path, Map<String, String> headers, String body)` — конструктор для создания объекта запроса.
- `static HttpRequest parse(String rawRequest)` — метод для парсинга сырого HTTP запроса из строки.
- `String getMethod()` — возвращает HTTP метод запроса.
- `String getPath()` — возвращает путь запроса.
- `Map<String, String> getHeaders()` — возвращает заголовки запроса.
- `String getBody()` — возвращает тело запроса.

### `HttpResponse`
Класс для представления HTTP ответа.

#### Поля:
- `int statusCode` — код статуса ответа (например, 200 для OK).
- `String body` — тело ответа.

#### Методы:
- `HttpResponse(int statusCode, String body)` — конструктор для создания ответа.
- `byte[] toBytes()` — метод для преобразования ответа в формат, который может быть отправлен клиенту.

### `HttpHeaders`
Класс для хранения и работы с заголовками HTTP запроса.

#### Поля:
- `Map<String, String> headers` — карта для хранения заголовков.

#### Методы:
- `void addHeader(String name, String value)` — добавляет заголовок.
- `String getHeader(String name)` — получает значение заголовка по его имени.
- `Map<String, String> getAllHeaders()` — возвращает все заголовки.

### `HttpMethod`
Перечисление, содержащее все поддерживаемые HTTP методы.

#### Значения:
- `GET`
- `POST`
- `PUT`
- `PATCH`
- `DELETE`

#### Методы:
- `static HttpMethod fromString(String method)` — метод для преобразования строки в соответствующее значение перечисления.

### `RequestHandler`
Интерфейс для создания обработчиков запросов. Каждая реализация этого интерфейса должна определять логику обработки конкретного HTTP запроса.

#### Метод:
- `HttpResponse handle(HttpRequest request)` — метод, который реализует логику обработки запроса и возвращает ответ.

### `PathListener`
Класс для добавления слушателей (обработчиков) для конкретных путей и HTTP методов.

#### Поля:
- `Map<String, Map<HttpMethod, RequestHandler>> routes` — карта маршрутов и их обработчиков.

#### Методы:
- `void addListener(String path, HttpMethod method, RequestHandler handler)` — добавляет обработчик для конкретного пути и метода.
- `RequestHandler getHandler(String path, HttpMethod method)` — возвращает обработчик для указанного пути и метода.

### `HttpServerTest`
Класс для тестирования работы сервера с использованием JUnit 5. Включает тесты для различных HTTP методов (GET, POST, PUT, PATCH, DELETE) и проверяет корректность обработки запросов.

## Тесты
Для проверки работы сервера используется набор юнит-тестов, которые проверяют корректность обработки HTTP запросов и ответов.

Тесты проверяют:
- Обработку GET, POST, PUT, PATCH и DELETE запросов.
- Ответы сервера с корректными HTTP кодами.
- Обработку ошибок и маршрутов, для которых нет обработчиков (404 Not Found).
