package microservices.book.multiplication.repository;

import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MultiplicationResultAttemptRepository extends CrudRepository<MultiplicationResultAttempt, Long> {

    /**
     * Devuelve los últimos 5
     * <p>
     * Lo curioso de este método, es que no es necesario realizar una implementación, puesto que estamos utilizando una
     * feature de Spring Data JPA que se llama query methods, donde le damos algunos naming patterns para crear
     * querys personalizadas definiendo el método en la interfaz
     *
     * @param userAlias
     * @return
     */
    List<MultiplicationResultAttempt> findTop5ByUserAliasOrderByIdDesc(String userAlias);

}