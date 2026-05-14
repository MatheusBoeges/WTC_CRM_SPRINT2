package wtc_crm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import wtc_crm.model.Customer;
import wtc_crm.repository.CustomerRepository;
import wtc_crm.repository.MessageRepository;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class  CustomerController {

    private final CustomerRepository repository;
    private final MessageRepository messageRepository;

    @PostMapping
    public Customer create(@RequestBody Customer c) {
        return repository.save(c);
    }

    @GetMapping
    public List<Customer> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Customer getById(@PathVariable String id) {
        return repository.findById(id).orElseThrow();
    }

    @GetMapping("/{id}/profile")
    public Object getProfile(@PathVariable String id) {

        var customer = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        var messages = messageRepository.findByReceiverId(id);

        return new Object() {
            public final Object customerData = customer;
            public final Object lastMessages = messages;
        };
    }

    @PutMapping("/{id}")
    public Customer update(@PathVariable String id, @RequestBody Customer c) {
        c.setId(id);
        return repository.save(c);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repository.deleteById(id);
    }
}
