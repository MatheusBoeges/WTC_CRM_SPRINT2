package wtc_crm.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import wtc_crm.model.Segment;

public interface SegmentRepository extends MongoRepository<Segment, String> {
}
