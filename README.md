# Drone Service

Rest service to interact with Drone.

## Getting Started
____

Project needed to be imported as maven project and Java 17 must be used.

### Building
___

To build runnable jar:

```sh
$ ./mvnw clean package
```
### Run application
___
To run application:
```sh
$ ./mvnw clean spring-boot:run -f pom.xml
```


### Request examples
____

There are example requests

- **Register a new drone**

```
curl --location 'localhost:8080/drones' \
--header 'Content-Type: application/json' \
--data '{
    "serialNumber": "6A",
    "model": "Lightweight",
    "batteryCapacity": 0,
    "weightLimit": 490
}'
```
  where ...

  Result is created Drone:

```json
{
  "serialNumber": "6A",
  "model": "Lightweight",
  "weightLimit": 490.0,
  "batteryCapacity": 0,
  "state": "IDLE"
}
```

### Swagger UI

---
Service has as swagger-ui page located:
```
http://localhost:8080/swagger-ui.html
```

### Unit Testing
___
To run unit tests:
```sh
$ ./mvnw clean test
```

## Testing Service via Docker

For testing Drone Service with required environment provided a **docker-compose-test.yaml** file.

There are several steps to deploy Drone Service:
1. Build an executable jar:
```sh
$ ./mvnw clean package
```
2. Deploy via docker-compose:
```sh
$ docker-compose -f docker-compose-test.yml  up --build -d
```