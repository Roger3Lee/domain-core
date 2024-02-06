package com.artframework.domain.meta.domain;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * <related table="profile" many="false" fk="uuid:user_uuid"/>
 */
@Getter
public class RelatedTableMetaInfo {
    private String table;
    private Boolean many=false  ;
    private String fk;
    private String implement;
    private Boolean deletable=true;
    private List<RefTableMetaInfo> refList;

    @XmlAttribute(name = "implement")
    public void setImplement(String implement) {
        this.implement = implement;
    }

    @XmlAttribute(name = "table")
    public void setTable(String table) {
        this.table = table;
    }

    @XmlAttribute(name = "many")
    public void setMany(Boolean many) {
        this.many = many;
    }

    @XmlAttribute(name = "deletable")
    public void setDeletable(Boolean deletable) {
        this.deletable = deletable;
    }

    @XmlAttribute(name = "fk")
    public void setFk(String fk) {
        this.fk = fk;
    }

    @XmlElement(name = "ref")
    public void setRefList(List<RefTableMetaInfo> refList) {
        this.refList = refList;
    }
}
