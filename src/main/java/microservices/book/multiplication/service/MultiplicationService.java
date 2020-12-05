package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;

import java.util.List;

public interface MultiplicationService {

    /**
     * Crea un Multiplication con dos numeros generados random
     *
     * @return
     */
    Multiplication createRandomMultiplication();

    /**
     * @param multiplicationResultAttempt
     * @return true si el el intento matches con la multiplicación
     */
    boolean checkAttempt(final MultiplicationResultAttempt multiplicationResultAttempt);

    /**
     * Recupera los últimos top 5 intentos
     *
     * @param userAlias
     * @return
     */
    List<MultiplicationResultAttempt> getStatsForUser(String userAlias);
}
