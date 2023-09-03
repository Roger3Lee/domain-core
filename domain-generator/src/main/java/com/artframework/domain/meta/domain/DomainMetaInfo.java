package com.artframework.domain.meta.domain;


import lombok.Getter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/8/30
 **/
@Getter
public class DomainMetaInfo {
    private String name;
    private String description;
    private String mainTable;
    private List<RelatedTableMetaInfo> relatedList;

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "description")
    public void setDescription(String description) {
        this.description = description;
    }

    @XmlAttribute(name = "main-table")
    public void setMainTable(String mainTable) {
        this.mainTable = mainTable;
    }

    @XmlElement(name = "related")
    public void setRelatedList(List<RelatedTableMetaInfo> relatedList) {
        this.relatedList = relatedList;
    }
}
