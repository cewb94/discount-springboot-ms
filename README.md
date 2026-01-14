# discount-springboot-ms
A quick demo for a discount microservice built in Java SpringBoot

To run in DEV, use:\
mvn spring-boot:run -Dspring-boot.run.profiles=dev


If using PowerShell:\
mvn clean spring-boot:run "-Dspring-boot.run.profiles=dev"


To run in Docker, user:\
docker compose up --build\
docker compose up


## Testing & Code Coverage

Run unit tests:\
mvn test

Run unit tests + generate JaCoCo coverage report:\
mvn verify


## Cloud Deployment (AWS ECS)

This service can be deployed to AWS ECS as a standalone application container.  
The provided `docker-compose.yml` is intended for local development only and will not work in ECS, as ECS does not run Docker Compose setups.

To build the application image, package the Spring Boot app and build the Docker image:

mvn clean package  
docker build -t discount-springboot-ms .

In a cloud setup, the database must be created separately (for example using Amazon RDS for PostgreSQL).  
The application container does not include PostgreSQL. After creating the database, run the provided `.sql` initialization script manually and configure the database connection using ECS environment variables.


________________________API________________________

DEV port: 8080, Docker port: 70


localhost:port/api/customers\
localhost:port/api/customers/{id}


localhost:port/api/bills/all\
localhost:port/api/bills/id/{id}\
localhost:port/api/bills/add                -> this one is POST


sample bill request JSON to use in body of POST HTTP


**Request Body**
```json
{
  "customerId": 1,
  "lines": [
    {
      "itmId": 1,
      "itmCategory": "GROCERY",
      "price": 2.00,
      "quantity": 3
    },
    {
      "itmId": 11,
      "itmCategory": "NON_GROCERY",
      "price": 10.00,
      "quantity": 1
    }
  ]
}
