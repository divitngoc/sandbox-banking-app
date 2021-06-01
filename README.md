# sandbox-banking-app

This Sandbox Banking project allows creation of Users, Accounts, Balances and internal transfer of funds.
Every transfers will generate a transaction on both the payee and payer accounts.

## Diagram
![banking-app-diagram](https://user-images.githubusercontent.com/26686429/120256617-6d4b7500-c286-11eb-8601-24d22e174677.png)

## Prerequisite
- Java 11
- Maven 3.5.3+
- lombok (https://projectlombok.org/ Remember to restart your IDE if you're installing lombok into your IDE)
- Docker CE

## How to run
Since we're using Jooq for our db layer, we need the database up and running so we can generate our models. We can do a ```docker-compose up -d``` on root project directory to start all of our services.
After that, we can do a ```mvn generate-sources``` on the root pom.xml.

You can either start the application through your IDE or run ```mvn package``` and run it as a jar.

## How it works
Only authorized users with the role "user" can call our API application by having ```Authorization: Bearer + access_token``` on the request headers. Our applications will validate the access token from the public JWK provided by Keycloak. After creating a User on keycloak, the user can authorize Keycloak to give access to our banking-application by generating an access token from Keycloak. Using the newly generated access token, we can make calls to our banking-application.

Access tokens are short-lived by default but can be newly generated with a refresh token.

## Docs
- JSON: http://localhost:8080/v3/api-docs/
- UI: http://localhost:8080/swagger-ui.html

## Postman
We can import postman collection located at "postman/" directory. This has all our application endpoints and token endpoints from Keycloak.

## End to End tests
Since this is all API based, we can use our Postman to run our End-to-End testing after starting up our Java banking-application. To run tests from the Postman, click the 3 dots from our imported collection ("Banking Application e2e API"), click on "Run collection" from the dropdown and then click Run Banking Application e2e API.

Each endpoint will have a Test script in them.

![banking-app-postman-ss](https://user-images.githubusercontent.com/26686429/120259992-e0f08080-c28c-11eb-90d1-f22511e1e07e.png)

## Limitations
- Performance will likely to suffer from huge traffic due to locking of the database for transfers (Proposal: have read replicas of DB).
- Authorizing via password is not great and is a security risk. (Proposal: Redirecting them directly to Keycloak site, and then have Keycloak to redirect to us with code after the user has authorized. Our application can then exchange it for an access-token which is much more secured.)
