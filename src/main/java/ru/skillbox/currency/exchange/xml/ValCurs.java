package ru.skillbox.currency.exchange.xml;

import lombok.Getter;
import lombok.Setter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@XmlRootElement(name = "ValCurs") // Указывает на самый главный тег в XML
@XmlAccessorType(XmlAccessType.FIELD)
public class ValCurs {

    @XmlElement(name = "Valute") // Говорит JAXB, что каждый тег <Valute> нужно положить в список
    private List<Valute> valutes;
}
