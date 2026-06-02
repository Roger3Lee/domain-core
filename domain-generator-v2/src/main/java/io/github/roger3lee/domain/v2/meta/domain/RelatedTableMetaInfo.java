package io.github.roger3lee.domain.v2.meta.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class RelatedTableMetaInfo {
    private String name;
    private String table;
    private Boolean many = false;
    private String fk;
    private String redundancy;
    private String implement;
    private Boolean deletable = true;
    private List<RefTableMetaInfo> refList;

    public String getName() { return name; }
    public String getTable() { return table; }
    public Boolean getMany() { return many; }
    public String getFk() { return fk; }
    public String getRedundancy() { return redundancy; }
    public String getImplement() { return implement; }
    public Boolean getDeletable() { return deletable; }
    public List<RefTableMetaInfo> getRefList() { return refList; }

    @XmlAttribute(name = "implement")
    public void setImplement(String implement) { this.implement = implement; }
    @XmlAttribute(name = "name")
    public void setName(String name) { this.name = name; }
    @XmlAttribute(name = "table")
    public void setTable(String table) { this.table = table; }
    @XmlAttribute(name = "many")
    public void setMany(Boolean many) { this.many = many; }
    @XmlAttribute(name = "deletable")
    public void setDeletable(Boolean deletable) { this.deletable = deletable; }
    @XmlAttribute(name = "fk")
    public void setFk(String fk) { this.fk = fk; }
    @XmlAttribute(name = "redundancy")
    public void setRedundancy(String redundancy) { this.redundancy = redundancy; }
    @XmlElement(name = "ref")
    public void setRefList(List<RefTableMetaInfo> refList) { this.refList = refList; }
}
