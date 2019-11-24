# Roche Orders

## Usage
This is a project using maven and jdk 8.

Download as a zip or git clone https://github.com/andreypereverzin/orders-app

Run the following command to build the application:

`mvn clean install`

To start the application run this command:

`java -jar target/roche-orders.jar`

## Application Endpoints
After the application is started its Swagger documentation is available on the URL:

http://localhost:8080/roche-orders/swagger-ui.html

(in JSON format Swagger documentation is available on this URL: http://localhost:8080/roche-orders/v2/api-docs)

## Actuator Endpoints
The following actuator endpoints are available after the application is started:

http://localhost:8080/roche-orders/actuator/health

http://localhost:8080/roche-orders/actuator/info

## Implementation Notes
The specification can be found in `doc/Home_Assignment.pdf` file.

This implementation uses h2 in-memory database for data persistence.

DELETE endpoint is not implemented for orders as this is not defined by the specification.

As defined by the specification products are not physically deleted from the database, 
`is_active` boolean column is used as an indicator of 'soft deletion'.

## Outstanding Issues
1) HATEOAS should be used when building REST responses
2) Pagination should be implemented in GET endpoints for multiple orders and products
3) Error responses should be more informative and contain detailed description of the error
4) In real application proper SQL database should be used instead of h2 in-memory database
5) Other Actuator endpoints should be enabled (/metrics, /env etc)
