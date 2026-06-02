package io.github.roger3lee.domain.v2.meta.table;

public class ColumnMetaInfo {
    private String name;
    private String type;
    private String comment;
    private Boolean key = false;
    private Boolean inherit = false;

    public String getName() { return name; }
    public String getType() { return type; }
    public Boolean getKey() { return key; }
    public Boolean getInherit() { return inherit; }

    public String getComment() {
        if (comment != null) {
            return comment.replace("\"", "");
        }
        return comment;
    }

    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setComment(String comment) { this.comment = comment; }
    public void setKey(Boolean key) { this.key = key; }
    public void setInherit(Boolean inherit) { this.inherit = inherit; }
}
