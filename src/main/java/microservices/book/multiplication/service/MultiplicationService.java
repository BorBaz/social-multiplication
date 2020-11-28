package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;

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
}
