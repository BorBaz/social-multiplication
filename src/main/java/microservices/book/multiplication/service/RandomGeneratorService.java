package microservices.book.multiplication.service;

public interface RandomGeneratorService {

    /**
     * @return un numero generado de forma random entre el 11 y el 99
     */
    int generateRandomFactor();
}
