package com.artframework.domain.meta.table;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * 表結構
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/8/30
 **/
@Getter
public class TableMetaInfo {
    private String name;
    private Boolean basic = false;
    private String inherit;
    private List<ColumnMetaInfo> column;
    private Boolean keyGenerator = true;

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.name = name;
    }
    @XmlAttribute(name = "basic")
    public void setBasic(Boolean basic) {
        this.basic = basic;
    }
    @XmlAttribute(name = "inherit")
    public void setInherit(String inherit) {
        this.inherit = inherit;
    }
    @XmlElement(name = "column")
    public void setColumn(List<ColumnMetaInfo> column) {
        this.column = column;
    }

    @XmlAttribute(name = "keyGenerator")
    public void setKeyGenerator(Boolean keyGenerator) {
        this.keyGenerator = keyGenerator;
    }
}
