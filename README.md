# LogiBot

LogiBot – это Telegram-бот, написанный на **Kotlin 21** с использованием **Spring Boot**.  
Бот работает в контейнере **Docker**, а также может быть развернут с помощью **docker-compose**.

## 🚀 Функционал
- Работа с Telegram API.
- Поддержка команд и автоматических ответов.
- Логирование событий и ошибок.
- Возможность развертывания в Docker.

## 🛠️ Технологии
- **Kotlin 21**
- **Spring Boot**
- **Telegram Bot API**
- **Docker / Docker Compose**
- **Gradle**
- **PostgresSQL**
- **Redis**
- **MongoDB**

## 📦 Установка и запуск

### Локальный запуск
1. Установите **JDK 21** и **Gradle**.
2. Склонируйте репозиторий:
   ```sh
   git clone https://github.com/Gazdea/LogiBot.git
   cd LogiBot

    Установите переменные окружения (.env).
    Запустите бота:

    ./gradlew bootRun

Запуск через Docker

    Убедитесь, что у вас установлен Docker.
    Соберите и запустите контейнер:

docker-compose up --build -d

Остановить контейнер:

    docker-compose down

🔧 Конфигурация

Настройки бота хранятся в файле .env:

Минимальный набор:

- TELEGRAM_BOT_USERNAME=your_bot_username
- TELEGRAM_BOT_TOKEN=your_bot_token

Дополнительные настройки:

- DATABASE_URL=your_postgres_url
- DB_USERNAME=your_postgres_username
- DB_PASSWORD=your_postgres_password


- REDIS_HOST=your_redis_host
- REDIS_PORT=your_redis_port
- CACHE_PASSWORD=your_redis_password


- MONGO_HOST=your_mongo_host
- MONGO_PORT=your_mongo_port
- MONGO_DATABASE=your_mongo_database
- MONGO_USERNAME=your_mongo_username
- MONGO_PASSWORD=your_mongo_password
- MONGO_AUTH=your_mongo_auth

📞 Контакты / Автор

    Telegram: @Gazdea
    Автор: Mikhail Tutko
