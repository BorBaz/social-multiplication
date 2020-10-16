package microservices.book.multiplication.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomGeneratorServiceImpl implements RandomGeneratorService {

     static final int MINIMUM_FACTOR = 11;
     static final int MAXIMUM_FACTOR = 100;

    @Override
    public int generateRandomFactor() {
        // Genera un random entre 0 y 88 y luego suma 11 y así consigue el random  entre 11 y 100
        return new Random().nextInt((MAXIMUM_FACTOR - MINIMUM_FACTOR) + 1) + MINIMUM_FACTOR;
    }
}
