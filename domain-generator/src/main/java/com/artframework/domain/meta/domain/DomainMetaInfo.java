package com.artframework.domain.meta.domain;


import lombok.Getter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/8/30
 **/
@Getter
public class DomainMetaInfo {
    /**
     * 文件夾
     */
    private String folder;
    /**
     * 域名稱
     */
    private String name;
    /**
     * 域描述
     */
    private String description;

    /**
     * 實體
     */
    private String mainTable;

    private String implement;

    /**
     * 引用屬性
     */
    private List<RelatedTableMetaInfo> relatedList = new ArrayList<>();

    /**
     * 聚合實體
     */
    private RelatedTableMetaInfo aggregate;

    @XmlAttribute(name = "folder")
    public void setFolder(String folder) {
        this.folder = folder;
    }
    @XmlAttribute(name = "implement")
    public void setImplement(String implement) {
        this.implement = implement;
    }
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

    @XmlElement(name = "aggregate")
    public void setAggregate(RelatedTableMetaInfo aggregate) {
        this.aggregate = aggregate;
    }
}
