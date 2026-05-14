package wtc_crm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import wtc_crm.model.Segment;
import wtc_crm.repository.SegmentRepository;

import java.util.List;

@RestController
@RequestMapping("/segments")
@RequiredArgsConstructor
public class SegmentController {

    private final SegmentRepository repository;

    @PostMapping
    public Segment create(@RequestBody Segment segment) {
        return repository.save(segment);
    }

    @GetMapping
    public List<Segment> getAll() {
        return repository.findAll();
    }

    @PutMapping("/{id}")
    public Segment update(@PathVariable String id, @RequestBody Segment segment) {
        segment.setId(id);
        return repository.save(segment);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repository.deleteById(id);
    }
}
