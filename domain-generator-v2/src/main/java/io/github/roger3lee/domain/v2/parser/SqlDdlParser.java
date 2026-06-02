package io.github.roger3lee.domain.v2.parser;

import io.github.roger3lee.domain.v2.meta.table.ColumnMetaInfo;
import io.github.roger3lee.domain.v2.meta.table.TableMetaInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Parses SQL DDL (CREATE TABLE statements) to extract table metadata.
 * Supports both MySQL and PostgreSQL syntax.
 */
@Slf4j
public class SqlDdlParser {

    // SQL-to-Java type mapping
    private static final Map<String, String> TYPE_MAP = new LinkedHashMap<>();
    static {
        TYPE_MAP.put("BIGINT", "Long");
        TYPE_MAP.put("BIGSERIAL", "Long");
        TYPE_MAP.put("SERIAL(8)", "Long");
        TYPE_MAP.put("SERIAL", "Integer");
        TYPE_MAP.put("INT", "Integer");
        TYPE_MAP.put("INTEGER", "Integer");
        TYPE_MAP.put("SMALLINT", "Integer");
        TYPE_MAP.put("TINYINT", "Boolean");
        TYPE_MAP.put("BOOLEAN", "Boolean");
        TYPE_MAP.put("DATE", "java.time.LocalDate");
        TYPE_MAP.put("TIMESTAMP", "java.time.LocalDateTime");
        TYPE_MAP.put("DATETIME", "java.time.LocalDateTime");
        TYPE_MAP.put("TIME", "java.time.LocalTime");
        TYPE_MAP.put("TEXT", "String");
        TYPE_MAP.put("CLOB", "String");
    }

    /**
     * Parse SQL DDL script into a list of TableMetaInfo.
     * Handles both MySQL and PostgreSQL CREATE TABLE syntax.
     * Defaults to MySQL dialect (no identifier case normalization).
     */
    public static List<TableMetaInfo> parse(String sql) {
        return parse(sql, "mysql");
    }

    /**
     * Parse SQL DDL script into a list of TableMetaInfo.
     * Handles both MySQL and PostgreSQL CREATE TABLE syntax.
     * @param sql SQL DDL script
     * @param dialect SQL dialect: "mysql" or "postgresql"
     */
    public static List<TableMetaInfo> parse(String sql, String dialect) {
        List<TableMetaInfo> tables = new ArrayList<>();
        boolean normalizeToLower = "postgresql".equalsIgnoreCase(dialect);

        // Extract table comments (PostgreSQL COMMENT ON TABLE)
        Map<String, String> tableComments = parsePostgresTableComments(sql);
        // Extract column comments (PostgreSQL COMMENT ON COLUMN)
        Map<String, String> columnComments = parsePostgresColumnComments(sql);

        // Find all CREATE TABLE blocks - use balanced parenthesis matching for body
        Pattern createStartPattern = Pattern.compile(
                "CREATE\\s+TABLE\\s+(?:IF\\s+NOT\\s+EXISTS\\s+)?[`\"']?(\\w+)[`\"']?\\s*\\(",
                Pattern.CASE_INSENSITIVE);
        Matcher startMatcher = createStartPattern.matcher(sql);

        while (startMatcher.find()) {
            String tableName = startMatcher.group(1);
            int bodyStart = startMatcher.end();

            // Find matching closing parenthesis using balanced matching
            int depth = 1;
            int pos = bodyStart;
            while (pos < sql.length() && depth > 0) {
                char c = sql.charAt(pos);
                if (c == '(') depth++;
                else if (c == ')') depth--;
                pos++;
            }
            String body = sql.substring(bodyStart, pos - 1);

            // Extract MySQL table comment after closing paren
            String restAfterBody = sql.substring(pos).trim();
            String mysqlTableComment = null;
            Pattern commentPattern = Pattern.compile("COMMENT\\s*(?:=\\s*)?'([^']*)'", Pattern.CASE_INSENSITIVE);
            Matcher commentMatcher = commentPattern.matcher(restAfterBody);
            if (commentMatcher.find()) {
                mysqlTableComment = commentMatcher.group(1);
            }

            TableMetaInfo table = new TableMetaInfo();
            table.setName(tableName);

            // Table comment: prefer PostgreSQL COMMENT ON, then MySQL inline
            String tableCommentKey = normalizeToLower ? tableName.toLowerCase() : tableName;
            if (tableComments.containsKey(tableCommentKey)) {
                table.setComment(tableComments.get(tableCommentKey));
            } else if (mysqlTableComment != null) {
                table.setComment(mysqlTableComment);
            } else {
                // Fallback: try case-insensitive match for PostgreSQL comments
                String lowerTableKey = tableName.toLowerCase();
                for (Map.Entry<String, String> entry : tableComments.entrySet()) {
                    if (entry.getKey().toLowerCase().equals(lowerTableKey)) {
                        table.setComment(entry.getValue());
                        break;
                    }
                }
            }

            // Determine key generator based on SQL dialect
            if (sql.toUpperCase().contains("BIGSERIAL") || sql.toUpperCase().contains("NEXTVAL")) {
                table.setKeyGenerator(false);
            }

            // Normalize unquoted identifiers to lowercase for PostgreSQL
            if (normalizeToLower) {
                table.setName(table.getName().toLowerCase());
            }

            // Parse columns from body
            List<ColumnMetaInfo> columns = parseColumns(body, columnComments, tableName, normalizeToLower);


            table.setColumn(columns);

            tables.add(table);
        }

        return tables;
    }

    /**
     * Parse column definitions from a CREATE TABLE body.
     */
    private static List<ColumnMetaInfo> parseColumns(String body, Map<String, String> columnComments, String tableName, boolean normalizeToLower) {
        List<ColumnMetaInfo> columns = new ArrayList<>();

        // Split by comma, but be careful about nested parentheses
        List<String> columnDefs = splitColumnDefs(body);

        // Track primary key columns
        Set<String> pkColumns = new LinkedHashSet<>();

        // First pass: find PRIMARY KEY constraints
        for (String def : columnDefs) {
            String trimmed = def.trim();
            // Match: PRIMARY KEY (col1, col2)
            Pattern pkPattern = Pattern.compile("PRIMARY\\s+KEY\\s*\\(([^)]+)\\)", Pattern.CASE_INSENSITIVE);
            Matcher pkMatcher = pkPattern.matcher(trimmed);
            if (pkMatcher.find()) {
                String[] pkCols = pkMatcher.group(1).split(",");
                for (String pkCol : pkCols) {
                    pkColumns.add(pkCol.trim().replace("`", "").replace("\"", "").replace("'", ""));
                }
            }
        }

        // Second pass: parse individual columns
        for (String def : columnDefs) {
            String trimmed = def.trim();
            // Skip constraints
            if (trimmed.toUpperCase().startsWith("PRIMARY") ||
                trimmed.toUpperCase().startsWith("UNIQUE") ||
                trimmed.toUpperCase().startsWith("INDEX") ||
                trimmed.toUpperCase().startsWith("KEY") ||
                trimmed.toUpperCase().startsWith("CONSTRAINT") ||
                trimmed.toUpperCase().startsWith("FOREIGN") ||
                trimmed.toUpperCase().startsWith("CHECK")) {
                continue;
            }

            ColumnMetaInfo column = parseColumnDef(trimmed, columnComments, tableName, pkColumns, normalizeToLower);
            if (column != null) {
                columns.add(column);
            }
        }

        return columns;
    }

    /**
     * Parse a single column definition line.
     */
    private static ColumnMetaInfo parseColumnDef(String def, Map<String, String> columnComments, String tableName, Set<String> pkColumns, boolean normalizeToLower) {
        // Match: `col_name` or "col_name" or col_name, followed by TYPE [(size)], then optional rest
        Pattern colPattern = Pattern.compile(
                "[`\"']?(\\w+)[`\"']?\\s+(\\w+(?:\\s*\\([^)]*\\))?)\\s*(.*)",
                Pattern.CASE_INSENSITIVE);
        Matcher colMatcher = colPattern.matcher(def.trim());
        if (!colMatcher.find()) {
            return null;
        }

        String colName = colMatcher.group(1);
        String sqlType = colMatcher.group(2).toUpperCase();
        String rest = colMatcher.group(3) != null ? colMatcher.group(3) : "";

        // PostgreSQL: unquoted identifiers are folded to lowercase by the database
        if (normalizeToLower) {
            colName = colName.toLowerCase();
        }

        ColumnMetaInfo column = new ColumnMetaInfo();
        column.setName(colName);
        column.setType(mapSqlTypeToJava(sqlType));

        // Check if this is a primary key
        boolean isPk = pkColumns.contains(colName) || rest.toUpperCase().contains("PRIMARY KEY");
        column.setKey(isPk);

        // Extract MySQL COMMENT '...'
        Pattern commentPattern = Pattern.compile("COMMENT\\s*'([^']*)'", Pattern.CASE_INSENSITIVE);
        Matcher commentMatcher = commentPattern.matcher(rest);
        if (commentMatcher.find()) {
            column.setComment(commentMatcher.group(1));
        } else {
            // Use PostgreSQL COMMENT ON COLUMN
            String key = (normalizeToLower ? tableName.toLowerCase() : tableName) + "." + colName;
            // Try case-insensitive lookup for PostgreSQL comments
            if (columnComments.containsKey(key)) {
                column.setComment(columnComments.get(key));
            } else {
                // Fallback: try lowercase key match
                String lowerKey = key.toLowerCase();
                for (Map.Entry<String, String> entry : columnComments.entrySet()) {
                    if (entry.getKey().toLowerCase().equals(lowerKey)) {
                        column.setComment(entry.getValue());
                        break;
                    }
                }
            }
        }

        return column;
    }

    /**
     * Map SQL type to Java type.
     */
    private static String mapSqlTypeToJava(String sqlType) {
        // Normalize: remove MySQL backtick quoting, trim
        String normalized = sqlType.replace("`", "").trim();
        
        // Remove size specification: VARCHAR(255) -> VARCHAR, DECIMAL(10,2) -> DECIMAL, SERIAL(8) -> SERIAL(8) (keep for specific mapping)
        String baseType = normalized.replaceAll("\\(.*\\)", "").trim();
        String fullType = normalized.toUpperCase();

        // First try exact match with full type (including size) for SERIAL(8)
        if (TYPE_MAP.containsKey(fullType)) {
            return TYPE_MAP.get(fullType);
        }

        // Direct mapping by base type
        if (TYPE_MAP.containsKey(baseType.toUpperCase())) {
            return TYPE_MAP.get(baseType.toUpperCase());
        }

        // DECIMAL/NUMERIC with precision and scale
        if (baseType.equalsIgnoreCase("DECIMAL") || baseType.equalsIgnoreCase("NUMERIC")) {
            Pattern sizePattern = Pattern.compile("\\((\\d+)(?:,(\\d+))?\\)");
            Matcher m = sizePattern.matcher(normalized);
            if (m.find()) {
                int precision = Integer.parseInt(m.group(1));
                String scale = m.group(2);
                if (scale != null && Integer.parseInt(scale) > 0) {
                    return "java.math.BigDecimal";
                }
                if (precision <= 9) {
                    return "Integer";
                }
                return "Long";
            }
            return "java.math.BigDecimal";
        }

        // VARCHAR, CHAR, etc.
        if (baseType.toUpperCase().contains("CHAR") || baseType.equalsIgnoreCase("ENUM")) {
            return "String";
        }

        // Default
        return "String";
    }

    /**
     * Split column definitions by comma, respecting parentheses.
     */
    private static List<String> splitColumnDefs(String body) {
        List<String> parts = new ArrayList<>();
        int depth = 0;
        StringBuilder current = new StringBuilder();

        for (char c : body.toCharArray()) {
            if (c == '(') depth++;
            else if (c == ')') depth--;

            if (c == ',' && depth == 0) {
                parts.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        if (current.length() > 0) {
            parts.add(current.toString());
        }
        return parts;
    }

    /**
     * Parse PostgreSQL COMMENT ON TABLE statements.
     */
    private static Map<String, String> parsePostgresTableComments(String sql) {
        Map<String, String> comments = new HashMap<>();
        Pattern pattern = Pattern.compile("COMMENT\\s+ON\\s+TABLE\\s+(\\w+)\\s+IS\\s+'([^']*)'",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            comments.put(matcher.group(1), matcher.group(2));
        }
        return comments;
    }

    /**
     * Parse PostgreSQL COMMENT ON COLUMN statements.
     */
    private static Map<String, String> parsePostgresColumnComments(String sql) {
        Map<String, String> comments = new HashMap<>();
        Pattern pattern = Pattern.compile("COMMENT\\s+ON\\s+COLUMN\\s+(\\w+\\.\\w+)\\s+IS\\s+'([^']*)'",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            comments.put(matcher.group(1), matcher.group(2));
        }
        return comments;
    }
}
