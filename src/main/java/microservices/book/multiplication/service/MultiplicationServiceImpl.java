package microservices.book.multiplication.service;


import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.event.EventDispatcher;
import microservices.book.multiplication.event.MultiplicationSolvedEvent;
import microservices.book.multiplication.repository.MultiplicationRepository;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {

    private RandomGeneratorService randomGeneratorService;
    private MultiplicationResultAttemptRepository attemptRepository;
    private UserRepository userRepository;
    private MultiplicationRepository multiplicationRepository;
    private EventDispatcher eventDispatcher;

    @Autowired
    public MultiplicationServiceImpl(RandomGeneratorService randomGeneratorService, MultiplicationResultAttemptRepository attemptRepository, final UserRepository userRepository, final MultiplicationRepository multiplicationRepository, final EventDispatcher eventDispatcher) {
        this.randomGeneratorService = randomGeneratorService;
        this.userRepository = userRepository;
        this.attemptRepository = attemptRepository;
        this.multiplicationRepository = multiplicationRepository;
        this.eventDispatcher = eventDispatcher;
    }


    @Override
    public Multiplication createRandomMultiplication() {
        int factorA = randomGeneratorService.generateRandomFactor();
        int factorB = randomGeneratorService.generateRandomFactor();
        return new Multiplication(factorA, factorB);
    }

    /**
     * Comprobamos un intento
     *
     * @param attempt
     * @return
     */
    @Transactional
    @Override
    public boolean checkAttempt(final MultiplicationResultAttempt attempt) {

        // Comprobamos que no existe un user ya para ese alias y una multiplication con estos factores
        Optional<User> user = userRepository.findByAlias(attempt.getUser().getAlias());
        Optional<Multiplication> multiplication = multiplicationRepository.findByFactorAAndFactorB(attempt.getMultiplication().getFactorA(), attempt.getMultiplication().getFactorB());

        // Evitamos intentos 'Hackeados', en caso de que no se cumpla esto, se lanza una RuntimeException
        Assert.isTrue(!attempt.isCorrect(), "No se puede enviar un intento marcado ya como correcto!");

        boolean isCorrect = attempt.getResultAttempt() == attempt.getMultiplication().getFactorA() * attempt.getMultiplication().getFactorB();

        // Creamos una copia para persistir el intento, donde irá a true el atributo correct (Inmutabilidad)
        MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(user.orElse(attempt.getUser()), multiplication.orElse(attempt.getMultiplication()), attempt.getResultAttempt(), isCorrect);

        // Persistimos el intento, algo importante a tener en cuenta es que como en la entidad hemos configurado @ManyToOne(cascade = CascadeType.PERSIST), cuando se persista en BBDD una de estas
        // entidades, también lo harán sus dependientes
        attemptRepository.save(checkedAttempt);

        // Lanzamos el evento al Message Broker
        eventDispatcher.send(new MultiplicationSolvedEvent(checkedAttempt.getId(), checkedAttempt.getUser().getId(), checkedAttempt.isCorrect()));

        return isCorrect;
    }

    /**
     * @param userAlias
     * @return
     */
    public List<MultiplicationResultAttempt> getStatsForUser(String userAlias) {
        return attemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
    }
}