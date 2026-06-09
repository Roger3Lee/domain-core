---
name: ddd-code-generator
description: Generate DDD domain model code (entities, mappers, domain DTOs, repositories, services, controllers) from domain XML configuration and SQL DDL. Use when the user asks to generate domain model code, scaffold DDD layers, create CRUD boilerplate from SQL/XML, or mentions domain generation, code generation for domain-core framework.
---

# ddd-code-generator

Generates a complete DDD domain model layer stack from a domain XML config + SQL DDL file, using the **domain-generator-v2** JAR tool.

## Prerequisites

- JDK 8+ installed (required to run the JAR)
- The JAR is bundled in this skill directory: `domain-generator-v2-3.0.2.jar`

If the JAR needs to be rebuilt from source, build it from the `domain-generator-v2` module:

```bash
# Set JAVA_HOME to JDK 11+ for Gradle build
./gradlew :domain-generator-v2:shadowJar
# Then copy the JAR into the skill directory
cp domain-generator-v2/build/libs/domain-generator-v2-3.0.2.jar skills/ddd-code-generator/domain-generator-v2-3.0.2.jar
```

## Inputs Required

The user must provide:

1. **Base package path** (e.g. `com.example.project`)
2. **Domain XML** — domain configuration file or enough info to construct it
3. **SQL DDL file** — CREATE TABLE statements for all tables involved
4. **SQL dialect** — `mysql` (default) or `postgresql`

## Generation Workflow

### Step 1: Prepare Domain XML and SQL DDL

If the user doesn't have existing files, create them:

**Domain XML** (`config/domain-config.xml`):

```xml
<domains>
  <domain name="domainName" description="描述" main-table="main_table"
          folder="optionalFolder" implement="optionalInterface">
    <related table="related_table" many="true|false"
             fk="sourceCol:targetCol" redundancy="sourceCol:targetCol"
             deletable="true|false" implement="optionalInterface">
      <ref table="ref_table" many="true|false"
           fk="sourceCol:targetCol" redundancy="sourceCol:targetCol"/>
    </related>
  </domain>
</domains>
```

**MySQL SQL DDL** (`config/SQL/domain-sample-mysql.sql`):

```sql
DROP TABLE IF EXISTS table_name;
CREATE TABLE table_name(
    `ID` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `col_name` VARCHAR(255) COMMENT '描述',
    PRIMARY KEY (ID)
) COMMENT = '表描述';
```

**PostgreSQL SQL DDL** (`config/SQL/domain-sample-pg.sql`):

```sql
DROP TABLE IF EXISTS table_name;
CREATE TABLE table_name(
    ID BIGSERIAL PRIMARY KEY,
    col_name VARCHAR(255)
);
COMMENT ON TABLE table_name IS '表描述';
COMMENT ON COLUMN table_name.col_name IS '描述';
```

### Step 2: Run the Generator JAR

```bash
java -jar "domain-generator-v2-3.0.2.jar" \
    -d "path/to/domain-config.xml" \
    -s "path/to/domain-sample-mysql.sql" \
    -o "path/to/output-dir" \
    -p "com.example.project" \
    --dialect mysql
```

> **Note:** The JAR is bundled alongside this skill document. Use the absolute path to the JAR based on your project workspace, e.g. `{workspace}/skills/ddd-code-generator/domain-generator-v2-3.0.2.jar`.

**CLI Options:**
| Option | Description | Default |
|--------|-------------|---------|
| `-d` / `--domain-xml` | Path to domain configuration XML | Required |
| `-s` / `--sql-file` | Path to SQL DDL file | Required |
| `-o` / `--output-dir` | Output directory for generated files | Required |
| `-p` / `--base-package` | Java base package name | Required |
| `--dialect` | SQL dialect: `mysql` or `postgresql` | `mysql` |
| `--no-overwrite` | Do not overwrite existing files | (off by default) |

### Step 3: Copy Generated Files to Target Project

The generator outputs files organized by package structure:

```
{output-dir}/
└── {basePackagePath}/
    ├── entities/
    │   ├── {Table}DO.java
    │   └── ...
    ├── mappers/
    │   ├── {Table}Mapper.java
    │   └── ...
    ├── domains/
    │   └── {domainFolder}/
    │       ├── domain/
    │       │   ├── {Name}Domain.java
    │       │   └── {Name}FindDomain.java
    │       ├── repository/
    │       │   ├── {Name}Repository.java
    │       │   ├── {RelatedName}Repository.java
    │       │   └── impl/
    │       │       ├── {Name}RepositoryImpl.java
    │       │       └── {RelatedName}RepositoryImpl.java
    │       ├── service/
    │       │   ├── {Name}Service.java
    │       │   └── impl/
    │       │       └── {Name}ServiceImpl.java
    │       ├── lambdaexp/
    │       │   └── {Name}LambdaExp.java
    │       ├── convertor/
    │       │   ├── {Name}Convertor.java
    │       │   └── {Name}ConvertorDecorator.java
    ├── controllers/
    │   └── {Name}Controller.java
```

Copy these files into the target project's `src/main/java` directory, preserving the package structure.

### Step 4: Verify

After copying, check:

- All imports reference correct package paths
- DO field types match SQL column types
- FK lambda references match Domain DTO field names
- Related table columns include both FK and redundancy columns
- `@Schema(description=...)` annotations match table/column comments

## Key Generation Rules Reference

**Dialect differences:**
| Aspect | MySQL | PostgreSQL |
|--------|-------|------------|
| Primary key | `IdType.AUTO`, no `@KeySequence` | `IdType.INPUT`, `@KeySequence("seq_{table}_id")` |
| Column names | Preserved as-is from DDL | Unquoted identifiers folded to lowercase |
| Table comments | `COMMENT = '...'` after CREATE TABLE | `COMMENT ON TABLE ... IS '...'` |
| Column comments | `COMMENT '...'` inline | `COMMENT ON COLUMN ... IS '...'` |

**DO class annotations:**

- `@TableName(value="table_name", autoResultMap=true)`
- `@KeySequence("seq_{table}_id")` (PostgreSQL only)
- `@TableId(value="col", type=IdType.AUTO/INPUT)`
- `@TableField("col_name")` for non-PK columns

**Domain DTO:**

- With related tables → `extends BaseAggregateDomain<{Name}Domain, {Name}Service>`
- Without related tables → `extends BaseDomain`
- Related `many=true` → `List<{Related}Domain>` with List getter/setter
- Related `many=false` → single `{Related}Domain` field
- Includes `LoadFlag` inner class with boolean for each related table

**FindDomain:**

- `key` field uses the main table’s actual key type (e.g. `Long`), NOT `Serializable`
- Only used for primary key lookup via `find(FindDomain)` method
- `findByKey(Serializable key, SFunction)` does NOT use FindDomain — accepts raw key directly

**Service method signatures:**

| Method | Parameter | Purpose |
|--------|-----------|----------|
| `find(FindDomain)` | FindDomain with strongly-typed key | Primary key lookup |
| `findByKey(Serializable, SFunction)` | Raw Serializable key + lambda | Generic field lookup |
| `insert(Domain)` | Domain DTO | Returns `keyType` |
| `delete(keyType)` | Strongly-typed key | Primary key delete |

**SQL-to-Java type mapping:**
| SQL Type | Java Type |
|----------|-----------|
| BIGINT, BIGSERIAL, SERIAL(8) | `Long` |
| INT, INTEGER, SERIAL(4) | `Integer` |
| NUMERIC(n), DECIMAL(n) (n<=9) | `Integer` |
| NUMERIC(n,m), DECIMAL(n,m) | `java.math.BigDecimal` |
| VARCHAR, CHAR, TEXT | `String` |
| BOOLEAN, TINYINT | `Boolean` |
| DATE | `java.time.LocalDate` |
| TIMESTAMP, DATETIME | `java.time.LocalDateTime` |
| TIME | `java.time.LocalTime` |
