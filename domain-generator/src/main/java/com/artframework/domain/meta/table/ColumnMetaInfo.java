package com.artframework.domain.meta.table;

import cn.hutool.Hutool;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/8/30
 **/
@Getter
public class ColumnMetaInfo {
    private String name;
    private String type;
    private String comment;
    private Boolean key = false;

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "type")
    public void setType(String type) {
        this.type = type;
    }

    @XmlAttribute(name = "comment")
    public void setComment(String comment) {
        this.comment = comment;
    }

    @XmlAttribute(name = "key")
    public void setKey(Boolean key) {
        this.key = key;
    }
}
