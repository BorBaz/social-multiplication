package microservices.book.multiplication.repository;

import microservices.book.multiplication.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Haciendo uso de queryString, seleccionamos un user por su alias, además que hacemos uso de la interfaz Optional de Java
     * <p>
     * Lo que nos permite hacer Optional es, en la implementación, elegir el valor que tendrá User en caso de que se devuelva un null,
     * para así poder evitar NullPointers
     *
     * @param alias
     * @return
     */
    Optional<User> findByAlias(final String alias);
}
