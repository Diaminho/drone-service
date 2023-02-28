# Drone Service

Rest service to interact with Drone.

## Getting Started
____

Project needed to be imported as maven project and Java 17 must be used.

### Building
___

To build a runnable jar:

```sh
$ ./mvnw clean package
```

## Environment variables
___
To launch a Drone service there are must to set a several environment variables:

| name              	| description                             	| example                                    	|
|-------------------	|-----------------------------------------	|--------------------------------------------	|
| KAFKA_SERVER      	| kafka server                            	| localhost:29092                            	|
| KAFKA_TOPIC_NAME  	| topic name to send events               	| topic                                      	|
| DB_URL            	| database url                            	| jdbc:postgresql://localhost:5432/drones_db 	|
| DB_USER           	| database user                           	| testUser                                   	|
| DB_PASSWORD       	| database password                       	| testPassword                               	|
| MINIO_URL         	| minio url to save images                	| http://localhost:9000                      	|
| MINIO_ACCESS_KEY  	| access key (user) to minio              	| testKey                                    	|
| MINIO_SECRET_KEY  	| secret key (password) to minio          	| testSecret                                 	|
| MINIO_BUCKET_NAME 	| bucket name to store images             	| test-bucket                                	|
| CRON_EXPRESSION   	| Spring CronExpression for periodic task 	| "*/30 * * * * *"                           	|

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
  where
  - **serialNumber** - serialNumber of drone;
  - **model** - model of drone;
  - **batteryCapacity** - current battery level;
  - **weightLimit** - drone's weight limit for load in grams.

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

- **Get drone's battery level**

```
curl --location 'localhost:8080/drones/{serialNumber}/battery'
```
where
- **serialNumber** - serialNumber of drone;

Result is found information about drone's battery level:

```json
{
  "currentLevel": 95
}
```

- **Drone's load information**

```
curl --location 'localhost:8080/drones/{serialNumber}/load'
```
where
- **serialNumber** - serialNumber of drone;

Result is found information about drone's load with medication information:

```json
{
  "medications": [
    {
      "id": 2,
      "count": 2,
      "name": "Pill Two",
      "weight": 85.0,
      "code": "99877666",
      "image": "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"
    }
  ],
  "currentWeight": 170.0,
  "maxWeight": 255.5
}
```

- **Get List of available to load drones**

```
curl --location 'localhost:8080/drones/available'
```

Result is List of available drones:

```json
[
  {
    "serialNumber": "4A",
    "model": "Heavyweight",
    "weightLimit": 455.5,
    "batteryCapacity": 95,
    "state": "IDLE"
  },
  {
    "serialNumber": "1A",
    "model": "Lightweight",
    "weightLimit": 55.5,
    "batteryCapacity": 95,
    "state": "IDLE"
  }
]
```

- **Get List of available to load drones**

```
curl --location 'localhost:8080/drones/load' \
--header 'Content-Type: application/json' \
--data '{
    "serialNumber": "4A",
    "loadedMedications": [
        {
            "id": 1,
            "count": 2
        },
        {
            "id": 2,
            "count": 1
        }
    ]
}'
```

Result is Drone's information with changed state:

```json
{
  "serialNumber": "4A",
  "model": "Heavyweight",
  "weightLimit": 455.5,
  "batteryCapacity": 95,
  "state": "LOADING"
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