# Candidate app demo

## General Info
- 5 records are loaded when server starts
- Token generation steps
  - register with /api/candidate/register (user and password provided in Postman)
  - login with /api/candidate/login and get a token
  - use the rest of endpoints with token provided
- Postman collections available
- Techs used: SpringBoot 3, Spring Security, OpenAPI, Lombok, JPA, H2 and Java 17 
- Unit tests: JUnit, Mockito
- Swagger UI  http://localhost:8080/swagger-ui/index.html

## Postman collections
- candidate.postman_collection.json for localhost testing
- candidate gcp.postman_collection.json for GCP testing
