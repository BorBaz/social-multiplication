package microservices.book.multiplication.service;


import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

// Import estático, muy interesante para constantes por ejemplo

public class MultiplicationServiceTest {

    private MultiplicationServiceImpl multiplicationServiceImpl;

    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Before
    public void setUp() {
        // Con esta llamada a iniMocks le indicamos a Mokito que procese las anotaciones
        MockitoAnnotations.initMocks(this);
        multiplicationServiceImpl = new MultiplicationServiceImpl(randomGeneratorService);
    }

    // Este test actualmente no pasa luego de implementar lombok
    @Test
    public void createRandomMultiplicationTest() {
        given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);

        Multiplication multiplication = multiplicationServiceImpl.createRandomMultiplication();

        assertThat(multiplication.getFactorA()).isEqualTo(50);
        assertThat(multiplication.getFactorB()).isEqualTo(30);
        assertThat(multiplication.getResultado()).isEqualTo(1500);
    }

    //Este test sí pasa
    @Test
    public void checkCorrectAttemptTest() {
        //given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("Borja");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3000);

        //when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);
        //
        assertThat(attemptResult).isTrue();
    }

    @Test
    public void checkWrongAttemptTest() {
        //given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("Borja");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3010);

        //when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);
        //
        assertThat(attemptResult).isFalse();
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

