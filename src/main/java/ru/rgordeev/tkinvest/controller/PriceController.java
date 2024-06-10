package ru.rgordeev.tkinvest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rgordeev.tkinvest.Price;
import ru.rgordeev.tkinvest.service.PriceService;

import java.util.List;

@RestController
@RequestMapping("/price")
public class PriceController {
    private final PriceService priceService;

    @Autowired
    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping("/all")
    public List<Price> getAllPrices() {
        return priceService.getAllPrices();
    }

    @GetMapping("/ids")
    public List<Long> getAllIds() {
        return priceService.getAllIds();
    }

    @GetMapping("/closens")
    public List<Integer> getAllClosens() {
        return priceService.getAllClosen();
    }

    @GetMapping("/idsAndClosens")
    public List<Object> getAllIdsAndClosens() {
        return priceService.getAllIdsAndClosen();
    }
}
