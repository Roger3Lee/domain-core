package io.github.roger3lee.domain.v2.meta.table;

import java.util.List;

public class TableMetaInfo {
    private String name;
    private String comment;
    private Boolean basic = false;
    private String inherit;
    private List<ColumnMetaInfo> column;
    private Boolean keyGenerator = true;

    public String getName() { return name; }
    public String getComment() { return comment; }
    public Boolean getBasic() { return basic; }
    public String getInherit() { return inherit; }
    public List<ColumnMetaInfo> getColumn() { return column; }
    public Boolean getKeyGenerator() { return keyGenerator; }

    public void setName(String name) { this.name = name; }
    public void setComment(String comment) { this.comment = comment; }
    public void setBasic(Boolean basic) { this.basic = basic; }
    public void setInherit(String inherit) { this.inherit = inherit; }
    public void setColumn(List<ColumnMetaInfo> column) { this.column = column; }
    public void setKeyGenerator(Boolean keyGenerator) { this.keyGenerator = keyGenerator; }
}
