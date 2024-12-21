package cl.playground.techshop_plus.repository;

import cl.playground.techshop_plus.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // El CustomerRepository permite la interaccion con la base datos en relación
    // con la tabla de Customer para solicitar o ingresar algun dato.

    //    Optional<Customer> findById(Long id);
    //El Optional<Customer> findById(Long id); permite obtener el ID del customer o cliente de la base de datos.
    /*El uso de Optional en este caso tiene varias ventajas:
            * - Indica explícitamente que el resultado puede ser nulo, promoviendo un manejo más seguro y evitando
 *   posibles NullPointerExceptions.
            * - Proporciona métodos útiles para manejar el valor (por ejemplo, `isPresent()`, `ifPresent()`, `orElse()`)
            *   de forma fluida y concisa.*/

    //    Optional<Customer> findByName(String name);
    /*
    * JpaRepository<T, ID> necesita 2 parametros en donde:
    * T: Es la entidad a la cual esta apuntando.
    * ID: es el tipo de dato 'No Primitivo' del atributo ID de esa clase.
    */


    /*
     <S extends T> S saveAndFlush(S entity);

    <S extends T> java.util.List<S> saveAllAndFlush(java.lang.Iterable<S> entities);

    @java.lang.Deprecated
    default void deleteInBatch(java.lang.Iterable<T> entities) { }

    void deleteAllInBatch(java.lang.Iterable<T> entities);

    void deleteAllByIdInBatch(java.lang.Iterable<ID> ids);

    void deleteAllInBatch();

    T getOne(ID id);

    T getById(ID id);

    T getReferenceById(ID id);

    <S extends T> java.util.List<S> findAll(org.springframework.data.domain.Example<S> example);

    <S extends T> java.util.List<S> findAll(org.springframework.data.domain.Example<S> example, org.springframework.data.domain.Sort sort);
    *
    */
}

