package wtc_crm.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import wtc_crm.model.Customer;

import java.util.List;

public interface CustomerRepository extends MongoRepository<Customer, String> {

    List<Customer> findByTagsContaining(String tag);
}