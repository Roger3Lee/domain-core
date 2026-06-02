package io.github.roger3lee.domain.v2.meta.domain;

import javax.xml.bind.annotation.XmlAttribute;

public class RefTableMetaInfo {
    private String name;
    private String table;
    private Boolean many = false;
    private String fk;
    private String redundancy;

    public String getName() { return name; }
    public String getTable() { return table; }
    public Boolean getMany() { return many; }
    public String getFk() { return fk; }
    public String getRedundancy() { return redundancy; }

    @XmlAttribute(name = "name")
    public void setName(String name) { this.name = name; }
    @XmlAttribute(name = "table")
    public void setTable(String table) { this.table = table; }
    @XmlAttribute(name = "many")
    public void setMany(Boolean many) { this.many = many; }
    @XmlAttribute(name = "fk")
    public void setFk(String fk) { this.fk = fk; }
    @XmlAttribute(name = "redundancy")
    public void setRedundancy(String redundancy) { this.redundancy = redundancy; }
}
