package io.github.roger3lee.domain.v2.meta.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class DomainMetaInfo {
    private String folder;
    private String name;
    private String description;
    private String mainTable;
    private String implement;
    private List<RelatedTableMetaInfo> relatedList = new ArrayList<>();
    private RelatedTableMetaInfo aggregate;

    public String getFolder() { return folder; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getMainTable() { return mainTable; }
    public String getImplement() { return implement; }
    public List<RelatedTableMetaInfo> getRelatedList() { return relatedList; }
    public RelatedTableMetaInfo getAggregate() { return aggregate; }

    @XmlAttribute(name = "folder")
    public void setFolder(String folder) { this.folder = folder; }
    @XmlAttribute(name = "implement")
    public void setImplement(String implement) { this.implement = implement; }
    @XmlAttribute(name = "name")
    public void setName(String name) { this.name = name; }
    @XmlAttribute(name = "description")
    public void setDescription(String description) { this.description = description; }
    @XmlAttribute(name = "main-table")
    public void setMainTable(String mainTable) { this.mainTable = mainTable; }
    @XmlElement(name = "related")
    public void setRelatedList(List<RelatedTableMetaInfo> relatedList) { this.relatedList = relatedList; }
    @XmlElement(name = "aggregate")
    public void setAggregate(RelatedTableMetaInfo aggregate) { this.aggregate = aggregate; }
}
