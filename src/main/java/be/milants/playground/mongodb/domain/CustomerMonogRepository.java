package be.milants.playground.mongodb.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CustomerMonogRepository extends MongoRepository<Customer, String> {

    public Customer findByFirstName(String firstName);
    public List<Customer> findByLastName(String lastName);
}
