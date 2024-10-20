# Руководство по запуску Backend-приложения

Это руководство объясняет, как настроить и запустить backend-сервисы приложения.

## ПО, необходимое для запуска:
1) JDK 17, скачать ее можно с сайта https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html;
2) Maven 3.9.*, скачать его можно с сайта https://maven.apache.org/, нужно будет добавить в переменные среды, советую почитать гадйы на эту тему.

Проверка установки Java 17:
```
java -version
```
Проверка устанвоки Maven:
```
mvn -version
```
## Инструкции по сборке и запуску
Выполните следующие шаги для сборки и запуска backend-сервисов:

1) Сборка модуля backend-commons
Перейдите в директорию /backend-commons и установите необходимые зависимости, выполнив следующую команду:
```
cd backend-commons
mvn clean install -DskipTests
```
Эта команда соберёт модуль backend-commons и установит его в локальное Maven-хранилище, пропустив тесты.

2) Сборка модуля backend
Затем перейдите в директорию /backend и соберите модуль backend, выполнив команду:
```
cd ../backend
mvn package -DskipTests
```
Эта команда выполнит упаковку приложения backend.

3) Сборка модуля bank-card
После сборки модуля backend, перейдите в директорию /bank-card и соберите модуль bank-card, выполнив команду:
```
cd ../bank-card
mvn package -DskipTests
```
Эта команда выполнит упаковку приложения bank-card.

4) Запуск модуля backend
После завершения сборки, запустите приложение backend, выполнив следующую команду в директории /backend:
```
cd ../backend
mvn spring-boot:run
```

5) Запуск модуля bank-card
В отдельном окне терминала перейдите в директорию /bank-card и запустите приложение bank-card:
```
cd ../bank-card
mvn spring-boot:run
```

Теперь оба сервиса backend и bank-card должны быть запущены на портах 8080 и 8081 соответственно.

## Важные моменты:
OpenAPI документацию можно найти по пути http://localhost:8080/swagger-ui.html.

Скрипты для баз данных вставлять не нужно, базы данных создадутся автоматически, а также создадутся нужные таблицы и вставятся тестовые данные.