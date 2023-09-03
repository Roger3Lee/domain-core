package com.artframework.domain.utils;

import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 *
 *
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/3/10
 **/
public class XmlUtils {

    public static String beanToXml(Object obj, Class<?> load) throws JAXBException {
        if (null == obj) {
            return null;
        }
        JAXBContext context = JAXBContext.newInstance(load);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF8");
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        return writer.toString();
    }

    public static <T> T xmlToBean(String xml, Class<T> load) throws JAXBException {
        if (StringUtils.isEmpty(xml)) {
            return null;
        }

        JAXBContext context = JAXBContext.newInstance(load);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new StringReader(xml));
    }

}
