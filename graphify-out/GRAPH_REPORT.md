# Graph Report - Chicos_Ingenieros_Back-End  (2026-11-03)

## Corpus Check
- 28 files · ~17,645 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 1159 nodes · 3901 edges · 43 communities (35 shown, 8 thin omitted)
- Extraction: 85% EXTRACTED · 15% INFERRED · 0% AMBIGUOUS · INFERRED: 566 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- Test Mocks & Assertions
- Auth Tests & Exceptions
- Category Module
- User Use Cases
- Security & Auth Config
- Lot Use Cases & Repos
- Test Utilities & Jackson
- Backup Controller & Download
- Lot Service
- Backup Service
- Cloudinary & HTTP Utils
- Date & Functional Utils
- REST Controller Annotations
- Product Repository Counts
- Exception Domain
- Product MySQL Adapter
- Sale MySQL Adapter
- Sale Details & Cancel
- Backup Domain & Types
- Sale Entity & Mapping
- FEFO Lot Queries
- Dashboard Admin
- Backup JSON Transactions
- Delete/Get Endpoints
- Backup Test Captors
- Product CRUD
- Image Upload
- JWT Filter & Servlet
- HTTP Verb Mappings
- Docker Compose Stack
- Purchase History
- Sale Queries
- JDBC Backup Adapter
- Maven Wrapper
- Dashboard Metrics
- Local Backup Storage
- Lots Expiring Soon
- Backup Checksums
- Lot Status Scheduler
- App Bootstrap
- Backup Row Writer
- Sale CRUD
- Project Root

## God Nodes (most connected - your core abstractions)
1. `User` - 55 edges
2. `Sale` - 44 edges
3. `Lot` - 43 edges
4. `ProductDTO` - 40 edges
5. `Backup` - 40 edges
6. `Category` - 37 edges
7. `ResourceNotFoundException` - 36 edges
8. `Product` - 35 edges
9. `UserEntity` - 35 edges
10. `ProductService` - 33 edges

## Surprising Connections (you probably didn't know these)
- `sonarqube service` --conceptually_related_to--> `backend service (Zenkai API)`  [INFERRED]
  docker-compose.sonar.yml → docker-compose.yml
- `UserService` --implements--> `UserCrudUseCase`  [EXTRACTED]
  src/main/java/com/chicos_ingenieros/zenkai/Users/Application/UserService.java → src/main/java/com/chicos_ingenieros/zenkai/Users/Application/UseCases/UserCrudUseCase.java
- `UserService` --references--> `UserDTOMapper`  [EXTRACTED]
  src/main/java/com/chicos_ingenieros/zenkai/Users/Application/UserService.java → src/main/java/com/chicos_ingenieros/zenkai/Users/Infrastructure/Mapper/UserDTOMapper.java
- `UserController` --references--> `UserService`  [EXTRACTED]
  src/main/java/com/chicos_ingenieros/zenkai/Users/Infrastructure/UserController.java → src/main/java/com/chicos_ingenieros/zenkai/Users/Application/UserService.java
- `ApplicationConfig` --references--> `UserRepository`  [EXTRACTED]
  src/main/java/com/chicos_ingenieros/zenkai/Config/Infrastructure/ApplicationConfig.java → src/main/java/com/chicos_ingenieros/zenkai/Users/Domain/UserRepository.java

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **SonarQube service and its persistent volumes** — docker_compose_sonar_sonarqube, docker_compose_sonar_sonar_data, docker_compose_sonar_sonar_logs, docker_compose_sonar_sonar_extensions [EXTRACTED 1.00]
- **Zenkai stack services sharing zenkai-network** — docker_compose_db, docker_compose_backend, docker_compose_frontend, docker_compose_zenkai_network [EXTRACTED 1.00]

## Communities (43 total, 8 thin omitted)

### Community 0 - "Test Mocks & Assertions"
Cohesion: 0.05
Nodes (60): argumentmatchers, assertions, bigdecimal, collection, dayofweek, injectmocks, list, localdate (+52 more)

### Community 1 - "Auth Tests & Exceptions"
Cohesion: 0.05
Nodes (19): org.junit.jupiter.api.DisplayName, org.junit.jupiter.api.Test, org.springframework.security.crypto.password.PasswordEncoder, ResourceNotFoundException, UserCountUseCase, Override, UserService, UserRepository (+11 more)

### Community 2 - "Category Module"
Cohesion: 0.06
Nodes (19): mapping, org.springframework.stereotype.Repository, CategoryService, Override, CategoryCountUseCase, CategoryCrudUseCase, Category, CategoryRepository (+11 more)

### Community 3 - "User Use Cases"
Cohesion: 0.07
Nodes (20): UserCrudUseCase, Role, ADMIN, CUSTOMER, STORE, User, UserStatus, ACTIVE (+12 more)

### Community 4 - "Security & Auth Config"
Cohesion: 0.06
Nodes (46): abstracthttpconfigurer, badcredentialsexception, bcryptpasswordencoder, com.chicos_ingenieros.zenkai.Auth.Infrastructure.AuthResponse, com.chicos_ingenieros.zenkai.Auth.Infrastructure.LoginRequest, com.chicos_ingenieros.zenkai.Auth.Infrastructure.RegisterRequest, com.chicos_ingenieros.zenkai.Config.Application.JwtService, com.chicos_ingenieros.zenkai.Config.Infrastructure.Filter.JwtAuthFilter (+38 more)

### Community 5 - "Lot Use Cases & Repos"
Cohesion: 0.10
Nodes (10): org.mapstruct.Mapper, org.springframework.data.jpa.repository.Modifying, LotCrudUseCase, LotFeFoUseCase, Lot, LotMapper, Override, LotMySqlRepository (+2 more)

### Community 6 - "Test Utilities & Jackson"
Cohesion: 0.12
Nodes (24): any, arrays, autowired, com.fasterxml.jackson.databind.ObjectMapper, csrf, eq, get, httpmethod (+16 more)

### Community 7 - "Backup Controller & Download"
Cohesion: 0.09
Nodes (17): contentdisposition, GetMapping, inputstreamresource, java.security.Principal, org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration, org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration, org.springframework.core.io.Resource, PostMapping (+9 more)

### Community 8 - "Lot Service"
Cohesion: 0.10
Nodes (7): Override, LotService, DashboardAdminUseCase, LotCountUseCase, LotStatusUpdateUseCase, LotRepository, LotServiceTest

### Community 9 - "Backup Service"
Cohesion: 0.14
Nodes (6): BackupService, Override, BackupDataPort, BackupRepository, BackupStoragePort, BackupServiceTest

### Community 10 - "Cloudinary & HTTP Utils"
Cohesion: 0.15
Nodes (20): annotation, arraylist, com.cloudinary.Cloudinary, httpstatus, lombok.RequiredArgsConstructor, objectutils, org.springframework.stereotype.Service, org.springframework.web.bind.annotation.RequestMapping (+12 more)

### Community 11 - "Date & Functional Utils"
Cohesion: 0.11
Nodes (14): date, decoders, function, hashmap, io.jsonwebtoken.Claims, javax.crypto.SecretKey, jwts, keys (+6 more)

### Community 12 - "REST Controller Annotations"
Cohesion: 0.12
Nodes (14): org.springframework.http.ResponseEntity, PatchMapping, DeleteMapping, GetMapping, RequestMapping, RestController, SaleController, DeleteMapping (+6 more)

### Community 13 - "Product Repository Counts"
Cohesion: 0.16
Nodes (3): Override, ProductRepository, ProductServiceTest

### Community 14 - "Exception Domain"
Cohesion: 0.16
Nodes (15): com.chicos_ingenieros.zenkai.Exceptions.Domain.ErrorDetails, com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceDuplicateException, com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException, lombok.EqualsAndHashCode, lombok.extern.slf4j.Slf4j, mockhttpservletrequest, org.springframework.web.bind.annotation.ControllerAdvice, org.springframework.web.bind.annotation.ExceptionHandler (+7 more)

### Community 15 - "Product MySQL Adapter"
Cohesion: 0.17
Nodes (4): Override, ProductMySqlRepository, SpringProductRepository, ProductMySqlRepositoryTest

### Community 16 - "Sale MySQL Adapter"
Cohesion: 0.19
Nodes (5): org.springframework.data.domain.Pageable, Override, SaleMySqlRepository, SpringSaleRepository, SaleMySqlRepositoryTest

### Community 17 - "Sale Details & Cancel"
Cohesion: 0.15
Nodes (3): pagerequest, Override, SaleRepository

### Community 18 - "Backup Domain & Types"
Cohesion: 0.17
Nodes (12): Backup, BackupType, MANUAL, PRE_PURGE, PRE_RESTORE, BackupEntity, Entity, Table (+4 more)

### Community 19 - "Sale Entity & Mapping"
Cohesion: 0.24
Nodes (6): org.mapstruct.Mapping, Sale, Entity, Table, SaleEntity, SaleMapper

### Community 20 - "FEFO Lot Queries"
Cohesion: 0.21
Nodes (4): SaleDetailRepository, SaleItemRequest, PostMapping, SaleServiceTest

### Community 21 - "Dashboard Admin"
Cohesion: 0.16
Nodes (5): DashboardAdminUseCase, InventoryUseCase, ProductCountUseCase, ProductDTO, ProductInventoryDTO

### Community 22 - "Backup JSON Transactions"
Cohesion: 0.25
Nodes (5): com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.JsonNode, org.springframework.transaction.annotation.Transactional, Override, JdbcBackupDataAdapter

### Community 23 - "Delete/Get Endpoints"
Cohesion: 0.15
Nodes (7): DeleteMapping, GetMapping, PostMapping, PutMapping, RequestMapping, RestController, ProductController

### Community 24 - "Backup Test Captors"
Cohesion: 0.16
Nodes (11): anystring, argumentcaptor, bytearrayinputstream, bytearrayoutputstream, datetimeformatter, domain, gzipinputstream, gzipoutputstream (+3 more)

### Community 25 - "Product CRUD"
Cohesion: 0.19
Nodes (3): ProductCrudUseCase, Product, ProductDTOMapper

### Community 26 - "Image Upload"
Cohesion: 0.20
Nodes (7): com.cloudinary.Uploader, ioexception, org.springframework.web.multipart.MultipartFile, Override, CloudinaryUploadImageUseCase, CloudinaryServiceTest, valid

### Community 27 - "JWT Filter & Servlet"
Cohesion: 0.21
Nodes (11): httpheaders, jakarta.servlet.FilterChain, jakarta.servlet.http.HttpServletRequest, jakarta.servlet.http.HttpServletResponse, org.springframework.web.filter.OncePerRequestFilter, securitycontextholder, servletexception, Override (+3 more)

### Community 28 - "HTTP Verb Mappings"
Cohesion: 0.18
Nodes (7): DeleteMapping, GetMapping, PostMapping, PutMapping, RequestMapping, RestController, LotController

### Community 29 - "Docker Compose Stack"
Cohesion: 0.27
Nodes (10): backend service (Zenkai API), ./Backups volume (/app/backups), db service (MySQL 8.0), frontend service (Zenkai Web), zenkai-network, sonar_data volume, sonar_extensions volume, sonar_logs volume (+2 more)

### Community 32 - "JDBC Backup Adapter"
Cohesion: 0.20
Nodes (9): biginteger, deserializationfeature, jsontoken, org.springframework.jdbc.core.JdbcTemplate, resultsetmetadata, rowcallbackhandler, sqlexception, types (+1 more)

### Community 33 - "Maven Wrapper"
Cohesion: 0.38
Nodes (8): mvnw script, clean(), die(), exec_maven(), hash_string(), set_java_home(), trim(), verbose()

### Community 37 - "Backup Checksums"
Cohesion: 0.25
Nodes (7): files, hexformat, messagedigest, nosuchalgorithmexception, path, standardopenoption, uncheckedioexception

### Community 38 - "Lot Status Scheduler"
Cohesion: 0.43
Nodes (4): com.chicos_ingenieros.zenkai.Lots.Application.UseCases.LotStatusUpdateUseCase, org.springframework.scheduling.annotation.Scheduled, org.springframework.stereotype.Component, LotStatusScheduler

### Community 39 - "App Bootstrap"
Cohesion: 0.38
Nodes (5): dotenv, org.springframework.boot.autoconfigure.SpringBootApplication, org.springframework.scheduling.annotation.EnableScheduling, springapplication, ZenkaiApplication

## Knowledge Gaps
- **16 isolated node(s):** `AVAILABLE`, `EMPTY`, `EXPIRED`, `ACTIVE`, `INACTIVE` (+11 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 160 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **8 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `User` connect `User Use Cases` to `Test Mocks & Assertions`, `Auth Tests & Exceptions`, `Test Utilities & Jackson`, `Cloudinary & HTTP Utils`, `Date & Functional Utils`, `REST Controller Annotations`?**
  _High betweenness centrality (0.034) - this node is a cross-community bridge._
- **Why does `IlegalActionException` connect `Exception Domain` to `JDBC Backup Adapter`, `Test Mocks & Assertions`, `Auth Tests & Exceptions`, `Local Backup Storage`, `Security & Auth Config`, `Backup Checksums`, `Test Utilities & Jackson`, `Backup Service`, `Cloudinary & HTTP Utils`, `FEFO Lot Queries`, `Backup JSON Transactions`, `Backup Test Captors`?**
  _High betweenness centrality (0.032) - this node is a cross-community bridge._
- **Why does `LotService` connect `Lot Service` to `Auth Tests & Exceptions`, `Dashboard Metrics`, `Lots Expiring Soon`, `Lot Use Cases & Repos`, `Test Utilities & Jackson`, `Cloudinary & HTTP Utils`, `HTTP Verb Mappings`?**
  _High betweenness centrality (0.025) - this node is a cross-community bridge._
- **What connects `AVAILABLE`, `EMPTY`, `EXPIRED` to the rest of the system?**
  _16 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Test Mocks & Assertions` be split into smaller, more focused modules?**
  _Cohesion score 0.05459171871385612 - nodes in this community are weakly interconnected._
- **Should `Auth Tests & Exceptions` be split into smaller, more focused modules?**
  _Cohesion score 0.05123428039124359 - nodes in this community are weakly interconnected._
- **Should `Category Module` be split into smaller, more focused modules?**
  _Cohesion score 0.05754527162977867 - nodes in this community are weakly interconnected._