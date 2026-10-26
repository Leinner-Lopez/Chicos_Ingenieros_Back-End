# Graph Report - Chicos_Ingenieros_Back-End  (2026-09-17)

## Corpus Check
- Corpus is ~15,021 words - fits in a single context window. You may not need a graph.

## Summary
- 973 nodes · 3426 edges · 23 communities (22 shown, 1 thin omitted)
- Extraction: 84% EXTRACTED · 16% INFERRED · 0% AMBIGUOUS · INFERRED: 532 edges (avg confidence: 0.8)
- Token cost: 44,792 input · 0 output

## Community Hubs (Navigation)
- User Management Core
- Lot & Dashboard Analytics
- Cloudinary & Product API
- Sale FEFO Processing
- Product Image & CRUD
- JWT Token Handling
- Category Management
- Lot Use Cases & Mapping
- Security Filter Testing
- Sale CRUD & Entity
- User CRUD & Domain
- Security Configuration
- DTOs & Entity Annotations
- Exception Handling & Scheduler
- Auth Login & Register
- Repository Adapters
- Mockito Test Setup
- User Entity & Security Details
- Maven Wrapper Script
- User Roles & DTO
- Docker Compose Stack
- Application Bootstrap
- Maven Project Coordinate

## God Nodes (most connected - your core abstractions)
1. `User` - 58 edges
2. `Sale` - 44 edges
3. `Lot` - 43 edges
4. `ProductDTO` - 40 edges
5. `ResourceNotFoundException` - 38 edges
6. `Category` - 37 edges
7. `Product` - 35 edges
8. `UserEntity` - 35 edges
9. `ProductService` - 33 edges
10. `SaleService` - 31 edges

## Surprising Connections (you probably didn't know these)
- `sonarqube service` --conceptually_related_to--> `backend service (Zenkai API)`  [INFERRED]
  docker-compose.sonar.yml → docker-compose.yml
- `AuthService` --references--> `JwtService`  [EXTRACTED]
  src/main/java/com/chicos_ingenieros/zenkai/Auth/Application/AuthService.java → src/main/java/com/chicos_ingenieros/zenkai/Config/Application/JwtService.java
- `AuthService` --references--> `UserService`  [EXTRACTED]
  src/main/java/com/chicos_ingenieros/zenkai/Auth/Application/AuthService.java → src/main/java/com/chicos_ingenieros/zenkai/Users/Application/UserService.java
- `AuthController` --references--> `AuthService`  [EXTRACTED]
  src/main/java/com/chicos_ingenieros/zenkai/Auth/Infrastructure/AuthController.java → src/main/java/com/chicos_ingenieros/zenkai/Auth/Application/AuthService.java
- `CategoryControllerTest` --references--> `CategoryService`  [EXTRACTED]
  src/test/java/com/chicos_ingenieros/zenkai/Categories/Infrastructure/CategoryControllerTest.java → src/main/java/com/chicos_ingenieros/zenkai/Categories/Application/CategoryService.java

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Zenkai stack services sharing zenkai-network** — docker_compose_db, docker_compose_backend, docker_compose_frontend, docker_compose_zenkai_network [EXTRACTED 1.00]
- **SonarQube service and its persistent volumes** — docker_compose_sonar_sonarqube, docker_compose_sonar_sonar_data, docker_compose_sonar_sonar_logs, docker_compose_sonar_sonar_extensions [EXTRACTED 1.00]

## Communities (23 total, 1 thin omitted)

### Community 0 - "User Management Core"
Cohesion: 0.05
Nodes (19): org.springframework.security.crypto.password.PasswordEncoder, PatchMapping, UserCountUseCase, Override, UserService, UserRepository, UserDTO, UserDTOMapper (+11 more)

### Community 1 - "Lot & Dashboard Analytics"
Cohesion: 0.06
Nodes (27): arraylist, bigdecimal, dayofweek, list, localdate, localdatetime, optional, org.springframework.data.jpa.repository.Query (+19 more)

### Community 2 - "Cloudinary & Product API"
Cohesion: 0.06
Nodes (35): annotation, com.cloudinary.Cloudinary, httpstatus, ioexception, lombok.RequiredArgsConstructor, objectutils, org.springframework.http.ResponseEntity, org.springframework.stereotype.Service (+27 more)

### Community 3 - "Sale FEFO Processing"
Cohesion: 0.06
Nodes (16): SaleDetail, SaleDetailRepository, Override, SaleService, SaleRepository, SaleItemRequest, DeleteMapping, GetMapping (+8 more)

### Community 4 - "Product Image & CRUD"
Cohesion: 0.06
Nodes (17): com.cloudinary.Uploader, org.junit.jupiter.api.DisplayName, org.springframework.web.multipart.MultipartFile, Override, Override, ProductCrudUseCase, Product, ProductRepository (+9 more)

### Community 5 - "JWT Token Handling"
Cohesion: 0.05
Nodes (16): date, decoders, function, hashmap, io.jsonwebtoken.Claims, javax.crypto.SecretKey, jwts, keys (+8 more)

### Community 6 - "Category Management"
Cohesion: 0.06
Nodes (19): mapping, CategoryService, Override, CategoryCountUseCase, CategoryCrudUseCase, Category, CategoryRepository, CategoryController (+11 more)

### Community 7 - "Lot Use Cases & Mapping"
Cohesion: 0.08
Nodes (13): org.mapstruct.Mapper, org.mapstruct.Mapping, org.springframework.data.jpa.repository.Modifying, LotCrudUseCase, LotFeFoUseCase, Lot, LotMapper, Override (+5 more)

### Community 8 - "Security Filter Testing"
Cohesion: 0.09
Nodes (37): any, arrays, autowired, com.fasterxml.jackson.databind.ObjectMapper, csrf, eq, httpheaders, httpmethod (+29 more)

### Community 9 - "Sale CRUD & Entity"
Cohesion: 0.10
Nodes (12): org.springframework.data.domain.Pageable, pagerequest, SaleCrudUseCase, Sale, Entity, Table, SaleEntity, SaleMapper (+4 more)

### Community 10 - "User CRUD & Domain"
Cohesion: 0.11
Nodes (9): UserCrudUseCase, User, UserStatus, ACTIVE, INACTIVE, SpringUserRepository, Override, UserMySqlRepository (+1 more)

### Community 11 - "Security Configuration"
Cohesion: 0.08
Nodes (28): abstracthttpconfigurer, bcryptpasswordencoder, customizer, daoauthenticationprovider, org.springframework.boot.test.context.SpringBootTest, org.springframework.boot.test.context.TestConfiguration, org.springframework.boot.testcontainers.service.connection.ServiceConnection, org.springframework.context.annotation.Bean (+20 more)

### Community 12 - "DTOs & Entity Annotations"
Cohesion: 0.17
Nodes (19): lombok.AllArgsConstructor, lombok.Builder, lombok.Data, lombok.NoArgsConstructor, persistence, RegisterRequest, CategoryEntity, Entity (+11 more)

### Community 13 - "Exception Handling & Scheduler"
Cohesion: 0.11
Nodes (18): lombok.EqualsAndHashCode, lombok.extern.slf4j.Slf4j, mockhttpservletrequest, org.springframework.scheduling.annotation.Scheduled, org.springframework.stereotype.Component, org.springframework.web.bind.annotation.ControllerAdvice, org.springframework.web.bind.annotation.ExceptionHandler, org.springframework.web.context.request.WebRequest (+10 more)

### Community 14 - "Auth Login & Register"
Cohesion: 0.17
Nodes (11): badcredentialsexception, org.springframework.security.authentication.AuthenticationManager, org.springframework.web.bind.annotation.PostMapping, AuthService, Override, LoginUserCase, AuthResponse, LoginRequest (+3 more)

### Community 15 - "Repository Adapters"
Cohesion: 0.18
Nodes (8): org.springframework.data.jpa.repository.JpaRepository, org.springframework.stereotype.Repository, SpringCategoryRepository, SaleDetailMapper, Override, SaleDetailMySqlRepository, SpringSaleDetailRepository, SaleDetailMySqlRepositoryTest

### Community 16 - "Mockito Test Setup"
Cohesion: 0.33
Nodes (10): argumentmatchers, assertions, injectmocks, mock, mockito, multipartfile, org.junit.jupiter.api.extension.ExtendWith, org.mockito.junit.jupiter.MockitoExtension (+2 more)

### Community 17 - "User Entity & Security Details"
Cohesion: 0.21
Nodes (7): collection, org.springframework.security.core.GrantedAuthority, simplegrantedauthority, Entity, Override, Table, UserEntity

### Community 18 - "Maven Wrapper Script"
Cohesion: 0.38
Nodes (8): mvnw script, clean(), die(), exec_maven(), hash_string(), set_java_home(), trim(), verbose()

### Community 19 - "User Roles & DTO"
Cohesion: 0.28
Nodes (6): mappers, spy, Role, ADMIN, CUSTOMER, STORE

### Community 20 - "Docker Compose Stack"
Cohesion: 0.32
Nodes (8): backend service (Zenkai API), db service (MySQL 8.0), frontend service (Zenkai Web), sonar_data volume, sonar_extensions volume, sonar_logs volume, sonarqube service, zenkai-network bridge network

### Community 21 - "Application Bootstrap"
Cohesion: 0.38
Nodes (5): dotenv, org.springframework.boot.autoconfigure.SpringBootApplication, org.springframework.scheduling.annotation.EnableScheduling, springapplication, ZenkaiApplication

## Knowledge Gaps
- **12 isolated node(s):** `com.chicos_ingenieros:Zenkai`, `EXPIRED`, `AVAILABLE`, `EMPTY`, `ADMIN` (+7 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 123 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **1 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `User` connect `User CRUD & Domain` to `User Management Core`, `Cloudinary & Product API`, `JWT Token Handling`, `Security Filter Testing`, `DTOs & Entity Annotations`, `Auth Login & Register`, `Mockito Test Setup`, `User Roles & DTO`?**
  _High betweenness centrality (0.042) - this node is a cross-community bridge._
- **Why does `ProductService` connect `Cloudinary & Product API` to `Lot & Dashboard Analytics`, `Sale FEFO Processing`, `Product Image & CRUD`, `Lot Use Cases & Mapping`, `Security Filter Testing`?**
  _High betweenness centrality (0.031) - this node is a cross-community bridge._
- **Why does `Lot` connect `Lot Use Cases & Mapping` to `Lot & Dashboard Analytics`, `Cloudinary & Product API`, `Sale FEFO Processing`, `JWT Token Handling`, `Security Filter Testing`, `DTOs & Entity Annotations`, `Mockito Test Setup`?**
  _High betweenness centrality (0.031) - this node is a cross-community bridge._
- **Are the 6 inferred relationships involving `User` (e.g. with `.isTokenValid_withWrongUser_returnsFalse()` and `.findAll_ShouldReturnListOfUsers()`) actually correct?**
  _`User` has 6 INFERRED edges - model-reasoned connections that need verification._
- **What connects `com.chicos_ingenieros:Zenkai`, `EXPIRED`, `AVAILABLE` to the rest of the system?**
  _12 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `User Management Core` be split into smaller, more focused modules?**
  _Cohesion score 0.050686641697877656 - nodes in this community are weakly interconnected._
- **Should `Lot & Dashboard Analytics` be split into smaller, more focused modules?**
  _Cohesion score 0.06424050632911392 - nodes in this community are weakly interconnected._