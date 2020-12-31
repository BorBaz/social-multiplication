package microservices.book.multiplication.service;


import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.event.EventDispatcher;
import microservices.book.multiplication.event.MultiplicationSolvedEvent;
import microservices.book.multiplication.repository.MultiplicationRepository;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

// Import estático, muy interesante para constantes por ejemplo

public class MultiplicationServiceTest {

    private MultiplicationServiceImpl multiplicationServiceImpl;

    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Mock
    private MultiplicationResultAttemptRepository attemptRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MultiplicationRepository multiplicationRepository;

    @Mock
    private EventDispatcher eventDispatcher;

    @Before
    public void setUp() {
        // Con esta llamada a iniMocks le indicamos a Mokito que procese las anotaciones
        MockitoAnnotations.initMocks(this);
        multiplicationServiceImpl = new MultiplicationServiceImpl(randomGeneratorService, attemptRepository, userRepository, multiplicationRepository, eventDispatcher);
    }

    // Este test actualmente no pasa luego de implementar lombok
    @Test
    public void createRandomMultiplicationTest() {
        given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);

        Multiplication multiplication = multiplicationServiceImpl.createRandomMultiplication();

        assertThat(multiplication.getFactorA()).isEqualTo(50);
        assertThat(multiplication.getFactorB()).isEqualTo(30);
        //assertThat(multiplication.getResultado()).isEqualTo(1500);
    }

    @Test
    public void checkCorrectAttemptTest() {
        //given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("Borja");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3000, false);

        // En nuestra implementación actual, al comprobar un intento creamos un nuevo MultiplicaitonResultAttempt con el valor correct a true
        MultiplicationResultAttempt verifiedAttempt = new MultiplicationResultAttempt(user, multiplication, 3000, true);
        given(userRepository.findByAlias("Borja")).willReturn(Optional.empty());

        MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(verifiedAttempt.getId(), verifiedAttempt.getUser().getId(), true);

        //when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);
        //40

        assertThat(attemptResult).isTrue();
        verify(attemptRepository).save(verifiedAttempt);
        verify(eventDispatcher).send(eq(event));
    }

    @Test
    public void checkWrongAttemptTest() {
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("Borja");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3010, false);
        MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(attempt.getId(), attempt.getUser().getId(), false);

        given(userRepository.findByAlias("Borja")).willReturn(Optional.empty());

        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        assertThat(attemptResult).isFalse();
        verify(attemptRepository).save(attempt);
        verify(eventDispatcher).send(eq(event));
    }

    @Test
    public void retrieveStatsTest() {

        //given
        Multiplication multiplication = new Multiplication(20, 20);
        User user = new User("Bor");
        MultiplicationResultAttempt attempt1 = new MultiplicationResultAttempt(user, multiplication, 500, false);
        MultiplicationResultAttempt attempt2 = new MultiplicationResultAttempt(user, multiplication, 505, false);
        List<MultiplicationResultAttempt> latestAttemps = Lists.newArrayList(attempt1, attempt2);
        given(userRepository.findByAlias("Bor")).willReturn(Optional.empty());
        given(attemptRepository.findTop5ByUserAliasOrderByIdDesc("Bor")).willReturn(latestAttemps);

        //when
        List<MultiplicationResultAttempt> statsList = attemptRepository.findTop5ByUserAliasOrderByIdDesc("Bor");

        //then
        assertThat(latestAttemps).isEqualTo(statsList);
    }
}


//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class MultiplicationServiceTest {
//
//    // @MockBean es muy importante ponerlo aquí, puesto que le dice a Spring que injecte un mock en el bean,  para que así no busque por una implementación de la interface
//    @MockBean
//    private RandomGeneratorService randomGeneratorService;
//
//    @Autowired
//    private MultiplicationService multiplicationService;
//
//    @Test
//    public void createRandomMultiplicationTest() {
//        // Como estamos moqueando el servicio, primero devolverá 50 y luego 30
//        given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);
//
//        // cuando
//        Multiplication multiplication = multiplicationService.createRandomMultiplication();
//
//        //afirma
//        assertThat(multiplication.getFactorA()).isEqualTo(50);
//        assertThat(multiplication.getFactorB()).isEqualTo(30);
//        assertThat(multiplication.getResultado()).isEqualTo(1500);
//
//    }
//}

