package com.artframework.domain.meta.table;

import lombok.Getter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/8/30
 **/
@Getter
@XmlRootElement(name="tables")
public class TableCollection {

    /**
     * <pre>
     * table
     * </pre>
     */
    private List<TableMetaInfo>	tables;

    @XmlElement(name ="table")
    public void setTables(List<TableMetaInfo> tables) {
        this.tables = tables;
    }
}
