package microservices.book.multiplication.service;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class RandomGeneratorServiceTest {

    private RandomGeneratorService randomGeneratorService;

    @Before
    public void setUp() {
        randomGeneratorService = new RandomGeneratorServiceImpl();
    }

    @Test
    public void generateRandoFactorIsBetweenExpectedLimits() {

        List<Integer> randomFactors = IntStream.range(0, 1000)
                .map(i -> randomGeneratorService.generateRandomFactor())
                .boxed()
                .collect(Collectors.toList());

        assertThat(randomFactors).containsAnyElementsOf(IntStream.range(11,100).boxed().collect(Collectors.toList()));
    }


}


// Con SpringBootTest

/**
 * package microservices.book.multiplication.service;
 * <p>
 * import org.junit.Test;
 * import org.junit.runner.RunWith;
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.boot.test.context.SpringBootTest;
 * import org.springframework.test.context.junit4.SpringRunner;
 * <p>
 * import java.util.List;
 * import java.util.stream.Collectors;
 * import java.util.stream.IntStream;
 * <p>
 * import static org.assertj.core.api.Assertions.assertThat;
 *
 * @RunWith(SpringRunner.class)
 * @SpringBootTest public class RandomGeneratorServiceTest {
 * @Autowired private RandomGeneratorService randomGeneratorService;
 * @Test public void generateRandomFactorIsBetweenExpectedLimits() {
 * // Cuando se genera un numero random dentro de nuestros límites
 * List<Integer> randomFactor = IntStream.range(0, 1000)
 * .map(i -> randomGeneratorService.generateRandomFactor())
 * .boxed()
 * .collect(Collectors.toList());
 * <p>
 * // Todos deberían estar entre 11 y 100
 * assertThat(randomFactor).containsExactlyElementsOf(IntStream.range(11, 100).boxed().collect(Collectors.toList()));
 * }
 * }
 */
