package ru.rgordeev.tkinvest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rgordeev.tkinvest.Price;
import ru.rgordeev.tkinvest.PriceRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Service
public class PriceService {

    private final PriceRepository priceRepository;

    @Autowired
    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public List<Price> getAllPrices() {
        List<Price> prices = new ArrayList<>();
        priceRepository.findAll().forEach(prices::add);
        return prices;
    }

    public List<Long> getAllIds() {
        List<Long> ids = new ArrayList<>();
        for (Price price : priceRepository.findAll()) {
            ids.add(price.getId());
        }
        return ids;
    }

    public List<Integer> getAllClosen() {
        List<Integer> closens = new ArrayList<>();
        for (Price price : priceRepository.findAll()) {
            closens.add(price.getClosen());
        }
        return closens;
    }

    public List<Long> getAllCloseu() {
        List<Long> closeu = new ArrayList<>();
        for (Price price : priceRepository.findAll()) {
            closeu.add(price.getCloseu());
        }
        return closeu;
    }

    public List<Object> getAllIdsAndClosen() {
        List<Object> idsAndClosens = new LinkedList<>();
        double d = 1_000_000_000.0;
        for (Price price : priceRepository.findAll()) {
            if(idsAndClosens.size()<105){
                Object a = (double)price.getId();
                Object b = (double)price.getCloseu()+(((double)price.getClosen())/d);
                //Object[] idAndClosen =  {(double)price.getId()};
                //Object[] is = {(double)price.getCloseu()+(((double)price.getClosen())/d)};
                idsAndClosens.add(a);
                idsAndClosens.add(b);
            }else{
                break;
            }

        }
        return idsAndClosens;
    }
}


