package com.artframework.domain.meta.domain;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2024/1/24
 **/
@Getter
public class RefTableMetaInfo {
    private String name;
    private String table;
    private Boolean many=false  ;
    private String fk;
    private String redundancy;

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "table")
    public void setTable(String table) {
        this.table = table;
    }

    @XmlAttribute(name = "many")
    public void setMany(Boolean many) {
        this.many = many;
    }

    @XmlAttribute(name = "fk")
    public void setFk(String fk) {
        this.fk = fk;
    }
    /**
     * 冗餘字段
     * @param redundancy
     */
    @XmlAttribute(name = "redundancy")
    public void setRedundancy(String redundancy) {
        this.redundancy = redundancy;
    }
}
