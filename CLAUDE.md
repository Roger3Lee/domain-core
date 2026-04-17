# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a **Domain-Driven Design (DDD) Code Generation Framework** that generates DDD scaffolding code including entities, aggregates, repositories, and domain services from XML configurations. The framework supports multiple database systems (MySQL, PostgreSQL, Oracle, PolarDB) with database-specific batch operations.

**Technology Stack:**
- Java 17
- Spring Boot 2.6.7
- MyBatis Plus 3.5.3.1
- MapStruct 1.4.2.Final
- Hutool 5.8.23
- Maven (multi-module project)

## Project Structure

This is a multi-module Maven project:

```
domain-core/                          # Root project
├── domain-dependencies/              # Dependency management BOM
├── domain-core/                      # Core framework (base classes, utils)
│   ├── batch/                       # Batch operation support
│   ├── domain/                      # Base domain classes
│   ├── repository/                  # Repository interfaces
│   └── config/                      # MyBatis and framework configuration
├── domain-generator/                 # Code generator core
│   ├── domain/                      # Domain model generators
│   └── table/                       # Table-based generators
├── domain-mysql-support/            # MySQL-specific batch operations
├── domain-postgresql-support/       # PostgreSQL-specific batch operations
├── domain-oracle-support/           # Oracle-specific batch operations
├── domain-polardb-support/          # PolarDB-specific batch operations
├── domain-web-generator/            # Web layer code generator
├── domain-sample/                   # Sample Spring Boot application
└── config/                          # Configuration files
    ├── domain-config.xml            # Domain model definitions
    └── SQL/                         # Sample SQL scripts
```

## Build and Development Commands

### Build Project
```bash
# Build all modules
./mvnw clean install

# Build without tests
./mvnw clean install -DskipTests

# Build specific module
./mvnw clean install -pl domain-core -am

# Compile only
./mvnw clean compile
```

### Running Tests
```bash
# Run all tests
./mvnw test

# Run tests for specific module
./mvnw test -pl domain-sample

# Run specific test class
./mvnw test -Dtest=FamilyControllerTest

# Run with coverage
./mvnw clean test jacoco:report
```

### Running the Sample Application
```bash
# Navigate to sample module
cd domain-sample

# Run Spring Boot application
../mvnw spring-boot:run

# Run with specific profile
../mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Package and run
../mvnw package
java -jar target/domain-sample-1.0-SNAPSHOT.jar
```

### Code Generation
```bash
# Generate code from domain-config.xml
# The generator reads config/domain-config.xml and generates:
# - Domain classes (*Domain.java)
# - Service interfaces (*Service.java)
# - Repository interfaces (*Repository.java)
# - Converters (*Convertor.java)
# - Lambda expressions (*LambdaExp.java)

# Run generator (typically through main class or IDE)
# See domain-generator module for generator entry points
```

## Architecture and Core Concepts

### DDD Code Generation Workflow

1. **Define Domain Model in XML** (`config/domain-config.xml`):
   ```xml
   <domains>
       <domain name="Family" description="家庭领域模型" main-table="family">
           <related description="家庭住址" table="family_address" many="false" 
                    fk="id:family_id" redundancy="name:family_name"/>
           <related description="家庭成员" table="family_member" many="true" 
                    fk="id:family_id" redundancy="name:family_name"/>
       </domain>
   </domains>
   ```

2. **Generated Code Structure**:
   - `FamilyDomain` - Aggregate root extending `BaseAggregateDomain`
   - `FamilyService` - Domain service interface
   - `FamilyRepository` - Repository interface for data access
   - `FamilyConvertor` - MapStruct converter for DTO ↔ Domain
   - `FamilyLambdaExp` - Type-safe lambda expressions for queries
   - Related domain classes: `FamilyAddressDomain`, `FamilyMemberDomain`

3. **Key Architectural Patterns**:
   - **Aggregate Root**: Main entity managing lifecycle of related entities
   - **Repository Pattern**: Data access abstraction
   - **Domain Service**: Business logic orchestration
   - **Immutable LoadFlag**: Controls which related entities to load
   - **CacheDomain**: Internal caching for referenced relationships

### Domain Model Loading Patterns

```java
// Load aggregate root by ID
FamilyDomain domain = FamilyDomain.load(id, familyService);

// Load related entities
domain.loadRelated(FamilyDomain.FamilyAddressDomain.class);
domain.loadRelated(FamilyDomain.FamilyMemberDomain.class, query -> {
    query.eq(FamilyMemberDomain::getAge, 18)
         .orderBy(FamilyMemberDomain::getCreateTime, Order.DESC);
});

// Access loaded data
FamilyAddressDomain address = domain.getFamilyAddress();
List<FamilyMemberDomain> members = domain.getFamilyMemberList();
```

### CRUD Operations with Domain Service

```java
// Insert (auto-handles FK and redundancy fields)
Integer id = service.insert(domain);

// Update (compares differences, only updates changed fields)
service.update(newDomain, originalDomain);

// Delete (cascade deletes related entities)
service.delete(id);
service.delete(id, loadFlag); // Control cascade scope

// Query with pagination
IPage<FamilyDomain> page = service.queryPage(
    FamilyDomain.class, 
    pageDomain, 
    lambdaQuery
);
```

### Database-Specific Batch Operations

Each database support module provides optimized batch operations:

- **MySQL**: `ON DUPLICATE KEY UPDATE`, `REPLACE INTO`, `INSERT IGNORE`
- **PostgreSQL**: `ON CONFLICT`, `COPY`, `RETURNING`, `MERGE`
- **Oracle**: `INSERT ALL`, `MERGE`
- **PolarDB**: `INSERT ALL`, `MERGE` (Oracle-compatible)

Batch methods automatically handle:
- Primary key generation and backfill
- Sequence-based IDs (`@KeySequence` annotation)
- Null field handling (ignore null strategy)
- Logical delete field filtering

### MyBatis Plus Configuration

The framework provides comprehensive MyBatis Plus configuration:

- **Pagination Plugin**: Auto-detects DB type, limits max 10000 records
- **Custom ID Generator**: Snowflake algorithm or timestamp-based
- **Optimistic Lock Plugin**: Auto-handles `@Version` fields
- **Block Attack Plugin**: Prevents UPDATE/DELETE without WHERE clause
- **Enhanced Batch Methods**: Via `BatchBaseMapper` interface

## Important Development Rules

### Generated Code - DO NOT MODIFY
**CRITICAL**: All auto-generated domain model code must NOT be modified manually:
- `*Domain.java` classes
- `*Service.java` interfaces
- `*Repository.java` interfaces
- `*LambdaExp.java` classes

To change domain structure, update `domain-config.xml` and regenerate.

### Layered Architecture Call Chain
Follow this strict call chain:
```
Controller → AppService → DomainService → Repository → Mapper
```

Never skip layers or call Mapper directly from Controller/AppService.

### XML Configuration Elements

**Domain Element (Aggregate Root)**:
- `name`: Domain name for class generation (e.g., "Family" → `FamilyDomain`)
- `description`: API documentation description
- `main-table`: Primary database table

**Related Element (Associated Entity)**:
- `table`: Associated table name
- `many`: `true` for one-to-many, `false` for one-to-one
- `fk`: Foreign key mapping "source_field:target_field"
- `redundancy`: Redundant field mapping "source_field:target_field"

**Ref Element (Reference Relationship)**:
- `name`: Reference field name
- `table`: Referenced table
- `fk`: Reference key mapping
- In-memory filtering using lambda predicates, with caching

### MapStruct Converters
Use MapStruct for all DTO ↔ Domain conversions:
```java
@Mapper(componentModel = "spring")
public interface FamilyConvertor {
    FamilyConvertor INSTANCE = Mappers.getMapper(FamilyConvertor.class);
    
    FamilyDomain toDomain(FamilyAddRequest request);
    FamilyResponse toResponse(FamilyDomain domain);
}
```

### LoadFlag Pattern
Control related entity loading scope:
```java
domain.setLoadFlag(FamilyDomain.LoadFlag.builder()
    .loadFamilyAddressDomain(true)
    .loadFamilyMemberDomain(true)
    .build());
```

### Version Compatibility Issues

**KeySequence Annotation**: 
- MyBatis Plus 3.5.2 and below: `TableInfo.getKeySequence()` not available
- Use `TableInfoUtils.getKeySequence(tableInfo)` for version compatibility
- See `KEYSEQUENCE_ISSUE_SOLUTION.md` for details

## Testing

### Test Endpoints (domain-sample module)
- `GET /family/v1/page` - Pagination test
- `GET /family/v1/test-id-generator` - ID generator test
- `POST /family/v1/batch-insert` - Batch insert test
- `GET /family/v1/config-overview` - Configuration overview

### Writing Tests
Follow AAA pattern (Arrange-Act-Assert):
```java
@Test
public void testFamilyService() {
    // Arrange
    FamilyDomain domain = createTestDomain();
    
    // Act
    Integer id = familyService.insert(domain);
    
    // Assert
    assertNotNull(id);
    assertEquals(domain.getName(), loadedDomain.getName());
}
```

## Common Troubleshooting

### Build Issues
- **Java version**: Ensure Java 17 is installed and JAVA_HOME is set
- **Maven wrapper**: Use `./mvnw` (Unix) or `mvnw.cmd` (Windows)
- **Module dependencies**: Build with `-am` to include dependencies

### Code Generation Issues
- Check `domain-config.xml` syntax and table names
- Verify database connection in generator configuration
- Ensure all referenced tables exist in database

### MyBatis Plus Issues
- **Pagination not working**: Verify `MybatisPlusInterceptor` bean is configured
- **ID not generated**: Use `IdType.ASSIGN_ID` with custom generator
- **Batch methods missing**: Mapper must extend `BatchBaseMapper`

### Performance Optimization
- Use `LoadFlag` to avoid loading unnecessary relationships
- Batch operations for bulk inserts/updates (limit ~1000 records per batch)
- Cache reference relationships using `CacheDomain`
- Proper pagination with reasonable page sizes (≤100)

## Reference Documentation

- `README.md` - Project introduction and database support
- `KEYSEQUENCE_ISSUE_SOLUTION.md` - KeySequence annotation compatibility
- `MYBATIS_CONFIGURATION_GUIDE.md` - MyBatis Plus configuration details
- `.cursor/rules/` - Detailed development guidelines and patterns
