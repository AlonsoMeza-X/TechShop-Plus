package cl.playground.techshop_plus.repository;

import cl.playground.techshop_plus.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findById(Long id);

    Optional<Customer> findByName(String name);

}

