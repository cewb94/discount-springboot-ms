# discount-springboot-ms
A quick demo for a discount microservice built in Java SpringBoot

To run in DEV, use:
mvn spring-boot:run -Dspring-boot.run.profiles=dev

To run in Docker, user:
docker compose up --build
docker compose up


To test, use:
mvn clean test -Dspring.profiles.active=<env>
