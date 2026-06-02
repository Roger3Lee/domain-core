package io.github.roger3lee.domain.v2.meta.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "domains")
public class DomainCollection {
    private List<DomainMetaInfo> domain;

    public List<DomainMetaInfo> getDomain() { return domain; }

    @XmlElement(name = "domain")
    public void setDomain(List<DomainMetaInfo> domain) { this.domain = domain; }
}
