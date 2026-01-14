# discount-springboot-ms
A quick demo for a discount microservice built in Java SpringBoot

To run in DEV, use:
mvn spring-boot:run -Dspring-boot.run.profiles=dev

If using PowerShell:
mvn clean spring-boot:run "-Dspring-boot.run.profiles=dev"

To run in Docker, user:
docker compose up --build
docker compose up


To test, use:
mvn clean test -Dspring.profiles.active=<env>

___________________API___________________

DEV port: 8080, Docker port: 70

localhost:port/api/customers
localhost:port/api/customers/{id}

localhost:port/api/bills/all
localhost:port/api/bills/id/{id}
localhost:port/api/bills/add                -> this one is POST

sample bill request JSON to use in body of POST HTTP
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
