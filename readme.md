# TranslateTask-SpringBoot-LibreTranslate

## Description

A translator based on SpringBoot and LibreTranslate.

## Requirements

- Java 21
- Maven
- Docker Compose

## Running the Application with Docker

1. Clone the repository:
    ```sh
    git clone https://github.com/unrlight/translate-task.git
    ```
2. Navigate to the project directory:
    ```sh
    cd translate-task
    ```
3. Build the project using Maven:
    ```sh
    mvn clean package
    ```
4. Start Docker Compose:
    ```sh
    docker-compose up --build
    ```

The application will be available at `http://localhost:8080`.

## Usage

To translate a string, send a POST request to `http://localhost:8080/translate` with the parameters `text`, `from`, and `to`.

Example request:

```sh
curl -X POST "http://localhost:8080/translate?text=hello%20world&from=en&to=ru"
