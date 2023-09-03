package com.artframework.domain.meta.domain;

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
@XmlRootElement(name = "domains")
public class DomainCollection {
    private List<DomainMetaInfo> domain;

    @XmlElement(name = "domain")
    public void setDomain(List<DomainMetaInfo> domain) {
        this.domain = domain;
    }
}
