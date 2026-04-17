package ru.skillbox.currency.exchange.dto;

import lombok.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {
    private Long id;

    private String name;

    private Long nominal;

    private Double value;

    private Long isoNumCode;

    @Size(min = 3, max = 3, message = "ISO Char Code must be exactly 3 characters (e.g. USD)")
    private String isoCharCode;
}