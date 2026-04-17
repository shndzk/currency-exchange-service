package ru.skillbox.currency.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrenciesDto {
    private List<CurrencyShortDto> currencies;

    @Getter @Setter
    @AllArgsConstructor @NoArgsConstructor
    public static class CurrencyShortDto {
        private String name;
        private Double value;
    }
}
