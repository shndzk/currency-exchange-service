package ru.skillbox.currency.exchange.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.currency.exchange.dto.CurrenciesDto;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.service.CurrencyService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/currency")
@Tag(name = "Валютный контроллер", description = "Методы для работы с курсами валют и конвертацией")
public class CurrencyController {
    private final CurrencyService service;

    @GetMapping
    @Operation(summary = "Получение списка всех валют")
    public ResponseEntity<CurrenciesDto> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Получение валюты по ID")
    public ResponseEntity<CurrencyDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping(value = "/convert")
    @Operation(summary = "Конвертация валюты")
    public ResponseEntity<Double> convertValue(@RequestParam("value") Long value, @RequestParam("numCode") Long numCode) {
        return ResponseEntity.ok(service.convertValue(value, numCode));
    }

    @PostMapping("/create")
    @Operation(summary = "Создание новой валюты")
    public ResponseEntity<CurrencyDto> create(@Valid @RequestBody CurrencyDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }
}

