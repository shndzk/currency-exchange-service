package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;
import ru.skillbox.currency.exchange.xml.ValCurs;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CbrService {

    @Value("${cbr.url}")
    private String cbrUrl;

    private final CurrencyRepository repository;
    private final RestTemplate restTemplate;

    @Scheduled(fixedRateString = "${cbr.update-interval}")
    @Transactional
    public void updateCurrencies() {
        try {
            log.info("Starting optimized currency update from CBR API...");

            String xmlResponse = restTemplate.getForObject(cbrUrl, String.class);

            if (xmlResponse == null || xmlResponse.isEmpty()) {
                log.warn("CBR API returned no data.");
                return;
            }

            JAXBContext jaxbContext = JAXBContext.newInstance(ValCurs.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            ValCurs valCurs = (ValCurs) unmarshaller.unmarshal(new StringReader(xmlResponse));

            Map<String, Currency> existingCurrencies = repository.findAll().stream()
                    .filter(c -> c.getIsoCharCode() != null)
                    .collect(Collectors.toMap(Currency::getIsoCharCode, Function.identity()));

            List<Currency> toSave = new ArrayList<>();
            int updatedCount = 0;
            int createdCount = 0;

            for (var xmlValute : valCurs.getValutes()) {
                Double value = Double.valueOf(xmlValute.getValue().replace(",", "."));
                String charCode = xmlValute.getIsoCharCode();

                Currency currency = existingCurrencies.get(charCode);

                if (currency != null) {
                    currency.setValue(value);
                    currency.setNominal(xmlValute.getNominal());
                    updatedCount++;
                } else {
                    currency = new Currency();
                    currency.setName(xmlValute.getName());
                    currency.setNominal(xmlValute.getNominal());
                    currency.setValue(value);
                    currency.setIsoNumCode(xmlValute.getIsoNumCode());
                    currency.setIsoCharCode(charCode);
                    createdCount++;
                }
                toSave.add(currency);
            }

            repository.saveAll(toSave);

            log.info("Update finished: {} currencies updated, {} new currencies created.", updatedCount, createdCount);

        } catch (ResourceAccessException e) {
            log.error("CBR API is unavailable (timeout or network error): {}", e.getMessage());
        } catch (Exception e) {
            log.error("Failed to perform automated currency update: {}", e.getMessage());
        }
    }
}
