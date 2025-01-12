# Travel Buddy

Travel Buddy is an application which makes multi-modal travel planning easier. The microservices reside in this repository.

## Microservices

### [timetable-service](TimetableService) 

Port: 8080
OpenAPI endpoint: /swagger

This service stores static timetables and routes. The data can imported via the GTFS static feed. 

### [account-manager](account-manager) 

Port: 8091
OpenAPI endpoint: /openapi

This service allows for registering new users and logging in.  

### [crowdsourcing-service](crowdsourcing-service)

Port: 8082
OpenAPI endpoint: /swagger

This service stores crowd-sourced data about departure delays and allows for calculating the average delay for a certain departure.

### [poi-service](poiService)

Port: 8090
OpenAPI endpoint: /openapi

This service implements adding and retrieving points of interest.

### [purchase-service](purchase-service)

Port: 8094
OpenAPI endpoint: /api-specs/ui

This service implements creating and confirming purchase transactions.

### [routing-service](routing-service)

Port: 8081
OpenAPI endpoint: /swagger

This service retrieves and filters data from the NCUP API, which returns trip routes.

### [purchase-service](ticket-search-service)

Port: 8093
OpenAPI endpoint: /api-specs/ui

This service allow for searching for tickets for a specific departure.

### [user-manager](user-manager)

Port: 8092
OpenAPI endpoint: /openapi

This service allows for interacting with data associated with a specific user.

## Making a new microservice

No matter which build system you are using (Maven, Gradle, ...), each microservice must contain three subprojects:
- `api` for defining REST endpoints/resource classes
- `entity` for defining entities
- `service` for defining services which interact with the DB

![Screenshot from 2025-01-12 23-01-18](https://github.com/user-attachments/assets/65163c41-ac4e-4b91-ab63-068427fcf6f6)

All of these subprojects must be in a shared group which starts with `si.travelbuddy` (for example, `si.travelbuddy.timetable`). 
The parent project must be in the `si.travelbuddy` group.
